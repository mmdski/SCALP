package gov.usgs.scalp;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class InputDataDescriptorTest {

	@Test
	public void testParseWidth() {

		String widthNotSpecifiedDescriptor = "A";
		int width = InputDataDescriptor.parseDescriptor(widthNotSpecifiedDescriptor)[0].getWidth();
		assertEquals(width, 1);

		try {
			String nonDigitWidthDescriptor = "A?";
			InputDataDescriptor.parseDescriptor(nonDigitWidthDescriptor);
			fail();
		} catch (RuntimeException e) {
			assertEquals(e.getMessage(), "Width character is not a digit");
		}
	}

	@Test
	public void testParseType() {

		try {
			String nonAlphabeticTypeDescriptor = "?";
			InputDataDescriptor.parseDescriptor(nonAlphabeticTypeDescriptor);
			fail();
		} catch (RuntimeException e) {
			assertEquals("Type character must be alphabetic", e.getMessage());
		}
	}

	@Test
	public void testParseRepeatCount() {

		try {
			String typeNotSpecifiedDescriptor = "3";
			InputDataDescriptor.parseDescriptor(typeNotSpecifiedDescriptor);
			fail();
		} catch (RuntimeException e) {
			assertEquals("Character queue is empty", e.getMessage());
		}
	}

	@Test
	public void testParseDescriptorNull() {

		try {
			String nullDescriptor = null;
			InputDataDescriptor.parseDescriptor(nullDescriptor);
			fail();
		} catch (NullPointerException e) {
			;
		}
	}

	@Test
	public void testParseDescriptorExample() {

		String descriptor = "3(2X,A3)";

		InputDataDescriptor[] expected = { new InputDataDescriptor('X', 1), new InputDataDescriptor('X', 1),
				new InputDataDescriptor('A', 3), new InputDataDescriptor('X', 1), new InputDataDescriptor('X', 1),
				new InputDataDescriptor('A', 3), new InputDataDescriptor('X', 1), new InputDataDescriptor('X', 1),
				new InputDataDescriptor('A', 3) };

		InputDataDescriptor[] results = InputDataDescriptor.parseDescriptor(descriptor);

		assertEquals(expected.length, results.length);

		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i].getType(), results[i].getType());
			assertEquals(expected[i].getWidth(), results[i].getWidth());
		}

	}

}
