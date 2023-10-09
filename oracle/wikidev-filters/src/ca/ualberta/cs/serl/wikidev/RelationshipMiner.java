 package ca.ualberta.cs.serl.wikidev;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import ca.ualberta.cs.serl.wikidev.artifacts.ChangeSet;
import ca.ualberta.cs.serl.wikidev.artifacts.Message;
import ca.ualberta.cs.serl.wikidev.artifacts.IArtifact;
import ca.ualberta.cs.serl.wikidev.artifacts.Ticket;
import ca.ualberta.cs.serl.wikidev.artifacts.Wiki;
import ca.ualberta.cs.serl.wikidev.artifacts.WikiRevision;
import ca.ualberta.cs.serl.wikidev.clustering.MatrixOperator;

/**
 * @author   User
 */
public class RelationshipMiner {
	
	/**
	 * @uml.property  name="projectsChanged"
	 */
	private Set<Project> projectsChanged;
	/**
	 * @uml.property  name="artifactsChanged"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="ca.ualberta.cs.serl.wikidev.artifacts.IArtifact"
	 */
	private Set<IArtifact> artifactsChanged;
	/**
	 * @uml.property  name="usersChanged"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="ca.ualberta.cs.serl.wikidev.User"
	 */
	private Set<User> usersChanged;
	
	/**
	 * @uml.property  name="sourceArtifact"
	 * @uml.associationEnd  
	 */
	private IArtifact sourceArtifact;
	/**
	 * @uml.property  name="project"
	 * @uml.associationEnd  
	 */
	private Project project;
	/**
	 * @uml.property  name="documents"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="ca.ualberta.cs.serl.wikidev.Document"
	 */
	private ArrayList<Document> documents;
	/**
	 * @uml.property  name="changesets"
	 */
	//private ArrayList<ChangeSet> changesets;
	/**
	 * @uml.property  name="artifacts"
	 */
	private ArrayList<IArtifact> artifacts;
	
	public RelationshipMiner(ArrayList<IArtifact> artifacts) {
		this.projectsChanged = new HashSet<Project>();
		this.artifactsChanged = new HashSet<IArtifact>();
		this.usersChanged = new HashSet<User>();
		this.documents = new ArrayList<Document>();
		this.artifacts = artifacts;
	}
	
	/**
	 * @return
	 * @uml.property  name="projectsChanged"
	 */
	public Set<Project> getProjectsChanged() {
		return projectsChanged;
	}

	/**
	 * @return
	 * @uml.property  name="artifactsChanged"
	 */
	public Set<IArtifact> getArtifactsChanged() {
		return artifactsChanged;
	}

	/**
	 * @return
	 * @uml.property  name="usersChanged"
	 */
	public Set<User> getUsersChanged() {
		return usersChanged;
	}

