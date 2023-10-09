package ca.ualberta.cs.serl.wikidev;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import ca.ualberta.cs.serl.wikidev.artifacts.ChangeSet;
import ca.ualberta.cs.serl.wikidev.artifacts.ChangeSetDetail;
import ca.ualberta.cs.serl.wikidev.artifacts.IArtifact;
import ca.ualberta.cs.serl.wikidev.artifacts.Message;
import ca.ualberta.cs.serl.wikidev.artifacts.SourceClass;
import ca.ualberta.cs.serl.wikidev.artifacts.Ticket;
import ca.ualberta.cs.serl.wikidev.artifacts.TicketChange;
import ca.ualberta.cs.serl.wikidev.artifacts.Wiki;
import ca.ualberta.cs.serl.wikidev.artifacts.WikiRevision;
import ca.ualberta.cs.serl.wikidev.city3d.CityLayout;
import ca.ualberta.cs.serl.wikidev.city3d.IndustrialLayout;
import ca.ualberta.cs.serl.wikidev.city3d.animation.BuildingData;
import ca.ualberta.cs.serl.wikidev.city3d.animation.ClusterData;
import ca.ualberta.cs.serl.wikidev.city3d.animation.CommunicationBuildingData;
import ca.ualberta.cs.serl.wikidev.city3d.animation.FileBuildingData;
import ca.ualberta.cs.serl.wikidev.city3d.animation.UserData;
import ca.ualberta.cs.serl.wikidev.clustering.Cluster;
import ca.ualberta.cs.serl.wikidev.clustering.ClusterPoint;

public class DataManager {
	private static final String databaseDriver = "com.mysql.jdbc.Driver";
    private static final String databaseName = "jdbc:mysql://hypatia.cs.ualberta.ca/wikidev_ucosp";
    //private static final String databaseName = "jdbc:mysql://hypatia.cs.ualberta.ca/c301f09_wikidev";
    private static final String[] entityTypes = {"wikidev_changesets", "wikidev_tickets", "mw_page", "wikidev_messages"};
    public static final long WEEK_INTERVAL_IN_MILLIS = 604800000;
    private static Connection conn;
    private static Statement statement;
    public static Set<Integer> userIDs;
    public static Set<User> users;
    public static Set<String> usernames;
    public static Set<Integer> projectIDs;
    public static Set<Project> projects;
    public static Set<String> artifactURLs;
    public static Set<IArtifact> allArtifacts;
    public static Set<String> classNames;
    public static Set<SourceClass> classes;
    public static HashMap<Integer, Integer> ticketids;
    public static Set<Ticket> tickets;
    public static Set<Integer> systems;
    public static Set<ChangeSet> changesets;
    public static Set<Integer> commBuildingIDs;
    public static Set<CommunicationBuildingData> commBuildings;
    public static Set<Integer> fileBuildingIDs;
    public static Set<FileBuildingData> fileBuildings;
    
    public DataManager() throws Exception {
        Class.forName(databaseDriver).newInstance();
        conn=DriverManager.getConnection(databaseName, "wikidev", "wikidev989");
        statement=conn.createStatement();
    }
    
    public static void openTheConnection() throws Exception{
    	Class.forName(databaseDriver).newInstance();
        conn=DriverManager.getConnection(databaseName, "wikidev_ucosp", "XvWRSVaEVLeSH2fW ");
        //conn=DriverManager.getConnection(databaseName, "fokaefs", "filimon9786#7@gr");
        statement=conn.createStatement();
        userIDs = new HashSet<Integer>();
        users = new HashSet<User>();
        projectIDs = new HashSet<Integer>();
        projects = new HashSet<Project>();
        artifactURLs = new HashSet<String>();
        allArtifacts = new HashSet<IArtifact>();
        classNames = new HashSet<String>();
        classes = new HashSet<SourceClass>();
        ticketids = new HashMap<Integer, Integer>();
        tickets = new HashSet<Ticket>();
        usernames = new HashSet<String>();
        systems = new HashSet<Integer>();
        changesets = new HashSet<ChangeSet>();
        commBuildingIDs = new HashSet<Integer>();
        commBuildings = new HashSet<CommunicationBuildingData>();
        fileBuildingIDs = new HashSet<Integer>();
        fileBuildings = new HashSet<FileBuildingData>();
    }
    
    public static void openTheConnection(String username, String password, String databaseName) throws Exception{
    	Class.forName(databaseDriver).newInstance();
    	try {
        conn=DriverManager.getConnection(databaseName, username, password);
    	}
    	catch(SQLException e) {
    		System.out.println(e.getMessage());
    		System.exit(1);
    	}
        statement=conn.createStatement();
        userIDs = new HashSet<Integer>();
        users = new HashSet<User>();
        projectIDs = new HashSet<Integer>();
        projects = new HashSet<Project>();
        artifactURLs = new HashSet<String>();
        allArtifacts = new HashSet<IArtifact>();
        classNames = new HashSet<String>();
        classes = new HashSet<SourceClass>();
        ticketids = new HashMap<Integer, Integer>();
        tickets = new HashSet<Ticket>();
        usernames = new HashSet<String>();
        systems = new HashSet<Integer>();
        changesets = new HashSet<ChangeSet>();
        commBuildingIDs = new HashSet<Integer>();
        commBuildings = new HashSet<CommunicationBuildingData>();
        fileBuildingIDs = new HashSet<Integer>();
        fileBuildings = new HashSet<FileBuildingData>();
    }

    public static void closeTheConnection() throws SQLException {
        if(statement != null) statement.close();
        if (conn != null) conn.close();
    }
    
    public static Timestamp getTheEarlierstTimeOfActivity() throws SQLException {
    	String sqlStmt = QueryBuilder.selectTheEarliestTimeOfActivity();
    	ResultSet res = null;
    	Timestamp timestamp = null;
    	res = statement.executeQuery(sqlStmt);
    	while(res.next()) {
    		timestamp = res.getTimestamp("time");
    	}
    	if(res != null) res.close();
    	return timestamp;
    }
    
    public static Timestamp getTheLatestTimeOfActivity() throws SQLException {
    	String sqlStmt = QueryBuilder.selectTheLatestTimeOfActivity();
    	ResultSet res = null;
    	Timestamp timestamp = null;
    	res = statement.executeQuery(sqlStmt);
    	while(res.next()) {
    		timestamp = res.getTimestamp("time");
    	}
    	if(res != null) res.close();
    	return timestamp;
    }
    
