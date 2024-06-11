import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;
    private InvocationOnMock invocationMock;

    @BeforeEach
    public void setup() {
        mockHandler = new MockHandler<>();
    }

    @Test
    public void testHandleMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Answer<Object> answer = Mockito.mock(Answer.class);

        when(mockHandler.getInvocationContainer()).thenReturn(mockHandler.invocationContainerImpl);
        when(mockHandler.matchersBinder.bindMatchers(Mockito.mock(ArgumentMatcherStorage.class), invocation)).thenReturn(new StubbedInvocationMatcher(answer));

        mockHandler.handle(invocation);
        verify(answer).answer(invocation);
    }
}