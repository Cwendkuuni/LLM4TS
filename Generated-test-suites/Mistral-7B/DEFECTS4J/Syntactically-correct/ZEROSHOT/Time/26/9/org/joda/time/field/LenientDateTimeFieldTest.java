import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDateTimeField;
import org.joda.time.MutableDateTime;
import org.joda.time.ZoneOffset;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class LenientDateTimeFieldTest {

    @Test
    public void testInstance() {
        DateTimeField field = LenientDateTimeField.getInstance(new LocalDateTimeField(DateTimeFieldType.HOUR_OF_DAY(), 3, Chronology.UTC), Chronology.UTC);
        assertNotNull(field);
        assertEquals("LenientDateTimeField", field.getClass().getSimpleName());
    }

    @Test
    public void testLenient() {
        DateTimeField field = new LenientDateTimeField(new LocalDateTimeField(DateTimeFieldType.HOUR_OF_DAY(), 3, Chronology.UTC), Chronology.UTC);
        assertTrue(field.isLenient());
    }

    @Test
    public void testSetOutOfBoundsValue() {
        DateTimeField field = new LenientDateTimeField(new LocalDateTimeField(DateTimeFieldType.HOUR_OF_DAY(), 3, Chronology.UTC), Chronology.UTC);
        MutableDateTime dateTime = new MutableDateTime(new BaseDateTime(0L, Chronology.UTC));
        long originalValue = dateTime.get(field);
        long valueToSet = originalValue + 12;

        long setValue = field.set(dateTime.toInstant().getEpochMilli(), valueToSet);
        MutableDateTime resultDateTime = new MutableDateTime(setValue);

        assertEquals("Date time should be increased by 12 hours", originalValue + 12 * 60 * 60 * 1000L, resultDateTime.toInstant().getEpochMilli());
    }

    @Test
    public void testSetNegativeValue() {
        DateTimeField field = new LenientDateTimeField(new LocalDateTimeField(DateTimeFieldType.HOUR_OF_DAY(), 3, Chronology.UTC), Chronology.UTC);
        MutableDateTime dateTime = new MutableDateTime(new BaseDateTime(0L, Chronology.UTC));
        long originalValue = dateTime.get(field);
        long valueToSet = originalValue - 12;

        long setValue = field.set(dateTime.toInstant().getEpochMilli(), valueToSet);
        MutableDateTime resultDateTime = new MutableDateTime(setValue);

        // Since the value is negative, the result should be the same as if we added the difference to the original value
        long expectedValue = originalValue - 12 * 60 * 60 * 1000L;
        assertEquals("Date time should be decreased by 12 hours", expectedValue, resultDateTime.toInstant().getEpochMilli());
    }

    @Test
    public void testSetValueWithinBounds() {
        DateTimeField field = new LenientDateTimeField(new LocalDateTimeField(DateTimeFieldType.HOUR_OF_DAY(), 3, Chronology.UTC), Chronology.UTC);
        MutableDateTime dateTime = new MutableDateTime(new BaseDateTime(0L, Chronology.UTC));
        long originalValue = dateTime.get(field);
        long valueToSet = originalValue + 1;

        long setValue = field.set(dateTime.toInstant().getEpochMilli(), valueToSet);
        MutableDateTime resultDateTime = new MutableDateTime(setValue);

        assertEquals("Date time should be equal to the original value", originalValue + 1, resultDateTime.get(field));
    }

    @Test
    public void testSetValueWithinBoundsWithDuration() {
        DateTimeField field = new LenientDateTimeField(new LocalDateTimeField(DateTimeFieldType.HOUR_OF_DAY(), 3, Chronology.UTC), Chronology.UTC);
        MutableDateTime dateTime = new MutableDateTime(new BaseDateTime(0L, Chronology.UTC));
        long originalValue = dateTime.get(field);
        DurationFieldType durationType = DurationFieldType.HOURS();
        long duration = 1;

        long setValue = field.set(dateTime.toInstant().getEpochMilli(), originalValue + duration.getStandardValue());
        MutableDateTime resultDateTime = new MutableDateTime(setValue);

        assertEquals("Date time should be equal to the original value plus the duration", originalValue + duration.getStandardValue(), resultDateTime.get(field));
    }

    @Test
    public void testSetValueWithInstant() {
        DateTimeField field = new LenientDateTimeField(new LocalDateTimeField(DateTimeFieldType.HOUR_OF_DAY(), 3, Chronology.UTC), Chronology.UTC);
        MutableDateTime dateTime = new MutableDateTime(new BaseDateTime(0L, Chronology.UTC));
        long originalValue = dateTime.get(field);
        long valueToSet = originalValue + 12 * 60 * 60 * 1000L;
        long instant = dateTime.toInstant().getEpochMilli();

        long setValue = field.set(instant, valueToSet);
        MutableDateTime resultDateTime = new MutableDateTime(setValue);

        assertEquals("Date time should be increased by 12 hours", originalValue + 12 * 60 * 60 * 1000L, resultDateTime.toInstant().getEpochMilli());
    }

    @Test
    public void testSetValueWithUTCInstant() {
        DateTimeField field = new LenientDateTimeField(new LocalDateTimeField(DateTimeFieldType.HOUR_OF_DAY(), 3, Chronology.UTC), Chronology.UTC);
        long instant = System.currentTimeMillis() + 12 * 60 * 60 * 1000L;

        long setValue = field.set(instant, getFieldValue(instant, field));
        MutableDateTime resultDateTime = new MutableDateTime(setValue);

        // Check if the value was set correctly
        assertEquals("Date time should be increased by 12 hours", instant + 12 * 60 * 60 * 1000L, resultDateTime.toInstant().getEpochMilli());
    }

    @Test
    public void testSetValueWithLocalDateTime() {
        DateTimeField field = new LenientDateTimeField(new LocalDateTimeField(DateTimeFieldType.HOUR_OF_DAY(), 3, Chronology.UTC), Chronology.UTC);
        LocalDateTime localDateTime = new LocalDateTime(2023, 1, 1, 10, 0, 0, Chronology.UTC);

        long setValue = field.set(localDateTime.toInstant().getEpochMilli(), getFieldValue(localDateTime.toInstant().getEpochMilli(), field));
        MutableDateTime resultDateTime = new MutableDateTime(setValue);

        // Check if the value was set correctly
        LocalDateTime expectedLocalDateTime = new LocalDateTime(2023, 1, 1, 12, 0, 0, Chronology.UTC);
        assertEquals("Date time should be increased by 2 hours", expectedLocalDateTime.toInstant().getEpochMilli(), resultDateTime.toInstant().getEpochMilli());
    }

    @Test
    public void testSetValueWithZoneOffset() {
        DateTimeField field = new LenientDateTimeField(new LocalDateTimeField(DateTimeFieldType.HOUR_OF_DAY(), 3, Chronology.UTC), Chronology.UTC);
        ZoneOffset zoneOffset = ZoneOffset.ofHours(1);

        long setValue = field.set(new BaseDateTime(0L, Chronology.UTC).toInstant().getEpochMilli() + zoneOffset.getTotalSeconds() * 1000, getFieldValue(new BaseDateTime(0L, Chronology.UTC).toInstant().getEpochMilli() + zoneOffset.getTotalSeconds() * 1000, field));
        MutableDateTime resultDateTime = new MutableDateTime(setValue);

        // Check if the value was set correctly
        assertEquals("Date time should be increased by 1 hour", new BaseDateTime(0L, Chronology.UTC).plus(Duration.standardHours(1)).toInstant().getEpochMilli(), resultDateTime.toInstant().getEpochMilli());
    }

    // Utility method to get the expected value for a given instant and DateTimeField
    private long getFieldValue(long instant, DateTimeField field) {
        MutableDateTime dateTime = new MutableDateTime(instant);
        return dateTime.get(field);
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
    }

    private boolean isLeapYear(Chronology chronology, int year) {
        if (chronology instanceof org.joda.time.base.GregorianChronology) {
            return isLeapYear(year);
        }
        // Implement the logic to determine if a year is a leap year for other chronologies
        throw new UnsupportedOperationException("Leap year determination for chronologies other than Gregorian is not supported.");
    }

    private void assertLeapYear(Chronology chronology, int year, boolean expected) {
        boolean actual = isLeapYear(chronology, year);
        assertTrue("Year " + year + " should be a leap year: actual=" + actual + ", expected=" + expected, actual == expected);
    }

    private void assertNotLeapYear(Chronology chronology, int year) {
        assertFalse("Year " + year + " should not be a leap year", isLeapYear(chronology, year));
    }

    private void testLeapYearBehavior(DateTimeField field, int year) {
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, Chronology.UTC);

        MutableDateTime dateTime = new MutableDateTime(new BaseDateTime(0, Chronology.UTC).withYear(year).withDayOfMonth(29));
        long originalValue = dateTime.get(field);
        long valueToSet = originalValue + 1;

        long setValue = lenientField.set(dateTime.toInstant().getEpochMilli(), valueToSet);
        MutableDateTime resultDateTime = new MutableDateTime(setValue);

        if (isLeapYear(year)) {
            // If the year is a leap year, the set method should allow a value of 29 for the month with the field
            assertEquals("Date time should be equal to the original value plus 1 for a leap year", originalValue + 1, resultDateTime.get(field));
        } else {
            // If the year is not a leap year, the set method should not allow a value of 29 for the month with the field
            assertEquals("Date time should be equal to the original value for a non-leap year", originalValue, resultDateTime.get(field));
        }

        // Test with a value less than the original value
        long valueToSetLess = originalValue - 1;
        long setValueLess = lenientField.set(dateTime.toInstant().getEpochMilli(), valueToSetLess);
        MutableDateTime resultDateTimeLess = new MutableDateTime(setValueLess);

        if (isLeapYear(year)) {
            // If the year is a leap year, the set method should accept a value less than the original value
            assertEquals("Date time should be equal to the original value minus 1 for a leap year", originalValue - 1, resultDateTimeLess.get(field));
        } else {
            // If the year is not a leap year, the set method should throw an exception for a value less than the original value
            long expectedExceptionValue = originalValue - 2;
            Throwable exception = assertThrows(IllegalArgumentException.class, () -> lenientField.set(dateTime.toInstant().getEpochMilli(), expectedExceptionValue));
            assertEquals("Exception message should indicate that the value is out of bounds", "Value is out of bounds for lenient field: Value: " + expectedExceptionValue + ", Instant: " + dateTime.toInstant().getMillis() + ", Field: " + field.getClass().getSimpleName(), exception.getMessage());
        }
    }

    @Test
    public void testLeapYearBehavior() {
        DateTimeField field = new LocalDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), 29, Chronology.UTC);

        testLeapYearBehavior(field, 2000);
        testLeapYearBehavior(field, 2001);
        testLeapYearBehavior(field, 2004);
        testLeapYearBehavior(field, 2005);
        testLeapYearBehavior(field, 2012);
    }

    @Test
    public void testSetValueWithLocalDate() {
        DateTimeField field = new LenientLocalDateTimeField(DateTimeFieldType.DAY_OF_MONTH(), 29, Chronology.UTC);
        LocalDate date = LocalDate.of(2023, 2, 28);

        long setValue = field.set(date.atStartOfDay().toInstant().getEpochMilli(), getFieldValue(date.atStartOfDay().toInstant().getEpochMilli(), field));
        MutableDateTime resultDateTime = new MutableDateTime(setValue);

        if (isLeapYear(Chronology.UTC, 2023)) {
            // If the year is a leap year, the set method should allow a value of 29 for the month with the field
            assertEquals("Date time should be equal to the original value plus 1 for a leap year", getFieldValue(date.atStartOfDay().toInstant().getEpochMilli(), field) + 1, resultDateTime.get(field));
        } else {
            // If the year is not a leap year, the set method should not allow a value of 29 for the month with the field
            assertEquals("Date time should be equal to the original value for a non-leap year", getFieldValue(date.atStartOfDay().toInstant().getEpochMilli(), field), resultDateTime.get(field));
        }
    }
}