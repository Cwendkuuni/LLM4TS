package org.junit.joda.time.test;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.format.*;
import org.joda.time.field.*;

public class PartialTest {

    @org.junit.Test
    public void testEmptyPartial() {
        Partial emptyPartial = new Partial();
        assertEquals(0, emptyPartial.size());
        assertNull(emptyPartial.getChronology());
        assertArrayEquals(new int[0], emptyPartial.getValues());
        assertArrayEquals(new DateTimeFieldType[0], emptyPartial.getFieldTypes());
    }

    @org.junit.Test
    public void testPartialWithSingleField() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType fieldType = ISODateField.DAY_OF_MONTH;
        Partial partial = new Partial(fieldType, 31, chronology);
        assertEquals(1, partial.size());
        assertEquals(chronology, partial.getChronology());
        assertEquals(fieldType, partial.getFieldTypes()[0]);
        assertEquals(31, partial.getValues()[0]);
    }

    @org.junit.Test
    public void testPartialWithMultipleFields() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType fieldType1 = ISODateField.DAY_OF_MONTH;
        DateTimeFieldType fieldType2 = ISOMonthField.MONTH_OF_YEAR;
        Partial partial = new Partial(new DateTimeFieldType[] { fieldType1, fieldType2 }, new int[] { 31, 2 }, chronology);
        assertEquals(2, partial.size());
        assertEquals(chronology, partial.getChronology());
        assertEquals(fieldType1, partial.getFieldTypes()[0]);
        assertEquals(fieldType2, partial.getFieldTypes()[1]);
        assertEquals(31, partial.getValues()[0]);
        assertEquals(2, partial.getValues()[1]);
    }

    @org.junit.Test
    public void testPartialWithInvalidArguments() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType fieldType = ISODateField.DAY_OF_MONTH;
        try {
            new Partial(null, 31, chronology);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new Partial(fieldType, null, chronology);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new Partial(fieldType, 31, null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new Partial(new DateTimeFieldType[0], new int[1]);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new Partial(new DateTimeFieldType[0], new int[0], chronology);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testClone() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType fieldType = ISODateField.DAY_OF_MONTH;
        Partial partial = new Partial(fieldType, 31, chronology);
        Partial clonedPartial = new Partial(partial);
        assertEquals(partial, clonedPartial);
        assertEquals(chronology, clonedPartial.getChronology());
        assertEquals(fieldType, clonedPartial.getFieldTypes()[0]);
        assertEquals(31, clonedPartial.getValues()[0]);
    }

    @org.junit.Test
    public void testSize() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType fieldType1 = ISODateField.DAY_OF_MONTH;
        DateTimeFieldType fieldType2 = ISOMonthField.MONTH_OF_YEAR;
        Partial partial = new Partial(new DateTimeFieldType[] { fieldType1, fieldType2 }, new int[] { 31, 2 }, chronology);
        assertEquals(2, partial.size());
    }

    @org.junit.Test
    public void testGetChronology() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType fieldType = ISODateField.DAY_OF_MONTH;
        Partial partial = new Partial(fieldType, 31, chronology);
        assertEquals(chronology, partial.getChronology());
    }

    @org.junit.Test
    public void testGetField() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType fieldType = ISODateField.DAY_OF_MONTH;
        Partial partial = new Partial(fieldType, 31, chronology);
        assertEquals(fieldType, partial.getField(0));
    }

    @org.junit.Test
    public void testGetFieldTypes() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType fieldType1 = ISODateField.DAY_OF_MONTH;
        DateTimeFieldType fieldType2 = ISOMonthField.MONTH_OF_YEAR;
        Partial partial = new Partial(new DateTimeFieldType[] { fieldType1, fieldType2 }, new int[] { 31, 2 }, chronology);
        assertEquals(fieldType1, partial.getFieldTypes()[0]);
        assertEquals(fieldType2, partial.getFieldTypes()[1]);
    }

    @org.junit.Test
    public void testGetValues() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType fieldType = ISODateField.DAY_OF_MONTH;
        Partial partial = new Partial(fieldType, 31, chronology);
        assertArrayEquals(new int[] { 31 }, partial.getValues());
    }

    @org.junit.Test
    public void testWithChronologyRetainFields() {
        Chronology chronology1 = GregorianChronology.getInstance();
        Chronology chronology2 = ISOWeeksChronology.getInstance();
        DateTimeFieldType fieldType = ISODateField.DAY_OF_MONTH;
        Partial partial = new Partial(fieldType, 31, chronology1);
        Partial newPartial = partial.withChronologyRetainFields(chronology2);
        assertEquals(chronology2, newPartial.getChronology());
        assertEquals(partial.getFieldTypes(), newPartial.getFieldTypes());
        assertEquals(partial.getValues(), newPartial.getValues());
    }

    @org.junit.Test
    public void testWith() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType fieldType = ISODateField.DAY_OF_MONTH;
        Partial partial = new Partial(fieldType, 31, chronology);
        Partial newPartial = partial.with(fieldType, 32);
        assertEquals(2, newPartial.size());
        assertArrayEquals(new int[] { 31, 32 }, newPartial.getValues());
        assertEquals(fieldType, newPartial.getFieldTypes()[0]);
        assertEquals(fieldType, newPartial.getFieldTypes()[1]);
    }

    @org.junit.Test
    public void testWithout() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType fieldType = ISODateField.DAY_OF_MONTH;
        Partial partial = new Partial(fieldType, 31, chronology);
        Partial newPartial = partial.without(fieldType);
        assertEquals(0, newPartial.size());
    }

    @org.junit.Test
    public void testWithField() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType fieldType = ISODateField.DAY_OF_MONTH;
        Partial partial = new Partial(fieldType, 31, chronology);
        Partial newPartial = partial.withField(fieldType, 32);
        assertEquals(2, newPartial.size());
        assertArrayEquals(new int[] { 31, 32 }, newPartial.getValues());
    }

    @org.junit.Test
    public void testWithFieldAdded() {
        Chronology chronology = GregorianChronology.getInstance();
        DurationFieldType durationFieldType = Days.ONE;
        Partial partial = new Partial();
        Partial newPartial = partial.withFieldAdded(durationFieldType, 1);
        assertEquals(1, newPartial.size());
        assertEquals(durationFieldType, newPartial.getFieldTypes()[0]);
        assertEquals(1, newPartial.getValues()[0]);
    }

    @org.junit.Test
    public void testWithFieldAddWrapped() {
        Chronology chronology = GregorianChronology.getInstance();
        DurationFieldType durationFieldType = Days.ONE;
        Partial partial = new Partial();
        Partial newPartial = partial.withFieldAddWrapped(durationFieldType, 1);
        assertEquals(1, newPartial.size());
        assertEquals(durationFieldType, newPartial.getFieldTypes()[0]);
        assertEquals(1, newPartial.getValues()[0]);
    }

    @org.junit.Test
    public void testWithPeriodAdded() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType fieldType = ISODateField.DAY_OF_MONTH;
        Partial partial = new Partial(fieldType, 31, chronology);
        ReadablePeriod period = new GeneralInstant(3600000, chronology).plus(Period.days(1));
        Partial newPartial = partial.withPeriodAdded(period, 1);
        assertEquals(2, newPartial.size());
        assertArrayEquals(new int[] { 31, 32 }, newPartial.getValues());
        assertEquals(fieldType, newPartial.getFieldTypes()[0]);
    }

    @org.junit.Test
    public void testPlus() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType fieldType = ISODateField.DAY_OF_MONTH;
        Partial partial = new Partial(fieldType, 31, chronology);
        ReadablePeriod period = Period.days(1);
        Partial newPartial = partial.plus(period);
        assertEquals(2, newPartial.size());
        assertArrayEquals(new int[] { 31, 32 }, newPartial.getValues());
        assertEquals(fieldType, newPartial.getFieldTypes()[0]);
    }

    @org.junit.Test
    public void testMinus() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType fieldType = ISODateField.DAY_OF_MONTH;
        Partial partial = new Partial(fieldType, 32, chronology);
        ReadablePeriod period = Period.days(1);
        Partial newPartial = partial.minus(period);
        assertEquals(2, newPartial.size());
        assertArrayEquals(new int[] { 31, 31 }, newPartial.getValues());
        assertEquals(fieldType, newPartial.getFieldTypes()[0]);
    }

    @org.junit.Test
    public void testIsMatch() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType fieldType = ISODateField.DAY_OF_MONTH;
        Partial partial = new Partial(fieldType, 31, chronology);
        Instant instant = new DateTime(2019, 1, 31, 0, 0, 0, 0, chronology).toInstant();
        assertTrue(partial.isMatch(instant));
    }

    @org.junit.Test
    public void testIsMatchWithDifferentChronology() {
        Chronology chronology1 = GregorianChronology.getInstance();
        Chronology chronology2 = ISOWeeksChronology.getInstance();
        DateTimeFieldType fieldType = ISODateField.DAY_OF_MONTH;
        Partial partial = new Partial(fieldType, 31, chronology1);
        Instant instant = new DateTime(2019, 1, 31, 0, 0, 0, 0, chronology2).toInstant();
        assertFalse(partial.isMatch(instant));
    }

    @org.junit.Test
    public void testIsMatchWithPartial() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType fieldType1 = ISODateField.DAY_OF_MONTH;
        DateTimeFieldType fieldType2 = ISOMonthField.MONTH_OF_YEAR;
        Partial partial1 = new Partial(new DateTimeFieldType[] { fieldType1, fieldType2 }, new int[] { 31, 1 }, chronology);
        Partial partial2 = new Partial(new DateTimeFieldType[] { fieldType1, fieldType2 }, new int[] { 31, 2 }, chronology);
        assertTrue(partial1.isMatch(partial2));
    }

    @org.junit.Test
    public void testGetFormatter() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType fieldType1 = ISODateField.DAY_OF_MONTH;
        DateTimeFieldType fieldType2 = ISOMonthField.MONTH_OF_YEAR;
        Partial partial = new Partial(new DateTimeFieldType[] { fieldType1, fieldType2 }, new int[] { 31, 1 }, chronology);
        DateTimeFormatter formatter = partial.getFormatter();
        assertNotNull(formatter);
    }

    @org.junit.Test
    public void testToString() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType fieldType = ISODateField.DAY_OF_MONTH;
        Partial partial = new Partial(fieldType, 31, chronology);
        assertEquals("[dayOfMonth=31]", partial.toString());
    }

    @org.junit.Test
    public void testToStringList() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType fieldType1 = ISODateField.DAY_OF_MONTH;
        DateTimeFieldType fieldType2 = ISOMonthField.MONTH_OF_YEAR;
        Partial partial = new Partial(new DateTimeFieldType[] { fieldType1, fieldType2 }, new int[] { 31, 1 }, chronology);
        assertEquals("[dayOfMonth=31, monthOfYear=1]", partial.toStringList());
    }

    @org.junit.Test
    public void testToStringWithCustomFormat() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType fieldType = ISODateField.DAY_OF_MONTH;
        Partial partial = new Partial(fieldType, 31, chronology);
        assertEquals("dayOfMonth=31", partial.toString("dayOfMonth= %d"));
    }

    @org.junit.Test
    public void testToStringWithLocale() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType fieldType = ISODateField.DAY_OF_MONTH;
        Partial partial = new Partial(fieldType, 31, chronology);
        assertEquals("dayOfMonth = '31'", partial.toString("dayOfMonth = '%1$'d'", Locale.ENGLISH));
    }
}