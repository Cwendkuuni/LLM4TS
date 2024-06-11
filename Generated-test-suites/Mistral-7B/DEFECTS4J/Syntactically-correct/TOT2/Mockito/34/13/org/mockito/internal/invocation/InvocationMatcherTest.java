import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.matchers.Arguments.notNull;
import static org.junit.Assert.*;

public class InvocationMatcherTest {

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation actualInvocation = mock(Invocation.class);

        assertTrue(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testDifferentInvocationArguments() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Object[] actualArgs = new Object[]{"arg1", "arg2"};
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getArguments()).thenReturn(actualArgs);

        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testDifferentMocks() {
        Invocation invocation1 = mock(Invocation.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = mock(Invocation.class);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testDifferentMethods() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).then(invocation -> {
            Method method = new Method("differentMethodName", String[].class);
            return method;
        });

        assertFalse(invocationMatcher.matches(candidate));
    }
}