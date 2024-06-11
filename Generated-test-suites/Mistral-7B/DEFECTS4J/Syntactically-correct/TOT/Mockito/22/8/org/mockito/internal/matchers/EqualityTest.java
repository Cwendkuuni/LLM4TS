import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {

 @Test
 public void testAreEqualNullObjects() {
 // Test case for areEqual(null, null)
 Assert.assertTrue(Equality.areEqual(null, null));
 }

 @Test
 public void testAreEqualSameObject() {
 // Test case for areEqual(object, object) where objects are the same instance
 Integer testObject = new Integer(1);
 Assert.assertTrue(Equality.areEqual(testObject, testObject));
 }

 @Test
 public void testAreEqualDifferentObjectsWithEqualValues() {
 // Test case for areEqual(object1, object2) where objects have equal values
 Integer obj1 = new Integer(1);
 Integer obj2 = new Integer(1);
 Assert.assertTrue(Equality.areEqual(obj1, obj2));
 }

 @Test
 public void testAreEqualDifferentObjectsWithDifferentValues() {
 // Test case for areEqual(object1, object2) where objects have different values
 Integer obj1 = new Integer(1);
 Integer obj2 = new Integer(2);
 Assert.assertFalse(Equality.areEqual(obj1, obj2));
 }

 @Test
 public void testAreEqualArraysWithEqualLengthAndEqualElements() {
 // Test case for areArraysEqual(object1, object2) where arrays have equal length and equal elements
 Object[] arr1 = new Object[]{new Integer(1), new Integer(2)};
 Object[] arr2 = new Object[]{new Integer(1), new Integer(2)};
 Assert.assertTrue(Equality.areArraysEqual(arr1, arr2));
 }

 @Test
 public void testAreEqualArraysWithDifferentLengths() {
 // Test case for areArraysEqual(object1, object2) where arrays have different lengths
 Object[] arr1 = new Object[]{new Integer(1), new Integer(2)};
 Object[] arr2 = new Object[]{new Integer(1), new Integer(2), new Integer(3)};
 Assert.assertFalse(Equality.areArraysEqual(arr1, arr2));
 }

 @Test
 public void testAreEqualArraysWithEqualLengthButDifferentElements() {
 // Test case for areArraysEqual(object1, object2) where arrays have equal length but different elements
 Object[] arr1 = new Object[]{new Integer(1), new Integer(2)};
 Object[] arr2 = new Object[]{new Integer(2), new Integer(1)};
 Assert.assertFalse(Equality.areArraysEqual(arr1, arr2));
 }

 @Test
 public void testAreEqualArraysWithDifferentLengthsButSameElements() {
 // Test case for areArraysEqual(object1, object2) where arrays have different lengths but same elements
 Object[] arr1 = new Object[]{new Integer(1), new Integer(2)};
 Object[] arr2 = new Object[]{new Integer(2), new Integer(1), new Integer(2)};
 Assert.assertTrue(Equality.areArraysEqual(arr1, arr2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAreEqualNullArray() {
 // Test case for an IllegalArgumentException when areArraysEqual(object1, null) is called
 Object[] arr = null;
 Assert.assertArrayEquals(new Object[0], arr); // Calling this line to initialize arr for Equality.areArraysEqual()
 Assert.assertFalse(Equality.areArraysEqual(new Object[]{1}, arr));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAreEqualArrayWithNullElement() {
 // Test case for an IllegalArgumentException when areArrayElementsEqual(object1, object2) is called with a null element
 Object[] arr = new Object[]{new Integer(1), null};
 Assert.assertFalse(Equality.areEqual(arr[0], null)); // Calling this line to initialize arr[0] for Equality.areArrayElementsEqual()
 Assert.assertThrows(IllegalArgumentException.class, () -> Equality.areArraysEqual(arr, new Object[0]));
 }
}