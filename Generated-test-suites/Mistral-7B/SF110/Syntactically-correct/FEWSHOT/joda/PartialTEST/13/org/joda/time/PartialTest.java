package org.joda.time;

import static org.junit.Assert.*;

import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDateTimeField;
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Arrays;

public class PartialTest {

    @Test
    public void testEmptyPartial() {
        Partial partial = new Partial();
        assertEquals(0, partial.size());
        assertNull(partial.getChronology());
    }

    @Test
    public void testPartialWithSingleField() {
        Chronology chronology = GregorianChronology.INSTANCE;
        DateTimeFieldType dateTimeFieldType = LocalDateTimeField.DAY_OF_MONTH;
        Partial partial = new Partial(dateTimeFieldType, 31, chronology);

        assertEquals(1, partial.size());
        assertEquals(chronology, partial.getChronology());
        assertEquals(dateTimeFieldType, partial.getFieldTypes()[0]);
        assertEquals(31, partial.getValue(0));
    }

    @Test
    public void testPartialWithMultipleFields() {
        Chronology chronology = GregorianChronology.INSTANCE;
        DateTimeFieldType dateTimeFieldType1 = LocalDateTimeField.HOUR_OF_DAY;
        DateTimeFieldType dateTimeFieldType2 = LocalDateTimeField.MINUTE_OF_HOUR;
        int[] values = {13, 30};
        Partial partial = new Partial(new DateTimeFieldType[]{dateTimeFieldType1, dateTimeFieldType2}, values, chronology);

        assertEquals(2, partial.size());
        assertEquals(chronology, partial.getChronology());
        assertEquals(dateTimeFieldType1, partial.getFieldTypes()[0]);
        assertEquals(13, partial.getValue(0));
        assertEquals(dateTimeFieldType2, partial.getFieldTypes()[1]);
        assertEquals(30, partial.getValue(1));
    }

