package gov.usgs.scalp;

import hec.lang.DSSPathString;

/**
 * DSS paths for SCALP input
 */
public class InputDSSPathStrings {

	private DSSPathString improPath;
	private DSSPathString olfroPath;
	private DSSPathString subroPath;

	private DSSPartsString partsString;

	/**
	 * Constructs a new input DSS parts string
	 *
	 * @param dssPartsString
	 *            DSS parts string
	 */
	public InputDSSPathStrings(String dssPartsString) {

		if (dssPartsString == null)
			throw new NullPointerException();

		partsString = new DSSPartsString(dssPartsString);

		String partA = partsString.getPartA();
		String partB = partsString.getPartB();
		String partC = partsString.getPartC();
		String partD = partsString.getPartD();
		String partE = partsString.getPartE();

		improPath = new DSSPathString(partA, partB, partC, partD, partE, "IMPRO");
		olfroPath = new DSSPathString(partA, partB, partC, partD, partE, "OLFRO");
		subroPath = new DSSPathString(partA, partB, partC, partD, partE, "SUBRO");
	}

	/**
	 * Returns the impervious runoff DSS path
	 *
	 * @return impervious runoff DSS path
	 */
	public String getImproPath() {
		return improPath.getPathname();
	}

	/**
	 * Returns the overland runoff DSS path
	 *
	 * @return overland runoff DSS path
	 */
	public String getOlfroPath() {
		return olfroPath.getPathname();
	}

	/**
	 * Returns the subsurface runoff DSS path
	 *
	 * @return subsurface runoff DSS path
	 */
	public String getSubroPath() {
		return subroPath.getPathname();
	}

}
