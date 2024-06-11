import org.junit.Test;
import org.mockito.Answer;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerMethodWithNullArgument() {
        Answer<Object> returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String.class, returnsSmartNulls);

        String result = (String) mock.answer(new InvocationOnMock(null, new MethodData("toString"), new Object[0]));

        verify(mock).answer(new InvocationOnMock(null, new MethodData("toString"), new Object[0]));
        verify(mock).toString();

        assertNull(result);
    }

    @Test
    public void testAnswerMethodWithNonMockableReturnType() {
        Answer<Object> returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String.class, returnsSmartNulls);

        String result = (String) mock.answer(new InvocationOnMock(null, new MethodData("hashCode"), new Object[0]));

        assertNull(result);
    }

    @Test
    public void testAnswerMethodWithMockableReturnType() {
        Answer<Object> returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(String[].class, returnsSmartNulls);

        String[] result = (String[]) mock.answer(new InvocationOnMock(null, new MethodData("getClass"), new Object[0]));

        assertArrayEquals(new String[0], result);
    }
}