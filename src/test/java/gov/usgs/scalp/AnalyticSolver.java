package gov.usgs.scalp;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * Solves a three linear reservoir in series problem analytically.
 * <p>
 * The three reservoirs must have non-zero, unique storage coefficients (k).
 * Non-unique and zero coefficients require a different solution method than the
 * method implemented by this class.
 */
public class AnalyticSolver {

	private double[] eigenValues;
	private double[] constants;
	private RealMatrix eigenMatrix;
	private RealVector particularSolution;

	/**
	 * Constructs an AnalyticSolver
	 * <p>
	 * The k values for the reservoirs must be unique and non-zero.
	 *
	 * @param k
	 *            array of storage coefficients
	 * @param initialFlows
	 *            array of initial inflows
	 * @param initialInflow
	 *            constant system inflow
	 */
	public AnalyticSolver(double[] k, double[] initialFlows, double initialInflow) {

		double k1 = k[0];
		double k2 = k[1];
		double k3 = k[2];

		double absDiff12 = Math.abs(k1 - k2);
		double absDiff23 = Math.abs(k2 - k3);
		double absDiff13 = Math.abs(k1 - k3);

		// throw exception if any of the storage constants are too close together
		if ((absDiff12 < 1) || (absDiff23 < 1) || (absDiff13 < 1))
			throw new IllegalArgumentException();

		// throw exception if any of the storage constants are less than or equal to
		// zero
		if (k1 <= 0 || k2 <= 0 || k3 <= 0)
			throw new IllegalArgumentException();

		if (initialInflow < 0)
			throw new IllegalArgumentException();

		double[][] matrixData = { { -1 / k1, 0, 0 }, { 1 / k2, -1 / k2, 0 }, { 0, 1 / k3, -1 / k3 } };
		RealMatrix A = MatrixUtils.createRealMatrix(matrixData);

		RealVector f = new ArrayRealVector(new double[] { 1 / k1 * initialInflow, 0, 0 });
		RealVector zero = new ArrayRealVector(3); // zero vector

		// find the particular solution
		DecompositionSolver particularSolver = new LUDecomposition(A).getSolver();
		particularSolution = particularSolver.solve(zero.subtract(f));

		EigenDecomposition eigenSolver = new EigenDecomposition(A);
		eigenMatrix = eigenSolver.getV();
		eigenValues = eigenSolver.getRealEigenvalues();

		RealVector initialCondition = new ArrayRealVector(initialFlows);

		// solve for constants, assuming initial condition is t = 0
		DecompositionSolver constantSolver = new LUDecomposition(eigenMatrix).getSolver();
		constants = constantSolver.solve(initialCondition.subtract(particularSolution)).toArray();
	}

	private RealVector timeVector(double time) {

		double[] result = new double[3];

		for (int i = 0; i < 3; i++) {
			result[i] = constants[i] * Math.exp(eigenValues[i] * time);
		}

		return new ArrayRealVector(result);
	}

	/**
	 * Computes the reservoir outflows for a given time
	 *
	 * @param time
	 *            time to compute reservoir outflows
	 * @return reservoir outflows
	 */
	public double[] compute(double time) {

		RealVector timeVector = timeVector(time);
		RealVector solution = eigenMatrix.operate(timeVector).add(particularSolution);
		return solution.toArray();
	}

	/**
	 * Computes the reservoir outflows for a given time
	 *
	 * @param k
	 *            storage coefficients
	 * @param initialFlows
	 *            initial reservoir flows
	 * @param initialInflow
	 *            initial system inflow
	 * @param time
	 *            the amount of time after t=0 to compute reservoir flows
	 * @return
	 */
	public static double[] compute(double[] k, double[] initialFlows, double initialInflow, double time) {

		AnalyticSolver solver = new AnalyticSolver(k, initialFlows, initialInflow);
		return solver.compute(time);

	}
}
