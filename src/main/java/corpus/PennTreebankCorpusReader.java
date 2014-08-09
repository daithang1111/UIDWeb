package corpus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.trees.PennTreeReader;
import edu.stanford.nlp.trees.PennTreeReaderFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeReader;
import edu.stanford.nlp.util.StringUtils;

/**
 * 
 * @author alvin
 * @author nguyentd4
 */
public class PennTreebankCorpusReader extends CorpusReader {

	TreeReader treeReader = null;
	String nextString = null;

	public PennTreebankCorpusReader(File file) throws FileNotFoundException,
			IOException {
		PennTreeReaderFactory factory = new PennTreeReaderFactory();
		treeReader = factory.newTreeReader(new BufferedReader(new FileReader(
				file)));
		treeReader = (PennTreeReader) treeReader;
		cacheNextSentence();
	}

	private void cacheNextSentence() {
		ArrayList<Word> words = null;
		try {
			Tree tree = treeReader.readTree();
			if (tree == null) {
				this.nextString = null;
				return;
			}
			words = tree.yieldWords();
		} catch (IOException ex) {
			Logger.getLogger(PennTreebankCorpusReader.class.getName()).log(
					Level.SEVERE, null, ex);
		}
		List<String> list = new ArrayList<String>();
		for (Word word : words) {
			list.add(word.word());
		}
		nextString = StringUtils.join(list, " ");
	}

	@Override
	public boolean hasNextSentence() {
		return nextString != null;
	}

	@Override
	public String getNextSentence() {
		return nextString;
	}

}
