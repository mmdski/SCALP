package gov.usgs.scalp;

import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Special contributing area input block
 */
public class SCAInputBlock {

	private int scaNumber;
	private double scaArea; // total sca area
	private LinkedHashMap<Integer, LandSegmentInfo> landSegments; // hspf land segments
	private int sanitaryInfoSet; // sanitary info set number
	private double popEquivalent; // population equivalent
	private double[] initialFlows; // initial flows in sewers
	private RoutingInfo routingInfo;
	private int sumTreatment; // summation indicator for total, inflow, infiltration, and sanitary treatment
								// plant flows
	private String sewerParts;
	private OutputDSSPathStrings sewerPaths; // DSS paths for sewer flows output

	private String overflowParts;
	private OutputDSSPathStrings overflowPaths; // DSS path for overflows output

	private int sumOverflows; // summation indicator for total, inflow, infiltration, and sanitary overflows
	private final static Logger LOGGER = Logger.getLogger("gov.usgs.scalp.SCAInputBlock");

	/**
	 * Constructs special contributing area input block from an input file. The
	 * current line of the input file must be the beginning of the special
	 * contributing area input block.
	 *
	 * @param inputFile
	 *            SCALP input file
	 * @throws ParseException
	 *             if a parsing error is encountered
	 */
	public SCAInputBlock(InputFile inputFile) throws ParseException {

		if (inputFile == null)
			throw new NullPointerException();

		InputLine inputLine;
		Object[] data;
		String errorString = "Error parsing SCA input block";
		String unexpectedLogLine = "Unexpected line: %s, line number %d";

		// beginning of sca block
		inputLine = inputFile.nextNonEmptyLine();
		data = inputLine.parse("A3");
		if (!"SCA".equals((String) data[0])) {
			LOGGER.log(Level.SEVERE, String.format(unexpectedLogLine, inputLine.getLine(), inputLine.getLineNo()));
			throw new ParseException(errorString, inputLine.getLineNo());
		}

		// analysis
		inputLine = inputFile.nextNonEmptyLine();
		data = inputLine.parse("A8");
		if (!"ANALYSIS".equals((String) data[0])) {
			LOGGER.log(Level.SEVERE, String.format(unexpectedLogLine, inputLine.getLine(), inputLine.getLineNo()));
			throw new ParseException(errorString, inputLine.getLineNo());
		}

		// sca number and total sca area
		inputLine = inputFile.nextNonEmptyLine();
		data = inputLine.parse("A3,13X,I8,F8");
		scaNumber = (int) data[1];
		scaArea = (double) data[2];

		// beginning of lands block
		inputLine = inputFile.nextNonEmptyLine();
		data = inputLine.parse("A5");
		if (!"LANDS".equals((String) data[0])) {
			LOGGER.log(Level.SEVERE, String.format(unexpectedLogLine, inputLine.getLine(), inputLine.getLineNo()));
			throw new ParseException(errorString, inputLine.getLineNo());
		}

		// land segments
		landSegments = new LinkedHashMap<>();
		boolean landsEndReached = false;
		LandSegmentInfo segment;
		while (!landsEndReached) {
			inputLine = inputFile.nextNonEmptyLine();
			data = inputLine.parse("A3");
			if ("END".equals((String) data[0]))
				landsEndReached = true;
			else {
				segment = new LandSegmentInfo(inputLine);

				if (landSegments.containsKey(segment.segmentNumber)) {
					LOGGER.log(Level.SEVERE,
							String.format("Duplicate land segment number: %2d", segment.segmentNumber));
					throw new RuntimeException("Duplicate land segment number encountered");
				}

				landSegments.put(segment.segmentNumber, segment);
			}
		}

		// beginning of sanitary block
		inputLine = inputFile.nextNonEmptyLine();
		data = inputLine.parse("A8");
		if (!"SANITARY".equals((String) data[0])) {
			LOGGER.log(Level.SEVERE, String.format(unexpectedLogLine, inputLine.getLine(), inputLine.getLineNo()));
			throw new ParseException(errorString, inputLine.getLineNo());
		}

		// sanitary info set and population equivalent
		inputLine = inputFile.nextNonEmptyLine();
		data = inputLine.parse("A3,13X,I8,F8");
		sanitaryInfoSet = (int) data[1];
		popEquivalent = (double) data[2];

		if (popEquivalent < 0) {
			LOGGER.log(Level.SEVERE, "Negative population equivalent encountered");
			throw new RuntimeException("Negative population equivalent encountered");
		}

		// end of sanitary info set
		inputLine = inputFile.nextNonEmptyLine();
		data = inputLine.parse("A3");
		if (!"END".equals((String) data[0])) {
			LOGGER.log(Level.SEVERE, String.format(unexpectedLogLine, inputLine.getLine(), inputLine.getLineNo()));
			throw new ParseException(errorString, inputLine.getLineNo());
		}

		// beginning of initial flows block
		inputLine = inputFile.nextNonEmptyLine();
		data = inputLine.parse("A13");
		if (!"INITIAL FLOWS".equals((String) data[0])) {
			LOGGER.log(Level.SEVERE, String.format(unexpectedLogLine, inputLine.getLine(), inputLine.getLineNo()));
			throw new ParseException(errorString, inputLine.getLineNo());
		}

		// initial sewer flows
		inputLine = inputFile.nextNonEmptyLine();
		data = inputLine.parse("16X,3F8");
		initialFlows = new double[3];
		for (int i = 0; i < 3; i++) {
			initialFlows[i] = (double) data[i];
			if (initialFlows[i] < 0) {
				LOGGER.log(Level.SEVERE, "Negative initial flow value encountered");
				throw new RuntimeException("Negative initial flow value encountered");
			}
		}

		// routing info
		routingInfo = new RoutingInfo(inputFile);

		// output paths
		inputLine = inputFile.nextNonEmptyLine();
		data = inputLine.parse("A6");
		if (!"OUTPUT".equals((String) data[0])) {
			LOGGER.log(Level.SEVERE, String.format(unexpectedLogLine, inputLine.getLine(), inputLine.getLineNo()));
			throw new ParseException(errorString, inputLine.getLineNo());
		}

		// sanitary treatment plant flows
		inputLine = inputFile.nextNonEmptyLine();
		boolean sumTreatmentFound = false;
		try {
			data = inputLine.parse("I8,A72");
			sumTreatment = (int) data[0];
			sewerParts = (String) data[1];
			sumTreatmentFound = true;
		} catch (ParseException e) {
			data = inputLine.parse("8X,A72");
			sumTreatment = 0;
			sewerParts = (String) data[0];
		}
		sewerPaths = new OutputDSSPathStrings(sewerParts, "STP");

		if (sumTreatmentFound) {
			LOGGER.log(Level.SEVERE, String.format("Output summation not supported: %d, line number %d", sumTreatment,
					inputLine.getLineNo()));
			throw new RuntimeException(errorString + ": Output summation not supported");
		}

		// overflows
		inputLine = inputFile.nextNonEmptyLine();
		boolean sumOverflowsFound = false;
		try {
			data = inputLine.parse("I8,A72");
			sumOverflows = (int) data[0];
			overflowParts = (String) data[1];
			sumOverflowsFound = true;
		} catch (ParseException e) {
			data = inputLine.parse("8X,A72");
			sumOverflows = 0;
			overflowParts = (String) data[0];
		}
		overflowPaths = new OutputDSSPathStrings(overflowParts, "OVF");

		if (sumOverflowsFound) {
			LOGGER.log(Level.SEVERE, String.format("Output summation not supported: %d, line number %d", sumOverflows,
					inputLine.getLineNo()));
			throw new RuntimeException(errorString + ": Output summation not supported");
		}

		// expect execute command
		inputLine = inputFile.nextNonEmptyLine();
		data = inputLine.parse("A7");
		if (!"EXECUTE".equals((String) data[0])) {
			LOGGER.log(Level.SEVERE, String.format(unexpectedLogLine, inputLine.getLine(), inputLine.getLineNo()));
			throw new ParseException(errorString, inputLine.getLineNo());
		}
	}

