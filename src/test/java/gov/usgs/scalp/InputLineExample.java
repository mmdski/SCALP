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
public class InputLineExample {

	public static void main(String[] args) {

		/* parse and print a starting date line */
		String line = "STARTING DATE       2016      10      01";
		String descriptor = "(16X,3I8)";
		InputLine inputLine = new InputLine(line);
		Object[] data;

		System.out.println("Parsing line: " + inputLine.getLine());
		System.out.println("Using descriptor " + descriptor);

		try {
			data = inputLine.parse(descriptor);
		} catch (ParseException e) {
			System.out.println("Parse failed");
			return;
		}

		System.out.format("%d %d %d", (int) data[0], (int) data[1], (int) data[2]);

		/* parse and print a sanitary line */
		line = "JAN-JUNE=           0.92    0.94    1.03    1.04    1.04    1.08";
		descriptor = "(16X,6F8)";
		inputLine = new InputLine(line);

		System.out.println("Parsing line: " + inputLine.getLine());
		System.out.println("Using descriptor " + descriptor);

		try {
			data = inputLine.parse(descriptor);
		} catch (ParseException e) {
			System.out.println("Parse failed");
			return;
		}

		for (int i = 0; i < 6; i++)
			System.out.format("%.2f\n", (double) data[i]);
	}
}
