package gov.usgs.scalp;

/**
 * Constituent storage value
 */
public class StorageValue {

	private double infiltrationVolume;
	private double sanitaryVolume;
	private double stormWaterVolume;

	/**
	 * Constructs a storage value and initializes constituent volumes to zero
	 */
	public StorageValue() {
		infiltrationVolume = 0;
		sanitaryVolume = 0;
		stormWaterVolume = 0;
	}

	/**
	 * Constructs a storage value and initializes constituent volumes
	 *
	 * @param infiltration
	 *            infiltration volume
	 * @param sanitary
	 *            sanitary volume
	 * @param stormWater
	 *            storm water volume
	 */
	public StorageValue(double infiltration, double sanitary, double stormWater) {
		infiltrationVolume = infiltration;
		sanitaryVolume = sanitary;
		stormWaterVolume = stormWater;
	}

	/**
	 * Adds a storage value to this storage value
	 *
	 * @param other
	 *            other storage value
	 * @return sum of storage values
	 */
	public StorageValue add(StorageValue other) {
		double infiltration = this.infiltrationVolume + other.infiltrationVolume;
		double sanitary = this.sanitaryVolume + other.sanitaryVolume;
		double stormWater = this.stormWaterVolume + other.stormWaterVolume;
		return new StorageValue(infiltration, sanitary, stormWater);
	}

	/**
	 * Divide this storage value by a time and returns a flow value
	 *
	 * @param time
	 *            time in seconds
	 * @return flow value
	 */
	public FlowValue divideTime(double time) {
		double infiltration = infiltrationVolume / time;
		double sanitary = sanitaryVolume / time;
		double stormWater = stormWaterVolume / time;
		return new FlowValue(infiltration, sanitary, stormWater);
	}

	/**
	 * Returns the infiltration volume
	 *
	 * @return infiltration volume
	 */
	public double getInfiltration() {
		return infiltrationVolume;
	}

	/**
	 * Returns the sanitary volume
	 *
	 * @return sanitary volume
	 */
	public double getSanitary() {
		return sanitaryVolume;
	}

	/**
	 * Returns the storm water volume
	 *
	 * @return storm water volume
	 */
	public double getStormWater() {
		return stormWaterVolume;
	}

	/**
	 * Returns the total volume
	 *
	 * @return total volume
	 */
	public double getTotal() {
		return infiltrationVolume + sanitaryVolume + stormWaterVolume;
	}

}