	public void getRelationships() throws SQLException {
		ArrayList<String> buffers = new ArrayList<String>();
		for(IArtifact artifact : artifacts) {
			
			if(artifact instanceof Ticket) {
				TextParser parser = new TextParser();
				Ticket ticket = (Ticket)artifact;
				parser.parseTextInWords(ticket.getDesription().toLowerCase());
				parser.parseTextInWords(ticket.getSummary().toLowerCase());
				documents.add(new Document(parser.getWords(), ticket.toString()));
				ticket.setDocument(new Document(parser.getWords(), ticket.toString()));
			}
			else if(artifact instanceof ChangeSet) {
				TextParser parser = new TextParser();
				ChangeSet changeset = (ChangeSet)artifact;
				//relateChangeSetToTicket(changeset);
				parser.parseTextInWords(changeset.getComment().toLowerCase());
				documents.add(new Document(parser.getWords(), changeset.toString()));
				changeset.setDocument(new Document(parser.getWords(), changeset.toString()));
			}
			else if (artifact instanceof Message) {
				TextParser parser = new TextParser();
				Message communication = (Message)artifact;
				parser.parseTextInWords(communication.getSubject().toLowerCase());
				parser.parseTextInWords(communication.getBody().toLowerCase());
				documents.add(new Document(parser.getWords(), communication.toString()));
				communication.setDocument(new Document(parser.getWords(), communication.toString()));
			}
			else if (artifact instanceof Wiki) {
				Wiki wiki = (Wiki)artifact;
					TextParser parser = new TextParser();
					String text = "";
					for (WikiRevision revision : wiki.getRevisions()) {
						text += revision.getRev_text();
					}
					parser.parseTextInWords(text.toLowerCase());
					documents.add(new Document(parser.getWords(), wiki.toString()));
					wiki.setDocument(new Document(parser.getWords(), wiki.toString()));
			}
		}
		
		for(IArtifact artifact : artifacts) {
			
			if(artifact instanceof Ticket) {
				TextParser parser = new TextParser(documents);
				Ticket ticket = (Ticket)artifact;
				parser.parseTextInWords(ticket.getDesription().toLowerCase());
				parser.parseTextInWords(ticket.getSummary().toLowerCase());
				ticket.getDocument().setTfidf(parser.calculateTFIDF());
			}
			else if(artifact instanceof ChangeSet) {
				TextParser parser = new TextParser(documents);
				ChangeSet changeset = (ChangeSet)artifact;
				parser.parseTextInWords(changeset.getComment().toLowerCase());
				changeset.getDocument().setTfidf(parser.calculateTFIDF());
			}
			else if (artifact instanceof Message) {
				TextParser parser = new TextParser(documents);
				Message communication = (Message)artifact;
				parser.parseTextInWords(communication.getSubject().toLowerCase());
				parser.parseTextInWords(communication.getBody().toLowerCase());
				communication.getDocument().setTfidf(parser.calculateTFIDF());
			}
			else if (artifact instanceof Wiki) {
				Wiki wiki = (Wiki)artifact;
					TextParser parser = new TextParser(documents);
					String text = "";
					for (WikiRevision revision : wiki.getRevisions()) {
						text += revision.getRev_text();
					}
					parser.parseTextInWords(text.toLowerCase());
					wiki.getDocument().setTfidf(parser.calculateTFIDF());
			}
		}
		
		for(IArtifact artifact : artifacts) {
			System.out.println("Relationships: "+artifact.toString());
			
			sourceArtifact = artifact;
			project = artifact.getProject();
			getGenericRelationships(sourceArtifact);
			if(sourceArtifact instanceof Ticket) {
				TextParser parser = new TextParser(documents);
				Ticket ticket = (Ticket)sourceArtifact;
				parser.parseTextInWords(ticket.getDesription().toLowerCase());
				buffers.add(ticket.getDesription().toLowerCase());
				parser.parseTextInWords(ticket.getSummary().toLowerCase());
				buffers.add(ticket.getSummary().toLowerCase());
				mineRelationshipsFromText();
				/*for(URL url : parser.getUrls()) {
					mineRelationshipsFromURL(url);
				}*/
			}
			else if(sourceArtifact instanceof ChangeSet) {
				TextParser parser = new TextParser(documents);
				ChangeSet changeset = (ChangeSet)sourceArtifact;
				//relateChangeSetToTicket(changeset);
				parser.parseTextInWords(changeset.getComment().toLowerCase());
				buffers.add(changeset.getComment().toLowerCase());
				mineRelationshipsFromText();
				/*for(URL url : parser.getUrls()) {
					mineRelationshipsFromURL(url);
				}*/
				
			}
			else if (sourceArtifact instanceof Message) {
				TextParser parser = new TextParser(documents);
				Message communication = (Message)sourceArtifact;
				parser.parseTextInWords(communication.getSubject().toLowerCase());
				buffers.add(communication.getSubject().toLowerCase());
				parser.parseTextInWords(communication.getBody().toLowerCase());
				buffers.add(communication.getBody().toLowerCase());
				mineRelationshipsFromText();
				/*for(URL url : parser.getUrls()) {
					mineRelationshipsFromURL(url);
				}*/
			}
			else if (sourceArtifact instanceof Wiki) {
				Wiki wiki = (Wiki)sourceArtifact;
					TextParser parser = new TextParser(documents);
					String text = "";
					for (WikiRevision revision : wiki.getRevisions()) {
						text += revision.getRev_text();
					}
					parser.parseTextInWords(text.toLowerCase());
					buffers.add(text.toLowerCase());
					mineRelationshipsFromText();
			}
		}
		//saveResults(buffers, project.toString());
		//System.out.println("done");
	}
	
	private double getCosineDistance(IArtifact artifact1, IArtifact artifact2) {
		TreeMap<String, Double> unified = new TreeMap<String, Double>();
		unified.putAll(artifact1.getDocument().getTfidf());
		unified.putAll(artifact2.getDocument().getTfidf());
		int size = unified.keySet().size();
		double[] vector1 = new double[size];
		double[] vector2 = new double[size];
		int index=-1;
		for(String term : unified.keySet()) {
			index++;
			if(artifact1.getDocument().getTfidf().containsKey(term)) {
				vector1[index] = artifact1.getDocument().getTfidf().get(term);
			}
			else {
				vector1[index] = 0;
			}
			if(artifact2.getDocument().getTfidf().containsKey(term)) {
				vector2[index] = artifact2.getDocument().getTfidf().get(term);
			}
			else {
				vector2[index] = 0;
			}
		}
		double sum=0;
		double norm1=0;
		double norm2=0;
		for(int i=0; i<size; i++) {
			sum += vector1[i]*vector2[i];
			norm1 += Math.pow(vector1[i], 2);
			norm2 += Math.pow(vector2[i], 2);
		}
		double dis = 1-sum/(Math.sqrt(norm1)*Math.sqrt(norm2));
		return dis;
	}
	
