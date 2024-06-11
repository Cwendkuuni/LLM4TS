package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.Serializable;
import java.lang.reflect.Proxy;

public class MockUtilTest {

    private MockCreationValidator creationValidator = mock(MockCreationValidator.class);
    private MockUtil mockUtil = new MockUtil(creationValidator);
    private Factory mockFactory = mock(Factory.class);
    private MethodInterceptorFilter methodInterceptorFilter = mock(MethodInterceptorFilter.class);
    private InvocationNotifierHandler invocationNotifierHandler = mock(InvocationNotifierHandler.class);
    private MockHandler mockHandler = mock(MockHandler.class);
    private MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
    private ClassImposterizer classImposterizer = mock(ClassImposterizer.class);
    private LenientCopyTool lenientCopyTool = mock(LenientCopyTool.class);

    @Test
    public void testCreateMock() {
        when(creationValidator.validateType(any(Class.class))).thenReturn(null);
        when(creationValidator.validateExtraInterfaces(any(Class.class), any(Class[][].class))).thenReturn(null);
        when(creationValidator.validateMockedType(any(Class.class), any())).thenReturn(null);
        when(mockSettings.getSpiedInstance()).thenReturn(new Object());
        when(classImposterizer.imposterise(any(MethodInterceptorFilter.class), any(Class.class), any(Class[][].class))).thenReturn(mock(Object.class));

        Object result = mockUtil.createMock(String.class, mockSettings);
        verify(creationValidator).validateType(eq(String.class));
        verify(creationValidator).validateExtraInterfaces(eq(String.class), isNull());
        verify(creationValidator).validateMockedType(eq(String.class), eq(null));
        verify(mockSettings).initiateMockName(eq(String.class));
        verify(classImposterizer).imposterise(any(MethodInterceptorFilter.class), eq(String.class), isNull());
        assertNotNull(result);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNullClass() {
        when(creationValidator.validateType(null)).thenThrow(new NotAMockException());

        mockUtil.createMock(null, mockSettings);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithInvalidClass() {
        when(creationValidator.validateType(any(Class.class))).thenThrow(new NotAMockException());

        mockUtil.createMock(Integer.class, mockSettings);
    }

    @Test
    public void testCreateMockWithInterfaces() {
        when(creationValidator.validateType(any(Class.class))).thenReturn(null);
        when(creationValidator.validateExtraInterfaces(any(Class.class), any(Class[][].class))).thenReturn(new Class[][]{new Class[]{Serializable.class}});
        when(mockSettings.getSpiedInstance()).thenReturn(new Object());
        when(classImposterizer.imposterise(any(MethodInterceptorFilter.class), any(Class.class), any(Class[][].class))).thenReturn(mock(Object.class));

        Object result = mockUtil.createMock(String.class, mockSettings);
        verify(creationValidator).validateType(eq(String.class));
        verify(creationValidator).validateExtraInterfaces(eq(String.class), arrayContaining(Serializable.class));
        verify(mockSettings).initiateMockName(eq(String.class));
        verify(classImposterizer).imposterise(any(MethodInterceptorFilter.class), eq(String.class), arrayContaining(Serializable.class));
        assertNotNull(result);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithInvalidInterface() {
        when(creationValidator.validateType(any(Class.class))).thenReturn(null);
        when(creationValidator.validateExtraInterfaces(any(Class.class), any(Class[][].class))).thenThrow(new NotAMockException());

        mockUtil.createMock(String.class, mockSettings);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithMockedInstance() {
        when(creationValidator.validateType(any(Class.class))).thenReturn(null);
        when(creationValidator.validateExtraInterfaces(any(Class.class), any(Class[][].class))).thenReturn(null);
        when(creationValidator.validateMockedType(any(Class.class), any())).thenThrow(new NotAMockException());

        mockUtil.createMock(String.class, mockSettings);
    }

    @Test
    public void testResetMock() {
        when(mockHandler.getHandler()).thenReturn(new Object());

        mockUtil.resetMock(mockFactory);

        verify(mockFactory).setCallback(eq(0), any(MethodInterceptorFilter.class));
    }

    @Test
    public void testGetMockHandler() {
        Object mock = mock(Object.class);
        when(getInterceptor(mock)).thenReturn(methodInterceptorFilter);

        MockHandlerInterface<Object> result = mockUtil.getMockHandler(mock);

        verify(mockHandler).getHandler();
        assertEquals(methodInterceptorFilter, result);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockitoMock() {
        Object mock = new Object();

        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testIsMock() {
        Object mock = mock(Object.class);
        when(isMockitoMock(mock)).thenReturn(true);

        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMockFalse() {
        Object mock = new Object();
        when(isMockitoMock(mock)).thenReturn(false);

        assertFalse(mockUtil.isMock(mock));
    }

    @Test
    public void testGetInterceptor() {
        Object mock = mock(Factory.class);
        when(getInterceptor(mock)).thenReturn(methodInterceptorFilter);

        assertSame(methodInterceptorFilter, mockUtil.getInterceptor(mock));
    }

    @Test
    public void testGetInterceptorNull() {
        Object mock = new Object();
        when(getInterceptor(mock)).thenReturn(null);

        assertNull(mockUtil.getInterceptor(mock));
    }

    @Test
    public void testGetMockName() {
        Object mock = mock(Object.class);
        when(getMockHandler(mock)).thenReturn(mockHandler);
        when(mockHandler.getMockSettings()).thenReturn(mockSettings);

        MockName result = mockUtil.getMockName(mock);

        assertEquals(mockSettings.getMockName(), result);
    }

    @Test
    public void testGetMockNameNull() {
        Object mock = new Object();

        assertNull(mockUtil.getMockName(mock));
    }
}