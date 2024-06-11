package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void test_isEmptyListAnswer() {
        // given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // when
        List<String> list = Mockito.spy(new ArrayList<>());
        Answer<List<String>> answer = Mockito.answer(returnsEmptyValues);
        Mockito.doAnswer(answer).when(list).someMethodReturningList();

        // then
        List<String> result = list.someMethodReturningList();
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void test_isEmptySetAnswer() {
        // given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // when
        Set<String> set = Mockito.spy(new HashSet<>());
        Answer<Set<String>> answer = Mockito.answer(returnsEmptyValues);
        Mockito.doAnswer(answer).when(set).someMethodReturningSet();

        // then
        Set<String> result = set.someMethodReturningSet();
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void test_isEmptyMapAnswer() {
        // given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // when
        Map<String, String> map = Mockito.spy(new HashMap<>());
        Answer<Map<String, String>> answer = Mockito.answer(returnsEmptyValues);
        Mockito.doAnswer(answer).when(map).someMethodReturningMap();

        // then
        Map<String, String> result = map.someMethodReturningMap();
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void test_isPrimitiveWrapperTypeAnswer() {
        // given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // when
        int primitiveInt = 0;
        Mockito.when(Mockito.mock(Integer.class)).thenReturn(primitiveInt);
        Integer wrapperInt = Mockito.mock(Integer.class);
        Mockito.when(wrapperInt).thenAnswer(returnsEmptyValues);

        // then
        assertEquals(0, Mockito.verify(wrapperInt).someMethodReturningInteger());
    }

    @Test
    public void test_compareToMethodAnswer() {
        // given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // when
        Object mock1 = Mockito.mock(Comparable.class);
        Object mock2 = Mockito.mock(Comparable.class);
        Mockito.when(mock1).thenAnswer(returnsEmptyValues);
        Mockito.when(mock2).thenAnswer(returnsEmptyValues);

        // then
        int result = Mockito.verify(mock1, ArgumentMatchers.eq(mock2)).compareTo(mock2);
        assertEquals(1, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void test_modifyCollection() {
        // given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        List<String> list = Mockito.spy(new ArrayList<>());
        Answer<List<String>> answer = Mockito.answer(returnsEmptyValues);
        Mockito.doAnswer(answer).when(list).someMethodReturningList();

        // when
        list.add("test");

        // then (should throw UnsupportedOperationException)
    }
}