/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corpus;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

/**
 * 
 * @author alvin
 * @author nguyentd4
 */
public abstract class CorpusReader {
	private String filename = null;
	protected SentenceModel sentenceModel = null;
	protected SentenceDetectorME detector = null;

	public String getFilename() {
		return filename;
	}

	protected Scanner scanner = null;

	public void loadFile(File file) throws FileNotFoundException {
		scanner = new Scanner(file);
		filename = file.toString();
	}

	public abstract String getNextSentence();

	public abstract boolean hasNextSentence();

	/**
	 * 
	 * @param rawText
	 * @return
	 */
	public String[] getSentences(String rawText) {
		if (detector != null) {
			return detector.sentDetect(rawText);
		}
		return null;
	}

}
