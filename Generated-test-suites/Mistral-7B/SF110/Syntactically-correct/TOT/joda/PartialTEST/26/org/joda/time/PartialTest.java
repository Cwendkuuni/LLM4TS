package org.joda.time.test;

import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class PartialTest
{
    @Test
    public void testEmptyPartial()
    {
        Partial emptyPartial = new Partial();
        assertEquals(0, emptyPartial.size());
        assertTrue(emptyPartial.isMatch(new BaseDateTime()));
        assertNull(emptyPartial.getFormatter());
        assertEquals("[]", emptyPartial.toString());
    }

    @Test
    public void testPartialWithOneField()
    {
        final DateTimeFieldType yearField = DateTimeFieldType.year();
        Partial partial = new Partial(yearField, 2000);
        assertEquals(1, partial.size());
        assertEquals(DateTimeFieldType.year(), partial.getFieldTypes()[0]);
        assertEquals(2000, partial.getValue(0));
        assertTrue(partial.isMatch(new BaseDateTime(2000, DateTimeConstants.JANUARY_1, DateTimeConstants.DAY_OF_MONTH_1, DateTimeConstants.HOUR_OF_DAY, DateTimeConstants.MINUTE_OF_HOUR, DateTimeConstants.SECOND_OF_MINUTE, DateTimeConstants.MILLISECOND_OF_SECOND, DateTimeConstants.MILLIS_PER_DAY)));
        assertEquals("year=2000", partial.toString());
        assertEquals("yyyy", partial.getFormatter().toString());
    }

    @Test
    public void testPartialWithMultipleFields()
    {
        final DateTimeFieldType yearField = DateTimeFieldType.year();
        final DateTimeFieldType monthField = DateTimeFieldType.monthOfYear();
        final DateTimeFieldType dayOfMonthField = DateTimeFieldType.dayOfMonth();
        Partial partial = new Partial(yearField, 2000, monthField, 3, dayOfMonthField, 15);
        assertEquals(3, partial.size());
        assertEquals(yearField, partial.getFieldTypes()[0]);
        assertEquals(monthField, partial.getFieldTypes()[1]);
        assertEquals(dayOfMonthField, partial.getFieldTypes()[2]);
        assertEquals(2000, partial.getValue(0));
        assertEquals(3, partial.getValue(1));
        assertEquals(15, partial.getValue(2));
        LocalDateTime localDateTime = new LocalDateTime(2000, 3, 15, 0, 0, 0);
        assertTrue(partial.isMatch(localDateTime));
        assertEquals("year=2000, monthOfYear=3, dayOfMonth=15", partial.toString());
        assertEquals("yyyy-MM-dd", partial.getFormatter().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullTypesArray()
    {
        new Partial((DateTimeFieldType[])null, new int[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullValuesArray()
    {
        new Partial(DateTimeFieldType.year(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedArraySizes()
    {
        new Partial(DateTimeFieldType.year(), new int[] { 1, 2, 3 });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidFieldType()
    {
        new Partial((DateTimeFieldType[])null, new int[] { 1, DateTimeFieldType.DAY_OF_WEEK.getValue() });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidFieldTypesOrder()
    {
        new Partial(new DateTimeFieldType[] { DateTimeFieldType.DAY_OF_MONTH, DateTimeFieldType.YEAR }, new int[] { 2000, 31 });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullChronology()
    {
        new Partial(DateTimeFieldType.year(), Integer.MIN_VALUE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidChronology()
    {
        new Partial(DateTimeFieldType.year(), Integer.MIN_VALUE, new GregorianChronology());
    }

    @Test
    public void testCreateWithReadablePartial()
    {
        ReadablePartial readablePartial = new LocalDateTime(2000, 3, 15, 0, 0, 0).toPartial();
        Partial partial = new Partial(readablePartial);
        assertEquals(3, partial.size());
        assertEquals(readablePartial.getFieldTypes(), partial.getFieldTypes());
        assertArrayEquals(readablePartial.getValues(), partial.getValues());
        assertEquals("year=2000, monthOfYear=3, dayOfMonth=15", partial.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNegativeNumberOfFields()
    {
        new Partial(new DateTimeFieldType[0], new int[1], null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithLongNumberOfFields()
    {
        new Partial(new DateTimeFieldType[1000], new int[1000], null);
    }

    @Test
    public void testSetValue()
    {
        final DateTimeFieldType yearField = DateTimeFieldType.year();
        Partial partial = new Partial(yearField, 2000);
        partial = partial.with(yearField, 2010);
        assertEquals(1, partial.size());
        assertEquals(2010, partial.getValue(0));
    }

    @Test
    public void testSetValueOutOfRange()
    {
        final DateTimeFieldType yearField = DateTimeFieldType.year();
        Partial partial = new Partial(yearField, 2000);
        try {
            partial = partial.with(yearField, -1);
            fail();
        }
        catch (IllegalArgumentException e) {}

        try {
            partial = partial.with(yearField, 9999);
            fail();
        }
        catch (IllegalArgumentException e) {}
    }

    @Test
    public void testGetField()
    {
        final DateTimeFieldType yearField = DateTimeFieldType.year();
        Partial partial = new Partial(yearField, 2000);
        DateTimeField field = partial.getField(0, null);
        assertEquals(yearField, field);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFieldWithInvalidIndex()
    {
        final DateTimeFieldType yearField = DateTimeFieldType.year();
        Partial partial = new Partial(yearField, 2000);
        partial.getField(2, null);
    }

    @Test
    public void testGetValues()
    {
        final DateTimeFieldType yearField = DateTimeFieldType.year();
        final DateTimeFieldType monthField = DateTimeFieldType.monthOfYear();
        Partial partial = new Partial(yearField, 2000, monthField, 3);
        int[] values = partial.getValues();
        assertEquals(3, values.length);
        assertEquals(2000, values[0]);
        assertEquals(3, values[1]);
        assertEquals(-1, values[2]);
    }

    @Test
    public void testGetFieldTypes()
    {
        final DateTimeFieldType yearField = DateTimeFieldType.year();
        final DateTimeFieldType monthField = DateTimeFieldType.monthOfYear();
        Partial partial = new Partial(yearField, 2000, monthField, 3);
        DateTimeFieldType[] fieldTypes = partial.getFieldTypes();
        assertEquals(3, fieldTypes.length);
        assertEquals(yearField, fieldTypes[0]);
        assertEquals(monthField, fieldTypes[1]);
        assertEquals(null, fieldTypes[2]);
    }

    @Test
    public void testToString()
    {
        final DateTimeFieldType yearField = DateTimeFieldType.year();
        final DateTimeFieldType monthField = DateTimeFieldType.monthOfYear();
        Partial partial = new Partial(yearField, 2000, monthField, 3);
        assertEquals("year=2000, monthOfYear=3", partial.toString());
    }

    @Test
    public void testFormat()
    {
        final DateTimeFieldType yearField = DateTimeFieldType.year();
        final DateTimeFieldType monthField = DateTimeFieldType.monthOfYear();
        Partial partial = new Partial(yearField, 2000, monthField, 3);
        DateTimeFormatter formatter = ISODateTimeFormat.forFields(new DateTimeFieldType[] { yearField, monthField }, true, false);
        assertEquals("2000-03", formatter.print(partial));
    }

    @Test
    public void testEquals()
    {
        final DateTimeFieldType yearField = DateTimeFieldType.year();
        Partial partial1 = new Partial(yearField, 2000);
        Partial partial2 = new Partial(yearField, 2000);
        Partial partial3 = new Partial(yearField, 2001);
        assertEquals(partial1, partial2);
        assertNotEquals(partial1, partial3);
    }

    @Test
    public void testIsMatchWithInstant()
    {
        final DateTimeFieldType yearField = DateTimeFieldType.year();
        Partial partial = new Partial(yearField, 2000);
        Instant instant = new Instant(0);
        assertTrue(partial.isMatch(new BaseDateTime(instant)));
        instant = new Instant(DateTimeConstants.MILLIS_PER_DAY);
        assertFalse(partial.isMatch(new BaseDateTime(instant)));
        instant = new Instant(DateTimeConstants.MILLIS_PER_DAY * 2);
        assertTrue(partial.isMatch(new BaseDateTime(instant)));
    }

    @Test
    public void testIsMatchWithReadablePartial()
    {
        final DateTimeFieldType yearField = DateTimeFieldType.year();
        Partial partial1 = new Partial(yearField, 2000);
        Partial partial2 = new Partial(yearField, 2001);
        ReadablePartial readablePartial = new LocalDateTime(2000, 1, 1, 0, 0, 0).toPartial();
        assertTrue(partial1.isMatch(readablePartial));
        assertFalse(partial2.isMatch(readablePartial));
    }

    @Test
    public void testIsMatchWithNull()
    {
        Partial partial = new Partial();
        assertFalse(partial.isMatch(null));
    }

    @Test
    public void testAdd()
    {
        final DateTimeFieldType yearField = DateTimeFieldType.year();
        final DateTimeFieldType monthField = DateTimeFieldType.monthOfYear();
        Partial partial = new Partial(yearField, 2000, monthField, 12);
        partial = partial.plus(DurationFieldType.days().withValue(365));
        DateTimeField field = partial.getField(0, null);
        assertEquals(DateTimeFieldType.year(), field);
        assertEquals(2001, partial.getValue(0));
    }

    @Test
    public void testAddWrapPartial()
    {
        final DateTimeFieldType yearField = DateTimeFieldType.year();
        final DateTimeFieldType monthField = DateTimeFieldType.monthOfYear();
        Partial partial = new Partial(yearField, 2000, monthField, 12);
        partial = partial.withFieldAddWrapped(DurationFieldType.days(), 1000);
        DateTimeField field = partial.getField(0, null);
        assertEquals(DateTimeFieldType.year(), field);
        assertEquals(2001, partial.getValue(0));
    }

    @Test
    public void testAddWrapped()
    {
        final DateTimeFieldType yearField = DateTimeFieldType.year();
        final DateTimeFieldType monthField = DateTimeFieldType.monthOfYear();
        Partial partial = new Partial(yearField, 2000, monthField, 12);
        partial = partial.withFieldAdded(DurationFieldType.days(), 1000);
        DateTimeField field = partial.getField(0, null);
        assertEquals(DateTimeFieldType.year(), field);
        assertEquals(2001, partial.getValue(0));
    }

    @Test
    public void testAddPeriod()
    {
        final DateTimeFieldType yearField = DateTimeFieldType.year();
        final DateTimeFieldType monthField = DateTimeFieldType.monthOfYear();
        final DurationFieldType daysField = DurationFieldType.days();
        Partial partial = new Partial(yearField, 2000, monthField, 12);
        ReadablePeriod period = Duration.standardDays(365);
        partial = partial.withPeriodAdded(period, 1);
        DateTimeField field = partial.getField(0, null);
        assertEquals(DateTimeFieldType.year(), field);
        assertEquals(2001, partial.getValue(0));
    }
}