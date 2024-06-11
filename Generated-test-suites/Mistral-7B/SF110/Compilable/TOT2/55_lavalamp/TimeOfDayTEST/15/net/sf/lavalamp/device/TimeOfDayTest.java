package net.sf.lavalamp.device;
import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {
    @Test
    public void testTimeOfDayWithIntArguments() {
        TimeOfDay time = new TimeOfDay(12, 30);
        Integer expectedHours = 12;
        Integer expectedMinutes = 30;
        assertEquals(expectedHours, time.getHours());
        assertEquals(expectedMinutes, time.getMinutes());
    }

    @Test
    public void testTimeOfDayWithStringArgument() {
        TimeOfDay time = new TimeOfDay("14:45");
        Integer expectedHours = 14;
        Integer expectedMinutes = 45;
        assertEquals(expectedHours, time.getHours());
        assertEquals(expectedMinutes, time.getMinutes());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testExceptionForInvalidIntArguments() {
        new TimeOfDay(24, 30);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testExceptionForInvalidStringArguments() {
        new TimeOfDay("12:60");
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        TimeOfDay time3 = new TimeOfDay(13, 30);
        assertEquals(time1, time2);
        assertNotEquals(time1, time3);
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(14, 45);
        assertEquals("14:45", time.toString());
    }
}