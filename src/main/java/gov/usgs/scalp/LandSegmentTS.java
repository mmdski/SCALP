package gov.usgs.scalp;

/**
 * SCALP input file land segment runoff time series
 */
public class LandSegmentTS {

	private InputDSSPathStrings dssPathStrings;

	private String pathParts;

	private TimeSeries imperviousRunoff;
	private TimeSeries overlandRunoff;
	private TimeSeries subsurfaceRunoff;

	/**
	 * Initialize a land segment
	 *
	 * @param inputDSS
	 *            input DSS file
	 * @param dssParts
	 *            parts A and B of a DSS path
	 * @param simulationTimes
	 *            array of simulation times
	 */
	public LandSegmentTS(DSS inputDSS, String dssParts, DateTime[] simulationTimes) {

		if (inputDSS == null || dssParts == null || simulationTimes == null)
			throw new NullPointerException();

		pathParts = dssParts;

		this.dssPathStrings = new InputDSSPathStrings(dssParts);

		int nTimes = simulationTimes.length;
		DateTime startDate = simulationTimes[0];
		DateTime endDate = simulationTimes[nTimes - 1];

		// load time series upon initialization
		TimeSeries impro = inputDSS.readTimeSeries(getImproPath(), startDate, endDate);
		if (impro == null) {
			String errorMessage = String.format("Failed to load time series from DSS path %s", getImproPath());
			throw new RuntimeException(errorMessage);
		}

		TimeSeries olfro = inputDSS.readTimeSeries(getOlfroPath(), startDate, endDate);
		if (olfro == null) {
			String errorMessage = String.format("Failed to load time series from DSS path %s", getOlfroPath());
			throw new RuntimeException(errorMessage);
		}

		TimeSeries subro = inputDSS.readTimeSeries(getSubroPath(), startDate, endDate);
		if (subro == null) {
			String errorMessage = String.format("Failed to load time series from DSS path %s", getSubroPath());
			throw new RuntimeException(errorMessage);
		}

		imperviousRunoff = new TimeSeries();
		overlandRunoff = new TimeSeries();
		subsurfaceRunoff = new TimeSeries();

		// make sure values exist for all simulation times
		for (DateTime time : simulationTimes) {
			imperviousRunoff.put(time, impro.getValue(time));
			overlandRunoff.put(time, olfro.getValue(time));
			subsurfaceRunoff.put(time, subro.getValue(time));
		}
	}

	/**
	 * Returns the impervious runoff DSS path
	 *
	 * @return DSS path
	 */
	public String getImproPath() {
		return dssPathStrings.getImproPath();
	}

	/**
	 * Returns the overland flow runoff DSS path
	 *
	 * @return DSS path
	 */
	public String getOlfroPath() {
		return dssPathStrings.getOlfroPath();
	}

	/**
	 * Returns parts A and B of the DSS path
	 *
	 * @return DSS path parts
	 */
	public String getPathParts() {
		return pathParts;
	}

	/**
	 * Returns the subsurface runoff DSS path
	 *
	 * @return DSS path
	 */
	public String getSubroPath() {
		return dssPathStrings.getSubroPath();
	}

	/**
	 * Returns the impervious runoff time series
	 *
	 * @return impervious runoff time series in inches per hour
	 */
	public TimeSeries imperviousRunoff() {
		return imperviousRunoff;
	}

	/**
	 * Returns the overland runoff time series
	 *
	 * @return overland runoff time series in inches per hour
	 */
	public TimeSeries overlandRunoff() {
		return overlandRunoff;
	}

	/**
	 * Returns the subsurface runoff time series
	 *
	 * @return subsurface runoff time series in inches per hour
	 */
	public TimeSeries subsurfaceRunoff() {
		return subsurfaceRunoff;
	}

}
