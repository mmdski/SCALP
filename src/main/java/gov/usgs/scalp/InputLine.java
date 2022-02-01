package gov.usgs.scalp;

import java.text.ParseException;
import java.util.LinkedList;

/**
 * SCALP input line
 */
public class InputLine {

	private String line;
	private int lineNo = -1;

	/**
	 * Constructs an input line from a string.
	 *
	 * @param line
	 *            SCALP input file line
	 */
	public InputLine(String line) {

		if (line == null)
			throw new NullPointerException();

		this.line = line;
	}

	/**
	 * Constructs an input line from a string and assigns a line number.
	 *
	 * @param line
	 *            SCALP input file line
	 * @param lineNo
	 *            Line number
	 */
	public InputLine(String line, int lineNo) {

		if (line == null)
			throw new NullPointerException();

		this.line = line;
		this.lineNo = lineNo;
	}

	/**
	 * Returns true if this line is a comment, false otherwise.
	 *
	 * SCALP comment lines start with "+++"
	 *
	 * @return true if this line is a comment, false if it is not
	 */
	public boolean isComment() {
		return getLine().startsWith("+++");
	}

	/**
	 * Returns true if this line is empty, false otherwise.
	 *
	 * A line is considered empty if it contains
	 *
	 * @return true if this line is empty, false if it is not.
	 */
	public boolean isEmpty() {
		return (getLine().trim().length() == 0);
	}

	/**
	 * Returns this line as a string.
	 *
	 * @return this line as a string.
	 */
	public String getLine() {
		return line;
	}

	/**
	 * Returns the line number of this line.
	 *
	 * @return line number
	 */
	public int getLineNo() {
		return lineNo;
	}

	/**
	 * Parses this line and returns an array of parsed values.
	 * <p>
	 * Raises ParseError if this method fails to parse this line based on the
	 * descriptor.
	 * <p>
	 * Recognized types include
	 *
	 * <table summary="">
	 * <thead>
	 * <tr>
	 * <th>Type</th>
	 * <th>Parsed as</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td><code>A</code></td>
	 * <td>String</td>
	 * </tr>
	 * <tr>
	 * <td><code>I</code></td>
	 * <td>Integer</td>
	 * </tr>
	 * <tr>
	 * <td><code>F</code></td>
	 * <td>Double</td>
	 * </tr>
	 * <tr>
	 * <td><code>E</code></td>
	 * <td>Double</td>
	 * </tr>
	 * <tr>
	 * <td><code>X</code></td>
	 * <td>Skipped (not returned)</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * <p>
	 * Note that the <code>X</code> type isn't parsed and no object is returned.
	 *
	 * Examples of descriptors
	 *
	 * <table summary="">
	 * <thead>
	 * <tr>
	 * <th>Descriptor</th>
	 * <th>Parsed as</th>
	 * <th>Returns</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td><code>I8</code></td>
	 * <td>Integer with an 8 character field width</td>
	 * <td><code>int</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>16X,E20</code></td>
	 * <td>16 characters are skipped and a float with a 20 character field
	 * width</td>
	 * <td><code>double</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>3F8</code></td>
	 * <td>Three doubles each with an 8 character field width</td>
	 * <td><code>double</code>, <code>double</code>, <code>double</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>3(5X,A3)</code></td>
	 * <td>Three sets of 5 skipped characters followed by strings with a field width
	 * of 3</td>
	 * <td><code>String</code>, <code>String</code>, <code>String</code></td>
	 * </tr>
	 * </tbody>
	 * </table>
	 *
	 * @param descriptor
	 *            format descriptor
	 * @return array of parsed objects
	 * @throws ParseException
	 *             if this instance is unable to parse the line using
	 *             <code>descriptor</code>
	 */
	public Object[] parse(String descriptor) throws ParseException {

		if (descriptor == null)
			throw new NullPointerException();

		InputDataDescriptor[] dataDescriptors = InputDataDescriptor.parseDescriptor(descriptor);

		return parse(dataDescriptors);
	}

	/**
	 * Parse this line and returns an array of parsed values
	 *
	 * @param dataDescriptors
	 *            array of data descriptors to parse this line as
	 * @return array of parsed objects
	 * @throws ParseException
	 *             if this instance is unable to parse the line using
	 *             <code>descriptor</code>
	 */
	public Object[] parse(InputDataDescriptor[] dataDescriptors) throws ParseException {

		if (dataDescriptors == null)
			throw new NullPointerException();

		LinkedList<Object> results = new LinkedList<Object>();

		int beginIndex = 0;
		int endIndex;

		String substring;
		int intValue;
		double doubleValue;

		// loop over each descriptor
		for (InputDataDescriptor d : dataDescriptors) {

			endIndex = beginIndex + d.getWidth();

			if (beginIndex > getLine().length() - 1)
				throw new ParseException("Parse failure", getLineNo());

			if (endIndex > getLine().length() - 1)
				substring = getLine().substring(beginIndex);
			else
				substring = getLine().substring(beginIndex, endIndex);

			// skip character if 'X'
			if (d.getType() == 'X') {
			}

			// parse as String
			else if (d.getType() == 'A') {
				results.addLast((Object) substring);
			}

			// parse as Integer
			else if (d.getType() == 'I') {
				try {
					intValue = Integer.parseInt(substring.trim());
					results.addLast((Object) intValue);
				} catch (NumberFormatException e) {
					throw new ParseException("Unable to parse as int", getLineNo());
				}
			}

			// parse as double
			else if (d.getType() == 'F' || d.getType() == 'E') {
				try {
					doubleValue = Double.parseDouble(substring.trim());
					results.addLast((Object) doubleValue);
				} catch (NumberFormatException e) {
					throw new ParseException("Unable to parse as double", getLineNo());
				}
			}

			// throw exception if type char is unrecognized
			else
				throw new ParseException(String.format("Unrecognized type %c", d.getType()), getLineNo());

			beginIndex = endIndex;
		}

		return results.toArray();

	}
}
