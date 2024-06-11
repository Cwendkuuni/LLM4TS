import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TimeOfDayTest {
    @Test
    public void testValidTimeOfDayConstruction() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals("12:30", time.toString());
    }
}