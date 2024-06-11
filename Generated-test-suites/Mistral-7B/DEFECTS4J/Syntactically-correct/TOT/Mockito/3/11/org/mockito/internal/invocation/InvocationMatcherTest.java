import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Stubber;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.hamcrest.CoreMatchers.isNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Invocation invocation;
    private Stubber stubber;

    @Before
    public void setup() {
        stubber = mock(Stubber.class);
        invocation = new InvocationOnMock(stubber, new MethodTestClass(), "testMethod", new Object[]{1, "two"});
        matcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testEmptyMatcher() {
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher emptyMatcher = new InvocationMatcher(invocation, matchers);
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation);
        assertThat(emptyMatcher, is(expectedMatcher));
    }

    @Test
    public void testSimpleMatcher() {
        Matcher matcher = eq(1);
        List<Matcher> matchers = Arrays.asList(matcher);
        InvocationMatcher actualMatcher = new InvocationMatcher(invocation, matchers);
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation, Arrays.asList(matcher));
        assertThat(actualMatcher, is(expectedMatcher));
    }

    @Test
    public void testStringMatcher() {
        Matcher matcher = eq("two");
        List<Matcher> matchers = Arrays.asList(matcher);
        InvocationMatcher actualMatcher = new InvocationMatcher(invocation, matchers);
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation, Arrays.asList(matcher));
        assertThat(actualMatcher, is(expectedMatcher));
    }

    @Test
    public void testVoidMatcher() {
        List<Matcher> matchers = Arrays.asList(isNull());
        InvocationMatcher actualMatcher = new InvocationMatcher(invocation, matchers);
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation, matchers);
        assertThat(actualMatcher, is(expectedMatcher));
    }

    @Test
    public void testListMatcher() {
        Matcher<List> listMatcher = hasItem(eq(1));
        List<Matcher> matchers = Arrays.asList(listMatcher);
        InvocationMatcher actualMatcher = new InvocationMatcher(invocation, matchers);
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation, matchers);
        assertThat(actualMatcher, is(expectedMatcher));
    }

    @Test
    public void testComplexMatcher() {
        Matcher<Object[]> complexMatcher = argThat(new TypeSafeMatcher<Object[]>() {
            @Override
            public boolean matches(Object[] arguments) {
                return Arrays.asList(arguments).contains(eq(1));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("contains 1");
            }
        });
        List<Matcher> matchers = Arrays.asList(complexMatcher);
        InvocationMatcher actualMatcher = new InvocationMatcher(invocation, matchers);
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation, matchers);
        assertThat(actualMatcher, is(expectedMatcher));
    }

    @Test
    public void testArgMatcher() {
        List<Matcher> matchers = Arrays.asList(argThat(isA(Integer.class)));
        InvocationMatcher actualMatcher = new InvocationMatcher(invocation, matchers);
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation, matchers);
        assertThat(actualMatcher, is(expectedMatcher));
    }

    @Test
    public void testMatches() {
        Matcher<Object[]> complexMatcher = argThat(new TypeSafeMatcher<Object[]>() {
            @Override
            public boolean matches(Object[] arguments) {
                return Arrays.asList(arguments).contains(eq(1));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("contains 1");
            }
        });
        List<Matcher> matchers = Arrays.asList(complexMatcher);
        InvocationMatcher actualMatcher = new InvocationMatcher(invocation, matchers);
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation, matchers);
        InvocationMatcher anotherMatcher = new InvocationMatcher(new InvocationOnMock(stubber, new MethodTestClass(), "testMethod", new Object[]{2, "two"}));
        InvocationMatcher anotherExpectedMatcher = new InvocationMatcher(new InvocationOnMock(stubber, new MethodTestClass(), "testMethod", new Object[]{2, "two"}));
        InvocationMatcher anotherDifferentMatcher = new InvocationMatcher(new InvocationOnMock(stubber, new MethodTestClass(), "anotherMethod", new Object[]{1, "two"}));
        InvocationMatcher anotherDifferentExpectedMatcher = new InvocationMatcher(new InvocationOnMock(stubber, new MethodTestClass(), "anotherMethod", new Object[]{1, "two"}));

        assertThat(actualMatcher, matches(invocation));
        assertThat(actualMatcher, matches(invocation));
        assertThat(actualMatcher, is(equalTo(expectedMatcher)));
        assertThat(actualMatcher, is(not(equalTo(anotherMatcher))));
        assertThat(actualMatcher, is(not(equalTo(anotherDifferentMatcher))));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] arguments = new Object[]{1, "two"};
        Matcher<Object[]> complexMatcher = argThat(new TypeSafeMatcher<Object[]>() {
            @Override
            public boolean matches(Object[] arguments) {
                return Arrays.asList(arguments).contains(eq(1));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("contains 1");
            }
        });
        List<Matcher> matchers = Arrays.asList(complexMatcher);
        InvocationMatcher actualMatcher = new InvocationMatcher(invocation, matchers);
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation, matchers);
        boolean result = actualMatcher.safelyArgumentsMatch(arguments);
        boolean expectedResult = true;
        assertThat(result, is(expectedResult));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchWithException() {
        Object[] arguments = new Object[]{1, "two"};
        Matcher<Object[]> complexMatcher = argThat(new TypeSafeMatcher<Object[]>() {
            @Override
            public boolean matches(Object[] arguments) {
                throw new RuntimeException();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("contains 1");
            }
        });
        List<Matcher> matchers = Arrays.asList(complexMatcher);
        InvocationMatcher actualMatcher = new InvocationMatcher(invocation, matchers);
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation, matchers);
        actualMatcher.safelyArgumentsMatch(arguments);
    }

    @Test
    public void testHasSimilarMethod() {
        InvocationMatcher firstMatcher = new InvocationMatcher(invocation);
        InvocationMatcher secondMatcher = new InvocationMatcher(new InvocationOnMock(stubber, new MethodTestClass(), "testMethod", new Object[]{1, "two"}));
        boolean result = firstMatcher.hasSimilarMethod(secondMatcher);
        boolean expectedResult = false;
        assertThat(result, is(expectedResult));

        InvocationMatcher firstMatcherVarargs = new InvocationMatcher(new InvocationOnMock(stubber, new MethodTestClass(), "testVarargsMethod", new Object[]{1, "two", "three"}));
        InvocationMatcher secondMatcherVarargs = new InvocationMatcher(new InvocationOnMock(stubber, new MethodTestClass(), "testVarargsMethod", new Object[]{1, "two", "four"}));
        boolean resultVarargs = firstMatcherVarargs.hasSimilarMethod(secondMatcherVarargs);
        boolean expectedResultVarargs = false;
        assertThat(resultVarargs, is(expectedResultVarargs));

        InvocationMatcher firstMatcherSameMethod = new InvocationMatcher(new InvocationOnMock(stubber, new MethodTestClass(), "testMethod", new Object[]{1, "two"}));
        InvocationMatcher secondMatcherSameMethod = new InvocationMatcher(new InvocationOnMock(stubber, new MethodTestClass(), "testMethod", new Object[]{1, "two"}));
        boolean resultSameMethod = firstMatcherSameMethod.hasSimilarMethod(secondMatcherSameMethod);
        boolean expectedResultSameMethod = true;
        assertThat(resultSameMethod, is(expectedResultSameMethod));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Matcher<Integer> integerMatcher = argThat(isA(Integer.class));
        Matcher<String> stringMatcher = argThat(isA(String.class));
        List<Matcher> matchers = Arrays.asList(integerMatcher, stringMatcher);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(invocation);

        Object capturedInteger = integerMatcher.getValue();
        Object capturedString = stringMatcher.getValue();

        assertThat(capturedInteger, is(1));
        assertThat(capturedString, is("two"));
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(invocation, new InvocationOnMock(stubber, new MethodTestClass(), "testMethod", new Object[]{1, "two"}));
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);
        InvocationMatcher firstMatcher = matchers.get(0);
        InvocationMatcher secondMatcher = matchers.get(1);

        assertThat(firstMatcher, is(equalTo(matcher)));
        assertThat(secondMatcher, is(instanceOf(InvocationMatcher.class)));
    }
}