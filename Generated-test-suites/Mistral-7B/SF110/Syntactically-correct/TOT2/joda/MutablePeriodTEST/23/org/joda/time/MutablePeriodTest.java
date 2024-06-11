import org.joda.time.MutablePeriod;
import org.joda.time.PeriodFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class MutablePeriodTest {
    @Test
    public void testParseEmptyString() {
        MutablePeriod period = MutablePeriod.parse("");
        assertEquals(0, period.getYears());
        assertEquals(0, period.getMonths());
        assertEquals(0, period.getWeeks());
        assertEquals(0, period.getDays());
        assertEquals(0, period.getHours());
        assertEquals(0, period.getMinutes());
        assertEquals(0, period.getSeconds());
        assertEquals(0, period.getMillis());
    }

    @Test
    public void testParseValidPeriodString() {
        String periodString = "P1Y2M3DT4H5M6S";
        MutablePeriod period = MutablePeriod.parse(periodString);
        PeriodFormat parser = PeriodFormat.parser("P");
        Period expectedPeriod = parser.parsePeriod(periodString);
        assertEquals(expectedPeriod.getYears(), period.getYears());
        assertEquals(expectedPeriod.getMonths(), period.getMonths());
        assertEquals(expectedPeriod.getWeeks(), period.getWeeks());
        assertEquals(expectedPeriod.getDays(), period.getDays());
        assertEquals(expectedPeriod.getHours(), period.getHours());
        assertEquals(expectedPeriod.getMinutes(), period.getMinutes());
        assertEquals(expectedPeriod.getSeconds(), period.getSeconds());
        assertEquals(expectedPeriod.getMillis(), period.getMillis());
    }
}