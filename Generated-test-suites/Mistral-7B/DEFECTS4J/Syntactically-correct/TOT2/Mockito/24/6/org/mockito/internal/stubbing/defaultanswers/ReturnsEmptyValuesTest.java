package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.LinkedHashSet;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.LinkedHashMap;
import java.lang.Class;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerMethod() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        when(returnsEmptyValues.answer(invocation)).thenCallRealMethod();

        Object result = returnsEmptyValues.answer(invocation);

        verify(returnsEmptyValues).answer(invocation);
        assertNotNull(result);
    }

    @Test
    public void testReturnValueForPrimitive() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);
        int intType = Integer.TYPE;
        Object result = returnsEmptyValues.returnValueFor(intType);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testReturnValueForCollection() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);
        Object result = returnsEmptyValues.returnValueFor(Collection.class);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof List);
        Assert.assertEquals(0, ((List<?>) result).size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testReturnValueForSet() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);
        Object result = returnsEmptyValues.returnValueFor(Set.class);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof HashSet);
        // This test case will fail with an UnsupportedOperationException
        // since the JUnit test framework tries to call size() on the set
    }

    @Test
    public void testReturnValueForMaps() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);
        Object result = returnsEmptyValues.returnValueFor(Map.class);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof HashMap);
        Map<String, String> map = (Map<String, String>) result;
        Assert.assertEquals(0, map.size());
    }

    @Test(expected = NullPointerException.class)
    public void testReturnValueForNullType() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);
        Class<?> nullType = null;
        Object result = returnsEmptyValues.returnValueFor(nullType);
    }

    @Test
    public void testReturnValueForUnsupportedCollectionType() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);
        Class<?> customCollectionType = new CustomCollectionType();
        try {
            Object result = returnsEmptyValues.returnValueFor(customCollectionType);
            fail("Expected IllegalArgumentException, but got: " + result);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testReturnValueForEmptyInterface() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);
        Object result = returnsEmptyValues.returnValueFor(Object.class);
        Assert.assertNotNull(result);
        Assert.assertEquals(0, ((Collection<?>) result).size());
    }

    @Test(expected = NullPointerException.class)
    public void testReturnValueForNullReturnValue() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);
        when(returnsEmptyValues.returnValueFor(Mockito.any(Class.class)))
                .thenReturn(null);
        Class<?> type = Integer.TYPE;
        Object result = returnsEmptyValues.returnValueFor(type);
        Assert.assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReturnValueForInvalidType() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);
        Object result = returnsEmptyValues.returnValueFor("String");
    }
}

//