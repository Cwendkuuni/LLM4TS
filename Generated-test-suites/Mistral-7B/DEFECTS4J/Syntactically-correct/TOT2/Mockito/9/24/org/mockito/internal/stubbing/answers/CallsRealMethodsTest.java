import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class CallsRealMethodsTest {

    @Test
    public void test_CallsRealMethods_answerMethod() {
        CallsRealMethods callRealMethods = mock(CallsRealMethods.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(callRealMethods.answer(invocation)).thenReturn("Test Value");

        assertEquals("Test Value", callRealMethods.answer(invocation));
        verify(callRealMethods).callRealMethod();
    }

    @Test(expected = NullPointerException.class)
    public void test_CallsRealMethods_answerMethod_NullInvocation() {
        CallsRealMethods callRealMethods = mock(CallsRealMethods.class);
        InvocationOnMock invocation = null;

        callRealMethods.answer(invocation);
    }

    @Test
    public void test_CallsRealMethods_answerMethod_ExceptionFromRealMethod() {
        CallsRealMethods callRealMethods = mock(CallsRealMethods.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(callRealMethods.answer(invocation)).thenAnswer(new ThrowingAnswer<Object>(new RuntimeException("Test Exception")));

        try {
            callRealMethods.answer(invocation);
            fail("Should have thrown an exception");
        } catch (RuntimeException e) {
            // Expected
        }
    }

    @Test
    public void test_CallsRealMethods_Constructor() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        assertNotNull(callRealMethods);
    }

    @Test
    public void test_CallsRealMethods_getSerialVersionUID() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        assertEquals(9057165148930624087L, callRealMethods.getSerialVersionUID());
    }
}