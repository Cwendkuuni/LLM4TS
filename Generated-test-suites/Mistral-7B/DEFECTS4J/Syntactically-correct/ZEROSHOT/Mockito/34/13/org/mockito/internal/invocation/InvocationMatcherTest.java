import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.InvocationOnMockArguments;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private InvocationOnMock invocationOnMock;
    private ArgumentMatcher argumentMatcher;
    private CapturesArguments capturesArguments;
    private ArgumentsComparator argumentsComparator;

    @Before
    public void setup() {
        Method method = Mockito.method(String.class, "foo", String.class);
        Object mock = Mockito.mock(String.class);
        Object[] arguments = new Object[]{"arg1"};
        invocationOnMock = new InvocationOnMock(mock, method, arguments, new Location(1, 1, "TestClass.java"));
        argumentMatcher = ArgumentMatchers.eq("arg1");
        capturesArguments = Mockito.argumentCaptor(String.class);
        invocationMatcher = new InvocationMatcher(invocationOnMock, new ArrayList<>());
        argumentsComparator = new ArgumentsComparator();
    }

    @Test
    public void testNewInvocationMatcherWithMatchers() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(argumentMatcher);
        InvocationMatcher invocationMatcherWithMatchers = new InvocationMatcher(invocationOnMock, matchers);

        assertTrue(invocationMatcherWithMatchers.matches(invocationOnMock));
    }

    @Test
    public void testNewInvocationMatcherWithoutMatchers() {
        InvocationMatcher invocationMatcherWithoutMatchers = new InvocationMatcher(invocationOnMock);

        assertTrue(invocationMatcherWithoutMatchers.matches(invocationOnMock));
    }

    @Test
    public void testGetMethod() {
        Method wantedMethod = invocationMatcher.getMethod();
        assertTrue(wantedMethod.equals(invocationOnMock.getMethod()));
    }

    @Test
    public void testGetInvocation() {
        Invocation actualInvocation = Mockito.mock(Invocation.class);
        invocationMatcher.captureArgumentsFrom(actualInvocation);
        Invocation invocation = invocationMatcher.getInvocation();

        assertTrue(invocation.equals(invocationOnMock));
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = invocationMatcher.getMatchers();

        assertTrue(matchers.isEmpty());
    }

    @Test
    public void testToString() {
        String expectedToString = invocationOnMock.toString() + "\n" +
                "with:\n" +
                "arguments:\n" +
                "   [arg1]\n";

        String actualToString = invocationMatcher.toString();
        assertTrue(actualToString.equals(expectedToString));
    }

    @Test
    public void testMatchesWithSameMethodAndArguments() {
        InvocationOnMock candidate = new InvocationOnMock(invocationOnMock.getMock(), invocationOnMock.getMethod(), invocationOnMock.getArguments(), new Location(1, 1, "TestClass.java"));

        assertTrue(invocationMatcher.matches(candidate));
    }

    @Test
    public void testMatchesWithDifferentMethod() {
        Method differentMethod = Mockito.method(Integer.class, "bar", String.class);
        InvocationOnMock candidate = new InvocationOnMock(invocationOnMock.getMock(), differentMethod, invocationOnMock.getArguments(), new Location(1, 1, "TestClass.java"));

        assertFalse(invocationMatcher.matches(candidate));
    }

    @Test
    public void testMatchesWithSameMethodButDifferentArguments() {
        Object[] differentArguments = new Object[]{"arg2"};
        InvocationOnMock candidate = new InvocationOnMock(invocationOnMock.getMock(), invocationOnMock.getMethod(), differentArguments, new Location(1, 1, "TestClass.java"));

        assertFalse(invocationMatcher.matches(candidate));
    }

    @Test
    public void testHasSimilarMethod() {
        InvocationOnMock candidate = new InvocationOnMock(invocationOnMock.getMock(), invocationOnMock.getMethod(), invocationOnMock.getArguments(), new Location(1, 1, "TestClass.java"));

        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        InvocationOnMock candidate = new InvocationOnMock(invocationOnMock.getMock(), invocationOnMock.getMethod(), invocationOnMock.getArguments(), new Location(1, 1, "TestClass.java"));

        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        String expectedToString = invocationOnMock.toString() + "\n" +
                "with:\n" +
                "arguments:\n" +
                "   [arg1]\n";

        String actualToString = invocationMatcher.toString(printSettings);
        assertTrue(actualToString.equals(expectedToString));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        invocationMatcher.captureArgumentsFrom(invocationOnMock);

        String capturedArgument = capturesArguments.getValue();
        assertTrue(capturedArgument.equals("arg1"));
    }
}