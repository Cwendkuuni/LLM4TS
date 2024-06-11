package org.apache.commons.lang3;

import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.lang3.BooleanUtils.negate;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.apache.commons.lang3.BooleanUtils.toBoolean;
import static org.apache.commons.lang3.BooleanUtils.toBooleanDefaultIfNull;
import static org.apache.commons.lang3.BooleanUtils.toBooleanObject;
import static org.apache.commons.lang3.BooleanUtils.toBooleanObject;
import static org.apache.commons.lang3.BooleanUtils.toBoolean;
import static org.apache.commons.lang3.BooleanUtils.toBoolean;
import static org.apache.commons.lang3.BooleanUtils.toBooleanObject;
import static org.apache.commons.lang3.BooleanUtils.toBooleanObject;
import static org.apache.commons.lang3.BooleanUtils.toInteger;
import static org.apache.commons.lang3.BooleanUtils.toIntegerObject;
import static org.apache.commons.lang3.BooleanUtils.toIntegerObject;
import static org.apache.commons.lang3.BooleanUtils.toInteger;
import static org.apache.commons.lang3.BooleanUtils.toInteger;
import static org.apache.commons.lang3.BooleanUtils.toBooleanObject;
import static org.apache.commons.lang3.BooleanUtils.toBooleanObject;

public class BooleanUtilsTest
{
    @Test
    public void testNegate() {
        Assert.assertNull(negate(null));
        Assert.assertEquals(Boolean.FALSE, negate(Boolean.TRUE));
        Assert.assertEquals(Boolean.TRUE, negate(Boolean.FALSE));
    }

    @Test
    public void testIsTrue() {
        Assert.assertTrue(isTrue(Boolean.TRUE));
        Assert.assertFalse(isTrue(Boolean.FALSE));
        Assert.assertFalse(isTrue(null));
    }

    @Test
    public void testIsNotTrue() {
        Assert.assertFalse(isNotTrue(Boolean.TRUE));
        Assert.assertTrue(isNotTrue(Boolean.FALSE));
        Assert.assertFalse(isNotTrue(null));
    }

    @Test
    public void testIsFalse() {
        Assert.assertFalse(isFalse(Boolean.TRUE));
        Assert.assertTrue(isFalse(Boolean.FALSE));
        Assert.assertFalse(isFalse(null));
    }

    @Test
    public void testIsNotFalse() {
        Assert.assertTrue(isNotFalse(Boolean.TRUE));
        Assert.assertFalse(isNotFalse(Boolean.FALSE));
        Assert.assertTrue(isNotFalse(null));
    }

    @Test
    public void testToBoolean() {
        Assert.assertTrue(toBoolean(Boolean.TRUE));
        Assert.assertFalse(toBoolean(Boolean.FALSE));
        Assert.assertFalse(toBoolean(null));
        Assert.assertTrue(toBoolean(1));
        Assert.assertFalse(toBoolean(0));
        Assert.assertFalse(toBoolean(""));
        Assert.assertFalse(toBoolean("", "true", "false"));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        Assert.assertTrue(toBooleanDefaultIfNull(Boolean.TRUE, true));
        Assert.assertFalse(toBooleanDefaultIfNull(Boolean.FALSE, true));
        Assert.assertTrue(toBooleanDefaultIfNull(null, true));
        Assert.assertFalse(toBooleanDefaultIfNull(Boolean.TRUE, false));
        Assert.assertTrue(toBooleanDefaultIfNull(Boolean.FALSE, false));
        Assert.assertFalse(toBooleanDefaultIfNull(null, false));
    }

    @Test
    public void testToBooleanObject() {
        Assert.assertEquals(Boolean.TRUE, toBooleanObject(Boolean.TRUE));
        Assert.assertEquals(Boolean.FALSE, toBooleanObject(Boolean.FALSE));
        Assert.assertNull(toBooleanObject(null));
        Assert.assertEquals(Boolean.TRUE, toBooleanObject(1));
        Assert.assertEquals(Boolean.FALSE, toBooleanObject(0));
    }

    @Test
    public void testToBooleanObjectWithBoolean() {
        Assert.assertEquals(Boolean.TRUE, toBooleanObject(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE));
        Assert.assertEquals(Boolean.FALSE, toBooleanObject(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE));
        Assert.assertNull(toBooleanObject(null, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE));
    }

    @Test
    public void testToBooleanObjectWithInt() {
        Assert.assertEquals(Boolean.TRUE, toBooleanObject(1, 1, 0, -1));
        Assert.assertEquals(Boolean.FALSE, toBooleanObject(0, 1, 0, -1));
        Assert.assertNull(toBooleanObject(-1, 1, 0, -1));
    }

    @Test
    public void testToInteger() {
        Assert.assertTrue(toInteger(Boolean.TRUE) == 1);
        Assert.assertTrue(toInteger(Boolean.FALSE) == 0);
        Assert.assertTrue(toInteger(null) == 0);
    }

