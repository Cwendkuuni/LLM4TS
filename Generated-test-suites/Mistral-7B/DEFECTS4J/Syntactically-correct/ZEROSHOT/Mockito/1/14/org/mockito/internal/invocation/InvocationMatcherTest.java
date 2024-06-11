package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherCreationWithMatchers() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(ArgumentMatchers.eq("arg1"));
        matchers.add(ArgumentMatchers.eq("arg2"));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // then
        assertTrue(invocationMatcher instanceof DescribedInvocation);
        assertTrue(invocationMatcher instanceof CapturesArgumentsFromInvocation);
        assertTrue(invocationMatcher.getMatchers().size() == 2);
    }

    @Test
    public void testInvocationMatcherCreationWithoutMatchers() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        assertTrue(invocationMatcher instanceof DescribedInvocation);
        assertTrue(invocationMatcher instanceof CapturesArgumentsFromInvocation);
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        // given
        Method method = Mockito.mock(Method.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // when
        Method actualMethod = invocationMatcher.getMethod();

        // then
        Mockito.verify(invocation).getMethod();
        assertTrue(actualMethod.equals(method));
    }

    @Test
    public void testGetInvocation() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // when
        Invocation actualInvocation = invocationMatcher.getInvocation();

        // then
        assertTrue(actualInvocation.equals(invocation));
    }

    @Test
    public void testGetMatchers() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(ArgumentMatchers.eq("arg1"));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        // then
        assertTrue(actualMatchers.size() == 1);
        assertTrue(actualMatchers.get(0).equals(ArgumentMatchers.eq("arg1")));
    }

    @Test
    public void testToString() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(ArgumentMatchers.eq("arg1"));
        matchers.add(ArgumentMatchers.eq("arg2"));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when
        String actualString = invocationMatcher.toString();

        // then
        String expectedString = String.format("Invocation on %s: %s", invocation.getMock(), matchers);
        assertTrue(actualString.equals(expectedString));
    }

    @Test
    public void testMatches() {
        // given
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Mockito.when(invocation1.getMock()).thenReturn(Mockito.mock(Object.class));
        Mockito.when(invocation2.getMock()).thenReturn(Mockito.mock(Object.class));

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(ArgumentMatchers.eq("arg1"));
        matchers.add(ArgumentMatchers.eq("arg2"));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // when
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Mockito.when(invocation1.getMethod()).thenReturn(method1);
        Mockito.when(invocation2.getMethod()).thenReturn(method2);
        Mockito.when(method1.getName()).thenReturn("methodName");
        Mockito.when(method2.getName()).thenReturn("methodName");

        Object[] arguments1 = new Object[2];
        arguments1[0] = "arg1";
        arguments1[1] = "arg2";
        Mockito.when(invocation1.getArguments()).thenReturn(arguments1);

        Object[] arguments2 = new Object[2];
        arguments2[0] = "arg1";
        arguments2[1] = "arg2";
        Mockito.when(invocation2.getArguments()).thenReturn(arguments2);

        // then
        assertTrue(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[2];
        arguments[0] = "arg1";
        arguments[1] = "arg2";
        Mockito.when(invocation.getArguments()).thenReturn(arguments);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // when
        boolean actualResult = invocationMatcher.safelyArgumentsMatch(arguments);

        // then
        assertTrue(actualResult);
    }

    @Test
    public void testHasSimilarMethod() {
        // given
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Mockito.when(invocation1.getMock()).thenReturn(Mockito.mock(Object.class));
        Mockito.when(invocation2.getMock()).thenReturn(Mockito.mock(Object.class));

        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Mockito.when(invocation1.getMethod()).thenReturn(method1);
        Mockito.when(invocation2.getMethod()).thenReturn(method2);
        Mockito.when(method1.getName()).thenReturn("methodName1");
        Mockito.when(method2.getName()).thenReturn("methodName2");

        // when
        boolean actualResult = invocationMatcher1.hasSimilarMethod(invocationMatcher2);

        // then
        assertFalse(actualResult);
    }

    @Test
    public void testHasSameMethod() {
        // given
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Mockito.when(invocation1.getMock()).thenReturn(Mockito.mock(Object.class));
        Mockito.when(invocation2.getMock()).thenReturn(Mockito.mock(Object.class));

        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Mockito.when(invocation1.getMethod()).thenReturn(method1);
        Mockito.when(invocation2.getMethod()).thenReturn(method2);
        Mockito.when(method1.getName()).thenReturn("methodName");
        Mockito.when(method2.getName()).thenReturn("methodName");

        Class[] parameterTypes1 = new Class[2];
        parameterTypes1[0] = String.class;
        parameterTypes1[1] = Integer.class;
        Mockito.when(method1.getParameterTypes()).thenReturn(parameterTypes1);

        Class[] parameterTypes2 = new Class[2];
        parameterTypes2[0] = String.class;
        parameterTypes2[1] = Integer.class;
        Mockito.when(method2.getParameterTypes()).thenReturn(parameterTypes2);

        // when
        boolean actualResult = invocationMatcher1.hasSameMethod(invocationMatcher2);

        // then
        assertTrue(actualResult);
    }

    @Test
    public void testGetLocation() {
        // given
        Location location = Mockito.mock(Location.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getLocation()).thenReturn(location);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // when
        Location actualLocation = invocationMatcher.getLocation();

        // then
        assertTrue(actualLocation.equals(location));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // given
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Object arg1 = Mockito.mock(Object.class);
        Object arg2 = Mockito.mock(Object.class);
        Object[] arguments = new Object[2];
        arguments[0] = arg1;
        arguments[1] = arg2;
        Mockito.when(invocation.getArguments()).thenReturn(arguments);

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(ArgumentMatchers.eq("arg1"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when
        invocationMatcher.captureArgumentsFrom(invocation);

        // then
        Matcher arg1Matcher = matchers.get(0);
        assertTrue(arg1Matcher instanceof CapturesArguments);
        assertTrue(((CapturesArguments) arg1Matcher).getCapturedValue().equals(arg1));
    }

    @Test
    public void testIsVarargMatcher() {
        // given
        VarargMatcher varargMatcher = Mockito.mock(VarargMatcher.class);
        Matcher matcher = Mockito.mock(Matcher.class);
        Mockito.when(varargMatcher.getActualMatcher()).thenReturn(matcher);

        // when
        boolean actualResult = invocationMatcher.isVarargMatcher(varargMatcher);

        // then
        assertTrue(actualResult);
    }

    @Test
    public void testIsVariableArgument() {
        // given
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object[] arguments = new Object[2];
        arguments[0] = "arg1";
        Object arg2 = Mockito.mock(Object[].class);
        arguments[1] = arg2;
        Mockito.when(invocation.getRawArguments()).thenReturn(arguments);

        // when
        boolean actualResult = invocationMatcher.isVariableArgument(invocation, 1);

        // then
        assertTrue(actualResult);
    }

    @Test
    public void testCreateFrom() {
        // given
        List<Invocation> invocations = new LinkedList<>();
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        invocations.add(invocation1);
        invocations.add(invocation2);

        // when
        List<InvocationMatcher> actualMatchers = InvocationMatcher.createFrom(invocations);

        // then
        assertTrue(actualMatchers.size() == 2);
        for (int i = 0; i < 2; i++) {
            InvocationMatcher invocationMatcher = actualMatchers.get(i);
            assertTrue(invocationMatcher instanceof DescribedInvocation);
            assertTrue(invocationMatcher instanceof CapturesArgumentsFromInvocation);
            assertTrue(invocationMatcher.getMatchers().isEmpty());
        }
    }
}