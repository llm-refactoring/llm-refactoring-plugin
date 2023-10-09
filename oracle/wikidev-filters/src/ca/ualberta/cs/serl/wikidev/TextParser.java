package ca.ualberta.cs.serl.wikidev;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author   User
 */
public class TextParser {

	/**
	 * @uml.property  name="urls"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="ca.ualberta.cs.serl.wikidev.URL"
	 */
	private ArrayList<URL> urls;
	/**
	 * @uml.property  name="keywords"
	 */
	private ArrayList<String> keywords;
	/**
	 * @uml.property  name="words"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	private ArrayList<String> words;
	/**
	 * @uml.property  name="frequencies"
	 * @uml.associationEnd  qualifier="word:java.lang.String java.lang.Integer"
	 */
	private HashMap<String, Integer> frequencies;
	/**
	 * @uml.property  name="punctuation"
	 */
	private boolean punctuation;
	/**
	 * @uml.property  name="tfidf"
	 */
	private TreeMap<String, Double> tfidf;
	/**
	 * @uml.property  name="documents"
	 */
	private ArrayList<Document> documents;
	
	public TextParser(ArrayList<Document> documents) {
		this.urls = new ArrayList<URL>();
		this.keywords = new ArrayList<String>();
		this.words = new ArrayList<String>();
		this.frequencies = new HashMap<String, Integer>();
		this.tfidf = new TreeMap<String, Double>();
		this.documents = documents;
	}
	
	public TextParser() {
		this.urls = new ArrayList<URL>();
		this.keywords = new ArrayList<String>();
		this.words = new ArrayList<String>();
		this.frequencies = new HashMap<String, Integer>();
		this.tfidf = new TreeMap<String, Double>();
	}
	
	
	
	public ArrayList<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(ArrayList<String> keywords) {
		this.keywords = keywords;
	}

	public TreeMap<String, Double> getTfidf() {
		return tfidf;
	}

	public void setTfidf(TreeMap<String, Double> tfidf) {
		this.tfidf = tfidf;
	}

	public TreeMap<String, Double> calculateTFIDF() {
		TreeMap<String, Double> tfidf = new TreeMap<String, Double>();
		double numberOfDocuments = 0.0;
		double idf = 0.0;
		for(String word : frequencies.keySet()) {
			for(Document document : documents) {
				if(document.contains(word)) {
					numberOfDocuments++;
				}
			}
			idf = Math.log(1 + documents.size()/numberOfDocuments);
			tfidf.put(word, Math.log(1+frequencies.get(word))*idf);
			numberOfDocuments = 0.0;
		}
		return tfidf;
	}
	
	public void setFrequencies() {
		for(String word : words) {
			if(frequencies.containsKey(word)) {
				frequencies.put(word, (frequencies.get(word)+1));
			}
			else {
				frequencies.put(word, 1);
			}
		}
	}
	
	/**
	 * @return
	 * @uml.property  name="urls"
	 */
	public ArrayList<URL> getUrls() {
		return urls;
	}
	
	
	
	/**
	 * @return
	 * @uml.property  name="words"
	 */
	public Set<String> getWords() {
		return frequencies.keySet();
	}
	
	public HashMap<String, Integer> getFrequencies() {
		return frequencies;
	}

	public void parseFile(File file){}
	
	public void parseTextInWords(String text){
		int index = 0;
		String word = "";
		while(index<text.length()) {
			if(text.charAt(index) == ' ') {
				word = text.substring(0, index);
				text = text.substring(index+1);
				words.add(word);
				index = 0;
			}
			else {
				if(!text.contains(" ")) {
					words.add(text);
					break;
				}
				index++;
			}
		}
		separateURLandWords();
		removeEveryPunctuation();
		words = applyStoplist();
		words = stem();
		setFrequencies();
		frequencies = removeRareTerms();
		//tfidf = calculateTFIDF();
	}
	
	private HashMap<String, Integer> removeRareTerms() {
		ArrayList<String> rareTerms = new ArrayList<String>();
		double avg = 0.0;
		for(String word : frequencies.keySet()) {
			avg += frequencies.get(word);
		}
		int size = frequencies.size();
		if (size > 0) {
			avg /= size;
		}
		for(String word : frequencies.keySet()) {
			if(!word.matches("[a-z]+")) {
				rareTerms.add(word);
			}
		}
		for(String word : rareTerms) {
			frequencies.remove(word);
		}
		return frequencies;
	}
	
	private ArrayList<String> stem() {
		Stemmer stemmer = new Stemmer();
		ArrayList<String> stemmedWords = new ArrayList<String>();
		for(String word : words) {
			stemmer.add(word.toCharArray(), word.length());
			stemmer.stem();
			stemmedWords.add(stemmer.toString());
		}
		return stemmedWords;
	}
	
