/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.usgs.scalp;

import java.util.Hashtable;

/**
 * Watershed
 */
public class Watershed {

	private WatershedInputBlock inputBlock;
	private Hashtable<Integer, LandSegmentTS> landSegments;

	/**
	 * Constructor for watershed
	 *
	 * Initializes a watershed from a watershed input block
	 * 
	 * @param watershedInputBlock
	 *            watershed input block
	 * @param inputDSS
	 *            DSS input file
	 */
	public Watershed(WatershedInputBlock watershedInputBlock, DSS inputDSS) {

		if (watershedInputBlock == null || inputDSS == null)
			throw new NullPointerException();

		inputBlock = watershedInputBlock;

		// time span
		DateTime[] simulationTimes = computeSimTimes(inputBlock.getStartDate(), inputBlock.getEndDate());

		// land segments
		String dssPath;
		LandSegmentTS segment;
		landSegments = new Hashtable<>();

		int[] segmentNumbers = inputBlock.getSegmentNumbers();
		for (int segmentNumber : segmentNumbers) {
			dssPath = inputBlock.getDssPath(segmentNumber);
			segment = new LandSegmentTS(inputDSS, dssPath, simulationTimes);
			landSegments.put(segmentNumber, segment);
		}

	}

	private static DateTime[] computeSimTimes(DateTime startDateTime, DateTime endDateTime) {

		if (startDateTime == null || endDateTime == null)
			throw new NullPointerException();

		int nTimes = endDateTime.diffHours(startDateTime);

		DateTime[] simulationTimes = new DateTime[nTimes];

		DateTime dateTime = new DateTime(startDateTime);

		for (int i = 0; i < nTimes; i++) {
			simulationTimes[i] = dateTime;
			dateTime = dateTime.addHours(1);
		}

		return simulationTimes;
	}

	/**
	 * Returns a land segment
	 *
	 * @param segmentNumber
	 *            segment number
	 * @return land segment
	 */
	public LandSegmentTS getLandSegment(int segmentNumber) {
		return landSegments.get(segmentNumber);
	}

	/**
	 * Returns the DSS path associated with a segment number in this input block. If
	 * the segment number is not in this block, returns null.
	 *
	 * @param segmentNumber
	 *            segment number
	 * @return DSS path
	 */
	public String getDssPathParts(int segmentNumber) {
		return landSegments.get(segmentNumber).getPathParts();
	}

	/**
	 * Returns the segment numbers in the simulation
	 * 
	 * @return array of segment numbers
	 */
	public int[] getSegmentNumbers() {
		return inputBlock.getSegmentNumbers();
	}

}
