package org.joda.time.chrono.tests;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.ReadablePartial;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    private static final Chronology ISO_8601 = DateTimeUtils.getISO8601UTC();

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        BasicMonthOfYearDateTimeField field = ISO_8601.monthOfYear();
        assertEquals("Type", DateTimeFieldType.monthOfYear(), field.getDataType());
        assertEquals("Minimum Value", DateTimeConstants.JANUARY, field.getMinimumValue());
        assertEquals("Maximum Value", DateTimeConstants.DECEMBER, field.getMaximumValue());
        
        // Test get method
        assertEquals("Get current month", DateTimeConstants.JANUARY, field.get(System.currentTimeMillis()));
        
        // Test add method
        long millis = BaseDateTime.parse("2012-01-31T12:00:00.000Z").getMillis();
        assertEquals("Add one month", DateTimeConstants.FEBRUARY, field.get(millis + 43200000L));
        
        // Test addWrapField method
        millis = BaseDateTime.parse("2012-01-31T12:00:00.000Z").getMillis();
        assertEquals("Add 13 months", DateTimeConstants.MARCH, field.addWrapField(millis, 13));

        // Test set method
        millis = BaseDateTime.parse("2012-01-31T12:00:00.000Z").getMillis();
        assertEquals("Set month to February", LocalDate.parse("2012-02-29").toMillis(),
                field.set(millis, DateTimeConstants.FEBRUARY));

        // Test add method with long argument
        millis = BaseDateTime.parse("2012-01-31T12:00:00.000Z").getMillis();
        assertEquals("Add one month with long argument", LocalDate.parse("2012-02-29").toMillis(),
                field.add(millis, 1));

        // Test add method with ReadablePartial argument
        ReadablePartial partial = new LocalDate();
        partial.setMillis(millis);
        int[] values = new int[]{DateTimeConstants.JANUARY};
        int valueToAdd = 1;
        int[] result = field.add(partial, DurationFieldType.MONTHS.getMillis(), values, valueToAdd);
        assertEquals("Add one month with ReadablePartial argument", DateTimeConstants.FEBRUARY, result[0]);

        // Test isLeap method
        millis = BaseDateTime.parse("2000-02-29T12:00:00.000Z").getMillis();
        assertTrue("Is leap year", field.isLeap(millis));

        // Test getLeapAmount method
        millis = BaseDateTime.parse("2001-01-31T12:00:00.000Z").getMillis();
        assertEquals("Get leap amount", 0, field.getLeapAmount(millis));

        // Test getLeapDurationField method
        DurationField df = field.getLeapDurationField();
        assertEquals("Leap duration field type", DurationFieldType.DAYS, df.getDataType());
        assertEquals("Leap duration field minimum value", 0, df.getMinimumValue());
        assertEquals("Leap duration field maximum value", 1, df.getMaximumValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddException() {
        BasicMonthOfYearDateTimeField field = ISO_8601.monthOfYear();
        long millis = BaseDateTime.parse("2012-01-31T12:00:00.000Z").getMillis();
        field.add(millis, Integer.MAX_VALUE + 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetException() {
        BasicMonthOfYearDateTimeField field = ISO_8601.monthOfYear();
        long millis = BaseDateTime.parse("2012-01-31T12:00:00.000Z").getMillis();
        field.set(millis, DateTimeConstants.JANUARY + 13);
    }
}