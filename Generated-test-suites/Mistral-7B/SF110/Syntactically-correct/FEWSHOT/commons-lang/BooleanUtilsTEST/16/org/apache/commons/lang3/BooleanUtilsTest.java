package org.apache.commons.lang3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        Boolean bool = Boolean.TRUE;
        Boolean negatedBool = BooleanUtils.negate(bool);
        assertFalse(negatedBool.booleanValue());
        assertNotNull(negatedBool);
        bool = null;
        negatedBool = BooleanUtils.negate(bool);
        assertNull(negatedBool);
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
        assertNull(BooleanUtils.isNotFalse(null));
    }

    @Test
    public void testToBoolean() {
        assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        assertNull(BooleanUtils.toBoolean(null));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, false));
    }

    @Test
    public void testToBooleanWithInteger() {
        assertTrue(BooleanUtils.toBoolean(1));
        assertFalse(BooleanUtils.toBoolean(0));
        assertNull(BooleanUtils.toBoolean(null));
    }

    @Test
    public void testToBooleanObjectWithInteger() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ONE));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ZERO));
        assertNull(BooleanUtils.toBooleanObject(null));
    }

    @Test
    public void testToBooleanWithBooleanValueAndIntegerValues() {
        assertTrue(BooleanUtils.toBoolean(true, 1, 0));
        assertFalse(BooleanUtils.toBoolean(false, 1, 0));
        assertNull(BooleanUtils.toBoolean(null, 1, 0));
    }

    @Test
    public void testToBooleanWithBooleanValueAndIntegerValuesDifferentThanTrueAndFalse() {
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.toBoolean(true, 2, 3));
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.toBoolean(false, 2, 3));
    }

    @Test
    public void testToBooleanWithIntegerValueAndBooleanValues() {
        assertTrue(BooleanUtils.toBoolean(1, Boolean.TRUE));
        assertFalse(BooleanUtils.toBoolean(0, Boolean.TRUE));
        assertNull(BooleanUtils.toBoolean(null, Boolean.TRUE));
        assertTrue(BooleanUtils.toBoolean(1, Boolean.FALSE));
        assertFalse(BooleanUtils.toBoolean(0, Boolean.FALSE));
        assertNull(BooleanUtils.toBoolean(null, Boolean.FALSE));
    }

    @Test
    public void testToBooleanWithIntegerValueAndBooleanValueDifferentThanTrueAndFalse() {
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.toBoolean(2, Boolean.TRUE));
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.toBoolean(2, Boolean.FALSE));
    }

    @Test
    public void testToBooleanObjectWithIntegerValueAndBooleanValues() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ONE, Boolean.TRUE));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ZERO, Boolean.TRUE));
        assertNull(BooleanUtils.toBooleanObject(null, Boolean.TRUE));
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ONE, Boolean.FALSE));
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ZERO, Boolean.FALSE));
        assertNull(BooleanUtils.toBooleanObject(null, Boolean.FALSE));
    }

    @Test
    public void testToBooleanObjectWithIntegerValueAndBooleanValueDifferentThanTrueAndFalse() {
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.toBooleanObject(2, Boolean.TRUE));
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.toBooleanObject(2, Boolean.FALSE));
    }

    @Test
    public void testToBooleanWithBooleanArray() {
        assertTrue(BooleanUtils.and(new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE }));
        assertFalse(BooleanUtils.and(new Boolean[] { Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE }));
        assertFalse(BooleanUtils.and(new Boolean[] { null, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE }));
        assertFalse(BooleanUtils.and(new Boolean[] {}));
        assertFalse(BooleanUtils.and(new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE }));
    }

    @Test
    public void testToBooleanWithBooleanArrayNullElement() {
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.and(new Boolean[] { Boolean.TRUE, null, Boolean.TRUE, Boolean.TRUE }));
    }

    @Test
    public void testToBooleanWithBooleanArrayEmpty() {
        assertFalse(BooleanUtils.and(new Boolean[] {}));
    }

    @Test
    public void testToBooleanWithBooleanArrayAllFalse() {
        assertFalse(BooleanUtils.and(new Boolean[] { Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE }));
    }

    @Test
    public void testToBooleanWithBooleanArrayMixed() {
        assertTrue(BooleanUtils.or(new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE }));
        assertFalse(BooleanUtils.or(new Boolean[] { Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE }));
        assertNull(BooleanUtils.or(new Boolean[] { null, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE }));
        assertFalse(BooleanUtils.or(new Boolean[] {}));
        assertFalse(BooleanUtils.or(new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE }));
    }

    @Test
    public void testToBooleanWithBooleanArrayNullElementInMiddle() {
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.or(new Boolean[] { Boolean.TRUE, null, Boolean.TRUE, Boolean.TRUE }));
    }

    @Test
    public void testToBooleanWithBooleanArrayEmpty() {
        assertFalse(BooleanUtils.or(new Boolean[] {}));
    }

    @Test
    public void testToBooleanWithBooleanArrayAllFalse() {
        assertFalse(BooleanUtils.or(new Boolean[] { Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE }));
    }

    @Test
    public void testToBooleanWithBooleanArrayMixed2() {
        assertTrue(BooleanUtils.xor(new Boolean[] { Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE }));
        assertFalse(BooleanUtils.xor(new Boolean[] { Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE }));
        assertFalse(BooleanUtils.xor(new Boolean[] { null, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE }));
        assertNull(BooleanUtils.xor(new Boolean[] {}));
        assertFalse(BooleanUtils.xor(new Boolean[] { Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE }));
    }

    @Test
    public void testToBooleanWithBooleanArrayNullElementInBeginning() {
        assertThrows(IllegalArgumentException.class, () -> BooleanUtils.xor(new Boolean[] { null, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE }));
    }

    @Test
    public void testToBooleanWithBooleanArrayEmpty() {
        assertFalse(BooleanUtils.xor(new Boolean[] {}));
    }

    @Test
    public void testToBooleanWithBooleanArrayAllFalse() {
        assertFalse(BooleanUtils.xor(new Boolean[] { Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE }));
    }
}