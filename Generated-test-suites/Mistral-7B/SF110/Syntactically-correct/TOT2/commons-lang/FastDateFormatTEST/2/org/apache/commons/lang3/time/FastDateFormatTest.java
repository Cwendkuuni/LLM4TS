import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FastDateFormatTest {
    @Test
    public void testFormat_LongMilliseconds() {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance();
        long millis = 1662331200000L; // Mon Feb 21 15:10:00 UTC 2023
        String expectedResult = "Mon Feb 21 15:10:00 UTC 2023";

        String actualResult = fastDateFormat.format(millis);
        assertEquals(expectedResult, actualResult);
    }
}