    public static void writeCluster(Cluster cluster) throws SQLException {
    	Project project = DataManager.getProjectByID(cluster.getProject());
        String sqlStmt = QueryBuilder.insertIntoClusters(cluster, project.getProjectName());
        statement.executeUpdate(sqlStmt);
        DataManager.writeArtifactInCluster(cluster);
        DataManager.writeUserInCluster(cluster);
    }
    
    public static void writeClusterPerProject(int projectid, int clustered, int total, String fromdate, String todate, String cluster_set_name, double threshold) throws SQLException{
    	Project project = DataManager.getProjectByID(projectid);
    	String sqlStmt = QueryBuilder.insertClusterPerProject(clustered, project.getProjectName(), fromdate, todate, total, cluster_set_name, threshold);
    	statement.executeUpdate(sqlStmt);
    }
    
    public static void writeUserInCluster(Cluster cluster) throws SQLException {
    	int clusterid = DataManager.getClusterID(cluster);
    	for(User user : cluster.getUsers()) {
    		String sqlStmt = QueryBuilder.insertUserInCluster(user.getUserName(), clusterid);
    		//System.out.println(sqlStmt);
    		statement.executeUpdate(sqlStmt);
    	}
    }
    
    public static int getClusterID(Cluster cluster) throws SQLException {
    	Project project = DataManager.getProjectByID(cluster.getProject());
    	String sqlStmt = QueryBuilder.selectClusterID(cluster, project.getProjectName());
    	ResultSet res = null;
    	int clusterid = 0;
    	res = statement.executeQuery(sqlStmt);
    	while(res.next()) {
    		clusterid = res.getInt("clusterid");
    	}
    	if(res != null) res.close();
    	return clusterid;
    }
    
    private static Cluster getClusterByID(int clusterid) throws SQLException {
    	String sqlStmt = QueryBuilder.selectClusterByID(clusterid);
    	ResultSet res = null;
    	Cluster cluster = null;
    	res = statement.executeQuery(sqlStmt);
    	while(res.next()) {
    		cluster = new Cluster(res.getTimestamp("to_date"), res.getString("project_name"),res.getDouble("threshold"), res.getString("terms"), res.getInt("clusterid"));
    	}
    	if(res != null) res.close();
    	cluster.setProject(DataManager.getProjectByName(cluster.getProjectname()).getProjectID());
    	return cluster;
    }
    
    public static void writeCommunicationsBuildingData(CommunicationBuildingData building) throws SQLException {
    	String lastEditor = "";
    	if (building.getLastEditor() != null) {
			lastEditor += building.getLastEditor().getUserName();
		}
        String sqlStmt = QueryBuilder.insertCommunicationsBuildingData(building.getType(), building.getArtifact_id(), building.getWeek().toString(), building.getHeight(), building.getNeighborhoodX(), building.getNeighborhoodY(), building.getCityBlock(), building.getUrl(), building.getProjectid(), lastEditor, building.getCreated(), building.getLastModified());
        statement.executeUpdate(sqlStmt);
        DataManager.writeCommunicationsUserData(building);
    }
    
    public static void writeFilesBuildingData(FileBuildingData building) throws SQLException {
    	String lastEditor = "";
    	if (building.getLastEditor() != null) {
			lastEditor += building.getLastEditor().getUserName();
		}
		String sqlStmt = QueryBuilder.insertFilesBuildingData(building.getType(), building.getWeek().toString(), building.getHeight(), building.getNeighborhoodX(), building.getNeighborhoodY(), building.getCityBlock(), building.getUrl(), building.getProjectid(), lastEditor, building.getCreated(), building.getLastModified());
        statement.executeUpdate(sqlStmt);
        DataManager.writeFilesUserData(building);
    }
    
    private static ArrayList<Integer> getClusterIDsByBuilding(CommunicationBuildingData building) throws SQLException {
    	ArrayList<Integer> clusterIDs = new ArrayList<Integer>();
    	String sqlStmt = QueryBuilder.selectClusterIDsByBuilding(building);
    	ResultSet res = null;
    	res = statement.executeQuery(sqlStmt);
    	while(res.next()) {
    		clusterIDs.add(res.getInt("clusterid"));
    	}
    	if(res != null) res.close();
    	return clusterIDs;
    }
    
    private static HashMap<IArtifact, Double> getArtifactsInCluster(Cluster cluster) throws SQLException {
    	ArrayList<IArtifact> artifacts = new ArrayList<IArtifact>();
    	HashMap<IArtifact, Double> finalArtifacts = new HashMap<IArtifact, Double>();
    	String sqlStmt = QueryBuilder.selectArtifactsInCluster(cluster);
    	IArtifact artifact = null;
    	ResultSet res = null;
    	res = statement.executeQuery(sqlStmt);
    	while(res.next()) {
    		artifact = new IArtifact(res.getString("type"), res.getInt("id"));
    		artifacts.add(artifact);
    	}
    	if(res != null) res.close();
    	for(IArtifact anArtifact : artifacts) {
    		finalArtifacts.put(DataManager.getArtifactByTypeAndID(anArtifact, cluster.getProject()), cluster.getThreshold());
    	}
    	return finalArtifacts;
    	
    }
    
