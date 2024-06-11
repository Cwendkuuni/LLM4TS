package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToString() {
        Object mock = mock(String.class);
        Answer<String> answer = new ReturnsEmptyValues();

        when(mock.toString()).thenAnswer(answer);

        String result = mock.toString();
        assertEquals("Mock for String, hashCode: " + mock.hashCode(), result);
        verify(mock).toString();
    }

    @Test
    public void testCompareTo() {
        Object mock1 = mock(Comparable.class);
        Object mock2 = mock(Comparable.class);

        Answer<Integer> answer = new ReturnsEmptyValues();

        when(mock1.compareTo(ArgumentMatchers.any())).thenAnswer(answer);

        Integer result = mock1.compareTo(mock2);
        assertEquals(1, result);
        verify(mock1).compareTo(mock2);
    }

    @Test
    public void testPrimitiveTypes() {
        Object mock = mock(Byte.TYPE);
        Answer<Byte> answer = new ReturnsEmptyValues();

        when(mock.byteValue()).thenAnswer(answer);

        Byte result = mock.byteValue();
        assertEquals((byte) 0, result);
        verify(mock).byteValue();

        mock = mock(Short.TYPE);
        answer = new ReturnsEmptyValues();

        when(mock.shortValue()).thenAnswer(answer);

        Short result1 = mock.shortValue();
        assertEquals((short) 0, result1);
        verify(mock).shortValue();

        mock = mock(Integer.TYPE);
        answer = new ReturnsEmptyValues();

        when(mock.intValue()).thenAnswer(answer);

        Integer result2 = mock.intValue();
        assertEquals(0, result2);
        verify(mock).intValue();

        mock = mock(Long.TYPE);
        answer = new ReturnsEmptyValues();

        when(mock.longValue()).thenAnswer(answer);

        Long result3 = mock.longValue();
        assertEquals(0L, result3);
        verify(mock).longValue();

        mock = mock(Float.TYPE);
        answer = new ReturnsEmptyValues();

        when(mock.floatValue()).thenAnswer(answer);

        Float result4 = mock.floatValue();
        assertEquals(0.0f, result4);
        verify(mock).floatValue();

        mock = mock(Double.TYPE);
        answer = new ReturnsEmptyValues();

        when(mock.doubleValue()).thenAnswer(answer);

        Double result5 = mock.doubleValue();
        assertEquals(0.0d, result5);
        verify(mock).doubleValue();

        mock = mock(Boolean.TYPE);
        answer = new ReturnsEmptyValues();

        when(mock.booleanValue()).thenAnswer(answer);

        Boolean result6 = mock.booleanValue();
        assertEquals(false, result6);
        verify(mock).booleanValue();

        mock = mock(Character.TYPE);
        answer = new ReturnsEmptyValues();

        when(mock.charValue()).thenAnswer(answer);

        Character result7 = mock.charValue();
        assertEquals((char) 0, result7);
        verify(mock).charValue();
    }

    @Test
    public void testEmptyCollection() {
        Object mock = mock(Collection.class);
        Answer<Collection<?>> answer = new ReturnsEmptyValues();

        when(mock.size()).thenReturn(0);
        when(mock.isEmpty()).thenReturn(true);

        when(mock.add(ArgumentMatchers.any())).thenThrow(UnsupportedOperationException.class);

        when(mock).thenAnswer(answer);

        Collection<String> result = mock;
        assertNotNull(result);
        assertEquals(0, result.size());
        assertEquals(true, result.isEmpty());
        try {
            result.add("test");
        } catch (UnsupportedOperationException e) {
            // expected
        }
        verify(mock).size();
        verify(mock).isEmpty();
    }

    @Test
    public void testEmptySet() {
        Object mock = mock(Set.class);
        Answer<Set<?>> answer = new ReturnsEmptyValues();

        when(mock.size()).thenReturn(0);
        when(mock.isEmpty()).thenReturn(true);

        when(mock.add(ArgumentMatchers.any())).thenThrow(UnsupportedOperationException.class);

        when(mock).thenAnswer(answer);

        Set<String> result = mock;
        assertNotNull(result);
        assertEquals(0, result.size());
        assertEquals(true, result.isEmpty());
        try {
            result.add("test");
        } catch (UnsupportedOperationException e
                ) {
            // expected
        }
        verify(mock).size();
        verify(mock).isEmpty();
    }

    @Test
    public void testEmptyHashSet() {
        Object mock = mock(HashSet.class);
        Answer<Set<?>> answer = new ReturnsEmptyValues();

        when(mock.size()).thenReturn(0);
        when(mock.isEmpty()).thenReturn(true);

        when(mock.add(ArgumentMatchers.any())).thenThrow(UnsupportedOperationException.class);

        when(mock).thenAnswer(answer);

        Set<String> result = mock;
        assertNotNull(result);
        assertEquals(0, result.size());
        assertEquals(true, result.isEmpty());
        try {
            result.add("test");
        } catch (UnsupportedOperationException e) {
            // expected
        }
        verify(mock).size();
        verify(mock).isEmpty();
    }

    @Test
    public void testEmptyTreeSet() {
        Object mock = mock(TreeSet.class);
        Answer<SortedSet<?>> answer = new ReturnsEmptyValues();

        when(mock.size()).thenReturn(0);
        when(mock.isEmpty()).thenReturn(true);

        when(mock.add(ArgumentMatchers.any())).thenThrow(UnsupportedOperationException.class);

        when(mock).thenAnswer(answer);

        SortedSet<String> result = mock;
        assertNotNull(result);
        assertEquals(0, result.size());
        assertEquals(true, result.isEmpty());
        try {
            result.add("test");
        } catch (UnsupportedOperationException e) {
            // expected
        }
        verify(mock).size();
        verify(mock).isEmpty();
    }

    @Test
    public void testEmptyLinkedHashSet() {
        Object mock = mock(LinkedHashSet.class);
        Answer<Set<?>> answer = new ReturnsEmptyValues();

        when(mock.size()).thenReturn(0);
        when(mock.isEmpty()).thenReturn(true);

        when(mock.add(ArgumentMatchers.any())).thenThrow(UnsupportedOperationException.class);

        when(mock).thenAnswer(answer);

        Set<String> result = mock;
        assertNotNull(result);
        assertEquals(0, result.size());
        assertEquals(true, result.isEmpty());
        try {
            result.add("test");
        } catch (UnsupportedOperationException e) {
            // expected
        }
        verify(mock).size();
        verify(mock).isEmpty();
    }

    @Test
    public void testEmptyList() {
        Object mock = mock(List.class);
        Answer<List<?>> answer = new ReturnsEmptyValues();

        when(mock.size()).thenReturn(0);
        when(mock.isEmpty()).thenReturn(true);

        when(mock.add(ArgumentMatchers.any())).thenThrow(UnsupportedOperationException.class);

        when(mock).thenAnswer(answer);

        List<String> result = mock;
        assertNotNull(result);
        assertEquals(0, result.size());
        assertEquals(true, result.isEmpty());
        try {
            result.add("test");
        } catch (UnsupportedOperationException e) {
            // expected
        }
        verify(mock).size();
        verify(mock).isEmpty();
    }

    @Test
    public void testEmptyLinkedList() {
        Object mock = mock(LinkedList.class);
        Answer<List<?>> answer = new ReturnsEmptyValues();

        when(mock.size()).thenReturn(0);
        when(mock.isEmpty()).thenReturn(true);

        when(mock.add(ArgumentMatchers.any())).thenThrow(UnsupportedOperationException.class);

        when(mock).thenAnswer(answer);

        List<String> result = mock;
        assertNotNull(result);
        assertEquals(0, result.size());
        assertEquals(true, result.isEmpty());
        try {
            result.add("test");
        } catch (UnsupportedOperationException e) {
            // expected
        }
        verify(mock).size();
        verify(mock).isEmpty();
    }

    @Test
    public void testEmptyArrayList() {
        Object mock = mock(ArrayList.class);
        Answer<List<?>> answer = new ReturnsEmptyValues();

        when(mock.size()).thenReturn(0);
        when(mock.isEmpty()).thenReturn(true);

        when(mock.add(ArgumentMatchers.any())).thenThrow(UnsupportedOperationException.class);

        when(mock).thenAnswer(answer);

        List<String> result = mock;
        assertNotNull(result);
        assertEquals(0, result.size());
        assertEquals(true, result.isEmpty());
        try {
            result.add("test");
        } catch (UnsupportedOperationException e) {
            // expected
        }
        verify(mock).size();
        verify(mock).isEmpty();
    }

    @Test
    public void testEmptyMap() {
        Object mock = mock(Map.class);
        Answer<Map<?, ?>> answer = new ReturnsEmptyValues();

        when(mock.isEmpty()).thenReturn(true);

        when(mock).thenAnswer(answer);

        Map<String, String> result = mock;
        assertNotNull(result);
        assertEquals(true, result.isEmpty());
        verify(mock).isEmpty();
    }

    @Test
    public void testEmptyHashMap() {
        Object mock = mock(HashMap.class);
        Answer<Map<?, ?>> answer = new ReturnsEmptyValues();

        when(mock.isEmpty()).thenReturn(true);

        when(mock).thenAnswer(answer);

        Map<String, String> result = mock;
        assertNotNull(result);
        assertEquals(true, result.isEmpty());
        verify(mock).isEmpty();
    }

    @Test
    public void testEmptyTreeMap() {
        Object mock = mock(TreeMap.class);
        Answer<SortedMap<?, ?>> answer = new ReturnsEmptyValues();

        when(mock.isEmpty()).thenReturn(true);

        when(mock).thenAnswer(answer);

        SortedMap<String, String> result = mock;
        assertNotNull(result);
        assertEquals(true, result.isEmpty());
        verify(mock).isEmpty();
    }

    @Test
    public void testEmptyLinkedHashMap() {
        Object mock = mock(LinkedHashMap.class);
        Answer<Map<?, ?>> answer = new ReturnsEmptyValues();

        when(mock.isEmpty()).thenReturn(true);

        when(mock).thenAnswer(answer);

        Map<String, String> result = mock;
        assertNotNull(result);
        assertEquals(true, result.isEmpty());
        verify(mock).isEmpty();
    }
}