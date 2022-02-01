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
public class SCAInputBlockExample {

	public static void main(String[] args) {

		String[] lines = { "SCA", "ANALYSIS", "SCA#,AREA=             1   1.122", "LANDS",
				"SEG#,AREA=             2   0.021   0.031   0.031", "SEG#,AREA=             3   0.046   0.060   0.060",
				"END", "SANITARY", "SNA#,PE#               1   19531", "END", "INITIAL FLOWS",
				"OUT1(*)=            14.4    14.4    14.4", "ROUTING", "STOPSTORE=            NO      NO      NO",
				"RK(*)=              2580    2580    2580", "QMAX(*)             1436    1436      49",
				"SPLIT(*)           27.75   27.75   27.75", "OUTPUT", "        A=NORTH B=N1 F=",
				"        A=NORTH B=N1 F=", "EXECUTE" };

		InputFile inputFile = new InputFile(lines);
		SCAInputBlock block;

		try {
			block = new SCAInputBlock(inputFile);
		} catch (ParseException e) {
			int errorOffset = e.getErrorOffset();
			System.out.println(String.format("Unable to parse file. Failed on line %d", errorOffset));
			System.out.println(lines[errorOffset - 1]);
			return;
		}

		for (String s : block.asString()) {
			System.out.println(s);
		}
	}

}
