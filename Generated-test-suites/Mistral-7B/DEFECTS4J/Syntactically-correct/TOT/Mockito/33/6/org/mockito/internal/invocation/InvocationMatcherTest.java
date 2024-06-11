package org.mockito.internal.invocation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgMatchers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Stubber;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvocationMatcherTest {

    private Mockito mockito;
    private Object mock;

    @Before
    public void setup() {
        mockito = new Mockito();
        mock = mockito.mock(Object.class);
    }

    @Test
    public void testNewInvocationMatcherWithMatchers() {
        // given
        Stubber stubber = mockito.stubbing();
        stubber.given(mock).method("testMethod", ArgMatchers.eq("expectedValue"));

        // when
        Invocation invocation = new Invocation(mockito, new Method("testMethod", new Class[0]), new Object[0]);
        List<Matcher> matchers = Arrays.asList(ArgMatchers.eq("expectedValue"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // then
        assertTrue(invocationMatcher.matches(new InvocationOnMock(mockito, invocation)));
    }

    @Test
    public void testNewInvocationMatcherWithoutMatchers() {
        // given
        Invocation invocation = new Invocation(mockito, new Method("testMethod", new Class[0]), new Object[0]);

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        assertFalse(invocationMatcher.matches(new InvocationOnMock(mockito, new Invocation(mockito, new Method("testMethod2", new Class[0]), new Object[0]))));
    }

    @Test
    public void testHasSimilarMethod() {
        // given
        Invocation invocation1 = new Invocation(mockito, new Method("testMethod", new Class[0]), new Object[0]);
        Invocation invocation2 = new Invocation(mockito, new Method("testMethod", new Class[0]), new Object[0]);

        // when
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // then
        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethod_differentMethods() {
        // given
        Invocation invocation1 = new Invocation(mockito, new Method("testMethod1", new Class[0]), new Object[0]);
        Invocation invocation2 = new Invocation(mockito, new Method("testMethod2", new Class[0]), new Object[0]);

        // when
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // then
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethod_unverifiedInvocation() {
        // given
        Invocation invocation1 = new Invocation(mockito, new Method("testMethod", new Class[0]), new Object[0]);
        Invocation invocation2 = new Invocation(mockito, new Method("testMethod", new Class[0]), new Object[0], true);

        // when
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // then
        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethod() {
        // given
        Invocation invocation1 = new Invocation(mockito, new Method("testMethod", new Class[0]), new Object[0]);
        Invocation invocation2 = new Invocation(mockito, new Method("testMethod", new Class[0]), new Object[0]);

        // when
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // then
        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethod_differentInvocations() {
        // given
        Invocation invocation1 = new Invocation(mockito, new Method("testMethod1", new Class[0]), new Object[0]);
        Invocation invocation2 = new Invocation(mockito, new Method("testMethod2", new Class[0]), new Object[0]);

        // when
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // then
        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        // given
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        stubber.when(mock.testMethod(ArgMatchers.eq("arg1"), stringArgumentCaptor.capture())).thenReturn("result");

        // when
        invocation.callRealMethod(mock, "arg1", "arg2");

        // then
        assertEquals("arg2", stringArgumentCaptor.getValue());
    }
}