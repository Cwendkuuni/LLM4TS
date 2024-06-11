package org.joda.time;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import org.joda.time.BasePeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationFields;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.ReadOnlyDateTimeField;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class PartialTest {

    @Test
    public void testEmptyPartial() {
        Partial partial = new Partial();
        assertEquals(0, partial.size());
        assertNull(partial.getChronology());
    }

    @Test
    public void testPartialWithOneField() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(new DateTimeFieldType[] { DurationFields.YEARS }, new int[] { 2023 }, chronology);
        assertEquals(1, partial.size());
        assertEquals(chronology, partial.getChronology());
        assertEquals(DurationFields.YEARS, partial.getFieldTypes()[0]);
        assertEquals(2023, partial.getValue(0));
    }

    @Test
    public void testPartialWithMultipleFields() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(new DateTimeFieldType[] { DurationFields.YEARS, DurationFields.MONTHS, DurationFields.DAYS },
                new int[] { 2023, 5, 31 }, chronology);
        assertEquals(3, partial.size());
        assertEquals(chronology, partial.getChronology());
        assertEquals(DurationFields.YEARS, partial.getFieldTypes()[0]);
        assertEquals(2023, partial.getValue(0));
        assertEquals(DurationFields.MONTHS, partial.getFieldTypes()[1]);
        assertEquals(5, partial.getValue(1));
        assertEquals(DurationFields.DAYS, partial.getFieldTypes()[2]);
        assertEquals(31, partial.getValue(2));
    }

    @Test
    public void testPartialWithNullFields() {
        Chronology chronology = DateTimeUtils.forUTC();
        assertThrows(IllegalArgumentException.class, () -> new Partial(null, null, chronology));
        assertThrows(IllegalArgumentException.class, () -> new Partial(new DateTimeFieldType[] { null }, new int[0], chronology));
    }

    @Test
    public void testPartialWithDuplicateFields() {
        Chronology chronology = DateTimeUtils.forUTC();
        assertThrows(IllegalArgumentException.class, () -> new Partial(new DateTimeFieldType[] { DurationFields.YEARS, DurationFields.YEARS },
                new int[] { 2023, 2023 }, chronology));
    }

    @Test
    public void testPartialWithOutOfOrderFields() {
        Chronology chronology = DateTimeUtils.forUTC();
        assertThrows(IllegalArgumentException.class, () -> new Partial(new DateTimeFieldType[] { DurationFields.DAYS, DurationFields.MONTHS },
                new int[] { 31, 5 }, chronology));
    }

    @Test
    public void testPartialWithReadablePartial() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial1 = new Partial(new DateTimeFieldType[] { DurationFields.YEARS, DurationFields.MONTHS, DurationFields.DAYS },
                new int[] { 2023, 5, 31 }, chronology);
        Partial partial2 = new Partial(new LocalDate(2023, 5, 31).toSerializable());
        Partial partial3 = new Partial(partial2);
        assertEquals(partial1, partial3);
    }

    @Test
    public void testPartialWithChronology() {
        Chronology chronology1 = DateTimeUtils.forUTC();
        Chronology chronology2 = DateTimeUtils.forZoneOffset(DateTimeZone.forID("UTC-5"));
        Partial partial = new Partial(new DateTimeFieldType[] { DurationFields.HOURS }, new int[] { 5 }, chronology1);
        Partial partialWithChronology = partial.withChronologyRetainFields(chronology2);
        assertEquals(chronology2, partialWithChronology.getChronology());
    }

    @Test
    public void testPartialWithField() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(new DateTimeFieldType[] { DurationFields.HOURS }, new int[] { 5 }, chronology);
        Partial partialWithField = partial.with(DurationFields.HOURS, 6);
        assertEquals(7, partialWithField.getValue(0));
    }

    @Test
    public void testPartialWithFieldAdded() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(new DateTimeFieldType[] { DurationFields.HOURS }, new int[] { 5 }, chronology);
        Partial partialWithFieldAdded = partial.withFieldAdded(DurationFields.MINUTES, 30);
        assertEquals(35, partialWithFieldAdded.getValue(0));
    }

    @Test
    public void testPartialWithFieldAddWrapped() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(new DateTimeFieldType[] { DurationFields.HOURS }, new int[] { 5 }, chronology);
        Partial partialWithFieldAddWrapped = partial.withFieldAddWrapped(DurationFields.MINUTES, 30);
        assertEquals(115, partialWithFieldAddWrapped.getValue(0));
    }

    @Test
    public void testPartialWithPeriodAdded() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(new DateTimeFieldType[] {}, new int[0], chronology);
        ReadablePeriod period = new Period(new LocalDate(2023, 5, 31), new LocalDate(2024, 5, 31));
        Partial partialWithPeriodAdded = partial.withPeriodAdded(period, 1);
        DateTimeFormatter formatter = ISODateTimeFormat.forFields(Arrays.asList(DurationFields.YEARS, DurationFields.MONTHS, DurationFields.DAYS), true, false);
        assertEquals("2024-05-01", formatter.print(partialWithPeriodAdded));
    }

    @Test
    public void testPartialWithPlusPeriod() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(new DateTimeFieldType[] {}, new int[0], chronology);
        ReadablePeriod period = new Period(new LocalDate(2023, 5, 31), new LocalDate(2024, 5, 31));
        Partial partialWithPlusPeriod = partial.plus(period);
        DateTimeFormatter formatter = ISODateTimeFormat.forFields(Arrays.asList(DurationFields.YEARS, DurationFields.MONTHS, DurationFields.DAYS), true, false);
        assertEquals("2024-05-01", formatter.print(partialWithPlusPeriod));
    }

    @Test
    public void testPartialWithMinusPeriod() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(new DateTimeFieldType[] {}, new int[0], chronology);
        ReadablePeriod period = new Period(new LocalDate(2024, 5, 31), new LocalDate(2023, 5, 31));
        Partial partialWithMinusPeriod = partial.minus(period);
        DateTimeFormatter formatter = ISODateTimeFormat.forFields(Arrays.asList(DurationFields.YEARS, DurationFields.MONTHS, DurationFields.DAYS), true, false);
        assertEquals("2023-05-31", formatter.print(partialWithMinusPeriod));
    }

    @Test
    public void testPartialWithProperty() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(new DateTimeFieldType[] { DurationFields.YEARS, DurationFields.MONTHS, DurationFields.DAYS },
                new int[] { 2023, 5, 31 }, chronology);
        Property property = partial.property(DurationFields.YEARS);
        assertEquals(2023, property.get());
        Partial partialWithPropertySet = property.set(2024);
        assertEquals(2024, partialWithPropertySet.getValue(0));
    }

    @Test
    public void testPartialIsMatch() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(new DateTimeFieldType[] { DurationFields.YEARS, DurationFields.MONTHS, DurationFields.DAYS },
                new int[] { 2023, 5, 31 }, chronology);
        Instant instant = new Instant(1672444800000L);
        assertTrue(partial.isMatch(instant));
        LocalDateTime localDateTime = new LocalDateTime(2023, 5, 31, 12, 0);
        assertTrue(partial.isMatch(localDateTime));
        Partial partial2 = new Partial(new DateTimeFieldType[] { DurationFields.YEARS, DurationFields.MONTHS, DurationFields.DAYS },
                new int[] { 2024, 6, 1 }, chronology);
        assertFalse(partial.isMatch(partial2));
    }

    @Test
    public void testPartialToString() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(new DateTimeFieldType[] { DurationFields.YEARS, DurationFields.MONTHS, DurationFields.DAYS },
                new int[] { 2023, 5, 31 }, chronology);
        assertEquals("[years=2023, months=5, days=31]", partial.toString());
        DateTimeFormatter formatter = ISODateTimeFormat.forFields(Arrays.asList(DurationFields.YEARS, DurationFields.MONTHS, DurationFields.DAYS), true, false);
        assertEquals("2023-05-31", formatter.print(partial));
    }

    @Test
    public void testPartialToStringList() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(new DateTimeFieldType[] { DurationFields.YEARS, DurationFields.MONTHS, DurationFields.DAYS },
                new int[] { 2023, 5, 31 }, chronology);
        assertEquals("[years=2023, months=5, days=31]", partial.toStringList());
    }

    @Test
    public void testPartialToStringWithFormat() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(new DateTimeFieldType[] { DurationFields.YEARS, DurationFields.MONTHS, DurationFields.DAYS },
                new int[] { 2023, 5, 31 }, chronology);
        assertEquals("2023-05-31", partial.toString("yyyy-MM-dd"));
    }

    @Test
    public void testPartialToStringWithLocale() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(new DateTimeFieldType[] { DurationFields.YEARS, DurationFields.MONTHS, DurationFields.DAYS },
                new int[] { 2023, 5, 31 }, chronology);
        assertEquals("2023-05-31", partial.toString("yyyy-MM-dd", Locale.US));
    }
}