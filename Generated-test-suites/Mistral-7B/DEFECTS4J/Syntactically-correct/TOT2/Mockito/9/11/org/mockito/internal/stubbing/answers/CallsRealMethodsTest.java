import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.RunWith;
import org.mockito.stubbing.Answer;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class CallsRealMethodsTest {

    @Test
    public void testAnswerMethod() throws Throwable {
        CallsRealMethods callsRealMethods = Mockito.spy(new CallsRealMethods());
        InvocationOnMock invocation = Mockito.mock(Object.class);
        Object result = callsRealMethods.answer(invocation);
        verify(invocation).callRealMethod();
        assertNotNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInvocation() throws Throwable {
        CallsRealMethods callsRealMethods = Mockito.spy(new CallsRealMethods());
        InvocationOnMock nullInvocation = null;
        callsRealMethods.answer(nullInvocation);
    }

    @Test(expected = Throwable.class)
    public void testThrowingException() throws Throwable {
        CallsRealMethods callsRealMethods = Mockito.spy(new CallsRealMethods());
        InvocationOnMock invocation = Mockito.mock(Object.class);
        doThrow(new RuntimeException("Test Exception")).when(invocation).callRealMethod();
        callsRealMethods.answer(invocation);
    }
}