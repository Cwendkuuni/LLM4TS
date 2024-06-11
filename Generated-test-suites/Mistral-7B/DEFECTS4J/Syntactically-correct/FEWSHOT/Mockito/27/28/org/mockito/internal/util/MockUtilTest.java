package org.mockito.internal.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Proxy;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.invocation.MockHandler;
import org.mockito.internal.invocation.MockHandlerInterface;
import org.mockito.internal.invocation.ProxyStatement;
import org.mockito.internal.util.reflection.LenientCopyTool;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(creationValidator);

        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        String mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);

        verify(creationValidator, ArgumentMatchers.eq(classToMock)).validateType(ArgumentMatchers.isA(String.class));
        verify(creationValidator, ArgumentMatchers.eq(classToMock)).validateExtraInterfaces(ArgumentMatchers.isA(Class[].class), ArgumentMatchers.eq(new Class[0]));
        verify(creationValidator, ArgumentMatchers.eq(classToMock)).validateMockedType(ArgumentMatchers.isA(String.class), ArgumentMatchers.isNull());

        MockHandler<String> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);

        ProxyStatement proxyStatement = (ProxyStatement) Proxy.getInvocationHandler(mock);
        assertNotNull(proxyStatement);
        assertNotNull(proxyStatement.getHandler());
        assertEquals(mockHandler, proxyStatement.getHandler());

        ClassImposterizer imposterizer = ClassImposterizer.INSTANCE;
        MockHandlerInterface<String> imposter = imposterizer.imposterise(mockUtil.newMethodInterceptorFilter(settings), classToMock, new Class[0]);
        assertEquals(mockHandler, imposter);

        LenientCopyTool copyTool = new LenientCopyTool();
        Object spiedInstance = spy(new String("foo"));
        mockUtil.createMock(classToMock, settings).lenient().when(ArgumentMatchers.any()).thenReturn(spiedInstance);
        copyTool.copyToMock(spiedInstance, mock);
        assertEquals("foo", mock.toString());
    }

    @Test
    public void testCreateMockWithExtraInterfaces() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(creationValidator);

        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Serializable.class};

        String mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);

        verify(creationValidator, ArgumentMatchers.eq(classToMock)).validateType(ArgumentMatchers.isA(String.class));
        verify(creationValidator, ArgumentMatchers.eq(classToMock)).validateExtraInterfaces(ArgumentMatchers.isA(Class[].class), ArgumentMatchers.arrayContaining(extraInterfaces));
        verify(creationValidator, ArgumentMatchers.eq(classToMock)).validateMockedType(ArgumentMatchers.isA(String.class), ArgumentMatchers.isNull());

        MockHandler<String> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);

        ClassImposterizer imposterizer = ClassImposterizer.INSTANCE;
        MockHandlerInterface<String> imposter = imposterizer.imposterise(mockUtil.newMethodInterceptorFilter(settings), classToMock, extraInterfaces);
        assertEquals(mockHandler, imposter);
    }

    @Test
    public void testCreateMockWithSpiedInstance() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(creationValidator);

        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        Object spiedInstance = "foo";

        String mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);

        verify(creationValidator, ArgumentMatchers.eq(classToMock)).validateType(ArgumentMatchers.isA(String.class));
        verify(creationValidator, ArgumentMatchers.eq(classToMock)).validateExtraInterfaces(ArgumentMatchers.eq(new Class[0]));
        verify(creationValidator, ArgumentMatchers.eq(classToMock)).validateMockedType(ArgumentMatchers.isA(String.class), ArgumentMatchers.eq(spiedInstance));

        MockHandler<String> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);

        LenientCopyTool copyTool = new LenientCopyTool();
        mockUtil.createMock(classToMock, settings).lenient().when(ArgumentMatchers.any()).thenReturn((String) spiedInstance);
        copyTool.copyToMock(spiedInstance, mock);
        assertEquals("foo", mock.toString());
    }

    @Test
    public void testResetMock() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(creationValidator);
        String mock = mock(String.class);

        mockUtil.resetMock(mock);

        MockHandler<String> oldMockHandler = mockUtil.getMockHandler(mock);
        MockHandler<String> newMockHandler = new MockHandler<String>(oldMockHandler);
        MethodInterceptorFilter newFilter = mockUtil.newMethodInterceptorFilter(new MockSettingsImpl());

        verify(mock, ArgumentMatchers.times(1)).setCallback(ArgumentMatchers.eq(0), ArgumentMatchers.isA(newFilter));
    }

    @Test
    public void testGetMockHandler() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(creationValidator);
        Object mock = mock(Object.class);

        MockHandler<Object> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);

        when(mockUtil.isMockitoMock(mock)).thenReturn(true);
        assertTrue(mockUtil.isMock(mock));

        when(mockUtil.isMockitoMock(mock)).thenReturn(false);
        assertFalse(mockUtil.isMock(mock));
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        assertNull(mockUtil.getMockHandler(null));
    }

    @Test(expected = NotAMockException.class)
    public void testIsMockWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        assertFalse(mockUtil.isMock(null));
    }

    @Test
    public void testIsMockitoMockWithMock() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mock(Object.class);
        assertTrue(mockUtil.isMockitoMock(mock));
    }

    @Test
    public void testIsMockitoMockWithNonMock() {
        MockUtil mockUtil = new MockUtil();
        Object nonMock = new Object();
        assertFalse(mockUtil.isMockitoMock(nonMock));
    }
}