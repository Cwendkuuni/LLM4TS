import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class InvocationTest {

    private MockitoMock mock;
    private InvocationOnMock invocation;

    @Before
    public void setup() {
        mock = Mockito.mock(MockitoMock.class);
    }

    @Test
    public void testConstructor() {
        Method method = Mockito.methodCalled("someMethod");
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);

        invocation = new Invocation(mock, method, arguments, sequenceNumber, realMethod);

        assertEquals(mock, invocation.getMock());
        assertEquals(method, invocation.getMethod());
        assertNull(invocation.getArguments());
        assertEquals(1, invocation.getSequenceNumber());
        assertEquals(realMethod, invocation.getRealMethod());
    }

    @Test
    public void testExpandVarArgs() {
        Object[] args = new Object[]{1, new Object[]{2, 3}, 4};
        Object[] expandedArgs = Invocation.expandVarArgs(true, args);

        assertArrayEquals(new Object[]{1, 2, 3, 4}, expandedArgs);
    }

    @Test
    public void testIsToString() {
        Method toStringMethod = Mockito.methodCalled("toString");

        assertTrue(Invocation.isToString(Mockito.mock(InvocationOnMock.class, toStringMethod)));
        assertFalse(Invocation.isToString(Mockito.mock(InvocationOnMock.class, new Method("someMethod"))));
    }

    @Test
    public void testEquals() {
        Object[] arguments = new Object[]{1, 2, 3};
        Object[] differentArguments = new Object[]{1, 2, 4};

        Invocation invocation1 = new Invocation(mock, Mockito.methodCalled("someMethod"), arguments, 1, Mockito.mock(RealMethod.class));
        Invocation invocation2 = new Invocation(mock, Mockito.methodCalled("someMethod"), arguments, 1, Mockito.mock(RealMethod.class));
        Invocation invocation3 = new Invocation(mock, Mockito.methodCalled("someMethod"), differentArguments, 1, Mockito.mock(RealMethod.class));

        assertTrue(invocation1.equals(invocation2));
        assertFalse(invocation1.equals(invocation3));
    }

    @Test
    public void testGetLocation() {
        Location expectedLocation = new Location();
        Mockito.when(mock.getLocation()).thenReturn(expectedLocation);

        Invocation invocation = new Invocation(mock, Mockito.methodCalled("someMethod"), new Object[0], 1, Mockito.mock(RealMethod.class));

        assertEquals(expectedLocation, invocation.getLocation());
    }

    @Test
    public void testCallRealMethod() throws Exception {
        Object[] arguments = new Object[]{1, 2};
        Object returnValue = 3;

        Mockito.when(mock.someMethod(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Invocation invocation = (Invocation) invocationOnMock;
                assertTrue(invocation.isVerifiedInOrder());
                return returnValue;
            }
        });

        Object result = invocation.callRealMethod();

        assertEquals(returnValue, result);
    }

    @Test(expected = RuntimeException.class)
    public void testHashCode() {
        Invocation invocation = new Invocation(mock, Mockito.methodCalled("someMethod"), new Object[0], 1, Mockito.mock(RealMethod.class));
        invocation.hashCode();
    }

    @Test
    public void testToString() {
        Object[] arguments = new Object[]{1, 2, 3};

        String expectedToString = "MockitoMock.someMethod(1, 2, 3)";
        String actualToString = invocation.toString();

        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testToStringWithPrintSettings() {
        Object[] arguments = new Object[]{1, 2, 3};
        PrintSettings printSettings = new PrintSettings();

        String expectedToString = "MockitoMock.someMethod([1, 2, 3])";
        String actualToString = invocation.toString(printSettings);

        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testIsVerified() {
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testIsVerifiedInOrder() {
        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerifiedInOrder());
    }

    @Test
    public void testIsVerifiedInOrder_multipleInvocations() {
        Object[] arguments1 = new Object[]{1, 2};
        Object[] arguments2 = new Object[]{3, 4};

        Invocation invocation1 = new Invocation(mock, Mockito.methodCalled("someMethod"), arguments1, 1, Mockito.mock(RealMethod.class));
        Invocation invocation2 = new Invocation(mock, Mockito.methodCalled("someMethod"), arguments2, 2, Mockito.mock(RealMethod.class));

        invocation1.markVerifiedInOrder();
        invocation2.markVerifiedInOrder();

        assertTrue(invocation1.isVerifiedInOrder());
        assertTrue(invocation2.isVerifiedInOrder());
        assertFalse(invocation1.isVerified());
        assertFalse(invocation2.isVerified());
        assertTrue(invocation1.isVerifiedBefore(invocation2));
        assertTrue(invocation2.isVerifiedBefore(invocation1));
    }

    @Test
    public void testIsVerifiedBefore() {
        Object[] arguments1 = new Object[]{1, 2};
        Object[] arguments2 = new Object[]{3, 4};

        Invocation invocation1 = new Invocation(mock, Mockito.methodCalled("someMethod"), arguments1, 1, Mockito.mock(RealMethod.class));
        Invocation invocation2 = new Invocation(mock, Mockito.methodCalled("someMethod"), arguments2, 2, Mockito.mock(RealMethod.class));

        invocation1.markVerifiedInOrder();
        invocation2.markVerifiedInOrder();

        assertTrue(invocation1.isVerifiedBefore(invocation2));
        assertTrue(invocation2.isVerifiedBefore(invocation1));
    }

    @Test
    public void testIsVoid() {
        Method voidMethod = Mockito.methodCalled("someVoidMethod");

        assertTrue(invocation.isVoid());
    }

    @Test
    public void testIsValidException() {
        Throwable exception = new RuntimeException("Test exception");
        Class<? extends Throwable> expectedExceptionType = RuntimeException.class;

        assertTrue(invocation.isValidException(exception));
        assertTrue(invocation.isValidException(expectedExceptionType));
        assertFalse(invocation.isValidException("Invalid class type"));
    }

    @Test
    public void testIsValidReturnType() {
        Class<String> expectedReturnType = String.class;

        assertTrue(invocation.isValidReturnType(expectedReturnType));
    }
}