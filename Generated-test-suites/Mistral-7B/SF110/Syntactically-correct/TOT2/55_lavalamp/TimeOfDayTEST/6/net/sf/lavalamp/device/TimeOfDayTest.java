import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructorWithHoursAndMinutes() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertNotNull(time);
        assertEquals(12, time.getHours());
        assertEquals(30, time.getMinutes());
    }

    @Test
    public void testTimeOfDayConstructorWithTimeString() {
        TimeOfDay time = new TimeOfDay("14:45");
        assertNotNull(time);
        assertEquals(14, time.getHours());
        assertEquals(45, time.getMinutes());
    }

    // Add similar tests for getHours(), getMinutes(), toString(), hashCode(), and equals()
}