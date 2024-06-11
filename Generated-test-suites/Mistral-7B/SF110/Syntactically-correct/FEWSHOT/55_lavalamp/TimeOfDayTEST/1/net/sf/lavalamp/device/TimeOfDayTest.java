package net.sf.lavalamp.device;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructorWithHoursAndMinutes() {
        TimeOfDay timeOfDay = new TimeOfDay(3, 45);
        assertEquals(3, timeOfDay.getHours());
        assertEquals(45, timeOfDay.getMinutes());
    }

    @Test
    public void testTimeOfDayConstructorWithString() {
        TimeOfDay timeOfDay = new TimeOfDay("07:30");
        assertEquals(7, timeOfDay.getHours());
        assertEquals(30, timeOfDay.getMinutes());
    }

    @Test
    public void testCompareToWithSameTimeOfDay() {
        TimeOfDay timeOfDay1 = new TimeOfDay(14, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(14, 30);
        assertEquals(0, timeOfDay1.compareTo(timeOfDay2));
    }

    @Test
    public void testCompareToWithDifferentHours() {
        TimeOfDay timeOfDay1 = new TimeOfDay(14, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(15, 0);
        assertGreaterThan(timeOfDay2.getHours(), timeOfDay1.getHours());
        assertEquals(1, timeOfDay1.compareTo(timeOfDay2));
    }

    @Test
    public void testCompareToWithSameHoursButDifferentMinutes() {
        TimeOfDay timeOfDay1 = new TimeOfDay(14, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(14, 45);
        assertEquals(1, timeOfDay2.compareTo(timeOfDay1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructorWithInvalidTimeString() {
        new TimeOfDay("25:60");
    }

    @Test
    public void testToString() {
        TimeOfDay timeOfDay = new TimeOfDay(15, 30);
        assertEquals("15:30", timeOfDay.toString());
    }

    @Test
    public void testHashCodeAndEquals() {
        TimeOfDay timeOfDay1 = new TimeOfDay(14, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(14, 30);
        assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
        assertTrue(timeOfDay1.equals(timeOfDay2));
    }

    @Test
    public void testHashCodeAndEqualsWithDifferentObjects() {
        TimeOfDay timeOfDay1 = new TimeOfDay(14, 30);
        Object obj = new Object();
        assertNotEquals(timeOfDay1.hashCode(), obj.hashCode());
        assertFalse(timeOfDay1.equals(obj));
    }

    @Test
    public void testHashCodeAndEqualsWithSameObject() {
        TimeOfDay timeOfDay1 = new TimeOfDay(14, 30);
        TimeOfDay timeOfDay2 = timeOfDay1;
        assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
        assertTrue(timeOfDay1.equals(timeOfDay2));
    }

    private void assertGreaterThan(int expected, int actual) {
        assertNotEquals(expected, actual);
        assertTrue(expected > actual);
    }
}