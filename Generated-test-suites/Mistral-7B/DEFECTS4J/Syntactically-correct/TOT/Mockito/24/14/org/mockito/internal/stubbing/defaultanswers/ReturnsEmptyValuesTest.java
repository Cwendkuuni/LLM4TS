package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues;

    @Before
    public void setup() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }

    @Test
    public void testToStringAnswer() {
        Object mock = mock(String.class);
        Answer<String> answer = Mockito.answer(returnsEmptyValues);
        Mockito.doAnswer(answer).when(mock).toString();

        assertEquals("Mock for java.lang.String, hashCode: 1511220935", mock.toString());

        verify(mock, Mockito.times(1)).toString();
    }

    @Test
    public void testCompareToAnswer() {
        Object mock1 = mock(Comparable.class);
        Object mock2 = mock(Comparable.class);

        Answer<Integer> answer = Mockito.answer(returnsEmptyValues);
        Mockito.doAnswer(answer).when(mock1).compareTo(mock2);

        assertEquals(1, mock1.compareTo(mock2));

        verify(mock1, Mockito.times(1)).compareTo(mock2);
    }

    @Test
    public void testEmptyArrayListAnswer() {
        Object mock = mock(List.class);
        Answer<List<Integer>> answer = Mockito.answer(returnsEmptyValues);
        Mockito.doAnswer(answer).when(mock).toString();

        Mockito.when(mock.size()).thenReturn(0);

        List<Integer> list = (List<Integer>) mock.toString();
        assertNotNull(list);
        assertEquals(0, list.size());

        verify(mock, Mockito.times(1)).toString();
    }

    @Test
    public void testEmptyHashSetAnswer() {
        Object mock = mock(Set.class);
        Answer<Set<String>> answer = Mockito.answer(returnsEmptyValues);
        Mockito.doAnswer(answer).when(mock).toString();

        Mockito.when(mock.size()).thenReturn(0);

        Set<String> set = (Set<String>) mock.toString();
        assertNotNull(set);
        assertEquals(0, set.size());

        verify(mock, Mockito.times(1)).toString();
    }

    @Test
    public void testEmptyTreeSetAnswer() {
        Object mock = mock(SortedSet.class);
        Answer<SortedSet<String>> answer = Mockito.answer(returnsEmptyValues);
        Mockito.doAnswer(answer).when(mock).toString();

        Mockito.when(mock.size()).thenReturn(0);

        SortedSet<String> sortedSet = (SortedSet<String>) mock.toString();
        assertNotNull(sortedSet);
        assertEquals(0, sortedSet.size());

        verify(mock, Mockito.times(1)).toString();
    }

    @Test
    public void testEmptyArrayListWithCustomTypeAnswer() {
        Object mock = mock(User.class);
        Answer<List<User>> answer = Mockito.answer(returnsEmptyValues);
        Mockito.doAnswer(answer).when(mock).getUsers();

        List<User> list = (List<User>) mock.getUsers();
        assertNotNull(list);
        assertEquals(0, list.size());

        verify(mock, Mockito.times(1)).getUsers();
    }

    static class User {
    }
}