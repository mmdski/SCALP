package gov.usgs.scalp;

import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sanitary input block
 *
 * Contains sanitary flow info sets
 */
public class SanitaryInputBlock {

	private LinkedHashMap<Integer, SanitaryFlowSetInfo> infoSets;
	private final static Logger LOGGER = Logger.getLogger("gov.usgs.scalp.SanitaryInputBlock");

	/**
	 * Constructs a sanitary input block from an input file
	 *
	 * The current line of the input file must be the first line of the sanitary
	 * block.
	 *
	 * @param inputFile
	 *            SCALP input file
	 * @throws ParseException
	 *             if parsing error is encountered
	 */
	public SanitaryInputBlock(InputFile inputFile) throws ParseException {

		if (inputFile == null)
			throw new NullPointerException();

		InputLine inputLine;
		Object[] data;
		String errorString = "Error parsing SANITARY input block";
		String unexpectedLogLine = "Unexpected line: %s, line number %d";

		inputLine = inputFile.nextNonEmptyLine();
		data = inputLine.parse("A8");
		if (!"SANITARY".equals((String) data[0])) {
			LOGGER.log(Level.SEVERE, String.format(unexpectedLogLine, inputLine.getLine(), inputLine.getLineNo()));
			throw new ParseException(errorString, inputLine.getLineNo());
		}

		SanitaryFlowSetInfo infoSet;
		boolean endReached = false;

		infoSets = new LinkedHashMap<>();
		while (!endReached) {

			infoSet = new SanitaryFlowSetInfo(inputFile);

			if (infoSets.containsKey(infoSet.setNumber)) {
				LOGGER.log(Level.SEVERE, String.format("Duplicate sanitary info set number: %2d", infoSet.setNumber));
				throw new RuntimeException("Duplicate sanitary info set number encountered");
			}

			infoSets.put(infoSet.setNumber, infoSet);

			// if this is the end of the sanitary block, the line will contain END,
			// otherwise, it will parse as SNA.
			inputLine = inputFile.nextNonEmptyLine();
			data = inputLine.parse("A3");
			if ("END".equals((String) data[0]))
				endReached = true;
			else
				inputFile.setCurrentLineNo(inputLine.getLineNo());
		}
	}

	private class SanitaryFlowSetInfo {
		int setNumber; // sanitary flow info set number
		double populationFactor; // sanitary flow population factor
		double[] monthlyFlowFactors;
		double[] weekdayFlowFactors;
		double[] hourlyFlowFactors;

		public SanitaryFlowSetInfo(InputFile inputFile) throws ParseException {
			InputLine inputLine;
			Object[] data;
			String errorString = "Error parsing sanitary flow info set";
			String unexpectedLogLine = "Unexpected line: %s, line number %d";

			// block number
			inputLine = inputFile.nextNonEmptyLine();
			data = inputLine.parse("A3,13X,I8");
			if (!"SNA".equals((String) data[0])) {
				LOGGER.log(Level.SEVERE, String.format(unexpectedLogLine, inputLine.getLine(), inputLine.getLineNo()));
				throw new ParseException(errorString, inputLine.getLineNo());
			}
			setNumber = (int) data[1];

			// sanitary flow per person
			inputLine = inputFile.nextNonEmptyLine();
			data = inputLine.parse("16X,E20");
			populationFactor = (double) data[0];

			if (populationFactor < 0) {
				LOGGER.log(Level.SEVERE,
						String.format("Negative SNACOMP encountered in sanitary info set number %d", setNumber));
				throw new RuntimeException("Negative SNACOMP encountered");
			}

			monthlyFlowFactors = new double[12];

			// monthly flow factors for Jan-June
			inputLine = inputFile.nextNonEmptyLine();
			data = inputLine.parse("16X,6F8");
			for (int i = 0; i < 6; i++) {
				monthlyFlowFactors[i] = (double) data[i];
			}

			// monthly flow factors for July-Dec
			inputLine = inputFile.nextNonEmptyLine();
			data = inputLine.parse("16X,6F8");
			for (int i = 0; i < 6; i++) {
				monthlyFlowFactors[i + 6] = (double) data[i];
			}

			for (int i = 0; i < monthlyFlowFactors.length; i++) {
				if (monthlyFlowFactors[i] < 0) {
					LOGGER.log(Level.SEVERE, "Negative monthly flow factor encountered in sanitary info set number %d",
							setNumber);
					throw new RuntimeException("Negative monthly flow factor encountered");
				}
			}

			// weekday flow factors
			weekdayFlowFactors = new double[7];
			inputLine = inputFile.nextNonEmptyLine();
			data = inputLine.parse("16X,7F8");
			for (int i = 0; i < 7; i++) {
				weekdayFlowFactors[i] = (double) data[i];
				if (weekdayFlowFactors[i] < 0) {
					LOGGER.log(Level.SEVERE, "Negative weekday flow factor encountered in sanitary info set number %d",
							setNumber);
					throw new RuntimeException("Negative weekday flow factor encountered");
				}
			}

			// hourly flow factors
			hourlyFlowFactors = new double[24];

			// 1 am to 6 am
			inputLine = inputFile.nextNonEmptyLine();
			data = inputLine.parse("16X,6F8");
			for (int i = 0; i < 6; i++)
				hourlyFlowFactors[i] = (double) data[i];

			// 7 am to 12 pm
			inputLine = inputFile.nextNonEmptyLine();
			data = inputLine.parse("16X,6F8");
			for (int i = 0; i < 6; i++)
				hourlyFlowFactors[i + 6] = (double) data[i];

			// 1 pm to 6 pm
			inputLine = inputFile.nextNonEmptyLine();
			data = inputLine.parse("16X,6F8");
			for (int i = 0; i < 6; i++)
				hourlyFlowFactors[i + 12] = (double) data[i];

			// 7 pm to 12 am
			inputLine = inputFile.nextNonEmptyLine();
			data = inputLine.parse("16X,6F8");
			for (int i = 0; i < 6; i++)
				hourlyFlowFactors[i + 18] = (double) data[i];

			for (double f : hourlyFlowFactors) {
				if (f < 0) {
					LOGGER.log(Level.SEVERE, "Negative hourly flow factor encountered in sanitary info set number %d",
							setNumber);
					throw new RuntimeException("Negative hourly flow factor encountered");
				}
			}
		}

