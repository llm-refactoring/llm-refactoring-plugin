package ca.ualberta.cs.serl.wikidev;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

import ca.ualberta.cs.serl.wikidev.artifacts.ChangeSet;
import ca.ualberta.cs.serl.wikidev.artifacts.IArtifact;
import ca.ualberta.cs.serl.wikidev.artifacts.Message;
import ca.ualberta.cs.serl.wikidev.artifacts.Ticket;
import ca.ualberta.cs.serl.wikidev.artifacts.Wiki;
import ca.ualberta.cs.serl.wikidev.city3d.CityLayout;
import ca.ualberta.cs.serl.wikidev.city3d.Layout;
import ca.ualberta.cs.serl.wikidev.city3d.IndustrialLayout;
import ca.ualberta.cs.serl.wikidev.city3d.animation.BuildingData;
import ca.ualberta.cs.serl.wikidev.city3d.animation.ClusterData;
import ca.ualberta.cs.serl.wikidev.city3d.animation.CommunicationBuildingData;
import ca.ualberta.cs.serl.wikidev.clustering.Cluster;
import ca.ualberta.cs.serl.wikidev.clustering.ClusterPoint;
import ca.ualberta.cs.serl.wikidev.clustering.Clustering;
import ca.ualberta.cs.serl.wikidev.clustering.MultidimensionalScaling;
import ca.ualberta.cs.serl.wikidev.clustering.SammonsProjection;

public class CityMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			String[] args2 = {"jdbc:mysql://hypatia.cs.ualberta.ca/wikidev_ucosp", "fokaefs", "filimon9786#7@gr", "2009-09-01 00:00:00", "2010-01-01 00:00:00", "1", "weekly", "-lb", "0.05", "-ub", "0.95", "-i", "0.05"};
			InvocationParser ip = new InvocationParser();
			ip.parseArgs(args2);
			String databaseName = ip.getDatabaseName();
			String username = ip.getUsername();
			String password = ip.getPassword();
			DataManager.openTheConnection(username, password, databaseName);
			//String fromdate = ip.getFromdate();
			//String todate = ip.getTodate();
			String fromdate = DataManager.getTheEarlierstTimeOfActivity().toString();
			String todate = DataManager.getTheLatestTimeOfActivity().toString();
			//String fromdate = "2009-09-01 00:00:00";
			//String todate = "2009-10-01 00:00:00";
			int project = ip.getProject();
			String window = ip.getWindow();
			double lowerBound = ip.getLowerBound();
			double upperBound = ip.getUpperBound();
			double interval = ip.getInterval();
			String week = "week";
			
			System.out.println("Relationship miner start");
			ArrayList<IArtifact> newArtifacts = DataManager.getArtifactsBetweenDates(fromdate, todate,project);
			ArrayList<ChangeSet> changesets = new ArrayList<ChangeSet>();
			for(IArtifact artifact : newArtifacts) {
				if(artifact instanceof ChangeSet) {
					ChangeSet changeset = (ChangeSet)artifact;
						changesets.add(changeset);
				}
			}
			newArtifacts.removeAll(changesets);
			int i=0;
			for(IArtifact artifact : newArtifacts) {
				System.out.println(i+artifact.toString());
				i++;
				
			}
			//System.exit(0);
			RelationshipMiner rm = new RelationshipMiner(newArtifacts);
			rm.getRelationships();
			System.out.println("Relationship miner end "+newArtifacts.size());
			//Layout.assignUserColors(7);
			CityLayout cityLayout = new CityLayout(newArtifacts, rm, project);
			cityLayout.prepareToWriteCitySnapshots(Timestamp.valueOf(fromdate), Timestamp.valueOf(todate));
			IndustrialLayout layout = new IndustrialLayout(changesets, project);
			layout.prepareToWriteCitySnapshots(Timestamp.valueOf(fromdate), Timestamp.valueOf(todate));
			/*ArrayList<CommunicationBuildingData> buildings = DataManager.getCommunicationsBuildingHistory("Message", 66, 1);
			ClusterData clustered = DataManager.getClusteredBuildings(buildings.get(0));
			int countComm=0;
			int countFile=0;
			for(BuildingData data : clustered.getBuildings().keySet()) {
				
				if (data instanceof CommunicationBuildingData) {
					//System.out.println(data.getUrl());
					countComm++;
				}
				else {
					countFile++;
				}
			}
			System.out.println(clustered.getTerms().size());
			for(String term : clustered.getTerms().keySet()) {
				System.out.println(term+" "+clustered.getTerms().get(term));
			}
			System.out.println(countComm);
			System.out.println(countFile);*/
			
			
			DataManager.closeTheConnection();
			System.out.println("Success!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public static void printClusters(ArrayList<Cluster> clusters, String week) throws IOException {
		//BufferedWriter out = new BufferedWriter(new FileWriter("C:\\Documents and Settings\\Marios Fokaefs\\Desktop\\wikidev-files\\ucosp\\clusters"+week+".txt"));
		BufferedWriter out = new BufferedWriter(new FileWriter("C:\\Documents and Settings\\Marios Fokaefs\\Desktop\\wikidev-files\\c301f09\\clusters"+week+".txt"));
		int index = 1;
		for(Cluster cluster : clusters) {
			out.write("======================================================================");
			out.newLine();
			out.write("Cluster "+index);
			out.newLine();
			out.write("======================================================================");
			out.newLine();
			out.newLine();
			out.write("Users");
			out.newLine();
			out.write("----------------------------------------------------------------------");
			out.newLine();
			for(User user : cluster.getUsers()) {
				out.write(user.toString());
				out.newLine();
			}
			out.newLine();
			out.write("Terms");
			out.newLine();
			out.write("-----------------------------------------------------------------------");
			out.newLine();
			for(String term : cluster.getWords()) {
				out.write(term);
				out.newLine();
			}
			out.newLine();
			out.write("Artifacts");
			out.newLine();
			out.write("----------------------------------------------------------------------");
			out.newLine();
			for(IArtifact artifact : cluster.getArtifacts()) {
				out.write(artifact.toString());
				out.newLine();
			}
			out.newLine();
			out.newLine();
			index++;
		}
		out.close();
	}

}
