package gov.usgs.scalp;

/**
 * Instantaneous-values flow time series
 */
public class InstFlowTimeSeries extends FlowTimeSeries {

	/**
	 * Returns an hourly-averaged flow time series
	 *
	 * @return hourly-averaged flow time series
	 */
	public AvgFlowTimeSeries getAveraged() {

		DateTime[] times = getTimes();
		FlowValue[] values = getValues();

		int nTimes = times.length;
		DateTime nextHour;

		AvgFlowTimeSeries avgTimeSeries = new AvgFlowTimeSeries();
		int nValues;

		FlowValue sum;
		FlowValue average;

		nextHour = times[0].getNextHour();
		sum = values[0];
		nValues = 1;
		for (int i = 1; i < nTimes; i++) {

			// case 1
			// times[i] is before nextHour
			if (times[i].lessThan(nextHour)) {
				sum = sum.add(values[i]); // add values[i] to the sum
				nValues++; // increment the number of summed values

				// case 2
				// times[i] is at or after nextHour
			} else {
				average = sum.divide((double) nValues); // average the sum of flow values

				// add the average to the average time series for the time nextHour
				avgTimeSeries.put(nextHour, average);
				sum = values[i]; // set the sum to values[i]
				nValues = 1; // set the number of values to 1
				nextHour = times[i].getNextHour(); // set the next hour
			}
		}

		// average and add remaining flow
		average = sum.divide((double) nValues);
		avgTimeSeries.put(nextHour, average);

		return avgTimeSeries;
	}

}
