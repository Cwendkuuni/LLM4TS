import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMockImpl;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationOnMockInvocation;
import org.mockito.internal.matchers.ArgThat;
import org.mockito.internal.reporting.PrintSettings;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher() {
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "testMethod", new Class[]{Integer.class, String.class});
        Object[] invocationArguments = new Object[]{1, "hello"};

        Invocation invocation = new InvocationOnMockInvocation(new InvocationOnMockImpl(mock, method, invocationArguments));
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        List<Matcher> expectedMatchers = Arrays.asList(new ArgThat<Integer>() {
            @Override
            public boolean matches(Integer argument) {
                return argument.equals(1);
            }
        }, new ArgThat<String>() {
            @Override
            public boolean matches(String argument) {
                return argument.equals("hello");
            }
        });

        InvocationMatcher matcherWithMatchers = new InvocationMatcher(invocation, expectedMatchers);

        assertTrue(matcher.matches(new InvocationOnMockInvocation(new InvocationOnMockImpl(mock, method, invocationArguments))));
        assertTrue(matcherWithMatchers.matches(new InvocationOnMockInvocation(new InvocationOnMockImpl(mock, method, invocationArguments))));

        Object[] differentArguments = new Object[]{2, "world"};
        Invocation differentInvocation = new InvocationOnMockInvocation(new InvocationOnMockImpl(mock, method, differentArguments));

        assertFalse(matcher.matches(differentInvocation));
        assertFalse(matcherWithMatchers.matches(differentInvocation));

        List<Matcher> emptyMatchers = Collections.emptyList();
        InvocationMatcher emptyMatcher = new InvocationMatcher(invocation, emptyMatchers);

        Invocation emptyInvocation = Mockito.mock(Invocation.class);
        emptyInvocation.setMethod(method);

        assertTrue(emptyMatcher.matches(emptyInvocation));

        List<Matcher> wrongClassMatcher = Arrays.asList(new ArgThat<String>() {
            @Override
            public boolean matches(Integer argument) {
                return false;
            }
        });

        InvocationMatcher wrongMatcher = new InvocationMatcher(invocation, wrongClassMatcher);

        assertFalse(wrongMatcher.matches(new InvocationOnMockInvocation(new InvocationOnMockImpl(mock, method, invocationArguments))));

        List<Matcher> wrongMethodMatcher = Arrays.asList(new ArgThat<Object>() {
            @Override
            public boolean matches(Object argument) {
                Method argumentMethod = argument.getClass().getMethod("testMethod", new Class[]{Integer.class, String.class});
                return !argumentMethod.equals(method);
            }
        });

        InvocationMatcher wrongMethodMatcherMatcher = new InvocationMatcher(invocation, wrongMethodMatcher);

        assertFalse(wrongMethodMatcherMatcher.matches(new InvocationOnMockInvocation(new InvocationOnMockImpl(mock, method, invocationArguments))));

        List<Matcher> wrongMockMatcher = Arrays.asList(Mockito.mock(Object.class));

        InvocationMatcher wrongMockMatcherMatcher = new InvocationMatcher(new InvocationOnMockInvocation(new InvocationOnMockImpl(wrongMockMatcher.get(0), method, invocationArguments)), wrongMockMatcher);

        assertFalse(wrongMockMatcherMatcher.matches(new InvocationOnMockInvocation(new InvocationOnMockImpl(mock, method, invocationArguments))));
    }

    @Test
    public void testCreateFromInvocations() {
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "testMethod", new Class[]{Integer.class, String.class});

        Invocation invocation1 = Mockito.mock(Invocation.class);
        invocation1.setMethod(method);
        Object[] args1 = new Object[]{1, "hello"};
        invocation1.setArguments(args1);

        Invocation invocation2 = Mockito.mock(Invocation.class);
        invocation2.setMethod(method);
        Object[] args2 = new Object[]{2, "world"};
        invocation2.setArguments(args2);

        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        for (InvocationMatcher matcher : matchers) {
            assertTrue(matcher.matches(new InvocationOnMockInvocation(invocation1)));
            assertFalse(matcher.matches(new InvocationOnMockInvocation(invocation2)));
        }
    }

    @Test(expected = AssertionError.class)
    public void testArgumentsComparatorThrowsException() {
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "testMethod", new Class[]{Integer.class, String.class});
        Object[] invocationArguments = new Object[]{1, "hello"};

        Invocation invocation = new InvocationOnMockInvocation(new InvocationOnMockImpl(mock, method, invocationArguments));

        try {
            new InvocationMatcher(invocation).matches(null);
        } catch (NullPointerException expected) {
            // expected
        }

        InvocationMatcher matcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = Arrays.asList(Mockito.mock(Matcher.class));

        try {
            matcher.matches(matchers);
        } catch (ClassCastException expected) {
            // expected
        }

        try {
            matcher.matches(new Object[]{});
        } catch (AssertionError expected) {
            // expected
        }

        try {
            matcher.matches(new Object[2]);
        } catch (AssertionError expected) {
            // expected
        }

        try {
            matcher.matches(new String[2]);
        } catch (AssertionError expected) {
            // expected
        }

        try {
            matcher.matches(new PrintSettings());
        } catch (AssertionError expected) {
            // expected
        }

        try {
            matcher.matches("hello");
        } catch (ClassCastException expected) {
            // expected
        }

        try {
            matcher.matches(1);
        } catch (ClassCastException expected) {
            // expected
        }

        try {
            matcher.matches(true);
        } catch (ClassCastException expected) {
            // expected
        }

        try {
            matcher.matches((Object) null);
        } catch (NullPointerException expected) {
            // expected
        }

        try {
            matcher.matches(new InvocationMatcher(new InvocationOnMockInvocation(new InvocationOnMockImpl(mock, method, invocationArguments))));
        } catch (AssertionError expected) {
            // expected
        }

        try {
            matcher.matches(new InvocationMatcher(new InvocationOnMockInvocation(new InvocationOnMockImpl(mock, method))));
        } catch (AssertionError expected) {
            // expected
        }

        try {
            matcher.matches(new InvocationMatcher(new InvocationOnMockInvocation(new InvocationOnMockImpl(mock, method, new Object[2]))));
        } catch (AssertionError expected) {
            // expected
        }

        try {
            matcher.matches(new InvocationMatcher(new InvocationOnMockInvocation(new InvocationOnMockImpl(mock, null, invocationArguments))));
        } catch (NullPointerException expected) {
            // expected
        }

        try {
            matcher.matches(new InvocationMatcher(new InvocationOnMockInvocation(new InvocationOnMockImpl(null, method, invocationArguments))));
        } catch (NullPointerException expected) {
            // expected
        }

        try {
            matcher.matches(new InvocationMatcher(new InvocationOnMockInvocation(new InvocationOnMockImpl(mock, method, invocationArguments)), Arrays.asList(Mockito.mock(Matcher.class))));
        } catch (AssertionError expected) {
            // expected
        }

        try {
            matcher.matches(new InvocationMatcher(new InvocationOnMockInvocation(new InvocationOnMockImpl(mock, method, invocationArguments)), Arrays.asList(Mockito.mock(Matcher.class), Mockito.mock(Matcher.class))));
        } catch (AssertionError expected) {
            // expected
        }

        try {
            matcher.matches(new InvocationMatcher(new InvocationOnMockInvocation(new InvocationOnMockImpl(mock, method, invocationArguments)), Arrays.asList(Mockito.mock(Matcher.class), Mockito.mock(Matcher.class), Mockito.mock(Matcher.class))));
        } catch (AssertionError expected) {
            // expected
        }
    }
}