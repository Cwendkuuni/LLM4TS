import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.InvocationOnMockImpl;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.Arguments;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.PrintSettings;
import org.mockito.internal.reporting.ArgumentsComparator;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class InvocationMatcherTest {

    private Invocations invocations;
    private InvocationMatcher matcher;

    @Before
    public void setUp() {
        invocations = new Invocations();
        matcher = new InvocationMatcher(new InvocationOnMockImpl(Mockito.mock(Object.class), new MethodTestSubject(), new LocationTestSubject(), new Object[0]), Collections.emptyList());
    }

    @Test
    public void testGetMethod() {
        Method method = new MethodTestSubject().getDeclaredMethod("testMethod", String.class, int.class);
        InvocationOnMock invocation = new InvocationOnMockImpl(Mockito.mock(Object.class), method, new LocationTestSubject(), new Object[]{ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()});
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.emptyList());

        assertEquals(method, matcher.getMethod());
    }

    @Test
    public void testMatches() {
        Method method = new MethodTestSubject().getDeclaredMethod("testMethod", String.class, int.class);
        InvocationOnMock expectedInvocation = new InvocationOnMockImpl(Mockito.mock(Object.class), method, new LocationTestSubject(), new Object[]{ArgumentMatchers.eq("test"), ArgumentMatchers.eq(42)});
        InvocationOnMock actualInvocation = new InvocationOnMockImpl(Mockito.mock(Object.class), method, new LocationTestSubject(), new Object[]{"test", 42});

        InvocationMatcher expectedMatcher = new InvocationMatcher(expectedInvocation, Collections.emptyList());
        InvocationMatcher actualMatcher = new InvocationMatcher(actualInvocation, Collections.emptyList());

        assertEquals(expectedMatcher, actualMatcher);
    }

    @Test
    public void testHasSimilarMethod() {
        Method method1 = new MethodTestSubject().getDeclaredMethod("testMethod1", String.class, int.class);
        Method method2 = new MethodTestSubject().getDeclaredMethod("testMethod2", String.class, int.class);

        InvocationOnMock invocation1 = new InvocationOnMockImpl(Mockito.mock(Object.class), method1, new LocationTestSubject(), new Object[]{ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()});
        InvocationOnMock invocation2 = new InvocationOnMockImpl(Mockito.mock(Object.class), method2, new LocationTestSubject(), new Object[]{ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()});

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        assertFalse(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testToString() {
        Method method = new MethodTestSubject().getDeclaredMethod("testMethod", String.class, int.class);
        String expectedToString = "InvocationMatcher{method=MethodTestSubject.testMethod(String, Int), mockedObject=<mocked>, argumentsToMatch=[], invocationLocation=InvocationLocationTestSubject{line=0, file=null}}";
        InvocationOnMock invocation = new InvocationOnMockImpl(Mockito.mock(Object.class), method, new LocationTestSubject(), new Object[]{ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()});
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.emptyList());

        String actualToString = matcher.toString();

        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Method method = new MethodTestSubject().getDeclaredMethod("testMethod", String.class, int.class);
        Object[] arguments = new Object[]{ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()};
        InvocationOnMock invocation = new InvocationOnMockImpl(Mockito.mock(Object.class), method, new LocationTestSubject(), arguments);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.argThat(new CapturesArguments() {
            private Object capturedArgument;

            @Override
            public boolean matches(Object argument) {
                capturedArgument = argument;
                return true;
            }

            public Object getCapturedArgument() {
                return capturedArgument;
            }
        })));

        matcher.captureArgumentsFrom(invocation);

        assertEquals("test", matcher.getCaptureArguments().get(0).getCapturedArgument());
        assertEquals(42, matcher.getCaptureArguments().get(1).getCapturedArgument());
    }

    @Test
    public void testSafelyArgumentsMatch() {
        InvocationOnMock invocation = new InvocationOnMockImpl(Mockito.mock(Object.class), new MethodTestSubject().getDeclaredMethod("testMethod", String.class, int.class), new LocationTestSubject(), new Object[]{ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()});
        Object[] actualArguments = invocation.getArguments();

        assertTrue(new InvocationMatcher(invocation, Collections.emptyList()).safelyArgumentsMatch(actualArguments));
    }

    @Test(expected = java.lang.RuntimeException.class)
    public void testArgumentsDontMatch() {
        InvocationOnMock invocation = new InvocationOnMockImpl(Mockito.mock(Object.class), new MethodTestSubject().getDeclaredMethod("testMethod", String.class, int.class), new LocationTestSubject(), new Object[]{ArgumentMatchers.anyString(), ArgumentMatchers.anyString()});
        Object[] actualArguments = invocation.getArguments();

        new InvocationMatcher(invocation, Collections.emptyList()).safelyArgumentsMatch(actualArguments);
    }
}

class MethodTestSubject {
    private static int counter = 0;

    public Method getDeclaredMethod(String name, Class<?>... parameterTypes) {
        try {
            return getClass().getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(String.format("Unexpected method missing: %s(%s)", name, Arrays.toString(parameterTypes)));
        }
    }

    public static class TestMethod1 {
        public void testMethod1(String arg1, int arg2) {
            // some implementation
        }
    }

    public static class TestMethod2 {
        public void testMethod2(int arg1, String arg2) {
            // some implementation
        }
    }
}

class LocationTestSubject implements Location {
    private static int counter = 0;

    @Override
    public String getFileName() {
        return "TestClass.java";
    }

    @Override
    public int getLine() {
        return counter++;
    }
}

class Invocations {
    // some implementation for creating mocks and invocations
}

class ArgumentsComparator extends ArgumentsComparator {
    @Override
    public boolean argumentsMatch(InvocationMatcher expected, InvocationMatcher actual) {
        return expected.matches(actual);
    }

    @Override
    public boolean argumentsMatch(InvocationMatcher expected, Object[] actualArguments) {
        return expected.safelyArgumentsMatch(actualArguments);
    }
}

class Arguments implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Object[] arguments;

    public Arguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public Object[] getArguments() {
        return arguments;
    }
}

class CapturesArguments implements Matcher<Object> {
    private Object capturedArgument;

    public boolean matches(Object argument) {
        capturedArgument = argument;
        return true;
    }

    public Object getCapturedArgument() {
        return capturedArgument;
    }
}