	/*private void saveResults(ArrayList<String> buffers, String filename) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("C:\\Documents and Settings\\Marios Fokaefs\\Desktop\\wikidev-files\\"+filename+".txt"));
			for(String buffer : buffers) {
				out.write(buffer);
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/

	/*public void mineRelationshipsFromURL(URL url) throws SQLException {
		
		String projectName = url.getProjectName();
		Project project = DataManager.getProjectByName(projectName);
		String entityType = url.getEntityType();
		IArtifact artifact = DataManager.getArtifactByURL(entityType, url.getUrl());
		project.addRelatedArtifact(artifact);
		projectsChanged.add(project);
		relateArtifacts(sourceArtifact, artifact);
	}*/
	
	public void relateChangeSetToTicket(ChangeSet changeset) throws SQLException {
		ArrayList<String> ids = new ArrayList<String>();
		String comment = changeset.getComment();
		String ticketid = "";
		String pattern = "[0-9]";
		for(int i=0;i<comment.length(); i++) {
			if(comment.charAt(i) == '#') {
				int j=i+1;
				String ch = comment.substring(j, j+1);
				while(ch.matches(pattern)) {
					ticketid += ch;
					j++;
					if (j<comment.length()) {
						ch = comment.substring(j, j + 1);
					}
					else {
						break;
					}
				}
				ids.add(ticketid);
				ticketid = "";
			}
		}
		for (String ticket_id : ids) {
			if (!ticket_id.equals("")) {
				int id = Integer.parseInt(ticket_id);
				Project project = DataManager.getProjectByExternalSystem(changeset.getExternalSystemID());
				Ticket ticket = DataManager.getTicketByDrID(project.getProjectID(), id);
				if (ticket != null) {
					relateArtifacts(ticket, changeset, 0, "<TicketReference>");
				}
			}
		}
	}
	
	/*private boolean containsArtifact(IArtifact artifact1, IArtifact artifact2) {
		if (!artifact1.equals(artifact2)) {
			if (!artifact1.getRelatedArtifacts().isEmpty()) {
				if (artifact1.getRelatedArtifacts().containsKey(artifact2)) {
					if (artifact1.getRelatedArtifacts().get(artifact2) == 0) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		else {
			return true;
		}
	}*/
	
