package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {

ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

@Test
public void testToString() {
 MockName mockName = mock(MockName.class);
 MockitoMock mock = mock(Object.class);

 InvocationOnMock invocation = mock.mockingDetails().getInvocation("toString");

 String actual = (String) returnsEmptyValues.answer(invocation);

 String expected = "Mock for " + Object.class.getSimpleName() + ", hashCode: " + mock.hashCode();

 assertEquals(expected, actual);
}

@Test
public void testCompareTo() {
 Object obj1 = mock(Comparable.class);
 Object obj2 = mock(Comparable.class);

 InvocationOnMock compareToInvocation1 = mock.mockingDetails().getInvocation("compareTo", obj1);
 InvocationOnMock compareToInvocation2 = mock.mockingDetails().getInvocation("compareTo", obj2);

 int result1 = (Integer) returnsEmptyValues.answer(compareToInvocation1.getArguments());
 int result2 = (Integer) returnsEmptyValues.answer(compareToInvocation2.getArguments());

 assertEquals(0, result1); //comparing to the same object
 assertEquals(1, result2); //comparing to a different object
}

@Test
public void testPrimitiveTypes() {
 Byte byteReturnType = Byte.TYPE;
 Short shortReturnType = Short.TYPE;
 Integer intReturnType = Integer.TYPE;
 Long longReturnType = Long.TYPE;
 Float floatReturnType = Float.TYPE;
 Double doubleReturnType = Double.TYPE;
 Character charReturnType = Character.TYPE;
 Boolean boolReturnType = Boolean.TYPE;

 Class<?>[] primitiveTypes = { byteReturnType, shortReturnType, intReturnType, longReturnType, floatReturnType, doubleReturnType, charReturnType, boolReturnType };

 for (Class<?> primitiveType : primitiveTypes) {
 Object actual = returnsEmptyValues.returnValueFor(primitiveType);
 Object expected = Primitives.defaultValueForPrimitiveOrWrapper(primitiveType);

 assertEquals(expected, actual);
 }
}

@Test
public void testCollections() {
 Class<?> collectionType = Collection.class;
 Class<?> setType = Set.class;
 Class<?> hashSetType = HashSet.class;
 Class<?> sortedSetType = SortedSet.class;
 Class<?> treeSetType = TreeSet.class;
 Class<?> linkedHashSetType = LinkedHashSet.class;
 Class<?> listType = List.class;
 Class<?> linkedListType = LinkedList.class;
 Class<?> arrayListType = ArrayList.class;
 Class<?> mapType = Map.class;
 Class<?> hashMapType = HashMap.class;
 Class<?> sortedMapType = SortedMap.class;
 Class<?> treeMapType = TreeMap.class;
 Class<?> linkedHashMapType = LinkedHashMap.class;

 Class<?> types[] = { collectionType, setType, hashSetType, sortedSetType, treeSetType, linkedHashSetType, listType, linkedListType, arrayListType, mapType, hashMapType, sortedMapType, treeMapType, linkedHashMapType };

 for (Class<?> type : types) {
 Object actual = returnsEmptyValues.returnValueFor(type);

 if (Collection.class.isAssignableFrom(type)) {
 List<?> list = (List<?>) actual;
 assertEquals(0, list.size());
 } else if (Set.class.isAssignableFrom(type)) {
 Set<?> set = (Set<?>) actual;
 assertEquals(0, set.size());
 } else {
 assertEquals(null, actual);
 }
 }
}

}