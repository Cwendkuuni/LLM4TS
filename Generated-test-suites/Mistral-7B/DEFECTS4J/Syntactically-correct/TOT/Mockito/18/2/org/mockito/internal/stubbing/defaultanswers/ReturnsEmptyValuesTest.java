package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        ReturnsEmptyValues answer = Mockito.mock(Answer.class);
        Object mock = mock(Object.class);
        when(answer.answer(ArgumentMatchers.any(InvocationOnMock.class))).then(answer);

        MockName name = Mockito.mock(MockName.class);
        when(name.isDefault()).thenReturn(false);
        when(name.toString()).thenReturn("CustomMockName");
        when(answer.answer(ArgumentMatchers.argThat(inv -> inv.getMock() == mock && inv.getMethod().getName().equals("toString"))))
                .thenReturn("CustomMockName");

        String result = answer.answer(new InvocationOnMock(mock, "toString", Collections.emptyList()));
        assertEquals("CustomMockName", result);

        name = Mockito.mock(MockName.class);
        when(name.isDefault()).thenReturn(true);
        when(name.toString()).thenCallRealMethod();

        result = answer.answer(new InvocationOnMock(mock, "toString", Collections.emptyList()));
        assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testCompareToMethod() {
        ReturnsEmptyValues answer = Mockito.mock(Answer.class);
        Object mock1 = Mockito.mock(Comparable.class);
        Object mock2 = Mockito.mock(Comparable.class);

        when(answer.answer(ArgumentMatchers.any(InvocationOnMock.class))).then(answer);

        when(answer.answer(ArgumentMatchers.argThat(inv -> inv.getMethod().getName().equals("compareTo"))))
                .then((InvocationOnMock invocation) -> invocation.getMock() == invocation.getArguments()[0] ? 0 : 1);

        int result1 = answer.answer(new InvocationOnMock(mock1, "compareTo", Arrays.asList(mock1))).intValue();
        int result2 = answer.answer(new InvocationOnMock(mock2, "compareTo", Arrays.asList(mock1))).intValue();
        int result3 = answer.answer(new InvocationOnMock(mock1, "compareTo", Arrays.asList(mock2))).intValue();

        assertEquals(0, result1);
        assertEquals(1, result2);
        assertEquals(-1, result3);
    }

    @Test
    public void testPrimitiveMethods() {
        ReturnsEmptyValues answer = Mockito.mock(Answer.class);

        byte byteResult = (byte) answer.answer(new InvocationOnMock(null, "byteValue", Collections.emptyList())).byteValue();
        short shortResult = answer.answer(new InvocationOnMock(null, "shortValue", Collections.emptyList())).shortValue();
        int intResult = answer.answer(new InvocationOnMock(null, "intValue", Collections.emptyList())).intValue();
        long longResult = answer.answer(new InvocationOnMock(null, "longValue", Collections.emptyList())).longValue();
        float floatResult = answer.answer(new InvocationOnMock(null, "floatValue", Collections.emptyList())).floatValue();
        double doubleResult = answer.answer(new InvocationOnMock(null, "doubleValue", Collections.emptyList())).doubleValue();
        boolean booleanResult = answer.answer(new InvocationOnMock(null, "booleanValue", Collections.emptyList())).booleanValue();
        char charResult = answer.answer(new InvocationOnMock(null, "charValue", Collections.emptyList())).charValue();

        assertEquals(0, byteResult);
        assertEquals(0, shortResult);
        assertEquals(0, intResult);
        assertEquals(0L, longResult);
        assertEquals(0.0F, floatResult, 0.001F);
        assertEquals(0.0D, doubleResult, 0.001D);
        assertEquals(false, booleanResult);
        assertEquals((char) 0, charResult);
    }

    @Test
    public void testEmptyCollections() {
        ReturnsEmptyValues answer = Mockito.mock(Answer.class);

        Collection<Object> collectionResult = (Collection<Object>) answer.answer(new InvocationOnMock(null, "size", Collections.emptyList()));
        Set<Object> setResult = (Set<Object>) answer.answer(new InvocationOnMock(null, "size", Collections.emptySet()));
        SortedSet<Object> sortedSetResult = (SortedSet<Object>) answer.answer(new InvocationOnMock(null, "size", Collections.emptySortedSet()));
        SortedMap<Object, Object> sortedMapResult = (SortedMap<Object, Object>) answer.answer(new InvocationOnMock(null, "size", Collections.emptySortedMap()));
        Map<Object, Object> mapResult = (Map<Object, Object>) answer.answer(new InvocationOnMock(null, "size", Collections.emptyMap()));
        List<Object> listResult = (List<Object>) answer.answer(new InvocationOnMock(null, "size", Collections.emptyList()));
        LinkedList<Object> linkedListResult = (LinkedList<Object>) answer.answer(new InvocationOnMock(null, "size", Collections.emptyList()));
        ArrayList<Object> arrayListResult = (ArrayList<Object>) answer.answer(new InvocationOnMock(null, "size", Collections.emptyList()));

        assertNotNull(collectionResult);
        assertNotNull(setResult);
        assertNotNull(sortedSetResult);
        assertNotNull(sortedMapResult);
        assertNotNull(mapResult);
        assertNotNull(listResult);
        assertNotNull(linkedListResult);
        assertNotNull(arrayListResult);

        assertEquals(0, collectionResult.size());
        assertEquals(0, setResult.size());
        assertEquals(0, sortedSetResult.size());
        assertEquals(0, sortedMapResult.size());
        assertEquals(0, mapResult.size());
        assertEquals(0, listResult.size());
        assertEquals(0, linkedListResult.size());
        assertEquals(0, arrayListResult.size());
    }
}