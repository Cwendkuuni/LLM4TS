package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructorWithInts() {
        TimeOfDay timeOfDay1 = new TimeOfDay(3, 15);
        TimeOfDay timeOfDay2 = new TimeOfDay(5, 30);

        assertEquals(timeOfDay1.toString(), "3:15");
        assertEquals(timeOfDay2.toString(), "5:30");

        TimeOfDay timeOfDay3 = new TimeOfDay(24, 0);
        assertEquals(timeOfDay3.toString(), "0:00"); // 24 hours wraps around to 0:00

        TimeOfDay timeOfDay4 = new TimeOfDay(-1, 15);
        assertNull(timeOfDay4); // negative hours is not valid
    }

    @Test
    public void testTimeOfDayConstructorWithString() {
        TimeOfDay timeOfDay1 = new TimeOfDay("03:15");
        TimeOfDay timeOfDay2 = new TimeOfDay("05:30");
        TimeOfDay timeOfDay3 = new TimeOfDay("23:59");
        TimeOfDay timeOfDay4 = new TimeOfDay("25:00"); // invalid time

        assertEquals(timeOfDay1.toString(), "3:15");
        assertEquals(timeOfDay2.toString(), "5:30");
        assertEquals(timeOfDay3.toString(), "23:59");
        assertNull(timeOfDay4);
    }

    @Test
    public void testCompareTo() {
        TimeOfDay timeOfDay1 = new TimeOfDay(3, 15);
        TimeOfDay timeOfDay2 = new TimeOfDay(5, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(3, 30);
        TimeOfDay timeOfDay4 = new TimeOfDay(5, 15);
        TimeOfDay timeOfDay5 = new TimeOfDay(3, 15);

        assertEquals(timeOfDay1.compareTo(timeOfDay2), 1);
        assertEquals(timeOfDay2.compareTo(timeOfDay1), -1);
        assertEquals(timeOfDay1.compareTo(timeOfDay3), -1);
        assertEquals(timeOfDay3.compareTo(timeOfDay1), 1);
        assertEquals(timeOfDay1.compareTo(timeOfDay4), -1);
        assertEquals(timeOfDay4.compareTo(timeOfDay1), 1);
        assertEquals(timeOfDay1.compareTo(timeOfDay5), 0);
        assertEquals(timeOfDay5.compareTo(timeOfDay1), 0);
    }

    @Test
    public void testGetters() {
        TimeOfDay timeOfDay = new TimeOfDay(12, 30);

        assertEquals(timeOfDay.getHours(), 12);
        assertEquals(timeOfDay.getMinutes(), 30);
    }

    @Test
    public void testToString() {
        TimeOfDay timeOfDay = new TimeOfDay(12, 30);

        assertEquals(timeOfDay.toString(), "12:30");
    }

    @Test
    public void testHashCodeEquals() {
        TimeOfDay timeOfDay1 = new TimeOfDay(3, 15);
        TimeOfDay timeOfDay2 = new TimeOfDay(3, 15);
        TimeOfDay timeOfDay3 = new TimeOfDay(5, 30);

        assertEquals(timeOfDay1.hashCode(), timeOfDay1.hashCode()); // same object
        assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode()); // same value
        assertNotEquals(timeOfDay1.hashCode(), timeOfDay3.hashCode()); // different value

        assertTrue(timeOfDay1.equals(timeOfDay1)); // reflexive
        assertTrue(timeOfDay1.equals(timeOfDay2)); // symmetric and transitive
        assertFalse(timeOfDay1.equals(timeOfDay3)); // not symmetric and transitive
    }
}