package gov.usgs.scalp;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Land segment
 */
public class LandSegment {

	private LandSegmentAreaInfo areaInfo;
	private LandSegmentTS timeSeries;

	// inches per hour to feet per second
	private static final double INPHR_TO_FPS = 1.0 / (12.0 * 60.0 * 60.0);

	private static final Logger LOGGER = Logger.getLogger("gov.usgs.scalp.LandSegment");

	/**
	 * Constructs a new land segment
	 *
	 * @param areaInfo
	 *            land segment area info
	 * @param timeSeries
	 *            land segment runoff time series
	 */
	public LandSegment(LandSegmentAreaInfo areaInfo, LandSegmentTS timeSeries) {

		if (areaInfo == null || timeSeries == null)
			throw new NullPointerException();

		this.areaInfo = areaInfo;
		this.timeSeries = timeSeries;
	}

	/**
	 * Returns a sewer infiltration time series
	 *
	 * @return sewer infiltration time series in cubic feet per second
	 */
	public TimeSeries infiltration() {

		// subsurface area is in sq ft
		double subsurfaceArea = areaInfo.getSubsurface();

		// runoff is in inches per hour
		TimeSeries subsurfaceRunoff = timeSeries.subsurfaceRunoff();

		DateTime[] times = subsurfaceRunoff.getTimes();
		double[] runoff = subsurfaceRunoff.getValues();

		int nTimes = times.length;

		double[] flow = new double[nTimes];

		for (int i = 0; i < nTimes; i++) {
			// convert runoff from inches per hour to feet per second
			flow[i] = INPHR_TO_FPS * subsurfaceArea * runoff[i];
		}

		return new TimeSeries(times, flow);
	}

	/**
	 * Returns a storm water flow time series
	 *
	 * @return storm water flow time series in cubic feet per second
	 */
	public TimeSeries stormWaterFlow() {

		// area is in sq ft
		double imperviousArea = areaInfo.getImpervious();
		double perviousArea = areaInfo.getOverland();

		TimeSeries imperviousRunoffTS = timeSeries.imperviousRunoff();
		TimeSeries overlandRunoffTS = timeSeries.overlandRunoff();

		DateTime[] imperviousTimes = imperviousRunoffTS.getTimes();
		DateTime[] overlandTimes = overlandRunoffTS.getTimes();
		double[] imperviousRunoff = imperviousRunoffTS.getValues();
		double[] overlandRunoff = overlandRunoffTS.getValues();

		int nTimes = imperviousTimes.length;

		double[] flow = new double[nTimes];

		if (imperviousTimes.length != overlandTimes.length) {
			LOGGER.log(Level.SEVERE, "Impervious and overland runoff time series are different lengths");
			throw new RuntimeException();
		}

		for (int i = 0; i < nTimes; i++) {
			if (imperviousTimes[i] != overlandTimes[i]) {
				LOGGER.log(Level.SEVERE, "Impervious and overland runoff observation times do not match");
				throw new RuntimeException();
			}
			flow[i] = INPHR_TO_FPS * (imperviousArea * imperviousRunoff[i] + perviousArea * overlandRunoff[i]);
		}

		return new TimeSeries(imperviousTimes, flow);
	}

}
