package ca.ualberta.cs.serl.wikidev;

import java.sql.Timestamp;

import ca.ualberta.cs.serl.wikidev.city3d.animation.CommunicationBuildingData;
import ca.ualberta.cs.serl.wikidev.city3d.animation.FileBuildingData;
import ca.ualberta.cs.serl.wikidev.clustering.Cluster;
import ca.ualberta.cs.serl.wikidev.clustering.ClusterPoint;


public abstract class QueryBuilder {
	
	public static final String SELECT_ALL_USERS = "select * from mw_user";
	public static final String SELECT_ALL_PROJECTS = "select * from wikidev_projects";
	
	public static String selectTheEarliestTimeOfActivity() {
		return "SELECT min(time) as time FROM (SELECT min(timestamp) as time from wikidev_changesets UNION SELECT min(date) as time from wikidev_messages UNION SELECT min(change_time) as time from wikidev_ticketchanges UNION SELECT min(created) as time from wikidev_tickets) times";
	}
	
	public static String selectTheLatestTimeOfActivity() {
		return "SELECT max(time) as time FROM (SELECT max(timestamp) as time from wikidev_changesets UNION SELECT max(date) as time from wikidev_messages UNION SELECT max(change_time) as time from wikidev_ticketchanges UNION SELECT max(created) as time from wikidev_tickets) times";
	}
	
	public static String selectUsersPerProject(int projectid) {
		return "select userid from wikidev_projectroles where projectid=\'"+projectid+"\'";
	}
	
	public static String selectProjectByName(String projectName) {
		return "select * from wikidev_projects where projectname=\'"+projectName+"\'";
	}
	
	public static String selectClusterID(Cluster cluster, String projectname) {
		return "select clusterid from wikidev_clusters where project_name=\'"+projectname+"\' and clusterindex=\'"+cluster.getIndex()+"\' and from_date=\'"+cluster.getFromdate().toString()+"\' and to_date=\'"+cluster.getTodate().toString()+"\'";
	}
	
	public static String insertIntoClusters(Cluster cluster, String projectname) {
		return "insert into wikidev_clusters (clusterid, project_name, clusterindex, terms, from_date, to_date, cluster_set_name, threshold) values (null, \'"+projectname+"\', "+cluster.getIndex()+", \'"
		+Dictionary.concatStrings(cluster.getWords())+"\', \'"+cluster.getFromdate().toString()+"\', \'"+cluster.getTodate().toString()+"\', \'"+cluster.getCluster_set_name()+"\', \'"+cluster.getThreshold()+"\')";
	}
	
	public static String insertArtifactInCluster(String type, int clusterid, String projectname, int id, ClusterPoint point) {
		return "insert into wikidev_artifacts_in_clusters values (\'"+type+"\', "+clusterid+", \'"+projectname+"\', "+id+", "+point.getX()+", "+point.getY()+")";
	}
	
	public static String insertUserInCluster(String username, int clusterid) {
		return "insert into wikidev_users_in_clusters values (\'"+username+"\', "+clusterid+")";
	}
	
	public static String insertClusterPerProject(int clustered, String projectname, String fromdate, String todate, int total, String cluster_set_name, double threshold) {
		return "insert into wikidev_clusters_per_project values (\'"+cluster_set_name+"\', \'"+fromdate+"\', \'"+todate+"\', \'"+projectname+"\', \'"+clustered+"\', \'"+total+"\', \'"+threshold+"\')";
	}
	
	public static String selectCommunicationsBuildingID(CommunicationBuildingData building, int projectid) {
		return "select building_id from wikidev_wikidev3d_communications_city where project_id=\'"+projectid+"\' and week=\'"+building.getWeek()+"\' and type=\'"+building.getType()+"\' and artifact_id=\'"+building.getArtifact_id()+"\'";
	}
	
	public static String selectFilesBuildingID(FileBuildingData building, int projectid) {
		return "select building_id from wikidev_wikidev3d_files_city where project_id=\'"+projectid+"\' and url=\'"+building.getUrl()+"\'";
	}
	
	public static String insertCommunicationsBuildingData(String type, int artifact_id, String week, int height, int neighborhoodX, int neighborhoodY, int cityBlock, String url, int projectid, String lastEditor, Timestamp created, Timestamp lastModified) {
		return "insert into wikidev_wikidev3d_communications_city (type, artifact_id, week, height, neighborhoodX, neighborhoodY, cityBlock, url, project_id, last_editor, created, last_modified) values (\""+type+"\", \""+artifact_id+"\", \""+week+"\", \""+height+"\", \""+neighborhoodX+"\", \""+neighborhoodY+"\", \""+cityBlock+"\", \""+url.replaceAll("\"", "\\\\\"")+"\", \""+projectid+"\", \""+lastEditor+"\", \""+created.toString()+"\", \""+lastModified.toString()+"\")";
	}
	
