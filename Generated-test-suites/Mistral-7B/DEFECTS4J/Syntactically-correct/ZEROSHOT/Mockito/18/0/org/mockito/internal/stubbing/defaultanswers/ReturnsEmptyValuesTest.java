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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = Mockito.mock(String.class);

        // When
        String toStringAnswer = (String) returnsEmptyValues.answer(Mockito.argument(0, String.class, ArgumentMatchers.isA(String.class)), mock);

        // Then
        assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), toStringAnswer);

        // When and Then
        Mockito.verify(mock).toString();
    }

    @Test
    public void testCompareToAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object obj1 = Mockito.mock(Comparable.class);
        Object obj2 = Mockito.mock(Comparable.class);

        // When
        int compareToAnswer = (Integer) returnsEmptyValues.answer(Mockito.argument(0, Comparable.class, ArgumentMatchers.isInstanceOf(Comparable.class)), obj1, ArgumentMatchers.eq(obj2));

        // Then
        assertEquals(0, compareToAnswer);

        // When
        Mockito.verify(obj1).compareTo(obj2);
    }

    @Test
    public void testEmptyCollectionAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        Collection<Integer> collectionAnswer = (Collection<Integer>) returnsEmptyValues.answer(Mockito.argument(0, Collection.class, ArgumentMatchers.isA(Collection.class)));

        // Then
        assertInstanceOf(LinkedList.class, collectionAnswer);
        assertNull(collectionAnswer.toArray());
    }

    @Test
    public void testEmptySetAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        Set<Integer> setAnswer = (Set<Integer>) returnsEmptyValues.answer(Mockito.argument(0, Set.class, ArgumentMatchers.isA(Set.class)));

        // Then
        assertInstanceOf(HashSet.class, setAnswer);
        assertNull(setAnswer.toArray());
    }

    @Test
    public void testEmptyHashSetAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        Set<Integer> hashSetAnswer = (Set<Integer>) returnsEmptyValues.answer(Mockito.argument(0, HashSet.class, ArgumentMatchers.isA(HashSet.class)));

        // Then
        assertInstanceOf(HashSet.class, hashSetAnswer);
        assertNull(hashSetAnswer.toArray());
    }

    @Test
    public void testEmptyTreeSetAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        SortedSet<Integer> treeSetAnswer = (SortedSet<Integer>) returnsEmptyValues.answer(Mockito.argument(0, SortedSet.class, ArgumentMatchers.isA(SortedSet.class)));

        // Then
        assertInstanceOf(TreeSet.class, treeSetAnswer);
        assertNull(treeSetAnswer.toArray());
    }

    @Test
    public void testEmptyTreeMapAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        Map<String, Integer> treeMapAnswer = (Map<String, Integer>) returnsEmptyValues.answer(Mockito.argument(0, Map.class, ArgumentMatchers.isA(Map.class)));

        // Then
        assertInstanceOf(TreeMap.class, treeMapAnswer);
        assertEquals(0, treeMapAnswer.size());
    }

    @Test
    public void testEmptyLinkedHashMapAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        Map<String, Integer> linkedHashMapAnswer = (Map<String, Integer>) returnsEmptyValues.answer(Mockito.argument(0, LinkedHashMap.class, ArgumentMatchers.isA(LinkedHashMap.class)));

        // Then
        assertInstanceOf(LinkedHashMap.class, linkedHashMapAnswer);
        assertEquals(0, linkedHashMapAnswer.size());
    }

    @Test
    public void testEmptyArrayListAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        List<Integer> listAnswer = (List<Integer>) returnsEmptyValues.answer(Mockito.argument(0, List.class, ArgumentMatchers.isA(List.class)));

        // Then
        assertInstanceOf(LinkedList.class, listAnswer);
        assertNull(listAnswer.toArray());
    }

    @Test
    public void testEmptyLinkedListAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        List<Integer> linkedListAnswer = (List<Integer>) returnsEmptyValues.answer(Mockito.argument(0, LinkedList.class, ArgumentMatchers.isA(LinkedList.class)));

        // Then
        assertInstanceOf(LinkedList.class, linkedListAnswer);
        assertNull(linkedListAnswer.toArray());
    }

    @Test
    public void testEmptyArrayListAnswerWithPrimitiveType() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        List<Integer> listAnswer = (List<Integer>) returnsEmptyValues.answer(Mockito.argument(0, List.class, ArgumentMatchers.isA(List.class)), Mockito.mock(Integer[].class));

        // Then
        assertInstanceOf(LinkedList.class, listAnswer);
        assertNull(listAnswer.toArray());
    }

    @Test
    public void testEmptyArrayListAnswerWithPrimitiveTypeWrapper() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        List<Integer> listAnswer = (List<Integer>) returnsEmptyValues.answer(Mockito.argument(0, List.class, ArgumentMatchers.isA(List.class)), Mockito.mock(Integer[].class));

        // Then
        assertInstanceOf(LinkedList.class, listAnswer);
        assertNull(listAnswer.toArray());
    }
}