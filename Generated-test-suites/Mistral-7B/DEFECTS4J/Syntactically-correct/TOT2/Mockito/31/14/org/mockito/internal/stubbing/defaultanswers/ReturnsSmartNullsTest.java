import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerWithNullInput() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class, returnsSmartNulls);
        Object result = mock.answer(null);
        assertNull(result);
        verify(mock).answer(null);
    }

    @Test
    public void testAnswerWithNonMockableType() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class, returnsSmartNulls);
        Class<?> nonMockableType = int.class;
        Object result = mock.answer(new Object() {});
        assertNull(result);
        verify(mock).answer(new Object() {});
    }

    @Test
    public void testAnswerWithMockableType() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        Object mock = mock(Object.class, returnsSmartNulls);
        Class<?> mockableType = String.class;
        String result = (String) mock.answer(new Object() {});
        assertNotNull(result);
        assertEquals("SmartNull returned by unstubbed method on mock", result);
    }
}