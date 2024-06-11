import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private Method method;
    private List<Matcher> matchers;

    @Before
    public void setUp() throws Exception {
        Object mock = Mockito.mock(Object.class);
        method = Mockito.method(Object.class, "testMethod", new Class[0]);
        invocation = new Invocation(new Location(1, 1), method, mock, new Object[0]);
        matchers = Arrays.asList(ArgumentMatchers.anyInt());
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testMatches() {
        Invocation invocationToTest = new Invocation(new Location(1, 1), method, mock, new Object[]{1});
        InvocationMatcher invocationMatcherToTest = new InvocationMatcher(invocationToTest, matchers);

        assertTrue(invocationMatcher.matches(invocationMatcherToTest));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocationToTest = new Invocation(new Location(1, 1), method, mock, new Object[]{1});
        InvocationMatcher invocationMatcherToTest = new InvocationMatcher(invocationToTest, matchers);

        Invocation invocationToCompare = new Invocation(new Location(1, 1), method, mock, new Object[]{2});
        InvocationMatcher invocationMatcherToCompare = new InvocationMatcher(invocationToCompare, matchers);

        assertFalse(invocationMatcher.hasSimilarMethod(invocationMatcherToCompare));

        Invocation invocationWithSameMethod = new Invocation(new Location(1, 1), method, mock, new Object[0]);
        InvocationMatcher invocationMatcherWithSameMethod = new InvocationMatcher(invocationWithSameMethod, matchers);

        assertTrue(invocationMatcher.hasSimilarMethod(invocationMatcherWithSameMethod));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocationToTest = new Invocation(new Location(1, 1), method, mock, new Object[]{1});
        Invocation invocationToCompare = new Invocation(new Location(1, 1), method, mock, new Object[]{1});

        assertTrue(invocationMatcher.hasSameMethod(invocationMatcherToCompare));
    }

    @Test
    public void testToString() {
        String expected = String.format("InvocationMatcher[%s, %s]", method.getName(), matchers.toString());
        String actual = invocationMatcher.toString();

        assertTrue(expected.equals(actual));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Object arg1 = new Object();
        Object arg2 = new Object();

        Invocation invocationToTest = new Invocation(new Location(1, 1), method, mock, new Object[]{arg1, arg2});

        CapturesArguments capturesArguments = Mockito.mock(CapturesArguments.class);
        Matcher matcher = ArgumentMatchers.argThat(capturesArguments);

        invocationMatcher.captureArgumentsFrom(invocationToTest);

        assertTrue(capturesArguments.getValue().equals(arg1));
    }

    @Test(expected = AssertionError.class)
    public void testCaptureArgumentsFrom_ThrowsExceptionWhenArgumentMismatch() {
        Object arg1 = new Object();
        Object arg2 = new Object();

        Invocation invocationToTest = new Invocation(new Location(1, 1), method, mock, new Object[]{arg1, arg2});

        Matcher matcher = ArgumentMatchers.argThat(ArgumentsComparator.instance());

        invocationMatcher.captureArgumentsFrom(invocationToTest);
    }

    @Test(expected = AssertionError.class)
    public void testMatches_ThrowsExceptionWhenArgumentsDoNotMatch() {
        Object arg1 = new Object();
        Object arg2 = new Object();

        Invocation invocationToTest = new Invocation(new Location(1, 1), method, mock, new Object[]{arg1, arg2});
        Invocation invocationToCompare = new Invocation(new Location(1, 1), method, mock, new Object[]{1, 2});

        invocationMatcher.matches(invocationToCompare);
    }

    @Test
    public void testCreateFrom() {
        Object mock = Mockito.mock(Object.class);
        List<Invocation> invocations = Arrays.asList(new Invocation(new Location(1, 1), method, mock, new Object[0]),
                new Invocation(new Location(1, 1), method, mock, new Object[]{1}),
                new Invocation(new Location(1, 1), method, mock, new Object[]{1, 1}));

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);
        InvocationMatcher invocationMatcher3 = invocationMatchers.get(2);

        assertTrue(invocationMatcher1.matches(invocationMatcher2));
        assertFalse(invocationMatcher1.matches(invocationMatcher3));
        assertTrue(invocationMatcher3.hasSimilarMethod(invocationMatcher2));
        assertFalse(invocationMatcher3.matches(invocationMatcher2));
    }
}