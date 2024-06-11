import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testSameMethodInvocation() {
        Object mock = Mockito.mock(Object.class);

        Invocation invocation1 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("sameMethodName");
        when(method1.getParameterTypes()).thenReturn(new Class[]{int.class, int.class});
        when(invocation1.getArguments()).thenReturn(new Object[]{1, 2});

        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method2 = Mockito.mock(Method.class);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method2.getName()).thenReturn("sameMethodName");
        when(method2.getParameterTypes()).thenReturn(new Class[]{int.class, int.class});
        when(invocation2.getArguments()).thenReturn(new Object[]{1, 2});

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertTrue(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testDifferentMethodInvocation() {
        Object mock = Mockito.mock(Object.class);

        Invocation invocation1 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("method1Name");
        when(method1.getParameterTypes()).thenReturn(new Class[]{int.class, int.class});
        when(invocation1.getArguments()).thenReturn(new Object[]{1, 2});

        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method2 = Mockito.mock(Method.class);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method2.getName()).thenReturn("method2Name");
        when(method2.getParameterTypes()).thenReturn(new Class[]{double.class, double.class});

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertFalse(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testSameMethodWithDifferentArguments() {
        Object mock = Mockito.mock(Object.class);

        Invocation invocation1 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("sameMethodName");
        when(method1.getParameterTypes()).thenReturn(new Class[]{int.class, int.class});
        when(invocation1.getArguments()).thenReturn(new Object[]{1, 3});

        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method2 = Mockito.mock(Method.class);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method2.getName()).thenReturn("sameMethodName");
        when(method2.getParameterTypes()).thenReturn(new Class[]{int.class, int.class});
        when(invocation2.getArguments()).thenReturn(new Object[]{1, 2});

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertFalse(matcher1.matches(new InvocationOnMock(matcher2.getInvocation(), mock)));
    }

    @Test
    public void testSameMethodWithSameArguments() {
        Object mock = Mockito.mock(Object.class);

        Invocation invocation1 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("sameMethodName");
        when(method1.getParameterTypes()).thenReturn(new Class[]{int.class, int.class});
        when(invocation1.getArguments()).thenReturn(new Object[]{1, 2});

        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method2 = Mockito.mock(Method.class);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method2.getName()).thenReturn("sameMethodName");
        when(method2.getParameterTypes()).thenReturn(new Class[]{int.class, int.class});
        when(invocation2.getArguments()).thenReturn(new Object[]{1, 2});

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        Invocation invocationToTest = Mockito.mock(Invocation.class);
        Method methodToTest = Mockito.mock(Method.class);
        when(invocationToTest.getMethod()).thenReturn(methodToTest);
        when(methodToTest.getName()).thenReturn("sameMethodName");
        when(methodToTest.getParameterTypes()).thenReturn(new Class[]{int.class, int.class});
        when(invocationToTest.getArguments()).thenReturn(new Object[]{1, 2});

        InvocationMatcher testMatcher = new InvocationMatcher(invocationToTest);

        assertTrue(matcher1.matches(testMatcher));
    }

    @Test
    public void testDifferentMethodsWithSameArguments() {
        Object mock = Mockito.mock(Object.class);

        Invocation invocation1 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("method1Name");
        when(method1.getParameterTypes()).thenReturn(new Class[]{int.class, int.class});
        when(invocation1.getArguments()).thenReturn(new Object[]{1, 2});

        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method2 = Mockito.mock(Method.class);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method2.getName()).thenReturn("method2Name");
        when(method2.getParameterTypes()).thenReturn(new Class[]{int.class, int.class});

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        Invocation invocationToTest = Mockito.mock(Invocation.class);
        Method methodToTest = Mockito.mock(Method.class);
        when(invocationToTest.getMethod()).thenReturn(methodToTest);
        when(methodToTest.getName()).thenReturn("differentMethodName");
        when(methodToTest.getParameterTypes()).thenReturn(new Class[]{int.class, int.class});
        when(invocationToTest.getArguments()).thenReturn(new Object[]{1, 2});

        InvocationMatcher testMatcher = new InvocationMatcher(invocationToTest);

        assertFalse(matcher1.matches(testMatcher));
    }

    @Test
    public void testCaptureArguments() {
        Object mock = Mockito.mock(Object.class);

        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("testMethod");
        when(method.getParameterTypes()).thenReturn(new Class[]{int[].class, int.class});
        when(invocation.getArguments()).thenReturn(new Object[]{new int[]{1, 2, 3}, 4});

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        int[] arg = {5, 6, 7};
        Invocation matchedInvocation = Mockito.mock(Invocation.class);
        when(matchedInvocation.getMethod()).thenReturn(method);
        when(matchedInvocation.getArguments()).thenReturn(new Object[]{arg, 4});

        matcher.captureArgumentsFrom(matchedInvocation);

        List<Matcher> matchers = matcher.getMatchers();

        Matcher argMatcher = matchers.get(0);
        assertTrue(argMatcher instanceof MatcherDecorator);
        Matcher actualMatcher = ((MatcherDecorator) argMatcher).getActualMatcher();
        assertTrue(actualMatcher instanceof VarargMatcher);
        VarargMatcher varargMatcher = (VarargMatcher) actualMatcher;
        Matcher[] args = varargMatcher.getMatchers();

        assertTrue(Arrays.equals(new int[]{5, 6, 7}, args[0].toArray(new Object[0])));
    }
}