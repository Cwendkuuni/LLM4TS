import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.format.*;
import org.joda.time.field.*;
import org.junit.*;

public class MutablePeriodTest {

    @Test
    public void testParseString() {
        // valid test cases
        MutablePeriod period1 = MutablePeriod.parse("P1Y2M3W1D1H2M3S1MS");
        MutablePeriod period2 = MutablePeriod.parse("P5D");
        assertEquals("One year and two months", period1.toString());
        assertEquals("Five days", period2.toString());

        // invalid test cases
        try {
            MutablePeriod period3 = MutablePeriod.parse("P-1Y2M3W1D1H2M3S1MS");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            MutablePeriod period4 = MutablePeriod.parse("P100Y");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseStringWithFormat() {
        ISOPeriodFormat isoFormat = ISOPeriodFormat.standard();
        MutablePeriod period1 = MutablePeriod.parse("P1Y2M3W1D1H2M3S1MS", isoFormat);
        assertEquals("One year and two months", period1.toString());
    }

    @Test
    public void testConstructors() {
        // valid test cases
        MutablePeriod period1 = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);
        MutablePeriod period2 = new MutablePeriod(new Period(1, 2, 3, 4, 5, 6, 7, 8));
        MutablePeriod period3 = new MutablePeriod("P1Y2M3W1D1H2M3S1MS");
        MutablePeriod period4 = new MutablePeriod(1, PeriodType.standard());
        MutablePeriod period5 = new MutablePeriod(new BigDecimal("1.5"), PeriodType.standard());
        MutablePeriod period6 = new MutablePeriod(DateTimeConstants.MINUTE_OF_DAY, DateTimeConstants.MILLISECS_PER_MINUTE);

        // invalid test cases
        try {
            MutablePeriod period7 = new MutablePeriod(-1, 2, 3, 4, 5, 6, 7, 8);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            MutablePeriod period8 = new MutablePeriod(1, -1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            MutablePeriod period9 = new MutablePeriod("InvalidString");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // ... edge cases and exception handling tests

    @After
    public void tearDown() {
        // clean up
    }
}