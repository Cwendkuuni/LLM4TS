import org.apache.commons.lang3.BooleanUtils;
import org.junit.Assert;
import org.junit.Test;

public class BooleanUtilsTest {

 // Test for negate method
 @Test
 public void testNegate() {
 Boolean bool = Boolean.TRUE;
 Assert.assertEquals(Boolean.FALSE, BooleanUtils.negate(bool));
 Assert.assertNull(BooleanUtils.negate(null));
 }

 // Test for isTrue method
 @Test
 public void testIsTrue() {
 Assert.assertTrue(BooleanUtils.isTrue(Boolean.TRUE));
 Assert.assertFalse(BooleanUtils.isTrue(Boolean.FALSE));
 Assert.assertFalse(BooleanUtils.isTrue(null));
 }

 // Test for isNotTrue method
 @Test
 public void testIsNotTrue() {
 Assert.assertFalse(BooleanUtils.isNotTrue(Boolean.TRUE));
 Assert.assertTrue(BooleanUtils.isNotTrue(Boolean.FALSE));
 Assert.assertFalse(BooleanUtils.isNotTrue(null));
 }

 // Test for isFalse method
 @Test
 public void testIsFalse() {
 Assert.assertFalse(BooleanUtils.isFalse(Boolean.TRUE));
 Assert.assertTrue(BooleanUtils.isFalse(Boolean.FALSE));
 Assert.assertFalse(BooleanUtils.isFalse(null));
 }

 // Test for isNotFalse method
 @Test
 public void testIsNotFalse() {
 Assert.assertTrue(BooleanUtils.isNotFalse(Boolean.TRUE));
 Assert.assertFalse(BooleanUtils.isNotFalse(Boolean.FALSE));
 Assert.assertFalse(BooleanUtils.isNotFalse(null));
 }

 // Test for toBoolean method with Boolean argument
 @Test
 public void testToBooleanWithBoolean() {
 Assert.assertTrue(BooleanUtils.toBoolean(Boolean.TRUE));
 Assert.assertFalse(BooleanUtils.toBoolean(Boolean.FALSE));
 Assert.assertNull(BooleanUtils.toBoolean(null));
 }

 // Test for toBoolean method with int argument
 @Test
 public void testToBooleanWithInt() {
 Assert.assertTrue(BooleanUtils.toBoolean(1));
 Assert.assertFalse(BooleanUtils.toBoolean(0));
 Assert.assertNull(BooleanUtils.toBoolean(-1));
 }

