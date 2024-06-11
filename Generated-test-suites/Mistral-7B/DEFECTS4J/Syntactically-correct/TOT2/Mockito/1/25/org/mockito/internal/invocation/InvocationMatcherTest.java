import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatcherList() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        verify(invocation).getMethod();
        verify(invocation).getLocation();
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullMatcherList() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeSizeMatcherList() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.remove(0); // remove one element to make the size negative
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testConstructorWithInvocationAndMatcherList() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        verify(invocation).getMethod();
        verify(invocation).getLocation();
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = invocationMatcher.getMethod();

        verify(invocation).getMethod();
        assertNotNull(method);
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation result = invocationMatcher.getInvocation();

        verify(invocation).getMock();
        assertSame(invocation, result);
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        List<Matcher> result = invocationMatcher.getMatchers();

        verify(invocation).getArguments();
        assertSame(matchers, result);
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String result = invocationMatcher.toString();

        String expected = new PrintSettings().print(matchers, invocation);
        assertEquals(expected, result);
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, Collections.emptyList());

        verify(invocation1).getMock();
        verify(invocation1).getMethod();
        verify(invocation1).getArguments();
        verify(invocation2).getMock();
        verify(invocation2).getMethod();
        verify(invocation2).getArguments();

        assertTrue(invocationMatcher.matches(invocation1));
        assertFalse(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Object[] actualArgs = new Object[0];
        assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));

        Object[] actualArgsWithThrowable = new Object[1];
        actualArgsWithThrowable[0] = new RuntimeException();
        assertFalse(invocationMatcher.safelyArgumentsMatch(actualArgsWithThrowable));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);

        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("testMethod");
        when(method2.getName()).thenReturn("testMethod");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, Collections.emptyList());

        assertTrue(invocationMatcher.hasSimilarMethod(invocation1));
        assertTrue(invocationMatcher.hasSimilarMethod(invocation2));
        assertFalse(invocationMatcher.hasSimilarMethod(mock(Invocation.class)));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);

        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("testMethod");
        when(method2.getName()).thenReturn("testMethod");
        when(method1.getParameterTypes()).thenReturn(new Class[]{int.class, String.class});
        when(method2.getParameterTypes()).thenReturn(new Class[]{String.class, int.class});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, Collections.emptyList());

        assertFalse(invocationMatcher.hasSameMethod(invocation2));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Location result = invocationMatcher.getLocation();

        verify(invocation).getLocation();
        assertSame(invocation.getLocation(), result);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<>();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Object[] arguments = new Object[1];
        arguments[0] = "test";
        invocationMatcher.captureArgumentsFrom(invocation);

        for (Matcher matcher : matchers) {
            if (matcher instanceof CapturesArguments) {
                CapturesArguments capturesArguments = (CapturesArguments) matcher;
                assertEquals("test", capturesArguments.getCapturedValue());
            }
        }
    }
}