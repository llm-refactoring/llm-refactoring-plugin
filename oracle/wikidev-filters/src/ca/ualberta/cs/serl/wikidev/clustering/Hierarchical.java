package ca.ualberta.cs.serl.wikidev.clustering;

import static org.math.array.DoubleArray.deleteColumns;
import static org.math.array.DoubleArray.deleteRows;
import static org.math.array.DoubleArray.insertColumns;
import static org.math.array.DoubleArray.insertRows;

import java.util.ArrayList;

import ca.ualberta.cs.serl.wikidev.artifacts.IArtifact;

public class Hierarchical extends Clustering {
	
	/**
	 * @uml.property  name="tHRESHOLD"
	 */
	private double THRESHOLD; 
	
	public Hierarchical(double[][] distanceMatrix, double threshold) {
		this.distanceMatrix = distanceMatrix;
		this.THRESHOLD = threshold;
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Cluster> clustering(ArrayList<IArtifact> artifacts) {
		ArrayList<Cluster> clusters = new ArrayList<Cluster>();
		for(IArtifact artifact : artifacts) {
			Cluster cluster = new Cluster();
			cluster.addArtifact(artifact);
			clusters.add(cluster);
		}
		while(clusters.size()>2) {
			double minVal = 2.0;
			int minRow = 0;
			int minCol = 1;
			for(int i=0; i<distanceMatrix.length;i++) {
				for(int j=0;j<distanceMatrix.length;j++) {
					if (i != j) {
						if (distanceMatrix[i][j] < minVal) {
							minVal = distanceMatrix[i][j];
							minRow = i;
							minCol = j;
						}
					}
				}
			}
			if(minVal >= THRESHOLD) break;
			
			if(minRow < minCol) {
				clusters.get(minRow).addArtifacts(clusters.get(minCol).getArtifacts());
				double[] newDistances = new double[distanceMatrix.length-1];
				for(int i=0;i<distanceMatrix.length;i++) {
						if (i != minCol) {
							if (i != minRow) {
								if (distanceMatrix[minRow][i] < distanceMatrix[minCol][i]) {
									if (i > minCol) {
										newDistances[i - 1] = distanceMatrix[minRow][i];
									} else {
										newDistances[i] = distanceMatrix[minRow][i];
									}
								} else {
									if (i > minCol) {
										newDistances[i - 1] = distanceMatrix[minCol][i];
									} else {
										newDistances[i] = distanceMatrix[minCol][i];
									}
								}
							}
							else {
								newDistances[i] = 0.0;
							}
						}
						
				}
				
				distanceMatrix = deleteRows(distanceMatrix, minRow, minCol);
				distanceMatrix = deleteColumns(distanceMatrix, minCol);
				distanceMatrix = insertRows(distanceMatrix, minRow, newDistances);
				distanceMatrix = deleteColumns(distanceMatrix, minRow);
				distanceMatrix = insertColumns(distanceMatrix, minRow, newDistances);
				clusters.remove(minCol);
			}
			else {
				clusters.get(minCol).addArtifacts(clusters.get(minRow).getArtifacts());
				double[] newDistances = new double[distanceMatrix.length-1];
				for(int i=0;i<distanceMatrix.length;i++) {
					if (i != minRow) {
						if (i != minCol) {
							if (distanceMatrix[minRow][i] < distanceMatrix[minCol][i]) {
								if (i > minRow) {
									newDistances[i - 1] = distanceMatrix[minRow][i];
								} else {
									newDistances[i] = distanceMatrix[minRow][i];
								}
							} else {
								if (i > minRow) {
									newDistances[i - 1] = distanceMatrix[minCol][i];
								} else {
									newDistances[i] = distanceMatrix[minCol][i];
								}
							}
						}
						else {
							newDistances[i] = 0.0;
						}
					}
					
				}
				distanceMatrix = deleteRows(distanceMatrix, minRow, minCol);
				distanceMatrix = deleteColumns(distanceMatrix, minCol);
				distanceMatrix = insertRows(distanceMatrix, minCol, newDistances);
				distanceMatrix = deleteColumns(distanceMatrix, minRow);
				distanceMatrix = insertColumns(distanceMatrix, minCol, newDistances);
				clusters.remove(minRow);
			}
		}
		return clusters;
	}

}
