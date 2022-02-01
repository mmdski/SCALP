/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.usgs.scalp;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;

/**
 *
 * @author hfdoyle
 */
public class SCALPTextInputExample {

	public static void main(String[] args) {
		Path filePath = Paths.get("src", "test", "resources", "example.c06");
		String filePathStr = filePath.toAbsolutePath().toString();
		String dssFilePath = Paths.get("src", "test", "resources", "test.dss").toAbsolutePath().toString();
		DSS dssFile = new DSS(dssFilePath);
		SCALPTextInput scalpInput;

		try {
			scalpInput = new SCALPTextInput(filePathStr, dssFile);
		} catch (ParseException e) {
			int errorOffset = e.getErrorOffset();
			System.out.println(String.format("Unable to parse file. Failed on line %d", errorOffset));
			dssFile.close();
			return;
		}

		for (String s : scalpInput.scalpLines())
			System.out.println(s);
		dssFile.close();
	}

}
