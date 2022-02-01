/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.usgs.scalp;

import java.text.ParseException;

public class WatershedInputBlockExample {

	public static void main(String[] args) {

		String[] lines = { "WATERSHED", "+++ This is a test file", "", "TIME SPAN",
				"STARTING DATE       2016      10      01", "ENDING   DATE       2017      09      30", "",
				"OUTPUT LEVEL", "           0", "", "DATA", "       2 A=BONEYARD B=GAGE 2", "END" };
		InputFile inputFile = new InputFile(lines);

		WatershedInputBlock block;

		try {
			block = new WatershedInputBlock(inputFile);
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
