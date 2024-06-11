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

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;

 @Before
 public void before() {
 mockSettings = new MockSettingsImpl();
 }

 @Test
 public void testSerializable() {
 mockSettings = mockSettings.serializable();
 Class<?>[] expected = new Class[] { Serializable.class };
 assertEquals(expected, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testExtraInterfacesNull() {
 assertThrows(MockitoException.class, () -> mockSettings.extraInterfaces(null));
 }

 @Test
 public void testExtraInterfacesEmpty() {
 assertThrows(MockitoException.class, () -> mockSettings.extraInterfaces(new Class[0]));
 }

 @Test
 public void testExtraInterfacesNullInterface() {
 Class<?> nullClass = null;
 assertThrows(MockitoException.class, () -> mockSettings.extraInterfaces(new Class[] { nullClass }));
 }

 @Test
 public void testExtraInterfacesNonInterface() {
 Class<?> nonInterface = Mockito.class;
 assertThrows(MockitoException.class, () -> mockSettings.extraInterfaces(new Class[] { nonInterface }));
 }

 @Test
 public void testExtraInterfacesValidInterfaces() {
 Class<?> interface1 = Serializable.class;
 Class<?> interface2 = java.util.List.class;
 Class<?>[] interfaces = new Class[] { interface1, interface2 };
 mockSettings = mockSettings.extraInterfaces(interfaces);
 assertArrayEquals(interfaces, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testDefaultAnswer() {
 Answer<Object> answer = Mockito.answer( invitation -> "Answer");
 mockSettings = mockSettings.defaultAnswer(answer);
 assertEquals(answer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testGetName() {
 String name = "MockName";
 mockSettings = mockSettings.name(name);
 assertEquals(name, mockSettings.getMockName().getName());
 }

 @Test
 public void testGetSpiedInstance() {
 Object spiedInstance = new Object();
 mockSettings = mockSettings.spiedInstance(spiedInstance);
 assertEquals(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testIsSerializable() {
 mockSettings = mockSettings.serializable();
 assertTrue(mockSettings.isSerializable());
 }

 @Test
 public void testInitiateMockName() {
 Class<?> classToMock = Mockito.class;
 mockSettings.initiateMockName(classToMock);
 assertNotNull(mockSettings.getMockName());
 }
}