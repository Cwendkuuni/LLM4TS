package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Test class for the ReturnsEmptyValues class.
 */
public class ReturnsEmptyValuesTest {

    /**
     * Test method for the toString() answer.
     */
    @Test
    public void testToStringAnswer() {
        Mockito<MyMockType> mock = Mockito.mock(MyMockType.class);
        Answer<Object> answer = Mockito.answer(new ReturnsEmptyValues());

        Mockito.when(mock.toString()).thenAnswer(answer);

        Mockito.verify(mock).toString();

        String actual = (String) answer.answers[0];
        String expected = Mockito.mockSettings(mock).getTypeToMock().getSimpleName() + ", hashCode: " + mock.hashCode();
        assertEquals(expected, actual);
    }

    /**
     * Test method for the compareTo() answer.
     */
    @Test
    public void testCompareToAnswer() {
        Object mock = Mockito.mock(Comparable.class);
        Object anotherMock = Mockito.mock(Comparable.class);

        Answer<Object> answer = Mockito.answer(new ReturnsEmptyValues());

        Mockito.when(mock.compareTo(ArgumentMatchers.eq(anotherMock))).thenAnswer(answer);

        int actual = (int) answer.answers[0];
        assertTrue(actual != 0);

        Mockito.verify(mock).compareTo(anotherMock);
        Mockito.verifyNoMoreInteractions(mock);

        // Verify that the mocks are distinct
        assertNotSame(mock, anotherMock);
    }

    /**
     * Test method for the empty collection answer.
     */
    @Test
    public void testEmptyCollectionAnswer() {
        Mockito<MyCollectionType> mock = Mockito.mock(MyCollectionType.class);

        Answer<Object> answer = Mockito.answer(new ReturnsEmptyValues());

        Mockito.when(mock.isEmpty()).thenReturn(true);
        Mockito.when(mock.size()).thenReturn(0);
        Mockito.when(mock.getClass()).thenReturn(List.class); // or any other collection type

        Mockito.when(mock.iterator()).thenReturn(mock.iterator());

        List<?> actualList = mock.isEmpty() ? mock : mock.iterator().next();

        assertTrue(actualList.isEmpty());
    }
}

interface Comparable<T> {
    int compareTo(T another);
}

interface MyMockType {
    String toString();
}

interface MyCollectionType extends Collection<Object> {
    boolean isEmpty();
    int size();
}