package gov.usgs.scalp;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Data descriptor for parsing input data from text
 */

public class InputDataDescriptor {

	/**
	 * Type of data
	 */
	private char type;

	/**
	 * Field width of data
	 */
	private int width;

	/**
	 * Class constructor
	 *
	 * @param type
	 *            type of data
	 * @param width
	 *            field width of data
	 */
	public InputDataDescriptor(char type, int width) {
		this.type = type;
		this.width = width;
	}

	private static int parseRepeatCount(Queue<Character> formatCharacters) {

		assert formatCharacters != null;

		StringBuffer countBuffer = new StringBuffer();

		while (!formatCharacters.isEmpty() && Character.isDigit(formatCharacters.peek())) {
			countBuffer.append(formatCharacters.remove());
		}

		if (formatCharacters.isEmpty())
			throw new RuntimeException("Character queue is empty");

		if (countBuffer.length() == 0)
			return 1;
		else {
			return Integer.parseInt(countBuffer.toString());
		}
	}

	private static char parseType(Queue<Character> formatCharacters) {

		assert formatCharacters != null;

		assert !formatCharacters.isEmpty();

		if (!Character.isAlphabetic(formatCharacters.peek()))
			throw new RuntimeException("Type character must be alphabetic");

		return formatCharacters.remove();
	}

	private static int parseWidth(Queue<Character> formatCharacters) {

		assert formatCharacters != null;

		int width;

		if (formatCharacters.isEmpty()) {
			width = 1;
		} else {

			StringBuffer widthBuffer = new StringBuffer();

			while (!formatCharacters.isEmpty() && (formatCharacters.peek() != ',')
					&& (formatCharacters.peek() != ')')) {

				if (!Character.isDigit(formatCharacters.peek()))
					throw new RuntimeException("Width character is not a digit");

				widthBuffer.append(formatCharacters.remove());
			}
			width = Integer.parseInt(widthBuffer.toString());
		}

		return width;
	}

	private static InputDataDescriptor[] parseDescriptor(Queue<Character> descriptorStr) {

		assert descriptorStr != null;

		int repeatCount;
		char type;
		int width;

		LinkedList<InputDataDescriptor> dataDescriptorList = new LinkedList<InputDataDescriptor>();
		InputDataDescriptor[] descriptors;

		while (descriptorStr.peek() != null) {

			if (descriptorStr.peek() == ',')
				descriptorStr.remove();

			repeatCount = parseRepeatCount(descriptorStr);

			if (descriptorStr.peek() == '(') {
				descriptorStr.remove();
				descriptors = parseDescriptor(descriptorStr);
			} else if (descriptorStr.peek() == ')') {
				descriptorStr.remove();
				descriptors = new InputDataDescriptor[0];
			} else {
				type = parseType(descriptorStr);
				if (type == 'X')
					width = 1;
				else
					width = parseWidth(descriptorStr);

				descriptors = new InputDataDescriptor[1];
				descriptors[0] = new InputDataDescriptor(type, width);
			}

			for (int i = 0; i < repeatCount; i++) {
				for (int j = 0; j < descriptors.length; j++)
					dataDescriptorList.add(descriptors[j]);
			}
		}

		InputDataDescriptor[] dataDescriptors = new InputDataDescriptor[dataDescriptorList.size()];
		return dataDescriptorList.toArray(dataDescriptors);
	}

	/**
	 * Parses descriptor string into array of input data descriptors
	 * <p>
	 * The general format of <code>descriptor</code> is <code>{r}tw</code>, where
	 * <code>{r}</code> is the repeat count (optional), <code>t</code> is the type,
	 * and <code>w</code> is the field width.
	 *
	 * @param descriptor
	 *            format descriptor
	 * @return array of data descriptors
	 */
	public static InputDataDescriptor[] parseDescriptor(String descriptor) {

		if (descriptor == null)
			throw new NullPointerException();

		LinkedList<Character> descriptorStr = new LinkedList<Character>();

		for (char c : descriptor.toCharArray())
			descriptorStr.add(c);

		return parseDescriptor(descriptorStr);
	}

	/**
	 * @return the type
	 */
	public char getType() {
		return type;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

}
