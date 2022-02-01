package gov.usgs.scalp;

/**
 * Integrates an SLR ODE using the Runge-Kutta method
 */
public class RungeKuttaIntegrator implements Integrator {

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

		FlowValue k1 = ode.computeDerivative(inflow, outflow).multiplyTime(step);

		FlowValue fv2 = outflow.add(k1.multiply(0.5));
		FlowValue k2 = ode.computeDerivative(inflow, fv2).multiplyTime(step);

		FlowValue fv3 = outflow.add(k2.multiply(0.5));
		FlowValue k3 = ode.computeDerivative(inflow, fv3).multiplyTime(step);

		FlowValue fv4 = outflow.add(k3);
		FlowValue k4 = ode.computeDerivative(inflow, fv4).multiplyTime(step);

		FlowValue dFV = k1.add(k2.multiply(2)).add(k3.multiply(2)).add(k4).multiply(1 / 6.);

		return outflow.add(dFV);
	}
}
