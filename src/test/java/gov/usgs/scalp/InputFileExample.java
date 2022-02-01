/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.usgs.scalp;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author hfdoyle
 */
public class InputFileExample {

	public static void main(String[] args) {

		Path filePath = Paths.get("src", "test", "resources", "example.c06");
		String filePathStr = filePath.toAbsolutePath().toString();

		InputFile inputFile = new InputFile(filePathStr);

		for (InputLine line : inputFile.getLines()) {
			System.out.format("%2d: " + line.getLine() + "%n", line.getLineNo());
		}
	}

}
