package gov.usgs.scalp;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SCALPExample {

	public static void main(String[] args) {

		Path inputFilePath = Paths.get("src", "test", "resources", "example", "example.c06");
		String filePathStr = inputFilePath.toAbsolutePath().toString();

		Path inputDSSFilePath = Paths.get("src", "test", "resources", "example", "example.dss");
		String inDSSPathStr = inputDSSFilePath.toAbsolutePath().toString();

		Path outputDSSFilePath = Paths.get("src", "test", "resources", "example", "output.dss");
		String outDSSPathStr = outputDSSFilePath.toAbsolutePath().toString();

		Path userLogFilePath = Paths.get("src", "test", "resources", "example", "userlog.txt");
		String userLogPathStr = userLogFilePath.toAbsolutePath().toString();

		String[] scalpArgs = new String[] { filePathStr, inDSSPathStr, outDSSPathStr, userLogPathStr };

		SCALP.main(scalpArgs);
	}

}
