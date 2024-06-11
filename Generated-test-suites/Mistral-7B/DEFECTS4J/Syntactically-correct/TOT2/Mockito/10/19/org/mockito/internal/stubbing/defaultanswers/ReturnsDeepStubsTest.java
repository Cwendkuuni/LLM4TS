import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;
import static org.mockito.internal.MockitoCore.mockitoCore;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerMethod() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        Object result = mockitoCore().mock(Object.class).answer(new InvocationOnMock(mock, new MethodTestDouble()));
        verify(mock).answer(Mockito.isA(InvocationOnMock.class));
        assertSame(mock, result);
    }

    @Test(expected = RuntimeException.class)
    public void testAnswerMethodWithNonMockableType() {
        Object mock = mock(IOException.class, new ReturnsDeepStubs());
        mockitoCore().mock(String.class).answer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "Hello World";
            }
        });
        try {
            mock.answer(new InvocationOnMock(mockitoCore().mock(String.class), new MethodTestDouble()));
            fail();
        } catch (Throwable e) {
            // expected
        }
    }

    @Test
    public void testDeepStubbing() {
        Object parentMock = mock(Object.class, new ReturnsDeepStubs());
        Object deepStub = mockitoCore().mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = new InvocationOnMock(parentMock, new MethodTestDouble());
        Object result = deepStub.answer(invocation);
        verify(deepStub).answer(Mockito.isA(InvocationOnMock.class));
        assertSame(deepStub, result);
    }

    @Test
    public void testDeepStubbingWithVerification() {
        Object parentMock = mock(Object.class, new ReturnsDeepStubs());
        Object deepStub = mockitoCore().mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = new InvocationOnMock(parentMock, new MethodTestDouble());
        Object result = deepStub.answer(invocation);
        verify(deepStub).answer(Mockito.isA(InvocationOnMock.class));
        assertSame(deepStub, result);
        verify(parentMock).answer(Mockito.isA(InvocationOnMock.class));
    }
}