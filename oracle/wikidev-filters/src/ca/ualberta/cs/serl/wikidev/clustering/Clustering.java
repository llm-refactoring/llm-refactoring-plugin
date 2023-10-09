package ca.ualberta.cs.serl.wikidev.clustering;

import java.util.ArrayList;

import ca.ualberta.cs.serl.wikidev.artifacts.IArtifact;

public abstract class Clustering {

	/**
	 * @uml.property  name="distanceList"
	 */
	protected ArrayList<ArrayList<Double>> distanceList;
	/**
	 * @uml.property  name="distanceMatrix" multiplicity="(0 -1)" dimension="2"
	 */
	protected double[][] distanceMatrix;
	
	public static Clustering getInstance(int type, double[][] distanceMatrix, double threshold) {
		return new Hierarchical(distanceMatrix, threshold);
	}
	
	public abstract ArrayList<Cluster> clustering(ArrayList<IArtifact> artifacts);
	
	public static double getEuclideanDistance(ArrayList<Double> p1, ArrayList<Double> p2) {
		double sum = 0.0;
		for(int i=0; i<p1.size(); i++) {
			sum += Math.pow(p1.get(i)-p2.get(i), 2);
		}
		return Math.sqrt(sum);
	}
	
	public static ArrayList<Double> toArrayList(double[] array) {
		ArrayList<Double> list = new ArrayList<Double>();
		for(int i=0; i<array.length; i++) {
			list.add(array[i]);
		}
		return list;
	}
	
	
}
