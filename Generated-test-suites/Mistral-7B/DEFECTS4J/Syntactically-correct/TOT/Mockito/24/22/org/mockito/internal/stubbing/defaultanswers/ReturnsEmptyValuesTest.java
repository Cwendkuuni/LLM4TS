package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues;
    private MockName mockName;
    private Object mock;

    @Before
    public void setup() {
        returnsEmptyValues = new ReturnsEmptyValues();
        mock = Mockito.mock(String.class);
        mockName = mockUtil.getMockName(mock);
    }

    @Test
    public void testToStringMethod() {
        // Given
        String expectedToString = "Mock for String, hashCode: " + mock.hashCode();

        // When
        String result = (String) returnsEmptyValues.answer(ArgumentMatchers.any(InvocationOnMock.class));

        // Then
        assertEquals(expectedToString, result);
    }

    @Test
    public void testCompareToMethod() {
        // Given
        Object other = Mockito.mock(String.class);

        // When
        int result = (Integer) returnsEmptyValues.answer(ArgumentMatchers.method(String.class, "compareTo", ArgumentMatchers.any(String.class)));

        // Then
        assertNotEquals(0, result);
    }

    @Test
    public void testPrimitiveReturningMethod() {
        // Given
        int expectedInt = 42;

        // When
        int result = (Integer) returnsEmptyValues.answer(ArgumentMatchers.method(Integer.class, "valueOf", ArgumentMatchers.anyInt()));

        // Then
        assertEquals(expectedInt, result);
    }

    @Test
    public void testCollectionReturningMethod() {
        // Given
        Collection<String> expectedCollection = new ArrayList<>();

        // When
        Collection<String> result = (Collection<String>) returnsEmptyValues.answer(ArgumentMatchers.method(Collection.class, "newInstance"));

        // Then
        assertSame(expectedCollection, result);
    }

    @Test
    public void testEmptyListMethod() {
        // Given
        List<String> expectedList = new ArrayList<>();

        // When
        List<String> result = (List<String>) returnsEmptyValues.answer(ArgumentMatchers.method(List.class, "of"));

        // Then
        assertSame(expectedList, result);
    }

    @Test
    public void testEmptySetMethod() {
        // Given
        Set<String> expectedSet = new HashSet<>();

        // When
        Set<String> result = (Set<String>) returnsEmptyValues.answer(ArgumentMatchers.method(Set.class, "of"));

        // Then
        assertSame(expectedSet, result);
    }

    @Test
    public void testEmptyMapMethod() {
        // Given
        Map<String, String> expectedMap = new HashMap<>();

        // When
        Map<String, String> result = (Map<String, String>) returnsEmptyValues.answer(ArgumentMatchers.method(Map.class, "of"));

        // Then
        assertSame(expectedMap, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifyCollectionReturnedByMock() {
        // Given
        List<String> list = new ArrayList<>();
        when(mock.toString()).thenAnswer(returnsEmptyValues);
        when(mock.add(ArgumentMatchers.anyString())).thenReturn(true);

        // When
        List<String> result = (List<String>) returnsEmptyValues.answer(ArgumentMatchers.method(List.class, "addAll", ArgumentMatchers.anyCollection()));
        result.add("new element");

        // Then
        // This should throw an UnsupportedOperationException as the mocked collection is expected to return an unmodifiable linked list
    }
}