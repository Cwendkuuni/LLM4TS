import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testSimpleInvocationMatch() {
        Object mock = Mockito.mock(String.class);
        Invocation invocation = new InvocationOnMock(mock, new MethodTestClass(), "testMethod", new Object[]{"Hello World"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        InvocationMatcher expectedMatcher = new InvocationMatcher(Mockito.mock(MethodTestClass.class), Arrays.asList(is("Hello World")));

        assertThat(invocationMatcher, is(equalTo(expectedMatcher)));
    }

    @Test
    public void testEmptyMatcherList() {
        Object mock = Mockito.mock(String.class);
        Invocation invocation = new InvocationOnMock(mock, new MethodTestClass(), "testMethod", new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation);

        assertThat(invocationMatcher, is(equalTo(expectedMatcher)));
    }

    @Test
    public void testVarArgsInvocationMatch() {
        Object mock = Mockito.mock(String.class);
        Invocation invocation = new InvocationOnMock(mock, new VarArgsTestClass(), "testMethod", new Object[]{"Hello", "World"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation, Arrays.asList(is("Hello"), is("World")));

        assertThat(invocationMatcher, is(equalTo(expectedMatcher)));
    }

    @Test
    public void testVarArgsInvocationMismatch() {
        Object mock = Mockito.mock(String.class);
        Invocation invocation = new InvocationOnMock(mock, new VarArgsTestClass(), "testMethod", new Object[]{"Hello"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(is("World")));

        InvocationMatcher expectedMatcher = null;

        assertThat(invocationMatcher, is(nullValue()));
    }

    @Test(expected = AssertionError.class)
    public void testInvocationMatcherThrowsException() {
        Object mock = Mockito.mock(String.class);
        Invocation invocation = new InvocationOnMock(mock, new ExceptionThrowingTestClass(), "testMethod");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Should throw an AssertionError because mismatched invocations
    }

    @Test
    public void testToString() {
        Object mock = Mockito.mock(String.class);
        Invocation invocation = new InvocationOnMock(mock, new MethodTestClass(), "testMethod", new Object[]{"Hello World"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        String expectedString = "InvocationMatcher{method=MethodTestClass.testMethod(java.lang.String, java.lang.String), arguments=[Hello World]}";

        String actualString = invocationMatcher.toString();

        assertThat(actualString, is(equalTo(expectedString)));
    }

    @Test
    public void testMatches() {
        Object mock = Mockito.mock(String.class);
        Invocation invocation = new InvocationOnMock(mock, new MethodTestClass(), "testMethod", new Object[]{"Hello World"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation);

        Invocation candidateInvocation = new InvocationOnMock(mock, new MethodTestClass(), "testMethod", new Object[]{"Hello World"});
        InvocationMatcher candidateMatcher = new InvocationMatcher(candidateInvocation);

        assertThat(invocationMatcher.matches(candidateMatcher), is(true));
    }

    @Test
    public void testDoesNotMatch() {
        Object mock = Mockito.mock(String.class);
        Invocation invocation = new InvocationOnMock(mock, new MethodTestClass(), "testMethod", new Object[]{"Hello World"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidateInvocation = new InvocationOnMock(mock, new DifferentMethodTestClass(), "differentMethod", new Object[0]);
        InvocationMatcher candidateMatcher = new InvocationMatcher(candidateInvocation);

        assertThat(invocationMatcher.matches(candidateMatcher), is(false));
    }

    @Test
    public void testDoesNotMatchDifferentMock() {
        Object mock1 = Mockito.mock(String.class);
        Object mock2 = Mockito.mock(String.class);
        Invocation invocation = new InvocationOnMock(mock1, new MethodTestClass(), "testMethod", new Object[]{"Hello World"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation candidateInvocation = new InvocationOnMock(mock2, new MethodTestClass(), "testMethod", new Object[]{"Hello World"});
        InvocationMatcher candidateMatcher = new InvocationMatcher(candidateInvocation);

        assertThat(invocationMatcher.matches(candidateMatcher), is(false));
    }

    @Test
    public void testCaptureArguments() {
        Object mock = Mockito.mock(String.class);
        Invocation invocation = new InvocationOnMock(mock, new ArgsCaptureTestClass(), "testMethod", new Object[]{"Hello World"});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = invocationMatcher.getMatchers();

        CapturesArguments capturesArguments = Mockito.mock(CapturesArguments.class);
        matchers.get(0).captureFrom(Mockito.mock(String.class));
        ((CapturesArguments) matchers.get(0)).setCaptor(capturesArguments);

        String capturedArgument = (String) capturesArguments.getValue();

        assertThat(capturedArgument, is("Hello World"));
    }

    @Test
    public void testCaptureArgumentsVarArgs() {
        Object mock = Mockito.mock(String.class);
        Invocation invocation = new InvocationOnMock(mock, new ArgsCaptureTestClass(), "testMethod", new Object[]{"Hello", "World"});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = invocationMatcher.getMatchers();

        CapturesArguments capturesArguments1 = Mockito.mock(CapturesArguments.class);
        matchers.get(0).captureFrom(Mockito.mock(String.class));
        ((CapturesArguments) matchers.get(0)).setCaptor(capturesArguments1);

        String capturedArgument1 = (String) capturesArguments1.getValue();

        CapturesArguments capturesArguments2 = Mockito.mock(CapturesArguments.class);
        matchers.get(1).captureFrom(Mockito.mock(String.class));
        ((CapturesArguments) matchers.get(1)).setCaptor(capturesArguments2);

        String capturedArgument2 = (String) capturesArguments2.getValue();

        assertThat(capturedArgument1, is("Hello"));
        assertThat(capturedArgument2, is("World"));
    }
}