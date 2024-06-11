package org.junit.test;

import org.joda.time.*;
import org.joda.time.base.Defaults;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class PartialTest {

    @Test
    public void testEmptyPartial() {
        Partial emptyPartial = new Partial();
        assertEquals(0, emptyPartial.size());
        assertNull(emptyPartial.getChronology());
        assertTrue(emptyPartial.isMatch(null));
    }

    @Test
    public void testBasicPartialConstruction() {
        Chronology chrono = DateTimeUtils.forUTC();
        DateTimeFieldType dateType = DateTimeFieldType.dayOfMonth();
        Partial partial = new Partial(dateType, 1, chrono);
        assertEquals(1, partial.size());
        assertEquals(dateType, partial.getFieldType(0));
        assertEquals(1, partial.getValue(0));
    }

    @Test
    public void testBasicPartialWithUTCChronology() {
        Partial partial = new Partial(DateTimeFieldType.hourOfDay(), 11, null);
        assertEquals(1, partial.size());
        assertEquals(DateTimeFieldType.hourOfDay(), partial.getFieldType(0));
        assertEquals(11, partial.getValue(0));
    }

    @Test
    public void testPartialWithInvalidFieldType() {
        Chronology chrono = DateTimeUtils.forUTC();
        DateTimeFieldType invalidFieldType = null;
        try {
            new Partial(invalidFieldType, 1, chrono);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPartialWithInvalidValue() {
        Chronology chrono = DateTimeUtils.forUTC();
        DateTimeFieldType dateType = DateTimeFieldType.dayOfMonth();
        try {
            new Partial(dateType, 33, chrono);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPartialWithNullChronology() {
        DateTimeFieldType dateType = DateTimeFieldType.dayOfMonth();
        try {
            new Partial(dateType, 1, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPartialWithFieldTypesAndValues() {
        Chronology chrono = DateTimeUtils.forUTC();
        DateTimeFieldType dateType1 = DateTimeFieldType.dayOfMonth();
        DateTimeFieldType dateType2 = DateTimeFieldType.monthOfYear();
        int[] values = {31, 12};
        Partial partial = new Partial(new DateTimeFieldType[] {dateType1, dateType2}, values, chrono);
        assertEquals(2, partial.size());
        assertEquals(dateType1, partial.getFieldType(0));
        assertEquals(31, partial.getValue(0));
        assertEquals(dateType2, partial.getFieldType(1));
        assertEquals(12, partial.getValue(1));
    }

    @Test
    public void testPartialWithReadablePartial() {
        Chronology chrono = DateTimeUtils.forUTC();
        DateTimeFieldType dateType = DateTimeFieldType.dayOfMonth();
        ReadablePartial readablePartial = new Partial(dateType, 31, chrono);
        Partial partial = new Partial(readablePartial);
        assertEquals(1, partial.size());
        assertEquals(dateType, partial.getFieldType(0));
        assertEquals(31, partial.getValue(0));
    }

    @Test
    public void testPartialWithCopy() {
        Chronology chrono = DateTimeUtils.forUTC();
        DateTimeFieldType dateType = DateTimeFieldType.dayOfMonth();
        Partial partial = new Partial(dateType, 31, chrono);
        Partial copy = new Partial(partial, partial.getValues().clone());
        assertEquals(1, copy.size());
        assertEquals(dateType, copy.getFieldType(0));
        assertEquals(31, copy.getValue(0));
    }

    @Test
    public void testPartialWithChronologyRetainFields() {
        Partial original = new Partial(DateTimeFieldType.hourOfDay(), 11, null);
        Chronology newChrono = DateTimeUtils.forDate(new Date());
        Partial newPartial = original.withChronologyRetainFields(newChrono);
        assertNotSame(original, newPartial);
        assertEquals(1, newPartial.size());
        assertEquals(DateTimeFieldType.hourOfDay(), newPartial.getFieldType(0));
        assertEquals(11, newPartial.getValue(0));
        assertEquals(newChrono, newPartial.getChronology());
    }

    @Test
    public void testPartialWithField() {
        Chronology chrono = DateTimeUtils.forUTC();
        DateTimeFieldType dateType = DateTimeFieldType.dayOfMonth();
        Partial partial = new Partial(chrono, new DateTimeFieldType[] {dateType}, new int[] {1});
        assertEquals(1, partial.size());
        assertEquals(dateType, partial.getFieldType(0));
        assertEquals(1, partial.getValue(0));
        Partial updatedPartial = partial.withField(dateType, 31);
        assertEquals(1, updatedPartial.size());
        assertEquals(dateType, updatedPartial.getFieldType(0));
        assertEquals(31, updatedPartial.getValue(0));
    }

    @Test
    public void testPartialWithFieldAdded() {
        Partial partial = new Partial(DateTimeFieldType.dayOfMonth(), 31, null);
        Partial updatedPartial = partial.withFieldAdded(DateTimeFieldType.minuteOfHour(), 30);
        assertEquals(2, updatedPartial.size());
        assertEquals(DateTimeFieldType.dayOfMonth(), updatedPartial.getFieldType(0));
        assertEquals(31, updatedPartial.getValue(0));
        assertEquals(DateTimeFieldType.minuteOfHour(), updatedPartial.getFieldType(1));
        assertEquals(30, updatedPartial.getValue(1));
    }

    @Test
    public void testPartialWithFieldAddWrapped() {
        Partial partial = new Partial(DateTimeFieldType.dayOfMonth(), 31, null);
        Partial updatedPartial = partial.withFieldAddWrapped(DateTimeFieldType.minuteOfHour(), 120);
        assertEquals(2, updatedPartial.size());
        assertEquals(DateTimeFieldType.dayOfMonth(), updatedPartial.getFieldType(0));
        assertEquals(31, updatedPartial.getValue(0));
        assertEquals(DateTimeFieldType.minuteOfHour(), updatedPartial.getFieldType(1));
        assertEquals(2, updatedPartial.getValue(1));
    }

    @Test
    public void testPartialWithPeriodAdded() {
        ReadablePeriod period = new GeneralDate().plus(Duration.standardDays(5));
        Partial partial = new Partial(DateTimeFieldType.dayOfMonth(), 1, null);
        Partial updatedPartial = partial.withPeriodAdded(period, 1);
        assertEquals(2, updatedPartial.size());
        assertEquals(DateTimeFieldType.dayOfMonth(), updatedPartial.getFieldType(0));
        assertEquals(1, updatedPartial.getValue(0));
        assertEquals(DateTimeFieldType.dayOfMonth(), updatedPartial.getFieldType(1));
        assertEquals(6, updatedPartial.getValue(1));
    }

    @Test
    public void testPartialWithAdditionAndSubtraction() {
        Partial partial = new Partial(DateTimeFieldType.dayOfMonth(), 31, null);
        Partial addedPartial = partial.plus(Duration.standardDays(5));
        Partial subtractedPartial = addedPartial.minus(Duration.standardDays(3));
        assertEquals(3, partial.size());
        assertEquals(DateTimeFieldType.dayOfMonth(), partial.getFieldType(0));
        assertEquals(31, partial.getValue(0));
        assertEquals(DateTimeFieldType.dayOfMonth(), partial.getFieldType(1));
        assertEquals(30, partial.getValue(1));
        assertEquals(DateTimeFieldType.dayOfMonth(), partial.getFieldType(2));
        assertEquals(28, partial.getValue(2));
        assertEquals(3, addedPartial.size());
        assertEquals(DateTimeFieldType.dayOfMonth(), addedPartial.getFieldType(0));
        assertEquals(36, addedPartial.getValue(0));
        assertEquals(DateTimeFieldType.dayOfMonth(), addedPartial.getFieldType(1));
        assertEquals(36, addedPartial.getValue(1));
        assertEquals(DateTimeFieldType.dayOfMonth(), addedPartial.getFieldType(2));
        assertEquals(33, addedPartial.getValue(2));
        assertEquals(2, subtractedPartial.size());
        assertEquals(DateTimeFieldType.dayOfMonth(), subtractedPartial.getFieldType(0));
        assertEquals(34, subtractedPartial.getValue(0));
        assertEquals(DateTimeFieldType.dayOfMonth(), subtractedPartial.getFieldType(1));
        assertEquals(34, subtractedPartial.getValue(1));
    }

    @Test
    public void testProperty() {
        Partial partial = new Partial(DateTimeFieldType.dayOfMonth(), 31, null);
        Property property = new Property(partial, 0);
        assertEquals(DateTimeFieldType.dayOfMonth(), property.getField());
        assertEquals(31, property.get());
        Partial updatedPartial = property.set(5);
        assertEquals(1, updatedPartial.size());
        assertEquals(DateTimeFieldType.dayOfMonth(), updatedPartial.getFieldType(0));
        assertEquals(5, updatedPartial.getValue(0));
    }

    @Test
    public void testToString() {
        Partial partial = new Partial(DateTimeFieldType.dayOfMonth(), 31, null);
        String toString = partial.toString();
        String expected = "[dayOfMonth=31]";
        assertEquals(expected, toString);
    }

    @Test
    public void testToStringList() {
        Partial partial = new Partial(DateTimeFieldType.dayOfMonth(), 31, null);
        String toStringList = partial.toStringList();
        String expected = "[dayOfMonth=31]";
        assertEquals(expected, toStringList);
    }

    @Test
    public void testToStringWithCustomPattern() {
        Partial partial = new Partial(DateTimeFieldType.dayOfMonth(), 31, null);
        String toString = partial.toString("dd");
        String expected = "31";
        assertEquals(expected, toString);
    }

    @Test
    public void testToStringWithLocale() {
        Partial partial = new Partial(DateTimeFieldType.dayOfMonth(), 31, null);
        String toString = partial.toString("dd", Defaults.US_LOCALE);
        String expected = "31";
        assertEquals(expected, toString);
    }

    @Test
    public void testToStringWithInvalidFormat() {
        Partial partial = new Partial(DateTimeFieldType.dayOfMonth(), 31, null);
        String toString = partial.toString("ddd");
        assertTrue(toString.isEmpty());
    }
}