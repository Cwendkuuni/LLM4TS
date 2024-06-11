import org.apache.commons.lang3.BooleanUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class BooleanUtilsTest {

    // Test for negate method
    @Test
    public void testNegate() {
        assertTrue(BooleanUtils.negate(Boolean.TRUE) == Boolean.FALSE);
        assertNull(BooleanUtils.negate(null));
    }

    // Test for isTrue method
    @Test
    public void testIsTrue() {
        assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
        assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
    }

    // Test for isNotTrue method
    @Test
    public void testIsNotTrue() {
        assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
        assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
    }

    // Test for isFalse method
    @Test
    public void testIsFalse() {
        assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
        assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
    }

    // Test for isNotFalse method
    @Test
    public void testIsNotFalse() {
        assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
        assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
    }

    // Test for toBoolean method with Boolean argument
    @Test
    public void testToBooleanWithBoolean() {
        assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
    }

    // Test for toBoolean method with int argument
    @Test
    public void testToBooleanWithInt() {
        assertTrue(BooleanUtils.toBoolean(1));
        assertFalse(BooleanUtils.toBoolean(0));
    }

    // Test for toBoolean method with Integer argument
    @Test
    public void testToBooleanWithInteger() {
        assertTrue(BooleanUtils.toBoolean(Integer.valueOf(1)));
        assertFalse(BooleanUtils.toBoolean(Integer.valueOf(0)));
    }

    // Test for toBooleanDefaultIfNull method with Boolean argument
    @Test
    public void testToBooleanDefaultIfNullWithBoolean() {
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, true));
    }

    // Test for toBooleanDefaultIfNull method with int argument
    @Test
    public void testToBooleanDefaultIfNullWithInt() {
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(1, true));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(0, true));
    }

    // Test for toBooleanDefaultIfNull method with Integer argument
    @Test
    public void testToBooleanDefaultIfNullWithInteger() {
        assertTrue(BooleanUtils.toBooleanDefaultIfNull(Integer.valueOf(1), true));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(Integer.valueOf(0), true));
        assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, true));
    }

    // Test for toBooleanObject method with int argument
    @Test
    public void testToBooleanObjectWithInt() {
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0));
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
        assertNull(BooleanUtils.toBooleanObject(-1));
    }

    // Test for toBooleanObject method with Integer argument
    @Test
    public void testToBooleanObjectWithInteger() {
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0)));
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1)));
        assertNull(BooleanUtils.toBooleanObject(null));
    }

    // Test for toBooleanObject method with int and Boolean arguments
    @Test
    public void testToBooleanObjectWithIntAndBoolean() {
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0, Boolean.FALSE));
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1, Boolean.FALSE));
        assertNull(BooleanUtils.toBooleanObject(-1, Boolean.FALSE));
    }

    // Test for toBooleanObject method with Integer and Boolean arguments
    @Test
    public void testToBooleanObjectWithIntegerAndBoolean() {
        assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0), Boolean.FALSE));
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1), Boolean.FALSE));
        assertNull(BooleanUtils.toBooleanObject(null, Boolean.FALSE));
    }

    // Test for toInteger method with Boolean argument
    @Test
    public void testToIntegerWithBoolean() {
        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
    }

    // Test for toIntegerObject method with Boolean argument
    @Test
    public void testToIntegerObjectWithBoolean() {
        assertEquals(Integer.valueOf(1), BooleanUtils.toIntegerObject(Boolean.TRUE));
        assertEquals(Integer.valueOf(0), BooleanUtils.toIntegerObject(Boolean.FALSE));
    }

    // Test for toInteger method with int argument
    @Test
    public void testToIntegerWithInt() {
        assertEquals(1, BooleanUtils.toInteger(true));
        assertEquals(0, BooleanUtils.toInteger(false));
    }

    // Test for toInteger method with boolean and int arguments
    @Test
    public void testToIntegerWithBooleanAndInt() {
        assertEquals(1, BooleanUtils.toInteger(true, 1));
        assertEquals(0, BooleanUtils.toInteger(false, 1));
    }

    // Test for toInteger method with Boolean and int arguments
    @Test
    public void testToIntegerWithBooleanAndInt2() {
        assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1));
        assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1));
    }

    // Test for toIntegerObject method with boolean argument
    @Test
    public void testToIntegerObjectWithBooleanArg() {
        assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
        assertNull(BooleanUtils.toIntegerObject(null));
    }

    // Test for toIntegerObject method with boolean and Integer arguments
    @Test
    public void testToIntegerObjectWithBooleanAndInteger() {
        assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE, NumberUtils.INTEGER_ONE));
        assertNull(BooleanUtils.toIntegerObject(null, NumberUtils.INTEGER_ONE));
    }

    // Test for toIntegerObject method with int and Integer arguments
    @Test
    public void testToIntegerObjectWithIntAndInteger() {
        assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(true, NumberUtils.INTEGER_ONE));
        assertNull(BooleanUtils.toIntegerObject(false, NumberUtils.INTEGER_ONE));
    }

    // Test for toBooleanObject method with String argument
    @Test
    public void testToBooleanObjectWithString() {
        assertTrue(BooleanUtils.toBooleanObject("true") == Boolean.TRUE);
        assertFalse(BooleanUtils.toBooleanObject("false") == Boolean.FALSE);
        assertNull(BooleanUtils.toBooleanObject("null"));
    }

    // Test for toBooleanObject method with String, String, String, String arguments
    @Test
    public void testToBooleanObjectWithStringStringStringString() {
        assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("true", "true", "false", null));
        assertFalse(BooleanUtils.toBooleanObject("false", "true", "false", null));
        assertNull(BooleanUtils.toBooleanObject("null", "true", "false", null));
    }

    // Test for toBoolean method with String argument
    @Test
    public void testToBooleanWithString() {
        assertTrue(BooleanUtils.toBoolean("true") == Boolean.TRUE);
        assertFalse(BooleanUtils.toBoolean("false") == Boolean.FALSE);
    }

    // Test for toBoolean method with String and String arguments
    @Test
    public void testToBooleanWithStringString() {
        assertTrue(BooleanUtils.toBoolean("true", "true") == Boolean.TRUE);
        assertFalse(BooleanUtils.toBoolean("false", "true") == Boolean.FALSE);
    }

    // Test for toStringTrueFalse method with Boolean argument
    @Test
    public void testToStringTrueFalseWithBoolean() {
        assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
        assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
    }

    // Test for toStringTrueFalse method with boolean argument
    @Test
    public void testToStringTrueFalseWithBooleanArg() {
        assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
        assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
    }

    // Test for toStringOnOff method with Boolean argument
    @Test
    public void testToStringOnOffWithBoolean() {
        assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
        assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
    }

    // Test for toStringOnOff method with boolean argument
    @Test
    public void testToStringOnOffWithBooleanArg() {
        assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
        assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
    }

    // Test for toStringYesNo method with Boolean argument
    @Test
    public void testToStringYesNoWithBoolean() {
        assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
        assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
    }

    // Test for toStringYesNo method with boolean argument
    @Test
    public void testToStringYesNoWithBooleanArg() {
        assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
        assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
    }

    // Test for and method with boolean array
    @Test
    public void testAndWithBooleanArray() {
        assertTrue(BooleanUtils.and(new boolean[]{true, true, true}));
        assertFalse(BooleanUtils.and(new boolean[]{true, true, false}));
        assertFalse(BooleanUtils.and(new boolean[]{false, false, false}));
        assertFalse(BooleanUtils.and(new boolean[]{null}));
        assertFalse(BooleanUtils.and(new boolean[]{null, null, null}));
    }

    // Test for and method with Boolean array
    @Test
    public void testAndWithBooleanArray2() {
        assertTrue(BooleanUtils.and(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        assertFalse(BooleanUtils.and(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.FALSE}));
        assertFalse(BooleanUtils.and(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}));
        assertFalse(BooleanUtils.and(new Boolean[]{null}));
        assertFalse(BooleanUtils.and(new Boolean[]{null, null, null}));
    }
}