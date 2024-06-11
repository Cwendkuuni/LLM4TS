package org.mockito.tests;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class InvocationTest {

    @Test
    public void testInvocationCreation() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] args = new Object[]{new String("Test")};
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        assert invocation.getMock() == mock;
        assert invocation.getMethod() == mockitoMethod;
        assert invocation.getArguments().length == 1;
        assert invocation.isVerified() == false;
        assert invocation.isVerifiedInOrder() == false;
        assert invocation.getSequenceNumber() == sequenceNumber;
    }

    @Test
    public void testEquals() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] args = new Object[]{new String("Test")};
        Object[] args2 = new Object[]{new String("Test2")};
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        RealMethod realMethod2 = Mockito.mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);
        Invocation invocation2 = new Invocation(mock, mockitoMethod, args2, sequenceNumber, realMethod2);

        assert invocation.equals(invocation) == true;
        assert invocation.equals(invocation2) == false;
    }

    @Test
    public void testToString() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] args = new Object[]{new String("Test")};

        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, Mockito.mock(RealMethod.class));

        List<Matcher> matchers = invocation.argumentsToMatchers();
        Matcher mockMatcher = ArgumentMatchers.eq(mock);
        matchers.set(0, mockMatcher);

        String expectedResult = "Test length\n    at org.mockito.tests.InvocationTest.<anonymous InvocationTest.testToString>(InvocationTest.java:62)\n";
        String result = invocation.toString();

        assert result.equals(expectedResult);
    }

    @Test
    public void testIsToString() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "toString");

        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, Mockito.mock(RealMethod.class));

        assert Invocation.isToString(invocation) == true;
    }

    @Test
    public void testIsValidException() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] args = new Object[]{new String("Test")};
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        Throwable throwable = new RuntimeException();

        assert invocation.isValidException(throwable) == false;

        mockitoMethod.addExceptionType(RuntimeException.class);

        assert invocation.isValidException(throwable) == true;
    }

    @Test
    public void testIsValidReturnType() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] args = new Object[]{new String("Test")};
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        assert invocation.isValidReturnType(int.class) == true;
        assert invocation.isValidReturnType(String.class) == true;
        assert invocation.isValidReturnType(Float.class) == false;
    }

    @Test
    public void testIsVoid() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Object[] args = new Object[]{new String("Test")};
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        assert invocation.isVoid() == false;
    }

    @Test
    public void testPrintMethodReturnType() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Object[] args = new Object[]{new String("Test")};
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        assert invocation.printMethodReturnType().equals("int");
    }

    @Test
    public void testGetMethodName() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Object[] args = new Object[]{new String("Test")};
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        assert invocation.getMethodName().equals("length");
    }

    @Test
    public void testReturnsPrimitive() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Object[] args = new Object[]{new String("Test")};
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        assert invocation.returnsPrimitive() == false;
    }

    @Test
    public void testGetLocation() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Object[] args = new Object[]{new String("Test")};
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        assert invocation.getLocation() != null;
    }

    @Test
    public void testGetArgumentsCount() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Object[] args = new Object[]{new String("Test")};
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        assert invocation.getArgumentsCount() == 0;
    }

    @Test
    public void testCallRealMethod() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Object[] args = new Object[]{new String("Test")};
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        int expectedResult = 4;

        when(realMethod.invoke(mock, args)).thenReturn(expectedResult);

        int result = invocation.callRealMethod();

        assert result == expectedResult;
    }

    @Test
    public void testMarkVerified() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Object[] args = new Object[]{new String("Test")};
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        invocation.markVerified();

        assert invocation.isVerified() == true;
    }

    @Test
    public void testMarkVerifiedInOrder() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Object[] args = new Object[]{new String("Test1")};
        Object[] args2 = new Object[]{new String("Test2")};
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation1 = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);
        Invocation invocation2 = new Invocation(mock, mockitoMethod, args2, sequenceNumber, realMethod);

        invocation1.markVerifiedInOrder();
        invocation2.markVerifiedInOrder();

        verify(mock).length();
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testVerifyZeroInteractions() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Object[] args = new Object[]{new String("Test")};
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        verifyZeroInteractions(mock);
    }
}