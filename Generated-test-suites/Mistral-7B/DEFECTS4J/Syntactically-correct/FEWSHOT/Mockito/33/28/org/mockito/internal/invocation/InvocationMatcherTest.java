package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatchers.*;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.InvocationNotRecognizedException;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.reporting.PrintSettings;

public class InvocationMatcherTest {

    @Test
    public void testEmptyList() {
        Invocation invocation = createMock(Object.class);
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher im = new InvocationMatcher(invocation, matchers);

        Method method = im.getMethod();
        assertEquals(method.getName(), "");
        List<Matcher> matchersFromInvocation = im.getArgumentsToMatchers();
        assertNull(matchersFromInvocation);
    }

    @Test
    public void testSingleMatcher() {
        Invocation invocation = createMock(Object.class);
        Method method = createMockMethod("test", Object.class, int.class);
        Matcher<Integer> intMatcher = is(1);
        List<Matcher> matchers = new LinkedList<Matcher>() {{
            add(intMatcher);
        }};
        InvocationMatcher im = new InvocationMatcher(invocation, matchers);

        assertEquals(method.getName(), "test");
        List<Matcher> matchersFromInvocation = im.getArgumentsToMatchers();
        assertEquals(matchersFromInvocation.size(), 1);
        assertEquals(matchersFromInvocation.get(0), intMatcher);
    }

    @Test
    public void testMultipleMatchers() {
        Invocation invocation = createMock(Object.class);
        Method method = createMockMethod("test", Object.class, int.class, String.class);
        Matcher<Integer> intMatcher = is(1);
        Matcher<String> stringMatcher = is("foo");
        List<Matcher> matchers = new LinkedList<Matcher>() {{
            add(intMatcher);
            add(stringMatcher);
        }};
        InvocationMatcher im = new InvocationMatcher(invocation, matchers);

        assertEquals(method.getName(), "test");
        List<Matcher> matchersFromInvocation = im.getArgumentsToMatchers();
        assertEquals(matchersFromInvocation.size(), 2);
        assertEquals(matchersFromInvocation.get(0), intMatcher);
        assertEquals(matchersFromInvocation.get(1), stringMatcher);
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = createInvocation(Object.class, "test", new Object[] { 1, "foo" });
        InvocationMatcher im1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = createInvocation(Object.class, "test", new Object[] { 1, "foo" });

        List<Matcher> matchers = im1.getMatchers();
        InvocationMatcher im2 = new InvocationMatcher(invocation2, matchers);
        assertTrue(im1.matches(im2));
    }

    @Test
    public void testDoesNotMatchSameMethodDifferentArgs() {
        Invocation invocation1 = createInvocation(Object.class, "test", new Object[] { 1, "foo" });
        Invocation invocation2 = createInvocation(Object.class, "test", new Object[] { 2, "bar" });

        InvocationMatcher im1 = new InvocationMatcher(invocation1);
        InvocationMatcher im2 = new InvocationMatcher(invocation2);
        assertFalse(im1.matches(im2));
    }

    @Test
    public void testDoesNotMatchDifferentMethods() {
        Invocation invocation1 = createInvocation(Object.class, "test1", new Object[] { 1, "foo" });
        Invocation invocation2 = createInvocation(Object.class, "test2", new Object[] { 1, "foo" });

        InvocationMatcher im1 = new InvocationMatcher(invocation1);
        InvocationMatcher im2 = new InvocationMatcher(invocation2);
        assertFalse(im1.matches(im2));
    }

    @Test(expected = InvocationNotRecognizedException.class)
    public void testDoesNotMatchDifferentClasses() {
        Invocation invocation1 = createInvocation(Object.class, "test", new Object[] { 1, "foo" });
        Invocation invocation2 = createInvocation(String.class, "test", new Object[] { 1, "foo" });

        InvocationMatcher im1 = new InvocationMatcher(invocation1);
        InvocationMatcher im2 = new InvocationMatcher(invocation2);
        im1.matches(im2);
    }

    @Test
    public void testToString() {
        Invocation invocation = createInvocation(Object.class, "test", new Object[] { 1, "foo" });
        List<Matcher> matchers = new LinkedList<Matcher>() {{
            add(is(1));
            add(is("foo"));
        }};
        InvocationMatcher im = new InvocationMatcher(invocation, matchers);
        String expected = "InvocationMatcher{invocation=Invocation [this=org.mockito.internal.invocation.MockInvocation@2ea4c321, method=test(I Ljava/lang/String;)(Ljava/lang/Object;I)V, arguments=[1, foo], target=org.mockito.internal.invocation.MockInvocation[this=org.mockito.internal.invocation.MockInvocation@2ea4c321], invocationSite=<not available>], matchers=[ArgumentsMatcher{description=}, ArgumentsMatcher{description=is (1)}], printSettings=PrintSettings{suppressDefaultValues=false, suppressParameterNames=false, suppressBoth=false}}";
        String actual = im.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = createMock(Object.class);
        List<Matcher> matchers = new LinkedList<Matcher>() {{
            add(new ArgumentCaptor<>("arg1"));
        }};
        InvocationMatcher im = new InvocationMatcher(invocation, matchers);
        Object arg = new Object();
        im.captureArgumentsFrom(createInvocation(Object.class, "test", new Object[] { arg }));
        ArgumentCaptor<?> captor = matchers.get(0);
        verify(captor).capture("arg1", eq(arg));
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<Invocation>() {{
            add(createInvocation(Object.class, "test1", new Object[] { 1, "foo" }));
            add(createInvocation(Object.class, "test2", new Object[] { 2, "bar" }));
            add(createInvocation(Object.class, "test3", new Object[] { 3, "baz" }));
        }};
        List<InvocationMatcher> ims = InvocationMatcher.createFrom(invocations);

        assertEquals(ims.size(), 3);
        InvocationMatcher im1 = ims.get(0);
        assertEquals(im1.getInvocation().getMethod().getName(), "test1");
        InvocationMatcher im2 = ims.get(1);
        assertEquals(im2.getInvocation().getMethod().getName(), "test2");
        InvocationMatcher im3 = ims.get(2);
        assertEquals(im3.getInvocation().getMethod().getName(), "test3");
    }

    private Invocation createInvocation(Class<?> clazz, String methodName, Object[] arguments) {
        try {
            Method method = clazz.getMethod(methodName, arguments.getClass());
            return new org.mockito.internal.invocation.MockInvocation(method, arguments);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private Method createMockMethod(String name, Class<?>... parameterTypes) {
        try {
            return Mockito.mock(Class.forName("java.lang.reflect.Method"), new MockitoContext()).method(name, parameterTypes);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Object createMock(Class<?> clazz) {
        return Mockito.mock(clazz);
    }
}