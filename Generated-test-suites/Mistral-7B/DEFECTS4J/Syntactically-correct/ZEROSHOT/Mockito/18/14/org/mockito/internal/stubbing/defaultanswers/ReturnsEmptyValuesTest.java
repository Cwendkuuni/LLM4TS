package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        Object mock = mock(Object.class);
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        String result = (String) returnsEmptyValues.answer(new InvocationOnMock(mock, ArgumentMatchers.anyMethod()));

        assertNotNull(result);
    }

    @Test
    public void testCompareToMethod() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        int result = (Integer) returnsEmptyValues.answer(new InvocationOnMock(mock1, ArgumentMatchers.method(Object.class, "compareTo", ArgumentMatchers.any(Object.class))));

        assertEquals(0, result);
        result = (Integer) returnsEmptyValues.answer(new InvocationOnMock(mock1, ArgumentMatchers.method(Object.class, "compareTo", ArgumentMatchers.any(Object.class)), mock2));

        // Since we return 1 for any other comparison, we just check that it's not 0.
        assert (! (0 == result));
    }

    @Test
    public void testReturnValueForPrimitiveTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        byte result = (Byte) returnsEmptyValues.returnValueFor(byte.class);
        assertEquals((byte) 0, result);

        short resultShort = (Short) returnsEmptyValues.returnValueFor(short.class);
        assertEquals((short) 0, resultShort);

        int resultInt = (Integer) returnsEmptyValues.returnValueFor(int.class);
        assertEquals(0, resultInt);

        long resultLong = (Long) returnsEmptyValues.returnValueFor(long.class);
        assertEquals(0L, resultLong);

        float resultFloat = (Float) returnsEmptyValues.returnValueFor(float.class);
        assertEquals((float) 0.0f, resultFloat);

        double resultDouble = (Double) returnsEmptyValues.returnValueFor(double.class);
        assertEquals(0.0, resultDouble, 0.0);

        char resultChar = (Character) returnsEmptyValues.returnValueFor(char.class);
        assertEquals((char) 0, resultChar);

        boolean resultBoolean = (Boolean) returnsEmptyValues.returnValueFor(boolean.class);
        assertEquals(false, resultBoolean);
    }

    @Test
    public void testReturnValueForPrimitiveWrappers() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Byte resultByte = (Byte) returnsEmptyValues.returnValueFor(Byte.class);
        assertEquals((byte) 0, resultByte.byteValue());

        Short resultShort = (Short) returnsEmptyValues.returnValueFor(Short.class);
        assertEquals((short) 0, resultShort.shortValue());

        Integer resultInt = (Integer) returnsEmptyValues.returnValueFor(Integer.class);
        assertEquals(0, resultInt.intValue());

        Long resultLong = (Long) returnsEmptyValues.returnValueFor(Long.class);
        assertEquals(0L, resultLong.longValue());

        Float resultFloat = (Float) returnsEmptyValues.returnValueFor(Float.class);
        assertEquals((float) 0.0f, resultFloat.floatValue(), 0.0f);

        Double resultDouble = (Double) returnsEmptyValues.returnValueFor(Double.class);
        assertEquals(0.0, resultDouble.doubleValue(), 0.0);

        Character resultChar = (Character) returnsEmptyValues.returnValueFor(Character.class);
        assertEquals((char) 0, resultChar.charValue());

        Boolean resultBoolean = (Boolean) returnsEmptyValues.returnValueFor(Boolean.class);
        assertEquals(false, resultBoolean.booleanValue());
    }

    @Test
    public void testReturnValueForCollectionTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Collection<String> resultCollection = (Collection<String>) returnsEmptyValues.returnValueFor(Collection.class);
        assertTrue(resultCollection instanceof LinkedList);
        assertEquals(0, resultCollection.size());

        Set<String> resultSet = (Set<String>) returnsEmptyValues.returnValueFor(Set.class);
        assertTrue(resultSet instanceof HashSet);
        assertEquals(0, resultSet.size());

        SortedSet<String> resultSortedSet = (SortedSet<String>) returnsEmptyValues.returnValueFor(SortedSet.class);
        assertTrue(resultSortedSet instanceof TreeSet);
        assertEquals(0, resultSortedSet.size());

        List<String> resultList = (List<String>) returnsEmptyValues.returnValueFor(List.class);
        assertTrue(resultList instanceof LinkedList);
        assertEquals(0, resultList.size());

        Map<String, String> resultMap = (Map<String, String>) returnsEmptyValues.returnValueFor(Map.class);
        assertTrue(resultMap instanceof HashMap);
        assertEquals(0, resultMap.size());

        SortedMap<String, String> resultSortedMap = (SortedMap<String, String>) returnsEmptyValues.returnValueFor(SortedMap.class);
        assertTrue(resultSortedMap instanceof TreeMap);
        assertEquals(0, resultSortedMap.size());
    }
}