 // Test for toBooleanDefaultIfNull method with Boolean argument and default value true
 @Test
 public void testToBooleanDefaultIfNullWithBooleanAndTrue() {
 Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true));
 Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(null, true));
 }

 // Test for toBooleanDefaultIfNull method with Boolean argument and default value false
 @Test
 public void testToBooleanDefaultIfNullWithBooleanAndFalse() {
 Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, false));
 Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(null, false));
 }

 // Test for toBooleanDefaultIfNull method with int argument and default value true
 @Test
 public void testToBooleanDefaultIfNullWithIntAndTrue() {
 Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(1, true));
 Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(0, true));
 Assert.assertTrue(BooleanUtils.toBooleanDefaultIfNull(-1, true));
 }

 // Test for toBooleanDefaultIfNull method with int argument and default value false
 @Test
 public void testToBooleanDefaultIfNullWithIntAndFalse() {
 Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(-1, false));
 Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(0, false));
 Assert.assertFalse(BooleanUtils.toBooleanDefaultIfNull(1, false));
 }

 // Test for toBooleanObject method with Boolean argument
 @Test
 public void testToBooleanObjectWithBoolean() {
 Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Boolean.TRUE));
 Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Boolean.FALSE));
 Assert.assertNull(BooleanUtils.toBooleanObject(null));
 }

 // Test for toBooleanObject method with int argument
 @Test
 public void testToBooleanObjectWithInt() {
 Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(1));
 Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(0));
 Assert.assertNull(BooleanUtils.toBooleanObject(-1));
 }

 // Test for toBooleanObject method with Integer argument
 @Test
 public void testToBooleanObjectWithInteger() {
 Assert.assertEquals(Boolean.TRUE, BooleanUtils.toBooleanObject(Integer.valueOf(1)));
 Assert.assertEquals(Boolean.FALSE, BooleanUtils.toBooleanObject(Integer.valueOf(0)));
 Assert.assertNull(BooleanUtils.toBooleanObject(null));
 }

 // Test for toBoolean method with Integer argument
 @Test
 public void testToBooleanWithInteger() {
 Assert.assertTrue(BooleanUtils.toBoolean(Integer.valueOf(1)));
 Assert.assertFalse(BooleanUtils.toBoolean(Integer.valueOf(0)));
 Assert.assertNull(BooleanUtils.toBoolean(null));
 }

 // Test for toBooleanObject method with boolean array argument
 @Test
 public void testToBooleanWithBooleanArray() {
 final boolean[] booleanArray = {true, false, null};
 final Boolean[] expectedBooleanArray = {Boolean.TRUE, Boolean.FALSE, null};
 Assert.assertArrayEquals(expectedBooleanArray, BooleanUtils.toBooleanArray(booleanArray));
 }

 // Test for and method
 @Test
 public void testAnd() {
 Assert.assertTrue(BooleanUtils.and(true, true, true));
 Assert.assertFalse(BooleanUtils.and(true, true, false));
 Assert.assertFalse(BooleanUtils.and(true, false, true));
 Assert.assertFalse(BooleanUtils.and(true, false, false));
 Assert.assertFalse(BooleanUtils.and(false, true, true));
 Assert.assertFalse(BooleanUtils.and(false, true, false));
 Assert.assertFalse(BooleanUtils.and(false, false, true));
 Assert.assertFalse(BooleanUtils.and(false, false, false));
 }

 // Test for and method with Boolean array argument
 @Test
 public void testAndWithBooleanArray() {
 final Boolean[] booleanArray = {Boolean.TRUE, Boolean.FALSE, null};
 final Boolean expectedBoolean = Boolean.FALSE;
 Assert.assertEquals(expectedBoolean, BooleanUtils.and(booleanArray));
 }

 // Test for or method
 @Test
 public void testOr() {
 Assert.assertTrue(BooleanUtils.or(true, true, true));
 Assert.assertTrue(BooleanUtils.or(true, true, false));
 Assert.assertTrue(BooleanUtils.or(true, false, true));
 Assert.assertTrue(BooleanUtils.or(true, false, false));
 Assert.assertTrue(BooleanUtils.or(false, true, true));
 Assert.assertTrue(BooleanUtils.or(false, true, false));
 Assert.assertTrue(BooleanUtils.or(false, false, true));
 Assert.assertTrue(BooleanUtils.or(false, false, false));
 }

 // Test for or method with Boolean array argument
 @Test
 public void testOrWithBooleanArray() {
 final Boolean[] booleanArray = {Boolean.TRUE, Boolean.FALSE, null};
 final Boolean expectedBoolean = Boolean.TRUE;
 Assert.assertEquals(expectedBoolean, BooleanUtils.or(booleanArray));
 }

 // Test for xor method
 @Test
 public void testXor() {
 Assert.assertTrue(BooleanUtils.xor(true, true, false));
 Assert.assertFalse(BooleanUtils.xor(true, true, true));
 Assert.assertFalse(BooleanUtils.xor(true, false, true));
 Assert.assertTrue(BooleanUtils.xor(true, false, false));
 Assert.assertTrue(BooleanUtils.xor(false, true, true));
 Assert.assertFalse(BooleanUtils.xor(false, true, false));
 Assert.assertFalse(BooleanUtils.xor(false, false, true));
 Assert.assertFalse(BooleanUtils.xor(false, false, false));
 }

 // Test for xor method with Boolean array argument
 @Test
 public void testXorWithBooleanArray() {
 final Boolean[] booleanArray = {Boolean.TRUE, Boolean.FALSE, null};
 final Boolean expectedBoolean = Boolean.TRUE;
 Assert.assertEquals(expectedBoolean, BooleanUtils.xor(booleanArray));
 }
}