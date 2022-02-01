package gov.usgs.scalp;

public class InputDataDescriptorExample {

	public static void main(String[] args) {

		String descriptor = "3(A25,3I8)";
		InputDataDescriptor[] dataDescriptors;

		try {
			dataDescriptors = InputDataDescriptor.parseDescriptor(descriptor);
		} catch (RuntimeException e) {
			System.out.println("Unable to parse " + descriptor);
			System.out.println(e.getMessage());
			return;
		}

		for (int i = 0; i < dataDescriptors.length; i++) {
			System.out.format(dataDescriptors[i].getType() + "\t%d\n", dataDescriptors[i].getWidth());
		}
	}

}
