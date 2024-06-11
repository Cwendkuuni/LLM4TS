package org.mockito.tests.internal.invocation;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.MockitoException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.junit.runners.MockitoJUnitRunner;
import org.mockito.matchers.ArgumentsMatchers;
import org.mockito.mock.MockitoMock;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.Spy;
import org.mockito.plugins.SpyEngine;
import org.mockito.reporting.PrintSettings;
import org.mockito.reporting.VerificationMode;
import org.mockito.stubbing.Answer;
import org.mockito.util.MockUtil;
import org.mockito.util.Primitives;
import org.mockito.verification.VerificationModeFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class InvocationTest {

    @MockMaker
    private static MockMaker mockMaker = new MockMaker();

    @Spy
    private static SpyEngine spyEngine = new SpyEngine();

    @Test
    public void testNewInstance() {
        // given
        Object mock = mockMaker.createMock(String.class);
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[]{argThat(is("test"))};
        RealMethod realMethod = mock(RealMethod.class);
        int sequenceNumber = 42;

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, sequenceNumber, realMethod);

        // then
        assertEquals(mock, invocation.getMock());
        assertEquals(mockitoMethod, invocation.getMethod());
        assertArrayEquals(arguments, invocation.getArguments());
        assertEquals(sequenceNumber, invocation.getSequenceNumber());
        assertFalse(invocation.isVerified());
        assertFalse(invocation.isVerifiedInOrder());
        assertEquals(realMethod, invocation.getRealMethod());
        assertEquals(locationOfCallingTestMethod(), invocation.getLocation());
    }

    @Test
    public void testExpandVarArgs() {
        // given
        Object mock = mockMaker.createMock(String[].class);
        Object[] arguments = new Object[]{argThat(isArrayWithSize(2)), argThat(is(new String[]{"a", "b"}))};

        // when
        Object[] expandedArguments = Invocation.expandVarArgs(true, arguments);

        // then
        assertArrayEquals(new Object[]{null, new String[]{"a", "b"}}, expandedArguments);
    }

    @Test
    public void testVerify() {
        // given
        Object mock = mockMaker.createMock(String.class);
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[]{argThat(is("test"))};
        RealMethod realMethod = mock(RealMethod.class);
        int sequenceNumber = 42;
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, sequenceNumber, realMethod);

        // when
        verify(mock).method(eq("test"));

        // then
        assertTrue(invocation.isVerified());
        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testVerifyInOrder() {
        // given
        Object mock = mockMaker.createMock(String.class);
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments1 = new Object[]{argThat(is("test1"))};
        Object[] arguments2 = new Object[]{argThat(is("test2"))};
        RealMethod realMethod = mock(RealMethod.class);
        int sequenceNumber1 = 42;
        int sequenceNumber2 = 43;
        Invocation invocation1 = new Invocation(mock, mockitoMethod, arguments1, sequenceNumber1, realMethod);
        Invocation invocation2 = new Invocation(mock, mockitoMethod, arguments2, sequenceNumber2, realMethod);

        // when
        inOrder(mock).method(eq("test1"));
        inOrder(mock).method(eq("test2"));

        // then
        assertTrue(invocation1.isVerified());
        assertTrue(invocation1.isVerifiedInOrder());
        assertTrue(invocation2.isVerified());
        assertTrue(invocation2.isVerifiedInOrder());
        assertEquals(invocation1, invocationInOrder(0));
        assertEquals(invocation2, invocationInOrder(1));
    }

    @Test
    public void testToString() {
        // given
        Object mock = mockMaker.createMock(String.class);
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[]{argThat(is("test"))};
        RealMethod realMethod = mock(RealMethod.class);
        int sequenceNumber = 42;
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, sequenceNumber, realMethod);

        // when
        String result = invocation.toString();

        // then
        String expected = String.format("%s(%s)", mockitoMethod.getName(), Arrays.toString(arguments));
        assertEquals(expected, result);
    }

    @Test(expected = MockitoException.class)
    public void testVerifyExceptionsThrown() {
        // given
        Object mock = mockMaker.createMock(String.class);
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[]{argThat(is("test"))};
        RealMethod realMethod = mock(RealMethod.class);
        int sequenceNumber = 42;
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, sequenceNumber, realMethod);

        // when, then
        doThrow(new RuntimeException("test")).when(mock).method(eq("test"));
        verify(mock).method(eq("test"));
    }

    @Test
    public void testInvokeRealMethod() {
        // given
        Object mock = mockMaker.createMock(String.class);
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[]{argThat(is("test"))};
        RealMethod realMethod = mock(RealMethod.class);
        int sequenceNumber = 42;
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, sequenceNumber, realMethod);

        // when
        String result = (String) invocation.callRealMethod();

        // then
        assertEquals("test", result);
    }

    private Object[] argThat(Matcher<?> matcher) {
        return new Object[]{matcher};
    }

    private Object[] argThat(Class<? extends Matcher> matcherClass) {
        return new Object[]{MatcherFactory.getMatcher(matcherClass)};
    }

    private Object[] argThat(IsInstanceOf<?> matcher) {
        return new Object[]{matcher.getUsing()};
    }

    private Object[] argThat(IsA<?> matcher) {
        return new Object[]{matcher.getUsing()};
    }

    private Object[] argThat(IsNotInstanceOf<?> matcher) {
        return new Object[]{matcher.getUsing()};
    }

    private Object[] argThat(IsNotA<?> matcher) {
        return new Object[]{matcher.getUsing()};
    }

    private Object[] argThat(ArgumentMatcher<?> matcher) {
        return new Object[]{matcher};
    }

    private Object[] argThat(ArgumentsMatcher<? extends Object[]> matcher) {
        return new Object[]{matcher.match(new Object[0])};
    }

    private Object[] argThat(ArgumentsMatchers matcher) {
        return new Object[]{matcher};
    }

    private Object[] argThat(ArrayMatcher<?, ?> matcher) {
        return new Object[]{matcher.argThat(is(notNullValue()))};
    }

    private Object[] argThat(ArrayContains<?, ?> matcher) {
        return new Object[]{matcher.getArray(), matcher.getContainee()};
    }

    private Object[] argThat(HasProperty matcher) {
        return new Object[]{matcher.getObject(), matcher.getPropertyName(), matcher.getMatcher()};
    }

    private Object[] argThat(hasField matcher) {
        return new Object[]{matcher.getObject(), matcher.getFieldName(), matcher.getMatcher()};
    }

    private Object[] argThat(hasProperty matcher) {
        return new Object[]{matcher.getObject(), matcher.getPropertyName(), matcher.getMatcher()};
    }

    private Object[] argThat(hasMethod matcher) {
        return new Object[]{matcher.getObject(), matcher.getMethodName(), matcher.getMatcher()};
    }

    private Object[] argThat(hasPropertyValue matcher) {
        return new Object[]{matcher.getObject(), matcher.getFieldName(), matcher.getValue()};
    }

    private Object[] argThat(hasMethodValue matcher) {
        return new Object[]{matcher.getObject(), matcher.getMethodName(), matcher.getValue()};
    }

    private Object[] argThat(returns matcher) {
        return new Object[]{matcher.getMock(), matcher.getMethod(), matcher.getMatcher()};
    }

    private Object[] argThat(returnsReferences matcher) {
        return new Object[]{matcher.getMock(), matcher.getMethod(), matcher.getMatcher()};
    }

    private Object[] argThat(returnsSame matcher) {
        return new Object[]{matcher.getMock(), matcher.getExpected()};
    }

    private Object[] argThat(argument matcher) {
        return new Object[]{matcher.getIndex(), matcher.getMatcher()};
    }

    private Object[] argThat(arguments matcher) {
        return new Object[]{matcher.getMatchers()};
    }

    private Object[] argThat(Answer<?> answer) {
        return new Object[]{answer};
    }

    private Object[] argThat(VerificationMode verificationMode) {
        return new Object[]{verificationMode, eq(VerificationModeFactory.times(1))};
    }

    private Object[] argThat(VerificationModeFactory verificationModeFactory) {
        return new Object[]{verificationModeFactory, eq(VerificationModeFactory.never())};
    }

    private Object[] argThat(InvocationOnMock invocation) {
        return new Object[]{invocation};
    }

    private Object[] argThat(IsToString invocation) {
        return new Object[]{invocation};
    }

    private Invocation invocationInOrder(int index) {
        return mockitoSession().getMockitoSession().getMockVerificationOrder().getInvocations().get(index);
    }

    private Location locationOfCallingTestMethod() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        int index = 1;
        while (index < stackTrace.length && !stackTrace[index].getClassName().startsWith("org.mockito")) {
            index++;
        }
        return new Location(stackTrace[index]);
    }

    private MockitoSession mockitoSession() {
        return spyEngine.getMockingContext().getMockitoSession();
    }
}