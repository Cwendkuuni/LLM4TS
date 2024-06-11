package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
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
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ReturnsEmptyValuesTest {

    private Object mock;

    @Before
    public void setUp() {
        mock = Mockito.spy(new Object());
    }

    @Test
    public void testToString() {
        Answer<String> answer = new ReturnsEmptyValues();
        when(mock.toString()).thenAnswer(answer);

        String expected = "Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode();
        String actual = mock.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testCompareTo() {
        Answer<Object> answer = new ReturnsEmptyValues();
        Object mockA = Mockito.spy(new Object());
        Object mockB = Mockito.spy(new Object());

        when(mockA.compareTo(mockB)).thenAnswer(answer);

        int actual = mockA.compareTo(mockB);

        assertEquals(1, actual);
    }

    @Test
    public void testPrimitiveTypeMethods() {
        Answer<Object> answer = new ReturnsEmptyValues();

        // int returning method
        when(mock.hashCode()).thenAnswer(answer);
        assertEquals(0, mock.hashCode());

        // Integer returning method
        when(mock instanceof Number).thenReturn(true);
        when(mock.intValue()).thenAnswer(answer);
        assertEquals(0, mock.intValue());

        // boolean returning method
        when(mock.booleanValue()).thenAnswer(answer);
        assertNull(mock.booleanValue());

        // char returning method
        when(mock.charValue()).thenAnswer(answer);
        assertNull(mock.charValue());

        // byte returning method
        when(mock instanceof Byte).thenReturn(true);
        when(mock.byteValue()).thenAnswer(answer);
        assertEquals((byte) 0, mock.byteValue());

        // short returning method
        when(mock instanceof Short).thenReturn(true);
        when(mock.shortValue()).thenAnswer(answer);
        assertEquals((short) 0, mock.shortValue());

        // long returning method
        when(mock instanceof Long).thenReturn(true);
        when(mock.longValue()).thenAnswer(answer);
        assertEquals(0L, mock.longValue());

        // float returning method
        when(mock instanceof Float).thenReturn(true);
        when(mock.floatValue()).thenAnswer(answer);
        assertEquals(0.0f, mock.floatValue(), 0.001);

        // double returning method
        when(mock instanceof Double).thenReturn(true);
        when(mock.doubleValue()).thenAnswer(answer);
        assertEquals(0.0d, mock.doubleValue(), 0.001);
    }

    @Test
    public void testEmptyCollectionMethods() {
        Answer<Object> answer = new ReturnsEmptyValues();

        // Collection returning method
        when(mock.toString()).thenAnswer(answer);
        assertNull(mock.toString());

        // List returning method
        when(mock.getClass()).thenReturn(List.class);
        when(mock.toString()).thenAnswer(answer);
        assertNull(mock.toString());

        // ArrayList returning method
        when(mock.getClass()).thenReturn(ArrayList.class);
        when(mock.toString()).thenAnswer(answer);
        assertNull(mock.toString());

        // LinkedList returning method
        when(mock.getClass()).thenReturn(LinkedList.class);
        when(mock.toString()).thenAnswer(answer);
        assertNull(mock.toString());

        // Set returning method
        when(mock.getClass()).thenReturn(HashSet.class);
        when(mock.toString()).thenAnswer(answer);
        assertNull(mock.toString());

        // LinkedHashSet returning method
        when(mock.getClass()).thenReturn(LinkedHashSet.class);
        when(mock.toString()).thenAnswer(answer);
        assertNull(mock.toString());

        // SortedSet returning method
        when(mock.getClass()).thenReturn(TreeSet.class);
        when(mock.toString()).thenAnswer(answer);
        assertNull(mock.toString());

        // TreeSet returning method
        when(mock.getClass()).thenReturn(TreeSet.class);
        when(mock.toString()).thenAnswer(answer);
        assertNull(mock.toString());
    }

    @Test
    public void testNullReturn() {
        Answer<Object> answer = new ReturnsEmptyValues();

        Class<?> type = null;
        when(mock.getClass()).thenReturn(type);

        when(mock.toString()).thenAnswer(answer);
        assertNull(mock.toString());
    }
}