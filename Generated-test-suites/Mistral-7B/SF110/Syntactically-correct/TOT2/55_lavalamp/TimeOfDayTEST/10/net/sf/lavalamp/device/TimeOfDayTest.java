import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TimeOfDayTest {
    @Test
    public void testTimeOfDayConstructionWithHoursAndMinutes() {
        TimeOfDay timeOfDay = new TimeOfDay(12, 30);
        assertNotNull(timeOfDay);
        assertEquals("12:30", timeOfDay.toString());
    }

    @Test
    public void testTimeOfDayConstructionWithTimeString() {
        TimeOfDay timeOfDay = new TimeOfDay("14:25");
        assertNotNull(timeOfDay);
        assertEquals("14:25", timeOfDay.toString());
    }

    @Test
    public void testGetHours() {
        TimeOfDay timeOfDay = new TimeOfDay(15, 45);
        assertEquals(Integer.valueOf(15), timeOfDay.getHours());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay timeOfDay = new TimeOfDay(15, 45);
        assertEquals(Integer.valueOf(45), timeOfDay.getMinutes());
    }

    // Add test cases for compareTo(), toString(), hashCode(), and equals() methods here
}