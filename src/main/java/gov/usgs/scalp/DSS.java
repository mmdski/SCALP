package gov.usgs.scalp;

import java.util.logging.Level;
import java.util.logging.Logger;

import hec.heclib.util.HecTime;
import hec.hecmath.DSSFile;
import hec.hecmath.DSSFileException;
import hec.hecmath.HecMathException;
import hec.hecmath.TimeSeriesMath;
import hec.io.TimeSeriesContainer;

/**
 * SCALP Wrapper for HEC-DSS file functionality
 */
public class DSS {

	private String filePath;
	private static final Logger LOGGER = Logger.getLogger("gov.usgs.scalp.DSS");

	/**
	 * Construct a DSS
	 *
	 * @param filePath
	 *            path to DSS file
	 */
	public DSS(String filePath) {

		if (filePath == null)
			throw new NullPointerException();

		this.filePath = filePath;
	}

	private static TimeSeries convertTSC(TimeSeriesContainer tsContainer) {

		assert tsContainer != null;

		TimeSeries timeSeries;

		if (tsContainer == null) {
			timeSeries = null;
		} else {
			timeSeries = new TimeSeries();
			HecTime hecTime = new HecTime();
			for (int time : tsContainer.times) {
				hecTime.set(time);
				timeSeries.put(new DateTime(hecTime), tsContainer.getValue(hecTime));
			}
		}

		return timeSeries;
	}

	private static DSSFile openDSS(String filePath) {

		assert filePath != null;

		LOGGER.log(Level.INFO, "Opening DSS file " + filePath);
		DSSFile dssFile = hec.hecmath.DSS.open(filePath);
		return dssFile;
	}

	private static void closeDSS(DSSFile dssFile) {

		assert dssFile != null;

		LOGGER.log(Level.INFO, "Closing DSS file " + dssFile.getFilename());
		dssFile.close();
	}

	/**
	 * Closes this DSS file.
	 * <p>
	 * Does nothing in current implementation.
	 */
	public void close() {
		;
	}

	/**
	 * Returns the path to this DSS file
	 *
	 * @return path to DSS file
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * Read a time series from a DSS instance
	 *
	 * @param pathname
	 *            DSS path name
	 * @return time series
	 */
	public TimeSeries readTimeSeries(String pathname) {

		if (pathname == null)
			throw new NullPointerException();

		DSSFile dssFile = openDSS(filePath);
		TimeSeriesMath timeSeriesMath;
		TimeSeriesContainer tsContainer;
		LOGGER.log(Level.INFO, "Reading path " + pathname + " from " + dssFile.getFilename());
		try {
			timeSeriesMath = (TimeSeriesMath) dssFile.read(pathname);
			tsContainer = timeSeriesMath.getContainer();
		} catch (HecMathException e) {
			LOGGER.log(Level.SEVERE, "Failed to read record " + pathname);
			tsContainer = null;
		} catch (DSSFileException e) {
			LOGGER.log(Level.SEVERE, "Failed to read record " + pathname);
			tsContainer = null;
		}
		closeDSS(dssFile);

		if (tsContainer == null)
			return null;
		else
			return convertTSC(tsContainer);
	}

	/**
	 * Read a time series from a DSS instance for a given time period
	 *
	 * @param pathname
	 *            DSS path name
	 * @param startDateTime
	 *            time series start time
	 * @param endDateTime
	 *            time series end time
	 * @return time series
	 */
	public TimeSeries readTimeSeries(String pathname, DateTime startDateTime, DateTime endDateTime) {

		if (pathname == null || startDateTime == null || endDateTime == null)
			throw new NullPointerException();

		DSSFile dssFile = openDSS(filePath);

		HecTime hecStartTime = startDateTime.getHecTime();
		HecTime hecEndTime = endDateTime.getHecTime();

		TimeSeriesMath timeSeriesMath;
		TimeSeriesContainer tsContainer;
		LOGGER.log(Level.INFO, "Reading path " + pathname + " from " + dssFile.getFilename() + " between "
				+ hecStartTime.toString() + " and " + hecEndTime.toString());
		try {
			timeSeriesMath = (TimeSeriesMath) dssFile.read(pathname, hecStartTime.toString(), hecEndTime.toString());
			tsContainer = timeSeriesMath.getContainer();
		} catch (HecMathException e) {
			LOGGER.log(Level.SEVERE, "Failed to read record " + pathname);
			tsContainer = null;
		} catch (DSSFileException e) {
			LOGGER.log(Level.SEVERE, "Failed to read record " + pathname);
			tsContainer = null;
		}

		closeDSS(dssFile);

		return convertTSC(tsContainer);
	}

	/**
	 * Write a time series to a DSS instance
	 * <p>
	 * Returns a write code of 0 if write is successful, -1 if an error was
	 * encountered during write.
	 *
	 * @param pathname
	 *            DSS path name
	 * @param timeSeries
	 *            time series to write
	 * @return write code
	 */
	public int writeTimeSeries(String pathname, TimeSeries timeSeries) {

		if (pathname == null || timeSeries == null)
			throw new NullPointerException();

		DSSFile dssFile = openDSS(filePath);

		LOGGER.log(Level.INFO, "Writing time series to path " + pathname + " to file " + dssFile.getFilename());
		TimeSeriesContainer tsc = new TimeSeriesContainer();

		int nObs = timeSeries.nObs();
		int[] tscTimes = new int[nObs];
		DateTime[] times = timeSeries.getTimes();

		for (int i = 0; i < nObs; i++) {
			tscTimes[i] = times[i].getHecTime().value();
		}

		tsc.fullName = pathname;
		tsc.times = tscTimes;
		tsc.values = timeSeries.getValues();
		tsc.numberValues = nObs;

		// only writing instantaneous flow in us customary units
		tsc.units = "CFS";
		tsc.type = "INST-VAL";

		try {
			dssFile.write(tsc);
		} catch (HecMathException e) {
			LOGGER.log(Level.SEVERE, "Failed to write record " + pathname);
			return -1;
		} catch (DSSFileException e) {
			LOGGER.log(Level.SEVERE, "Failed to write record " + pathname);
			return -1;
		}

		closeDSS(dssFile);

		return 0;
	}
}
