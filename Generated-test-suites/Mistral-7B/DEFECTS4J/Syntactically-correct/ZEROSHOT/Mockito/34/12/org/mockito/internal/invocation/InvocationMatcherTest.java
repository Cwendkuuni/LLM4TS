import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.stubbing.MockWithMocks;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private Invocation candidateInvocation;
    private MockWithMocks mock;

    @Before
    public void setUp() {
        mock = new MockWithMocks(this);
        invocation = new InvocationOnMock(mock, new Method(getClass(), "methodWithTwoArgs"), new Object[]{1L, "test"},
                new Object[0], new Location(1, 1));
        candidateInvocation = new InvocationOnMock(mock, new Method(getClass(), "methodWithTwoArgs"), new Object[]{2L, "test2"},
                new Object[0], new Location(1, 2));

        List<Matcher> matchers = Arrays.asList(is(1L), is("test"));
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testNewInstanceWithMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers());
        assertThat(invocationMatcher.getMatchers(), notNullValue());
        assertThat(invocationMatcher.getMatchers().size(), is(2));
    }

    @Test
    public void testNewInstanceWithoutMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertThat(invocationMatcher.getMatchers(), is(Collections.emptyList()));
    }

    @Test
    public void testGetMethod() {
        Method method = invocationMatcher.getMethod();
        assertThat(method, notNullValue());
        assertThat(method.getName(), is("methodWithTwoArgs"));
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = invocationMatcher.getInvocation();
        assertThat(invocation, notNullValue());
        assertThat(invocation.getMethod(), is(invocationMatcher.getMethod()));
        assertThat(invocation.getArguments(), is(invocationMatcher.getInvocation().getArguments()));
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertThat(matchers, notNullValue());
        assertThat(matchers.size(), is(2));
    }

    @Test
    public void testToString() {
        String toString = invocationMatcher.toString();
        String printableToString = invocationMatcher.toString(new PrintSettings());
        assertThat(toString, notNullValue());
        assertThat(printableToString, notNullValue());
        assertThat(toString, is(printableToString));
    }

    @Test
    public void testMatches() {
        InvocationMatcher invocationMatcherForCandidate = new InvocationMatcher(candidateInvocation);
        invocationMatcherForCandidate = new InvocationMatcher(candidateInvocation, matchers());
        assertThat(invocationMatcher.matches(invocationMatcherForCandidate), is(true));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] candidateArguments = new Object[]{2L, "test2"};
        ArgumentComparator argumentComparator = new ArgumentComparator();
        boolean safelyArgumentsMatch = argumentComparator.argumentsMatch(invocationMatcher, candidateArguments);
        assertThat(safelyArgumentsMatch, is(false));
    }

    @Test
    public void testHasSimilarMethod() {
        InvocationMatcher invocationMatcherForCandidate = new InvocationMatcher(candidateInvocation);
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(invocationMatcherForCandidate);
        assertThat(hasSimilarMethod, is(false));
    }

    @Test
    public void testHasSameMethod() {
        InvocationMatcher invocationMatcherForCandidate = new InvocationMatcher(candidateInvocation);
        boolean hasSameMethod = invocationMatcher.hasSameMethod(invocationMatcherForCandidate);
        assertThat(hasSameMethod, is(false));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers());
        invocationMatcher.captureArgumentsFrom(invocation);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        for (Matcher matcher : invocationMatcher.getMatchers()) {
            if (matcher instanceof CapturesArguments) {
                ((CapturesArguments) matcher).captureFrom(1L);
                verify(capturesArguments).captureFrom(eq(1L));
            }
        }
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(invocation);
        Object[] arguments = invocation.getArguments();
        for (int i = 0; i < invocationMatcher.getMatchers().size(); i++) {
            Matcher matcher = invocationMatcher.getMatchers().get(i);
            if (matcher instanceof CapturesArguments) {
                ((CapturesArguments) matcher).captureFrom(arguments[i]);
                verify(mock).thenAnswer(new Answer<Object>() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        return arguments[i];
                    }
                });
            }
        }
    }

    private List<Matcher> matchers() {
        return Arrays.asList(is(1L), is("test"));
    }
}

class ArgumentComparator extends ArgumentsComparator {
}