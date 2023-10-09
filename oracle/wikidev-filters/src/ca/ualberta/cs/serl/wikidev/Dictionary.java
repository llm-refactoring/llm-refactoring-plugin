package ca.ualberta.cs.serl.wikidev;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author  Marios Fokaefs
 */
public abstract class Dictionary {
	
	public static final String GLOBAL_CLASS = "classes|class";
	public static final String GLOBAL_SVN = "svn|browser";
	public static final String GLOBAL_TICKET = "ticket";
	public static final String GLOBAL_MAIL = "mail";
	public static final String GLOBAL_WIKI = "wiki";
	public static final String GLOBAL_SRC = "src";
	
	public static String getDataBaseTable(String entityType) {
		if(entityType.matches(GLOBAL_SVN)) {
			return "wikidev_changesets";
		}
		else if(entityType.matches(GLOBAL_TICKET)) {
			return "wikidev_tickets";
		}
		else if(entityType.matches(GLOBAL_WIKI)) {
			return "wikidev_wikis";
		}
		else if(entityType.matches(GLOBAL_MAIL)) {
			return "wikidev_communications";
		}
		else if(entityType.matches(GLOBAL_SRC)) {
			return "wikidev_sourceclasses";
		}
		else {
			return "";
		}
	}
	
	public static String getUnionPatternOfStrings(ArrayList<String> strings) {
		String pattern = strings.get(0);
		for(int i=1; i<strings.size(); i++) {
			pattern +="|"+strings.get(i);
		}
		return pattern;
	}
	
	public static String getMatchedString(Collection<String> words, String keyword) {
		keyword = keyword.toLowerCase();
		for(String word : words) {
			String wordLowerCase = word.toLowerCase();
			if(wordLowerCase.contains(keyword) || keyword.contains(wordLowerCase)) {
				return word;
			}
		}
		return null;
	}
	
	public static boolean matchesWord(String word, String keyword) {
		keyword = keyword.toLowerCase();
		String wordLowerCase = word.toLowerCase();
		if(wordLowerCase.contains(keyword) || keyword.contains(wordLowerCase)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean matchesPattern(String word, String pattern) {
		return word.matches(pattern);
	}
	
	public static String concatStrings(Collection<String> terms) {
		String res = "";
		for(String term : terms) {
			res += term+"\t";
		}
		return res;
	}

}
