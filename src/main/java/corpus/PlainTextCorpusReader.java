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
 * Reader plain text
 * 
 * @author alvin
 * @author nguyentd4
 */
public class PlainTextCorpusReader extends CorpusReader {
	private File sentenceModelFile = null;

	public PlainTextCorpusReader(File sentenceModelFile) {
		this.setSentenceModelFile(sentenceModelFile);
	}

	private List<String> sentences = new ArrayList<String>();

	@Override
	public void loadFile(File file) throws FileNotFoundException {
		super.loadFile(file);

		// StringBuilder builder = new StringBuilder();
		while (scanner.hasNextLine()) {
			// builder.append(s.nextLine());
			String[] currentSentences = this.getSentences(scanner.nextLine());
			for (String sent : currentSentences) {
				// sent = sent.replace('-', ' '); //remove dashes. Might be
				// unnecessary.

				if (!sent.trim().equals("")) {
					sentences.add(sent);
				}
			}
		}
		// sentences.remove(sentences.size() - 1); //removes {/speech}
		scanner.close();
	}

	@Override
	public boolean hasNextSentence() {
		return sentences.size() > 0;
	}

	@Override
	public String getNextSentence() {
		String sent = sentences.get(0);
		sentences.remove(0);
		return sent;
	}

	/**
	 * @param sentenceModelFile
	 *            the sentenceModelFile to set
	 */
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

	public void setSentenceModelFile(File sentenceModelFile) {
		this.sentenceModelFile = sentenceModelFile;
		setSentenceDetector();
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getSentences() {
		return this.sentences;
	}
}
