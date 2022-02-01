package gov.usgs.scalp;

import java.lang.Math;

/**
 * Single linear reservoir
 */
public class SingleLinearReservoir {

	private double K;
	private double splitQ;
	private double maxQ;

	private FlowValue outflow;

	private StorageValue storage;

	private SingleLinearReservoirODE ode;
	private Integrator integrator;
	private boolean stopStore = false;

	/**
	 * Constructs a single linear reservoir
	 *
	 * @param storageCoefficient
	 *            reservoir storage coefficient in seconds
	 */
	public SingleLinearReservoir(double storageCoefficient) {
		this(storageCoefficient, new FlowValue(0));
	}

	/**
	 * Constructs a single linear reservoir from reservoir info
	 *
	 * @param reservoirInfo
	 *            reservoir initialization information
	 */
	public SingleLinearReservoir(ReservoirInfo reservoirInfo) {

		if (reservoirInfo == null)
			throw new NullPointerException();

		// check validity of values from reservoir info
		double initialFlow = reservoirInfo.getInitialFlow();

		if (initialFlow < 0)
			throw new IllegalArgumentException();

		double K = reservoirInfo.getStorageConstant();

		if (K < 0)
			throw new IllegalArgumentException();

		double splitQ = reservoirInfo.getSplitFlow();

		if (splitQ < 0)
			throw new IllegalArgumentException();

		double maxQ = reservoirInfo.getMaxFlow();

		if (maxQ < 0)
			throw new IllegalArgumentException();

		// initialize values for this instance
		storage = new StorageValue();
		ode = new SingleLinearReservoirODE(K);
		outflow = new FlowValue(initialFlow / 3, initialFlow / 3, initialFlow / 3); // split initial flows evenly
		this.K = K;
		this.splitQ = splitQ;
		this.maxQ = maxQ;
		this.stopStore = reservoirInfo.getStopStore();

		if (K == 0)
			integrator = new PassThroughIntegrator();
		else
			integrator = new EulerIntegrator();
	}

	/**
	 * Constructs a single linear reservoir with initial inflow and outflow
	 * specified
	 *
	 * @param storageCoefficient
	 *            reservoir storage coefficient in seconds
	 * @param initialOutflow
	 *            initial outflow of the reservoir in cubic feet per second
	 */
	public SingleLinearReservoir(double storageCoefficient, FlowValue initialOutflow) {
		this(storageCoefficient, initialOutflow, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
	}

	/**
	 * Constructs a single linear reservoir and specifies split and maximum
	 * discharge values
	 *
	 * @param storageCoefficient
	 *            storage coefficient of this reservoir, in seconds
	 * @param initialOutflow
	 *            initial outflow of this reservoir
	 * @param splitDischarge
	 *            split discharge value
	 * @param maxDischarge
	 *            maximum discharge
	 */
	public SingleLinearReservoir(double storageCoefficient, FlowValue initialOutflow, double splitDischarge,
			double maxDischarge) {

		if (K < 0 || splitDischarge < 0 || maxDischarge < 0)
			throw new IllegalArgumentException();

		if (initialOutflow == null)
			throw new NullPointerException();

		K = storageCoefficient;
		outflow = initialOutflow;
		storage = new StorageValue();
		maxQ = maxDischarge;
		splitQ = splitDischarge;
		ode = new SingleLinearReservoirODE(storageCoefficient);

		if (K == 0)
			integrator = new PassThroughIntegrator();
		else
			integrator = new EulerIntegrator();
	}

	private FlowValue reservoirOutflow(FlowValue previousQ, FlowValue currentI, double dT) {

		assert previousQ != null && currentI != null;

		FlowValue outflow;
		outflow = integrator.integrate(ode, dT, currentI, previousQ);

		return outflow;
	}

	/**
	 * Reservoir routing step method
	 * <p>
	 * Returns an array of 2 flow values. The 0-index is interceptor flow, the
	 * 1-index is reservoir overflow.
	 *
	 * @param currentInflow
	 *            inflow to this reservoir at the current time step
	 * @param deltaT
	 *            time step, in seconds
	 * @return reservoir outflow and overflow
	 */
	public FlowValue[] step(FlowValue currentInflow, double deltaT) {

		if (currentInflow == null)
			throw new NullPointerException();

		if (deltaT <= 0)
			throw new IllegalArgumentException();

		// compute routed flow
		FlowValue routedFlow = reservoirOutflow(outflow, currentInflow, deltaT);

		// storage and storage flow
		double excessFlow = routedFlow.getTotal() - maxQ;

		double storageFlow = Math.max(-storage.getTotal() / K, excessFlow);

		// storage frac will go nan if total routed flow is 0
		double storageFrac = storageFlow / routedFlow.getTotal();
		if (Double.isNaN(storageFrac))
			storageFrac = 0;

		FlowValue storageFlowValue = routedFlow.multiply(storageFrac);

		if (!stopStore) {
			storage = storage.add(storageFlowValue.multiplyTime(K));
		}

		// outflow for current time step
		FlowValue currentOutflow = routedFlow.subtract(storageFlowValue);

		double interceptorFlow = Math.min(currentOutflow.getTotal(), splitQ);

		// interceptor frac will go nan if current outflow is 0
		double interceptorFrac = interceptorFlow / currentOutflow.getTotal();
		if (Double.isNaN(interceptorFrac))
			interceptorFrac = 0;

		FlowValue interceptorFlowValue = currentOutflow.multiply(interceptorFrac);
		FlowValue overflowValue = currentOutflow.subtract(interceptorFlowValue);

		// store previous flow values for next call to step()
		outflow = currentOutflow;

		FlowValue[] flow = new FlowValue[2];
		flow[0] = interceptorFlowValue;
		flow[1] = overflowValue;

		return flow;
	}

}
