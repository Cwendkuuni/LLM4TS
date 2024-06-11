package org.joda.time;

import static org.junit.Assert.*;

import org.joda.time.base.*;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.*;
import org.joda.time.field.*;
import org.junit.Test;

public class PartialTest {

    @Test
    public void testEmptyPartial() {
        Partial partial = new Partial();
        assertEquals(0, partial.size());
        assertNull(partial.getChronology());
    }

    @Test
    public void testPartialWithChronology() {
        Partial partial = new Partial(ISOChronology.INSTANCE);
        assertEquals(0, partial.size());
        assertEquals(ISOChronology.INSTANCE, partial.getChronology());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPartialWithNullChronology() {
        new Partial(null);
    }

    @Test
    public void testPartialWithSingleField() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.instance(DateTimeFields.MILLI_SECONDS);
        Partial partial = new Partial(dateTimeFieldType, 123456);
        assertEquals(1, partial.size());
        assertEquals(dateTimeFieldType, partial.getFieldTypes()[0]);
        assertEquals(123456, partial.getValue(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPartialWithNullFieldType() {
        new Partial(null, 123456);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPartialWithInvalidLengthValues() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.instance(DateTimeFields.MILLI_SECONDS);
        new Partial(dateTimeFieldType, new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22 });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPartialWithDuplicateFields() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.instance(DateTimeFields.MILLI_SECONDS);
        new Partial(new DateTimeFieldType[] { dateTimeFieldType, dateTimeFieldType }, new int[] { 1, 2 });
    }

    @Test
    public void testPartialWithMultipleFields() {
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.instance(DateTimeFields.MILLI_SECONDS);
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.instance(DateTimeFields.SECONDS);
        Partial partial = new Partial(new DateTimeFieldType[] { dateTimeFieldType1, dateTimeFieldType2 }, new int[] { 123456, 7 });
        assertEquals(2, partial.size());
        assertEquals(dateTimeFieldType1, partial.getFieldTypes()[0]);
        assertEquals(dateTimeFieldType2, partial.getFieldTypes()[1]);
        assertEquals(123456, partial.getValue(0));
        assertEquals(7, partial.getValue(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPartialWithNullTypesArray() {
        new Partial(null, new int[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPartialWithNullValuesArray() {
        new Partial(new DateTimeFieldType[0], null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPartialWithMismatchedLengthTypesAndValues() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.instance(DateTimeFields.MILLI_SECONDS);
        new Partial(new DateTimeFieldType[] { dateTimeFieldType, DateTimeFieldType.instance(DateTimeFields.SECONDS) }, new int[] { 1, 2, 3 });
    }

    @Test
    public void testPartialWithReadablePartial() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.instance(DateTimeFields.MILLI_SECONDS);
        Partial partial1 = new Partial(dateTimeFieldType, 123456);
        Partial partial2 = new Partial(new DateTimeFieldType[] { dateTimeFieldType }, new int[] { 123456 });
        Partial partial3 = new Partial(partial1);
        assertEquals(partial3.size(), partial1.size());
        assertEquals(partial3.getChronology(), partial1.getChronology());
        assertEquals(partial3.getFieldTypes(), partial1.getFieldTypes());
        assertEquals(partial3.getValue(0), partial1.getValue(0));
        
        Partial partial4 = new Partial(partial3, new int[] { 123457 });
        assertEquals(partial4.size(), partial3.size() + 1);
        assertNotEquals(partial3.getValue(0), partial4.getValue(0));
        assertEquals(partial3.getFieldTypes()[0], partial4.getFieldTypes()[0]);
    }

    @Test
    public void testPartialWithChronologyRetainFields() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.instance(DateTimeFields.MILLI_SECONDS);
        Partial partial1 = new Partial(dateTimeFieldType, 123456, ISOChronology.INSTANCE);
        Partial partial2 = partial1.withChronologyRetainFields(ISOChronology.UTC);
        assertEquals(partial1.size(), partial2.size());
        assertEquals(partial1.getChronology(), partial2.getChronology());
        assertEquals(partial1.getFieldTypes(), partial2.getFieldTypes());
        assertEquals(partial1.getValue(0), partial2.getValue(0));
    }

    @Test
    public void testPartialWithField() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.instance(DateTimeFields.MILLI_SECONDS);
        Partial partial1 = new Partial(dateTimeFieldType, 123456, ISOChronology.INSTANCE);
        Partial partial2 = partial1.with(dateTimeFieldType, 56789);
        assertEquals(partial1.size(), partial2.size());
        assertEquals(partial1.getChronology(), partial2.getChronology());
        assertEquals(partial1.getFieldTypes(), partial2.getFieldTypes());
        assertNotEquals(partial1.getValue(0), partial2.getValue(0));
    }

    @Test
    public void testPartialWithFieldAdded() {
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.instance(DateTimeFields.MILLI_SECONDS);
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.instance(DateTimeFields.SECONDS);
        Partial partial1 = new Partial(new DateTimeFieldType[] { dateTimeFieldType1 }, new int[] { 123456 });
        Partial partial2 = partial1.withFieldAdded(DateTimeFieldType.instance(DateTimeFields.MILLISECONDS_PER_MINUTE), 60);
        assertEquals(partial1.size() + 1, partial2.size());
        assertEquals(partial1.getChronology(), partial2.getChronology());
        assertEquals(DateTimeFieldType.instance(DateTimeFields.MILLISECONDS_PER_MINUTE), partial2.getFieldTypes()[partial2.size() - 1]);
        assertEquals(60, partial2.getValue(partial2.size() - 1));
    }

    @Test
    public void testPartialWithFieldAddWrapped() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.instance(DateTimeFields.MILLI_SECONDS);
        Partial partial1 = new Partial(dateTimeFieldType, 123456, ISOChronology.INSTANCE);
        Partial partial2 = partial1.withFieldAddWrapped(DateTimeFieldType.instance(DateTimeFields.HOURS), 2);
        assertEquals(partial1.size(), partial2.size());
        assertEquals(partial1.getChronology(), partial2.getChronology());
        assertEquals(partial1.getFieldTypes(), partial2.getFieldTypes());
        assertEquals(partial1.getValue(0) + 2 * 60 * 60 * 1000, partial2.getValue(0));
    }

    @Test
    public void testPartialWithPeriodAdded() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.instance(DateTimeFields.MILLI_SECONDS);
        ReadablePeriod readablePeriod = Period.hours(1).withMillisAdded(500);
        Partial partial1 = new Partial(dateTimeFieldType, 123456, ISOChronology.INSTANCE);
        Partial partial2 = partial1.withPeriodAdded(readablePeriod, 1);
        assertEquals(partial1.size(), partial2.size());
        assertEquals(partial1.getChronology(), partial2.getChronology());
        assertEquals(partial1.getFieldTypes(), partial2.getFieldTypes());
        assertEquals(partial1.getValue(0) + 1 * 60 * 60 * 1000 + 500, partial2.getValue(0));
    }

    @Test
    public void testPartialPlus() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.instance(DateTimeFields.MILLI_SECONDS);
        ReadablePeriod readablePeriod = Period.hours(1).withMillisAdded(500);
        Partial partial1 = new Partial(dateTimeFieldType, 123456, ISOChronology.INSTANCE);
        Partial partial2 = partial1.plus(readablePeriod);
        assertEquals(partial1.size(), partial2.size());
        assertEquals(partial1.getChronology(), partial2.getChronology());
        assertEquals(partial1.getFieldTypes(), partial2.getFieldTypes());
        assertEquals(partial1.getValue(0) + 1 * 60 * 60 * 1000 + 500, partial2.getValue(0));
    }

    @Test
    public void testPartialMinus() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.instance(DateTimeFields.MILLI_SECONDS);
        ReadablePeriod readablePeriod = Period.hours(1).withMillisAdded(500);
        Partial partial1 = new Partial(dateTimeFieldType, 123567, ISOChronology.INSTANCE);
        Partial partial2 = partial1.minus(readablePeriod);
        assertEquals(partial1.size(), partial2.size());
        assertEquals(partial1.getChronology(), partial2.getChronology());
        assertEquals(partial1.getFieldTypes(), partial2.getFieldTypes());
        assertEquals(partial1.getValue(0) - 1 * 60 * 60 * 1000 - 500, partial2.getValue(0));
    }

    @Test
    public void testPartialProperty() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.instance(DateTimeFields.MILLI_SECONDS);
        Partial partial = new Partial(dateTimeFieldType, 123456, ISOChronology.INSTANCE);
        Property property = partial.property(DateTimeFieldType.instance(DateTimeFields.MILLI_SECONDS));
        assertEquals(partial, property.getPartial());
        assertEquals(DateTimeFieldType.instance(DateTimeFields.MILLI_SECONDS), property.getField());
        assertEquals(123456, property.get());
    }

    @Test
    public void testPartialIsMatch() {
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.instance(DateTimeFields.MILLI_SECONDS);
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.instance(DateTimeFields.SECONDS);
        Partial partial1 = new Partial(new DateTimeFieldType[] { dateTimeFieldType1 }, new int[] { 123456 });
        Instant instant = new DateTime(123456)
                .toInstant()
                .withChronology(ISOChronology.INSTANCE);
        Instant instant2 = new DateTime(123455)
                .toInstant()
                .withChronology(ISOChronology.INSTANCE);
        assertTrue(partial1.isMatch(instant));
        assertFalse(partial1.isMatch(instant2));
    }

    @Test
    public void testPartialIsMatchWithReadablePartial() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.instance(DateTimeFields.MILLI_SECONDS);
        Partial partial1 = new Partial(new DateTimeFieldType[] { dateTimeFieldType }, new int[] { 123456 });
        Partial partial2 = new Partial(new DateTimeFieldType[] { dateTimeFieldType }, new int[] { 123456 });
        assertTrue(partial1.isMatch(partial2));
    }