    @Test
    public void testPartialConstructorWithNullTypesArray() {
        try {
            new Partial(null, new int[0], GregorianChronology.INSTANCE);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testPartialConstructorWithNullValuesArray() {
        try {
            new Partial(DateTimeFieldType.DAY_OF_MONTH, null, GregorianChronology.INSTANCE);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testPartialConstructorWithInvalidValuesArrayLength() {
        try {
            new Partial(DateTimeFieldType.DAY_OF_MONTH, new int[]{1, 2, 3}, GregorianChronology.INSTANCE);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testPartialCopyConstructor() {
        Chronology chronology = GregorianChronology.INSTANCE;
        DateTimeFieldType dateTimeFieldType = LocalDateTimeField.DAY_OF_MONTH;
        Partial partial = new Partial(dateTimeFieldType, 31, chronology);
        Partial partialCopy = new Partial(partial);

        assertEquals(partial.size(), partialCopy.size());
        assertEquals(partial.getChronology(), partialCopy.getChronology());
        assertEquals(Arrays.asList(partial.getFieldTypes()), Arrays.asList(partialCopy.getFieldTypes()));
        assertEquals(partial.getValue(0), partialCopy.getValue(0));
    }

    @Test
    public void testPartialConstructorWithReadablePartial() {
        Chronology chronology = GregorianChronology.INSTANCE;
        ReadablePartial readablePartial = new LocalDateTime(2019, 1, 1, 13, 30, 0, 0, chronology);
        Partial partial = new Partial(readablePartial);

        assertEquals(2, partial.size());
        assertEquals(chronology, partial.getChronology());
        assertEquals(LocalDateTimeField.HOUR_OF_DAY, partial.getFieldTypes()[0]);
        assertEquals(13, partial.getValue(0));
        assertEquals(LocalDateTimeField.MINUTE_OF_HOUR, partial.getFieldTypes()[1]);
        assertEquals(30, partial.getValue(1));
    }

    @Test
    public void testPartialWithChronologyRetainFields() {
        Chronology chronology1 = GregorianChronology.INSTANCE;
        DateTimeFieldType dateTimeFieldType = LocalDateTimeField.DAY_OF_MONTH;
        Partial partial1 = new Partial(dateTimeFieldType, 31, chronology1);
        Chronology chronology2 = UTCChronology.INSTANCE;
        Partial partial2 = partial1.withChronologyRetainFields(chronology2);

        assertEquals(partial1.size(), partial2.size());
        assertEquals(chronology2, partial2.getChronology());
        assertEquals(Arrays.asList(partial1.getFieldTypes()), Arrays.asList(partial2.getFieldTypes()));
        assertEquals(partial1.getValue(0), partial2.getValue(0));
    }

    @Test
    public void testPartialWith() {
        Chronology chronology = GregorianChronology.INSTANCE;
        DateTimeFieldType dateTimeFieldType = LocalDateTimeField.DAY_OF_MONTH;
        Partial partial1 = new Partial(dateTimeFieldType, 31, chronology);
        DateTimeFieldType newDateTimeFieldType = LocalDateTimeField.HOUR_OF_DAY;
        Partial partial2 = partial1.with(newDateTimeFieldType, 12);

        assertEquals(2, partial2.size());
        assertEquals(chronology, partial2.getChronology());
        assertEquals(newDateTimeFieldType, partial2.getFieldTypes()[1]);
        assertEquals(12, partial2.getValue(1));
    }

    @Test
    public void testPartialWithout() {
        Chronology chronology = GregorianChronology.INSTANCE;
        DateTimeFieldType dateTimeFieldType = LocalDateTimeField.DAY_OF_MONTH;
        Partial partial1 = new Partial(dateTimeFieldType, 31, chronology);
        DateTimeFieldType dateTimeFieldTypeToRemove = LocalDateTimeField.HOUR_OF_DAY;
        Partial partial2 = partial1.without(dateTimeFieldTypeToRemove);

        assertEquals(1, partial2.size());
        assertEquals(chronology, partial2.getChronology());
        assertFalse(Arrays.asList(partial1.getFieldTypes()).contains(dateTimeFieldTypeToRemove));
    }

    @Test
    public void testPartialWithField() {
        Chronology chronology = GregorianChronology.INSTANCE;
        DateTimeFieldType dateTimeFieldType1 = LocalDateTimeField.DAY_OF_MONTH;
        DateTimeFieldType dateTimeFieldType2 = LocalDateTimeField.MINUTE_OF_HOUR;
        Partial partial1 = new Partial(new DateTimeFieldType[]{dateTimeFieldType1, dateTimeFieldType2}, new int[]{31, 30}, chronology);
        DateTimeFieldType newDateTimeFieldType = LocalDateTimeField.HOUR_OF_DAY;
        Partial partial2 = partial1.withField(newDateTimeFieldType, 12);

        assertEquals(3, partial2.size());
        assertEquals(chronology, partial2.getChronology());
        assertEquals(newDateTimeFieldType, partial2.getFieldTypes()[2]);
        assertEquals(12, partial2.getValue(2));
    }

    @Test
    public void testPartialWithFieldAdded() {
        Chronology chronology = GregorianChronology.INSTANCE;
        DateTimeFieldType dateTimeFieldType1 = LocalDateTimeField.DAY_OF_MONTH;
        DateTimeFieldType dateTimeFieldType2 = LocalDateTimeField.MINUTE_OF_HOUR;
        Partial partial1 = new Partial(new DateTimeFieldType[]{dateTimeFieldType1, dateTimeFieldType2}, new int[]{31, 30}, chronology);
        DurationFieldType durationFieldType = DurationFieldType.MILLIS;
        Partial partial2 = partial1.withFieldAdded(durationFieldType, 500);

        assertEquals(3, partial2.size());
        assertEquals(chronology, partial2.getChronology());
        assertEquals(durationFieldType, partial2.getFieldTypes()[2]);
        assertEquals(500, partial2.getValue(2));
    }

    @Test
    public void testPartialWithFieldAddWrapped() {
        Chronology chronology = GregorianChronology.INSTANCE;
        DateTimeFieldType dateTimeFieldType1 = LocalDateTimeField.DAY_OF_MONTH;
        DateTimeFieldType dateTimeFieldType2 = LocalDateTimeField.MINUTE_OF_HOUR;
        Partial partial1 = new Partial(new DateTimeFieldType[]{dateTimeFieldType1, dateTimeFieldType2}, new int[]{31, 30}, chronology);
        DurationFieldType durationFieldType = DurationFieldType.MILLIS;
        Partial partial2 = partial1.withFieldAddWrapped(durationFieldType, 500);

        assertEquals(3, partial2.size());
        assertEquals(chronology, partial2.getChronology());
        assertEquals(durationFieldType, partial2.getFieldTypes()[2]);
        assertEquals(500, partial2.getValue(2));
    }

    @Test
    public void testPartialWithPeriodAdded() {
        Chronology chronology = GregorianChronology.INSTANCE;
        DateTimeFieldType dateTimeFieldType = LocalDateTimeField.DAY_OF_MONTH;
        Partial partial1 = new Partial(dateTimeFieldType, 31, chronology);
        ReadablePeriod readablePeriod = new BaseDuration(new LocalDateTime(2019, 1, 1, 13, 30, 0, 0, chronology), new LocalDateTime(2019, 1, 1, 14, 30, 0, 0, chronology));
        Partial partial2 = partial1.withPeriodAdded(readablePeriod, 1);

        assertEquals(2, partial2.size());
        assertEquals(chronology, partial2.getChronology());
        assertEquals(dateTimeFieldType, partial2.getFieldTypes()[0]);
        assertEquals(32, partial2.getValue(0));
    }

    @Test
    public void testPartialPlus() {
        Chronology chronology = GregorianChronology.INSTANCE;
        DateTimeFieldType dateTimeFieldType = LocalDateTimeField.DAY_OF_MONTH;
        Partial partial1 = new Partial(dateTimeFieldType, 31, chronology);
        ReadablePeriod readablePeriod = new BaseDuration(new LocalDateTime(2019, 1, 1, 13, 30, 0, 0, chronology), new LocalDateTime(2019, 1, 1, 14, 30, 0, 0, chronology));
        Partial partial2 = partial1.plus(readablePeriod);

        assertEquals(2, partial2.size());
        assertEquals(chronology, partial2.getChronology());
        assertEquals(dateTimeFieldType, partial2.getFieldTypes()[0]);
        assertEquals(32, partial2.getValue(0));
    }

    @Test
    public void testPartialMinus() {
        Chronology chronology = GregorianChronology.INSTANCE;
        DateTimeFieldType dateTimeFieldType = LocalDateTimeField.DAY_OF_MONTH;
        Partial partial1 = new Partial(dateTimeFieldType, 32, chronology);
        ReadablePeriod readablePeriod = new BaseDuration(new LocalDateTime(2019, 1, 1, 13, 30, 0, 0, chronology), new LocalDateTime(2019, 1, 1, 13, 30, 0, 0, chronology));
        Partial partial2 = partial1.minus(readablePeriod);

        assertEquals(1, partial2.size());
        assertEquals(chronology, partial2.getChronology());
        assertEquals(dateTimeFieldType, partial2.getFieldTypes()[0]);
        assertEquals(31, partial2.getValue(0));
    }

    @Test
    public void testPartialProperty() {
        Chronology chronology = GregorianChronology.INSTANCE;
        DateTimeFieldType dateTimeFieldType = LocalDateTimeField.DAY_OF_MONTH;
        Partial partial = new Partial(dateTimeFieldType, 31, chronology);
        Property property = new Property(partial, 0);

        assertEquals(partial, property.getPartial());
        assertEquals(dateTimeFieldType, property.getField());
        assertEquals(31, property.get());
    }

    @Test
    public void testPartialIsMatchReadableInstant() {
        Chronology chronology = GregorianChronology.INSTANCE;
        DateTimeFieldType dateTimeFieldType = LocalDateTimeField.DAY_OF_MONTH;
        Partial partial = new Partial(dateTimeFieldType, 31, chronology);
        LocalDateTime localDateTime = new LocalDateTime(chronology, 2019, 1, 31, 12, 0, 0);
        assertTrue(partial.isMatch(localDateTime));
    }

    @Test
    public void testPartialIsMatchReadablePartial() {
        Chronology chronology = GregorianChronology.INSTANCE;
        DateTimeFieldType dateTimeFieldType1 = LocalDateTimeField.DAY_OF_MONTH;
        DateTimeFieldType dateTimeFieldType2 = LocalDateTimeField.HOUR_OF_DAY;
        Partial partial1 = new Partial(new DateTimeFieldType[]{dateTimeFieldType1, dateTimeFieldType2}, new int[]{31, 12}, chronology);
        DateTimeFieldType dateTimeFieldType3 = LocalDateTimeField.MINUTE_OF_HOUR;
        Partial partial2 = new Partial(new DateTimeFieldType[]{dateTimeFieldType1, dateTimeFieldType3}, new int[]{31, 30}, chronology);
        assertTrue(partial1.isMatch(partial2));
    }

    @Test
    public void testPartialFormatter() {
        Chronology chronology = GregorianChronology.INSTANCE;
        DateTimeFieldType dateTimeFieldType1 = LocalDateTimeField.DAY_OF_MONTH;
        DateTimeFieldType dateTimeFieldType2 = LocalDateTimeField.HOUR_OF_DAY;
        Partial partial = new Partial(new DateTimeFieldType[]{dateTimeFieldType1, dateTimeFieldType2}, new int[]{31, 12}, chronology);
        DateTimeFormatter formatter = partial.getFormatter();
        String formatted = formatter != null ? formatter.print(partial) : partial.toStringList();

        assertEquals("[[DAY_OF_MONTH=31, HOUR_OF_DAY=12]]", formatted);
    }

    @Test
    public void testPartialToString() {
        Chronology chronology = GregorianChronology.INSTANCE;
        DateTimeFieldType dateTimeFieldType1 = LocalDateTimeField.DAY_OF_MONTH;
        DateTimeFieldType dateTimeFieldType2 = LocalDateTimeField.HOUR_OF_DAY;
        Partial partial = new Partial(new DateTimeFieldType[]{dateTimeFieldType1, dateTimeFieldType2}, new int[]{31, 12}, chronology);
        String toString = partial.toString();

        assertEquals("[DAY_OF_MONTH=31, HOUR_OF_DAY=12]", toString);
    }

    @Test
    public void testPartialToStringWithPattern() {
        Chronology chronology = GregorianChronology.INSTANCE;
        DateTimeFieldType dateTimeFieldType = LocalDateTimeField.DAY_OF_MONTH;
        Partial partial = new Partial(dateTimeFieldType, 31, chronology);
        String toString = partial.toString("dd");

        assertEquals("31", toString);
    }

    @Test
    public void testPartialToStringWithLocale() {
        Chronology chronology = GregorianChronology.INSTANCE;
        DateTimeFieldType dateTimeFieldType = LocalDateTimeField.DAY_OF_MONTH;
        Partial partial = new Partial(dateTimeFieldType, 31, chronology);
        String toString = partial.toString("dd", Locale.FRANCE);

        assertEquals("31", toString);
    }
}