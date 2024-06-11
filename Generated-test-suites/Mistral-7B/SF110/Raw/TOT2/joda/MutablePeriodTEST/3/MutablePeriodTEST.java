import org.junit.Test;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodFormatter;
import org.joda.time.format.ISOPeriodFormat;

public class MutablePeriodTest {
    @Test
    public void testParseValidString() {
        String input = "P3Y2M1D";
        MutablePeriod mutablePeriod = MutablePeriod.parse(input);
        int expectedYears = 3;
        int expectedMonths = 2;
        int expectedDays = 1;
        verify(mutablePeriod).setPeriod(expectedYears, expectedMonths, expectedDays, 0, 0, 0, 0, 0);
    }
}