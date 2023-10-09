package ca.ualberta.cs.serl.wikidev.city3d;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import ca.ualberta.cs.serl.wikidev.DataManager;
import ca.ualberta.cs.serl.wikidev.RelationshipMiner;
import ca.ualberta.cs.serl.wikidev.User;
import ca.ualberta.cs.serl.wikidev.artifacts.ChangeSet;
import ca.ualberta.cs.serl.wikidev.artifacts.IArtifact;
import ca.ualberta.cs.serl.wikidev.artifacts.Message;
import ca.ualberta.cs.serl.wikidev.artifacts.SVNFile;
import ca.ualberta.cs.serl.wikidev.artifacts.Ticket;
import ca.ualberta.cs.serl.wikidev.artifacts.TicketChange;
import ca.ualberta.cs.serl.wikidev.artifacts.Wiki;
import ca.ualberta.cs.serl.wikidev.city3d.animation.CommunicationBuildingData;
import ca.ualberta.cs.serl.wikidev.city3d.animation.UserData;

public class CityLayout extends Layout {
	
	public static final String CITY_TYPE = "communications";
	
	private ArrayList<Message> messages;
	private int projectid;

	public CityLayout(ArrayList<IArtifact> newArtifacts, RelationshipMiner rm, int projectid)
			throws IOException {
		this.projectid = projectid;
		System.out.print("pch<-c(");
		messages = new ArrayList<Message>();
		for (IArtifact artifact : newArtifacts) {
			if (artifact instanceof Ticket) {
				System.out.print("\'*\', ");
			} else if (artifact instanceof Wiki) {
				System.out.print("\'o\', ");
			} else if (artifact instanceof Message) {
				System.out.print("\'+\', ");
				Message message = (Message)artifact;
				messages.add(message);
			} else if (artifact instanceof ChangeSet) {
				System.out.print("\'$\', ");
			}
		}
		System.out.println(")");

		System.out.println("MDS start");
		double[][] distanceMatrix = rm.getDistanceMatrix();
		this.getLayout(newArtifacts, distanceMatrix);
		HashMap<Message, Integer> messageCount = getMessageCount(messages);
		//printCityBlocks(cityBlocks, messageCount);
	}
	
	private HashMap<Message, Integer> getMessageCount(ArrayList<Message> messages) {
		HashMap<Message, Integer> messageCount = new HashMap<Message, Integer>();
		for(Message message1 : messages) {
			int count = 1;
			for(Message message2 : messages) {
				if(message1.getRefid_header().equals(message2.getRefid_header())) {
					count++;
				}
			}
			messageCount.put(message1, count);
		}
		return messageCount;
	}
	
	public HashMap<Message, Integer> getMessageCountBeforeDate(ArrayList<Message> messages, Timestamp timestamp) {
		HashMap<Message, Integer> messageCount = new HashMap<Message, Integer>();
		for(Message message1 : messages) {
			int count = 1;
			for(Message message2 : messages) {
				if(message1.getTimestamp().before(timestamp) && message2.getTimestamp().before(timestamp) && message1.getRefid_header().equals(message2.getRefid_header())) {
					count++;
				}
			}
			messageCount.put(message1, count);
		}
		return messageCount;
	}
	
