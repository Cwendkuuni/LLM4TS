package org.joda.time.test;

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
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNullChronology() {
        new Partial(null);
    }

    @org.junit.Test
    public void testPartialWithChronology() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(chronology);
        assertEquals(0, partial.size());
        assertEquals(chronology, partial.getChronology());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNullDateTimeFieldType() {
        Chronology chronology = DateTimeUtils.forUTC();
        new Partial(chronology, null, null);
    }

    @org.junit.Test
    public void testPartialWithDateTimeFieldType() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.dayOfWeek();
        Partial partial = new Partial(dateTimeFieldType, 1, chronology);
        assertEquals(1, partial.size());
        assertEquals(chronology, partial.getChronology());
        assertEquals(dateTimeFieldType, partial.getFieldType(0));
        assertEquals(1, partial.getValue(0));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNullChronologyWithUTC() {
        new Partial(null, null);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNullTypesArray() {
        Chronology chronology = DateTimeUtils.forUTC();
        new Partial(chronology, null, null);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNullValuesArray() {
        Chronology chronology = DateTimeUtils.forUTC();
        new Partial(chronology, new DateTimeFieldType[] { DateTimeFieldType.dayOfWeek() }, null);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testValuesArrayNotSameLengthAsTypesArray() {
        Chronology chronology = DateTimeUtils.forUTC();
        new Partial(chronology, new DateTimeFieldType[] { DateTimeFieldType.dayOfWeek(), DateTimeFieldType.year() }, new int[] { 1, 2, 3 });
    }

    @org.junit.Test
    public void testPartialWithTypesAndValues() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.dayOfWeek();
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.year();
        Partial partial = new Partial(new DateTimeFieldType[] { dateTimeFieldType1, dateTimeFieldType2 }, new int[] { 1, 2005 }, chronology);
        assertEquals(2, partial.size());
        assertEquals(chronology, partial.getChronology());
        assertEquals(dateTimeFieldType1, partial.getFieldType(0));
        assertEquals(1, partial.getValue(0));
        assertEquals(dateTimeFieldType2, partial.getFieldType(1));
        assertEquals(2005, partial.getValue(1));
    }

    @org.junit.Test
    public void testPartialWithReadablePartial() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.dayOfWeek();
        Partial partialWithTypesAndValues = new Partial(new DateTimeFieldType[] { dateTimeFieldType }, new int[] { 1 }, chronology);
        Partial partialWithReadablePartial = new Partial(partialWithTypesAndValues);
        assertEquals(1, partialWithReadablePartial.size());
        assertEquals(chronology, partialWithReadablePartial.getChronology());
        assertEquals(dateTimeFieldType, partialWithReadablePartial.getFieldType(0));
        assertEquals(1, partialWithReadablePartial.getValue(0));
    }

    @org.junit.Test
    public void testPartialCopyConstructor() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.dayOfWeek();
        Partial originalPartial = new Partial(new DateTimeFieldType[] { dateTimeFieldType }, new int[] { 1 }, chronology);
        Partial copyPartial = new Partial(originalPartial, originalPartial.getValues());
        assertEquals(originalPartial, copyPartial);
    }

    @org.junit.Test
    public void testPartialConstructorWithChronologyTypesAndValues() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.dayOfWeek();
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.year();
        Partial partial = new Partial(chronology, new DateTimeFieldType[] { dateTimeFieldType1, dateTimeFieldType2 }, new int[] { 1, 2005 });
        assertEquals(2, partial.size());
        assertEquals(chronology, partial.getChronology());
        assertEquals(dateTimeFieldType1, partial.getFieldType(0));
        assertEquals(1, partial.getValue(0));
        assertEquals(dateTimeFieldType2, partial.getFieldType(1));
        assertEquals(2005, partial.getValue(1));
    }

    @org.junit.Test
    public void testGetChronology() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(new DateTimeFieldType[] { DateTimeFieldType.dayOfWeek() }, new int[] { 1 }, chronology);
        assertEquals(chronology, partial.getChronology());
    }

    @org.junit.Test
    public void testSize() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(new DateTimeFieldType[] { DateTimeFieldType.dayOfWeek(), DateTimeFieldType.year() }, new int[] { 1, 2005 }, chronology);
        assertEquals(2, partial.size());
    }

    @org.junit.Test
    public void testGetFieldType() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(new DateTimeFieldType[] { DateTimeFieldType.dayOfWeek(), DateTimeFieldType.year() }, new int[] { 1, 2005 }, chronology);
        assertEquals(DateTimeFieldType.dayOfWeek(), partial.getFieldType(0));
        assertEquals(DateTimeFieldType.year(), partial.getFieldType(1));
    }

    @org.junit.Test
    public void testGetField() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(new DateTimeFieldType[] { DateTimeFieldType.dayOfWeek(), DateTimeFieldType.year() }, new int[] { 1, 2005 }, chronology);
        DateTimeField dayOfWeekField = DateTimeField.weekdayOfWeek(chronology);
        DateTimeField yearField = DateTimeField.year(chronology);
        assertEquals(dayOfWeekField, partial.getField(0));
        assertEquals(yearField, partial.getField(1));
    }

    @org.junit.Test
    public void testGetFieldTypes() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(new DateTimeFieldType[] { DateTimeFieldType.dayOfWeek(), DateTimeFieldType.year() }, new int[] { 1, 2005 }, chronology);
        DateTimeFieldType[] fieldTypes = partial.getFieldTypes();
        assertEquals(2, fieldTypes.length);
        assertEquals(DateTimeFieldType.dayOfWeek(), fieldTypes[0]);
        assertEquals(DateTimeFieldType.year(), fieldTypes[1]);
    }

    @org.junit.Test
    public void testToString() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(new DateTimeFieldType[] { DateTimeFieldType.dayOfWeek(), DateTimeFieldType.year() }, new int[] { 1, 2005 }, chronology);
        assertEquals("[dayOfWeek=1 year=2005]", partial.toString());
    }

    @org.junit.Test
    public void testToStringList() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(new DateTimeFieldType[] { DateTimeFieldType.dayOfWeek(), DateTimeFieldType.year() }, new int[] { 1, 2005 }, chronology);
        assertEquals("[dayOfWeek=1 year=2005]", partial.toStringList());
    }

    @org.junit.Test
    public void testToStringWithFormat() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(new DateTimeFieldType[] { DateTimeFieldType.dayOfWeek(), DateTimeFieldType.year() }, new int[] { 1, 2005 }, chronology);
        assertEquals("dayOfWeek=1 year=2005", partial.toString("dayOfWeek=y MM/dd/yyyy 'at' HH:mm:ss.SSS z"));
    }

    @org.junit.Test
    public void testToStringWithLocale() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(new DateTimeFieldType[] { DateTimeFieldType.dayOfWeek(), DateTimeFieldType.year() }, new int[] { 1, 2005 }, chronology);
        assertEquals("dayOfWeek=1 year=2005", partial.toString("dd 'of' MMMM yyyy", Locale.US));
    }

    @org.junit.Test
    public void testGetFormatter() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(new DateTimeFieldType[] { DateTimeFieldType.dayOfWeek(), DateTimeFieldType.year() }, new int[] { 1, 2005 }, chronology);
        DateTimeFormatter formatter = partial.getFormatter();
        assertTrue(formatter != null);
    }

    @org.junit.Test
    public void testIsMatchWithInstant() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(new DateTimeFieldType[] { DateTimeFieldType.dayOfWeek(), DateTimeFieldType.year() }, new int[] { 1, 2005 }, chronology);
        Instant instant = new DateTime(2005, 6, 1, 0, 0, 0, 0, chronology).toInstant();
        assertTrue(partial.isMatch(new DateTime(instant)));
    }

    @org.junit.Test
    public void testIsMatchWithPartial() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial1 = new Partial(new DateTimeFieldType[] { DateTimeFieldType.dayOfWeek(), DateTimeFieldType.year() }, new int[] { 1, 2005 }, chronology);
        Partial partial2 = new Partial(new DateTimeFieldType[] { DateTimeFieldType.dayOfWeek(), DateTimeFieldType.dayOfMonth() }, new int[] { 1, 31 }, chronology);
        Partial partial3 = new Partial(new DateTimeFieldType[] { DateTimeFieldType.dayOfWeek(), DateTimeFieldType.year() }, new int[] { 1, 2006 }, chronology);
        assertTrue(partial1.isMatch(partial1));
        assertTrue(partial1.isMatch(partial2));
        assertFalse(partial1.isMatch(partial3));
    }

}