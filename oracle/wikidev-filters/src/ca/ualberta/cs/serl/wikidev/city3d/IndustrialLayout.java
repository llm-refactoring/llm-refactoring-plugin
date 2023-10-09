package ca.ualberta.cs.serl.wikidev.city3d;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import ca.ualberta.cs.serl.wikidev.DataManager;
import ca.ualberta.cs.serl.wikidev.User;
import ca.ualberta.cs.serl.wikidev.artifacts.ChangeSet;
import ca.ualberta.cs.serl.wikidev.artifacts.ChangeSetDetail;
import ca.ualberta.cs.serl.wikidev.artifacts.IArtifact;
import ca.ualberta.cs.serl.wikidev.artifacts.Message;
import ca.ualberta.cs.serl.wikidev.artifacts.SVNFile;
import ca.ualberta.cs.serl.wikidev.artifacts.Ticket;
import ca.ualberta.cs.serl.wikidev.artifacts.Wiki;
import ca.ualberta.cs.serl.wikidev.city3d.animation.CommunicationBuildingData;
import ca.ualberta.cs.serl.wikidev.city3d.animation.FileBuildingData;
import ca.ualberta.cs.serl.wikidev.city3d.animation.UserData;

import static org.math.array.DoubleArray.deleteRows;
import static org.math.array.DoubleArray.deleteColumns;

public class IndustrialLayout extends Layout {
	
	public static final String CITY_TYPE = "files";

	private HashMap<ChangeSet, ArrayList<SVNFile>> filesPerChangeset;
	private ArrayList<SVNFile> totalFiles;
	private ArrayList<ChangeSet> changesets;
	private int projectid;

	public IndustrialLayout(ArrayList<ChangeSet> changesets, int projectid) throws IOException {
		this.projectid = projectid;
		this.changesets = changesets;
		this.totalFiles = new ArrayList<SVNFile>();
		this.filesPerChangeset = new HashMap<ChangeSet, ArrayList<SVNFile>>();
		for (ChangeSet changeset : changesets) {
			ArrayList<SVNFile> fileList = new ArrayList<SVNFile>();
			for (ChangeSetDetail detail : changeset.getChangesetDetails()) {
				if (detail.getKind().equals("file")) {
					SVNFile file = new SVNFile(detail.getFilename());
					file.increaseChanges();
					if (detail.getChangetype().equals("deleted")) {
						file.setDeleted(true);
					}
					file.addUser(changeset.getOwner());
					file.setType();
					fileList.add(file);
					if (!totalFiles.contains(file)) {
						this.totalFiles.add(file);
					}
				}
			}
			this.filesPerChangeset.put(changeset, fileList);
		}
		double[][] distanceMatrix = getCosineDistanceMatrix(changesets.size());
		/*try {
			printDistanceMatrix(distanceMatrix);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		ArrayList<Integer> indicesToDelete = new ArrayList<Integer>();
		for (int i = 0; i < distanceMatrix.length; i++) {
			int count = 0;
			for (int j = 0; j < distanceMatrix.length; j++) {
				if (distanceMatrix[i][j] == 1) {
					count++;
				}
			}
			if (count == distanceMatrix.length - 1) {
				indicesToDelete.add(i);
			}
		}
		ArrayList<Integer> originalIndices = new ArrayList<Integer>();
		originalIndices.addAll(indicesToDelete);
		for (int i = 0; i < indicesToDelete.size(); i++) {
			distanceMatrix = deleteRows(distanceMatrix, indicesToDelete.get(i));
			distanceMatrix = deleteColumns(distanceMatrix, indicesToDelete
					.get(i));
			indicesToDelete = decreaseIndices(indicesToDelete);
		}
		ArrayList<IArtifact> fileArtifacts = new ArrayList<IArtifact>();
		ArrayList<SVNFile> removedFiles = new ArrayList<SVNFile>();
		for (int i = 0; i < totalFiles.size(); i++) {
			if (!originalIndices.contains(i)) {
				fileArtifacts.add(totalFiles.get(i));
			} else {
				removedFiles.add(totalFiles.get(i));
			}
		}
		totalFiles.removeAll(removedFiles);
		System.out.println(fileArtifacts.size());
		this.getLayout(fileArtifacts, distanceMatrix);
		HashMap<SVNFile, Integer> fileCount = getFileCount(totalFiles);
		//printCityBlocks(cityBlocks, fileCount);

		/*
		 * for(int i=0; i<distanceMatrix.length; i++) { for(int j=0;
		 * j<distanceMatrix.length; j++) {
		 * System.out.print(distanceMatrix[i][j]+"\t"); } System.out.println();
		 * }
		 */
	}

	private HashMap<SVNFile, Integer> getFileCount(ArrayList<SVNFile> files) {
		HashMap<SVNFile, Integer> fileCount = new HashMap<SVNFile, Integer>();
		for (SVNFile file : files) {
			int count = 0;
			for (ChangeSet changeset : changesets) {
				for (ChangeSetDetail detail : changeset.getChangesetDetails()) {
					if (detail.getFilename().equals(file.getName())) {
						count++;
					}
				}
			}
			fileCount.put(file, count);
		}
		return fileCount;
	}
	
