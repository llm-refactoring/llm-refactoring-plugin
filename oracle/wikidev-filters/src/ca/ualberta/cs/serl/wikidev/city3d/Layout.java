package ca.ualberta.cs.serl.wikidev.city3d;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import ca.ualberta.cs.serl.wikidev.DataManager;
import ca.ualberta.cs.serl.wikidev.RelationshipMiner;
import ca.ualberta.cs.serl.wikidev.User;
import ca.ualberta.cs.serl.wikidev.artifacts.ChangeSet;
import ca.ualberta.cs.serl.wikidev.artifacts.IArtifact;
import ca.ualberta.cs.serl.wikidev.artifacts.Message;
import ca.ualberta.cs.serl.wikidev.artifacts.SVNFile;
import ca.ualberta.cs.serl.wikidev.artifacts.Ticket;
import ca.ualberta.cs.serl.wikidev.artifacts.Wiki;
import ca.ualberta.cs.serl.wikidev.clustering.Cluster;
import ca.ualberta.cs.serl.wikidev.clustering.Clustering;
import ca.ualberta.cs.serl.wikidev.clustering.MultidimensionalScaling;
import ca.ualberta.cs.serl.wikidev.clustering.SammonsProjection;

public class Layout {

	protected ArrayList<CityBlock> cityBlocks;
	public static final String URLprefix = "http://hypatia.cs.ualberta.ca/ucosp/index.php/";

	protected void getLayout(ArrayList<IArtifact> newArtifacts, double[][] distanceMatrix) throws IOException {
		double[][] coords = null;
		if (distanceMatrix.length > 0) {
			MultidimensionalScaling mds = new MultidimensionalScaling(
					distanceMatrix);
			double[][] mdsCoords = mds.cMDS();
			SammonsProjection sammon = new SammonsProjection(mdsCoords, 2, 1000);
			sammon.CreateMapping();
			coords = sammon.getProjection();
		}
		for (int i = 0; i < newArtifacts.size(); i++) {
			newArtifacts.get(i).setCoords(coords[i]);
		}
		System.out.print("x<-c(");
		double maxX = 0;
		double minX = Double.MAX_VALUE;
		double maxY = 0;
		double minY = Double.MAX_VALUE;
		for (int i = 0; i < coords.length; i++) {
			for (int j = 0; j < coords[0].length; j++) {
				if (j == 0) {
					if (coords[i][j] > maxX) {
						maxX = coords[i][j];
					}
					if (coords[i][j] < minX) {
						minX = coords[i][j];
					}
				} else {
					if (coords[i][j] > maxY) {
						maxY = coords[i][j];
					}
					if (coords[i][j] < minY) {
						minY = coords[i][j];
					}
				}
				System.out.print((float) coords[i][j] + ", ");
			}
		}
		System.out.println(")");

		double rangeX = Math.ceil(maxX - minX);
		double rangeY = Math.ceil(maxY - minY);
		double range = 0;
		if (rangeX > rangeY) {
			range = rangeX;
		} else {
			range = rangeY;
		}
		double blocks = Math.ceil(Math
				.sqrt(Math.ceil(newArtifacts.size() / 16))) + 2;

		cityBlockInitialization(newArtifacts, coords, minX, minY, range, blocks);

		redistributeBuildings();

		pushBuildings(minX, minY, range);

		//printCityBlocks(cityBlocks);
		System.out.println("Blocks done");
	}

	private void cityBlockInitialization(ArrayList<IArtifact> newArtifacts,
			double[][] coords, double minX, double minY, double range,
			double blocks) {
		this.cityBlocks = new ArrayList<CityBlock>();
		for (int i = 0; i < blocks; i++) {
			for (int j = 0; j < blocks; j++) {
				double[] center = { minX + i * (range / 16) + (range / 16) / 2,
						minY + j * (range / 16) + (range / 16) / 2 };
				CityBlock cityblock = new CityBlock(new Point(i, j));
				cityblock.setCenter(center);
				cityBlocks.add(cityblock);
			}
		}
		double r = range / blocks;
		for (int i = 0; i < newArtifacts.size(); i++) {
			assignArtifactToCityBlock(newArtifacts.get(i), coords[i][0],
					coords[i][1], minX, minY, blocks, r);
		}
	}

