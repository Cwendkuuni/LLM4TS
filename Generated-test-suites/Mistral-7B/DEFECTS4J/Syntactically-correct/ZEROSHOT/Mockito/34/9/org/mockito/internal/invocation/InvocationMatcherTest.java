import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.InvocationOnMockImpl;
import org.mockito.internal.invocation.CapturesArguments;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.PrintSettings;
import org.mockito.internal.matchers.ArgumentsComparator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(org.mockito.runners.JUnit4.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithEmptyMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        Method method = Mockito.mock(Method.class);
        assertTrue(matcher.hasSameMethod(new InvocationMatcher(new InvocationOnMockImpl(method))));
    }

    @Test
    public void testInvocationMatcherWithMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("someArgument"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Method method = Mockito.mock(Method.class);
        InvocationOnMock invocationOnMock = new InvocationOnMockImpl(method);
        Object[] arguments = new Object[]{"someArgument"};

        InvocationMatcher testMatcher = new InvocationMatcher(invocationOnMock, matchers);
        assertTrue(matcher.matches(testMatcher));
    }

    @Test
    public void testInvocationMatcherWithEmptyMatchersAndArguments() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        Method method = Mockito.mock(Method.class);
        InvocationOnMock invocationOnMock = new InvocationOnMockImpl(method);
        Object[] arguments = new Object[]{"someArgument"};

        InvocationMatcher testMatcher = new InvocationMatcher(invocationOnMock, Collections.emptyList());
        assertFalse(matcher.matches(testMatcher));
    }

    @Test
    public void testInvocationMatcherWithMatchersAndArguments() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("someArgument"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Method method = Mockito.mock(Method.class);
        InvocationOnMock invocationOnMock = new InvocationOnMockImpl(method);
        Object[] arguments = new Object[]{"someArgument"};

        InvocationMatcher testMatcher = new InvocationMatcher(invocationOnMock, matchers);
        assertTrue(matcher.matches(testMatcher));
    }

    @Test
    public void testInvocationMatcherWithNonNullMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.notNullValue());
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Method method = Mockito.mock(Method.class);
        InvocationOnMock invocationOnMock = new InvocationOnMockImpl(method);
        Object[] arguments = new Object[]{null};

        InvocationMatcher testMatcher = new InvocationMatcher(invocationOnMock, matchers);
        assertFalse(matcher.matches(testMatcher));
    }

    @Test
    public void testInvocationMatcherWithDifferentMocks() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = Mockito.mock(Invocation.class);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        Method method = Mockito.mock(Method.class);
        InvocationOnMock invocationOnMock = new InvocationOnMockImpl(method);

        assertFalse(matcher1.matches(matcher2));
    }

    @Test
    public void testInvocationMatcherWithDifferentMethods() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("someArgument"));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation, matchers);

        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        InvocationOnMock invocationOnMock = new InvocationOnMockImpl(method1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocationOnMock, matchers);

        assertFalse(matcher1.matches(matcher2));
    }

    @Test
    public void testInvocationMatcherWithSameMethodButDifferentArguments() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("someArgument1"), ArgumentMatchers.eq("someArgument2"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Method method = Mockito.mock(Method.class);
        InvocationOnMock invocationOnMock = new InvocationOnMockImpl(method);
        Object[] arguments1 = new Object[]{"someArgument1", "someArgument2"};
        Object[] arguments2 = new Object[]{"someArgument1", "otherArgument2"};

        InvocationMatcher testMatcher1 = new InvocationMatcher(invocationOnMock, matchers);
        assertFalse(matcher.matches(testMatcher1));

        InvocationMatcher testMatcher2 = new InvocationMatcher(invocationOnMock, matchers);
        assertFalse(matcher.matches(testMatcher2));
    }

    @Test
    public void testInvocationMatcherWithSameMethodAndSameArguments() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("someArgument"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Method method = Mockito.mock(Method.class);
        InvocationOnMock invocationOnMock = new InvocationOnMockImpl(method);
        Object[] arguments = new Object[]{"someArgument"};

        InvocationMatcher testMatcher = new InvocationMatcher(invocationOnMock, matchers);
        assertTrue(matcher.matches(testMatcher));
    }

    @Test
    public void testToStringWithEmptyMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        PrintSettings printSettings = Mockito.mock(PrintSettings.class);
        String expectedResult = invocation.toString(printSettings);

        String result = matcher.toString(printSettings);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testToStringWithMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("someArgument"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        PrintSettings printSettings = Mockito.mock(PrintSettings.class);
        String expectedResult = invocation.toString(matchers, printSettings);

        String result = matcher.toString(printSettings);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, Collections.emptyList());

        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method2 = Mockito.mock(Method.class);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        assertTrue(matcher1.hasSameMethod(matcher2));
        assertTrue(matcher1.hasSameMethod(new InvocationMatcher(new InvocationOnMockImpl(method1))));
        assertTrue(matcher2.hasSameMethod(new InvocationMatcher(new InvocationOnMockImpl(method2))));
        assertTrue(matcher1.hasSameMethod(matcher1));
        assertTrue(matcher2.hasSameMethod(matcher2));
    }

    @Test
    public void testHasSameMethodWithDifferentMocks() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, Collections.emptyList());

        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method2 = Mockito.mock(Method.class);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        assertFalse(matcher1.hasSameMethod(matcher2));
    }

    @Test
    public void testHasSameMethodWithDifferentMethods() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation, Collections.emptyList());

        Method method2 = Mockito.mock(Method.class);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation, Collections.emptyList());

        assertFalse(matcher1.hasSameMethod(matcher2));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(Mockito.argThat(new CapturesArguments() {
            @Override
            public void captureFrom(Object argument) {
                String capturedValue = argument.toString();
                assertEquals("expectedValue", capturedValue);
            }
        }));

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        Object[] arguments = new Object[]{"expectedValue"};

        matcher.captureArgumentsFrom(new InvocationOnMockImpl(mock(Method.class).withArguments(arguments)));
    }

    @Test
    public void testCaptureArgumentsFromInvocationWithEmptyMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        Object[] arguments = new Object[]{"expectedValue"};

        matcher.captureArgumentsFrom(new InvocationOnMockImpl(mock(Method.class).withArguments(arguments)));

        assertNotNull(matcher.getInvocation().getArguments());
    }
}

class CapturesArguments implements ArgumentMatcher<Object> {
    private CapturesArguments() {}

    @Override
    public boolean matches(Object argument) {
        return true;
    }

    public void captureFrom(Object argument) {
        // implementation specific code
    }
}

class ArgumentsComparator extends ArgumentsComparatorBase {
    @Override
    protected boolean areArgumentsEqual(Object[] arguments1, Object[] arguments2) {
        return Arrays.deepEquals(arguments1, arguments2);
    }
}