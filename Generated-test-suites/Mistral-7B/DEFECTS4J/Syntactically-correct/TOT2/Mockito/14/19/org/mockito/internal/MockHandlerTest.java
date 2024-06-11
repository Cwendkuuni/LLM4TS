import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.AnswerWithinvocationOnMock;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;
    private static final Object testObject = new Object();

    @Before
    public void setup() {
        mockHandler = new MockHandler<>();
    }

    @Test
    public void testHandleMethodWithProperInvocation() throws Throwable {
        Invocation invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = answer(testObject);
        mockHandler.setAnswersForStubbing(answer);

        Object result = mockHandler.handle(invocation);
        verify(mockHandler).setInvocationForPotentialStubbing(eq(invocation));
        verify(mockHandler).reportOngoingStubbing(isInstanceOf(OngoingStubbingImpl.class));

        Mockito.verifyNoMoreInteractions(mockHandler);
        Mockito.verify(invocation).getMethod();

        Mockito.verify(answer).answer(eq(invocation));
        Mockito.verify(answer).isAnswer();
        Mockito.verify(answer).getInvocation();
        Mockito.verify(answer).getAnswer();

        Assert.assertEquals(testObject, result);
    }

    @Test
    public void testHandleMethodWithNullInvocation() {
        Assert.assertThrows(NullPointerException.class, () -> mockHandler.handle(null));
    }

    @Test
    public void testHandleMethodWithEmptyInvocation() {
        Invocation emptyInvocation = mock(Invocation.class);

        mockHandler.handle(emptyInvocation);

        Mockito.verify(mockHandler).setInvocationForPotentialStubbing(isNull());
        Mockito.verifyNoMoreInteractions(mockHandler);
    }

    @Test
    public void testHandleMethodWithInvocationWithArguments() {
        Object[] testArguments = new Object[]{"arg1", "arg2"};
        Invocation invocation = Mockito.mock(Invocation.class);
        ArgumentCaptor<Object[]> argumentsCaptor = ArgumentCaptor.forClass(Object[].class);
        Mockito.when(invocation.getArguments()).thenReturn(testArguments);

        mockHandler.handle(invocation);

        Mockito.verify(mockHandler).setInvocationForPotentialStubbing(eq(invocation));
        Mockito.verify(mockHandler).reportOngoingStubbing(isInstanceOf(OngoingStubbingImpl.class));
        Mockito.verify(mockHandler).validateState();
        Mockito.verifyNoMoreInteractions(mockHandler);
    }

    @Test
    public void testHandleMethodWithVoidMethod() throws Throwable {
        Invocation voidInvocation = mock(Invocation.class);
        mockHandler.handle(voidInvocation);

        Mockito.verify(mockHandler).setInvocationForPotentialStubbing(isNull());
        Mockito.verifyNoMoreInteractions(mockHandler);
    }

    @Test
    public void testMockHandlerConstructionWithMockSettings() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Mockito.verify(mockSettings).getDefaultAnswer();

        Assert.assertNotNull(mockHandler.getInvocationContainer());
        Assert.assertNotNull(mockHandler.getMatchersBinder());
        Assert.assertNotNull(mockHandler.getMockingProgress());
        Assert.assertNotNull(mockHandler.getMockSettings());
    }

    @Test
    public void testMockHandlerConstructionWithOldMockHandler() {
        MockHandlerInterface<Object> oldMockHandler = mock(MockHandlerInterface.class);
        MockHandler<Object> newMockHandler = new MockHandler<>(oldMockHandler);

        Mockito.verify(oldMockHandler).getMockSettings();

        Assert.assertSame(oldMockHandler, newMockHandler.getOldMockHandler());
        Assert.assertNotNull(newMockHandler.getInvocationContainer());
        Assert.assertNotNull(newMockHandler.getMatchersBinder());
        Assert.assertNotNull(newMockHandler.getMockingProgress());
        Assert.assertNotNull(newMockHandler.getMockSettings());
    }

    @Test
    public void testGetInvocationContainer() {
        MockHandler<Object> mockHandler = new MockHandler<>();
        Assert.assertNotNull(mockHandler.getInvocationContainer());
    }

    @Test
    public void testGetMockSettings() {
        MockHandler<Object> mockHandler = new MockHandler<>();
        Assert.assertNotNull(mockHandler.getMockSettings());
    }

    @Test
    public void testVoidMethodStubbable() {
        Object testMock = mock(Object.class);
        MockHandler<Object> mockHandler = new MockHandler<>();
        VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(testMock);

        Assert.assertNotNull(voidMethodStubbable);
    }
}