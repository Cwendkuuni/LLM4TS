import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.reporting.PrintSettings;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.junit.Assert.assertEquals;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstruction() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new ArgumentsMatcher("arg1"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
        assertEquals("Invocation [method=Method[public void testMethod(java.lang.String, java.lang.String)], target=<mock> [org.mockito.internal.invocation.Invocation], arguments=[arg1]]", invocationMatcher.toString());
        verifyNoMoreInteractions(invocation);
    }

    // ... other test cases for other constructors and methods go here
}