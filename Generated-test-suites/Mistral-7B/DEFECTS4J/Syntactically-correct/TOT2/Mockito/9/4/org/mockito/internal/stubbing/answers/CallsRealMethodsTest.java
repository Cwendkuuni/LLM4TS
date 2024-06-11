import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CallsRealMethodsTest {

    @Test
    public void testAnswerMethod() throws Throwable {
        CallsRealMethods answer = new CallsRealMethods();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object realMethodReturnValue = new Object();

        when(invocation.callRealMethod()).thenReturn(realMethodReturnValue);

        Object result = answer.answer(invocation);

        verify(invocation).callRealMethod();
        assertEquals(realMethodReturnValue, result);
    }
}