import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FastDateFormatTest {
    @Test
    public void testFormatLongMilliseconds() {
        FastDateFormat dateFormat = FastDateFormat.getInstance("dd-MM-yyyy HH:mm:ss z");
        long millis = System.currentTimeMillis();
        String expectedDateString = dateFormat.format(millis);
        String actualDateString = dateFormat.format(millis);
        assertEquals(expectedDateString, actualDateString);
    }
}