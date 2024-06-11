import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {
    @Test
    public void testAnswer() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.spy(new ReturnsSmartNulls());
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object result = returnsSmartNulls.answer(invocation);
        Mockito.verify(returnsSmartNulls).answer(invocation);
        Mockito.verifyNoMoreInteractions(returnsSmartNulls);
        assertNull(result);
    }
}