	private static IArtifact getArtifactByTypeAndID(IArtifact anArtifact, int projectid) throws SQLException{
		Project project = DataManager.getProjectByID(projectid);
    	String sqlStmt = QueryBuilder.selectArtifactByTypeAndID(anArtifact.getType(), anArtifact.getId(), projectid);
		ResultSet res = null;
		IArtifact artifact = null;
		res = statement.executeQuery(sqlStmt);
		while (res.next()) {
			if (anArtifact.getType().equals("ChangeSet")) {
				artifact = new ChangeSet(res.getInt("id"), res
						.getString("comment"), res
						.getString("externalauthor"), res
						.getInt("externalsystem_id"), res
						.getTimestamp("timestamp"), res.getInt("rev"),
						res.getInt("project_id"));
			} else if (anArtifact.getType().equals("Ticket")) {
				artifact = new Ticket(res.getTimestamp("created"), res
						.getString("description"), res
						.getTimestamp("last_modified"), res.getString("owner_name"), res.getString("reporter"), 
						res.getString("summary"),
						res.getInt("id"), res.getInt("project_id"), res.getInt("project_ticket_id"), res.getString("priority"));
			} else if (anArtifact.getType().equals("Wiki")) {
				artifact = new Wiki(res.getInt("page_id"), res.getInt("page_namespace"), res.getString("page_title"), res.getTimestamp("page_touched"));
			}
			else if(anArtifact.getType().equals("Message")) {
            	artifact = new Message(res.getString("address"), res.getString("author"), res.getString("body"), res.getInt("id"),
						res.getInt("deleted"), res.getString("mid_header"), 
						res.getInt("project_id"),  res.getString("subject"), res.getTimestamp("date"), res.getString("user_name"), res.getString("refid_header"));;
            }
		}
		if(res !=null) res.close();
			if (artifact instanceof ChangeSet) {
				ChangeSet changeSet = (ChangeSet)artifact;
				changeSet.setOwner(getUserByUserName(changeSet.getExternalAuthorUserName()));
				changeSet.setProject(project);
				changeSet.setChangesetDetails(getChangeSetDetailsByRevAndProject(changeSet.getRevision(), changeSet.getExternalSystemID()));
				for(ChangeSetDetail detail : changeSet.getChangesetDetails()) {
					detail.setTimestamp(changeSet.getTimestamp());
				}
				changeSet.setAuthorForChangeSetDetails();
				changeSet.setType("ChangeSet");
				project.addChangeset(changeSet);
			} else if (artifact instanceof Ticket) {
				Ticket ticket = (Ticket)artifact;
				ticket.setOwner(getUserByUserName(ticket.getOwnerName()));
				ticket.setReporter(getUserByUserName(ticket.getReporterName()));
				ticket.setTicketChanges(getTicketChangesByTicketID(ticket
						.getTicketID()));
				ticket.setProject(project);
				ticket.setType("Ticket");
				project.addTicket(ticket);
			} else if (artifact instanceof Message) {
				Message communication = (Message)artifact;
				User user = getUserByUserName(communication
						.getUser_name());
				communication.setOwner(user);
				user.setUserRealName(communication.getAuthor());
				communication.setProject(project);
				communication.setType("Message");
				project.addMessage(communication);
			}
			else if(artifact instanceof Wiki) {
	        	Wiki wiki = (Wiki)artifact;
	        	wiki.setRevisions(getRevisionsByWikiID(wiki.getPage_id()));
	        	wiki.setProject(DataManager.getProjectByName(getProjectNamespaceByNamespaceID(wiki.getPage_namespace_id())));
	        	wiki.setProjectID(wiki.getProject().getProjectID());
	        	wiki.setType("Wiki");
	        }
		return artifact;
    }
    
    public static ClusterData getClusteredBuildings(CommunicationBuildingData building) throws SQLException {
    	ClusterData clusterData = new ClusterData(building.getWeek());
    	ArrayList<Integer> clusterIDs = DataManager.getClusterIDsByBuilding(building);
    	ArrayList<Cluster> clusters = new ArrayList<Cluster>();
    	for(Integer id : clusterIDs) {
    		clusters.add(DataManager.getClusterByID(id));
    	}
    	for(Cluster cluster : clusters) {
    		HashMap<IArtifact, Double> artifacts = DataManager.getArtifactsInCluster(cluster);
    		clusterData.addArtifacts(artifacts);
    		HashMap<String, Double> terms = new HashMap<String, Double>();
    		for(String term : cluster.getWords()) {
    			terms.put(term, cluster.getThreshold());
    		}
    		clusterData.addTerms(terms);
    	}
    	clusterData.calculateBuildingData();
    	return clusterData;
    }
    
    public static int getCommunicationsBuildingID(CommunicationBuildingData building) throws SQLException {
    	String sqlStmt = QueryBuilder.selectCommunicationsBuildingID(building, building.getProjectid());
    	ResultSet res = null;
    	int buildingid = 0;
    	res = statement.executeQuery(sqlStmt);
    	while(res.next()) {
    		buildingid = res.getInt("building_id");
    	}
    	if(res != null) res.close();
    	return buildingid;
    }
    
    public static int getFilesBuildingID(FileBuildingData building) throws SQLException {
    	String sqlStmt = QueryBuilder.selectFilesBuildingID(building, building.getProjectid());
    	ResultSet res = null;
    	int buildingid = 0;
    	res = statement.executeQuery(sqlStmt);
    	while(res.next()) {
    		buildingid = res.getInt("building_id");
    	}
    	if(res != null) res.close();
    	return buildingid;
    }
    
    public static void writeCommunicationsUserData(CommunicationBuildingData building) throws SQLException {
    	int buildingid = DataManager.getCommunicationsBuildingID(building);
    	for(UserData user : building.getUsers()) {
	        String sqlStmt = QueryBuilder.insertUserData(buildingid, user.getUsername(), user.getR(), user.getG(), user.getB(), user.getContribution(), user.getWeek().toString(), user.getCityType());
	        statement.executeUpdate(sqlStmt);
	        //conn.commit();
    	}
    }
    
    public static void writeFilesUserData(FileBuildingData building) throws SQLException {
    	int buildingid = DataManager.getFilesBuildingID(building);
    	for(UserData user : building.getUsers()) {
	        String sqlStmt = QueryBuilder.insertUserData(buildingid, user.getUsername(), user.getR(), user.getG(), user.getB(), user.getContribution(), user.getWeek().toString(), user.getCityType());
	        statement.executeUpdate(sqlStmt);
	        //conn.commit();
    	}
    }
    
    public static void writeArtifactInCluster(Cluster cluster) throws SQLException {
    	Project project = DataManager.getProjectByID(cluster.getProject());
    	int clusterid = DataManager.getClusterID(cluster);
    	for(IArtifact artifact : cluster.getArtifacts()) {
    		String type = "";
    		int id = 0;
    		ClusterPoint point = cluster.getCoordinates().get(artifact);
    		if(artifact instanceof ChangeSet) {
    			type = "ChangeSet";
    			ChangeSet changeset = (ChangeSet)artifact;
    			id = changeset.getRevision();
    		}
    		else if(artifact instanceof Ticket) {
    			type = "Ticket";
    			Ticket ticket = (Ticket)artifact;
    			id = ticket.getTicketID();
    		}
    		else if(artifact instanceof Message) {
    			type = "Message";
    			Message message = (Message)artifact;
    			id = message.getCommunicationID();
    		}
    		else if(artifact instanceof Wiki) {
    			type = "Wiki";
    			Wiki wiki = (Wiki)artifact;
    			id = wiki.getPage_id();
    		}
	        String sqlStmt = QueryBuilder.insertArtifactInCluster(type, clusterid, project.getProjectName(), id, point);
	        statement.executeUpdate(sqlStmt);
	        //conn.commit();
    	}
    }
    
