package gov.usgs.scalp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DateTimeTest {

	@Test
	public void testCompare() {
		DateTime t1 = new DateTime(2021, 10, 01, 12, 00);
		DateTime t2 = new DateTime(2021, 10, 01, 12, 00);
		DateTime t3 = new DateTime(2000, 01, 01, 01, 00);
		DateTime t4 = new DateTime(2021, 12, 03, 12, 00);

		assertTrue(t1.compareTo(t1) == 0);
		assertTrue(t1.compareTo(t2) == 0);
		assertTrue(t1.compareTo(t3) > 0);
		assertTrue(t3.compareTo(t1) < 0);
		assertTrue(t4.compareTo(t3) > 0);

		assertThrows(NullPointerException.class, () -> t1.compareTo(null));
	}

	@Test
	public void testEquals() {

		DateTime t1 = new DateTime(2021, 10, 01, 12, 00);
		DateTime t2 = new DateTime(2021, 10, 01, 12, 00);
		DateTime t3 = new DateTime(2021, 12, 03, 12, 00);
		DateTime reference = t1;

		Object obj = new Object();

		assertTrue(t1.equals(t1));
		assertTrue(t1.equals(t2));
		assertTrue(t1.equals(reference));
		assertTrue(t2.equals(t1));
		assertFalse(t1.equals(t3));
		assertFalse(t3.equals(t1));
		assertFalse(t1.equals(null));
		assertFalse(t1.equals(obj));

		assertEquals(t1, t1);
		assertEquals(t1, t2);
		assertEquals(t2, t1);
		assertNotEquals(t3, t1);
		assertNotEquals(obj, t1);
	}

}
