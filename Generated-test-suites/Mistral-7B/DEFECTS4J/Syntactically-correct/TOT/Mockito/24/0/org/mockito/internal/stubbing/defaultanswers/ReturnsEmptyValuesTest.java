package org.mockito.internal.stubbing.defaultanswers.test;

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
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues;
    private Object mock;

    @Before
    public void setup() {
        returnsEmptyValues = new ReturnsEmptyValues();
        mock = mock(Object.class);
    }

    @Test
    public void testToStringMethod() {
        InvocationOnMock invocation = Mockito.methodCall(mock, "toString");
        String result = (String) returnsEmptyValues.answer(invocation);
        assertEquals("Mock for Object, hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testComparableMethod() {
        Object otherMock = mock(Object.class);
        InvocationOnMock invocation = Mockito.methodCall(mock, "compareTo", ArgumentMatchers.any());
        invocation.arg(otherMock);
        int result = (int) returnsEmptyValues.answer(invocation);
        verify(mock).compareTo(otherMock);
        assertEquals(1, result);
    }

    @Test
    public void testPrimitiveReturningMethod() {
        InvocationOnMock intInvocation = Mockito.methodCall(mock, "intValue");
        int result = (int) returnsEmptyValues.answer(intInvocation);
        assertEquals(0, result);

        InvocationOnMock longInvocation = Mockito.methodCall(mock, "longValue");
        long resultLong = (long) returnsEmptyValues.answer(longInvocation);
        assertEquals(0L, resultLong);

        InvocationOnMock booleanInvocation = Mockito.methodCall(mock, "booleanValue");
        boolean resultBoolean = (boolean) returnsEmptyValues.answer(booleanInvocation);
        assertEquals(false, resultBoolean);
    }

    @Test
    public void testEmptyCollectionMethods() {
        // test empty ArrayList
        InvocationOnMock listInvocation = Mockito.methodCall(mock, "isEmpty", ArgumentMatchers.isInstance(ArrayList.class));
        ArrayList<Integer> list = ArgumentMatchers.argument();
        ArrayList<Integer> emptyList = new ArrayList<>();
        returnsEmptyValues.answer(Mockito.any(InvocationOnMock.class));
        Mockito.when(mock.getClass()).thenReturn(ArrayList.class);
        Mockito.when(mock.isEmpty()).thenReturn(true);
        assertEquals(true, returnsEmptyValues.answer(listInvocation.arg(emptyList)));

        // test empty HashSet and LinkedHashSet
        InvocationOnMock setInvocation = Mockito.methodCall(mock, "isEmpty", ArgumentMatchers.isInstance(HashSet.class));
        HashSet<String> set = ArgumentMatchers.argument();
        HashSet<String> emptySet = new HashSet<>();
        returnsEmptyValues.answer(Mockito.any(InvocationOnMock.class));
        Mockito.when(mock.getClass()).thenReturn(HashSet.class);
        Mockito.when(mock.isEmpty()).thenReturn(true);
        assertEquals(true, returnsEmptyValues.answer(setInvocation.arg(emptySet)));

        InvocationOnMock linkedSetInvocation = Mockito.methodCall(mock, "isEmpty", ArgumentMatchers.isInstance(LinkedHashSet.class));
        LinkedHashSet<String> linkedSet = ArgumentMatchers.argument();
        LinkedHashSet<String> emptyLinkedSet = new LinkedHashSet<>();
        returnsEmptyValues.answer(Mockito.any(InvocationOnMock.class));
        Mockito.when(mock.getClass()).thenReturn(LinkedHashSet.class);
        Mockito.when(mock.isEmpty()).thenReturn(true);
        assertEquals(true, returnsEmptyValues.answer(linkedSetInvocation.arg(emptyLinkedSet)));

        // test empty LinkedList
        InvocationOnMock listInstanceInvocation = Mockito.methodCall(mock, "getClass");
        List<Integer> emptyListInstance = new LinkedList<>();
        Mockito.when(mock.getClass()).thenReturn(LinkedList.class);
        Mockito.when(mock.isEmpty()).thenReturn(true);
        assertEquals(LinkedList.class, returnsEmptyValues.answer(listInstanceInvocation));
        InvocationOnMock listInvocation = Mockito.methodCall(mock, "isEmpty");
        assertEquals(true, returnsEmptyValues.answer(listInvocation));

        // test empty TreeMap and TreeSet
        InvocationOnMock mapInvocation = Mockito.methodCall(mock, "isEmpty", ArgumentMatchers.isInstance(TreeMap.class));
        TreeMap<Integer, String> treeMap = ArgumentMatchers.argument();
        TreeMap<Integer, String> emptyTreeMap = new TreeMap<>();
        returnsEmptyValues.answer(Mockito.any(InvocationOnMock.class));
        Mockito.when(mock.getClass()).thenReturn(TreeMap.class);
        Mockito.when(mock.isEmpty()).thenReturn(true);
        assertEquals(true, returnsEmptyValues.answer(mapInvocation.arg(emptyTreeMap)));

        InvocationOnMock setInstanceInvocation = Mockito.methodCall(mock, "getClass");
        TreeSet<Integer> emptyTreeSet = new TreeSet<>();
        Mockito.when(mock.getClass()).thenReturn(TreeSet.class);
        Mockito.when(mock.isEmpty()).thenReturn(true);
        assertEquals(TreeSet.class, returnsEmptyValues.answer(setInstanceInvocation));
        InvocationOnMock setInvocation = Mockito.methodCall(mock, "isEmpty");
        assertEquals(true, returnsEmptyValues.answer(setInvocation));
    }

    @Test
    public void testEmptyMapMethods() {
        // test empty HashMap and LinkedHashMap
        InvocationOnMock mapInvocation = Mockito.methodCall(mock, "isEmpty", ArgumentMatchers.isInstance(HashMap.class));
        HashMap<String, String> map = ArgumentMatchers.argument();
        HashMap<String, String> emptyMap = new HashMap<>();
        returnsEmptyValues.answer(Mockito.any(InvocationOnMock.class));
        Mockito.when(mock.getClass()).thenReturn(HashMap.class);
        Mockito.when(mock.isEmpty()).thenReturn(true);
        assertEquals(true, returnsEmptyValues.answer(mapInvocation.arg(emptyMap)));

        InvocationOnMock linkedMapInvocation = Mockito.methodCall(mock, "isEmpty", ArgumentMatchers.isInstance(LinkedHashMap.class));
        LinkedHashMap<String, String> linkedMap = ArgumentMatchers.argument();
        LinkedHashMap<String, String> emptyLinkedMap = new LinkedHashMap<>();
        returnsEmptyValues.answer(Mockito.any(InvocationOnMock.class));
        Mockito.when(mock.getClass()).thenReturn(LinkedHashMap.class);
        Mockito.when(mock.isEmpty()).thenReturn(true);
        assertEquals(true, returnsEmptyValues.answer(linkedMapInvocation.arg(emptyLinkedMap)));
    }
}