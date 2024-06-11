import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.InvocationOnMockVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Object mock;

    @Before
    public void setUp() {
        mock = mock(Object.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[]{1, "test", mock};
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq("test"), ArgumentMatchers.eq(mock)));
        matcher = new InvocationMatcher(invocationMatcher);
    }

    @Test
    public void testMatches() {
        Invocation invocationToTest = Mockito.mock(Invocation.class);
        when(invocationToTest.getMethod()).thenReturn(getMethodWithName("testMethod", Object[].class));
        when(invocationToTest.getMock()).thenReturn(mock);
        when(invocationToTest.getArguments()).thenReturn(arguments);

        verify(invocationMatcher).matches(invocationToTest);
    }

    @Test
    public void testDoesNotMatchDifferentMethod() {
        Invocation invocationToTest = Mockito.mock(Invocation.class);
        when(invocationToTest.getMethod()).thenReturn(getMethodWithName("otherMethod", int[].class));

        verify(matcher).matches(invocationToTest);

        verifyZeroInteractions(matcher);
    }

    @Test
    public void testDoesNotMatchDifferentMock() {
        Object otherMock = mock(Object.class);
        Invocation invocationToTest = Mockito.mock(Invocation.class);
        when(invocationToTest.getMethod()).thenReturn(getMethodWithName("testMethod", Object[].class));
        when(invocationToTest.getMock()).thenReturn(otherMock);

        verifyZeroInteractions(matcher);
    }

    @Test
    public void testDoesNotMatchDifferentArguments() {
        Invocation invocationToTest = Mockito.mock(Invocation.class);
        when(invocationToTest.getMethod()).thenReturn(getMethodWithName("testMethod", Object[].class));
        when(invocationToTest.getMock()).thenReturn(mock);
        Object[] differentArguments = new Object[]{2, "test", new Object()};

        verifyZeroInteractions(matcher);
    }

    @Test
    public void testDoesNotMatchOverloadedMethodWithSameArguments() {
        Invocation invocationToTest = Mockito.mock(Invocation.class);
        when(invocationToTest.getMethod()).thenReturn(getMethodWithName("testMethod", Object[].class));
        when(invocationToTest.getMock()).thenReturn(mock);

        InvocationMatcher invocationMatcherWithVarargs = new InvocationMatcher(invocationToTest, Arrays.asList(ArgumentMatchers.contains("test")));
        InvocationMatcher invocationMatcherWithoutVarargs = new InvocationMatcher(invocationToTest);

        verifyZeroInteractions(invocationMatcherWithVarargs);
        verifyZeroInteractions(invocationMatcherWithoutVarargs);
    }

    @Test
    public void testMatchesOverloadedMethodWithSameArguments() {
        Invocation invocationToTest = Mockito.mock(Invocation.class);
        Invocation invocationWithVarargs = Mockito.mock(Invocation.class);

        when(invocationToTest.getMethod()).thenReturn(getMethodWithName("testMethod1", String[].class));
        when(invocationWithVarargs.getMethod()).thenReturn(getMethodWithName("testMethod2", String[].class));
        when(invocationToTest.getMock()).thenReturn(mock);
        when(invocationWithVarargs.getMock()).thenReturn(mock);

        InvocationMatcher invocationMatcherWithVarargs = new InvocationMatcher(invocationWithVarargs, Arrays.asList(ArgumentMatchers.contains("test")));
        InvocationMatcher invocationMatcherWithoutVarargs = new InvocationMatcher(invocationToTest);

        verify(invocationMatcherWithVarargs).matches(invocationToTest);
        verify(invocationMatcherWithoutVarargs).matches(invocationWithVarargs);
    }

    private Method getMethodWithName(String name, Class<?>[] parameterTypes) {
        try {
            Class<?> clazz = InvocationMatcher.class;
            return clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}