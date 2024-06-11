import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher;

public class InvocationMatcherTest {
    private Invocation invocation;
    private InvocationMatcher matcher;
    private Invocation actual;

    @Before
    public void setup() {
        invocation = new Invocation(new Location(), new Object[]{}, new Method("testMethod", new Class[0]), new Class[0]);
        actual = new Invocation(new Location(), new Object[]{"test"}, new Method("testMethod", new Class[0]), new Class[0]);
        matcher = new InvocationMatcher(invocation, Collections.singletonList(ArgumentsMatcher.is("test")));
    }

    @Test
    public void testMatches() {
        Assert.assertTrue(matcher.matches(actual));
    }

    @Test
    public void testDifferentMocks() {
        InvocationMatcher matcher = new InvocationMatcher(new Invocation(new Location(), new Object[]{}, new Method("testMethod1", new Class[0]), new Class[0]), Collections.singletonList(ArgumentsMatcher.is("test")));
        Invocation actual = new Invocation(new Location(), new Object[]{"test2"}, new Method("testMethod2", new Class[0]), new Class[0]);
        Assert.assertFalse(matcher.matches(actual));
    }

    @Test
    public void testDifferentMethods() {
        InvocationMatcher matcher = new InvocationMatcher(new Invocation(new Location(), new Object[]{}, new Method("testMethod1", new Class[0]), new Class[0]), Collections.singletonList(ArgumentsMatcher.is("test")));
        Invocation actual = new Invocation(new Location(), new Object[]{}, new Method("testMethod2", new Class[0]), new Class[0]);
        Assert.assertFalse(matcher.matches(actual));
    }

    @Test
    public void testDifferentArguments() {
        InvocationMatcher matcher = new InvocationMatcher(new Invocation(new Location(), new Object[]{"arg1"}, new Method("testMethod", new Class[0]), new Class[0]), Collections.singletonList(ArgumentsMatcher.is("test")));
        Invocation actual = new Invocation(new Location(), new Object[]{"arg2"}, new Method("testMethod", new Class[0]), new Class[0]);
        Assert.assertFalse(matcher.matches(actual));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArguments() {
        InvocationMatcher matcher = new InvocationMatcher(new Invocation(new Location(), new Object[]{}, new Method("testMethod", new Class[0]), new Class[0]), Collections.singletonList(ArgumentsMatcher.is("test")));
        Invocation actual = new Invocation(new Location(), null, new Method("testMethod", new Class[0]), new Class[0]);
    }
}