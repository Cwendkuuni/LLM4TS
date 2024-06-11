import org.junit.Assert;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.Mockito;

public class InvocationMatcherTest {

    @Test
    public void testValidInvocationMatcherConstruction() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher im = new InvocationMatcher(invocation);
        Assert.assertNotNull(im);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInvocationMatcherConstruction() {
        Invocation invocation = null;
        InvocationMatcher im = new InvocationMatcher(invocation);
    }
}