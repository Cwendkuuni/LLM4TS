import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerMethod() {
        ReturnsDeepStubs returnsDeepStubs = Mockito.mock(ReturnsDeepStubs.class);
        Object mock = Mockito.mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        when(returnsDeepStubs.answer(ArgumentMatchers.eq(invocation))).thenReturn(mock);

        Object returnedMock = returnsDeepStubs.answer(invocation);

        assertSame(mock, returnedMock);
    }
}