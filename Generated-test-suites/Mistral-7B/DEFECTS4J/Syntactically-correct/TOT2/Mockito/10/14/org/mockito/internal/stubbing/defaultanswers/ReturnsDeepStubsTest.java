import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerMethod() {
        Object mockString = Mockito.mock(String.class, Answers.RETURNS_DEEP_STUBS);
        Object mockList = Mockito.mock(List.class, Answers.RETURNS_DEEP_STUBS);
        Object mockMap = Mockito.mock(Map.class, Answers.RETURNS_DEEP_STUBS);

        verify(mockString).toString(); //Verifies that a deep stub is returned for String
        verify(mockList).add(1); //Verifies that a deep stub is returned for List
        verify(mockMap).put(1, 1); //Verifies that a deep stub is returned for Map
    }

    @Test(expected = Throwable.class)
    public void testAnswerMethodWithException() {
        Object mockException = Mockito.mock(Exception.class, Answers.RETURNS_DEEP_STUBS);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.getMethod().invoke(mockException);
    }

    @Test
    public void testAnswerMethodNullInvocation() {
        Object mock = Mockito.mock(String.class, Answers.RETURNS_DEEP_STUBS);
        InvocationOnMock invocation = null;
        mock.answer(invocation); //Should not throw an exception
    }
}