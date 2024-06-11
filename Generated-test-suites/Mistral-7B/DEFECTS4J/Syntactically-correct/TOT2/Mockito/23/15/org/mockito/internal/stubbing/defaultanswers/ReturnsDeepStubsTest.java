import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {
    @Test
    public void testAnswer() throws Exception {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object returnedValue = new ReturnsDeepStubs().answer(invocation);
        verify(mock).answers(new ReturnsDeepStubs());
        assertEquals(mock, returnedValue);
    }
}