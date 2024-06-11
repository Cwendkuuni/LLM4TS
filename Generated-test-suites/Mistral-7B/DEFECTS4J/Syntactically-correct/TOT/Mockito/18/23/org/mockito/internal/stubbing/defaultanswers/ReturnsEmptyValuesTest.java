package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        Object mock = mock(String.class);
        ReturnsEmptyValues answer = Mockito.mock(ReturnsEmptyValues.class);
        Mockito.when(answer.answer(ArgumentMatchers.any(InvocationOnMock.class))).then(answer);

        String actual = (String) answer.answer(new InvocationOnMock(mock, Mockito.method(String.class, "toString")));

        assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), actual);
    }

    @Test
    public void testCompareToMethod() {
        Object mock1 = mock(Comparable.class);
        Object mock2 = mock(Comparable.class);

        ReturnsEmptyValues answer = Mockito.mock(ReturnsEmptyValues.class);
        Mockito.when(answer.answer(ArgumentMatchers.any(InvocationOnMock.class))).then(answer);

        int comparisonResult = (Integer) answer.answer(new InvocationOnMock(mock1, Mockito.method(Comparable.class, "compareTo", mock2)));

        Mockito.verify(mock1).equals(mock2); // Verify that mocks were compared by reference
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testPrimitiveTypes() {
        ReturnsEmptyValues answer = Mockito.mock(ReturnsEmptyValues.class);

        byte expectedByte = (byte) 0;
        short expectedShort = (short) 0;
        int expectedInt = 0;
        long expectedLong = 0L;
        float expectedFloat = 0f;
        double expectedDouble = 0d;
        char expectedChar = (char) 0;
        boolean expectedBoolean = false;

        byte actualByte = (byte) answer.answer(new InvocationOnMock(null, Mockito.method(Byte.class, "valueOf", (Object) null)));
        short actualShort = (short) answer.answer(new InvocationOnMock(null, Mockito.method(Short.class, "valueOf", (Object) null)));
        int actualInt = (int) answer.answer(new InvocationOnMock(null, Mockito.method(Integer.class, "valueOf", (Object) null)));
        long actualLong = (long) answer.answer(new InvocationOnMock(null, Mockito.method(Long.class, "valueOf", (Object) null)));
        float actualFloat = (float) answer.answer(new InvocationOnMock(null, Mockito.method(Float.class, "valueOf", (Object) null)));
        double actualDouble = (double) answer.answer(new InvocationOnMock(null, Mockito.method(Double.class, "valueOf", (Object) null)));
        char actualChar = (char) answer.answer(new InvocationOnMock(null, Mockito.method(Character.class, "valueOf", (Object) null)));
        boolean actualBoolean = (boolean) answer.answer(new InvocationOnMock(null, Mockito.method(Boolean.class, "valueOf", (Object) null)));

        assertEquals(expectedByte, actualByte);
        assertEquals(expectedShort, actualShort);
        assertEquals(expectedInt, actualInt);
        assertEquals(expectedLong, actualLong);
        assertEquals(expectedFloat, actualFloat, 0.001f);
        assertEquals(expectedDouble, actualDouble, 0.001d);
        assertEquals(expectedChar, actualChar);
        assertEquals(expectedBoolean, actualBoolean);
    }

    @Test
    public void testEmptyCollectionTypes() {
        ReturnsEmptyValues answer = Mockito.mock(ReturnsEmptyValues.class);

        List<String> expectedList = Collections.emptyList();
        Set<String> expectedSet = Collections.emptySet();
        HashSet<String> expectedHashSet = new HashSet<>();
        SortedSet<String> expectedSortedSet = new TreeSet<>();
        SortedMap<String, String> expectedSortedMap = new TreeMap<>();

        List<String> actualList = (List<String>) answer.answer(new InvocationOnMock(null, Mockito.method(List.class, "ofElements", (Object[]) new Object[0])));
        Set<String> actualSet = (Set<String>) answer.answer(new InvocationOnMock(null, Mockito.method(Set.class, "ofElements", (Object[]) new Object[0])));
        HashSet<String> actualHashSet = (HashSet<String>) answer.answer(new InvocationOnMock(null, Mockito.method(HashSet.class, "newEmptySet", (Class<HashSet<String>>) null)));
        SortedSet<String> actualSortedSet = (SortedSet<String>) answer.answer(new InvocationOnMock(null, Mockito.method(SortedSet.class, "newTreeSet", (Class<SortedSet<String>>) null)));
        Map<String, String> actualMap = (Map<String, String>) answer.answer(new InvocationOnMock(null, Mockito.method(Map.class, "newHashMap", (Map<String, String>) null)));
        SortedMap<String, String> actualSortedMap = (SortedMap<String, String>) answer.answer(new InvocationOnMock(null, Mockito.method(SortedMap.class, "newTreeMap", (Class<SortedMap<String, String>>) null)));

        assertEquals(expectedList, actualList);
        assertEquals(expectedSet, actualSet);
        assertEquals(expectedHashSet, actualHashSet);
        assertEquals(expectedSortedSet, actualSortedSet);
        assertEquals(expectedSortedMap, actualSortedMap);
    }

    @Test
    public void testOtherCollectionTypes() {
        ReturnsEmptyValues answer = Mockito.mock(ReturnsEmptyValues.class);

        ArrayList<String> expectedArrayList = new ArrayList<>();
        LinkedList<String> expectedLinkedList = new LinkedList<>();
        ArrayList<Integer> expectedArrayListOfInts = new ArrayList<>();

        List<String> actualArrayList = (List<String>) answer.answer(new InvocationOnMock(null, Mockito.method(ArrayList.class, "<init>")));
        List<String> actualLinkedList = (List<String>) answer.answer(new InvocationOnMock(null, Mockito.method(LinkedList.class, "<init>")));
        List<Integer> actualArrayListOfInts = (List<Integer>) answer.answer(new InvocationOnMock(null, Mockito.method(ArrayList.class, "<init>", int.class, int.class)));

        assertEquals(expectedArrayList, actualArrayList);
        assertEquals(expectedLinkedList, actualLinkedList);
        assertEquals(expectedArrayListOfInts, actualArrayListOfInts);
    }
}