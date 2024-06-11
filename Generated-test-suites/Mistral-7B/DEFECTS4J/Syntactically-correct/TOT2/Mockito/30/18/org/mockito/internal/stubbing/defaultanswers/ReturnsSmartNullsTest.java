import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;

public class ReturnsSmartNullsTest {

    @Test
    public void testReturnsSmartNullsConstructor() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        assertNotNull(returnsSmartNulls);
    }

    @Test
    public void testAnswerMethodWithToStringReturnType(Answer<Object> answer) {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object result = new ReturnsSmartNulls().answer(invocation);
        assertEquals("SmartNull returned by unstubbed method on mock", result.toString());
    }

    @Test
    public void testAnswerMethodWithNoToStringReturnType() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object result = new ReturnsSmartNulls().answer(invocation);
        assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testAnswerMethodWithThrowableReturnType() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        new ReturnsSmartNulls().answer(invocation);
    }
}