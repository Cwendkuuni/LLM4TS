package net.sf.lavalamp.device;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testConstructorWithHoursAndMinutes() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(12, time.getHours());
        assertEquals(30, time.getMinutes());
    }

    @Test
    public void testConstructorWithTimeString() {
        TimeOfDay time = new TimeOfDay("14:45");
        assertEquals(14, time.getHours());
        assertEquals(45, time.getMinutes());
    }

    @Test
    public void testCompareToSmallerTime() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(11, 59);
        int comparisonResult = time1.compareTo(time2);
        assert lessThan(comparisonResult, 0);
    }

    @Test
    public void testCompareToEqualTime() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        int comparisonResult = time1.compareTo(time2);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testCompareToLargerTime() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(13, 0);
        int comparisonResult = time1.compareTo(time2);
        assertGreaterThan(comparisonResult, 0);
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(14, 45);
        assertEquals("14:45", time.toString());
    }

    @Test
    public void testHashcode() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(13, 45);
        assertNotEquals(time1.hashCode(), time2.hashCode());
    }

    @Test
    public void testEqualsNull() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertFalse(time.equals(null));
    }

    @Test
    public void testEqualsSameInstance() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertTrue(time.equals(time));
    }

    @Test
    public void testEqualsDifferentInstances() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(13, 45);
        assertFalse(time1.equals(time2));
    }

    @Test
    public void testEqualsSameValues() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        assertTrue(time1.equals(time2));
    }
}