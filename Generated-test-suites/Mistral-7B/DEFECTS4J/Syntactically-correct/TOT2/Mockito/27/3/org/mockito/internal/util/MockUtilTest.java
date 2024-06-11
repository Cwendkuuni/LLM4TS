import org.mockito.ArgumentMatchers.*;
import org.mockito.Mockito.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    @Test
    public void testConstructor() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(creationValidator);
        MockUtil mockUtil2 = new MockUtil();
        // Verify that the constructor is called with the correct arguments
        verify(creationValidator, times(mockUtil.creationValidator == creationValidator ? 1 : 0)).validateType(MockUtil.class);
        verifyNoMoreInteractions(creationValidator);
    }

    @SuppressWarnings("unchecked")
    @Test
    public <T> void testCreateMock() throws Exception {
        Class<T> classToMock = mock(Class.class);
        MockSettingsImpl settings = new MockSettingsImpl();
        T mock = mockUtil.createMock(classToMock, settings);
        // Verify that the createMock method is called with the correct arguments
        verify(creationValidator).validateType(classToMock);
        verify(creationValidator).validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        verify(creationValidator).validateMockedType(classToMock, settings.getSpiedInstance());
        // Test the case when the spied instance is null
        settings.setSpiedInstance(null);
        try {
            mockUtil.createMock(classToMock, settings);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected
        }
    }

    @Test
    public <T> void testResetMock() {
        Class<T> classToMock = mock(Class.class);
        MockSettingsImpl settings = new MockSettingsImpl();
        T mock = mockUtil.createMock(classToMock, settings);
        // Test that the method is called without any arguments
        mockUtil.resetMock(mock);
        // Test that the old mock handler is replaced with a new one
        MockHandlerInterface<T> oldMockHandler = mockUtil.getMockHandler(mock);
        mockUtil.resetMock(mock);
        MockHandlerInterface<T> newMockHandler = mockUtil.getMockHandler(mock);
        assertNotSame(oldMockHandler, newMockHandler);
    }

    @Test
    public <T> void testNewMethodInterceptorFilter() {
        MockSettingsImpl settings = new MockSettingsImpl();
        MethodInterceptorFilter filter = mockUtil.newMethodInterceptorFilter(settings);
        // Verify that the filter is initialized with the correct mock handler and settings
        MockHandler<T> mockHandler = filter.getMockHandler();
        InvocationNotifierHandler<T> invocationNotifierHandler = filter.getInvocationNotifierHandler();
        assertEquals(settings, mockHandler.getMockSettings());
    }

    @Test
    public <T> void testGetMockHandler() {
        Class<T> classToMock = mock(Class.class);
        MockSettingsImpl settings = new MockSettingsImpl();
        T mock = mockUtil.createMock(classToMock, settings);
        MockHandlerInterface<T> mockHandler = mockUtil.getMockHandler(mock);
        // Verify that the method returns the correct mock handler
        assertEquals(settings.getMockHandler(), mockHandler);
    }

    @Test
    public <T> void testIsMockitoMock() {
        Class<T> classToMock = mock(Class.class);
        MockSettingsImpl settings = new MockSettingsImpl();
        T mock = mockUtil.createMock(classToMock, settings);
        // Test that the method returns true when the argument is a mockito mock
        assertTrue(mockUtil.isMockitoMock(mock));
        // Test that the method returns false when the argument is not a mockito mock
        assertFalse(mockUtil.isMockitoMock(new Object()));
    }

    @Test
    public void testIsMock() {
        Object mock = mock(Object.class);
        assertFalse(mockUtil.isMock(mock));
        Factory factory = mockitoMock(Factory.class);
        MockHandler<Object> mockHandler = new MockHandler<Object>(null);
        factory.setCallback(0, mockUtil.newMethodInterceptorFilter(mockHandler));
        assertTrue(mockUtil.isMock(factory));
    }

    @Test
    public <T> void testGetInterceptor() {
        Class<T> classToMock = mock(Class.class);
        MockSettingsImpl settings = new MockSettingsImpl();
        T mock = mockUtil.createMock(classToMock, settings);
        MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
        // Verify that the method returns the correct interceptor
        assertNotNull(interceptor);
        assertEquals(settings.getMockHandler().getMockSettings(), interceptor.getMockSettings());
    }

    @Test
    public void testGetMockName() {
        Object mock = mock(Object.class);
        MockName mockName = mockUtil.getMockName(mock);
        // Verify that the method returns a non-null mock name
        assertNotNull(mockName);
    }
}