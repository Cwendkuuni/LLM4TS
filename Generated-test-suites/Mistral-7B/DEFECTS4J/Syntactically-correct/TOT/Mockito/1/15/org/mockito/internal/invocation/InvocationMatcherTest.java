import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMockImpl;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private InvocationOnMock mock;

    @Before
    public void setup() {
        mock = Mockito.mock(Object.class);
    }

    @Test
    public void testMatchesSameInvocation() {
        Invocation invocation = new InvocationOnMockImpl(mock, new Method(new Object() {}.getClass().getDeclaredMethod("testMethod", String.class, int.class)), new Object[]{"test", 1});
        matcher = new InvocationMatcher(invocation);
        Invocation sameInvocation = new InvocationOnMockImpl(mock, new Method(new Object() {}.getClass().getDeclaredMethod("testMethod", String.class, int.class)), new Object[]{"test", 1});

        assertTrue(matcher.matches(sameInvocation));
    }

    @Test
    public void testMatchesDifferentMethod() {
        Invocation invocation = new InvocationOnMockImpl(mock, new Method(new Object() {}.getClass().getDeclaredMethod("testMethod1", String.class)), new Object[]{"test"});
        matcher = new InvocationMatcher(invocation);
        Invocation differentInvocation = new InvocationOnMockImpl(mock, new Method(new Object() {}.getClass().getDeclaredMethod("testMethod2", int.class)), new Object[]{1});

        assertFalse(matcher.matches(differentInvocation));
    }

    @Test
    public void testMatchesDifferentMock() {
        Invocation invocation = new InvocationOnMockImpl(mock1, new Method(new Object() {}.getClass().getDeclaredMethod("testMethod", String.class, int.class)), new Object[]{"test", 1});
        matcher = new InvocationMatcher(invocation);
        Invocation differentInvocation = new InvocationOnMockImpl(mock2, new Method(new Object() {}.getClass().getDeclaredMethod("testMethod", String.class, int.class)), new Object[]{"test", 1});

        assertFalse(matcher.matches(differentInvocation));
    }

    @Test
    public void testMatchesOverloadedMethodWithSameArguments() {
        Invocation invocation = new InvocationOnMockImpl(mock, new Method(new Object() {}.getClass().getDeclaredMethod("testMethod", String.class)), new Object[]{"test"});
        Invocation overloadedInvocation = new InvocationOnMockImpl(mock, new Method(new Object() {}.getClass().getDeclaredMethod("testMethod", Integer.TYPE)), new Object[]{1});

        InvocationMatcher overloadedMatcher = new InvocationMatcher(overloadedInvocation);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"));
        matcher = new InvocationMatcher(invocation, matchers);

        assertTrue(matcher.matches(overloadedMatcher));
    }

    @Test
    public void testMatchesOverloadedMethodWithDifferentArguments() {
        Invocation invocation = new InvocationOnMockImpl(mock, new Method(new Object() {}.getClass().getDeclaredMethod("testMethod", String.class)), new Object[]{"test"});
        Invocation overloadedInvocation = new InvocationOnMockImpl(mock, new Method(new Object() {}.getClass().getDeclaredMethod("testMethod", Integer.TYPE)), new Object[]{1});

        InvocationMatcher overloadedMatcher = new InvocationMatcher(overloadedInvocation);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1));
        matcher = new InvocationMatcher(invocation, matchers);

        assertFalse(matcher.matches(overloadedMatcher));
    }

    @Test
    public void testMatchesVarargs() {
        Invocation invocation = new InvocationOnMockImpl(mock, new Method(new Object() {}.getClass().getDeclaredMethod("testMethod", String.class, String[].class)), new Object[]{"test", new String[]{"arg1", "arg2"}});
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.arrayContaining(ArgumentMatchers.contains("arg1")));
        matcher = new InvocationMatcher(invocation, matchers);

        Invocation varargsInvocation = new InvocationOnMockImpl(mock, new Method(new Object() {}.getClass().getDeclaredMethod("testMethod", String.class, String[].class)), new Object[]{"test", new String[]{"arg1", "arg2", "arg3"}});

        assertTrue(matcher.matches(varargsInvocation));
    }

    @Test
    public void testMatchesVarargsWithEmptyArray() {
        Invocation invocation = new InvocationOnMockImpl(mock, new Method(new Object() {}.getClass().getDeclaredMethod("testMethod", String.class, String[].class)), new Object[]{"test", new String[0]});
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.arrayContaining(ArgumentMatchers.isEmpty()));
        matcher = new InvocationMatcher(invocation, matchers);

        Invocation emptyVarargsInvocation = new InvocationOnMockImpl(mock, new Method(new Object() {}.getClass().getDeclaredMethod("testMethod", String.class, String[].class)), new Object[]{"test", new String[0]});

        assertTrue(matcher.matches(emptyVarargsInvocation));
    }
}