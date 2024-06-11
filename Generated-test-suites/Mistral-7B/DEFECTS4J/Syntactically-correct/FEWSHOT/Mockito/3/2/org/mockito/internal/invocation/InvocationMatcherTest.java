package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.matchers.ArgumentsMatcher.argumentsWithAnyValues;
import static org.mockito.internal.matchers.ArgumentsMatcher.argumentsWithSameValues;
import static org.mockito.internal.matchers.ArgumentsMatcher.isA;
import static org.mockito.internal.matchers.ArgumentsMatcher.isInstanceOf;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MockInvocation;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

public class InvocationMatcherTest {

    private static final String CLASS_UNDER_TEST = "org.mockito.internal.invocation.InvocationMatcher";
    private static final String METHOD_UNDER_TEST = "hasSameMethod";

    private MockInvocation invocation1;
    private MockInvocation invocation2;
    private MockInvocation invocation3;
    private MockInvocation invocation4;
    private Object mock;

    @Before
    public void setUp() {
        mock = mock(Object.class);
        invocation1 = new MockInvocation(mock, "m1", new Object[0]);
        invocation2 = new MockInvocation(mock, "m1", new Object[]{1});
        invocation3 = new MockInvocation(mock, "m2", new Object[]{1, 2});
        invocation4 = new MockInvocation(mock, "m1", new Object[]{1, 2, 3});
    }

    @Test
    public void testHasSameMethod_sameMethod_sameMock_unverified() {
        InvocationMatcher im1 = new InvocationMatcher(invocation1);
        InvocationMatcher im2 = new InvocationMatcher(invocation1);

        assertTrue(im1.hasSameMethod(im2));
    }

    @Test
    public void testHasSameMethod_sameMethod_sameMock_verified() {
        invocation1.proceed();
        invocation2.proceed();

        InvocationMatcher im1 = new InvocationMatcher(invocation1);
        InvocationMatcher im2 = new InvocationMatcher(invocation2);

        assertFalse(im1.hasSameMethod(im2));
    }

    @Test
    public void testHasSameMethod_sameMethod_differentMocks() {
        MockInvocation invocationMock1 = mock(MockInvocation.class);
        MockInvocation invocationMock2 = mock(MockInvocation.class);

        InvocationMatcher im1 = new InvocationMatcher(invocationMock1);
        InvocationMatcher im2 = new InvocationMatcher(invocationMock2);

        assertFalse(im1.hasSameMethod(im2));
    }

    @Test
    public void testHasSameMethod_differentMethods_sameMock_unverified() {
        InvocationMatcher im1 = new InvocationMatcher(invocation1);
        InvocationMatcher im2 = new InvocationMatcher(invocation3);

        assertFalse(im1.hasSameMethod(im2));
    }

    @Test
    public void testHasSameMethod_differentMethods_sameMock_verified() {
        invocation1.proceed();
        invocation3.proceed();

        InvocationMatcher im1 = new InvocationMatcher(invocation1);
        InvocationMatcher im2 = new InvocationMatcher(invocation3);

        assertFalse(im1.hasSameMethod(im2));
    }

    @Test
    public void testHasSameMethod_differentMethods_differentMocks() {
        MockInvocation invocationMock1 = mock(MockInvocation.class);
        MockInvocation invocationMock2 = mock(MockInvocation.class);

        InvocationMatcher im1 = new InvocationMatcher(invocationMock1);
        InvocationMatcher im2 = new InvocationMatcher(invocationMock2);

        assertFalse(im1.hasSameMethod(im2));
    }

    @Test
    public void testHasSameMethod_methodWithVarArgs_sameMock_unverified() {
        InvocationMatcher im1 = new InvocationMatcher(invocation1);
        InvocationMatcher im2 = new InvocationMatcher(invocation2);

        assertTrue(im1.hasSameMethod(im2));
    }

    @Test
    public void testHasSameMethod_methodWithVarArgs_sameMock_verified() {
        invocation1.proceed();
        invocation2.proceed();

        InvocationMatcher im1 = new InvocationMatcher(invocation1);
        InvocationMatcher im2 = new InvocationMatcher(invocation2);

        assertFalse(im1.hasSameMethod(im2));
    }

    @Test
    public void testHasSameMethod_methodWithVarArgs_differentMocks() {
        MockInvocation invocationMock1 = mock(MockInvocation.class);
        MockInvocation invocationMock2 = mock(MockInvocation.class);

        InvocationMatcher im1 = new InvocationMatcher(invocationMock1);
        InvocationMatcher im2 = new InvocationMatcher(invocationMock2);

        assertFalse(im1.hasSameMethod(im2));
    }

    @Test
    public void testHasSameMethod_methodWithVarArgs_argumentsMatch() {
        Object[] args1 = new Object[]{1};
        Object[] args2 = new Object[]{1};

        InvocationMatcher im1 = new InvocationMatcher(new MockInvocation(mock, "m1", args1));
        InvocationMatcher im2 = new InvocationMatcher(new MockInvocation(mock, "m1", args2));

        assertTrue(im1.hasSameMethod(im2));
    }

    @Test
    public void testHasSameMethod_methodWithVarArgs_argumentsDontMatch() {
        Object[] args1 = new Object[]{1};
        Object[] args2 = new Object[]{2};

        InvocationMatcher im1 = new InvocationMatcher(new MockInvocation(mock, "m1", args1));
        InvocationMatcher im2 = new InvocationMatcher(new MockInvocation(mock, "m1", args2));

        assertFalse(im1.hasSameMethod(im2));
    }

    @Test
    public void testHasSameMethod_methodWithVarArgs_differentMethods() {
        Object[] args1 = new Object[]{1};
        Object[] args2 = new Object[]{1, 2};

        InvocationMatcher im1 = new InvocationMatcher(new MockInvocation(mock, "m1", args1));
        InvocationMatcher im2 = new InvocationMatcher(new MockInvocation(mock, "m1", args2));

        assertFalse(im1.hasSameMethod(im2));
    }

    @Test
    public void testHasSameMethod_methodWithVarArgs_differentMethods_argumentsMatch() {
        Object[] args1 = new Object[]{1, 2};
        Object[] args2 = new Object[]{1, 2};

        InvocationMatcher im1 = new InvocationMatcher(new MockInvocation(mock, "m1", args1));
        InvocationMatcher im2 = new InvocationMatcher(new MockInvocation(mock, "m2", args2));

        assertFalse(im1.hasSameMethod(im2));
    }

    @Test
    public void testHasSameMethod_methodWithVarArgs_differentMethods_argumentsDontMatch() {
        Object[] args1 = new Object[]{1, 2};
        Object[] args2 = new Object[]{1};

        InvocationMatcher im1 = new InvocationMatcher(new MockInvocation(mock, "m1", args1));
        InvocationMatcher im2 = new InvocationMatcher(new MockInvocation(mock, "m2", args2));

        assertFalse(im1.hasSameMethod(im2));
    }

    @Test
    public void testToString() {
        InvocationMatcher im = new InvocationMatcher(invocation1, Arrays.asList(isA(Integer.class), argumentsWithAnyValues()));

        String expected = new PrintSettings().print(Arrays.asList(isA(Integer.class), argumentsWithAnyValues()), invocation1);

        assertEquals(expected, im.toString());
    }

    @After
    public void cleanUp() {
        Arrays.stream(new Object[]{invocation1, invocation2, invocation3, invocation4}).forEach(i -> i.getMock().reset());
    }
}