import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.mock(ReturnsSmartNulls.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        Mockito.when(returnsSmartNulls.answer(ArgumentMatchers.eq(invocation))).thenCallRealMethod();

        Object result = returnsSmartNulls.answer(invocation);

        Mockito.verify(returnsSmartNulls).delegate.answer(invocation);
        verify(returnsSmartNulls).answer(invocation); // verify the call to the real method
    }
}