	// contains land segment information
	private class LandSegmentInfo {
		int segmentNumber;
		double[] tributaryArea;

		public LandSegmentInfo(InputLine inputLine) throws ParseException {

			assert inputLine != null;

			Object[] data;
			data = inputLine.parse("A3,13X,I8,3F8");
			segmentNumber = (int) data[1];
			tributaryArea = new double[3];
			for (int i = 2; i < 5; i++)
				tributaryArea[i - 2] = (double) data[i];

			for (double area : tributaryArea) {
				if (area < 0) {
					LOGGER.log(Level.SEVERE, "Negative land segment area encountered");
					throw new RuntimeException("Negative land segment area encountered");
				}
			}
		}

		public String asString() {
			return String.format("%-16s%8d%8.3g%8.3g%8.3g", "SEG#,AREA=", segmentNumber, tributaryArea[0],
					tributaryArea[1], tributaryArea[2]);
		}

		public LandSegmentAreaInfo getAreaInfo() {
			return new LandSegmentAreaInfo(tributaryArea);
		}
	}

	// contains routing info information
	private class RoutingInfo {
		public String[] stopStore;
		public double[] routingConstants;
		public double[] maximumQs;
		public double[] split;

		public RoutingInfo(InputFile inputFile) throws ParseException {

			assert inputFile != null;

			Object[] data;
			InputLine inputLine;

			String errorString = "Error parsing routing info";
			String unexpectedLogLine = "Unexpected line: %s, line number %d";

			// ROUTING
			inputLine = inputFile.nextNonEmptyLine();
			data = inputLine.parse("A7");
			if (!"ROUTING".equals((String) data[0])) {
				LOGGER.log(Level.SEVERE, String.format(unexpectedLogLine, inputLine.getLine(), inputLine.getLineNo()));
				throw new ParseException(errorString, inputLine.getLineNo());
			}

			// STOPSTORE
			inputLine = inputFile.nextNonEmptyLine();
			data = inputLine.parse("16X,3(5X,A3)");
			stopStore = new String[3];
			for (int i = 0; i < 3; i++) {
				stopStore[i] = ((String) data[i]).trim();
				if (!("YES".equals(stopStore[i]) || "NO".equals(stopStore[i]))) {
					LOGGER.log(Level.SEVERE, String.format("Expected YES/NO: %s", stopStore[i]));
					throw new ParseException(errorString, inputLine.getLineNo());
				}
			}

			// RK(*)
			inputLine = inputFile.nextNonEmptyLine();
			data = inputLine.parse("16X,3F8");
			routingConstants = new double[3];
			for (int i = 0; i < 3; i++) {
				routingConstants[i] = (double) data[i];
				if (routingConstants[i] < 0) {
					LOGGER.log(Level.SEVERE, "Negative routing constant encountered");
					throw new RuntimeException("Negative routing constant encountered");
				}
			}

			// QMAX(*)
			inputLine = inputFile.nextNonEmptyLine();
			data = inputLine.parse("16X,3F8");
			maximumQs = new double[3];
			for (int i = 0; i < 3; i++) {
				maximumQs[i] = (double) data[i];
				if (maximumQs[i] < 0) {
					LOGGER.log(Level.SEVERE, "Negative QMAX value encountered");
					throw new RuntimeException("Negative QMAX value encountered");
				}
			}

			// SPLIT(*)
			inputLine = inputFile.nextNonEmptyLine();
			data = inputLine.parse("16X,3F8");
			split = new double[3];
			for (int i = 0; i < 3; i++) {
				split[i] = (double) data[i];
				if (split[i] < 0) {
					LOGGER.log(Level.SEVERE, "Negative QSPLIT value encountered");
					throw new RuntimeException("Negative QSPLIT value encountered");
				}
			}
		}

