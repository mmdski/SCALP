package gov.usgs.scalp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * User log
 */
public class SCALPUserLog {

	private final ArrayList<String> stringList = new ArrayList<>();

	/**
	 * Constructs a user log instance
	 */
	public SCALPUserLog() {
	}

	private void log(String... stringList) {

		if (stringList == null)
			throw new NullPointerException();

		this.stringList.addAll(Arrays.asList(stringList));
		this.stringList.add("");
	}

	/**
	 * Logs the command line arguments
	 *
	 * @param args
	 *            command line arguments
	 */
	public void logArgs(String[] args) {

		if (args == null)
			throw new NullPointerException();

		String[] argsDescriptions = { "Input text file path", "Input DSS file path", "Output DSS file path",
				"User log file path" };

		log("*** Command line arguments ***");

		for (int i = 0; i < args.length; i++) {
			log(argsDescriptions[i] + ":", args[i]);
		}
	}

	/**
	 * Logs an error message
	 *
	 * @param message
	 *            error message
	 */
	public void logError(Exception e) {

		if (e == null)
			throw new NullPointerException();

		String message = e.getMessage();

		if (message == null) {
			log("Error with no message");
			StackTraceElement[] elements = e.getStackTrace();
			for (StackTraceElement element : elements) {
				log(element.toString());
			}
		} else
			log("Error: " + message);
	}

	/**
	 * Logs the input file header
	 */
	public void logInput() {
		log("*** Parsing text input file ***");
	}

	/**
	 * Logs the input file as parsed by SCALP
	 *
	 * @param scalpLines
	 *            input file lines
	 */
	public void logInput(String[] scalpLines) {

		if (scalpLines == null)
			throw new NullPointerException();

		log(scalpLines);
	}

	/**
	 * Logs the watershed initialization header
	 */
	public void logWatershed() {
		log("*** Initializing watershed block ***");
	}

	/**
	 * Logs the land segments in the watershed block
	 *
	 * @param watershed
	 *            watershed
	 */
	public void logWatershed(Watershed watershed) {

		if (watershed == null)
			throw new NullPointerException();

		String improPath;
		String olfroPath;
		String subroPath;

		for (int segmentNumber : watershed.getSegmentNumbers()) {

			improPath = watershed.getLandSegment(segmentNumber).getImproPath();
			olfroPath = watershed.getLandSegment(segmentNumber).getOlfroPath();
			subroPath = watershed.getLandSegment(segmentNumber).getSubroPath();

			log("Land segment " + segmentNumber + ":", "    Impervious runoff input DSS path:", "    " + improPath,
					"    Overland flow runoff input DSS path:", "    " + olfroPath,
					"    Subsurface runoff input DSS path:", "    " + subroPath);
		}
	}

	/**
	 * Logs the sanitary initialization header
	 */
	public void logSanitary() {
		log("*** Initializing sanitary input block ***");
	}

	/**
	 * Logs the SCA initialization header for the specified SCA
	 *
	 * @param scaNumber
	 *            SCA number
	 */
	public void logSCA(int scaNumber) {
		log("*** Initializing SCA " + scaNumber + " ***");
	}

	/**
	 * Logs the SCA execution header for the specified SCA
	 *
	 * @param scaNumber
	 *            SCA number
	 */
	public void logExecution(int scaNumber) {
		log("*** Commencing execution on SCA " + scaNumber + " ***");
	}

	/**
	 * Logs the SCA writing output header for the specified SCA
	 *
	 * @param scaNumber
	 *            SCA number
	 */
	public void logWritingOutput(int scaNumber) {
		log("*** Writing output for SCA " + scaNumber + " ***");
	}

	/**
	 * Logs the DSS output file paths for the specified SCA
	 *
	 * @param sca
	 *            special contributing area
	 */
	public void logWritingOutput(SpecialContributingArea sca) {

		if (sca == null)
			throw new NullPointerException();

		String ovf_infil = sca.getOverflowPaths().getInfiltration();
		log("Overflow infiltration output dss path:", ovf_infil);

		String ovf_inflo = sca.getOverflowPaths().getInflow();
		log("Overflow inflow output dss path:", ovf_inflo);

		String ovf_sanit = sca.getOverflowPaths().getSanitary();
		log("Overflow sanitary output dss path:", ovf_sanit);

		String ovf_total = sca.getOverflowPaths().getTotal();
		log("Overflow total output dss path:", ovf_total);

		String stp_infil = sca.getSewerPaths().getInfiltration();
		log("Sewer infiltration output dss path:", stp_infil);

		String stp_inflo = sca.getSewerPaths().getInflow();
		log("Sewer inflow output dss path:", stp_inflo);

		String stp_sanit = sca.getSewerPaths().getSanitary();
		log("Sewer sanitary output dss path:", stp_sanit);

		String stp_total = sca.getSewerPaths().getTotal();
		log("Sewer total output dss path:", stp_total);
	}

	/**
	 * Logs the success message
	 */
	public void logSuccess() {
		log("*** SCALP has been executed on all special contributing areas ***");
	}

	/**
	 * Writes the user log to a text file
	 *
	 * @param filePathStr
	 *            user log path name
	 * @throws gov.usgs.scalp.SCALPUserLog.UserLogException
	 *             if writing error is encountered
	 */
	public void writeLog(String filePathStr) throws UserLogException {

		if (filePathStr == null)
			throw new NullPointerException();

		BufferedWriter writer = null;

		try {
			File userLogFile = new File(filePathStr);
			writer = new BufferedWriter(new FileWriter(userLogFile));

			for (int i = 0; i < stringList.size(); i++) {
				writer.write(stringList.get(i));
				writer.newLine();
			}

		} catch (IOException e) {
			throw new UserLogException("Failed to write text to user log file.", e);

		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				throw new UserLogException("Failed to close the user log writer.", e);
			}
		}
	}

	/**
	 * User log exception
	 */
	public class UserLogException extends Exception {

		/**
		 * Constructs an exception instance
		 *
		 * @param message
		 *            error message
		 * @param cause
		 *            original exception
		 */
		public UserLogException(String message, Throwable cause) {
			super(message, cause);
		}

	}

}
