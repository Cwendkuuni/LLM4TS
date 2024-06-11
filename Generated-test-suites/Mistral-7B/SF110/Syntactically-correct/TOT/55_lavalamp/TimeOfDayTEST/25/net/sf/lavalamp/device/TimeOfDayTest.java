import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {
    @Test
    public void testTimeOfDayWithIntArray() {
        TimeOfDay time = new TimeOfDay(1, 30);
        assertEquals("1:30", time.toString());
        assertEquals(1, time.getHours());
        assertEquals(30, time.getMinutes());
    }
}