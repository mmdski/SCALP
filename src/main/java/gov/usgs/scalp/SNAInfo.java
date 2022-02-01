package gov.usgs.scalp;

/**
 * Sanitary flow set initialization information
 */
public class SNAInfo {

	private final double populationFactor; // sanitary flow population factor
	private final double[] monthlyFlowFactors;
	private final double[] weekdayFlowFactors;
	private final double[] hourlyFlowFactors;

	/**
	 * Construct a sanitary flow set information instance
	 * 
	 * @param populationFactor
	 *            sanitary flow population factor
	 * @param monthlyFlowFactors
	 *            monthly flow factors
	 * @param weekdayFlowFactors
	 *            weekday flow factors
	 * @param hourlyFlowFactors
	 *            hourly flow factors
	 */
	public SNAInfo(double populationFactor, double[] monthlyFlowFactors, double[] weekdayFlowFactors,
			double[] hourlyFlowFactors) {
		this.populationFactor = populationFactor;
		this.monthlyFlowFactors = monthlyFlowFactors;
		this.weekdayFlowFactors = weekdayFlowFactors;
		this.hourlyFlowFactors = hourlyFlowFactors;
	}

	/**
	 * Returns the sanitary flow population factor
	 * 
	 * @return population factor
	 */
	public double getPopulationFactor() {
		return populationFactor;
	}

	/**
	 * Returns the monthly flow factors
	 * 
	 * @return monthly flow factors
	 */
	public double[] getMonthlyFlowFactors() {
		return monthlyFlowFactors;
	}

	/**
	 * Returns the weekday flow factors
	 * 
	 * @return weekday flow factors
	 */
	public double[] getWeekdayFlowFactors() {
		return weekdayFlowFactors;
	}

	/**
	 * Return the hourly flow factors
	 * 
	 * @return hourly flow factors
	 */
	public double[] getHourlyFlowFactors() {
		return hourlyFlowFactors;
	}

}
