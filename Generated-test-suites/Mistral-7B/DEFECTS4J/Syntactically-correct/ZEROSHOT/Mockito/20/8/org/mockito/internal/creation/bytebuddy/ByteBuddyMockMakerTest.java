package org.mockito.internal.creation.bytebuddy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerHandler;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private GlobalConfiguration globalConfiguration;
    private ClassInstantiator classInstantiatorMock;
    private CachingMockBytecodeGenerator cachingMockBytecodeGeneratorMock;

    @Before
    public void setUp() {
        globalConfiguration = Mockito.mock(GlobalConfiguration.class);
        classInstantiatorMock = Mockito.spy(new ClassInstantiator());
        cachingMockBytecodeGeneratorMock = Mockito.mock(CachingMockBytecodeGenerator.class);
        byteBuddyMockMaker = new ByteBuddyMockMaker() {
            @Override
            protected ClassInstantiator classInstantiator() {
                return classInstantiatorMock;
            }

            @Override
            protected CachingMockBytecodeGenerator cachingMockBytecodeGenerator() {
                return cachingMockBytecodeGeneratorMock;
            }
        };
    }

    @Test
    public void testCreateMock() throws Exception {
        MockCreationSettings<String> settings = new MockCreationSettings.Builder<String>().build();
        MockHandler handler = Mockito.mock(MockHandler.class);
        Class<String> mockedProxyType = Mockito.mock(String.class);
        Class<? extends String> typeToMock = String.class;
        Object mockInstance = byteBuddyMockMaker.createMock(settings, handler);

        assertNotNull(mockInstance);
        MockMethodInterceptor mockAccess = (MockMethodInterceptor) mockInstance;
        MockHandler returnedHandler = byteBuddyMockMaker.getHandler(mockInstance);

        assertNotNull(returnedHandler);
        verify(handler).handle(Mockito.any());
        verify(mockAccess).setMockitoInterceptor(Mockito.argThat(i -> i instanceof MockMethodInterceptor.MockAccess));
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeAcrossClassLoaders() throws Exception {
        MockCreationSettings<String> settings = new MockCreationSettings.Builder<String>()
                .serializableMode(SerializableMode.ACROSS_CLASSLOADERS)
                .build();
        MockHandler handler = mock(MockHandler.class);

        byteBuddyMockMaker.createMock(settings, handler);
    }

    @Test
    public void testGetHandler() {
        MockCreationSettings<String> settings = new MockCreationSettings.Builder<String>().build();
        Object mock = byteBuddyMockMaker.createMock(settings, mock(MockHandler.class));
        MockHandler handler = byteBuddyMockMaker.getHandler(mock);

        assertNotNull(handler);
    }

    @Test
    public void testResetMock() {
        MockCreationSettings<String> settings = new MockCreationSettings.Builder<String>().build();
        MockHandler handler = mock(MockHandler.class);
        Object mock = byteBuddyMockMaker.createMock(settings, handler);

        byteBuddyMockMaker.resetMock(mock, mock(MockHandler.class), settings);

        MockHandler newHandler = byteBuddyMockMaker.getHandler(mock);

        assertNotNull(newHandler);
        assertNotNull(newHandler);
        verify(handler).handle(Mockito.never());
        verify(newHandler).handle(Mockito.never());
    }
}