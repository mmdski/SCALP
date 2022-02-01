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
 *
 * @author hfdoyle
 */
public class SanitaryInputBlockTest {

	/**
	 * Test of constructor method, of class SanitaryInputBlock.
	 */
	@Test
	public void testSanitaryInputBlock() {
		String[] lines = { "????????" };
		InputFile inputFile = new InputFile(lines);
		try {
			new SanitaryInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			assertEquals("Error parsing SANITARY input block", e.getMessage());
			assertEquals(1, e.getErrorOffset());
		}
	}

	/**
	 * Test of asString method, of class SanitaryInputBlock.
	 */
	@Test
	public void testAsString() {
		String[] lines = { "SANITARY", "SNA#                   1", "SANCOMP=            2.468E-4",
				"JAN-JUNE=           0.92    0.94    1.03    1.04    1.04    1.08",
				"JULY-DEC=           1.10    1.09    1.01    0.93    0.92    0.90",
				"WEEKLY=             0.93    1.00    1.03    1.05    1.03    1.00    0.96",
				"01-06 AM            0.72    0.71    0.70    0.70    0.71    0.88",
				"07-12 AM            1.04    1.12    1.14    1.14    1.15    1.17",
				"01-06 PM            1.16    1.16    1.16    1.14    1.14    1.12",
				"07-12 PM            1.10    1.07    1.03    0.98    0.90    0.86", "SNA#                   2",
				"SNACOMP=            2.818E-3", "JAN-JUNE=           0.92    0.94    1.03    1.04    1.04    1.08",
				"JULY-DEC=           1.10    1.09    1.01    0.93    0.92    0.90",
				"WEEKLY=             0.93    1.00    1.03    1.05    1.03    1.00    0.96",
				"01-06 AM            0.72    0.71    0.70    0.70    0.71    0.88",
				"07-12 AM            1.04    1.12    1.14    1.14    1.15    1.17",
				"01-06 PM            1.16    1.16    1.16    1.14    1.14    1.12",
				"07-12 PM            1.10    1.07    1.03    0.98    0.90    0.86", "END" };
		InputFile inputFile = new InputFile(lines);
		SanitaryInputBlock instance;
		try {
			instance = new SanitaryInputBlock(inputFile);
		} catch (ParseException e) {
			fail();
			return;
		}

		String[] expResult = { "SANITARY", "SNA#                         1", "SNACOMP=                  2.46800e-04",
				"JAN-JUNE=           0.92    0.94    1.03    1.04    1.04    1.08",
				"JULY-DEC=           1.10    1.09    1.01    0.93    0.92    0.90",
				"WEEKLY=             0.93    1.00    1.03    1.05    1.03    1.00    0.96",
				"01-06AM             0.72    0.71    0.70    0.70    0.71    0.88",
				"07AM-12PM           1.04    1.12    1.14    1.14    1.15    1.17",
				"01-06PM             1.16    1.16    1.16    1.14    1.14    1.12",
				"07PM-12AM           1.10    1.07    1.03    0.98    0.90    0.86", "SNA#                         2",
				"SNACOMP=                  2.81800e-03",
				"JAN-JUNE=           0.92    0.94    1.03    1.04    1.04    1.08",
				"JULY-DEC=           1.10    1.09    1.01    0.93    0.92    0.90",
				"WEEKLY=             0.93    1.00    1.03    1.05    1.03    1.00    0.96",
				"01-06AM             0.72    0.71    0.70    0.70    0.71    0.88",
				"07AM-12PM           1.04    1.12    1.14    1.14    1.15    1.17",
				"01-06PM             1.16    1.16    1.16    1.14    1.14    1.12",
				"07PM-12AM           1.10    1.07    1.03    0.98    0.90    0.86", "END" };
		String[] result = instance.asString();
		assertArrayEquals(expResult, result);
	}

