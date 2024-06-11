import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.Instant;
import org.joda.time.JodaTimeTestCase;
import org.joda.time.LocalDateTime;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.Field;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.MonthOfYearField;

public class BasicMonthOfYearDateTimeFieldTest extends JodaTimeTestCase {

    public void testBasicMonthOfYearDateTimeField() {
        Chronology gregorianChrono = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(
                gregorianChrono, DateTimeConstants.JANUARY);

        // isLenient()
        assertFalse(field.isLenient());

        // get()
        long instant = BaseDateTime.UTC.getMillis();
        int month = field.get(instant);
        assertEquals(12, month);

        // add()
        long newInstant = field.add(instant, 1);
        int newMonth = field.get(newInstant);
        assertEquals(1, newMonth);

        // add() with negative value
        long newInstant2 = field.add(instant, -1);
        int newMonth2 = field.get(newInstant2);
        assertEquals(12, newMonth2);

        // add wrap field
        long wrapMinuendInstant = instant - (2 * 30 * 24 * 60 * 60 * 1000);
        long wrapSubtrahendInstant = instant - (1 * 30 * 24 * 60 * 60 * 1000);
        long wrapDifference = field.addWrapField(wrapMinuendInstant, 1);
        int wrapNewMonth = field.get(wrapDifference);
        assertEquals(2, wrapNewMonth);

        // getDifferenceAsLong()
        long diff1 = field.getDifferenceAsLong(instant, instant + 1);
        assertEquals(1, diff1);
        long diff2 = field.getDifferenceAsLong(instant, instant - 1);
        assertEquals(-1, diff2);

        // set()
        long setInstant = field.set(instant, 1);
        int setMonth = field.get(setInstant);
        assertEquals(1, setMonth);

        // getRangeDurationField()
        DurationField rangeField = field.getRangeDurationField();
        assertEquals(DateTimeFieldType.years(), rangeField.getFieldType());

        // isLeap()
        long leapInstant = BaseDateTime.UTC.withYear(2000).withMonthOfYear(2).withDayOfMonth(29).getMillis();
        assertTrue(field.isLeap(leapInstant));

        // getLeapAmount()
        int leapAmount = field.getLeapAmount(leapInstant);
        assertEquals(1, leapAmount);

        // getLeapDurationField()
        DurationField leapField = field.getLeapDurationField();
        assertEquals(DateTimeFieldType.days(), leapField.getFieldType());

        // getMinimumValue()
        int minValue = field.getMinimumValue();
        assertEquals(DateTimeConstants.JANUARY, minValue);

        // getMaximumValue()
        int maxValue = field.getMaximumValue();
        assertEquals(DateTimeConstants.DECEMBER, maxValue);

        // roundFloor()
        long roundFloorInstant = field.roundFloor(instant);
        int roundFloorMonth = field.get(roundFloorInstant);
        assertEquals(12, roundFloorMonth);

        // remainder()
        long remainderInstant = field.remainder(instant);
        int remainderMonth = field.get(remainderInstant);
        assertEquals(12, remainderMonth);

        // serialization
        field = (BasicMonthOfYearDateTimeField) readObject(new ByteArrayInputStream(
                toByteArray(new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(field))));
        assertEquals(field.getFieldType(), DateTimeFieldType.monthOfYear());
    }

    public void testAddMonthsToDateTime() {
        Chronology gregorianChrono = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField monthField = new BasicMonthOfYearDateTimeField(
                gregorianChrono, DateTimeConstants.JANUARY);

        LocalDateTime date = new LocalDateTime(2010, 1, 31, 12, 30, 0, 0);
        Instant instant = date.toInstant();

        long newInstant = monthField.add(instant, 1);
        LocalDateTime newDate = new LocalDateTime(newInstant);
        LocalDateTime expectedDate = new LocalDateTime(2010, 2, 28, 12, 30, 0, 0);
        assertEquals(expectedDate, newDate);

        newDate = newDate.plusMonths(2);
        expectedDate = new LocalDateTime(2010, 4, 30, 12, 30, 0, 0);
        assertEquals(expectedDate, newDate);

        newDate = newDate.plusMonths(-4);
        expectedDate = new LocalDateTime(2010, 1, 31, 12, 30, 0, 0);
        assertEquals(expectedDate, newDate);
    }

