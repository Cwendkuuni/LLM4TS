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
import org.mockito.plugins.MockMaker.MockMakerType;
import static org.mockito.internal.util.StringJoiner.join;
import static org.mockito.Mockito.*;
import static org.mockito.internal.util.reflection.FieldAccessUtils.getFieldValue;
import static org.mockito.internal.util.reflection.ReflectionUtils.callConstructor;
import static org.mockito.internal.util.reflection.ReflectionUtils.getMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private GlobalConfiguration globalConfiguration;
    private ClassInstantiator classInstantiator;
    private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;

    @Before
    public void setUp() {
        globalConfiguration = new GlobalConfiguration();
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        classInstantiator = getFieldValue(byteBuddyMockMaker, "classInstantiator");
        cachingMockBytecodeGenerator = getFieldValue(byteBuddyMockMaker, "cachingMockBytecodeGenerator");
    }

    @Test
    public void testCreateMockWithValidSettingsAndHandler() {
        MockCreationSettings<Object> settings = MockCreationSettings.builder().typeToMock(Object.class).build();
        MockHandler handler = mock(MockHandler.class);

        Object mock = byteBuddyMockMaker.createMock(settings, handler);

        assertEquals(Object.class, mock.getClass());
        assertNotNull(byteBuddyMockMaker.getHandler(mock));
    }

    @Test
    public void testCreateMockWithInvalidSerializableMode() {
        MockCreationSettings<Object> settings = MockCreationSettings.builder().typeToMock(Object.class).serializableMode(SerializableMode.ACROSS_CLASSLOADERS).build();

        assertThrows(MockitoException.class, () -> byteBuddyMockMaker.createMock(settings, mock(MockHandler.class)));
    }

    @Test
    public void testCreateMockWithNullTypeToMock() {
        MockCreationSettings<Object> settings = MockCreationSettings.builder().typeToMock(null).build();

        assertThrows(NullPointerException.class, () -> byteBuddyMockMaker.createMock(settings, mock(MockHandler.class)));
    }

    @Test
    public void testCreateMockWithInvalidExtraInterfaces() {
        Class<?> invalidInterface = null;

        MockCreationSettings<Object> settings = MockCreationSettings.builder()
                .typeToMock(Object.class)
                .extraInterfaces(invalidInterface)
                .build();

        assertThrows(NullPointerException.class, () -> byteBuddyMockMaker.createMock(settings, mock(MockHandler.class)));
    }

    @Test
    public void testCreateMockWithClassCastException() {
        MockCreationSettings<Object> settings = MockCreationSettings.builder()
                .typeToMock(String.class)
                .build();

        assertThrows(ClassCastException.class, () -> byteBuddyMockMaker.createMock(settings, mock(MockHandler.class)));
    }

    @Test
    public void testCreateMockWithInstantiationException() {
        MockCreationSettings<Object> settings = MockCreationSettings.builder()
                .typeToMock(IllegalArgumentException.class)
                .build();

        assertThrows(MockitoException.class, () -> byteBuddyMockMaker.createMock(settings, mock(MockHandler.class)));
    }

    @Test
    public void testGetHandler() {
        Object mock = mock(Object.class);

        MockHandler handler = byteBuddyMockMaker.getHandler(mock);

        assertNull(handler);
    }

    @Test
    public void testResetMockWithNullMock() {
        Object mock = mock(Object.class);

        byteBuddyMockMaker.resetMock(mock, mock(MockHandler.class), MockCreationSettings.builder().build());

        assertNull(byteBuddyMockMaker.getHandler(mock));
    }

    @Test
    public void testInitializeClassInstantiator() {
        MockMaker mockMaker = mock(MockMaker.class);
        when(mockMaker.getTypeMaker()).thenReturn(mock(TypeMaker.class));

        ByteBuddyMockMaker byteBuddyMockMakerSpy = spy(new ByteBuddyMockMaker());
        doReturn(mockMaker).when(byteBuddyMockMakerSpy).getMockMaker();

        Class<?> classLoaderClass = Class.forName("org.mockito.internal.creation.bytebuddy.ClassLoaderMockMaker");
        Constructor<ClassLoaderMockMaker> classLoaderMockMakerConstructor = getMethod(ClassLoaderMockMaker.class, "newInstance").getConstructor(MockMaker.class);
        ClassLoaderMockMaker classLoaderMockMaker = callConstructor(classLoaderMockMakerConstructor, byteBuddyMockMakerSpy);

        byteBuddyMockMakerSpy.initializeClassInstantiator();

        assertEquals(ClassInstantiator.class, classInstantiator.getClass());
        assertEquals(classLoaderMockMaker, classInstantiator);
    }

    @Test
    public void testAsInternalMockHandler() {
        MockHandler mockHandler = mock(MockHandler.class);

        InternalMockHandler internalMockHandler = byteBuddyMockMaker.asInternalMockHandler(mockHandler);

        assertEquals(mockHandler, internalMockHandler);
    }
}