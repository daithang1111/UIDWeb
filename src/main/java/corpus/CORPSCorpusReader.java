package corpus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;

/**
 * Reader corps corpus files
 * 
 * @author alvin
 * @author nguyentd4
 */
public class CORPSCorpusReader extends CorpusReader {

	private boolean skipAnnotations = true;
	private File sentenceModelFile = null;
	List<String> sentences = new ArrayList<String>();

	public CORPSCorpusReader(File modelFile) {
		setSentenceModelFile(modelFile);
	}

	@Override
	public void loadFile(File file) throws FileNotFoundException {
		super.loadFile(file);
		skipHeader();
		while (scanner.hasNextLine()) {
			String[] currentSentences = this.getSentences(scanner.nextLine());
			for (String sent : currentSentences) {
				// sent = sent.replace('-', ' '); //remove dashes. Might be
				// unnecessary.
				if (this.skipAnnotations) {
					sent = stripAnnotations(sent);
				}
				if (!sent.trim().equals("")) {
					sentences.add(sent);
				}
			}
		}
		// sentences.remove(sentences.size() - 1); //removes {/speech}
		scanner.close();
	}

	private void setSentenceDetector() {
		try {
			sentenceModel = new SentenceModel(sentenceModelFile);
			detector = new SentenceDetectorME(sentenceModel);
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public final void setSentenceModelFile(File modelFile) {
		this.sentenceModelFile = modelFile;
		setSentenceDetector();
	}

	private void skipHeader() {

		for (int i = 0; i < 10; i++) {
			if (scanner.hasNext()) {
				scanner.nextLine();
			} else {
				break;
			}
		}
	}

	@Override
	public String getNextSentence() {
		String next = this.sentences.get(0);
		sentences.remove(0);
		return next;
	}

	@Override
	public boolean hasNextSentence() {
		return this.sentences.size() > 0;
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	private static String stripAnnotations(String string) {
		if (string.startsWith("{AUDIENCE}")) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == '{') {
				while (string.charAt(i) != '}' && i < string.length()) {
					if (i + 1 < string.length()) {
						i++;
					} else {
						break;
					}
				}
				i++;
			}
			if (i < string.length()) {
				builder.append(string.charAt(i));
			}
		}
		return builder.toString();
	}

	public static void main(String[] args) {
		System.out.println(stripAnnotations("This is an {annotation} test."));
	}

	/**
	 * @return the skipAnnotations
	 */
	public boolean isSkipAnnotations() {
		return skipAnnotations;
	}

	/**
	 * @param skipAnnotations
	 *            the skipAnnotations to set
	 */
	public void setSkipAnnotations(boolean skipAnnotations) {
		this.skipAnnotations = skipAnnotations;
	}
}
