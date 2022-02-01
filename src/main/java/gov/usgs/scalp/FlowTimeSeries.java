package gov.usgs.scalp;

import java.util.TreeMap;

/**
 * Time series containing constituent flow values
 */
public abstract class FlowTimeSeries {

	private TreeMap<DateTime, FlowValue> map;

	public FlowTimeSeries(long[] times, FlowValue[] values) {

		if (times == null || values == null)
			throw new NullPointerException();

		int nObs = times.length;

		if (nObs != values.length)
			throw new RuntimeException("times.length must be equal to values.length");

		map = new TreeMap<DateTime, FlowValue>();

		for (int i = 0; i < nObs; i++) {
			map.put(new DateTime(times[i]), values[i]);
		}

	}

	/**
	 * Initializes an empty ConstituentFlowTimeSeries.
	 */
	public FlowTimeSeries() {
		map = new TreeMap<DateTime, FlowValue>();
	}

	@Override
	public boolean equals(Object other) {

		if (this == other)
			return true;

		if (other == null)
			return false;

		if (getClass() != other.getClass())
			return false;

		FlowTimeSeries timeSeries = (FlowTimeSeries) other;

		return map.equals(timeSeries.map);
	}

	/**
	 * Returns the infiltration flow time series.
	 *
	 * @return infiltration flow time series
	 */
	public TimeSeries getInfiltration() {

		TimeSeries infiltration = new TimeSeries();
		FlowValue flow;

		for (DateTime t : getTimes()) {
			flow = getValue(t);
			infiltration.put(t, flow.getInfiltration());
		}

		return infiltration;
	}

	/**
	 * Returns the sanitary flow time series.
	 *
	 * @return sanitary flow time series
	 */
	public TimeSeries getSanitary() {

		TimeSeries sanitary = new TimeSeries();
		FlowValue flow;

		for (DateTime t : getTimes()) {
			flow = getValue(t);
			sanitary.put(t, flow.getSanitary());
		}

		return sanitary;
	}

	/**
	 * Returns the storm water flow time series.
	 *
	 * @return storm water flow time series
	 */
	public TimeSeries getStormWater() {

		TimeSeries stormWater = new TimeSeries();
		FlowValue flow;

		for (DateTime t : getTimes()) {
			flow = getValue(t);
			stormWater.put(t, flow.getStormWater());
		}

		return stormWater;
	}

	/**
	 * Returns a time series with the total flow of flow observations as the values.
	 *
	 * @return flow time series
	 */
	public TimeSeries getTotalFlow() {

		TimeSeries totalFlow = new TimeSeries();
		FlowValue flow;

		for (DateTime t : getTimes()) {
			flow = getValue(t);
			totalFlow.put(t, flow.getTotal());
		}

		return totalFlow;
	}

	/**
	 * Returns the number of observations in this time series
	 *
	 * @return number of observations
	 */
	public int nObs() {
		return map.size();
	}

	/**
	 * Inserts an observation in this time series. If an observation at {@code time}
	 * exists, the value will be replaced by {@code value}.
	 *
	 * @param time
	 *            the observation time
	 * @param value
	 *            the observed value
	 */
	public void put(DateTime time, FlowValue value) {

		if (time == null || value == null)
			throw new NullPointerException();

		map.put(time, (FlowValue) value);
	}

	/**
	 * Returns the observation times of this time series in a sorted array.
	 *
	 * @return observation times
	 */
	public DateTime[] getTimes() {

		int nObs = nObs();
		DateTime[] times = new DateTime[nObs];

		times = map.keySet().toArray(times);

		return times;
	}

	/**
	 * Returns the value observed at a time. If {@code time} is not a valid
	 * observation time, the behavior is undefined.
	 *
	 * @param time
	 *            observation time
	 *
	 * @return observed value
	 */
	public FlowValue getValue(DateTime time) {

		if (time == null)
			throw new NullPointerException();

		return map.get(time);
	}

	/**
	 * Returns the observed values of this time series in an array sorted according
	 * to the order of the observed times.
	 *
	 * @return observed values
	 */
	public FlowValue[] getValues() {

		int nObs = nObs();
		FlowValue[] mapValues = new FlowValue[nObs];

		return map.values().toArray(mapValues);
	}

}
