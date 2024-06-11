import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.Months;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicGet() {
        Chronology chrono = DateTimeUtils.getUTC();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, 1);
        long instant = new DateTime(2022, 7, 15, 10, 30, 0, 0).getMillis();
        int expected = 7; // July
        int actual = field.get(instant);
        assertEquals(expected, actual);
    }

    @Test
    public void testBasicAdd() {
        Chronology chrono = DateTimeUtils.getUTC();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, 1);
        long instant = new DateTime(2022, 7, 15, 10, 30, 0, 0).getMillis();
        int months = 1;
        long expected = new DateTime(2022, 8, 15, 10, 30, 0, 0).getMillis();
        long actual = field.add(instant, months);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddAcrossBoundary() {
        Chronology chrono = DateTimeUtils.getUTC();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, 1);
        long instant = new DateTime(2022, 12, 31, 23, 59, 59, 999).getMillis();
        int months = 1;
        long expected = new DateTime(2023, 1, 31, 23, 59, 59, 999).getMillis();
        long actual = field.add(instant, months);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOutOfRange() {
        Chronology chrono = DateTimeUtils.getUTC();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chrono, 1);
        long instant = new DateTime(1900, 1, 1, 12, 59, 59, 999).getMillis();
        int months = 12 * 12; // too many months
        field.add(instant, months);
        fail("Should throw IllegalArgumentException for excessive months");
    }
}