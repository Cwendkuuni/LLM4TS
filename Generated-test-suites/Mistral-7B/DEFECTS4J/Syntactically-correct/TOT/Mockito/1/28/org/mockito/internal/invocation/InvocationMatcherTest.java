package org.mockito.internal.invocation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.verification.VerificationMode;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class InvocationMatcherTest {

    private MockInvocation mockInvocation;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        mockInvocation = new MockInvocation();
        invocationMatcher = new InvocationMatcher(mockInvocation);
    }

    @Test
    public void testHasSameMethod() {
        // given
        Invocation testInvocation = createTestInvocation();
        InvocationMatcher testMatcher = new InvocationMatcher(testInvocation);

        // when
        boolean result = invocationMatcher.hasSameMethod(testMatcher);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void testHasSimilarMethodWithDifferentArguments() {
        // given
        Invocation testInvocation1 = createTestInvocationWithArguments(1, "test");
        InvocationMatcher testMatcher1 = new InvocationMatcher(testInvocation1);
        Invocation testInvocation2 = createTestInvocationWithArguments(2, "test2");
        InvocationMatcher testMatcher2 = new InvocationMatcher(testInvocation2);

        // when
        boolean result = invocationMatcher.hasSameMethod(testMatcher2);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void testHasSimilarMethodWithSameArguments() {
        // given
        Invocation testInvocation1 = createTestInvocationWithArguments(1, "test");
        InvocationMatcher testMatcher1 = new InvocationMatcher(testInvocation1);
        Invocation testInvocation2 = createTestInvocationWithArguments(1, "test");
        InvocationMatcher testMatcher2 = new InvocationMatcher(testInvocation2);

        // when
        boolean result = invocationMatcher.hasSameMethod(testMatcher2);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void testMatches() {
        // given
        Invocation testInvocation = createTestInvocation();
        Object[] testArguments = testInvocation.getArguments();
        InvocationMatcher testMatcher = new InvocationMatcher(testInvocation);
        InvocationMatcher testMatcherWithArguments = new InvocationMatcher(testInvocation, Arrays.asList(
                is(testArguments[0]),
                is(testArguments[1])
        ));

        // when
        boolean result = testMatcherWithArguments.matches(testMatcher);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void testMatchesWithDifferentArguments() {
        // given
        Invocation testInvocation = createTestInvocation();
        Object[] testArguments = testInvocation.getArguments();
        InvocationMatcher testMatcher = new InvocationMatcher(testInvocation);
        InvocationMatcher testMatcherWithArguments = new InvocationMatcher(testInvocation, Arrays.asList(
                is(testArguments[0] + 1),
                is(testArguments[1] + 1)
        ));

        // when
        boolean result = testMatcherWithArguments.matches(testMatcher);

        // then
        assertThat(result, is(false));
    }

    @Test
    public void testCaptureArguments() {
        // given
        Invocation testInvocation = createTestInvocation();
        InvocationMatcher testMatcher = new InvocationMatcher(testInvocation);

        // when
        testMatcher.captureArgumentsFrom(mockInvocation);

        // then
        verify(mockInvocation).getArgumentAt(0, Object.class);
        verify(mockInvocation).getArgumentAt(1, Object.class);
    }

    @Test
    public void testCaptureArgumentsWithVarargs() {
        // given
        Invocation testInvocation = createTestInvocationWithVarargs();
        InvocationMatcher testMatcher = new InvocationMatcher(testInvocation);

        // when
        testMatcher.captureArgumentsFrom(mockInvocation);

        // then
        verify(mockInvocation, VerificationMode.TIMES(3)).getArgumentAt(0, Object.class);
    }

    @Test
    public void testCaptureArgumentsWithVarargsAndVarargMatcher() {
        // given
        Invocation testInvocation = createTestInvocationWithVarargs();
        InvocationMatcher testMatcher = new InvocationMatcher(testInvocation);
        Matcher matcher = new VarargMatcher(new String[0]);

        // when
        testMatcher.captureArgumentsFrom(mockInvocation);

        // then
        verify(mockInvocation, times(3)).getArguments();
        verify(mockInvocation).getArguments()[0].getClass().isArray();
        assertThat(matcher.getArguments(), notNullValue());
        assertThat(Arrays.toString(matcher.getArguments()), is(Arrays.toString(mockInvocation.getArguments())));
    }

    @Test
    public void testCaptureArgumentsWithNoVarargs() {
        // given
        Invocation testInvocation = createTestInvocation();
        InvocationMatcher testMatcher = new InvocationMatcher(testInvocation);

        // when
        testMatcher.captureArgumentsFrom(mockInvocation);

        // then
        verify(mockInvocation, VerificationMode.TIMES(2)).getArgumentAt(0, Object.class);
        verify(mockInvocation, VerificationMode.TIMES(2)).getArgumentAt(1, Object.class);
    }

    @Test
    public void testCaptureArgumentsWithNoMatchingArguments() {
        // given
        Invocation testInvocation = createTestInvocation();
        InvocationMatcher testMatcher = new InvocationMatcher(testInvocation);

        // when - should fail
        try {
            testMatcher.captureArgumentsFrom(mockInvocation);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // then
            verifyNoMoreInteractions(mockInvocation);
        }
    }

    @Test
    public void testCaptureArgumentsWithNoArguments() {
        // given
        Invocation testInvocation = new InvocationOnMock(null, null, null, null);
        InvocationMatcher testMatcher = new InvocationMatcher(testInvocation);

        // when
        testMatcher.captureArgumentsFrom(mockInvocation);

        // then
        verifyZeroInteractions(mockInvocation);
    }

    private Invocation createTestInvocation() {
        Mockito.when(mockInvocation.getMethod()).thenReturn(new Method(String.class.getName(), new Class[]{String.class, Integer.TYPE}));
        return new InvocationOnMock(mockInvocation, new Method(String.class.getName(), new Class[]{String.class, Integer.TYPE}), mockInvocation, new Object[]{new String("test"), 1});
    }

    private Invocation createTestInvocationWithArguments(Object arg1, Object arg2) {
        Mockito.when(mockInvocation.getMethod()).thenReturn(new Method(String.class.getName(), new Class[]{String.class, Integer.TYPE}));
        return new InvocationOnMock(mockInvocation, new Method(String.class.getName(), new Class[]{String.class, Integer.TYPE}), mockInvocation, new Object[]{arg1, arg2});
    }

    private Invocation createTestInvocationWithVarargs() {
        Mockito.when(mockInvocation.getMethod()).thenReturn(new Method(String.class.getName(), new Class[]{String.class, Integer.TYPE, Integer[].class}));
        return new InvocationOnMock(mockInvocation, new Method(String.class.getName(), new Class[]{String.class, Integer.TYPE, Integer[].class}), mockInvocation, new Object[]{null, 1, new Integer[]{1, 2, 3}});
    }
}