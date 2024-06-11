import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Stubber;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.hamcrest.CoreMatchers.isNull;
import static org.hamcrest.MatcherAssert.assertThat;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Stubber stubber;
    private Object mock;

    @Before
    public void setUp() {
        mock = Mockito.mock(String.class);
        stubber = Mockito.stub(mock);
    }

    @Test
    public void testInvocationMatcherWithEmptyMatchersList() {
        Invocation invocation = new InvocationOnMock(mock, new Method(null, "testMethod", new Class[0]));
        List<Matcher> matchers = Collections.emptyList();

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertThat(matcher.getMatchers(), isEmpty());
    }

    @Test
    public void testInvocationMatcherWithSpecifiedMatchersList() {
        Invocation invocation = new InvocationOnMock(mock, new Method(null, "testMethod", new Class[]{String.class, int.class}));
        Object[] arguments = new Object[]{Mockito.mock(String.class), 42};

        List<Matcher> matchers = Arrays.asList(
                isA(String.class),
                isA(Integer.class)
        );

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertThat(matcher.getMatchers(), containsInAnyOrder(isInstanceOf(String.class), isInstanceOf(Integer.class)));
    }

    @Test
    public void testToString() {
        Invocation invocation = new InvocationOnMock(mock, new Method(null, "testMethod", new Class[]{String.class, int.class}));
        Object[] arguments = new Object[]{Mockito.mock(String.class), 42};

        List<Matcher> matchers = Arrays.asList(
                isA(String.class),
                isA(Integer.class)
        );

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        String expected = "InvocationMatcher[invocation=InvocationOnMock [this=org.mockito.mockitows.TestStubber$1@5d94d13d, method=public java.lang.String org.mockito.mockitows.TestStubber.testMethod(java.lang.String, int), arguments=[mockito-mock-String, 42]], matchers=[Matcher[class = java.lang.String], Matcher[class = java.lang.Integer]]]";

        assertThat(matcher.toString(), is(expected));
    }

    @Test
    public void testMatchesWithSameInvocation() {
        Invocation invocation = new InvocationOnMock(mock, new Method(null, "testMethod", new Class[]{String.class, int.class}));
        Object[] arguments = new Object[]{Mockito.mock(String.class), 42};

        List<Matcher> matchers = Arrays.asList(
                isA(String.class),
                isA(Integer.class)
        );

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation, matchers);

        stubber.thenReturn("expectedResult");

        String result = matcher.getInvocation().proceed(arguments);

        assertThat(matcher, is(expectedMatcher));
        assertThat(result, is("expectedResult"));
    }

    @Test
    public void testMatchesWithDifferentInvocationButSameMethod() {
        Invocation invocation1 = new InvocationOnMock(mock, new Method(null, "testMethod", new Class[]{String.class, int.class}));
        Invocation invocation2 = new InvocationOnMock(mock, new Method(null, "testMethod", new Class[]{String.class, int.class}));
        Object[] arguments = new Object[]{Mockito.mock(String.class), 42};

        List<Matcher> matchers = Arrays.asList(
                isA(String.class),
                isA(Integer.class)
        );

        InvocationMatcher matcher = new InvocationMatcher(invocation1, matchers);

        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation2, matchers);

        stubber.thenReturn("expectedResult");

        String result1 = matcher.getInvocation1().proceed(arguments);
        String result2 = expectedMatcher.getInvocation1().proceed(arguments);

        assertThat(matcher, is(expectedMatcher));
        assertThat(result1, is("expectedResult"));
        assertThat(result2, is("expectedResult"));
    }

    @Test
    public void testMatchesWithDifferentInvocationButDifferentMethod() {
        Invocation invocation1 = new InvocationOnMock(mock, new Method(null, "testMethod1", new Class[]{String.class, int.class}));
        Invocation invocation2 = new InvocationOnMock(mock, new Method(null, "testMethod2", new Class[]{String.class, int.class}));
        Object[] arguments = new Object[]{Mockito.mock(String.class), 42};

        List<Matcher> matchers = Arrays.asList(
                isA(String.class),
                isA(Integer.class)
        );

        InvocationMatcher matcher = new InvocationMatcher(invocation1, matchers);

        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation2, matchers);

        stubber.thenReturn("expectedResult1");
        stubber.thenReturn("expectedResult2");

        String result1 = matcher.getInvocation1().proceed(arguments);
        String result2 = expectedMatcher.getInvocation1().proceed(arguments);

        assertThat(matcher, isNot(expectedMatcher));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        Invocation invocation = new InvocationOnMock(mock, new Method(null, "testMethod", new Class[]{String.class, int.class}));
        Object[] arguments = new Object[]{Mockito.mock(String.class), 42};

        List<Matcher> matchers = Arrays.asList(
                new CapturesArgumentsMatcher("stringArgument"),
                new CapturesArgumentsMatcher("integerArgument")
        );

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        String actualStringArgument = null;
        int actualIntegerArgument = 0;

        matcher.captureArgumentsFrom(invocation);

        actualStringArgument = ((CapturesArgumentsMatcher) matchers.get(0)).getCaptured();
        actualIntegerArgument = ((CapturesArgumentsMatcher) matchers.get(1)).getCaptured();

        assertThat(actualStringArgument, is(arguments[0]));
        assertThat(actualIntegerArgument, is(arguments[1]));
    }

    @Test
    public void testCaptureArgumentsFromVarargsInvocation() {
        Invocation invocation = new InvocationOnMock(mock, new Method(null, "testMethod", new Class[]{String[].class}));
        Object[] arguments = new Object[]{new String[]{"arg1", "arg2"}};

        List<Matcher> matchers = Arrays.asList(
                new VarargMatcher("stringArguments")
        );

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        String[] actualStringArguments = null;

        matcher.captureArgumentsFrom(invocation);

        actualStringArguments = ((VarargMatcher) matchers.get(0)).getCaptured();

        assertThat(actualStringArguments, is(arguments[0]));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromVarargsInvocationWithSingleMatcher() {
        Invocation invocation = new InvocationOnMock(mock, new Method(null, "testMethod", new Class[]{String[].class}));
        Object[] arguments = new Object[]{new String[]{"arg1", "arg2"}};

        List<Matcher> matchers = Arrays.asList(
                isA(String.class)
        );

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        matcher.captureArgumentsFrom(invocation);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromVarargsInvocationWithMultipleMatchers() {
        Invocation invocation = new InvocationOnMock(mock, new Method(null, "testMethod", new Class[]{String[].class}));
        Object[] arguments = new Object[]{new String[]{"arg1", "arg2"}};

        List<Matcher> matchers = Arrays.asList(
                isA(String.class),
                isA(Integer.class)
        );

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        matcher.captureArgumentsFrom(invocation);
    }

    private static <T> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }
}

class CapturesArgumentsMatcher implements Matcher<Object>, CapturesArguments {
    private String name;
    private Object captured;

    public CapturesArgumentsMatcher(String name) {
        this.name = name;
    }

    @Override
    public boolean matches(Object argument) {
        return true;
    }

    @Override
    public void captureFrom(Invocation invocation) {
        captured = invocation.getArgumentAt(0, Object.class);
    }

    public Object getCaptured() {
        return captured;
    }

    @Override
    public String toString() {
        return String.format("Captures %s", name);
    }
}

class VarargMatcher implements Matcher<Object>, CapturesArguments {
    private String name;
    private Object captured;

    public VarargMatcher(String name) {
        this.name = name;
    }

    @Override
    public boolean matches(Object argument) {
        if (argument instanceof Object[]) {
            Object[] array = (Object[]) argument;
            return Arrays.stream(array).anyMatch(this::matches);
        }
        return false;
    }

    @Override
    public void captureFrom(Invocation invocation) {
        captured = invocation.getArgumentAt(0, Object[].class);
    }

    public Object getCaptured() {
        return captured;
    }

    @Override
    public String toString() {
        return String.format("Captures %s array", name);
    }
}