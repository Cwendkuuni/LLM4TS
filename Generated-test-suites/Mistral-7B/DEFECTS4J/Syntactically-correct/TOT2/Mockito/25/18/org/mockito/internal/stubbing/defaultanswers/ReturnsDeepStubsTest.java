import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import java.lang.reflect.Type;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        InvocationOnMock invocation = new InvocationOnMock(mock, Mockito.method(Object.class), new Object[0]);

        when(returnsDeepStubs.answer(invocation)).thenReturn("Test Result");

        assertEquals("Test Result", returnsDeepStubs.answer(invocation));
        verify(mock).answer(ArgumentMatchers.any(InvocationOnMock.class));
    }

    @Test
    public void testGetMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        InvocationOnMock invocation = new InvocationOnMock(mock, Mockito.method(Object.class), new Object[0]);

        Object result = returnsDeepStubs.getMock(invocation);

        assertEquals(mock, result);
        verify(mock).answer(ArgumentMatchers.any(InvocationOnMock.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNonMockableType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        InvocationOnMock invocation = new InvocationOnMock(mock, Mockito.method(String.class), new Object[0]);

        when(returnsDeepStubs.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn(mock);

        assertThrows(UnsupportedOperationException.class, () -> returnsDeepStubs.answer(invocation));
    }

    @Test
    public void testMultipleMatches() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        InvocationOnMock invocation = new InvocationOnMock(mock1, Mockito.method(Object.class), new Object[0]);

        when(returnsDeepStubs.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn(mock1).thenReturn(mock2);

        assertNotSame(mock1, returnsDeepStubs.answer(invocation));
        assertNotSame(mock2, returnsDeepStubs.answer(invocation));
    }

    @Test
    public void testNoMatches() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        InvocationOnMock invocation = new InvocationOnMock(mock, Mockito.method(Object.class), new Object[0]);

        when(returnsDeepStubs.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn(mock);

        assertEquals(mock, returnsDeepStubs.answer(invocation));
        verify(mock).answer(ArgumentMatchers.any(InvocationOnMock.class));

        Object otherMock = mock(Object.class);
        InvocationOnMock otherInvocation = new InvocationOnMock(otherMock, Mockito.method(Object.class), new Object[0]);

        assertNotSame(otherMock, returnsDeepStubs.answer(otherInvocation));
    }
}