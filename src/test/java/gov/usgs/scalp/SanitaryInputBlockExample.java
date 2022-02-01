/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.usgs.scalp;

import java.text.ParseException;

/**
 *
 * @author hfdoyle
 */
public class SanitaryInputBlockExample {

	public static void main(String[] args) {

		String[] lines = { "SANITARY", "SNA#                   1", "SANCOMP=            2.468E-4",
				"JAN-JUNE=           0.92    0.94    1.03    1.04    1.04    1.08",
				"JULY-DEC=           1.10    1.09    1.01    0.93    0.92    0.90",
				"WEEKLY=             0.93    1.00    1.03    1.05    1.03    1.00    0.96",
				"01-06 AM            0.72    0.71    0.70    0.70    0.71    0.88",
				"07-12 AM            1.04    1.12    1.14    1.14    1.15    1.17",
				"01-06 PM            1.16    1.16    1.16    1.14    1.14    1.12",
				"07-12 PM            1.10    1.07    1.03    0.98    0.90    0.86", "SNA#                   2",
				"SNACOMP=            2.818E-3", "JAN-JUNE=           0.92    0.94    1.03    1.04    1.04    1.08",
				"JULY-DEC=           1.10    1.09    1.01    0.93    0.92    0.90",
				"WEEKLY=             0.93    1.00    1.03    1.05    1.03    1.00    0.96",
				"01-06 AM            0.72    0.71    0.70    0.70    0.71    0.88",
				"07-12 AM            1.04    1.12    1.14    1.14    1.15    1.17",
				"01-06 PM            1.16    1.16    1.16    1.14    1.14    1.12",
				"07-12 PM            1.10    1.07    1.03    0.98    0.90    0.86", "END" };

		InputFile inputFile = new InputFile(lines);
		SanitaryInputBlock block;

		try {
			block = new SanitaryInputBlock(inputFile);
		} catch (ParseException e) {
			int errorOffset = e.getErrorOffset();
			System.out.println(String.format("Unable to parse file. Failed on line %d", errorOffset));
			System.out.println(lines[errorOffset - 1]);
			return;
		}

		for (String s : block.asString())
			System.out.println(s);
	}

}
