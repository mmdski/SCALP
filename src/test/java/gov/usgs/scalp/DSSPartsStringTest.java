package gov.usgs.scalp;

import static org.junit.Assert.*;
import org.junit.Test;

public class DSSPartsStringTest {

	@Test
	public void testParseAFail() {
		String dssParts = "A=BONEYARD";

		try {
			DSSPartsString partsString = new DSSPartsString(dssParts);
			fail();
			partsString.getPartA();
		} catch (RuntimeException e) {
		}
	}

	@Test
	public void testParseAandB() {
		String dssParts = "A=BONEYARD B=GAGE 2";
		DSSPartsString partsString = new DSSPartsString(dssParts);
		assertEquals("BONEYARD", partsString.getPartA());
		assertEquals("GAGE 2", partsString.getPartB());
		assertNull(partsString.getPartF());
	}

	@Test
	public void testParseABandF() {
		String dssParts = "A=BONEYARD B=GAGE 2 F=OUTFLOW";
		DSSPartsString partsString = new DSSPartsString(dssParts);
		assertEquals("BONEYARD", partsString.getPartA());
		assertEquals("GAGE 2", partsString.getPartB());
		assertEquals("OUTFLOW", partsString.getPartF());
	}

	@Test
	public void testParseGFail() {
		String dssParts = "A=BONEYARD B=GAGE 2 G=OUTFLOW";
		try {
			DSSPartsString partsString = new DSSPartsString(dssParts);
			fail();
			partsString.getPartA();
		} catch (RuntimeException e) {
		}

	}

	@Test
	public void testEmptyF() {
		String dssParts = "A=BONEYARD B=GAGE 2 F=";
		DSSPartsString partsString = new DSSPartsString(dssParts);
		assertEquals("BONEYARD", partsString.getPartA());
		assertEquals("GAGE 2", partsString.getPartB());
		assertEquals("", partsString.getPartF());
	}
}
