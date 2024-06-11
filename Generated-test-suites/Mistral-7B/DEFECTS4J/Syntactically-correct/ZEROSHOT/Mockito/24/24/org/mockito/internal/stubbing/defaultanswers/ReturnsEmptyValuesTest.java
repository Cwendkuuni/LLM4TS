package org.mockito.internal.stubbing.defaultanswers.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.InvocationOnMock;
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
import static org.junit.Assert.assertNotNull;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringAnswer() {
        // given
        Answer<Object> answer = Answers.delegateAnswers(new ReturnsEmptyValues());
        Object mock = Mockito.mock(String.class);

        // when
        String toStringResult = (String) answer.answer(Mockito.mockingDetails(mock, "toString"));

        // then
        assertNotNull(toStringResult);
        assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), toStringResult);
    }

    @Test
    public void testCompareToAnswer() {
        // given
        Answer<Object> answer = Answers.delegateAnswers(new ReturnsEmptyValues());

        // when
        int compareToResult = (Integer) answer.answer(Mockito.mockingDetails(Mockito.mock(Comparable.class), "compareTo"));

        // then
        assertEquals(1, compareToResult);
    }

    @Test
    public void testPrimitiveAnswer() {
        // given
        Answer<Object> answer = Answers.delegateAnswers(new ReturnsEmptyValues());
        Class<?> booleanType = boolean.class;

        // when
        Boolean booleanResult = (Boolean) answer.answer(Mockito.mockingDetails(Mockito.mock(booleanType), "booleanValue"));

        // then
        assertEquals(false, booleanResult);
    }

    @Test
    public void testEmptyCollectionAnswer() {
        // given
        Answer<Object> answer = Answers.delegateAnswers(new ReturnsEmptyValues());
        Class<?> collectionType = Collection.class;

        // when
        Collection<?> collectionResult = (Collection<?>) answer.answer(Mockito.mockingDetails(Mockito.mock(collectionType), "size"));

        // then
        assertEquals(0, collectionResult.size());
        assertNotNull(collectionResult);
    }

    @Test
    public void testEmptyMapAnswer() {
        // given
        Answer<Object> answer = Answers.delegateAnswers(new ReturnsEmptyValues());
        Class<?> mapType = Map.class;

        // when
        Map<?, ?> mapResult = (Map<?, ?>) answer.answer(Mockito.mockingDetails(Mockito.mock(mapType), "size"));

        // then
        assertEquals(0, mapResult.size());
        assertNotNull(mapResult);
    }

    @Test
    public void testEmptySetAnswer() {
        // given
        Answers<Object> answer = Answers.delegateAnswers(new ReturnsEmptyValues());
        Class<?> setType = Set.class;

        // when
        Set<?> setResult = (Set<?>) answer.answer(Mockito.mockingDetails(Mockito.mock(setType), "size"));

        // then
        assertEquals(0, setResult.size());
        assertNotNull(setResult);
    }

    @Test
    public void testEmptyListAnswer() {
        // given
        Answers<Object> answer = Answers.delegateAnswers(new ReturnsEmptyValues());
        Class<?> listType = List.class;

        // when
        List<?> listResult = (List<?>) answer.answer(Mockito.mockingDetails(Mockito.mock(listType), "size"));

        // then
        assertEquals(0, listResult.size());
        assertNotNull(listResult);
    }

    @Test
    public void testEmptyArrayListAnswer() {
        // given
        Answers<Object> answer = Answers.delegateAnswers(new ReturnsEmptyValues());
        Class<?> arrayListType = ArrayList.class;

        // when
        List<?> arrayListResult = (List<?>) answer.answer(Mockito.mockingDetails(Mockito.mock(arrayListType), "size"));

        // then
        assertEquals(0, arrayListResult.size());
        assertNotNull(arrayListResult);
    }

    @Test
    public void testEmptyLinkedHashSetAnswer() {
        // given
        Answers<Object> answer = Answers.delegateAnswers(new ReturnsEmptyValues());
        Class<?> linkedHashSetType = LinkedHashSet.class;

        // when
        Set<?> linkedHashSetResult = (Set<?>) answer.answer(Mockito.mockingDetails(Mockito.mock(linkedHashSetType), "size"));

        // then
        assertEquals(0, linkedHashSetResult.size());
        assertNotNull(linkedHashSetResult);
    }

    @Test
    public void testEmptyLinkedListAnswer() {
        // given
        Answers<Object> answer = Answers.delegateAnswers(new ReturnsEmptyValues());
        Class<?> linkedListType = LinkedList.class;

        // when
        List<?> linkedListResult = (List<?>) answer.answer(Mockito.mockingDetails(Mockito.mock(linkedListType), "size"));

        // then
        assertEquals(0, linkedListResult.size());
        assertNotNull(linkedListResult);
    }

    @Test
    public void testEmptyTreeMapAnswer() {
        // given
        Answers<Object> answer = Answers.delegateAnswers(new ReturnsEmptyValues());
        Class<?> treeMapType = TreeMap.class;

        // when
        Map<?, ?> treeMapResult = (Map<?, ?>) answer.answer(Mockito.mockingDetails(Mockito.mock(treeMapType), "size"));

        // then
        assertEquals(0, treeMapResult.size());
        assertNotNull(treeMapResult);
    }

    @Test
    public void testEmptyTreeSetAnswer() {
        // given
        Answers<Object> answer = Answers.delegateAnswers(new ReturnsEmptyValues());
        Class<?> treeSetType = TreeSet.class;

        // when
        Set<?> treeSetResult = (Set<?>) answer.answer(Mockito.mockingDetails(Mockito.mock(treeSetType), "size"));

        // then
        assertEquals(0, treeSetResult.size());
        assertNotNull(treeSetResult);
    }

    @Test
    public void testEmptyLinkedHashMapAnswer() {
        // given
        Answers<Object> answer = Answers.delegateAnswers(new ReturnsEmptyValues());
        Class<?> linkedHashMapType = LinkedHashMap.class;

        // when
        Map<?, ?> linkedHashMapResult = (Map<?, ?>) answer.answer(Mockito.mockingDetails(Mockito.mock(linkedHashMapType), "size"));

        // then
        assertEquals(0, linkedHashMapResult.size());
        assertNotNull(linkedHashMapResult);
    }

    @Test
    public void testEmptySortedMapAnswer() {
        // given
        Answers<Object> answer = Answers.delegateAnswers(new ReturnsEmptyValues());
        Class<?> sortedMapType = SortedMap.class;

        // when
        Map<?, ?> sortedMapResult = (Map<?, ?>) answer.answer(Mockito.mockingDetails(Mockito.mock(sortedMapType), "size"));

        // then
        assertEquals(0, sortedMapResult.size());
        assertNotNull(sortedMapResult);
    }

    @Test
    public void testEmptySortedSetAnswer() {
        // given
        Answers<Object> answer = Answers.delegateAnswers(new ReturnsEmptyValues());
        Class<?> sortedSetType = SortedSet.class;

        // when
        Set<?> sortedSetResult = (Set<?>) answer.answer(Mockito.mockingDetails(Mockito.mock(sortedSetType), "size"));

        // then
        assertEquals(0, sortedSetResult.size());
        assertNotNull(sortedSetResult);
    }
}