package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithEmptyListOfMatchers() {
        // Given
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher<Object>> emptyMatchers = Collections.emptyList();

        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, emptyMatchers);

        // Then
        assertThat(invocationMatcher, is(notNullValue()));
        assertThat(invocationMatcher.getMatchers(), is(empty()));
        assertThat(invocationMatcher.getMethod(), is(notNullValue()));
    }

    @Test
    public void testInvocationMatcherWithListOfMatchers() {
        // Given
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher<Object>> matchers = Arrays.asList(Matchers.eq("test"), Matchers.instanceOf(String.class));

        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Then
        assertThat(invocationMatcher, is(notNullValue()));
        assertThat(invocationMatcher.getMatchers(), is(matchers));
        assertThat(invocationMatcher.getMethod(), is(notNullValue()));

        // Test toString() method
        String invocationMatcherToString = invocationMatcher.toString();
        String expectedString = "Invocation [method=[public void org.mockito.example.TestClass.testMethod(java.lang.String, java.lang.String)], arguments=[arg0=test, arg1=<instance of java.lang.String>]]";
        assertThat(invocationMatcherToString, is(sameInstance(expectedString)));
    }

    @Test
    public void testMatchesWithSameMethodAndArguments() {
        // Given
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Matcher<Object>> matchers = Arrays.asList(Matchers.eq("test"), Matchers.instanceOf(String.class));

        Mockito.when(invocation1.getMock()).thenReturn(Mockito.mock(Object.class));
        Mockito.when(invocation1.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(invocation1.getArguments()).thenReturn(new Object[]{new String("test1"), new String("test2")});

        Mockito.when(invocation2.getMock()).thenReturn(Mockito.mock(Object.class));
        Mockito.when(invocation2.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(invocation2.getArguments()).thenReturn(new Object[]{new String("test1"), new String("test2")});

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers);

        // When
        boolean result = invocationMatcher1.matches(invocationMatcher2);

        // Then
        assertThat(result, is(true));
    }

    @Test
    public void testMatchesWithDifferentMethods() {
        // Given
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Matcher<Object>> matchers = Arrays.asList(Matchers.eq("test"), Matchers.instanceOf(String.class));

        Mockito.when(invocation1.getMock()).thenReturn(Mockito.mock(Object.class));
        Mockito.when(invocation1.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(invocation1.getArguments()).thenReturn(new Object[]{new String("test1")});

        Mockito.when(invocation2.getMock()).thenReturn(Mockito.mock(Object.class));
        Mockito.when(invocation2.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(invocation2.getArguments()).thenReturn(new Object[]{new String("test2")});

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers);

        // When
        boolean result = invocationMatcher1.matches(invocationMatcher2);

        // Then
        assertThat(result, is(false));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        // Given
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher<Object>> matchers = Arrays.asList(new ArgumentCaptor<String>(), Matchers.instanceOf(String.class));

        Mockito.when(invocation.getMock()).thenReturn(Mockito.mock(Object.class));
        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{"test1", "test2"});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // When
        invocationMatcher.captureArgumentsFrom(invocation);

        // Then
        ArgumentCaptor<String> argumentCaptor = invocationMatcher.getMatchers().get(0);
        String capturedString1 = (String) argumentCaptor.getValue();
        assertThat(capturedString1, is("test1"));

        ArgumentCaptor<String> argumentCaptor2 = invocationMatcher.getMatchers().get(1);
        String capturedString2 = (String) argumentCaptor2.getValue();
        assertThat(capturedString2, is("test2"));
    }

    @Test(expected = RuntimeException.class)
    public void testCaptureArgumentsFromInvocationWithVarargs() {
        // Given
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher<Object>> matchers = Arrays.asList(new ArgumentCaptor<String>(), Matchers.instanceOf(String[].class));

        Mockito.when(invocation.getMock()).thenReturn(Mockito.mock(Object.class));
        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{"test1", new String[]{"test3", "test4"}});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // When
        invocationMatcher.captureArgumentsFrom(invocation);

        // Then
        ArgumentCaptor<String> argumentCaptor = invocationMatcher.getMatchers().get(0);
        String capturedString1 = (String) argumentCaptor.getValue();
        assertThat(capturedString1, is("test1"));

        ArgumentCaptor<String[]> argumentCaptor2 = invocationMatcher.getMatchers().get(1);
        String[] capturedStrings = (String[]) argumentCaptor2.getValue();
        assertThat(capturedStrings, is(Arrays.asList(new String[]{"test3", "test4"})));
    }
}