	private void separateURLandWords() {
		ArrayList<String> wordsToBeRemoved = new ArrayList<String>();
		for(String word : words) {
			if(word.startsWith("http") || word.startsWith("file:///")) {
				parseURL(word);
				wordsToBeRemoved.add(word);
			}
		}
		for(String word : wordsToBeRemoved) {
			words.remove(word);
		}
	}
	
	private void removeEveryPunctuation() {
		ArrayList<String> newWords = new ArrayList<String>();
		String newWord = "";
		for(String word : words) {
			newWord = word;
			newWord = removePunctuation(newWord);
			while(punctuation) {
				newWord = removePunctuation(newWord);
			}
			newWords.add(newWord);
		}
		words.clear();
		words.addAll(newWords);
		/*for(int i=0; i<urls.size(); i++) {
			URL newUrl = urls.get(i);
			newUrl.setUrl(removePunctuation(urls.get(i).getUrl()));
			urls.set(i, newUrl);
		}*/
	}
	
	private String removePunctuation(String word) {
		punctuation = false;
		if(word.startsWith(".") || word.endsWith(".")) {
			word = removeChar(word, '.');
			punctuation = true;
		}
		if(word.startsWith(",") || word.endsWith(",")) {
			word = removeChar(word, ',');
			punctuation = true;
		}
		if(word.startsWith("?") || word.endsWith("?")) {
			word = removeChar(word, '?');
			punctuation = true;
		}
		if(word.startsWith("!") || word.endsWith("!")) {
			word = removeChar(word, '!');
			punctuation = true;
		}
		if(word.startsWith("\"") || word.endsWith("\"")) {
			word = removeChar(word, '\"');
			punctuation = true;
		}
		if(word.startsWith("\'") || word.endsWith("\'")) {
			word = removeChar(word, '\'');
			punctuation = true;
		}
		if(word.startsWith("(") || word.endsWith("(")) {
			word = removeChar(word, '(');
			punctuation = true;
		}
		if(word.startsWith(")") || word.endsWith(")")) {
			word = removeChar(word, ')');
			punctuation = true;
		}
		if(word.startsWith("{") || word.endsWith("{")) {
			word = removeChar(word, '{');
			punctuation = true;
		}
		if(word.startsWith("}") || word.endsWith("}")) {
			word = removeChar(word, '}');
			punctuation = true;
		}
		if(word.startsWith("[") || word.endsWith("[")) {
			word = removeChar(word, '[');
			punctuation = true;
		}
		if(word.startsWith("]") || word.endsWith("]")) {
			word = removeChar(word, ']');
			punctuation = true;
		}
		if(word.startsWith("*") || word.endsWith("*")) {
			word = removeChar(word, '*');
			punctuation = true;
		}
		if(word.startsWith("#") || word.endsWith("#")) {
			word = removeChar(word, '#');
			punctuation = true;
		}
		if(word.startsWith("-") || word.endsWith("-")) {
			word = removeChar(word, '-');
			punctuation = true;
		}
		if(word.startsWith("_") || word.endsWith("_")) {
			word = removeChar(word, '_');
			punctuation = true;
		}
		if(word.startsWith("<") || word.endsWith("<")) {
			word = removeChar(word, '<');
			punctuation = true;
		}
		if(word.startsWith(">") || word.endsWith(">")) {
			word = removeChar(word, '>');
			punctuation = true;
		}
		if(word.startsWith("=") || word.endsWith("=")) {
			word = removeChar(word, '=');
			punctuation = true;
		}
		if(word.startsWith("~") || word.endsWith("~")) {
			word = removeChar(word, '~');
			punctuation = true;
		}
		if(word.startsWith(":") || word.endsWith(":")) {
			word = removeChar(word, ':');
			punctuation = true;
		}
		if(word.startsWith("\\") || word.endsWith("\\")) {
			word = removeChar(word, '\\');
			punctuation = true;
		}
		if(word.startsWith("/") || word.endsWith("/")) {
			word = removeChar(word, '/');
			punctuation = true;
		}
		if(word.startsWith("€") || word.endsWith("€")) {
			word = removeChar(word, '€');
			punctuation = true;
		}
		return word;
	}
	
