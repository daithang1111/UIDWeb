package uid.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uid.util.Consts;
import uid.util.Util;

public class GetCorpusGraph extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("application/json");

		PrintWriter out = response.getWriter();
		String corpus = request.getParameter("corpus");
		try {

			String[] entropies = Util.readFromFile(Consts.DATA_PATH + corpus)[0]
					.split(",");// corpus contains values of 1,2,3,4,5
			// System.out.println(entropies.size());
			out.write("{\"longestStreak\":\"0\",");
			out.write("\"results\":[");
			out.write("{\"hightlight\":\"0\", \"line\":\"0\", \"sentence\":\""
					+ "EMPTY" + "\",\"entropy\":\"" + entropies[0] + "\"}");

			for (int i = 1; i < entropies.length; i++) {
				out.write(",{\"hightlight\":\"0\", \"line\":\"" + i
						+ "\", \"sentence\":\"" + "NONE" + "\",\"entropy\":\""
						+ entropies[i] + "\"}");
			}
			out.write("]}");

			out.flush();
		} catch (Exception e) {
			System.out.println("Error executing GetCorpusGraph");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}