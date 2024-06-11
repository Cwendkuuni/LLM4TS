import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Stubber;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private List<Matcher> matchers;
    private Stubber stubber;

    @Before
    public void setup() {
        stubber = mock(Stubber.class);
        invocation = new InvocationOnMock(mock(Object.class), new MethodTest(), stubber);
        matchers = Arrays.asList(is(1), is("test"));
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertThat(invocationMatcher, notNullValue());
        assertThat(invocationMatcher.getInvocation(), sameInstance(invocation));
        assertThat(invocationMatcher.getMatchers(), sameInstance(matchers));
    }

    @Test
    public void testConstructorWithOnlyInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertThat(invocationMatcher, notNullValue());
        assertThat(invocationMatcher.getInvocation(), sameInstance(invocation));
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        Method method = invocation.getMethod();
        Method expectedMethod = methodTest();
        assertThat(invocationMatcher.getMethod(), sameInstance(expectedMethod));
    }

    @Test
    public void testGetInvocation() {
        Invocation expectedInvocation = invocation;
        Invocation actualInvocation = invocationMatcher.getInvocation();
        assertThat(actualInvocation, sameInstance(expectedInvocation));
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> expectedMatchers = matchers;
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        assertThat(actualMatchers, sameInstance(expectedMatchers));
    }

    @Test
    public void testToString() {
        String expectedToString = invocation.toString(matchers, new PrintSettings());
        String actualToString = invocationMatcher.toString();
        assertThat(actualToString, is(expectedToString));
    }

    @Test
    public void testMatches() {
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, matchers);
        assertTrue(invocationMatcher.matches(invocationMatcher2));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] invocationArguments = new Object[]{1, "test"};
        Object[] otherInvocationArguments = new Object[]{1, "test2"};
        assertTrue(invocationMatcher.safelyArgumentsMatch(invocationArguments));
        assertFalse(invocationMatcher.safelyArgumentsMatch(otherInvocationArguments));
    }

    @Test
    public void testHasSimilarMethod() {
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, matchers);
        InvocationMatcher invocationMatcher3 = new InvocationMatcher(new InvocationOnMock(mock(Object.class), methodTest(), stubber), matchers);
        assertTrue(invocationMatcher.hasSimilarMethod(invocationMatcher2));
        assertFalse(invocationMatcher.hasSimilarMethod(invocationMatcher3));
    }

    @Test
    public void testHasSameMethod() {
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, matchers);
        InvocationMatcher invocationMatcher3 = new InvocationMatcher(invocation, matchers);
        assertTrue(invocationMatcher.hasSameMethod(invocationMatcher2));
        assertTrue(invocationMatcher.hasSameMethod(invocationMatcher3));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Object arg1 = new Object();
        Object arg2 = new Object();
        Object[] arguments = new Object[]{arg1, arg2};
        invocationMatcher.captureArgumentsFrom(new InvocationOnMock(mock(Object.class), methodTest(), stubber));
        verify(stubber).argThat(matchers.get(0));
        verify(stubber).argThat(matchers.get(1));
    }

    private Method methodTest() {
        try {
            return this.getClass().getMethod("methodTest");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}