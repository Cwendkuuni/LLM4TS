package org.mockito.internal.invocation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockitoMock;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class InvocationMatcherTest {

    @MockitoMock
    private MyMock mock;
    private InvocationMatcher invocationMatcher;
    private InvocationOnMock invocation;

    @Before
    public void setup() {
        Object[] args = new Object[]{1, "string", new Integer(3)};
        invocation = new InvocationOnMock(mock, "methodName", args);
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void test_whenInvocationMatcherIsCreatedWithEmptyMatchers_thenArgumentsAreMatchedWithDefaultMatchers() {
        // Test case for when InvocationMatcher is created with empty matchers list
        List<Matcher<?>> matchers = Collections.emptyList();
        InvocationMatcher emptyMatcher = new InvocationMatcher(invocation, matchers);
        boolean result = emptyMatcher.matches(invocation);
        // Since default behavior is to match invocations, the test should pass
        assertTrue("InvocationMatcher should match empty invocation when default matchers are used", result);
    }

    @Test
    public void test_whenInvocationMatcherIsCreatedWithCustomMatchers_thenMatchesInvocationWithCorrespondingMatchers() {
        // Test case for when InvocationMatcher is created with custom matchers
        Matcher<Object> firstArgMatcher = ArgumentMatchers.eq(1);
        Matcher<String> secondArgMatcher = ArgumentMatchers.eq("string");
        Matcher<Integer> thirdArgMatcher = ArgumentMatchers.isA(Integer.class);
        List<Matcher<?>> matchers = Arrays.asList(firstArgMatcher, secondArgMatcher, thirdArgMatcher);
        InvocationMatcher customMatcher = new InvocationMatcher(invocation, matchers);
        boolean result = customMatcher.matches(invocation);
        // Since the custom matchers are provided, the test should pass
        assertTrue("InvocationMatcher should match invocation with custom matchers", result);
    }

    @Test
    public void test_whenInvocationMatcherDoesNotMatch_thenReturnsFalse() {
        // Test case for when InvocationMatcher does not match
        InvocationOnMock differentInvocation = new InvocationOnMock(mock, "differentMethodName", new Object[]{1, 2, 3});
        InvocationMatcher mismatchingMatcher = new InvocationMatcher(invocation);
        boolean result = mismatchingMatcher.matches(differentInvocation);
        // Since the invocations are different, the test should fail
        assertFalse("InvocationMatcher should not match different invocation", result);
    }

    @Test
    public void test_whenInvocationMatcherIsCreatedWithVarargs_thenCaptureArgumentsCorrectly() {
        // Test case for when InvocationMatcher is created with varargs
        Matcher<Object> firstArgMatcher = ArgumentMatchers.eq(1);
        Matcher<String> secondArgMatcher = ArgumentMatchers.eq("string");
        Matcher<Integer[]> thirdArgMatcher = ArgumentMatchers.arrayContaining(ArgumentMatchers.isA(Integer.class));
        List<Matcher<?>> matchers = Arrays.asList(firstArgMatcher, secondArgMatcher, thirdArgMatcher);
        InvocationMatcher varargsMatcher = new InvocationMatcher(invocation, matchers);
        varargsMatcher.captureArgumentsFrom(invocation);
        Object[] capturedArgs = varargsMatcher.getInvocation().getArguments();
        // Verify that the arguments are captured correctly
        assertArrayEquals("Captured arguments should match the original ones", args, capturedArgs);
    }

    @Test
    public void test_whenInvocationMatcherDoesNotCaptureAllArguments_thenThrowsException() {
        // Test case for when InvocationMatcher does not capture all arguments
        Matcher<Object> firstArgMatcher = ArgumentMatchers.eq(1);
        Matcher<String> secondArgMatcher = ArgumentMatchers.eq("string");
        List<Matcher<?>> matchers = Arrays.asList(firstArgMatcher, secondArgMatcher);
        InvocationMatcher notCapturingMatcher = new InvocationMatcher(invocation, matchers);
        // Trigger the exception
        notCapturingMatcher.captureArgumentsFrom(invocation);
        try {
            Object[] capturedArgs = notCapturingMatcher.getInvocation().getRawArguments();
            fail("InvocationMatcher should throw an exception when it does not capture all arguments");
        } catch (AssertionError e) {
            // Expected exception
        }
    }

    @Test
    public void test_whenTwoInvocationMatchersHaveSameMethod_thenReturnTrue() {
        // Test case for when two InvocationMatchers have the same method
        InvocationOnMock sameMethodInvocation = new InvocationOnMock(mock, "methodName", args);
        InvocationMatcher sameMethodMatcher = new InvocationMatcher(sameMethodInvocation);
        boolean result = invocationMatcher.hasSimilarMethod(sameMethodMatcher);
        assertTrue("Two InvocationMatchers should have similar method", result);
    }

    @Test
    public void test_whenTwoInvocationMatchersHaveDifferentMethods_thenReturnFalse() {
        // Test case for when two InvocationMatchers have different methods
        InvocationOnMock differentMethodInvocation = new InvocationOnMock(mock, "differentMethodName", args);
        InvocationMatcher mismatchingMatcher = new InvocationMatcher(invocation);
        boolean result = mismatchingMatcher.hasSimilarMethod(new InvocationMatcher(differentMethodInvocation));
        assertFalse("Two InvocationMatchers should not have similar method", result);
    }

    @Test
    public void test_whenTwoInvocationMatchersHaveSameMethodAndSameArguments_thenReturnTrue() {
        // Test case for when two InvocationMatchers have the same method and same arguments
        InvocationOnMock sameMethodAndArgsInvocation = new InvocationOnMock(mock, "methodName", args);
        InvocationMatcher sameMethodAndArgsMatcher = new InvocationMatcher(sameMethodAndArgsInvocation);
        boolean result = invocationMatcher.matches(sameMethodAndArgsMatcher);
        assertTrue("Two InvocationMatchers should match when they have the same method and same arguments", result);
    }

    @Test
    public void test_whenTwoInvocationMatchersHaveSameMethodButDifferentArguments_thenReturnFalse() {
        // Test case for when two InvocationMatchers have the same method but different arguments
        Object[] differentArguments = new Object[]{1, "differentString", new Integer(3)};
        InvocationOnMock sameMethodDifferentArgsInvocation = new InvocationOnMock(mock, "methodName", differentArguments);
        InvocationMatcher mismatchingMatcher = new InvocationMatcher(invocation);
        boolean result = mismatchingMatcher.matches(new InvocationMatcher(sameMethodDifferentArgsInvocation));
        assertFalse("Two InvocationMatchers should not match when they have the same method but different arguments", result);
    }

    @Test
    public void test_whenTwoInvocationMatchersHaveDifferentMethodsButVerifiedFalse_thenReturnTrue() {
        // Test case for when two InvocationMatchers have different methods but are not verified
        InvocationOnMock differentMethodUnverifiedInvocation1 = new InvocationOnMock(mock, "differentMethodName1", args);
        InvocationMatcher mismatchingMatcher1 = new InvocationMatcher(differentMethodUnverifiedInvocation1);
        InvocationOnMock differentMethodUnverifiedInvocation2 = new InvocationOnMock(mock, "differentMethodName2", args);
        InvocationMatcher mismatchingMatcher2 = new InvocationMatcher(differentMethodUnverifiedInvocation2);
        boolean result = mismatchingMatcher1.hasSimilarMethod(mismatchingMatcher2);
        assertTrue("Two unverified InvocationMatchers should be considered similar when they have different methods", result);
    }

    @Test
    public void test_whenTwoInvocationMatchersHaveDifferentMethodsButVerifiedTrue_thenReturnFalse() {
        // Test case for when two InvocationMatchers have different methods and are verified
        InvocationOnMock verifiedDifferentMethodInvocation1 = new InvocationOnMock(mock, "differentMethodName1", args);
        InvocationMatcher mismatchingMatcher1 = new InvocationMatcher(verifiedDifferentMethodInvocation1);
        mock.verify(verifiedDifferentMethodInvocation1, VerificationMode.TIMES(1));
        InvocationOnMock verifiedDifferentMethodInvocation2 = new InvocationOnMock(mock, "differentMethodName2", args);
        InvocationMatcher mismatchingMatcher2 = new InvocationMatcher(verifiedDifferentMethodInvocation2);
        mock.verify(verifiedDifferentMethodInvocation2, VerificationMode.TIMES(1));
        boolean result = mismatchingMatcher1.hasSimilarMethod(mismatchingMatcher2);
        assertFalse("Two verified InvocationMatchers should not be considered similar when they have different methods", result);
    }

    @Test
    public void test_whenInvocationMatcherIsCreatedWithNullInvocation_thenThrowsException() {
        // Test case for when InvocationMatcher is created with null invocation
        assertThrows("InvocationMatcher should not be able to be created with null invocation", () -> new InvocationMatcher(null));
    }
}