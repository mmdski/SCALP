/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.usgs.scalp;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hfdoyle
 */
public class InputFileTest {

	@Test
	public void testInputFile() {
		String filePath = "";
		try {
			new InputFile(filePath);
		} catch (RuntimeException e) {
			assertEquals("Unable to open file " + filePath, e.getMessage());
		}
	}

	@Test
	public void testGetLine() {
		int lineNo = 0;
		String[] lines = { "This is the first test line." };
		InputFile instance = new InputFile(lines);
		InputLine expResult = new InputLine(lines[0]);
		InputLine result = instance.getLine(lineNo);
		assertEquals(expResult.getLine(), result.getLine());

		lineNo = -1;
		try {
			instance.getLine(lineNo);
			fail();
		} catch (IllegalArgumentException e) {
			;
		}

		lineNo = 4;
		try {
			instance.getLine(lineNo);
			fail();
		} catch (IllegalArgumentException e) {
			;
		}

	}

	/**
	 * Test of lines method, of class InputFile.
	 */
	@Test
	public void testGetLines() {
		String[] lines = { "This is the first test line.", "This is the second test line.",
				"This is the third test line." };
		InputFile instance = new InputFile(lines);
		InputLine[] expResult = { new InputLine(lines[0]), new InputLine(lines[1]), new InputLine(lines[2]) };
		InputLine[] result = instance.getLines();
		for (int i = 0; i < lines.length; i++) {
			assertEquals(expResult[i].getLine(), result[i].getLine());
		}
	}

	/**
	 * Test of nextLine method, of class InputFile.
	 */
	@Test
	public void testNextLine() {
		String[] lines = { "This is the first test line." };
		InputFile instance = new InputFile(lines);
		InputLine expResult = new InputLine(lines[0]);
		InputLine result = instance.nextLine();
		assertEquals(expResult.getLine(), result.getLine());

		result = instance.nextLine();
		assertNull(result);
	}

	/**
	 * Test of nextNonEmptyLine method, of class InputFile.
	 */
	@Test
	public void testNextNonEmptyLine() {
		String[] lines = { "This is the first test line.", "+++ This is a comment line.", " " };
		InputFile instance = new InputFile(lines);
		InputLine expResult = new InputLine(lines[0]);
		InputLine result = instance.nextNonEmptyLine();
		assertEquals(expResult.getLine(), result.getLine());

		result = instance.nextNonEmptyLine();
		assertNull(result);
	}

	/**
	 * Test of setCurrentLineNo method, of class InputFile.
	 */
	@Test
	public void testSetCurrentLineNo() {
		int lineNo = 0;
		Path filePath = Paths.get("src", "test", "resources", "example.c06");
		String filePathStr = filePath.toAbsolutePath().toString();
		InputFile instance = new InputFile(filePathStr);
		instance.setCurrentLineNo(lineNo);

		lineNo = -1;
		try {
			instance.setCurrentLineNo(lineNo);
			fail();
		} catch (IllegalArgumentException e) {
			;
		}

		lineNo = 100;
		try {
			instance.setCurrentLineNo(lineNo);
			fail();
		} catch (IllegalArgumentException e) {
			;
		}
	}

}
