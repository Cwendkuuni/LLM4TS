package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.argumentsWithTypes;
import static org.mockito.internal.matchers.ArgumentsMatcher.capture;
import static org.mockito.internal.matchers.ArgumentsMatcher.isNull;
import static org.mockito.internal.matchers.ArgumentsMatcher.isInstanceOf;
import static org.mockito.internal.matchers.ArgumentsMatcher.isA;
import static org.mockito.internal.matchers.ArgumentsMatcher.isAnyString;
import static org.mockito.internal.matchers.ArgumentsMatcher.not;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructionWithInvocationAndEmptyMatchersList() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        assertNotNull(invocationMatcher);
        assertTrue(invocationMatcher instanceof InvocationMatcher);
        assertNull(invocationMatcher.getMatchers());
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testInvocationMatcherConstructionWithInvocationAndNonEmptyMatchersList() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(isAnyString(), isNull());

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // then
        assertNotNull(invocationMatcher);
        assertTrue(invocationMatcher instanceof InvocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testInvocationMatcherMatchesInvocationWithSameMethodAndSameArguments() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Collections.singletonList(isAnyString());

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation invocationToMatch = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        when(method.getName()).thenReturn("testMethod");
        when(invocationToMatch.getMethod()).thenReturn(method);
        when(invocationToMatch.getArguments()).thenReturn(new Object[]{ "stringArgument" });

        // when
        boolean matches = invocationMatcher.matches(invocationToMatch);

        // then
        assertTrue(matches);
    }

    @Test
    public void testInvocationMatcherDoesNotMatchInvocationWithDifferentMethod() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Collections.singletonList(isAnyString());

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation invocationToMatch = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        when(method.getName()).thenReturn("differentMethod");
        when(invocationToMatch.getMethod()).thenReturn(method);
        when(invocationToMatch.getArguments()).thenReturn(new Object[]{ "stringArgument" });

        // when
        boolean matches = invocationMatcher.matches(invocationToMatch);

        // then
        assertFalse(matches);
    }

    @Test
    public void testInvocationMatcherDoesNotMatchInvocationWithDifferentMock() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Collections.singletonList(isAnyString());

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation invocationToMatch = Mockito.mock(InvocationOnMock.class);
        when(invocationToMatch.getMethod()).thenReturn(invocation.getMethod());
        when(invocationToMatch.getArguments()).thenReturn(invocation.getArguments());
        when(invocationToMatch.getMock()).thenReturn(Mockito.mock(Object.class));

        // when
        boolean matches = invocationMatcher.matches(invocationToMatch);

        // then
        assertFalse(matches);
    }

    @Test
    public void testInvocationMatcherDoesNotMatchInvocationWithDifferentArguments() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(isAnyString(), isInstanceOf(Integer.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation invocationToMatch = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);
        when(method.getName()).thenReturn("testMethod");
        when(invocationToMatch.getMethod()).thenReturn(method);
        when(invocationToMatch.getArguments()).thenReturn(new Object[]{ "stringArgument", 1 });

        // when
        boolean matches = invocationMatcher.matches(invocationToMatch);

        // then
        assertFalse(matches);
    }

    @Test
    public void testInvocationMatcherMatchesInvocationWithOverloadedMethodButSameArguments() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(isAnyString(), isInstanceOf(Integer.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation invocationToMatch = Mockito.mock(InvocationOnMock.class);
        Method method1 = Mockito.mock(Method.class);
        when(method1.getName()).thenReturn("testMethod1");
        when(method1.getParameterTypes()).thenReturn(new Class[]{ String.class, Integer.TYPE });
        when(invocationToMatch.getMethod()).thenReturn(method1);
        when(invocationToMatch.getArguments()).thenReturn(new Object[]{ "stringArgument", 1 });

        Method method2 = Mockito.mock(Method.class);
        when(method2.getName()).thenReturn("testMethod2");
        when(method2.getParameterTypes()).thenReturn(new Class[]{ String.class });
        when(invocation.getMethod()).thenReturn(method2);

        // when
        boolean matches = invocationMatcher.matches(invocationToMatch);

        // then
        assertTrue(matches);
    }

    @Test
    public void testInvocationMatcherDoesNotMatchInvocationWithOverloadedMethodButDifferentArguments() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(isAnyString(), isInstanceOf(Integer.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation invocationToMatch = Mockito.mock(InvocationOnMock.class);
        Method method1 = Mockito.mock(Method.class);
        when(method1.getName()).thenReturn("testMethod1");
        when(method1.getParameterTypes()).thenReturn(new Class[]{ String.class, Integer.TYPE });
        when(invocationToMatch.getMethod()).thenReturn(method1);
        when(invocationToMatch.getArguments()).thenReturn(new Object[]{ "stringArgument2", 2 });

        Method method2 = Mockito.mock(Method.class);
        when(method2.getName()).thenReturn("testMethod2");
        when(method2.getParameterTypes()).thenReturn(new Class[]{ String.class });
        when(invocation.getMethod()).thenReturn(method2);

        // when
        boolean matches = invocationMatcher.matches(invocationToMatch);

        // then
        assertFalse(matches);
    }

    @Test
    public void testInvocationMatcherDoesNotMatchInvocationWithDifferentLocation() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Location location1 = Mockito.mock(Location.class);
        when(location1.getFileName()).thenReturn("testFile1.java");
        when(location1.getLineNumber()).thenReturn(1);
        when(invocation.getLocation()).thenReturn(location1);

        List<Matcher> matchers = Collections.singletonList(isAnyString());

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Location location2 = Mockito.mock(Location.class);
        when(location2.getFileName()).thenReturn("testFile2.java");
        when(location2.getLineNumber()).thenReturn(2);
        when(invocationToMatch.getLocation()).thenReturn(location2);

        // when
        boolean matches = invocationMatcher.matches(invocationToMatch);

        // then
        assertFalse(matches);
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(capture(ArgumentCaptor.forClass(String.class)), capture(ArgumentCaptor.forClass(Integer.class)));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String stringArgument = "foo";
        Integer integerArgument = 1;

        // when
        invocationMatcher.captureArgumentsFrom(invocation);

        // then
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> integerCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(invocation).getArgumentAt(0, String.class);
        verify(invocation).getArgumentAt(1, Integer.class);

        verify(stringCaptor.getValue()).equals(stringArgument);
        verify(integerCaptor.getValue()).equals(integerArgument);
    }

    @Test
    public void testCreateFromInvocations() {
        // given
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation3 = Mockito.mock(InvocationOnMock.class);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(Arrays.asList(invocation1, invocation2, invocation3));

        // then
        assertEquals(3, invocationMatchers.size());
        assertTrue(invocationMatchers.get(0) instanceof InvocationMatcher);
        assertTrue(invocationMatchers.get(1) instanceof InvocationMatcher);
        assertTrue(invocationMatchers.get(2) instanceof InvocationMatcher);
    }

}