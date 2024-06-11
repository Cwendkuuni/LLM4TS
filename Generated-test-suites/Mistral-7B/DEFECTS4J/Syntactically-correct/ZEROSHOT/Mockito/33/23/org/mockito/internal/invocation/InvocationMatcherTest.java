package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.verification.VerificationMode;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testNewInstanceWithMatchers() {
        // given
        Object mock = mock(SomeClass.class);
        Method someMethod = SomeClass.class.getMethod("someMethod", int.class, String.class);
        Invocation invocation = new Invocation(mock, someMethod, new Object[]{1, "test"}, new Location(3, 4));
        List<Matcher> matchers = Collections.singletonList(is(1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when

        // no verification needed

        // then
        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), is(matchers));
        assertThat(invocationMatcher.getMethod(), is(someMethod));
        assertThat(invocationMatcher.matches(invocation), is(true));
        assertThat(invocationMatcher.toString(), is("InvocationMatcher [invocation=Invocation [target=SomeClass@78d2b626, method=public int someMethod(int, java.lang.String) SomeClass.someMethod(int, java.lang.String), arguments=[1, test], location=Location [line=3, column=4]], matchers=[ArgumentsMatcher [matcher=org.hamcrest.CoreMatchers#is(1)]]]"));
    }

    @Test
    public void testNewInstanceWithoutMatchers() {
        // given
        Object mock = mock(SomeClass.class);
        Method someMethod = SomeClass.class.getMethod("someMethod", int.class, String.class);
        Invocation invocation = new Invocation(mock, someMethod, new Object[]{1, "test"}, new Location(3, 4));

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), is(Collections.emptyList()));
        assertThat(invocationMatcher.getMethod(), is(someMethod));
        assertThat(invocationMatcher.matches(invocation), is(true));
        assertThat(invocationMatcher.toString(), is("InvocationMatcher [invocation=Invocation [target=SomeClass@78d2b626, method=public int someMethod(int, java.lang.String) SomeClass.someMethod(int, java.lang.String), arguments=[1, test], location=Location [line=3, column=4]]]"));
    }

    @Test
    public void testHasSimilarMethod() {
        // given
        Object mock = mock(SomeClass.class);
        Method someMethod = SomeClass.class.getMethod("someMethod", int.class, String.class);
        Method otherMethod = SomeClass.class.getMethod("someMethod", double.class, String.class);
        Invocation invocation = new Invocation(mock, someMethod, new Object[]{1, "test"}, new Location(3, 4));
        Invocation otherInvocation = new Invocation(mock, otherMethod, new Object[]{2.0, "test2"}, new Location(5, 6));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        InvocationMatcher otherInvocationMatcher = new InvocationMatcher(otherInvocation);

        // when

        // no verification needed

        // then
        assertThat(invocationMatcher.hasSimilarMethod(otherInvocationMatcher), is(false));
    }

    @Test
    public void testHasSameMethod() {
        // given
        Object mock = mock(SomeClass.class);
        Method someMethod = SomeClass.class.getMethod("someMethod", int.class, String.class);
        Method otherMethod = SomeClass.class.getMethod("someMethod", int.class, String.class);
        Invocation invocation = new Invocation(mock, someMethod, new Object[]{1, "test"}, new Location(3, 4));
        Invocation otherInvocation = new Invocation(mock, otherMethod, new Object[]{1, "test"}, new Location(5, 6));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        InvocationMatcher otherInvocationMatcher = new InvocationMatcher(otherInvocation);

        // when

        // no verification needed

        // then
        assertThat(invocationMatcher.hasSameMethod(otherInvocationMatcher), is(true));
    }

    @Test
    public void testToString() {
        // given
        Object mock = mock(SomeClass.class);
        Method someMethod = SomeClass.class.getMethod("someMethod", int.class, String.class);
        Invocation invocation = new Invocation(mock, someMethod, new Object[]{1, "test"}, new Location(3, 4));
        List<Matcher> matchers = Collections.singletonList(is("testString"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when
        String toString = invocationMatcher.toString();

        // then
        assertThat(toString, is("InvocationMatcher [invocation=Invocation [target=SomeClass@78d2b626, method=public int someMethod(int, java.lang.String) SomeClass.someMethod(int, java.lang.String), arguments=[1, test], location=Location [line=3, column=4]], matchers=[ArgumentsMatcher [matcher=org.hamcrest.CoreMatchers#is(testString)]]]"));
    }

    @Test
    public void testMatches() {
        // given
        Object mock = mock(SomeClass.class);
        Method someMethod = SomeClass.class.getMethod("someMethod", int.class, String.class);
        Invocation invocation = new Invocation(mock, someMethod, new Object[]{1, "test"}, new Location(3, 4));
        List<Matcher> matchers = Collections.singletonList(is("testString"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation otherInvocation = new Invocation(mock, someMethod, new Object[]{1, "test2"}, new Location(3, 5));

        // when
        boolean matches = invocationMatcher.matches(otherInvocation);

        // then
        assertThat(matches, is(false));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        // given
        Object mock = mock(SomeClass.class);
        Method someMethod = SomeClass.class.getMethod("someMethod", int.class);
        Invocation invocation = new Invocation(mock, someMethod, new Object[]{1}, new Location(3, 4));
        List<Matcher> matchers = Collections.singletonList(is(1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation otherInvocation = new Invocation(mock, someMethod, new Object[]{2}, new Location(3, 5));

        // when
        boolean safelyArgumentsMatch = invocationMatcher.safelyArgumentsMatch(otherInvocation.getArguments());

        // then
        assertThat(safelyArgumentsMatch, is(false));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // given
        Object mock = mock(SomeClass.class);
        Method someMethod = SomeClass.class.getMethod("someMethod", int.class, String.class);
        Invocation invocation = new Invocation(mock, someMethod, new Object[]{1, "test"}, new Location(3, 4));
        List<Matcher> matchers = Collections.singletonList(new ArgumentCaptor<Integer>() {
            @Override
            public void capture(Integer value) {
                capturedInt = value;
            }

            int capturedInt;
        });

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when
        Invocation testInvocation = new Invocation(mock, someMethod, new Object[]{2}, new Location(3, 5));
        invocationMatcher.captureArgumentsFrom(testInvocation);

        // then
        assertThat(invocationMatcher.capturedInt, is(2));
    }

    @Test
    public void testCreateFrom() {
        // given
        Object mock = mock(SomeClass.class);
        Method someMethod = SomeClass.class.getMethod("someMethod", int.class, String.class);
        Invocation invocation1 = new Invocation(mock, someMethod, new Object[]{1, "test1"}, new Location(3, 4));
        Invocation invocation2 = new Invocation(mock, someMethod, new Object[]{2, "test2"}, new Location(3, 5));

        List<Invocation> invocations = new LinkedList<>();
        invocations.add(invocation1);
        invocations.add(invocation2);

        // when
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        // then
        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);

        assertThat(invocationMatcher1.getInvocation(), is(invocation1));
        assertThat(invocationMatcher2.getInvocation(), is(invocation2));
    }

    @Test
    public void testCreateFromNullInvocations() {
        // given
        List<Invocation> invocations = null;

        // when
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        // then
        assertThat(invocationMatchers, is(Collections.emptyList()));
    }

    private static class SomeClass {
        public int someMethod(int arg1, String arg2) {
            return 0;
        }
    }
}