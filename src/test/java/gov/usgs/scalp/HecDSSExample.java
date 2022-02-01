package gov.usgs.scalp;

import java.nio.file.Path;
import java.nio.file.Paths;

import hec.heclib.util.HecTime;
import hec.hecmath.DSSFile;
import hec.hecmath.HecMathException;
import hec.hecmath.TimeSeriesMath;
import hec.io.TimeSeriesContainer;
import hec.lang.DSSPathString;

public class HecDSSExample {

	public static void main(String[] args) {
		Path dssPath = Paths.get("src", "test", "resources", "test.dss");
		String dssFilePath = dssPath.toAbsolutePath().toString();
		DSSFile dssFile = hec.hecmath.DSS.open(dssFilePath);

		String partA = "BONEYARD";
		String partB = "GAGE 2";
		String partC = "FLOW";
		String partD = "01OCT1970";
		String partE = "1HOUR";
		String partF = "SUBRO";

		DSSPathString dssPathString = new DSSPathString(partA, partB, partC, partD, partE, partF);

		String pathName = dssPathString.getPathname();

		TimeSeriesMath timeSeriesMath;
		TimeSeriesContainer tsContainer;

		DateTime startDateTime = new DateTime(2016, 10, 15, 0, 0);
		DateTime endDateTime = new DateTime(2016, 11, 1, 0, 0);
		HecTime readStartTime = startDateTime.getHecTime();
		HecTime readEndTime = endDateTime.getHecTime();

		try {
			timeSeriesMath = (TimeSeriesMath) dssFile.read(pathName, readStartTime.toString(), readEndTime.toString());
			tsContainer = timeSeriesMath.getContainer();
		} catch (HecMathException e) {
			System.out.println(e.getMessage());
			tsContainer = null;
		}

		HecTime startTime = new HecTime();
		HecTime endTime = new HecTime();

		int nTimes = tsContainer.times.length;

		startTime.set(tsContainer.times[0]);
		endTime.set(tsContainer.times[nTimes - 1]);

		System.out.println("Number of times: " + nTimes);
		System.out.println("Start time: " + startTime.toString());
		System.out.println("End time: " + endTime.toString());

		dssFile.close();
	}

}
