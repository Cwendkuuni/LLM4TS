import org.joda.time.MutablePeriod;
import org.joda.time.PeriodFormatter;
import org.joda.time.format.ISOPeriodFormat;
import org.junit.Test;

public class MutablePeriodTest {

    @Test
    public void testParse() {
        String input = "P3Y2M1DT4H30M"; // three years, two months, one day, four hours, thirty minutes
        MutablePeriod期expect = new MutablePeriod(3, 2, 1, 4, 30, 0, 0, 0);
        MutablePeriod actual = MutablePeriod.parse(input);
        assertMutablePeriodEquals(期expect, actual);
    }

    // utility method for asserting MutablePeriod equality
    private void assertMutablePeriodEquals(MutablePeriod expect, MutablePeriod actual) {
        // test equality of all fields
        for (int i = 0; i < expect.size(); i++) {
            int expectValue = getFieldValue(expect, i);
            int actualValue = getFieldValue(actual, i);
            assertEquals(expectValue, actualValue);
        }
    }

    private int getFieldValue(MutablePeriod period, int fieldIndex) {
        switch (fieldIndex) {
            case PeriodType.YEAR_INDEX:
                return period.getYears();
            case PeriodType.MONTH_INDEX:
                return period.getMonths();
            case PeriodType.WEEK_INDEX:
                return period.getWeeks();
            case PeriodType.DAY_INDEX:
                return period.getDays();
            case PeriodType.HOUR_INDEX:
                return period.getHours();
            case PeriodType.MINUTE_INDEX:
                return period.getMinutes();
            case PeriodType.SECOND_INDEX:
                return period.getSeconds();
            case PeriodType.MILLI_INDEX:
                return period.getMillis();
            default:
                throw new IllegalArgumentException("Invalid field index: " + fieldIndex);
        }
    }
}