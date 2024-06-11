package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
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
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues;

    @Before
    public void setup() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }

    @Test
    public void testToStringMethod() {
        Object mock = Mockito.mock(String.class);
        Answer<String> answer = returnsEmptyValues;
        String expected = "Mock for " + String.class.getSimpleName() + ", hashCode: " + mock.hashCode();
        when(mock.toString()).thenAnswer(answer);
        assertEquals(expected, mock.toString());
    }

    @Test
    public void testCompareToMethod() {
        Object mock1 = Mockito.mock(Comparable.class);
        Object mock2 = Mockito.mock(Comparable.class);
        Answer<Integer> answer = returnsEmptyValues;

        when(mock1.compareTo(ArgumentMatchers.any())).thenAnswer(answer);
        when(mock2.compareTo(ArgumentMatchers.any())).thenAnswer(answer);

        assertEquals(0, mock1.compareTo(mock1));
        assertEquals(1, mock1.compareTo(mock2));
    }

    @Test
    public void testEmptyCollection() {
        List<Integer> list = Mockito.mock(List.class);
        Answer<List<Integer>> answer = returnsEmptyValues;

        when(list.isEmpty()).thenReturn(true);
        when(list.size()).thenReturn(0);
        when(list.getClass()).thenReturn(ArrayList.class);
        when(list.toString()).thenReturn("[]");
        when(list.iterator()).thenReturn(Collections.emptyIterator());

        when(list.getClass()).thenReturn(LinkedList.class);
        when(list.isEmpty()).thenReturn(true);
        when(list.size()).thenReturn(0);
        when(list.toString()).thenReturn("[]");
        when(list.iterator()).thenReturn(Collections.emptyIterator());

        when(list.getClass()).thenReturn(ArrayList.class);
        when(list.isEmpty()).thenReturn(false);
        when(list.size()).thenReturn(1);
        when(list.get(0)).thenReturn(1);

        Answer<List<Integer>> answerWithSingleElement = returnsEmptyValues;
        when(list.getClass()).thenReturn(ArrayList.class);
        when(list.isEmpty()).thenReturn(false);
        when(list.size()).thenReturn(1);
        when(list.get(0)).thenAnswer(answerWithSingleElement);

        List<Integer> result1 = answer.answer(new InvocationOnMock(list, "size"));
        List<Integer> result2 = answer.answer(new InvocationOnMock(list, "iterator"));
        List<Integer> result3 = answer.answer(new InvocationOnMock(list, "get", 0));

        assertNotNull(result1);
        assertEquals(Collections.emptyList(), result1);

        assertNotNull(result2);
        assertEquals(Collections.emptyIterator(), result2);

        assertNotNull(result3);
        assertEquals(Collections.emptyList(), result3);
    }

    @Test
    public void testEmptySet() {
        Set<Integer> set = Mockito.mock(Set.class);
        Answer<Set<Integer>> answer = returnsEmptyValues;

        when(set.isEmpty()).thenReturn(true);
        when(set.size()).thenReturn(0);
        when(set.toString()).thenReturn("[]");

        Set<Integer> result = answer.answer(new InvocationOnMock(set, "size"));
        assertNotNull(result);
        assertEquals(Collections.emptySet(), result);
    }

    @Test
    public void testEmptyHashSet() {
        Set<Integer> hashSet = Mockito.mock(HashSet.class);
        Answer<Set<Integer>> answer = returnsEmptyValues;

        when(hashSet.isEmpty()).thenReturn(true);
        when(hashSet.size()).thenReturn(0);
        when(hashSet.toString()).thenReturn("[ ]");

        Set<Integer> result = answer.answer(new InvocationOnMock(hashSet, "size"));
        assertNotNull(result);
        assertEquals(Collections.emptySet(), result);
    }

    @Test
    public void testEmptyTreeSet() {
        SortedSet<Integer> treeSet = Mockito.mock(TreeSet.class);
        Answer<SortedSet<Integer>> answer = returnsEmptyValues;

        when(treeSet.isEmpty()).thenReturn(true);
        when(treeSet.size()).thenReturn(0);
        when(treeSet.toString()).thenReturn("[ ]");

        SortedSet<Integer> result = answer.answer(new InvocationOnMock(treeSet, "size"));
        assertNotNull(result);
        assertEquals(Collections.emptySortedSet(), result);
    }

    @Test
    public void testEmptyLinkedHashSet() {
        SortedSet<Integer> linkedHashSet = Mockito.mock(LinkedHashSet.class);
        Answer<SortedSet<Integer>> answer = returnsEmptyValues;

        when(linkedHashSet.isEmpty()).thenReturn(true);
        when(linkedHashSet.size()).thenReturn(0);
        when(linkedHashSet.toString()).thenReturn("[ ]");

        SortedSet<Integer> result = answer.answer(new InvocationOnMock(linkedHashSet, "size"));
        assertNotNull(result);
        assertEquals(Collections.emptySortedSet(), result);
    }

    @Test
    public void testEmptyMap() {
        Map<Integer, String> map = Mockito.mock(Map.class);
        Answer<Map<Integer, String>> answer = returnsEmptyValues;

        when(map.isEmpty()).thenReturn(true);
        when(map.size()).thenReturn(0);
        when(map.toString()).thenReturn("{}");

        Map<Integer, String> result = answer.answer(new InvocationOnMock(map, "size"));
        assertNotNull(result);
        assertEquals(Collections.emptyMap(), result);
    }

    @Test
    public void testEmptyLinkedHashMap() {
        Map<Integer, String> linkedHashMap = Mockito.mock(LinkedHashMap.class);
        Answer<Map<Integer, String>> answer = returnsEmptyValues;

        when(linkedHashMap.isEmpty()).thenReturn(true);
        when(linkedHashMap.size()).thenReturn(0);
        when(linkedHashMap.toString()).thenReturn("{}");

        Map<Integer, String> result = answer.answer(new InvocationOnMock(linkedHashMap, "size"));
        assertNotNull(result);
        assertEquals(Collections.emptyMap(), result);
    }

    @Test
    public void testEmptyTreeMap() {
        SortedMap<Integer, String> treeMap = Mockito.mock(TreeMap.class);
        Answer<SortedMap<Integer, String>> answer = returnsEmptyValues;

        when(treeMap.isEmpty()).thenReturn(true);
        when(treeMap.size()).thenReturn(0);
        when(treeMap.toString()).thenReturn("{}");

        SortedMap<Integer, String> result = answer.answer(new InvocationOnMock(treeMap, "size"));
        assertNotNull(result);
        assertEquals(Collections.emptySortedMap(), result);
    }
}