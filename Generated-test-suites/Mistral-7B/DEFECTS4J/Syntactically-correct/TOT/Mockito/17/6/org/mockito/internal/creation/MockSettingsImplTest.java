package org.mockito.internal.creation.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Answer;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.MockSettings;
import org.mockito.internal.creation.MockSettingsImpl;
import static org.junit.Assert.*;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;

 @Before
 public void setup() {
 mockSettings = new MockSettingsImpl();
 }

 @Test
 public void testSerializableMethod() {
 Class<?>[] extraInterfaces = new Class[]{java.io.Serializable.class};
 MockSettings serializableMockSettings = mockSettings.serializable();
 assertEquals(extraInterfaces, serializableMockSettings.getExtraInterfaces());
 assertTrue(serializableMockSettings.isSerializable());
 }

 @Test(expected = IllegalStateException.class)
 public void testExtraInterfacesRequiresAtLeastOneInterface() {
 mockSettings.extraInterfaces(new Class[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExtraInterfacesAcceptsOnlyInterfaces() {
 mockSettings.extraInterfaces(String.class);
 }

 @Test
 public void testGetMockName() {
 String name = "mockName";
 MockSettings nameMockSettings = mockSettings.name(name);
 MockName mockName = nameMockSettings.getMockName();
 assertEquals(name, mockName.getName());
 }

 @Test
 public void testGetExtraInterfaces() {
 Class<?>[] extraInterfaces = new Class[]{java.io.Serializable.class, java.lang.Runnable.class};
 MockSettings extraInterfacesMockSettings = mockSettings.extraInterfaces(extraInterfaces);
 assertArrayEquals(extraInterfaces, extraInterfacesMockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetSpiedInstance() {
 Object spiedInstance = new Object();
 MockSettings spiedInstanceMockSettings = mockSettings.spiedInstance(spiedInstance);
 assertEquals(spiedInstance, spiedInstanceMockSettings.getSpiedInstance());
 }

 @Test
 public void testDefaultAnswer() {
 Answer<Object> answer = Mockito.mock(Answer.class);
 MockSettings defaultAnswerMockSettings = mockSettings.defaultAnswer(answer);
 assertEquals(answer, defaultAnswerMockSettings.getDefaultAnswer());
 }

 @Test
 public void testSerializableWithNullName() {
 try {
 mockSettings.serializable().name(null);
 fail("Expecting Reporter.nameRequiresNonnullArgument() to be thrown");
 } catch (IllegalStateException e) {
 assertEquals("name requires non-null argument", e.getMessage());
 }
 }

 @Test
 public void testIsSerializableWithEmptyExtraInterfaces() {
 MockSettings mockSettingsWithEmptyExtraInterfaces = mockSettings.serializable();
 assertFalse(mockSettingsWithEmptyExtraInterfaces.isSerializable());
 }

 @Test
 public void testInitiateMockName() {
 Class<?> classToMock = Mockito.class;
 MockSettings mockSettingsWithInitiatedMockName = mockSettings.initiateMockName(classToMock);
 assertNotNull(mockSettingsWithInitiatedMockName.getMockName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitiateMockNameWithNullClassToMock() {
 mockSettings.initiateMockName(null);
 }
}