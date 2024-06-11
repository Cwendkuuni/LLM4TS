package org.mockito.internal.invocation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMockArgumentsCaptor;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testNewInstanceWithEmptyMatcherList() {
        // Given
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // When
        Method method = matcher.getMethod();

        // Then
        assertThat(method, is(notNullValue()));
        assertThat(matcher.getMatchers(), is(Collections.emptyList()));
    }

    @Test
    public void testNewInstanceWithMatcherList() {
        // Given
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(Mockito.argThat(new MyMatcher()), Mockito.anyInt());
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        // When
        Method method = matcher.getMethod();

        // Then
        assertThat(method, is(notNullValue()));
        assertThat(matcher.getMatchers(), is(matchers));
    }

    @Test
    public void testGetInvocation() {
        // Given
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // When
        Invocation result = matcher.getInvocation();

        // Then
        verify(invocation).getInvocation();
        assertThat(result, is(invocation));
    }

    @Test
    public void testGetMatchers() {
        // Given
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // When
        List<Matcher> result = matcher.getMatchers();

        // Then
        verify(invocation).getArgumentsToMatchers();
        assertThat(result, is(Collections.emptyList()));
    }

    @Test
    public void testMatches() {
        // Given
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation1, Arrays.asList(Mockito.eq(invocation2)));

        // When
        boolean result = matcher.matches(new InvocationOnMock() {
            @Override
            public Object invoke(Object... arguments) throws Throwable {
                return null;
            }

            @Override
            public Method getMethod() {
                return new Method("testMethod", new Class[0]);
            }

            @Override
            public Object getMock() {
                return invocation2;
            }

            @Override
            public Location getLocation() {
                return new Location(1, 1, "TestClass.java");
            }
        });

        // Then
        assertThat(result, is(true));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        // Given
        InvocationMatcher matcher = new InvocationMatcher(mock(Invocation.class), new Object[]{});

        // When
        boolean result = matcher.safelyArgumentsMatch(new Object[]{1, "test"});

        // Then
        assertThat(result, is(false));
    }

    @Test
    public void testHasSimilarMethod() {
        // Given
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        // When
        boolean result = matcher.hasSimilarMethod(new InvocationOnMock() {
            @Override
            public Object invoke(Object... arguments) throws Throwable {
                return null;
            }

            @Override
            public Method getMethod() {
                return new Method("testMethod", new Class[0]);
            }

            @Override
            public Object getMock() {
                return invocation2;
            }

            @Override
            public Location getLocation() {
                return new Location(1, 1, "TestClass.java");
            }
        });

        // Then
        assertThat(result, is(false));
    }

    @Test
    public void testHasSameMethod() {
        // Given
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);

        // When
        Method method1 = invocation1.getMethod();
        Method method2 = invocation2.getMethod();
        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        // Then
        boolean result = matcher.hasSameMethod(new InvocationOnMock() {
            @Override
            public Object invoke(Object... arguments) throws Throwable {
                return null;
            }

            @Override
            public Method getMethod() {
                return method1;
            }

            @Override
            public Object getMock() {
                return invocation2;
            }

            @Override
            public Location getLocation() {
                return new Location(1, 1, "TestClass.java");
            }
        });

        // Then
        assertThat(result, is(false));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // Given
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        InvocationOnMockArgumentsCaptor argumentsCaptor = Mockito.argumentCaptor(InvocationOnMock.class);

        // When
        matcher.captureArgumentsFrom(Mockito.mock(InvocationOnMock.class));

        // Then
        InvocationOnMock result = argumentsCaptor.getValue();
        verify(invocation).getArguments();
        assertThat(result, is(notNullValue()));
    }
}