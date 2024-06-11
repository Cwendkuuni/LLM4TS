import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsProcessor;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private Method method;
    private List<Matcher> matchers;

    @Before
    public void setUp() {
        Object mock = Mockito.mock(Object.class);
        method = Mockito.method(Integer.class, "testMethod", new Class[]{Integer.class, String.class});
        invocation = Mockito.on(mock).invoke(method, 1, "test");
        matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq("test"));
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testInvocationMatcher_ValidConstructor() {
        assertThat(invocationMatcher, notNullValue());
        assertThat(invocationMatcher.getInvocation(), sameInstance(invocation));
        assertThat(invocationMatcher.getMatchers(), sameInstance(matchers));
    }

    @Test
    public void testGetMethod() {
        assertThat(invocationMatcher.getMethod(), sameInstance(method));
    }

    @Test
    public void testGetInvocation() {
        assertThat(invocationMatcher.getInvocation(), sameInstance(invocation));
    }

    @Test
    public void testGetMatchers_EmptyList() {
        InvocationMatcher emptyInvocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> emptyMatchers = Collections.emptyList();

        assertThat(emptyInvocationMatcher.getMatchers(), sameInstance(emptyMatchers));
    }

    @Test
    public void testToString() {
        String expected = new PrintSettings().print(matchers, invocation);
        String actual = invocationMatcher.toString();

        assertThat(actual, is(expected));
    }

    @Test
    public void testMatches_SameInvocation() {
        InvocationMatcher sameMatcher = new InvocationMatcher(invocation, matchers);

        assertTrue(invocationMatcher.matches(sameMatcher));
    }

    @Test
    public void testMatches_DifferentInvocation_SameMethod_SameArguments_SameMock() {
        Object mock2 = Mockito.mock(Object.class);
        InvocationOnMock invocation2 = Mockito.on(mock2).invoke(method, 1, "test");
        InvocationMatcher otherMatcher = new InvocationMatcher(invocation2, matchers);

        assertTrue(invocationMatcher.matches(otherMatcher));
    }

    @Test
    public void testMatches_DifferentInvocation_SameMethod_DifferentArguments_SameMock() {
        InvocationOnMock invocation2 = Mockito.on(mock).invoke(method, 2, "test");
        InvocationMatcher otherMatcher = new InvocationMatcher(invocation2, matchers);

        assertFalse(invocationMatcher.matches(otherMatcher));
    }

    @Test
    public void testMatches_DifferentInvocation_DifferentMethod_SameArguments_SameMock() {
        Method differentMethod = Mockito.method(Integer.class, "testOtherMethod", new Class[]{Integer.class, String.class});
        InvocationOnMock invocation2 = Mockito.on(mock).invoke(differentMethod, 1, "test");
        InvocationMatcher otherMatcher = new InvocationMatcher(invocation2, matchers);

        assertFalse(invocationMatcher.matches(otherMatcher));
    }

    @Test
    public void testMatches_DifferentInvocation_SameMethod_SameArguments_DifferentMock() {
        Object mock2 = Mockito.mock(Object.class);
        Method method2 = Mockito.method(Integer.class, "testMethod", new Class[]{Integer.class, String.class});
        InvocationOnMock invocation2 = Mockito.on(mock2).invoke(method2, 1, "test");
        InvocationMatcher otherMatcher = new InvocationMatcher(invocation2, matchers);

        assertFalse(invocationMatcher.matches(otherMatcher));
    }

    @Test
    public void testHasSameMethod_SameMethods() {
        Method method2 = Mockito.method(Integer.class, "testMethod", new Class[]{Integer.class, String.class});
        InvocationMatcher otherMatcher = new InvocationMatcher(new InvocationMatcher(Mockito.on(mock).invoke(method2, 1, "test")), matchers);

        assertTrue(invocationMatcher.hasSameMethod(otherMatcher));
    }

    @Test
    public void testHasSameMethod_DifferentMethods() {
        Method differentMethod = Mockito.method(Integer.class, "testOtherMethod", new Class[]{Integer.class, String.class});
        InvocationMatcher otherMatcher = new InvocationMatcher(new InvocationMatcher(Mockito.on(mock).invoke(differentMethod, 1, "test")), matchers);

        assertFalse(invocationMatcher.hasSameMethod(otherMatcher));
    }

    @Test
    public void testHasSimilarMethod_SameMethods() {
        Method method2 = Mockito.method(Integer.class, "testMethod", new Class[]{Integer.class, String.class});
        InvocationMatcher otherMatcher = new InvocationMatcher(new InvocationMatcher(Mockito.on(mock).invoke(method2, 1, "test")), matchers);

        assertTrue(invocationMatcher.hasSimilarMethod(otherMatcher));
    }

    @Test
    public void testHasSimilarMethod_DifferentMethods() {
        Method differentMethod = Mockito.method(Integer.class, "testOtherMethod", new Class[]{Integer.class, String.class});
        InvocationMatcher otherMatcher = new InvocationMatcher(new InvocationMatcher(Mockito.on(mock).invoke(differentMethod, 1, "test")), matchers);

        assertFalse(invocationMatcher.hasSimilarMethod(otherMatcher));
    }

    @Test
    public void testCaptureArgumentsFromInvocation_Varargs() {
        Matcher argMatcher1 = ArgumentMatchers.instanceOf(String.class);
        CapturesArguments captureArg1 = Mockito.argThat(argMatcher1);
        matchers.set(1, captureArg1);

        Object[] arguments = new Object[]{1, "test", "test2"};
        InvocationMatcher invocationMatcherWithVarargs = new InvocationMatcher(new InvocationMatcher(Mockito.on(mock).invoke(method, 1, "test", arguments)), matchers);

        invocationMatcherWithVarargs.captureArgumentsFrom(invocation);

        Object capturedArgument = captureArg1.getValue();
        assertThat(capturedArgument, is(arguments[1]));
    }

    @Test
    public void testCaptureArgumentsFromInvocation_NoVarargs() {
        Matcher argMatcher1 = ArgumentMatchers.instanceOf(String.class);
        CapturesArguments captureArg1 = Mockito.argThat(argMatcher1);
        matchers.set(1, captureArg1);

        InvocationMatcher invocationMatcherNoVarargs = new InvocationMatcher(new InvocationMatcher(Mockito.on(mock).invoke(method, 1, "test")), matchers);

        invocationMatcherNoVarargs.captureArgumentsFrom(invocation);

        Object capturedArgument = captureArg1.getValue();
        assertThat(capturedArgument, is(invocation.getArgumentAt(1, Object.class)));
    }
}