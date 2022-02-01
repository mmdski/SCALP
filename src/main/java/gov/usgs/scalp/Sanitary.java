package gov.usgs.scalp;

import java.util.Hashtable;

/**
 * Sanitary
 */
public class Sanitary {

	private final Hashtable<Integer, SanitaryFlowSet> sanitaryFlowSets;

	/**
	 * Constructor for Sanitary
	 * 
	 * Initializes a sanitary set from a sanitary input block
	 * 
	 * @param sanitaryInputBlock
	 *            sanitary input block
	 */
	public Sanitary(SanitaryInputBlock sanitaryInputBlock) {

		if (sanitaryInputBlock == null)
			throw new NullPointerException();

		SNAInfo snaInfo;
		SanitaryFlowSet sanitaryFlowSet;
		sanitaryFlowSets = new Hashtable<>();

		int[] setNumbers = sanitaryInputBlock.getInfoSetNumbers();
		for (int setNumber : setNumbers) {
			snaInfo = sanitaryInputBlock.getSNAInfo(setNumber);
			sanitaryFlowSet = new SanitaryFlowSet(snaInfo);
			sanitaryFlowSets.put(setNumber, sanitaryFlowSet);
		}
	}

	/**
	 * Returns a sanitary flow set
	 * 
	 * @param setNumber
	 *            set number
	 * @return sanitary flow set
	 */
	public SanitaryFlowSet getFlowSet(int setNumber) {
		return sanitaryFlowSets.get(setNumber);
	}

}
