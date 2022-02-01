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
public class SCAInputBlockTest {

	@Test
	public void testSCAException() {
		String[] lines = { "???" };
		InputFile inputFile = new InputFile(lines);
		try {
			new SCAInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			assertEquals("Error parsing SCA input block", e.getMessage());
			assertEquals(1, e.getErrorOffset());
		}
	}

	@Test
	public void testAnalysisException() {
		String[] lines = { "SCA", "????????" };
		InputFile inputFile = new InputFile(lines);
		try {
			new SCAInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			assertEquals("Error parsing SCA input block", e.getMessage());
			assertEquals(2, e.getErrorOffset());
		}
	}

	@Test
	public void testLandsException() {
		String[] lines = { "SCA", "ANALYSIS", "SCA#,AREA=             1   1.122", "?????" };
		InputFile inputFile = new InputFile(lines);
		try {
			new SCAInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			assertEquals("Error parsing SCA input block", e.getMessage());
			assertEquals(4, e.getErrorOffset());
		}
	}

	@Test
	public void testSanitaryException() {
		String[] lines = { "SCA", "ANALYSIS", "SCA#,AREA=             1   1.122", "LANDS",
				"SEG#,AREA=             2   0.021   0.031   0.031", "SEG#,AREA=             3   0.046   0.060   0.060",
				"END", "????????" };
		InputFile inputFile = new InputFile(lines);
		try {
			new SCAInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			assertEquals("Error parsing SCA input block", e.getMessage());
			assertEquals(8, e.getErrorOffset());
		}
	}

	@Test
	public void testEndException() {
		String[] lines = { "SCA", "ANALYSIS", "SCA#,AREA=             1   1.122", "LANDS",
				"SEG#,AREA=             2   0.021   0.031   0.031", "SEG#,AREA=             3   0.046   0.060   0.060",
				"END", "SANITARY", "SNA#,PE#               1   19531", "???" };
		InputFile inputFile = new InputFile(lines);
		try {
			new SCAInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			assertEquals("Error parsing SCA input block", e.getMessage());
			assertEquals(10, e.getErrorOffset());
		}
	}

	@Test
	public void testInitialFlowsException() {
		String[] lines = { "SCA", "ANALYSIS", "SCA#,AREA=             1   1.122", "LANDS",
				"SEG#,AREA=             2   0.021   0.031   0.031", "SEG#,AREA=             3   0.046   0.060   0.060",
				"END", "SANITARY", "SNA#,PE#               1   19531", "END", "?????????????" };
		InputFile inputFile = new InputFile(lines);
		try {
			new SCAInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			assertEquals("Error parsing SCA input block", e.getMessage());
			assertEquals(11, e.getErrorOffset());
		}
	}

	@Test
	public void testOutputException() {
		String[] lines = { "SCA", "ANALYSIS", "SCA#,AREA=             1   1.122", "LANDS",
				"SEG#,AREA=             2   0.021   0.031   0.031", "SEG#,AREA=             3   0.046   0.060   0.060",
				"END", "SANITARY", "SNA#,PE#               1   19531", "END", "INITIAL FLOWS",
				"OUT1(*)=            14.4    14.4    14.4", "ROUTING", "STOPSTORE=            NO      NO      NO",
				"RK(*)=              2580    2580    2580", "QMAX(*)             1436    1436      49",
				"SPLIT(*)           27.75   27.75   27.75", "??????" };
		InputFile inputFile = new InputFile(lines);
		try {
			new SCAInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			assertEquals("Error parsing SCA input block", e.getMessage());
			assertEquals(18, e.getErrorOffset());
		}
	}

	@Test
	public void testExecuteException() {
		String[] lines = { "SCA", "ANALYSIS", "SCA#,AREA=             1   1.122", "LANDS",
				"SEG#,AREA=             2   0.021   0.031   0.031", "SEG#,AREA=             3   0.046   0.060   0.060",
				"END", "SANITARY", "SNA#,PE#               1   19531", "END", "INITIAL FLOWS",
				"OUT1(*)=            14.4    14.4    14.4", "ROUTING", "STOPSTORE=            NO      NO      NO",
				"RK(*)=              2580    2580    2580", "QMAX(*)             1436    1436      49",
				"SPLIT(*)           27.75   27.75   27.75", "OUTPUT", "        A=NORTH B=N1 F=",
				"        A=NORTH B=N1 F=", "???????" };
		InputFile inputFile = new InputFile(lines);
		try {
			new SCAInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			assertEquals("Error parsing SCA input block", e.getMessage());
			assertEquals(21, e.getErrorOffset());
		}
	}

