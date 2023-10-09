package ca.ualberta.cs.serl.wikidev;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import ca.ualberta.cs.serl.wikidev.artifacts.ChangeSet;
import ca.ualberta.cs.serl.wikidev.artifacts.ChangeSetDetail;
import ca.ualberta.cs.serl.wikidev.artifacts.IArtifact;
import ca.ualberta.cs.serl.wikidev.artifacts.Message;
import ca.ualberta.cs.serl.wikidev.artifacts.Ticket;
import ca.ualberta.cs.serl.wikidev.artifacts.Wiki;

public class Printer {
	
	//private static final double SCALEFACTOR = 1.0;
	/**
	 * @uml.property  name="index"
	 */
	private int index;
	
	/*public void print(RelationshipMiner rm) throws IOException {
		index=1;
		for(IArtifact artifact : rm.getArtifactsChanged()) {
			BufferedWriter out = new BufferedWriter(new FileWriter("C:\\wikidev-files\\"+artifact.toString()+".net"));
			int size = artifact.getDirectlyRelatedArtifacts().size()+artifact.getRelatedArtifactsWithWeights().size()+1;
			out.write("*Vertices " + size);
			out.newLine();
			printVertice(out, artifact);
			for(IArtifact directArtifact : artifact.getDirectlyRelatedArtifacts().keySet()) {
				printVertice(out, directArtifact);
			}
			for(IArtifact indirectArtifact : artifact.getRelatedArtifactsWithWeights().keySet()) {
				printVertice(out, indirectArtifact);
			}
			out.write("*Edges");
			out.newLine();
			for(IArtifact directArtifact : artifact.getDirectlyRelatedArtifacts().keySet()) {
				if(directArtifact.getIndex() == 0) {
					directArtifact.setIndex(index);
					index++;
				}
				if (directArtifact.getIndex() <= size) {
					String text = artifact.getIndex() + " "
							+ directArtifact.getIndex() + " "
							+ artifact.getDirectlyRelatedArtifacts().get(directArtifact);
					text += " c Blue";
					out.write(text);
					out.newLine();
				}
			}
			for(IArtifact indirectArtifact : artifact.getRelatedArtifactsWithWeights().keySet()) {
				if(indirectArtifact.getIndex() == 0) {
					indirectArtifact.setIndex(index);
					index++;
				}
				if (indirectArtifact.getIndex() <= size) {
					String text = artifact.getIndex() + " "
							+ indirectArtifact.getIndex() + " "
							+ ((artifact.getRelatedArtifactsWithWeights().get(indirectArtifact)/SCALEFACTOR)+1);
					text += " c Red";
					out.write(text);
					out.newLine();
				}
			}
			out.close();
			index=1;
		}
	}*/
	
	public void printVertice(BufferedWriter out, IArtifact artifact) throws IOException {
		if (artifact instanceof Ticket) {
			Ticket ticket = (Ticket)artifact;
			ticket.setIndex(index);
			out.write(ticket.getIndex() + " \"" + ticket.toString()
					+ "\" ic Green x_fact");
			out.newLine();
			index++;
		}
		else if (artifact instanceof Message) {
			Message message = (Message)artifact;
			message.setIndex(index);
			out.write(message.getIndex() + " \"" + message.toString()
					+ "\" ic Yellow x_fact ");
			out.newLine();
			index++;
		}
		else if (artifact instanceof Wiki) {
			Wiki wiki = (Wiki)artifact;
			wiki.setIndex(index);
			out.write(wiki.getIndex() + " \"" + wiki.toString()
					+ "\" ic Blue x_fact ");
			out.newLine();
			index++;
		}
		else if (artifact instanceof ChangeSet) {
			ChangeSet changeset = (ChangeSet)artifact;
			changeset.setIndex(index);
			out.write(changeset.getIndex() + " \"" + changeset.toString()
					+ "\" ic Black x_fact ");
			out.newLine();
			index++;
		}
		else if (artifact instanceof ChangeSetDetail) {
			ChangeSetDetail changesetdetail = (ChangeSetDetail)artifact;
			changesetdetail.setIndex(index);
			out.write(changesetdetail.getIndex() + " \"" + changesetdetail.toString()
					+ "\" ic Magenta x_fact ");
			out.newLine();
			index++;
		}
		
	}
	
	public void printEverything(ArrayList<IArtifact> artifacts, double[][] distanceMatrix, int project) throws IOException {
		index=1;
		BufferedWriter out = new BufferedWriter(new FileWriter("C:\\Documents and Settings\\Marios Fokaefs\\Desktop\\wikidev-files\\"+project+"\\wikidev.net"));
		int size = artifacts.size();
		out.write("*Vertices " + size);
		out.newLine();
		for(IArtifact artifact : artifacts) {
			printVertice(out, artifact);
		}
		out.write("*Edges");
		out.newLine();
		for(int i=1; i<distanceMatrix.length; i++) {
			for(int j=0; j<distanceMatrix.length-1; j++) {
				if (distanceMatrix[i][j] < 1) {
					String text = (i + 1) + " " + (j + 1) + " "
							+ distanceMatrix[i][j];
					out.write(text);
					out.newLine();
				}
			}
			
		}
		out.close();
	}

}
