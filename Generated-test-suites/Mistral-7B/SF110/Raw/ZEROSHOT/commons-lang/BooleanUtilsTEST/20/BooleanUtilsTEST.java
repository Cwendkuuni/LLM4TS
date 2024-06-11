package org.apache.commons.lang3;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.IllegalArgumentException;
import java.lang.reflect.Array;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        Assert.assertNull(BooleanUtils.negate(null));
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.negate(Boolean.TRUE));
        Assert.assertEquals(Boolean.TRUE, BooleanUtils.negate(Boolean.FALSE));
    }

    @Test
    public void testIsTrue() {
        Assert.assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.isTrue(null));
    }

    @Test
    public void testIsNotTrue() {
        Assert.assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
        Assert.assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.isNotTrue(null));
    }

    @Test
    public void testIsFalse() {
        Assert.assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
        Assert.assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.isFalse(null));
    }

    @Test
    public void testIsNotFalse() {
        Assert.assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.isNotFalse(null));
    }

    @Test
    public void testToBoolean() {
        Assert.assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.toBoolean(null));
        Assert.assertFalse(BooleanUtils.toBoolean((Object) 0));
        Assert.assertTrue(BooleanUtils.toBoolean(1));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
        Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
        Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
    }

    @Test
    public void testToBooleanWithInteger() {
        Assert.assertTrue(BooleanUtils.toBoolean(Integer.valueOf(1)));
        Assert.assertFalse(BooleanUtils.toBoolean(Integer.valueOf(0)));
        Assert.assertNull(BooleanUtils.toBoolean(null));
    }

    @Test
    public void testToBooleanObjectWithInteger() {
        Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1)));
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0)));
        Assert.assertNull(BooleanUtils.toBooleanObject(null));
    }

    @Test
    public void testToBooleanWithIntegerIntegerInteger() {
        Assert.assertTrue(BooleanUtils.toBoolean(1, 1, 0));
        Assert.assertFalse(BooleanUtils.toBoolean(0, 1, 0));
        try {
            BooleanUtils.toBoolean(2, 1, 0);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match either specified value", ex.getMessage());
        }
    }

    @Test
    public void testToBooleanWithIntegerIntegerIntegerInteger() {
        Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0), null));
        Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0), null));
        assertNull(BooleanUtils.toBooleanObject(null, Integer.valueOf(1), Integer.valueOf(0), null));
        try {
            BooleanUtils.toBooleanObject(Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(0), null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match any specified value", ex.getMessage());
        }
    }

    @Test
    public void testToInteger() {
        Assert.assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
        Assert.assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
    }

    @Test
    public void testToIntegerObjectWithBoolean() {
        Assert.assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
        Assert.assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
    }

    @Test
    public void testToIntegerObjectWithNullableBoolean() {
        Assert.assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
        Assert.assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
        assertNull(BooleanUtils.toIntegerObject(null));
    }

    @Test
    public void testToIntegerWithBooleanBooleanBoolean() {
        Assert.assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0));
        Assert.assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0));
    }

    @Test
    public void testToIntegerWithBooleanBooleanBooleanBoolean() {
        Assert.assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO, null));
        Assert.assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO, null));
        assertNull(BooleanUtils.toIntegerObject(null, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO, null));
    }

    @Test
    public void testToIntegerWithBooleanIntegerInteger() {
        Assert.assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0));
        Assert.assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0));
    }

    @Test
    public void testToIntegerWithBooleanIntegerIntegerBoolean() {
        Assert.assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0, null));
        Assert.assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0, null));
        assertNull(BooleanUtils.toInteger(null, 1, 0, null));
    }

    @Test
    public void testToIntegerObjectWithBooleanIntegerInteger() {
        Assert.assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE, 1, 0));
        Assert.assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE, 1, 0));
    }

    @Test
    public void testToIntegerObjectWithBooleanIntegerIntegerBoolean() {
        Assert.assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO, null));
        Assert.assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO, null));
        assertNull(BooleanUtils.toIntegerObject(null, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO, null));
    }

    @Test
    public void testToStringTrueFalse() {
        Assert.assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
        Assert.assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
    }

    @Test
    public void testToStringOnOff() {
        Assert.assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
        Assert.assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
    }

    @Test
    public void testToStringYesNo() {
        Assert.assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
        Assert.assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
        Assert.assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
    }

    @Test
    public void testToStringWithBoolean() {
        Assert.assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
        Assert.assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
    }

    @Test
    public void testToStringWithBooleanBoolean() {
        Assert.assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
        Assert.assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
    }

    @Test
    public void testAnd() {
        Assert.assertTrue(BooleanUtils.and(new boolean[]{true, true, true, true}));
        Assert.assertFalse(BooleanUtils.and(new boolean[]{true, true, true, false}));
        Assert.assertFalse(BooleanUtils.and(new boolean[]{false, false, false, false}));
        try {
            BooleanUtils.and((boolean[]) null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
        try {
            BooleanUtils.and((boolean[]) Array.newInstance(boolean.class, 0));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Array is empty", ex.getMessage());
        }
    }

    @Test
    public void testAndWithBoolean() {
        try {
            BooleanUtils.and((Boolean[]) null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
        try {
            BooleanUtils.and((Boolean[]) Array.newInstance(Boolean.class, 0));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Array is empty", ex.getMessage());
        }
        Boolean[] booleanArray = new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
        Boolean result = BooleanUtils.and(booleanArray);
        Assert.assertEquals(Boolean.TRUE, result);
        booleanArray[2] = Boolean.FALSE;
        result = BooleanUtils.and(booleanArray);
        Assert.assertFalse(result);
    }

    @Test
    public void testOr() {
        Assert.assertTrue(BooleanUtils.or(new boolean[]{true, true, true, true}));
        Assert.assertTrue(BooleanUtils.or(new boolean[]{true, true, true, false}));
        Assert.assertFalse(BooleanUtils.or(new boolean[]{false, false, false, false}));
        try {
            BooleanUtils.or((boolean[]) null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
        try {
            BooleanUtils.or((boolean[]) Array.newInstance(boolean.class, 0));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Array is empty", ex.getMessage());
        }
    }

    @Test
    public void testOrWithBoolean() {
        try {
            BooleanUtils.or((Boolean[]) null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
        try {
            BooleanUtils.or((Boolean[]) Array.newInstance(Boolean.class, 0));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Array is empty", ex.getMessage());
        }
        Boolean[] booleanArray = new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
        Boolean result = BooleanUtils.or(booleanArray);
        Assert.assertEquals(Boolean.TRUE, result);
        booleanArray[2] = Boolean.FALSE;
        result = BooleanUtils.or(booleanArray);
        Assert.assertTrue(result);
    }

    @Test
    public void testXor() {
        Assert.assertFalse(BooleanUtils.xor(new boolean[]{true, true, true, true}));
        Assert.assertTrue(BooleanUtils.xor(new boolean[]{true, true, true, false}));
        Assert.assertFalse(BooleanUtils.xor(new boolean[]{false, false, false, false}));
        try {
            BooleanUtils.xor((boolean[]) null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
        try {
            BooleanUtils.xor((boolean[]) Array.newInstance(boolean.class, 0));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Array is empty", ex.getMessage());
        }
    }

    @Test
    public void testXorWithBoolean() {
        try {
            BooleanUtils.xor((Boolean[]) null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
        try {
            BooleanUtils.xor((Boolean[]) Array.newInstance(Boolean.class, 0));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Array is empty", ex.getMessage());
        }
        Boolean[] booleanArray = new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
        Boolean result = BooleanUtils.xor(booleanArray);
        Assert.assertFalse(result);
        booleanArray[2] = Boolean.FALSE;
        result = BooleanUtils.xor(booleanArray);
        Assert.assertTrue(result);
    }
}