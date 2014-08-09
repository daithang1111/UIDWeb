package util;

import java.util.ArrayList;
import java.util.List;

import models.BerkeleyKNLanguageModel;
import calculator.BerkeleyLMInformationDensityCalculator;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] sentences = new String[2];
		sentences[0] = "this is stupid string";
		sentences[1] = "ok, that's is fine";
		String modelPath = "wsjTrain.kylm";
		List<Float> densities = new ArrayList<Float>();
		// TODO Auto-generated method stub
		BerkeleyLMInformationDensityCalculator calculator = new BerkeleyLMInformationDensityCalculator(
				new BerkeleyKNLanguageModel(modelPath));
		for (int i = 0; i < sentences.length; i++) {
			densities.add(calculator
					.getInformationDensityOfSentence(sentences[i]));
		}
		for (int i = 0; i < densities.size(); i++) {
			System.out.println(densities.get(i));
		}
	}

}
