import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationOnMockImpl;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private InvocationOnMock invocationOnMock;
    private InvocationMatcher anotherInvocationMatcher;

    @Before
    public void setUp() {
        Method method = Mockito.method(String.class, "toString", new Class[0]);
        invocationOnMock = new InvocationOnMockImpl(method, new Object[0]);

        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.anyString(), ArgumentMatchers.isA(String.class));
        invocationMatcher = new InvocationMatcher(invocationOnMock, matchers);

        anotherInvocationMatcher = new InvocationMatcher(invocationOnMock);
    }

    @Test
    public void testGetMethod() {
        Method method = invocationMatcher.getMethod();
        assertEquals(method, invocationOnMock.getMethod());
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = invocationMatcher.getInvocation();
        assertEquals(invocationOnMock, invocation);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertEquals(matchers.size(), 2);
        assertTrue(matchers.get(0) instanceof ArgumentMatchers.AnyString);
        assertTrue(matchers.get(1) instanceof ArgumentMatchers.IsA);
    }

    @Test
    public void testToString() {
        assertEquals(invocationMatcher.toString(), invocationOnMock.toString(invocationMatcher.getMatchers(new PrintSettings())));
    }

    @Test
    public void testMatches() {
        assertTrue(invocationMatcher.matches(invocationOnMock));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] arguments = new Object[]{"test"};
        assertTrue(invocationMatcher.safelyArgumentsMatch(arguments));
    }

    @Test
    public void testHasSimilarMethod() {
        assertTrue(invocationMatcher.hasSimilarMethod(anotherInvocationMatcher));
    }

    @Test
    public void testHasSameMethod() {
        assertTrue(invocationMatcher.hasSameMethod(anotherInvocationMatcher));
    }

    @Test
    public void testGetLocation() {
        assertEquals(invocationMatcher.getLocation(), invocationOnMock.getLocation());
    }

    @Test
    public void testToStringWithPrintSettings() {
        assertEquals(invocationMatcher.toString(new PrintSettings()), invocationOnMock.toString(invocationMatcher.getMatchers(new PrintSettings())));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        invocationMatcher.captureArgumentsFrom(invocationOnMock);

        Matcher anyStringMatcher = ArgumentMatchers.anyString();
        Matcher isAStringMatcher = ArgumentMatchers.isA(String.class);

        assertTrue(anyStringMatcher.matches("test"));
        assertTrue(isAStringMatcher.matches(invocationOnMock.getArguments()[0]));
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(invocationOnMock, invocationOnMock);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(invocationMatchers.size(), 2);
        assertEquals(invocationMatchers.get(0), invocationMatcher);
        assertEquals(invocationMatchers.get(1), anotherInvocationMatcher);
    }
}