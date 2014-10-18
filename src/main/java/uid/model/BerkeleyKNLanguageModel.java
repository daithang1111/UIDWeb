package uid.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import edu.berkeley.nlp.lm.NgramLanguageModel;
import edu.berkeley.nlp.lm.io.MakeKneserNeyArpaFromText;
import edu.berkeley.nlp.lm.io.MakeLmBinaryFromArpa;

/**
 * 
 * @author alvin
 * @author nguyentd4
 */
public class BerkeleyKNLanguageModel extends LanguageModel {

	float prior = 1; // used by BerkeleyMultiKneserNeyLanguageModel to calculate
						// priors
	NgramLanguageModel<String> lm = null;
	String lmName = null;
	String fileName = null;

	/**
	 * 
	 * @param fileName
	 */
	public BerkeleyKNLanguageModel(String fileName) {
		this.fileName = fileName;
		lmName = FilenameUtils.getBaseName(fileName);
		setLMName(lmName);
		lm = loadBinaryLM(fileName);

	}

	/**
	 * 
	 * @param name
	 */
	public final void setLMName(String name) {
		this.lmName = name;
	}

	/**
	 * 
	 * @return
	 */
	public String getLMName() {
		return this.lmName;
	}

	/**
	 * 
	 */
	public void loadLM() {
		this.lm = loadBinaryLM(fileName);
	}

	/**
	 * 
	 * @param filename
	 * @return
	 */
	public static NgramLanguageModel<String> loadBinaryLM(String filename) {
		edu.berkeley.nlp.lm.util.Logger
				.setGlobalLogger(new edu.berkeley.nlp.lm.util.Logger.SystemLogger(
						System.err, System.err));
		NgramLanguageModel<String> lm = TextStreamLogCal.readBinary(false,
				null, filename);
		return lm;
	}

	/**
	 * 
	 * @param ngramSize
	 * @param inFile
	 * @param outFile
	 * @throws IOException
	 */
	public static void makeKneserNeyBinaryFromText(int ngramSize,
			String inFile, String outFile) throws IOException {
		// make a temporary ARPA; convert it to binary; delete the ARPA
		File outDir = new File(FilenameUtils.getFullPath(outFile));
		String arpaFilename = outDir + "/" + FilenameUtils.getName(inFile)
				+ ".arpa.tmp";
		if (!outDir.exists()) {
			FileUtils.forceMkdir(outDir);
			System.err.println("Creating directory " + outDir + ".");
		}

		String[] args = { Integer.toString(ngramSize), arpaFilename, inFile };
		MakeKneserNeyArpaFromText.main(args);
		args = new String[2];
		args[0] = arpaFilename;
		args[1] = outFile;
		MakeLmBinaryFromArpa.main(args);
		File arpaFile = new File(arpaFilename);
		arpaFile.delete();
	}

	@Override
	public float getScore(String sequence) {
		StringTokenizer st = new StringTokenizer(sequence);
		List<String> tokens = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			tokens.add(st.nextToken());
		}

		return lm.getLogProb(tokens);

	}

	@Override
	public float getScore(List<String> tokens) {
		return lm.getLogProb(tokens);
	}

	@Override
	public int getLmOrder() {
		// TODO Auto-generated method stub
		return 0;
	}
}
