package gov.usgs.scalp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import hec.lang.DSSPathString;

public class InputDSSPathStringsTest {

	@Test
	public void TestDSSPath() {

		String path = "A=BONEYARD B=GAGE 2";

		InputDSSPathStrings pathStrings = new InputDSSPathStrings(path);

		String partA = "BONEYARD";
		String partB = "GAGE 2";
		String partC = "FLOW";
		String partD = "01JAN2000";
		String partE = "1HOUR";

		String improPath = DSSPathString.getPathname(new String[] { partA, partB, partC, partD, partE, "IMPRO" });
		assertEquals(improPath, pathStrings.getImproPath());

		String olfroPath = DSSPathString.getPathname(new String[] { partA, partB, partC, partD, partE, "OLFRO" });
		assertEquals(olfroPath, pathStrings.getOlfroPath());

		String subroPath = DSSPathString.getPathname(new String[] { partA, partB, partC, partD, partE, "SUBRO" });
		assertEquals(subroPath, pathStrings.getSubroPath());
	}

}