	@Test
	public void testRoutingException() {
		String[] lines = { "SCA", "ANALYSIS", "SCA#,AREA=             1   1.122", "LANDS",
				"SEG#,AREA=             2   0.021   0.031   0.031", "SEG#,AREA=             3   0.046   0.060   0.060",
				"END", "SANITARY", "SNA#,PE#               1   19531", "END", "INITIAL FLOWS",
				"OUT1(*)=            14.4    14.4    14.4", "???????" };
		InputFile inputFile = new InputFile(lines);
		try {
			new SCAInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			assertEquals("Error parsing routing info", e.getMessage());
			assertEquals(13, e.getErrorOffset());
		}
	}

	@Test
	public void testStopStoreException() {
		String[] lines = { "SCA", "ANALYSIS", "SCA#,AREA=             1   1.122", "LANDS",
				"SEG#,AREA=             2   0.021   0.031   0.031", "SEG#,AREA=             3   0.046   0.060   0.060",
				"END", "SANITARY", "SNA#,PE#               1   19531", "END", "INITIAL FLOWS",
				"OUT1(*)=            14.4    14.4    14.4", "ROUTING", "STOPSTORE=           ???     ???     ???" };
		InputFile inputFile = new InputFile(lines);
		try {
			new SCAInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			assertEquals("Error parsing routing info", e.getMessage());
			assertEquals(14, e.getErrorOffset());
		}
	}

	/**
	 * Test for specifying sumTreatment in InputFile
	 */
	@Test
	public void testSumTreatmentSpecified() {
		String[] lines = { "SCA", "ANALYSIS", "SCA#,AREA=             1   1.122", "LANDS",
				"SEG#,AREA=             2   0.021   0.031   0.031", "SEG#,AREA=             3   0.046   0.060   0.060",
				"END", "SANITARY", "SNA#,PE#               1   19531", "END", "INITIAL FLOWS",
				"OUT1(*)=            14.4    14.4    14.4", "ROUTING", "STOPSTORE=            NO      NO      NO",
				"RK(*)=              2580    2580    2580", "QMAX(*)             1436    1436      49",
				"SPLIT(*)           27.75   27.75   27.75", "OUTPUT", "       1A=NORTH B=N1 F=",
				"        A=NORTH B=N1 F=", "EXECUTE" };
		InputFile inputFile = new InputFile(lines);
		try {
			new SCAInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			fail();
		} catch (RuntimeException e) {
			assertEquals("Error parsing SCA input block: Output summation not supported", e.getMessage());
		}
	}

	/**
	 * Test for specifying sumOverflows in InputFile
	 */
	@Test
	public void testSumOverflowsSpecified() {
		String[] lines = { "SCA", "ANALYSIS", "SCA#,AREA=             1   1.122", "LANDS",
				"SEG#,AREA=             2   0.021   0.031   0.031", "SEG#,AREA=             3   0.046   0.060   0.060",
				"END", "SANITARY", "SNA#,PE#               1   19531", "END", "INITIAL FLOWS",
				"OUT1(*)=            14.4    14.4    14.4", "ROUTING", "STOPSTORE=            NO      NO      NO",
				"RK(*)=              2580    2580    2580", "QMAX(*)             1436    1436      49",
				"SPLIT(*)           27.75   27.75   27.75", "OUTPUT", "        A=NORTH B=N1 F=",
				"       2A=NORTH B=N1 F=", "EXECUTE" };
		InputFile inputFile = new InputFile(lines);
		try {
			new SCAInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			fail();
		} catch (RuntimeException e) {
			assertEquals("Error parsing SCA input block: Output summation not supported", e.getMessage());
		}
	}

	@Test
	public void testDuplicateSegNo() {
		String[] lines = { "SCA", "ANALYSIS", "SCA#,AREA=             1   1.122", "LANDS",
				"SEG#,AREA=             1   0.021   0.031   0.031", "SEG#,AREA=             1   0.046   0.060   0.060",
				"END", "SANITARY", "SNA#,PE#               1   19531", "END", "INITIAL FLOWS",
				"OUT1(*)=            14.4    14.4    14.4", "ROUTING", "STOPSTORE=            NO      NO      NO",
				"RK(*)=              2580    2580    2580", "QMAX(*)             1436    1436      49",
				"SPLIT(*)           27.75   27.75   27.75", "OUTPUT", "        A=NORTH B=N1 F=",
				"        A=NORTH B=N1 F=", "EXECUTE" };
		InputFile inputFile = new InputFile(lines);
		try {
			new SCAInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			fail();
		} catch (RuntimeException e) {
			assertEquals("Duplicate land segment number encountered", e.getMessage());
		}
	}

