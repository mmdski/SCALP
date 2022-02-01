package gov.usgs.scalp;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AnalyticComparison {

	// compute analytic solution time series
	private static TimeSeries[] analyticTimeSeries(long[] times, double[] k, double[] initialFlows,
			double systemInflow) {

		long initialTime = times[0];

		double[] solution;

		TimeSeries[] timeSeries = new TimeSeries[3];
		timeSeries[0] = new TimeSeries();
		timeSeries[1] = new TimeSeries();
		timeSeries[2] = new TimeSeries();

		AnalyticSolver solver = new AnalyticSolver(k, initialFlows, systemInflow);
		double time;
		DateTime dateTime;

		for (long t : times) {
			time = (t - initialTime) / 1000.0;
			solution = solver.compute(time);
			dateTime = new DateTime(t);
			for (int i = 0; i < 3; i++) {
				timeSeries[i].put(dateTime, solution[i]);
			}
		}

		return timeSeries;
	}

	private static long[] createTimes(int timeStep) {
		int nHours = 12;
		DateTime startDateTime = new DateTime(2021, 10, 01, 01, 00);
		long startTime = startDateTime.getTimeInMillis();
		long timeStepInMillis = timeStep * 1000;
		int nTimes = nHours * 3600 / timeStep;
		long times[] = new long[nTimes];
		for (int i = 0; i < nTimes; i++)
			times[i] = startTime + timeStepInMillis * i;
		return times;
	}

	// compute time series using series of SLR
	private static TimeSeries[] simulatedTimeSeries(long[] times, double[] k, double[] initialFlows,
			double systemInflow) {

		int nReservoirs = 3;

		// initialize reservoirs and time series
		SingleLinearReservoir[] reservoirs = new SingleLinearReservoir[nReservoirs];
		FlowTimeSeries[] timeSeries = new FlowTimeSeries[3];
		FlowValue initialFlow;
		for (int i = 0; i < nReservoirs; i++) {
			initialFlow = new FlowValue(initialFlows[i]);
			reservoirs[i] = new SingleLinearReservoir(k[i], initialFlow);
			timeSeries[i] = new InstFlowTimeSeries();
			timeSeries[i].put(new DateTime(times[0]), initialFlow);
		}

		double dT;
		int nTimes = times.length;
		DateTime dateTime;
		FlowValue inflow = new FlowValue(systemInflow);
		FlowValue[] lateralOutflow;
		FlowValue[] submainOutflow;
		FlowValue[] mainOutflow;
		for (int i = 1; i < nTimes; i++) {
			dT = (times[i] - times[i - 1]) / 1000.0;
			dateTime = new DateTime(times[i]);

			lateralOutflow = reservoirs[0].step(inflow, dT);
			timeSeries[0].put(dateTime, lateralOutflow[0]);

			submainOutflow = reservoirs[1].step(lateralOutflow[0], dT);
			timeSeries[1].put(dateTime, submainOutflow[0]);

			mainOutflow = reservoirs[2].step(submainOutflow[0], dT);
			timeSeries[2].put(dateTime, mainOutflow[0]);
		}

		TimeSeries[] outflowTimeSeries = new TimeSeries[3];
		for (int i = 0; i < nReservoirs; i++) {
			outflowTimeSeries[i] = timeSeries[i].getTotalFlow();
		}

		return outflowTimeSeries;
	}

	private static String getTimeStepName(int timeStep) {
		String timeStepName;
		switch (timeStep) {
		case 1 * 60:
			timeStepName = "1MIN";
			break;
		case 5 * 60:
			timeStepName = "5MIN";
			break;
		case 15 * 60:
			timeStepName = "15MIN";
			break;
		case 30 * 60:
			timeStepName = "30MIN";
			break;
		case 60 * 60:
			timeStepName = "1HOUR";
			break;
		default:
			throw new RuntimeException();
		}

		return timeStepName;
	}

	public static void writeAnalyticSolution(int timeStepMins, double[] k, double[] initialFlows, double systemInflow,
			String name) {
		int timeStepSeconds = timeStepMins * 60; // time step in seconds
		String timeStepStr = getTimeStepName(timeStepSeconds); // needed for DSS
		long[] times = createTimes(timeStepSeconds);

		TimeSeries[] analyticTimeSeries = analyticTimeSeries(times, k, initialFlows, systemInflow);

		// write time series
		Path dssPath = Paths.get("src", "test", "resources", "analytic.dss");
		String dssFilePath = dssPath.toAbsolutePath().toString();

		DSS dss = new DSS(dssFilePath);

		String lateralPathName = "/" + name + "/LATERAL/FLOW/01OCT2020/" + timeStepStr + "/";
		String submainPathName = "/" + name + "/SUBMAIN/FLOW/01OCT2020/" + timeStepStr + "/";
		String mainPathName = "/" + name + "/MAIN/FLOW/01OCT2020/" + timeStepStr + "/";

		dss.writeTimeSeries(lateralPathName + "ANALYTIC", analyticTimeSeries[0]);
		dss.writeTimeSeries(submainPathName + "ANALYTIC", analyticTimeSeries[1]);
		dss.writeTimeSeries(mainPathName + "ANALYTIC", analyticTimeSeries[2]);
	}

	public static void writeSimulatedFlows(int timeStepMins, double[] k, double[] initialFlows, double systemInflow,
			String name) {
		int timeStepSeconds = timeStepMins * 60; // time step in seconds
		String timeStepStr = getTimeStepName(timeStepSeconds); // needed for DSS
		long[] times = createTimes(timeStepSeconds);

		TimeSeries[] simulatedTimeSeries = simulatedTimeSeries(times, k, initialFlows, systemInflow);

		// write time series
		Path dssPath = Paths.get("src", "test", "resources", "analytic.dss");
		String dssFilePath = dssPath.toAbsolutePath().toString();

		DSS dss = new DSS(dssFilePath);

		String lateralPathName = "/" + name + "/LATERAL/FLOW/01OCT2020/" + timeStepStr + "/";
		String submainPathName = "/" + name + "/SUBMAIN/FLOW/01OCT2020/" + timeStepStr + "/";
		String mainPathName = "/" + name + "/MAIN/FLOW/01OCT2020/" + timeStepStr + "/";

		dss.writeTimeSeries(lateralPathName + "SIMULATED", simulatedTimeSeries[0]);
		dss.writeTimeSeries(submainPathName + "SIMULATED", simulatedTimeSeries[1]);
		dss.writeTimeSeries(mainPathName + "SIMULATED", simulatedTimeSeries[2]);
	}

	// initial inflow for a period of time, then flow goes to zero
	public static void variedInflow() {

		// initialize inflow time series
		DateTime startTime = new DateTime(2021, 10, 1, 1, 0); // start time for hourly average time series
		int nHours = 24;
		int inflowHours = 9;
		double systemInflow = 500;
		FlowValue flowValue;

		AvgFlowTimeSeries avgTimeSeries = new AvgFlowTimeSeries();

		for (int i = 0; i < nHours; i++) {
			if (i < inflowHours)
				flowValue = new FlowValue(systemInflow);
			else
				flowValue = new FlowValue(0);
			avgTimeSeries.put(startTime.addHours(i), flowValue);
		}

		// initialize reservoirs and reservoir time series
		SingleLinearReservoir[] reservoirs = new SingleLinearReservoir[3];
		TimeSeries[] slrTimeSeries = new TimeSeries[3];
		TimeSeries[] analyticSolution = new TimeSeries[3];
		double[] k = new double[] { 3500, 3600, 3700 };
		for (int i = 0; i < 3; i++) {
			reservoirs[i] = new SingleLinearReservoir(k[i]);
			slrTimeSeries[i] = new TimeSeries();
			analyticSolution[i] = new TimeSeries();
		}

		int timeStep = 1; // time step in minutes
		InstFlowTimeSeries inflowTimeSeries = avgTimeSeries.getInstantaneous(timeStep);
		DateTime[] times = inflowTimeSeries.getTimes();
		FlowValue[] inflow = inflowTimeSeries.getValues();
		int nTimes = times.length;
		FlowValue outflow;
		FlowValue[] out;

		double initialInflow;
		double[] initialFlows = new double[] { 0, 0, 0 };
		double[] outflows;

		double dT = timeStep * 60;
		for (int i = 0; i < nTimes; i++) {

			outflow = inflow[i];

			for (int j = 0; j < 3; j++) {
				out = reservoirs[j].step(outflow, dT);
				outflow = out[0];
				slrTimeSeries[j].put(new DateTime(times[i]), outflow.getTotal());
			}

			initialInflow = inflow[i].getTotal();
			outflows = AnalyticSolver.compute(k, initialFlows, initialInflow, dT);

			for (int j = 0; j < 3; j++) {
				analyticSolution[j].put(new DateTime(times[i]), outflows[j]);
			}
			initialFlows = outflows;
		}

		// write time series
		Path dssPath = Paths.get("src", "test", "resources", "analytic.dss");
		String dssFilePath = dssPath.toAbsolutePath().toString();

		DSS dss = new DSS(dssFilePath);

		// write inflow time series
		String inflowPath = "/VARIED INFLOW/INFLOW/FLOW/01OCT2020/1MIN/INFLOW";
		dss.writeTimeSeries(inflowPath, inflowTimeSeries.getTotalFlow());

		// write outflow time series
		String partA = "/VARIED INFLOW/";
		String partsCDE = "/FLOW/01OCT2020/1MIN/";
		String[] reservoirNames = new String[] { "LATERAL", "SUBMAIN", "MAIN" };
		String numericalPath;
		String analyticPath;
		for (int i = 0; i < 3; i++) {
			numericalPath = partA + reservoirNames[i] + partsCDE + "NUMERICAL/";
			dss.writeTimeSeries(numericalPath, slrTimeSeries[i]);

			analyticPath = partA + reservoirNames[i] + partsCDE + "ANALYTIC/";
			dss.writeTimeSeries(analyticPath, analyticSolution[i]);
		}
	}

	public static void main(String[] args) {

		double[] k = new double[] { 3500, 3600, 3700 };

		writeAnalyticSolution(1, k, new double[] { 500, 500, 500 }, 500, "CONST FLOW");
		writeAnalyticSolution(1, k, new double[] { 0, 0, 0 }, 500, "CONST INFLOW");
		writeAnalyticSolution(1, k, new double[] { 500, 0, 0 }, 0, "INITIAL	LATERAL");

		int[] timeStepMins = new int[] { 1 };
		for (int ts : timeStepMins) {

			// 500 cfs initial flow, 500 cfs constant inflow starting at t=0
			writeSimulatedFlows(ts, k, new double[] { 500, 500, 500 }, 500, "CONST FLOW");

			// 0 initial flow, 500 cfs constant inflow starting at t=0
			writeSimulatedFlows(ts, k, new double[] { 0, 0, 0 }, 500, "CONST INFLOW");

			// initial lateral inflow of 500 cfs, 0 initial inflow after t=0
			writeSimulatedFlows(ts, k, new double[] { 500, 0, 0 }, 0, "INITIAL LATERAL");
		}

		variedInflow();
	}

}
