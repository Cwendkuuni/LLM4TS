import org.junit.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.stubbing.answers.ReturnsDeepStubs;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() {
        Object mock = Mockito.mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(mock.answer(invocation)).thenAnswer(new ReturnsDeepStubs());

        Object result = mock.answer(invocation);
        Mockito.verify(mock).answer(invocation);
        Mockito.verifyNoMoreInteractions(mock);
        Mockito.assertSame(mock, result);
    }
}