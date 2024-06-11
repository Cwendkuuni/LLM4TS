package org.junit.tests; // replace with your testing package name if needed

import static org.junit.Assert.*;
import org.apache.commons.lang3.BooleanUtils;

public class BooleanUtilsTest
{
    @Test
    public void testNegate()
    {
        assertNull(BooleanUtils.negate(null));
        assertTrue(BooleanUtils.negate(Boolean.TRUE) == Boolean.FALSE);
        assertFalse(BooleanUtils.negate(Boolean.FALSE) == Boolean.TRUE);
    }

    @Test
    public void testIsTrue()
    {
        assertFalse(BooleanUtils.isTrue(null));
        assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
        assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
    }

    @Test
    public void testIsNotTrue()
    {
        assertFalse(BooleanUtils.isNotTrue(null));
        assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
        assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
    }

    @Test
    public void testIsFalse()
    {
        assertFalse(BooleanUtils.isFalse(null));
        assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
        assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
    }

    @Test
    public void testIsNotFalse()
    {
        assertFalse(BooleanUtils.isNotFalse(null));
        assertTrue(BooleanUtils.isNotFalse(Boolean.FALSE));
        assertFalse(BooleanUtils.isNotFalse(Boolean.TRUE));
    }

    @Test
    public void testToBoolean()
    {
        assertFalse(BooleanUtils.toBoolean(null));
        assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        assertFalse(BooleanUtils.toBoolean(0));
        assertTrue(BooleanUtils.toBoolean(1));
        assertFalse(BooleanUtils.toBoolean(-1));
        try {
            BooleanUtils.toBoolean("invalid");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The String did not match any specified value", ex.getMessage());
        }
    }

    @Test
    public void testToBooleanDefaultIfNull()
    {
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
    }

    @Test
    public void testToBooleanObject()
    {
        assertNull(BooleanUtils.toBooleanObject(null));
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1)));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0)));
    }

    @Test
    public void testToBooleanWithInteger()
    {
        assertFalse(BooleanUtils.toBoolean(null, 0, 1));
        assertTrue(BooleanUtils.toBoolean(Integer.valueOf(1), 1, 0));
        assertFalse(BooleanUtils.toBoolean(Integer.valueOf(0), 1, 0));
    }

    @Test
    public void testToBooleanObjectWithIntegerAndNullValue()
    {
        assertNull(BooleanUtils.toBooleanObject(null, 1, 0, null));
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1), 1, 0, null));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0), 1, 0, null));
    }

    // Add test cases for other methods like isFalse, isNotFalse, toBoolean, toBooleanDefaultIfNull, toInteger, toIntegerObject, toIntegerObjectWithNullableBoolean, and toString methods

    // Test edge cases and exception handling scenarios

    @Test(expected = NullPointerException.class)
    public void testNegateNullParameter()
    {
        BooleanUtils.negate(null);
    }

    @Test(expected = NullPointerException.class)
    public void testIsTrueNullParameter()
    {
        BooleanUtils.isTrue(null);
    }

    @Test(expected = NullPointerException.class)
    public void testIsNotTrueNullParameter()
    {
        BooleanUtils.isNotTrue(null);
    }

    @Test(expected = NullPointerException.class)
    public void testIsFalseNullParameter()
    {
        BooleanUtils.isFalse(null);
    }

    @Test(expected = NullPointerException.class)
    public void testIsNotFalseNullParameter()
    {
        BooleanUtils.isNotFalse(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanIllegalArgument()
    {
        BooleanUtils.toBoolean("invalid");
    }

    @Test(expected = NullPointerException.class)
    public void testToBooleanObjectNullArray()
    {
        Boolean[] array = null;
        BooleanUtils.and(array);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanObjectNullElement()
    {
        Boolean[] array = new Boolean[] { null };
        BooleanUtils.and(array);
    }

    // Add test cases for other methods like toInteger, toIntegerObject, toBooleanObject, and toString methods with edge cases and exception handling scenarios

    // Test all and, or, xor methods with empty and null arrays

    @Test(expected = IllegalArgumentException.class)
    public void testAndEmptyArray()
    {
        BooleanUtils.and();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAndNullArray()
    {
        BooleanUtils.and(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOrEmptyArray()
    {
        BooleanUtils.or();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOrNullArray()
    {
        BooleanUtils.or(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXorEmptyArray()
    {
        BooleanUtils.xor();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXorNullArray()
    {
        BooleanUtils.xor(null);
    }

    // Add test cases for other methods like and, or, xor with edge cases and exception handling scenarios
}