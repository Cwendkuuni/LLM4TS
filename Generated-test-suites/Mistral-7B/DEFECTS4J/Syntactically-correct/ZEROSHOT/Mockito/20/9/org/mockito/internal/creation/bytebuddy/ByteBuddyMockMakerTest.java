package org.mockito.internal.creation.bytebuddy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerRegistry;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.mockito.internal.util.StringJoiner.join;
import static org.mockito.internal.util.reflection.FieldAccessUtils.readField;
import static org.mockito.internal.util.reflection.FieldAccessUtils.writeField;

public class ByteBuddyMockMakerTest {

    private MockMaker mockMaker;
    private MockMakerRegistry mockMakerRegistry;
    private GlobalConfiguration globalConfiguration;
    private ClassInstantiator classInstantiator;
    private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;

    @Before
    public void setUp() {
        mockMakerRegistry = new MockMakerRegistry();
        globalConfiguration = new GlobalConfiguration();
        mockMaker = new ByteBuddyMockMaker();
        mockMakerRegistry.register(ByteBuddyMockMaker.class, mockMaker);
        classInstantiator = mockMaker.initializeClassInstantiator();
        cachingMockBytecodeGenerator = new CachingMockBytecodeGenerator();
        mockMaker.setGlobalConfiguration(globalConfiguration);
    }

    @Test
    public void testCreateMockWithValidSettingsAndHandler() {
        MockHandler mockHandler = mock(MockHandler.class);
        Class<Object> mockedType = Object.class;
        MockCreationSettings<Object> settings = new MockCreationSettings<Object>(mockedType, SerializableMode.DEFAULT);
        Object mock = mockMaker.createMock(settings, mockHandler);

        verify(mockHandler).handle(any());
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithInvalidSerializableMode() {
        MockHandler mockHandler = mock(MockHandler.class);
        Class<Object> mockedType = Object.class;
        MockCreationSettings<Object> settings = new MockCreationSettings<Object>(mockedType, SerializableMode.ACROSS_CLASSLOADERS);

        mockMaker.createMock(settings, mockHandler);
    }

    @Test
    public void testCreateMockWithInvalidHandler() {
        Object mock = mockMaker.createMock(new MockCreationSettings<Object>(Object.class, SerializableMode.DEFAULT), null);
        verifyZeroInteractions(mock);
    }

    @Test
    public void testGetHandler() {
        Object mock = mockMaker.createMock(new MockCreationSettings<Object>(Object.class, SerializableMode.DEFAULT), mock(MockHandler.class));
        MockHandler expectedMockHandler = mock(MockHandler.class);
        when(expectedMockHandler.handle(any())).thenReturn(mock);
        writeField(mock, "mockitoInterceptor", expectedMockHandler);

        MockHandler actualMockHandler = mockMaker.getHandler(mock);
        verify(expectedMockHandler).handle(any());
        verify(expectedMockHandler).equals(actualMockHandler);
    }

    @Test
    public void testResetMock() {
        Object mock = mockMaker.createMock(new MockCreationSettings<Object>(Object.class, SerializableMode.DEFAULT), mock(MockHandler.class));
        InternalMockHandler internalMockHandler = mock(InternalMockHandler.class);
        MockHandler mockHandler = internalMockHandler;

        mockMaker.resetMock(mock, mockHandler, new MockCreationSettings<Object>(Object.class, SerializableMode.DEFAULT));

        MockHandler actualMockHandler = mockMaker.getHandler(mock);
        verify(internalMockHandler).handle(any());
        verify(internalMockHandler).equals(actualMockHandler);
    }

    @Test
    public void testInitializeClassInstantiator() {
        try {
            mockMaker.initializeClassInstantiator();
        } catch (Throwable throwable) {
            fail("Should not throw any exception");
        }
    }
}