package com.example.junit4; // replace with your package name

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Runners;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(Runners.JUnit4.class)
public class MockSettingsImplTest {

 private MockSettings mockSettings;
 @Mock private Reporter reporter;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 Mockito.spy(Mockito.mock(Class.class)); // initialize mockName in MockSettingsImpl
 }

 @Test
 public void testSerializable() {
 mockSettings.serializable();
 verify(reporter).extraInterfacesRequiresAtLeastOneInterface();
 verify(reporter, Mockito.never()).extraInterfacesAcceptsOnlyInterfaces(ArgumentMatchers.any());
 verify(reporter, Mockito.never()).extraInterfacesDoesNotAcceptNullParameters();
 }

 @Test(expected = AssertionError.class)
 public void testSerializableWithNullExtraInterfaces() {
 mockSettings.extraInterfaces(null);
 }

 @Test
 public void testSerializableWithInvalidExtraInterface() {
 mockSettings.extraInterfaces(new Object() {}.getClass());
 }

 @Test
 public void testSerializableWithValidExtraInterface() {
 Class<?>[] validExtraInterfaces = new Class[]{Serializable.class};
 mockSettings.extraInterfaces(validExtraInterfaces);
 verify(mockSettings).isSerializable();
 }

 @Test
 public void testName() {
 String name = "testName";
 mockSettings.name(name);
 assertEquals(name, mockSettings.getMockName());
 }

 @Test
 public void testSpiedInstance() {
 Object spiedInstance = new Object();
 mockSettings.spiedInstance(spiedInstance);
 assertEquals(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testDefaultAnswer() {
 Answer<Object> answer = Mockito.mock(Answer.class);
 mockSettings.defaultAnswer(answer);
 assertEquals(answer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializable() {
 mockSettings.extraInterfaces(Serializable.class);
 assertTrue(mockSettings.isSerializable());
 }

 @Test
 public void testInitiateMockName() {
 Class<?> classToMock = Mockito.mock(Class.class);
 mockSettings.initiateMockName(classToMock);
 assertNotNull(mockSettings.getMockName());
 }
}