/*
 */

package uid.corpus;


/**
 * Simple wrapper for a Scanner
 * A more efficient way than PlainTextCorpusReader to read files that have one sentence per line.
 * Can handle files of arbitrary size.
 * @author alvin
 */
public class OneSentencePerLineCorpusReader extends CorpusReader {

    @Override
    public String getNextSentence() {
        return scanner.nextLine();
    }

    @Override
    public boolean hasNextSentence() {
        return scanner.hasNextLine();
    }
    
}