    @Test
    public void testToIntegerObject() {
        Assert.assertEquals(NumberUtils.INTEGER_ONE, toIntegerObject(Boolean.TRUE));
        Assert.assertEquals(NumberUtils.INTEGER_ZERO, toIntegerObject(Boolean.FALSE));
        Assert.assertNull(toIntegerObject(null));
    }

    @Test
    public void testToIntegerObjectWithBoolean() {
        Assert.assertEquals(NumberUtils.INTEGER_ONE, toIntegerObject(Boolean.TRUE, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO));
        Assert.assertEquals(NumberUtils.INTEGER_ZERO, toIntegerObject(Boolean.FALSE, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO));
        Assert.assertNull(toIntegerObject(null, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO));
    }

    @Test
    public void testToIntegerObjectWithInt() {
        Assert.assertEquals(NumberUtils.INTEGER_ONE, toIntegerObject(1, 1, 0, -1));
        Assert.assertEquals(NumberUtils.INTEGER_ZERO, toIntegerObject(0, 1, 0, -1));
        Assert.assertNull(toIntegerObject(-1, 1, 0, -1));
    }

    @Test
    public void testToBooleanWithInteger() {
        Assert.assertTrue(toBoolean(1, 1, 0)
                == BooleanUtils.toBoolean(true, true, false));
        Assert.assertFalse(toBoolean(0, 1, 0)
                == BooleanUtils.toBoolean(false, true, false));
        Assert.assertFalse(toBoolean(1, 0, 1)
                == BooleanUtils.toBoolean(true, false, true));
        Assert.assertFalse(toBoolean(1, 1, 1)
                == BooleanUtils.toBoolean(true, true, true));
        Assert.assertFalse(toBoolean(1, 1, -1)
                == BooleanUtils.toBoolean(true, true, null));
    }

    @Test
    public void testToBooleanWithBooleanAndInteger() {
        Assert.assertTrue(toBoolean(Boolean.TRUE, 1, 0)
                == BooleanUtils.toBoolean(true, true, false));
        Assert.assertFalse(toBoolean(Boolean.FALSE, 1, 0)
                == BooleanUtils.toBoolean(false, true, false));
        Assert.assertFalse(toBoolean(Boolean.TRUE, 0, 1)
                == BooleanUtils.toBoolean(true, false, true));
        Assert.assertFalse(toBoolean(Boolean.TRUE, 1, 1)
                == BooleanUtils.toBoolean(true, true, true));
        Assert.assertFalse(toBoolean(Boolean.TRUE, 1, null)
                == BooleanUtils.toBoolean(true, true, null));
    }

    @Test
    public void testToBooleanWithString() {
        Assert.assertTrue(toBoolean("true") == Boolean.TRUE);
        Assert.assertFalse(toBoolean("false") == Boolean.FALSE);
        Assert.assertFalse(toBoolean("") == Boolean.FALSE);
        Assert.assertFalse(toBoolean("null", "true", "false") == Boolean.FALSE);
        Assert.assertFalse(toBoolean("null") == Boolean.FALSE);
        Assert.assertFalse(toBoolean("yes", "true", "false") == Boolean.TRUE);
        Assert.assertFalse(toBoolean("no", "true", "false") == Boolean.FALSE);
        Assert.assertFalse(toBoolean("on", "true", "false") == Boolean.TRUE);
        Assert.assertFalse(toBoolean("off", "true", "false") == Boolean.FALSE);
    }

    @Test
    public void testToBooleanWithStringBooleanAndString() {
        Assert.assertTrue(toBoolean("true", "true", "false") == Boolean.TRUE);
        Assert.assertFalse(toBoolean("false", "true", "false") == Boolean.FALSE);
        Assert.assertFalse(toBoolean("null", "true", "false") == Boolean.FALSE);
        Assert.assertFalse(toBoolean("yes", "false", "no") == Boolean.FALSE);
        Assert.assertFalse(toBoolean("no", "false", "yes") == Boolean.FALSE);
        Assert.assertFalse(toBoolean("on", "false", "off") == Boolean.FALSE);
        Assert.assertFalse(toBoolean("off", "false", "on") == Boolean.FALSE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanWithIntegerIllegalArgument() {
        toBoolean(1, 1, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanWithBooleanAndIntegerIllegalArgument() {
        toBoolean(Boolean.TRUE, 1, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanWithStringIllegalArgument() {
        toBoolean("", "true", "false");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToBooleanWithStringBooleanAndStringIllegalArgument() {
        toBoolean("null", "true", "false");
        toBoolean("yes", "true", "false");
        toBoolean("no", "true", "false");
        toBoolean("on", "true", "false");
        toBoolean("off", "true", "false");
    }
}