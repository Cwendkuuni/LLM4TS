import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answer;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {

    @Mock
    private Object mock;

    private ReturnsDeepStubs returnsDeepStubs;

    @BeforeEach
    public void setup() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object answer = returnsDeepStubs.answer(invocation);
        Assertions.assertNotNull(answer);
    }

    @Test
    public void testAnswerWithMatchingInvocation() {
        Object mockResult = new Object();
        InternalMockHandler<Object> handler = Mockito.mock(InternalMockHandler.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);

        Mockito.when(handler.getMock()).thenReturn(mock);
        Mockito.when(handler.getInvocationContainer()).thenReturn(container);
        Mockito.when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{stubbedInvocationMatcher});
        Mockito.when(stubbedInvocationMatcher.answer(invocation)).thenReturn(mockResult);

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object answer = returnsDeepStubs.answer(invocation);
        Assertions.assertSame(mockResult, answer);
    }

    @Test
    public void testAnswerWithNoMatchingInvocation() {
        Object mockResult = new Object();
        InternalMockHandler<Object> handler = Mockito.mock(InternalMockHandler.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

        Mockito.when(handler.getMock()).thenReturn(mock);
        Mockito.when(handler.getInvocationContainer()).thenReturn(container);

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object answer = returnsDeepStubs.answer(invocation);
        Assertions.assertNotNull(answer);
        Assertions.assertNotSame(mock, answer);
    }
}