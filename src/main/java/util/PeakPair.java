package util;

public class PeakPair {

	int start;
	int end;
	float distance;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public PeakPair(int s, int e, float d) {
		start = s;
		end = e;
		distance = d;
	}

	public void printPeakPair() {
		System.out.println("Start:" + start + ", End:" + end + ", Distance:"
				+ distance);
	}

	public float getDistance() {
		return distance;
	}

	public int getStart() {
		return start;

	}

	public int getEnd() {
		return end;
	}

	public void setStart(int s) {
		start = s;
	}

	public void setEnd(int e) {
		end = e;
	}

	public void setDistance(float d) {
		distance = d;
	}

}
