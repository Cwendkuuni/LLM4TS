import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherInitialization() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocation.getMethod();
        Invocation getInvocation = invocationMatcher.getInvocation();
        List<Matcher> matchers = invocationMatcher.getMatchers();

        assertEquals(invocation, getInvocation);
        assertEquals(method, invocationMatcher.getMethod());
        assertTrue(matchers.isEmpty());
    }
}