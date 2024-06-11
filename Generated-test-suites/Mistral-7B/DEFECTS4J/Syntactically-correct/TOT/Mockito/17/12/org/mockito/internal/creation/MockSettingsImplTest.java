import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.Mockito;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettingsImpl;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;

 @Test
 public void testSerializableMethod() {
 mockSettings = new MockSettingsImpl();
 // typical use case
 mockSettings.serializable();
 assertTrue(mockSettings.isSerializable());
 // edge case: passing an empty array
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(new Class[0]);
 assertTrue(mockSettings.isSerializable());
 // error scenario: passing a non-serializable class
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(String.class);
 assertFalse(mockSettings.isSerializable());
 // error scenario: passing a null class
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(null);
 new Reporter().extraInterfacesDoesNotAcceptNullParameters();
 }

 @Test
 public void testExtraInterfacesMethod() {
 mockSettings = new MockSettingsImpl();
 // typical use case
 mockSettings.extraInterfaces(java.io.Serializable.class);
 assertEquals(java.io.Serializable.class, mockSettings.getExtraInterfaces()[0]);
 // edge case: passing an empty array
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(new Class[0]);
 assertNull(mockSettings.getExtraInterfaces());
 // error scenario: passing a non-interface class
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(String[].class);
 new Reporter().extraInterfacesAcceptsOnlyInterfaces(String[].class);
 // error scenario: passing a null interface
 mockSettings = new MockSettingsImpl();
 mockSettings.extraInterfaces(null);
 new Reporter().extraInterfacesDoesNotAcceptNullParameters();
 }

 @Test
 public void testDefaultAnswerMethod() {
 mockSettings = new MockSettingsImpl();
 // typical use case
 Answer<Object> answer = Mockito.answer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "Mock answer";
 }
 });
 mockSettings.defaultAnswer(answer);
 Object obj = Mockito.spy(new Object());
 assertEquals("Mock answer", mockSettings.getDefaultAnswer().answer(Mockito.mock(Object.class)));
 // edge case: passing a null answer
 mockSettings = new MockSettingsImpl();
 mockSettings.defaultAnswer(null);
 try {
 mockSettings.getDefaultAnswer().answer(Mockito.mock(Object.class));
 fail("Expected Reporter.defaultAnswerRequiresAnswerObject");
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void testNameMethod() {
 mockSettings = new MockSettingsImpl();
 // typical use case
 mockSettings.name("testName");
 assertEquals("testName", mockSettings.getMockName().getName());
 // edge case: passing a null name
 mockSettings = new MockSettingsImpl();
 mockSettings.name(null);
 new Reporter().nameCannotBeNull();
 }

 @Test
 public void testInitiateMockNameMethod() {
 Class testClass = Mockito.class;
 mockSettings = new MockSettingsImpl();
 // typical use case
 mockSettings.initiateMockName(testClass);
 assertEquals("Mockito", mockSettings.getMockName().getName());
 // edge case: passing a null class
 mockSettings = new MockSettingsImpl();
 mockSettings.initiateMockName(null);
 new Reporter().mockNameClassCannotBeNull();
 }
}