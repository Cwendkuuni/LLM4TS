import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class CallsRealMethodsTest {

    

    @Test
    public void testCallsRealMethodWhenInvokedWithValidArgument() {
        Object realObject = new Object() {
            public String methodToBeCalled() {
                return "Real method result";
            }
        };

        CallsRealMethods answer = Mockito.mock(CallsRealMethods.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setTarget(realObject);
        Mockito.when(invocation.getMethod()).thenReturn(realObject.getClass().getMethod("methodToBeCalled"));

        Object result = answer.answer(invocation);

        Mockito.verify(invocation).callRealMethod();
        Mockito.verifyNoMoreInteractions(answer);

        assertEquals("Real method result", result);
    }

    
}