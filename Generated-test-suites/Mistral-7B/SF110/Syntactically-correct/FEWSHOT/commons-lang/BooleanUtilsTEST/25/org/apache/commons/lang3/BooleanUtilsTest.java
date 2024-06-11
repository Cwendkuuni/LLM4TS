package org.apache.commons.lang3;

import static org.junit.Assert.*;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        assertEquals(Boolean.FALSE, BooleanUtils.negate(Boolean.TRUE));
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
        assertNull(BooleanUtils.isNotTrue(null));
    }

    @Test
    public void testIsFalse() {
        assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
        assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
        assertNull(BooleanUtils.isFalse(null));
    }

    @Test
    public void testIsNotFalse() {
        assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
        assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
        assertNull(BooleanUtils.isNotFalse(null));
    }

    @Test
    public void testToBoolean() {
        assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        assertNull(BooleanUtils.toBoolean(null));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
    }

    @Test
    public void testToBooleanWithInteger() {
        assertTrue(BooleanUtils.toBoolean(1));
        assertFalse(BooleanUtils.toBoolean(0));
        assertNull(BooleanUtils.toBoolean(-1));
    }

    @Test
    public void testToBooleanObjectWithInteger() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ONE));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ZERO));
        assertNull(BooleanUtils.toBooleanObject(null));
    }

    @Test
    public void testToBooleanWithValueAndDefault() {
        assertTrue(BooleanUtils.toBoolean(true, true, false));
        assertFalse(BooleanUtils.toBoolean(false, true, false));
        assertNull(BooleanUtils.toBoolean(null, true, false));
    }

    @Test
    public void testToBooleanWithNullValueAndDefault() {
        assertTrue(BooleanUtils.toBoolean(null, true, false));
        assertTrue(BooleanUtils.toBoolean(null, false, true));
        assertFalse(BooleanUtils.toBoolean(null, false, false));
    }

    @Test
    public void testToBooleanWithArray() {
        assertTrue(BooleanUtils.toBoolean(new boolean[]{true, true, true}));
        assertFalse(BooleanUtils.toBoolean(new boolean[]{false, false, false}));
        assertFalse(BooleanUtils.toBoolean(null));
    }

    @Test
    public void testToBooleanWithBooleanArray() {
        assertTrue(BooleanUtils.toBoolean(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        assertFalse(BooleanUtils.toBoolean(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}));
        assertNull(BooleanUtils.toBoolean(null));
    }

    @Test
    public void testToBooleanWithPrimitiveArray() {
        boolean[] primitives = ArrayUtils.toObject(new int[]{1, 1, 1});
        assertTrue(BooleanUtils.toBoolean(primitives));

        primitives = ArrayUtils.toObject(new int[]{0, 0, 0});
        assertFalse(BooleanUtils.toBoolean(primitives));

        primitives = new boolean[0];
        assertFalse(BooleanUtils.toBoolean(primitives));
    }

    @Test
    public void testToBooleanWithNullableBooleanArray() {
        assertTrue(BooleanUtils.toBoolean(new Boolean[]{Boolean.TRUE, null, Boolean.TRUE}));
        assertFalse(BooleanUtils.toBoolean(new Boolean[]{Boolean.FALSE, null, Boolean.FALSE}));
        assertNull(BooleanUtils.toBoolean(new Boolean[]{null, null, null}));
    }

    @Test
    public void testToStringTrueFalse() {
        assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
        assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
        assertEquals(StringUtils.EMPTY, BooleanUtils.toStringTrueFalse(null));
    }

    @Test
    public void testToStringOnOff() {
        assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
        assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
        assertEquals(StringUtils.EMPTY, BooleanUtils.toStringOnOff(null));
    }

    @Test
    public void testToStringYesNo() {
        assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
        assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
        assertEquals(StringUtils.EMPTY, BooleanUtils.toStringYesNo(null));
    }

    @Test
    public void testToStringWithValues() {
        assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
        assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
        assertEquals(StringUtils.EMPTY, BooleanUtils.toString(null, "true", "false"));
    }

    @Test
    public void testToStringWithTrueFalse() {
        assertEquals("true", BooleanUtils.toStringTrueFalse(true));
        assertEquals("false", BooleanUtils.toStringTrueFalse(false));
        assertEquals(StringUtils.EMPTY, BooleanUtils.toStringTrueFalse(null));
    }

    @Test
    public void testToStringWithOnOff() {
        assertEquals("on", BooleanUtils.toStringOnOff(true));
        assertEquals("off", BooleanUtils.toStringOnOff(false));
        assertEquals(StringUtils.EMPTY, BooleanUtils.toStringOnOff(null));
    }

    @Test
    public void testToStringWithYesNo() {
        assertEquals("yes", BooleanUtils.toStringYesNo(true));
        assertEquals("no", BooleanUtils.toStringYesNo(false));
        assertEquals(StringUtils.EMPTY, BooleanUtils.toStringYesNo(null));
    }

    @Test
    public void testAnd() {
        assertTrue(BooleanUtils.and(true, true, true));
        assertFalse(BooleanUtils.and(true, false, true));
        assertFalse(BooleanUtils.and(false, true, true));
        assertFalse(BooleanUtils.and(false, false, false));
        assertFalse(BooleanUtils.and(null, true, true));
        assertFalse(BooleanUtils.and(true, null, true));
        assertFalse(BooleanUtils.and(true, true, null));
        assertFalse(BooleanUtils.and(null, null, null));
    }

    @Test
    public void testAndWithBooleanArray() {
        assertTrue(BooleanUtils.and(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        assertFalse(BooleanUtils.and(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}));
        assertFalse(BooleanUtils.and(null));
    }

    @Test
    public void testAndWithPrimitiveArray() {
        boolean[] primitives = new boolean[]{true, true, true};
        assertTrue(BooleanUtils.and(primitives));

        primitives = new boolean[]{true, false, true};
        assertFalse(BooleanUtils.and(primitives));

        primitives = new boolean[0];
        assertFalse(BooleanUtils.and(primitives));
    }

    @Test
    public void testAndWithNullableBooleanArray() {
        assertTrue(BooleanUtils.and(new Boolean[]{Boolean.TRUE, null, Boolean.TRUE}));
        assertFalse(BooleanUtils.and(new Boolean[]{Boolean.FALSE, null, Boolean.FALSE}));
        assertFalse(BooleanUtils.and(new Boolean[]{null, null, null}));
    }

    @Test
    public void testOr() {
        assertTrue(BooleanUtils.or(true, true, true));
        assertTrue(BooleanUtils.or(true, true, false));
        assertTrue(BooleanUtils.or(true, false, true));
        assertFalse(BooleanUtils.or(false, false, false));
        assertFalse(BooleanUtils.or(null, true, true));
        assertFalse(BooleanUtils.or(true, null, true));
        assertFalse(BooleanUtils.or(true, true, null));
        assertFalse(BooleanUtils.or(null, null, null));
    }

    @Test
    public void testOrWithBooleanArray() {
        assertTrue(BooleanUtils.or(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        assertTrue(BooleanUtils.or(new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}));
        assertTrue(BooleanUtils.or(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        assertFalse(BooleanUtils.or(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}));
        assertFalse(BooleanUtils.or(null));
    }

    @Test
    public void testOrWithPrimitiveArray() {
        boolean[] primitives = new boolean[]{true, true, true};
        assertTrue(BooleanUtils.or(primitives));

        primitives = new boolean[]{true, true, false};
        assertTrue(BooleanUtils.or(primitives));

        primitives = new boolean[]{true, false, true};
        assertTrue(BooleanUtils.or(primitives));

        primitives = new boolean[0];
        assertFalse(BooleanUtils.or(primitives));
    }

    @Test
    public void testOrWithNullableBooleanArray() {
        assertTrue(BooleanUtils.or(new Boolean[]{Boolean.TRUE, null, Boolean.TRUE}));
        assertTrue(BooleanUtils.or(new Boolean[]{Boolean.TRUE, null, Boolean.FALSE}));
        assertTrue(BooleanUtils.or(new Boolean[]{Boolean.TRUE, Boolean.FALSE, null}));
        assertFalse(BooleanUtils.or(new Boolean[]{null, null, null}));
    }

    @Test
    public void testXor() {
        assertTrue(BooleanUtils.xor(true, true, true));
        assertFalse(BooleanUtils.xor(true, false, true));
        assertFalse(BooleanUtils.xor(false, true, true));
        assertFalse(BooleanUtils.xor(false, false, false));
        assertFalse(BooleanUtils.xor(null, true, true));
        assertFalse(BooleanUtils.xor(true, null, true));
        assertFalse(BooleanUtils.xor(true, true, null));
        assertFalse(BooleanUtils.xor(null, null, null));
    }

    @Test
    public void testXorWithBooleanArray() {
        assertTrue(BooleanUtils.xor(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        assertFalse(BooleanUtils.xor(new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}));
        assertFalse(BooleanUtils.xor(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        assertFalse(BooleanUtils.xor(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}));
        assertFalse(BooleanUtils.xor(null));
    }

    @Test
    public void testXorWithPrimitiveArray() {
        boolean[] primitives = new boolean[]{true, true, true};
        assertTrue(BooleanUtils.xor(primitives));

        primitives = new boolean[]{true, false, true};
        assertFalse(BooleanUtils.xor(primitives));

        primitives = new boolean[]{true, true, false};
        assertFalse(BooleanUtils.xor(primitives));

        primitives = new boolean[0];
        assertFalse(BooleanUtils.xor(primitives));
    }

    @Test
    public void testXorWithNullableBooleanArray() {
        assertTrue(BooleanUtils.xor(new Boolean[]{Boolean.TRUE, null, Boolean.TRUE}));
        assertFalse(BooleanUtils.xor(new Boolean[]{Boolean.TRUE, null, Boolean.FALSE}));
        assertFalse(BooleanUtils.xor(new Boolean[]{Boolean.TRUE, Boolean.FALSE, null}));
        assertFalse(BooleanUtils.xor(new Boolean[]{null, null, null}));
    }
}