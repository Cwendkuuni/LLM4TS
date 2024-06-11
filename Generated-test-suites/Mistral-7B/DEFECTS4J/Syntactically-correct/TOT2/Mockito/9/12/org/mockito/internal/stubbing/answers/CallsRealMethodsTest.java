import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class CallsRealMethodsTest {
    @Test
    public void testAnswerMethod() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        Object mockedObject = Mockito.mock(Object.class);
        InvocationOnMock invocation = new InvocationOnMock(mockedObject, new Class[]{}, new Object[0]);
        when(mockedObject.someMethod()).thenAnswer(callRealMethods);
        Object result = mockedObject.someMethod();
        assertEquals(invocation.callRealMethod(), result);
    }

    // Test for null InvocationOnMock
    @Test(expected = NullPointerException.class)
    public void testNullInvocationOnMock() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        InvocationOnMock invocation = null;
        Object result = callRealMethods.answer(invocation);
    }

    // Test for null Invoked Method
    @Test(expected = NullPointerException.class)
    public void testNullInvokedMethod() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        Object mockedObject = Mockito.mock(Object.class);
        InvocationOnMock invocation = new InvocationOnMock(mockedObject, new Class[]{}, new Object[0]);
        mockedObject = null;
        when(mockedObject.someMethod()).thenAnswer(callRealMethods);
        Object result = callRealMethods.answer(invocation);
    }

    // Test for Throwable Exception
    @Test
    public void testThrowableException() throws Exception {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        Object mockedObject = Mockito.mock(Object.class);
        when(mockedObject.someMethod()).thenThrow(new Exception());
        InvocationOnMock invocation = new InvocationOnMock(mockedObject, new Class[]{}, new Object[0]);
        when(mockedObject.someMethod()).thenAnswer(callRealMethods);
        Object result = mockedObject.someMethod();
        Throwable thrownException = invocation.getThrown();
        assertEquals(thrownException, result);
    }
}