	/**
	 * Test of getInfoSetNumbers method, of class SanitaryInputBlock
	 */
	@Test
	public void testGetInfoSetNumbers() {
		String[] lines = { "SANITARY", "SNA#                   1", "SANCOMP=            2.468E-4",
				"JAN-JUNE=           0.92    0.94    1.03    1.04    1.04    1.08",
				"JULY-DEC=           1.10    1.09    1.01    0.93    0.92    0.90",
				"WEEKLY=             0.93    1.00    1.03    1.05    1.03    1.00    0.96",
				"01-06 AM            0.72    0.71    0.70    0.70    0.71    0.88",
				"07-12 AM            1.04    1.12    1.14    1.14    1.15    1.17",
				"01-06 PM            1.16    1.16    1.16    1.14    1.14    1.12",
				"07-12 PM            1.10    1.07    1.03    0.98    0.90    0.86", "SNA#                   3",
				"SNACOMP=            2.818E-3", "JAN-JUNE=           0.92    0.94    1.03    1.04    1.04    1.08",
				"JULY-DEC=           1.10    1.09    1.01    0.93    0.92    0.90",
				"WEEKLY=             0.93    1.00    1.03    1.05    1.03    1.00    0.96",
				"01-06 AM            0.72    0.71    0.70    0.70    0.71    0.88",
				"07-12 AM            1.04    1.12    1.14    1.14    1.15    1.17",
				"01-06 PM            1.16    1.16    1.16    1.14    1.14    1.12",
				"07-12 PM            1.10    1.07    1.03    0.98    0.90    0.86", "SNA#                   2",
				"SNACOMP=            2.818E-3", "JAN-JUNE=           0.92    0.94    1.03    1.04    1.04    1.08",
				"JULY-DEC=           1.10    1.09    1.01    0.93    0.92    0.90",
				"WEEKLY=             0.93    1.00    1.03    1.05    1.03    1.00    0.96",
				"01-06 AM            0.72    0.71    0.70    0.70    0.71    0.88",
				"07-12 AM            1.04    1.12    1.14    1.14    1.15    1.17",
				"01-06 PM            1.16    1.16    1.16    1.14    1.14    1.12",
				"07-12 PM            1.10    1.07    1.03    0.98    0.90    0.86", "END" };
		InputFile inputFile = new InputFile(lines);
		SanitaryInputBlock instance;
		try {
			instance = new SanitaryInputBlock(inputFile);
		} catch (ParseException e) {
			fail();
			return;
		}

		int[] expResult = { 1, 3, 2 };
		int[] result = instance.getInfoSetNumbers();
		assertArrayEquals(expResult, result);
	}

	@Test
	public void testDuplicateSanitaryInfoSetNos() {
		String[] lines = { "SANITARY", "SNA#                   1", "SANCOMP=            2.468E-4",
				"JAN-JUNE=           0.92    0.94    1.03    1.04    1.04    1.08",
				"JULY-DEC=           1.10    1.09    1.01    0.93    0.92    0.90",
				"WEEKLY=             0.93    1.00    1.03    1.05    1.03    1.00    0.96",
				"01-06 AM            0.72    0.71    0.70    0.70    0.71    0.88",
				"07-12 AM            1.04    1.12    1.14    1.14    1.15    1.17",
				"01-06 PM            1.16    1.16    1.16    1.14    1.14    1.12",
				"07-12 PM            1.10    1.07    1.03    0.98    0.90    0.86", "SNA#                   1",
				"SNACOMP=            2.818E-3", "JAN-JUNE=           0.92    0.94    1.03    1.04    1.04    1.08",
				"JULY-DEC=           1.10    1.09    1.01    0.93    0.92    0.90",
				"WEEKLY=             0.93    1.00    1.03    1.05    1.03    1.00    0.96",
				"01-06 AM            0.72    0.71    0.70    0.70    0.71    0.88",
				"07-12 AM            1.04    1.12    1.14    1.14    1.15    1.17",
				"01-06 PM            1.16    1.16    1.16    1.14    1.14    1.12",
				"07-12 PM            1.10    1.07    1.03    0.98    0.90    0.86", "SNA#                   2",
				"SNACOMP=            2.818E-3", "JAN-JUNE=           0.92    0.94    1.03    1.04    1.04    1.08",
				"JULY-DEC=           1.10    1.09    1.01    0.93    0.92    0.90",
				"WEEKLY=             0.93    1.00    1.03    1.05    1.03    1.00    0.96",
				"01-06 AM            0.72    0.71    0.70    0.70    0.71    0.88",
				"07-12 AM            1.04    1.12    1.14    1.14    1.15    1.17",
				"01-06 PM            1.16    1.16    1.16    1.14    1.14    1.12",
				"07-12 PM            1.10    1.07    1.03    0.98    0.90    0.86", "END" };
		InputFile inputFile = new InputFile(lines);
		try {
			new SanitaryInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			fail();
		} catch (RuntimeException e) {
			assertEquals("Duplicate sanitary info set number encountered", e.getMessage());
		}
	}