    public static ArrayList<CommunicationBuildingData> getCommmunicationCitySnapshot(Timestamp timestamp, int projectid) throws SQLException{
    	ArrayList<CommunicationBuildingData> buildings = new ArrayList<CommunicationBuildingData>();
    	String sqlStmt = QueryBuilder.selectCommunicationCitySnapshot(timestamp, projectid);
    	CommunicationBuildingData building = null;
    	ResultSet res = null;
		res = statement.executeQuery(sqlStmt);
		while (res.next()) {
			building = new CommunicationBuildingData(res.getString("type"), res.getInt("artifact_id"), res.getTimestamp("week"), res.getInt("height"), 
					res.getInt("neighborhoodX"), res.getInt("neighborhoodY"), res.getInt("cityBlock"), res.getString("url"), res.getInt("project_id"), res.getInt("building_id"), res.getString("last_editor"), res.getTimestamp("created"), res.getTimestamp("last_modified"));
			buildings.add(building);
			
		}
		if (res != null)
			res.close();
		for(CommunicationBuildingData data : buildings) {
			int id = DataManager.getCommunicationsBuildingID(data);
			data.setUsers(DataManager.getUsersForBuilding(id, CityLayout.CITY_TYPE));
			data.setLastEditor(DataManager.getUserByUserName(data.getLastEditorString()));
		}
		return buildings;
    }
    
    public static ArrayList<FileBuildingData> getFileCitySnapshot(Timestamp timestamp, int projectid) throws SQLException{
    	ArrayList<FileBuildingData> buildings = new ArrayList<FileBuildingData>();
    	String sqlStmt = QueryBuilder.selectFileCitySnapshot(timestamp, projectid);
    	FileBuildingData building = null;
    	ResultSet res = null;
		res = statement.executeQuery(sqlStmt);
		while (res.next()) {
			building = new FileBuildingData(res.getString("type"), res.getTimestamp("week"), res.getInt("height"), 
					res.getInt("neighborhoodX"), res.getInt("neighborhoodY"), res.getInt("cityBlock"), res.getString("url"), res.getInt("project_id"), res.getInt("building_id"), res.getString("last_editor"), res.getTimestamp("created"), res.getTimestamp("last_modified"));
			buildings.add(building);
			
		}
		if (res != null)
			res.close();
		for(FileBuildingData data : buildings) {
			int id = DataManager.getFilesBuildingID(data);
			data.setUsers(DataManager.getUsersForBuilding(id, IndustrialLayout.CITY_TYPE));
			data.setLastEditor(DataManager.getUserByUserName(data.getLastEditorString()));
		}
		return buildings;
    }
    
    private static ArrayList<UserData> getUsersForBuilding(int id, String cityType) throws SQLException{
		ArrayList<UserData> users = new ArrayList<UserData>();
		UserData user = null;
		String sqlStmt = QueryBuilder.selectUsersForBuilding(id, cityType);
    	ResultSet res = null;
		res = statement.executeQuery(sqlStmt);
		while (res.next()) {
			user = new UserData(res.getString("username"), res.getInt("R"), res.getInt("G"), res.getInt("B"), res.getDouble("contribution"), res.getTimestamp("week"), res.getString("city_type"));
			users.add(user);
			
		}
		if (res != null)
			res.close();
		return users;
	}
    
    public static ArrayList<CommunicationBuildingData> getCommunicationsBuildingHistory(String type, int artifact_id, int projectid) throws SQLException{
    	ArrayList<CommunicationBuildingData> buildings = new ArrayList<CommunicationBuildingData>();
    	CommunicationBuildingData building = null;
    	String sqlStmt = QueryBuilder.selectCommunicationsBuildingHistory(type, artifact_id, projectid);
    	ResultSet res = null;
		res = statement.executeQuery(sqlStmt);
		while (res.next()) {
			building = new CommunicationBuildingData(res.getString("type"), res.getInt("artifact_id"), res.getTimestamp("week"), res.getInt("height"), 
					res.getInt("neighborhoodX"), res.getInt("neighborhoodY"), res.getInt("cityBlock"), res.getString("url"), res.getInt("project_id"), res.getInt("building_id"), res.getString("last_editor"), res.getTimestamp("created"), res.getTimestamp("last_modified"));
			buildings.add(building);
			
		}
		if (res != null)
			res.close();
		for(CommunicationBuildingData aBuilding : buildings) {
			int id = getCommunicationsBuildingID(aBuilding);
			aBuilding.setUsers(getUsersForBuilding(id, CityLayout.CITY_TYPE));
			aBuilding.setLastEditor(DataManager.getUserByUserName(aBuilding.getLastEditorString()));
		}
		return buildings;
    }
    
    public static ArrayList<FileBuildingData> getFilesBuildingHistory(String url) throws SQLException{
    	ArrayList<FileBuildingData> buildings = new ArrayList<FileBuildingData>();
    	FileBuildingData building = null;
    	String sqlStmt = QueryBuilder.selectFilesBuildingHistory(url);
    	ResultSet res = null;
		res = statement.executeQuery(sqlStmt);
		while (res.next()) {
			building = new FileBuildingData(res.getString("type"), res.getTimestamp("week"), res.getInt("height"), 
					res.getInt("neighborhoodX"), res.getInt("neighborhoodY"), res.getInt("cityBlock"), res.getString("url"), res.getInt("project_id"), res.getInt("building_id"), res.getString("last_editor"), res.getTimestamp("created"), res.getTimestamp("last_modified"));
			buildings.add(building);
			
		}
		if (res != null)
			res.close();
		for(FileBuildingData aBuilding : buildings) {
			int id = getFilesBuildingID(aBuilding);
			aBuilding.setUsers(getUsersForBuilding(id, IndustrialLayout.CITY_TYPE));
			aBuilding.setLastEditor(DataManager.getUserByUserName(aBuilding.getLastEditorString()));
		}
		return buildings;
    }
    
    public static int getCommunicationsCitySize(int projectid) throws SQLException {
    	int size = 3;
    	String sqlStmt = QueryBuilder.selectCommunicationsCitySize(projectid);
    	ResultSet res = null;
		res = statement.executeQuery(sqlStmt);
		while (res.next()) {
			size += res.getInt("size");
			
		}
		if (res != null)
			res.close();
		return size;
    }
    
    public static int getFilesCitySize(int projectid) throws SQLException {
    	int size = 3;
    	String sqlStmt = QueryBuilder.selectFilesCitySize(projectid);
    	ResultSet res = null;
		res = statement.executeQuery(sqlStmt);
		while (res.next()) {
			size += res.getInt("size");
			
		}
		if (res != null)
			res.close();
		return size;
    }

