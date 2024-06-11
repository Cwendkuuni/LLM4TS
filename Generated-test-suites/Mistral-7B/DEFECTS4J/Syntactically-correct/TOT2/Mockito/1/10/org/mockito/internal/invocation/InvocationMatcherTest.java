package org.mockito.internal.invocation;

import static org.junit.Assert.assertThrows;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.InvocationWithMocks;
import org.mockito.Mockito;
import org.mockito.internal.matchers.ArgumentsComparator;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private List<Matcher> matchers;

    @Before
    public void setUp() {
        invocation = new InvocationWithMocks(new Method(getClass(), "testMethod"), new Object[0]);
        matchers = Collections.singletonList(Mockito.any());
    }

    @Test
    public void testInvocationMatcherWithEmptyList() {
        invocationMatcher = new InvocationMatcher(invocation);
        verifyNoMoreInteractions(invocationMatcher.getInvocation());
    }

    @Test
    public void testInvocationMatcherWithCustomMatchers() {
        invocationMatcher = new InvocationMatcher(invocation, matchers);
        MyClass target = spy(new MyClass());
        invocationMatcher.getInvocation().call(target);
        Mockito.verify(target).testMethod(Mockito.any());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromVarargs() {
        invocation.setRawArguments(new Object[]{new Object[0], "test"});
        invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testInvocationMatcherWithCustomMatchersAndVerifiedInvocation() {
        invocation = new InvocationWithMocks(new Method(getClass(), "testMethod"), new Object[0]);
        MyClass target = spy(new MyClass());
        invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.getInvocation().call(target);
        Mockito.verify(target).testMethod(Mockito.any());

        // Verify that calling captureArgumentsFrom on a verified invocation throws UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> invocationMatcher.captureArgumentsFrom(invocation));
    }

    @Test
    public void testInvocationMatcherWithVarargsAndVarargMatcher() {
        invocation = new InvocationWithMocks(new Method(getClass(), "testMethod", int[].class), new Object[]{new int[0]});
        Matcher<int[]> varargMatcher = new VarargMatcher(Matcher.greaterThan(0));
        matchers = Collections.singletonList(varargMatcher);
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        MyClass target = spy(new MyClass());
        int[] intArray = new int[]{1, 2, 3};
        invocationMatcher.getInvocation().call(target, intArray);

        ArgumentCaptor<int[]> argumentCaptor = ArgumentCaptor.forClass(int[].class);
        Mockito.verify(target).testMethod(argumentCaptor.capture());
        int[] captured = argumentCaptor.getValue();

        assertThat(captured, is(intArray));
    }

    @Test
    public void testInvocationMatcherWithVarargsAndNonVarargMatcher() {
        invocation = new InvocationWithMocks(new Method(getClass(), "testMethod", int.class), new Object[]{new Integer(1), new int[0]});
        Matcher<Integer> nonVarargMatcher = Mockito.eq(1);
        matchers = Arrays.asList(nonVarargMatcher, Mockito.any(int[].class));
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        MyClass target = spy(new MyClass());
        int intValue = 1;
        int[] intArray = new int[]{1, 2, 3};
        invocationMatcher.getInvocation().call(target, intValue, intArray);

        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<int[]> intArrayArgumentCaptor = ArgumentCaptor.forClass(int[].class);
        Mockito.verify(target).testMethod(integerArgumentCaptor.capture(), intArrayArgumentCaptor.capture());
        Integer capturedInteger = integerArgumentCaptor.getValue();
        int[] capturedIntArray = intArrayArgumentCaptor.getValue();

        assertThat(capturedInteger, is(intValue));
        assertThat(capturedIntArray, is(intArray));
    }

    @Test
    public void testInvocationMatcherWithVarargsAndNullMatcher() {
        invocation = new InvocationWithMocks(new Method(getClass(), "testMethod", int[].class), new Object[]{new int[0]});
        Matcher<Integer> nullMatcher = Mockito.isNull();
        matchers = Arrays.asList(Mockito.any(int.class), nullMatcher);
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        MyClass target = spy(new MyClass());
        int intValue = 1;
        int[] intArray = null;
        invocationMatcher.getInvocation().call(target, intValue, intArray);

        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<int[]> intArrayArgumentCaptor = ArgumentCaptor.forClass(int[].class);
        Mockito.verify(target).testMethod(integerArgumentCaptor.capture(), intArrayArgumentCaptor.capture());
        Integer capturedInteger = integerArgumentCaptor.getValue();
        int[] capturedIntArray = intArrayArgumentCaptor.getValue();

        assertThat(capturedInteger, is(intValue));
        assertThat(capturedIntArray, is(nullValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvocationMatcherWithInvocationWithoutMethod() {
        invocation = new InvocationWithMocks(null, new Object[0]);
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvocationMatcherWithInvocationWithoutMock() {
        invocation = new InvocationWithMocks(new Method(getClass(), "testMethod"), null);
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvocationMatcherWithNullMatchers() {
        invocation = new InvocationWithMocks(new Method(getClass(), "testMethod"), new Object[0]);
        invocationMatcher = new InvocationMatcher(invocation, null);
    }

    @Test
    public void testInvocationMatcherWithCustomMatchersAndNonEmptyList() {
        invocation = new InvocationWithMocks(new Method(getClass(), "testMethod"), new Object[0]);
        Matcher<Integer> customMatcher = new CustomMatcher();
        List<Matcher> matchers = Arrays.asList(customMatcher);
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        MyClass target = spy(new MyClass());
        int intValue = 1;
        invocationMatcher.getInvocation().call(target, intValue);

        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(target).testMethod(argumentCaptor.capture());
        Integer captured = argumentCaptor.getValue();

        // Custom matcher logic here
        assertTrue(customMatcher.matches(captured));
    }
}

class CustomMatcher implements Matcher<Integer> {
    @Override
    public boolean matches(Object argument) {
        Integer intArgument = (Integer) argument;
        return intArgument > 0;
    }
}

class MyClass {
    public void testMethod(Integer i, int... ints) {
        // Test method implementation
    }
}