import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.JUnit4;

@RunWith(JUnit4.class)
public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerMethodWithEmptyInvocation() {
        Object mock = Mockito.mock(Object.class, Mockito.withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        ReturnsDeepStubs returnsDeepStubs = Mockito.spy(new ReturnsDeepStubs());
        Object result = returnsDeepStubs.answer(invocation);

        Mockito.verify(returnsDeepStubs).delegate().returnValueFor(invocation.getMethod().getReturnType());
        Mockito.verify(mock).answer(invocation);

        Mockito.verifyNoMoreInteractions(mock, returnsDeepStubs);
        Mockito.verifyNoMoreInteractions(invocation);

        Mockito.assertEquals(mock, result);
    }

    @Test
    public void testAnswerMethodWithValidInvocation() {
        Object mock = Mockito.mock(String.class, Mockito.withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
        String methodName = "testMethod";
        Class<String> stringClass = String.class;
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Mockito.method(methodName, stringClass));

        ReturnsDeepStubs returnsDeepStubs = Mockito.spy(new ReturnsDeepStubs());
        Object result = returnsDeepStubs.answer(invocation);

        Mockito.verify(mock).answer(invocation);
        Mockito.verifyNoMoreInteractions(mock, returnsDeepStubs, invocation);

        Mockito.assertEquals(mock, result);
    }

    @Test(expected = Throwable.class)
    public void testAnswerMethodWithNonMockableType() {
        Object mock = Mockito.mock(IOException.class, Mockito.withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        ReturnsDeepStubs returnsDeepStubs = Mockito.spy(new ReturnsDeepStubs());
        returnsDeepStubs.delegate().returnValueFor(IOException.class);

        try {
            Object result = returnsDeepStubs.answer(invocation);
            Mockito.fail("Expected exception: IOException");
        } catch (IOException e) {
            // expected exception
        }

        Mockito.verifyNoMoreInteractions(mock, returnsDeepStubs, invocation);
    }

    @Test
    public void testDeepStubMethodWithMockMatch() {
        Object mockA = Mockito.mock(Object.class, Mockito.withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
        Object mockB = Mockito.mock(Object.class, Mockito.withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));

        ReturnsDeepStubs returnsDeepStubs = Mockito.spy(new ReturnsDeepStubs());
        InternalMockHandler<Object> handlerA = new MockUtil().getMockHandler(mockA);
        handlerA.getInvocationContainer().addAnswer(new DeeplyStubbedAnswer(mockB), false);

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMock()).thenReturn(mockA);

        Object result = returnsDeepStubs.answer(invocation);

        Mockito.verify(mockB).answer(invocation);
        Mockito.verifyNoMoreInteractions(mockA, mockB, returnsDeepStubs, invocation);

        Mockito.assertEquals(mockB, result);
    }

    @Test
    public void testDeepStubMethodWithNoMatch() {
        Object mockA = Mockito.mock(Object.class, Mockito.withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
        Object mockB = Mockito.mock(Object.class, Mockito.withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));

        ReturnsDeepStubs returnsDeepStubs = Mockito.spy(new ReturnsDeepStubs());
        InternalMockHandler<Object> handlerB = new MockUtil().getMockHandler(mockB);
        handlerB.getInvocationContainer().addAnswer(new DeeplyStubbedAnswer(mockA), false);

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMock()).thenReturn(mockA);

        Object result = returnsDeepStubs.answer(invocation);

        Mockito.verifyNoMoreInteractions(mockA, mockB, returnsDeepStubs, invocation);

        Mockito.assertEquals(mockA, result);
    }
}