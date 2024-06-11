import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationMatcher_CapturesArguments;
import org.mockito.internal.invocation.InvocationMatcher_Invocation;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.stub.Stubber;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class InvocationMatcherTest {

    private InvocationMatcher_Invocation invocation;
    private InvocationMatcher invocationMatcher;
    private InvocationMatcher_CapturesArguments capturesArguments;
    private Stubber stubber;
    private Object[] arguments;

    @Before
    public void setUp() {
        stubber = mock(Stubber.class);
        invocation = new InvocationMatcher_Invocation(stubber.createMock(Object.class), new Method(Object.class, "testMethod", new Class[0]), new PrintSettings());
        capturesArguments = mock(InvocationMatcher_CapturesArguments.class);
        invocationMatcher = new InvocationMatcher(invocation, Collections.singletonList(capturesArguments));
        arguments = new Object[]{new Integer(1), new Integer(2)};
    }

    @Test
    public void testGetMethod() {
        Method method = invocation.getMethod();
        assertTrue(method.getName().equals("testMethod"));
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = invocationMatcher.getInvocation();
        assertTrue(sameInstance(this.invocation, invocation));
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertTrue(matchers.size() == 1);
        assertTrue(sameInstance(capturesArguments, matchers.get(0)));
    }

    @Test
    public void testToString() {
        String result = invocationMatcher.toString();
        String expected = "InvocationMatcher [invocation=InvocationMatcher_Invocation [method=public void testMethod()], matchers=[InvocationMatcher_CapturesArguments]]";
        assertTrue(result.equals(expected));
    }

    @Test
    public void testMatchesWithSameInvocation() {
        InvocationMatcher_Invocation invocationToMatch = new InvocationMatcher_Invocation(stubber.createMock(Object.class), invocation.getMethod(), new PrintSettings());
        assertTrue(invocationMatcher.matches(invocationToMatch));
    }

    @Test
    public void testMatchesWithDifferentInvocationSameMethod() {
        InvocationMatcher_Invocation invocationToMatch = new InvocationMatcher_Invocation(stubber.createMock(Object.class), invocation.getMethod(), new PrintSettings());
        assertFalse(invocationMatcher.matches(invocationToMatch));
    }

    @Test
    public void testMatchesWithDifferentInvocationDifferentMethod() {
        Method methodToMatch = new Method(Object.class, "testMethod2", new Class[0]);
        InvocationMatcher_Invocation invocationToMatch = new InvocationMatcher_Invocation(stubber.createMock(Object.class), methodToMatch, new PrintSettings());
        assertFalse(invocationMatcher.matches(invocationToMatch));
    }

    @Test
    public void testHasSimilarMethod() {
        InvocationMatcher_Invocation invocationToMatch = new InvocationMatcher_Invocation(stubber.createMock(Object.class), invocation.getMethod(), new PrintSettings());
        assertTrue(invocationMatcher.hasSimilarMethod(invocationToMatch));
    }

    @Test
    public void testHasSimilarMethodDifferentMock() {
        InvocationMatcher_Invocation invocationToMatch = new InvocationMatcher_Invocation(stubber.createMock(Object.class), invocation.getMethod(), new PrintSettings());
        Object mock = invocation.getInvocation().getMock();
        Whitebox.setInternalState(invocationToMatch, "mock", spy(mock));
        assertFalse(invocationMatcher.hasSimilarMethod(invocationToMatch));
    }

    @Test
    public void testHasSameMethodDifferentInvocation() {
        InvocationMatcher_Invocation invocationToMatch = new InvocationMatcher_Invocation(stubber.createMock(Object.class), invocation.getMethod(), new PrintSettings());
        assertFalse(invocationMatcher.hasSameMethod(invocationToMatch));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        invocationMatcher.captureArgumentsFrom(invocation);
        verifyNoMoreInteractions(capturesArguments);
        Object capturedArgument = capturesArguments.getValue();
        assertThat(capturedArgument, is(arguments[0]));
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<>();
        invocations.add(invocation);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertTrue(invocationMatchers.size() == 1);
        InvocationMatcher invocationMatcher = invocationMatchers.get(0);
        assertTrue(sameInstance(invocationMatcher, invocationMatcher));
    }

    @Test
    public void testMatchesInvocationWithNoMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        InvocationMatcher_Invocation invocationToMatch = new InvocationMatcher_Invocation(stubber.createMock(Object.class), invocation.getMethod(), new PrintSettings());
        assertFalse(invocationMatcher.matches(invocationToMatch));
    }
}