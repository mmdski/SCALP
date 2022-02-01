/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.usgs.scalp;

import java.text.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests of class WatershedInputBlock
 */
public class WatershedInputBlockTest {

	/**
	 * Test of constructor of class WatershedInputBlock.
	 */
	@Test
	public void testWatershedException() {
		String[] lines = { "?????????", "+++ This is an extra comment line" };
		InputFile inputFile = new InputFile(lines);
		try {
			new WatershedInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			assertEquals("Error parsing WATERSHED input block", e.getMessage());
			assertEquals(1, e.getErrorOffset());
		}
	}

	/**
	 * Test of constructor of class WatershedInputBlock.
	 */
	@Test
	public void testTimeSpanException() {
		String[] lines = { "WATERSHED", "+++ This is a test file", "", "?????????",
				"+++ This is an extra comment line" };
		InputFile inputFile = new InputFile(lines);
		try {
			new WatershedInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			assertEquals("Error parsing WATERSHED input block", e.getMessage());
			assertEquals(4, e.getErrorOffset());
		}
	}

	/**
	 * Test of constructor of class WatershedInputBlock.
	 */
	@Test
	public void testOutputLevelException() {
		String[] lines = { "WATERSHED", "+++ This is a test file", "", "TIME SPAN",
				"STARTING DATE       2016      10      01", "ENDING   DATE       2017      09      30", "",
				"????????????", "+++ This is an extra comment line" };
		InputFile inputFile = new InputFile(lines);
		try {
			new WatershedInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			assertEquals("Error parsing WATERSHED input block", e.getMessage());
			assertEquals(8, e.getErrorOffset());
		}
	}

	/**
	 * Test of constructor of class WatershedInputBlock.
	 */
	@Test
	public void testDataException() {
		String[] lines = { "WATERSHED", "+++ This is a test file", "", "TIME SPAN",
				"STARTING DATE       2016      10      01", "ENDING   DATE       2017      09      30", "",
				"OUTPUT LEVEL", "           0", "", "????", "+++ This is an extra comment line" };
		InputFile inputFile = new InputFile(lines);
		try {
			new WatershedInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			assertEquals("Error parsing WATERSHED input block", e.getMessage());
			assertEquals(11, e.getErrorOffset());
		}
	}

	/**
	 * Test of constructor of class WatershedInputBlock.
	 */
	@Test
	public void testLandSegmentsException() {
		String[] lines = { "WATERSHED", "+++ This is a test file", "", "TIME SPAN",
				"STARTING DATE       2016      10      01", "ENDING   DATE       2017      09      30", "",
				"OUTPUT LEVEL", "           0", "", "DATA", "?" };
		InputFile inputFile = new InputFile(lines);
		try {
			new WatershedInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			assertEquals("Error parsing WATERSHED input block", e.getMessage());
			assertEquals(12, e.getErrorOffset());
		}
	}

	/**
	 * Test of constructor of class WatershedInputBlock.
	 */
	@Test
	public void testEndException() {
		String[] lines = { "WATERSHED", "+++ This is a test file", "", "TIME SPAN",
				"STARTING DATE       2016      10      01", "ENDING   DATE       2017      09      30", "",
				"OUTPUT LEVEL", "           0", "", "DATA", "       2 A=BONEYARD B=GAGE 2", "???" };
		InputFile inputFile = new InputFile(lines);
		try {
			new WatershedInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			assertEquals("Error parsing WATERSHED input block", e.getMessage());
			assertEquals(13, e.getErrorOffset());
		}
	}

	/**
	 * Test of asString method, of class WatershedInputBlock.
	 */
	@Test
	public void testAsString() {
		String[] lines = { "WATERSHED", "+++ This is a test file", "", "TIME SPAN",
				"STARTING DATE       2016      10      01", "ENDING   DATE       2017      09      30", "",
				"OUTPUT LEVEL", "           0", "", "DATA", "       2 A=BONEYARD B=GAGE 2", "END" };
		InputFile inputFile = new InputFile(lines);
		WatershedInputBlock instance;
		try {
			instance = new WatershedInputBlock(inputFile);
		} catch (ParseException e) {
			fail();
			return;
		}

		String[] expResult = { "WATERSHED", "TIME SPAN", "STARTING DATE       2016      10       1",
				"ENDING DATE         2017       9      30", "OUTPUT LEVEL", "           0", "DATA     ",
				"       2 A=BONEYARD B=GAGE 2                                                    ", "END" };
		String[] result = instance.asString();
		assertArrayEquals(expResult, result);
	}

