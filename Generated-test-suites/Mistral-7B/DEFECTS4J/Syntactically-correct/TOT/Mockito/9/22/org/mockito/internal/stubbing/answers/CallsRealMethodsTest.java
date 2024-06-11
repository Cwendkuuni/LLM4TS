import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CallsRealMethodsTest {
    
    private Object realObject;
    private CallsRealMethods callRealMethods;

    @Before
    public void setup() {
        realObject = mock(Object.class);
        callRealMethods = new CallsRealMethods();
    }

    @Test
    public void testCallRealMethod() {
        ArgumentCaptor<InvocationOnMock> invocationCaptor = ArgumentCaptor.forClass(InvocationOnMock.class);
        callRealMethods.answer(invocationCaptor.capture(invocation));

        Object result = callRealMethods.answer(Mockito.mock(InvocationOnMock.class));
        verify(realObject).callRealMethod();
        Assert.assertSame(result, invocationCaptor.getValue().getMock().callRealMethod());
    }
    
}