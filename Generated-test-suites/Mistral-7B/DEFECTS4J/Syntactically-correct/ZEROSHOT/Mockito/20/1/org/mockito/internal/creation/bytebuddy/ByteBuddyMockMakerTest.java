package org.mockito.internal.creation.bytebuddy;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.ExactInvocationMatcher;
import org.mockito.MockHandler;
import org.mockito.Mockito;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMaker.MockMakerAnnotations;
import org.mockito.plugins.MockMakerAnnotations.SerializableMode;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private ClassInstantiator classInstantiator;
    private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;

    @Rule
    public MockMaker mockMaker = new MockitoJUnitRunner.MockMaker(MockMakerAnnotations.withSerializableAnnotations());

    @Before
    public void setUp() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        classInstantiator = Mockito.spy(byteBuddyMockMaker.initializeClassInstantiator());
        cachingMockBytecodeGenerator = mock(CachingMockBytecodeGenerator.class);
        byteBuddyMockMaker = new ByteBuddyMockMaker() {
            @Override
            private CachingMockBytecodeGenerator cachingMockBytecodeGenerator() {
                return cachingMockBytecodeGenerator;
            }

            @Override
            private ClassInstantiator classInstantiator() {
                return classInstantiator;
            }
        };
    }

    @Test
    public void testCreateMockWithSerializableModeAcrossClassLoaders() {
        MockHandler handler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = MockMakerAnnotations.withSettings(Object.class, SerializableMode.ACROSS_CLASSLOADERS);

        try {
            byteBuddyMockMaker.createMock(settings, handler);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            // Expected
        }
    }

    @Test
    public void testCreateMockWithValidSerializableMode() {
        MockHandler handler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = MockMakerAnnotations.withSettings(Object.class, SerializableMode.MOCKITO_DEFAULT);

        Object mock = byteBuddyMockMaker.createMock(settings, handler);

        verify(cachingMockBytecodeGenerator).get(ArgumentMatchers.eq(Object.class), ArgumentMatchers.array(new Class[0]));
        verify(classInstantiator).instantiate(ArgumentMatchers.eq(Object.class));
        ArgumentCaptor<MockMethodInterceptor.MockAccess> mockAccessCaptor = ArgumentCaptor.forClass(MockMethodInterceptor.MockAccess.class);
        verify(mockAccessCaptor.capture(mock)).setMockitoInterceptor(ArgumentMatchers.instanceOf(MockMethodInterceptor.class));
    }

    @Test
    public void testCreateMockWithExistingMockHandler() {
        MockHandler handler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = MockMakerAnnotations.withSettings(Object.class);

        Object mock = byteBuddyMockMaker.createMock(settings, handler);

        verify(cachingMockBytecodeGenerator).get(ArgumentMatchers.eq(Object.class), ArgumentMatchers.array(new Class[0]));
        verify(classInstantiator).instantiate(ArgumentMatchers.eq(Object.class));
        ArgumentCaptor<MockMethodInterceptor.MockAccess> mockAccessCaptor = ArgumentCaptor.forClass(MockMethodInterceptor.MockAccess.class);
        verify(mockAccessCaptor.capture(mock)).setMockitoInterceptor(ArgumentMatchers.instanceOf(MockMethodInterceptor.class));
        verify(handler).handle(ArgumentMatchers.any());
    }

    @Test
    public void testCreateMockWithException() {
        MockHandler handler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = MockMakerAnnotations.withSettings(Object.class);

        when(classInstantiator.instantiate(ArgumentMatchers.eq(Object.class)))
                .thenThrow(new InstantiationException("Exception message"));

        try {
            byteBuddyMockMaker.createMock(settings, handler);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            Mockito.verify(handler).handle(ArgumentMatchers.any());
            String expectedMessage = "Unable to create mock instance of type 'java.lang.Object'";
            String actualMessage = e.getMessage();
            assert(expectedMessage.contains(actualMessage));
        }
    }

    @Test
    public void testCreateMockWithInstantiationExceptionCausingClassCastException() {
        MockHandler handler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = MockMakerAnnotations.withSettings(Object.class);

        when(classInstantiator.instantiate(ArgumentMatchers.eq(Object.class)))
                .thenThrow(new InstantiationException(new ClassCastException("ClassCastException message")));

        try {
            byteBuddyMockMaker.createMock(settings, handler);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            String expectedMessage = join(
                    "ClassCastException occurred while creating the mockito mock :",
                    "  class to mock : Object', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@1b6d3523'",
                    "  created class : 'java.lang.Object', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@1b6d3523'",
                    "  proxy instance class : 'org.mockito.internal.creation.bytebuddy.ByteBuddyMock$3'",
                    "  instance creation by : ClassInstantiator",
                    "",
                    "You might experience classloading issues, please ask the mockito mailing-list.",
                    ""
            );
            String actualMessage = e.getMessage();
            assert(expectedMessage.contains(actualMessage));
        }
    }

    @Test
    public void testGetHandler() {
        Object mock = mock(Object.class);

        MockHandler handler = byteBuddyMockMaker.getHandler(mock);

        assertNull(handler);
    }

    @Test
    public void testResetMock() {
        Object mock = mock(Object.class);
        MockHandler handler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = MockMakerAnnotations.withSettings(Object.class);

        byteBuddyMockMaker.resetMock(mock, handler, settings);

        verify(handler).handle(ArgumentMatchers.any());
    }

    @Test
    public void testDescribeClass() {
        Class<?> mockType = Mockito.mock(Class.class);
        Object mockInstance = mock(Object.class);

        String expectedClassDescription = "class 'java.lang.Object', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@1b6d3523'";
        String expectedInstanceClassDescription = describeClass(mockType);
        String expectedMockInstanceClassDescription = describeClass(mockInstance);

        assertEquals(expectedClassDescription, byteBuddyMockMaker.describeClass(mockType));
        assertEquals(expectedMockInstanceClassDescription, byteBuddyMockMaker.describeClass(mockInstance));
    }

    @Test
    public void testInitializeClassInstantiator() {
        try {
            byteBuddyMockMaker.initializeClassInstantiator();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            String expectedMessage = "Mockito could not create mock: Objenesis is missing on the classpath.";
            String actualMessage = e.getMessage();
            assert(expectedMessage.contains(actualMessage));
        }

        MockHandler handler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = MockMakerAnnotations.withSettings(Object.class);

        byteBuddyMockMaker = new ByteBuddyMockMaker() {
            @Override
            private ClassInstantiator initializeClassInstantiator() {
                return new ClassInstantiator();
            }
        };

        Object mock = byteBuddyMockMaker.createMock(settings, handler);

        verify(handler).handle(ArgumentMatchers.any());
    }
}