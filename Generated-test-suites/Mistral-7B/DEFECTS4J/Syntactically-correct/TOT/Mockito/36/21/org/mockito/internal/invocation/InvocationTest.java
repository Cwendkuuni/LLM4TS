import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class InvocationTest {

    private Invocation mockInvocation;

    @Before
    public void setUp() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockMethod = Mockito.method(String.class, "length");
        Object[] arguments = new Object[]{null};
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);

        mockInvocation = new Invocation(mock, mockMethod, arguments, sequenceNumber, realMethod);
    }

    @Test
    public void testGetMock() {
        assertThat(mockInvocation.getMock(), is(sameInstance(mock)));
    }

    @Test
    public void testGetMethod() {
        assertThat(mockInvocation.getMethod(), is(sameInstance(mockMethod)));
    }

    @Test
    public void testGetArguments() {
        Object[] expectedArguments = new Object[]{"Test"};
        given(mock.length()).willReturn(expectedArguments.length);
        when(mockInvocation.getArguments()).thenReturn(expectedArguments);

        Object[] actualArguments = mockInvocation.getArguments();

        assertThat(actualArguments, is(sameInstance(expectedArguments)));
    }

    @Test
    public void testIsVerified() {
        mockInvocation.markVerified();

        assertTrue(mockInvocation.isVerified());
    }

    @Test
    public void testIsVerifiedInOrder() {
        mockInvocation.markVerifiedInOrder();

        assertTrue(mockInvocation.isVerified());
        assertTrue(mockInvocation.isVerifiedInOrder());
    }

    @Test
    public void testToString() {
        String expectedToString = "mock.length([])";
        given(mockInvocation.toString(new PrintSettings())).willReturn(expectedToString);

        String actualToString = mockInvocation.toString();

        assertEquals(expectedToString, actualToString);
    }

    @Test(expected = RuntimeException.class)
    public void testHashCode() {
        mockInvocation.hashCode();
    }

    @Test
    public void testIsToString() {
        Invocation invocationMock = mock(InvocationOnMock.class);
        given(invocationMock.getMethod()).willReturn(Mockito.method(String.class, "toString"));

        assertTrue(Invocation.isToString(invocationMock));
    }

    @Test
    public void testIsValidException() {
        MockitoMethod mockMethod = Mockito.method(String.class, "length", new Class[0]);
        mockMethod.addException(RuntimeException.class);

        Invocation invocation = new Invocation(Mockito.mock(String.class), mockMethod, new Object[0], 1, null);

        Throwable throwable = new RuntimeException("Test Exception");

        assertFalse(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidReturnType() {
        Invocation invocation = new Invocation(Mockito.mock(String.class), Mockito.method(String.class, "length"), new Object[0], 1, null);

        assertTrue(invocation.isValidReturnType(int.class));
        assertTrue(invocation.isValidReturnType(Integer.class));
        assertFalse(invocation.isValidReturnType(String.class));
    }

    @Test
    public void testIsVoid() {
        Invocation invocation = new Invocation(Mockito.mock(String.class), Mockito.method(void.class, "length", new Class[0]), new Object[0], 1, null);

        assertTrue(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Invocation invocation = new Invocation(Mockito.mock(String.class), Mockito.method(String.class, "length"), new Object[0], 1, null);

        assertEquals("int", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        Invocation invocation = new Invocation(Mockito.mock(String.class), Mockito.method(String.class, "length"), new Object[0], 1, null);

        assertEquals("length", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        Invocation invocation = new Invocation(Mockito.mock(String.class), Mockito.method(boolean.class, "length"), new Object[0], 1, null);

        assertFalse(invocation.returnsPrimitive());

        invocation = new Invocation(Mockito.mock(byte.class), Mockito.method(byte.class, "length"), new Object[0], 1, null);

        assertTrue(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(Mockito.mock(String.class), Mockito.method(String.class, "length"), new Object[0], 1, null);

        Location expectedLocation = new Location();
        given(invocation.getLocation()).willReturn(expectedLocation);

        Location actualLocation = mockInvocation.getLocation();

        assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void testGetArgumentsCount() {
        Invocation invocation = new Invocation(Mockito.mock(String.class), Mockito.method(String.class, "length"), new Object[0], 1, null);

        assertEquals(0, invocation.getArgumentsCount());

        Object[] arguments = new Object[]{"Test"};
        invocation = new Invocation(Mockito.mock(String.class), Mockito.method(String.class, "concat", String[].class), arguments, 1, null);

        assertEquals(1, invocation.getArgumentsCount());
    }

    @Test
    public void testCallRealMethod() {
        String mockString = Mockito.mock(String.class);
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        given(realMethod.invoke(mockString, new Object[0])).willAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) {
                Object[] arguments = invocationOnMock.getArguments();
                assertEquals("Test", arguments[0]);

                return "Test";
            }
        });

        Invocation invocation = new Invocation(mockString, Mockito.method(String.class, "length"), new Object[0], 1, realMethod);

        assertEquals("Test", invocation.callRealMethod());
    }
}