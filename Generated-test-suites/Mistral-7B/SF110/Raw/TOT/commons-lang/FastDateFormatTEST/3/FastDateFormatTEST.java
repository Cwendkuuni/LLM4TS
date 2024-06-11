import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import static org.junit.Assert.*;;

public class FastDateFormatTest {
    @Test
    public void testFormatDate() {
        // Create a FastDateFormat instance with default settings
        FastDateFormat dateFormat = FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        
        // Create a test date and calendar
        Calendar calendar = new GregorianCalendar(2022, 11, 30); // December 30, 2022
        Date testDate = calendar.getTime();

        // Test formatting
        String expectedOutput = "Dec 30, 2022"; // Medium date format
        String actualOutput = dateFormat.format(testDate);
        assertEquals(expectedOutput, actualOutput);
        
        // Test formatting with different formats
        FastDateFormat longDateFormat = FastDateFormat.getDateInstance(FastDateFormat.LONG);
        String longExpectedOutput = "December 30, 2022";
        assertEquals(longExpectedOutput, longDateFormat.format(testDate));

        FastDateFormat shortDateFormat = FastDateFormat.getDateInstance(FastDateFormat.SHORT);
        String shortExpectedOutput = "12/30/2022";
        assertEquals(shortExpectedOutput, shortDateFormat.format(testDate));
    }
}