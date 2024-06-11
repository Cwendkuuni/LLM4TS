import org.apache.commons.lang3.BooleanUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

 // Test for isTrue method
 @Test
 public void testIsTrue() {
 Boolean bool = Boolean.TRUE;
 assertTrue(BooleanUtils.isTrue(bool));

 Boolean falseBool = Boolean.FALSE;
 assertFalse(BooleanUtils.isTrue(falseBool));

 Boolean nullBool = null;
 assertFalse(BooleanUtils.isTrue(nullBool));
 }

 // Test for isNotTrue method
 @Test
 public void testIsNotTrue() {
 Boolean bool = Boolean.TRUE;
 assertFalse(BooleanUtils.isNotTrue(bool));

 Boolean falseBool = Boolean.FALSE;
 assertTrue(BooleanUtils.isNotTrue(falseBool));

 Boolean nullBool = null;
 assertFalse(BooleanUtils.isNotTrue(nullBool));
 }

 // Test for isFalse method
 @Test
 public void testIsFalse() {
 Boolean bool = Boolean.FALSE;
 assertTrue(BooleanUtils.isFalse(bool));

 Boolean trueBool = Boolean.TRUE;
 assertFalse(BooleanUtils.isFalse(trueBool));

 Boolean nullBool = null;
 assertFalse(BooleanUtils.isFalse(nullBool));
 }

 // Test for isNotFalse method
 @Test
 public void testIsNotFalse() {
 Boolean bool = Boolean.FALSE;
 assertFalse(BooleanUtils.isNotFalse(bool));

 Boolean trueBool = Boolean.TRUE;
 assertTrue(BooleanUtils.isNotFalse(trueBool));

 Boolean nullBool = null;
 assertFalse(BooleanUtils.isNotFalse(nullBool));
 }

 // Test for toBoolean method with Boolean argument
 @Test
 public void testToBooleanWithBoolean() {
 assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
 assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
 assertNull(BooleanUtils.toBoolean(null));
 }

 // Test for toBooleanDefaultIfNull method with Boolean argument
 @Test
 public void testToBooleanDefaultIfNullWithBoolean() {
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, true));
 assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
 assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
 }

 // Test for toBoolean method with int argument
 @Test
 public void testToBooleanWithInt() {
 assertTrue(BooleanUtils.toBoolean(1));
 assertFalse(BooleanUtils.toBoolean(0));
 }

 // Test for toBooleanObject method with int argument
 @Test
 public void testToBooleanObjectWithInt() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0));
 assertNull(BooleanUtils.toBooleanObject(null));
 }

 // Test for toBoolean method with Integer argument
 @Test
 public void testToBooleanWithInteger() {
 assertTrue(BooleanUtils.toBoolean(Integer.valueOf(1)));
 assertFalse(BooleanUtils.toBoolean(Integer.valueOf(0)));
 assertNull(BooleanUtils.toBoolean(null));
 }

 // Test for toBooleanObject method with Integer argument and default values
 @Test
 public void testToBooleanObjectWithIntegerAndDefaultValues() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1), Boolean.TRUE, Boolean.FALSE, null));
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0), Boolean.TRUE, Boolean.FALSE, null));
 assertNull(BooleanUtils.toBooleanObject(null, Boolean.TRUE, Boolean.FALSE, null));
 }

 // Test for toInteger method with Boolean argument
 @Test
 public void testToIntegerWithBoolean() {
 assertEquals(1, BooleanUtils.toInteger(true));
 assertEquals(0, BooleanUtils.toInteger(false));
 }

 // Test for toIntegerObject method with Boolean argument
 @Test
 public void testToIntegerObjectWithBoolean() {
 assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(true));
 assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(false));
 assertNull(BooleanUtils.toIntegerObject(null));
 }

 // Test for toInteger method with int argument
 @Test
 public void testToIntegerWithInt() {
 assertEquals(1, BooleanUtils.toInteger(true, 1, 0));
 assertEquals(0, BooleanUtils.toInteger(false, 1, 0));
 }

 // Test for toInteger method with Boolean argument and default values
 @Test
 public void testToIntegerWithBooleanAndDefaultValues() {
 assertEquals(1, BooleanUtils.toInteger(true, 1, 0));
 assertEquals(0, BooleanUtils.toInteger(false, 1, 0));
 }

 // Test for toIntegerObject method with Boolean argument and default values
 @Test
 public void testToIntegerObjectWithBooleanAndDefaultValues() {
 assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(true, null));
 assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(false, null));
 }

 // Test for toBooleanObject method with String argument
 @Test
 public void testToBooleanObjectWithString() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("true"));
 assertNull(BooleanUtils.toBooleanObject(null));
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject("false"));
 }

 // Test for toBooleanObject method with String argument and default values
 @Test
 public void testToBooleanObjectWithStringAndDefaultValues() {
 assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("true", Boolean.TRUE, Boolean.FALSE, null));
 assertNull(BooleanUtils.toBooleanObject(null, Boolean.TRUE, Boolean.FALSE, null));
 assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject("false", Boolean.TRUE, Boolean.FALSE, null));
 }

 // Test for toBoolean method with String argument
 @Test
 public void testToBooleanWithString() {
 assertTrue(BooleanUtils.toBoolean("true"));
 assertFalse(BooleanUtils.toBoolean("false"));
 assertFalse(BooleanUtils.toBoolean(null));
 }

 // Test for toBoolean method with String argument and default values
 @Test
 public void testToBooleanWithStringAndDefaultValues() {
 assertTrue(BooleanUtils.toBoolean("true", "true", "false"));
 assertFalse(BooleanUtils.toBoolean("false", "true", "false"));
 assertFalse(BooleanUtils.toBoolean(null, "true", "false"));
 }

 // Test for toStringTrueFalse method with Boolean argument
 @Test
 public void testToStringTrueFalseWithBoolean() {
 assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
 assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
 assertNull(BooleanUtils.toStringTrueFalse(null));
 }

 // Test for toStringOnOff method with Boolean argument
 @Test
 public void testToStringOnOffWithBoolean() {
 assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
 assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
 assertNull(BooleanUtils.toStringOnOff(null));
 }

 // Test for toStringYesNo method with Boolean argument
 @Test
 public void testToStringYesNoWithBoolean() {
 assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
 assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
 assertNull(BooleanUtils.toStringYesNo(null));
 }

 // Test for and method with Boolean array
 @Test
 public void testAndWithBooleanArray() {
 Boolean[] booleanArray = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 assertTrue(BooleanUtils.and(booleanArray));

 Boolean[] booleanArray1 = {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE};
 assertFalse(BooleanUtils.and(booleanArray1));

 Boolean[] booleanArray2 = {Boolean.TRUE, Boolean.TRUE, null, Boolean.TRUE};
 throw new AssertionError("Expected IllegalArgumentException to be thrown");
 }

 // Test for and method with Boolean array and primitive type
 @Test
 public void testAndWithBooleanArrayAndPrimitiveType() {
 boolean[] booleanArray = {true, true, true, true};
 assertTrue(BooleanUtils.and(booleanArray));

 boolean[] booleanArray1 = {true, false, true, true};
 assertFalse(BooleanUtils.and(booleanArray1));

 boolean[] booleanArray2 = {true, true, null, true};
 throw new AssertionError("Expected IllegalArgumentException to be thrown");
 }

 // Test for or method with Boolean array
 @Test
 public void testOrWithBooleanArray() {
 Boolean[] booleanArray = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 assertTrue(BooleanUtils.or(booleanArray));

 Boolean[] booleanArray1 = {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE};
 assertTrue(BooleanUtils.or(booleanArray1));

 Boolean[] booleanArray2 = {Boolean.TRUE, Boolean.TRUE, null, Boolean.TRUE};
 throw new AssertionError("Expected IllegalArgumentException to be thrown");
 }

 // Test for or method with Boolean array and primitive type
 @Test
 public void testOrWithBooleanArrayAndPrimitiveType() {
 boolean[] booleanArray = {true, true, true, true};
 assertTrue(BooleanUtils.or(booleanArray));

 boolean[] booleanArray1 = {true, false, true, true};
 assertTrue(BooleanUtils.or(booleanArray1));

 boolean[] booleanArray2 = {true, true, null, true};
 throw new AssertionError("Expected IllegalArgumentException to be thrown");
 }

 // Test for xor method with Boolean array
 @Test
 public void testXorWithBooleanArray() {
 Boolean[] booleanArray = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 assertEquals(Boolean.FALSE, BooleanUtils.xor(booleanArray));

 Boolean[] booleanArray1 = {Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE};
 assertEquals(Boolean.TRUE, BooleanUtils.xor(booleanArray1));

 Boolean[] booleanArray2 = {Boolean.TRUE, Boolean.TRUE, null, Boolean.TRUE};
 throw new AssertionError("Expected IllegalArgumentException to be thrown");
 }

 // Test for xor method with Boolean array and primitive type
 @Test
 public void testXorWithBooleanArrayAndPrimitiveType() {
 boolean[] booleanArray = {true, true, true, true};
 assertEquals(false, BooleanUtils.xor(booleanArray));

 boolean[] booleanArray1 = {true, false, true, true};
 assertEquals(true, BooleanUtils.xor(booleanArray1));

 boolean[] booleanArray2 = {true, true, null, true};
 throw new AssertionError("Expected IllegalArgumentException to be thrown");
 }
}