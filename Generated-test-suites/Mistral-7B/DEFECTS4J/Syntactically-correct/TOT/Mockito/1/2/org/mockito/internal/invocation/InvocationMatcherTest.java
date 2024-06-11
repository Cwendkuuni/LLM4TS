import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private Object mock;

    @Before
    public void setup() {
        mock = Mockito.mock(Object.class);
    }

    @Test
    public void test_matches_same_method_and_arguments() {
        // given
        Method method = Mockito.method(MyClass.class, "myMethod", String.class, int.class);
        Invocation invocation = new Invocation(mock, method, new Object[]{"hello", 5});
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // when
        Invocation testInvocation = new InvocationOnMock(mock, method, Arrays.asList(ArgumentMatchers.eq("hello"), ArgumentMatchers.eq(5)));
        InvocationMatcher testMatcher = new InvocationMatcher(testInvocation);

        // then
        assertTrue(matcher.matches(testMatcher));
    }

    @Test
    public void test_matches_same_method_but_different_arguments() {
        // given
        Method method = Mockito.method(MyClass.class, "myMethod", String.class, int.class);
        Invocation invocation = new Invocation(mock, method, new Object[]{"hello", 5});
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // when
        Invocation testInvocation = new InvocationOnMock(mock, method, Arrays.asList(ArgumentMatchers.eq("world"), ArgumentMatchers.eq(10)));
        InvocationMatcher testMatcher = new InvocationMatcher(testInvocation);

        // then
        assertFalse(matcher.matches(testMatcher));
    }

    @Test
    public void test_matches_different_methods() {
        // given
        Method method1 = Mockito.method(MyClass.class, "myMethod1", String.class);
        Invocation invocation1 = new Invocation(mock, method1, new Object[]{"hello"});
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);

        Method method2 = Mockito.method(MyClass.class, "myMethod2", int.class);
        Invocation invocation2 = new Invocation(mock, method2, new Object[]{5});
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        // when

        // then
        assertFalse(matcher1.matches(matcher2));
    }

    @Test
    public void test_matches_different_mocks() {
        // given
        Object mock1 = Mockito.mock(Object.class);
        Method method = Mockito.method(MyClass.class, "myMethod", String.class, int.class);
        Invocation invocation = new Invocation(mock1, method, new Object[]{"hello", 5});
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        Object mock2 = Mockito.mock(Object.class);
        Invocation testInvocation = new InvocationOnMock(mock2, method, Arrays.asList(ArgumentMatchers.eq("world"), ArgumentMatchers.eq(10)));
        InvocationMatcher testMatcher = new InvocationMatcher(testInvocation);

        // when

        // then
        assertFalse(matcher.matches(testMatcher));
    }

    @Test
    public void test_has_similar_method_with_overloaded_methods() {
        // given
        Method method1 = Mockito.method(MyClass.class, "myMethod", String.class, int.class);
        Invocation invocation1 = new Invocation(mock, method1, new Object[]{"hello", 5});
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);

        Method method2 = Mockito.method(MyClass.class, "myMethod", int.class);
        Invocation invocation2 = new Invocation(mock, method2, new Object[]{5});
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        // when

        // then
        assertTrue(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void test_has_similar_method_but_not_overloaded() {
        // given
        Method method1 = Mockito.method(MyClass.class, "myMethod1", String.class, int.class);
        Invocation invocation1 = new Invocation(mock, method1, new Object[]{"hello", 5});
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);

        Method method2 = Mockito.method(MyClass.class, "myMethod2", String.class);
        Invocation invocation2 = new Invocation(mock, method2, new Object[]{"world"});
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        // when

        // then
        assertFalse(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void test_capture_arguments() {
        // given
        Method method = Mockito.method(MyClass.class, "myMethod", String.class, int.class);
        Invocation invocation = new Invocation(mock, method, new Object[]{"hello", 5});
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        List<Object> capturedArguments = new LinkedList<>();
        doAnswer((Answer<Object>) invocation1 -> {
            capturedArguments.add(invocation1.getArgumentAt(0, Object.class));
            return null;
        }).when(mock).myMethod((String) ArgumentMatchers.anyString(), (int) ArgumentMatchers.anyInt());

        // when
        matcher.captureArgumentsFrom(new InvocationOnMock(mock, method, new Object[]{"world", 10}));

        // then
        assertTrue(capturedArguments.contains("world"));
        assertTrue(capturedArguments.contains(10));
    }

    @Test
    public void test_capture_varargs() {
        // given
        Method method = Mockito.method(MyClass.class, "myMethod", String[].class);
        Invocation invocation = new Invocation(mock, method, new Object[]{new String[]{"hello", "world"}});
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        List<String> capturedArguments = new LinkedList<>();
        doAnswer((Answer<Object>)(InvocationOnMock invocation1) -> {
            Object[] arguments = invocation1.getRawArguments();
            for (Object arg : Arrays.asList(arguments)) {
                if (arg instanceof String[]) {
                    String[] stringArgs = (String[]) arg;
                    for (String s : stringArgs) {
                        capturedArguments.add(s);
                    }
                }
            }
            return null;
        }).when(mock).myMethod((String[]) ArgumentMatchers.any());

        // when
        matcher.captureArgumentsFrom(new InvocationOnMock(mock, method, new Object[]{"hello", "world", "test"}));

        // then
        assertTrue(capturedArguments.contains("hello"));
        assertTrue(capturedArguments.contains("world"));
        assertTrue(capturedArguments.contains("test"));
    }
}

class MyClass {
    void myMethod(String s, int i) {}

    void myMethod(String[] args) {}
}