package gov.usgs.scalp;

import java.util.Arrays;
import java.util.TreeMap;

/**
 * Time series class
 */
public class TimeSeries {

	private TreeMap<DateTime, Double> map;

	/**
	 * Initializes a TimeSeries from an array of observations times and observed
	 * values. The length of each array must be equal.
	 *
	 * @param times
	 *            observation times
	 * @param values
	 *            observed values
	 */
	public TimeSeries(DateTime[] times, double[] values) {

		if (times == null || values == null)
			throw new NullPointerException();

		int nObs = times.length;

		if (nObs != values.length)
			throw new RuntimeException("times.length must be equal to values.length");

		map = new TreeMap<DateTime, Double>();

		for (int i = 0; i < nObs; i++) {
			map.put(times[i], values[i]);
		}
	}

	/**
	 * Initializes a TimeSeries from an array of observations times and observed
	 * values. The length of each array must be equal.
	 *
	 * @param times
	 *            observation times
	 * @param values
	 *            observed values
	 */
	public TimeSeries(long[] times, double[] values) {

		if (times == null || values == null)
			throw new NullPointerException();

		int nObs = times.length;

		if (nObs != values.length)
			throw new RuntimeException("times.length must be equal to values.length");

		map = new TreeMap<DateTime, Double>();

		for (int i = 0; i < nObs; i++) {
			map.put(new DateTime(times[i]), values[i]);
		}

	}

	/**
	 * Initializes an empty TimeSeries.
	 */
	public TimeSeries() {
		map = new TreeMap<DateTime, Double>();
	}

	/**
	 * Adds a time series to this time series
	 *
	 * @param other
	 *            other time series
	 * @return sum of this and other time series
	 */
	public TimeSeries add(TimeSeries other) {

		if (other == null)
			throw new NullPointerException();

		DateTime[] times = getTimes();

		if (!Arrays.equals(times, other.getTimes()))
			throw new RuntimeException("Unequal times in time series addition");

		TimeSeries result = new TimeSeries();
		double value;

		for (DateTime t : times) {
			value = getValue(t) + other.getValue(t);
			result.put(t, value);
		}

		return result;
	}

	/**
	 * Returns true of this object is equal to {@code other}, false otherwise.
	 *
	 * @return boolean
	 */
	public boolean equals(Object other) {

		if (this == other)
			return true;

		if (this.getClass() != other.getClass())
			return false;

		return this.map.equals(((TimeSeries) other).map);
	}

	/**
	 * Returns the number of observations in this time series
	 *
	 * @return int
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
	public void put(DateTime time, double value) {

		if (time == null)
			throw new NullPointerException();

		map.put(time, value);
	}

	/**
	 * Returns the observation times of this time series in a sorted array.
	 *
	 * @return array of times
	 */
	public DateTime[] getTimes() {

		int nObs = nObs();
		DateTime[] times = new DateTime[nObs];

		times = map.keySet().toArray(times);

		return times;
	}

	/**
	 * Returns the value observed at a time. If {@code time} is not a valid
	 * observation time, 0 is returned.
	 *
	 * @param time
	 *            observation time
	 * @return time series value
	 */
	public double getValue(DateTime time) {

		if (time == null)
			throw new NullPointerException();

		Double value = map.get(time);
		if (value == null)
			value = 0.0;
		return value;
	}

	/**
	 * Returns the observed values of this time series in an array sorted according
	 * to the order of the observed times.
	 *
	 * @return array of values
	 */
	public double[] getValues() {

		int nObs = nObs();
		double[] values = new double[nObs];
		Double[] mapValues = new Double[nObs];

		mapValues = map.values().toArray(mapValues);

		for (int i = 0; i < nObs; i++)
			values[i] = mapValues[i];

		return values;
	}

}
