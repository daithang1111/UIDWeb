package util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class ErrorAnalysis {
	public static double x = 1;// Math.log(10);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length != 3) {
			System.out
					.println("we need a directory of line\tscore files, window size, and an output name of this model");
			return;
		}
		String dirName = args[0];
		int windowSize = Integer.parseInt(args[1]);
		String outFile = args[2];

		// read the folder which contains files of wsj, each of which is
		// line\tscore
		TreeMap<Integer, ArrayList<Double>> total = new TreeMap<Integer, ArrayList<Double>>();
		File dir = new File(dirName);
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				HashMap<Integer, Double> hash = readEntropyFile(files[i]
						.getAbsolutePath());

				for (int line : hash.keySet()) {
					if (total.containsKey(line)) {
						total.get(line).add(hash.get(line));
					} else {
						ArrayList<Double> array = new ArrayList<Double>();
						array.add(hash.get(line));
						total.put(line, array);
					}
				}

			}
		}

		// we print 5 different lines (to)
		String minLine = outFile + ".1.min.tsv";
		String maxLine = outFile + ".1.max.tsv";
		String avgLine = outFile + ".1.avg.tsv";
		String lowerBoundLine = outFile + ".1.lower.tsv";
		String upperBoundLine = outFile + ".1.upper.tsv";

		// calculate statistics
		Util.fileWriter("line\tmin\n", minLine, true);
		Util.fileWriter("line\tmax\n", maxLine, true);
		Util.fileWriter("line\taverage\n", avgLine, true);

		Util.fileWriter("line\tlower\n", lowerBoundLine, true);
		Util.fileWriter("line\tupper\n", upperBoundLine, true);

		ArrayList<Double> minValues = new ArrayList<Double>();
		ArrayList<Double> maxValues = new ArrayList<Double>();
		ArrayList<Double> averageValues = new ArrayList<Double>();
		ArrayList<Double> lowerValues = new ArrayList<Double>();
		ArrayList<Double> upperValues = new ArrayList<Double>();

		for (int line : total.keySet()) {
			EntropyStat es = calculateStat(line, total.get(line));
			// print to file
			Util.fileWriter(line + "\t" + es.getSmallest() + "\n", minLine,
					true);
			Util.fileWriter(line + "\t" + es.getLargest() + "\n", maxLine, true);
			Util.fileWriter(line + "\t" + es.getAverage() + "\n", avgLine, true);

			Util.fileWriter(line + "\t" + es.getLowerBound() + "\n",
					lowerBoundLine, true);
			Util.fileWriter(line + "\t" + es.getUpperBound() + "\n",
					upperBoundLine, true);
			// add stats for each
			minValues.add(es.getSmallest());
			maxValues.add(es.getLargest());
			averageValues.add(es.getAverage());
			lowerValues.add(es.getLowerBound());
			upperValues.add(es.getUpperBound());
		}

		// extract window size values
		minValues = extractValues(minValues, windowSize);
		maxValues = extractValues(maxValues, windowSize);
		averageValues = extractValues(averageValues, windowSize);
		lowerValues = extractValues(lowerValues, windowSize);
		upperValues = extractValues(upperValues, windowSize);

		// file names
		minLine = outFile + "." + windowSize + ".min.tsv";
		maxLine = outFile + "." + windowSize + ".max.tsv";
		avgLine = outFile + "." + windowSize + ".avg.tsv";
		lowerBoundLine = outFile + "." + windowSize + ".lower.tsv";
		upperBoundLine = outFile + "." + windowSize + ".upper.tsv";

		// header
		Util.fileWriter("line\tmin\n", minLine, true);
		Util.fileWriter("line\tmax\n", maxLine, true);
		Util.fileWriter("line\taverage\n", avgLine, true);
		Util.fileWriter("line\tlower\n", lowerBoundLine, true);
		Util.fileWriter("line\tupper\n", upperBoundLine, true);

		// print these to file
		// MIN

		for (int i = 0; i < minValues.size(); i++) {
			Util.fileWriter(i + "\t" + minValues.get(i) + "\n", minLine, true);
		}

		// MAX

		for (int i = 0; i < maxValues.size(); i++) {
			Util.fileWriter(i + "\t" + maxValues.get(i) + "\n", maxLine, true);
		}

		// AVG

		for (int i = 0; i < averageValues.size(); i++) {
			Util.fileWriter(i + "\t" + averageValues.get(i) + "\n", avgLine,
					true);
		}

		// LOWER

		for (int i = 0; i < lowerValues.size(); i++) {
			Util.fileWriter(i + "\t" + lowerValues.get(i) + "\n",
					lowerBoundLine, true);
		}

		// UPPER
		for (int i = 0; i < upperValues.size(); i++) {
			Util.fileWriter(i + "\t" + upperValues.get(i) + "\n",
					upperBoundLine, true);
		}
	}

	/**
	 * 
	 * @param values
	 * @param windowSize
	 * @return
	 */
	public static ArrayList<Double> extractValues(ArrayList<Double> values,
			int windowSize) {
		ArrayList<Double> newValues = new ArrayList<Double>();
		if (values != null && values.size() >= windowSize && windowSize > 0) {
			for (int i = windowSize - 1; i < values.size(); i++) {
				double sum = 0.0;
				for (int j = 0; j < windowSize; j++) {
					sum += values.get(i - j);
				}
				sum = sum / (double) windowSize;
				newValues.add(sum);
			}

		}
		return newValues;

	}

	/**
	 * 
	 * @param values
	 * @param windowSize
	 * @return
	 */
	public static List<Float> extractValues(List<Float> values, int windowSize) {
		List<Float> newValues = new ArrayList<Float>();
		if (values != null && values.size() >= windowSize && windowSize > 0) {
			for (int i = windowSize - 1; i < values.size(); i++) {
				float sum = 0;
				for (int j = 0; j < windowSize; j++) {
					sum += values.get(i - j);
				}
				sum = sum / (float) windowSize;
				newValues.add(sum);
			}

		}
		return newValues;

	}

	/**
	 * 
	 * @param line
	 * @param values
	 * @return
	 */
	public static EntropyStat calculateStat(int line, ArrayList<Double> values) {
		if (values == null || values.size() == 0) {
			return new EntropyStat();
		}

		double size = (double) values.size();
		double totalSquare = 0.0, total = 0.0, min = Double.MAX_VALUE, max = Double.MIN_VALUE, average = 0.0, std = 0.0;
		for (double d : values) {
			total += d;
			totalSquare += d * d;
			min = Math.min(min, d);
			max = Math.max(max, d);
		}

		average = total / size;

		std = Math.sqrt(((totalSquare - 2 * average * total) / size) + average
				* average);

		EntropyStat es = new EntropyStat(line, min, max, average, std);
		return es;
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static HashMap<Integer, Double> readEntropyFile(String file) {
		HashMap<Integer, Double> hash = new HashMap<Integer, Double>();
		try {
			FileInputStream fstream = new FileInputStream(file);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int line;
			double entropy;
			while ((strLine = br.readLine()) != null) {
				String[] values = strLine.split("\\t");
				if (values.length == 2) {
					line = Integer.parseInt(values[0]);
					line = line - 1;
					if (values[1].equalsIgnoreCase("infinity")) {
						entropy = 0.0;
					} else {
						entropy = Double.parseDouble(values[1]) * x;
					}
					hash.put(line, entropy);
				}
			}
			br.close();
			in.close();
		} catch (Exception e) {

		}
		return hash;

	}
}