	private boolean containsArtifact(User user, IArtifact artifact) {
		if(!user.getRelatedArtifacts().isEmpty()) {
			if(user.getRelatedArtifacts().containsKey(artifact)) {
				if(user.getRelatedArtifacts().get(artifact) == 0) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	private void relateArtifacts(IArtifact artifact1, IArtifact artifact2, double value, String word) {
		artifact1.addDirectlyRelatedArtifact(artifact2, value, word);
		artifactsChanged.add(artifact1);
		notifyUsers(artifact1, artifact2);
		artifact2.addDirectlyRelatedArtifact(artifact1, value, word);
		artifactsChanged.add(artifact2);
		notifyUsers(artifact2, artifact1);
		//calculateTransitiveRelationship(artifact1, artifact2);
	}
	
	/*private void calculateTransitiveRelationship(IArtifact artifact1, IArtifact artifact2) {
		if(artifact1.getDirectlyRelatedArtifacts().containsKey(artifact2)) {
				for(IArtifact artifact : artifact1.getDirectlyRelatedArtifacts().keySet()) {
					if(!artifact2.getDirectlyRelatedArtifacts().containsKey(artifact) && !artifact2.equals(artifact)) {
						artifact2.addIndirectlyRelatedArtifact(artifact, artifact1);
						artifactsChanged.add(artifact2);
					}
					if(!artifact.getDirectlyRelatedArtifacts().containsKey(artifact2) && !artifact.equals(artifact2)) {
						artifact.addIndirectlyRelatedArtifact(artifact2, artifact1);
						artifactsChanged.add(artifact);
					}
					notifyUsers(artifact, artifact2);
				}
		}
		if(artifact2.getDirectlyRelatedArtifacts().containsKey(artifact1)) {
			for(IArtifact artifact : artifact2.getDirectlyRelatedArtifacts().keySet()) {
				if(!artifact1.getDirectlyRelatedArtifacts().containsKey(artifact) && !artifact1.equals(artifact)) {
					artifact1.addIndirectlyRelatedArtifact(artifact, artifact2);
					artifactsChanged.add(artifact1);
				}
				if(!artifact.getDirectlyRelatedArtifacts().containsKey(artifact1) && !artifact.equals(artifact1)) {
					artifact.addIndirectlyRelatedArtifact(artifact1, artifact2);
					artifactsChanged.add(artifact);
				}
				notifyUsers(artifact, artifact1);
			}
		}
	}*/
	
	private void notifyUsers(IArtifact artifact1, IArtifact artifact2) {
		for(User user : artifact1.getAssociatedUsers()) {
			if (user != null) {
				if (!containsArtifact(user, artifact2)) {
					user.addRelatedArtifacts(artifact2, 1);
					usersChanged.add(user);
				}
			}
		}
	}
	
	private void getGenericRelationships(IArtifact artifact) {
		for(User user : artifact.getAssociatedUsers()) {
			if (user != null) {
				if (!containsArtifact(user, artifact)) {
					user.addRelatedArtifacts(artifact, 0);
					usersChanged.add(user);
				}
			}
		}
		project.addRelatedArtifact(artifact);
		projectsChanged.add(project);
	}
	
	
	public void mineRelationshipsFromText() {
		Set<User> users = project.getUsers();
		for(String word : sourceArtifact.getDocument().getTerms()) {
			for(IArtifact artifact : artifacts) {
				if (!sourceArtifact.equals(artifact)) {
					for (String word2 : artifact.getDocument().getTerms()) {
						if (Dictionary.matchesWord(word, word2)) {
							relateArtifacts(
									sourceArtifact,
									artifact,
									getCosineDistance(sourceArtifact, artifact),
									word);
						}
					}
				}
			}
			/*if (!(sourceArtifact instanceof ChangeSet)) {
				for (ChangeSet changeset : project.getChangesets()) {
					for (ChangeSetDetail changesetdetail : changeset
							.getChangesetDetails()) {
						if (changesetdetail.getFile().endsWith("java")) {
							if (Dictionary.matchesWord(word, changesetdetail
									.getFile())) {
								relateArtifacts(sourceArtifact, changeset, getCosineDistance(sourceArtifact, changeset), word);
							}
						}
					}
				}
			}*/
			for(User user : users) {
				if(Dictionary.matchesPattern(word, Dictionary.getUnionPatternOfStrings(user.getAliases()))) {
					if(!containsArtifact(user, sourceArtifact)) {
						user.addRelatedArtifacts(sourceArtifact, 0);
						usersChanged.add(user);
					}
				}
			}
		}
	}
	
	public HashMap<IArtifact, HashMap<IArtifact, Double>> getSimilarityMap() {
		HashMap<IArtifact, HashMap<IArtifact, Double>> fullMap = new HashMap<IArtifact, HashMap<IArtifact, Double>>();
		for(IArtifact artifact1 : artifacts) {
			HashMap<IArtifact, Double> map = new HashMap<IArtifact, Double>();
			for(IArtifact artifact2 : artifacts) {
				if(artifact1.containsRelatedArtifact(artifact2)) {
					map.put(artifact2, artifact1.getRelatedArtifact(artifact2).getDistance());
				}
				else {
					map.put(artifact2, 1.0);
				}
			}
			fullMap.put(artifact1, map);
		}
		return fullMap;
	}
	
	public double[][] getSimilarityMatrix() {
		
		int i=0;
		int j=0;
		HashMap<IArtifact, HashMap<IArtifact,Double>> similarityMap = getSimilarityMap();		
		double[][] matrix = new double[similarityMap.size()][similarityMap.size()];
		for(IArtifact artifact : artifacts) {
			Map<IArtifact,Double> similarities = similarityMap.get(artifact);			
			for(IArtifact artifact2 : artifacts) {
				if(i==j) {
					matrix[i][j] = 0;
				}
				else {
					matrix[i][j] = similarities.get(artifact2);
					matrix[j][i] = similarities.get(artifact2);
				}
				j++;
			}
			i++;
			j=0;
		}
		return matrix;
	}
	
	public double[][] getDistanceMatrix() {
		double[][] distanceMatrix = getSimilarityMatrix();
		for (int l = 0; l < 10; l++) {
			for (int j = 0; j < distanceMatrix.length; j++) {
				double vector[] = MatrixOperator.getColumn(distanceMatrix, j);
				for (int i = 0; i < distanceMatrix.length; i++) {
					for (int k = i + 1; k < distanceMatrix.length; k++) {
						if (vector[i] != 0 && vector[k] != 0) {
							double value = vector[i] + vector[k];
							if (value < distanceMatrix[i][k]) {
								distanceMatrix[i][k] = value;
								distanceMatrix[k][i] = value;
							}
						}
					}
				}
			}
		}
		return distanceMatrix;
	}
	
	public void printResults(int project) {
		double[][] distanceMatrix = getSimilarityMatrix();
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("C:\\Documents and Settings\\Marios Fokaefs\\Desktop\\wikidev-files\\ucosp\\results.txt"));
			out.write("x=c(");
			for(int i=0; i<distanceMatrix.length; i++) {
				for(int j=0; j<distanceMatrix.length; j++) {
					out.write(distanceMatrix[i][j]+", ");
				}
			}
			out.write(")");
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
