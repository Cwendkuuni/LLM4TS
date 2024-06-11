package org.joda.time;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.field.*;
import org.joda.time.format.*;
import java.util.*;

public class PartialTest {

    @Test
    public void testEmptyPartial() {
        Partial partial = new Partial();
        assertEquals(0, partial.size());
        assertNull(partial.getChronology());
    }

    @Test
    public void testPartialWithChronology() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(chronology);
        assertEquals(0, partial.size());
        assertEquals(chronology, partial.getChronology());
    }

    @Test
    public void testPartialWithOneFieldType() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Partial partial = new Partial(dateTimeFieldType, 2022, chronology);
        assertEquals(1, partial.size());
        assertEquals(dateTimeFieldType, partial.getFieldType(0));
        assertEquals(2022, partial.getValue(0));
        assertEquals(chronology, partial.getChronology());
    }

    @Test
    public void testPartialWithMultipleFieldTypes() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.year();
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.monthOfYear();
        int[] values = new int[]{2022, 1};
        Partial partial = new Partial(new DateTimeFieldType[]{dateTimeFieldType1, dateTimeFieldType2}, values, chronology);
        assertEquals(2, partial.size());
        assertEquals(dateTimeFieldType1, partial.getFieldType(0));
        assertEquals(2022, partial.getValue(0));
        assertEquals(dateTimeFieldType2, partial.getFieldType(1));
        assertEquals(1, partial.getValue(1));
        assertEquals(chronology, partial.getChronology());
    }

    @Test
    public void testPartialWithInvalidFieldTypes() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.year();
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.dayOfMonth();
        int[] values = new int[]{2022, 33};
        try {
            new Partial(new DateTimeFieldType[]{dateTimeFieldType1, dateTimeFieldType2}, values, chronology);
            fail("should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Values array must be in order largest-smallest: " + dateTimeFieldType1.getName() + " < " + dateTimeFieldType2.getName(), e.getMessage());
        }
    }

    @Test
    public void testPartialWithInvalidFieldTypes2() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.year();
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.dayOfMonth();
        DateTimeFieldType dateTimeFieldType3 = DateTimeFieldType.hourOfDay();
        int[] values = new int[]{2022, 32, 25};
        try {
            new Partial(new DateTimeFieldType[]{dateTimeFieldType1, dateTimeFieldType2, dateTimeFieldType3}, values, chronology);
            fail("should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Types array must be in order largest-smallest: " + dateTimeFieldType2.getName() + " < " + dateTimeFieldType3.getName(), e.getMessage());
        }
    }

    @Test
    public void testPartialWithNullFieldTypes() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType[] fieldTypes = null;
        int[] values = new int[]{2022, 1};
        try {
            new Partial(fieldTypes, values, chronology);
            fail("should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Types array must not be null", e.getMessage());
        }
    }

    @Test
    public void testPartialWithNullValues() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType[] fieldTypes = new DateTimeFieldType[]{DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()};
        int[] values = null;
        try {
            new Partial(fieldTypes, values, chronology);
            fail("should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Values array must not be null", e.getMessage());
        }
    }

    @Test
    public void testGetFormatter() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType[] fieldTypes = new DateTimeFieldType[]{DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()};
        Partial partial = new Partial(fieldTypes, new int[]{2022, 1}, chronology);
        DateTimeFormatter formatter = partial.getFormatter();
        assertNotNull(formatter);
    }

    @Test
    public void testToString() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType[] fieldTypes = new DateTimeFieldType[]{DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()};
        Partial partial = new Partial(fieldTypes, new int[]{2022, 1}, chronology);
        String expected = "[year=2022, monthOfYear=1]";
        assertEquals(expected, partial.toString());
    }

    @Test
    public void testToStringList() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType[] fieldTypes = new DateTimeFieldType[]{DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()};
        Partial partial = new Partial(fieldTypes, new int[]{2022, 1}, chronology);
        String expected = "[year=2022, monthOfYear=1]";
        assertEquals(expected, partial.toStringList());
    }

    @Test
    public void testToStringWithPattern() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType[] fieldTypes = new DateTimeFieldType[]{DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()};
        Partial partial = new Partial(fieldTypes, new int[]{2022, 1}, chronology);
        String expected = "yyyy-MM";
        assertEquals(expected, partial.toString(expected));
    }

    @Test
    public void testToStringWithLocale() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType[] fieldTypes = new DateTimeFieldType[]{DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()};
        Partial partial = new Partial(fieldTypes, new int[]{2022, 1}, chronology);
        String expected = "yyyy-MM";
        assertEquals(expected, partial.toString("yyyy-MM", Locale.US));
    }

    @Test
    public void testIsMatch() {
        Instant instant = new DateTime("2022-01-01T00:00:00.000Z").toInstant();
        Chronology chronology = DateTimeUtils.getInstantChronology(instant);
        DateTimeFieldType[] fieldTypes = new DateTimeFieldType[]{DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()};
        Partial partial = new Partial(fieldTypes, new int[]{2022, 1}, chronology);
        Instant testInstant = new DateTime("2022-01-01T00:00:00.000Z").toInstant();
        Instant otherInstant = new DateTime("2021-12-31T23:59:59.999Z").toInstant();
        ReadableInstant readableInstant1 = new Instant(testInstant);
        ReadableInstant readableInstant2 = new Instant(otherInstant);
        assertTrue(partial.isMatch(readableInstant1));
        assertFalse(partial.isMatch(readableInstant2));
    }

    @Test
    public void testIsMatchWithPartial() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType[] fieldTypes = new DateTimeFieldType[]{DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()};
        Partial partial1 = new Partial(fieldTypes, new int[]{2022, 1}, chronology);
        Partial partial2 = new Partial(fieldTypes, new int[]{2022, 2}, chronology);
        assertTrue(partial1.isMatch(partial2));
    }

    @Test
    public void testProperty() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType[] fieldTypes = new DateTimeFieldType[]{DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()};
        Partial partial = new Partial(fieldTypes, new int[]{2022, 1}, chronology);
        Property property = partial.property(DateTimeFieldType.year());
        assertEquals(partial, property.getPartial());
        assertEquals(DateTimeFieldType.year(), property.getField());
        assertEquals(2022, property.get());
    }
}