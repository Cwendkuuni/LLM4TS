import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.NumberUtils;
import org.junit.Assert;
import org.junit.Test;

public class BooleanUtilsTest {

 @Test
 public void testNegate() {
 Boolean bool = Boolean.TRUE;
 Assert.assertFalse(BooleanUtils.negate(bool));
 Assert.assertNull(BooleanUtils.negate(null));
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
 Assert.assertFalse(BooleanUtils.isNotTrue(null));
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
 Assert.assertFalse(BooleanUtils.isNotFalse(null));
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
 Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true));
 Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
 Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false));
 Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
 Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
 }

 @Test
 public void testToBooleanWithInt() {
 Assert.assertTrue(BooleanUtils.toBoolean(1));
 Assert.assertFalse(BooleanUtils.toBoolean(0));
 }

 @Test
 public void testToBooleanObjectWithInt() {
 Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
 Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0));
 Assert.assertNull(BooleanUtils.toBooleanObject(null));
 }

 @Test
 public void testToBooleanWithBooleanAndValues() {
 Assert.assertTrue(BooleanUtils.toBoolean(Boolean.TRUE, 1, 0));
 Assert.assertFalse(BooleanUtils.toBoolean(Boolean.FALSE, 1, 0));
 }

 @Test
 public void testToBooleanWithIntegerAndValues() {
 Assert.assertTrue(BooleanUtils.toBoolean(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(0)));
 Assert.assertFalse(BooleanUtils.toBoolean(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(0)));
 }

 @Test
 public void testToBooleanObjectWithIntAndValues() {
 Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1, 1, 0, 0));
 Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0, 1, 0, 0));
 Assert.assertNull(BooleanUtils.toBooleanObject(-1, 1, 0, 0));
 }

 @Test
 public void testToBooleanObjectWithNullableBooleanAndValues() {
 Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Boolean.TRUE, Integer.valueOf(1), Integer.valueOf(0), null));
 Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Boolean.FALSE, Integer.valueOf(1), Integer.valueOf(0), null));
 Assert.assertNull(BooleanUtils.toBooleanObject(null, Integer.valueOf(1), Integer.valueOf(0), null));
 }

 @Test
 public void testToInteger() {
 Assert.assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
 Assert.assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
 }

 @Test
 public void testToIntegerObjectWithBoolean() {
 Assert.assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
 Assert.assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
 }

 @Test
 public void testToIntegerObjectWithNullableBoolean() {
 Assert.assertEquals(NumberUtils.INTEGER_ONE, BooleanUtils.toIntegerObject(Boolean.TRUE));
 Assert.assertEquals(NumberUtils.INTEGER_ZERO, BooleanUtils.toIntegerObject(Boolean.FALSE));
 Assert.assertNull(BooleanUtils.toIntegerObject(null));
 }

 @Test
 public void testToIntegerWithBoolean() {
 Assert.assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE));
 Assert.assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE));
 }

 @Test
 public void testToIntegerWithBooleanAndValues() {
 Assert.assertEquals(1, BooleanUtils.toInteger(Boolean.TRUE, 1, 0));
 Assert.assertEquals(0, BooleanUtils.toInteger(Boolean.FALSE, 1, 0));
 }

 @Test
 public void testToIntegerWithBooleanAndException() {
 try {
 BooleanUtils.toInteger(Boolean.TRUE, 1, 2);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("The Boolean did not match either specified value", e.getMessage());
 }
 }

 @Test
 public void testToBooleanWithString() {
 Assert.assertTrue(BooleanUtils.toBoolean("true"));
 Assert.assertFalse(BooleanUtils.toBoolean("false"));
 Assert.assertNull(BooleanUtils.toBoolean(null));
 }

 @Test
 public void testToBooleanObjectWithString() {
 Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("true"));
 Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject("false"));
 Assert.assertNull(BooleanUtils.toBooleanObject(null));
 }

 @Test
 public void testToBooleanObjectWithStringAndValues() {
 Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject("true", "true", "false", null));
 Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject("false", "false", "true", null));
 Assert.assertNull(BooleanUtils.toBooleanObject(null, "true", "false", null));
 }

 @Test
 public void testToBooleanWithStringAndException() {
 try {
 BooleanUtils.toBoolean("yes");
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("The String did not match any specified value", e.getMessage());
 }
 }

 @Test
 public void testToStringTrueFalse() {
 Assert.assertEquals("true", BooleanUtils.toStringTrueFalse(Boolean.TRUE));
 Assert.assertEquals("false", BooleanUtils.toStringTrueFalse(Boolean.FALSE));
 }

 @Test
 public void testToStringOnOff() {
 Assert.assertEquals("on", BooleanUtils.toStringOnOff(Boolean.TRUE));
 Assert.assertEquals("off", BooleanUtils.toStringOnOff(Boolean.FALSE));
 }

 @Test
 public void testToStringYesNo() {
 Assert.assertEquals("yes", BooleanUtils.toStringYesNo(Boolean.TRUE));
 Assert.assertEquals("no", BooleanUtils.toStringYesNo(Boolean.FALSE));
 }

 @Test
 public void testToStringWithBoolean() {
 Assert.assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false"));
 Assert.assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false"));
 }

 @Test
 public void testToStringWithBooleanAndValues() {
 Assert.assertEquals("true", BooleanUtils.toString(Boolean.TRUE, "true", "false", null));
 Assert.assertEquals("false", BooleanUtils.toString(Boolean.FALSE, "true", "false", null));
 }

 @Test
 public void testToStringWithBooleanAndValuesAndEmptyString() {
 Assert.assertEquals("", BooleanUtils.toString(Boolean.TRUE, "", "", null));
 Assert.assertEquals("", BooleanUtils.toString(Boolean.FALSE, "", "", null));
 }

 @Test
 public void testToStringWithBooleanAndValuesAndNullString() {
 Assert.assertEquals(null, BooleanUtils.toString(Boolean.TRUE, null, null, null));
 Assert.assertEquals(null, BooleanUtils.toString(Boolean.FALSE, null, null, null));
 }

 @Test
 public void testToStringWithBooleanAndValuesAndException() {
 try {
 BooleanUtils.toString(Boolean.TRUE, null, "false", null);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("The String or Boolean did not match any specified value", e.getMessage());
 }
 }

 @Test
 public void testAnd() {
 boolean[] booleanArray = {true, true, true, true, true};
 Assert.assertTrue(BooleanUtils.and(booleanArray));
 booleanArray[3] = false;
 Assert.assertFalse(BooleanUtils.and(booleanArray));
 }

 @Test
 public void testAndWithBooleanArray() {
 Boolean[] booleanArray = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 Assert.assertEquals(Boolean.TRUE, BooleanUtils.and(booleanArray));
 booleanArray[3] = Boolean.FALSE;
 Assert.assertEquals(Boolean.FALSE, BooleanUtils.and(booleanArray));
 }

 @Test
 public void testAndWithNullableBooleanArray() {
 Boolean[] booleanArray = {Boolean.TRUE, null, null, Boolean.TRUE, Boolean.TRUE};
 try {
 BooleanUtils.and(booleanArray);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("The Array must not contain any null elements", e.getMessage());
 }
 }

 @Test
 public void testOr() {
 boolean[] booleanArray = {true, false, true, false, true};
 Assert.assertFalse(BooleanUtils.or(booleanArray));
 booleanArray[3] = true;
 Assert.assertTrue(BooleanUtils.or(booleanArray));
 }

 @Test
 public void testOrWithBooleanArray() {
 Boolean[] booleanArray = {Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 Assert.assertEquals(Boolean.FALSE, BooleanUtils.or(booleanArray));
 booleanArray[3] = Boolean.TRUE;
 Assert.assertEquals(Boolean.TRUE, BooleanUtils.or(booleanArray));
 }

 @Test
 public void testOrWithNullableBooleanArray() {
 Boolean[] booleanArray = {Boolean.FALSE, null, null, Boolean.TRUE, Boolean.TRUE};
 try {
 BooleanUtils.or(booleanArray);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("The Array must not contain any null elements", e.getMessage());
 }
 }

 @Test
 public void testXor() {
 boolean[] booleanArray = {true, true, true, true, true};
 Assert.assertFalse(BooleanUtils.xor(booleanArray));
 booleanArray[3] = false;
 Assert.assertTrue(BooleanUtils.xor(booleanArray));
 booleanArray[4] = true;
 Assert.assertFalse(BooleanUtils.xor(booleanArray));
 }

 @Test
 public void testXorWithBooleanArray() {
 Boolean[] booleanArray = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
 Assert.assertFalse(BooleanUtils.xor(booleanArray));
 booleanArray[3] = Boolean.FALSE;
 Assert.assertTrue(BooleanUtils.xor(booleanArray));
 booleanArray[4] = Boolean.TRUE;
 Assert.assertFalse(BooleanUtils.xor(booleanArray));
 }

 @Test
 public void testXorWithNullableBooleanArray() {
 Boolean[] booleanArray = {Boolean.TRUE, null, null, Boolean.TRUE, Boolean.TRUE};
 try {
 BooleanUtils.xor(booleanArray);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("The Array must not contain any null elements", e.getMessage());
 }
 }
}