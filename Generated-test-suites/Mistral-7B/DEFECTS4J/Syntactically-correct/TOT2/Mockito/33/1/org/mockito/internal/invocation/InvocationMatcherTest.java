import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.mock.MockInvocation;
import org.mockito.internal.mock.MockMethod;
import org.mockito.internal.reporting.PrintSettings;
import static org.junit.Assert.assertEquals;

public class InvocationMatcherTest {
    @Test
    public void testGetMethod() {
        MockMethod mockMethod = new MockMethod("testMethod", new Class[0]);
        MockInvocation mockInvocation = new MockInvocation("testClass", new Object[0], mockMethod);
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, Collections.emptyList());
        Method expectedMethod = mockMethod.getMethod();
        Method actualMethod = invocationMatcher.getMethod();
        assertEquals(expectedMethod, actualMethod);
    }
}