		public String[] asString() {
			String[] infoSetStrings = new String[9];

			infoSetStrings[0] = String.format("%-3s %16s %8d", "SNA#", " ", setNumber);
			infoSetStrings[1] = String.format("%-16s %20.5e", "SNACOMP=", populationFactor);

			StringBuffer formattedSB = new StringBuffer();

			// monthly flow factors
			formattedSB.append(String.format("%-16s", "JAN-JUNE="));
			for (int i = 0; i < 6; i++)
				formattedSB.append(String.format("%8.2f", monthlyFlowFactors[i]));
			infoSetStrings[2] = formattedSB.toString();
			formattedSB.setLength(0); // clear string buffer

			formattedSB.append(String.format("%-16s", "JULY-DEC="));
			for (int i = 6; i < 12; i++)
				formattedSB.append(String.format("%8.2f", monthlyFlowFactors[i]));
			infoSetStrings[3] = formattedSB.toString();
			formattedSB.setLength(0); // clear string buffer

			// weekday flow factors
			formattedSB.append(String.format("%-16s", "WEEKLY="));
			for (int i = 0; i < 7; i++)
				formattedSB.append(String.format("%8.2f", weekdayFlowFactors[i]));
			infoSetStrings[4] = formattedSB.toString();
			formattedSB.setLength(0); // clear string buffer

			// hourly flow factors
			formattedSB.append(String.format("%-16s", "01-06AM"));
			for (int i = 0; i < 6; i++)
				formattedSB.append(String.format("%8.2f", hourlyFlowFactors[i]));
			infoSetStrings[5] = formattedSB.toString();
			formattedSB.setLength(0); // clear string buffer

			formattedSB.append(String.format("%-16s", "07AM-12PM"));
			for (int i = 6; i < 12; i++)
				formattedSB.append(String.format("%8.2f", hourlyFlowFactors[i]));
			infoSetStrings[6] = formattedSB.toString();
			formattedSB.setLength(0); // clear string buffer

			formattedSB.append(String.format("%-16s", "01-06PM"));
			for (int i = 12; i < 18; i++)
				formattedSB.append(String.format("%8.2f", hourlyFlowFactors[i]));
			infoSetStrings[7] = formattedSB.toString();
			formattedSB.setLength(0); // clear string buffer

			formattedSB.append(String.format("%-16s", "07PM-12AM"));
			for (int i = 18; i < 24; i++)
				formattedSB.append(String.format("%8.2f", hourlyFlowFactors[i]));
			infoSetStrings[8] = formattedSB.toString();
			formattedSB.setLength(0); // clear string buffer

			return infoSetStrings;
		}

		public SNAInfo getSNAInfo() {
			return new SNAInfo(populationFactor, monthlyFlowFactors, weekdayFlowFactors, hourlyFlowFactors);
		}
	}

	/**
	 * Returns the sanitary input block represented as an array of strings
	 *
	 * @return string representation of input block
	 */
	public String[] asString() {
		LinkedList<String> stringList = new LinkedList<>();

		stringList.add(String.format("%8s", "SANITARY"));

		String[] infoSetStrings;

		for (SanitaryFlowSetInfo infoSet : infoSets.values()) {
			infoSetStrings = infoSet.asString();
			for (String s : infoSetStrings)
				stringList.add(s);
		}

		stringList.add(String.format("%3s", "END"));

		String[] blockAsString = new String[stringList.size()];
		return stringList.toArray(blockAsString);
	}

	/**
	 * Returns the sanitary flow info set numbers
	 *
	 * @return array of sanitary info set numbers
	 */
	public int[] getInfoSetNumbers() {
		return infoSets.keySet().stream().mapToInt(i -> i).toArray();
	}

	/**
	 * Returns a sanitary flow info set
	 *
	 * @param infoSetNo
	 *            sanitary flow info set number
	 * @return sanitary flow info set
	 */
	public SNAInfo getSNAInfo(int infoSetNo) {
		return infoSets.get(infoSetNo).getSNAInfo();
	}
}
