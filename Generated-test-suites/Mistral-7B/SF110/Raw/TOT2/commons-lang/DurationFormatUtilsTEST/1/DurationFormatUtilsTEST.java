import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DurationFormatUtilsTest {

    @Test
    public void testFormatDurationHMS() {
        long durationMillis = 1234567L; // 2 hours, 7 minutes, 17 seconds, 670 milliseconds
        String expectedResult = "2:07:17.670";
        String result = DurationFormatUtils.formatDurationHMS(durationMillis);
        assertEquals(expectedResult, result);
    }
}