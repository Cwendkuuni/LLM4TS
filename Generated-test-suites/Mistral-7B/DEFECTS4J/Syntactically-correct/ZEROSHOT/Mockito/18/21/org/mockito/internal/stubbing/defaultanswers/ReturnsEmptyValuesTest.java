package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import java.util.*;

import static org.junit.Assert.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void test_ReturnsEmptyCollectionForCollectionReturningMethods() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        CollectionMock collectionMock = Mockito.mock(Collection.class);
        Mockito.when(collectionMock.isEmpty()).thenReturn(true);
        Mockito.when(collectionMock.size()).thenReturn(0);

        Answer answer = Mockito.answer(returnsEmptyValues);
        Mockito.when(collectionMock).thenAnswer(answer);

        Object collection = collectionMock.getMock();

        List<Object> list = (List<Object>) Mockito.invokeMethod(collection, "asList");
        Set<Object> set = (Set<Object>) Mockito.invokeMethod(collection, "asSet");
        HashSet<Object> hashSet = (HashSet<Object>) Mockito.invokeMethod(collection, "asHashSet");
        LinkedHashSet<Object> linkedHashSet = (LinkedHashSet<Object>) Mockito.invokeMethod(collection, "asLinkedHashSet");
        SortedSet<Object> sortedSet = (SortedSet<Object>) Mockito.invokeMethod(collection, "asSortedSet");
        TreeSet<Object> treeSet = (TreeSet<Object>) Mockito.invokeMethod(collection, "asTreeSet");

        assertTrue(list instanceof LinkedList);
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());

        assertTrue(set instanceof HashSet);
        assertTrue(set.isEmpty());
        assertEquals(0, set.size());

        assertTrue(hashSet instanceof HashSet);
        assertTrue(hashSet.isEmpty());
        assertEquals(0, hashSet.size());

        assertTrue(linkedHashSet instanceof LinkedHashSet);
        assertTrue(linkedHashSet.isEmpty());
        assertEquals(0, linkedHashSet.size());

        assertTrue(sortedSet instanceof TreeSet);
        assertTrue(sortedSet.isEmpty());
        assertEquals(0, sortedSet.size());

        assertTrue(treeSet instanceof TreeSet);
        assertTrue(treeSet.isEmpty());
        assertEquals(0, treeSet.size());
    }

    @Test
    public void test_ReturnsEmptyMapForMapReturningMethods() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        MapMock mapMock = Mockito.mock(Map.class);
        Mockito.when(mapMock.isEmpty()).thenReturn(true);
        Mockito.when(mapMock.size()).thenReturn(0);

        Answer answer = Mockito.answer(returnsEmptyValues);
        Mockito.when(mapMock).thenAnswer(answer);

        Object map = mapMock.getMock();

        Map<Object, Object> hashMap = (Map<Object, Object>) Mockito.invokeMethod(map, "hashMap");
        Map<Object, Object> linkedHashMap = (Map<Object, Object>) Mockito.invokeMethod(map, "linkedHashMap");
        Map<Object, Object> treeMap = (Map<Object, Object>) Mockito.invokeMethod(map, "treeMap");

        assertTrue(hashMap instanceof HashMap);
        assertTrue(hashMap.isEmpty());
        assertEquals(0, hashMap.size());

        assertTrue(linkedHashMap instanceof LinkedHashMap);
        assertTrue(linkedHashMap.isEmpty());
        assertEquals(0, linkedHashMap.size());

        assertTrue(treeMap instanceof TreeMap);
        assertTrue(treeMap.isEmpty());
        assertEquals(0, treeMap.size());
    }

    @Test
    public void test_ReturnsZeroForComparableCompareToMethods() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        ComparableMock comparableMock = Mockito.mock(Comparable.class);

        Answer answer = Mockito.answer(returnsEmptyValues);
        Mockito.when(comparableMock).thenAnswer(answer);

        Object obj1 = Mockito.mock(Object.class);
        Object obj2 = Mockito.mock(Object.class);
        Mockito.when(comparableMock.compareTo(ArgumentMatchers.any())).thenReturn(0);

        int comparisonResult = (Integer) Mockito.invokeMethod(comparableMock, "compareTo", obj1);
        int comparisonResultWithDifferentObjects = (Integer) Mockito.invokeMethod(comparableMock, "compareTo", obj2);

        assertEquals(0, comparisonResult);
        assertEquals(1, comparisonResultWithDifferentObjects);
    }

    @Test
    public void test_ReturnsDefaultValueForPrimitiveAndWrapperMethods() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        PrimitiveMock primitiveMock = Mockito.mock(Number.class);

        Answer answer = Mockito.answer(returnsEmptyValues);
        Mockito.when(primitiveMock).thenAnswer(answer);

        int intValue = (Integer) Mockito.invokeMethod(primitiveMock, "intValue");
        double doubleValue = (Double) Mockito.invokeMethod(primitiveMock, "doubleValue");

        assertEquals(0, intValue.intValue());
        assertEquals(0.0, doubleValue.doubleValue(), 0.0);
    }

    @Test
    public void test_ReturnsNullForOtherMethods() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        ObjectMock objectMock = Mockito.mock(Object.class);

        Answer answer = Mockito.answer(returnsEmptyValues);
        Mockito.when(objectMock).thenAnswer(answer);

        Object nullValue = Mockito.invokeMethod(objectMock, "toString");
        Mockito.verify(objectMock).toString();

        assertNull(nullValue);
    }
}