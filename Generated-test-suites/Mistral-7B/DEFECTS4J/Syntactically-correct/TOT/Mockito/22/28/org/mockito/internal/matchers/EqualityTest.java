import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {

 @Test
 public void testAreEqual_NullAndNull() {
 assert True(Equality.areEqual(null, null));
 }

 @Test
 public void testAreEqual_NullAndNonNull() {
 assert False(Equality.areEqual(null, new Object()[0]));
 }

 @Test
 public void testAreEqual_NonNullAndNull() {
 assert False(Equality.areEqual(new Object()[0], null));
 }

 @Test
 public void testAreEqual_DifferentObjects() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 assert False(Equality.areEqual(obj1, obj2));
 }

 @Test
 public void testAreEqual_SameObject() {
 Object obj = new Object();
 assert True(Equality.areEqual(obj, obj));
 }

 @Test
 public void testAreEqual_Arrays_SameLength_SameElements() {
 Object[] array1 = new Object[]{"one", "two", "three"};
 Object[] array2 = new Object[]{"one", "two", "three"};
 assert True(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqual_Arrays_SameLength_DifferentElements() {
 Object[] array1 = new Object[]{"one", "two", "three"};
 Object[] array2 = new Object[]{"one", "two", "three", "four"};
 assert False(Equality.areEqual(array1, array2));
 }

 @Test
 public void testAreEqual_Arrays_DifferentLength() {
 Object[] array1 = new Object[]{"one", "two"};
 Object[] array2 = new Object[]{"one", "two", "three"};
 assert False(Equality.areEqual(array1, array2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAreEqual_InvalidType() {
 Equality.areEqual(new Integer(1), new Double(1.0));
 }

 @Test
 public void testAreArrayLengthsEqual_SameArrays() {
 Object[] array1 = new Object[]{"one", "two", "three"};
 Object[] array2 = new Object[]{"one", "two", "three"};
 assert True(Equality.areArrayLengthsEqual(array1, array2));
 }

 @Test
 public void testAreArrayLengthsEqual_DifferentArrays() {
 Object[] array1 = new Object[]{"one", "two", "three"};
 Object[] array2 = new Object[]{"one", "two"};
 assert False(Equality.areArrayLengthsEqual(array1, array2));
 }

 @Test
 public void testAreArrayElementsEqual_SameArrays() {
 Object[] array1 = new Object[]{"one", "two", "three"};
 Object[] array2 = new Object[]{"one", "two", "three"};
 assert True(Equality.areArrayElementsEqual(array1, array2));
 }

 @Test
 public void testAreArrayElementsEqual_DifferentArrays() {
 Object[] array1 = new Object[]{"one", "two", "three"};
 Object[] array2 = new Object[]{"one", "two", "four"};
 assert False(Equality.areArrayElementsEqual(array1, array2));
 }

 @Test
 public void testIsArray_True() {
 Object[] array = new Object[0];
 assert True(Equality.isArray(array));
 }

 @Test
 public void testIsArray_False() {
 new Object();
 assert False(Equality.isArray(new Object()));
 }
}