import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.RunWith;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CallsRealMethodsTest {

    @Test
    public void testAnswerMethod() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = callRealMethods.answer(invocation);
        verify(invocation).callRealMethod();
        assertEquals(result, invocation.getMock().getInvocationTarget());
    }
}