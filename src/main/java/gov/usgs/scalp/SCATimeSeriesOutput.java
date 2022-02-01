package gov.usgs.scalp;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Special contributing area time series output
 */
public class SCATimeSeriesOutput {

	private FlowTimeSeries[] routedTimeSeries;
	private OutputDSSPathStrings sewerPaths;
	private OutputDSSPathStrings overflowPaths;

	private int scaNumber;

	private static final Logger LOGGER = Logger.getLogger("gov.usgs.scalp.SCATimeSeriesOutput");

	/**
	 * Constructs a new time series output
	 * <p>
	 * Routes SCA flows on construction.
	 *
	 * @param sca
	 *            special contributing area
	 */
	public SCATimeSeriesOutput(SpecialContributingArea sca) {

		if (sca == null)
			throw new NullPointerException();

		scaNumber = sca.getSCANumber();

		LOGGER.log(Level.INFO, "Initializing time series output for SCA number " + scaNumber);

		routedTimeSeries = sca.routeFlows();
		sewerPaths = sca.getSewerPaths();
		overflowPaths = sca.getOverflowPaths();
	}

	/**
	 * Writes time series output to a DSS file
	 *
	 * @param dssFile
	 *            output DSS file
	 */
	public void writeTimeSeries(DSS dssFile) {

		if (dssFile == null)
			throw new NullPointerException();

		LOGGER.log(Level.INFO, "Writing output of SCA number " + scaNumber + " to DSS file " + dssFile.getFilePath());

		FlowTimeSeries sewerFlow = routedTimeSeries[0];
		dssFile.writeTimeSeries(sewerPaths.getTotal(), sewerFlow.getTotalFlow());
		dssFile.writeTimeSeries(sewerPaths.getInfiltration(), sewerFlow.getInfiltration());
		dssFile.writeTimeSeries(sewerPaths.getInflow(), sewerFlow.getStormWater());
		dssFile.writeTimeSeries(sewerPaths.getSanitary(), sewerFlow.getSanitary());

		FlowTimeSeries overflow = routedTimeSeries[1];
		dssFile.writeTimeSeries(overflowPaths.getTotal(), overflow.getTotalFlow());
		dssFile.writeTimeSeries(overflowPaths.getInfiltration(), overflow.getInfiltration());
		dssFile.writeTimeSeries(overflowPaths.getInflow(), overflow.getStormWater());
		dssFile.writeTimeSeries(overflowPaths.getSanitary(), overflow.getSanitary());
	}
}