	public static Project getProjectByName(String projectName) throws SQLException {
    	boolean found = false;
    	Project aProject = null;
    	for(Project project : projects) {
    		if(project.getProjectName().equals(projectName)) {
    			aProject = project;
    			found = true;
    		}
    	}
    	if (!found) {
			String sqlStmt = QueryBuilder.selectProjectByName(projectName);
			ResultSet res = null;
			res = statement.executeQuery(sqlStmt);
			while (res.next()) {
				aProject = new Project(res.getInt("projectid"), res
						.getString("projectname"));
				
			}
			if (res != null)
				res.close();
			aProject.setUsers(getProjectUsers(aProject.getProjectID()));
			aProject.setSystem(getExternalSystemByProjectID(aProject.getProjectID()));
	    	projects.add(aProject);
			projectIDs.add(aProject.getProjectID());
			systems.add(aProject.getSystem());
		}
    	
    	return aProject;
    }
    
    public static Project getProjectByID(int projectID) throws SQLException {
    	Project aProject = null;
    	if (!projectIDs.contains(projectID)) {
			String sqlStmt = QueryBuilder.selectProjectByID(projectID);
			ResultSet res = null;
			res = statement.executeQuery(sqlStmt);
			while (res.next()) {
				aProject = new Project(res.getInt("projectid"), res
						.getString("projectname"));
			}
			if (res != null)
				res.close();
			aProject.setUsers(getProjectUsers(projectID));
			aProject.setSystem(getExternalSystemByProjectID(projectID));
	    	projects.add(aProject);
			projectIDs.add(aProject.getProjectID());
			systems.add(aProject.getSystem());
		}
    	else {
    		for(Project project : projects) {
    			if(project.getProjectID() == projectID) {
    				aProject = project;
    			}
    		}
    	}
    	return aProject;
    }
    
    public static Project getProjectByExternalSystem(int system) throws SQLException {
    	Project aProject = null;
    	int projectid = 0;
    	if (!systems.contains(system)) {
			String sqlStmt = QueryBuilder.selectProjectIDByExternalSystem(system);
			ResultSet res = null;
			res = statement.executeQuery(sqlStmt);
			while (res.next()) {
				projectid = res.getInt("project");
			}
			if (res != null)
				res.close();
			aProject = getProjectByID(projectid);
			aProject.setUsers(getProjectUsers(projectid));
			aProject.setSystem(system);
	    	projects.add(aProject);
			projectIDs.add(aProject.getProjectID());
			systems.add(system);
		}
    	else {
    		for(Project project : projects) {
    			if(project.getSystem() == system) {
    				aProject = project;
    			}
    		}
    	}
    	return aProject;
    }
    
    public static int getExternalSystemByProjectID(int projectid) throws SQLException {
    	int system=0;
    	String sqlStmt = QueryBuilder.selectExternalSystemByProjectID(projectid);
    	ResultSet res = null;
    	res = statement.executeQuery(sqlStmt);
    	while(res.next()) {
    		system = res.getInt("externalsystem");
    	}
    	if(res != null) res.close();
    	return system;
    }
    
    public static User getUserByID(int userID) throws SQLException {
    	User user = null;
    	if (!userIDs.contains(userID)) {
			String sqlStmt = QueryBuilder.selectUserByID(userID);
			ResultSet res = null;
			res = statement.executeQuery(sqlStmt);
			while (res.next()) {
				user = new User(res.getInt("user_id"), res
						.getString("user_name"), res
						.getString("user_real_name"));

			}
			if (res != null)
				res.close();
			users.add(user);
			userIDs.add(userID);
			usernames.add(user.getUserName());
		}
    	else {
    		for(User aUser : users) {
    			if(aUser.getUserID() == userID) {
    				user = aUser;
    			}
    		}
    	}
    	return user;
    }
    
    public static Ticket getTicketByDrID(int project, int ticketid) throws SQLException {
    	Ticket ticket = null;
    	if (!ticketids.containsKey(ticketid) || ticketids.get(ticketid) != null) {
			String sqlStmt = QueryBuilder.selectTicketByDrID(ticketid, project);
			ResultSet res = null;
			res = statement.executeQuery(sqlStmt);
			while (res.next()) {
				ticket = new Ticket(res.getTimestamp("created"), res
						.getString("description"), res
						.getTimestamp("last_modified"), res.getString("owner_name"), res.getString("reporter"), res.getString("summary"),
						res.getInt("id"), res.getInt("project_id"), res.getInt("project_ticket_id"), res.getString("priority"));

			}
			if (res != null)
				res.close();
			ticket.setOwner(getUserByUserName(ticket.getOwnerName()));
			ticket.setReporter(getUserByUserName(ticket.getReporterName()));
			ticket.setTicketChanges(getTicketChangesByTicketID(ticket
					.getTicketID()));
			ticket.setProject(getProjectByID(ticket.getProjectID()));
			ticketids.put(ticket.getDr_id(), ticket.getProjectID());
			allArtifacts.add(ticket);
		}
    	else {
    		for(Ticket aTicket : tickets) {
    			if(aTicket.getDr_id() == ticketid && aTicket.getProjectID() == project) {
    				ticket = aTicket;
    			}
    		}
    	}
    	return ticket;
    }
    
    public static ArrayList<User> getUsersPerProject(int projectid) throws SQLException {
    	ArrayList<User> users = new ArrayList<User>();
    	ArrayList<String> userNames = new ArrayList<String>();
    	String sqlStmt = QueryBuilder.selectUsersPerProject(projectid);
    	ResultSet res = null;
    	res = statement.executeQuery(sqlStmt);
    	while(res.next()) {
    		userNames.add(res.getString("userid"));
    	}
    	if(res != null) res.close();
    	for(String username : userNames) {
    		users.add(getUserByUserName(username));
    	}
    	return users;
    }
    
    public static User getUserByUserName(String userName) throws SQLException {
    	User user = null;
    	userName = userName.toLowerCase();
    	if (!usernames.contains(userName)) {
			String sqlStmt = QueryBuilder.selectUserByUserName(userName);
			ResultSet res = null;
			res = statement.executeQuery(sqlStmt);
			while (res.next()) {

				user = new User(res.getInt("user_id"), res
						.getString("user_name").toLowerCase(), res
						.getString("user_real_name"));
			}
			if (res != null)
				res.close();
			if (user != null) {
				users.add(user);
				userIDs.add(user.getUserID());
				usernames.add(userName);
			}
			else {
				user = new User(userName);
				users.add(user);
				userIDs.add(user.getUserID());
				usernames.add(userName);
			}
		}
    	else {
    		for(User aUser : users) {
    			if(aUser.getUserName().equalsIgnoreCase(userName)) {
    				user = aUser;
    				break;
    			}
    		}
    	}
			return user;
    }
    
