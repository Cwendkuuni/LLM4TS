package org.joda.time.chrono;

import org.joda.time.BaseDateTime;
import org.joda.time.BaseDuration;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationFieldTypeValidator;
import org.joda.time.FieldUtils;
import org.joda.time.ImmutableDateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadableDateTime;
import org.joda.time.ReadablePartial;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.FieldPosition;
import org.joda.time.ImmutableDuration;
import org.joda.time.LocalDateTimeField;
import org.joda.time.LocalMonthOfYearDateTimeField;
import org.joda.time.LocalMonthOfYearField;
import org.joda.time.Months;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    private static final Chronology ISO_8601 = DateTimeUtils.getISO8601UTC();

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(ISO_8601, DateTimeConstants.JANUARY);
        assertEquals(DateTimeFieldType.monthOfYear(), field.getType());
        assertEquals(DateTimeFieldType.monthOfYear().getMillisPerValue(), field.getPrecision());
        assertEquals(DateTimeFieldType.monthOfYear().getMillisPerValue() * 12, field.getMaximumValue() * field.getPrecision());
        assertEquals(DateTimeFieldType.monthOfYear().getMillisPerValue() * DateTimeConstants.JANUARY, field.getMinimumValue() * field.getPrecision());

        // Test get
        MutableDateTime date = new MutableDateTime(0, 0, 0, 0, 0, 0, 0);
        date.setField(field, DateTimeConstants.JANUARY);
        assertEquals(DateTimeConstants.JANUARY, field.get(date.getMillis()));

        // Test add
        MutableDateTime date2 = new MutableDateTime(0, 0, 0, 0, 0, 0, 0);
        date2.setField(field, 3);
        long result = field.add(date.getMillis(), 5);
        assertEquals(result, date2.getMillis());

        // Test add wrap
        MutableDateTime date3 = new MutableDateTime(0, 0, 0, 0, 0, 0, 0);
        date3.setField(field, 13);
        result = field.addWrapField(date.getMillis(), 5);
        assertEquals(result, date3.getMillis());

        // Test set
        MutableDateTime date4 = new MutableDateTime(0, 0, 0, 0, 0, 0, 0);
        result = field.set(date.getMillis(), 14);
        assertEquals(result, date4.getMillis());

        // Test getDifference
        assertEquals(12, field.getDifferenceAsLong(date.getMillis(), date2.getMillis()));

        // Test add with negative values
        MutableDateTime date5 = new MutableDateTime(0, 0, 0, 0, 0, 0, 0);
        result = field.add(date.getMillis(), -5);
        assertEquals(result, date5.getMillis());

        // Test add with zero value
        result = field.add(date.getMillis(), 0);
        assertEquals(result, date.getMillis());

        // Test add with large values
        MutableDateTime date6 = new MutableDateTime(Long.MAX_VALUE, 0, 0, 0, 0, 0, 0);
        result = field.add(date.getMillis(), Integer.MAX_VALUE);
        assertEquals(result, date6.getMillis());

        // Test isLeap
        MutableDateTime date7 = new MutableDateTime(0, 0, 0, 0, 0, 0, 0);
        date7.setField(field, DateTimeConstants.FEBRUARY);
        assertTrue(field.isLeap(date7.getMillis()));
        assertFalse(field.isLeap(date.getMillis()));

        // Test getLeapAmount
        assertEquals(1, field.getLeapAmount(date7.getMillis()));
        assertEquals(0, field.getLeapAmount(date.getMillis()));

        // Test getLeapDurationField
        DurationField leapDurationField = field.getLeapDurationField();
        assertEquals(DateTimeFieldType.dayOfMonth(), leapDurationField.getType());
        assertEquals(DateTimeFieldType.dayOfMonth().getMillisPerValue(), leapDurationField.getPrecision());
        assertEquals(DateTimeFieldType.dayOfMonth().getMillisPerValue(), leapDurationField.getMillisPerValue());

        // Test getRangeDurationField
        DurationField rangeDurationField = field.getRangeDurationField();
        assertEquals(DateTimeFieldType.years(), rangeDurationField.getType());
        assertEquals(DateTimeFieldType.years().getMillisPerValue(), rangeDurationField.getPrecision());
        assertEquals(DateTimeFieldType.years().getMillisPerValue(), rangeDurationField.getMillisPerValue());

        // Test set with invalid value
        try {
            result = field.set(date.getMillis(), 13);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test add with invalid value
        try {
            result = field.add(date.getMillis(), 13);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test add with ReadablePartial
        ReadablePartial partial = new LocalDateTimeField(new LocalMonthOfYearField(ISO_8601), DateTimeFieldType.monthOfYear(), DateTimeFieldType.monthOfYear().getMillisPerValue()).getField(new LocalDateTimeField(new LocalDate(2001, 1, 31), DateTimeFieldType.dayOfMonth(), DateTimeFieldType.dayOfMonth().getMillisPerValue()).getField(new LocalTime(20, 0, 0, 0)));
        result = field.add(partial.getMillis(), 3);
        LocalDateTimeField resultPartial = new LocalDateTimeField(new LocalMonthOfYearField(ISO_8601), DateTimeFieldType.monthOfYear(), DateTimeFieldType.monthOfYear().getMillisPerValue()).getField(new LocalDate(2001, 4, 2));
        assertEquals(resultPartial.getMillis(), result);

        // Test add with long value
        long testAdd = 0;
        for (int i = 0; i < 500; i++) {
            testAdd += FieldPosition.FIELD_POSITION_MONTH_OF_YEAR.getValue();
            assertEquals(new ImmutableDateTime(date.withFieldAdded(FieldPosition.FIELD_POSITION_MONTH_OF_YEAR, i + 1).getMillis()), new ImmutableDateTime(date.getMillis() + testAdd * DateTimeConstants.MILLIS_PER_MONTH));
        }

        // Test add wrap field
        MutableDateTime date8 = new MutableDateTime(0, 0, 0, 0, 0, 0, 0);
        result = field.addWrapField(date.getMillis(), 3);
        assertEquals(result, date8.getMillis());
        assertEquals(DateTimeConstants.JANUARY, field.get(result));

        // Test getDifferenceAsLong with negative values
        result = field.getDifferenceAsLong(date2.getMillis(), date.getMillis());
        assertEquals(-12, result);

        // Test getDifferenceAsLong with large values
        result = field.getDifferenceAsLong(Long.MIN_VALUE, date.getMillis());
        assertEquals(-1134, result);
        result = field.getDifferenceAsLong(Long.MAX_VALUE, date.getMillis());
        assertEquals(1134, result);

        // Test roundFloor and remainder
        assertEquals(DateTimeConstants.JANUARY, field.roundFloor(date.getMillis()));
        assertEquals(DateTimeConstants.JANUARY, field.roundFloor(date2.getMillis()));
        assertEquals(DateTimeConstants.JANUARY, field.remainder(date.getMillis()));
        assertEquals(DateTimeConstants.JANUARY, field.remainder(date2.getMillis()));

        // Test roundFloor and remainder with negative values
        assertEquals(DateTimeConstants.DECEMBER, field.roundFloor(date.getMillis() - 31 * DateTimeConstants.MILLIS_PER_DAY));
        assertEquals(DateTimeConstants.DECEMBER, field.remainder(date.getMillis() - 31 * DateTimeConstants.MILLIS_PER_DAY));

        // Test roundFloor and remainder with large values
        MutableDateTime date9 = new MutableDateTime(Long.MAX_VALUE, 0, 0, 0, 0, 0, 0);
        assertEquals(DateTimeConstants.JANUARY, field.roundFloor(date9.getMillis()));
        assertEquals(DateTimeConstants.JANUARY, field.remainder(date9.getMillis()));

        // Test set with negative values
        result = field.set(date.getMillis(), -1);
        assertEquals(result, date.getMillis());

        // Test set with large values
        result = field.set(date.getMillis(), 13 * 12 + 31);
        assertEquals(result, date.getMillis());

        // Test isLeap with negative values
        date7.setField(field, DateTimeConstants.FEBRUARY);
        date7.setField(DateTimeFieldType.year(), -1000);
        assertFalse(field.isLeap(date7.getMillis()));

        // Test getLeapAmount with negative values
        assertEquals(0, field.getLeapAmount(date7.getMillis()));

        // Test getLeapDurationField with negative values
        DurationField leapDurationFieldNegative = field.getLeapDurationField();
        assertEquals(DateTimeFieldType.dayOfMonth(), leapDurationFieldNegative.getType());
        assertEquals(DateTimeFieldType.dayOfMonth().getMillisPerValue(), leapDurationFieldNegative.getPrecision());
        assertEquals(DateTimeFieldType.dayOfMonth().getMillisPerValue(), leapDurationFieldNegative.getMillisPerValue());
        assertEquals(-1, leapDurationFieldNegative.getLeapCount(DateTimeConstants.JANUARY, -1000));

        // Test getLeapAmount with large values
        assertEquals(1, field.getLeapAmount(date7.getMillis() + 1000 * DateTimeConstants.MILLIS_PER_YEAR));

        // Test getLeapDurationField with large values
        DurationField leapDurationFieldLarge = field.getLeapDurationField();
        assertEquals(DateTimeFieldType.dayOfMonth(), leapDurationFieldLarge.getType());
        assertEquals(DateTimeFieldType.dayOfMonth().getMillisPerValue(), leapDurationFieldLarge.getPrecision());
        assertEquals(DateTimeFieldType.dayOfMonth().getMillisPerValue(), leapDurationFieldLarge.getMillisPerValue());
        assertEquals(1, leapDurationFieldLarge.getLeapCount(DateTimeConstants.JANUARY, 1000));

        // Test getLeapDurationField validation
        DurationFieldTypeValidator validator = new DurationFieldTypeValidator();
        try {
            validator.validate(leapDurationField.getType());
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        // Test getLeapDurationField validation with long value
        try {
            validator.validate(leapDurationFieldLarge.getType());
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }
}