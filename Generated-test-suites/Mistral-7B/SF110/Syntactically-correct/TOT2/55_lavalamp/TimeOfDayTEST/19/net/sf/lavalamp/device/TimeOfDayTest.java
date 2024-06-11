import static org.junit.Assert.*;
import net.sf.lavalamp.device.TimeOfDay;

public class TimeOfDayTest {
    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(12, time.getHours());
    }
}