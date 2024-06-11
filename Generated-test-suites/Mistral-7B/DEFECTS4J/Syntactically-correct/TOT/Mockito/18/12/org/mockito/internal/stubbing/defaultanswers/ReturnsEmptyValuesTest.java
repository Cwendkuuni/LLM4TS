package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

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
import static org.mockito.Mockito.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        Object mock = Mockito.mock(String.class);
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        when(mock.toString()).thenAnswer(answer);

        String actual = mock.toString();
        String expected = "Mock for " + String.class.getSimpleName() + ", hashCode: " + mock.hashCode();

        assertEquals(expected, actual);
        verify(mock, times(1)).toString();
    }

    @Test
    public void testCompareToMethod() {
        Object mock1 = Mockito.mock(Comparable.class);
        Object mock2 = Mockito.mock(Comparable.class);

        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        when(mock1.compareTo(ArgumentMatchers.any())).thenAnswer(answer);

        int actual1 = mock1.compareTo(mock1);
        int expected1 = 0;

        int actual2 = mock1.compareTo(mock2);
        int expected2 = 1;

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
        verify(mock1, times(2)).compareTo(any());
    }

    @Test
    public void testPrimitiveTypes() {
        Byte mock = Mockito.mock(Byte.class);
        Short mockShort = Mockito.mock(Short.class);
        Integer mockInt = Mockito.mock(Integer.class);
        Long mockLong = Mockito.mock(Long.class);
        Float mockFloat = Mockito.mock(Float.class);
        Double mockDouble = Mockito.mock(Double.class);
        Character mockChar = Mockito.mock(Character.class);
        Boolean mockBoolean = Mockito.mock(Boolean.class);

        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        when(mockByte("byteValue")).thenReturn((byte) 0);
        when(mockByte("byteValue")).thenAnswer(answer);

        when(mockShort("shortValue")).thenReturn((short) 0);
        when(mockShort("shortValue")).thenAnswer(answer);

        when(mockInt("intValue")).thenReturn(0);
        when(mockInt("intValue")).thenAnswer(answer);

        when(mockLong("longValue")).thenReturn(0L);
        when(mockLong("longValue")).thenAnswer(answer);

        when(mockFloat("floatValue")).thenReturn(0f);
        when(mockFloat("floatValue")).thenAnswer(answer);

        when(mockDouble("doubleValue")).thenReturn(0d);
        when(mockDouble("doubleValue")).thenAnswer(answer);

        when(mockChar("charValue")).thenReturn((char) 0);
        when(mockChar("charValue")).thenAnswer(answer);

        when(mockBoolean("booleanValue")).thenReturn(false);
        when(mockBoolean("booleanValue")).thenAnswer(answer);

        byte byteValue = mockByte().byteValue();
        short shortValue = mockShort().shortValue();
        int intValue = mockInt().intValue();
        long longValue = mockLong().longValue();
        float floatValue = mockFloat().floatValue();
        double doubleValue = mockDouble().doubleValue();
        char charValue = mockChar().charValue();
        boolean booleanValue = mockBoolean().booleanValue();

        assertEquals((byte) 0, byteValue);
        assertEquals((short) 0, shortValue);
        assertEquals(0, intValue);
        assertEquals(0L, longValue);
        assertEquals(0f, floatValue, 0.01f);
        assertEquals(0d, doubleValue, 0.01d);
        assertEquals((char) 0, charValue);
        assertFalse(booleanValue);
    }

    @Test
    public void testCollectionTypes() {
        Collection<Integer> mockCollection = Mockito.mock(Collection.class);
        Set<Integer> mockSet = Mockito.mock(Set.class);
        HashSet<Integer> mockHashSet = Mockito.mock(HashSet.class);
        SortedSet<Integer> mockSortedSet = Mockito.mock(SortedSet.class);
        TreeSet<Integer> mockTreeSet = Mockito.mock(TreeSet.class);
        LinkedHashSet<Integer> mockLinkedHashSet = Mockito.mock(LinkedHashSet.class);
        List<Integer> mockList = Mockito.mock(List.class);
        LinkedList<Integer> mockLinkedList = Mockito.mock(LinkedList.class);
        ArrayList<Integer> mockArrayList = Mockito.mock(ArrayList.class);
        Map<String, Integer> mockMap = Mockito.mock(Map.class);
        HashMap<String, Integer> mockHashMap = Mockito.mock(HashMap.class);
        SortedMap<String, Integer> mockSortedMap = Mockito.mock(SortedMap.class);
        TreeMap<String, Integer> mockTreeMap = Mockito.mock(TreeMap.class);
        LinkedHashMap<String, Integer> mockLinkedHashMap = Mockito.mock(LinkedHashMap.class);

        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        when(mockCollection.size()).thenReturn(0);
        when(mockCollection.isEmpty()).thenReturn(true);
        when(mockCollection).thenAnswer(answer);

        when(mockSet.size()).thenReturn(0);
        when(mockSet.isEmpty()).thenReturn(true);
        when(mockSet).thenAnswer(answer);

        when(mockHashSet.size()).thenReturn(0);
        when(mockHashSet.isEmpty()).thenReturn(true);
        when(mockHashSet).thenAnswer(answer);

        when(mockSortedSet.size()).thenReturn(0);
        when(mockSortedSet.isEmpty()).thenReturn(true);
        when(mockSortedSet).thenAnswer(answer);

        when(mockTreeSet.size()).thenReturn(0);
        when(mockTreeSet.isEmpty()).thenReturn(true);
        when(mockTreeSet).thenAnswer(answer);

        when(mockLinkedHashSet.size()).thenReturn(0);
        when(mockLinkedHashSet.isEmpty()).thenReturn(true);
        when(mockLinkedHashSet).thenAnswer(answer);

        when(mockList.size()).thenReturn(0);
        when(mockList.isEmpty()).thenReturn(true);
        when(mockList).thenAnswer(answer);

        when(mockLinkedList.size()).thenReturn(0);
        when(mockLinkedList.isEmpty()).thenReturn(true);
        when(mockLinkedList).thenAnswer(answer);

        when(mockArrayList.size()).thenReturn(0);
        when(mockArrayList.isEmpty()).thenReturn(true);
        when(mockArrayList).thenAnswer(answer);

        when(mockMap.size()).thenReturn(0);
        when(mockMap.isEmpty()).thenReturn(true);
        when(mockMap).thenAnswer(answer);

        when(mockHashMap.size()).thenReturn(0);
        when(mockHashMap.isEmpty()).thenReturn(true);
        when(mockHashMap).thenAnswer(answer);

        when(mockSortedMap.size()).thenReturn(0);
        when(mockSortedMap.isEmpty()).thenReturn(true);
        when(mockSortedMap).thenAnswer(answer);

        when(mockTreeMap.size()).thenReturn(0);
        when(mockTreeMap.isEmpty()).thenReturn(true);
        when(mockTreeMap).thenAnswer(answer);

        when(mockLinkedHashMap.size()).thenReturn(0);
        when(mockLinkedHashMap.isEmpty()).thenReturn(true);
        when(mockLinkedHashMap).thenAnswer(answer);

        Collection<Integer> actualCollection = mockCollection;
        Set<Integer> actualSet = mockSet;
        HashSet<Integer> actualHashSet = mockHashSet;
        SortedSet<Integer> actualSortedSet = mockSortedSet;
        TreeSet<Integer> actualTreeSet = mockTreeSet;
        LinkedHashSet<Integer> actualLinkedHashSet = mockLinkedHashSet;
        List<Integer> actualList = mockList;
        LinkedList<Integer> actualLinkedList = mockLinkedList;
        ArrayList<Integer> actualArrayList = mockArrayList;
        Map<String, Integer> actualMap = mockMap;
        HashMap<String, Integer> actualHashMap = mockHashMap;
        SortedMap<String, Integer> actualSortedMap = mockSortedMap;
        TreeMap<String, Integer> actualTreeMap = mockTreeMap;
        LinkedHashMap<String, Integer> actualLinkedHashMap = mockLinkedHashMap;

        assertEquals(0, actualCollection.size());
        assertTrue(actualCollection.isEmpty());
        assertSame(answer, actualCollection);

        assertEquals(0, actualSet.size());
        assertTrue(actualSet.isEmpty());
        assertSame(answer, actualSet);

        assertEquals(0, actualHashSet.size());
        assertTrue(actualHashSet.isEmpty());
        assertSame(answer, actualHashSet);

        assertEquals(0, actualSortedSet.size());
        assertTrue(actualSortedSet.isEmpty());
        assertSame(answer, actualSortedSet);

        assertEquals(0, actualTreeSet.size());
        assertTrue(actualTreeSet.isEmpty());
        assertSame(answer, actualTreeSet);

        assertEquals(0, actualLinkedHashSet.size());
        assertTrue(actualLinkedHashSet.isEmpty());
        assertSame(answer, actualLinkedHashSet);

        assertEquals(0, actualList.size());
        assertTrue(actualList.isEmpty());
        assertSame(answer, actualList);

        assertEquals(0, actualLinkedList.size());
        assertTrue(actualLinkedList.isEmpty());
        assertSame(answer, actualLinkedList);

        assertEquals(0, actualArrayList.size());
        assertTrue(actualArrayList.isEmpty());
        assertSame(answer, actualArrayList);

        assertEquals(0, actualMap.size());
        assertTrue(actualMap.isEmpty());
        assertSame(answer, actualMap);

        assertEquals(0, actualHashMap.size());
        assertTrue(actualHashMap.isEmpty());
        assertSame(answer, actualHashMap);

        assertEquals(0, actualSortedMap.size());
        assertTrue(actualSortedMap.isEmpty());
        assertSame(answer, actualSortedMap);

        assertEquals(0, actualTreeMap.size());
        assertTrue(actualTreeMap.isEmpty());
        assertSame(answer, actualTreeMap);

        assertEquals(0, actualLinkedHashMap.size());
        assertTrue(actualLinkedHashMap.isEmpty());
        assertSame(answer, actualLinkedHashMap);
    }
}