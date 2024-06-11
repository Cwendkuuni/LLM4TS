package org.mockito.internal.creation.bytebuddy.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerImpl;
import org.mockito.plugins.MockMakerRegistry;
import org.mockito.plugins.MockMakerRegistryImpl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ByteBuddyMockMakerTest {

    private MockMaker mockMaker;
    private ClassInstantiator classInstantiator;
    private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;

    @Before
    public void setup() {
        GlobalConfiguration globalConfiguration = new GlobalConfiguration();
        globalConfiguration.enableClassCache();
        this.classInstantiator = mock(ClassInstantiator.class);
        this.cachingMockBytecodeGenerator = mock(CachingMockBytecodeGenerator.class);
        this.mockMaker = new ByteBuddyMockMaker() {
            @Override
            public ClassInstantiator getClassInstantiator() {
                return classInstantiator;
            }

            @Override
            public CachingMockBytecodeGenerator getCachingMockBytecodeGenerator() {
                return cachingMockBytecodeGenerator;
            }
        };

        MockMakerRegistry mockMakerRegistry = new MockMakerRegistryImpl();
        MockMakerRegistry.setGlobalMockMakerRegistry(mockMakerRegistry);
        mockMakerRegistry.registerMockMaker(mockMaker);
    }

    @Test
    public void testCreateMockSuccess() {
        MockHandler handler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings<>(Object.class, handler);
        Object mock = mockMaker.createMock(settings, handler);
        verify(handler).mockMethodCall(mock, any());
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockSerializationAcrossClassloaders() {
        MockHandler handler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings<>(Object.class, handler);
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        mockMaker.createMock(settings, handler);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockInstantiationException() {
        MockHandler handler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings<>(String[].class, handler);
        mockMaker.createMock(settings, handler);
    }

    @Test(expected = ClassCastException.class)
    public void testCreateMockClassCastException() {
        MockHandler handler = mock(MockHandler.class);
        MockCreationSettings<Integer> settings = new MockCreationSettings<>(String.class, handler);
        Object mock = mockMaker.createMock(settings, handler);
    }

    @Test
    public void testGetHandler() {
        Object mock = spy(mockMaker.createMock(new MockCreationSettings<>(Object.class, mock(MockHandler.class))));
        MockHandler handler = mockMaker.getHandler(mock);
        verify(handler).handle(eq(mock), any());
    }

    @Test
    public void testResetMock() {
        Object mock = mockMaker.createMock(new MockCreationSettings<>(Object.class, mock(MockHandler.class)));
        MockHandler newHandler = mock(MockHandler.class);
        mockMaker.resetMock(mock, newHandler, new MockCreationSettings<>(Object.class, newHandler));
        MockHandler oldHandler = mockMaker.getHandler(mock);
        verify(newHandler).handle(eq(mock), any());
        verify(oldHandler).mockMethodCall(mock, any());
    }
}