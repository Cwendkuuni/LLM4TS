import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.InvocationOnMockStealthMode;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class InvocationMatcherTest {

    private Object mock;

    @Before
    public void setup() {
        mock = Mockito.mock(Object.class);
    }

    @Test
    public void testInvocationMatcher_SameMethodAndArguments() {
        Object[] args = new Object[]{1, "test"};
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getArguments()).thenReturn(args);
        when(invocation.getMethod()).thenReturn(getMethod("testMethod", Object.class, String.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> matchers = Arrays.asList(eq(1), eq("test"));
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(expectedMatcher, invocationMatcher);
    }

    @Test
    public void testInvocationMatcher_SameMethodDifferentArguments() {
        Object[] args1 = new Object[]{1, "test1"};
        Object[] args2 = new Object[]{1, "test2"};
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getArguments()).thenReturn(args1);
        when(invocation.getMethod()).thenReturn(getMethod("testMethod", Object.class, String.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> matchers = Arrays.asList(eq(1));
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation, matchers);

        assertFalse(invocationMatcher.matches(new InvocationOnMockStealthMode(getMethod("testMethod", Object.class, String.class), mock, args2)));
    }

    @Test
    public void testInvocationMatcher_DifferentMethods() {
        Object[] args = new Object[]{1, "test"};
        Invocation invocation1 = Mockito.mock(Invocation.class);
        when(invocation1.getArguments()).thenReturn(args);
        when(invocation1.getMethod()).thenReturn(getMethod("testMethod1", Object.class, String.class));

        Invocation invocation2 = Mockito.mock(Invocation.class);
        when(invocation2.getArguments()).thenReturn(args);
        when(invocation2.getMethod()).thenReturn(getMethod("testMethod2", Object.class));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcher_ArgumentsComparator_ThrowsException() {
        Object[] args = new Object[]{1, "test"};
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getArguments()).thenReturn(args);
        when(invocation.getMethod()).thenReturn(getMethod("testMethod", Object.class, String.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> matchers = Arrays.asList(hasProperty("propertyName", "expectedValue"));

        try {
            invocationMatcher.matches(new InvocationMatcher(invocation, matchers));
        } catch (AssertionError e) {
            assertEquals("Expected :<[org.mockito.internal.InvocationMatcher@3e2d41a4]>" +
                    ", but: <[org.mockito.internal.InvocationMatcher@61c28a5c]>", e.getMessage());
            return;
        }

        fail("Expected an AssertionError");
    }

    private Method getMethod(String name, Class<?>... parameterTypes) {
        try {
            return Class.forName("org.mockito.internal.invocation.InvocationMatcherTest$TestMethod").getMethod(name, parameterTypes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Matcher eq(Object value) {
        return Mockito.eq(value);
    }

    private Matcher hasProperty(String propertyName, Object expectedValue) {
        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        return Mockito.argThat(new PropertyValueMatcher(propertyName, expectedValue));
    }
}

class PropertyValueMatcher implements Matcher<Object> {

    private String propertyName;
    private Object expectedValue;

    public PropertyValueMatcher(String propertyName, Object expectedValue) {
        this.propertyName = propertyName;
        this.expectedValue = expectedValue;
    }

    @Override
    public boolean matches(Object argument) {
        try {
            Object propertyValue = ReflectionUtils.getFieldValue(argument, propertyName);
            return expectedValue.equals(propertyValue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("has property '")
                .appendValue(propertyName)
                .appendText("' with value '")
                .appendValue(expectedValue)
                .appendText('"');
    }
}