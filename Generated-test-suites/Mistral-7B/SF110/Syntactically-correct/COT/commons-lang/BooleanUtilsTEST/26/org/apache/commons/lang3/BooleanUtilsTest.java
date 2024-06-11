import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.lang3.BooleanUtils;

public class BooleanUtilsTest {

    @Test
    public void testNegate() {
        Assert.assertNull(BooleanUtils.negate(null));
        Assert.assertFalse(BooleanUtils.negate(Boolean.TRUE));
        Assert.assertTrue(BooleanUtils.negate(Boolean.FALSE));
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
        Assert.assertTrue(BooleanUtils.isNotTrue(null));
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
        Assert.assertTrue(BooleanUtils.isNotFalse(null));
    }

    @Test
    public void testToBoolean() {
        Assert.assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
        Assert.assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
        Assert.assertFalse(BooleanUtils.toBoolean(null));
    }

    @Test
    public void testToBooleanDefaultIfNull() {
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
        Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
        Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
    }

    @Test
    public void testToBooleanWithInt() {
        Assert.assertTrue(BooleanUtils.toBoolean(1));
        Assert.assertFalse(BooleanUtils.toBoolean(0));
    }

    @Test
    public void testToBooleanObjectWithInt() {
        Assert.assertTrue(BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ONE));
        Assert.assertFalse(BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ZERO));
        Assert.assertNull(BooleanUtils.toBooleanObject(null));
    }

    @Test
    public void testToBooleanWithBooleanAndValues() {
        Assert.assertTrue(BooleanUtils.toBoolean(true, 1, 0));
        Assert.assertFalse(BooleanUtils.toBoolean(false, 1, 0));
        try {
            BooleanUtils.toBoolean(2, 1, 0);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match either specified value", ex.getMessage());
        }
    }

    @Test
    public void testToBooleanObjectWithIntAndValues() {
        Assert.assertTrue(BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ONE, 1, 0, null));
        Assert.assertFalse(BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ZERO, 1, 0, null));
        assertNull(BooleanUtils.toBooleanObject(null, 1, 0, null));
        try {
            BooleanUtils.toBooleanObject(NumberUtils.INTEGER_ONE, 1, 0, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Integer did not match any specified value", ex.getMessage());
        }
    }

    @Test
    public void testToStringTrueFalse() {
        Assert.assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
        Assert.assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
        Assert.assertEquals(null, BooleanUtils.toStringTrueFalse(null));
    }

    @Test
    public void testToStringOnOff() {
        Assert.assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
        Assert.assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
        Assert.assertEquals(null, BooleanUtils.toStringOnOff(null));
    }

    @Test
    public void testToStringYesNo() {
        Assert.assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
        Assert.assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
        Assert.assertEquals(null, BooleanUtils.toStringYesNo(null));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
        Assert.assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
        Assert.assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false", "false"));
    }

    @Test
    public void testAnd() {
        Assert.assertTrue(BooleanUtils.and(new boolean[]{true, true, true}));
        Assert.assertFalse(BooleanUtils.and(new boolean[]{true, false, false}));
        try {
            BooleanUtils.and(new boolean[]{null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
        try {
            BooleanUtils.and(new boolean[]{null, true, false});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testAndWithBooleans() {
        Assert.assertTrue(BooleanUtils.and(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        Assert.assertFalse(BooleanUtils.and(new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.FALSE}));
        try {
            BooleanUtils.and(new Boolean[]{null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
        try {
            BooleanUtils.and(new Boolean[]{null, Boolean.TRUE, Boolean.FALSE});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testOr() {
        Assert.assertTrue(BooleanUtils.or(new boolean[]{true, true, true}));
        Assert.assertFalse(BooleanUtils.or(new boolean[]{false, false, false}));
        try {
            BooleanUtils.or(new boolean[]{null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
        try {
            BooleanUtils.or(new boolean[]{null, true, false});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testOrWithBooleans() {
        Assert.assertTrue(BooleanUtils.or(new Boolean[]{Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
        Assert.assertFalse(BooleanUtils.or(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}));
        try {
            BooleanUtils.or(new Boolean[]{null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
        try {
            BooleanUtils.or(new Boolean[]{null, Boolean.TRUE, Boolean.FALSE});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testXor() {
        Assert.assertTrue(BooleanUtils.xor(new boolean[]{true, false, true}));
        Assert.assertFalse(BooleanUtils.xor(new boolean[]{false, false, false}));
        try {
            BooleanUtils.xor(new boolean[]{null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
        try {
            BooleanUtils.xor(new boolean[]{null, true, false});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The array must not contain any null elements", ex.getMessage());
        }
    }

    @Test
    public void testXorWithBooleans() {
        Assert.assertTrue(BooleanUtils.xor(new Boolean[]{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}));
        Assert.assertFalse(BooleanUtils.xor(new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}));
        try {
            BooleanUtils.xor(new Boolean[]{null});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The Array must not be null", ex.getMessage());
        }
        try {
            BooleanUtils.xor(new Boolean[]{null, Boolean.TRUE, Boolean.FALSE});
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("The array must not contain any null elements", ex.getMessage());
        }
    }
}