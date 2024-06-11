import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructionWithInvocationAndEmptyMatcherList() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertNotNull(invocationMatcher.getMatchers());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testEmptyInvocationMatcherMatchesEmptyInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(mock(Invocation.class));
        Invocation invocation = mock(Invocation.class);
        Invocation emptyInvocation = mock(Invocation.class);
        when(invocationMatcher.matches(invocation)).thenReturn(true);
        when(invocationMatcher.matches(emptyInvocation)).thenReturn(true);

        assertTrue(invocationMatcher.matches(invocation));
        assertTrue(invocationMatcher.matches(emptyInvocation));
    }
}