package gov.usgs.scalp;

/**
 * Land segment area
 */
public class LandSegmentAreaInfo {

	// areas are stored in square feet
	private double impArea;
	private double ovlArea;
	private double subArea;
	private final double SQMI_TO_SQFT = 5280.0 * 5280.0;

	/**
	 * Constructs a new LandSegmentArea
	 *
	 * @param impArea
	 *            impervious area in square miles
	 * @param ovlArea
	 *            overland area in square miles
	 * @param subArea
	 *            subsurface area in square miles
	 */
	public LandSegmentAreaInfo(double impArea, double ovlArea, double subArea) {
		this.impArea = SQMI_TO_SQFT * impArea;
		this.ovlArea = SQMI_TO_SQFT * ovlArea;
		this.subArea = SQMI_TO_SQFT * subArea;
	}

	/**
	 * Constructs a new LandSegmentArea from an array
	 * <p>
	 * <code>area[0]</code> is impervious area, <code>area[1]</code> is overland
	 * area, and <code>area[2]</code> is subsurface area.
	 *
	 * @param area
	 *            array of areas in square miles
	 */
	public LandSegmentAreaInfo(double[] area) {
		impArea = SQMI_TO_SQFT * area[0];
		ovlArea = SQMI_TO_SQFT * area[1];
		subArea = SQMI_TO_SQFT * area[2];
	}

	/**
	 * Returns the impervious area
	 *
	 * @return impervious area in square feet
	 */
	public double getImpervious() {
		return impArea;
	}

	/**
	 * Returns the overland area
	 *
	 * @return overland area in square feet
	 */
	public double getOverland() {
		return ovlArea;
	}

	/**
	 * Returns the subsurface area
	 *
	 * @return subsurface area in square feet
	 */
	public double getSubsurface() {
		return subArea;
	}
}
