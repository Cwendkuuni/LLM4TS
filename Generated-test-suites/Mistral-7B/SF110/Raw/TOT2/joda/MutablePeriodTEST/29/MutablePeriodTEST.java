import org.junit.Test;
import org.joda.time.MutablePeriod;
import static org.junit.Assert.assertEquals;

public class MutablePeriodTest {
    @Test
    public void testParseValidPeriod() {
        String validPeriodString = "P3Y2M6W1D";
        MutablePeriod parsedPeriod = MutablePeriod.parse(validPeriodString);
        int[] expectedValues = {3, 2, 6, 1, 0, 0, 0, 0};
        assertEquals(expectedValues, parsedPeriod.getValues());
    }

    @Test
    public void testParseInvalidPeriod() {
        String invalidPeriodString = "InvalidPeriod";
        try {
            MutablePeriod parsedPeriod = MutablePeriod.parse(invalidPeriodString);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
}