package gov.usgs.scalp;

/**
 * Parses DSS parts strings
 */
public class DSSPartsString {

	private String partA;
	private String partB;
	private String partC = "FLOW";
	private String partD = "01JAN2000"; // Seems to work so long as there's a date
	private String partE = "1HOUR";
	private String partF;

	/**
	 * Constructs a DSS parts string
	 * <p>
	 * Parts A and B must be present, part F is optional.
	 *
	 * @param dssParts
	 *            string of DSS parts
	 */
	public DSSPartsString(String dssParts) {

		if (dssParts == null)
			throw new NullPointerException();

		char[] chars = dssParts.trim().toCharArray();

		// get part A
		if (chars[0] != 'A' && chars[1] != '=')
			throw new RuntimeException("Malformed string");

		int nChars = chars.length;
		int partABegin = 2;
		int i = partABegin;
		int partAEnd;

		// traverse the string until a second equal sign is encountered
		while (i < nChars && chars[i] != '=')
			i++;

		// throw an exception if the end of the string has been reached
		if (i == nChars)
			throw new RuntimeException("Malformed string");

		// part B must be present
		if (chars[i - 1] == 'B' && chars[i - 2] == ' ')
			partAEnd = i - 2;
		else
			throw new RuntimeException("Malformed string");

		partA = new String(chars, partABegin, partAEnd - partABegin);

		// set the beginning of part B to the character after the equal sign
		int partBBegin = i + 1;

		// look for part F
		i++;
		while (i < nChars && chars[i] != '=')
			i++;

		// assume part F isn't present if the end of the string has been reached
		int partBEnd;
		int partFBegin;
		if (i == nChars) {
			partBEnd = nChars;
			partF = null;
		} else {
			// the last part must be part F
			if (chars[i - 1] == 'F' && chars[i - 2] == ' ') {
				partBEnd = i - 2;
				partFBegin = i + 1;
				partF = new String(chars, partFBegin, nChars - partFBegin);
			} else
				throw new RuntimeException("Malformed string");
		}

		partB = new String(chars, partBBegin, partBEnd - partBBegin);
	}

	/**
	 * Returns part A
	 *
	 * @return part A
	 */
	public String getPartA() {
		return partA;
	}

	/**
	 * Returns part B
	 *
	 * @return part B
	 */
	public String getPartB() {
		return partB;
	}

	/**
	 * Returns part C
	 *
	 * @return part C
	 */
	public String getPartC() {
		return partC;
	}

	/**
	 * Returns part D
	 *
	 * @return part D
	 */
	public String getPartD() {
		return partD;
	}

	/**
	 * Returns part E
	 *
	 * @return part E
	 */
	public String getPartE() {
		return partE;
	}

	/**
	 * Returns part F, null if part F is not present
	 *
	 * @return part F
	 */
	public String getPartF() {
		return partF;
	}
}
