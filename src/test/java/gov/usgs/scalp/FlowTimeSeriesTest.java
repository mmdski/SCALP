package gov.usgs.scalp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FlowTimeSeriesTest {

	@Test
	public void testGetInstFlow() {

		DateTime avgDateTime = new DateTime(2021, 10, 1, 1, 0);
		DateTime instDateTime = avgDateTime.subtractHours(1);

		int minTimeStep = 5;

		int nHours = 12;

		AvgFlowTimeSeries avgTimeSeries = new AvgFlowTimeSeries();
		InstFlowTimeSeries expected = new InstFlowTimeSeries();

		for (int i = 0; i < nHours; i++) {
			avgTimeSeries.put(avgDateTime, new FlowValue(i));
			while (instDateTime.lessThan(avgDateTime)) {
				expected.put(instDateTime, new FlowValue(i));
				instDateTime = instDateTime.addMinutes(minTimeStep);
			}
			avgDateTime = avgDateTime.addHours(1);
		}

		InstFlowTimeSeries result = avgTimeSeries.getInstantaneous(minTimeStep);

		assertEquals(expected, result);
	}

	@Test
	public void testGetAvgFlow() {

		DateTime avgDateTime = new DateTime(2021, 10, 1, 1, 0);
		DateTime instDateTime = avgDateTime.subtractHours(1);

		int minTimeStep = 1;

		int nHours = 12;

		AvgFlowTimeSeries expected = new AvgFlowTimeSeries();
		InstFlowTimeSeries instTimeSeries = new InstFlowTimeSeries();

		for (int i = 0; i < nHours; i++) {
			expected.put(avgDateTime, new FlowValue(i));
			while (instDateTime.lessThan(avgDateTime)) {
				instTimeSeries.put(instDateTime, new FlowValue(i));
				instDateTime = instDateTime.addMinutes(minTimeStep);
			}
			avgDateTime = avgDateTime.addHours(1);
		}

		AvgFlowTimeSeries result = instTimeSeries.getAveraged();

		assertEquals(expected, result);
	}

}
