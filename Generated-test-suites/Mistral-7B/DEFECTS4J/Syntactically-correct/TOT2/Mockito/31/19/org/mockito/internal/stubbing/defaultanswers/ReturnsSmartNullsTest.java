import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.verify;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls;

    @Before
    public void setUp() {
        returnsSmartNulls = new ReturnsSmartNulls();
    }

    @Test
    public void testAnswer() {
        Answer<Object> answer = Mockito.mock(Answer.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object result = returnsSmartNulls.answer(invocation);
        verify(answer).answer(invocation);
        Mockito.verifyNoMoreInteractions(answer);
        assertNull(result);
    }
}