    @Test
    public void testPartialGetFormatter() {
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.instance(DateTimeFields.MILLI_SECONDS);
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.instance(DateTimeFields.SECONDS);
        Partial partial = new Partial(new DateTimeFieldType[] { dateTimeFieldType1, dateTimeFieldType2 }, new int[] { 123456, 7 });
        DateTimeFormatter formatter = partial.getFormatter();
        assertNotNull(formatter);
    }

    @Test
    public void testPartialToString() {
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.instance(DateTimeFields.MILLI_SECONDS);
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.instance(DateTimeFields.SECONDS);
        Partial partial = new Partial(new DateTimeFieldType[] { dateTimeFieldType1, dateTimeFieldType2 }, new int[] { 123456, 7 });
        assertEquals("[milliseconds=123456, seconds=7]", partial.toString());
    }

    @Test
    public void testPartialToStringList() {
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.instance(DateTimeFields.MILLI_SECONDS);
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.instance(DateTimeFields.SECONDS);
        Partial partial = new Partial(new DateTimeFieldType[] { dateTimeFieldType1, dateTimeFieldType2 }, new int[] { 123456, 7 });
        assertEquals("[milliseconds=123456, seconds=7]", partial.toStringList());
    }

    @Test
    public void testPartialToStringWithPattern() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.instance(DateTimeFields.MILLI_SECONDS);
        Partial partial = new Partial(dateTimeFieldType, 123456, ISOChronology.INSTANCE);
        assertEquals("2001-12-31T12:34:56.123Z", partial.toString("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
    }

    @Test
    public void testPartialToStringWithLocale() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.instance(DateTimeFields.MILLI_SECONDS);
        Partial partial = new Partial(dateTimeFieldType, 123456, ISOChronology.INSTANCE);
        assertEquals("2001-12-31 12:34:56.123", partial.toString("yyyy-MM-dd HH:mm:ss.SSS"));
    }
}