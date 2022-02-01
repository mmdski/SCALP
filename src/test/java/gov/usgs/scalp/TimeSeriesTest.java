package gov.usgs.scalp;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Random;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TimeSeriesTest {

	@Test
	public void testTimeSeries() {

		int nObs = 50;
		DateTime[] times = new DateTime[nObs];
		double[] values = new double[nObs];

		Random random = new Random();

		for (int i = 0; i < nObs; i++) {
			times[i] = new DateTime(random.nextLong());
			values[i] = random.nextDouble();
		}

		TimeSeries timeSeries = new TimeSeries(times, values);

		// test number of observations
		assertEquals(nObs, timeSeries.nObs());

		// test values
		for (int i = 0; i < nObs; i++) {
			assertEquals(values[i], timeSeries.getValue(times[i]), 0);
		}

		// sort times and test times
		Arrays.sort(times);
		DateTime[] tsTimes = timeSeries.getTimes();
		for (int i = 0; i < nObs; i++) {
			assertEquals(times[i], tsTimes[i]);
		}
	}

	@Test
	public void testInvalidTimeSeries() {
		long[] times = new long[2];
		double[] values = new double[3];
		try {
			new TimeSeries(times, values);
			fail();
		} catch (RuntimeException e) {
			assertEquals("times.length must be equal to values.length", e.getMessage());
		}
	}

	@Test
	public void testGetValues() {

		int[] hours = { 1, 3, 2, 4 };
		int nTimes = hours.length;
		DateTime[] timesOutOfOrder = new DateTime[nTimes];
		timesOutOfOrder[0] = new DateTime(2021, 12, 3, 3, 46);
		for (int i = 1; i < nTimes; i++) {
			timesOutOfOrder[i] = timesOutOfOrder[0].addHours(hours[i]);
		}
		double[] valuesOutOfOrder = { 1, 3, 2, 4 };
		double[] valuesInCorrectOrder = { 1, 2, 3, 4 };
		TimeSeries timeSeries = new TimeSeries(timesOutOfOrder, valuesOutOfOrder);

		double[] values = timeSeries.getValues();

		for (int i = 0; i < 4; i++) {
			assertEquals(valuesInCorrectOrder[i], values[i], 0);
		}
	}

	@Test
	public void testPut() {
		TimeSeries timeSeries = new TimeSeries();
		timeSeries.put(new DateTime(0), 0);
		assertEquals(0, timeSeries.getValue(new DateTime(0)), 0);
	}

	@Test
	public void testEquals() {

		int[] times1 = { 0, 1, 2 };
		int[] times2 = { 0, 1, 2, 3 };
		double[] values1 = { 0, 0, 0 };
		double[] values2 = { 0, 0, 0, 0 };

		DateTime dateTime = new DateTime(2021, 12, 3, 3, 46);

		TimeSeries timeSeries1 = new TimeSeries();
		TimeSeries sameTimeSeriesReference = timeSeries1;
		Object notTimeSeries = new Object();
		TimeSeries timeSeries2 = new TimeSeries();
		TimeSeries timeSeries3 = new TimeSeries();

		for (int i = 0; i < times1.length; i++) {
			timeSeries1.put(dateTime.addHours(times1[i]), values1[i]);
			timeSeries2.put(dateTime.addHours(times1[i]), values1[i]);
			timeSeries3.put(dateTime.addHours(times2[i]), values2[i]);
		}

		timeSeries3.put(dateTime.addHours(times2[3]), values2[3]);

		assertTrue(timeSeries1.equals(sameTimeSeriesReference));
		assertFalse(timeSeries1.equals(notTimeSeries));
		assertTrue(timeSeries1.equals(timeSeries2));
		assertFalse(timeSeries1.equals(timeSeries3));
	}

}