	/**
	 * Test of asString method, of class SCAInputBlock.
	 */
	@Test
	public void testAsString() {
		String[] lines = { "SCA", "ANALYSIS", "SCA#,AREA=             1   1.122", "LANDS",
				"SEG#,AREA=             2   0.021   0.031   0.031", "SEG#,AREA=             3   0.046   0.060   0.060",
				"END", "SANITARY", "SNA#,PE#               1   19531", "END", "INITIAL FLOWS",
				"OUT1(*)=            14.4    14.4    14.4", "ROUTING", "STOPSTORE=            NO      NO      NO",
				"RK(*)=              2580    2580    2580", "QMAX(*)             1436    1436      49",
				"SPLIT(*)           27.75   27.75   27.75", "OUTPUT", "        A=NORTH B=N1 F=",
				"        A=NORTH B=N1 F=", "EXECUTE" };
		InputFile inputFile = new InputFile(lines);
		SCAInputBlock instance;
		try {
			instance = new SCAInputBlock(inputFile);
		} catch (ParseException e) {
			fail();
			return;
		}
		String[] expResult = { "SCA", "ANALYSIS", "SCA#,AREA=             1   1.122", "LANDS",
				"SEG#,AREA=             2  0.0210  0.0310  0.0310", "SEG#,AREA=             3  0.0460  0.0600  0.0600",
				"END", "SANITARY", "SNA#,PE=               1   19531", "END", "INITIAL FLOWS",
				"OUT1(*)            14.40   14.40   14.40", "ROUTING", "STOPSTORE=            NO      NO      NO",
				"RK(*)            2580.00 2580.00 2580.00", "QMAX(*)          1436.00 1436.00 49.0000",
				"SPLIT(*)         27.7500 27.7500 27.7500", "OUTPUT",
				"        A=NORTH B=N1 F=                                                         ",
				"        A=NORTH B=N1 F=                                                         ", "EXECUTE" };
		String[] result = instance.asString();
		assertArrayEquals(expResult, result);
	}

	/**
	 * Test of getScaNumber method, of class SCAInputBlock.
	 */
	@Test
	public void testGetScaNumber() {
		String[] lines = { "SCA", "ANALYSIS", "SCA#,AREA=             1   1.122", "LANDS",
				"SEG#,AREA=             2   0.021   0.031   0.031", "SEG#,AREA=             3   0.046   0.060   0.060",
				"END", "SANITARY", "SNA#,PE#               1   19531", "END", "INITIAL FLOWS",
				"OUT1(*)=            14.4    14.4    14.4", "ROUTING", "STOPSTORE=            NO      NO      NO",
				"RK(*)=              2580    2580    2580", "QMAX(*)             1436    1436      49",
				"SPLIT(*)           27.75   27.75   27.75", "OUTPUT", "        A=NORTH B=N1 F=",
				"        A=NORTH B=N1 F=", "EXECUTE" };
		InputFile inputFile = new InputFile(lines);
		SCAInputBlock instance;
		try {
			instance = new SCAInputBlock(inputFile);
		} catch (ParseException e) {
			fail();
			return;
		}
		int expResult = 1;
		int result = instance.getSCANumber();
		assertEquals(expResult, result);
	}

	@Test
	public void testNegLandSegmentArea() {
		String[] lines = { "SCA", "ANALYSIS", "SCA#,AREA=             1   1.122", "LANDS",
				"SEG#,AREA=             2   0.021   -.031   0.031", "SEG#,AREA=             3   0.046   0.060   0.060",
				"END", "SANITARY", "SNA#,PE#               1   19531", "END", "INITIAL FLOWS",
				"OUT1(*)=            14.4    14.4    14.4", "ROUTING", "STOPSTORE=            NO      NO      NO",
				"RK(*)=              2580    2580    2580", "QMAX(*)             1436    1436      49",
				"SPLIT(*)           27.75   27.75   27.75", "OUTPUT", "        A=NORTH B=N1 F=",
				"        A=NORTH B=N1 F=", "EXECUTE" };
		InputFile inputFile = new InputFile(lines);
		try {
			new SCAInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			fail();
			return;
		} catch (RuntimeException e) {
			assertEquals("Negative land segment area encountered", e.getMessage());
		}
	}

	@Test
	public void testNegPopulationEquivalent() {
		String[] lines = { "SCA", "ANALYSIS", "SCA#,AREA=             1   1.122", "LANDS",
				"SEG#,AREA=             2   0.021   0.031   0.031", "SEG#,AREA=             3   0.046   0.060   0.060",
				"END", "SANITARY", "SNA#,PE#               1  -19531", "END", "INITIAL FLOWS",
				"OUT1(*)=            14.4    14.4    14.4", "ROUTING", "STOPSTORE=            NO      NO      NO",
				"RK(*)=              2580    2580    2580", "QMAX(*)             1436    1436      49",
				"SPLIT(*)           27.75   27.75   27.75", "OUTPUT", "        A=NORTH B=N1 F=",
				"        A=NORTH B=N1 F=", "EXECUTE" };
		InputFile inputFile = new InputFile(lines);
		try {
			new SCAInputBlock(inputFile);
		} catch (ParseException e) {
			fail();
			return;
		} catch (RuntimeException e) {
			assertEquals("Negative population equivalent encountered", e.getMessage());
		}
	}

