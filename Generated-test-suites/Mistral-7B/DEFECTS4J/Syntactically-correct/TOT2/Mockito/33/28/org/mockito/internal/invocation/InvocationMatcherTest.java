import org.junit.jupiter.api.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.Invocation;
import static org.junit.jupiter.api.Assertions.*;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithInvocationAndEmptyMatchers() {
        Invocation invocation = new Invocation(new Object() {}.getClass().getMethod("testMethod", String.class), new Object[]{"testarg"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = invocationMatcher.getMethod();
        assertEquals(method.getName(), "testMethod");
        assertEquals(invocationMatcher.getInvocation(), invocation);
        assertNotNull(invocationMatcher.getMatchers());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }
}