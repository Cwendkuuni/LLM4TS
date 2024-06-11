package org.joda.time;

import static org.junit.Assert.*;

import org.junit.Test;

public class PartialTest {

    @Test
    public void testSize() {
        Partial partial = new Partial();
        assertEquals(0, partial.size());

        partial = new Partial(DateTimeFieldType.year(), 2022);
        assertEquals(1, partial.size());

        partial = new Partial(DateTimeFieldType.year(), 2022, DateTimeUtils.UTC);
        assertEquals(1, partial.size());

        partial = new Partial(new DateTimeFieldType[] {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()}, new int[] {2022, 12});
        assertEquals(2, partial.size());
    }

    @Test
    public void testGetChronology() {
        Partial partial = new Partial();
        assertEquals(DateTimeUtils.UTC, partial.getChronology());

        partial = new Partial(DateTimeUtils.UTC);
        assertEquals(DateTimeUtils.UTC, partial.getChronology());
    }

    @Test
    public void testGetField() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        assertEquals(DateTimeFieldType.year(), partial.getFieldType(0));

        DateTimeField field = partial.getField(0, DateTimeUtils.UTC);
        assertEquals(2022, field.get(DateTimeUtils.UTC, 0L));
    }

    @Test
    public void testGetValues() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        int[] values = partial.getValues();
        assertEquals(1, values.length);
        assertEquals(2022, values[0]);

        partial = new Partial(new DateTimeFieldType[] {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()}, new int[] {2022, 12});
        values = partial.getValues();
        assertEquals(2, values.length);
        assertEquals(2022, values[0]);
        assertEquals(12, values[1]);
    }

    @Test
    public void testWithChronologyRetainFields() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022, DateTimeUtils.UTC);
        Partial newPartial = partial.withChronologyRetainFields(DateTimeUtils.UTC);
        assertEquals(partial, newPartial);

        newPartial = partial.withChronologyRetainFields(DateTimeUtils.ISO);
        assertNotEquals(partial, newPartial);
    }

    @Test
    public void testWith() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        Partial newPartial = partial.with(DateTimeFieldType.year(), 2023);
        assertEquals(1, newPartial.size());
        assertEquals(2023, newPartial.getValue(0));

        newPartial = partial.with(DateTimeFieldType.monthOfYear(), 12, DateTimeFieldType.monthOfYear(), 1);
        assertEquals(2, newPartial.size());
        assertEquals(12, newPartial.getValue(1));
        assertEquals(1, newPartial.getValue(0));
    }

    @Test
    public void testWithout() {
        Partial partial = new Partial(new DateTimeFieldType[] {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()}, new int[] {2022, 12});
        Partial newPartial = partial.without(DateTimeFieldType.monthOfYear());
        assertEquals(1, newPartial.size());
        assertEquals(2022, newPartial.getValue(0));
    }

    @Test
    public void testWithField() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        Partial newPartial = partial.withField(DateTimeFieldType.year(), 2023);
        assertEquals(1, newPartial.size());
        assertEquals(2023, newPartial.getValue(0));

        newPartial = partial.withField(DateTimeFieldType.monthOfYear(), 12);
        assertEquals(1, newPartial.size());
        assertEquals(12, newPartial.getValue(0));
    }

    @Test
    public void testWithFieldAdded() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        Partial newPartial = partial.withFieldAdded(DateTimeFieldType.dayOfMonth(), 31);
        assertEquals(2, newPartial.size());
        assertEquals(31, newPartial.getValue(1));
        assertEquals(2022, newPartial.getValue(0));
    }

    @Test
    public void testWithFieldAddWrapped() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        Partial newPartial = partial.withFieldAddWrapped(DateTimeFieldType.dayOfWeek(), 1);
        assertEquals(3, newPartial.size());
        assertEquals(1, newPartial.getValue(2));
        assertEquals(2022, newPartial.getValue(0));
        assertEquals(7, newPartial.getValue(1)); //default value for dayOfWeek
    }

    @Test
    public void testWithPeriodAdded() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        Partial newPartial = partial.withPeriodAdded(Period.days(1));
        assertEquals(1, newPartial.size());
        assertEquals(2023, newPartial.getValue(0));

        newPartial = partial.withPeriodAdded(Period.days(-1).withHours(2));
        assertEquals(1, newPartial.size());
        assertEquals(2021, newPartial.getValue(0));
    }

    @Test
    public void testPlus() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        Partial newPartial = partial.plus(Period.days(1));
        assertEquals(1, newPartial.size());
        assertEquals(2023, newPartial.getValue(0));
    }

    @Test
    public void testMinus() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2023);
        Partial newPartial = partial.minus(Period.days(1));
        assertEquals(1, newPartial.size());
        assertEquals(2022, newPartial.getValue(0));
    }

    @Test
    public void testProperty() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        Property property = partial.property(DateTimeFieldType.year());
        assertEquals(DateTimeFieldType.year(), property.getField());
        assertEquals(2022, property.get());
    }

    @Test
    public void testIsMatch() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        Instant instant = new DateTime(2022, 12, 31, 23, 59, 59, 999).toInstant();
        assertTrue(partial.isMatch(new DateTime(instant)));

        partial = new Partial(DateTimeFieldType.year(), 2023);
        assertFalse(partial.isMatch(new DateTime(2022, 12, 31, 23, 59, 59, 999).toInstant()));
    }

    @Test
    public void testGetFormatter() {
        Partial partial = new Partial(new DateTimeFieldType[] {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()}, new int[] {2022, 12});
        DateTimeFormatter formatter = partial.getFormatter();
        assertEquals("yyyy-MM", formatter.pattern());
    }

    @Test
    public void testToString() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        assertEquals("[year=2022]", partial.toString());

        partial = new Partial(new DateTimeFieldType[] {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()}, new int[] {2022, 12});
        assertEquals("[year=2022, monthOfYear=12]", partial.toString());
    }

    @Test
    public void testToStringList() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        assertEquals("[year=2022]", partial.toStringList());

        partial = new Partial(new DateTimeFieldType[] {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()}, new int[] {2022, 12});
        assertEquals("[year=2022, monthOfYear=12]", partial.toStringList());
    }

    @Test
    public void testToStringWithFormat() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        assertEquals("yyyy", partial.toString("yyyy"));
        assertEquals("yyyyyyyy", partial.toString("yyyyyyyy"));
    }

    @Test
    public void testToStringWithLocale() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        assertEquals("2022", partial.toString("yyyy", Locale.US));
        assertEquals("2022", partial.toString("yyyy", Locale.CHINA));
    }
}