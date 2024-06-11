// Expert 1 test cases
package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructorWithInts() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(14, 45);
        TimeOfDay time3 = new TimeOfDay(23, 59);
        TimeOfDay time4 = new TimeOfDay(0, 0);

        assertEquals("12:30", time1.toString());
        assertEquals("14:45", time2.toString());
        assertEquals("23:59", time3.toString());
        assertEquals("0:0", time4.toString());

        // Test comparisons
        assertTrue(time1.compareTo(time4) > 0);
        assertTrue(time1.compareTo(time2) < 0);
        assertTrue(time2.compareTo(time1) > 0);
        assertTrue(time3.compareTo(time4) > 0);
        assertTrue(time3.compareTo(time1) > 0);
        assertTrue(time4.compareTo(time1) < 0);
    }

    // Expert 1 test case for the second constructor
    @Test
    public void testTimeOfDayConstructorWithString() {
        TimeOfDay time1 = new TimeOfDay("12:30");
        TimeOfDay time2 = new TimeOfDay("14:45");
        TimeOfDay time3 = new TimeOfDay("23:59");
        TimeOfDay time4 = new TimeOfDay("00:00");

        assertEquals("12:30", time1.toString());
        assertEquals("14:45", time2.toString());
        assertEquals("23:59", time3.toString());
        assertEquals("0:0", time4.toString());

        // Test comparisons
        assertTrue(time1.compareTo(time4) > 0);
        assertTrue(time1.compareTo(time2) < 0);
        assertTrue(time2.compareTo(time1) > 0);
        assertTrue(time3.compareTo(time4) > 0);
        assertTrue(time3.compareTo(time1) > 0);
        assertTrue(time4.compareTo(time1) < 0);
    }
}