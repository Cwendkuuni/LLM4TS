package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link ReturnsEmptyValues} class.
 */
@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    /**
     * Test method that checks if {@link ReturnsEmptyValues#answer(InvocationOnMock)} returns empty list when called on a mocked object and the method returns Collection type.
     */
    @Test
    public void testReturnsEmptyList() {
        Collection<String> mockedCollection = mock(Collection.class);
        when(mockedCollection.isEmpty()).thenReturn(true);
        Answer<Collection<String>> answer = new ReturnsEmptyValues();

        Collection<String> result = answer.answer(new InvocationOnMock(this, mockedCollection.getClass(), "isEmpty", new Object[0]));

        assertEquals(mockedCollection, result);
        verify(mockedCollection).isEmpty();
        assertEquals(0, result.size());
    }

    /**
     * Test method that checks if {@link ReturnsEmptyValues#answer(InvocationOnMock)} returns empty hash set when called on a mocked object and the method returns Set type.
     */
    @Test
    public void testReturnsEmptyHashSet() {
        Set<String> mockedSet = mock(Set.class);
        when(mockedSet.isEmpty()).thenReturn(true);
        Answer<Set<String>> answer = new ReturnsEmptyValues();

        Set<String> result = answer.answer(new InvocationOnMock(this, mockedSet.getClass(), "isEmpty", new Object[0]));

        assertEquals(mockedSet, result);
        verify(mockedSet).isEmpty();
        assertEquals(0, result.size());
    }

    /**
     * Test method that checks if {@link ReturnsEmptyValues#answer(InvocationOnMock)} returns empty ArrayList when called on a mocked object and the method returns List type.
     */
    @Test
    public void testReturnsEmptyArrayList() {
        List<String> mockedArrayList = mock(ArrayList.class);
        when(mockedArrayList.isEmpty()).thenReturn(true);
        Answer<List<String>> answer = new ReturnsEmptyValues();

        List<String> result = answer.answer(new InvocationOnMock(this, mockedArrayList.getClass(), "isEmpty", new Object[0]));

        assertEquals(mockedArrayList, result);
        verify(mockedArrayList).isEmpty();
        assertEquals(0, result.size());
    }

    /**
     * Test method that checks if {@link ReturnsEmptyValues#answer(InvocationOnMock)} returns empty HashMap when called on a mocked object and the method returns Map type.
     */
    @Test
    public void testReturnsEmptyHashMap() {
        Map<String, String> mockedMap = mock(Map.class);
        when(mockedMap.isEmpty()).thenReturn(true);
        Answer<Map<String, String>> answer = new ReturnsEmptyValues();

        Map<String, String> result = answer.answer(new InvocationOnMock(this, mockedMap.getClass(), "isEmpty", new Object[0]));

        assertEquals(mockedMap, result);
        verify(mockedMap).isEmpty();
        assertEquals(0, result.size());
    }

    /**
     * Test method that checks if {@link ReturnsEmptyValues#answer(InvocationOnMock)} returns empty TreeSet when called on a mocked object and the method returns SortedSet type.
     */
    @Test
    public void testReturnsEmptyTreeSet() {
        SortedSet<String> mockedTreeSet = mock(TreeSet.class);
        when(mockedTreeSet.isEmpty()).thenReturn(true);
        Answer<SortedSet<String>> answer = new ReturnsEmptyValues();

        SortedSet<String> result = answer.answer(new InvocationOnMock(this, mockedTreeSet.getClass(), "isEmpty", new Object[0]));

        assertEquals(mockedTreeSet, result);
        verify(mockedTreeSet).isEmpty();
        assertEquals(0, result.size());
    }

    /**
     * Test method that checks if {@link ReturnsEmptyValues#answer(InvocationOnMock)} returns null when called on a mocked object and the method returns null type.
     */
    @Test
    public void testReturnsNull() {
        Object mockedObject = mock(Object.class);
        Answer<Object> answer = new ReturnsEmptyValues();

        Object result = answer.answer(new InvocationOnMock(this, mockedObject.getClass(), null, new Object[0]));

        assertNotNull(result);
        assertEquals(null, result);
    }

    /**
     * Test method that checks if {@link ReturnsEmptyValues#toString()} method returns proper description of a mocked object.
     */
    @Test
    public void testToString() {
        Object mockedObject = mock(Object.class);
        Answer<Object> answer = new ReturnsEmptyValues();

        String result = answer.toString();

        String expected = String.format("Mock for %s, hashCode: %s", mockedObject.getClass().getSimpleName(), mockedObject.hashCode());
        assertEquals(expected, result);
    }
}