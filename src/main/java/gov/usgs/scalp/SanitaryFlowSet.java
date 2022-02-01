package gov.usgs.scalp;

/**
 * Sanitary flow set
 */
public class SanitaryFlowSet {

	private final double populationFactor;
	private final double[] monthlyFlowFactors;
	private final double[] weekdayFlowFactors;
	private final double[] hourlyFlowFactors;

	/**
	 * Constructs a sanitary flow set from SNA info
	 * 
	 * @param snaInfo
	 *            sanitary flow set initialization information
	 */
	public SanitaryFlowSet(SNAInfo snaInfo) {
		populationFactor = snaInfo.getPopulationFactor();
		monthlyFlowFactors = snaInfo.getMonthlyFlowFactors();
		weekdayFlowFactors = snaInfo.getWeekdayFlowFactors();
		hourlyFlowFactors = snaInfo.getHourlyFlowFactors();
	}

	/**
	 * Returns the sanitary flow per person
	 *
	 * @param dateTime
	 *            Time of flow observation
	 * @return sanitary flow per person
	 */
	public double sanitaryFlowPerPerson(DateTime dateTime) {

		int month = dateTime.getMonth();

		int dayOfWeek = dateTime.getDayOfWeek();

		int hour = dateTime.getHourOfDay();

		double monthFlowFactor = monthlyFlowFactors[month - 1];
		double weekdayFlowFactor = weekdayFlowFactors[dayOfWeek - 1];
		double hourlyFlowFactor = hourlyFlowFactors[hour - 1];

		double sanitaryFlowPer = hourlyFlowFactor * weekdayFlowFactor * monthFlowFactor * populationFactor;

		return sanitaryFlowPer;
	}

}