	public static String insertFilesBuildingData(String type, String week, int height, int neighborhoodX, int neighborhoodY, int cityBlock, String url, int projectid, String lastEditor, Timestamp created, Timestamp lastModified) {
		return "insert into wikidev_wikidev3d_files_city (type, week, height, neighborhoodX, neighborhoodY, cityBlock, url, project_id, last_editor, created, last_modified) values (\'"+type+"\', \'"+week+"\', \'"+height+"\', \'"+neighborhoodX+"\', \'"+neighborhoodY+"\', \'"+cityBlock+"\', \'"+url+"\', \'"+projectid+"\', \'"+lastEditor+"\', \'"+created.toString()+"\', \'"+lastModified.toString()+"\')";
	}
	
	public static String insertUserData(int buildingid, String username, int R, int G, int B, double contribution, String week, String cityType) {
		return "insert into wikidev_wikidev3d_users (building_id, username, R, G, B, contribution, week, city_type) values (\'"+buildingid+"\', \'"+username+"\', \'"+R+"\', \'"+G+"\', \'"+B+"\', \'"+contribution+"\', \'"+week+"\', \'"+cityType+"\')";
	}
	
	public static String selectProjectByID(int projectID) {
		return "select * from wikidev_projects where projectid="+projectID;
	}
	
	public static String selectArtifactByURL(String entityType, String URL) {
		return "select * from "+entityType+" where url=\'"+URL+"\'";
	}
	
	public static String selectUserByID(int userID) {
		return "select * from mw_user where user_id="+userID;
	}
	
	public static String selectUserByExternalUserName(String externalUserName) {
		return "select userid from wikidev_usermap where externalusername=\'"+externalUserName+"\'";
	}
	
	public static String selectUserByUserName(String userName) {
		return "select user_id,user_name,user_real_name from mw_user where user_name=\'"+userName+"\'";
	}
	
	public static String selectArtifactsAfterTimestamp(String entityType, String timestamp) {
		return "select * from "+entityType+" where timestamp>"+timestamp;
	}
	
	public static String selectArtifactsForProject(String entityType, int projectid) {
			return "select * from "+entityType+" where project_id=\'"+projectid+"\'";
	}
	
	public static String selectProjectIDByExternalSystem(int externalsystem_id) {
		return "select project from wikidev_projects_system where externalsystem="+externalsystem_id;
	}
	
	public static String selectExternalSystemByProjectID(int projectid) {
		return "select externalsystem from wikidev_projects_system where project="+projectid;
	}
	
	public static String selectTicketChangesByTicketID(int ticketID) {
		return "select * from wikidev_ticketchanges where ticket_id="+ticketID;
	}
	
	public static String selectChangeSetDetailByRevAndProject(int rev, int externalsystem_id) {
		return "select * from wikidev_changesetdetails where rev=\'"+rev+"\' and externalsystem_id=\'"+externalsystem_id+"\'";
	}
	
	public static String selectSourceClassByName(String className) {
		return "select * from wikidev_sourceclasses where name=\'"+className+"\'";
 	}
	
	public static String selectUserAliases(int userID) {
		return "select * from wikidev_usermap where userid="+userID;
	}
	
	public static String selectProjectRoles(int userID) {
		return "select * from wikidev_projectroles where userid="+userID;
	}
	
	public static String selectCommunicationParticipants(int communicationID) {
		return "select * from wikidev_communicationparticipants where communicationid="+communicationID;
	}
	
	public static String selectSourceClassEditors(int sourceClassID) {
		return "select * from wikidev_sourceclasseditors where classid="+sourceClassID;
	}
	
	public static String selectProjectSourceClasses(int projectID) {
		return "select * from wikidev_projectsourceclasses where projectid="+projectID;
	}
	
	public static String selectSourceClassByID(int classID) {
		return "select * from wikidev_sourceclasses where classid="+classID;
	}
	
	public static String selectProjectUsers(int projectID) {
		return "select * from wikidev_projectroles where projectid="+projectID;
	}
	
	public static String selectTicketByDrID(int ticketid, int project) {
		return "select * from wikidev_tickets where project_ticket_id=\'"+ticketid+"\' AND project_id=\'"+project+"\'";
	}
	
	public static String selectAllChangesetsForProject(int externalsystem) {
		return "select * from wikidev_changesets where externalsystem_id=\'"+externalsystem+"\'";
	}
	
