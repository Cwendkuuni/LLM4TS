package com.example.junit4; // replace with your package name

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.MockitoAnnotations.spy;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.assertNotSame;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Exceptions;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;
 private Class<?>[] extraInterfaces;
 private String name;
 private Object spiedInstance;
 private Answer<Object> defaultAnswer;
 private MockName mockName;

 @Mock
 private Reporter reporter;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 extraInterfaces = new Class[]{Serializable.class}; // replace with your mocked class
 name = "MockName";
 spiedInstance = mock(Object.class);
 defaultAnswer = doAnswer(invocation -> "default answer").when(mockSettings).getDefaultAnswer();
 initMocks(this); // initialize mocks
 }

 @Test
 public void testSerializable() {
 mockSettings.serializable();
 verify(reporter).extraInterfaces(eq(new Class[]{Serializable.class}));
 }

 @Test
 public void testExtraInterfacesNull() {
 try {
 mockSettings.extraInterfaces(null);
 Exceptions.verify(reporter).extraInterfacesRequiresAtLeastOneInterface();
 } catch (Exception e) {
 // do nothing
 }
 }

 @Test
 public void testExtraInterfacesEmpty() {
 try {
 mockSettings.extraInterfaces(new Class[]{});
 Exceptions.verify(reporter).extraInterfacesRequiresAtLeastOneInterface();
 } catch (Exception e) {
 // do nothing
 }
 }

 @Test
 public void testExtraInterfaceNull() {
 try {
 mockSettings.extraInterfaces(new Class[]{null});
 Exceptions.verify(reporter).extraInterfacesDoesNotAcceptNullParameters();
 } catch (Exception e) {
 // do nothing
 }
 }

 @Test
 public void testExtraInterfaceNotInterface() {
 try {
 mockSettings.extraInterfaces(new Class[]{Object.class});
 Exceptions.verify(reporter).extraInterfacesAcceptsOnlyInterfaces(Object.class);
 } catch (Exception e) {
 // do nothing
 }
 }

 @Test
 public void testGetName() {
 mockSettings.name(name);
 assertEquals(name, mockSettings.getMockName().getName());
 }

 @Test
 public void testGetExtraInterfaces() {
 assertEquals(extraInterfaces, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetSpiedInstance() {
 assertEquals(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testGetDefaultAnswer() {
 assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializable() {
 assertTrue(mockSettings.isSerializable());
 }

 @Test
 public void testInitiateMockName() {
 Class<?> classToMock = Mockito.mock(Class.class); // replace with your mocked class
 mockSettings.initiateMockName(classToMock);
 ArgumentCaptor<String> nameArgCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<Class<?>> classToMockArgCaptor = ArgumentCaptor.forClass(Class.class);
 verify(mockName).__init__(nameArgCaptor.capture(), classToMockArgCaptor.capture());
 assertEquals(name, mockName.getName());
 assertEquals(classToMock, mockName.getMockType());
 }

 @Test
 public void testDefaultAnswer() {
 mockSettings.defaultAnswer(doAnswer(invocation -> "test default answer"));
 Object result = mockSettings.getDefaultAnswer().answer(any());
 assertEquals("test default answer", result);
 }

 @Test
 public void testDefaultAnswerNull() {
 try {
 mockSettings.defaultAnswer(null);
 Exceptions.verify(reporter).defaultAnswerRequiresAnswer();
 } catch (Exception e) {
 // do nothing
 }
 }

 @Test
 public void testDefaultAnswerNotAnswer() {
 try {
 mockSettings.defaultAnswer("not an answer");
 Exceptions.verify(reporter).defaultAnswerRequiresAnAnswerObject();
 } catch (Exception e) {
 // do nothing
 }
 }

 @Test
 public void testGetMockName() {
 mockSettings.initiateMockName(Object.class);
 assertEquals(mockName, mockSettings.getMockName());
 }

 @Test
 public void testGetMockNameNull() {
 assertEquals(null, mockSettings.getMockName());
 }

 @Test
 public void testDefaultAnswerTwice() {
 doAnswer(invocation -> "first default answer").when(mockSettings).getDefaultAnswer();
 doAnswer(invocation -> "second default answer").when(mockSettings).getDefaultAnswer();
 Object firstResult = mockSettings.getDefaultAnswer().answer(any());
 Object secondResult = mockSettings.getDefaultAnswer().answer(any());
 assertEquals("first default answer", firstResult);
 assertEquals("second default answer", secondResult);
 }

 @Test
 public void testInitiateMockNameTwice() {
 Class<?> classToMock = Mockito.mock(Class.class); // replace with your mocked class
 mockSettings.initiateMockName(classToMock);
 mockSettings.initiateMockName(classToMock);
 ArgumentCaptor<String> nameArgCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<Class<?>> classToMockArgCaptor = ArgumentCaptor.forClass(Class.class);
 verify(mockName).__init__(nameArgCaptor.capture(), classToMockArgCaptor.capture());
 assertEquals(name, mockName.getName());
 assertEquals(classToMock, mockName.getMockType());
 verifyZeroInteractions(mockName); // verify that no more interactions are performed
 }

 @Test
 public void testInitiateMockNameDifferentName() {
 Class<?> classToMock = Mockito.mock(Class.class); // replace with your mocked class
 mockSettings.initiateMockName("name1");
 mockSettings.initiateMockName(classToMock);
 ArgumentCaptor<String> nameArgCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<Class<?>> classToMockArgCaptor = ArgumentCaptor.forClass(Class.class);
 ArgumentCaptor<String> nameArgCaptor2 = ArgumentCaptor.forClass(String.class);
 verify(mockName).__init__(nameArgCaptor.capture(), classToMockArgCaptor.capture());
 assertEquals("name1", mockName.getName());
 assertEquals(classToMock, mockName.getMockType());
 ArgumentCaptor<String> nameArgCaptor3 = ArgumentCaptor.forClass(String.class);
 verify(mockName).__init__(eq(name), eq(classToMock));
 assertEquals("name", nameArgCaptor3.getValue());
 assertNotSame(nameArgCaptor.getValue(), nameArgCaptor3.getValue());
 }

}