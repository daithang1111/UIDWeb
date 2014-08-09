/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import models.BerkeleyKNLanguageModel;

import org.apache.commons.io.FileUtils;

import calculator.BerkeleyLMInformationDensityCalculator;
import corpus.CORPSCorpusReader;
import corpus.CorpusReader;
import corpus.OneSentencePerLineCorpusReader;
import corpus.PennTreebankCorpusReader;
import corpus.PlainTextCorpusReader;

/**
 * 
 * @author nguyentd4
 * 
 */
public class Util {

	public static void main(String[] args) {

		// test peakpair algorithm
		float[] a1 = { 1 };
		float[] a2 = { 1, 2 };
		float[] a3 = { 2, 1 };
		float[] a4 = { 1, 3, 2 };
		float[] a5 = { 1, 2, 3, 4, 5, 6, 8 };
		float[] a6 = { 1, 2, 5, 3, 4, 5, 6, 10, 2, 9 };

		//
		ArrayList<PeakPair> out = findHills(a1, 2);
		System.out.println("TEST a1");
		for (int i = 0; i < out.size(); i++) {
			out.get(i).printPeakPair();
		}

		//
		out = findHills(a2, 2);
		System.out.println("TEST a2");
		for (int i = 0; i < out.size(); i++) {
			out.get(i).printPeakPair();
		}

		//
		out = findHills(a3, 2);
		System.out.println("TEST a3");
		for (int i = 0; i < out.size(); i++) {
			out.get(i).printPeakPair();
		}

		//
		out = findHills(a4, 2);
		System.out.println("TEST a4");
		for (int i = 0; i < out.size(); i++) {
			out.get(i).printPeakPair();
		}

		//
		out = findHills(a5, 2);
		System.out.println("TEST a5");
		for (int i = 0; i < out.size(); i++) {
			out.get(i).printPeakPair();
		}
		//
		//
		//
		// //
		out = findHills(a6, 2);
		System.out.println("TEST a6");
		for (int i = 0; i < out.size(); i++) {
			out.get(i).printPeakPair();
		}
		//
		out = findHillsWithPositiveDistance(a6, 2);
		System.out.println("TEST a6, positive hills");
		for (int i = 0; i < out.size(); i++) {
			out.get(i).printPeakPair();
		}

		out = findTopHills(a6, 1, 1);
		System.out.println("TEST a6, top hills");
		for (int i = 0; i < out.size(); i++) {
			out.get(i).printPeakPair();
		}

		// PlainTextCorpusReader pt = new PlainTextCorpusReader(new File(
		// "en-sent.bin"));
		// try {
		// pt.loadFile(new File("test.txt"));
		// System.out.println(pt.getSentences().get(0).toString());
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// String modelType = Consts.LANGUAGE_MODEL;
		// String modelPath = "CORPS.bilm";
		// String inputText =
		// "We've got a real chance in the Senate.And all this ought to be dealt with in the next three weeks.And if it's not, the voters ought to deal with it in November.Choices, choices, choices.Elections should be about you and your children and your future, not what somebody else tells you they ought to be about.";
		// String sentences[] = inputText.split("\\.");
		// try {
		// List<Float> entropies = UID.getEntropyForSentences(sentences,
		// modelType, modelPath);
		// System.out.println(entropies.get(0).floatValue());
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// String test = "this is stupid \n nothing compare";
		// String lines[] = test.split("\\n");
		// for (int i = 0; i < lines.length; i++) {
		// System.out.println(lines[i]);
		// }
	}

	public static String[] getNewSentences(String[] sentences, int windowSize) {
		if (sentences == null || sentences.length < windowSize) {
			return null;
		} else {
			String[] newSentences = new String[sentences.length - windowSize
					+ 1];

			for (int i = windowSize - 1; i < sentences.length; i++) {
				newSentences[i + 1 - windowSize] = "";
				for (int j = 0; j < windowSize; j++) {
					newSentences[i + 1 - windowSize] += sentences[i - j];
				}
			}

			return newSentences;

		}

	}

	/**
	 * 
	 * @param array
	 * @return
	 */
	public static int getLongestStreak(List<Float> array) {
		float[] array1 = new float[array.size()];// not sure if needed
		for (int i = 0; i < array.size(); i++) {
			array1[i] = array.get(i).floatValue();
		}
		ArrayList<PeakPair> output = findHills(array1, 2);
		int longestStreak = 0;
		for (int i = 0; i < output.size(); i++) {
			longestStreak = Math.max(longestStreak, output.get(i).getEnd()
					- output.get(i).getStart());
		}
		return longestStreak;
	}

	/**
	 * find top peak sentences
	 * 
	 * @param array
	 * @param top
	 * @return
	 */
	public static HashMap<Integer, Boolean> findPeaks(List<Float> array, int top) {
		float[] array1 = new float[array.size()];// not sure if needed
		for (int i = 0; i < array.size(); i++) {
			array1[i] = array.get(i).floatValue();
		}

		//
		ArrayList<PeakPair> output = findTopHills(array1, 2, top);
		HashMap<Integer, Boolean> indexes = new HashMap<Integer, Boolean>();
		for (int i = 0; i < output.size(); i++) {
			PeakPair pp = output.get(i);
			for (int j = pp.getStart(); j <= pp.getEnd(); j++) {
				indexes.put(j, true);
			}
		}
		return indexes;

	}

