/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.usgs.scalp;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hfdoyle
 */
public class SCALPTextInputTest {

	/**
	 * Test of scalpLines method, of class SCALPTextInput.
	 */
	@Test
	public void testScalpLines() {
		Path filePath = Paths.get("src", "test", "resources", "example.c06");
		String filePathStr = filePath.toAbsolutePath().toString();
		String dssFilePath = Paths.get("src", "test", "resources", "test.dss").toAbsolutePath().toString();
		DSS inputDSS = new DSS(dssFilePath);
		SCALPTextInput instance;
		try {
			instance = new SCALPTextInput(filePathStr, inputDSS);
		} catch (ParseException e) {
			inputDSS.close();
			fail();
			return;
		}

		String[] expResult = { "WATERSHED", "TIME SPAN", "STARTING DATE       2016      10       1",
				"ENDING DATE         2017       9      30", "OUTPUT LEVEL", "           0", "DATA     ",
				"       2 A=BONEYARD B=GAGE 2                                                    ", "END", "SANITARY",
				"SNA#                         1", "SNACOMP=                  2.46800e-04",
				"JAN-JUNE=           0.92    0.94    1.03    1.04    1.04    1.08",
				"JULY-DEC=           1.10    1.09    1.01    0.93    0.92    0.90",
				"WEEKLY=             0.93    1.00    1.03    1.05    1.03    1.00    0.96",
				"01-06AM             0.72    0.71    0.70    0.70    0.71    0.88",
				"07AM-12PM           1.04    1.12    1.14    1.14    1.15    1.17",
				"01-06PM             1.16    1.16    1.16    1.14    1.14    1.12",
				"07PM-12AM           1.10    1.07    1.03    0.98    0.90    0.86", "END", "SCA", "ANALYSIS",
				"SCA#,AREA=             1   1.122", "LANDS", "SEG#,AREA=             2  0.0210  0.0310  0.0310", "END",
				"SANITARY", "SNA#,PE=               1   19531", "END", "INITIAL FLOWS",
				"OUT1(*)            14.40   14.40   14.40", "ROUTING", "STOPSTORE=            NO      NO      NO",
				"RK(*)            2580.00 2580.00 2580.00", "QMAX(*)          1436.00 1436.00 49.0000",
				"SPLIT(*)         27.7500 27.7500 27.7500", "OUTPUT",
				"        A=NORTH B=N1 F=                                                         ",
				"        A=NORTH B=N1 F=                                                         ", "EXECUTE",
				"FINISH   " };
		String[] result = instance.scalpLines();
		assertArrayEquals(expResult, result);
	}

	/**
	 * Test of inputFile method, of class SCALPTextInput.
	 */
	@Test
	public void testInputFile() {
		Path filePath = Paths.get("src", "test", "resources", "example.c06");
		String filePathStr = filePath.toAbsolutePath().toString();
		String dssFilePath = Paths.get("src", "test", "resources", "test.dss").toAbsolutePath().toString();
		DSS inputDSS = new DSS(dssFilePath);
		SCALPTextInput instance;
		try {
			instance = new SCALPTextInput(filePathStr, inputDSS);
		} catch (ParseException e) {
			inputDSS.close();
			fail();
			return;
		}

		InputFile inputFile = new InputFile(filePathStr);
		InputLine[] inputLine = inputFile.getLines();
		String[] expResult = new String[inputLine.length];
		for (int i = 0; i < inputFile.getLines().length; i++) {
			expResult[i] = inputLine[i].getLine();
		}
		String[] result = instance.inputFile();
		assertArrayEquals(expResult, result);
	}

