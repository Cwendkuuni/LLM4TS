import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Invocation invocation;
    private Invocation otherInvocation;

    @Before
    public void setUp() {
        Object mock = Mockito.mock(Object.class);
        invocation = Mockito.mock(Invocation.class);
        otherInvocation = Mockito.mock(Invocation.class);
        Method method = Mockito.spy(Mockito.method(Integer.class, "testMethod", int.class, int.class));
        matcher = new InvocationMatcher(new DescribedInvocation(mock, method, Arrays.asList(ArgumentMatchers.intThat(i -> i > 0), ArgumentMatchers.intThat(i -> i < 10)), new Location("TestClass.java", 1, 1)));
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(otherInvocation.getMethod()).thenReturn(method);
    }

    @Test
    public void testMatchesSameMethodAndArguments() {
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{1, 2});
        Mockito.when(otherInvocation.getArguments()).thenReturn(new Object[]{1, 2});
        assertTrue(matcher.matches(invocation));
        assertTrue(matcher.matches(otherInvocation));
    }

    @Test
    public void testMatchesDifferentMethods() {
        Method otherMethod = Mockito.spy(Mockito.method(Integer.class, "otherMethod", int.class));
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(otherInvocation.getMethod()).thenReturn(otherMethod);
        assertFalse(matcher.matches(otherInvocation));
    }

    @Test
    public void testMatchesSameMethodButDifferentArguments() {
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{1, 2});
        Mockito.when(otherInvocation.getArguments()).thenReturn(new Object[]{1, 3});
        assertFalse(matcher.matches(otherInvocation));
    }

    @Test
    public void testMatchesSameMethodWithVarargs() {
        Method methodWithVarargs = Mockito.spy(Mockito.method(Integer.class, "testMethodWithVarargs", int.class, int[].class));
        matcher = new InvocationMatcher(new DescribedInvocation(mock, methodWithVarargs, Collections.emptyList(), new Location("TestClass.java", 1, 1)));
        Mockito.when(invocation.getMethod()).thenReturn(methodWithVarargs);
        Mockito.when(otherInvocation.getMethod()).thenReturn(methodWithVarargs);

        int[] invocationArgs = new int[]{1, 2};
        Mockito.when(invocation.getArguments()).thenReturn(invocationArgs);
        Mockito.when(otherInvocation.getArguments()).thenReturn(new int[]{1, 2, 3});

        matcher.captureArgumentsFrom(invocation);
        matcher.captureArgumentsFrom(otherInvocation);

        assertTrue(matcher.matches(invocation));
        assertFalse(matcher.matches(otherInvocation));
    }
}