	public static String selectArtifactsForProjectBetweenDates(String entityType, String timestamp1, String timestamp2, int projectid, int page_namespace_id) {
		String query = "";
		if(entityType.equals("wikidev_tickets")) {
			query =  "select * from wikidev_tickets where ((created>=\'"+timestamp1+"\'"+" AND created<=\'"+timestamp2+"\') OR (last_modified>=\'"+timestamp1+"\'"+" AND last_modified<=\'"+timestamp2+"\'))"+" AND project_id=\'"+projectid+"\'";
		}
		else if(entityType.equals("wikidev_messages")) {
			query =  "select * from wikidev_messages where date>=\'"+timestamp1+"\'"+" AND date<=\'"+timestamp2+"\'"+" AND project_id=\'"+projectid+"\'";
		}
		else if(entityType.equals("mw_page")) {
			query =  "select * from mw_page where page_touched>=\'"+timestamp1+"\'"+" AND page_touched<=\'"+timestamp2+"\'"+" AND page_namespace=\'"+page_namespace_id+"\'";
		}
		else if(entityType.equals("wikidev_changesets")) {
			query =  "select * from wikidev_changesets where timestamp>=\'"+timestamp1+"\'"+" AND timestamp<=\'"+timestamp2+"\'"+" AND project_id="+projectid;
		}
		return query;
	}
	
	/*private static String getProjectName(int projectid) {
		if(projectid<10) {
			return "team0"+projectid;
		}
		else {
			return "team"+projectid;
		}
	}*/

	public static String selectRevisionsByWikiID(int page_id) {
		return "select * from mw_revision where rev_page=\'"+page_id+"\'";
	}

	public static String selectRevisionTextByRevisionID(int rev_id) {
		return "select CAST(old_text AS CHAR) from mw_text where old_id=\'"+rev_id+"\'";
	}

	public static String selectProjectNamespaceByNamespaceID(int page_namespace_id) {
		return "select * from mw_an_extranamespaces where nsId=\'"+page_namespace_id+"\'";
	}

	public static String selectProjectNamespaceByID(int projectid) {
		return "select * from wikidev_projects where projectid=\'"+projectid+"\'";
	}

	public static String selectProjectNamespaceIDbyNamespace(String namespace) {
		return "select * from mw_an_extranamespaces where nsName=\'"+namespace+"\'";
	}

	public static String selectCommunicationCitySnapshot(Timestamp timestamp,
			int projectid) {
		return "select * from wikidev_wikidev3d_communications_city where week=\'"+timestamp+"\' and project_id=\'"+projectid+"\'";
	}
	
	public static String selectFileCitySnapshot(Timestamp timestamp,
			int projectid) {
		return "select * from wikidev_wikidev3d_files_city where week=\'"+timestamp+"\' and project_id=\'"+projectid+"\'";
	}

	public static String selectUsersForBuilding(int id, String city_type) {
		return "select * from wikidev_wikidev3d_users where building_id=\'"+id+"\' and city_type=\'"+city_type+"\'";
	}

	public static String selectCommunicationsBuildingHistory(String type, int artifactId, int projectid) {
		return "select * from wikidev_wikidev3d_communications_city where type=\'"+type+"\' and project_id=\'"+projectid+"\' and artifact_id=\'"+artifactId+"\'";
	}
	
	public static String selectFilesBuildingHistory(String url) {
		return "select * from wikidev_wikidev3d_files_city where url=\'"+url+"\'";
	}
	
	public static String selectCommunicationsCitySize(int projectid) {
		return "SELECT max( size ) AS size FROM (SELECT max( neighborhoodX ) AS size FROM wikidev_wikidev3d_communications_city AS a UNION SELECT max( neighborhoodY ) AS size FROM wikidev_wikidev3d_communications_city AS b)times";
	}
	
	public static String selectFilesCitySize(int projectid) {
		return "SELECT max( size ) AS size FROM (SELECT max( neighborhoodX ) AS size FROM wikidev_wikidev3d_files_city AS a UNION SELECT max( neighborhoodY ) AS size FROM wikidev_wikidev3d_files_city AS b)times";
	}

	public static String selectClusterIDsByBuilding(CommunicationBuildingData building) {
		return "select clusterid from wikidev_artifacts_in_clusters where type=\'"+building.getType()+"\' and id=\'"+building.getArtifact_id()+"\'";
	}

	public static String selectClusterByID(int clusterid) {
		return "select * from wikidev_clusters where clusterid=\'"+clusterid+"\'";
	}

	public static String selectArtifactsInCluster(Cluster cluster) {
		return "select type,id from wikidev_artifacts_in_clusters where clusterid=\'"+cluster.getClusterid()+"\'";
	}

	public static String selectArtifactByTypeAndID(String type, int id, int projectid) {
		String query = "";
		if(type.equals("Ticket")) {
			query =  "select * from wikidev_tickets where id=\'"+id+"\'";
		}
		else if(type.equals("Message")) {
			query =  "select * from wikidev_messages where id=\'"+id+"\'";
		}
		else if(type.equals("Wiki")) {
			query =  "select * from mw_page where page_id=\'"+id+"\'";
		}
		else if(type.equals("ChangeSet")) {
			query =  "select * from wikidev_changesets where rev=\'"+id+"\' AND project_id=\'"+projectid+"\'";
		}
		return query;
	}

	

}
