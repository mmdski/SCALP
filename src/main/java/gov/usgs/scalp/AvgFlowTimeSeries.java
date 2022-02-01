package gov.usgs.scalp;

/**
 * Hour-averaged flow time series
 */
public class AvgFlowTimeSeries extends FlowTimeSeries {

	/**
	 * Returns an instantaneous flow time series from this average time series
	 *
	 * @param timeStep
	 *            instantaneous time step in minutes
	 * @return instantaneous flow time series
	 */
	public InstFlowTimeSeries getInstantaneous(int timeStep) {

		// time step must be positive, non-zero, and less than 60 minutes
		if (timeStep <= 0 || timeStep > 60) {
			throw new IllegalArgumentException();
		}

		DateTime[] times = getTimes();
		FlowValue[] values = getValues();

		int nTimes = times.length;
		DateTime dateTime;
		DateTime nextHour;

		InstFlowTimeSeries instTimeSeries = new InstFlowTimeSeries();

		// the quantities in the hourly-averaged time series are representative of the
		// previous hour. make the instantaneous time series representative of the top
		// of the hour.
		for (int i = 0; i < nTimes; i++) {
			nextHour = times[i];
			dateTime = times[i].subtractHours(1);
			while (dateTime.lessThan(nextHour)) {
				instTimeSeries.put(dateTime, new FlowValue(values[i]));
				dateTime = dateTime.addMinutes(timeStep);
			}
		}

		return instTimeSeries;
	}

}
