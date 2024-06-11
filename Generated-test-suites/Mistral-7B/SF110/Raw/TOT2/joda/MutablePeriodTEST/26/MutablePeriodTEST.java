import org.joda.time.MutablePeriod;
import org.joda.time.PeriodFormatter;
import org.joda.time.format.ISOPeriodFormat;
import org.junit.Test;

public class MutablePeriodTest {

    @Test
    public void testParse() {
        // Test valid input
        String validInput = "P3Y6M10D12H2M30S";
        MutablePeriod mutablePeriod = MutablePeriod.parse(validInput);
        assertNotNull(mutablePeriod);

        // Test empty string
        String emptyInput = "";
        try {
            MutablePeriod emptyPeriod = MutablePeriod.parse(emptyInput);
            fail("Expected IllegalArgumentException but got MutablePeriod instance");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Test null input
        String nullInput = null;
        try {
            MutablePeriod nullPeriod = MutablePeriod.parse(nullInput);
            fail("Expected IllegalArgumentException but got MutablePeriod instance");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}