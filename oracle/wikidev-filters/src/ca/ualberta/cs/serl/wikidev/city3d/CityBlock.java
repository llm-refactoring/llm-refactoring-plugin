package ca.ualberta.cs.serl.wikidev.city3d;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import ca.ualberta.cs.serl.wikidev.artifacts.IArtifact;

public class CityBlock {
	
	private Point index;
	private HashMap<IArtifact, Integer> artifacts;
	private double[] center;
	private boolean marked;
	
	public CityBlock(Point index) {
		this.index = index;
		this.artifacts = new HashMap<IArtifact, Integer>();
	}
	
	
	
	public boolean isMarked() {
		return marked;
	}



	public void setMarked(boolean marked) {
		this.marked = marked;
	}



	public double[] getCenter() {
		return center;
	}



	public void setCenter(double[] center) {
		this.center = center;
	}



	public Point getIndex() {
		return index;
	}



	public void setIndex(Point index) {
		this.index = index;
	}



	public HashMap<IArtifact, Integer> getArtifacts() {
		return artifacts;
	}



	public void setArtifacts(HashMap<IArtifact, Integer> artifacts) {
		this.artifacts = artifacts;
	}



	public void addArtifact(IArtifact artifact, int index) {
		artifacts.put(artifact, index);
	}

	
}
