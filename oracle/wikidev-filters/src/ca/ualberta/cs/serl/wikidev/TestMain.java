package ca.ualberta.cs.serl.wikidev;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import ca.ualberta.cs.serl.wikidev.city3d.animation.BuildingData;
import ca.ualberta.cs.serl.wikidev.city3d.animation.ClusterData;
import ca.ualberta.cs.serl.wikidev.city3d.animation.CommunicationBuildingData;

public class TestMain {

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
			/*ArrayList<CommunicationBuildingData> buildings = DataManager.getCommunicationsBuildingHistory("Message", 99, 1);
			ClusterData clustered = DataManager.getClusteredBuildings(buildings.get(buildings.size()-1));
			int countComm=0;
			int countFile=0;
			for(BuildingData data : clustered.getBuildings().keySet()) {
				
				if (data instanceof CommunicationBuildingData) {
					CommunicationBuildingData commData = (CommunicationBuildingData)data;
					System.out.println(commData.getType()+commData.getArtifact_id()+"\t"+clustered.getBuildings().get(data)+"\t"+commData.getWeek());
					countComm++;
				}
				else {
					countFile++;
				}
			}*/
			Timestamp start = Timestamp.valueOf("2009-09-01 00:00:00");
			Timestamp firstWeek = new Timestamp(start.getTime()+DataManager.WEEK_INTERVAL_IN_MILLIS);
			ArrayList<CommunicationBuildingData> buildings = DataManager.getCommmunicationCitySnapshot(firstWeek, 1);
			for(CommunicationBuildingData building : buildings) {
				System.out.println(building.toString());
			}
			/*System.out.println(clustered.getTerms().size());
			for(String term : clustered.getTerms().keySet()) {
				System.out.println(term+" "+clustered.getTerms().get(term));
			}
			System.out.println(countComm);
			System.out.println(countFile);*/
			
			DataManager.closeTheConnection();
			System.out.println("Success!!!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
