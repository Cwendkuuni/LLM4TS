import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationWithMocks;
import org.mockito.invocation.Invocation;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithInvocationAndEmptyMatchers() throws Exception {
        Invocation invocation = new InvocationWithMocks("mock", new Method(Test.class.getMethod("testMethod", Integer.class, String.class)), new Object[]{1, "test"});
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Assert.assertNotNull(matcher);
        Assert.assertEquals(invocation, matcher.getInvocation());
        Assert.assertEquals(Collections.emptyList(), matcher.getMatchers());
        Assert.assertEquals("Invocation[this=InvocationWithMocks@6bf51632, method=public static void testMethod(int,java.lang.String) in class Test, args=[1, test]],", matcher.toString());
    }

    // ... continuing with tests for other methods
}