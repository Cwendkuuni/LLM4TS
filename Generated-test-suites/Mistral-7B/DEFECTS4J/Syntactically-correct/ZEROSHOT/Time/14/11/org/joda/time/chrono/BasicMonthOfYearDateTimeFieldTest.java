import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.chrono.BasicChronology;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        BasicChronology chronology = new BasicChronology("Gregorian", DateTimeZone.forID("UTC"));
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        // Test isLenient
        assertFalse(testField.isLenient());

        // Test get
        long instant = DateTimeConstants.MILLIS_IN_DAY * 30;
        int expected = 2;
        int actual = testField.get(instant);
        assertEquals(expected, actual);

        // Test add
        long newInstant = testField.add(instant, 1);
        assertEquals(DateTimeConstants.MILLIS_PER_MONTH + DateTimeConstants.MILLIS_IN_DAY * 30, newInstant);
        int newExpected = 3;
        int newActual = testField.get(newInstant);
        assertEquals(newExpected, newActual);

        // Test addWrapField
        long wrapAroundInstant = DateTimeConstants.MILLIS_PER_YEAR - DateTimeConstants.MILLIS_IN_DAY;
        long wrappedInstant = testField.addWrapField(wrapAroundInstant, -12);
        assertEquals(DateTimeConstants.MILLIS_PER_YEAR - DateTimeConstants.MILLIS_IN_DAY * 11, wrappedInstant);
        int wrappedActual = testField.get(wrappedInstant);
        assertEquals(11, wrappedActual);

        // Test getDifferenceAsLong
        long diff1 = testField.getDifferenceAsLong(instant, wrapAroundInstant);
        assertEquals(-11, diff1);
        long diff2 = testField.getDifferenceAsLong(wrapAroundInstant, instant);
        assertEquals(11, diff2);

        // Test set
        long newInstantSet = testField.set(instant, 4);
        assertEquals(DateTimeConstants.MILLIS_IN_DAY * 30 + DateTimeConstants.MILLIS_PER_MONTH * 3 + 1, newInstantSet);
        int newExpectedSet = 4;
        int newActualSet = testField.get(newInstantSet);
        assertEquals(newExpectedSet, newActualSet);

        // Test getRangeDurationField
        DurationField rangeField = testField.getRangeDurationField();
        assertEquals(DurationFieldType.YEARS, rangeField.getFieldType());
        long range = rangeField.getIntegralNumber(1);
        assertEquals(12, (int) range);

        // Test isLeap
        long leapInstant = DateTimeConstants.MILLIS_PER_YEAR + DateTimeConstants.MILLIS_IN_DAY * 60;
        assertTrue(testField.isLeap(leapInstant));

        // Test getLeapAmount
        int leapAmount = testField.getLeapAmount(leapInstant);
        assertEquals(1, leapAmount);

        // Test getLeapDurationField
        DurationField leapField = testField.getLeapDurationField();
        assertEquals(DurationFieldType.DAYS, leapField.getFieldType());
        long leap = leapField.getIntegralNumber(1);
        assertEquals(1, leap);

        // Test getMinimumValue
        int min = testField.getMinimumValue();
        assertEquals(DateTimeConstants.JANUARY, min);

        // Test getMaximumValue
        int max = testField.getMaximumValue();
        assertEquals(DateTimeConstants.DECEMBER, max);

        // Test roundFloor
        long roundFloorInstant = DateTimeConstants.MILLIS_PER_YEAR + DateTimeConstants.MILLIS_IN_DAY * 365;
        long roundFloorResult = testField.roundFloor(roundFloorInstant);
        assertEquals(DateTimeConstants.MILLIS_PER_YEAR, roundFloorResult);

        // Test remainder
        long remainderInstant = DateTimeConstants.MILLIS_PER_YEAR + DateTimeConstants.MILLIS_IN_DAY * 366;
        long remainderResult = testField.remainder(remainderInstant);
        assertEquals(DateTimeConstants.MILLIS_PER_DAY, remainderResult);

        // Test readResolve
        Object o = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY).readResolve();
        assertTrue(o instanceof DateTimeField);
    }

    @Test
    public void testAddLongLong() {
        BasicChronology chronology = new BasicChronology("Gregorian", DateTimeZone.forID("UTC"));
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        long instant = DateTimeConstants.MILLIS_IN_DAY * 30;
        long months = 12;
        long result = testField.add(instant, months);

        long expected = DateTimeConstants.MILLIS_PER_YEAR * 2 + DateTimeConstants.MILLIS_IN_DAY * 30;
        assertEquals(expected, result);

        int expectedMonth = 2;
        int actualMonth = testField.get(result);
        assertEquals(expectedMonth, actualMonth);
    }

    @Test
    public void testAddReadablePartial() {
        BasicChronology chronology = new BasicChronology("Gregorian", DateTimeZone.forID("UTC"));
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        LocalDateTime dateTime = new LocalDateTime(2000, 1, 1, 0, 0);
        MutableDateTime mutableDateTime = new MutableDateTime(dateTime);
        LocalDate date = mutableDateTime.toLocalDate();

        int[] values = new int[]{1, 1, 1};
        int fieldIndex = 0;

        int monthsToAdd = 12;
        testField.add(dateTime, fieldIndex, values, monthsToAdd);

        LocalDateTime expectedDateTime = new LocalDateTime(2001, 2, 1, 0, 0);
        LocalDate expectedDate = expectedDateTime.toLocalDate();

        LocalDateTime actualDateTime = new MutableDateTime(dateTime).toLocalDateTime();

        assertEquals(expectedDateTime, actualDateTime);
        assertEquals(expectedDate, date);
    }

    @Test
    public void testAddWrapField() {
        BasicChronology chronology = new BasicChronology("Gregorian", DateTimeZone.forID("UTC"));
        BasicMonthOfYearDateTimeField testField = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        long instant = DateTimeConstants.MILLIS_PER_YEAR - DateTimeConstants.MILLIS_IN_DAY;
        int monthsToAdd = 13;
        long result = testField.addWrapField(instant, monthsToAdd);

        long expected = DateTimeConstants.MILLIS_PER_YEAR - DateTimeConstants.MILLIS_IN_DAY * 12;
        assertEquals(expected, result);

        int expectedMonth = 12;
        int actualMonth = testField.get(result);
        assertEquals(expectedMonth, actualMonth);
    }
}