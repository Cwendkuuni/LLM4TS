import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

class ReturnsDeepStubsTest {

    @Test
    void testAnswer() {
        Answer<Object> answer = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(Object.class);

        Object result = answer.answer(invocation);
        assertNotNull(result);
    }
}