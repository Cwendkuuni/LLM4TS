package org.junit.test;

import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.format.*;
import org.joda.time.field.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PartialTest {

    @Test
    public void testEmptyConstructor() {
        Partial partial = new Partial();
        assertEquals(0, partial.size());
    }

    @Test
    public void testConstructorWithNullChronology() {
        try {
            new Partial(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testConstructorWithChronology() {
        Partial partial = new Partial(DateTimeUtils.UTC);
        assertEquals(0, partial.size());
    }

    @Test
    public void testConstructorWithDateTimeFieldTypeAndInt() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        assertEquals(1, partial.size());
        assertEquals(DateTimeFieldType.year(), partial.getFieldTypes()[0]);
        assertEquals(2022, partial.getValue(0));
    }

    @Test
    public void testConstructorWithDateTimeFieldTypeArrayAndIntArray() {
        DateTimeFieldType[] types = {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth()};
        int[] values = {2022, 11, 30};
        Partial partial = new Partial(types, values);
        assertEquals(3, partial.size());
        assertEquals(DateTimeFieldType.year(), partial.getFieldTypes()[0]);
        assertEquals(DateTimeFieldType.monthOfYear(), partial.getFieldTypes()[1]);
        assertEquals(DateTimeFieldType.dayOfMonth(), partial.getFieldTypes()[2]);
        assertEquals(2022, partial.getValue(0));
        assertEquals(11, partial.getValue(1));
        assertEquals(30, partial.getValue(2));
    }

    @Test
    public void testConstructorWithReadablePartial() {
        Partial partial1 = new Partial(new DateTime(2022, 11, 30));
        Partial partial2 = new Partial(partial1);
        assertEquals(partial1.size(), partial2.size());
        for (int i = 0; i < partial1.size(); ++i) {
            assertEquals(partial1.getFieldTypes()[i], partial2.getFieldTypes()[i]);
            assertEquals(partial1.getValue(i), partial2.getValue(i));
        }
    }

    @Test
    public void testSize() {
        Partial partial = new Partial();
        Partial partialWithFields = new Partial(DateTimeFieldType.year(), 2022);
        assertEquals(0, partial.size());
        assertEquals(1, partialWithFields.size());
    }

    @Test
    public void testGetChronology() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        assertEquals(DateTimeUtils.UTC, partial.getChronology());
    }

    @Test
    public void testGetField() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        DateTimeField yearField = partial.getField(0, DateTimeUtils.UTC);
        assertEquals(DateTimeFieldType.year(), yearField.getFieldType());
        assertEquals(2022, yearField.get(DateTimeUtils.UTC, 2022));
    }

    @Test
    public void testGetFieldType() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        assertEquals(DateTimeFieldType.year(), partial.getFieldType(0));
    }

    @Test
    public void testGetFieldTypes() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022, DateTimeFieldType.monthOfYear(), 11);
        DateTimeFieldType[] fieldTypes = partial.getFieldTypes();
        assertEquals(DateTimeFieldType.year(), fieldTypes[0]);
        assertEquals(DateTimeFieldType.monthOfYear(), fieldTypes[1]);
        assertArrayEquals(new DateTimeFieldType[]{DateTimeFieldType.year(), DateTimeFieldType.monthOfYear()}, fieldTypes);
    }

    @Test
    public void testGetValues() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        int[] values = partial.getValues();
        assertEquals(1, values.length);
        assertEquals(2022, values[0]);
    }

    @Test
    public void testWithChronologyRetainFields() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        Partial newPartial = partial.withChronologyRetainFields(DateTimeUtils.ISO);
        assertNotSame(partial, newPartial);
        assertEquals(1, newPartial.size());
        assertEquals(DateTimeFieldType.year(), newPartial.getFieldTypes()[0]);
        assertEquals(2022, newPartial.getValue(0));
        assertEquals(DateTimeUtils.ISO, newPartial.getChronology());
    }

    @Test
    public void testWith() {
        Partial partial = new Partial();
        Partial newPartial = partial.with(DateTimeFieldType.year(), 2022);
        assertNotSame(partial, newPartial);
        assertEquals(1, newPartial.size());
        assertEquals(DateTimeFieldType.year(), newPartial.getFieldTypes()[0]);
        assertEquals(2022, newPartial.getValue(0));
    }

    @Test
    public void testWithout() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022, DateTimeFieldType.monthOfYear(), 11);
        Partial newPartial = partial.without(DateTimeFieldType.monthOfYear());
        assertNotSame(partial, newPartial);
        assertEquals(1, newPartial.size());
        assertEquals(DateTimeFieldType.year(), newPartial.getFieldTypes()[0]);
        assertEquals(2022, newPartial.getValue(0));
    }

    @Test
    public void testWithField() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        Partial newPartial = partial.withField(DateTimeFieldType.monthOfYear(), 11);
        assertNotSame(partial, newPartial);
        assertEquals(2, newPartial.size());
        assertEquals(DateTimeFieldType.year(), newPartial.getFieldTypes()[0]);
        assertEquals(DateTimeFieldType.monthOfYear(), newPartial.getFieldTypes()[1]);
        assertEquals(2022, newPartial.getValue(0));
        assertEquals(11, newPartial.getValue(1));
    }

    @Test
    public void testWithFieldAdded() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022, DateTimeFieldType.monthOfYear(), 11);
        Partial newPartial = partial.withFieldAdded(DateTimeFieldType.dayOfMonth(), 31);
        assertNotSame(partial, newPartial);
        assertEquals(3, newPartial.size());
        assertEquals(DateTimeFieldType.year(), newPartial.getFieldTypes()[0]);
        assertEquals(DateTimeFieldType.monthOfYear(), newPartial.getFieldTypes()[1]);
        assertEquals(DateTimeFieldType.dayOfMonth(), newPartial.getFieldTypes()[2]);
        assertEquals(2022, newPartial.getValue(0));
        assertEquals(11, newPartial.getValue(1));
        assertEquals(31, newPartial.getValue(2));
    }

    @Test
    public void testWithFieldAddWrapped() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022, DateTimeFieldType.monthOfYear(), 11);
        Partial newPartial = partial.withFieldAddWrapped(DateTimeFieldType.dayOfMonth(), 32);
        assertEquals(3, partial.size());
        assertEquals(3, newPartial.size());
        assertEquals(DateTimeFieldType.year(), newPartial.getFieldTypes()[0]);
        assertEquals(DateTimeFieldType.monthOfYear(), newPartial.getFieldTypes()[1]);
        assertEquals(DateTimeFieldType.dayOfMonth(), newPartial.getFieldTypes()[2]);
        assertEquals(2022, newPartial.getValue(0));
        assertEquals(11, newPartial.getValue(1));
        assertEquals(1, newPartial.getValue(2));
    }

    @Test
    public void testWithPeriodAdded() {
        Partial partial = new Partial();
        ReadablePeriod period = new GeneralMillis(System.currentTimeMillis() - 3600000);
        Partial newPartial = partial.withPeriodAdded(period, 1);
        assertNotSame(partial, newPartial);
        assertEquals(partial.size(), newPartial.size());
        for (int i = 0; i < partial.size(); ++i) {
            assertEquals(partial.getFieldTypes()[i], newPartial.getFieldTypes()[i]);
            assertEquals(partial.getValue(i), newPartial.getValue(i));
        }
    }

    @Test
    public void testPlus() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        Partial newPartial = partial.plus(new GeneralMillis(3600000));
        assertNotSame(partial, newPartial);
        assertEquals(1, newPartial.size());
        assertEquals(DateTimeFieldType.year(), newPartial.getFieldTypes()[0]);
        assertEquals(2023, newPartial.getValue(0));
    }

    @Test
    public void testMinus() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2023);
        Partial newPartial = partial.minus(new GeneralMillis(3600000));
        assertNotSame(partial, newPartial);
        assertEquals(1, newPartial.size());
        assertEquals(DateTimeFieldType.year(), newPartial.getFieldTypes()[0]);
        assertEquals(2022, newPartial.getValue(0));
    }

    @Test
    public void testProperty() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        Property property = new Property(partial, 0);
        assertEquals(DateTimeFieldType.year(), property.getField().getFieldType());
        assertEquals(2022, property.get());
        Partial newPartial = property.setCopy(2023);
        assertNotSame(partial, newPartial);
        assertEquals(1, newPartial.size());
        assertEquals(DateTimeFieldType.year(), newPartial.getFieldTypes()[0]);
        assertEquals(2023, newPartial.getValue(0));
    }

    @Test
    public void testIsMatch() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        boolean matches = partial.isMatch(new DateTime(2022, 11, 30));
        assertTrue(matches);
    }

    @Test
    public void testIsMatchWithNullReadableInstant() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        assertFalse(partial.isMatch(null));
    }

    @Test
    public void testGetFormatter() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        DateTimeFormatter formatter = partial.getFormatter();
        assertNotNull(formatter);
    }

    @Test
    public void testToString() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        assertEquals("[year=2022]", partial.toString());
    }

    @Test
    public void testToStringList() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022, DateTimeFieldType.monthOfYear(), 11);
        assertEquals("[year=2022, monthOfYear=11]", partial.toStringList());
    }

    @Test
    public void testToStringWithNullPattern() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        assertEquals("[year=2022]", partial.toString());
    }

    @Test
    public void testToStringWithLocale() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        assertEquals("[year=2022]", partial.toString("", Locale.US));
    }

    @Test
    public void testToStringWithPattern() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        assertEquals("year=2022", partial.toString("year='yyyy'"));
    }

    @Test
    public void testToStringWithPatternAndLocale() {
        Partial partial = new Partial(DateTimeFieldType.year(), 2022);
        assertEquals("yyyy", partial.toString("yyyy", Locale.US));
    }
}