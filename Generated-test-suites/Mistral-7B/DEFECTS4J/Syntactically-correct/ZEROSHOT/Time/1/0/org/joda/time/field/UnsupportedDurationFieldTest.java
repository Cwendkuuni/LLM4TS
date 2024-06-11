package org.joda.time.field.test;

import org.junit.Test;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertSame;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType. minutes();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(type);
        assertEquals(type, unsupported.getType());
        assertEquals(type.getName(), unsupported.getName());
        assertSame(unsupported, other);
    }

    @Test
    public void testIsSupported() {
        DurationFieldType type = DurationFieldType. minutes();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertFalse(unsupported.isSupported());
    }

    @Test
    public void testGetValue() {
        DurationFieldType type = DurationFieldType. minutes();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValue(1));
    }

    @Test
    public void testGetValueAsLong() {
        DurationFieldType type = DurationFieldType. minutes();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValueAsLong(1));
    }

    @Test
    public void testGetValue_long() {
        DurationFieldType type = DurationFieldType. minutes();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValue(1, 2));
    }

    @Test
    public void testGetValueAsLong_long() {
        DurationFieldType type = DurationFieldType. minutes();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getValueAsLong(1, 2));
    }

    @Test
    public void testGetMillis() {
        DurationFieldType type = DurationFieldType. minutes();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(1));
    }

    @Test
    public void testGetMillis_int() {
        DurationFieldType type = DurationFieldType. minutes();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(1));
    }

    @Test
    public void testGetMillis_long() {
        DurationFieldType type = DurationFieldType. minutes();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(1, 2));
    }

    @Test
    public void testGetMillis_long_long() {
        DurationFieldType type = DurationFieldType. minutes();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getMillis(1, 2));
    }

    @Test
    public void testAdd() {
        DurationFieldType type = DurationFieldType. minutes();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.add(1, 2));
    }

    @Test
    public void testAdd_long() {
        DurationFieldType type = DurationFieldType. minutes();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.add(1, 2));
    }

    @Test
    public void testGetDifference() {
        DurationFieldType type = DurationFieldType. minutes();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getDifference(1, 2));
    }

    @Test
    public void testGetDifferenceAsLong() {
        DurationFieldType type = DurationFieldType. minutes();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertThrows(UnsupportedOperationException.class, () -> unsupported.getDifferenceAsLong(1, 2));
    }

    @Test
    public void testCompareTo() {
        DurationFieldType typeA = DurationFieldType. minutes();
        DurationFieldType typeB = DurationFieldType. hours();
        UnsupportedDurationField unsupportedA = UnsupportedDurationField.getInstance(typeA);
        UnsupportedDurationField unsupportedB = UnsupportedDurationField.getInstance(typeB);
        assertEquals(1, unsupportedA.compareTo(unsupportedB));
    }

    @Test
    public void testEquals() {
        DurationFieldType type = DurationFieldType. minutes();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField otherType = UnsupportedDurationField.getInstance(DurationFieldType. hours());
        assertEquals(unsupported, unsupported);
        assertEquals(unsupported, other);
        assertNotEquals(unsupported, otherType);
    }

    @Test
    public void testHashCode() {
        DurationFieldType type = DurationFieldType. minutes();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals(unsupported.hashCode(), unsupported.hashCode());
        assertEquals(unsupported.hashCode(), UnsupportedDurationField.getInstance(type).hashCode());
        UnsupportedDurationField other = UnsupportedDurationField.getInstance(DurationFieldType. hours());
        assertNotEquals(unsupported.hashCode(), other.hashCode());
    }

    @Test
    public void testToString() {
        DurationFieldType type = DurationFieldType. minutes();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField[minutes]", unsupported.toString());
    }

    @Test
    public void testReadResolve() {
        DurationFieldType type = DurationFieldType. minutes();
        UnsupportedDurationField unsupported = new UnsupportedDurationField(type);
        assertSame(UnsupportedDurationField.getInstance(type), unsupported.readResolve());
    }

}