	private int getChangesForFileBeforeDate(SVNFile file, Timestamp timestamp) {
		int count=0;
		for(ChangeSet changeset : changesets) {
			for(ChangeSetDetail detail : changeset.getChangesetDetails()) {
				if(detail.getFilename().equals(file.getName()) && detail.getTimestamp().before(timestamp)) {
					count++;
				}
			}
		}
		return count;
	}
	
	public void setLastEditorAndTimeLastModifiedBeforeDate(SVNFile file, Timestamp timestamp) {
		file.setLastModified(file.getCreated());
		file.setLastEditor(file.getOwner());
		for(ChangeSet changeset : changesets) {
			for(ChangeSetDetail detail : changeset.getChangesetDetails()) {
				if(detail.getFilename().equals(file.getName()) && detail.getTimestamp().before(timestamp) && detail.getTimestamp().after(file.getLastModified())) {
					file.setLastModified(detail.getTimestamp());
					file.setLastEditor(detail.getOwner());
				}
			}
		}
	}
	
	public void setTimeCreatedBeforeDate(SVNFile file, Timestamp timestamp) {
		file.setCreated(Timestamp.valueOf("2015-01-01 00:00:00"));
		for(ChangeSet changeset : changesets) {
			for(ChangeSetDetail detail : changeset.getChangesetDetails()) {
				if(detail.getFilename().equals(file.getName()) && detail.getTimestamp().before(timestamp) && detail.getTimestamp().before(file.getCreated())) {
					file.setCreated(detail.getTimestamp());
					file.setOwner(changeset.getOwner());
				}
			}
		}
	}

	private ArrayList<Integer> decreaseIndices(ArrayList<Integer> indices) {
		for (int i = 0; i < indices.size(); i++) {
			indices.set(i, indices.get(i) - 1);
		}
		return indices;
	}

	public double[][] getSupportDistanceMatrix(int totalChangesets) {
		double[][] distanceMatrix = new double[totalFiles.size()][totalFiles
				.size()];
		int i = 0;
		int j = 0;
		for (SVNFile file : totalFiles) {
			for (SVNFile file2 : totalFiles) {
				if (!file.equals(file2)) {
					double count = 0;
					for (ChangeSet changeset : filesPerChangeset.keySet()) {
						if (filesPerChangeset.get(changeset).contains(file)
								&& filesPerChangeset.get(changeset).contains(
										file2)) {
							count++;
						}
					}
					distanceMatrix[i][j] = 1 - count / totalChangesets;
				} else {
					distanceMatrix[i][j] = 0;
				}
				j++;
			}
			i++;
			j = 0;
		}
		return distanceMatrix;
	}

	public double[][] getCosineDistanceMatrix(int totalChangesets) {
		double[][] distanceMatrix = new double[totalFiles.size()][totalFiles
				.size()];
		int i = 0;
		int j = 0;
		for (SVNFile file : totalFiles) {
			for (SVNFile file2 : totalFiles) {
				if (!file.equals(file2)) {
					double count = 0;
					for (ChangeSet changeset : filesPerChangeset.keySet()) {
						if (filesPerChangeset.get(changeset).contains(file)
								&& filesPerChangeset.get(changeset).contains(
										file2)) {
							count++;
						}
					}
					distanceMatrix[i][j] = count;
				} else {
					distanceMatrix[i][j] = 1;
				}
				j++;
			}
			i++;
			j = 0;
		}
		double[][] cosineDistance = new double[totalFiles.size()][totalFiles
				.size()];
		for (int k = 0; k < totalFiles.size(); k++) {
			double[] vector1 = distanceMatrix[k];
			for (int l = 0; l < totalFiles.size(); l++) {
				if (l != k) {
					double[] vector2 = distanceMatrix[l];
					double sum = 0;
					double norm1 = 0;
					double norm2 = 0;
					for (int m = 0; m < totalFiles.size(); m++) {
						sum += vector1[m] * vector2[m];
						norm1 += Math.pow(vector1[m], 2);
						norm2 += Math.pow(vector2[m], 2);
					}
					cosineDistance[k][l] = 1 - sum
							/ (Math.sqrt(norm1) * Math.sqrt(norm2));
				}
			}
		}
		return cosineDistance;
	}

	public static void printDistanceMatrix(double[][] distanceMatrix)
			throws IOException {
		// BufferedWriter out = new BufferedWriter(new
		// FileWriter("C:\\Documents and Settings\\Marios Fokaefs\\Desktop\\wikidev-files\\ucosp\\clusters"+week+".txt"));
		BufferedWriter out = new BufferedWriter(
				new FileWriter(
						"C:\\Documents and Settings\\Marios Fokaefs\\Desktop\\wikidev-files\\c301f09\\distances.txt"));
		for (int i = 0; i < distanceMatrix.length; i++) {
			for (int j = 0; j < distanceMatrix.length; j++) {
				out.write(distanceMatrix[i][j] + "\t");
			}
			out.newLine();
		}
		out.close();
	}
	
