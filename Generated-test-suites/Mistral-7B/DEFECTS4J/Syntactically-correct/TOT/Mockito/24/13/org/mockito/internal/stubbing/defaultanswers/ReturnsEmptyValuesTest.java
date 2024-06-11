import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

 @InjectMocks
 private ReturnsEmptyValues returnsEmptyValues;

 @Test
 public void testToStringMethod() {
 Mockito.doReturn(mock(Object.class)).when(returnsEmptyValues).answer(eq(new Object() {
 @Override
 public String toString() {
 return "TestObject";
 }
 })).calledTimes(1);

 String result = returnsEmptyValues.answer(Mockito.mock(MethodCall.class));
 assertEquals("Mock for TestObject, hashCode: 123456", result);
 }

 @Test
 public void testCompareToMethod() {
 Object mock1 = mock(Comparable.class);
 Object mock2 = mock(Comparable.class);

 when(mock1.compareTo(mock2)).thenCallRealMethod();
 when(mock2.compareTo(mock1)).thenCallRealMethod();

 int result1 = ((Comparable) returnsEmptyValues).answer(Mockito.mock(MethodCall.class)).compareTo(mock1);
 int result2 = ((Comparable) returnsEmptyValues).answer(Mockito.mock(MethodCall.class)).compareTo(mock2);

 assertEquals(1, result1);
 assertEquals(1, result2);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyEmptyCollection() {
 // Given
 Collection<Integer> mockCollection = Mockito.mock(Collection.class);
 when(mockCollection.getClass()).thenReturn(ArrayList.class);

 // When
 List<Integer> list = (List<Integer>) returnsEmptyValues.answer(Mockito.mock(MethodCall.class));
 list.add(1);

 // Then
 verify(mockCollection, times(1)).add(anyInt());
 }

 @Test
 public void testPrimitiveTypes() {
 // Given
 byte byteValue = 127;
 short shortValue = 32767;
 int intValue = 123456;
 long longValue = 9876543210L;
 float floatValue = 3.14f;
 double doubleValue = 1.23456789;
 char charValue = 'A';
 boolean booleanValue = true;

 // When
 byte byteAnswer = (byte) returnsEmptyValues.answer(Mockito.mock(MethodCall.class)).byteValue();
 short shortAnswer = (short) returnsEmptyValues.answer(Mockito.mock(MethodCall.class)).shortValue();
 int intAnswer = (int) returnsEmptyValues.answer(Mockito.mock(MethodCall.class)).intValue();
 long longAnswer = (long) returnsEmptyValues.answer(Mockito.mock(MethodCall.class)).longValue();
 float floatAnswer = (float) returnsEmptyValues.answer(Mockito.mock(MethodCall.class)).floatValue();
 double doubleAnswer = (double) returnsEmptyValues.answer(Mockito.mock(MethodCall.class)).doubleValue();
 char charAnswer = (char) returnsEmptyValues.answer(Mockito.mock(MethodCall.class)).charValue();
 boolean booleanAnswer = (boolean) returnsEmptyValues.answer(Mockito.mock(MethodCall.class)).booleanValue();

 // Then
 assertEquals(Byte.MIN_VALUE, byteAnswer);
 assertEquals(Short.MIN_VALUE, shortAnswer);
 assertEquals(0, intAnswer);
 assertEquals(Long.MIN_VALUE, longAnswer);
 assertEquals(0.0f, floatAnswer, 0.001f);
 assertEquals(0.0d, doubleAnswer, 0.001d);
 assertEquals((char) 0, charAnswer);
 assertEquals(false, booleanAnswer);
 }

 @Test
 public void testCollectionTypes() {
 // Given
 Collection<Integer> mockCollection = Mockito.mock(Collection.class);
 when(mockCollection.getClass()).thenReturn(ArrayList.class);

 // When
 Collection<Object> emptyCollection = returnsEmptyValues.answer(Mockito.mock(MethodCall.class));

 // Then
 assertTrue(emptyCollection instanceof LinkedList);
 assertTrue(emptyCollection.isEmpty());
 }

 @Test
 public void testSetTypes() {
 // Given
 Set<String> mockSet = Mockito.mock(Set.class);
 when(mockSet.getClass()).thenReturn(HashSet.class);

 // When
 Set<Object> emptySet = returnsEmptyValues.answer(Mockito.mock(MethodCall.class));

 // Then
 assertTrue(emptySet instanceof HashSet);
 assertTrue(emptySet.isEmpty());
 }

 @Test
 public void testMapTypes() {
 // Given
 Map<String, Integer> mockMap = Mockito.mock(Map.class);
 when(mockMap.getClass()).thenReturn(HashMap.class);

 // When
 Map<Object, Object> emptyMap = returnsEmptyValues.answer(Mockito.mock(MethodCall.class));

 // Then
 assertTrue(emptyMap instanceof HashMap);
 assertTrue(emptyMap.isEmpty());
 }

 @Test
 public void testSortedMapTypes() {
 // Given
 SortedMap<String, Integer> mockSortedMap = Mockito.mock(SortedMap.class);
 when(mockSortedMap.getClass()).thenReturn(TreeMap.class);

 // When
 SortedMap<Object, Object> emptySortedMap = returnsEmptyValues.answer(Mockito.mock(MethodCall.class));

 // Then
 assertTrue(emptySortedMap instanceof TreeMap);
 assertTrue(emptySortedMap.isEmpty());
 }
}