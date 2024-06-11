import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.reflection.ClassImposterizer;
import org.mockito.internal.util.reflection.ClassUtils;
import org.mockito.internal.util.reflection.ArrayUtils;
import org.mockito.internal.util.reflection.MethodInterceptorFilter;
import org.mockito.internal.util.reflection.MockSettingsImpl;
import org.mockito.internal.util.reflection.InvocationNotifierHandler;
import org.mockito.internal.util.reflection.InvocationNotifier;
import org.mockito.internal.creation.jmock.ClassImposterizerImpl;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.io.Serializable;

public class MockUtilTest {

    @Test
    public void testCreateMock_NullClassToMock() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockSettingsImpl settings = new MockSettingsImpl();

        MockUtil mockUtil = new MockUtil();

        try {
            mockUtil.createMock(null, settings);
            fail("Expected MockitoException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testCreateMock_NullSettings() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        Class<String> classToMock = String.class;

        MockUtil mockUtil = new MockUtil();

        try {
            mockUtil.createMock(classToMock, null);
            fail("Expected MockitoException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testCreateMock_NullSpiedInstance() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<String> classToMock = String.class;

        MockUtil mockUtil = new MockUtil();

        try {
            mockUtil.createMock(classToMock, settings);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected
        }

        settings.setSpiedInstance(null);

        try {
            mockUtil.createMock(classToMock, settings);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected
        }
    }

    @Test
    public void testCreateMock_AbstractClassToMock() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<Object> abstractClassToMock = AbstractClass.class;

        MockUtil mockUtil = new MockUtil();

        try {
            mockUtil.createMock(abstractClassToMock, settings);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected
        }
    }

    @Test
    public void testCreateMock_FinalClassToMock() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<String> finalClassToMock = String.class;
        finalClassToMock.declareFinal();

        MockUtil mockUtil = new MockUtil();

        try {
            mockUtil.createMock(finalClassToMock, settings);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected
        }
    }

    @Test
    public void testCreateMock_MockitoMock() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<String> classToMock = String.class;
        Object mockToMock = mock(String.class);

        MockUtil mockUtil = new MockUtil();

        try {
            mockUtil.createMock(classToMock, settings);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected
        }

        try {
            mockUtil.createMock(classToMock, settings);
            mockUtil.resetMock(mockToMock);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected
        }
    }

    @Test
    public void testCreateMock_NullExtraInterfaces() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        MockUtil mockUtil = new MockUtil();

        try {
            mockUtil.createMock(classToMock, settings);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected
        }

        settings.setExtraInterfaces(null);

        try {
            mockUtil.createMock(classToMock, settings);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected
        }
    }

    @Test
    public void testCreateMock_NullExtraInterfaceInArray() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = new Class<?>[]{Object.class, null};

        MockUtil mockUtil = new MockUtil();

        try {
            mockUtil.createMock(classToMock, settings);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected
        }

        settings.setExtraInterfaces(extraInterfaces);

        try {
            mockUtil.createMock(classToMock, settings);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected
        }

        ClassUtils.removeArrayElement(extraInterfaces, 0);
    }

    @Test
    public void testCreateMock_CreationException() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        ClassImposterizer imposterizer = mock(ClassImposterizer.class);
        LenientCopyTool copyTool = mock(LenientCopyTool.class);

        MockUtil mockUtil = spy(new MockUtil());
        when(mockUtil.getClassImposterizer()).thenReturn(imposterizer);
        when(mockUtil.newLenientCopyTool()).thenThrow(new RuntimeException());

        try {
            mockUtil.createMock(classToMock, settings);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected
        }

        settings.setSpiedInstance(spiedInstance);

        try {
            mockUtil.createMock(classToMock, settings);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected
        }
    }

    @Test
    public void testResetMock() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockSettingsImpl settings = new MockSettingsImpl();
        Object mock = mock(String.class);

        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(mock);

        MockHandler<String> handler = mockUtil.getMockHandler(mock);
        MockHandler<String> newHandler = new MockHandler<String>(handler);
        MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newHandler, settings);

        assertEquals(handler, mockUtil.getMockHandler(mock));
        assertEquals(newHandler, mockUtil.getMockHandler(mock));
    }

    @Test
    public void testGetMockHandler() {
        Object mock = mock(String.class);

        MockUtil mockUtil = new MockUtil();

        MockHandler<String> handler = mockUtil.getMockHandler(mock);
        assertNotNull(handler);

        assertNull(mockUtil.getMockHandler(null));
        assertNull(mockUtil.getMockHandler(new Object()));
    }

    @Test
    public void testIsMockitoMock() {
        Object mock = mock(String.class);

        MockUtil mockUtil = new MockUtil();

        assertTrue(mockUtil.isMockitoMock(mock));
        assertFalse(mockUtil.isMockitoMock(null));
        assertFalse(mockUtil.isMockitoMock(new Object()));
    }

    @Test
    public void testIsMock() {
        Object mock = mock(String.class);

        MockUtil mockUtil = new MockUtil();

        assertTrue(mockUtil.isMock(mock));
        assertFalse(mockUtil.isMock(null));
        assertFalse(mockUtil.isMock(new Object()));
    }

    @Test
    public void testGetInterceptor() {
        Object mock = mock(String.class);

        MockUtil mockUtil = new MockUtil();

        assertNull(mockUtil.getInterceptor(null));
        assertNull(mockUtil.getInterceptor(new Object()));

        Factory factory = mock(Factory.class);
        when(factory.getCallback(0)).thenReturn(mock);
        when(mockUtil.newMockUtilSpy()).thenReturn(mockUtil);

        MethodInterceptorFilter interceptorFilter = mockUtil.getInterceptor(factory);

        assertEquals(mockUtil, interceptorFilter.getHandler());
    }

    @Test
    public void testGetMockName() {
        Object mock = mock(String.class);

        MockUtil mockUtil = new MockUtil();

        MockHandler<String> handler = mockUtil.getMockHandler(mock);
        MockSettingsImpl settings = handler.getMockSettings();

        MockName mockName = mockUtil.getMockName(mock);

        assertEquals(settings.getMockName(), mockName);
    }
}