package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collections;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToString() {
        Mockito<String> mockString = mock(String.class);
        Answer answer = new ReturnsEmptyValues();

        when(mockString).thenAnswer(answer);

        assertEquals("Mock for String, hashCode: 1352519834", mockString.toString());
    }

    @Test
    public void testCompareTo() {
        Object mockObject1 = mock(Object.class);
        Object mockObject2 = mock(Object.class);

        Answer answer = new ReturnsEmptyValues();

        when(mockObject1).thenAnswer(answer);
        when(mockObject2).thenAnswer(answer);

        int comparisonResult = mockObject1.compareTo(mockObject2);

        verify(mockObject1).compareTo(mockObject2);
        verify(mockObject2).compareTo(mockObject1);
        assertEquals(1, comparisonResult);
    }

    @Test
    public void testEmptyCollection() {
        Mockito<Collection<Integer>> mockCollection = mock(Collection.class);
        Answer answer = new ReturnsEmptyValues();

        when(mockCollection).thenAnswer(answer);

        List<Integer> collectedValues = new ArrayList<>();
        mockCollection.addAll(collectedValues);

        verifyZeroInteractions(mockCollection);
        assertEquals(0, collectedValues.size());
    }

    @Test
    public void testEmptySet() {
        Mockito<Set<Integer>> mockSet = mock(Set.class);
        Answer answer = new ReturnsEmptyValues();

        when(mockSet).thenAnswer(answer);

        mockSet.add(1);

        verifyZeroInteractions(mockSet);
        assertEquals(0, mockSet.size());
    }

    @Test
    public void testEmptyHashSet() {
        Mockito<HashSet<Integer>> mockHashSet = mock(HashSet.class);
        Answer answer = new ReturnsEmptyValues();

        when(mockHashSet).thenAnswer(answer);

        mockHashSet.add(1);

        verifyZeroInteractions(mockHashSet);
        assertEquals(0, mockHashSet.size());
    }

    @Test
    public void testEmptyTreeSet() {
        Mockito<TreeSet<Integer>> mockTreeSet = mock(TreeSet.class);
        Answer answer = new ReturnsEmptyValues();

        when(mockTreeSet).thenAnswer(answer);

        mockTreeSet.add(1);

        verifyZeroInteractions(mockTreeSet);
        assertEquals(0, mockTreeSet.size());
    }

    @Test
    public void testEmptyLinkedHashSet() {
        Mockito<LinkedHashSet<Integer>> mockLinkedHashSet = mock(LinkedHashSet.class);
        Answer answer = new ReturnsEmptyValues();

        when(mockLinkedHashSet).thenAnswer(answer);

        mockLinkedHashSet.add(1);

        verifyZeroInteractions(mockLinkedHashSet);
        assertEquals(0, mockLinkedHashSet.size());
    }

    @Test
    public void testEmptyList() {
        Mockito<List<Integer>> mockList = mock(List.class);
        Answer answer = new ReturnsEmptyValues();

        when(mockList).thenAnswer(answer);

        List<Integer> collectedValues = new ArrayList<>();
        mockList.addAll(collectedValues);

        verifyZeroInteractions(mockList);
        assertEquals(0, collectedValues.size());
    }

    @Test
    public void testEmptyLinkedList() {
        Mockito<LinkedList<Integer>> mockLinkedList = mock(LinkedList.class);
        Answer answer = new ReturnsEmptyValues();

        when(mockLinkedList).thenAnswer(answer);

        List<Integer> collectedValues = new ArrayList<>();
        mockLinkedList.addAll(collectedValues);

        verifyZeroInteractions(mockLinkedList);
        assertEquals(0, collectedValues.size());
    }

    @Test
    public void testEmptyArrayList() {
        Mockito<ArrayList<Integer>> mockArrayList = mock(ArrayList.class);
        Answer answer = new ReturnsEmptyValues();

        when(mockArrayList).thenAnswer(answer);

        List<Integer> collectedValues = new ArrayList<>();
        mockArrayList.addAll(collectedValues);

        verifyZeroInteractions(mockArrayList);
        assertEquals(0, collectedValues.size());
    }

    @Test
    public void testEmptyHashMap() {
        Mockito<Map<Integer, String>> mockHashMap = mock(Map.class);
        Answer answer = new ReturnsEmptyValues();

        when(mockHashMap).thenAnswer(answer);

        mockHashMap.put(1, "value");

        verifyZeroInteractions(mockHashMap);
        assertEquals(0, mockHashMap.size());
    }

    @Test
    public void testEmptyHashMapWithIntegerKeys() {
        Mockito<Map<Integer, Integer>> mockHashMap = mock(Map.class);
        Answer answer = new ReturnsEmptyValues();

        when(mockHashMap).thenAnswer(answer);

        mockHashMap.put(1, 1);

        verifyZeroInteractions(mockHashMap);
        assertEquals(0, mockHashMap.size());
    }

    @Test
    public void testEmptyTreeMap() {
        Mockito<SortedMap<Integer, String>> mockTreeMap = mock(SortedMap.class);
        Answer answer = new ReturnsEmptyValues();

        when(mockTreeMap).thenAnswer(answer);

        mockTreeMap.put(1, "value");

        verifyZeroInteractions(mockTreeMap);
        assertEquals(0, mockTreeMap.size());
    }

    @Test
    public void testEmptyTreeMapWithIntegerKeys() {
        Mockito<SortedMap<Integer, Integer>> mockTreeMap = mock(SortedMap.class);
        Answer answer = new ReturnsEmptyValues();

        when(mockTreeMap).thenAnswer(answer);

        mockTreeMap.put(1, 1);

        verifyZeroInteractions(mockTreeMap);
        assertEquals(0, mockTreeMap.size());
    }

    @Test
    public void testEmptyLinkedHashMap() {
        Mockito<LinkedHashMap<Integer, String>> mockLinkedHashMap = mock(LinkedHashMap.class);
        Answer answer = new ReturnsEmptyValues();

        when(mockLinkedHashMap).thenAnswer(answer);

        mockLinkedHashMap.put(1, "value");

        verifyZeroInteractions(mockLinkedHashMap);
        assertEquals(0, mockLinkedHashMap.size());
    }

    @Test
    public void testEmptyLinkedHashMapWithIntegerKeys() {
        Mockito<LinkedHashMap<Integer, Integer>> mockLinkedHashMap = mock(LinkedHashMap.class);
        Answer answer = new ReturnsEmptyValues();

        when(mockLinkedHashMap).thenAnswer(answer);

        mockLinkedHashMap.put(1, 1);

        verifyZeroInteractions(mockLinkedHashMap);
        assertEquals(0, mockLinkedHashMap.size());
    }
}