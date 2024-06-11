package org.mockito.internal.invocation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.MethodInterceptor;
import org.mockito.stub.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class InvocationMatcherTest {

    private MethodInterceptor interceptor;
    private Object mock;

    @Before
    public void setUp() {
        interceptor = Mockito.mock(MethodInterceptor.class);
        mock = mock(Object.class);
    }

    @Test
    public void testMatchesSameInvocation() {
        // given
        Invocation invocation = new Invocation(mock, new MethodTestClass(), new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // when
        Invocation identicalInvocation = new Invocation(mock, new MethodTestClass(), new Object[0]);
        InvocationMatcher identicalInvocationMatcher = new InvocationMatcher(identicalInvocation);

        // then
        assertThat(invocationMatcher.matches(identicalInvocationMatcher), is(true));
    }

    @Test
    public void testMatchesDifferentInvocationNoMatchers() {
        // given
        Invocation invocation = new Invocation(mock, new MethodTestClass(), new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation differentInvocation = new Invocation(mock, new DifferentMethodTestClass(), new Object[0]);
        InvocationMatcher differentInvocationMatcher = new InvocationMatcher(differentInvocation);

        // then
        assertThat(invocationMatcher.matches(differentInvocationMatcher), is(false));
    }

    @Test
    public void testMatchesDifferentInvocationWithMatchers() {
        // given
        Invocation invocation = new Invocation(mock, new MethodTestClass(), new Object[]{1, "abc"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.equalTo(1), ArgumentMatchers.equalTo("abc")));

        Invocation differentInvocation = new Invocation(mock, new DifferentMethodTestClass(), new Object[]{2, "def"});
        InvocationMatcher differentInvocationMatcher = new InvocationMatcher(differentInvocation);

        // then
        assertThat(invocationMatcher.matches(differentInvocationMatcher), is(false));
    }

    @Test
    public void testMatchesDifferentInvocationWithSameMethodButDifferentArguments() {
        // given
        Invocation invocation = new Invocation(mock, new MethodTestClass(), new Object[]{1, "abc"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.equalTo(1), ArgumentMatchers.equalTo("abc")));

        Invocation differentInvocation = new Invocation(mock, new MethodTestClass(), new Object[]{2, "def"});
        InvocationMatcher differentInvocationMatcher = new InvocationMatcher(differentInvocation);

        // then
        assertThat(invocationMatcher.matches(differentInvocationMatcher), is(false));
    }

    @Test
    public void testMatchesDifferentInvocationWithSameMethodAndSameArguments() {
        // given
        Invocation invocation = new Invocation(mock, new MethodTestClass(), new Object[]{1, "abc"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.equalTo(1), ArgumentMatchers.equalTo("abc")));

        Invocation identicalInvocation = new Invocation(mock, new MethodTestClass(), new Object[]{1, "abc"});
        InvocationMatcher identicalInvocationMatcher = new InvocationMatcher(identicalInvocation);

        // then
        assertThat(invocationMatcher.matches(identicalInvocationMatcher), is(true));
    }

    @Test
    public void testMatchesDifferentInvocationWithSameMethodDifferentVerificationStatus() {
        // given
        Invocation invocation = new Invocation(mock, new MethodTestClass(), new Object[]{1, "abc"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.equalTo(1), ArgumentMatchers.equalTo("abc")));

        Invocation differentInvocation = new Invocation(mock, new MethodTestClass(), new Object[]{1, "abc"});
        InvocationMatcher differentInvocationMatcher = new InvocationMatcher(differentInvocation, Arrays.asList(ArgumentMatchers.equalTo(1), ArgumentMatchers.equalTo("abc")));
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) {
                return null;
            }
        }).when(mock).methodToMock();

        // then
        assertThat(invocationMatcher.matches(differentInvocationMatcher), is(true));
        verify(mock).methodToMock();
    }

    @Test
    public void testMatchesDifferentInvocationWithSameMethodButDifferentClasses() {
        // given
        Invocation invocation = new Invocation(mock, new MethodTestClass(), new Object[]{1, "abc"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.equalTo(1), ArgumentMatchers.equalTo("abc")));

        Invocation differentInvocation = new Invocation(mock, new DifferentMethodTestClass(), new Object[]{1, "abc"});
        InvocationMatcher differentInvocationMatcher = new InvocationMatcher(differentInvocation);

        // then
        assertThat(invocationMatcher.matches(differentInvocationMatcher), is(false));
    }

    @Test
    public void testHasSimilarMethod() {
        // given
        Invocation invocation1 = new Invocation(mock, new MethodTestClass(), new Object[]{1, "abc"});
        Invocation invocation2 = new Invocation(mock, new MethodTestClass(), new Object[]{2, "def"});
        Invocation invocation3 = new Invocation(mock, new MethodTestClass(), new Object[]{1, "abc"});

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        InvocationMatcher invocationMatcher3 = new InvocationMatcher(invocation3);

        // when
        boolean result = invocationMatcher1.hasSimilarMethod(invocationMatcher2);
        boolean result2 = invocationMatcher1.hasSimilarMethod(invocationMatcher3);

        // then
        assertThat(result, is(false));
        assertThat(result2, is(true));
    }

    @Test
    public void testToString() {
        // given
        Invocation invocation = new Invocation(mock, new MethodTestClass(), new Object[]{1, "abc"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.equalTo(1), ArgumentMatchers.equalTo("abc")));

        PrintSettings printSettings = new PrintSettings();
        String expected = "Invocation on mock <[mock]> using method <methodToMock(int,java.lang.String)> with arguments [1, abc]";

        // when
        String result = invocationMatcher.toString(printSettings);

        // then
        assertThat(result, is(expected));
    }

    @Test
    public void testToStringWithNoMatchers() {
        // given
        Invocation invocation = new Invocation(mock, new MethodTestClass(), new Object[]{});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        PrintSettings printSettings = new PrintSettings();
        String expected = "Invocation on mock <[mock]> using method <methodToMock()>";

        // when
        String result = invocationMatcher.toString(printSettings);

        // then
        assertThat(result, is(expected));
    }
}

class MethodTestClass {
    public void methodToMock(int i, String s) {
    }
}

class DifferentMethodTestClass {
    public void differentMethodToMock(int i, String s) {
    }
}