package org.joda.time.test;

import org.junit.Test;
import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.ReadOnlyDateTimeField;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import static org.junit.Assert.*;

public class PartialTest {

    @Test
    public void testEmptyPartial() {
        Partial emptyPartial = new Partial();
        assertEquals(0, emptyPartial.size());
        assertEquals(null, emptyPartial.getChronology());
        assertArrayEquals(new int[0], emptyPartial.getValues());
        assertNull(emptyPartial.getFormatter());
    }

    @Test
    public void testSingleFieldPartial() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateType = DateTimeFieldType.dayOfYear();
        Partial singleFieldPartial = new Partial(dateType, 1, chronology);
        assertEquals(1, singleFieldPartial.size());
        assertEquals(chronology, singleFieldPartial.getChronology());
        assertEquals(dateType, singleFieldPartial.getFieldType(0));
        assertEquals(1, singleFieldPartial.getValue(0));
        assertEquals(1, singleFieldPartial.getValues()[0]);
        assertNotNull(singleFieldPartial.getFormatter());
        assertEquals("2022-01-01[1]", singleFieldPartial.toString());
    }

    @Test
    public void testMultipleFieldsPartial() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateType1 = DateTimeFieldType.dayOfYear();
        DateTimeFieldType dateType2 = DateTimeFieldType.monthOfYear();
        DateTimeFieldType dateType3 = DateTimeFieldType.hourOfDay();
        int[] values = {1, 2, 23};
        Partial multipleFieldsPartial = new Partial(new DateTimeFieldType[]{dateType1, dateType2, dateType3}, values, chronology);
        assertEquals(3, multipleFieldsPartial.size());
        assertEquals(chronology, multipleFieldsPartial.getChronology());
        assertEquals(dateType1, multipleFieldsPartial.getFieldType(0));
        assertEquals(1, multipleFieldsPartial.getValue(0));
        assertEquals(1, multipleFieldsPartial.getValues()[0]);
        assertEquals(dateType2, multipleFieldsPartial.getFieldType(1));
        assertEquals(2, multipleFieldsPartial.getValue(1));
        assertEquals(2, multipleFieldsPartial.getValues()[1]);
        assertEquals(dateType3, multipleFieldsPartial.getFieldType(2));
        assertEquals(23, multipleFieldsPartial.getValue(2));
        assertEquals(23, multipleFieldsPartial.getValues()[2]);
        assertNotNull(multipleFieldsPartial.getFormatter());
        assertEquals("2022-01-02 23:00:00[1,2,23]", multipleFieldsPartial.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullChronology() {
        DateTimeFieldType dateType = DateTimeFieldType.dayOfYear();
        Partial partial = new Partial(dateType, 1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFieldType() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(null, 1, chronology);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFieldOrder() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateType1 = DateTimeFieldType.dayOfYear();
        DateTimeFieldType dateType2 = DateTimeFieldType.minuteOfHour();
        Partial partial = new Partial(new DateTimeFieldType[]{dateType2, dateType1}, new int[]{1, 1}, chronology);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidValuesLength() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateType = DateTimeFieldType.dayOfYear();
        Partial partial = new Partial(dateType, 1, chronology, new int[2]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidValuesArray() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateType = DateTimeFieldType.dayOfYear();
        Partial partial = new Partial(dateType, new int[]{1, 2, 3}, chronology);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFieldTypeArray() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(new DateTimeFieldType[0], new int[0], chronology);
    }

    @Test
    public void testGetField() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateType = DateTimeFieldType.dayOfYear();
        Partial partial = new Partial(dateType, 1, chronology);
        assertEquals(DateTimeFieldType.dayOfYear(), partial.getField(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldOutOfBounds() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateType = DateTimeFieldType.dayOfYear();
        Partial partial = new Partial(dateType, 1, chronology);
        partial.getField(1);
    }

    @Test
    public void testWithChronologyRetainFields() {
        Chronology fromChronology = DateTimeUtils.forUTC();
        Chronology toChronology = DateTimeUtils.forUTC().withZoneOffset(3600);
        DateTimeFieldType dateType = DateTimeFieldType.dayOfYear();
        Partial partial = new Partial(dateType, 1, fromChronology);
        Partial withChronologyRetainFields = partial.withChronologyRetainFields(toChronology);
        assertEquals(1, withChronologyRetainFields.size());
        assertEquals(toChronology, withChronologyRetainFields.getChronology());
        assertEquals(dateType, withChronologyRetainFields.getFieldType(0));
        assertEquals(1, withChronologyRetainFields.getValue(0));
    }

    @Test
    public void testWith() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateType = DateTimeFieldType.dayOfYear();
        Partial partial = new Partial(dateType, 1, chronology);
        Partial newPartial = partial.with(dateType, 2);
        assertEquals(2, newPartial.size());
        assertEquals(chronology, newPartial.getChronology());
        assertEquals(dateType, newPartial.getFieldType(0));
        assertEquals(1, newPartial.getValue(0));
        assertEquals(2, newPartial.getValue(1));
    }

    @Test
    public void testWithout() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateType = DateTimeFieldType.dayOfYear();
        Partial partial = new Partial(dateType, 1, chronology);
        Partial newPartial = partial.without(dateType);
        assertEquals(0, newPartial.size());
        assertEquals(chronology, newPartial.getChronology());
        try {
            newPartial.getValue(0);
            fail();
        } catch (ArrayIndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testWithField() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateType = DateTimeFieldType.dayOfYear();
        DateTimeFieldType rangeDateType = DateTimeFieldType.hourOfDay();
        Partial partial = new Partial(new DateTimeFieldType[]{dateType, rangeDateType}, new int[]{1, 2}, chronology);
        Partial newPartial = partial.withField(dateType, 3);
        assertEquals(2, newPartial.size());
        assertEquals(chronology, newPartial.getChronology());
        assertEquals(dateType, newPartial.getFieldType(0));
        assertEquals(3, newPartial.getValue(0));
        assertEquals(2, newPartial.getValue(1));
    }

    @Test
    public void testWithFieldAdded() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateType = DateTimeFieldType.dayOfYear();
        Partial partial = new Partial(new DateTimeFieldType[]{dateType}, new int[]{1}, chronology);
        DurationFieldType durationFieldType = DurationFieldType.hours();
        Partial newPartial = partial.withFieldAdded(durationFieldType, 1);
        assertEquals(2, newPartial.size());
        assertEquals(chronology, newPartial.getChronology());
        assertEquals(dateType, newPartial.getFieldType(0));
        assertEquals(1, newPartial.getValue(0));
        assertEquals(1, newPartial.getValue(1));
    }

    @Test
    public void testWithFieldAddWrapped() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateType = DateTimeFieldType.dayOfYear();
        Partial partial = new Partial(new DateTimeFieldType[]{dateType}, new int[]{1}, chronology);
        DurationFieldType durationFieldType = DurationFieldType.hours();
        Partial newPartial = partial.withFieldAddWrapped(durationFieldType, 12);
        assertEquals(2, newPartial.size());
        assertEquals(chronology, newPartial.getChronology());
        assertEquals(dateType, newPartial.getFieldType(0));
        assertEquals(1, newPartial.getValue(0));
        assertEquals(11, newPartial.getValue(1));
    }

    @Test
    public void testWithPeriodAdded() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateType = DateTimeFieldType.dayOfYear();
        Partial partial = new Partial(new DateTimeFieldType[]{dateType}, new int[]{1}, chronology);
        LocalDate date1 = new LocalDate(2022, 1, 1);
        LocalDateTime time1 = new LocalDateTime(2022, 1, 1, 12, 0, 0, 0, chronology);
        ReadablePeriod period = new Period(1, 1, 0, 1);
        Partial newPartial = partial.withPeriodAdded(period, 1);
        assertEquals(2, newPartial.size());
        assertEquals(chronology, newPartial.getChronology());
        assertEquals(dateType, newPartial.getFieldType(0));
        assertEquals(2, newPartial.getValue(0));
        assertEquals(1, newPartial.getValue(1));
    }

    @Test
    public void testPlus() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateType = DateTimeFieldType.dayOfYear();
        Partial partial = new Partial(new DateTimeFieldType[]{dateType}, new int[]{1}, chronology);
        LocalDate date1 = new LocalDate(2022, 1, 1);
        LocalDateTime time1 = new LocalDateTime(2022, 1, 1, 12, 0, 0, 0, chronology);
        ReadablePeriod period = new Period(1, 1, 0, 1);
        Partial newPartial = partial.plus(period);
        assertEquals(2, newPartial.size());
        assertEquals(chronology, newPartial.getChronology());
        assertEquals(dateType, newPartial.getFieldType(0));
        assertEquals(2, newPartial.getValue(0));
        assertEquals(1, newPartial.getValue(1));
    }

    @Test
    public void testMinus() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateType = DateTimeFieldType.dayOfYear();
        Partial partial = new Partial(new DateTimeFieldType[]{dateType}, new int[]{1}, chronology);
        LocalDate date1 = new LocalDate(2022, 1, 2);
        LocalDateTime time1 = new LocalDateTime(2022, 1, 2, 12, 0, 0, 0, chronology);
        ReadablePeriod period = new Period(1, 1, 0, 1);
        Partial newPartial = partial.minus(period);
        assertEquals(2, newPartial.size());
        assertEquals(chronology, newPartial.getChronology());
        assertEquals(dateType, newPartial.getFieldType(0));
        assertEquals(0, newPartial.getValue(0));
        assertEquals(1, newPartial.getValue(1));
    }

    @Test
    public void testProperty() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateType = DateTimeFieldType.dayOfYear();
        Partial partial = new Partial(new DateTimeFieldType[]{dateType}, new int[]{1}, chronology);
        Property property = new Property(partial, 0);
        assertEquals(partial, property.getPartial());
        assertEquals(0, property.getFieldIndex());
        assertEquals(dateType, property.getField());
        assertEquals(1, property.get());
    }

    @Test
    public void testIsMatch() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateType = DateTimeFieldType.dayOfYear();
        Partial partial = new Partial(new DateTimeFieldType[]{dateType}, new int[]{1}, chronology);
        LocalDate date1 = new LocalDate(2022, 1, 1);
        LocalDateTime time1 = new LocalDateTime(2022, 1, 1, 12, 0, 0, 0, chronology);
        assertTrue(partial.isMatch(date1));
        assertTrue(partial.isMatch(time1));
        LocalDate date2 = new LocalDate(2022, 1, 2);
        LocalDateTime time2 = new LocalDateTime(2022, 1, 2, 12, 0, 0, 0, chronology);
        assertFalse(partial.isMatch(date2));
        assertFalse(partial.isMatch(time2));
    }

    @Test
    public void testToString() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateType = DateTimeFieldType.dayOfYear();
        Partial partial = new Partial(new DateTimeFieldType[]{dateType}, new int[]{1}, chronology);
        String expected = "2022-01-01[1]";
        assertEquals(expected, partial.toString());
    }

    @Test
    public void testToStringList() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateType1 = DateTimeFieldType.dayOfYear();
        DateTimeFieldType dateType2 = DateTimeFieldType.monthOfYear();
        Partial partial = new Partial(new DateTimeFieldType[]{dateType1, dateType2}, new int[]{1, 2}, chronology);
        String expected = "[dayOfYear=1, monthOfYear=2]";
        assertEquals(expected, partial.toStringList());
    }

    @Test
    public void testToStringWithFormat() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateType = DateTimeFieldType.dayOfMonth();
        Partial partial = new Partial(new DateTimeFieldType[]{dateType}, new int[]{1}, chronology);
        DateTimeFormatter formatter = ISODateTimeFormat.forFields(new DateTimeFieldType[]{dateType}, true, false);
        String expected = "2022-01-01";
        assertEquals(expected, partial.toString(formatter));
    }

    @Test
    public void testToStringWithLocale() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateType = DateTimeFieldType.dayOfMonth();
        Partial partial = new Partial(new DateTimeFieldType[]{dateType}, new int[]{1}, chronology);
        DateTimeFormat format = DateTimeFormat.forPattern("d MMMM yyyy").withLocale(Locale.ENGLISH);
        String expected = "1 January 2022";
        assertEquals(expected, partial.toString(format));
    }
}