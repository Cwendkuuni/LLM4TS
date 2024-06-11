import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;
    private Object mock;

    @Before
    public void setup() {
        returnsDeepStubs = new ReturnsDeepStubs();
        mock = mock(Object.class);
    }

    @Test
    public void testAnswer() throws Exception {
        when(mock.method()).thenAnswer(returnsDeepStubs);
        Object result = mock.method();
        verify(mock).method();
        assertSame(mock, result);
    }
}