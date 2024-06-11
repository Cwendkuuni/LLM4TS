import org.joda.time.BasicDateTime;
import org.joda.time.DateTime;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testGetMethod() {
        BasicChronology chronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeFieldType.monthOfYear());
        long instant = new DateTime(2022, 1, 1, 0, 0, 0, 0).getMillis();
        int expected = 1;
        int result = field.get(instant);
        assertEquals(expected, result);
    }

    @Test
    public void testAddMethod() {
        BasicChronology chronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeFieldType.monthOfYear());
        long instant = new DateTime(2022, 1, 1, 0, 0, 0, 0).getMillis();
        int monthsToAdd = 3;
        long expected = new DateTime(2022, 4, 1, 0, 0, 0, 0).getMillis();
        long result = field.add(instant, monthsToAdd);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWithNegativeMonths() {
        BasicChronology chronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeFieldType.monthOfYear());
        long instant = new DateTime(2022, 1, 1, 0, 0, 0, 0).getMillis();
        int monthsToAdd = -3;
        field.add(instant, monthsToAdd);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWithLargeMonths() {
        BasicChronology chronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeFieldType.monthOfYear());
        long instant = new DateTime(2022, 1, 1, 0, 0, 0, 0).getMillis();
        int monthsToAdd = 13 * 12 + 1;
        field.add(instant, monthsToAdd);
    }

    @Test
    public void testAddWrapFieldMethod() {
        BasicChronology chronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeFieldType.monthOfYear());
        long instant = new DateTime(2022, 1, 1, 0, 0, 0, 0).getMillis();
        int monthsToAdd = 13;
        long expected = new DateTime(2023, 2, 1, 0, 0, 0, 0).getMillis();
        long result = field.addWrapField(instant, monthsToAdd);
        assertEquals(expected, result);
    }

    @Test
    public void testGetDifferenceAsLongMethod() {
        BasicChronology chronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeFieldType.monthOfYear());
        long minuendInstant = new DateTime(2022, 1, 1, 0, 0, 0, 0).getMillis();
        long subtrahendInstant = new DateTime(2021, 12, 31, 0, 0, 0, 0).getMillis();
        long expected = 1;
        long result = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(expected, result);
    }

    @Test
    public void testSetMethod() {
        BasicChronology chronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeFieldType.monthOfYear());
        long instant = new DateTime(2022, 1, 1, 0, 0, 0, 0).getMillis();
        int month = 4;
        long expected = new DateTime(2022, 4, 1, 0, 0, 0, 0).getMillis();
        long result = field.set(instant, month);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWithInvalidMonth() {
        BasicChronology chronology = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeFieldType.monthOfYear());
        long instant = new DateTime(2022, 1, 1, 0, 0, 0, 0).getMillis();
        int month = 13;
        field.set(instant, month);
    }
}