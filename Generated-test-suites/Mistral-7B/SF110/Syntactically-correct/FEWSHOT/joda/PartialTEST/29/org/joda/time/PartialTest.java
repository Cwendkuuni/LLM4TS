package org.joda.time;

import static org.junit.Assert.*;
import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.field.*;
import org.junit.Test;

public class PartialTest {

    @Test
    public void testEmptyPartial() {
        Partial emptyPartial = new Partial();
        assertEquals(0, emptyPartial.size());
    }

    @Test
    public void testPartialWithChronology() {
        Chronology chronology = DateTimeUtils.forDate(2022, 1, 1).getChronology();
        Partial partial = new Partial(chronology);
        assertEquals(0, partial.size());
    }

    @Test
    public void testPartialWithDateTimeFieldType() {
        Chronology chronology = DateTimeUtils.forDate(2022, 1, 1).getChronology();
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.millisecondOfMinute();
        Partial partial = new Partial(dateTimeFieldType, 123, chronology);
        assertEquals(1, partial.size());
        assertEquals(dateTimeFieldType, partial.getFieldType(0));
        assertEquals(123, partial.getValue(0));
    }

    @Test
    public void testPartialWithDateTimeFieldTypeAndNullChronology() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.millisecondOfMinute();
        try {
            new Partial(dateTimeFieldType, 123, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("The chronology must not be null", e.getMessage());
        }
    }

    @Test
    public void testPartialWithDateTimeFieldTypesAndIntValues() {
        Chronology chronology = DateTimeUtils.forDate(2022, 1, 1).getChronology();
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.hourOfDay();
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.minuteOfHour();
        int[] intValues = {13, 35};
        Partial partial = new Partial(new DateTimeFieldType[] { dateTimeFieldType1, dateTimeFieldType2 }, intValues, chronology);
        assertEquals(2, partial.size());
        assertEquals(dateTimeFieldType1, partial.getFieldType(0));
        assertEquals(13, partial.getValue(0));
        assertEquals(dateTimeFieldType2, partial.getFieldType(1));
        assertEquals(35, partial.getValue(1));
    }

    @Test
    public void testPartialWithNullTypesArray() {
        try {
            new Partial(null, new int[0]);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Types array must not be null", e.getMessage());
        }
    }

