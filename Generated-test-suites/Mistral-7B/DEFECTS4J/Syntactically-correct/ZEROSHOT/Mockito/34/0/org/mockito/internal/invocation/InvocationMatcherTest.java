import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationMatcher;
import org.mockito.internal.invocation.ArgumentsComparator;
import org.mockito.internal.invocation.CapturesArguments;
import org.mockito.internal.matchers.InvocationWith;
import org.mockito.internal.matchers.Verified;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.exceptions.base.MockitoException;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Invocation invocation;
    private List<Matcher> matchers;
    private Verified verifiedMock;
    private PrintingFriendlyInvocation printableInvocation;
    private ArgumentsComparator argumentsComparator;

    @Before
    public void setup() {
        invocation = mock(Invocation.class);
        matchers = Arrays.asList(is(1), is("mockito"));
        argumentsComparator = spy(new ArgumentsComparator());
        printableInvocation = spy(new PrintingFriendlyInvocation() {
            @Override
            public String toString(PrintSettings printSettings) {
                return "invocation with matchers: " + matchers;
            }
        });
        verifiedMock = mock(Verified.class);

        matcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testGetMethod() {
        Method expectedMethod = new Method("methodName", new Class[0]);
        when(invocation.getMethod()).thenReturn(expectedMethod);

        Method actualMethod = matcher.getMethod();

        assertThat(actualMethod, is(sameInstance(expectedMethod)));
    }

    @Test
    public void testGetInvocation() {
        assertThat(matcher.getInvocation(), is(sameInstance(invocation)));
    }

    @Test
    public void testGetMatchers() {
        assertThat(matcher.getMatchers(), is(sameInstance(matchers)));
    }

    @Test
    public void testToString() {
        PrintSettings printSettings = new PrintSettings();

        String expectedToString = "invocation with matchers: [org.hamcrest.CoreMatchers$IsInstanceOf: java.lang.Integer, org.hamcrest.CoreMatchers$IsInstanceOf: org.mockito.Verified]";
        when(printableInvocation.toString(printSettings)).thenReturn(expectedToString);
        when(argumentsComparator.argumentsMatch(matcher, (Object[]) anyArray())).thenReturn(true);

        String actualToString = matcher.toString();

        assertThat(actualToString, is(expectedToString));
    }

    @Test
    public void testMatches() {
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());

        InvocationMatcher anotherMatcher = new InvocationMatcher(actualInvocation);

        matcher.captureArgumentsFrom(invocation);
        anotherMatcher.captureArgumentsFrom(actualInvocation.getArguments());

        ArgumentsComparator argumentsComparatorSpy = spy(argumentsComparator);

        when(argumentsComparatorSpy.argumentsMatch(matcher, actualInvocation.getArguments())).thenReturn(true);

        InvocationMatcher testSubject = new InvocationMatcher(matcher, argumentsComparatorSpy);

        assertThat(testSubject.matches(anotherMatcher), is(true));
    }

    @Test(expected = MockitoException.class)
    public void testMatches_differentMocks() {
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        when(actualInvocation.getMock()).thenReturn(new Object());

        InvocationMatcher anotherMatcher = new InvocationMatcher(actualInvocation);

        matcher.captureArgumentsFrom(invocation);

        InvocationMatcher testSubject = new InvocationMatcher(matcher);

        testSubject.matches(anotherMatcher);
    }

    @Test(expected = MockitoException.class)
    public void testMatches_differentMethods() {
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMethod()).thenReturn(new Method("differentMethodName", new Class[0]));

        InvocationMatcher anotherMatcher = new InvocationMatcher(actualInvocation);

        matcher.captureArgumentsFrom(invocation);

        InvocationMatcher testSubject = new InvocationMatcher(matcher);

        testSubject.matches(anotherMatcher);
    }

    @Test(expected = MockitoException.class)
    public void testMatches_unverified() {
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        when(actualInvocation.isVerified()).thenReturn(false);

        InvocationMatcher anotherMatcher = new InvocationMatcher(actualInvocation);

        matcher.captureArgumentsFrom(invocation);

        InvocationMatcher testSubject = new InvocationMatcher(matcher);

        testSubject.matches(anotherMatcher);
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = new Object[0];
        when(argumentsComparator.argumentsMatch(matcher, actualArgs)).thenReturn(true);

        boolean actualResult = matcher.safelyArgumentsMatch(actualArgs);

        assertThat(actualResult, is(true));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMethod()).thenReturn(new Method("methodName", new Class[0]));

        InvocationMatcher anotherMatcher = new InvocationMatcher(actualInvocation);

        InvocationMatcher testSubject = new InvocationMatcher(matcher);

        boolean actualResult = testSubject.hasSimilarMethod(anotherMatcher);

        assertThat(actualResult, is(true));
    }

    @Test
    public void testHasSameMethod() {
        Method expectedMethod = new Method("methodName", new Class[0]);
        when(invocation.getMethod()).thenReturn(expectedMethod);
        when(actualInvocation.getMethod()).thenReturn(expectedMethod);

        InvocationMatcher anotherMatcher = new InvocationMatcher(actualInvocation);

        InvocationMatcher testSubject = new InvocationMatcher(matcher);

        boolean actualResult = testSubject.hasSameMethod(anotherMatcher);

        assertThat(actualResult, is(true));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Object[] invocationArguments = new Object[2];

        InvocationWith invocationWith = Whitebox.getInternalState(invocation, "invocationWith");

        when(verifiedMock.verifiedNoMoreInteractions()).thenReturn(invocationWith);

        matcher.captureArgumentsFrom(invocation);

        InvocationWith actualInvocationWith = Whitebox.getInternalState(actualInvocation, "invocationWith");

        testSubject.captureArgumentsFrom(actualInvocation);

        for (int i = 0; i < matchers.size(); i++) {
            CapturesArguments capturesArguments = (CapturesArguments) matchers.get(i);

            Object capturedArgument = capturesArguments.getValue();
            Object actualCapturedArgument = Whitebox.getInternalState(actualInvocationWith.getArguments(), i);

            assertEquals(capturedArgument, actualCapturedArgument);
        }
    }

    @Test
    public void testToStringWithEmptyMatchers() {
        PrintSettings printSettings = new PrintSettings();

        String expectedToString = "invocation with matchers: []";
        when(printableInvocation.toString(printSettings)).thenReturn(expectedToString);
        when(argumentsComparator.argumentsMatch(matcher, (Object[]) anyArray())).thenReturn(true);

        String actualToString = matcher.toString(printSettings);

        assertThat(actualToString, is(expectedToString));
    }

    @Test
    public void testToStringWithMatchers() {
        PrintSettings printSettings = new PrintSettings();

        String expectedToString = "invocation with matchers: [org.hamcrest.CoreMatchers$IsInstanceOf: java.lang.Integer, org.hamcrest.CoreMatchers$IsInstanceOf: org.mockito.Verified]";
        when(printableInvocation.toString(printSettings)).thenReturn(expectedToString);
        when(argumentsComparator.argumentsMatch(matcher, (Object[]) anyArray())).thenReturn(true);

        String actualToString = matcher.toString(printSettings);

        assertThat(actualToString, is(expectedToString));
    }
}