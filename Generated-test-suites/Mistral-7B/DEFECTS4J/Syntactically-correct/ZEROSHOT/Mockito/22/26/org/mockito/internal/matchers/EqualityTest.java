import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.matchers.Equality;
import org.mockito.internal.matchers.Equality.AreArraysEqual;

public class EqualityTest {

 @Test
 public void testAreEqual_whenBothNull_thenReturnTrue() {
 // Given
 Object o1 = null;
 Object o2 = null;

 // When
 boolean result = Equality.areEqual(o1, o2);

 // Then
 assertTrue(result);
 }

 @Test
 public void testAreEqual_whenBothNotNullAndEqual_thenReturnTrue() {
 // Given
 String value = "mockito";
 Object o1 = value;
 Object o2 = value;

 // When
 boolean result = Equality.areEqual(o1, o2);

 // Then
 assertTrue(result);
 }

 @Test
 public void testAreEqual_whenBothNotNullAndNotEqual_thenReturnFalse() {
 // Given
 String value1 = "mockito";
 String value2 = "junit";
 Object o1 = value1;
 Object o2 = value2;

 // When
 boolean result = Equality.areEqual(o1, o2);

 // Then
 assertFalse(result);
 }

 @Test
 public void testAreEqual_whenOneIsArray_andAnotherIsNotNull_thenReturnFalse() {
 // Given
 int[] array1 = {1, 2, 3};
 Object o1 = array1;
 String value = "mockito";
 Object o2 = value;

 // When
 boolean result = Equality.areEqual(o1, o2);

 // Then
 assertFalse(result);
 }

 @Test
 public void testAreEqual_whenBothAreArraysOfSameLengthAndElementsAreEqual_thenReturnTrue() {
 // Given
 int[] array1 = {1, 2, 3};
 int[] array2 = {1, 2, 3};
 Object o1 = array1;
 Object o2 = array2;

 // When
 boolean result = Equality.areEqual(o1, o2);

 // Then
 assertTrue(result);
 }

 @Test
 public void testAreEqual_whenBothAreArraysOfDifferentLengths_thenReturnFalse() {
 // Given
 int[] array1 = {1, 2, 3};
 int[] array2 = {1, 2};
 Object o1 = array1;
 Object o2 = array2;

 // When
 boolean result = Equality.areEqual(o1, o2);

 // Then
 assertFalse(result);
 }

 @Test
 public void testAreEqual_whenBothAreArraysOfSameLengthButElementsAreNotEqual_thenReturnFalse() {
 // Given
 int[] array1 = {1, 2, 3};
 int[] array2 = {1, 2, 4};
 Object o1 = array1;
 Object o2 = array2;

 // When
 boolean result = Equality.areEqual(o1, o2);

 // Then
 assertFalse(result);
 }

 @Test
 public void testAreEqual_whenBothAreArraysOfSameLengthButOneIsPrimitiveArray_thenReturnFalse() {
 // Given
 int[] intArray1 = {1, 2, 3};
 char[] charArray2 = {'a', 'b', 'c'};
 Object o1 = intArray1;
 Object o2 = charArray2;

 // When
 boolean result = Equality.areEqual(o1, o2);

 // Then
 assertFalse(result);
 }

 @Test
 public void testAreEqual_whenBothAreArraysOfSameTypeAndContentsAreEqual_butOneIsPrimitiveArray_thenReturnTrue() {
 // Given
 int[] intArray1 = {1, 2, 3};
 int[] intArray2 = {1, 2, 3};
 Object o1 = intArray1;
 Object o2 = intArray2;

 // When
 boolean result = Equality.areEqual(o1, o2);

 // Then
 assertTrue(result);
 }

 @Test
 public void testAreArraysEqual_whenArray1IsLongerThanArray2_thenReturnFalse() {
 // Given
 int[] array1 = {1, 2, 3, 4};
 int[] array2 = {1, 2, 3};
 Object o1 = array1;
 Object o2 = array2;

 // When
 boolean result = Equality.areArraysEqual(o1, o2);

 // Then
 assertFalse(result);
 }

 @Test
 public void testAreArraysEqual_whenArray1IsShorterThanArray2_thenReturnFalse() {
 // Given
 int[] array1 = {1, 2, 3};
 int[] array2 = {1, 2, 3, 4};
 Object o1 = array1;
 Object o2 = array2;

 // When
 boolean result = Equality.areArraysEqual(o1, o2);

 // Then
 assertFalse(result);
 }

 @Test
 public void testAreArraysEqual_whenBothArraysAreEmpty_thenReturnTrue() {
 // Given
 Object o1 = new Object[0];
 Object o2 = new Object[0];

 // When
 boolean result = Equality.areArraysEqual(o1, o2);

 // Then
 assertTrue(result);
 }

 @Test
 public void testAreArraysEqual_whenBothArraysContainEqualElements_thenReturnTrue() {
 // Given
 Object[] array1 = new Object[]{1, "mockito", new Integer(1)};
 Object[] array2 = new Object[]{1, "mockito", new Integer(1)};
 Object o1 = array1;
 Object o2 = array2;

 // When
 boolean result = Equality.areArraysEqual(o1, o2);

 // Then
 assertTrue(result);
 }

 @Test
 public void testAreArraysEqual_whenBothArraysContainDifferentTypes_thenReturnFalse() {
 // Given
 Object[] array1 = new Object[]{1, "mockito", new Integer(1)};
 Object[] array2 = new Object[]{"mockito", 2, new Float(1.0)};
 Object o1 = array1;
 Object o2 = array2;

 // When
 boolean result = Equality.areArraysEqual(o1, o2);

 // Then
 assertFalse(result);
 }

 @Test
 public void testAreArraysEqual_whenBothArraysContainSameTypesButDifferentValues_thenReturnFalse() {
 // Given
 Object[] array1 = new Object[]{1, 2, 3};
 Object[] array2 = new Object[]{2, 2, 3};
 Object o1 = array1;
 Object o2 = array2;

 // When
 boolean result = Equality.areArraysEqual(o1, o2);

 // Then
 assertFalse(result);
 }

 @Test
 public void testAreArraysEqual_whenBothArraysContainSameTypesAndValuesButDifferentOrder_thenReturnTrue() {
 // Given
 Object[] array1 = new Object[]{2, 1, 3};
 Object[] array2 = new Object[]{1, 3, 2};
 Object o1 = array1;
 Object o2 = array2;

 // When
 boolean result = Equality.areArraysEqual(o1, o2);

 // Then
 assertTrue(result);
 }

 @Test
 public void testAreArraysEqual_whenBothArraysContainSameTypesAndValuesButNullElements_thenReturnTrue() {
 // Given
 Object[] array1 = new Object[]{null, "mockito", new Integer(1)};
 Object[] array2 = new Object[]{null, "mockito", new Integer(1)};
 Object o1 = array1;
 Object o2 = array2;

 // When
 boolean result = Equality.areArraysEqual(o1, o2);

 // Then
 assertTrue(result);
 }

 @Test
 public void testIsArray_whenObjectIsArray_thenReturnTrue() {
 // Given
 int[] array = new int[5];
 Object o = array;

 // When
 boolean result = Equality.isArray(o);

 // Then
 assertTrue(result);
 }

 @Test
 public void testIsArray_whenObjectIsNotArray_thenReturnFalse() {
 // Given
 String value = "mockito";
 Object o = value;

 // When
 boolean result = Equality.isArray(o);

 // Then
 assertFalse(result);
 }
}