    @Test
    public void testPartialWithNullValuesArray() {
        DateTimeFieldType[] iTypes = {DateTimeFieldType.hourOfDay(), DateTimeFieldType.minuteOfHour()};
        try {
            new Partial(iTypes, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Values array must not be null", e.getMessage());
        }
    }

    @Test
    public void testPartialWithDifferentLengthTypesAndValuesArrays() {
        DateTimeFieldType[] iTypes = {DateTimeFieldType.hourOfDay(), DateTimeFieldType.minuteOfHour()};
        int[] iValues = {1, 2, 3};
        try {
            new Partial(iTypes, iValues);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Values array must be the same length as the types array", e.getMessage());
        }
    }

    @Test
    public void testPartialWithSupportedDateTimeFieldTypes() {
        Chronology chronology = DateTimeUtils.forDate(2022, 1, 1).getChronology();
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.hourOfDay();
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.minuteOfHour();
        Partial partial = new Partial(new DateTimeFieldType[] { dateTimeFieldType1, dateTimeFieldType2 }, new int[0], chronology);
        assertEquals(0, partial.size());
        try {
            partial.with(dateTimeFieldType1, 13);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("The field type is not supported by this chronology", e.getMessage());
        }
        try {
            partial.with(DateTimeFieldType.millisecondOfSecond(), 123);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("The field type is not supported by this chronology", e.getMessage());
        }
    }

    @Test
    public void testPartialWithSupportedDateTimeFieldTypesAndValues() {
        Chronology chronology = DateTimeUtils.forDate(2022, 1, 1).getChronology();
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.hourOfDay();
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.minuteOfHour();
        Partial partial = new Partial(new DateTimeFieldType[] { dateTimeFieldType1, dateTimeFieldType2 }, new int[] { 13, 35 }, chronology);
        assertEquals(2, partial.size());
        assertEquals(dateTimeFieldType1, partial.getFieldType(0));
        assertEquals(13, partial.getValue(0));
        assertEquals(dateTimeFieldType2, partial.getFieldType(1));
        assertEquals(35, partial.getValue(1));
        try {
            partial.with(DateTimeFieldType.hourOfDay(), 14);
            Partial newPartial = partial.with(DateTimeFieldType.hourOfDay(), 14);
            assertEquals(partial, newPartial);
            assertEquals(14, newPartial.getValue(0));
        } catch (IllegalArgumentException e) {
            fail();
        }
        try {
            partial.with(DateTimeFieldType.millisecondOfSecond(), 123);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("The field type is not supported by this partial", e.getMessage());
        }
        try {
            partial.without(DateTimeFieldType.minuteOfHour());
            Partial newPartial = partial.without(DateTimeFieldType.minuteOfHour());
            assertEquals(1, newPartial.size());
            assertEquals(dateTimeFieldType1, newPartial.getFieldType(0));
        } catch (IllegalArgumentException e) {
            fail();
        }
        try {
            partial.with(DateTimeFieldType.millisecondOfSecond(), 123);
            Partial newPartial = partial.with(DateTimeFieldType.millisecondOfSecond(), 456);
            assertEquals(2, newPartial.size());
            assertEquals(dateTimeFieldType1, newPartial.getFieldType(0));
            assertEquals(13, newPartial.getValue(0));
            assertEquals(dateTimeFieldType2, newPartial.getFieldType(1));
            assertEquals(35, newPartial.getValue(1));
            assertEquals(456, newPartial.getValue(2));
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void testPartialWithUnsupportedDateTimeFieldTypes() {
        Chronology chronology = DateTimeUtils.forDate(2022, 1, 1).getChronology();
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.hourOfDay();
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.secondOfMinute();
        DateTimeFieldType unsupportedDateTimeFieldType = DateTimeFieldType.millisecondOfSecond();
        Partial partial = new Partial(new DateTimeFieldType[] { dateTimeFieldType1, dateTimeFieldType2, unsupportedDateTimeFieldType }, new int[0], chronology);
        assertEquals(2, partial.size());
        try {
            partial.with(unsupportedDateTimeFieldType, 123);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("The field type is not supported by this partial", e.getMessage());
        }
        try {
            partial.without(dateTimeFieldType2);
            Partial newPartial = partial.without(dateTimeFieldType2);
            assertEquals(1, newPartial.size());
            assertEquals(dateTimeFieldType1, newPartial.getFieldType(0));
        } catch (IllegalArgumentException e) {
            fail();
        }
        try {
            Partial newPartial = partial.with(unsupportedDateTimeFieldType, 123);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("The field type is not supported by this partial", e.getMessage());
        }
    }

    @Test
    public void testPartialWithMultipleUnsupportedDateTimeFieldTypes() {
        Chronology chronology = DateTimeUtils.forDate(2022, 1, 1).getChronology();
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.hourOfDay();
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.secondOfMinute();
        DateTimeFieldType unsupportedDateTimeFieldType1 = DateTimeFieldType.millisecondOfSecond();
        DateTimeFieldType unsupportedDateTimeFieldType2 = DateTimeFieldType.microsecondOfMillisecond();
        Partial partial = new Partial(new DateTimeFieldType[] { dateTimeFieldType1, dateTimeFieldType2, unsupportedDateTimeFieldType1, unsupportedDateTimeFieldType2 }, new int[0], chronology);
        assertEquals(2, partial.size());
        try {
            partial.with(unsupportedDateTimeFieldType1, 123);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("The field type is not supported by this partial", e.getMessage());
        }
        try {
            partial.with(unsupportedDateTimeFieldType2, 456);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("The field type is not supported by this partial", e.getMessage());
        }
        try {
            Partial newPartial = partial.with(unsupportedDateTimeFieldType1, 123);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("The field type is not supported by this partial", e.getMessage());
        }
        try {
            Partial newPartial = partial.with(unsupportedDateTimeFieldType2, 456);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("The field type is not supported by this partial", e.getMessage());
        }
    }

    @Test
    public void testPartialWithSupportedDateTimeFieldTypesAndReadablePartial() {
        Chronology chronology = DateTimeUtils.forDate(2022, 1, 1).getChronology();
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.hourOfDay();
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.minuteOfHour();
        Partial partial1 = new Partial(new DateTimeFieldType[] { dateTimeFieldType1, dateTimeFieldType2 }, new int[] { 13, 35 }, chronology);
        Partial partial2 = new Partial(DateTimeFieldType.hourOfDay(), 13, chronology);
        Partial copyPartial = new Partial(partial2);
        Partial newPartial = partial1.with(partial2);
        assertEquals(2, partial1.size());
        assertEquals(partial2, newPartial.getReadablePartial());
        assertEquals(13, newPartial.getValue(0));
        assertEquals(dateTimeFieldType1, newPartial.getFieldType(0));
        assertEquals(13, partial2.getValue(0));
        assertEquals(dateTimeFieldType1, partial2.getFieldType(0));
        assertEquals(35, partial1.getValue(1));
        assertEquals(dateTimeFieldType2, partial1.getFieldType(1));
        assertEquals(35, partial2.size());
        assertEquals(dateTimeFieldType2, partial2.getFieldType(0));
        assertEquals(0, partial2.getValue(1));
        assertEquals(0, copyPartial.size());
        assertEquals(partial1, copyPartial.getReadablePartial());
        assertEquals(partial1, newPartial.getReadablePartial());
    }

    @Test
    public void testPartialWithSupportedDateTimeFieldTypesAndSerializablePartial() {
        Chronology chronology = DateTimeUtils.forDate(2022, 1, 1).getChronology();
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.hourOfDay();
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.minuteOfHour();
        Partial partial1 = new Partial(new DateTimeFieldType[] { dateTimeFieldType1, dateTimeFieldType2 }, new int[] { 13, 35 }, chronology);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(partial1);
        objectOutputStream.close();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Partial copyPartial = (Partial) objectInputStream.readObject();
        objectInputStream.close();
        assertEquals(partial1, copyPartial);
    }

    @Test
    public void testToString() {
        Chronology chronology = DateTimeUtils.forDate(2022, 1, 1).getChronology();
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.hourOfDay();
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.minuteOfHour();
        Partial partial = new Partial(new DateTimeFieldType[] { dateTimeFieldType1, dateTimeFieldType2 }, new int[] { 13, 35 }, chronology);
        assertEquals("[hourOfDay=13, minuteOfHour=35]", partial.toString());
    }

    @Test
    public void testToStringList() {
        Chronology chronology = DateTimeUtils.forDate(2022, 1, 1).getChronology();
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.hourOfDay();
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.minuteOfHour();
        Partial partial = new Partial(new DateTimeFieldType[] { dateTimeFieldType1, dateTimeFieldType2 }, new int[] { 13, 35 }, chronology);
        assertEquals("[hourOfDay=13, minuteOfHour=35]", partial.toStringList());
    }

    @Test
    public void testToStringWithFormat() {
        Chronology chronology = DateTimeUtils.forDate(2022, 1, 1).getChronology();
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.hourOfDay();
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.minuteOfHour();
        Partial partial = new Partial(new DateTimeFieldType[] { dateTimeFieldType1, dateTimeFieldType2 }, new int[] { 13, 35 }, chronology);
        assertEquals("13:35", partial.toString("HH:mm"));
    }

    @Test
    public void testToStringWithLocale() {
        Chronology chronology = DateTimeUtils.forDate(2022, 1, 1).getChronology();
        DateTimeFieldType dateTimeFieldType1 = DateTimeFieldType.hourOfDay();
        DateTimeFieldType dateTimeFieldType2 = DateTimeFieldType.minuteOfHour();
        Partial partial = new Partial(new DateTimeFieldType[] { dateTimeFieldType1, dateTimeFieldType2 }, new int[] { 13, 35 }, chronology);
        assertEquals("13:35", partial.toString("HH:mm", Locale.ENGLISH));
    }
}