	/**
	 * Test of getDssPath method, of class WatershedInputBlock.
	 */
	@Test
	public void testGetDssPath() {
		String[] lines = { "WATERSHED", "+++ This is a test file", "", "TIME SPAN",
				"STARTING DATE       2016      10      01", "ENDING   DATE       2017      09      30", "",
				"OUTPUT LEVEL", "           0", "", "DATA", "       2 A=BONEYARD B=GAGE 2", "END" };
		InputFile inputFile = new InputFile(lines);
		WatershedInputBlock instance;
		try {
			instance = new WatershedInputBlock(inputFile);
		} catch (ParseException e) {
			fail();
			return;
		}

		int segmentNumber = 2;
		String expResult = " A=BONEYARD B=GAGE 2";
		String result = instance.getDssPath(segmentNumber);
		assertEquals(expResult, result);
	}

	/**
	 * Test of endDate method, of class WatershedInputBlock.
	 */
	@Test
	public void testGetEndDate() {
		String[] lines = { "WATERSHED", "+++ This is a test file", "", "TIME SPAN",
				"STARTING DATE       2016      10      01", "ENDING   DATE       2017      09      30", "",
				"OUTPUT LEVEL", "           0", "", "DATA", "       2 A=BONEYARD B=GAGE 2", "END" };
		InputFile inputFile = new InputFile(lines);
		WatershedInputBlock instance;
		try {
			instance = new WatershedInputBlock(inputFile);
		} catch (ParseException e) {
			fail();
			return;
		}

		DateTime expResult = new DateTime(2017, 9, 30, 24, 0);
		DateTime result = instance.getEndDate();
		assertEquals(expResult, result);
	}

	/**
	 * Test of startDate method, of class WatershedInputBlock.
	 */
	@Test
	public void testGetStartDate() {
		String[] lines = { "WATERSHED", "+++ This is a test file", "", "TIME SPAN",
				"STARTING DATE       2016      10      01", "ENDING   DATE       2017      09      30", "",
				"OUTPUT LEVEL", "           0", "", "DATA", "       2 A=BONEYARD B=GAGE 2", "END" };
		InputFile inputFile = new InputFile(lines);
		WatershedInputBlock instance;
		try {
			instance = new WatershedInputBlock(inputFile);
		} catch (ParseException e) {
			fail();
			return;
		}

		DateTime expResult = new DateTime(2016, 10, 1, 1, 0);
		DateTime result = instance.getStartDate();
		assertEquals(expResult, result);
	}

	/**
	 * Test of outputLevel method, of class WatershedInputBlock.
	 */
	@Test
	public void testGetOutputLevel() {
		String[] lines = { "WATERSHED", "+++ This is a test file", "", "TIME SPAN",
				"STARTING DATE       2016      10      01", "ENDING   DATE       2016      10      02", "",
				"OUTPUT LEVEL", "           0", "", "DATA", "       2 A=BONEYARD B=GAGE 2", "END" };
		InputFile inputFile = new InputFile(lines);
		WatershedInputBlock instance;
		try {
			instance = new WatershedInputBlock(inputFile);
		} catch (ParseException e) {
			fail();
			return;
		}

		int expResult = 0;
		int result = instance.getOutputLevel();
		assertEquals(expResult, result);
	}

	/**
	 * Test of segmentNumbers method, of class WatershedInputBlock.
	 */
	@Test
	public void testGetSegmentNumbers() {
		String[] lines = { "WATERSHED", "+++ This is a test file", "", "TIME SPAN",
				"STARTING DATE       2016      10      01", "ENDING   DATE       2017      09      30", "",
				"OUTPUT LEVEL", "           0", "", "DATA", "       2 A=BONEYARD B=GAGE 2", "END" };
		InputFile inputFile = new InputFile(lines);
		WatershedInputBlock instance;
		try {
			instance = new WatershedInputBlock(inputFile);
		} catch (ParseException e) {
			fail();
			return;
		}

		int[] expResult = { 2 };
		int[] result = instance.getSegmentNumbers();
		assertArrayEquals(expResult, result);
	}

	@Test
	public void testDuplicateLandNos() {
		String[] lines = { "WATERSHED", "+++ This is a test file", "", "TIME SPAN",
				"STARTING DATE       2016      10      01", "ENDING   DATE       2017      09      30", "",
				"OUTPUT LEVEL", "           0", "", "DATA", "       2 A=BONEYARD B=GAGE 2",
				"       2 A=BONEYARD B=GAGE 2", "END" };
		InputFile inputFile = new InputFile(lines);
		try {
			new WatershedInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			fail();
		} catch (RuntimeException e) {
			if (!e.getMessage().equals("Duplicate land segment numbers encountered"))
				fail();
			return;
		}
		fail();
	}

}
