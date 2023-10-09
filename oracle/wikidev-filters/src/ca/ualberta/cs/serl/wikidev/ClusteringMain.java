package ca.ualberta.cs.serl.wikidev;

import java.sql.Timestamp;
import java.util.ArrayList;

import ca.ualberta.cs.serl.wikidev.artifacts.IArtifact;
import ca.ualberta.cs.serl.wikidev.clustering.Cluster;
import ca.ualberta.cs.serl.wikidev.clustering.ClusterPoint;
import ca.ualberta.cs.serl.wikidev.clustering.Clustering;
import ca.ualberta.cs.serl.wikidev.clustering.MultidimensionalScaling;
import ca.ualberta.cs.serl.wikidev.clustering.SammonsProjection;

public class ClusteringMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String[] args2 = {
					"jdbc:mysql://hypatia.cs.ualberta.ca/wikidev_ucosp",
					"fokaefs", "filimon9786#7@gr", "2009-09-01 00:00:00",
					"2010-01-01 00:00:00", "1", "weekly", "-lb", "0.05", "-ub",
					"0.95", "-i", "0.05" };
			InvocationParser ip = new InvocationParser();
			ip.parseArgs(args2);
			String databaseName = ip.getDatabaseName();
			String username = ip.getUsername();
			String password = ip.getPassword();
			DataManager.openTheConnection(username, password, databaseName);
			String fromdate = ip.getFromdate();
			String todate = ip.getTodate();
			//String fromdate = DataManager.getTheEarlierstTimeOfActivity().toString();
			//String todate = DataManager.getTheLatestTimeOfActivity().toString();
			//String fromdate = "2009-09-01 00:00:00";
			//String todate = "2010-01-01 00:00:00";
			int project = ip.getProject();
			String window = ip.getWindow();
			double lowerBound = ip.getLowerBound();
			double upperBound = ip.getUpperBound();
			double interval = ip.getInterval();
			String week = "week";
			
			Timestamp firstWeek = new Timestamp(Timestamp.valueOf(fromdate).getTime()+DataManager.WEEK_INTERVAL_IN_MILLIS);
			Timestamp previousWeek = Timestamp.valueOf(fromdate);
			for(long l=firstWeek.getTime(); l<Timestamp.valueOf(todate).getTime()+DataManager.WEEK_INTERVAL_IN_MILLIS; l+=DataManager.WEEK_INTERVAL_IN_MILLIS) {
				if(l>Timestamp.valueOf(todate).getTime()) {
					l = Timestamp.valueOf(todate).getTime();
				}
				
				Timestamp nextWeek = new Timestamp(l);
				System.out.println("From "+previousWeek.toString()+" to "+nextWeek.toString());
				ArrayList<Cluster> totalClusters = new ArrayList<Cluster>();
				ArrayList<IArtifact> newArtifacts = DataManager.getArtifactsBetweenDates(previousWeek.toString(), nextWeek.toString(), project);
				RelationshipMiner rm = new RelationshipMiner(newArtifacts);
				rm.getRelationships();
				double[][] distanceMatrix = rm.getDistanceMatrix();
				//System.out.println("Clustering start");
				for (double threshold = lowerBound; threshold <= upperBound; threshold+=interval) {
					double[][] coords = new double[distanceMatrix.length][distanceMatrix.length];
					Clustering clustering = Clustering.getInstance(0,
							distanceMatrix, threshold);
					ArrayList<Cluster> clusters = clustering
							.clustering(newArtifacts);
					if (clusters != null) {
						ArrayList<Cluster> finalClusters = new ArrayList<Cluster>();
						for (Cluster cluster : clusters) {
							if (cluster.getArtifacts().size() > 1) {
								if (!totalClusters.contains(cluster)) {
									finalClusters.add(cluster);
									totalClusters.add(cluster);
								}
							}
						}
						if (distanceMatrix.length > 0 && !finalClusters.isEmpty()) {
							MultidimensionalScaling mds = new MultidimensionalScaling(
									distanceMatrix);
							double[][] mdsCoords = mds.cMDS();
							SammonsProjection sammon = new SammonsProjection(mdsCoords, 2, 1000);
							sammon.CreateMapping();
							coords = sammon.getProjection();
						}
						int index = 1;
						int clustered = 0;
						for (Cluster cluster : finalClusters) {
							for(IArtifact artifact : cluster.getArtifacts()) {
								int j = newArtifacts.indexOf(artifact);
								cluster.addCoordinate(artifact, new ClusterPoint(coords[j][0], coords[j][1]));
							}
							clustered += cluster.getArtifacts().size();
							cluster.setUsersAndWords();
							cluster.setIndex(index);
							cluster.setFromdate(Timestamp.valueOf(fromdate));
							cluster.setTodate(Timestamp.valueOf(todate));
							cluster.setProject(project);
							cluster.setCluster_set_name(window);
							cluster.setThreshold(threshold);
							index++;
							//System.out.println("Writing cluster");
							DataManager.writeCluster(cluster);
						}
						DataManager.writeClusterPerProject(project,clustered, newArtifacts.size(), previousWeek.toString() ,nextWeek.toString(), window, threshold);
						//printClusters(finalClusters, week+threshold);
					}
					//System.out.println("Clustering end");
					//rm.printResults(project);
					
					
					//System.out.println("Finish!!!!");
				}
				
				
				System.out.println("Success!!!");
				previousWeek = nextWeek;
			}
			DataManager.closeTheConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