	public HashMap<User, Double> getMessageUserContributionBeforeDate(Timestamp timestamp, ArrayList<User> users, Message message, ArrayList<Message> messages) {
		HashMap<User, Double> userChanges = new HashMap<User, Double>();
		for(User user : users) {
			if(message.getOwner().equals(user)) {
				userChanges.put(user, 1.0);
			}
			else {
				userChanges.put(user, 0.0);
			}
		}
		int count=1;
		for(Message message2 : messages) {
			if(message.getTimestamp().before(timestamp) && message2.getTimestamp().before(timestamp) && message.getRefid_header().equals(message2.getRefid_header())) {
				count++;
				for(User user : users) {
					if(message2.getOwner().equals(user)) {
						userChanges.put(user, userChanges.get(user)+1);
					}
				}
			}
		}
		for(User user : userChanges.keySet()) {
			userChanges.put(user, userChanges.get(user)/(double)count);
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
					System.out.println("Writing "+artifact.toString());
					String type = "";
					int height = 0;
					HashMap<User, Double> userContribution = null;
					int id=0;
					String url = "";
					User lastEditor = null;
					Timestamp created = null;
					Timestamp lastModified = null; 
					if(artifact instanceof Ticket) {
						Ticket ticket = (Ticket)artifact;
						type = "Ticket";
						height = 6-Integer.parseInt(""+ticket.getLastPriorityBeforeDate(nextWeek).charAt(1));
						userContribution = ticket.getUserContributionBeforeDate(nextWeek, this.getUsers(projectid));
						id = ticket.getTicketID();
						url = "http://hypatia.cs.ualberta.ca/ucosp/index.php/UCOSP_Index:Ticket_Index";
						ticket.setCreated(ticket.getDateOpened());
						ticket.setLastEditorAndTimeLastModifiedBeforeDate(nextWeek);
						
						lastEditor = ticket.getLastEditor();
						created = ticket.getCreated();
						lastModified = ticket.getLastModified();
					}
					else if(artifact instanceof Message) {
						type = "Message";
						Message message = (Message)artifact;
						height = this.getMessageCountBeforeDate(messages, nextWeek).get(message);
						userContribution = getMessageUserContributionBeforeDate(nextWeek, getUsers(projectid), message, messages);
						id = message.getCommunicationID();
						try {
							url = Layout.URLprefix+DataManager.getProjectByID(projectid).getProjectName()+"_Mail:"+message.getSubject().replace(' ', '_');
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						message.setLastEditorAndTimeLastModifiedBeforeDate(nextWeek);
						message.setCreated(message.getTimestamp());
						lastEditor = message.getLastEditor();
						created = message.getCreated();
						lastModified = message.getLastModified();
					}
					else if(artifact instanceof Wiki) {
						type = "Wiki";
						Wiki wiki = (Wiki)artifact;
						height = wiki.getNumberOfRevisionsBeforeDate(nextWeek);
						userContribution = wiki.getUserContributionBeforeDate(nextWeek, getUsers(projectid));
						id = wiki.getPage_id();
						try {
							url = Layout.URLprefix+DataManager.getProjectByID(projectid).getProjectName()+wiki.getPage_title();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						wiki.setCreated(wiki.getRevisions().get(0).getRev_timestamp());
						wiki.setLastEditorAndTimeLastModifiedBeforeDate(nextWeek);
						
						lastEditor = wiki.getLastEditor();
						created = wiki.getCreated();
						lastModified = wiki.getLastModified();
					}
					ArrayList<UserData> userData = new ArrayList<UserData>();
					for(User user : userContribution.keySet()) {
						userData.add(new UserData(user.getUserName(), user.getColor().getRed(), user.getColor().getGreen(), user.getColor().getBlue(), userContribution.get(user), nextWeek, CityLayout.CITY_TYPE));
					}
					CommunicationBuildingData building = new CommunicationBuildingData(type, id, nextWeek, height, block.getIndex().x, block.getIndex().y, block.getArtifacts().get(artifact), url, projectid, userData, lastEditor, created, lastModified);
					
					try {
						DataManager.writeCommunicationsBuildingData(building);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void printCityBlocks(ArrayList<CityBlock> blocks, HashMap<Message, Integer> messageCount)
			throws IOException {
		// BufferedWriter out = new BufferedWriter(new
		// FileWriter("C:\\Documents and Settings\\Marios Fokaefs\\Desktop\\wikidev-files\\ucosp\\clusters"+week+".txt"));
		BufferedWriter out = new BufferedWriter(
				new FileWriter(
						"C:\\Documents and Settings\\Marios Fokaefs\\Desktop\\wikidev-files\\c301f09\\cityBlocks.txt"));
		int index = 1;
		out.write(""+Math.sqrt(blocks.size()));
		out.newLine();
		for (CityBlock block : blocks) {
			for (IArtifact artifact : block.getArtifacts().keySet()) {
				String type = ""+index+"\t";
				int height = 0;
				String line = "";
				if (artifact instanceof Ticket) {
					Ticket ticket = (Ticket)artifact;
					type += "Ticket";
					height = 6-ticket.getPriority();
				} else if (artifact instanceof Message) {
					type += "Message";
					Message message = (Message)artifact;
					height = messageCount.get(message);
				} else if (artifact instanceof Wiki) {
					Wiki wiki = (Wiki)artifact;
					type += "Wiki";
					height = wiki.getRevisions().size();
				}
				line += type + "\t" + block.getIndex().x + "\t"
				+ block.getIndex().y + "\t"
				+ block.getArtifacts().get(artifact)+"\t"+height+"\t";
				for(User user : artifact.getAssociatedUsers()) {
					line += "<"+user.getUserName()+", "+user.getColor().getRed()+", "+user.getColor().getGreen()+", "+user.getColor().getBlue()+"> \t";
				}
				out.write(line);
				out.newLine();
				index++;
			}
		}
		out.close();
	}

}
