import org.joda.time.BasePeriod;
import org.joda.time.PeriodType;
import org.junit.Test;

public class BasePeriodTest {
    @Test
    public void testBasePeriod_int() {
        // Test with valid input
        BasePeriod period = new BasePeriod(123456789);
        PeriodType expectedType = PeriodType.standard();
        int[] expectedValues = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        assertSame(expectedType, period.getPeriodType());
        assertArrayEquals(expectedValues, period.iValues);

        // Test with negative input
        try {
            new BasePeriod(-123456789);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}