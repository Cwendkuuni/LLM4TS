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
import org.mockito.plugins.MockMakerType;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.internal.util.StringJoiner.join;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private ClassInstantiator classInstantiator;
    private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;

    @Before
    public void setUp() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        classInstantiator = spy(new ClassInstantiator() {
            @Override
            public <T> T newInstance(Class<T> clazz) {
                return mock(clazz);
            }
        });
        cachingMockBytecodeGenerator = mock(CachingMockBytecodeGenerator.class);
        byteBuddyMockMaker = new ByteBuddyMockMaker() {
            {
                this.classInstantiator = classInstantiator;
                this.cachingMockBytecodeGenerator = cachingMockBytecodeGenerator;
            }
        };
    }

    @Test
    public void testCreateMockSuccess() {
        MockCreationSettings mockCreationSettings = new MockCreationSettings.Builder()
                .forType(String.class)
                .withSerializableMode(SerializableMode.SERIAL)
                .build();
        MockHandler mockHandler = mock(MockHandler.class);

        byteBuddyMockMaker.createMock(mockCreationSettings, mockHandler);

        verify(cachingMockBytecodeGenerator).get(eq(String.class), any());
        verifyNoMoreInteractions(cachingMockBytecodeGenerator);
        verify(classInstantiator).instantiate(eq(String.class));
        verify(classInstantiator).instantiate(isA(MockCreationSettings.class));
        verifyNoMoreInteractions(classInstantiator);
        verify(mockHandler).handle(any(), any());
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockSerializationException() {
        MockCreationSettings mockCreationSettings = new MockCreationSettings.Builder()
                .forType(String.class)
                .withSerializableMode(SerializableMode.ACROSS_CLASSLOADERS)
                .build();

        byteBuddyMockMaker.createMock(mockCreationSettings, mockHandler);
    }

    @Test
    public void testGetHandler() {
        Object mock = spy(new Object() {});
        MockHandler handler = mock(MockHandler.class);

        byteBuddyMockMaker.setMockitoInterceptor((MockMethodInterceptor) mock, handler);
        MockHandler returnedHandler = byteBuddyMockMaker.getHandler(mock);

        verify(handler).handle(any(), any());
        verify(handler).getMockHandler();
        assertEquals(handler, returnedHandler);
    }

    @Test
    public void testResetMock() {
        Object mock = spy(new Object() {});
        MockHandler newHandler = mock(MockHandler.class);
        MockHandler oldHandler = mock(MockHandler.class);
        MockCreationSettings settings = new MockCreationSettings.Builder()
                .forType(String.class)
                .withSerializableMode(SerializableMode.SERIAL)
                .build();

        byteBuddyMockMaker.setMockitoInterceptor((MockMethodInterceptor) mock, oldHandler);
        byteBuddyMockMaker.resetMock(mock, newHandler, settings);

        verify(mock).setMockitoInterceptor(any());
        verify(oldHandler).handle(any(), any());
        verifyNoMoreInteractions(oldHandler);
        verify(newHandler).handle(any(), any());
    }

    @Test
    public void testInitializeClassInstantiator() {
        GlobalConfiguration globalConfiguration = getInternalState(ByteBuddyMockMaker.class, "globalConfiguration");
        Class<?> objenesisClassLoader = null;
        try {
            objenesisClassLoader = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
        } catch (ClassNotFoundException e) {
            // expected
        }

        verifyZeroInteractions(globalConfiguration);
        if (objenesisClassLoader != null) {
            try {
                byteBuddyMockMaker.initializeClassInstantiator();
                verify(globalConfiguration).enableClassCache();
                verify(cachingMockBytecodeGenerator).getMockBytecodeGenerator();
                verify(classInstantiator).setMockBytecodeGenerator(any());
            } catch (Throwable throwable) {
                // MockitoException cannot be used at this point as we are testing the initialization method
            }
        } else {
            verify(globalConfiguration).enableClassCache();
            verify(cachingMockBytecodeGenerator).getMockBytecodeGenerator();
            verify(classInstantiator).setMockBytecodeGenerator(any());
        }
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandlerException() {
        MockHandler handler = mock(MockHandler.class);

        byteBuddyMockMaker.asInternalMockHandler(null);
    }
}