	@Test
	public void testNegSNACOMP() {
		String[] lines = { "SANITARY", "SNA#                   1", "SANCOMP=            -2.68E-4",
				"JAN-JUNE=           0.92    0.94    1.03    1.04    1.04    1.08",
				"JULY-DEC=           1.10    1.09    1.01    0.93    0.92    0.90",
				"WEEKLY=             0.93    1.00    1.03    1.05    1.03    1.00    0.96",
				"01-06 AM            0.72    0.71    0.70    0.70    0.71    0.88",
				"07-12 AM            1.04    1.12    1.14    1.14    1.15    1.17",
				"01-06 PM            1.16    1.16    1.16    1.14    1.14    1.12",
				"07-12 PM            1.10    1.07    1.03    0.98    0.90    0.86", "END" };
		InputFile inputFile = new InputFile(lines);
		try {
			new SanitaryInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			fail();
		} catch (RuntimeException e) {
			assertEquals("Negative SNACOMP encountered", e.getMessage());
		}
	}

	@Test
	public void testNegMonthlyFactorFirstHalf() {
		String[] lines = { "SANITARY", "SNA#                   1", "SANCOMP=            2.68E-4",
				"JAN-JUNE=           0.92    0.94    1.03    -.04    1.04    1.08",
				"JULY-DEC=           1.10    1.09    1.01    0.93    0.92    0.90",
				"WEEKLY=             0.93    1.00    1.03    1.05    1.03    1.00    0.96",
				"01-06 AM            0.72    0.71    0.70    0.70    0.71    0.88",
				"07-12 AM            1.04    1.12    1.14    1.14    1.15    1.17",
				"01-06 PM            1.16    1.16    1.16    1.14    1.14    1.12",
				"07-12 PM            1.10    1.07    1.03    0.98    0.90    0.86", "END" };
		InputFile inputFile = new InputFile(lines);
		try {
			new SanitaryInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			fail();
		} catch (RuntimeException e) {
			assertEquals("Negative monthly flow factor encountered", e.getMessage());
		}
	}

	@Test
	public void testNegMonthlyFactorSecondHalf() {
		String[] lines = { "SANITARY", "SNA#                   1", "SANCOMP=            2.68E-4",
				"JAN-JUNE=           0.92    0.94    1.03    1.04    1.04    1.08",
				"JULY-DEC=           1.10    1.09    1.01    -.93    0.92    0.90",
				"WEEKLY=             0.93    1.00    1.03    1.05    1.03    1.00    0.96",
				"01-06 AM            0.72    0.71    0.70    0.70    0.71    0.88",
				"07-12 AM            1.04    1.12    1.14    1.14    1.15    1.17",
				"01-06 PM            1.16    1.16    1.16    1.14    1.14    1.12",
				"07-12 PM            1.10    1.07    1.03    0.98    0.90    0.86", "END" };
		InputFile inputFile = new InputFile(lines);
		try {
			new SanitaryInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			fail();
		} catch (RuntimeException e) {
			assertEquals("Negative monthly flow factor encountered", e.getMessage());
		}
	}

	@Test
	public void testNegWeekdayFlowFactor() {
		String[] lines = { "SANITARY", "SNA#                   1", "SANCOMP=            2.68E-4",
				"JAN-JUNE=           0.92    0.94    1.03    1.04    1.04    1.08",
				"JULY-DEC=           1.10    1.09    1.01    0.93    0.92    0.90",
				"WEEKLY=             0.93    1.00    -.03    1.05    1.03    1.00    0.96",
				"01-06 AM            0.72    0.71    0.70    0.70    0.71    0.88",
				"07-12 AM            1.04    1.12    1.14    1.14    1.15    1.17",
				"01-06 PM            1.16    1.16    1.16    1.14    1.14    1.12",
				"07-12 PM            1.10    1.07    1.03    0.98    0.90    0.86", "END" };
		InputFile inputFile = new InputFile(lines);
		try {
			new SanitaryInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			fail();
		} catch (RuntimeException e) {
			assertEquals("Negative weekday flow factor encountered", e.getMessage());
		}
	}

	@Test
	public void testNegHourlyFlowFactor() {
		String[] lines = { "SANITARY", "SNA#                   1", "SANCOMP=            2.68E-4",
				"JAN-JUNE=           0.92    0.94    1.03    1.04    1.04    1.08",
				"JULY-DEC=           1.10    1.09    1.01    0.93    0.92    0.90",
				"WEEKLY=             0.93    1.00    1.03    1.05    1.03    1.00    0.96",
				"01-06 AM            0.72    0.71    0.70    0.70    0.71    0.88",
				"07-12 AM            1.04    1.12    1.14    1.14    1.15    1.17",
				"01-06 PM            1.16    1.16    -.16    1.14    1.14    1.12",
				"07-12 PM            1.10    1.07    1.03    0.98    0.90    0.86", "END" };
		InputFile inputFile = new InputFile(lines);
		try {
			new SanitaryInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			fail();
		} catch (RuntimeException e) {
			assertEquals("Negative hourly flow factor encountered", e.getMessage());
		}
	}

}
