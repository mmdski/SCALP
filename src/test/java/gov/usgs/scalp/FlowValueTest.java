package gov.usgs.scalp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class FlowValueTest {

	@Test
	public void testEquals() {

		FlowValue fv1 = new FlowValue(10);
		FlowValue fv2 = fv1;
		FlowValue fv3 = new FlowValue(10, 10, 10);

		assertEquals(fv1, fv1);
		assertEquals(fv1, fv2);
		assertNotEquals(fv3, fv1);
		assertNotEquals(fv1, null);
	}

}
