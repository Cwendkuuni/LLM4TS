import org.joda.time.MutablePeriod;
import org.joda.time.PeriodFormatter;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;

public class MutablePeriodTest {
    @Test
    public void testParseString() {
        String input = "P3Y2M1DT4H30M";
        MutablePeriod mutablePeriod = MutablePeriod.parse(input);
        int expectedYears = 3;
        int expectedMonths = 2;
        int expectedDays = 1;
        int expectedHours = 4;
        int expectedMinutes = 30;
        assert mutablePeriod.getYears() == expectedYears;
        assert mutablePeriod.getMonths() == expectedMonths;
        assert mutablePeriod.getDays() == expectedDays;
        assert mutablePeriod.getHours() == expectedHours;
        assert mutablePeriod.getMinutes() == expectedMinutes;
    }
}