package gov.usgs.scalp;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Special contributing area
 */
public class SpecialContributingArea {

	private int scaNumber;
	private LandSegment[] landSegments;
	private SanitaryFlowSet sanitaryFlowSet;
	private ReservoirInfo[] reservoirInfo;

	private OutputDSSPathStrings sewerPaths;
	private OutputDSSPathStrings overflowPaths;

	private SingleLinearReservoir lateralReservoir;
	private SingleLinearReservoir subMainReservoir;
	private SingleLinearReservoir mainReservoir;

	private double scaPopulation;

	private static final Logger LOGGER = Logger.getLogger("gov.usgs.scalp.SpecialContributingArea");

	/**
	 * Constructs a special contributing area
	 *
	 * @param scaInputBlock
	 *            special contributing area input block
	 * @param watershed
	 *            watershed
	 * @param sanitary
	 *            sanitary
	 */
	public SpecialContributingArea(SCAInputBlock scaInputBlock, Watershed watershed, Sanitary sanitary) {

		if (scaInputBlock == null || watershed == null || sanitary == null)
			throw new NullPointerException();

		scaNumber = scaInputBlock.getSCANumber();

		LOGGER.log(Level.INFO, "Initializing special contributing area number " + scaNumber);

		int infoSetNo = scaInputBlock.getSanitaryInfoSet();
		sanitaryFlowSet = sanitary.getFlowSet(infoSetNo);

		landSegments = initLandSegments(scaInputBlock, watershed);

		scaPopulation = scaInputBlock.getSCAPopulation();

		reservoirInfo = scaInputBlock.getReservoirInfo();

		sewerPaths = scaInputBlock.getSewerPaths();
		overflowPaths = scaInputBlock.getOverflowPaths();
	}

	// initialize land segments
	private LandSegment[] initLandSegments(SCAInputBlock scaInputBlock, Watershed watershed) {

		assert scaInputBlock != null && watershed != null;

		int[] segmentNumbers = scaInputBlock.landSegmentNumbers();
		int nLandSegments = segmentNumbers.length;
		LandSegment[] landSegments = new LandSegment[nLandSegments];

		LandSegmentAreaInfo areaInfo;
		LandSegmentTS timeSeries;

		for (int i = 0; i < nLandSegments; i++) {
			areaInfo = scaInputBlock.getSegmentArea(segmentNumbers[i]);
			timeSeries = watershed.getLandSegment(segmentNumbers[i]);
			if (timeSeries == null) {
				String errorMessage = String.format("Land segment number %d does not exist in watershed input block",
						segmentNumbers[i]);
				LOGGER.log(Level.SEVERE, errorMessage);
				throw new RuntimeException(errorMessage);
			}
			landSegments[i] = new LandSegment(areaInfo, timeSeries);
		}
		return landSegments;
	}

	// initialize flow time series
	private AvgFlowTimeSeries initTimeSeries(LandSegment[] landSegments, SanitaryFlowSet sanitaryFlowSet) {

		assert landSegments != null && sanitaryFlowSet != null;

		LOGGER.log(Level.INFO, "Initializing time series for SCA number " + getSCANumber());

		AvgFlowTimeSeries flowTimeSeries = new AvgFlowTimeSeries();

		TimeSeries infiltrationTS = landSegments[0].infiltration();
		TimeSeries stormWaterTS = landSegments[0].stormWaterFlow();

		for (int i = 1; i < landSegments.length; i++) {
			infiltrationTS = infiltrationTS.add(landSegments[i].infiltration());
			stormWaterTS = stormWaterTS.add(landSegments[i].stormWaterFlow());
		}

		DateTime[] times = infiltrationTS.getTimes();

		double infiltration;
		double stormWater;
		double sanitary;

		for (DateTime t : times) {
			infiltration = infiltrationTS.getValue(t);
			stormWater = stormWaterTS.getValue(t);
			sanitary = sanitaryFlowSet.sanitaryFlowPerPerson(t) * scaPopulation;
			flowTimeSeries.put(t, new FlowValue(infiltration, sanitary, stormWater));
		}

		return flowTimeSeries;
	}

