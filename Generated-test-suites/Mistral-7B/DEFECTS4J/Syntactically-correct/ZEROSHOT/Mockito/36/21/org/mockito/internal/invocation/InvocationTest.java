package org.mockito.tests.internal.invocation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
import org.mockito.internal.util.PrintingFriendlyInvocation;
import org.mockito.internal.util.MockitoMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.MockUtil.newMock;

public class InvocationTest {

    private Invocation invocation;
    private Object mock;
    private MockitoMethod mockitoMethod;
    private RealMethod realMethod;

    @Before
    public void setUp() throws Exception {
        mock = newMock(String.class);
        Class<?> clazz = String.class;
        mockitoMethod = new MockitoMethod(new Method("toString", new Class[0]) {
        }, clazz);
        realMethod = new RealMethod(mockitoMethod, new Object[0]) {
            @Override
            public Object invoke(Object instance, Object[] arguments) throws Throwable {
                return "result";
            }
        };

        invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, realMethod);
    }

    @Test
    public void shouldReturnCorrectMockObject() {
        // when
        Object actual = invocation.getMock();

        // then
        assertEquals(mock, actual);
    }

    @Test
    public void shouldReturnCorrectMethodObject() {
        // when
        MockitoMethod actual = invocation.getMethod();

        // then
        assertEquals(mockitoMethod, actual);
    }

    @Test
    public void shouldReturnCorrectArgumentsArray() {
        // when
        Object[] actual = invocation.getArguments();

        // then
        assertArrayEquals(new Object[0], actual);
    }

    @Test
    public void shouldReturnTrueWhenInvocationIsVerified() {
        // when
        invocation.markVerified();

        // then
        assertTrue(invocation.isVerified());
    }

    @Test
    public void shouldReturnFalseWhenInvocationIsNotVerified() {
        // when
        assertFalse(invocation.isVerified());

        // then
        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void shouldReturnTrueWhenInvocationIsVerifiedInOrder() {
        // when
        invocation.markVerified();
        invocation.markVerifiedInOrder();

        // then
        assertTrue(invocation.isVerifiedInOrder());
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionWhenInvokingHashCodeMethod() {
        // when
        invocation.hashCode();

        // then
        fail("hashCode() should not be called");
    }

    @Test
    public void shouldReturnCorrectToStringValue() {
        // when
        String actual = invocation.toString();

        // then
        assertThat(actual, notNullValue());
    }

    @Test
    public void shouldReturnCorrectToStringValueWithArguments() {
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocationWithArguments = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        // when
        String actual = invocationWithArguments.toString();

        // then
        List<Matcher> matchers = invocationWithArguments.argumentsToMatchers();
        PrintSettings printSettings = new PrintSettings();
        String expected = mockitoMethod.getName() + new MatchersPrinter().getArgumentsBlock(matchers, printSettings);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnTrueWhenInvocationMatchesAnotherInvocation() {
        // given
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocationWithArguments = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        // when
        Invocation anotherInvocation = new Invocation(mock, mockitoMethod, new Object[0], 1, realMethod);

        // then
        assertTrue(invocationWithArguments.equals(anotherInvocation));
    }

    @Test
    public void shouldReturnFalseWhenInvocationsDoNotMatch() {
        // given
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocationWithArguments = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        // given
        MockitoMethod anotherMockitoMethod = new MockitoMethod(new Method("anotherMethod", new Class[0]) {
        }, Mockito.class);

        // when
        Invocation anotherInvocation = new Invocation(mock, anotherMockitoMethod, new Object[0], 1, realMethod);

        // then
        assertFalse(invocationWithArguments.equals(anotherInvocation));
    }

    @Test
    public void shouldReturnCorrectReturnValueWhenInvokingRealMethod() throws Exception {
        // when
        String actual = (String) invocation.callRealMethod();

        // then
        assertEquals("result", actual);
    }

    @Test
    public void shouldThrowMockitoExceptionWhenInvocationIsNotVerified() throws Exception {
        // when
        // then
        try {
            invocation.callRealMethod();
        } catch (MockitoException e) {
            // then
            assertEquals("Invocation on mock was not verified", e.getMessage());
            return;
        }

        fail("MockitoException should be thrown");
    }

    @Test
    public void shouldReturnCorrectReturnTypeWhenInvokingRealMethod() throws Exception {
        // given
        Class<?> expectedReturnType = Integer.TYPE;
        RealMethod realMethodWithReturnType = new RealMethod(mockitoMethod, new Object[]{}, expectedReturnType, new Object[0]) {
            @Override
            public Object invoke(Object instance, Object[] arguments) throws Throwable {
                return 1;
            }
        };
        Invocation invocationWithReturnType = new Invocation(mock, mockitoMethod, new Object[0], 1, realMethodWithReturnType);

        // when
        int actual = (Integer) invocationWithReturnType.callRealMethod();

        // then
        assertEquals(1, actual);
    }

    @Test
    public void shouldReturnFalseWhenInvocationHasDifferentMethodName() throws Exception {
        // given
        MockitoMethod anotherMockitoMethod = new MockitoMethod(new Method("anotherMethod", new Class[0]) {
        }, Mockito.class);

        // when
        Invocation anotherInvocation = new Invocation(mock, anotherMockitoMethod, new Object[0], 1, realMethod);

        // then
        assertFalse(invocation.equals(anotherInvocation));
    }

    @Test
    public void shouldReturnTrueWhenInvocationHasTheSameMethodNameAndArguments() throws Exception {
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocationWithArguments = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        Invocation anotherInvocationWithArguments = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        // then
        assertTrue(invocationWithArguments.equals(anotherInvocationWithArguments));
    }

    @Test
    public void shouldReturnFalseWhenInvocationHasTheSameMethodNameButDifferentArguments() throws Exception {
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocationWithArguments = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        Object[] anotherArguments = new Object[]{2, "test2"};
        Invocation anotherInvocationWithArguments = new Invocation(mock, mockitoMethod, anotherArguments, 1, realMethod);

        // then
        assertFalse(invocationWithArguments.equals(anotherInvocationWithArguments));
    }

    @Test
    public void shouldReturnFalseWhenInvocationHasDifferentSequenceNumber() throws Exception {
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocationWithArguments = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        Invocation anotherInvocationWithDifferentSequenceNumber = new Invocation(mock, mockitoMethod, arguments, 2, realMethod);

        // then
        assertFalse(invocationWithArguments.equals(anotherInvocationWithDifferentSequenceNumber));
    }

    @Test
    public void shouldReturnTrueWhenInvocationHasTheSameSequenceNumber() throws Exception {
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocationWithArguments = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        Invocation anotherInvocationWithTheSameSequenceNumber = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        // then
        assertTrue(invocationWithArguments.equals(anotherInvocationWithTheSameSequenceNumber));
    }

    @Test
    public void shouldReturnTrueWhenInvocationIsAnInstanceOfPrintingFriendlyInvocation() {
        // given
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, realMethod);

        // when
        PrintingFriendlyInvocation printingFriendlyInvocation = (PrintingFriendlyInvocation) invocation;

        // then
        assertTrue(invocation instanceof PrintingFriendlyInvocation);
    }

    @Test(expected = MockitoException.class)
    public void shouldThrowMockitoExceptionWhenInvokingMethodWithInvalidReturnType() throws Exception {
        // given
        Class<?> invalidReturnType = Double.TYPE;

        // when
        try {
            new Invocation(mock, mockitoMethod, new Object[0], 1, realMethod).isValidReturnType(invalidReturnType);
        } catch (MockitoException e) {
            // then
            assertEquals("Return type [" + invalidReturnType.getName() + "] is not valid", e.getMessage());
            return;
        }

        fail("MockitoException should be thrown");
    }

    @Test
    public void shouldReturnTrueWhenInvocationHasValidReturnType() throws Exception {
        // given
        Class<?> validReturnType = String.class;

        // when
        assertTrue(invocation.isValidReturnType(validReturnType));
    }

    @Test
    public void shouldReturnTrueWhenInvocationHasValidException() throws Exception {
        // given
        Throwable exception = new RuntimeException("test exception");

        // when
        assertTrue(invocation.isValidException(exception));
    }

    @Test
    public void shouldReturnFalseWhenInvocationDoesNotHaveValidException() throws Exception {
        // given
        Throwable exception = new NullPointerException();

        // when
        assertFalse(invocation.isValidException(exception));
    }

    @Test
    public void shouldReturnTrueWhenInvocationHasVoidReturnType() throws Exception {
        // when
        assertTrue(invocation.isVoid());
    }

    @Test
    public void shouldReturnCorrectReturnTypeName() throws Exception {
        // when
        String actual = invocation.printMethodReturnType();

        // then
        assertEquals("void", actual);
    }

    @Test
    public void shouldReturnCorrectMethodName() throws Exception {
        // when
        String actual = invocation.getMethodName();

        // then
        assertEquals(mockitoMethod.getName(), actual);
    }

    @Test
    public void shouldReturnTrueWhenInvocationIsToString() throws Exception {
        // when
        assertTrue(Invocation.isToString(invocation));
    }

    @Test
    public void shouldReturnFalseWhenInvocationIsNotToString() throws Exception {
        // given
        Invocation anotherInvocation = new Invocation(mock, mockitoMethod, new Object[0], 1, realMethod);

        // when
        assertFalse(Invocation.isToString(anotherInvocation));
    }

    @Test
    public void shouldReturnTrueWhenInvocationHasValidArguments() throws Exception {
        // given
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocationWithArguments = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        // when
        assertTrue(Arrays.equals(invocationWithArguments.getRawArguments(), arguments));
    }

    @Test
    public void shouldReturnFalseWhenInvocationHasInvalidArguments() throws Exception {
        // given
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocationWithArguments = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        // given
        Object[] invalidArguments = new Object[]{new Object(), new Object()};

        // when
        assertFalse(invocationWithArguments.equals(new Invocation(mock, mockitoMethod, invalidArguments, 1, realMethod)));
    }

    @Test
    public void shouldReturnCorrectToStringValueWithCustomPrintSettings() throws Exception {
        // given
        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(true);

        // when
        String actual = invocation.toString(printSettings);

        // then
        List<Matcher> matchers = invocation.argumentsToMatchers();
        String expected = mockitoMethod.getName() + new MatchersPrinter().getArgumentsBlock(matchers, printSettings);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnToStringValueWithCustomPrintSettingsWhenInvocationHasArguments() throws Exception {
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocationWithArguments = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        // given
        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(true);

        // when
        String actual = invocationWithArguments.toString(printSettings);

        // then
        List<Matcher> matchers = invocationWithArguments.argumentsToMatchers();
        String expected = mockitoMethod.getName() + new MatchersPrinter().getArgumentsBlock(matchers, printSettings);
        assertEquals(expected, actual);
    }

    private void assertArrayEquals(Object[] expected, Object[] actual) {
        if (!Arrays.equals(expected, actual)) {
            fail("Expected: " + Arrays.toString(expected) + ", but was: " + Arrays.toString(actual));
        }
    }
}