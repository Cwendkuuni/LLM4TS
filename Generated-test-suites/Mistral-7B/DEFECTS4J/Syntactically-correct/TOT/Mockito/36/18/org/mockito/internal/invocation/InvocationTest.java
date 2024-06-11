import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class InvocationTest {

    private Invocation mockInvocation;
    private Stubber stubber;

    @Before
    public void setup() {
        stubber = mockitoErrorThrowing(Invocation.class);
        mockInvocation = new Invocation(mock(Object.class), mockMethod(), new Object[]{"arg1", "arg2"}, 1, new RealMethod(){
            @Override
            public Object invoke(Object mock, Object[] arguments) throws Throwable {
                throw new RuntimeException("Expected exception");
            }
        });
    }

    @Test
    public void testGetMock() {
        assertEquals(mock(Object.class), mockInvocation.getMock());
    }

    @Test
    public void testGetMethod() {
        assertEquals(mockMethod(), mockInvocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        Object[] expectedArguments = new Object[]{"arg1", "arg2"};
        assertEquals(expectedArguments, mockInvocation.getArguments());
        verify(mockInvocation).getArguments();
    }

    @Test(expected = RuntimeException.class)
    public void testInvokeRealMethodThrowsException() throws Throwable {
        stubber.doThrow(new RuntimeException("Expected exception")).when(mockInvocation).callRealMethod();
        mockInvocation.callRealMethod();
    }

    @Test
    public void testIsVerified() {
        mockInvocation.markVerified();
        assertTrue(mockInvocation.isVerified());
    }

    @Test
    public void testIsVerifiedInOrder() {
        mockInvocation.markVerifiedInOrder();
        assertTrue(mockInvocation.isVerifiedInOrder());
    }

    @Test
    public void testIsToString() {
        Invocation mockInvocationToString = mockitoOf(Invocation.class).when(invocation -> invocation.isToString(any()))
                .thenReturn(true).getMock();
        assertTrue(Invocation.isToString(mockInvocationToString));
    }

    @Test
    public void testIsValidException() {
        Class<?> exceptionClass = RuntimeException.class;
        assertTrue(mockInvocation.isValidException(new RuntimeException("Expected exception", exceptionClass)));
        assertFalse(mockInvocation.isValidException(new IllegalStateException("Unexpected state")));
    }

    @Test
    public void testIsValidReturnType() {
        Class<?> expectedReturnType = Integer.class;
        assertTrue(mockInvocation.isValidReturnType(expectedReturnType));
        assertFalse(mockInvocation.isValidReturnType(String.class));
    }

    @Test
    public void testIsVoid() {
        Method voidMethod = () -> {};
        Invocation voidInvocation = new Invocation(mock(Object.class), mockitoMethod(voidMethod), new Object[0], 1, new RealMethod(){});
        assertTrue(voidInvocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        assertEquals("Integer", mockInvocation.printMethodReturnType());
    }

    @Test
    public void testGetLocation() {
        assertEquals("InvocationTest.java:27", mockInvocation.getLocation().toString());
    }

    @Test
    public void testGetArgumentsCount() {
        assertEquals(2, mockInvocation.getArgumentsCount());
    }

    @Test
    public void testCallRealMethodReturnsValue() throws Throwable {
        Object expectedValue = "expectedValue";
        Answer<Object> answer = invocation -> expectedValue;
        stubber.when(mockInvocation).callRealMethod().thenAnswer(answer);
        assertEquals(expectedValue, mockInvocation.callRealMethod());
    }

    private MockitoMethod mockMethod() {
        return mockitoMethod(new MethodWithArguments(Object.class, "methodName", new Class[0], new Class[]{Integer.class}) {
            @Override
            public Object invoke(Object receiver, Object[] args) throws Throwable {
                Object arg1 = args[0];
                Object arg2 = args[1];
                return (int) arg1 + (int) arg2;
            }
        });
    }
}