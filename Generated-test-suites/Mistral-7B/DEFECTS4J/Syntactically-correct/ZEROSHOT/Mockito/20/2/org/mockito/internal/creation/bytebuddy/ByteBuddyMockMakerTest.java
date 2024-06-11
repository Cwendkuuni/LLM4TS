package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;

public class ByteBuddyMockMakerTest {

 private ByteBuddyMockMaker byteBuddyMockMaker;
 private Class<Foo> typeToMock;
 private Class<Bar> extraInterface;
 private MockHandler mockHandler;

 @Before
 public void setUp() {
 byteBuddyMockMaker = new ByteBuddyMockMaker();
 typeToMock = Foo.class;
 extraInterface = Bar.class;
 mockHandler = mock(MockHandler.class);
 }

 @Test
 public void testCreateMockWithSerializableModeAcrossClassLoaders() {
 try {
 byteBuddyMockMaker.createMock(
 new MockCreationSettings<>(typeToMock, extraInterface, SerializableMode.ACROSS_CLASSLOADERS, null), mockHandler);
 fail("Expected MockitoException");
 } catch (MockitoException e) {
 // Expected
 }
 }

 @Test
 public void testCreateMock() {
 Foo mockedProxy = byteBuddyMockMaker.createMock(new MockCreationSettings<>(typeToMock, extraInterface, null, mockHandler));
 assertNotNull(mockedProxy);
 assertEquals(typeToMock, mockedProxy.getClass().getInterfaces()[0]);
 assertEquals(mockHandler, byteBuddyMockMaker.getHandler(mockedProxy));
 }

 @Test
 public void testCreateMockWithSameClassLoader() {
 Class<Baz> anotherTypeToMock = Baz.class;
 Baz anotherMockedProxy = byteBuddyMockMaker.createMock(new MockCreationSettings<>(anotherTypeToMock, extraInterface, null, mockHandler));
 assertNotNull(anotherMockedProxy);
 assertEquals(anotherTypeToMock, anotherMockedProxy.getClass().getInterfaces()[0]);
 assertNotSame(mockedProxy, anotherMockedProxy);
 }

 @Test
 public void testResetMock() {
 Foo mockedProxy = byteBuddyMockMaker.createMock(new MockCreationSettings<>(typeToMock, extraInterface, null, mockHandler));
 byteBuddyMockMaker.resetMock(mockedProxy, mockHandler, new MockCreationSettings<>(typeToMock, extraInterface, null, null));
 assertEquals(mockHandler, byteBuddyMockMaker.getHandler(mockedProxy));
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockWithNonSerializableModeAndClassCastException() {
 Class<?> wrongTypeToMock = Object.class;
 byteBuddyMockMaker.createMock(new MockCreationSettings<>(wrongTypeToMock, extraInterface, null, mockHandler));
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockWithNonSerializableModeAndInstantiationException() {
 Class<?> wrongTypeToMock = Foo.class;
 when(mockHandler.supportsCreation()).thenReturn(false);
 byteBuddyMockMaker.createMock(new MockCreationSettings<>(wrongTypeToMock, extraInterface, null, mockHandler));
 }

 @Test
 public void testInitializeClassInstantiator() {
 GlobalConfiguration globalConfiguration = new GlobalConfiguration();
 globalConfiguration.enableClassCache();
 Class<ClassInstantiator> classInstantiatorClass = ClassInstantiator.class;
 assertEquals(classInstantiatorClass.cast(byteBuddyMockMaker.initializeClassInstantiator()), byteBuddyMockMaker.classInstantiator);
 }
}

class Foo {
}

class Bar {
}

class Baz {
}