    public static ArrayList<IArtifact> getArtifactsAfterTimestamp(String timestamp) throws SQLException {
    	ArrayList<IArtifact> artifacts = new ArrayList<IArtifact>();
    	ArrayList<IArtifact> finalArtifacts = new ArrayList<IArtifact>();
    	IArtifact artifact = null;
    	for (int i = 0; i < entityTypes.length; i++) {
    		String entityType = entityTypes[i];
			String sqlStmt = QueryBuilder.selectArtifactsAfterTimestamp(entityType, timestamp);
			ResultSet res = null;
			res = statement.executeQuery(sqlStmt);
			while (res.next()) {
				if (entityType.equals("wikidev_changesets")) {
					artifact = new ChangeSet(res.getInt("id"), res
							.getString("comment"), res
							.getString("externalauthor"), res
							.getInt("externalsystem_id"), res
							.getTimestamp("timestamp"), res.getInt("revision"),
							res.getInt("project"));
					artifacts.add(artifact);
				} else if (entityType.equals("wikidev_tickets")) {
					artifact = new Ticket(res.getTimestamp("created"), res
							.getString("description"), res
							.getTimestamp("last_modified"), res.getString("owner_name"), res.getString("reporter"), 
							res.getString("summary"),
							res.getInt("id"), res.getInt("project_id"), res.getInt("project_ticket_id"), res.getString("priority"));
					artifacts.add(artifact);
				} else if (entityType.equals("mw_page")) {
					artifact = new Wiki(res.getInt("page_id"), res.getInt("page_namespace"), res.getString("page_title"), res.getTimestamp("page_touched"));
					artifacts.add(artifact);
				}
				else if(entityType.equals("wikidev_messages")) {
                	artifact = new Message(res.getString("address"), res.getString("author"), res.getString("body"), res.getInt("id"),
							res.getInt("deleted"), res.getString("mid_header"), 
							res.getInt("project_id"),  res.getString("subject"), res.getTimestamp("date"), res.getString("user_name"), res.getString("refid_header"));;
                	artifacts.add(artifact);
                }
			}
			if(res !=null) res.close();
			
		}
    	for (IArtifact anArtifact : artifacts) {
			if (anArtifact instanceof ChangeSet) {
				ChangeSet changeSet = (ChangeSet)anArtifact;
				changeSet.setOwner(getUserByUserName(changeSet.getExternalAuthorUserName()));
				changeSet.setProject(getProjectByID(changeSet.getExternalSystemID()));
				changeSet.setChangesetDetails(getChangeSetDetailsByRevAndProject(changeSet.getRevision(), changeSet.getExternalSystemID()));
				changeSet.setAuthorForChangeSetDetails();
				finalArtifacts.add(changeSet);
			} else if (anArtifact instanceof Ticket) {
				Ticket ticket = (Ticket)anArtifact;
				ticket.setOwner(getUserByUserName(ticket.getOwnerName()));
				ticket.setReporter(getUserByUserName(ticket.getReporterName()));
				ticket.setTicketChanges(getTicketChangesByTicketID(ticket
						.getTicketID()));
				ticket.setProject(getProjectByID(ticket.getProjectID()));
				ticketids.put(ticket.getDr_id(), ticket.getProjectID());
				tickets.add(ticket);
				finalArtifacts.add(ticket);
			} else if (anArtifact instanceof Message) {
				Message communication = (Message)anArtifact;
				User user = getUserByUserName(communication
						.getUser_name());
				communication.setOwner(user);
				user.setUserRealName(communication.getAuthor());
				communication.setProject(getProjectByID(communication.getProject_id()));
				finalArtifacts.add(communication);
			}
			else if(anArtifact instanceof Wiki) {
            	Wiki wiki = (Wiki)anArtifact;
            	wiki.setRevisions(getRevisionsByWikiID(wiki.getPage_id()));
            	wiki.setProject(DataManager.getProjectByName(getProjectNamespaceByNamespaceID(wiki.getPage_namespace_id())));
            }
		}
    	allArtifacts.addAll(finalArtifacts);
    	return finalArtifacts;
    }

	private static String getProjectNamespaceByNamespaceID(int page_namespace_id) throws SQLException {
		String namespace = "";
		String sqlStmt = QueryBuilder.selectProjectNamespaceByNamespaceID(page_namespace_id);
		ResultSet res = null;
		res = statement.executeQuery(sqlStmt);
		while(res.next()) {
			namespace = res.getString("nsName");
		}
		if(res != null) res.close();
		return namespace;
	}
	
	private static String getProjectNamespaceByID(int projectid) throws SQLException {
		String namespace = "";
		String sqlStmt = QueryBuilder.selectProjectNamespaceByID(projectid);
		ResultSet res = null;
		res = statement.executeQuery(sqlStmt);
		while(res.next()) {
			namespace = res.getString("projectname");
		}
		if(res != null) res.close();
		return namespace;
	}
	
	private static int getProjectNamespaceIDByProjectID(int projectid) throws SQLException {
		int id = 0;
		String namespace = getProjectNamespaceByID(projectid);
		String sqlStmt = QueryBuilder.selectProjectNamespaceIDbyNamespace(namespace);
		ResultSet res = null;
		res = statement.executeQuery(sqlStmt);
		while(res.next()) {
			id = res.getInt("nsId");
		}
		if(res != null) res.close();
		return id;
	}

	private static ArrayList<WikiRevision> getRevisionsByWikiID(int page_id) throws SQLException {
    	ArrayList<WikiRevision> revisions = new ArrayList<WikiRevision>();
    	String sqlStmt = QueryBuilder.selectRevisionsByWikiID(page_id);
		ResultSet res = null;
		res = statement.executeQuery(sqlStmt);
		while (res.next()) {
			WikiRevision wikiRevision = new WikiRevision(res.getInt("rev_id"), res.getInt("rev_page"), res.getInt("rev_text_id")
					, res.getString("rev_user_text"), res.getTimestamp("rev_timestamp"));
			revisions.add(wikiRevision);
		}
		if(res != null) res.close();
		for(WikiRevision revision : revisions) {
			revision.setRev_user(getUserByUserName(revision.getRev_user_text()));
			revision.setRev_text(getRevisionTextByRevisionID(revision.getRev_id()));
		}
		return revisions;
	}

	private static String getRevisionTextByRevisionID(int rev_id) throws SQLException {
		String text = "";
		String sqlStmt = QueryBuilder.selectRevisionTextByRevisionID(rev_id);
		ResultSet res = null;
		res = statement.executeQuery(sqlStmt);
		while (res.next()) {
			text = res.getString(1);
		}
		if(res != null) res.close();
		return text;
	}

