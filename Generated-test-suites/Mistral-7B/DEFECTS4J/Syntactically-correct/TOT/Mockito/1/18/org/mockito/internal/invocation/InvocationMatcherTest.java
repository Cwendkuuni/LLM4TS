package org.mockito.internal.invocation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stub.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.nullable;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testSimpleMatcher() {
        Object mock = mock(SomeClass.class);
        Invocation invocation = new Invocation(mock, new Method("someMethod", Integer.TYPE), new Object[]{1});
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertThat(invocationMatcher.matches(invocation), is(true));
    }

    @Test
    public void testMultipleMatchers() {
        Object mock = mock(SomeClass.class);
        Invocation invocation = new Invocation(mock, new Method("someMethod", Integer.TYPE, String.class), new Object[]{1, "test"});

        List<Matcher> matchers = Arrays.asList(
                ArgumentMatchers.argThat(new MyCondition()),
                ArgumentMatchers.argThat(new MyStringMatcher()));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        given(mock.someMethod((Integer) ArgumentMatchers.anyInt(), ArgumentMatchers.anyString())).willReturn(42);

        int result = invocationMatcher.getInvocation().invoke();

        then(mock).should().someMethod((Integer) ArgumentMatchers.eq(1), ArgumentMatchers.eq("test"));
        verify(mock).someMethod((Integer) ArgumentMatchers.eq(1), ArgumentMatchers.eq("test"));

        assertThat(result, is(42));
    }

    @Test
    public void testNoMatchers() {
        Object mock = mock(SomeClass.class);
        Invocation invocation = new Invocation(mock, new Method("someMethod"), new Object[0]);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        given(mock.someMethod()).willReturn(42);

        int result = invocationMatcher.getInvocation().invoke();

        then(mock).should().someMethod();
        verify(mock).someMethod();

        assertThat(result, is(42));
    }

    @Test
    public void testMatchesInvocationWithDifferentMethod() {
        Object mock = mock(SomeClass.class);
        Invocation invocation1 = new Invocation(mock, new Method("someMethod1", Integer.TYPE), new Object[]{1});
        List<Matcher> matchers1 = Arrays.asList(ArgumentMatchers.eq(1));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers1);

        Object mock2 = mock(SomeClass.class);
        Invocation invocation2 = new Invocation(mock2, new Method("someMethod2", Integer.TYPE), new Object[]{1});

        assertThat(invocationMatcher1.matches(invocation2), is(false));
    }

    @Test
    public void testMatchesInvocationWithSameMethodButDifferentArguments() {
        Object mock = mock(SomeClass.class);
        Invocation invocation1 = new Invocation(mock, new Method("someMethod", Integer.TYPE), new Object[]{1});
        List<Matcher> matchers1 = Arrays.asList(ArgumentMatchers.eq(2));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers1);

        Object mock2 = mock(SomeClass.class);
        Invocation invocation2 = new Invocation(mock2, new Method("someMethod", Integer.TYPE), new Object[]{2});

        assertThat(invocationMatcher1.matches(invocation2), is(false));
    }

    @Test
    public void testMatchesInvocationWithSameMethodAndArguments() {
        Object mock = mock(SomeClass.class);
        Invocation invocation = new Invocation(mock, new Method("someMethod", Integer.TYPE), new Object[]{1});
        List<Matcher> matchers = Collections.singletonList(ArgumentMatchers.eq(1));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertThat(invocationMatcher.matches(invocation), is(true));
    }

    @Test
    public void testMatchesInvocationWithVarArgs() {
        Object mock = mock(SomeClass.class);
        Invocation invocation = new Invocation(mock, new Method("someMethod", Integer.TYPE, Integer[].class), new Object[]{1, new int[]{2, 3}});
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.argThat(new VarargMatcher<Integer>(Arrays.asList(2, 3))));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        given(mock.someMethod((Integer) ArgumentMatchers.anyInt(), ArgumentMatchers.vararg(Arrays.asList(2, 3)))).willReturn(42);

        int result = invocationMatcher.getInvocation().invoke();

        then(mock).should().someMethod((Integer) ArgumentMatchers.eq(1), ArgumentMatchers.args(Arrays.asList(2, 3)));
        verify(mock).someMethod((Integer) ArgumentMatchers.eq(1), ArgumentMatchers.args(Arrays.asList(2, 3)));

        assertThat(result, is(42));
    }

    @Test
    public void testMatchesInvocationWithVarArgsButDifferentArguments() {
        Object mock = mock(SomeClass.class);
        Invocation invocation = new Invocation(mock, new Method("someMethod", Integer.TYPE, Integer[].class), new Object[]{1, new int[]{2, 3}});
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.argThat(new VarargMatcher<Integer>(Arrays.asList(1, 3))));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        given(mock.someMethod((Integer) ArgumentMatchers.anyInt(), ArgumentMatchers.vararg(Arrays.asList(2, 3, 4)))).willReturn(42);

        int result = invocationMatcher.getInvocation().invoke();

        then(mock).shouldNotHaveBeenInvoked();
        verify(mock).someMethod((Integer) ArgumentMatchers.eq(1), ArgumentMatchers.args(Arrays.asList(1, 3)));

        assertThat(result, is(nullValue()));
    }

    @Test
    public void testMatchesInvocationWithVarArgsButSameArgumentsButDifferentOrder() {
        Object mock = mock(SomeClass.class);
        Invocation invocation = new Invocation(mock, new Method("someMethod", Integer.TYPE, Integer[].class), new Object[]{1, new int[]{3, 2}});
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.argThat(new VarargMatcher<Integer>(Arrays.asList(2, 3))));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        given(mock.someMethod((Integer) ArgumentMatchers.anyInt(), ArgumentMatchers.vararg(Arrays.asList(3, 2)))).willReturn(42);

        int result = invocationMatcher.getInvocation().invoke();

        then(mock).should().someMethod((Integer) ArgumentMatchers.eq(1), ArgumentMatchers.args(Arrays.asList(2, 3)));
        verify(mock).someMethod((Integer) ArgumentMatchers.eq(1), ArgumentMatchers.args(Arrays.asList(2, 3)));

        assertThat(result, is(42));
    }

    private static class MyCondition implements ArgumentMatcher<Object> {
        @Override
        public boolean matches(Object argument) {
            return argument instanceof Integer && ((Integer) argument).intValue() > 0;
        }
    }

    private static class MyStringMatcher implements ArgumentMatcher<String> {
        @Override
        public boolean matches(String argument) {
            return "test".equals(argument);
        }
    }

    private static class SomeClass {
        int someMethod(int arg) {
            return 42;
        }

        int someMethod(int arg1, int... args2) {
            return 42;
        }
    }
}