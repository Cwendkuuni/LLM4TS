package net.sf.lavalamp.device;

import org.junit.Test;
import org.junit.Assert.*;;

public class TimeOfDayTest {
    @Test
    public void testTimeOfDayConstructionWithInts() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals("12:30", time.toString());
    }

    @Test
    public void testTimeOfDayConstructionWithString() {
        TimeOfDay time = new TimeOfDay("14:59");
        assertEquals("14:59", time.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructionWithNegativeHours() {
        new TimeOfDay(-1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructionWithNegativeMinutes() {
        new TimeOfDay(0, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructionWithInvalidFormat() {
        try {
            new TimeOfDay("12:60");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructionWithNullString() {
        try {
            new TimeOfDay(null);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructionWithInvalidHours() {
        try {
            new TimeOfDay("25:00");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructionWithInvalidMinutes() {
        try {
            new TimeOfDay("12:61");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // Add other test cases here
}