	public static ArrayList<ChangeSet> getAllChangesetsForProject(int system) throws SQLException {
    	ArrayList<ChangeSet> changesets = new ArrayList<ChangeSet>();
    	Project project = getProjectByExternalSystem(system);
    	ChangeSet aChangeset = null;
    	String sqlStmt = QueryBuilder.selectAllChangesetsForProject(system);
    	ResultSet res = null;
    	res = statement.executeQuery(sqlStmt);
    	while(res.next()) {
    		aChangeset = new ChangeSet(res.getInt("id"), res
					.getString("comment"), res
					.getString("externalauthor"), res
					.getInt("externalsystem_id"), res
					.getTimestamp("timestamp"), res.getInt("rev"),
					res.getInt("project"));
    		changesets.add(aChangeset);
    		DataManager.changesets.add(aChangeset);
    	}
    	if(res != null) res.close();
    	
    	for(ChangeSet changeset : changesets) {
    		changeset.setOwner(getUserByUserName(changeset.getExternalAuthorUserName()));
			changeset.setProject(project);
			changeset.setChangesetDetails(getChangeSetDetailsByRevAndProject(changeset.getRevision(), changeset.getExternalSystemID()));
			changeset.setAuthorForChangeSetDetails();
			project.addChangeset(changeset);
    	}
    	return changesets;
    }
	
	private static String removeChar(String s, char c) {
		   String r = "";
		   for (int i = 0; i < s.length(); i ++) {
		      if (s.charAt(i) != c) r += s.charAt(i);
		      }
		   return r;
		}
    
    public static ArrayList<IArtifact> getArtifactsBetweenDates(String timestamp1, String timestamp2, int projectid) throws SQLException {
    	ArrayList<IArtifact> artifacts = new ArrayList<IArtifact>();
    	ArrayList<IArtifact> finalArtifacts = new ArrayList<IArtifact>();
    	Project project = getProjectByID(projectid);
    	int namespaceID = DataManager.getProjectNamespaceIDByProjectID(projectid);
    	for(ChangeSet changeset : changesets) {
    		if(changeset.getTimestamp().compareTo(Timestamp.valueOf(timestamp1))>=0 && changeset.getTimestamp().compareTo(Timestamp.valueOf(timestamp2))<=0) {
    			finalArtifacts.add(changeset);
    		}
    	}
    	IArtifact artifact = null;
    	for (int i = 0; i < entityTypes.length; i++) {
    		String entityType = entityTypes[i];
    		String nullString = "";
    		if(entityType.equals("mw_page")) {
    			timestamp1 = removeChar(timestamp1, '-');
    			timestamp1 = removeChar(timestamp1, ' ');
    			timestamp1 = removeChar(timestamp1, ':');
    			timestamp2 = removeChar(timestamp2, '-');
    			timestamp2 = removeChar(timestamp2, ' ');
    			timestamp2 = removeChar(timestamp2, ':');
    		}
			String sqlStmt = QueryBuilder.selectArtifactsForProjectBetweenDates(entityType, timestamp1, timestamp2, projectid, namespaceID);
			ResultSet res = null;
			res = statement.executeQuery(sqlStmt);
			while (res.next()) {
				if (entityType.equals("wikidev_changesets")) {
					artifact = new ChangeSet(res.getInt("id"), res
							.getString("comment"), res
							.getString("externalauthor"), res
							.getInt("externalsystem_id"), res
							.getTimestamp("timestamp"), res.getInt("rev"),
							res.getInt("project_id"));
					artifacts.add(artifact);
				} else if (entityType.equals("wikidev_tickets")) {
					artifact = new Ticket(res.getTimestamp("created"), res
							.getString("description"), res
							.getTimestamp("last_modified"), res.getString("owner_name"), res.getString("reporter"), 
							res.getString("summary"),
							res.getInt("id"), res.getInt("project_id"), res.getInt("project_ticket_id"), res.getString("priority"));
					artifacts.add(artifact);
				} else if (entityType.equals("mw_page")) {
					artifact = new Wiki(res.getInt("page_id"), res.getInt("page_namespace"), res.getString("page_title"), res.getTimestamp("page_touched"));
					artifacts.add(artifact);
				}
				else if(entityType.equals("wikidev_messages")) {
                	artifact = new Message(res.getString("address"), res.getString("author"), res.getString("body"), res.getInt("id"),
							res.getInt("deleted"), res.getString("mid_header"), 
							res.getInt("project_id"),  res.getString("subject"), res.getTimestamp("date"), res.getString("user_name"), res.getString("refid_header"));;
                	artifacts.add(artifact);
                }
			}
			if(res !=null) res.close();
			
		}
    	for (IArtifact anArtifact : artifacts) {
    		if (anArtifact instanceof ChangeSet) {
				ChangeSet changeSet = (ChangeSet)anArtifact;
				changeSet.setOwner(getUserByUserName(changeSet.getExternalAuthorUserName()));
				changeSet.setProject(project);
				changeSet.setChangesetDetails(getChangeSetDetailsByRevAndProject(changeSet.getRevision(), changeSet.getExternalSystemID()));
				for(ChangeSetDetail detail : changeSet.getChangesetDetails()) {
					detail.setTimestamp(changeSet.getTimestamp());
				}
				changeSet.setAuthorForChangeSetDetails();
				finalArtifacts.add(changeSet);
				project.addChangeset(changeSet);
			} else if (anArtifact instanceof Ticket) {
				Ticket ticket = (Ticket)anArtifact;
				ticket.setOwner(getUserByUserName(ticket.getOwnerName()));
				ticket.setReporter(getUserByUserName(ticket.getReporterName()));
				ticket.setTicketChanges(getTicketChangesByTicketID(ticket
						.getTicketID()));
				ticket.setProject(project);
				project.addTicket(ticket);
				ticketids.put(ticket.getDr_id(), ticket.getProjectID());
				tickets.add(ticket);
				finalArtifacts.add(ticket);
			} else if (anArtifact instanceof Message) {
				Message communication = (Message)anArtifact;
				User user = getUserByUserName(communication
						.getUser_name());
				communication.setOwner(user);
				user.setUserRealName(communication.getAuthor());
				communication.setProject(project);
				project.addMessage(communication);
				finalArtifacts.add(communication);
			}
			else if(anArtifact instanceof Wiki) {
            	Wiki wiki = (Wiki)anArtifact;
            	wiki.setRevisions(getRevisionsByWikiID(wiki.getPage_id()));
            	wiki.setOwner(wiki.getRevisions().get(0).getRev_user());
            	wiki.setProject(DataManager.getProjectByName(getProjectNamespaceByNamespaceID(wiki.getPage_namespace_id())));
            	wiki.setProjectID(wiki.getProject().getProjectID());
            	finalArtifacts.add(wiki);
            }
		}
    	allArtifacts.addAll(finalArtifacts);
    	return finalArtifacts;
    }
    
