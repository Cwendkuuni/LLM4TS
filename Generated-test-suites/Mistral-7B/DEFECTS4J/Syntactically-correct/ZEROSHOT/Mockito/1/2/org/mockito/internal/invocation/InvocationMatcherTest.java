package org.mockito.internal.invocation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.ArgumentsProcessor;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testCreateWithMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher<?>> matchers = new LinkedList<>();
        matchers.add(is(new Object()));
        matchers.add(isA(String.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), equalTo(matchers));
    }

    @Test
    public void testCreateWithoutMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), is(Collections.emptyList()));
    }

    @Test
    public void testGetMethod() {
        Method method = Mockito.mock(Method.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        invocation.setMethod(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertThat(invocationMatcher.getMethod(), is(method));
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertThat(invocationMatcher.getInvocation(), sameInstance(invocation));
    }

    @Test
    public void testGetMatchers() {
        Object arg1 = new Object();
        Object arg2 = new Object();
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher<?>> matchers = new LinkedList<>();
        matchers.add(is(arg1));
        matchers.add(is(arg2));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertThat(invocationMatcher.getMatchers(), equalTo(matchers));
    }

    @Test
    public void testToString() {
        Object arg1 = new Object();
        Object arg2 = new Object();
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher<?>> matchers = new LinkedList<>();
        matchers.add(is(arg1));
        matchers.add(is(arg2));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String expected = "InvocationMatcher{invocation=InvocationMock, matchers=[ArgumentMatcher[is](Object), ArgumentMatcher[is](Object)]}";

        assertThat(invocationMatcher.toString(), equalTo(expected));
    }

    @Test
    public void testMatchesWithSameInvocation() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        boolean result = invocationMatcher.matches(invocation1);
        assertTrue(result);

        result = invocationMatcher.matches(invocation2);
        assertFalse(result);
    }

    @Test
    public void testMatchesWithDifferentInvocationButSameMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        invocation1.setMethod(method);
        invocation2.setMethod(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        boolean result = invocationMatcher.matches(invocation2);
        assertTrue(result);
    }

    @Test
    public void testMatchesWithDifferentInvocationAndDifferentMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        invocation1.setMethod(method1);
        invocation2.setMethod(method2);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        boolean result = invocationMatcher.matches(invocation2);
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethodWithSameInvocation() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        invocation1.setInvocation(invocation1);
        invocation1.setMethod(method);
        invocation2.setInvocation(invocation2);
        invocation2.setMethod(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        boolean result = invocationMatcher.hasSimilarMethod(invocation2);
        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethodWithDifferentInvocationButSameMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        invocation1.setMethod(method);
        invocation2.setMethod(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        boolean result = invocationMatcher.hasSimilarMethod(invocation2);
        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethodWithDifferentInvocationAndDifferentMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        invocation1.setInvocation(invocation1);
        invocation1.setMethod(method1);
        invocation2.setInvocation(invocation2);
        invocation2.setMethod(method2);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        boolean result = invocationMatcher.hasSimilarMethod(invocation2);
        assertFalse(result);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Object arg1 = new Object();
        Object arg2 = new Object();
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher<?>> matchers = new LinkedList<>();
        matchers.add(new ArgumentsMatcher() {
            @Override
            public boolean matches(Object actual) {
                if (actual instanceof Object[]) {
                    Object[] args = (Object[]) actual;
                    return args[0] instanceof CapturesArguments
                            && (((CapturesArguments) args[0]).getCapturedValue() instanceof Object)
                            && args[1].equals(arg1);
                }
                return false;
            }
        });
        matchers.add(new CapturesArguments() {
            @Override
            public void captureFrom(Object value) {
                assertEquals(arg2, value);
            }
        });
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Object[] args = new Object[]{arg1, arg2};
        invocationMatcher.captureArgumentsFrom(Mockito.mock(Invocation.class, new Object[]{arg1, arg2}));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromVarargs() {
        Object[] args = new Object[]{new Object[2]};
        InvocationMatcher invocationMatcher = new InvocationMatcher(Mockito.mock(Invocation.class), new LinkedList<>());

        invocationMatcher.captureArgumentsFrom(Mockito.mock(Invocation.class, args));
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        List<InvocationMatcher> result = InvocationMatcher.createFrom(Arrays.asList(invocationMatcher1, invocationMatcher2));

        assertThat(result, hasSize(2));
        assertThat(result.get(0), is(invocationMatcher1));
        assertThat(result.get(1), is(invocationMatcher2));
    }

    private boolean hasSize(int size) {
        return size > 0;
    }
}