import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanUtilsTest {

 // Test for negate method
 @Test
 public void testNegateTrue() {
     Boolean expected = Boolean.FALSE;
     Boolean actual = BooleanUtils.negate(Boolean.TRUE);
     assertEquals(expected, actual);
 }

 @Test
 public void testNegateFalse() {
     Boolean expected = Boolean.TRUE;
     Boolean actual = BooleanUtils.negate(Boolean.FALSE);
     assertEquals(expected, actual);
 }

 @Test
 public void testNegateNull() {
     assertNull(BooleanUtils.negate(null));
 }

 // Test for isTrue method
 @Test
 public void testIsTrueTrue() {
     Boolean expected = Boolean.TRUE;
     Boolean actual = BooleanUtils.isTrue(Boolean.TRUE);
     assertEquals(expected, actual);
 }

 @Test
 public void testIsTrueFalse() {
     Boolean expected = Boolean.FALSE;
     Boolean actual = BooleanUtils.isTrue(Boolean.FALSE);
     assertEquals(expected, actual);
 }

 @Test
 public void testIsTrueNull() {
     assertFalse(BooleanUtils.isTrue(null));
 }

 // Test for isNotTrue method
 @Test
 public void testIsNotTrueFalse() {
     Boolean expected = Boolean.TRUE;
     Boolean actual = BooleanUtils.isNotTrue(Boolean.FALSE);
     assertEquals(expected, actual);
 }

 @Test
 public void testIsNotTrueTrue() {
     Boolean expected = Boolean.FALSE;
     Boolean actual = BooleanUtils.isNotTrue(Boolean.TRUE);
     assertEquals(expected, actual);
 }

 @Test
 public void testIsNotTrueNull() {
     assertFalse(BooleanUtils.isNotTrue(null));
 }

 // Test for isFalse method
 @Test
 public void testIsFalseFalse() {
     Boolean expected = Boolean.TRUE;
     Boolean actual = BooleanUtils.isFalse(Boolean.FALSE);
     assertEquals(expected, actual);
 }

 @Test
 public void testIsFalseTrue() {
     Boolean expected = Boolean.FALSE;
     Boolean actual = BooleanUtils.isFalse(Boolean.TRUE);
     assertEquals(expected, actual);
 }

 @Test
 public void testIsFalseNull() {
     assertFalse(BooleanUtils.isFalse(null));
 }

 // Test for isNotFalse method
 @Test
 public void testIsNotFalseFalse() {
     Boolean expected = Boolean.FALSE;
     Boolean actual = BooleanUtils.isNotFalse(Boolean.FALSE);
     assertEquals(expected, actual);
 }

 @Test
 public void testIsNotFalseTrue() {
     Boolean expected = Boolean.TRUE;
     Boolean actual = BooleanUtils.isNotFalse(Boolean.TRUE);
     assertEquals(expected, actual);
 }

 @Test
 public void testIsNotFalseNull() {
     assertFalse(BooleanUtils.isNotFalse(null));
 }

 // Test for toBoolean method
 @Test
 public void testToBooleanTrue() {
     Boolean expected = Boolean.TRUE;
     Boolean actual = BooleanUtils.toBoolean(true);
     assertEquals(expected, actual);
 }

 @Test
 public void testToBooleanFalse() {
     Boolean expected = Boolean.FALSE;
     Boolean actual = BooleanUtils.toBoolean(false);
     assertEquals(expected, actual);
 }

 @Test
 public void testToBooleanNull() {
     assertNull(BooleanUtils.toBoolean(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanNonBoolean() {
     BooleanUtils.toBoolean(1);
 }

 // Test for toBooleanDefaultIfNull method
 @Test
 public void testToBooleanDefaultIfNullTrue() {
     Boolean expected = Boolean.TRUE;
     Boolean actual = BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, true);
     assertEquals(expected, actual);
 }

 @Test
 public void testToBooleanDefaultIfNullFalse() {
     Boolean expected = false;
     Boolean actual = BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true);
     assertEquals(expected, actual);
 }

 @Test
 public void testToBooleanDefaultIfNullBooleanDefault() {
     Boolean expected = Boolean.FALSE;
     Boolean actual = BooleanUtils.toBooleanDefaultIfNull(null, Boolean.FALSE);
     assertEquals(expected, actual);
 }

 @Test
 public void testToBooleanDefaultIfNullBooleanFalse() {
     Boolean expected = true;
     Boolean actual = BooleanUtils.toBooleanDefaultIfNull(null, Boolean.FALSE);
     assertEquals(expected, actual);
 }

 // Test for toBoolean method with int values
 @Test
 public void testToBooleanIntTrue() {
     Boolean expected = Boolean.TRUE;
     Boolean actual = BooleanUtils.toBoolean(1);
     assertEquals(expected, actual);
 }

 @Test
 public void testToBooleanIntFalse() {
     Boolean expected = Boolean.FALSE;
     Boolean actual = BooleanUtils.toBoolean(0);
     assertEquals(expected, actual);
 }

 @Test
 public void testToBooleanIntNull() {
     assertNull(BooleanUtils.toBoolean(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanIntNonBoolean() {
     BooleanUtils.toBoolean(2);
 }

 // Test for toBooleanObject method
 @Test
 public void testToBooleanObjectBooleanTrue() {
     Boolean expected = Boolean.TRUE;
     Boolean actual = BooleanUtils.toBooleanObject(Boolean.TRUE);
     assertEquals(expected, actual);
 }

 @Test
 public void testToBooleanObjectBooleanFalse() {
     Boolean expected = Boolean.FALSE;
     Boolean actual = BooleanUtils.toBooleanObject(Boolean.FALSE);
     assertEquals(expected, actual);
 }

 @Test
 public void testToBooleanObjectBooleanNull() {
     assertNull(BooleanUtils.toBooleanObject(null));
 }

 // Test for toBooleanObject method with int values
 @Test
 public void testToBooleanObjectIntTrue() {
     Boolean expected = Boolean.TRUE;
     Boolean actual = BooleanUtils.toBooleanObject(1);
     assertEquals(expected, actual);
 }

 @Test
 public void testToBooleanObjectIntFalse() {
     Boolean expected = Boolean.FALSE;
     Boolean actual = BooleanUtils.toBooleanObject(0);
     assertEquals(expected, actual);
 }

 @Test
 public void testToBooleanObjectIntNull() {
     assertNull(BooleanUtils.toBooleanObject(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanObjectIntNonBoolean() {
     BooleanUtils.toBooleanObject(2);
 }

 // Test for toBoolean method with arrays
 @Test
 public void testToBooleanArrayTrue() {
     boolean[] expected = new boolean[] {true, true, true};
     Boolean[] actual = BooleanUtils.toBoolean(new boolean[] {true, true, true});
     assertArrayEquals(expected, actual);
 }

 @Test
 public void testToBooleanArrayFalse() {
     boolean[] expected = new boolean[] {false, false, false};
     Boolean[] actual = BooleanUtils.toBoolean(new boolean[] {false, false, false});
     assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToBooleanArrayNonBoolean() {
     BooleanUtils.toBoolean(new int[] {1, 2, 3});
 }

 // Test for and method
 @Test
 public void testAndTrueTrueTrue() {
     boolean expected = true;
     boolean[] array = new boolean[] {true, true, true};
     boolean actual = BooleanUtils.and(array);
     assertEquals(expected, actual);
 }

 @Test
 public void testAndTrueTrueFalse() {
     boolean expected = false;
     boolean[] array = new boolean[] {true, true, false};
     boolean actual = BooleanUtils.and(array);
     assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAndEmptyArray() {
     BooleanUtils.and(new boolean[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAndNullArray() {
     BooleanUtils.and(null);
 }

 @Test(expected = NullPointerException.class)
 public void testAndNullElement() {
     boolean[] array = new boolean[] {true, null, true};
     try {
         BooleanUtils.and(array);
     } catch (IllegalArgumentException ex) {
         fail("Expected NullPointerException to be thrown");
     }
 }

 // Test for and method with Boolean array
 @Test
 public void testAndBooleanTrueTrueTrue() {
     Boolean[] expected = new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
     Boolean[] actual = BooleanUtils.and(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE});
     assertArrayEquals(expected, actual);
 }

 @Test
 public void testAndBooleanTrueTrueFalse() {
     Boolean[] expected = new Boolean[] {Boolean.FALSE, Boolean.TRUE, Boolean.FALSE};
     Boolean[] actual = BooleanUtils.and(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.FALSE});
     assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAndBooleanEmptyArray() {
     BooleanUtils.and(new Boolean[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAndBooleanNullArray() {
     BooleanUtils.and(null);
 }

 @Test(expected = NullPointerException.class)
 public void testAndBooleanNullElement() {
     Boolean[] array = new Boolean[] {Boolean.TRUE, null, Boolean.TRUE};
     try {
         BooleanUtils.and(array);
     } catch (IllegalArgumentException ex) {
         fail("Expected NullPointerException to be thrown");
     }
 }

 // Test for or method
 @Test
 public void testOrTrueTrueTrue() {
     boolean expected = true;
     boolean[] array = new boolean[] {true, true, true};
     boolean actual = BooleanUtils.or(array);
     assertEquals(expected, actual);
 }

 @Test
 public void testOrTrueTrueFalse() {
     boolean expected = true;
     boolean[] array = new boolean[] {true, true, false};
     boolean actual = BooleanUtils.or(array);
     assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOrEmptyArray() {
     BooleanUtils.or(new boolean[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOrNullArray() {
     BooleanUtils.or(null);
 }

 @Test(expected = NullPointerException.class)
 public void testOrNullElement() {
     boolean[] array = new boolean[] {true, null, true};
     try {
         BooleanUtils.or(array);
     } catch (IllegalArgumentException ex) {
         fail("Expected NullPointerException to be thrown");
     }
 }

 // Test for or method with Boolean array
 @Test
 public void testOrBooleanTrueTrueTrue() {
     Boolean[] expected = new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
     Boolean[] actual = BooleanUtils.or(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE});
     assertArrayEquals(expected, actual);
 }

 @Test
 public void testOrBooleanTrueTrueFalse() {
     Boolean[] expected = new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.FALSE};
     Boolean[] actual = BooleanUtils.or(new Boolean[] {Boolean.TRUE, Boolean.TRUE, Boolean.FALSE});
     assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOrBooleanEmptyArray() {
     BooleanUtils.or(new Boolean[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOrBooleanNullArray() {
     BooleanUtils.or(null);
 }

 @Test(expected = NullPointerException.class)
 public void testOrBooleanNullElement() {
     Boolean[] array = new Boolean[] {Boolean.TRUE, null, Boolean.TRUE};
     try {
         BooleanUtils.or(array);
     } catch (IllegalArgumentException ex) {
         fail("Expected NullPointerException to be thrown");
     }
 }

 // Test for xor method
 @Test
 public void testXorTrueTrue() {
     boolean expected = false;
     boolean[] array = new boolean[] {true, true};
     boolean actual = BooleanUtils.xor(array);
     assertEquals(expected, actual);
 }

 @Test
 public void testXorTrueFalse() {
     boolean expected = true;
     boolean[] array = new boolean[] {true, false};
     boolean actual = BooleanUtils.xor(array);
     assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXorEmptyArray() {
     BooleanUtils.xor(new boolean[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXorNullArray() {
     BooleanUtils.xor(null);
 }

 @Test(expected = NullPointerException.class)
 public void testXorNullElement() {
     boolean[] array = new boolean[] {true, null, true};
     try {
         BooleanUtils.xor(array);
     } catch (IllegalArgumentException ex) {
         fail("Expected NullPointerException to be thrown");
     }
 }

 // Test for xor method with Boolean array
 @Test
 public void testXorBooleanTrueTrue() {
     Boolean[] expected = new Boolean[] {Boolean.FALSE, Boolean.FALSE};
     Boolean[] actual = BooleanUtils.xor(new Boolean[] {Boolean.TRUE, Boolean.TRUE});
     assertArrayEquals(expected, actual);
 }

 @Test
 public void testXorBooleanTrueFalse() {
     Boolean[] expected = new Boolean[] {Boolean.TRUE, Boolean.FALSE};
     Boolean[] actual = BooleanUtils.xor(new Boolean[] {Boolean.TRUE, Boolean.FALSE});
     assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXorBooleanEmptyArray() {
     BooleanUtils.xor(new Boolean[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXorBooleanNullArray() {
     BooleanUtils.xor(null);
 }

 @Test(expected = NullPointerException.class)
 public void testXorBooleanNullElement() {
     Boolean[] array = new Boolean[] {Boolean.TRUE, null, Boolean.TRUE};
     try {
         BooleanUtils.xor(array);
     } catch (IllegalArgumentException ex) {
         fail("Expected NullPointerException to be thrown");
     }
 }

}