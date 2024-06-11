package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import org.junit.Test;

import java.time.LocalTime;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructorWithHoursAndMinutes() {
        TimeOfDay timeOfDay = new TimeOfDay(3, 45);
        assertEquals(3, timeOfDay.getHours().intValue());
        assertEquals(45, timeOfDay.getMinutes().intValue());
    }

    @Test
    public void testTimeOfDayConstructorWithStringTime() {
        TimeOfDay timeOfDay = new TimeOfDay("15:30");
        assertEquals(15, timeOfDay.getHours().intValue());
        assertEquals(30, timeOfDay.getMinutes().intValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructorWithInvalidTimeString() {
        new TimeOfDay("25:60");
    }

    @Test
    public void testCompareTo() {
        TimeOfDay timeOfDay1 = new TimeOfDay(3, 45);
        TimeOfDay timeOfDay2 = new TimeOfDay(4, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(3, 45);
        TimeOfDay timeOfDay4 = new TimeOfDay(3, 45);

        assertEquals(1, timeOfDay1.compareTo(timeOfDay2));
        assertEquals(0, timeOfDay1.compareTo(timeOfDay3));
        assertEquals(0, timeOfDay3.compareTo(timeOfDay4));
    }

    @Test
    public void testGetHours() {
        TimeOfDay timeOfDay = new TimeOfDay(3, 45);
        assertEquals(3, timeOfDay.getHours().intValue());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay timeOfDay = new TimeOfDay(3, 45);
        assertEquals(45, timeOfDay.getMinutes().intValue());
    }

    @Test
    public void testToString() {
        TimeOfDay timeOfDay = new TimeOfDay(3, 45);
        assertEquals("03:45", timeOfDay.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay timeOfDay1 = new TimeOfDay(3, 45);
        TimeOfDay timeOfDay2 = new TimeOfDay(3, 45);
        assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay timeOfDay1 = new TimeOfDay(3, 45);
        TimeOfDay timeOfDay2 = new TimeOfDay(3, 45);
        TimeOfDay timeOfDay3 = new TimeOfDay(4, 30);

        assertEquals(timeOfDay1, timeOfDay1);
        assertEquals(timeOfDay1, timeOfDay2);
        assertNotEquals(timeOfDay1, timeOfDay3);
    }

    @Test
    public void testTimeOfDayToLocalTime() {
        TimeOfDay timeOfDay = new TimeOfDay(3, 45);
        LocalTime localTime = LocalTime.of(timeOfDay.getHours().intValue(), timeOfDay.getMinutes().intValue());
        assertEquals(timeOfDay, new TimeOfDay(localTime.toString()));
    }
}