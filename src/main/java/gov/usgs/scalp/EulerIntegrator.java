package gov.usgs.scalp;

/**
 * Integrates an SLR ODE using the Euler method
 */
public class EulerIntegrator implements Integrator {

	/**
	 * Integrates an SLR ODE for a single time step
	 *
	 * @param ode
	 *            SLR ODE
	 * @param step
	 *            time step in seconds
	 * @param inflow
	 *            reservoir inflow
	 * @param outflow
	 *            reservoir outflow
	 * @return flow value at next times step
	 */
	public FlowValue integrate(SingleLinearReservoirODE ode, double step, FlowValue inflow, FlowValue outflow) {

		if (ode == null || inflow == null || outflow == null)
			throw new NullPointerException();

		if (step <= 0)
			throw new IllegalArgumentException();

		FlowValue dFV = ode.computeDerivative(inflow, outflow).multiplyTime(step);
		return outflow.add(dFV);
	}

}
