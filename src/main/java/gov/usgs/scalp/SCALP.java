package gov.usgs.scalp;

import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Special contributing area loading program
 */
public class SCALP {

	// time step in minutes
	public static final int TIME_STEP = 1;

	private SpecialContributingArea[] specialContributingAreas;
	private final static SCALPUserLog USER_LOG = new SCALPUserLog();
	private final static Logger LOGGER = Logger.getLogger("gov.usgs.scalp.SCALP");

	/**
	 * Constructs a program instance
	 *
	 * @param inputFilePath
	 *            path to SCALP input file
	 * @param dssFilePath
	 *            path to DSS input file
	 */
	public SCALP(String inputFilePath, String dssFilePath) {

		if (inputFilePath == null || dssFilePath == null)
			throw new NullPointerException();

		SCALPTextInput inputFile;
		DSS inputDSS = new DSS(dssFilePath);

		LOGGER.log(Level.INFO,
				"Initializing SCALP with text input file " + inputFilePath + " and DSS file " + dssFilePath);

		try {
			USER_LOG.logInput();
			inputFile = new SCALPTextInput(inputFilePath, inputDSS);
			USER_LOG.logInput(inputFile.scalpLines());
		} catch (ParseException e) {
			LOGGER.log(Level.SEVERE, "Unable to parse input file " + inputFilePath);
			throw new RuntimeException("Unable to parse input file");
		}

		// watershed
		USER_LOG.logWatershed();
		WatershedInputBlock watershedBlock = inputFile.getWatershedBlock();
		Watershed watershed = new Watershed(watershedBlock, inputDSS);
		USER_LOG.logWatershed(watershed);

		// sanitary
		USER_LOG.logSanitary();
		SanitaryInputBlock sanitaryBlock = inputFile.getSanitaryBlock();
		Sanitary sanitary = new Sanitary(sanitaryBlock);

		// special contributing areas
		SCAInputBlock scaBlock;
		int[] scaNumbers = inputFile.getScaNumbers();
		specialContributingAreas = new SpecialContributingArea[scaNumbers.length];

		for (int i = 0; i < scaNumbers.length; i++) {
			USER_LOG.logSCA(scaNumbers[i]);
			scaBlock = inputFile.getScaBlock(scaNumbers[i]);
			try {
				specialContributingAreas[i] = new SpecialContributingArea(scaBlock, watershed, sanitary);
			} catch (RuntimeException e) {
				String exceptionMessage = e.getMessage();
				if (exceptionMessage == null) {
					LOGGER.log(Level.SEVERE, "Failed to initialize SCA number " + scaNumbers[i]);
					throw new RuntimeException("Failed to initialize SCA number " + scaNumbers[i]);
				} else {
					LOGGER.log(Level.SEVERE,
							"Failed to initialize SCA number " + scaNumbers[i] + ": " + e.getMessage());
					throw new RuntimeException(
							"Failed to initialize SCA number " + scaNumbers[i] + ": " + e.getMessage());
				}
			}
		}
	}

	/**
	 * Returns the special contributing areas
	 *
	 * @return special contributing areas
	 */
	public SpecialContributingArea[] getSCAs() {
		return specialContributingAreas;
	}

	/**
	 * Run the special contributing area loading program
	 *
	 * @param outputDSSPath
	 *            path to output DSS file
	 */
	public void runSCALP(String outputDSSPath) {

		if (outputDSSPath == null)
			throw new NullPointerException();

		DSS outputDSSFile = new DSS(outputDSSPath);
		SCATimeSeriesOutput timeSeriesOutput;

		for (SpecialContributingArea sca : specialContributingAreas) {

			// execution
			USER_LOG.logExecution(sca.getSCANumber());
			timeSeriesOutput = sca.getTimeSeriesOutput();

			// writing output
			USER_LOG.logWritingOutput(sca.getSCANumber());
			timeSeriesOutput.writeTimeSeries(outputDSSFile);
			USER_LOG.logWritingOutput(sca);
		}
	}

	/**
	 * Special contributing area loading program entry point
	 *
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {

		if (args.length != 4)
			throw new ArrayIndexOutOfBoundsException("Incorrect number of args: expected 4, got " + args.length);

		String inputFilePath = args[0];
		String inDSSFilePath = args[1];
		String outDSSFilePath = args[2];
		String userLogFilePath = args[3];

		try {
			USER_LOG.logArgs(args);
			SCALP scalp = new SCALP(inputFilePath, inDSSFilePath);
			scalp.runSCALP(outDSSFilePath);
			USER_LOG.logSuccess();
		} catch (Exception e) {
			USER_LOG.logError(e);
			System.err.println("Failed to run SCALP: " + e.getMessage());
		} finally {
			// write user log file
			try {
				USER_LOG.writeLog(userLogFilePath);
			} catch (SCALPUserLog.UserLogException e) {
				LOGGER.log(Level.SEVERE, "Failed to write user log.");
				throw new RuntimeException("Failed to write user log.", e);
			}
		}
	}

}
