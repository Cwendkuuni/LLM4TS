package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.NumberUtils;

import java.lang.IllegalArgumentException;
import java.lang.reflect.Array;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        assertTrue(BooleanUtils.negate(Boolean.TRUE) == Boolean.FALSE);
        assertTrue(BooleanUtils.negate(Boolean.FALSE) == Boolean.TRUE);
        assertNull(BooleanUtils.negate(null));
    }

    @Test
    public void testIsTrue() {
        assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
        assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
        assertFalse(BooleanUtils.isTrue(null));
    }

    @Test
    public void testIsNotTrue() {
        assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
        assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
        assertFalse(BooleanUtils.isNotTrue(null));
    }

    @Test
    public void testIsFalse() {
        assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
        assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
        assertFalse(BooleanUtils.isFalse(null));
    }

    @Test
    public void testIsNotFalse() {
        assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
        assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
        assertFalse(BooleanUtils.isNotFalse(null));
    }

    @Test
    public void testToBoolean() {
        assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        assertFalse(BooleanUtils.toBoolean(null));
        assertFalse(BooleanUtils.toBoolean(0));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
    }

    @Test
    public void testToBooleanWithInteger() {
        assertTrue(BooleanUtils.toBoolean(1));
        assertFalse(BooleanUtils.toBoolean(0));
        assertFalse(BooleanUtils.toBoolean(-1));
        assertNull(BooleanUtils.toBoolean(Integer.MIN_VALUE));
    }

    @Test
    public void testToBooleanObjectWithInteger() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ONE));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ZERO));
        assertNull(BooleanUtils.toBooleanObject(null));
    }

    @Test
    public void testToBooleanWithBooleanArray() {
        assertFalse(BooleanUtils.and(new boolean[0]));
        assertTrue(BooleanUtils.and(new boolean[]{true, true}));
        assertTrue(BooleanUtils.and(new boolean[]{true, false}));
        assertFalse(BooleanUtils.and(new boolean[]{false, true}));
        assertFalse(BooleanUtils.and(new boolean[]{false, false}));
        try {
            BooleanUtils.and(new boolean[]{true, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testToBooleanWithBooleanArray2() {
        Boolean[] booleanArray = new Boolean[3];
        booleanArray[0] = Boolean.TRUE;
        booleanArray[1] = Boolean.FALSE;
        booleanArray[2] = null;
        try {
            BooleanUtils.and(booleanArray);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }

    @Test
    public void testToBooleanWithBooleanArray3() {
        boolean[] booleanPrimitives = {true, true, true};
        assertTrue(BooleanUtils.and(BooleanUtils.toBooleanArray(booleanPrimitives)));
    }

    @Test
    public void testToBooleanWithBooleanArray4() {
        boolean[] booleanPrimitives = {true, false, true};
        assertFalse(BooleanUtils.and(BooleanUtils.toBooleanArray(booleanPrimitives)));
    }

    @Test
    public void testToBooleanWithBooleanArray5() {
        boolean[] booleanPrimitives = {false, false, false};
        assertFalse(BooleanUtils.and(BooleanUtils.toBooleanArray(booleanPrimitives)));
    }

    @Test
    public void testToBooleanWithBooleanArray6() {
        boolean[] booleanPrimitives = {false, true, null};
        try {
            BooleanUtils.and(BooleanUtils.toBooleanArray(booleanPrimitives));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testOr() {
        assertFalse(BooleanUtils.or(new boolean[0]));
        assertTrue(BooleanUtils.or(new boolean[]{true, false}));
        assertTrue(BooleanUtils.or(new boolean[]{false, true}));
        assertTrue(BooleanUtils.or(new boolean[]{true, true}));
        try {
            BooleanUtils.or(new boolean[]{true, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testOrWithBooleanArray2() {
        Boolean[] booleanArray = new Boolean[3];
        booleanArray[0] = Boolean.TRUE;
        booleanArray[1] = Boolean.FALSE;
        booleanArray[2] = null;
        try {
            BooleanUtils.or(booleanArray);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }

    @Test
    public void testOrWithBooleanArray3() {
        boolean[] booleanPrimitives = {true, true, true};
        assertTrue(BooleanUtils.or(BooleanUtils.toBooleanArray(booleanPrimitives)));
    }

    @Test
    public void testOrWithBooleanArray4() {
        boolean[] booleanPrimitives = {true, false, true};
        assertTrue(BooleanUtils.or(BooleanUtils.toBooleanArray(booleanPrimitives)));
    }

    @Test
    public void testOrWithBooleanArray5() {
        boolean[] booleanPrimitives = {false, false, false};
        assertFalse(BooleanUtils.or(BooleanUtils.toBooleanArray(booleanPrimitives)));
    }

    @Test
    public void testOrWithBooleanArray6() {
        boolean[] booleanPrimitives = {false, true, null};
        try {
            BooleanUtils.or(BooleanUtils.toBooleanArray(booleanPrimitives));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testXor() {
        assertFalse(BooleanUtils.xor(new boolean[0]));
        assertTrue(BooleanUtils.xor(new boolean[]{true, true}));
        assertTrue(BooleanUtils.xor(new boolean[]{true, false}));
        assertTrue(BooleanUtils.xor(new boolean[]{false, true}));
        assertTrue(BooleanUtils.xor(new boolean[]{false, false}));
        assertFalse(BooleanUtils.xor(new boolean[]{true, true}));
        try {
            BooleanUtils.xor(new boolean[]{true, null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testXorWithBooleanArray2() {
        Boolean[] booleanArray = new Boolean[3];
        booleanArray[0] = Boolean.TRUE;
        booleanArray[1] = Boolean.FALSE;
        booleanArray[2] = null;
        try {
            BooleanUtils.xor(booleanArray);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
    }

    @Test
    public void testXorWithBooleanArray3() {
        boolean[] booleanPrimitives = {true, false, true};
        assertTrue(BooleanUtils.xor(BooleanUtils.toBooleanArray(booleanPrimitives)));
    }

    @Test
    public void testXorWithBooleanArray4() {
        boolean[] booleanPrimitives = {true, true, true};
        assertFalse(BooleanUtils.xor(BooleanUtils.toBooleanArray(booleanPrimitives)));
    }

    @Test
    public void testXorWithBooleanArray5() {
        boolean[] booleanPrimitives = {false, false, false};
        assertFalse(BooleanUtils.xor(BooleanUtils.toBooleanArray(booleanPrimitives)));
    }

    @Test
    public void testXorWithBooleanArray6() {
        boolean[] booleanPrimitives = {false, true, null};
        try {
            BooleanUtils.xor(BooleanUtils.toBooleanArray(booleanPrimitives));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testToStringTrueFalse() {
        assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
        assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
        assertEquals("false", BooleanUtils.toStringTrueFalse(null));
    }

    @Test
    public void testToStringOnOff() {
        assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
        assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
        assertEquals("off", BooleanUtils.toStringOnOff(null));
    }

    @Test
    public void testToStringYesNo() {
        assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
        assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
        assertEquals("no", BooleanUtils.toStringYesNo(null));
    }

    @Test
    public void testToStringWithStringArray() {
        assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false", null));
        assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false", null));
        assertEquals("false", BooleanUtils.toString(null, "true", "false", null));
        assertEquals("false", BooleanUtils.toString(Boolean.TRUE, "yes", "no", null));
        assertEquals("yes", BooleanUtils.toString(Boolean.FALSE, "yes", "no", null));
        assertEquals("yes", BooleanUtils.toString(null, "yes", "no", null));
    }

    @Test
    public void testToStringWithStringArray2() {
        assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
        assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
        assertEquals("false", BooleanUtils.toString(null, "true", "false"));
    }

    @Test
    public void testToStringWithStringArray3() {
        assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "on", "off"));
        assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "on", "off"));
        assertEquals("false", BooleanUtils.toString(null, "on", "off"));
    }

    @Test
    public void testToStringWithStringArray4() {
        assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "yes", "no"));
        assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "yes", "no"));
        assertEquals("false", BooleanUtils.toString(null, "yes", "no"));
    }
}