package gov.usgs.scalp;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.ParseException;

public class InputLineTest {

	@Test
	public void testGetLineNo() {

		InputLine testLine = new InputLine("test line", 0);
		assertEquals(testLine.getLine(), "test line");
		assertEquals(testLine.getLineNo(), 0);
	}

	@Test
	public void testEmpty() {

		InputLine emptyLine = new InputLine("");
		InputLine notEmptyLine = new InputLine("this line is not empty");

		assertTrue(emptyLine.isEmpty());
		assertFalse(notEmptyLine.isEmpty());
	}

	@Test
	public void testComment() {

		InputLine commentLine = new InputLine("+++ this is a comment");
		InputLine notCommentLine = new InputLine("this is not a comment");

		assertTrue(commentLine.isComment());
		assertFalse(notCommentLine.isComment());
	}

	@Test
	public void testDouble() {
		InputLine doubleLine = new InputLine("                2.468E-4");
		try {
			Object[] results = doubleLine.parse("16X,E20");
			assertEquals((double) results[0], 2.468e-4, 1e-5);
		} catch (ParseException e) {
			fail();
		}
	}

	@Test
	public void testNonDouble() {
		InputLine nonDoubleLine = new InputLine("                ?");
		try {
			nonDoubleLine.parse("16X,E20");
		} catch (ParseException e) {
			assertEquals(e.getMessage(), "Unable to parse as double");
		}
	}

	@Test
	public void testInteger() {
		InputLine integerLine = new InputLine("           0");
		try {
			Object[] results = integerLine.parse("11X,I1");
			assertEquals((int) results[0], 0);
		} catch (ParseException e) {
			fail();
		}
	}

	@Test
	public void testNonInteger() {
		InputLine nonIntegerLine = new InputLine("           ?");
		try {
			nonIntegerLine.parse("11X,I1");
			fail();
		} catch (ParseException e) {
			assertEquals(e.getMessage(), "Unable to parse as int");
		}
	}

	@Test
	public void testString() {
		InputLine stringLine = new InputLine("WATERSHED");
		try {
			Object[] results = stringLine.parse("A9");
			assertEquals((String) results[0], "WATERSHED");
		} catch (ParseException e) {
			fail();
		}
	}

	@Test
	public void testParseEmpty() {
		InputLine emptyLine = new InputLine("");
		try {
			emptyLine.parse("A9");
			fail();
		} catch (ParseException e) {
			assertEquals(e.getMessage(), "Parse failure");
		}
	}

	@Test
	public void testParse() {
		InputLine inputline = new InputLine("Test line");
		try {
			inputline.parse("L3");
		} catch (ParseException e) {
			assertEquals(e.getMessage(), "Unrecognized type L");
		}

	}
}
