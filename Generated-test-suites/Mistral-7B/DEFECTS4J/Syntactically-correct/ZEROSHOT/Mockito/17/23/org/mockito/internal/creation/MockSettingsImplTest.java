package com.example.junit4; // replace with your package name

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.internal.creation.MockSettings;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;

import java.io.Serializable;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;
 private Class<Serializable> serializableInterface;
 private Class<String> stringClass;
 private Class<Object> objectClass;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 serializableInterface = Serializable.class;
 stringClass = String.class;
 objectClass = Object.class;
 }

 @Test
 public void testSerializable() {
 // given
 MockSettings serializableSettings = mockSettings.serializable();

 // when
 boolean isSerializable = serializableSettings.isSerializable();
 Class<?>[] extraInterfaces = serializableSettings.getExtraInterfaces();

 // then
 assertEquals(serializableInterface, Arrays.stream(extraInterfaces).filter(clazz -> clazz.equals(serializableInterface)).findFirst().orElse(null));
 assertTrue(isSerializable);
 }

 @Test(expected = AssertionError.class)
 public void testExtraInterfacesNull() {
 // given
 Class<?>[] nullExtraInterfaces = null;
 // when
 mockSettings.extraInterfaces(nullExtraInterfaces);

 // then
 fail("Expected Reporter.extraInterfacesRequiresAtLeastOneInterface to be thrown");
 }

 @Test(expected = AssertionError.class)
 public void testExtraInterfacesNullClass() {
 // given
 Class<?> nullClass = null;
 Class<?>[] extraInterfaces = new Class[] { nullClass };
 // when
 mockSettings.extraInterfaces(extraInterfaces);

 // then
 fail("Expected Reporter.extraInterfacesDoesNotAcceptNullParameters to be thrown");
 }

 @Test(expected = AssertionError.class)
 public void testExtraInterfacesNonInterface() {
 // given
 Class<Integer> integerClass = Integer.class;
 Class<?>[] extraInterfaces = new Class[] { integerClass };
 // when
 mockSettings.extraInterfaces(extraInterfaces);

 // then
 fail("Expected Reporter.extraInterfacesAcceptsOnlyInterfaces to be thrown");
 }

 @Test
 public void testGetName() {
 // given
 String name = "MockName";
 MockSettings nameSettings = mockSettings.name(name);

 // when
 String actualName = nameSettings.getMockName().getName();

 // then
 assertEquals(name, actualName);
 }

 @Test
 public void testGetExtraInterfaces() {
 // given
 Class<?>[] extraInterfaces = new Class[] { stringClass, objectClass };
 MockSettings extraInterfacesSettings = mockSettings.extraInterfaces(extraInterfaces);

 // when
 Class<?>[] actualExtraInterfaces = extraInterfacesSettings.getExtraInterfaces();

 // then
 assertArrayEquals(extraInterfaces, actualExtraInterfaces);
 }

 @Test
 public void testGetSpiedInstance() {
 // given
 Object spiedInstance = mock(Object.class);
 MockSettings spiedInstanceSettings = mockSettings.spiedInstance(spiedInstance);

 // when
 Object actualSpiedInstance = spiedInstanceSettings.getSpiedInstance();

 // then
 assertSame(spiedInstance, actualSpiedInstance);
 }

 @Test
 public void testGetDefaultAnswer() {
 // given
 Answer<Object> defaultAnswer = mock(Answer.class);
 MockSettings defaultAnswerSettings = mockSettings.defaultAnswer(defaultAnswer);

 // when
 Answer<Object> actualDefaultAnswer = defaultAnswerSettings.getDefaultAnswer();

 // then
 assertSame(defaultAnswer, actualDefaultAnswer);
 }

 @Test
 public void testIsSerializable() {
 // given
 MockSettings serializableSettings = mockSettings.serializable();
 MockSettings nonSerializableSettings = mockSettings.serializable();

 // when
 // then
 assertFalse(nonSerializableSettings.isSerializable());
 assertTrue(serializableSettings.isSerializable());
 }

 @Test
 public void testInitiateMockName() {
 // given
 Class<MockSettingsImpl> classToMock = MockSettingsImpl.class;
 MockSettings settings = mockSettings.serializable();

 // when
 settings.initiateMockName(classToMock);

 // then
 assertNotNull(settings.getMockName());
 }
}