	private void pushBuildings(double minX, double minY, double range) {
		for (CityBlock block : cityBlocks) {
			if (block.getArtifacts().size() > 4
					&& block.getArtifacts().size() <= 16) {
				double blockMinX = minX + block.getIndex().x * (range / 16);
				double blockMaxX = minX + (block.getIndex().x + 1)
						* (range / 16);
				double blockMinY = minY + block.getIndex().y * (range / 16);
				double blockMaxY = minY + (block.getIndex().y + 1)
						* (range / 16);
				double rX = Math.ceil(blockMaxX - blockMinX);
				double rY = Math.ceil(blockMaxY - blockMinY);
				double blockR = 0;
				if (rX > rY) {
					blockR = rX;
				} else {
					blockR = rY;
				}
				double[][] subblockCenters = new double[16][3];
				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 4; j++) {
						double[] subCenter = {
								blockMinX + i * blockR + blockR / 2,
								blockMinY + j * blockR + blockR / 2, 0 };
						subblockCenters[j + i * 4] = subCenter;
					}
				}
				redistributeArtifactsInBlock(block, subblockCenters);
			}
		}
	}

	private void redistributeBuildings() {
		for (CityBlock block : cityBlocks) {
			if (block.getArtifacts().size() > 16) {
				block.setMarked(true);
				HashMap<IArtifact, Integer> finalArtifacts = new HashMap<IArtifact, Integer>();
				for (IArtifact artifact : block.getArtifacts().keySet()) {
					if (artifact.isEnforced()) {
						finalArtifacts.put(artifact, -1);
					}
				}
				while (finalArtifacts.size() < 16) {
					IArtifact artifact = getClosestArtifact(block);
					finalArtifacts.put(artifact, -1);
					block.getArtifacts().remove(artifact);
				}
				Collection<IArtifact> surplus = block.getArtifacts().keySet();
				block.setArtifacts(finalArtifacts);
				for (IArtifact artifact : surplus) {
					CityBlock closestBlock = getClosestBlock(artifact);
					artifact.setEnforced(true);
					closestBlock.addArtifact(artifact, -1);
				}
			}
		}
	}

	private void assignArtifactToCityBlock(IArtifact artifact, double x,
			double y, double minX, double minY, double blocks, double r) {
		int blockX = 0;
		int blockY = 0;
		for (int i = 1; i < blocks; i++) {
			if (x > minX + (i - 1) * r && x <= minX + i * r) {
				blockX = i - 1;
			}
			if (y > minY + (i - 1) * r && y <= minY + i * r) {
				blockY = i - 1;
			}
		}
		CityBlock block = getCityBlock(new Point(blockX, blockY));
		block.addArtifact(artifact, -1);
	}

	private CityBlock getCityBlock(Point index) {
		for (CityBlock block : cityBlocks) {
			if (index.equals(block.getIndex())) {
				return block;
			}
		}
		return null;
	}

	private IArtifact getClosestArtifact(CityBlock block) {
		double minDist = Double.MAX_VALUE;
		IArtifact minArtifact = null;
		for (IArtifact artifact : block.getArtifacts().keySet()) {
			if (getEuclideanDistance(artifact.getCoords(), block.getCenter()) < minDist) {
				minDist = getEuclideanDistance(artifact.getCoords(), block
						.getCenter());
				minArtifact = artifact;
			}
		}
		return minArtifact;
	}

	private CityBlock getClosestBlock(IArtifact artifact) {
		double minDist = Double.MAX_VALUE;
		CityBlock minBlock = null;
		for (CityBlock block : cityBlocks) {
			if (!block.isMarked() && block.getArtifacts().size() < 16) {
				if (getEuclideanDistance(artifact.getCoords(), block
						.getCenter()) < minDist) {
					minDist = getEuclideanDistance(artifact.getCoords(), block
							.getCenter());
					minBlock = block;
				}
			}
		}
		return minBlock;
	}

	private void redistributeArtifactsInBlock(CityBlock block,
			double[][] subblocks) {
		Collection<IArtifact> artifacts = new ArrayList<IArtifact>();
		artifacts.addAll(block.getArtifacts().keySet());
		block.getArtifacts().clear();
		for (IArtifact artifact : artifacts) {
			double minDist = Double.MAX_VALUE;
			int minSubblock = -1;
			for (int i = 0; i < subblocks.length; i++) {
				if (subblocks[i][2] != 1) {
					double[] subcenter = new double[2];
					subcenter[0] = subblocks[i][0];
					subcenter[1] = subblocks[i][1];
					if (getEuclideanDistance(artifact.getCoords(), subcenter) < minDist) {
						minDist = getEuclideanDistance(artifact.getCoords(),
								subcenter);
						minSubblock = i;
					}
				}
			}
			subblocks[minSubblock][2] = 1;
			block.addArtifact(artifact, minSubblock);
		}
	}

	private double getEuclideanDistance(double[] p1, double[] p2) {
		double sum = 0.0;
		for (int i = 0; i < p1.length; i++) {
			sum += Math.pow(p1[i] - p2[i], 2);
		}
		return Math.sqrt(sum);
	}
	
	public ArrayList<User> getUsers(int projectid) {
		return assignUserColors2(projectid);
	}

	private ArrayList<User> assignUserColors(int projectid) {
		ArrayList<User> users = null;
		try {
			users = DataManager.getUsersPerProject(projectid);
			boolean emptyUsers = false;
			int count = 0;
			for(int r=0; r<=255; r+=127) {
				for(int g=0; g<=255; g+=127) {
					for(int b=0; b<=255; b+=127) {
						if (!(new Color(r,g,b).equals(Color.black) || new Color(r,g,b).equals(Color.white))) {
							users.get(count).setColor(new Color(r, g, b));
							count++;
							if (users.size() == count) {
								emptyUsers = true;
								break;
							}
						}
					}
					if(emptyUsers) break;
				}
				if(emptyUsers) break;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return users;
	}
	
	private ArrayList<User> assignUserColors2(int projectid) {
		ArrayList<User> users = null;
		try {
			users = DataManager.getUsersPerProject(projectid);
			ArrayList<Color> colors = new ArrayList<Color>();
			colors.add(new Color(0,0,255));
			colors.add(new Color(0,255,0));
			colors.add(new Color(255,0,0));
			colors.add(new Color(0,255,255));
			colors.add(new Color(255,0,255));
			colors.add(new Color(255,255,0));
			colors.add(new Color(127,0,0));
			colors.add(new Color(255,127,0));
			colors.add(new Color(127,0,127));
			for(int i=0; i<colors.size(); i++) {
				users.get(i).setColor(colors.get(i));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return users;
	}
	
}
