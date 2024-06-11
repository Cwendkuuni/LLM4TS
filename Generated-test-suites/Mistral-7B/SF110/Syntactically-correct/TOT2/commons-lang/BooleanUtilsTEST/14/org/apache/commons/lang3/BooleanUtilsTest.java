import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.lang3.BooleanUtils;

public class BooleanUtilsTest {

 // Test for negate method
 @Test
 public void testNegate() {
  Assert.assertNull(BooleanUtils.negate(null));
  Assert.assertFalse(BooleanUtils.negate(Boolean.FALSE));
  Assert.assertTrue(BooleanUtils.negate(Boolean.TRUE));
 }

 // Test for isTrue method
 @Test
 public void testIsTrue() {
  Assert.assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
  Assert.assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
 }

 // Test for isNotTrue method
 @Test
 public void testIsNotTrue() {
  Assert.assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
  Assert.assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
 }

 // Test for isFalse method
 @Test
 public void testIsFalse() {
  Assert.assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
  Assert.assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
 }

 // Test for isNotFalse method
 @Test
 public void testIsNotFalse() {
  Assert.assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
  Assert.assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
 }

 // Test for toBoolean method with Boolean argument
 @Test
 public void testToBooleanWithBooleanArgument() {
  Assert.assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
  Assert.assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
 }

 // Test for toBoolean method with Integer argument
 @Test
 public void testToBooleanWithIntegerArgument() {
  Assert.assertTrue(BooleanUtils.toBoolean(1));
  Assert.assertFalse(BooleanUtils.toBoolean(0));
 }

 // Test for toBooleanDefaultIfNull method with Boolean argument and default value
 @Test
 public void testToBooleanDefaultIfNullWithBooleanArgumentAndDefaultValue() {
  Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
  Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
  Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
 }

