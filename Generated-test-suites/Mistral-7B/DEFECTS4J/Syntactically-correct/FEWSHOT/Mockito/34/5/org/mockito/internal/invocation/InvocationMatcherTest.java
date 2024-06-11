package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.Arguments.not;
import static org.mockito.internal.matchers.Arguments.same;
import static org.mockito.internal.matchers.Arguments.withArray;
import static org.mockito.internal.matchers.Arguments.withClass;
import static org.mockito.internal.matchers.Arguments.withInteger;
import static org.mockito.internal.matchers.Arguments.withNull;
import static org.mockito.internal.matchers.Arguments.withString;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.reporting.PrintingFriendlyMockitoInvocation;
import org.mockito.internal.stubbing.answers.CallsRealMethod;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherEmptyMatchers() {
        Invocation invocation = new PrintingFriendlyMockitoInvocation(new Location("TestClass.java", 1), Mockito.method(Object.class, "method"), Arrays.asList("arg1", "arg2"), new Object[0]);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.emptyList());

        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void testInvocationMatcherSingleMatcher() {
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "method", new Class[] { Integer.TYPE, String.class });
        Object[] arguments = new Object[] { 1, "arg2" };
        Invocation invocation = new PrintingFriendlyMockitoInvocation(new Location("TestClass.java", 1), method, arguments, new Object[0]);

        Matcher<Integer> integerMatcher = withInteger(1);

        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.singletonList(integerMatcher));

        assertTrue(matcher.matches(invocation));
        assertTrue(integerMatcher.matches(invocation.getArguments()[0]));
    }

    @Test
    public void testInvocationMatcherMultipleMatchers() {
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "method", new Class[] { Integer.TYPE, String.class });
        Object[] arguments = new Object[] { 1, "arg2" };
        Invocation invocation = new PrintingFriendlyMockitoInvocation(new Location("TestClass.java", 1), method, arguments, new Object[0]);

        Matcher<Integer> integerMatcher = withInteger(1);
        Matcher<String> stringMatcher = withString("arg2");

        InvocationMatcher matcher = new InvocationMatcher(invocation, Arrays.asList(integerMatcher, stringMatcher));

        assertTrue(matcher.matches(invocation));
        assertTrue(integerMatcher.matches(invocation.getArguments()[0]));
        assertTrue(stringMatcher.matches(invocation.getArguments()[1]));
    }

    @Test
    public void testInvocationMatcherNoMatch() {
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "method", new Class[] { Integer.TYPE, String.class });
        Object[] arguments = new Object[] { 2, "arg2" };
        Invocation invocation = new PrintingFriendlyMockitoInvocation(new Location("TestClass.java", 1), method, arguments, new Object[0]);

        Matcher<Integer> integerMatcher = withInteger(1);

        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.singletonList(integerMatcher));

        assertFalse(matcher.matches(invocation));
    }

    @Test
    public void testInvocationMatcherNoMatchDifferentMethod() {
        Object mock = Mockito.mock(Object.class);
        Method method1 = Mockito.method(Object.class, "method1", new Class[0]);
        Method method2 = Mockito.method(Object.class, "method2", new Class[] { Integer.TYPE });
        Object[] arguments = new Object[] { 1 };
        Invocation invocation1 = new PrintingFriendlyMockitoInvocation(new Location("TestClass.java", 1), method1, arguments, new Object[0]);
        Invocation invocation2 = new PrintingFriendlyMockitoInvocation(new Location("TestClass.java", 1), method2, arguments, new Object[0]);

        Matcher<Object> mockMatcher = same(mock);

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, Collections.singletonList(mockMatcher));
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, Collections.singletonList(mockMatcher));

        assertFalse(matcher1.matches(invocation2));
        assertFalse(matcher2.matches(invocation1));
    }

    @Test
    public void testInvocationMatcherCaptureArguments() {
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "method", new Class[] { Integer.TYPE, String.class });
        Object[] arguments = new Object[] { 1, "arg2" };
        Invocation invocation = new PrintingFriendlyMockitoInvocation(new Location("TestClass.java", 1), method, arguments, new Object[0]);

        List<Matcher> matchers = Arrays.asList(withInteger(1), new CapturesArguments<Object>() {
            @Override
            public void captureFrom(Object value) {
                assertEquals("arg2", value);
            }
        });

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void testInvocationMatcherCaptureArgumentsNull() {
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "method", new Class[] { Integer.TYPE, String.class });
        Object[] arguments = new Object[] { 1, "arg2" };
        Invocation invocation = new PrintingFriendlyMockitoInvocation(new Location("TestClass.java", 1), method, arguments, new Object[0]);

        List<Matcher> matchers = Arrays.asList(withInteger(1), new CapturesArguments<Object>() {
            @Override
            public void captureFrom(Object value) {
                assertNull(value);
            }
        });

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void testInvocationMatcherCaptureArgumentsDifferentTypes() {
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "method", new Class[] { Integer.TYPE, String.class });
        Object[] arguments = new Object[] { 1, "arg2" };
        Invocation invocation = new PrintingFriendlyMockitoInvocation(new Location("TestClass.java", 1), method, arguments, new Object[0]);

        List<Matcher> matchers = Arrays.asList(withInteger(1), new CapturesArguments<String>() {
            @Override
            public void captureFrom(String value) {
                assertEquals("arg1", value);
            }
        });

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertTrue(matcher.matches(invocation));
        assertFalse(matcher.matches(new PrintingFriendlyMockitoInvocation(new Location("TestClass.java", 1), method, new Object[] { 2 }, new Object[0])));
    }

    @Test
    public void testInvocationMatcherCaptureArgumentsSameType() {
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "method", new Class[] { Integer.TYPE, Integer.TYPE });
        Object[] arguments = new Object[] { 1, 2 };
        Invocation invocation = new PrintingFriendlyMockitoInvocation(new Location("TestClass.java", 1), method, arguments, new Object[0]);

        List<Matcher> matchers = Arrays.asList(withInteger(1), new CapturesArguments<Integer>() {
            @Override
            public void captureFrom(Integer value) {
                assertEquals(2, value);
            }
        });

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void testInvocationMatcherWithAnswer() {
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "method", new Class[] { Integer.TYPE, String.class });
        Object[] arguments = new Object[] { 1, "arg2" };
        Invocation invocation = new PrintingFriendlyMockitoInvocation(new Location("TestClass.java", 1), method, arguments, new Object[0]);

        Answer<Integer> answer = new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                assertEquals(1, invocation.getArguments()[0]);
                assertEquals("arg2", invocation.getArguments()[1]);
                return 42;
            }
        };

        List<Matcher> matchers = Arrays.asList(withInteger(1), withString("arg2"));

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        Invocation result = Mockito.doAnswer(answer).invokeMock(mock, new Object[] { 1, "arg2" });

        assertTrue(matcher.matches(result));
    }

    @Test
    public void testInvocationMatcherWithAnswerCaptureArguments() {
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "method", new Class[] { Integer.TYPE, String.class });
        Object[] arguments = new Object[] { 1, "arg2" };
        Invocation invocation = new PrintingFriendlyMockitoInvocation(new Location("TestClass.java", 1), method, arguments, new Object[0]);

        List<Matcher> matchers = Arrays.asList(withInteger(1), new CapturesArguments<String>() {
            @Override
            public void captureFrom(String value) {
                assertEquals("arg1", value);
            }
        });

        Answer<Integer> answer = new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                assertEquals(1, invocation.getArguments()[0]);
                assertEquals("arg2", invocation.getArguments()[1]);
                captureArgumentsFromInvocation(invocation);
                return 42;
            }
        };

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Invocation result = Mockito.doAnswer(answer).invokeMock(mock, new Object[] { 1, "arg2" });

        assertTrue(matcher.matches(result));
    }

    @Test
    public void testInvocationMatcherWithNullMatcher() {
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "method", new Class[] { Integer.TYPE, String.class });
        Object[] arguments = new Object[] { 1, "arg2" };
        Invocation invocation = new PrintingFriendlyMockitoInvocation(new Location("TestClass.java", 1), method, arguments, new Object[0]);

        List<Matcher> matchers = Arrays.asList(withInteger(1), null);

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertFalse(matcher.matches(invocation));
    }

    @Test
    public void testInvocationMatcherWithEmptyMatcherList() {
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "method", new Class[] { Integer.TYPE, String.class });
        Object[] arguments = new Object[] { 1, "arg2" };
        Invocation invocation = new PrintingFriendlyMockitoInvocation(new Location("TestClass.java", 1), method, arguments, new Object[0]);

        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.emptyList());

        assertTrue(matcher.matches(invocation));
    }
}