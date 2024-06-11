import static org.junit.Assert.*;

import net.sf.lavalamp.device.TimeOfDay;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructionWithIntArguments() {
        TimeOfDay time = new TimeOfDay(13, 30);
        assertEquals(new TimeOfDay(13, 30), time);
    }

    @Test
    public void testTimeOfDayConstructionWithStringArgument() {
        TimeOfDay time = new TimeOfDay("13:30");
        assertEquals(new TimeOfDay(13, 30), time);
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
    public void testTimeOfDayConstructionWithInvalidHours() {
        new TimeOfDay(24, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructionWithInvalidMinutes() {
        new TimeOfDay(0, 60);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructionWithInvalidStringFormat() {
        new TimeOfDay("13:61");
    }

    @Test(expected = NullPointerException.class)
    public void testTimeOfDayConstructionWithNullStringArgument() {
        new TimeOfDay(null);
    }

    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(13, 30);
        assertEquals(13, time.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(13, 30);
        assertEquals(30, time.getMinutes());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(13, 30);
        TimeOfDay time2 = new TimeOfDay(13, 45);
        assertEquals(-15, time1.compareTo(time2));
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(13, 30);
        assertEquals("13:30", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(13, 30);
        TimeOfDay time2 = new TimeOfDay(13, 45);
        assertNotEquals(time1.hashCode(), time2.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(13, 30);
        TimeOfDay time2 = new TimeOfDay(13, 30);
        TimeOfDay time3 = new TimeOfDay(13, 31);
        assertEquals(time1, time1);
        assertEquals(time1, time2);
        assertNotEquals(time1, time3);
    }
}