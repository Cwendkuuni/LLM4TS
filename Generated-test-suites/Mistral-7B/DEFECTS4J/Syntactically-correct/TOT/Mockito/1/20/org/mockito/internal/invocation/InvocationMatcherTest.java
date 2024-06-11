package org.mockito.internal.invocation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.quality.QualityChecker;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.hamcrest.CoreMatchers.isNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher_happyPath() {
        Object mock = mock(SomeClass.class);
        Object arg1 = new Object();
        Object arg2 = new Object();

        Method method = Mockito.method(SomeClass.class, "someMethod", new Class[]{Integer.class, String.class});
        Invocation invocation = Mockito.on(mock).invoke(method, 1, arg1, arg2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> expectedMatchers = Arrays.asList(
                argThat(isA(Integer.class)),
                isNull(),
                argThat(isA(String.class))
        );

        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        assertThat(actualMatchers, is(equalTo(expectedMatchers)));
    }

    @Test
    public void testInvocationMatcher_unverifiedInvocation() {
        Object mock = mock(SomeClass.class);
        Object arg1 = new Object();
        Object arg2 = new Object();

        Method method = Mockito.method(SomeClass.class, "someMethod", new Class[]{Integer.class, String.class});
        Invocation invocation = Mockito.on(mock).invoke(method, 1, arg1, arg2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> expectedMatchers = Collections.emptyList();

        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        assertThat(actualMatchers, is(equalTo(expectedMatchers)));
    }

    @Test
    public void testInvocationMatcher_unmatchedInvocation() {
        Object mock = mock(SomeClass.class);
        Object arg1 = new Object();
        Object arg2 = new Object();

        Object otherMock = mock(AnotherClass.class);
        Object otherArg1 = new Object();
        Object otherArg2 = new Object();

        Method method = Mockito.method(SomeClass.class, "someMethod", new Class[]{Integer.class, String.class});
        Method otherMethod = Mockito.method(AnotherClass.class, "someOtherMethod", new Class[]{Integer.class, String.class});
        Invocation invocation = Mockito.on(mock).invoke(method, 1, arg1, arg2);
        Invocation otherInvocation = Mockito.on(otherMock).invoke(otherMethod, 1, otherArg1, otherArg2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        InvocationMatcher otherInvocationMatcher = new InvocationMatcher(otherInvocation);

        List<Matcher> expectedMatchers = Collections.emptyList();

        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        List<Matcher> otherActualMatchers = otherInvocationMatcher.getMatchers();

        List<Matcher> allMatchers = new LinkedList<>(actualMatchers);
        allMatchers.addAll(otherActualMatchers);

        assertThat(allMatchers, is(not(equalTo(expectedMatchers))));
    }

    @Test
    public void testInvocationMatcher_matchesInvocation() {
        Object mock = mock(SomeClass.class);
        Object arg1 = new Object();
        Object arg2 = new Object();

        Method method = Mockito.method(SomeClass.class, "someMethod", new Class[]{Integer.class, String.class});
        Invocation invocation = Mockito.on(mock).invoke(method, 1, arg1, arg2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        given(mock.someMethod(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString())).willReturn(new Object());

        invocationMatcher.matches(Mockito.on(mock).invoke(method, 1, arg1, arg2));

        verify(mock).someMethod(ArgumentMatchers.eq(1), ArgumentMatchers.anyString());
    }

    @Test
    public void testInvocationMatcher_doesNotMatchInvocation() {
        Object mock = mock(SomeClass.class);
        Object arg1 = new Object();
        Object arg2 = new Object();

        Method method = Mockito.method(SomeClass.class, "someMethod", new Class[]{Integer.class, String.class});
        Invocation invocation = Mockito.on(mock).invoke(method, 1, arg1, arg2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        given(mock.someMethod(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString())).willReturn(new Object());

        verifyNoInteractions(mock);
    }

    @Test
    public void testInvocationMatcher_matchesVarargs() {
        Object mock = mock(SomeClass.class);
        Object[] varargs = new Object[]{new Object(), new Object()};

        Method method = Mockito.method(SomeClass.class, "someMethod", new Class[]{Integer.class, String[].class});
        Invocation invocation = Mockito.on(mock).invoke(method, 1, varargs);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> expectedMatchers = Arrays.asList(
                argThat(isA(Integer.class)),
                argThat(isA(Object[].class))
        );

        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        Matcher varargMatcher = actualMatchers.get(1);
        List<Matcher> varargsMatchers = Collections.singletonList(argThat(isA(VarargMatcher.class)));

        List<Matcher> allMatchers = new LinkedList<>(actualMatchers);
        allMatchers.remove(varargMatcher);
        allMatchers.add(0, varargsMatchers.get(0));
        allMatchers.addAll(1, actualMatchers.subList(2, actualMatchers.size()));

        assertThat(allMatchers, is(equalTo(expectedMatchers)));
    }

    @Test
    public void testInvocationMatcher_doesNotMatchVarargs() {
        Object mock = mock(SomeClass.class);
        Object[] varargs = new Object[]{new Object(), new Object()};

        Method method = Mockito.method(SomeClass.class, "someMethod", new Class[]{Integer.class, String[].class});
        Invocation invocation = Mockito.on(mock).invoke(method, 1, varargs);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> expectedMatchers = Collections.emptyList();

        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        List<Matcher> allMatchers = new LinkedList<>(actualMatchers);

        assertThat(allMatchers, is(not(equalTo(expectedMatchers))));
    }

    @Test
    public void testInvocationMatcher_doesNotMatchInvocationWithDifferentMock() {
        Object mock = mock(SomeClass.class);
        Object arg1 = new Object();
        Object arg2 = new Object();

        Object otherMock = mock(AnotherClass.class);
        Object otherArg1 = new Object();
        Object otherArg2 = new Object();

        Method method = Mockito.method(SomeClass.class, "someMethod", new Class[]{Integer.class, String.class});
        Method otherMethod = Mockito.method(AnotherClass.class, "someOtherMethod", new Class[]{Integer.class, String.class});
        Invocation invocation = Mockito.on(mock).invoke(method, 1, arg1, arg2);
        Invocation otherInvocation = Mockito.on(otherMock).invoke(otherMethod, 1, otherArg1, otherArg2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        InvocationMatcher otherInvocationMatcher = new InvocationMatcher(otherInvocation);

        List<Matcher> expectedMatchers = Collections.emptyList();

        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        List<Matcher> otherActualMatchers = otherInvocationMatcher.getMatchers();

        List<Matcher> allMatchers = new LinkedList<>(actualMatchers);
        allMatchers.addAll(otherActualMatchers);

        assertThat(allMatchers, is(not(equalTo(expectedMatchers))));
    }

    @Test
    public void testInvocationMatcher_matchesInvocationWithSameMethodButDifferentArguments() {
        Object mock = mock(SomeClass.class);
        Object arg1 = new Object();
        Object arg2 = new Object();
        Object arg3 = new Object();

        Method method = Mockito.method(SomeClass.class, "someMethod", new Class[]{Integer.class, String.class, String.class});
        Invocation invocation1 = Mockito.on(mock).invoke(method, 1, arg1, arg2);
        Invocation invocation2 = Mockito.on(mock).invoke(method, 1, arg1, arg2, arg3);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        List<Matcher> expectedMatchers = Collections.emptyList();

        List<Matcher> actualMatchers1 = invocationMatcher1.getMatchers();
        List<Matcher> actualMatchers2 = invocationMatcher2.getMatchers();

        List<Matcher> allMatchers = new LinkedList<>(actualMatchers1);
        allMatchers.addAll(actualMatchers2);

        assertThat(allMatchers, is(not(equalTo(expectedMatchers))));
    }

    @Test
    public void testInvocationMatcher_matchesInvocationWithSameMethodButDifferentArgumentsButSafely() {
        Object mock = mock(SomeClass.class);
        Object arg1 = new Object();
        Object arg2 = new Object();
        Object arg3 = new Object();

        Method method = Mockito.method(SomeClass.class, "someMethod", new Class[]{Integer.class, String.class, String.class});
        Invocation invocation1 = Mockito.on(mock).invoke(method, 1, arg1, arg2);
        Invocation invocation2 = Mockito.on(mock).invoke(method, 1, arg1, arg2, arg3);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        try {
            List<Matcher> actualMatchers1 = invocationMatcher1.getMatchers();
            List<Matcher> actualMatchers2 = invocationMatcher2.getMatchers();

            List<Matcher> allMatchers = new LinkedList<>(actualMatchers1);
            allMatchers.addAll(actualMatchers2);

            List<Matcher> expectedMatchers = Arrays.asList(
                    argThat(isA(Integer.class)),
                    argThat(isA(String.class)),
                    argThat(isA(String.class))
            );

            assertThat(allMatchers, is(equalTo(expectedMatchers)));
        } catch (Throwable e) {
            QualityChecker.checkThat(e, isA(RuntimeException.class));
        }
    }
}