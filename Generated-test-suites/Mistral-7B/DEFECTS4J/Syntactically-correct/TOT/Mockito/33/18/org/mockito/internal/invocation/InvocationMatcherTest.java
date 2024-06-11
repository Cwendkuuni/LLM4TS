import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.Arguments;
import org.mockito.internal.matchers.Capture;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Object mock;

    @Before
    public void setup() {
        mock = Mockito.mock(Object.class);
    }

    @Test
    public void testMatcherWithNoMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        matcher = new InvocationMatcher(invocation);

        Method method = new Method("testMethod", new Class[0]);
        assertTrue(matcher.matches(new InvocationOnMock(mock, method)));
    }

    @Test
    public void testMatcherWithMatchers() {
        Object[] args = new Object[]{"arg1", "arg2"};
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.eq("arg1"), ArgumentMatchers.eq("arg2")));

        Method method = new Method("testMethod", new Class[0]);
        method.setParameterTypes(String[].class);
        InvocationOnMock invocationOnMock = new InvocationOnMock(mock, method);
        invocationOnMock.setArguments(args);

        assertTrue(matcher.matches(invocationOnMock));
    }

    @Test
    public void testMatcherWithDifferentNumberOfArguments() {
        Object[] args = new Object[]{"arg1"};
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        Method method = new Method("testMethod", new Class[0]);
        method.setParameterTypes(new Class[]{String[].class, Integer.TYPE});
        InvocationOnMock invocationOnMock = new InvocationOnMock(mock, method);
        invocationOnMock.setArguments(args);

        assertFalse(matcher.matches(invocationOnMock));
    }

    @Test
    public void testMatcherWithDifferentTypeOfArguments() {
        Object[] args = new Object[]{1, "arg2"};
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        Method method = new Method("testMethod", new Class[0]);
        method.setParameterTypes(new Class[]{Integer.TYPE, String.class});
        InvocationOnMock invocationOnMock = new InvocationOnMock(mock, method);
        invocationOnMock.setArguments(args);

        assertFalse(matcher.matches(invocationOnMock));
    }

    @Test
    public void testMatcherWithCapture() {
        Object[] args = new Object[]{"arg1", new Capture()};
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.singletonList(ArgumentMatchers.anyArray()));

        Method method = new Method("testMethod", new Class[0]);
        method.setParameterTypes(String[].class);
        InvocationOnMock invocationOnMock = new InvocationOnMock(mock, method);
        invocationOnMock.setArguments(args);

        matcher.captureArgumentsFrom(invocationOnMock);
        Object captured = ((Capture) matcher.getMatchers().get(0)).getValue();
        assertTrue(Arrays.equals(args[1], captured));

        assertTrue(matcher.matches(invocationOnMock));
    }

    @Test
    public void testToString() {
        Object[] args = new Object[]{"arg1", "arg2"};
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.eq("arg1"), ArgumentMatchers.eq("arg2")));
        PrintSettings printSettings = new PrintSettings();

        String expected = String.format("invocation: %s\nmatchers:%n%s%n", invocation.toString(), matchersToString(matcher.getMatchers()));
        String actual = matcher.toString(printSettings);

        assertTrue(expected.equals(actual));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);

        Method method1 = new Method("testMethod", new Class[0]);
        Method method2 = new Method("testMethod", new Class[0]);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        assertTrue(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testHasNotSimilarMethod() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);

        Method method1 = new Method("testMethod1", new Class[0]);
        Method method2 = new Method("testMethod2", new Class[0]);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        assertFalse(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testHasNotSimilarMethodWithSameNameButDifferentArguments() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);

        Method method1 = new Method("testMethod", new Class[]{String[].class});
        Method method2 = new Method("testMethod", new Class[0]);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        assertFalse(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testHasNotSimilarMethodWithSameNameButDifferentTypes() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);

        Method method1 = new Method("testMethod", new Class[]{String[].class});
        Method method2 = new Method("testMethod", new Class[]{Integer.TYPE});
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        assertFalse(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testHasNotSimilarMethodWithSameNameSameArgumentsButDifferentMocks() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);

        Method method1 = new Method("testMethod", new Class[0]);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, Collections.emptyList());

        assertFalse(matcher1.hasSimilarMethod(matcher2));
    }

    private String matchersToString(List<Matcher> matchers) {
        StringBuilder stringBuilder = new StringBuilder();
        int index = 0;
        for (Matcher matcher : matchers) {
            stringBuilder.append(String.format("Matcher %d: %s", index++, matcher.toString()));
        }
        return stringBuilder.toString();
    }
}