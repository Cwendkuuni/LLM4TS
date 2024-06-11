package org.junit.tests; // replace with your test package if needed

import org.apache.commons.lang3.BooleanUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

    // Test cases for negate method
    @Test
    public void testNegateTrue() {
        assertTrue(BooleanUtils.negate(Boolean.TRUE) == Boolean.FALSE);
    }

    @Test
    public void testNegateFalse() {
        assertTrue(BooleanUtils.negate(Boolean.FALSE) == Boolean.TRUE);
    }

    @Test
    public void testNegateNull() {
        assertNull(BooleanUtils.negate(null));
    }

    // Test cases for isTrue method
    @Test
    public void testIsTrueTrue() {
        assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
    }

    @Test
    public void testIsTrueFalse() {
        assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
    }

    @Test
    public void testIsTrueNull() {
        assertFalse(BooleanUtils.isTrue(null));
    }

    // Test cases for isNotTrue method
    @Test
    public void testIsNotTrueTrue() {
        assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
    }

    @Test
    public void testIsNotTrueFalse() {
        assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
    }

    @Test
    public void testIsNotTrueNull() {
        assertFalse(BooleanUtils.isNotTrue(null));
    }

    // Test cases for isFalse method
    @Test
    public void testIsFalseFalse() {
        assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
    }

    @Test
    public void testIsFalseTrue() {
        assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
    }

    @Test
    public void testIsFalseNull() {
        assertFalse(BooleanUtils.isFalse(null));
    }

    // Test cases for isNotFalse method
    @Test
    public void testIsNotFalseFalse() {
        assertTrue(BooleanUtils.isNotFalse(Boolean.FALSE));
    }

    @Test
    public void testIsNotFalseTrue() {
        assertFalse(BooleanUtils.isNotFalse(Boolean.TRUE));
    }

    @Test
    public void testIsNotFalseNull() {
        assertFalse(BooleanUtils.isNotFalse(null));
    }

    // Test cases for toBoolean method with Boolean argument
    @Test
    public void testToBooleanTrue() {
        assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
    }

    @Test
    public void testToBooleanFalse() {
        assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
    }

    @Test
    public void testToBooleanNull() {
        assertNull(BooleanUtils.toBoolean(null));
    }

    // Test cases for toBoolean method with int argument
    @Test
    public void testToBooleanIntTrue() {
        assertTrue(BooleanUtils.toBoolean(1));
    }

    @Test
    public void testToBooleanIntFalse() {
        assertFalse(BooleanUtils.toBoolean(0));
    }

    @Test
    public void testToBooleanIntNegative() {
        assertFalse(BooleanUtils.toBoolean(-1));
    }

    // Test cases for toBooleanDefaultIfNull method with Boolean argument
    @Test
    public void testToBooleanDefaultIfNullTrue() {
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
    }

    @Test
    public void testToBooleanDefaultIfNullFalse() {
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
    }

    @Test
    public void testToBooleanDefaultIfNullFalseValueNull() {
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, true));
    }

    @Test
    public void testToBooleanDefaultIfNullTrueValueNull() {
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, false));
    }

    // Test cases for toBooleanDefaultIfNull method with int argument
    @Test
    public void testToBooleanDefaultIfNullIntTrue() {
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(1, true));
    }

    @Test
    public void testToBooleanDefaultIfNullIntFalse() {
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(0, true));
    }

    @Test
    public void testToBooleanDefaultIfNullIntFalseValueNull() {
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(0, null));
    }

    @Test
    public void testToBooleanDefaultIfNullIntTrueValueNull() {
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, false));
    }

    // Test cases for edge cases and exceptions
    @Test(expected = NullPointerException.class)
    public void testToBooleanNullArray() {
        Boolean[] nullArray = null;
        BooleanUtils.toBoolean(nullArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanArrayNegativeSize() {
        boolean[] array = new boolean[ -1 ];
        assertFalse(BooleanUtils.toBoolean(array));
    }

    @Test(expected = NullPointerException.class)
    public void testToBooleanArrayNullElement() {
        boolean[] array = { true, null, true };
        assertFalse(BooleanUtils.toBoolean(array));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanArrayMismatchedTypes() {
        Object[] array = { true, "false" };
        assertFalse(BooleanUtils.toBoolean(array));
    }

    @Test
    public void testToStringTrueFalse() {
        assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
        assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
    }

    @Test
    public void testToStringOnOff() {
        assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
        assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
    }

    @Test
    public void testToStringYesNo() {
        assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
        assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
    }

}