
package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TimeOfDayTest {
    @Test
    public void testTimeOfDayConstructionWithValidInput() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(10, time.getHours());
        assertEquals(30, time.getMinutes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructionWithNullInput() {
        new TimeOfDay(null, 30);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructionWithNegativeHours() {
        new TimeOfDay(-1, 30);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructionWithNegativeMinutes() {
        new TimeOfDay(10, -1);
    }

    @Test
    public void testTimeOfDayConstructionWithTimeString() {
        TimeOfDay time = new TimeOfDay("10:30");
        assertEquals(10, time.getHours());
        assertEquals(30, time.getMinutes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructionWithNullTimeString() {
        new TimeOfDay(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructionWithEmptyTimeString() {
        new TimeOfDay("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructionWithInvalidTimeFormat() {
        new TimeOfDay("13:60");
    }

    @Test
    public void testCompareToDifferentTimes() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(11, 30);
        int result = time1.compareTo(time2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompareToEqualTimes() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        int result = time1.compareTo(time2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareToMinMaxTimes() {
        TimeOfDay timeMin = new TimeOfDay(0, 0);
        TimeOfDay timeMax = new TimeOfDay(23, 59);
        int result = timeMin.compareTo(timeMax);
        assertTrue(result < 0);
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(15, 45);
        assertEquals("15:45", time.toString());
    }

    @Test
    public void testEquality() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        TimeOfDay time3 = new TimeOfDay(11, 30);
        assertNotNull(time1);
        assertNotNull(time2);
        assertNotNull(time3);
        assertEquals(time1, time1);
        assertEquals(time1, time1);
        assertEquals(time1, time2); // Same values
        assertNotEquals(time1, time3); // Different values
    }

    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(10, 30);
        TimeOfDay time2 = new TimeOfDay(10, 30);
        TimeOfDay time3 = new TimeOfDay(11, 30);
        int hashCode1 = time1.hashCode();
        int hashCode2 = time2.hashCode();
        int hashCode3 = time3.hashCode();
        assertEquals(hashCode1, hashCode1);
        assertEquals(hashCode1, hashCode2); // Same values
        assertNotEquals(hashCode1, hashCode3); // Different values
    }
}