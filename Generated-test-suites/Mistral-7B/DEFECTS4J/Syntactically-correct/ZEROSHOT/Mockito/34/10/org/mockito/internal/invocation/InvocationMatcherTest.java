import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InternalInvocation;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MethodInterceptor;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.Arguments;
import org.mockito.internal.matchers.Capture;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.reflection.MethodUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private Method method;
    private List<Matcher> matchers;

    @Before
    public void setUp() throws Exception {
        Object mock = Mockito.mock(Object.class);
        method = MethodUtil.getPublicMethod(Object.class, "testMethod", new Class[]{String.class, int.class});
        invocation = new InternalInvocation(mock, method, Arrays.asList(new Object[]{"str1", 1}));
        matchers = new ArrayList<>();
        matchers.add(ArgumentMatchers.eq("str1"));
        matchers.add(ArgumentMatchers.eq(1));
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
        Assert.assertEquals(matchers, invocationMatcher.getMatchers());
        Assert.assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testConstructorWithInvocationOnly() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
        Assert.assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
        Assert.assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetMethod() {
        Assert.assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        Assert.assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        PrintSettings printSettings = new PrintSettings();
        String expectedToString = String.format("%s %s(%s, %s)", "InvocationMatcher", method.getName(), method.getParameterTypes()[0].getName(), method.getParameterTypes()[1].getName());
        Assert.assertEquals(expectedToString, invocationMatcher.toString(printSettings));
    }

    @Test
    public void testMatches() {
        Invocation anotherInvocation = new InternalInvocation(mock, method, new Object[]{new String("str2"), 2});
        InvocationMatcher anotherInvocationMatcher = new InvocationMatcher(anotherInvocation);
        Assert.assertFalse(invocationMatcher.matches(anotherInvocationMatcher));

        Invocation sameInvocation = new InternalInvocation(mock, method, new Object[]{new String("str1"), 1});
        InvocationMatcher sameInvocationMatcher = new InvocationMatcher(sameInvocation);
        Assert.assertTrue(invocationMatcher.matches(sameInvocationMatcher));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = new Object[]{"str2", 2};
        Invocation anotherInvocation = new InternalInvocation(mock, method, actualArgs);
        InvocationMatcher anotherInvocationMatcher = new InvocationMatcher(anotherInvocation);
        Assert.assertFalse(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation anotherInvocation = new InternalInvocation(mock, MethodUtil.getPublicMethod(Object.class, "testOtherMethod", new Class[]{String.class, int.class}));
        InvocationMatcher anotherInvocationMatcher = new InvocationMatcher(anotherInvocation);
        Assert.assertFalse(invocationMatcher.hasSimilarMethod(anotherInvocationMatcher));

        Invocation sameMethodInvocation = new InternalInvocation(mock, method, new Object[]{new String("str1"), 1});
        InvocationMatcher sameMethodInvocationMatcher = new InvocationMatcher(sameMethodInvocation);
        Assert.assertTrue(invocationMatcher.hasSimilarMethod(sameMethodInvocationMatcher));
    }

    @Test
    public void testHasSameMethod() {
        Invocation anotherInvocation = new InternalInvocation(mock, method, new Object[]{new String("str1"), 1});
        InvocationMatcher anotherInvocationMatcher = new InvocationMatcher(anotherInvocation);
        Assert.assertTrue(invocationMatcher.hasSameMethod(anotherInvocationMatcher));
    }

    @Test
    public void testGetLocation() {
        //location is not mocked in this test case
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        String expectedToString = String.format("%s %s(%s, %s)", "InvocationMatcher", method.getName(), method.getParameterTypes()[0].getName(), method.getParameterTypes()[1].getName());
        Assert.assertEquals(expectedToString, invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        Capture<String> capture = ArgumentCaptor.forClass(String.class);
        Matcher<Integer> intMatcher = ArgumentMatchers.eq(1);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(capture, intMatcher));
        invocationMatcher.captureArgumentsFrom(invocation);

        Assert.assertEquals("str1", capture.getValue());
        Assert.assertArrayEquals(new Object[]{capture, intMatcher}, invocationMatcher.getMatchers().toArray());
    }
}