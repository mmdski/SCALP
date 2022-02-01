package gov.usgs.scalp;

/**
 * Single linear reservoir ordinary differential equation
 */
public class SingleLinearReservoirODE {

	private double k;

	/**
	 * Constructs a SingleLinearReservoirODE
	 *
	 * @param storageCoefficient
	 *            reservoir storage coefficient
	 */
	public SingleLinearReservoirODE(double storageCoefficient) {
		k = storageCoefficient;
	}

	/**
	 * Computes the derivative of this ODE
	 *
	 * @param inflow
	 *            reservoir inflow
	 * @param outflow
	 *            reservoir outflow
	 * @return flow value derivative
	 */
	public FlowValuePerSecond computeDerivative(FlowValue inflow, FlowValue outflow) {
		return inflow.subtract(outflow).divideTime(k);
	}

}
