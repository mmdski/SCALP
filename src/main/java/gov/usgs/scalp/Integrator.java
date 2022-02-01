package gov.usgs.scalp;

/**
 * Interface for SLR ODE integrators
 */
public interface Integrator {

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
	public FlowValue integrate(SingleLinearReservoirODE ode, double step, FlowValue inflow, FlowValue outflow);

}
