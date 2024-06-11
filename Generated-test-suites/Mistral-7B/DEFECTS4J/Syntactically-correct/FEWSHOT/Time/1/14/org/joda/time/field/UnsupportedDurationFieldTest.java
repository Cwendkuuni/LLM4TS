package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.joda.time.DurationFieldType;
import org.junit.Test;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.INSTANCE;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField another = UnsupportedDurationField.getInstance(type);
        assertEquals(type, unsupported.getType());
        assertSame(unsupported, another);
    }

    @Test
    public void testGetName() {
        DurationFieldType type = DurationFieldType.INSTANCE;
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals("UnsupportedDurationField", unsupported.getName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsSupported() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        unsupported.isSupported();
    }

    @Test
    public void testIsPrecise() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        assertTrue(unsupported.isPrecise());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValue() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        unsupported.getValue(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValueAsLong() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        unsupported.getValueAsLong(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValueAtInstant() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        unsupported.getValue(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValueAsLongAtInstant() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        unsupported.getValueAsLong(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillis() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        unsupported.getMillis(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMillisAtInstant() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        unsupported.getMillis(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        unsupported.add(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAtInstant() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        unsupported.add(0, 0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifference() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        unsupported.getDifference(0, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetDifferenceAsLong() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        unsupported.getDifferenceAsLong(0, 0);
    }

    @Test
    public void testGetUnitMillis() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        assertEquals(0, unsupported.getUnitMillis());
    }

    @Test
    public void testCompareTo() {
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        assertEquals(1, unsupported1.compareTo(unsupported2));
    }

    @Test
    public void testEquals() {
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        UnsupportedDurationField unsupported3 = new UnsupportedDurationField(null);
        assertEquals(unsupported1, unsupported1);
        assertEquals(unsupported1, unsupported2);
        assertNotEquals(unsupported1, unsupported3);
    }

    @Test
    public void testHashCode() {
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        UnsupportedDurationField unsupported3 = new UnsupportedDurationField(null);
        assertEquals(unsupported1.hashCode(), unsupported1.hashCode());
        assertEquals(unsupported1.hashCode(), unsupported2.hashCode());
        assertNotEquals(unsupported1.hashCode(), unsupported3.hashCode());
    }

    @Test
    public void testToString() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.INSTANCE);
        assertEquals("UnsupportedDurationField[UnsupportedDurationField]", unsupported.toString());
    }

    @Test
    public void testReadResolve() {
        DurationFieldType type = DurationFieldType.INSTANCE;
        UnsupportedDurationField unsupported = new UnsupportedDurationField(type);
        assertEquals(type, unsupported.getType());
    }
}