package ca.ualberta.cs.serl.wikidev;

import java.util.Set;
import java.util.TreeMap;

public class Document {
	
	/**
	 * @uml.property  name="terms"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	private Set<String> terms;
	/**
	 * @uml.property  name="tfidf"
	 * @uml.associationEnd  qualifier="term:java.lang.String java.lang.Double"
	 */
	private TreeMap<String, Double> tfidf;
	/**
	 * @uml.property  name="name"
	 */
	private String name;

	public Document(Set<String> terms, String name) {
		this.terms = terms;
		this.name = name;
	}
	
	public boolean contains(String term) {
		return terms.contains(term);
	}

	public Set<String> getTerms() {
		return terms;
	}

	public TreeMap<String, Double> getTfidf() {
		return tfidf;
	}

	public void setTfidf(TreeMap<String, Double> tfidf) {
		this.tfidf = tfidf;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