    public static ArrayList<IArtifact> getArtifactsForProject(int projectid) throws SQLException {
    	ArrayList<IArtifact> artifacts = new ArrayList<IArtifact>();
    	ArrayList<IArtifact> finalArtifacts = new ArrayList<IArtifact>();
    	Project project = getProjectByID(projectid);
    	IArtifact artifact = null;
    	for (int i = 0; i < entityTypes.length; i++) {
    		String entityType = entityTypes[i];
			String sqlStmt = QueryBuilder.selectArtifactsForProject(entityType, projectid);
			ResultSet res = null;
			res = statement.executeQuery(sqlStmt);
			while (res.next()) {
				if (entityType.equals("wikidev_changesets")) {
					artifact = new ChangeSet(res.getInt("id"), res
							.getString("comment"), res
							.getString("externalauthor"), res
							.getInt("externalsystem_id"), res
							.getTimestamp("timestamp"), res.getInt("rev"),
							res.getInt("project"));
					artifacts.add(artifact);
				} else if (entityType.equals("wikidev_tickets")) {
					artifact = new Ticket(res.getTimestamp("created"), res
							.getString("description"), res
							.getTimestamp("last_modified"), res.getString("owner_name"), res.getString("reporter"), 
							res.getString("summary"),
							res.getInt("id"), res.getInt("project_id"), res.getInt("project_ticket_id"), res.getString("priority"));
					artifacts.add(artifact);
				} else if (entityType.equals("mw_wiki")) {
					artifact = new Wiki(res.getInt("page_id"), res.getInt("page_namespace"), res.getString("page_title"), res.getTimestamp("page_touched"));
					artifacts.add(artifact);
				}
				else if(entityType.equals("wikidev_messages")) {
                	artifact = new Message(res.getString("address"), res.getString("author"), res.getString("body"), res.getInt("id"),
							res.getInt("deleted"), res.getString("mid_header"), 
							res.getInt("project_id"),  res.getString("subject"), res.getTimestamp("date"), res.getString("user_name"), res.getString("refid_header"));;
                	artifacts.add(artifact);
                }
			}
			if(res !=null) res.close();
			
		}
    	for (IArtifact anArtifact : artifacts) {
			if (anArtifact instanceof ChangeSet) {
				ChangeSet changeSet = (ChangeSet)anArtifact;
				changeSet.setOwner(getUserByUserName(changeSet.getExternalAuthorUserName()));
				changeSet.setProject(project);
				changeSet.setChangesetDetails(getChangeSetDetailsByRevAndProject(changeSet.getRevision(), changeSet.getExternalSystemID()));
				changeSet.setAuthorForChangeSetDetails();
				finalArtifacts.add(changeSet);
				project.addChangeset(changeSet);
			} else if (anArtifact instanceof Ticket) {
				Ticket ticket = (Ticket)anArtifact;
				ticket.setOwner(getUserByUserName(ticket.getOwnerName()));
				ticket.setReporter(getUserByUserName(ticket.getReporterName()));
				ticket.setTicketChanges(getTicketChangesByTicketID(ticket
						.getTicketID()));
				ticket.setProject(project);
				project.addTicket(ticket);
				ticketids.put(ticket.getDr_id(), ticket.getProjectID());
				tickets.add(ticket);
				finalArtifacts.add(ticket);
			} else if (anArtifact instanceof Message) {
				Message communication = (Message)anArtifact;
				User user = getUserByUserName(communication
						.getUser_name());
				communication.setOwner(user);
				user.setUserRealName(communication.getAuthor());
				communication.setProject(project);
				project.addMessage(communication);
				finalArtifacts.add(communication);
			}
			else if(anArtifact instanceof Wiki) {
            	Wiki wiki = (Wiki)anArtifact;
            	wiki.setRevisions(getRevisionsByWikiID(wiki.getPage_id()));
            	wiki.setProject(DataManager.getProjectByName(getProjectNamespaceByNamespaceID(wiki.getPage_namespace_id())));
            	finalArtifacts.add(wiki);
            }
		}
    	allArtifacts.addAll(finalArtifacts);
    	return finalArtifacts;
    }
    
    public static ArrayList<TicketChange> getTicketChangesByTicketID(int ticketID) throws SQLException {
    	ArrayList<TicketChange> ticketChanges = new ArrayList<TicketChange>();
    	String sqlStmt = QueryBuilder.selectTicketChangesByTicketID(ticketID);
		ResultSet res = null;
		res = statement.executeQuery(sqlStmt);
		while (res.next()) {
			TicketChange ticketChange = new TicketChange(res.getString("author_name"), res.getString("field_changed"), res.getInt("id")
					, res.getString("new_value"), res.getString("old_value"), res.getInt("project_ticket_id"), res.getInt("ticket_id"), res.getInt("project_id"), res.getTimestamp("change_time"));
			ticketChanges.add(ticketChange);
		}
		if(res != null) res.close();
		for(TicketChange ticketChange : ticketChanges) {
			ticketChange.setExternalAuthor(getUserByUserName(ticketChange.getExternalAuthorUsername()));
		}
		return ticketChanges;
    }
    
    public static ArrayList<ChangeSetDetail> getChangeSetDetailsByRevAndProject(int rev, int externalsystem_id) throws SQLException {
    	ArrayList<ChangeSetDetail> changeSetDetails = new ArrayList<ChangeSetDetail>();
    	String sqlStmt = QueryBuilder.selectChangeSetDetailByRevAndProject(rev, externalsystem_id);
		ResultSet res = null;
		res = statement.executeQuery(sqlStmt);
		while (res.next()) {
			ChangeSetDetail changeSetDetail = new ChangeSetDetail(res.getString("changetype"), res.getInt("externalsystem_id"), res.getString("filename"), res.getInt("id"), 
					res.getString("kind"), res.getInt("linesadded"), res.getInt("lineschanged"), res.getInt("linesremoved"), res.getString("props"), res.getInt("rev"));
			changeSetDetails.add(changeSetDetail);
		}
		if(res != null) res.close();
		return changeSetDetails;
    }
    
    private static Set<User> getProjectUsers(int projectID) throws SQLException {
    	Set<String> usersID = new HashSet<String>();
    	Set<User> users = new HashSet<User>();
    	String sqlStmt = QueryBuilder.selectProjectUsers(projectID);
    	ResultSet res = null;
    	res = statement.executeQuery(sqlStmt);
    	while(res.next()) {
    		usersID.add(res.getString("userid"));
    	}
    	if(res != null) res.close();
    	for (String userID : usersID) {
			users.add(getUserByUserName(userID));
		}
		return users;
    }
    
}