	/**
	 * 
	 * @param array
	 * @param upDownBoth
	 * @param top
	 * @return
	 */
	public static ArrayList<PeakPair> findTopHills(float[] array,
			int upDownBoth, int top) {
		ArrayList<PeakPair> output = findHills(array, upDownBoth);
		ArrayList<PeakPair> newOutput = new ArrayList<PeakPair>();
		TreeMap<Float, PeakPair> sortedTree = new TreeMap<Float, PeakPair>();
		float small = Float.MIN_VALUE;
		for (int i = 0; i < output.size(); i++) {
			PeakPair pp = output.get(i);
			sortedTree.put(-pp.getDistance() + small * i, pp);
		}

		int count = 0;
		for (float f : sortedTree.keySet()) {
			if (count < top) {
				newOutput.add(sortedTree.get(f));
				count++;
			} else {
				break;
			}
		}
		return newOutput;

	}

	public static ArrayList<PeakPair> findHillsWithPositiveDistance(
			float[] array, int upDownBoth) {
		ArrayList<PeakPair> output = findHills(array, upDownBoth);
		ArrayList<PeakPair> newOutput = new ArrayList<PeakPair>();
		for (int i = 0; i < output.size(); i++) {
			if (output.get(i).getDistance() > 0) {
				newOutput.add(output.get(i));
			}
		}

		return newOutput;
	}

	public static ArrayList<PeakPair> findHills(float[] array, int upDownBoth) {
		ArrayList<PeakPair> output = new ArrayList<PeakPair>();
		if (array != null && array.length > 0) {
			if (upDownBoth == 0) {
				output = findHills(array);
			} else {
				float[] reverseArray = new float[array.length];
				for (int i = 0; i < array.length; i++) {
					reverseArray[i] = -array[i];
				}

				// if downHill
				if (upDownBoth == 1) {
					output = findHills(reverseArray);
				} else {
					output = findHills(array);
					output.addAll(findHills(reverseArray));

				}
			}
		}
		return output;

	}

	/**
	 * return the list of 'top' pairs, each of which contains indexes for
	 * consecutively increasing array values having biggest distances
	 * 
	 * @param array
	 * @return
	 */
	public static ArrayList<PeakPair> findHills(float[] array) {
		ArrayList<PeakPair> output = new ArrayList<PeakPair>();
		int start = 0, end = 0;
		float distance = 0f;
		if (array != null && array.length > 0) {
			if (array.length == 1) {
				output.add(new PeakPair(0, 0, 0));
			} else if (array.length == 2) {
				if (array[0] < array[1]) {
					start = 0;
					end = 1;
					distance = array[1] - array[0];
					output.add(new PeakPair(start, end, distance));
				} else {
					for (int i = 0; i < array.length; i++) {
						output.add(new PeakPair(i, i, 0));
					}
				}

			} else {
				int middleIndex = array.length / 2;
				ArrayList<PeakPair> preOutput = findHills(Arrays.copyOfRange(
						array, 0, middleIndex + 1));
				ArrayList<PeakPair> posOutput = findHills(Arrays.copyOfRange(
						array, middleIndex, array.length));

				// merge
				PeakPair lastOfPreOutput = preOutput.get(preOutput.size() - 1);
				PeakPair firstOfPosOutput = posOutput.get(0);

				for (int i = 0; i < preOutput.size() - 1; i++) {
					output.add(preOutput.get(i));
				}
				// we only merge if the middle is within increasing range

				if (lastOfPreOutput.getEnd() == middleIndex
						&& firstOfPosOutput.getStart() == 0) {

					output.add(new PeakPair(lastOfPreOutput.getStart(),
							firstOfPosOutput.getEnd() + middleIndex,
							lastOfPreOutput.getDistance()
									+ firstOfPosOutput.getDistance()));

				}
				for (int i = 1; i < posOutput.size(); i++) {
					output.add(new PeakPair(posOutput.get(i).getStart()
							+ middleIndex, posOutput.get(i).getEnd()
							+ middleIndex, posOutput.get(i).getDistance()));
				}
			}

		}

		return output;
	}

	/**
	 * 
	 * @param str
	 * @param fileName
	 * @param append
	 */
	public static void fileWriter(String str, String fileName, boolean append) {
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(fileName,
					append));
			output.write(str);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param text
	 * @return
	 */
	public static String[] getSentencesFromPlainText(String text)
			throws IOException {
		String[] paragraphs = text.split("\\r?\\n");
		ArrayList<String> sentences = new ArrayList<String>();
		for (int i = 0; i < paragraphs.length; i++) {
			String[] currentSentences = null;
			CorpusReader corpusReader = new PlainTextCorpusReader(new File(
					Consts.SENT_SEG_FILE));

			currentSentences = corpusReader.getSentences(paragraphs[i]);

			for (String sent : currentSentences) {

				if (!sent.trim().equals("")) {
					sentences.add(sent.replaceAll("\\s+", " "));
				}
			}
		}
		return sentences.toArray(new String[sentences.size()]);
	}