    public void testAddMonthsToDateTimeWithLeapYear() {
        Chronology gregorianLeapChrono = new BasicGregorianLeapChronology();
        BasicMonthOfYearDateTimeField monthField = new BasicMonthOfYearDateTimeField(
                gregorianLeapChrono, DateTimeConstants.JANUARY);

        LocalDateTime date = new LocalDateTime(2012, 2, 29, 12, 30, 0, 0);
        Instant instant = date.toInstant();

        long newInstant = monthField.add(instant, 1);
        LocalDateTime newDate = new LocalDateTime(newInstant);
        LocalDateTime expectedDate = new LocalDateTime(2012, 3, 31, 12, 30, 0, 0);
        assertEquals(expectedDate, newDate);

        newDate = newDate.plusMonths(11);
        expectedDate = new LocalDateTime(2013, 3, 1, 12, 30, 0, 0);
        assertEquals(expectedDate, newDate);

        newDate = newDate.plusMonths(-12);
        expectedDate = new LocalDateTime(2012, 2, 29, 12, 30, 0, 0);
        assertEquals(expectedDate, newDate);
    }

    public void testAddMonthsToDateTimeWithNonContiguousPartial() {
        Chronology gregorianChrono = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField monthField = new BasicMonthOfYearDateTimeField(
                gregorianChrono, DateTimeConstants.JANUARY);

        LocalDateTime date = new LocalDateTime(2010, 1, 31, 12, 30, 0, 12, 30);
        Instant instant = date.toInstant();

        long newInstant = monthField.add(instant, 2);
        LocalDateTime newDate = new LocalDateTime(newInstant);
        LocalDateTime expectedDate = new LocalDateTime(2010, 3, 31, 12, 30, 0, 12, 0);
        assertEquals(expectedDate, newDate);

        newDate = newDate.plusMonths(2);
        expectedDate = new LocalDateTime(2010, 5, 31, 12, 30, 0, 12, 0);
        assertEquals(expectedDate, newDate);

        newDate = newDate.plusMonths(-7);
        expectedDate = new LocalDateTime(2010, 3, 31, 12, 30, 0, 12, 30);
        assertEquals(expectedDate, newDate);
    }

    public void testAddMonthsToDateTimeWithContiguousPartial() {
        Chronology gregorianChrono = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField monthField = new BasicMonthOfYearDateTimeField(
                gregorianChrono, DateTimeConstants.JANUARY);

        LocalDateTime date = new LocalDateTime(2010, 1, 31, 12, 30, 0, 12, 30);
        Instant instant = date.toInstant();

        long newInstant = monthField.add(instant, 2);
        LocalDateTime newDate = new LocalDateTime(newInstant);
        LocalDateTime expectedDate = new LocalDateTime(2010, 3, 31, 12, 30, 0, 12, 0);
        assertEquals(expectedDate, newDate);

        newDate = newDate.plusMonths(2);
        expectedDate = new LocalDateTime(2010, 5, 31, 12, 30, 0, 12, 0);
        assertEquals(expectedDate, newDate);

        newDate = newDate.plusMonths(-7);
        expectedDate = new LocalDateTime(2010, 3, 31, 12, 30, 0, 12, 30);
        assertEquals(expectedDate, newDate);

        LocalDateTime date2 = new LocalDateTime(2010, 3, 31, 12, 30, 0, 12, 30);
        Instant instant2 = date2.toInstant();
        long newInstant2 = monthField.add(instant2, 2);
        LocalDateTime newDate2 = new LocalDateTime(newInstant2);
        LocalDateTime expectedDate2 = new LocalDateTime(2010, 5, 31, 12, 30, 0, 12, 0);
        assertEquals(expectedDate2, newDate2);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(Suite.class);
    }

    public static class Suite extends org.junit.runner.RunWith {
        public Suite() {
            super(BasicMonthOfYearDateTimeFieldTest.class);
        }
    }
}

class BasicGregorianLeapChronology extends BasicGregorianChronology {
    @Override
    public boolean isLeapYear(int year) {
        return (year % 4) == 0 && ((year % 100) != 0 || (year % 400) == 0);
    }
}