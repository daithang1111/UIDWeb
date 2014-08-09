package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import util.PeakPair;
import util.Util;

public class UtilTest {

	float[] a1 = { 1 };
	float[] a2 = { 1, 2 };
	float[] a3 = { 2, 1 };
	float[] a4 = { 1, 2, 1 };
	float[] a5 = { 1, 2, 3, 4, 5, 6, 7 };
	float[] a6 = { 1, 2, 5, 3, 4, 5, 6, 10, 2, 9 };

	@Test
	public void hillsOfA1ShouldBeOne() {

		// a1
		ArrayList<PeakPair> out = Util.findHills(a1, 0);
		assertEquals("only 1 output", 1, out.size());
		assertEquals("a1 has start 0", 0, out.get(0).getStart());
		assertEquals("a1 has end 0", 0, out.get(0).getEnd());
		assertEquals("a1 has distance 0.0", 0, out.get(0).getDistance(), 0);

		// a2
		out = Util.findHills(a2, 0);
		assertEquals("only 1 slope", 1, out.size());
		assertEquals("a2 has start 0", 0, out.get(0).getStart());
		assertEquals("a2 has end 1", 1, out.get(0).getEnd());
		assertEquals("a2 has distance 1", 1, out.get(0).getDistance(), 0);
		// a3
		out = Util.findHills(a3, 1);
		assertEquals("only 1 slope", 1, out.size());
		assertEquals("a3 has start 0", 0, out.get(0).getStart());
		assertEquals("a3 has end 1", 1, out.get(0).getEnd());
		assertEquals("a3 has distance 1", 1, out.get(0).getDistance(), 0);
		// a4
		out = Util.findHills(a4, 0);
		assertEquals("only 2 slopes", 2, out.size());
		assertEquals("a4 has start 0 for the first slope", 0, out.get(0)
				.getStart());
		assertEquals("a4 has end 1 for the first slope", 1, out.get(0).getEnd());
		assertEquals("a4 has distance 1 for the first slop", 1, out.get(0)
				.getDistance(), 0);

		assertEquals("a4 has start 2 for the second slope", 2, out.get(1)
				.getStart());

		// a5
		out = Util.findHills(a5, 0);
		assertEquals("only 1 slope", 1, out.size());
		assertEquals("a5 has start 0", 0, out.get(0).getStart());
		assertEquals("a5 has end 6", 6, out.get(0).getEnd());
		assertEquals("a5 has distance 6", 6, out.get(0).getDistance(), 0);
	}
}
