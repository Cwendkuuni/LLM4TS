import org.joda.time.BasicChronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationField;
import org.joda.time.FieldUtils;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testGetMinMaxValues() {
        BasicChronology gregorian = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);
        assertEquals(DateTimeConstants.JANUARY, field.getMinimumValue());
        assertEquals(DateTimeConstants.DECEMBER, field.getMaximumValue());
    }

    @Test
    public void testGetMonth() {
        BasicChronology gregorian = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);

        long instant = DateTimeConstants.MILLIS_IN_DAY;
        assertEquals(DateTimeConstants.JANUARY, field.get(instant));

        instant += DateTimeConstants.MILLIS_PER_DAY * 31;
        assertEquals(DateTimeConstants.FEBRUARY, field.get(instant));

        instant += DateTimeConstants.MILLIS_PER_DAY * (28 + DateTimeConstants.FEBRUARY_LEAPS[DateTimeUtils.getLeapYearForDate(new LocalDate(instant))]);
        assertEquals(DateTimeConstants.MARCH, field.get(instant));

        // Test negative month value
        instant -= DateTimeConstants.MILLIS_PER_DAY * 12;
        assertEquals(DateTimeConstants.DECEMBER, field.get(instant));

        instant -= DateTimeConstants.MILLIS_PER_DAY * 31;
        assertEquals(DateTimeConstants.NOVEMBER, field.get(instant));
    }

    @Test
    public void testAddMonths() {
        BasicChronology gregorian = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);

        long instant = DateTimeConstants.MILLIS_IN_DAY;
        long expected = DateTimeConstants.MILLIS_PER_DAY * 365;
        long result = field.add(instant, 11);
        assertEquals(expected, result);

        expected += DateTimeConstants.MILLIS_PER_DAY * 366; // Leap year
        result = field.add(instant, 12);
        assertEquals(expected, result);

        // Test negative months
        result = field.add(instant, -12);
        assertEquals(instant, result);

        result = field.add(instant, -32);
        assertEquals(instant, result);

        // Test adding months to February with and without leap year
        int leapYear = DateTimeConstants.getLeapYearForDate(new LocalDate(instant));
        int maxFebruaryDays = DateTimeConstants.FEBRUARY_LEAPS[leapYear] ? 29 : 28;

        instant += DateTimeConstants.MILLIS_PER_DAY * 29;
        result = field.add(instant, 2);
        assertEquals(instant + 2 * DateTimeConstants.MILLIS_PER_DAY, result);

        instant += DateTimeConstants.MILLIS_PER_DAY;
        result = field.add(instant, 2);
        assertEquals(instant + DateTimeConstants.MILLIS_PER_DAY, result);

        // Test adding months to February with leap year
        instant += DateTimeConstants.MILLIS_PER_DAY * 28;
        result = field.add(instant, 2);
        assertEquals(instant + 2 * DateTimeConstants.MILLIS_PER_DAY, result);

        instant += DateTimeConstants.MILLIS_PER_DAY;
        result = field.add(instant, 2);
        assertEquals(instant + 3 * DateTimeConstants.MILLIS_PER_DAY, result);

        // Test adding months to February with leap year, February already having max days
        instant += DateTimeConstants.MILLIS_PER_DAY * maxFebruaryDays;
        result = field.add(instant, 2);
        assertEquals(instant, result);
    }

    @Test
    public void testAddMonthsWithDays() {
        BasicChronology gregorian = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);

        long instant = DateTimeConstants.MILLIS_IN_DAY;
        long expected = DateTimeConstants.MILLIS_PER_DAY * 365;
        long result = field.add(instant, 11, new DurationFieldsupportingAddWrap(DateTimeFieldType.dayOfMonth(), 31));
        assertEquals(expected, result);

        // Test adding months with leap year
        expected += DateTimeConstants.MILLIS_PER_DAY * 366;
        result = field.add(instant, 12, new DurationFieldsupportingAddWrap(DateTimeFieldType.dayOfMonth(), maxDayOfMonthForLeapYear()));
        assertEquals(expected, result);

        // Test adding months to February with leap year
        int leapYear = DateTimeConstants.getLeapYearForDate(new LocalDate(instant));
        int maxFebruaryDays = DateTimeConstants.FEBRUARY_LEAPS[leapYear] ? 29 : 28;

        instant += DateTimeConstants.MILLIS_PER_DAY * 29;
        expected += 2 * DateTimeConstants.MILLIS_PER_DAY;
        result = field.add(instant, 2, new DurationFieldsupportingAddWrap(DateTimeFieldType.dayOfMonth(), maxFebruaryDays));
        assertEquals(expected, result);

        instant += DateTimeConstants.MILLIS_PER_DAY;
        expected += DateTimeConstants.MILLIS_PER_DAY;
        result = field.add(instant, 2, new DurationFieldsupportingAddWrap(DateTimeFieldType.dayOfMonth(), maxFebruaryDays));
        assertEquals(expected, result);

        // Test adding months to February with leap year, February already having max days
        instant += DateTimeConstants.MILLIS_PER_DAY * maxFebruaryDays;
        result = field.add(instant, 2, new DurationFieldsupportingAddWrap(DateTimeFieldType.dayOfMonth(), maxFebruaryDays));
        assertEquals(instant, result);
    }

    @Test
    public void testAddMonthsWithInvalidInput() {
        BasicChronology gregorian = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);

        long instant = DateTimeConstants.MILLIS_IN_DAY;

        try {
            field.add(instant, Int.MIN_VALUE);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            field.add(instant, Int.MAX_VALUE);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            field.add(instant, 12, new DurationFieldsupportingAddWrap(DateTimeFieldType.dayOfMonth(), Int.MIN_VALUE));
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            field.add(instant, 12, new DurationFieldsupportingAddWrap(DateTimeFieldType.dayOfMonth(), Int.MAX_VALUE));
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAddMonthsWithInvalidInput2() {
        BasicChronology gregorian = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);

        long instant = DateTimeConstants.MILLIS_IN_DAY;

        try {
            field.addWrapField(instant, Int.MIN_VALUE);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            field.addWrapField(instant, Int.MAX_VALUE);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetDifference() {
        BasicChronology gregorian = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);

        long minuendInstant = DateTimeConstants.MILLIS_IN_DAY;
        long subtrahendInstant = minuendInstant + DateTimeConstants.MILLIS_PER_DAY * 365;
        long difference = field.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        assertEquals(1, difference);

        difference = field.getDifferenceAsLong(subtrahendInstant, minuendInstant);
        assertEquals(-1, difference);
    }

    @Test
    public void testSet() {
        BasicChronology gregorian = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);

        long instant = DateTimeConstants.MILLIS_IN_DAY;
        long expected = DateTimeConstants.MILLIS_PER_DAY * (DateTimeConstants.JANUARY - 1) + DateTimeConstants.MILLIS_PER_DAY * 31;
        long result = field.set(instant, DateTimeConstants.DECEMBER);
        assertEquals(expected, result);

        instant = DateTimeConstants.MILLIS_IN_DAY;
        expected = DateTimeConstants.MILLIS_PER_DAY * (DateTimeConstants.DECEMBER + 1) - DateTimeConstants.MILLIS_PER_DAY;
        result = field.set(instant, DateTimeConstants.JANUARY);
        assertEquals(expected, result);

        instant = DateTimeConstants.MILLIS_IN_DAY;
        expected = DateTimeConstants.MILLIS_PER_DAY * (DateTimeConstants.JANUARY - 1) + DateTimeConstants.MILLIS_PER_DAY * 31 + DateTimeConstants.MILLIS_PER_DAY * 28;
        result = field.set(instant, DateTimeConstants.FEBRUARY);
        assertEquals(expected, result);

        instant = DateTimeConstants.MILLIS_IN_DAY + DateTimeConstants.MILLIS_PER_DAY * 28;
        expected = DateTimeConstants.MILLIS_PER_DAY * (DateTimeConstants.FEBRUARY + 1) - DateTimeConstants.MILLIS_PER_DAY;
        result = field.set(instant, DateTimeConstants.MARCH);
        assertEquals(expected, result);
    }

    @Test
    public void testIsLeap() {
        BasicChronology gregorian = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);

        LocalDate date = new LocalDate();
        int year = date.getYear();
        int month = date.getMonthOfYear();

        long instant = iChronology.getYearMonthDayMillis(year, month, 1);

        boolean result = field.isLeap(instant);
        assertEquals(DateTimeConstants.isLeapYear(year), result);
    }

    @Test
    public void testGetLeapAmount() {
        BasicChronology gregorian = new BasicGregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorian, DateTimeConstants.JANUARY);

        long instant = DateTimeConstants.MILLIS_IN_DAY;

        int result = field.getLeapAmount(instant);
        assertEquals(DateTimeConstants.isLeapYear(iChronology.getYear(instant)) ? 1 : 0, result);
    }

    private static int maxDayOfMonthForLeapYear() {
        return DateTimeConstants.FEBRUARY_LEAPS[DateTimeConstants.getLeapYearForDate(new LocalDate())] ? 29 : 28;
    }

    private static class DurationFieldsupportingAddWrap implements DurationField {
        private final DateTimeFieldType fieldType;
        private final int maxValue;

        public DurationFieldsupportingAddWrap(DateTimeFieldType fieldType, int maxValue) {
            this.fieldType = fieldType;
            this.maxValue = maxValue;
        }

        @Override
        public DateTimeFieldType getFieldType() {
            return fieldType;
        }

        @Override
        public long getValue(long instant) {
            int value = iChronology.getFieldValue(instant, fieldType);
            if (value > maxValue) {
                value -= maxValue;
                instant = iChronology.add(instant, fieldType, -maxValue);
            }
            return value;
        }

        @Override
        public long set(long instant, long value) {
            int newValue = (int)value;
            if (newValue > maxValue) {
                newValue -= maxValue;
                instant = iChronology.add(instant, fieldType, -maxValue);
            }
            return iChronology.set(instant, fieldType, newValue);
        }

        @Override
        public long add(long base, long value) {
            int newValue = (int)(value + iChronology.getFieldValue(base, fieldType));
            if (newValue > maxValue) {
                newValue -= maxValue;
                base = iChronology.add(base, fieldType, -maxValue);
            }
            return iChronology.get(new LocalDate(), fieldType).addTo(base, new Duration(value, fieldType)).getMillis();
        }
    }
}