package gov.usgs.scalp;

import java.nio.file.Paths;
import java.util.stream.IntStream;
import static org.junit.Assert.*;
import org.junit.Test;

public class DSSTest {

	/**
	 * Test of readTimeSeries method, of class DSS.
	 */
	@Test
	public void testReadTimeSeries() {
		String dssFilePath = Paths.get("src", "test", "resources", "test.dss").toAbsolutePath().toString();
		String pathname = "/THIS/IS/A/01OCT2020/1HOUR/TEST/";

		long[] times = IntStream.range(0, 24).asLongStream().map(t -> t * 3600000 + 1601946000000L).toArray();
		double[] values = IntStream.range(0, 24).asDoubleStream().toArray();

		TimeSeries expResult = new TimeSeries(times, values);

		DSS dss = new DSS(dssFilePath);
		TimeSeries result = dss.readTimeSeries(pathname);
		dss.close();

		assertTrue(expResult.equals(result));
	}

	@Test
	public void testDSSFileExceptionRead() {
		String dssFilePath = "? This is a bad path";
		String pathname = "/THIS/IS/A/01OCT2020/1HOUR/TEST/";

		DSS dss = new DSS(dssFilePath);
		TimeSeries result = dss.readTimeSeries(pathname);
		assertNull(result);
		dss.close();
	}

	/**
	 * Test of writeTimeSeries method, of class DSS.
	 */
	@Test
	public void testWriteTimeSeries() {
		String dssFilePath = Paths.get("src", "test", "resources", "testoutput.dss").toAbsolutePath().toString();
		String pathname = "/THIS/IS/ALSO A//1HOUR/TEST";
		long[] times = IntStream.range(0, 24).asLongStream().map(t -> t * 3600000 + 1601946000000L).toArray();
		double[] values = IntStream.range(0, 24).asDoubleStream().toArray();
		TimeSeries timeSeries = new TimeSeries(times, values);
		DSS dss = new DSS(dssFilePath);
		try {
			dss.writeTimeSeries(pathname, timeSeries);
		} catch (RuntimeException e) {
			fail();
		}

		dss.close();
	}

	@Test
	public void testDSSFileExceptionWrite() {
		String dssFilePath = Paths.get("src", "test", "resources", "test.dss").toAbsolutePath().toString();
		String pathname = "? This is a bad path";
		long[] times = IntStream.range(0, 24).asLongStream().map(t -> t * 3600000 + 1601946000000L).toArray();
		double[] values = IntStream.range(0, 24).asDoubleStream().toArray();
		TimeSeries timeSeries = new TimeSeries(times, values);
		DSS dss = new DSS(dssFilePath);
		int writeCode = dss.writeTimeSeries(pathname, timeSeries);
		dss.close();
		if (writeCode >= 0) // write must fail
			fail();
	}

}
