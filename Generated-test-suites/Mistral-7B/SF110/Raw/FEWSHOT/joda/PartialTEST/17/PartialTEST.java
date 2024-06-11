package org.joda.time;

import static org.junit.Assert.*;

import org.junit.Test;

public class PartialTest {

    @Test
    public void testEmptyPartial() {
        Partial emptyPartial = new Partial();
        assertEquals(0, emptyPartial.size());
    }

    @Test
    public void testPartialWithChronology() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(chronology);
        assertEquals(chronology, partial.getChronology());
    }

    @Test
    public void testPartialWithTypeAndValue() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.hourOfDay();
        Partial partial = new Partial(dateTimeFieldType, 12, chronology);
        assertEquals(dateTimeFieldType, partial.getFieldType(0));
        assertEquals(12, partial.getValue(0));
        assertEquals(chronology, partial.getChronology());
    }

    @Test
    public void testPartialWithTypesAndValues() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateTimeFieldTypeHour = DateTimeFieldType.hourOfDay();
        DateTimeFieldType dateTimeFieldTypeMinute = DateTimeFieldType.minuteOfHour();
        int[] values = {12, 30};
        Partial partial = new Partial(new DateTimeFieldType[]{dateTimeFieldTypeHour, dateTimeFieldTypeMinute}, values, chronology);
        assertEquals(dateTimeFieldTypeHour, partial.getFieldType(0));
        assertEquals(12, partial.getValue(0));
        assertEquals(dateTimeFieldTypeMinute, partial.getFieldType(1));
        assertEquals(30, partial.getValue(1));
        assertEquals(chronology, partial.getChronology());
    }

    @Test
    public void testPartialWithReadablePartial() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateTimeFieldTypeHour = DateTimeFieldType.hourOfDay();
        int value = 12;
        Partial partial = new Partial(new DateTimeFieldType[]{dateTimeFieldTypeHour}, new int[]{value}, chronology);
        ReadablePartial readablePartial = new GregorianLocalDate(2022, 1, 1).toDateTimeAtStartOfDay().toPartial(chronology);
        Partial partialFromReadable = new Partial(readablePartial);
        assertEquals(partial.getFieldType(0), partialFromReadable.getFieldType(0));
        assertEquals(partial.getValue(0), partialFromReadable.getValue(0));
        assertEquals(partial.getChronology(), partialFromReadable.getChronology());
        assertEquals(partial.size(), partialFromReadable.size());
    }

    @Test
    public void testPartialWithFieldTypeAndValueIndex() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateTimeFieldTypeHour = DateTimeFieldType.hourOfDay();
        int value = 12;
        Partial partial = new Partial(new DateTimeFieldType[]{dateTimeFieldTypeHour}, new int[]{value}, chronology);
        int index = partial.indexOf(dateTimeFieldTypeHour);
        assertEquals(index, 0);
        assertEquals(value, partial.getValue(index));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPartialWithNullTypesArray() {
        Chronology chronology = DateTimeUtils.forUTC();
        Partial partial = new Partial(null, new int[0], chronology);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPartialWithNullValuesArray() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType[] types = new DateTimeFieldType[1];
        types[0] = DateTimeFieldType.hourOfDay();
        Partial partial = new Partial(types, null, chronology);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPartialWithTypesAndValuesMismatchLength() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType[] types = new DateTimeFieldType[1];
        types[0] = DateTimeFieldType.hourOfDay();
        int[] values = new int[2];
        Partial partial = new Partial(types, values, chronology);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPartialWithDuplicateFieldTypes() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateTimeFieldTypeHour = DateTimeFieldType.hourOfDay();
        DateTimeFieldType dateTimeFieldTypeMinute = DateTimeFieldType.minuteOfHour();
        int[] values = {12, 30, 30};
        try {
            Partial partial = new Partial(new DateTimeFieldType[]{dateTimeFieldTypeHour, dateTimeFieldTypeMinute, dateTimeFieldTypeMinute}, values, chronology);
        } catch (IllegalArgumentException e) {
            // Expected
            assertEquals("Types array must not contain duplicate: " + dateTimeFieldTypeMinute.getName(), e.getMessage());
        }
    }

    @Test
    public void testPartialWithDuplicateFieldTypesOrder() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateTimeFieldTypeHour = DateTimeFieldType.hourOfDay();
        DateTimeFieldType dateTimeFieldTypeMinute = DateTimeFieldType.minuteOfHour();
        int[] values = {30, 12};
        Partial partial = new Partial(new DateTimeFieldType[]{dateTimeFieldTypeMinute, dateTimeFieldTypeHour}, values, chronology);
        assertEquals(dateTimeFieldTypeHour, partial.getFieldType(0));
        assertEquals(12, partial.getValue(0));
        assertEquals(dateTimeFieldTypeMinute, partial.getFieldType(1));
        assertEquals(30, partial.getValue(1));
    }

    @Test
    public void testPartialWithAddField() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateTimeFieldTypeHour = DateTimeFieldType.hourOfDay();
        DateTimeFieldType dateTimeFieldTypeMinute = DateTimeFieldType.minuteOfHour();
        Partial partial = new Partial(new DateTimeFieldType[]{dateTimeFieldTypeHour, dateTimeFieldTypeMinute}, new int[]{12, 30}, chronology);
        DateTimeField newFieldType = DateTimeFieldType.secondOfMinute();
        Partial newPartial = partial.with(newFieldType, 45);
        assertEquals(newPartial.size(), partial.size() + 1);
        assertEquals(newFieldType, newPartial.getFieldType(partial.size()));
        assertEquals(45, newPartial.getValue(partial.size()));
    }

    @Test
    public void testPartialWithRemoveField() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateTimeFieldTypeHour = DateTimeFieldType.hourOfDay();
        DateTimeFieldType dateTimeFieldTypeMinute = DateTimeFieldType.minuteOfHour();
        Partial partial = new Partial(new DateTimeFieldType[]{dateTimeFieldTypeHour, dateTimeFieldTypeMinute}, new int[]{12, 30}, chronology);
        Partial newPartial = partial.without(dateTimeFieldTypeMinute);
        assertEquals(newPartial.size(), partial.size() - 1);
        assertFalse(newPartial.getFieldType(1) instanceof DateTimeFieldType);
    }

    @Test
    public void testPartialWithSetField() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateTimeFieldTypeHour = DateTimeFieldType.hourOfDay();
        DateTimeFieldType dateTimeFieldTypeMinute = DateTimeFieldType.minuteOfHour();
        int initialValueHour = 12;
        int initialValueMinute = 30;
        Partial partial = new Partial(new DateTimeFieldType[]{dateTimeFieldTypeHour, dateTimeFieldTypeMinute}, new int[]{initialValueHour, initialValueMinute}, chronology);
        int newValueMinute = 15;
        Partial newPartial = partial.withField(dateTimeFieldTypeMinute, newValueMinute);
        assertEquals(newPartial.getValue(1), newValueMinute);
        assertNotEquals(newPartial.getValue(0), initialValueMinute);
    }

    @Test
    public void testPartialWithAddWrapField() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateTimeFieldTypeHour = DateTimeFieldType.hourOfDay();
        DateTimeFieldType dateTimeFieldTypeMinute = DateTimeFieldType.minuteOfHour();
        DateTimeFieldType dateTimeFieldTypeSecond = DateTimeFieldType.secondOfMinute();
        Partial partial = new Partial(new DateTimeFieldType[]{dateTimeFieldTypeHour, dateTimeFieldTypeMinute}, new int[]{12, 30}, chronology);
        int wrapValue = 60;
        Partial newPartial = partial.withFieldAddWrapped(dateTimeFieldTypeSecond, wrapValue);
        assertEquals(newPartial.size(), partial.size() + 1);
        assertEquals(newPartial.getValue(partial.size() - 1), wrapValue);
    }

    @Test
    public void testPartialWithAddPeriod() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateTimeFieldTypeHour = DateTimeFieldType.hourOfDay();
        DateTimeFieldType dateTimeFieldTypeMinute = DateTimeFieldType.minuteOfHour();
        Partial partial = new Partial(new DateTimeFieldType[]{dateTimeFieldTypeHour, dateTimeFieldTypeMinute}, new int[]{12, 30}, chronology);
        LocalTime localTime = new LocalTime(13, 32, 0);
        ReadablePeriod readablePeriod = new GeneralLocalTime(localTime).toPeriod();
        Partial newPartial = partial.withPeriodAdded(readablePeriod, 1);
        assertEquals(newPartial.getValues()[0], 13);
        assertEquals(newPartial.getValues()[1], 32);
    }

    @Test
    public void testPartialWithMatch() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateTimeFieldTypeHour = DateTimeFieldType.hourOfDay();
        DateTimeFieldType dateTimeFieldTypeMinute = DateTimeFieldType.minuteOfHour();
        Partial partial = new Partial(new DateTimeFieldType[]{dateTimeFieldTypeHour, dateTimeFieldTypeMinute}, new int[]{12, 30}, chronology);
        LocalDateTime localDateTime = new LocalDateTime(2022, 1, 1, 12, 30);
        assertTrue(partial.isMatch(localDateTime.toDateTimeAtStartOfDay()));
    }

    @Test
    public void testPartialWithToString() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateTimeFieldTypeHour = DateTimeFieldType.hourOfDay();
        DateTimeFieldType dateTimeFieldTypeMinute = DateTimeFieldType.minuteOfHour();
        Partial partial = new Partial(new DateTimeFieldType[]{dateTimeFieldTypeHour, dateTimeFieldTypeMinute}, new int[]{12, 30}, chronology);
        assertEquals("[hourOfDay=12, minuteOfHour=30]", partial.toString());
    }

    @Test
    public void testPartialWithGetFormatter() {
        Chronology chronology = DateTimeUtils.forUTC();
        DateTimeFieldType dateTimeFieldTypeHour = DateTimeFieldType.hourOfDay();
        DateTimeFieldType dateTimeFieldTypeMinute = DateTimeFieldType.minuteOfHour();
        Partial partial = new Partial(new DateTimeFieldType[]{dateTimeFieldTypeHour, dateTimeFieldTypeMinute}, new int[]{12, 30}, chronology);
        DateTimeFormatter formatter = partial.getFormatter();
        assertNotNull(formatter);
    }
}