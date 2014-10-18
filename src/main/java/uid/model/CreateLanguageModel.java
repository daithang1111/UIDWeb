package uid.model;

public class CreateLanguageModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length != 3) {
			System.out
					.println("Please enter: ngramSize, inputFile, outputFile");
			return;
		}

		try {
			int ngramSize = Integer.parseInt(args[0]);
			String inFile = args[1];
			String outFile = args[2];

			BerkeleyKNLanguageModel.makeKneserNeyBinaryFromText(ngramSize,
					inFile, outFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