		public String[] asString() {

			LinkedList<String> stringList = new LinkedList<String>();

			stringList.add(String.format("%-7s", "ROUTING"));
			stringList.add(String.format("%-16s%8s%8s%8s", "STOPSTORE=", stopStore[0], stopStore[1], stopStore[2]));
			stringList.add(String.format("%-16s%8g%8g%8g", "RK(*)", routingConstants[0], routingConstants[1],
					routingConstants[2]));
			stringList.add(String.format("%-16s%8g%8g%8g", "QMAX(*)", maximumQs[0], maximumQs[1], maximumQs[2]));
			stringList.add(String.format("%-16s%8g%8g%8g", "SPLIT(*)", split[0], split[1], split[2]));

			String[] asString = new String[stringList.size()];
			return stringList.toArray(asString);
		}

		public ReservoirInfo[] getReservoirInfo() {

			ReservoirInfo[] reservoirInfo = new ReservoirInfo[3];

			boolean store;

			for (int i = 0; i < 3; i++) {
				if (stopStore[i].equals("YES")) {
					store = true;
				} else {
					store = false;
				}
				reservoirInfo[i] = new ReservoirInfo(routingConstants[i], maximumQs[i], split[i], initialFlows[i],
						store);
			}

			return reservoirInfo;
		}
	}

