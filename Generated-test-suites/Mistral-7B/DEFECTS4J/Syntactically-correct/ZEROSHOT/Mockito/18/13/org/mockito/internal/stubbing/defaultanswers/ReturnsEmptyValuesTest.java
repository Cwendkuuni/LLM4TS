package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

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

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToString() {
        Object mock = mock(String.class);
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        given(mock.toString()).willReturn(answer);

        String result = Mockito.callRealMethod(mock, "toString");

        assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testCompareTo() {
        Object mock = mock(Comparable.class);
        Object anotherMock = mock(Comparable.class);

        given(mock.compareTo(ArgumentMatchers.any())).willReturn(answer);
        given(anotherMock.compareTo(ArgumentMatchers.any())).willReturn(answer);

        int result = Mockito.callRealMethod(mock, "compareTo", anotherMock);

        assertEquals(1, result);
    }

    @Test
    public void testPrimitiveReturningMethods() {
        Object mock = mock(byte.class);

        byte result = Mockito.callRealMethod(mock, "byteValue");

        assertEquals((byte) 0, result);

        // test for wrapper class
        Object intWrapperMock = mock(Integer.class);

        int resultInt = Mockito.callRealMethod(intWrapperMock, "intValue");

        assertEquals(0, resultInt);
    }

    @Test
    public void testCollectionReturningMethods() {
        Object mock = mock(Collection.class);

        Collection<Object> result = Mockito.callRealMethod(mock, "isEmpty") ? (Collection<Object>) Mockito.callRealMethod(mock, "get") : Mockito.callRealMethod(mock, "toArray");

        assertEquals(new LinkedList<>(), result);

        // test for specific collection types
        Object listMock = mock(List.class);

        List<Object> resultList = Mockito.callRealMethod(listMock, "isEmpty") ? (List<Object>) Mockito.callRealMethod(listMock, "get") : Mockito.callRealMethod(listMock, "toArray");

        assertEquals(new ArrayList<>(), resultList);

        Object setMock = mock(Set.class);

        Set<Object> resultSet = Mockito.callRealMethod(setMock, "isEmpty") ? (Set<Object>) Mockito.callRealMethod(setMock, "add", ArgumentMatchers.anyObject()) : Mockito.callRealMethod(setMock, "toArray");

        assertEquals(new HashSet<>(), resultSet);

        Object hashSetMock = mock(HashSet.class);

        Set<Object> resultHashSet = Mockito.callRealMethod(hashSetMock, "isEmpty") ? (Set<Object>) Mockito.callRealMethod(hashSetMock, "add", ArgumentMatchers.anyObject()) : Mockito.callRealMethod(hashSetMock, "toArray");

        assertEquals(new LinkedHashSet<>(), resultHashSet);

        Object linkedHashSetMock = mock(LinkedHashSet.class);

        Set<Object> resultLinkedHashSet = Mockito.callRealMethod(linkedHashSetMock, "isEmpty") ? (Set<Object>) Mockito.callRealMethod(linkedHashSetMock, "add", ArgumentMatchers.anyObject()) : Mockito.callRealMethod(linkedHashSetMock, "toArray");

        assertEquals(new LinkedHashSet<>(), resultLinkedHashSet);
    }

    @Test
    public void testMapReturningMethods() {
        Object mock = mock(Map.class);

        Map<Object, Object> result = Mockito.callRealMethod(mock, "isEmpty") ? (Map<Object, Object>) Mockito.callRealMethod(mock, "get", ArgumentMatchers.any()) : Mockito.callRealMethod(mock, "entrySet").get(0).getValue();

        assertEquals(new HashMap<>(), result);

        // test for specific map types
        Object mapMock = mock(Map.class);

        Map<Object, Object> resultMap = Mockito.callRealMethod(mapMock, "isEmpty") ? (Map<Object, Object>) Mockito.callRealMethod(mapMock, "get", ArgumentMatchers.any()) : Mockito.callRealMethod(mapMock, "entrySet").get(0).getValue();

        assertEquals(new HashMap<>(), resultMap);

        Object sortedMapMock = mock(SortedMap.class);

        Map<Object, Object> resultSortedMap = Mockito.callRealMethod(sortedMapMock, "isEmpty") ? (Map<Object, Object>) Mockito.callRealMethod(sortedMapMock, "get", ArgumentMatchers.any()) : Mockito.callRealMethod(sortedMapMock, "firstEntry").getValue();

        assertEquals(new TreeMap<>(), resultSortedMap);

        Object treeMapMock = mock(Map.class);

        Map<Object, Object> resultTreeMap = Mockito.callRealMethod(treeMapMock, "isEmpty") ? (Map<Object, Object>) Mockito.callRealMethod(treeMapMock, "get", ArgumentMatchers.any()) : Mockito.callRealMethod(treeMapMock, "firstEntry").getValue();

        assertEquals(new TreeMap<>(), resultTreeMap);

        Object linkedHashMapMock = mock(Map.class);

        Map<Object, Object> resultLinkedHashMap = Mockito.callRealMethod(linkedHashMapMock, "isEmpty") ? (Map<Object, Object>) Mockito.callRealMethod(linkedHashMapMock, "get", ArgumentMatchers.any()) : Mockito.callRealMethod(linkedHashMapMock, "entrySet").get(0).getValue();

        assertEquals(new LinkedHashMap<>(), resultLinkedHashMap);

        Object linkedTreeMapMock = mock(Map.class);

        Map<Object, Object> resultLinkedTreeMap = Mockito.callRealMethod(linkedTreeMapMock, "isEmpty") ? (Map<Object, Object>) Mockito.callRealMethod(linkedTreeMapMock, "get", ArgumentMatchers.any()) : Mockito.callRealMethod(linkedTreeMapMock, "entrySet").get(0).getValue();

        assertEquals(new LinkedHashMap<>(), resultLinkedTreeMap);
    }
}