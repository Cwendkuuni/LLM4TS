import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;

public class InvocationMatcherTest {
    @Test
    public void testInvocationMatcherConstruction() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertNotNull(matcher);
        assertEquals(invocation, matcher.getInvocation());
        assertTrue(matcher.getMatchers().isEmpty());
    }
}