 // Test for toBooleanDefaultIfNull method with Integer argument and default value
 @Test
 public void testToBooleanDefaultIfNullWithIntegerArgumentAndDefaultValue() {
  Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
  Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(1, false));
  Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(0, true));
 }

 // Test for toBoolean method with int array and given values
 @Test
 public void testToBooleanWithIntArrayAndGivenValues() {
  Assert.assertTrue(BooleanUtils.toBoolean(new int[] {1, 1, 1}));
  Assert.assertFalse(BooleanUtils.toBoolean(new int[] {0, 0, 0}));
  Assert.assertFalse(BooleanUtils.toBoolean(new int[] {1, 0, 1}));
  Assert.assertFalse(BooleanUtils.toBoolean(new int[] {0, 1, 1}));
 }

 // Test for toBoolean method with Integer array and given values
 @Test
 public void testToBooleanWithIntegerArrayAndGivenValues() {
  Assert.assertTrue(BooleanUtils.toBoolean(new Integer[] {1, 1, 1}));
  Assert.assertFalse(BooleanUtils.toBoolean(new Integer[] {0, 0, 0}));
  Assert.assertFalse(BooleanUtils.toBoolean(new Integer[] {1, 0, 1}));
  Assert.assertFalse(BooleanUtils.toBoolean(new Integer[] {0, 1, 1}));
 }

 // Test for toBooleanObject method with int and given values
 @Test
 public void testToBooleanObjectWithIntAndGivenValues() {
  Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1, 1, 0, -1));
  Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0, 1, 0, -1));
  Assert.assertNull(BooleanUtils.toBooleanObject(-1, 1, 0, -1));
 }

 // Test for toBooleanObject method with Integer and given values
 @Test
 public void testToBooleanObjectWithIntegerAndGivenValues() {
  Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0), null));
  Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0), null));
  Assert.assertNull(BooleanUtils.toBooleanObject(null, Integer.valueOf(1), Integer.valueOf(0), null));
 }

 // Test for toInteger method with Boolean argument
 @Test
 public void testToIntegerWithBooleanArgument() {
  Assert.assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
  Assert.assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
 }

 // Test for toIntegerObject method with Boolean argument
 @Test
 public void testToIntegerObjectWithBooleanArgument() {
  Assert.assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
  Assert.assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
 }

 // Test for toInteger method with Integer argument
 @Test
 public void testToIntegerWithIntegerArgument() {
  Assert.assertEquals(1, BooleanUtils.toInteger(1));
  Assert.assertEquals(0, BooleanUtils.toInteger(0));
 }

 // Test for toInteger method with int and given values
 @Test
 public void testToIntegerWithIntAndGivenValues() {
  Assert.assertEquals(1, BooleanUtils.toInteger(true, 1, 0));
  Assert.assertEquals(0, BooleanUtils.toInteger(false, 1, 0));
 }

 // Test for toInteger method with Boolean array and given values
 @Test
 public void testToIntegerWithBooleanArrayAndGivenValues() {
  Assert.assertEquals(1, BooleanUtils.toInteger(new boolean[] {true, true, true}));
  Assert.assertEquals(0, BooleanUtils.toInteger(new boolean[] {false, false, false}));
  Assert.assertEquals(0, BooleanUtils.toInteger(new boolean[] {true, false, true}));
  Assert.assertEquals(0, BooleanUtils.toInteger(new boolean[] {false, true, true}));
 }

 // Test for toInteger method with Integer array and given values
 @Test
 public void testToIntegerWithIntegerArrayAndGivenValues() {
  Assert.assertEquals(1, BooleanUtils.toInteger(new Integer[] {Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1)}));
  Assert.assertEquals(0, BooleanUtils.toInteger(new Integer[] {Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0)}));
  Assert.assertEquals(0, BooleanUtils.toInteger(new Integer[] {Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(1)}));
  Assert.assertEquals(0, BooleanUtils.toInteger(new Integer[] {Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(1)}));
 }

 // Test for toBooleanObject method with String and given values
 @Test
 public void testToBooleanObjectWithStringAndGivenValues() {
  Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("true", "true", "false", null));
  Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject("false", "true", "false", null));
  Assert.assertNull(BooleanUtils.toBooleanObject(null, "true", "false", null));
 }

 // Test for toBoolean method with String argument
 @Test
 public void testToBooleanWithStringArgument() {
  Assert.assertTrue(BooleanUtils.toBoolean("true"));
  Assert.assertFalse(BooleanUtils.toBoolean("false"));
 }

 // Test for toBoolean method with String and given values
 @Test
 public void testToBooleanWithStringAndGivenValues() {
  Assert.assertTrue(BooleanUtils.toBoolean("true", "true", "false"));
  Assert.assertFalse(BooleanUtils.toBoolean("false", "true", "false"));
 }

 // Test for toStringTrueFalse method with Boolean argument
 @Test
 public void testToStringTrueFalseWithBooleanArgument() {
  Assert.assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
  Assert.assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
 }

 // Test for toStringOnOff method with Boolean argument
 @Test
 public void testToStringOnOffWithBooleanArgument() {
  Assert.assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
  Assert.assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
 }

 // Test for toStringYesNo method with Boolean argument
 @Test
 public void testToStringYesNoWithBooleanArgument() {
  Assert.assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
  Assert.assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
 }

 // Test for and method with Boolean array
 @Test
 public void testAndWithBooleanArray() {
  Assert.assertTrue(BooleanUtils.and(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}));
  Assert.assertFalse(BooleanUtils.and(new Boolean[] {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}));
  Assert.assertFalse(BooleanUtils.and(new Boolean[] {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}));
  Assert.assertFalse(BooleanUtils.and(new Boolean[] {Boolean.FALSE, Boolean.TRUE, Boolean.TRUE}));
 }

 // Test for and method with boolean array
 @Test
 public void testAndWithBooleanArrayBoolean() {
  Assert.assertTrue(BooleanUtils.and(new boolean[] {true, true, true}));
  Assert.assertFalse(BooleanUtils.and(new boolean[] {false, false, false}));
  Assert.assertFalse(BooleanUtils.and(new boolean[] {true, false, true}));
  Assert.assertFalse(BooleanUtils.and(new boolean[] {false, true, true}));
 }
}