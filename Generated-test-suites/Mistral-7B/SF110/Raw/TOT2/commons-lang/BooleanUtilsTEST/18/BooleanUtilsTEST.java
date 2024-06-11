import org.apache.commons.lang3.BooleanUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class BooleanUtilsTest {

    // Test for negate method
    @Test
    public void testNegate() {
        assertNull(BooleanUtils.negate(null));
        assertTrue(BooleanUtils.negate(Boolean.TRUE));
        assertFalse(BooleanUtils.negate(Boolean.FALSE));
    }

    // Test for isTrue method
    @Test
    public void testIsTrue() {
        assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
        assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
        assertFalse(BooleanUtils.isTrue(null));
    }

    // Test for isNotTrue method
    @Test
    public void testIsNotTrue() {
        assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
        assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
        assertFalse(BooleanUtils.isNotTrue(null));
    }

    // Test for isFalse method
    @Test
    public void testIsFalse() {
        assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
        assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
        assertFalse(BooleanUtils.isFalse(null));
    }

    // Test for isNotFalse method
    @Test
    public void testIsNotFalse() {
        assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
        assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
        assertFalse(BooleanUtils.isNotFalse(null));
    }

    // Test for toBoolean method with Boolean argument
    @Test
    public void testToBooleanWithBoolean() {
        assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        assertNull(BooleanUtils.toBoolean(null));
    }

    // Test for toBoolean method with int argument
    @Test
    public void testToBooleanWithInt() {
        assertTrue(BooleanUtils.toBoolean(1));
        assertFalse(BooleanUtils.toBoolean(0));
        assertFalse(BooleanUtils.toBoolean(-1));
    }

    // Test for toBooleanDefaultIfNull method with Boolean argument
    @Test
    public void testToBooleanDefaultIfNullWithBoolean() {
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
    }

    // Test for toBooleanDefaultIfNull method with int argument
    @Test
    public void testToBooleanDefaultIfNullWithInt() {
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(1, false));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(0, false));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(-1, false));
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(1, true));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(0, true));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(-1, true));
    }

    // Test for edge cases and exception handling
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        BooleanUtils.toBoolean(null, Integer.valueOf(1), Integer.valueOf(1), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWithInts() {
        BooleanUtils.toBoolean(null, 1, 1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWithNullValues() {
        BooleanUtils.toBooleanObject(null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWithIntegerAndNull() {
        BooleanUtils.toBooleanObject(Integer.valueOf(1), null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWithBooleanAndNull() {
        BooleanUtils.toBooleanObject(Boolean.TRUE, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWithBoolsAndNull() {
        BooleanUtils.toBoolean(Boolean.TRUE, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWithIntsAndNull() {
        BooleanUtils.toBoolean(1, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWithNullInts() {
        BooleanUtils.toBooleanObject(null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWithNullBools() {
        BooleanUtils.toBooleanObject(null, null, null, null);
    }

    // Test for and method
    @Test
    public void testAnd() {
        assertFalse(BooleanUtils.and(new boolean[]{false, false}));
        assertFalse(BooleanUtils.and(new boolean[]{false, true}));
        assertFalse(BooleanUtils.and(new boolean[]{true, false}));
        assertTrue(BooleanUtils.and(new boolean[]{true, true}));
        assertFalse(BooleanUtils.and(new boolean[]{null}));
        assertFalse(BooleanUtils.and(new boolean[]{}));
    }

    // Test for or method
    @Test
    public void testOr() {
        assertFalse(BooleanUtils.or(new boolean[]{false, false}));
        assertTrue(BooleanUtils.or(new boolean[]{false, true}));
        assertTrue(BooleanUtils.or(new boolean[]{true, false}));
        assertTrue(BooleanUtils.or(new boolean[]{true, true}));
        assertFalse(BooleanUtils.or(new boolean[]{null}));
        assertFalse(BooleanUtils.or(new boolean[]{}));
    }

    // Test for xor method
    @Test
    public void testXor() {
        assertFalse(BooleanUtils.xor(new boolean[]{false, false}));
        assertFalse(BooleanUtils.xor(new boolean[]{false, true}));
        assertTrue(BooleanUtils.xor(new boolean[]{true, false}));
        assertFalse(BooleanUtils.xor(new boolean[]{true, true}));
        assertFalse(BooleanUtils.xor(new boolean[]{null}));
        assertFalse(BooleanUtils.xor(new boolean[]{}));
    }
}