	/**
	 * Test of getWatershedBlock method, of class SCALPTextInput.
	 */
	@Test
	public void testGetWatershedBlock() {
		Path filePath = Paths.get("src", "test", "resources", "example.c06");
		String filePathStr = filePath.toAbsolutePath().toString();
		String dssFilePath = Paths.get("src", "test", "resources", "test.dss").toAbsolutePath().toString();
		DSS inputDSS = new DSS(dssFilePath);
		SCALPTextInput instance;
		try {
			instance = new SCALPTextInput(filePathStr, inputDSS);
		} catch (ParseException e) {
			inputDSS.close();
			fail();
			return;
		}

		String[] expResult = { "WATERSHED", "TIME SPAN", "STARTING DATE       2016      10       1",
				"ENDING DATE         2017       9      30", "OUTPUT LEVEL", "           0", "DATA     ",
				"       2 A=BONEYARD B=GAGE 2                                                    ", "END" };
		String[] result = instance.getWatershedBlock().asString();
		assertArrayEquals(expResult, result);
	}

	/**
	 * Test of getSanitaryBlock method, of class SCALPTextInput.
	 */
	@Test
	public void testGetSanitaryBlock() {
		Path filePath = Paths.get("src", "test", "resources", "example.c06");
		String filePathStr = filePath.toAbsolutePath().toString();
		String dssFilePath = Paths.get("src", "test", "resources", "test.dss").toAbsolutePath().toString();
		DSS inputDSS = new DSS(dssFilePath);
		SCALPTextInput instance;
		try {
			instance = new SCALPTextInput(filePathStr, inputDSS);
		} catch (ParseException e) {
			inputDSS.close();
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
				"07PM-12AM           1.10    1.07    1.03    0.98    0.90    0.86", "END" };
		String[] result = instance.getSanitaryBlock().asString();
		assertArrayEquals(expResult, result);
	}

	/**
	 * Test of getScaNumbers method, of class SCALPTextInput, for an input with a
	 * single SCA block.
	 */
	@Test
	public void testGetScaNumbers() {
		Path filePath = Paths.get("src", "test", "resources", "example.c06");
		String filePathStr = filePath.toAbsolutePath().toString();
		String dssFilePath = Paths.get("src", "test", "resources", "test.dss").toAbsolutePath().toString();
		DSS inputDSS = new DSS(dssFilePath);
		SCALPTextInput instance;
		try {
			instance = new SCALPTextInput(filePathStr, inputDSS);
		} catch (ParseException e) {
			inputDSS.close();
			fail();
			return;
		}

		int[] expResult = { 1 };
		int[] result = instance.getScaNumbers();
		assertArrayEquals(expResult, result);
	}

