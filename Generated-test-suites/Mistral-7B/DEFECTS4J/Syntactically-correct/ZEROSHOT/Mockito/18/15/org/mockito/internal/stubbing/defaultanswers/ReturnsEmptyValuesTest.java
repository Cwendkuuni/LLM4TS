package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToString() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        Object mock = mock(String.class);
        when(mock.toString()).thenAnswer(answer);

        assertEquals("Mock for String, hashCode: " + mock.hashCode(), mock.toString());
    }

    @Test
    public void testCompareTo() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        Object obj1 = mock(Comparable.class);
        Object obj2 = mock(Comparable.class);

        when(obj1.compareTo(ArgumentMatchers.any())).thenAnswer(answer);
        when(obj2.compareTo(ArgumentMatchers.any())).thenAnswer(answer);

        assertEquals(1, obj1.compareTo(obj2));
    }

    @Test
    public void testPrimitiveTypes() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        byte byteValue = answer.returnValueFor(byte.class);
        short shortValue = answer.returnValueFor(short.class);
        int intValue = answer.returnValueFor(int.class);
        long longValue = answer.returnValueFor(long.class);
        float floatValue = answer.returnValueFor(float.class);
        double doubleValue = answer.returnValueFor(double.class);
        boolean booleanValue = answer.returnValueFor(boolean.class);
        char charValue = answer.returnValueFor(char.class);

        assertEquals((byte) 0, byteValue);
        assertEquals((short) 0, shortValue);
        assertEquals(0, intValue);
        assertEquals(0L, longValue);
        assertEquals(0f, floatValue, 0.01f);
        assertEquals(0d, doubleValue, 0.01d);
        assertEquals(false, booleanValue);
        assertEquals((char) 0, charValue);
    }

    @Test
    public void testEmptyCollection() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        Collection<String> collectionValue = (Collection<String>) answer.returnValueFor(Collection.class);

        assertEquals(0, collectionValue.size());
    }

    @Test
    public void testEmptySet() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        Set<String> setValue = (Set<String>) answer.returnValueFor(Set.class);

        assertEquals(0, setValue.size());
    }

    @Test
    public void testEmptyHashSet() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        Set<String> hashSetValue = (Set<String>) answer.returnValueFor(HashSet.class);

        assertEquals(0, hashSetValue.size());
    }

    @Test
    public void testEmptyTreeSet() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        SortedSet<String> treeSetValue = (SortedSet<String>) answer.returnValueFor(TreeSet.class);

        assertEquals(0, treeSetValue.size());
    }

    @Test
    public void testEmptyLinkedHashSet() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        Set<String> linkedHashSetValue = (Set<String>) answer.returnValueFor(LinkedHashSet.class);

        assertEquals(0, linkedHashSetValue.size());
    }

    @Test
    public void testEmptyList() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        List<String> listValue = (List<String>) answer.returnValueFor(List.class);

        assertEquals(0, listValue.size());
    }

    @Test
    public void testEmptyLinkedList() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        List<String> linkedListValue = (List<String>) answer.returnValueFor(LinkedList.class);

        assertEquals(0, linkedListValue.size());
    }

    @Test
    public void testEmptyArrayList() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        List<String> arrayListValue = (List<String>) answer.returnValueFor(ArrayList.class);

        assertEquals(0, arrayListValue.size());
    }

    @Test
    public void testEmptyMap() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        Map<String, String> mapValue = (Map<String, String>) answer.returnValueFor(Map.class);

        assertEquals(0, mapValue.size());
    }

    @Test
    public void testEmptyHashMap() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        Map<String, String> hashMapValue = (Map<String, String>) answer.returnValueFor(HashMap.class);

        assertEquals(0, hashMapValue.size());
    }

    @Test
    public void testEmptyTreeMap() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        SortedMap<String, String> treeMapValue = (SortedMap<String, String>) answer.returnValueFor(TreeMap.class);

        assertEquals(0, treeMapValue.size());
    }

    @Test
    public void testEmptyLinkedHashMap() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        Map<String, String> linkedHashMapValue = (Map<String, String>) answer.returnValueFor(LinkedHashMap.class);

        assertEquals(0, linkedHashMapValue.size());
    }
}