package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collections;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues defaultAnswer;
    private Object mock;

    @Before
    public void setup() {
        defaultAnswer = new ReturnsEmptyValues();
        mock = mock(Object.class);
    }

    @Test
    public void testToStringMethod() {
        // Given
        given(defaultAnswer).answer(ArgumentMatchers.any(InvocationOnMock.class));

        // When
        String result = (String) defaultAnswer.answer(Mockito.mock(MethodCaller.class).toInvocationOnMock());

        // Then
        assertEquals("Mock for Object, hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testCompareToMethod() {
        // Given
        given(defaultAnswer).answer(Mockito.mock(Comparable.class).toInvocationOnMock());

        // When
        int result = (int) defaultAnswer.answer(Mockito.mock(Comparable.class).toInvocationOnMock());

        // Then
        assertEquals(1, result);
    }

    @Test
    public void testPrimitiveMethods() {
        // Given
        given(defaultAnswer).answer(Mockito.mock(Byte.class).toInvocationOnMock());

        // When
        Byte result = (Byte) defaultAnswer.answer(Mockito.mock(Number.class).toInvocationOnMock());

        // Then
        assertEquals((byte) 0, result);

        given(defaultAnswer).answer(Mockito.mock(Short.class).toInvocationOnMock());

        Short resultShort = (Short) defaultAnswer.answer(Mockito.mock(Number.class).toInvocationOnMock());

        assertEquals((short) 0, resultShort);

        given(defaultAnswer).answer(Mockito.mock(Integer.class).toInvocationOnMock());

        Integer resultInt = (Integer) defaultAnswer.answer(Mockito.mock(Number.class).toInvocationOnMock());

        assertEquals(0, resultInt.intValue());

        given(defaultAnswer).answer(Mockito.mock(Long.class).toInvocationOnMock());

        Long resultLong = (Long) defaultAnswer.answer(Mockito.mock(Number.class).toInvocationOnMock());

        assertEquals(0L, resultLong);

        given(defaultAnswer).answer(Mockito.mock(Float.class).toInvocationOnMock());

        Float resultFloat = (Float) defaultAnswer.answer(Mockito.mock(Number.class).toInvocationOnMock());

        assertEquals(0.0f, resultFloat.floatValue(), 0.01f);

        given(defaultAnswer).answer(Mockito.mock(Double.class).toInvocationOnMock());

        Double resultDouble = (Double) defaultAnswer.answer(Mockito.mock(Number.class).toInvocationOnMock());

        assertEquals(0.0, resultDouble.doubleValue(), 0.01);

        given(defaultAnswer).answer(Mockito.mock(Boolean.class).toInvocationOnMock());

        Boolean resultBoolean = (Boolean) defaultAnswer.answer(Mockito.mock(Boolean.class).toInvocationOnMock());

        assertEquals(false, resultBoolean);

        given(defaultAnswer).answer(Mockito.mock(Character.class).toInvocationOnMock());

        Character resultChar = (Character) defaultAnswer.answer(Mockito.mock(Character.class).toInvocationOnMock());

        assertEquals((char) 0, resultChar);
    }

    @Test
    public void testEmptyCollectionTypes() {
        // Given
        given(defaultAnswer).answer(Mockito.mock(Collection.class).toInvocationOnMock());

        // When
        Collection<Object> result = (Collection<Object>) defaultAnswer.answer(Mockito.mock(Collection.class).toInvocationOnMock());

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());

        given(defaultAnswer).answer(Mockito.mock(List.class).toInvocationOnMock());

        List<Object> resultList = (List<Object>) defaultAnswer.answer(Mockito.mock(List.class).toInvocationOnMock());

        assertNotNull(resultList);
        assertEquals(0, resultList.size());

        given(defaultAnswer).answer(Mockito.mock(Set.class).toInvocationOnMock());

        Set<Object> resultSet = (Set<Object>) defaultAnswer.answer(Mockito.mock(Set.class).toInvocationOnMock());

        assertNotNull(resultSet);
        assertEquals(0, resultSet.size());

        given(defaultAnswer).answer(Mockito.mock(HashSet.class).toInvocationOnMock());

        Set<Object> resultHashSet = (Set<Object>) defaultAnswer.answer(Mockito.mock(HashSet.class).toInvocationOnMock());

        assertNotNull(resultHashSet);
        assertEquals(0, resultHashSet.size());

        given(defaultAnswer).answer(Mockito.mock(LinkedHashSet.class).toInvocationOnMock());

        Set<Object> resultLinkedHashSet = (Set<Object>) defaultAnswer.answer(Mockito.mock(LinkedHashSet.class).toInvocationOnMock());

        assertNotNull(resultLinkedHashSet);
        assertEquals(0, resultLinkedHashSet.size());

        given(defaultAnswer).answer(Mockito.mock(SortedSet.class).toInvocationOnMock());

        SortedSet<Object> resultSortedSet = (SortedSet<Object>) defaultAnswer.answer(Mockito.mock(SortedSet.class).toInvocationOnMock());

        assertNotNull(resultSortedSet);
        assertEquals(0, resultSortedSet.size());

        given(defaultAnswer).answer(Mockito.mock(TreeSet.class).toInvocationOnMock());

        SortedSet<Object> resultTreeSet = (SortedSet<Object>) defaultAnswer.answer(Mockito.mock(TreeSet.class).toInvocationOnMock());

        assertNotNull(resultTreeSet);
        assertEquals(0, resultTreeSet.size());
    }

    @Test
    public void testEmptyMapTypes() {
        // Given
        given(defaultAnswer).answer(Mockito.mock(Map.class).toInvocationOnMock());

        // When
        Map<Object, Object> result = (Map<Object, Object>) defaultAnswer.answer(Mockito.mock(Map.class).toInvocationOnMock());

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());

        given(defaultAnswer).answer(Mockito.mock(HashMap.class).toInvocationOnMock());

        Map<Object, Object> resultHashMap = (Map<Object, Object>) defaultAnswer.answer(Mockito.mock(HashMap.class).toInvocationOnMock());

        assertNotNull(resultHashMap);
        assertEquals(0, resultHashMap.size());

        given(defaultAnswer).answer(Mockito.mock(TreeMap.class).toInvocationOnMock());

        SortedMap<Object, Object> resultTreeMap = (SortedMap<Object, Object>) defaultAnswer.answer(Mockito.mock(TreeMap.class).toInvocationOnMock());

        assertNotNull(resultTreeMap);
        assertEquals(0, resultTreeMap.size());

        given(defaultAnswer).answer(Mockito.mock(LinkedHashMap.class).toInvocationOnMock());

        Map<Object, Object> resultLinkedHashMap = (Map<Object, Object>) defaultAnswer.answer(Mockito.mock(LinkedHashMap.class).toInvocationOnMock());

        assertNotNull(resultLinkedHashMap);
        assertEquals(0, resultLinkedHashMap.size());
    }

    @Test
    public void testNull() {
        // Given
        given(defaultAnswer).answer(Mockito.mock(Object.class).toInvocationOnMock());

        // When
        Object result = defaultAnswer.answer(Mockito.mock(Object.class).toInvocationOnMock());

        // Then
        assertNull(result);
    }
}