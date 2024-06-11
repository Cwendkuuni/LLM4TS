import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {
    @Test
    public void testTimeOfDayConstructorWithHoursAndMinutes() {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(10, time.getHours());
        assertEquals(30, time.getMinutes());
    }
}