	public static boolean containsArgument(String argName, String[] args) {
		for (String arg : args) {
			if (arg.equalsIgnoreCase(arg)) {
				return true;
			}
		}
		return false;
	}

	public static String getArgValueWithName(String argName, String[] args) {
		for (String arg : args) {
			String[] argArray = arg.split("=");
			if (argArray.length == 2) {
				if (argArray[0].equalsIgnoreCase(argName)) {
					return argArray[1].trim();
				}
			}
		}
		return null;
	}

	public static String getArgValue(String arg) {
		String[] argArray = arg.split("=");
		if (argArray.length < 2 || argArray.length > 2) {
			return null;
		} else {
			return argArray[1].trim();
		}
	}

	public static String getArgName(String arg) {
		String[] argArray = arg.split("=");
		if (argArray.length < 1) {
			return null;
		} else {
			return argArray[0].trim();
		}
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static String[] readFromFile(String file) {
		ArrayList<String> terms = new ArrayList<String>();
		FileInputStream fstream = null;
		DataInputStream in = null;
		BufferedReader br = null;
		try {
			fstream = new FileInputStream(file);
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String strLine = "";
			while ((strLine = br.readLine()) != null) {
				terms.add(strLine);
			}
			br.close();
			in.close();
		} catch (IOException e) {

		} finally {
			closeQuietly(br);
			closeQuietly(in);
			closeQuietly(fstream);
		}
		return terms.toArray(new String[terms.size()]);
	}

	/**
	 * 
	 * @param closeable
	 */
	public static void closeQuietly(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException ex) {
				// ignore
			}
		}
	}

	// /////////////Specific to language model
	/**
	 * 
	 * @param dir
	 * @param recursive
	 * @param corpusType
	 * @param sentenceSegmenterModelFilename
	 * @param outDir
	 * @throws IOException
	 */
	public static void extractSentencesFromFiles(String dir, boolean recursive,
			String corpusType, String sentenceSegmenterModelFilename,
			String outDir) throws IOException {
		Collection<File> files = FileUtils.listFiles(new File(dir), null,
				recursive);
		File outDirFile = new File(outDir);
		if (!outDirFile.exists()) {
			FileUtils.forceMkdir(outDirFile);
		}
		for (File file : files) {
			String newFilename = outDir + "/" + file.getName() + "."
					+ Consts.LINE_SUFFIX;

			System.out.println("Extracting " + file.getName() + " to "
					+ newFilename);
			extractSentencesFromFile(file.getAbsolutePath(), newFilename,
					corpusType, sentenceSegmenterModelFilename);
		}
	}

	/**
	 * 
	 * @param inFilename
	 * @param outFilename
	 * @param corpusType
	 * @param sentenceSegmenterModelFilename
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void extractSentencesFromFile(String inFilename,
			String outFilename, String corpusType,
			String sentenceSegmenterModelFilename)
			throws FileNotFoundException, IOException {
		CorpusReader reader = null;
		File inFile = new File(inFilename);

		if (corpusType.equalsIgnoreCase(Consts.CORPS_CORPUS)) {
			reader = new CORPSCorpusReader(new File(
					sentenceSegmenterModelFilename));
		} else if (corpusType.equalsIgnoreCase(Consts.ONESENTENCE_CORPUS)) {
			reader = new OneSentencePerLineCorpusReader();
		} else if (corpusType.equalsIgnoreCase(Consts.PLAINTEXT_CORPUS)) {
			reader = new PlainTextCorpusReader(new File(
					sentenceSegmenterModelFilename));
		} else if (corpusType.equalsIgnoreCase(Consts.PENNTREE_CORPUS)) {
			reader = new PennTreebankCorpusReader(new File(
					sentenceSegmenterModelFilename));
		} else {
			System.out.println("Corpus type unspecified.");
			System.exit(1);
		}
		reader.loadFile(inFile);
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				outFilename)));
		while (reader.hasNextSentence()) {
			writer.write(reader.getNextSentence());
			writer.write("\n");
		}
		writer.close();

	}

	/**
	 * 
	 * @param text
	 * @return
	 * @throws FileNotFoundException
	 */
	public static List<Float> getEntropyForSentences(String[] sentences,
			String modelPath, String languageModel, int windowSize)
			throws FileNotFoundException {

		List<Float> densities = new ArrayList<Float>();
		try {

			System.out.println("Loading language model " + modelPath + ".");
			if (languageModel.equalsIgnoreCase(Consts.BERKELEY_LM)) {
				BerkeleyLMInformationDensityCalculator calculator = new BerkeleyLMInformationDensityCalculator(
						new BerkeleyKNLanguageModel(modelPath));
				for (int i = 0; i < sentences.length; i++) {
					densities.add(calculator
							.getInformationDensityOfSentence(sentences[i]));
				}
			} else {
				System.err.println("Language model unrecognized");
				System.exit(1);
			}

		} catch (Exception e) {
			System.out.println("Error get Calculator");
		}

		return ErrorAnalysis.extractValues(densities, windowSize);
	}

}
