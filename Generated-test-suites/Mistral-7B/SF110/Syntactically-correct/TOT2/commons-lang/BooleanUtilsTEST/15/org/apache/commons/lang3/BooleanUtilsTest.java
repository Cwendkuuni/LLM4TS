import org.apache.commons.lang3.BooleanUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

 // Test for negate method
 @Test
 public void testNegate() {
 Boolean bool = Boolean.TRUE;
 assertTrue(BooleanUtils.negate(bool) == Boolean.FALSE);
 bool = Boolean.FALSE;
 assertTrue(BooleanUtils.negate(bool) == Boolean.TRUE);
 bool = null;
 assertNull(BooleanUtils.negate(bool));
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
 assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
 assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
 assertFalse(BooleanUtils.isNotTrue(null));
 }

 // Test for isFalse method
 @Test
 public void testIsFalse() {
 assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
 assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
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
 assertFalse(BooleanUtils.toBoolean(null));
 }

 // Test for toBoolean method with int argument
 @Test
 public void testToBooleanWithInt() {
 assertTrue(BooleanUtils.toBoolean(1));
 assertFalse(BooleanUtils.toBoolean(0));
 }

 // Test for toBooleanDefaultIfNull method with Boolean argument
 @Test
 public void testToBooleanDefaultIfNullWithBoolean() {
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
 }

 // Test for toBooleanDefaultIfNull method with int argument
 @Test
 public void testToBooleanDefaultIfNullWithInt() {
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(1, true));
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(0, true));
 }

 // Test for toBooleanObject method with Boolean argument
 @Test
 public void testToBooleanObjectWithBoolean() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Boolean.TRUE));
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Boolean.FALSE));
 assertNull(BooleanUtils.toBooleanObject(null));
 }

 // Test for toBooleanObject method with int argument
 @Test
 public void testToBooleanObjectWithInt() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0));
 assertNull(BooleanUtils.toBooleanObject(-1));
 }

 // Test for toBoolean method with int arguments and specified values
 @Test
 public void testToBooleanWithIntSpecifiedValues() {
 assertTrue(BooleanUtils.toBoolean(1, 1, 0));
 assertFalse(BooleanUtils.toBoolean(0, 1, 0));
 assertFalse(BooleanUtils.toBoolean(2, 1, 0));
 }

 // Test for toBoolean method with Integer arguments and specified values
 @Test
 public void testToBooleanWithIntegerSpecifiedValues() {
 assertTrue(BooleanUtils.toBoolean(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0)));
 assertFalse(BooleanUtils.toBoolean(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0)));
 assertFalse(BooleanUtils.toBoolean(Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(0)));
 }

 // Test for toBooleanObject method with int and specified values
 @Test
 public void testToBooleanObjectWithIntSpecifiedValues() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1, 1, 0, 0));
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0, 1, 0, 0));
 assertNull(BooleanUtils.toBooleanObject(-1, 1, 0, 0));
 }

 // Test for toBooleanObject method with Integer and specified values
 @Test
 public void testToBooleanObjectWithIntegerSpecifiedValues() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0), null));
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0), null));
 assertNull(BooleanUtils.toBooleanObject(null, Integer.valueOf(1), Integer.valueOf(0), null));
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
 assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
 assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
 }

 // Test for toIntegerObject method with nullable Boolean argument
 @Test
 public void testToIntegerObjectWithNullableBoolean() {
 assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
 assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
 assertNull(BooleanUtils.toIntegerObject(null));
 }

 // Test for toInteger method with Boolean argument and specified values
 @Test
 public void testToIntegerWithBooleanSpecifiedValues() {
 assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0));
 assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0));
 }

 // Test for toInteger method with Boolean argument and nullable specified values
 @Test
 public void testToIntegerWithNullableBooleanSpecifiedValues() {
 assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0)));
 assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(0)));
 assertNull(BooleanUtils.toInteger(null, null, null, null));
 }

 // Test for toIntegerObject method with Boolean argument and specified values
 @Test
 public void testToIntegerObjectWithBooleanSpecifiedValues() {
 assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO, null));
 assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE, NumberUtils.INTEGER_ONE, NumberUtils.INTEGER_ZERO, null));
 assertNull(BooleanUtils.toIntegerObject(null, null, null, null));
 }

 // Test for toIntegerObject method with String argument
 @Test
 public void testToBooleanObjectWithString() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("true"));
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject("false"));
 assertNull(BooleanUtils.toBooleanObject(null));
 }

 // Test for toBooleanObject method with String argument and specified values
 @Test
 public void testToBooleanObjectWithStringSpecifiedValues() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("true", "true", "false", null));
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject("false", "true", "false", null));
 assertNull(BooleanUtils.toBooleanObject(null, "true", "false", null));
 }

 // Test for toBoolean method with String argument
 @Test
 public void testToBooleanWithString() {
 assertTrue(BooleanUtils.toBoolean("true"));
 assertFalse(BooleanUtils.toBoolean("false"));
 }

 // Test for toBoolean method with String argument and specified values
 @Test
 public void testToBooleanWithStringSpecifiedValues() {
 assertTrue(BooleanUtils.toBoolean("true", "true", "false"));
 assertFalse(BooleanUtils.toBoolean("false", "true", "false"));
 assertTrue(BooleanUtils.toBoolean("true", null, "false", null));
 }

 // Test for toStringTrueFalse method with Boolean argument
 @Test
 public void testToStringTrueFalseWithBoolean() {
 assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
 assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
 }

 // Test for toStringOnOff method with Boolean argument
 @Test
 public void testToStringOnOffWithBoolean() {
 assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
 assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
 }

 // Test for toStringYesNo method with Boolean argument
 @Test
 public void testToStringYesNoWithBoolean() {
 assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
 assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
 }

 // Test for and method with Boolean arguments
 @Test
 public void testAndWithBooleans() {
 assertTrue(BooleanUtils.and(Boolean.TRUE, Boolean.TRUE));
 assertFalse(BooleanUtils.and(Boolean.TRUE, Boolean.FALSE));
 assertFalse(BooleanUtils.and(Boolean.FALSE, Boolean.TRUE));
 assertFalse(BooleanUtils.and(Boolean.FALSE, Boolean.FALSE));
 }

 // Test for and method with nullable Boolean arguments
 @Test(expected = IllegalArgumentException.class)
 public void testAndWithNullableBooleans() {
 BooleanUtils.and(Boolean.TRUE, null);
 }

 // Test for and method with Boolean arrays
 @Test
 public void testAndWithBooleanArrays() {
 assertTrue(BooleanUtils.and(new Boolean[] {Boolean.TRUE, Boolean.TRUE}));
 assertFalse(BooleanUtils.and(new Boolean[] {Boolean.TRUE, Boolean.FALSE}));
 assertFalse(BooleanUtils.and(new Boolean[] {Boolean.FALSE, Boolean.TRUE}));
 assertFalse(BooleanUtils.and(new Boolean[] {Boolean.FALSE, Boolean.FALSE}));
 }

 // Test for and method with nullable Boolean arrays
 @Test(expected = IllegalArgumentException.class)
 public void testAndWithNullableBooleanArrays() {
 BooleanUtils.and(new Boolean[] {Boolean.TRUE, null});
 }

 // Test for or method with Boolean arguments
 @Test
 public void testOrWithBooleans() {
 assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.TRUE));
 assertTrue(BooleanUtils.or(Boolean.TRUE, Boolean.FALSE));
 assertTrue(BooleanUtils.or(Boolean.FALSE, Boolean.TRUE));
 assertFalse(BooleanUtils.or(Boolean.FALSE, Boolean.FALSE));
 }

 // Test for or method with nullable Boolean arguments
 @Test(expected = IllegalArgumentException.class)
 public void testOrWithNullableBooleans() {
 BooleanUtils.or(Boolean.TRUE, null);
 }

 // Test for or method with Boolean arrays
 @Test
 public void testOrWithBooleanArrays() {
 assertTrue(BooleanUtils.or(new Boolean[] {Boolean.TRUE, Boolean.TRUE}));
 assertTrue(BooleanUtils.or(new Boolean[] {Boolean.TRUE, Boolean.FALSE}));
 assertTrue(BooleanUtils.or(new Boolean[] {Boolean.FALSE, Boolean.TRUE}));
 assertFalse(BooleanUtils.or(new Boolean[] {Boolean.FALSE, Boolean.FALSE}));
 }

 // Test for or method with nullable Boolean arrays
 @Test(expected = IllegalArgumentException.class)
 public void testOrWithNullableBooleanArrays() {
 BooleanUtils.or(new Boolean[] {Boolean.TRUE, null});
 }

 // Test for xor method with Boolean arguments
 @Test
 public void testXorWithBooleans() {
 assertTrue(BooleanUtils.xor(Boolean.TRUE, Boolean.TRUE));
 assertFalse(BooleanUtils.xor(Boolean.TRUE, Boolean.FALSE));
 assertFalse(BooleanUtils.xor(Boolean.FALSE, Boolean.TRUE));
 assertFalse(BooleanUtils.xor(Boolean.FALSE, Boolean.FALSE));
 }

 // Test for xor method with nullable Boolean arguments
 @Test(expected = IllegalArgumentException.class)
 public void testXorWithNullableBooleans() {
 BooleanUtils.xor(Boolean.TRUE, null);
 }

 // Test for xor method with Boolean arrays
 @Test
 public void testXorWithBooleanArrays() {
 assertTrue(BooleanUtils.xor(new Boolean[] {Boolean.TRUE, Boolean.TRUE}));
 assertFalse(BooleanUtils.xor(new Boolean[] {Boolean.TRUE, Boolean.FALSE}));
 assertFalse(BooleanUtils.xor(new Boolean[] {Boolean.FALSE, Boolean.TRUE}));
 assertFalse(BooleanUtils.xor(new Boolean[] {Boolean.FALSE, Boolean.FALSE}));
 }

 // Test for xor method with nullable Boolean arrays
 @Test(expected = IllegalArgumentException.class)
 public void testXorWithNullableBooleanArrays() {
 BooleanUtils.xor(new Boolean[] {Boolean.TRUE, null});
 }
}