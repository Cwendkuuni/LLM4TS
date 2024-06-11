import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doCallRealMethod;

public class CallsRealMethodsTest {

    @Test
    public void testAnswerMethod() {
        CallsRealMethods answer = new CallsRealMethods();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object expectedResult = new Object();

        doCallRealMethod().when(invocation).callRealMethod(); // stub invocation.callRealMethod() to do nothing
        Object result = answer.answer(invocation);
        verify(invocation).callRealMethod(); // check that callRealMethod() was called
        assertSame(expectedResult, result); // check that the expected result is returned
    }
}