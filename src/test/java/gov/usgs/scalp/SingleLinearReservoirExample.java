package gov.usgs.scalp;

import java.nio.file.Path;
import java.nio.file.Paths;

import hec.lang.DSSPathString;

public class SingleLinearReservoirExample {

	public static void test1(String dssFilePath) {
		String inflowPathname = "/THIS/INFLOW/FLOW/01OCT2020/1HOUR/TEST1/";

		DSS dssFile = new DSS(dssFilePath);

		TimeSeries inflow = dssFile.readTimeSeries(inflowPathname);
		TimeSeries outflow = new TimeSeries();

		FlowValue initialFlow = new FlowValue(0);
		double K = 2 * 3600;
		DateTime[] times = inflow.getTimes();
		double[] in = inflow.getValues();

		SingleLinearReservoir slr = new SingleLinearReservoir(K, initialFlow);

		FlowValue[] out;

		outflow.put(times[0], initialFlow.getTotal());

		int nTimes = times.length;
		double deltaT = (times[1].getTimeInMillis() - times[0].getTimeInMillis()) / 1000.0;

		for (int i = 1; i < nTimes; i++) {
			out = slr.step(new FlowValue(0, 0, in[i]), deltaT);
			outflow.put(times[i], out[0].getTotal());
		}

		String outflowPathname = "/THIS/OUTFLOW/FLOW/01OCT2020/1HOUR/TEST1/";
		dssFile.writeTimeSeries(outflowPathname, outflow);
	}

	public static void test1Overflow(String dssFilePath) {
		String inflowPathname = "/THIS/INFLOW/FLOW/01OCT2020/1HOUR/TEST1/";

		DSS dssFile = new DSS(dssFilePath);

		TimeSeries inflow = dssFile.readTimeSeries(inflowPathname);
		TimeSeries interceptorFlowSeries = new TimeSeries();
		TimeSeries overflowSeries = new TimeSeries();
		TimeSeries totalFlowSeries = new TimeSeries();

		FlowValue initialFlow = new FlowValue(0);
		double K = 2 * 3600;
		DateTime[] times = inflow.getTimes();
		double[] in = inflow.getValues();

		double maxFlow = 100;
		double splitFlow = 25;

		FlowValue inflowValue = new FlowValue(in[0]);

		SingleLinearReservoir slr = new SingleLinearReservoir(K, initialFlow, splitFlow, maxFlow);

		FlowValue[] out;

		interceptorFlowSeries.put(times[0], initialFlow.getTotal());
		overflowSeries.put(times[0], 0);
		totalFlowSeries.put(times[0], 0);

		int nTimes = times.length;
		double deltaT = (times[1].getTimeInMillis() - times[0].getTimeInMillis()) / 1000.0;

		for (int i = 1; i < nTimes; i++) {
			inflowValue = new FlowValue(in[i]);
			out = slr.step(inflowValue, deltaT);
			interceptorFlowSeries.put(times[i], out[0].getTotal());
			overflowSeries.put(times[i], out[1].getTotal());
			totalFlowSeries.put(times[i], out[0].add(out[1]).getTotal());
		}

		dssFile.writeTimeSeries("/THIS/OUTFLOW/FLOW/01OCT2020/1HOUR/TEST1-INT/", interceptorFlowSeries);
		dssFile.writeTimeSeries("/THIS/OUTFLOW/FLOW/01OCT2020/1HOUR/TEST1-OVF/", overflowSeries);
		dssFile.writeTimeSeries("/THIS/OUTFLOW/FLOW/01OCT2020/1HOUR/TEST1-TOT/", totalFlowSeries);
	}

	public static void test2(String dssFilePath) {
		String inflowPathname = "/THIS/INFLOW/FLOW/01OCT2020/1HOUR/TEST2/";

		DSS dssFile = new DSS(dssFilePath);

		TimeSeries inflow = dssFile.readTimeSeries(inflowPathname);
		TimeSeries outflow = new TimeSeries();

		FlowValue initialFlow = new FlowValue(0);
		double K = 2 * 3600;
		DateTime[] times = inflow.getTimes();
		double[] in = inflow.getValues();

		FlowValue inflowValue = new FlowValue(in[0]);

		SingleLinearReservoir slr = new SingleLinearReservoir(K, initialFlow);

		FlowValue[] out;

		outflow.put(times[0], initialFlow.getTotal());

		int nTimes = times.length;
		double deltaT = (times[1].getTimeInMillis() - times[0].getTimeInMillis()) / 1000.0;

		for (int i = 1; i < nTimes; i++) {
			inflowValue = new FlowValue(in[i]);
			out = slr.step(inflowValue, deltaT);
			outflow.put(times[i], out[0].getTotal());
		}

		String outflowPathname = "/THIS/OUTFLOW/FLOW/01OCT2020/1HOUR/TEST2/";
		dssFile.writeTimeSeries(outflowPathname, outflow);
	}

