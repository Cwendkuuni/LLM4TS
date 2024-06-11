package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stub;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.internal.matchers.ArgumentsMatcher.argumentsWithinPercentageDifference;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher() {
        // Test constructor with no matchers
        Invocation invocation = mock(InvocationOnMock.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Method method = invocation.getMethod();

        // Test getters
        assertEquals(invocation, matcher.getInvocation());
        assertEquals(method, matcher.getMethod());
        assertNotNull(matcher.getMatchers());
        assertEquals(0, matcher.getMatchers().size());

        // Test toString
        assertEquals(String.format("InvocationMatcher [%s]", method.toString()), matcher.toString());

        // Test matches
        InvocationMatcher otherInvocationMatcher = new InvocationMatcher(invocation);
        assertTrue(matcher.matches(otherInvocationMatcher));

        // Test hasSameMethod
        Invocation otherInvocation = mock(InvocationOnMock.class);
        when(otherInvocation.getMethod()).thenReturn(new Method(InvocationMatcher.class.getName(), new Class[0]));
        assertFalse(matcher.hasSameMethod(otherInvocation));

        // Test hasSimilarMethod
        Invocation similarInvocation = mock(InvocationOnMock.class);
        when(similarInvocation.getMethod()).thenReturn(new Method(method.getReturnType().getName(), method.getParameterTypes()));
        assertTrue(matcher.hasSimilarMethod(similarInvocation));

        // Test captureArgumentsFrom
        Stub stub = createStub(String.class);
        when(stub.toString()).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) {
                capture(argThat(eq("expectedString"))).inOrder();
                return "mockedString";
            }
        });
        Invocation invocationWithArgument = new InvocationOnMock(stub, new Method(String.class.getName(), String.class), new Object[]{"expectedString"});

        matcher.captureArgumentsFrom(invocationWithArgument);
        verify(stub).toString(argThat(argumentsWithinPercentageDifference(1.0, "expectedString")));

        // Test createFrom
        List<Invocation> invocations = Arrays.asList(invocation, invocationWithArgument);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        for (InvocationMatcher invocationMatcher : invocationMatchers) {
            assertEquals(invocation, invocationMatcher.getInvocation());
            assertNotNull(invocationMatcher.getMatchers());
            assertEquals(0, invocationMatcher.getMatchers().size());
        }

        // Test with matchers
        List<Matcher> matchers = Arrays.asList(argThat(argumentsWithinPercentageDifference(1.0, eq(1))), argThat(isA(Number.class)));
        InvocationMatcher matcherWithMatchers = new InvocationMatcher(invocation, matchers);
        assertEquals(matchers, matcherWithMatchers.getMatchers());
    }
}