package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.contains;
import static org.mockito.internal.matchers.ArgumentsMatcher.eq;
import static org.mockito.internal.matchers.ArgumentsMatcher.isNull;
import static org.mockito.internal.matchers.ArgumentsMatcher.notNull;
import static org.mockito.internal.matchers.ArgumentsMatcher.sameInstance;
import static org.mockito.internal.matchers.ArgumentsMatcher.type;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;

public class InvocationMatcherTest {

    private Object mock;

    @Before
    public void setUp() {
        mock = Mockito.mock(Object.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNewInstance() {
        Method method = Mockito.method(Object.class, new Class[]{}, Void.TYPE);
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(eq(mock), isNull());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertTrue(invocationMatcher.matches(invocation));
        assertEquals(method, invocationMatcher.getMethod());
        assertEquals(matchers, invocationMatcher.getMatchers());
        assertEquals(mock, invocationMatcher.getInvocation().getMock());
        assertNotNull(invocationMatcher.getLocation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNewInstanceWithEmptyMatcherList() {
        Method method = Mockito.method(Object.class, new Class[]{}, Void.TYPE);
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.matches(invocation));
        assertEquals(method, invocationMatcher.getMethod());
        assertNotNull(invocationMatcher.getMatchers());
        assertEquals(mock, invocationMatcher.getInvocation().getMock());
        assertNull(invocationMatcher.getLocation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNewInstanceWithMatcherList() {
        Method method = Mockito.method(Object.class, new Class[]{Object.class, int.class}, Void.TYPE);
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(eq(mock), eq(1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Object[] arguments = new Object[]{mock, 1};
        Invocation candidate = Mockito.mock(Invocation.class);
        Mockito.when(candidate.getArguments()).thenReturn(arguments);

        assertTrue(invocationMatcher.matches(invocation));
        assertTrue(invocationMatcher.matches(candidate));
        assertEquals(method, invocationMatcher.getMethod());
        assertEquals(mock, invocationMatcher.getInvocation().getMock());
        assertNotNull(invocationMatcher.getLocation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNewInstanceWithMatcherListAndVarArgs() {
        Method method = Mockito.method(Object.class, new Class[]{Object.class, Object[].class}, Void.TYPE);
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(eq(mock), contains(isNull()), contains(notNull()));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Object[] arguments = new Object[]{mock, new Object[]{null, new Integer(1)}};
        Invocation candidate = Mockito.mock(Invocation.class);
        Mockito.when(candidate.getArguments()).thenReturn(arguments);

        assertTrue(invocationMatcher.matches(invocation));
        assertTrue(invocationMatcher.matches(candidate));
        assertEquals(method, invocationMatcher.getMethod());
        assertEquals(mock, invocationMatcher.getInvocation().getMock());
        assertNotNull(invocationMatcher.getLocation());
    }

    @Test
    public void testHasSimilarMethod() {
        Method method1 = Mockito.method(Object.class, new Class[]{Object.class}, Void.TYPE);
        Method method2 = Mockito.method(Object.class, new Class[]{Integer.class}, Void.TYPE);

        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertFalse(invocationMatcher.hasSimilarMethod(new InvocationMatcher(invocation2, Arrays.asList(eq(mock)))));
        assertFalse(invocationMatcher.hasSimilarMethod(new InvocationMatcher(invocation2, Collections.singletonList(eq(method2)))));
        assertFalse(invocationMatcher.hasSimilarMethod(new InvocationMatcher(invocation2, Arrays.asList(eq(mock), eq(method2)))));
        assertTrue(invocationMatcher.hasSimilarMethod(new InvocationMatcher(invocation1)));
        assertTrue(invocationMatcher.hasSimilarMethod(new InvocationMatcher(invocation1, Collections.singletonList(eq(method1)))));
        assertTrue(invocationMatcher.hasSimilarMethod(new InvocationMatcher(invocation1, Arrays.asList(eq(mock), eq(method1)))));
    }

    @Test(expected = RuntimeException.class)
    public void testSafelyArgumentsMatchThrowsException() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(Mockito.mock(Invocation.class));
        invocationMatcher.safelyArgumentsMatch(new Object[]{"not an array"});
    }

    @Test
    public void testToString() {
        Method method = Mockito.method(Object.class, new Class[]{Object.class}, Void.TYPE);
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(eq(mock), eq(1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String expected = "InvocationMatcher:\narguments:\n  [<org.mockito.internal.matchers.ArgumentsMatcher: matcher #0>\n    equals actual\n    <org.hamcrest.Matcher: equals>\n      matches <" + mock + ">\n  ]\nmethod:\n  <" + method.getName() + " " + method.getReturnType().getName() + " " + Arrays.toString(method.getParameterTypes()) + ">\n";
        assertEquals(expected, invocationMatcher.toString());
    }

    @After
    public void tearDown() {
        Mockito.verifyNoMoreInteractions(mock);
    }
}