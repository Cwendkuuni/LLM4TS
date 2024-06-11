import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettings;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.AnswerCalledWith;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class MockSettingsImplTest {

 private MockSettings mockSettings;
 private Class<Serializable> serializable;
 private Class<?>[] extraInterfaces;
 private String name;
 private Object spiedInstance;
 private Answer<Object> defaultAnswer;
 private MockName mockName;

 @Before
 public void setup() {
 mockSettings = new MockSettingsImpl();
 serializable = Mockito.mock(Serializable.class);
 extraInterfaces = new Class[]{Serializable.class, String.class};
 name = "MockName";
 spiedInstance = mock(Object.class);
 defaultAnswer = Mockito.answer(new AnswerCalledWith<Object>(null) {
 @Override
 public Object answer(Object invocation) {
 return "Default Answer";
 }
 });
 }

 @Test
 public void testSerializable() {
 mockSettings = mockSettings.serializable();
 assertSame(mockSettings, mockSettings.serializable());
 assertEquals(serializable, Arrays.asList(mockSettings.getExtraInterfaces()).get(0));
 }

 @Test(expected = Reporter.class)
 public void testExtraInterfacesNull() {
 mockSettings = mockSettings.extraInterfaces(null);
 }

 @Test(expected = Reporter.class)
 public void testExtraInterfacesEmpty() {
 mockSettings = mockSettings.extraInterfaces(new Class[0]);
 }

 @Test
 public void testExtraInterfaces() {
 Class<?>[] expectedInterfaces = {serializable, String.class};
 mockSettings = mockSettings.extraInterfaces(extraInterfaces);
 assertArrayEquals(expectedInterfaces, mockSettings.getExtraInterfaces());
 }

 @Test(expected = Reporter.class)
 public void testExtraInterfaceNull() {
 mockSettings = mockSettings.extraInterfaces(new Class[]{null});
 }

 @Test(expected = Reporter.class)
 public void testExtraInterfaceNonInterface() {
 mockSettings = mockSettings.extraInterfaces(new Class[]{Integer.TYPE});
 }

 @Test
 public void testGetMockName() {
 mockSettings.initiateMockName(String.class);
 assertSame(mockName, mockSettings.getMockName());
 }

 @Test
 public void testGetExtraInterfaces() {
 assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
 }

 @Test
 public void testGetSpiedInstance() {
 assertSame(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testName() {
 mockSettings = mockSettings.name(name);
 assertEquals(name, mockSettings.getName());
 }

 @Test
 public void testSpiedInstance() {
 mockSettings = mockSettings.spiedInstance(spiedInstance);
 assertSame(spiedInstance, mockSettings.getSpiedInstance());
 }

 @Test
 public void testDefaultAnswer() {
 mockSettings = mockSettings.defaultAnswer(defaultAnswer);
 assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
 }

 @Test
 public void testIsSerializable() {
 mockSettings = mockSettings.serializable();
 assertTrue(mockSettings.isSerializable());
 }

 @Test
 public void testIsSerializableFalse() {
 mockSettings = new MockSettingsImpl();
 assertFalse(mockSettings.isSerializable());
 }
}