package uid.model;

import java.util.LinkedList;
import java.util.List;

import edu.stanford.nlp.util.StringUtils;

/**
 * 
 * @author alvin
 * @author nguyentd4
 */
public abstract class LanguageModel {

	public abstract float getScore(String sequence);

	public abstract float getScore(List<String> tokens);

	public abstract void loadLM();

	public abstract int getLmOrder();

	/**
	 * return n-gram for a list
	 * 
	 * @param tokens
	 * @param startPos
	 * @param ngramSize
	 * @return
	 */
	public static List<String> getNGrams(List<String> tokens, int startPos,
			int ngramSize) {
		if (tokens.size() < ngramSize) {
			return null;
		}

		List<String> ngrams = new LinkedList<String>();
		LinkedList<String> queue = new LinkedList<String>();
		String token = "";
		for (int i = startPos; i < tokens.size(); i++) {
			token = tokens.get(i);
			if (queue.size() < ngramSize) {
				queue.add(token);
			} else {
				queue.pop();
				queue.add(token);
				ngrams.add(listToString(queue));
			}
		}
		return ngrams;
	}

	/**
	 * 
	 * @param tokens
	 * @return
	 */
	private static String listToString(List<String> tokens) {
		if (tokens == null || tokens.size() == 0) {
			return "";
		} else {
			return StringUtils.join(tokens, " ");
		}
	}
}
