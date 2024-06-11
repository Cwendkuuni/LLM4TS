import org.junit.Test;
import org.mockito.internal.matchers.Equality;
import org.mockito.internal.matchers.Equality_areArrayElementsEqual_constructor_test; // For testing isArrayElementsEqual method
import org.mockito.internal.matchers.Equality_areArrayLengthsEqual_constructor_test; // For testing areArrayLengthsEqual method
import org.mockito.internal.matchers.Equality_areEqual_constructor_test; // For testing areEqual method
import static org.junit.Assert.*;

public class EqualityTest {

 @Test
 public void testAreEqualTrue() {
 EqualityEquality equality = new EqualityEquality();
 Object o1 = new Object();
 Object o2 = new Object();
 assertTrue(equality.areEqual(o1, o2));
 }

 @Test
 public void testAreEqualFalseNull() {
 EqualityEquality equality = new EqualityEquality();
 Object o1 = null;
 Object o2 = new Object();
 assertFalse(equality.areEqual(o1, o2));
 }

 @Test
 public void testAreEqualFalseNull2() {
 EqualityEquality equality = new EqualityEquality();
 Object o1 = new Object();
 Object o2 = null;
 assertFalse(equality.areEqual(o1, o2));
 }

 @Test
 public void testAreEqualFalse() {
 EqualityEquality equality = new EqualityEquality();
 Object o1 = new Object();
 Object o2 = new Object() {
 @Override
 public boolean equals(Object o) {
 return false;
 }
 };
 assertFalse(equality.areEqual(o1, o2));
 }

 @Test
 public void testAreArraysEqualSameArray() {
 EqualityEquality equality = new EqualityEquality();
 Object o1 = new Object[] { 1, 2, 3, 4 };
 Object o2 = new Object[] { 1, 2, 3, 4 };
 assertTrue(equality.areArraysEqual(o1, o2));
 }

 @Test
 public void testAreArraysEqualDifferentLengths() {
 EqualityEquality equality = new EqualityEquality();
 Object o1 = new Object[] { 1, 2, 3 };
 Object o2 = new Object[] { 1, 2, 3, 4 };
 assertFalse(equality.areArraysEqual(o1, o2));
 }

 @Test
 public void testAreArraysEqualDifferentElements() {
 EqualityEquality equality = new EqualityEquality();
 Object o1 = new Object[] { 1, 2, 3 };
 Object o2 = new Object[] { 1, 2, 4 };
 assertFalse(equality.areArraysEqual(o1, o2));
 }

 @Test
 public void testIsArrayTrue() {
 EqualityEquality equality = new EqualityEquality();
 Object o1 = new Object[] { 1, 2, 3 };
 assertTrue(equality.isArray(o1));
 }

 @Test
 public void testIsArrayFalse() {
 EqualityEquality equality = new EqualityEquality();
 Object o1 = new Object();
 assertFalse(equality.isArray(o1));
 }

 @Test(expected = AssertionError.class)
 public void testAreEqualArrayLengthsDifferent() {
 EqualityEquality equality = new EqualityEquality();
 Object o1 = new Object[] { 1, 2, 3 };
 Object o2 = new Object[] { 1, 2 };
 try {
 equality.areArraysEqual(o1, o2);
 } catch (AssertionError e) {
 // Expected
 }
 }
}

class EqualityEquality extends Equality {
 // Empty
}