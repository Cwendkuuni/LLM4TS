import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.stub.Stubber;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private InvocationMatcher emptyMatcher;
    private Invocation invocation;
    private Invocation candidateInvocation;
    private Stubber stubber;
    private Object mock;

    @Before
    public void setUp() {
        stubber = Mockito.stubber();
        mock = stubber.createMock(String.class);

        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("hello"), ArgumentMatchers.isA(String[].class));
        invocation = new InvocationOnMock(Mockito.method(String.class, "methodWithTwoArgs", new Class[]{String.class, String[].class}), mock, Collections.emptyList(), new Object[]{ArgumentMatchers.eq("arg1"), new Object[]{"arg2", "arg3"}}, new Object[0], null);
        matcher = new InvocationMatcher(invocation, matchers);
        emptyMatcher = new InvocationMatcher(invocation);
        candidateInvocation = new InvocationOnMock(Mockito.method(String.class, "methodWithTwoArgs", new Class[]{String.class, String[].class}), mock, Collections.emptyList(), new Object[]{ArgumentMatchers.eq("arg1"), new Object[]{"arg2", "arg3"}}, new Object[0], null);
    }

    @Test
    public void testMatches() {
        assertTrue(matcher.matches(candidateInvocation));
    }

    @Test
    public void testDoesNotMatchDifferentMock() {
        Invocation differentMockInvocation = new InvocationOnMock(Mockito.method(String.class, "methodWithTwoArgs", new Class[]{String.class, String[].class}), Mockito.mock(Integer.class), Collections.emptyList(), new Object[]{ArgumentMatchers.eq("arg1"), new Object[]{"arg2", "arg3"}}, new Object[0], null);
        assertFalse(matcher.matches(differentMockInvocation));
    }

    @Test
    public void testDoesNotMatchDifferentMethod() {
        Invocation differentMethodInvocation = new InvocationOnMock(Mockito.method(String.class, "anotherMethod", new Class[0]), mock, Collections.emptyList(), new Object[0], new Object[0], null);
        assertFalse(matcher.matches(differentMethodInvocation));
    }

    @Test
    public void testDoesNotMatchNonUnverified() {
        candidateInvocation.verify();
        assertFalse(matcher.matches(candidateInvocation));
    }

    @Test
    public void testDoesNotMatchDifferentLocation() {
        Invocation differentLocationInvocation = new InvocationOnMock(Mockito.method(String.class, "methodWithTwoArgs", new Class[]{String.class, String[].class}), mock, Collections.emptyList(), new Object[]{ArgumentMatchers.eq("arg1"), new Object[]{"arg2", "arg3"}}, new Object[0], new Location("foo"));
        assertFalse(matcher.matches(differentLocationInvocation));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation similarMethodInvocation = new InvocationOnMock(Mockito.method(String.class, "methodWithTwoArgs", new Class[]{String.class, String[].class}), mock, Collections.emptyList(), new Object[]{ArgumentMatchers.eq("arg1"), new Object[]{"arg2", "arg3"}}, new Object[0], null);
        assertTrue(matcher.hasSimilarMethod(similarMethodInvocation));
    }

    @Test
    public void testHasDifferentMethod() {
        Invocation differentMethodInvocation = new InvocationOnMock(Mockito.method(String.class, "anotherMethod", new Class[0]), mock, Collections.emptyList(), new Object[0], new Object[0], null);
        assertFalse(matcher.hasSimilarMethod(differentMethodInvocation));
    }

    @Test
    public void testHasSameMethod() {
        assertTrue(matcher.hasSameMethod(emptyMatcher.getInvocation()));
    }

    @Test
    public void testDifferentInvocationsWithSameArguments() {
        Invocation differentInvocation = new InvocationOnMock(Mockito.method(String.class, "methodWithTwoArgs", new Class[]{String.class, String[].class}), mock, Collections.emptyList(), new Object[]{ArgumentMatchers.eq("arg1"), new Object[]{"arg2", "arg3"}}, new Object[0], null);
        assertFalse(matcher.matches(differentInvocation));
        assertTrue(matcher.hasSimilarMethod(differentInvocation));
    }

    @Test
    public void testCaptureArguments() {
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.argThat(new CapturesMatcher<String>("captureString")), ArgumentMatchers.argThat(new CapturesMatcher<Integer>("captureInt")), ArgumentMatchers.argThat(new CapturesMatcher<String[]>("captureStringArray")));
        Invocation invocationWithVarargs = new InvocationOnMock(Mockito.method(String.class, "methodWithVarargs", new Class[]{String.class}), mock, Collections.emptyList(), new Object[]{"arg1"}, new Object[0], null);
        InvocationMatcher matcherWithVarargs = new InvocationMatcher(invocationWithVarargs, matchers);

        Object capturedString = null;
        Object capturedInt = null;
        Object capturedStringArray = null;

        matcherWithVarargs.captureArgumentsFrom(candidateInvocation);

        assertTrue(matcherWithVarargs.getMatchers().get(0).matches(invocation.getArgumentAt(0, String.class)));
        assertTrue(matcherWithVarargs.getMatchers().get(1).matches(invocation.getArgumentAt(1, Integer.class)));
        assertTrue(matcherWithVarargs.getMatchers().get(2).matches(invocation.getArguments()[2]));

        //TODO SF - this lot can be moved captureFrom implementation
//        assertEquals("arg2", ((CapturesArguments) matcherWithVarargs.getMatchers().get(1)).getCapturedValue());
//        assertEquals(new Object[]{"arg3", "arg4"}, ((CapturesArguments) matcherWithVarargs.getMatchers().get(2)).getCapturedValue());
    }
}

class CapturesMatcher<T> implements Matcher<Object> {

    private final String name;

    public CapturesMatcher(String name) {
        this.name = name;
    }

    @Override
    public boolean matches(Object argument) {
        System.out.println(String.format("Captured value for '%s' is: %s", name, argument));
        return true;
    }
}