package gov.usgs.scalp;

import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Watershed input block
 */
public class WatershedInputBlock {

	private DateTime startDateTime;
	private DateTime endDateTime;
	private int outputLevel;
	private LinkedHashMap<Integer, String> landSegmentPaths;
	private final static Logger LOGGER = Logger.getLogger("gov.usgs.scalp.WatershedInputBlock");

	/**
	 * Constructor for watershed input block
	 *
	 * Parses watershed input block contained in a SCALP input file
	 *
	 * @param inputFile
	 *            SCALP input file
	 * @throws ParseException
	 *             if an unrecoverable parsing error is encountered
	 */
	public WatershedInputBlock(InputFile inputFile) throws ParseException {

		if (inputFile == null)
			throw new NullPointerException();

		inputFile.setCurrentLineNo(1);
		Object[] data;
		String unexpectedLogLine = "Unexpected line: %s, line number %d";
		String errorString = "Error parsing WATERSHED input block";

		// WATERSHED
		InputLine inputLine = inputFile.nextNonEmptyLine();
		data = inputLine.parse("A9");
		if (!"WATERSHED".equals((String) data[0])) {
			LOGGER.log(Level.SEVERE, String.format(unexpectedLogLine, inputLine.getLine(), inputLine.getLineNo()));
			throw new ParseException(errorString, inputLine.getLineNo());
		}

		// TIME SPAN
		inputLine = inputFile.nextNonEmptyLine();
		data = inputLine.parse("A9");
		if (!"TIME SPAN".equals((String) data[0])) {
			LOGGER.log(Level.SEVERE, String.format(unexpectedLogLine, inputLine.getLine(), inputLine.getLineNo()));
			throw new ParseException(errorString, inputLine.getLineNo());
		}

		// starting date
		inputLine = inputFile.nextNonEmptyLine();
		data = inputLine.parse("16X,3I8");
		int startYear = (int) data[0];
		int startMonth = (int) data[1];
		int startDay = (int) data[2];
		startDateTime = new DateTime(startYear, startMonth, startDay, 1, 0);

		// ending date
		inputLine = inputFile.nextNonEmptyLine();
		data = inputLine.parse("16X,3I8");
		int endYear = (int) data[0];
		int endMonth = (int) data[1];
		int endDay = (int) data[2];
		endDateTime = new DateTime(endYear, endMonth, endDay, 24, 0);

		// output level header
		inputLine = inputFile.nextNonEmptyLine();
		data = inputLine.parse("A12");
		if (!"OUTPUT LEVEL".equals((String) data[0])) {
			LOGGER.log(Level.SEVERE, String.format(unexpectedLogLine, inputLine.getLine(), inputLine.getLineNo()));
			throw new ParseException(errorString, inputLine.getLineNo());
		}

		// output logging level
		inputLine = inputFile.nextNonEmptyLine();
		data = inputLine.parse("11X,I1");
		outputLevel = (int) data[0];

		// data/land segment header
		inputLine = inputFile.nextNonEmptyLine();
		data = inputLine.parse("A9");
		if (!"DATA".equals((String) data[0])) {
			LOGGER.log(Level.SEVERE, String.format(unexpectedLogLine, inputLine.getLine(), inputLine.getLineNo()));
			throw new ParseException(errorString, inputLine.getLineNo());
		}

		// land segments
		int segmentNumber;
		String dssPath;
		landSegmentPaths = new LinkedHashMap<>();

		// TODO: exit parse loop when END is found instead of on a ParseException
		// while loop exists when inputLine throws a ParseException
		while (true) {
			inputLine = inputFile.nextNonEmptyLine();
			try {
				data = inputLine.parse("I8,A72");
				segmentNumber = (int) data[0];

				// check for duplicated segment number
				if (landSegmentPaths.containsKey(segmentNumber)) {
					LOGGER.log(Level.SEVERE, String.format("Duplicate land segment number: %2d", segmentNumber));
					throw new RuntimeException("Duplicate land segment numbers encountered");
				}

				dssPath = (String) data[1];
				landSegmentPaths.put(segmentNumber, dssPath);
			} catch (ParseException e) {
				break;
			}
		}

		if (landSegmentPaths.isEmpty()) {
			LOGGER.log(Level.SEVERE,
					String.format("No land segments parsed. %2d: %s", inputLine.getLineNo(), inputLine.getLine()));
			throw new ParseException(errorString, inputLine.getLineNo());
		}

		// end of block statement
		data = inputLine.parse("A3");
		if (!"END".equals((String) data[0])) {
			LOGGER.log(Level.SEVERE, String.format(unexpectedLogLine, inputLine.getLine(), inputLine.getLineNo()));
			throw new ParseException(errorString, inputLine.getLineNo());
		}
	}

	/**
	 * Returns this watershed input block represented as an array of strings
	 *
	 * @return string representation of this block
	 */
	public String[] asString() {
		LinkedList<String> stringList = new LinkedList<>();

		stringList.add(String.format("%9s", "WATERSHED"));
		stringList.add(String.format("%9s", "TIME SPAN"));
		stringList.add(String.format("%-16s%8d%8d%8d", "STARTING DATE", getStartDate().getYear(),
				getStartDate().getMonth(), getStartDate().getDayOfMonth()));
		stringList.add(String.format("%-16s%8d%8d%8d", "ENDING DATE", getEndDate().getYear(), getEndDate().getMonth(),
				getEndDate().getDayOfMonth()));
		stringList.add(String.format("%-12s", "OUTPUT LEVEL"));
		stringList.add(String.format("%11s%1d", "", getOutputLevel()));
		stringList.add(String.format("%-9s", "DATA"));

		for (int segNo : getSegmentNumbers()) {
			stringList.add(String.format("%8d%-72s", segNo, getDssPath(segNo)));
		}

		stringList.add(String.format("%3s", "END"));

		String[] asString = new String[stringList.size()];

		return stringList.toArray(asString);
	}

	/**
	 * Returns the end date of the simulation
	 *
	 * @return simulation end date
	 */
	public DateTime getEndDate() {
		return endDateTime;
	}

	/**
	 * Returns a DSS path
	 *
	 * @param segmentNumber
	 *            segment number
	 * @return DSS path
	 */
	public String getDssPath(int segmentNumber) {
		return landSegmentPaths.get(segmentNumber);
	}

	/**
	 * Returns the output level of the simulation
	 *
	 * @return output level
	 */
	public int getOutputLevel() {
		return outputLevel;
	}

	/**
	 * Returns the segment numbers in the simulation
	 *
	 * @return array of segment numbers
	 */
	public int[] getSegmentNumbers() {
		return landSegmentPaths.keySet().stream().mapToInt(i -> i).toArray();
	}

	/**
	 * Returns the start date of the simulation
	 *
	 * @return simulation start date
	 */
	public DateTime getStartDate() {
		return startDateTime;
	}

}