	/**
	 * Returns this special contributing area block represented as an array of
	 * strings
	 *
	 * @return string representation of this block
	 */
	public String[] asString() {
		LinkedList<String> stringList = new LinkedList<String>();

		// beginning of sca block
		stringList.add(String.format("%-3s", "SCA"));

		// sca number & total sca area
		stringList.add(String.format("%-8s", "ANALYSIS"));
		stringList.add(String.format("%-16s%8d%8.4g", "SCA#,AREA=", getSCANumber(), scaArea));

		// land type breakdown
		stringList.add(String.format("%-5s", "LANDS"));
		for (LandSegmentInfo segment : landSegments.values())
			stringList.add(segment.asString());
		stringList.add(String.format("%-3s", "END"));

		// sanitary info
		stringList.add(String.format("%-8s", "SANITARY"));
		stringList.add(String.format("%-16s%8d%8.0f", "SNA#,PE=", sanitaryInfoSet, popEquivalent));
		stringList.add(String.format("%-3s", "END"));

		// initial flows block
		stringList.add(String.format("%-13s", "INITIAL FLOWS"));
		stringList.add(
				String.format("%-16s%8.4g%8.4g%8.4g", "OUT1(*)", initialFlows[0], initialFlows[1], initialFlows[2]));

		// routing info
		for (String s : routingInfo.asString())
			stringList.add(s);

		// output
		stringList.add(String.format("%-6s", "OUTPUT"));

		// treatment
		stringList.add(String.format("%8s%-72s", "", sewerParts));

		// overflow
		stringList.add(String.format("%8s%-72s", "", overflowParts));

		// end of SCA block
		stringList.add(String.format("%-7s", "EXECUTE"));

		String[] asString = new String[stringList.size()];
		return stringList.toArray(asString);
	}

	/**
	 * Returns the overflow output paths
	 *
	 * @return overflow output paths
	 */
	public OutputDSSPathStrings getOverflowPaths() {
		return overflowPaths;
	}

	/**
	 * Returns the population of this SCA
	 *
	 * @return SCA population
	 */
	public double getSCAPopulation() {
		return popEquivalent;
	}

	/**
	 * Returns the information to initialize the reservoirs for this special
	 * contributing area
	 *
	 * @return reservoir information
	 */
	public ReservoirInfo[] getReservoirInfo() {
		return routingInfo.getReservoirInfo();
	}

	/**
	 * Returns the sanitary info set number
	 *
	 * @return sanitary info set number
	 */
	public int getSanitaryInfoSet() {
		return sanitaryInfoSet;
	}

	/**
	 * Returns the area associated with this SCA
	 *
	 * @return area
	 */
	public double getSCAArea() {
		return scaArea;
	}

	/**
	 * Returns the special contributing area number for this input block
	 *
	 * @return SCA number
	 */
	public int getSCANumber() {
		return scaNumber;
	}

	/**
	 * Returns the LandSegmentAreaInfo for a land segment
	 *
	 * @param segmentNumber
	 *            land segment number
	 *
	 * @return land segment area info
	 */
	public LandSegmentAreaInfo getSegmentArea(int segmentNumber) {
		return landSegments.get(segmentNumber).getAreaInfo();
	}

	/**
	 * Returns the sewer output paths
	 *
	 * @return sewer output paths
	 */
	public OutputDSSPathStrings getSewerPaths() {
		return sewerPaths;
	}

	/**
	 * Returns the land segment numbers in this special contributing area
	 *
	 * @return land segment numbers
	 */
	public int[] landSegmentNumbers() {
		return landSegments.keySet().stream().mapToInt(i -> i).toArray();
	}

}
