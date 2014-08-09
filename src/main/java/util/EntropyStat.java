package util;

public class EntropyStat {

	int line = -1;
	double smallest = 0.0;
	double largest = 0.0;
	double average = 0.0;
	double std = 0.0;

	public EntropyStat(){
		
	}
	public EntropyStat(int l, double m, double lar, double avg, double s) {
		line = l;
		smallest = m;
		largest = lar;
		average = avg;
		std = s;
	}

	public int getLine() {
		return line;
	}

	public double getSmallest() {
		return smallest;

	}

	public double getLargest() {
		return largest;
	}

	public double getAverage() {
		return average;
	}

	public double getStd() {
		return std;
	}

	public double getLowerBound() {
		return average - std;
	}

	public double getUpperBound() {
		return average + std;
	}
}
