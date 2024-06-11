package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

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
        TimeOfDay timeOfDay = new TimeOfDay("09:15");
        assertEquals(9, timeOfDay.getHours());
        assertEquals(15, timeOfDay.getMinutes());
    }

    @Test(expected = NumberFormatException.class)
    public void testTimeOfDayConstructorWithInvalidString() {
        new TimeOfDay("123:456");
    }

    @Test
    public void testCompareTo() {
        TimeOfDay timeOfDay1 = new TimeOfDay(13, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(14, 15);
        TimeOfDay timeOfDay3 = new TimeOfDay(13, 30);

        assertEquals(1, timeOfDay1.compareTo(timeOfDay2));
        assertEquals(0, timeOfDay1.compareTo(timeOfDay3));
    }

    @Test
    public void testToString() {
        TimeOfDay timeOfDay = new TimeOfDay(15, 30);
        assertEquals("15:30", timeOfDay.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay timeOfDay1 = new TimeOfDay(15, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(15, 30);
        assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay timeOfDay1 = new TimeOfDay(15, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(15, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(16, 30);

        assertEquals(timeOfDay1, timeOfDay1);
        assertEquals(timeOfDay1, timeOfDay2);
        assertNotEquals(timeOfDay1, timeOfDay3);
    }
}