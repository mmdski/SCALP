package gov.usgs.scalp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * SCALP input file
 */
public class InputFile {

	private int currentLineNo;
	private int totalLines;
	private InputLine[] inputLines;
	private final static Logger logger = Logger.getLogger("gov.usgs.scalp.InputFile");

	/**
	 * Constructs an input file from a text file
	 *
	 * @param filePath
	 *            path to SCALP input file
	 */
	public InputFile(String filePath) {

		if (filePath == null)
			throw new NullPointerException();

		List<String> allLines;

		try {
			logger.log(Level.INFO, "Opening file " + filePath);
			allLines = Files.readAllLines(Paths.get(filePath));
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Unable to open file " + filePath);
			throw new RuntimeException("Unable to open file " + filePath);
		}

		String[] lines = new String[allLines.size()];
		lines = allLines.toArray(lines);

		inputLines = strToLine(lines);
		currentLineNo = 1;
		totalLines = inputLines.length;
	}

	/**
	 * Constructs an input file from an array of strings
	 *
	 * @param lines
	 *            array of strings
	 */
	public InputFile(String[] lines) {

		if (lines == null)
			throw new NullPointerException();

		inputLines = strToLine(lines);
		currentLineNo = 1;
		totalLines = inputLines.length;
	}

	// convert an array of strings to an array of lines
	private InputLine[] strToLine(String[] lines) {

		if (lines == null)
			throw new NullPointerException();

		inputLines = new InputLine[lines.length];

		String line;
		String logLine;
		InputLine inputLine;
		int lineNo;

		for (int i = 0; i < lines.length; i++) {

			lineNo = i + 1;
			line = lines[i];

			logLine = String.format("%03d: ", lineNo) + line;

			logger.log(Level.FINE, logLine);

			inputLine = new InputLine(line, lineNo);

			inputLines[i] = inputLine;
		}

		return inputLines;
	}

	/**
	 * Returns an input file line at a specific line number
	 *
	 * @param lineNo
	 *            line number
	 * @return input line
	 */
	public InputLine getLine(int lineNo) {

		if (lineNo < 0 || lineNo > totalLines - 1)
			throw new IllegalArgumentException();

		return inputLines[lineNo];
	}

	/**
	 * Returns all lines in this file as an array
	 *
	 * @return array of input lines
	 */
	public InputLine[] getLines() {
		return inputLines.clone();
	}

	/**
	 * Returns the next line in this file
	 *
	 * @return next line
	 */
	public InputLine nextLine() {
		if (currentLineNo <= totalLines)
			return inputLines[currentLineNo++ - 1];
		else
			return null;
	}

	/**
	 * Returns the next non-empty and non-comment line in this input file
	 *
	 * @return next non-empty line
	 */
	public InputLine nextNonEmptyLine() {
		InputLine inputLine = nextLine();

		if (inputLine == null)
			return null;

		if (inputLine.isComment() || inputLine.isEmpty())
			return nextNonEmptyLine();

		return inputLine;
	}

	/**
	 * Sets the current line number
	 *
	 * @param lineNo
	 *            line number
	 */
	public void setCurrentLineNo(int lineNo) {

		if (lineNo < 0 || lineNo > totalLines - 1)
			throw new IllegalArgumentException();

		currentLineNo = lineNo;
	}

}