	/**
	 * Test of getScaNumbers method, of class SCALPTextInput, for an input with
	 * multiple SCA blocks.
	 */
	@Test
	public void testMultipleScaNumbers() {
		String[] lines = { "WATERSHED", "+++ This is a test file", "", "TIME SPAN",
				"STARTING DATE       2016      10      01", "ENDING   DATE       2017      09      30", "",
				"OUTPUT LEVEL", "           0", "", "DATA", "       2 A=BONEYARD B=GAGE 2", "END", "", "SANITARY",
				"SNA#                   1", "SNACOMP=            2.468E-4",
				"JAN-JUNE=           0.92    0.94    1.03    1.04    1.04    1.08",
				"JULY-DEC=           1.10    1.09    1.01    0.93    0.92    0.90",
				"WEEKLY=             0.93    1.00    1.03    1.05    1.03    1.00    0.96",
				"01-06AM             0.72    0.71    0.70    0.70    0.71    0.88",
				"07AM-12PM           1.04    1.12    1.14    1.14    1.15    1.17",
				"01-06PM             1.16    1.16    1.16    1.14    1.14    1.12",
				"07PM-12AM           1.10    1.07    1.03    0.98    0.90    0.86", "END", "", "SCA", "", "ANALYSIS",
				"SCA#,AREA=             1   1.122", "", "LANDS", "SEG#,AREA=             2   0.021   0.031   0.031",
				"END", "", "SANITARY", "SNA#,PE#               1   19531", "END", "", "INITIAL FLOWS",
				"OUT1(*)=            14.4    14.4    14.4", "", "ROUTING", "STOPSTORE=            NO      NO      NO",
				"RK(*)=              2580    2580    2580", "QMAX(*)             1436    1436      49",
				"SPLIT(*)           27.75   27.75   27.75", "", "OUTPUT", "        A=NORTH B=N1 F=",
				"        A=NORTH B=N1 F=", "", "EXECUTE", "", "SCA", "", "ANALYSIS", "SCA#,AREA=             2   1.122",
				"", "LANDS", "SEG#,AREA=             2   0.021   0.031   0.031", "END", "", "SANITARY",
				"SNA#,PE#               1   19531", "END", "", "INITIAL FLOWS",
				"OUT1(*)=            14.4    14.4    14.4", "", "ROUTING", "STOPSTORE=            NO      NO      NO",
				"RK(*)=              2580    2580    2580", "QMAX(*)             1436    1436      49",
				"SPLIT(*)           27.75   27.75   27.75", "", "OUTPUT", "        A=NORTH B=N1 F=",
				"        A=NORTH B=N1 F=", "", "EXECUTE", "", "SCA", "", "ANALYSIS", "SCA#,AREA=             3   1.122",
				"", "LANDS", "SEG#,AREA=             2   0.021   0.031   0.031", "END", "", "SANITARY",
				"SNA#,PE#               1   19531", "END", "", "INITIAL FLOWS",
				"OUT1(*)=            14.4    14.4    14.4", "", "ROUTING", "STOPSTORE=            NO      NO      NO",
				"RK(*)=              2580    2580    2580", "QMAX(*)             1436    1436      49",
				"SPLIT(*)           27.75   27.75   27.75", "", "OUTPUT", "        A=NORTH B=N1 F=",
				"        A=NORTH B=N1 F=", "", "EXECUTE", "", "FINISH", "" };
		InputFile inputFile = new InputFile(lines);
		String dssFilePath = Paths.get("src", "test", "resources", "test.dss").toAbsolutePath().toString();
		DSS inputDSS = new DSS(dssFilePath);
		SCALPTextInput instance;
		try {
			instance = new SCALPTextInput(inputFile, inputDSS);
		} catch (ParseException e) {
			inputDSS.close();
			fail();
			return;
		}

		int[] expResult = { 1, 2, 3 };
		int[] result = instance.getScaNumbers();
		assertArrayEquals(expResult, result);
	}

