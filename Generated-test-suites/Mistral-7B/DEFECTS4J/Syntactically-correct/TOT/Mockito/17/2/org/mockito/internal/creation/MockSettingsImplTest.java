import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.stubbing.AnswerCalledWith;

import static org.junit.Assert.*;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 }

 @Test
 public void testSerializableMethod() {
 // Test normal usage
 mockSettings = mockSettings.serializable();
 assertTrue(mockSettings.isSerializable());

 // Test null extraInterfaces
 mockSettings = mockSettings.extraInterfaces(null);
 assertFalse(mockSettings.isSerializable());
 new Reporter().extraInterfacesRequiresAtLeastOneInterface();

 // Test empty extraInterfaces
 mockSettings = mockSettings.extraInterfaces(new Class[0]);
 assertFalse(mockSettings.isSerializable());
 new Reporter().extraInterfacesRequiresAtLeastOneInterface();

 // Test non-interface extraInterface
 mockSettings = mockSettings.extraInterfaces(Object.class);
 assertFalse(mockSettings.isSerializable());
 new Reporter().extraInterfacesAcceptsOnlyInterfaces(Object.class);

 // Test valid serializable extraInterface
 mockSettings = mockSettings.extraInterfaces(Serializable.class);
 assertTrue(mockSettings.isSerializable());
 }

 @Test
 public void testNameMethod() {
 mockSettings.name("CustomMockName");
 assertEquals("CustomMockName", mockSettings.getMockName().getName());

 mockSettings.name(null);
 assertNull(mockSettings.getMockName());
 new Reporter().nameCannotBeNull();
 }

 @Test
 public void testSpiedInstanceMethod() {
 // Test setting and getting spiedInstance
 Object spiedInstance = new Object();
 mockSettings.spiedInstance(spiedInstance);
 assertEquals(spiedInstance, mockSettings.getSpiedInstance());

 // Test setting null spiedInstance
 mockSettings.spiedInstance(null);
 assertNull(mockSettings.getSpiedInstance());
 new Reporter().spiedInstanceCannotBeNull();
 }

 @Test
 public void testDefaultAnswerMethod() {
 // Test setting and getting defaultAnswer
 Answer<Object> answer = Mockito.answer(new AnswerCalledWith<Object>("testArg") {
 @Override
 public Object answer(Object arg) {
 return "default answer";
 }
 });
 mockSettings.defaultAnswer(answer);
 assertEquals(answer, mockSettings.getDefaultAnswer());

 // Test setting null defaultAnswer
 mockSettings.defaultAnswer(null);
 assertNull(mockSettings.getDefaultAnswer());
 new Reporter().defaultAnswerCannotBeNull();
 }
}