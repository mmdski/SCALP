/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.usgs.scalp;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author hfdoyle
 */
public class DSSExample {

	public static void main(String args[]) {
		// open the dss file and read the time series
		Path dssPath = Paths.get("src", "test", "resources", "test.dss");
		String dssFilePath = dssPath.toAbsolutePath().toString();
		String pathname = "/THIS/IS/A/01OCT2020/1HOUR/TEST/";

		DSS dss = new DSS(dssFilePath);

		TimeSeries timeSeries = dss.readTimeSeries(pathname);

		String newPathname = "/THIS/IS/ALSO A//1HOUR/TEST";
		dss.writeTimeSeries(newPathname, timeSeries);
		dss.close();
	}

}
