package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructionWithHoursAndMinutes() {
        TimeOfDay timeOfDay = new TimeOfDay(1, 30);
        assertEquals(1, timeOfDay.getHours());
        assertEquals(30, timeOfDay.getMinutes());
    }

    @Test
    public void testTimeOfDayConstructionWithString() {
        TimeOfDay timeOfDay = new TimeOfDay("13:30");
        assertEquals(13, timeOfDay.getHours());
        assertEquals(30, timeOfDay.getMinutes());
    }

    @Test(expected = NumberFormatException.class)
    public void testTimeOfDayConstructionWithInvalidString() {
        new TimeOfDay("12:60");
    }

    @Test
    public void testTimeOfDayComparison() {
        TimeOfDay timeOfDay1 = new TimeOfDay(1, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(2, 0);
        TimeOfDay timeOfDay3 = new TimeOfDay(1, 30);

        int comparisonResult = timeOfDay1.compareTo(timeOfDay2);
        assertEquals(1, comparisonResult);

        comparisonResult = timeOfDay1.compareTo(timeOfDay3);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testToString() {
        TimeOfDay timeOfDay = new TimeOfDay(13, 30);
        assertEquals("13:30", timeOfDay.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay timeOfDay1 = new TimeOfDay(1, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(1, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(2, 0);

        int hashCode1 = timeOfDay1.hashCode();
        int hashCode2 = timeOfDay2.hashCode();
        int hashCode3 = timeOfDay3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotSame(hashCode1, hashCode3);
    }

    @Test
    public void testEquals() {
        TimeOfDay timeOfDay1 = new TimeOfDay(1, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(1, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(2, 0);

        assertTrue(timeOfDay1.equals(timeOfDay2));
        assertFalse(timeOfDay1.equals(timeOfDay3));
    }

    @Test
    public void testNullObject() {
        TimeOfDay timeOfDay = new TimeOfDay(1, 30);
        assertFalse(timeOfDay.equals(null));
    }

    @Test
    public void testObjectNotInstanceOfTimeOfDay() {
        TimeOfDay timeOfDay = new TimeOfDay(1, 30);
        assertFalse(timeOfDay.equals(new Object()));
    }
}