	/*private String removePunctuation(String word) {
		if(word.contains(".")) {
			word = removeChar(word, '.');
		}
		if(word.contains(",")) {
			word = removeChar(word, ',');		}
		if(word.contains("?")) {
			word = removeChar(word, '?');
		}
		if(word.contains("!")) {
			word = removeChar(word, '!');
		}
		if(word.contains("\"")) {
			word = removeChar(word, '\"');
		}
		if(word.contains("\'")) {
			word = removeChar(word, '\'');
		}
		if(word.contains("(")) {
			word = removeChar(word, '(');
		}
		if(word.contains(")")) {
			word = removeChar(word, ')');
		}
		if(word.contains("{")) {
			word = removeChar(word, '{');
		}
		if(word.contains("}")) {
			word = removeChar(word, '}');
		}
		if(word.contains("[")) {
			word = removeChar(word, '[');
		}
		if(word.contains("]")) {
			word = removeChar(word, ']');
		}
		if(word.contains("*")) {
			word = removeChar(word, '*');
		}
		if(word.contains("#")) {
			word = removeChar(word, '#');
		}
		if(word.contains("-")) {
			word = removeChar(word, '-');
		}
		if(word.contains("_")) {
			word = removeChar(word, '_');
		}
		return word;
	}*/
	
	
	private String removeChar(String s, char c) {
		   String r = "";
		   for (int i = 0; i < s.length(); i ++) {
		      if (s.charAt(i) != c) r += s.charAt(i);
		      }
		   return r;
		}
	
	public void parseURL(String text) {
		URL newURL = new URL();
		newURL.setUrl(text);
		if(text.startsWith("http://")) {
			text = text.substring(7);
		}
		else if(text.startsWith("https://") || text.startsWith("file:///")) {
			text = text.substring(8);
		}
		int indexOfNextSlash = text.indexOf("/");
		if (indexOfNextSlash != -1) {
			newURL.setHost(text.substring(0, indexOfNextSlash));
			text = text.substring(indexOfNextSlash + 1);
			indexOfNextSlash = text.indexOf("/");
			if (indexOfNextSlash != -1) {
				if (text.substring(0, indexOfNextSlash).contains(".")) {
					text = text.substring(indexOfNextSlash + 1);
					indexOfNextSlash = text.indexOf("/");
				}
				if (indexOfNextSlash != -1) {
					if (text.substring(0, indexOfNextSlash).equals("drproject")) {
						indexOfNextSlash = text.indexOf("/");
						text = text.substring(indexOfNextSlash + 1);
						indexOfNextSlash = text.indexOf("/");
						newURL.setProjectName(text.substring(0,
								indexOfNextSlash));
						text = text.substring(indexOfNextSlash + 1);
						indexOfNextSlash = text.indexOf("/");
						if (indexOfNextSlash != -1) {
							newURL.setEntityType(Dictionary
									.getDataBaseTable(text.substring(0,
											indexOfNextSlash)));
						}
						else {
							newURL.setEntityType(Dictionary
									.getDataBaseTable(text));
						}
					} else if (text.substring(0, indexOfNextSlash)
							.equals("svn")) {
						newURL.setEntityType(Dictionary.getDataBaseTable(text
								.substring(0, indexOfNextSlash)));
						text = text.substring(indexOfNextSlash + 1);
						indexOfNextSlash = text.indexOf("/");
						if (indexOfNextSlash != -1) {
							newURL.setProjectName(text.substring(0,
									indexOfNextSlash));
						}
						else {
							newURL.setProjectName(text);
						}
					} else {
						newURL.setProjectName(text.substring(0,
								indexOfNextSlash));
						text = text.substring(indexOfNextSlash + 1);
						indexOfNextSlash = text.indexOf("/");
						if (indexOfNextSlash != -1) {
							newURL.setEntityType(Dictionary
									.getDataBaseTable(text.substring(0,
											indexOfNextSlash)));
						}
						else {
							newURL.setEntityType(Dictionary
									.getDataBaseTable(text));
						}
					}
				}
			}
		}
		else {
			newURL.setHost(text);
		}
		if(newURL.getUrl().contains("src")) {
			newURL.setEntityType(Dictionary.getDataBaseTable("src"));
		}
		this.urls.add(newURL);	
	}
	
	private ArrayList<String> applyStoplist() {
		ArrayList<String> newWords = new ArrayList<String>();
		boolean found = false;
		try {
			for (String word : words) {
				//BufferedReader in = new BufferedReader(new FileReader("ca/ualberta/cs/serl/wikidev/files/glasgowstoplist.txt"));
				BufferedReader in = new BufferedReader(new InputStreamReader(TextParser.class.getResourceAsStream("files/glasgowstoplist.txt")));
				String next = in.readLine();
				while(next != null) {
					if(word.equalsIgnoreCase(next)) {
						found = true;
					}
					next = in.readLine();
				}
				in.close();
				if (found) {
					newWords.add(word);
				}
				found = false;
			}
			words.removeAll(newWords);
			/*found = false;
			for(String newWord : newWords) {
				for(String word : words) {
					if(word.equals(newWord)) {
						found=true;
					}
				}
				
				words.remove(newWord);
			}*/
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return words;
	}
	
	
	
	/*public String listOfWords(String text) {
		String result="";
		TaporwareServices ts = new TaporwareServicesLocator();
		try {
			TaporwareService_xml_bindingStub tapor = (TaporwareService_xml_bindingStub)ts.getTaporwareService_xml();
			result = tapor.list_Words_Plain(text, "stop", "glasgow", "2", "tab");
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}*/
}
