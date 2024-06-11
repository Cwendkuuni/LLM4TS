package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        Object mock = Mockito.mock(String.class);
        Answer<String> returnsEmptyValues = new ReturnsEmptyValues();
        Mockito.doAnswer(returnsEmptyValues).when(mock).toString();

        String actual = mock.toString();
        String expected = "Mock for " + String.class.getSimpleName() + ", hashCode: " + mock.hashCode();

        assertEquals(expected, actual);
        verify(mock).toString();
    }

    @Test
    public void testCompareToMethod() {
        Object mock1 = Mockito.mock(Comparable.class);
        Object mock2 = Mockito.mock(Comparable.class);
        Answer<Integer> returnsZeroIfEquals = new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) {
                return 0;
            }
        };
        Answer<Integer> returnsOneIfNotEqual = new ReturnsEmptyValues();
        Mockito.doAnswer(returnsZeroIfEquals).doAnswer(returnsOneIfNotEqual).when(mock1).compareTo(mock2);

        int comparisonResult = mock1.compareTo(mock2);

        // Test equal mocks
        Mockito.reset(mock1);
        Mockito.reset(mock2);
        Mockito.verifyNoMoreInteractions(mock1, mock2);
        mock1 = Mockito.mock(Comparable.class);
        mock2 = mock1;
        comparisonResult = mock1.compareTo(mock2);
        assertEquals(0, comparisonResult);
        verifyNoMoreInteractions(mock1, mock2);

        // Test not equal mocks
        mock1 = Mockito.mock(Comparable.class);
        mock2 = Mockito.mock(Comparable.class);
        comparisonResult = mock1.compareTo(mock2);
        assertNotEquals(0, comparisonResult);
        verify(mock1).compareTo(mock2);
        verifyNoMoreInteractions(mock1, mock2);
    }

    @Test
    public void testPrimitiveTypeMethods() {
        Object mock = Mockito.mock(Byte.TYPE);
        Answer<Byte> returnsEmptyValues = new ReturnsEmptyValues();
        Mockito.doAnswer(returnsEmptyValues).when(mock).byteValue();

        Byte actualByteValue = mock.byteValue();
        Byte expectedByteValue = Byte.MIN_VALUE;

        assertEquals(expectedByteValue, actualByteValue);
        verify(mock).byteValue();

        mock = Mockito.mock(Integer.TYPE);
        Answer<Integer> returnsEmptyValuesInteger = new ReturnsEmptyValues();
        Mockito.doAnswer(returnsEmptyValuesInteger).when(mock).intValue();

        Integer actualIntValue = mock.intValue();
        Integer expectedIntValue = Integer.MIN_VALUE;

        assertEquals(expectedIntValue, actualIntValue);
        verify(mock).intValue();

        mock = Mockito.mock(Long.TYPE);
        Answer<Long> returnsEmptyValuesLong = new ReturnsEmptyValues();
        Mockito.doAnswer(returnsEmptyValuesLong).when(mock).longValue();

        Long actualLongValue = mock.longValue();
        Long expectedLongValue = Long.MIN_VALUE;

        assertEquals(expectedLongValue, actualLongValue);
        verify(mock).longValue();
    }

    @Test
    public void testEmptyCollections() {
        Object mock = Mockito.mock(Collection.class);
        Answer<Collection<?>> returnsEmptyValues = new ReturnsEmptyValues();
        Mockito.doAnswer(returnsEmptyValues).when(mock).isEmpty();
        Mockito.doAnswer(returnsEmptyValues).when(mock).iterator();
        Mockito.doAnswer(returnsEmptyValues).when(mock).size();

        // Test empty List
        List<String> list = mock.isEmpty()
                ? null
                : (List<String>) mock;
        boolean isEmpty = list.isEmpty();
        assertTrue(isEmpty);
        assertEquals(0, list.size());
        verify(mock).isEmpty();
        verifyNoMoreInteractions(mock);

        // Test empty Set
        Set<String> set = mock.isEmpty()
                ? null
                : (Set<String>) mock;
        boolean isEmptySet = set.isEmpty();
        assertTrue(isEmptySet);
        verify(mock).isEmpty();
        verifyNoMoreInteractions(mock);

        // Test empty HashSet
        Set<String> hashSet = mock.isEmpty()
                ? null
                : (Set<String>) mock;
        boolean isEmptyHashSet = hashSet.isEmpty();
        assertTrue(isEmptyHashSet);
        verify(mock).isEmpty();
        verifyNoMoreInteractions(mock);

        // Test empty LinkedHashSet
        Set<String> linkedHashSet = mock.isEmpty()
                ? null
                : (Set<String>) mock;
        boolean isEmptyLinkedHashSet = linkedHashSet.isEmpty();
        assertTrue(isEmptyLinkedHashSet);
        verify(mock).isEmpty();
        verifyNoMoreInteractions(mock);

        // Test empty TreeSet
        SortedSet<String> treeSet = mock.isEmpty()
                ? null
                : (SortedSet<String>) mock;
        boolean isEmptyTreeSet = treeSet.isEmpty();
        assertTrue(isEmptyTreeSet);
        verify(mock).isEmpty();
        verifyNoMoreInteractions(mock);

        // Test empty TreeMap
        Map<String, String> treeMap = mock.isEmpty()
                ? null
                : (Map<String, String>) mock;
        boolean isEmptyTreeMap = treeMap.isEmpty();
        assertTrue(isEmptyTreeMap);
        verify(mock).isEmpty();
        verifyNoMoreInteractions(mock);

        // Test empty HashMap
        Map<String, String> hashMap = mock.isEmpty()
                ? null
                : (Map<String, String>) mock;
        boolean isEmptyHashMap = hashMap.isEmpty();
        assertTrue(isEmptyHashMap);
        verify(mock).isEmpty();
        verifyNoMoreInteractions(mock);

        // Test empty LinkedHashMap
        Map<String, String> linkedHashMap = mock.isEmpty()
                ? null
                : (Map<String, String>) mock;
        boolean isEmptyLinkedHashMap = linkedHashMap.isEmpty();
        assertTrue(isEmptyLinkedHashMap);
        verify(mock).isEmpty();
        verifyNoMoreInteractions(mock);
    }
}