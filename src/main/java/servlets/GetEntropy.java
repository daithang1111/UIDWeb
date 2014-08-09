package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.Consts;
import util.Util;

public class GetEntropy extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("application/json");

		PrintWriter out = response.getWriter();
		String inputText = request.getParameter("inputText");
		String topHL = request.getParameter("topHL");
		String LM = request.getParameter("LM");
		String corpus = request.getParameter("corpus");
		int windowSize = Integer.parseInt(request.getParameter("windowSize"));
		try {

			String[] sentences = Util.getSentencesFromPlainText(inputText
					.toLowerCase()); // lowercase the text

			if (sentences != null && sentences.length > 0) {

				String modelPath = Consts.DATA_PATH + corpus + "." + LM;

				List<Float> entropies = Util.getEntropyForSentences(sentences,
						modelPath, LM, windowSize);

				if (windowSize > 1) {
					sentences = Util.getNewSentences(sentences, windowSize);
				}
				HashMap<Integer, Boolean> peaks = Util.findPeaks(entropies,
						Integer.parseInt(topHL));
				int longestStreak = Util.getLongestStreak(entropies);
				// System.out.println(entropies.size());
				out.write("{\"longestStreak\":\"" + longestStreak + "\",");
				out.write("\"results\":[");
				out.write("{\"hightlight\":\"" + (peaks.containsKey(0) ? 1 : 0)
						+ "\", \"line\":\"0\", \"sentence\":\"" + sentences[0]
						+ "\",\"entropy\":\"" + entropies.get(0).floatValue()
						+ "\"}");

				for (int i = 1; i < sentences.length; i++) {
					out.write(",{\"hightlight\":\""
							+ (peaks.containsKey(i) ? 1 : 0)
							+ "\", \"line\":\"" + i + "\", \"sentence\":\""
							+ sentences[i] + "\",\"entropy\":\""
							+ entropies.get(i).floatValue() + "\"}");
				}
				out.write("]}");
			} else {
				out.write("{\"results\":\"ERROR\"}");
			}
			out.flush();
		} catch (Exception e) {
			System.out.println("Error executing GetEntropy");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}