	@Test
	public void testDuplicateScaNumbers() {
		String[] lines = { "WATERSHED", "+++ This is a test file", "", "TIME SPAN",
				"STARTING DATE       2016      10      01", "ENDING   DATE       2017      09      30", "",
				"OUTPUT LEVEL", "           0", "", "DATA", "       2 A=BONEYARD B=GAGE 2", "END", "", "SANITARY",
				"SNA#                   1", "SNACOMP=            2.468E-4",
				"JAN-JUNE=           0.92    0.94    1.03    1.04    1.04    1.08",
				"JULY-DEC=           1.10    1.09    1.01    0.93    0.92    0.90",
				"WEEKLY=             0.93    1.00    1.03    1.05    1.03    1.00    0.96",
				"01-06AM             0.72    0.71    0.70    0.70    0.71    0.88",
				"07AM-12PM           1.04    1.12    1.14    1.14    1.15    1.17",
				"01-06PM             1.16    1.16    1.16    1.14    1.14    1.12",
				"07PM-12AM           1.10    1.07    1.03    0.98    0.90    0.86", "END", "", "SCA", "", "ANALYSIS",
				"SCA#,AREA=             1   1.122", "", "LANDS", "SEG#,AREA=             2   0.021   0.031   0.031",
				"END", "", "SANITARY", "SNA#,PE#               1   19531", "END", "", "INITIAL FLOWS",
				"OUT1(*)=            14.4    14.4    14.4", "", "ROUTING", "STOPSTORE=            NO      NO      NO",
				"RK(*)=              2580    2580    2580", "QMAX(*)             1436    1436      49",
				"SPLIT(*)           27.75   27.75   27.75", "", "OUTPUT", "        A=NORTH B=N1 F=",
				"        A=NORTH B=N1 F=", "", "EXECUTE", "", "SCA", "", "ANALYSIS", "SCA#,AREA=             1   1.122",
				"", "LANDS", "SEG#,AREA=             2   0.021   0.031   0.031", "END", "", "SANITARY",
				"SNA#,PE#               1   19531", "END", "", "INITIAL FLOWS",
				"OUT1(*)=            14.4    14.4    14.4", "", "ROUTING", "STOPSTORE=            NO      NO      NO",
				"RK(*)=              2580    2580    2580", "QMAX(*)             1436    1436      49",
				"SPLIT(*)           27.75   27.75   27.75", "", "OUTPUT", "        A=NORTH B=N1 F=",
				"        A=NORTH B=N1 F=", "", "EXECUTE", "", "SCA", "", "ANALYSIS", "SCA#,AREA=             2   1.122",
				"", "LANDS", "SEG#,AREA=             2   0.021   0.031   0.031", "END", "", "SANITARY",
				"SNA#,PE#               1   19531", "END", "", "INITIAL FLOWS",
				"OUT1(*)=            14.4    14.4    14.4", "", "ROUTING", "STOPSTORE=            NO      NO      NO",
				"RK(*)=              2580    2580    2580", "QMAX(*)             1436    1436      49",
				"SPLIT(*)           27.75   27.75   27.75", "", "OUTPUT", "        A=NORTH B=N1 F=",
				"        A=NORTH B=N1 F=", "", "EXECUTE", "", "FINISH", "" };
		InputFile inputFile = new InputFile(lines);
		String dssFilePath = Paths.get("src", "test", "resources", "test.dss").toAbsolutePath().toString();
		DSS inputDSS = new DSS(dssFilePath);
		try {
			new SCALPTextInput(inputFile, inputDSS);
			fail();
		} catch (ParseException e) {
			inputDSS.close();
			fail();
			return;
		} catch (RuntimeException e) {
			if (!e.getMessage().equals("Duplicate special contributing area numbers encountered"))
				fail();
			return;
		}
		fail();
	}

	/**
	 * Test of getScaBlock method, of class SCALPTextInput.
	 */
	@Test
	public void testGetScaBlock() {
		Path filePath = Paths.get("src", "test", "resources", "example.c06");
		String filePathStr = filePath.toAbsolutePath().toString();
		String dssFilePath = Paths.get("src", "test", "resources", "test.dss").toAbsolutePath().toString();
		DSS inputDSS = new DSS(dssFilePath);
		SCALPTextInput instance;
		try {
			instance = new SCALPTextInput(filePathStr, inputDSS);
		} catch (ParseException e) {
			inputDSS.close();
			fail();
			return;
		}

		String[] expResult = { "SCA", "ANALYSIS", "SCA#,AREA=             1   1.122", "LANDS",
				"SEG#,AREA=             2  0.0210  0.0310  0.0310", "END", "SANITARY",
				"SNA#,PE=               1   19531", "END", "INITIAL FLOWS", "OUT1(*)            14.40   14.40   14.40",
				"ROUTING", "STOPSTORE=            NO      NO      NO", "RK(*)            2580.00 2580.00 2580.00",
				"QMAX(*)          1436.00 1436.00 49.0000", "SPLIT(*)         27.7500 27.7500 27.7500", "OUTPUT",
				"        A=NORTH B=N1 F=                                                         ",
				"        A=NORTH B=N1 F=                                                         ", "EXECUTE" };
		int scaNumber = 1;
		String[] result = instance.getScaBlock(scaNumber).asString();
		assertArrayEquals(expResult, result);
	}

}
