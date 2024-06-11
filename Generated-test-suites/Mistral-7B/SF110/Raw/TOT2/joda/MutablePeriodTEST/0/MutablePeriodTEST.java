import org.junit.Test;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodFormatter;
import org.joda.time.format.ISOPeriodFormat;

public class MutablePeriodTest {
    @Test
    public void testParse() {
        String periodString1 = "P1W2D";
        MutablePeriod parsedPeriod1 = MutablePeriod.parse(periodString1);
        int[] expectedValues1 = {1, 0, 1, 0, 0, 2, 0, 0};
        assertArrayEquals(expectedValues1, parsedPeriod1.getValues());

        String periodString2 = "P3Y6M4D";
        MutablePeriod parsedPeriod2 = MutablePeriod.parse(periodString2);
        int[] expectedValues2 = {3, 0, 0, 6, 0, 4, 0, 0};
        assertArrayEquals(expectedValues2, parsedPeriod2.getValues());

        String invalidPeriodString = "P-1W2D";
        try {
            MutablePeriod parsedPeriod3 = MutablePeriod.parse(invalidPeriodString);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}