	public static void test3(String dssFilePath) {
		String inflowPathname = "/THIS/INFLOW/FLOW/01OCT2020/1HOUR/TEST3/";

		DSS dssFile = new DSS(dssFilePath);

		TimeSeries inflow = dssFile.readTimeSeries(inflowPathname);
		TimeSeries outflow = new TimeSeries();

		FlowValue initialFlow = new FlowValue(0);
		double K = 2 * 3600;
		DateTime[] times = inflow.getTimes();
		double[] in = inflow.getValues();

		FlowValue inflowValue = new FlowValue(in[0]);

		SingleLinearReservoir slr = new SingleLinearReservoir(K, initialFlow);

		FlowValue[] out;

		outflow.put(times[0], initialFlow.getTotal());

		int nTimes = times.length;
		double deltaT = (times[1].getTimeInMillis() - times[0].getTimeInMillis()) / 1000.0;

		for (int i = 1; i < nTimes; i++) {
			inflowValue = new FlowValue(in[i]);
			out = slr.step(inflowValue, deltaT);
			outflow.put(times[i], out[0].getTotal());
		}

		String outflowPathname = "/THIS/OUTFLOW/FLOW/01OCT2020/1HOUR/TEST3/";
		dssFile.writeTimeSeries(outflowPathname, outflow);
	}

	public static void constituentFlowTest(SingleLinearReservoir slr, String dssFilePath, String partA, String partB) {

		String partC = "FLOW";
		String partD = "01OCT2020";
		String partE = "1HOUR";

		DSSPathString infiltrationPath = new DSSPathString(partA, partB, partC, partD, partE, "INFILTRATION");
		DSSPathString sanitaryPath = new DSSPathString(partA, partB, partC, partD, partE, "SANITARY");
		DSSPathString stormPath = new DSSPathString(partA, partB, partC, partD, partE, "STORM");

		DSS dssFile = new DSS(dssFilePath);

		TimeSeries infiltrationTS = dssFile.readTimeSeries(infiltrationPath.toString());
		TimeSeries sanitaryTS = dssFile.readTimeSeries(sanitaryPath.toString());
		TimeSeries stormTS = dssFile.readTimeSeries(stormPath.toString());

		FlowTimeSeries outflow = new InstFlowTimeSeries();
		FlowTimeSeries overflow = new InstFlowTimeSeries();

		DateTime[] times = infiltrationTS.getTimes();

		double[] infiltration = infiltrationTS.getValues();
		double[] sanitary = sanitaryTS.getValues();
		double[] storm = stormTS.getValues();

		FlowValue inflowValue;

		FlowValue[] out;

		int nTimes = times.length;
		double deltaT = (times[1].getTimeInMillis() - times[0].getTimeInMillis()) / 1000.0; // assume a constant time
																							// step

		for (int i = 0; i < nTimes; i++) {
			inflowValue = new FlowValue(infiltration[i], sanitary[i], storm[i]);
			out = slr.step(inflowValue, deltaT);
			outflow.put(times[i], out[0]);
			overflow.put(times[i], out[1]);
		}

		// write outflow
		DSSPathString totOutflowPath = new DSSPathString(partA, partB, partC, partD, partE, "TOTAL-OUT");
		dssFile.writeTimeSeries(totOutflowPath.toString(), outflow.getTotalFlow());

		DSSPathString infOutflowPath = new DSSPathString(partA, partB, partC, partD, partE, "INFIL-OUT");
		dssFile.writeTimeSeries(infOutflowPath.toString(), outflow.getInfiltration());

		DSSPathString sanOutflowPath = new DSSPathString(partA, partB, partC, partD, partE, "SANITARY-OUT");
		dssFile.writeTimeSeries(sanOutflowPath.toString(), outflow.getSanitary());

		DSSPathString stormOutflowPath = new DSSPathString(partA, partB, partC, partD, partE, "STORM-OUT");
		dssFile.writeTimeSeries(stormOutflowPath.toString(), outflow.getStormWater());

		// write overflow
		DSSPathString totOverflowPath = new DSSPathString(partA, partB, partC, partD, partE, "TOTAL-OVF");
		dssFile.writeTimeSeries(totOverflowPath.toString(), overflow.getTotalFlow());

		DSSPathString infOverflowPath = new DSSPathString(partA, partB, partC, partD, partE, "INFIL-OVF");
		dssFile.writeTimeSeries(infOverflowPath.toString(), overflow.getInfiltration());

		DSSPathString sanOverflowPath = new DSSPathString(partA, partB, partC, partD, partE, "SANITARY-OVF");
		dssFile.writeTimeSeries(sanOverflowPath.toString(), overflow.getSanitary());

		DSSPathString stormOverflowPath = new DSSPathString(partA, partB, partC, partD, partE, "STORM-OVF");
		dssFile.writeTimeSeries(stormOverflowPath.toString(), overflow.getStormWater());
	}

	public static void main(String[] args) {
		Path dssPath = Paths.get("src", "test", "resources", "reservoir.dss");
		String dssFilePath = dssPath.toAbsolutePath().toString();

		test1(dssFilePath);
		test1Overflow(dssFilePath);
		test2(dssFilePath);
		test3(dssFilePath);

		SingleLinearReservoir slr = new SingleLinearReservoir(2 * 3600);
		constituentFlowTest(slr, dssFilePath, "CONSTITUENT", "PULSE");
	}

}
