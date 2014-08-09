package util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Preprocessing {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out
					.println("We need a dir for lower case and remove puntuations");
			return;
		}
		File dir = new File(args[0]);
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				toLowerCase(files[i].getAbsolutePath());
			}
		}

	}

	public static void toLowerCase(String file) {
		try {
			FileInputStream fstream = new FileInputStream(file);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				String s = removeNonWords(removeExtraSpace(strLine))
						.toLowerCase();
				if (s != null && s.length() > 0) {
					Util.fileWriter(s + "\n", file + ".lower", true);
				}

			}
			br.close();
			in.close();
		} catch (Exception e) {

		}
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static String removeNonWords(String s) {
		return s.replaceAll("[^A-Za-z0-9 ]", "");
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static String removeExtraSpace(String s) {
		return s.replaceAll("\\s+", " ");
	}
}
