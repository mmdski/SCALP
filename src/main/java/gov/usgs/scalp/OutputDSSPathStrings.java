package gov.usgs.scalp;

import hec.lang.DSSPathString;

/**
 * DSS paths for SCALP output
 */
public class OutputDSSPathStrings {

	private String partA;
	private String partB;
	private String partC;
	private String partD;
	private String partE;
	private String partFPrefix;

	private DSSPartsString partsString;

	/**
	 * Constructs a new output DSS parts string
	 * <p>
	 * Part F of <code>dssPartsString</code> will be used as a prefix to part F of
	 * the DSS paths. If part F of <code>dssPartsString</code> is either blank or
	 * <code>null</code>, <code>partFPrefix</code> will be used as a prefix to part
	 * F of the path string.
	 *
	 * @param dssPartsString
	 *            DSS parts string
	 * @param partFPrefix
	 *            prefix to part F
	 */
	public OutputDSSPathStrings(String dssPartsString, String partFPrefix) {

		if (dssPartsString == null || partFPrefix == null)
			throw new NullPointerException();

		partsString = new DSSPartsString(dssPartsString);
		partA = partsString.getPartA();
		partB = partsString.getPartB();
		partC = partsString.getPartC();
		partD = partsString.getPartD();
		partE = partsString.getPartE();

		String partF = partsString.getPartF();

		if (partF.isEmpty() || partF == null)
			this.partFPrefix = partFPrefix;
		else
			this.partFPrefix = partF;
	}

	private String getPathname(String partF) {

		assert partF != null;

		DSSPathString pathString = new DSSPathString(partA, partB, partC, partD, partE, partF);
		return pathString.getPathname();
	}

	/**
	 * Returns the infiltration path
	 *
	 * @return infiltration path
	 */
	public String getInfiltration() {
		String partF = partFPrefix + "-INFIL";
		return getPathname(partF);
	}

	/**
	 * Returns the inflow path
	 *
	 * @return inflow path
	 */
	public String getInflow() {
		String partF = partFPrefix + "-INFLO";
		return getPathname(partF);
	}

	/**
	 * Returns the total path
	 *
	 * @return total path
	 */
	public String getTotal() {
		String partF = partFPrefix + "-TOTAL";
		return getPathname(partF);
	}

	/**
	 * Returns the sanitary path
	 *
	 * @return sanitary path
	 */
	public String getSanitary() {
		String partF = partFPrefix + "-SANIT";
		return getPathname(partF);
	}
}
