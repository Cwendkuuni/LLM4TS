package org.mockito.internal.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.reflection.MethodInterceptorFilter;
import org.mockito.internal.util.reflection.MethodInterceptorProxy;
import org.mockito.internal.util.reflection.MockCreationValidator;
import org.mockito.internal.util.reflection.MockHandler;
import org.mockito.internal.util.reflection.MockHandlerInterface;
import org.mockito.internal.util.reflection.MockSettings;
import org.mockito.internal.util.reflection.MethodInterceptor;

import java.io.Serializable;

public class MockUtilTest {
    private MockCreationValidator creationValidator;
    private MockUtil mockUtil;
    private MockSettings settings;
    private ClassImposterizer classImposterizer;

    @Before
    public void setup() {
        creationValidator = mock(MockCreationValidator.class);
        classImposterizer = mock(ClassImposterizer.class);
        settings = mock(MockSettingsImpl.class);
        mockUtil = new MockUtil(creationValidator);
    }

    @Test
    public void testCreateMock() {
        when(creationValidator.validateType(MockUtil.class)).thenReturn(true);
        when(creationValidator.validateExtraInterfaces(MockUtil.class, new Class[0])).thenReturn(true);
        when(creationValidator.validateMockedType(MockUtil.class, null)).thenReturn(true);
        when(settings.getSpiedInstance()).thenReturn(new Object());

        when(classImposterizer.imposterise(any(MethodInterceptorFilter.class), eq(MockUtil.class), anyArray())).thenReturn(mock(MethodInterceptorProxy.class));

        MockHandlerInterface mockHandler = mockUtil.createMock(MockUtil.class, settings);
        assertNotNull(mockHandler);
        verify(classImposterizer).imposterise(any(MethodInterceptorFilter.class), eq(MockUtil.class), anyArray());

        verify(settings, never()).initiateMockName(MockUtil.class);
        verify(settings, never()).isSerializable();
        verify(settings, never()).getExtraInterfaces();
        verify(settings, never()).getMockName();
    }

    @Test(expected = org.mockito.exceptions.misusing.NotAMockException.class)
    public void testCreateMockWithNullClass() {
        mockUtil.createMock(null, settings);
    }

    @Test
    public void testCreateMockWithSerializableSettings() {
        when(creationValidator.validateType(MockUtil.class)).thenReturn(true);
        when(creationValidator.validateExtraInterfaces(MockUtil.class, new Class[0])).thenReturn(true);
        when(creationValidator.validateMockedType(MockUtil.class, null)).thenReturn(true);
        when(settings.getSpiedInstance()).thenReturn(new Object());
        when(settings.isSerializable()).thenReturn(true);

        MockHandlerInterface mockHandler = mockUtil.createMock(MockUtil.class, settings);
        assertNotNull(mockHandler);

        verify(settings).initiateMockName(eq(MockUtil.class));
        verify(settings).isSerializable();
        verify(settings).getExtraInterfaces();
        verify(settings, never()).getMockName();
    }

    @Test
    public void testCreateMockWithExtraInterfaces() {
        when(creationValidator.validateType(MockUtil.class)).thenReturn(true);
        when(creationValidator.validateExtraInterfaces(MockUtil.class, new Class[0])).thenThrow(new RuntimeException());
        when(creationValidator.validateMockedType(MockUtil.class, null)).thenReturn(true);
        when(settings.getSpiedInstance()).thenReturn(new Object());

        try {
            mockUtil.createMock(MockUtil.class, settings);
            fail();
        } catch (RuntimeException e) {
            // expected
        }

        verify(creationValidator).validateType(eq(MockUtil.class));
        verify(creationValidator).validateExtraInterfaces(eq(MockUtil.class), anyArray());
        verify(creationValidator, never()).validateMockedType(eq(MockUtil.class), any());
    }

    @Test
    public void testCreateMockWithNullSpiedInstance() {
        when(creationValidator.validateType(MockUtil.class)).thenReturn(true);
        when(creationValidator.validateExtraInterfaces(MockUtil.class, new Class[0])).thenReturn(true);
        when(creationValidator.validateMockedType(MockUtil.class, null)).thenReturn(true);
        when(settings.getSpiedInstance()).thenReturn(null);

        try {
            mockUtil.createMock(MockUtil.class, settings);
            fail();
        } catch (NotAMockException e) {
            // expected
        }

        verify(creationValidator).validateType(eq(MockUtil.class));
        verify(creationValidator).validateExtraInterfaces(eq(MockUtil.class), anyArray());
        verify(creationValidator).validateMockedType(eq(MockUtil.class), null);
        verify(settings, never()).initiateMockName(eq(MockUtil.class));
        verify(settings, never()).isSerializable();
        verify(settings, never()).getExtraInterfaces();
        verify(settings, never()).getMockName();
    }

    @Test
    public void testResetMock() {
        MockHandler<?> mockHandler = mock(MockHandler.class);
        Object mock = mockHandler.getMock();
        mockUtil.resetMock(mock);

        verify(mockHandler).setCallback(eq(0), any(MethodInterceptorFilter.class));
        verifyNoInteractions(mock);
    }

    @Test
    public void testIsMock() {
        Object mock = mock(Factory.class);
        assertTrue(mockUtil.isMock(mock));

        Object nonMock = new Object();
        assertFalse(mockUtil.isMock(nonMock));
    }

    @Test
    public void testGetInterceptor() {
        Factory mock = mock(Factory.class);
        MethodInterceptorFilter interceptorFilter = mock(MethodInterceptorFilter.class);
        when(mock.getCallback(0)).thenReturn(interceptorFilter);

        MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
        assertEquals(interceptorFilter, interceptor);

        assertNull(mockUtil.getInterceptor(new Object()));
    }

    @Test
    public void testGetMockName() {
        Object mock = mock(Factory.class);
        MockHandler<?> mockHandler = mockHandler(mock);
        MockSettings settings = mockHandler.getMockSettings();

        String mockName = mockUtil.getMockName(mock);
        assertEquals(settings.getMockName(), mockName);
    }

    private MockHandler<?> mockHandler(Object mock) {
        MockHandler<?> mockHandler = mock(MockHandler.class);
        when(mockHandler.getMock()).thenReturn(mock);
        return mockHandler;
    }
}