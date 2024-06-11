package org.mockito.internal.stubbing.defaultanswers;

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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        MockitoAnswer answer = mock(Answer.class);
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        when(answer.answer(any(InvocationOnMock.class))).then(returnsEmptyValues);

        Object mock = mock(Object.class);
        when(mock.toString()).then(answer);

        String mockString = (String) mock.toString();
        String expectedString = "Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode();

        assertThat(mockString, is(expectedString));
    }

    @Test
    public void testCompareToMethod() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        when(mock1.toString()).thenReturn("mock1");
        when(mock2.toString()).thenReturn("mock2");
        doAnswer(returnsEmptyValues).when(mock1).compareTo(mock2);

        int result = mock1.compareTo(mock2);

        assertThat(result, is(1));
    }

    @Test
    public void testPrimitiveMethods() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        int intValue = (int) returnsEmptyValues.answer(argThat(i -> i.getMethod().getName().equals("intValue")));
        char charValue = (char) returnsEmptyValues.answer(argThat(i -> i.getMethod().getName().equals("charValue")));
        boolean booleanValue = (boolean) returnsEmptyValues.answer(argThat(i -> i.getMethod().getName().equals("booleanValue")));
        short shortValue = (short) returnsEmptyValues.answer(argThat(i -> i.getMethod().getName().equals("shortValue")));
        byte byteValue = (byte) returnsEmptyValues.answer(argThat(i -> i.getMethod().getName().equals("byteValue")));
        long longValue = (long) returnsEmptyValues.answer(argThat(i -> i.getMethod().getName().equals("longValue")));
        float floatValue = (float) returnsEmptyValues.answer(argThat(i -> i.getMethod().getName().equals("floatValue")));
        double doubleValue = (double) returnsEmptyValues.answer(argThat(i -> i.getMethod().getName().equals("doubleValue")));

        assertThat(intValue, is(0));
        assertThat(charValue, is((char) 0));
        assertThat(booleanValue, is(false));
        assertThat(shortValue, is((short) 0));
        assertThat(byteValue, is((byte) 0));
        assertThat(longValue, is(0L));
        assertThat(floatValue, is(0.0f));
        assertThat(doubleValue, is(0.0d));
    }

    @Test
    public void testEmptyCollectionMethods() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        List<String> list = (List<String>) returnsEmptyValues.answer(argThat(i -> i.getMethod().getName().equals("list")));
        Set<String> set = (Set<String>) returnsEmptyValues.answer(argThat(i -> i.getMethod().getName().equals("set")));
        HashSet<String> hashSet = (HashSet<String>) returnsEmptyValues.answer(argThat(i -> i.getMethod().getName().equals("hashSet")));
        LinkedHashSet<String> linkedHashSet = (LinkedHashSet<String>) returnsEmptyValues.answer(argThat(i -> i.getMethod().getName().equals("linkedHashSet")));
        SortedSet<String> sortedSet = (SortedSet<String>) returnsEmptyValues.answer(argThat(i -> i.getMethod().getName().equals("sortedSet")));
        TreeSet<String> treeSet = (TreeSet<String>) returnsEmptyValues.answer(argThat(i -> i.getMethod().getName().equals("treeSet")));

        ArrayList<String> emptyArrayList = new ArrayList<>();
        assertThat(list, is(emptyArrayList));

        HashMap<String, String> emptyHashMap = new HashMap<>();
        assertThat(set, is(Collections.emptySet()));
        assertThat(hashSet, is(new HashSet<>()));
        assertThat(linkedHashSet, is(new LinkedHashSet<>()));
        assertThat(sortedSet, is(Collections.emptySortedSet()));
        assertThat(treeSet, is(new TreeSet<>()));
    }

    @Test
    public void testNullReturn() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Object nullValue = returnsEmptyValues.answer(argThat(i -> i.getMethod().getName().equals("null")));

        assertThat(nullValue, is(nullValue()));
    }
}