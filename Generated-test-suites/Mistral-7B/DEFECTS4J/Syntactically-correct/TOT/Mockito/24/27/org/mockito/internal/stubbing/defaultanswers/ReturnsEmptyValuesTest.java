package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
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

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues;

    @Before
    public void setup() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }

    @Test
    public void testToStringMethod() {
        Object mock = mock(String.class);
        when(mock.toString()).thenAnswer(returnsEmptyValues);

        assertEquals("Mock for String, hashCode: " + mock.hashCode(), mock.toString());
        verify(mock).toString();
    }

    @Test
    public void testCompareToMethod() {
        Object mock1 = mock(Comparable.class);
        Object mock2 = mock(Comparable.class);

        Mockito.doReturn(1).when(mock1).compareTo(ArgumentMatchers.any());
        Mockito.doReturn(returnsEmptyValues).when(mock1).answer(ArgumentMatchers.any());

        Mockito.doReturn(2).when(mock2).compareTo(ArgumentMatchers.any());
        Mockito.doReturn(returnsEmptyValues).when(mock2).answer(ArgumentMatchers.any());

        assertEquals(1, mock1.compareTo(mock2));
        assertEquals(1, mock2.compareTo(mock1));
    }

    @Test
    public void testPrimitiveTypes() {
        int intMock = Mockito.mock(int.class);
        when(intMock.intValue()).thenAnswer(returnsEmptyValues);

        assertEquals(0, intMock.intValue());

        Integer intWrapperMock = Mockito.mock(Integer.class);
        when(intWrapperMock.intValue()).thenAnswer(returnsEmptyValues);

        assertEquals(0, intWrapperMock.intValue());
    }

    @Test
    public void testCollections() {
        Collection<String> collectionMock = Mockito.mock(Collection.class);
        when(collectionMock.isEmpty()).thenReturn(true);
        when(collectionMock.size()).thenReturn(0);

        List<String> listMock = Mockito.mock(List.class);
        when(listMock.isEmpty()).thenReturn(true);
        when(listMock.size()).thenReturn(0);

        Set<String> setMock = Mockito.mock(Set.class);
        when(setMock.isEmpty()).thenReturn(true);
        when(setMock.size()).thenReturn(0);

        Map<String, String> mapMock = Mockito.mock(Map.class);
        when(mapMock.isEmpty()).thenReturn(true);
        when(mapMock.size()).thenReturn(0);

        assertEquals(new ArrayList<>(), collectionMock);
        assertEquals(new ArrayList<>(), listMock);
        assertEquals(new HashSet<>(), setMock);
        assertEquals(new HashMap<>(), mapMock);

        // Let's not care about the rest of collections for this test.
    }
}