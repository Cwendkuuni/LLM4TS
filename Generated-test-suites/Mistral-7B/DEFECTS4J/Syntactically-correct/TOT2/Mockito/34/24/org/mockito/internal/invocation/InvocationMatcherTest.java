import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import org.mockito.internal.invocation.*;

public class InvocationMatcherTest {
    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, Arrays.asList(eq("test")));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation);

        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMethod()).thenReturn(mock(Method.class));
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        when(actualInvocation.getArguments()).thenReturn(new Object[0]);

        assertTrue(invocationMatcher1.matches(actualInvocation));
        assertTrue(invocationMatcher2.matches(actualInvocation));

        assertFalse(invocationMatcher1.matches(new InvocationMatcher(new Invocation(), Arrays.asList(eq("test2")))));
        assertFalse(invocationMatcher2.matches(new InvocationMatcher(new Invocation(), Arrays.asList(eq("test2")))));
    }
}