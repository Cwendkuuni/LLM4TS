import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.MockSettings;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.AnswerCalledWith;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;
 private Class<?>[] extraInterfaces;
 private String name;
 private Object spiedInstance;
 private Answer<Object> defaultAnswer;
 private MockName mockName;

 @Before
 public void setup() {
 mockSettings = new MockSettingsImpl();
 }

 @Test
 public void testSerializable() {
 mockSettings = mockSettings.serializable();
 assertTrue(mockSettings.isSerializable());
 }

 @Test
 public void testSerializableWithEmptyExtraInterfaces() {
 mockSettings = mockSettings.serializable();
 assertThrows(MockitoException.class, () -> mockSettings.serializable());
 }

 @Test
 public void testSerializableWithNullExtraInterfaces() {
 mockSettings = mockSettings.serializable();
 assertThrows(MockitoException.class, () -> mockSettings.serializable((Class<?>[]) null));
 }

 @Test
 public void testSerializableWithNonInterfaceExtraInterface() {
 mockSettings = mockSettings.serializable();
 assertThrows(MockitoException.class, () -> mockSettings.serializable(String.class));
 }

 @Test
 public void testExtraInterfaces() {
 extraInterfaces = new Class[] {Serializable.class, String.class};
 mockSettings = mockSettings.extraInterfaces(extraInterfaces);
 assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testExtraInterfacesWithEmptyArray() {
 mockSettings = mockSettings.extraInterfaces(new Class[0]);
 assertArrayEquals(new Class[0], mockSettings.getExtraInterfaces());
 }

 @Test
 public void testExtraInterfacesWithNullArray() {
 mockSettings = mockSettings.extraInterfaces((Class<?>[]) null);
 assertArrayEquals(new Class[0], mockSettings.getExtraInterfaces());
 }

 @Test
 public void testExtraInterfacesWithNullExtraInterface() {
 Class<?>[] extraInterfaces = new Class[] {Serializable.class, null};
 mockSettings = mockSettings.extraInterfaces(extraInterfaces);
 assertArrayEquals(new Class[] {Serializable.class}, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetMockName() {
 name = "MockNameTest";
 mockSettings = new MockSettingsImpl().name(name);
 mockSettings.initiateMockName(String.class);
 assertSame(new MockName(name, String.class), mockSettings.getMockName());
 }

 @Test
 public void testGetSpiedInstance() {
 spiedInstance = new Object();
 mockSettings = new MockSettingsImpl().spiedInstance(spiedInstance);
 assertSame(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testGetSpiedInstanceWithNullSpiedInstance() {
 mockSettings = new MockSettingsImpl().spiedInstance(null);
 assertNull(mockSettings.getSpiedInstance());
 }

 @Test
 public void testGetDefaultAnswer() {
 Answer<Object> answer = Mockito.mock(Answer.class);
 mockSettings = new MockSettingsImpl().defaultAnswer(answer);
 assertSame(answer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testGetDefaultAnswerWithNullDefaultAnswer() {
 mockSettings = new MockSettingsImpl();
 assertNull(mockSettings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializable() {
 mockSettings = new MockSettingsImpl().serializable();
 assertTrue(mockSettings.isSerializable());
 }

 @Test
 public void testIsSerializableWithEmptyExtraInterfaces() {
 mockSettings = new MockSettingsImpl();
 assertFalse(mockSettings.isSerializable());
 }

 @Test
 public void testIsSerializableWithNullExtraInterfaces() {
 mockSettings = new MockSettingsImpl();
 assertFalse(mockSettings.isSerializable());
 }

 @Test
 public void testInitiateMockName() {
 name = "MockNameTest";
 mockSettings = new MockSettingsImpl().name(name);
 mockSettings.initiateMockName(String.class);
 assertSame(new MockName(name, String.class), mockSettings.getMockName());
 }

 @Test
 public void testInitiateMockNameWithNullClassToMock() {
 name = "MockNameTest";
 mockSettings = new MockSettingsImpl().name(name);
 assertThrows(IllegalArgumentException.class, () -> mockSettings.initiateMockName(null));
 }

 @Test
 public void testAnswerMethodWithNullDefaultAnswer() {
 Answer<Object> answer = Mockito.mock(Answer.class);
 mockSettings = new MockSettingsImpl();
 assertThrows(NullPointerException.class, () -> mockSettings.defaultAnswer(answer));
 }

 @Test
 public void testAnswerMethodWithNonAnswerType() {
 Object obj = new Object();
 mockSettings = new MockSettingsImpl();
 assertThrows(ClassCastException.class, () -> mockSettings.defaultAnswer(obj));
 }

 @Test
 public void testAnswerMethodWithAnswerAndInvoke() throws InvocationTargetException, IllegalAccessException {
 Answer<String> answer = Mockito.mock(Answer.class, new AnswerCalledWith("test")); {
 String result = answer.answer(Mockito.mock(Answer.class), Mockito.mock(Object.class), Mockito.mock(Answer.class));
 assertEquals("test", result);
 }
 mockSettings = new MockSettingsImpl();
 mockSettings.defaultAnswer(answer);
 Object result = mockSettings.getSpiedInstance().getClass().getDeclaredMethod("getName").invoke(mockSettings.getSpiedInstance());
 assertEquals("test", result);
 }

}