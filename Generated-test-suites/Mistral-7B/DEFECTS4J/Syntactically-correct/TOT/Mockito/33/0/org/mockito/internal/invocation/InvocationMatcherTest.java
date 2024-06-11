import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Object mock;

    @Before
    public void setup() {
        mock = Mockito.mock(String.class);
    }

    @Test
    public void testInvocationMatcher_WithMethodAndMatchers() {
        // Given
        Method method = Mockito.method(String.class, "length");
        List<Matcher> matchers = Arrays.asList(isA(Integer.class));
        Invocation invocation = new Invocation(new Object[]{"test"}, method, new Location(1, 1), new Object[]{mock});
        matcher = new InvocationMatcher(invocation, matchers);

        // When & Then
        InvocationMatcher expectedMatcher = Mockito.argumentCaptor(InvocationMatcher.class);
        Invocation actualInvocation = invoke(mock, "length");
        verify(mock).length(expectedMatcher.capture(argument -> matcher.matches(argument)));
        assertThat(expectedMatcher.getValue(), is(matcher));
    }

    @Test
    public void testInvocationMatcher_WithMethodOnly() {
        // Given
        Method method = Mockito.method(String.class, "length");
        Invocation invocation = new Invocation(new Object[]{}, method, new Location(1, 1), new Object[]{mock});
        matcher = new InvocationMatcher(invocation);

        // When & Then
        Invocation actualInvocation = invoke(mock, "length");
        assertTrue(matcher.matches(actualInvocation));
    }

    @Test
    public void testInvocationMatcher_WithDifferentMethod() {
        // Given
        Method method1 = Mockito.method(String.class, "length");
        Method method2 = Mockito.method(Integer.class, "doubleValue");
        Invocation invocation = new Invocation(new Object[]{}, method1, new Location(1, 1), new Object[]{mock});
        matcher = new InvocationMatcher(invocation);

        // When & Then
        Invocation actualInvocation = new InvocationOnMock(mock).invocationCallingRealMethod("doubleValue", new Object[]{1});
        assertFalse(matcher.matches(actualInvocation));
    }

    @Test
    public void testInvocationMatcher_WithSameMethodButDifferentArguments() {
        // Given
        Method method = Mockito.method(String.class, "length");
        Invocation invocation1 = new Invocation(new Object[]{"test1"}, method, new Location(1, 1), new Object[]{mock});
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = new Invocation(new Object[]{"test2"}, method, new Location(1, 1), new Object[]{mock});

        // When & Then
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);
        assertFalse(matcher1.matches(matcher2));
    }

    @Test
    public void testInvocationMatcher_WithSameMethodAndSameArguments() {
        // Given
        Method method = Mockito.method(String.class, "length");
        Invocation invocation1 = new Invocation(new Object[]{"test"}, method, new Location(1, 1), new Object[]{mock});
        Invocation invocation2 = new Invocation(new Object[]{"test"}, method, new Location(1, 1), new Object[]{mock});

        // When & Then
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);
        assertTrue(matcher1.matches(matcher2));
    }

    @Test
    public void testInvocationMatcher_CaptureArgumentsFromInvocation() {
        // Given
        Method method = Mockito.method(String.class, "length");
        Invocation invocation = new Invocation(new Object[]{"test"}, method, new Location(1, 1), new Object[]{mock});
        matcher = new InvocationMatcher(invocation);

        // When
        String argument = "testArg";
        doAnswer(invocation -> argument).when(mock).length(isA(InvocationMatcher.class));

        // Then
        String capturedArgument = "";
        matcher.captureArgumentsFrom(invocation);
        assertEquals(argument, capturedArgument);
    }
}