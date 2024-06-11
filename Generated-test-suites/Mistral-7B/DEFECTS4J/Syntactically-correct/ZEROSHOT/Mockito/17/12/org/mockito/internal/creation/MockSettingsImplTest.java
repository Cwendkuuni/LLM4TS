package com.example.junit4; // replace with your package name

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;
 private String name;
 private Class<?>[] extraInterfaces;
 private Class<?> invalidClass;

 @Before
 public void setUp() {
 name = "MockName";
 extraInterfaces = new Class[]{Serializable.class};
 mockSettings = new MockSettingsImpl();
 }

 @Test
 public void serializable_shouldReturnThisInstance() {
 MockSettings actual = mockSettings.serializable();
 assertEquals(mockSettings, actual);
 }

 @Test
 public void serializable_shouldSetExtraInterfacesWithSerializable() {
 mockSettings = mockSettings.serializable();
 assertArrayEquals(new Class[]{Serializable.class}, mockSettings.getExtraInterfaces());
 }

 @Test
 public void extraInterfaces_shouldThrowExceptionWhenEmptyArrayIsProvided() {
 assertThrows(MockitoException.class, () -> mockSettings.extraInterfaces(new Class[0]));
 }

 @Test
 public void extraInterfaces_shouldAcceptInterfaceClasses() {
 mockSettings = mockSettings.extraInterfaces(Serializable.class, Serializable.class);
 assertArrayEquals(new Class[]{Serializable.class, Serializable.class}, mockSettings.getExtraInterfaces());
 }

 @Test
 public void extraInterfaces_shouldThrowExceptionWhenNullIsProvided() {
 assertThrows(MockitoException.class, () -> mockSettings.extraInterfaces(null));
 }

 @Test
 public void extraInterfaces_shouldThrowExceptionWhenNonInterfaceIsProvided() {
 invalidClass = new Object() {};
 assertThrows(MockitoException.class, () -> mockSettings.extraInterfaces(invalidClass));
 }

 @Test
 public void extraInterfaces_shouldAcceptMultipleInterfaceClasses() {
 mockSettings = mockSettings.extraInterfaces(Serializable.class, Serializable.class, Serializable.class);
 assertEquals(3, mockSettings.getExtraInterfaces().length);
 }

 @Test
 public void getName_shouldReturnExpectedName() {
 assertEquals(name, mockSettings.getName());
 }

 @Test
 public void getSpiedInstance_shouldReturnExpectedInstance() {
 Object spiedInstance = new Object();
 mockSettings = mockSettings.spiedInstance(spiedInstance);
 assertEquals(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test
 public void getDefaultAnswer_shouldReturnExpectedAnswer() {
 Answer<Object> answer = mock(Answer.class);
 mockSettings = mockSettings.defaultAnswer(answer);
 assertEquals(answer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void getDefaultAnswer_shouldReturnNullByDefault() {
 assertNull(mockSettings.getDefaultAnswer());
 }

 @Test
 public void isSerializable_shouldReturnTrueWhenExtraInterfacesContainsSerializable() {
 mockSettings = mockSettings.serializable();
 assertTrue(mockSettings.isSerializable());
 }

 @Test
 public void isSerializable_shouldReturnFalseWhenExtraInterfacesDoesNotContainSerializable() {
 mockSettings = mockSettings.extraInterfaces(new Class[0]);
 assertFalse(mockSettings.isSerializable());
 }

 @Test
 public void initiateMockName_shouldCreateMockNameInstance() {
 Class<?> classToMock = Mockito.class;
 mockSettings.initiateMockName(classToMock);
 verify(mockSettings).getName();
 verify(mockSettings).getName().equals(name);
 verify(mockSettings).getName().getMockName().equals(name + "_" + classToMock.getName());
 }

 @Test(expected = MockitoException.class)
 public void initiateMockName_shouldThrowExceptionWhenClassToMockIsNull() {
 mockSettings.initiateMockName(null);
 }
}