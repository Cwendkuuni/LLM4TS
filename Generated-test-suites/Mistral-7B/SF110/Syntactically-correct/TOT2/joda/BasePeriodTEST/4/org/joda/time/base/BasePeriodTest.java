import org.junit.Test;
import org.joda.time.BasePeriod;
import org.joda.time.PeriodType;
import static org.junit.Assert.*;

public class BasePeriodTest {

    @Test
    public void testBasePeriodConstruction() {
        int year = 2;
        int month = 3;
        int dayOfMonth = 4;
        int hourOfDay = 5;
        int minute = 6;
        int second = 7;
        int millisecond = 8;
        PeriodType periodType = PeriodType.standard();

        BasePeriod period = new BasePeriod(year, month, dayOfMonth, hourOfDay, minute, second, millisecond, periodType);
        assertEquals(PeriodType.standard(), period.getPeriodType());
        assertEquals(2, period.getValue(DurationFieldType.years()));
        assertEquals(3, period.getValue(DurationFieldType.months()));
        assertEquals(4, period.getValue(DurationFieldType.days()));
        assertEquals(5, period.getValue(DurationFieldType.hours()));
        assertEquals(6, period.getValue(DurationFieldType.minutes()));
        assertEquals(7, period.getValue(DurationFieldType.seconds()));
        assertEquals(8, period.getValue(DurationFieldType.millis()));
    }
}