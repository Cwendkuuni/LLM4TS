import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.Answers;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;
    private Object mock;

    @Before
    public void setup() {
        returnsDeepStubs = new ReturnsDeepStubs();
        mock = Mockito.mock(Object.class);
    }

    @Test
    public void testAnswerMethodWithMatchingInvocation() {
        when(mock.someMethod()).thenReturn(42);
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        Object result = deepStubs.answer(ArgumentMatchers.invocationWith(mock, "someMethod"));
        assertEquals(42, result);
    }

    @Test
    public void testAnswerMethodWithNonMatchingInvocation() {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        Object result = deepStubs.answer(ArgumentMatchers.any(Object.class));
        assertNull(result);
    }
}