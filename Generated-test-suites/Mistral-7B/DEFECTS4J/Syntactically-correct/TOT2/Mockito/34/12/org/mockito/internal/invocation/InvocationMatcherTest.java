import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.ArgumentsMatcher;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.*;

public class InvocationMatcherTest {

    @Test
    public void testDefaultConstructor() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        assertEquals(invocation, matcher.getInvocation());
        assertNull(matcher.getMatchers());
    }
}