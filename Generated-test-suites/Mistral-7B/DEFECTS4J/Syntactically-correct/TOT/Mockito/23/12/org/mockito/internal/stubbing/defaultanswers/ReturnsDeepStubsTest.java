package org.mockito.internal.stubbing.defaultanswers.test;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answer.Answer;
import org.mockito.internal.stubbing.returnvalues.ReturnsDeepStubs;
import org.mockito.internal.util.reflection.GenericMetadataSupport;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ReturnsDeepStubsTest {

    @Test
    public void testReturnsDeepStubsForMapEntrySet() {
        interface GenericsNest<K extends Comparable<K> & Cloneable, V> extends Map<K, Set<Number>> {
        }

        GenericsNest<Integer, Number> mock = mock(GenericsNest.class, new ReturnsDeepStubs());
        Number number = mock.entrySet().iterator().next().getValue().iterator().next();

        assertSame(number, number); // deep stub test for entry set
        assertSame(mock, mock.entrySet().iterator().next().getValue().iterator().next().getMock()); // deep stub test for value
    }

    @Test(expected = IllegalStateException.class)
    public void testThrowsExceptionForNonMockableType() {
        interface GenericsNest<K extends Comparable<K> & Cloneable, V> extends Map<K, String> {
        }

        GenericsNest<Integer, String> mock = mock(GenericsNest.class, new ReturnsDeepStubs());
        mock.entrySet().clear();
    }

    @Test
    public void testReturnsStubForMatchingInvocation() {
        interface GenericsNest<K> extends Map<K, Set<Number>> {
        }

        GenericsNest<Integer> mock = mock(GenericsNest.class, new ReturnsDeepStubs());
        GenericsNest<Integer> stub = mock(GenericsNest.class);
        when(stub.iterator()).thenReturn(Arrays.asList(mock.entrySet().iterator().next()).iterator());

        Map<Integer, Set<Number>> actualMap = mock.iterator().next().getValue();

        when(actualMap.iterator()).thenReturn(stub.iterator());

        Set<Number> set = actualMap.iterator().next().getValue();
        Number number = set.iterator().next();

        assertSame(number, number); // deep stub test for set
        assertSame(mock, set.iterator().next()); // deep stub test for number
    }
}