	/**
	 * Returns the special contributing area number
	 *
	 * @return special contributing area number
	 */
	public int getSCANumber() {
		return scaNumber;
	}

	/**
	 * Returns the sewer output paths for this SCA
	 *
	 * @return sewer output paths
	 */
	public OutputDSSPathStrings getSewerPaths() {
		return sewerPaths;
	}

	/**
	 * Returns the time series output for this SCA
	 *
	 * @return SCA time series output
	 */
	public SCATimeSeriesOutput getTimeSeriesOutput() {
		return new SCATimeSeriesOutput(this);
	}

	/**
	 * Returns the inflow time series for this special contributing area
	 *
	 * @return inflow time series
	 */
	public AvgFlowTimeSeries getInflowTimeSeries() {
		return initTimeSeries(landSegments, sanitaryFlowSet);
	}

	/**
	 * Returns the overflow output paths for this SCA
	 *
	 * @return overflow output paths
	 */
	public OutputDSSPathStrings getOverflowPaths() {
		return overflowPaths;
	}

	/**
	 * Routes flows through this special contributing area
	 *
	 * @return outflow and overflow time series routed through this special
	 *         contributing area
	 */
	public AvgFlowTimeSeries[] routeFlows() {

		int timeStep = SCALP.TIME_STEP;

		LOGGER.log(Level.INFO, "Routing flows for SCA number " + getSCANumber());

		// initialize reservoirs
		lateralReservoir = new SingleLinearReservoir(reservoirInfo[0]);
		subMainReservoir = new SingleLinearReservoir(reservoirInfo[1]);
		mainReservoir = new SingleLinearReservoir(reservoirInfo[2]);

		// initialize sewer and overflow time series
		InstFlowTimeSeries sewerTS = new InstFlowTimeSeries();
		InstFlowTimeSeries overflowTS = new InstFlowTimeSeries();

		AvgFlowTimeSeries avgInflowTS = getInflowTimeSeries();
		InstFlowTimeSeries inflowTS = avgInflowTS.getInstantaneous(timeStep);

		DateTime[] times = inflowTS.getTimes();
		int nTimes = times.length;

		FlowValue inflow;
		FlowValue overflow;

		FlowValue lateralSewer;
		FlowValue lateralOverflow;

		FlowValue subMainSewer;
		FlowValue subMainOverflow;

		FlowValue mainSewer;
		FlowValue mainOverflow;

		FlowValue[] out;

		// assume a constant time step
		double deltaT = (times[1].getTimeInMillis() - times[0].getTimeInMillis()) / 1000.0;

		for (int i = 0; i < nTimes; i++) {

			// inflow into special contributing area
			inflow = inflowTS.getValue(times[i]);

			// route flow through lateral sewer
			out = lateralReservoir.step(inflow, deltaT);
			lateralSewer = out[0];
			lateralOverflow = out[1];

			// route flow through sub main sewer
			out = subMainReservoir.step(lateralSewer, deltaT);
			subMainSewer = out[0];
			subMainOverflow = out[1];

			// route flow through main sewer
			out = mainReservoir.step(subMainSewer, deltaT);
			mainSewer = out[0];
			mainOverflow = out[1];

			// initialize new overflow for this time step and add all overflows
			overflow = new FlowValue(0, 0, 0);
			overflow = overflow.add(lateralOverflow.add(subMainOverflow.add(mainOverflow)));

			sewerTS.put(times[i], mainSewer);
			overflowTS.put(times[i], overflow);
		}

		AvgFlowTimeSeries[] routedFlow = new AvgFlowTimeSeries[2];
		routedFlow[0] = sewerTS.getAveraged();
		routedFlow[1] = overflowTS.getAveraged();

		LOGGER.log(Level.INFO, "Finished routing flows for SCA number " + getSCANumber());

		return routedFlow;
	}
}
