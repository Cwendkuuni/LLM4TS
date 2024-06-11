import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stub;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

    private MockHandler<MockitoTestClass> mockHandler;
    private MockitoTestClass mockitoTestClass;
    private InvocationOnMock invocation;

    @Before
    public void setup() {
        mockHandler = new MockHandler<>();
        mockitoTestClass = Mockito.mock(MockitoTestClass.class);
    }

    @Test
    public void testHandle_InvocationWithoutStubbingOrVerification() throws Throwable {
        when(mockitoTestClass.someMethod(anyInt())).thenReturn(42);

        Object result = mockHandler.handle(new InvocationOnMock(mockitoTestClass, "someMethod", new Object[]{1}, new Class[]{int.class}));

        verify(mockitoTestClass).someMethod(1);
        assertEquals(42, result);
    }

    @Test
    public void testHandle_InvocationWithStubbing_VoidMethod() throws Throwable {
        mockHandler.voidMethodStubbable(mockitoTestClass).stub().thenReturn(true);

        boolean result = (boolean) mockHandler.handle(new InvocationOnMock(mockitoTestClass, "someMethod", new Object[0], new Class[0]));

        verify(mockitoTestClass).someMethod();
        assertTrue(result);
    }

    @Test
    public void testHandle_InvocationWithStubbing_Answer() throws Throwable {
        Answer<Integer> answer = new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return 42;
            }
        };

        mockHandler.setAnswersForStubbing(answer);

        Object result = mockHandler.handle(new InvocationOnMock(mockitoTestClass, "someMethod", new Object[0], new Class[0]));

        verify(mockitoTestClass).someMethod();
        assertEquals(42, result);
    }

    @Test
    public void testVoidMethodStubbable() {
        VoidMethodStubbable<MockitoTestClass> voidMethodStubbable = mockHandler.voidMethodStubbable(mockitoTestClass);

        voidMethodStubbable.doAnswer(arg -> System.out.println("Void method stubbed")).when(mockitoTestClass).someMethod();

        mockHandler.handle(new InvocationOnMock(mockitoTestClass, "someMethod", new Object[0], new Class[0]));

        verify(mockitoTestClass).someMethod();
        assertTrue(System.out::println.matched);
    }
}