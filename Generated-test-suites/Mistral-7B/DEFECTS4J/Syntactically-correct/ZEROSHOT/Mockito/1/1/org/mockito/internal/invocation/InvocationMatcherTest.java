package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.ArgumentCapture;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Stubber;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class InvocationMatcherTest {

    @Test
    public void testMatcherWithNoArgs() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertEquals(0, matchers.size());
    }

    @Test
    public void testMatcherWithArgs() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[]{1, "test"};

        // when
        List<Matcher> matchers = Arrays.asList(is("test"), is(1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // then
        List<Matcher> matchersActual = invocationMatcher.getMatchers();
        assertEquals(2, matchersActual.size());
        assertEquals(matchers.get(0), matchersActual.get(0));
        assertEquals(matchers.get(1), matchersActual.get(1));
    }

    @Test
    public void testMatcherWithNoMatchers() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertEquals(0, matchers.size());
    }

    @Test
    public void testMatcherToString() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(is("test"), is(1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when
        String toString = invocationMatcher.toString();

        // then
        String expectedToString = String.format("InvocationMatcher [%s]", new PrintSettings().print(matchers, invocation));
        assertEquals(expectedToString, toString);
    }

    @Test
    public void testInvocationMatcherMatchesInvocation() {
        // given
        Invocation invocationToMatch = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocationToMatch, Collections.singletonList(is(1)));

        // when
        Invocation invocationToTest = Mockito.mock(Invocation.class);
        when(invocationToTest.getMethod()).thenReturn(new Method("test", int.class));
        when(invocationToTest.getArguments()).thenReturn(new Object[]{1});

        // then
        assertTrue(matcher.matches(invocationToTest));
    }

    @Test
    public void testInvocationMatcherDoesNotMatchInvocationDifferentMethod() {
        // given
        Invocation invocationToMatch = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocationToMatch, Collections.singletonList(is("test")));

        // when
        Invocation invocationToTest = Mockito.mock(Invocation.class);
        when(invocationToTest.getMethod()).thenReturn(new Method("differentMethod", int.class));

        // then
        assertFalse(matcher.matches(invocationToTest));
    }

    @Test
    public void testInvocationMatcherDoesNotMatchInvocationDifferentMock() {
        // given
        Invocation invocationToMatch = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocationToMatch, Collections.singletonList(is(1)));

        // when
        Invocation invocationToTest = Mockito.mock(Invocation.class);

        // then
        assertFalse(matcher.matches(invocationToTest));
    }

    @Test
    public void testInvocationMatcherDoesNotMatchInvocationDifferentArguments() {
        // given
        Invocation invocationToMatch = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocationToMatch, Collections.singletonList(is(1)));

        // when
        Invocation invocationToTest = Mockito.mock(Invocation.class);
        when(invocationToTest.getMethod()).thenReturn(new Method("test", int.class));
        when(invocationToTest.getArguments()).thenReturn(new Object[]{2});

        // then
        assertFalse(matcher.matches(invocationToTest));
    }

    @Test
    public void testInvocationMatcherDoesNotMatchInvocationWithVarargs() {
        // given
        Invocation invocationToMatch = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocationToMatch);
        Invocation invocationToTest = Mockito.mock(Invocation.class);
        when(invocationToTest.getMethod()).thenReturn(new Method("test", int.class, int[].class));

        // when
        Object[] arguments = new Object[]{1, 2};
        when(invocationToTest.getArguments()).thenReturn(arguments);

        // then
        assertFalse(matcher.matches(invocationToTest));
    }

    @Test
    public void testInvocationMatcherWithCapturedArguments() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        ArgumentCapture<String> stringCaptor = ArgumentCaptor.forClass(String.class);

        // when
        String result = invocationMatcher.captureArgumentsFrom(Mockito.mock(Invocation.class)).get(stringCaptor.capture(0));

        // then
        String expectedResult = "test";
        assertEquals(expectedResult, result);
    }

    @Test
    public void testInvocationMatcherWithVarargsAndVarargMatcher() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(new VarargMatcher<String>(is("test")), isA(String[].class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when
        Invocation invocationToTest = Mockito.mock(Invocation.class);
        when(invocationToTest.getMethod()).thenReturn(new Method("test", int.class, String[].class));
        when(invocationToTest.getArguments()).thenReturn(new Object[]{1, new Object[]{"test1", "test2"}});

        // then
        assertTrue(invocationMatcher.matches(invocationToTest));
    }

    @Test
    public void testInvocationMatcherDoesNotMatchInvocationWithVarargsAndSingleMatcher() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(is("test"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when
        Invocation invocationToTest = Mockito.mock(Invocation.class);
        when(invocationToTest.getMethod()).thenReturn(new Method("test", int.class, String[].class));
        when(invocationToTest.getArguments()).thenReturn(new Object[]{1, new Object[]{"test1", "test2"}});

        // then
        assertFalse(invocationMatcher.matches(invocationToTest));
    }

    @Test
    public void testHasSameMethod() {
        // given
        Invocation invocation1 = Mockito.mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(new Method("test", int.class, int[].class));
        when(invocation1.getArguments()).thenReturn(new Object[]{1, new Object[]{1, 2}});

        Invocation invocation2 = Mockito.mock(Invocation.class);
        when(invocation2.getMethod()).thenReturn(new Method("test", int.class, int[].class));

        // when
        boolean result = new InvocationMatcher(invocation1).hasSameMethod(invocation2);

        // then
        assertTrue(result);
    }

    @Test
    public void testHasSameMethodDifferentMethods() {
        // given
        Invocation invocation1 = Mockito.mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(new Method("test", int.class));

        Invocation invocation2 = Mockito.mock(Invocation.class);
        when(invocation2.getMethod()).thenReturn(new Method("differentTest", int.class));

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // then
        assertFalse(invocationMatcher.hasSameMethod(invocation2));
    }

    @Test
    public void testCreateFrom() {
        // given
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);

        // when
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(Arrays.asList(invocation1, invocation2));

        // then
        assertEquals(2, invocationMatchers.size());
        assertTrue(invocationMatchers.get(0) instanceof InvocationMatcher);
        assertTrue(invocationMatchers.get(1) instanceof InvocationMatcher);
    }

    private void when(Invocation invocation) {
        Stubber stubber = Mockito.mockingDetails().createStubber();
        stubber.stub(invocation.getMethod());
    }
}