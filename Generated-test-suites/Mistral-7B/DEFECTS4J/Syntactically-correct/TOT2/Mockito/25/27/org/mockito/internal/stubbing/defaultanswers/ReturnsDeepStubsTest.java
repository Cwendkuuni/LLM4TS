import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.Answer;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerMethod() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        when(mock.answer(invocation)).thenReturn("mocked_value");

        assertEquals("mocked_value", mock.answer(invocation));
        verify(mock).answer(invocation);
    }

    @Test
    public void testGetMockMethod() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        Object deepStub = ReturnsDeepStubs.getMock(invocation);

        verify(mock).answer(eq(invocation));
        assertEquals(mock, deepStub);
    }

    @Test
    public void testRecordDeepStubMockMethod() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        when(mock.answer(invocation)).thenCallRealMethod();
        when(container.getInvocationForStubbing()).thenReturn(invocation);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object deepStub = returnsDeepStubs.recordDeepStubMock(invocation, container);

        verify(mock).answer(invocation);
        verify(mock).answer(eq(invocation));
        assertEquals(mock, deepStub);
    }
}