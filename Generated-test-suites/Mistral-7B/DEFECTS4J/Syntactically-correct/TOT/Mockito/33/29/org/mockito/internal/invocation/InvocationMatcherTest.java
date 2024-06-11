package org.mockito.internal.invocation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testMatcherWithNoArguments() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when & then
        verify(invocationMatcher).matches(invocation);
    }

    @Test
    public void testMatcherWithArguments() {
        // given
        Object mock = mock(Object.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getMethod()).thenReturn(new Method(Mockito.class.getName(), "test", new Class[0]));
        Object[] arguments = new Object[]{1, 2, 3};

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq(2), ArgumentMatchers.eq(3)));

        // when & then
        verify(invocationMatcher).matches(new InvocationOnMock(invocation) {
            {
                setArguments(arguments);
            }
        });

        // another test case with no match
        Object[] noMatchArguments = new Object[]{1, "notMatched"};
        verifyZeroInteractions(new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq(2))));
    }

    @Test(expected = AssertionError.class)
    public void testMatcherWithMismatchedArguments() {
        // given
        Object mock = mock(Object.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getMethod()).thenReturn(new Method(Mockito.class.getName(), "test", new Class[0]));
        Object[] arguments = new Object[]{1, 2, 3};

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq(3), ArgumentMatchers.eq(2)));

        // when & then
        InvocationOnMock invocationOnMock = new InvocationOnMock(invocation) {
            {
                setArguments(arguments);
            }
        };

        invocationMatcher.matches(invocationOnMock);
    }

    @Test
    public void testToString() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq(2), ArgumentMatchers.eq(3));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when
        String toString = invocationMatcher.toString();

        // then
        String expectedToString = String.format("InvocationMatcher[%s, %s]", invocation.toString(), matchers.toString());
        assertEquals(expectedToString, toString);
    }

    @Test
    public void testHasSimilarMethod() {
        // given
        Object mock = mock(Object.class);
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        when(invocation1.getMock()).thenReturn(mock);
        when(invocation1.getMethod()).thenReturn(new Method("test1", new Class[0]));
        when(invocation2.getMock()).thenReturn(mock);
        when(invocation2.getMethod()).thenReturn(new Method("test2", new Class[0]));

        // when
        boolean result1 = new InvocationMatcher(invocation1).hasSimilarMethod(invocation1);
        boolean result2 = new InvocationMatcher(invocation1).hasSimilarMethod(invocation2);

        // then
        assertTrue(result1);
        assertFalse(result2);
    }

    @Test
    public void testHasSameMethod() {
        // given
        Object mock = mock(Object.class);
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        when(invocation1.getMock()).thenReturn(mock);
        when(invocation1.getMethod()).thenReturn(new Method("test", new Class[0]));
        when(invocation2.getMock()).thenReturn(mock);
        when(invocation2.getMethod()).thenReturn(new Method("test", new Class[0]));

        // when
        boolean result = new InvocationMatcher(invocation1).hasSameMethod(invocation2);

        // then
        assertTrue(result);
    }
}