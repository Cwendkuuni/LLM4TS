package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.argumentsWithTheSameTypesAs;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.reflection.Whitebox;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithEmptyMatcherList() {
        Invocation invocation = createMockInvocation(new Object() {
            Method method = new Method(new Object() {}.getClass(), "someMethod", new Class[0]);
        }.getClass(), "someMethod");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());

        Method method = invocationMatcher.getMethod();
        assertEquals(invocation.getMethod(), method);

        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertTrue(matchers.isEmpty());
    }

    @Test
    public void testInvocationMatcherWithMatchers() {
        Object[] arguments = new Object[]{"arg1", 1, new Object() {}};
        Invocation invocation = createMockInvocation(new Object() {
            Method method = new Method(new Object() {}.getClass(), "someMethod", new Class[]{String.class, Integer.TYPE, Object.class});
        }.getClass(), "someMethod", arguments);

        List<Matcher> matchers = Arrays.asList(
                Matchers.eq("arg1"),
                Matchers.eq(1),
                Matchers.instanceOf(Object.class)
        );

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testInvocationMatcherWithNullInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(null, Collections.emptyList());
        assertNull(invocationMatcher);
    }

    @Test
    public void testInvocationMatcherWithNullMatcherList() {
        Invocation invocation = createMockInvocation(new Object() {
            Method method = new Method(new Object() {}.getClass(), "someMethod", new Class[0]);
        }.getClass(), "someMethod");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, null);
        assertNull(invocationMatcher);
    }

    @Test
    public void testInvocationMatcherWithSameMethodDifferentArguments() {
        Object[] arguments1 = new Object[]{"arg1"};
        Object[] arguments2 = new Object[]{"arg2"};
        Invocation invocation1 = createMockInvocation(new Object() {
            Method method = new Method(new Object() {}.getClass(), "someMethod", new Class[]{String.class});
        }.getClass(), "someMethod", arguments1);
        Invocation invocation2 = createMockInvocation(new Object() {
            Method method = new Method(new Object() {}.getClass(), "someMethod", new Class[]{String.class});
        }.getClass(), "someMethod", arguments2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertFalse(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testInvocationMatcherWithDifferentMethods() {
        Object[] arguments = new Object[]{"arg1"};
        Invocation invocation1 = createMockInvocation(new Object() {
            Method method1 = new Method(new Object() {}.getClass(), "someMethod1", new Class[]{String.class});
        }.getClass(), "someMethod1", arguments);
        Invocation invocation2 = createMockInvocation(new Object() {
            Method method2 = new Method(new Object() {}.getClass(), "someMethod2", new Class[0]);
        }.getClass(), "someMethod2");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertFalse(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testInvocationMatcherWithSameMethodSameArguments() {
        Object[] arguments = new Object[]{"arg1"};
        Invocation invocation1 = createMockInvocation(new Object() {
            Method method = new Method(new Object() {}.getClass(), "someMethod", new Class[]{String.class});
        }.getClass(), "someMethod", arguments);
        Invocation invocation2 = createMockInvocation(new Object() {
            Method method = new Method(new Object() {}.getClass(), "someMethod", new Class[]{String.class});
        }.getClass(), "someMethod", arguments);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertTrue(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testInvocationMatcherWithSameMethodSameArgumentsButDifferentOrder() {
        Object[] arguments = new Object[]{"arg1", "arg2"};
        Invocation invocation1 = createMockInvocation(new Object() {
            Method method = new Method(new Object() {}.getClass(), "someMethod", new Class[]{String.class, String.class});
        }.getClass(), "someMethod", arguments);
        Invocation invocation2 = createMockInvocation(new Object() {
            Method method = new Method(new Object() {}.getClass(), "someMethod", new Class[]{String.class, String.class});
        }.getClass(), "someMethod", new Object[]{arguments[1], arguments[0]});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertTrue(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testInvocationMatcherWithDifferentMethodsButSameArguments() {
        Object[] arguments = new Object[]{"arg1"};
        Invocation invocation1 = createMockInvocation(new Object() {
            Method method1 = new Method(new Object() {}.getClass(), "someMethod1", new Class[]{String.class});
        }.getClass(), "someMethod1", arguments);
        Invocation invocation2 = createMockInvocation(new Object() {
            Method method2 = new Method(new Object() {}.getClass(), "someMethod2", new Class[0]);
        }.getClass(), "someMethod2", arguments);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertFalse(invocationMatcher.matches(invocation2));

        InvocationMatcher invocationMatcherWithMatchers = new InvocationMatcher(invocation1, argumentsWithTheSameTypesAs(arguments));

        assertTrue(invocationMatcherWithMatchers.matches(invocation2));
    }

    @Test
    public void testToString() {
        Object[] arguments = new Object[]{"arg1"};
        Invocation invocation = createMockInvocation(new Object() {
            Method method = new Method(new Object() {}.getClass(), "someMethod", new Class[]{String.class});
        }.getClass(), "someMethod", arguments);

        List<Matcher> matchers = Arrays.asList(
                Matchers.eq("arg1")
        );

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String toString = invocationMatcher.toString();

        String expected = String.format("InvocationMatcher[invocation=%s, method=%s, matchers=%s]", invocation, invocation.getMethod(), matchers);

        assertEquals(expected, toString);
    }

    private Invocation createMockInvocation(Object object, String methodName, Object... arguments) {
        return Whitebox.getInternalState(Mockito.mock(object.getClass()), "invocations").get(0);
    }

    private void assertEquals(Method expected, Method actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getParameterTypes(), actual.getParameterTypes());
    }
}