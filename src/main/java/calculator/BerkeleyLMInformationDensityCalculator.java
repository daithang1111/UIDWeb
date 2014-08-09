/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import models.BerkeleyKNLanguageModel;
import models.LanguageModel;

/**
 * 
 * @author alvin
 * @author nguyentd4
 */
public class BerkeleyLMInformationDensityCalculator {

	private BerkeleyKNLanguageModel lm = null;

	public BerkeleyLMInformationDensityCalculator(LanguageModel lm) {

		this.lm = (BerkeleyKNLanguageModel) lm;
		lm.loadLM();
	}

	public BerkeleyKNLanguageModel getLm() {
		return lm;
	}

	/**
	 * 
	 * @param sentence
	 * @return
	 */
	public float getInformationDensityOfSentence(List<String> sentence) {
		float score = (lm.getScore(sentence) / (float) sentence.size());
		score /= Math.log10(2D); // convert to base 2
		return -score;
	}

	/**
	 * 
	 * @param sentence
	 * @return
	 */
	public float getInformationDensityOfSentence(String sentence) {
		StringTokenizer st = new StringTokenizer(sentence);
		List<String> list = new ArrayList<String>();
		while (st.hasMoreElements()) {
			list.add(st.nextToken());
		}
		return getInformationDensityOfSentence(list);
	}

}