	@Test
	public void testNegRoutingConstant() {
		String[] lines = { "SCA", "ANALYSIS", "SCA#,AREA=             1   1.122", "LANDS",
				"SEG#,AREA=             2   0.021   0.031   0.031", "SEG#,AREA=             3   0.046   0.060   0.060",
				"END", "SANITARY", "SNA#,PE#               1   19531", "END", "INITIAL FLOWS",
				"OUT1(*)=            14.4    14.4    14.4", "ROUTING", "STOPSTORE=            NO      NO      NO",
				"RK(*)=              2580   -2580    2580", "QMAX(*)             1436    1436      49",
				"SPLIT(*)           27.75   27.75   27.75", "OUTPUT", "        A=NORTH B=N1 F=",
				"        A=NORTH B=N1 F=", "EXECUTE" };
		InputFile inputFile = new InputFile(lines);
		try {
			new SCAInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			fail();
			return;
		} catch (RuntimeException e) {
			assertEquals("Negative routing constant encountered", e.getMessage());
		}
	}

	@Test
	public void testNegInitialFlow() {
		String[] lines = { "SCA", "ANALYSIS", "SCA#,AREA=             1   1.122", "LANDS",
				"SEG#,AREA=             2   0.021   0.031   0.031", "SEG#,AREA=             3   0.046   0.060   0.060",
				"END", "SANITARY", "SNA#,PE#               1   19531", "END", "INITIAL FLOWS",
				"OUT1(*)=            14.4   -14.4    14.4", "ROUTING", "STOPSTORE=            NO      NO      NO",
				"RK(*)=              2580    2580    2580", "QMAX(*)             1436    1436      49",
				"SPLIT(*)           27.75   27.75   27.75", "OUTPUT", "        A=NORTH B=N1 F=",
				"        A=NORTH B=N1 F=", "EXECUTE" };
		InputFile inputFile = new InputFile(lines);
		try {
			new SCAInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			fail();
			return;
		} catch (RuntimeException e) {
			assertEquals("Negative initial flow value encountered", e.getMessage());
		}
	}

	@Test
	public void testNegMaxFlow() {
		String[] lines = { "SCA", "ANALYSIS", "SCA#,AREA=             1   1.122", "LANDS",
				"SEG#,AREA=             2   0.021   0.031   0.031", "SEG#,AREA=             3   0.046   0.060   0.060",
				"END", "SANITARY", "SNA#,PE#               1   19531", "END", "INITIAL FLOWS",
				"OUT1(*)=            14.4    14.4    14.4", "ROUTING", "STOPSTORE=            NO      NO      NO",
				"RK(*)=              2580    2580    2580", "QMAX(*)             1436    1436     -49",
				"SPLIT(*)           27.75   27.75   27.75", "OUTPUT", "        A=NORTH B=N1 F=",
				"        A=NORTH B=N1 F=", "EXECUTE" };
		InputFile inputFile = new InputFile(lines);
		try {
			new SCAInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			fail();
			return;
		} catch (RuntimeException e) {
			assertEquals("Negative QMAX value encountered", e.getMessage());
		}
	}

	@Test
	public void testNegSplitFlow() {
		String[] lines = { "SCA", "ANALYSIS", "SCA#,AREA=             1   1.122", "LANDS",
				"SEG#,AREA=             2   0.021   0.031   0.031", "SEG#,AREA=             3   0.046   0.060   0.060",
				"END", "SANITARY", "SNA#,PE#               1   19531", "END", "INITIAL FLOWS",
				"OUT1(*)=            14.4    14.4    14.4", "ROUTING", "STOPSTORE=            NO      NO      NO",
				"RK(*)=              2580    2580    2580", "QMAX(*)             1436    1436      49",
				"SPLIT(*)           27.75  -27.75   27.75", "OUTPUT", "        A=NORTH B=N1 F=",
				"        A=NORTH B=N1 F=", "EXECUTE" };
		InputFile inputFile = new InputFile(lines);
		try {
			new SCAInputBlock(inputFile);
			fail();
		} catch (ParseException e) {
			fail();
			return;
		} catch (RuntimeException e) {
			assertEquals("Negative QSPLIT value encountered", e.getMessage());
		}
	}

}
