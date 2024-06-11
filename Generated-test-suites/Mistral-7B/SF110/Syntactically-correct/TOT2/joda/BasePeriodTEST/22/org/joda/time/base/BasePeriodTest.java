import org.joda.time.BasePeriod;
import org.joda.time.PeriodType;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasePeriodTest {

    @Test
    public void testConstructorIntArguments() {
        int year = 2;
        int month = 3;
        int week = 4;
        int day = 5;
        int hour = 6;
        int minute = 7;
        int second = 8;
        int millisecond = 9;

        BasePeriod period = new BasePeriod(year, month, week, day, hour, minute, second, millisecond, PeriodType.standard());

        PeriodType expectedType = PeriodType.standard();
        assertEquals(expectedType, period.getPeriodType());

        int[] expectedValues = {year, month, week, day, hour, minute, second, millisecond};
        int[] actualValues = period.getValues();

        assertArrayEquals(expectedValues, actualValues);
    }
}