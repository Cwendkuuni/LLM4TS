import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.InvocationVerificationMode;
import org.mockito.Mockito;
import org.mockito.internal.MockitoInternalVerifications;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;
    private InvocationContainer invocationContainer;
    private Invocation invocation;
    private Answer answer;
    private ThreadSafeMockingProgress mockingProgress;

    @Before
    public void setup() {
        mockHandler = new MockHandler<>();
        invocationContainer = mock(InvocationContainer.class);
        invocation = mock(InvocationOnMock.class);
        answer = mock(Answer.class);
        mockingProgress = mock(ThreadSafeMockingProgress.class);
        when(mockHandler.mockingProgress).thenReturn(mockingProgress);
    }

    @Test
    public void testHandle() throws Throwable {
        when(mockHandler.handle(ArgumentMatchers.any(Invocation.class))).thenReturn(answer);
        assertEquals(answer, mockHandler.handle(invocation));

        verify(answer).answer(invocation);
    }

    @Test
    public void testMockHandlerConstructor() {
        // Already tested in the basic JUnit 4 test case
    }

    @Test
    public void testMockHandlerWithOldMockHandler() {
        MockHandlerInterface<Object> oldMockHandler = mock(MockHandlerInterface.class);
        MockHandler<Object> mockHandler = new MockHandler<Object>(oldMockHandler);

        assertEquals(oldMockHandler, mockHandler.getMockSettings());
    }

    @Test
    public void testVoidMethodStubbable() {
        T mock = mock(Object.class);
        VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);
        assertNull(voidMethodStubbable);
    }

    @Test
    public void testGetMockSettings() {
        assertNull(mockHandler.getMockSettings());
    }

    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = mock(List.class);
        mockHandler.setAnswersForStubbing(answers);
        assertEquals(answers, mockHandler.getInvocationContainer().getAnswersForStubbing());
    }

    @Test
    public void testGetInvocationContainer() {
        assertNotNull(mockHandler.getInvocationContainer());
    }

    @Test
    public void testHandleWithNullInvocation() throws Throwable {
        assertThrows(NullPointerException.class, () -> mockHandler.handle(null));
    }

    @Test
    public void testHandleWithException() throws Throwable {
        Mockito.doThrow(new RuntimeException("Test Exception"))
                .when(mockHandler).handle(ArgumentMatchers.any(Invocation.class));
        assertThrows(RuntimeException.class, () -> mockHandler.handle(invocation));
    }

    @Test
    public void testSetAnswersForStubbingWithEmptyList() {
        assertNull(mockHandler.setAnswersForStubbing(Collections.emptyList()));
    }

    @Test
    public void testGetArgumentMatcherStorageWithNullKey() {
        mockingProgress.getArgumentMatcherStorage().put(null, invocationMatcher);
        Mockito.verify(mockHandler.mockingProgress, Mockito.times(1))
                .putArgumentMatcherStorage(ArgumentMatchers.isNull(), ArgumentMatchers.any(InvocationMatcher.class));
    }

    @Test
    public void testPullVerificationModeWithNullVerificationMode() {
        Mockito.verify(mockHandler.mockingProgress, Mockito.times(1))
                .pullVerificationMode();
        assertNull(mockHandler.mockingProgress.pullVerificationMode());
    }

    @Test
    public void testHandleWithNoAnswers() throws Throwable {
        when(mockHandler.getInvocationContainer().getAnswersForStubbing()).thenReturn(Collections.emptyList());
        assertNull(mockHandler.handle(invocation));

        verifyNoMoreInteractions(mockHandler.invocationContainerImpl);
    }

    @Test
    public void testFindAnswerForWithNullInvocationMatcher() {
        assertNull(mockHandler.invocationContainerImpl.findAnswerFor(null));
        Mockito.verify(mockHandler.invocationContainerImpl, Mockito.times(1))
                .findAnswerFor(ArgumentMatchers.isNull());
    }

    @Test
    public void testHandleWithVerificationMode() throws Throwable {
        Invocation invocation = mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        InvocationContainer invocationContainer = mock(InvocationContainer.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        VerificationMode verificationMode = mock(VerificationMode.class);

        when(mockHandler.handle(invocation)).thenReturn(stubbedInvocation);
        when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(mockHandler.matchersBinder.bindMatchers(ArgumentMatchers.anyMap(), invocation))
                .thenReturn(invocationMatcher);
        when(mockHandler.invocationContainerImpl.findAnswerFor(invocationMatcher)).thenReturn(stubbedInvocation);

        InvocationVerificationMode verificationData = new InvocationVerificationMode();
        verificationData.setInvocation(invocation);
        verificationData.setInvocationMatcher(invocationMatcher);

        verificationMode.verify(verificationData);
        assertEquals(verificationData, stubbedInvocation.getData());
    }
}

//