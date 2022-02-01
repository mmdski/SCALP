package gov.usgs.scalp;

/**
 * Reservoir initialization information
 */
public class ReservoirInfo {

	private double K; // storage/routing constant
	private double maxQ; // maximum flow
	private double splitQ; // split flow
	private double initialQ; // initial flow
	private boolean stopStore;

	/**
	 * Constructs a reservoir routing information instance
	 *
	 * @param routingConstant
	 *            reservoir storage constant
	 * @param maxFlow
	 *            maximum reservoir outflow
	 * @param splitFlow
	 *            reservoir split flow
	 * @param initialFlow
	 *            reservoir initial flow
	 */
	public ReservoirInfo(double routingConstant, double maxFlow, double splitFlow, double initialFlow,
			boolean stopStore) {

		K = routingConstant;
		maxQ = maxFlow;
		splitQ = splitFlow;
		initialQ = initialFlow;
		this.stopStore = stopStore;
	}

	/**
	 * Returns the initial flow of a reservoir
	 *
	 * @return initial flow
	 */
	public double getInitialFlow() {
		return initialQ;
	}

	/**
	 * Returns the maximum outflow of a reservoir
	 *
	 * @return maximum outflow
	 */
	public double getMaxFlow() {
		return maxQ;
	}

	/**
	 * Returns the stop store setting
	 *
	 * @return
	 */
	public boolean getStopStore() {
		return stopStore;
	}

	/**
	 * Returns the storage constant of a reservoir
	 *
	 * @return storage constant
	 */
	public double getStorageConstant() {
		return K;
	}

	/**
	 * Returns the split flow of a reservoir
	 *
	 * @return split flow
	 */
	public double getSplitFlow() {
		return splitQ;
	}

}