	private HashMap<User, Double> getUserContributionBeforeDate(SVNFile file, Timestamp timestamp, ArrayList<User> users) {
		HashMap<User, Double> userChanges = new HashMap<User, Double>();
		for(User user : users) {
			userChanges.put(user, 0.0);
		}
		int count=0;
		for(ChangeSet changeset : changesets) {
			for(ChangeSetDetail detail : changeset.getChangesetDetails()) {
				if(detail.getFilename().equals(file.getName()) && detail.getTimestamp().before(timestamp)) {
					count++;
					for(User user : users) {
						if(detail.getOwner().equals(user)) {
							userChanges.put(user, userChanges.get(user)+1.0);
						}
					}
				}
			}
		}
		for(User user : userChanges.keySet()) {
			if (count !=0) {
				userChanges.put(user, userChanges.get(user) / (double) count);
			}
		}
		return userChanges;
	}
	
	public void prepareToWriteCitySnapshots(Timestamp start, Timestamp end) {
		Timestamp firstWeek = new Timestamp(start.getTime()+DataManager.WEEK_INTERVAL_IN_MILLIS);
		for(long l=firstWeek.getTime(); l<end.getTime()+DataManager.WEEK_INTERVAL_IN_MILLIS; l+=DataManager.WEEK_INTERVAL_IN_MILLIS) {
			if(l>end.getTime()) {
				l = end.getTime();
			}
			Timestamp nextWeek = new Timestamp(l);
			for(CityBlock block : cityBlocks) {
				for(IArtifact artifact : block.getArtifacts().keySet()) {
					String type = "";
					int height = 0;
					HashMap<User, Double> userContribution = null;
					int id=0;
					String url = "";
					User lastEditor = null;
					Timestamp created = null;
					Timestamp lastModified = null; 
					if (artifact instanceof SVNFile) {
						SVNFile file = (SVNFile)artifact;
						type += file.getType();
						height = getChangesForFileBeforeDate(file, nextWeek);
						try {
							url = URLprefix+DataManager.getProjectByID(projectid).getProjectName()+"_File:"+file.getName();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						userContribution = getUserContributionBeforeDate(file, nextWeek, getUsers(projectid));
						this.setTimeCreatedBeforeDate(file, nextWeek);
						this.setLastEditorAndTimeLastModifiedBeforeDate(file, nextWeek);
						
						lastEditor = file.getLastEditor();
						created = file.getCreated();
						lastModified = file.getLastModified();
					}
					ArrayList<UserData> userData = new ArrayList<UserData>();
					for(User user : userContribution.keySet()) {
						userData.add(new UserData(user.getUserName(), user.getColor().getRed(), user.getColor().getGreen(), user.getColor().getBlue(), userContribution.get(user), nextWeek, IndustrialLayout.CITY_TYPE));
					}
					FileBuildingData building = new FileBuildingData(type, nextWeek, height, block.getIndex().x, block.getIndex().y, block.getArtifacts().get(artifact), url, projectid, userData, lastEditor, created, lastModified);
					System.out.println("Writing "+building.getUrl());
					try {
						DataManager.writeFilesBuildingData(building);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void printCityBlocks(ArrayList<CityBlock> blocks, HashMap<SVNFile, Integer> fileCount)
			throws IOException {
		// BufferedWriter out = new BufferedWriter(new
		// FileWriter("C:\\Documents and Settings\\Marios Fokaefs\\Desktop\\wikidev-files\\ucosp\\clusters"+week+".txt"));
		BufferedWriter out = new BufferedWriter(
				new FileWriter(
						"C:\\Documents and Settings\\Marios Fokaefs\\Desktop\\wikidev-files\\c301f09\\industrialBlocks.txt"));
		int index = 1;
		out.write(""+Math.sqrt(blocks.size()));
		out.newLine();
		for (CityBlock block : blocks) {
			for (IArtifact artifact : block.getArtifacts().keySet()) {
				String type = ""+index+"\t";
				int height = 0;
				String line = "";
				if (artifact instanceof SVNFile) {
					SVNFile file = (SVNFile)artifact;
					type += file.getType();
					height = fileCount.get(file);
					line += type + "\t" + block.getIndex().x + "\t"
					+ block.getIndex().y + "\t"
					+ block.getArtifacts().get(artifact)+"\t"+height+"\t";
					for(User user : file.getUsers()) {
						line += "<"+user.getUserName()+", "+user.getColor().getRed()+", "+user.getColor().getGreen()+", "+user.getColor().getBlue()+"> \t";
					}
				}
				out.write(line);
				out.newLine();
				index++;
			}
		}
		out.close();
	}
}
