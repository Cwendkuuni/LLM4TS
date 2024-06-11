import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private InvocationMatcher emptyMatcher;
    private InvocationOnMock invocation;
    private Object mock;

    @Before
    public void setUp() {
        mock = Mockito.mock(Object.class);
        invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.method(Object.class, "testMethod", new Class[0]);
        matcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.any()));
        emptyMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testConstructorWithMatchers() {
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.argThat(arg -> arg.equals("expectedValue")), ArgumentMatchers.any());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertThat(invocationMatcher.getMatchers().size(), is(matchers.size()));
        for (Matcher matcher : matchers) {
            assertThat(invocationMatcher.getMatchers().get(0), is(matcher));
        }
    }

    @Test
    public void testConstructorWithoutMatchers() {
        assertThat(emptyMatcher.getMatchers().size(), is(0));
    }

    @Test
    public void testGetMethod() {
        Method expectedMethod = Mockito.method(Object.class, "testMethod", new Class[0]);
        assertThat(matcher.getMethod(), is(expectedMethod));
        assertThat(emptyMatcher.getMethod(), is(invocation.getMethod()));
    }

    @Test
    public void testGetInvocation() {
        assertThat(matcher.getInvocation(), is(invocation));
        assertThat(emptyMatcher.getInvocation(), is(invocation));
    }

    @Test
    public void testGetMatchers() {
        assertThat(matcher.getMatchers(), is(notNullValue()));
        assertThat(emptyMatcher.getMatchers(), is(Collections.emptyList()));
    }

    @Test
    public void testToString() {
        String expectedString = "InvocationMatcher[this = Invocation[this = [MockitoMockConfiguration:0.0], method = public void testMethod()], arguments = []]";
        String actualString = matcher.toString();
        String emptyString = emptyMatcher.toString();

        assertThat(actualString, is(expectedString));
        assertThat(emptyString, is(emptyMatcher.getClass().getName() + "@" + Integer.toHexString(hashCode(emptyMatcher))));
    }

    @Test
    public void testMatches() {
        InvocationMatcher otherInvocationMatcher = new InvocationMatcher(invocation);

        assertThat(matcher.matches(otherInvocationMatcher), is(true));
        assertThat(otherInvocationMatcher.matches(matcher), is(true));
        assertThat(matcher.matches(emptyMatcher), is(false));
        assertThat(emptyMatcher.matches(matcher), is(false));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] arguments = new Object[]{1L, "test"};
        InvocationOnMock otherInvocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher otherMatcher = new InvocationMatcher(otherInvocation, Arrays.asList(ArgumentMatchers.arrayContaining(ArgumentMatchers.any(Object.class))));

        assertThat(matcher.safelyArgumentsMatch(arguments), is(true));
        assertThat(otherMatcher.safelyArgumentsMatch(arguments), is(true));
    }

    @Test(expected = AssertionError.class)
    public void testArgumentsDoNotMatch() {
        Object[] arguments = new Object[]{1L, "test"};
        InvocationOnMock otherInvocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher otherMatcher = new InvocationMatcher(otherInvocation, Arrays.asList(ArgumentMatchers.arrayContaining(ArgumentMatchers.any(Integer.class))));

        matcher.safelyArgumentsMatch(arguments);
    }

    @Test
    public void testHasSimilarMethod() {
        InvocationOnMock otherInvocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher otherMatcher = new InvocationMatcher(otherInvocation);

        assertThat(matcher.hasSimilarMethod(otherMatcher), is(true));

        InvocationMatcherotherMatcherWithDifferentMethod = new InvocationMatcher(Mockito.mock(InvocationOnMock.class));

        assertThat(matcher.hasSimilarMethod(otherMatcherWithDifferentMethod), is(false));
    }

    @Test
    public void testHasSameMethod() {
        InvocationOnMock otherInvocation = Mockito.mock(InvocationOnMock.class);
        Method otherMethod = Mockito.method(Object.class, "otherTestMethod", new Class[0]);
        otherInvocation.setMethod(otherMethod);

        assertThat(matcher.hasSameMethod(new InvocationMatcher(invocation)), is(true));
        assertThat(matcher.hasSameMethod(new InvocationMatcher(otherInvocation)), is(false));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Object[] arguments = new Object[]{1L, "test"};

        matcher.captureArgumentsFrom(invocation);

        for (Matcher matcher : matcher.getMatchers()) {
            if (matcher instanceof CapturesArguments) {
                CapturesArguments capturesArguments = (CapturesArguments) matcher;
                Object capturedArgument = capturesArguments.getValue();
                if (capturesArguments.getIndex() == 0) {
                    assertThat(capturedArgument, is(arguments[0]));
                } else if (capturesArguments.getIndex() == 1) {
                    assertThat(capturedArgument, is(arguments[1]));
                }
            }
        }
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(invocation, invocation);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertThat(invocationMatchers.size(), is(2));

        for (InvocationMatcher invocationMatcher : invocationMatchers) {
            assertThat(invocationMatcher.getMatchers().size(), is(0));
        }
    }

    private int hashCode(Object obj) {
        return obj.hashCode();
    }
}