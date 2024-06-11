import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class MockHandlerTest {
    MockHandler<SomeMockType> mockHandler;
    SomeMockType mock;

    @Before
    public void setUp() {
        mock = Mockito.mock(SomeMockType.class);
        mockHandler = new MockHandler<>(mock);
    }

    @Test
    public void testHandleInvocationWithStubbing() {
        // ...
    }

    @Test(expected = NullPointerException.class)
    public void testHandleNullInvocation() {
        mockHandler.handle(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testHandleUnsupportedMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(new SomeUnsupportedMethodType().getClass().getMethod("someMethod", int[].class));

        mockHandler.handle(invocation);
    }

    @Test(expected = IllegalStateException.class)
    public void testHandleInconsistentState() {
        Answer<Integer> answer = Mockito.answer(invocation -> { throw new IllegalStateException(); });
        mockHandler.setAnswersForStubbing(answers -> answers.add(answer));

        mockHandler.handle(Mockito.argThat(ArgumentMatchers.isInstance(mock)));
    }

    @Test
    public void testHandleInvocationWithoutStubbing() {
        VerificationMode verificationMode = Mockito.verifyNoMoreInteractions(mock);

        mockHandler.handle(Mockito.argThat(ArgumentMatchers.isInstance(mock)));

        Mockito.verify(mock).handle(Mockito.argThat(ArgumentMatchers.isInstance(mock)));

        Assert.assertNotNull(verificationMode);
    }

    @Test
    public void testHandleInvocationWithNoAnswer() {
        mockHandler.setAnswersForStubbing(Collections.emptyList());

        mockHandler.handle(Mockito.argThat(ArgumentMatchers.isInstance(mock)));

        Mockito.verify(mock).handle(Mockito.argThat(ArgumentMatchers.isInstance(mock)));

        Assert.assertNull(mockHandler.getInvocationContainer().getInvocationForPotentialStubbing());
    }

    @Test
    public void testHandleInvocationWithMultipleAnswers() {
        Answer<Integer> answer1 = Mockito.answer(invocation -> 1);
        Answer<Integer> answer2 = Mockito.answer(invocation -> 2);

        mockHandler.setAnswersForStubbing(answers -> answers.addAll(Arrays.asList(answer1, answer2)));

        int actual = mockHandler.handle(Mockito.argThat(ArgumentMatchers.isInstance(mock)));

        Assert.assertEquals(1, actual);

        Mockito.verify(mock).answer(Mockito.eq(ArgumentMatchers.isInstance(invocation)), Mockito.eq(answer1));
        Mockito.verify(mock).answer(Mockito.eq(ArgumentMatchers.isInstance(invocation)), Mockito.eq(answer2));

        Assert.assertNull(mockHandler.getInvocationContainer().getInvocationForPotentialStubbing());
    }

    // ...
}