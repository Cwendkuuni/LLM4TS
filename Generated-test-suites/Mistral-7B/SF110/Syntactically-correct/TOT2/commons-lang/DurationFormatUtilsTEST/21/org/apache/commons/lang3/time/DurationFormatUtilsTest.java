import static org.apache.commons.lang3.time.DurationFormatUtils.*;
import static org.junit.Assert.*;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L; // 2 hours, 7 minutes, 12 seconds, 567 milliseconds
        String expected = "2:07:12.567";
        String actual = formatDurationHMS(durationMillis);
        assertEquals(expected, actual);
    }
}