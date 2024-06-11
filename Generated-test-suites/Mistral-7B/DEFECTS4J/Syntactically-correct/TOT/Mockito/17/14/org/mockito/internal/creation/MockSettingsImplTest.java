import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoInitializationException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.util.Arrays;

import static org.junit.Assert.*;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;

 @Before
 public void setup() {
 mockSettings = new MockSettingsImpl();
 }

 @Test
 public void testSerializable() {
 // Happy path
 mockSettings.serializable();
 Mockito.verify(mockSettings).extraInterfaces(Serializable.class);

 // Edge case: null extraInterfaces
 mockSettings.extraInterfaces(null);
 try {
 mockSettings.serializable();
 fail("Expected MockitoInitializationException");
 } catch (MockitoInitializationException e) {
 assertEquals("extraInterfaces requires at least one interface", e.getMessage());
 }

 // Error case: non-interface class
 mockSettings.extraInterfaces(String.class);
 try {
 mockSettings.serializable();
 fail("Expected MockitoInitializationException");
 } catch (MockitoInitializationException e) {
 assertEquals("extraInterfacesAcceptsOnlyInterfaces", e.getMessage());
 }
 }

 @Test
 public void testExtraInterfaces() {
 // Happy path
 Class<?>[] extraInterfaces = new Class[] {Serializable.class};
 mockSettings.extraInterfaces(extraInterfaces);
 Mockito.verify(mockSettings).extraInterfaces(ArgumentMatchers.arrayContaining(Serializable.class));

 // Edge case: null extraInterfaces
 mockSettings.extraInterfaces(null);
 try {
 mockSettings.extraInterfaces(new Class[]{Serializable.class});
 fail("Expected MockitoInitializationException");
 } catch (MockitoInitializationException e) {
 assertEquals("extraInterfaces requires at least one interface", e.getMessage());
 }

 // Edge case: empty extraInterfaces array
 mockSettings.extraInterfaces(new Class[] {});
 Mockito.verify(mockSettings).extraInterfaces(new Class[] {});

 // Error case: non-interface class in extraInterfaces
 mockSettings.extraInterfaces(new Class[] {String.class, Serializable.class});
 try {
 mockSettings.serializable();
 fail("Expected MockitoInitializationException");
 } catch (MockitoInitializationException e) {
 assertEquals("extraInterfacesAcceptsOnlyInterfaces", e.getMessage());
 }
 }

 @Test
 public void testGetExtraInterfaces() {
 // Happy path
 Class<?>[] extraInterfaces = new Class[] {Serializable.class};
 mockSettings.extraInterfaces(extraInterfaces);
 assertArrayEquals(new Class[] {Serializable.class}, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetSpiedInstance() {
 // Happy path
 Object spiedInstance = new Object();
 mockSettings.spiedInstance(spiedInstance);
 assertEquals(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testName() {
 // Happy path
 String name = "testName";
 mockSettings.name(name);
 assertEquals(name, mockSettings.getMockName().getName());
 }

 @Test
 public void testDefaultAnswer() {
 // Happy path
 Answer<Object> answer = Mockito.answer(Mockito.mock(String.class));
 mockSettings.defaultAnswer(answer);
 assertEquals(answer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializable() {
 // Happy path
 mockSettings.serializable();
 assertTrue(mockSettings.isSerializable());

 // Edge case: no extraInterfaces
 mockSettings = new MockSettingsImpl();
 assertFalse(mockSettings.isSerializable());
 }

 @Test
 public void testInitiateMockName() {
 // Happy path
 Class<?> classToMock = Mockito.class;
 mockSettings.initiateMockName(classToMock);
 assertNotNull(mockSettings.getMockName());
 }
}