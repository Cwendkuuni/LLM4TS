import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

public class CallsRealMethodsTest {

    @Test
    public void testAnswerMethod() {
        CallsRealMethods instance = mock(CallsRealMethods.class);
        Object result = instance.answer(any(InvocationOnMock.class));
        then(instance).should(times(1)).callRealMethod();
        verify(instance).callRealMethod();
        assertEquals(result, instance.callRealMethod());
    }
}