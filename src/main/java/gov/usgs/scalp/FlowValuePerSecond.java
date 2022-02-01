package gov.usgs.scalp;

/**
 * Class to store flow value derivative approximations for reservoir routing
 */
public class FlowValuePerSecond {

	private double infiltration;
	private double sanitary;
	private double stormWater;

	/**
	 * Constructs a flow value derivative with specified constituent flows
	 *
	 * @param infiltration
	 *            infiltration flow derivative
	 * @param sanitary
	 *            sanitary flow derivative
	 * @param stormWater
	 *            storm water flow derivative
	 */
	public FlowValuePerSecond(double infiltration, double sanitary, double stormWater) {

		this.stormWater = stormWater;
		this.infiltration = infiltration;
		this.sanitary = sanitary;
	}

	/**
	 * Multiplies this flow value derivative by a time and returns a flow value
	 *
	 * @param time
	 *            time in seconds
	 * @return flow value
	 */
	public FlowValue multiplyTime(double time) {
		double infiltration = time * this.infiltration;
		double sanitary = time * this.sanitary;
		double stormWater = time * this.stormWater;
		return new FlowValue(infiltration, sanitary, stormWater);
	}

}
