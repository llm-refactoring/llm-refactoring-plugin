package ca.ualberta.cs.serl.wikidev.artifacts;

public class RelatedArtifact {
	
	/**
	 * @uml.property  name="artifact"
	 * @uml.associationEnd  multiplicity="(1 1)" inverse="directlyRelatedArtifacts:ca.ualberta.cs.serl.wikidev.artifacts.IArtifact"
	 */
	private IArtifact artifact;
	/**
	 * @uml.property  name="distance"
	 */
	private double distance;
	/**
	 * @uml.property  name="word"
	 */
	private String word;
	
	
	public RelatedArtifact(IArtifact artifact, double distance, String word) {
		this.artifact = artifact;
		this.distance = distance;
		this.word = word;
	}


	/**
	 * @return
	 * @uml.property  name="artifact"
	 */
	public IArtifact getArtifact() {
		return artifact;
	}


	/**
	 * @param artifact
	 * @uml.property  name="artifact"
	 */
	public void setArtifact(IArtifact artifact) {
		this.artifact = artifact;
	}


	/**
	 * @return
	 * @uml.property  name="distance"
	 */
	public double getDistance() {
		return distance;
	}


	/**
	 * @param distance
	 * @uml.property  name="distance"
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}


	/**
	 * @return
	 * @uml.property  name="word"
	 */
	public String getWord() {
		return word;
	}


	/**
	 * @param word
	 * @uml.property  name="word"
	 */
	public void setWord(String word) {
		this.word = word;
	}
	
	public boolean equals(Object o) {
		RelatedArtifact art = (RelatedArtifact)o;
		return art.getArtifact().equals(this.getArtifact());
	}

}
