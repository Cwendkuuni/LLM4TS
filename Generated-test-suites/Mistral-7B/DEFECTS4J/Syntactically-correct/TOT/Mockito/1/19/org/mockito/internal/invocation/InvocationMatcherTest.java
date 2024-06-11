package org.mockito.internal.invocation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.quality.VerificationOrder;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;

class InvocationMatcherTest {

    @Test
    void testInvocationMatcher_emptyMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Method method = Mockito.mock(Method.class);

        Assertions.assertEquals(method, matcher.getMethod());
        Assertions.assertEquals(invocation, matcher.getInvocation());
        Assertions.assertEquals(Collections.emptyList(), matcher.getMatchers());
        Assertions.assertEquals("", matcher.toString());
        Assertions.assertTrue(matcher.matches(invocation));
    }

    @Test
    void testInvocationMatcher_withMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[]{1, "two", new Object()};
        List<Matcher> matchers = Arrays.asList(
                is(1),
                is("two"),
                instanceOf(Object.class)
        );
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        Method method = Mockito.mock(Method.class);

        Assertions.assertEquals(method, matcher.getMethod());
        Assertions.assertEquals(invocation, matcher.getInvocation());
        Assertions.assertIterableEquals(matchers, matcher.getMatchers());
        Assertions.assertEquals("Invocation matcher:\n" +
                "  method: <empty>\n" +
                "  arguments:\n" +
                "    [1, two, Object@1b6d393]\n" +
                "\n" +
                "  matchers:\n" +
                "    <1>\n" +
                "    <2>\n" +
                "    <java.lang.Object> (instance of: java.lang.Object)\n", matcher.toString());
        Assertions.assertTrue(matcher.matches(new InvocationOnMock(method, invocation, VerificationOrder.VERIFIED_LATE, new Object[0], new Object[0])));

        Assertions.assertThrows(UnsupportedOperationException.class, () -> matcher.captureArgumentsFrom(invocation));
    }

    @Test
    void testInvocationMatcher_matches_sameMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, method);

        Assertions.assertTrue(matcher1.matches(matcher2));
        Assertions.assertTrue(matcher2.matches(matcher1));
    }

    @Test
    void testInvocationMatcher_matches_differentMethods() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, method1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, method2);

        Assertions.assertFalse(matcher1.matches(matcher2));
        Assertions.assertFalse(matcher2.matches(matcher1));
    }

    @Test
    void testInvocationMatcher_matches_withArguments() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[]{1, "two", new Object()};
        List<Matcher> matchers = Arrays.asList(
                is(1),
                is("two"),
                instanceOf(Object.class)
        );

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        Object[] actualArguments = new Object[]{1, "two", new Object()};

        Assertions.assertTrue(matcher.matches(new InvocationOnMock(getMethod(), invocation, VerificationOrder.VERIFIED_LATE, actualArguments, new Object[0])));
        Assertions.assertTrue(matcher.safelyArgumentsMatch(actualArguments));
    }

    @Test
    void testInvocationMatcher_matches_differentArguments() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[]{1, "two", new Object()};
        List<Matcher> matchers = Arrays.asList(
                is(1),
                is("two"),
                instanceOf(Object.class)
        );
        Object[] differentArguments = new Object[]{2, "two", new Object()};

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Assertions.assertFalse(matcher.matches(new InvocationOnMock(getMethod(), invocation, VerificationOrder.VERIFIED_LATE, differentArguments, new Object[0])));
        Assertions.assertFalse(matcher.safelyArgumentsMatch(differentArguments));
    }

    @Test
    void testInvocationMatcher_matches_varargs() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[]{1, "two", new Object(), new Object[]{3, 4, 5}};
        List<Matcher> matchers = Arrays.asList(
                is(1),
                is("two"),
                instanceOf(Object.class),
                contains(is(3), is(4), is(5))
        );

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Assertions.assertTrue(matcher.matches(new InvocationOnMock(getMethod(), invocation, VerificationOrder.VERIFIED_LATE, arguments, new Object[0])));
        matcher.captureArgumentsFrom(invocation);

        for (int i = 0; i < matchers.size(); i++) {
            if (matchers.get(i) instanceof CapturesArguments) {
                Matcher capturedMatcher = ((CapturesArguments) matchers.get(i)).getCapturedValue();
                if (i == 3) {
                    Assertions.assertArrayEquals(new Object[]{3, 4, 5}, capturedMatcher.getValue());
                } else {
                    Assertions.assertNotNull(capturedMatcher.getValue());
                    Assertions.assertEquals(arguments[i], capturedMatcher.getValue());
                }
            }
        }
    }

    @Test
    void testInvocationMatcher_doesNotMatch_varargs_differentSize() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[]{1, "two", new Object(), new Object[]{3, 4, 5, 6}};
        List<Matcher> matchers = Arrays.asList(
                is(1),
                is("two"),
                instanceOf(Object.class),
                contains(is(3), is(4), is(5))
        );

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Assertions.assertFalse(matcher.matches(new InvocationOnMock(getMethod(), invocation, VerificationOrder.VERIFIED_LATE, arguments, new Object[0])));
    }

    @Test
    void testInvocationMatcher_doesNotMatch_varargs_differentTypes() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[]{1, "two", new Object(), new Object[]{3, 4, new Integer(5)}};
        List<Matcher> matchers = Arrays.asList(
                is(1),
                is("two"),
                instanceOf(Object.class),
                contains(is(3), is(4), is(5))
        );

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Assertions.assertFalse(matcher.matches(new InvocationOnMock(getMethod(), invocation, VerificationOrder.VERIFIED_LATE, arguments, new Object[0])));
    }

    private Method getMethod() {
        try {
            return InvocationMatcher.class.getMethod("toString");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}