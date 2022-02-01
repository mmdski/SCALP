package gov.usgs.scalp;

/**
 * Class to track constituent flows in reservoir routing
 */
public class FlowValue {

	private double infiltration;
	private double sanitary;
	private double stormWater;

	/**
	 * Constructs a flow value with a specified storm water flow
	 * <p>
	 * All other flow constituents are set to 0.
	 *
	 * @param stormWater
	 *            storm water flow
	 */
	public FlowValue(double stormWater) {
		this.stormWater = stormWater;
		sanitary = 0;
		infiltration = 0;
	}

	/**
	 * Constructs a flow value with specified constituent flows
	 *
	 * @param infiltration
	 *            infiltration flow
	 * @param sanitary
	 *            sanitary flow
	 * @param stormWater
	 *            storm water flow
	 */
	public FlowValue(double infiltration, double sanitary, double stormWater) {

		this.stormWater = stormWater;
		this.infiltration = infiltration;
		this.sanitary = sanitary;
	}

	/**
	 * Copy constructor
	 *
	 * @param flowValue
	 *            flow value to copy
	 */
	public FlowValue(FlowValue flowValue) {

		if (flowValue == null)
			throw new NullPointerException();

		stormWater = flowValue.stormWater;
		infiltration = flowValue.infiltration;
		sanitary = flowValue.sanitary;
	}

	/**
	 * Returns the sum of this and another flow value
	 *
	 * @param other
	 *            flow value
	 * @return sum of flow values
	 */
	public FlowValue add(FlowValue other) {

		if (other == null)
			throw new NullPointerException();

		double infiltration = this.infiltration + other.infiltration;
		double sanitary = this.sanitary + other.sanitary;
		double stormWater = this.stormWater + other.stormWater;
		return new FlowValue(infiltration, sanitary, stormWater);
	}

	/**
	 * Divides this flow value by a dimensionless constant
	 *
	 * @param c
	 *            dimensionless constant
	 * @return flow value
	 */
	public FlowValue divide(double c) {
		double infiltration = this.infiltration / c;
		double sanitary = this.sanitary / c;
		double stormWater = this.stormWater / c;

		return new FlowValue(infiltration, sanitary, stormWater);
	}

	/**
	 * Divides this flow value by time and returns a flow value per second
	 *
	 * @param time
	 *            time in seconds
	 * @return flow value per second
	 */
	public FlowValuePerSecond divideTime(double time) {
		double infiltration = this.infiltration / time;
		double sanitary = this.sanitary / time;
		double stormWater = this.stormWater / time;
		return new FlowValuePerSecond(infiltration, sanitary, stormWater);
	}

	@Override
	public boolean equals(Object other) {

		if (this == other)
			return true;

		if (other == null)
			return false;

		if (getClass() != other.getClass())
			return false;

		FlowValue flowValue = (FlowValue) other;

		boolean equals = ((infiltration == flowValue.infiltration) && (sanitary == flowValue.sanitary)
				&& (stormWater == flowValue.stormWater));

		return equals;
	}

	/**
	 * Returns the infiltration flow of this instance.
	 *
	 * @return Infiltration flow
	 */
	public double getInfiltration() {
		return infiltration;
	}

	/**
	 * Returns the sanitary flow of this instance.
	 *
	 * @return Sanitary flow
	 */
	public double getSanitary() {
		return sanitary;
	}

	/**
	 * Returns the storm water flow of this instance.
	 *
	 * @return Storm water flow
	 */
	public double getStormWater() {
		return stormWater;
	}

	/**
	 * Returns the total flow of this instance.
	 *
	 * @return Total flow
	 */
	public double getTotal() {
		return stormWater + infiltration + sanitary;
	}

	/**
	 * Multiplies this flow value by a dimensionless constant
	 *
	 * @param c
	 *            dimensionless constant
	 * @return flow value
	 */
	public FlowValue multiply(double c) {
		double infiltration = c * this.infiltration;
		double sanitary = c * this.sanitary;
		double stormWater = c * this.stormWater;
		return new FlowValue(infiltration, sanitary, stormWater);
	}

	/**
	 * Multiplies this flow value by a time and returns a storage value
	 *
	 * @param time
	 *            time in seconds
	 * @return storage value
	 */
	public StorageValue multiplyTime(double time) {
		double infiltration = time * this.infiltration;
		double sanitary = time * this.sanitary;
		double stormWater = time * this.stormWater;
		return new StorageValue(infiltration, sanitary, stormWater);
	}

	/**
	 * Returns the difference of this and another flow values
	 *
	 * @param other
	 *            flow value
	 * @return difference of flow values
	 */
	public FlowValue subtract(FlowValue other) {
		double infiltration = this.infiltration - other.infiltration;
		double sanitary = this.sanitary - other.sanitary;
		double stormWater = this.stormWater - other.stormWater;
		return new FlowValue(infiltration, sanitary, stormWater);
	}

}
