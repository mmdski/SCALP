package gov.usgs.scalp;

import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Input from a SCALP text file
 */
public class SCALPTextInput {

	private InputFile inputFile;
	private WatershedInputBlock watershedBlock;
	private SanitaryInputBlock sanitaryBlock;
	private LinkedHashMap<Integer, SCAInputBlock> scaBlocks;

	private final static Logger LOGGER = Logger.getLogger("gov.usgs.scalp.SCALPTextInput");

	/**
	 * Construct SCALP text input
	 *
	 * @param filePath
	 *            path to SCALP text file
	 * @param inputDSS
	 *            SCALP DSS instance
	 * @throws ParseException
	 *             when a parsing error is encountered
	 */
	public SCALPTextInput(String filePath, DSS inputDSS) throws ParseException {

		this(new InputFile(filePath), inputDSS);

	}

	/**
	 * Constructs SCALP text input
	 *
	 * @param inputFile
	 *            SCALP input file
	 * @param inputDSS
	 *            SCALP DSS instance
	 * @throws ParseException
	 *             when a parsing error is encountered
	 */
	public SCALPTextInput(InputFile inputFile, DSS inputDSS) throws ParseException {

		if (inputFile == null || inputDSS == null)
			throw new NullPointerException();

		this.inputFile = inputFile;
		watershedBlock = new WatershedInputBlock(this.inputFile);
		sanitaryBlock = new SanitaryInputBlock(this.inputFile);

		boolean finishFile = false;
		scaBlocks = new LinkedHashMap<>();
		InputLine inputLine;
		Object[] data;
		SCAInputBlock scaBlock;
		while (!finishFile) {
			inputLine = this.inputFile.nextNonEmptyLine();
			data = inputLine.parse("A9");
			if ("FINISH".equals((String) data[0]))
				finishFile = true;
			else {
				this.inputFile.setCurrentLineNo(inputLine.getLineNo());
				scaBlock = new SCAInputBlock(this.inputFile);

				int scaNumber = scaBlock.getSCANumber();

				if (scaBlocks.containsKey(scaNumber)) {
					LOGGER.log(Level.SEVERE,
							String.format("Duplicate special contributing area number: %2d", scaNumber));
					throw new RuntimeException("Duplicate special contributing area numbers encountered");
				}

				scaBlocks.put(scaNumber, scaBlock);
			}
		}
	}

	/**
	 * Returns the input file lines as an array of strings
	 *
	 * The contents of the file that is returned is equivalent to the contents of
	 * the file provided for construction.
	 *
	 * @return input file lines
	 */
	public String[] inputFile() {
		InputLine[] inputLines = inputFile.getLines();
		String[] lines = new String[inputLines.length];

		int i = 0;
		for (InputLine l : inputLines)
			lines[i++] = l.getLine();

		return lines;
	}

	/**
	 * Returns the lines of this input as parsed by this SCALP text input
	 *
	 * @return SCALP lines
	 */
	public String[] scalpLines() {

		LinkedList<String> inputList = new LinkedList<String>();
		for (String s : getWatershedBlock().asString())
			inputList.add(s);
		for (String s : getSanitaryBlock().asString())
			inputList.add(s);

		SCAInputBlock scaBlock;
		for (int scaNumber : scaBlocks.keySet()) {
			scaBlock = scaBlocks.get(scaNumber);
			for (String s : scaBlock.asString())
				inputList.add(s);
		}

		inputList.add(String.format("%-9s", "FINISH"));

		String[] inputLines = new String[inputList.size()];

		return inputList.toArray(inputLines);
	}

	/**
	 * Returns the watershed input block
	 *
	 * @return watershed input block
	 */
	public WatershedInputBlock getWatershedBlock() {
		return watershedBlock;
	}

	/**
	 * Returns the sanitary input block
	 *
	 * @return sanitary input block
	 */
	public SanitaryInputBlock getSanitaryBlock() {
		return sanitaryBlock;
	}

	/**
	 * Returns an array of the special contributing area numbers for this text input
	 * file
	 *
	 * @return array of SCA numbers
	 */
	public int[] getScaNumbers() {
		return scaBlocks.keySet().stream().mapToInt(i -> i).toArray();
	}

	/**
	 * Returns the special contributing area input block corresponding to a
	 * specified SCA number
	 *
	 * @param scaNumber
	 *            special contributing area number
	 * @return special contributing area input block
	 */
	public SCAInputBlock getScaBlock(int scaNumber) {
		return scaBlocks.get(scaNumber);
	}

}
