package tests.org.mockito.internal.creation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.AnswerStub;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.assertThat;

public class MockSettingsImplTest {

 private MockSettingsImpl mockSettings;
 private Class<?>[] extraInterfaces;
 private String name;
 private Object spiedInstance;
 private Answer<Object> defaultAnswer;
 private MockName mockName;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 extraInterfaces = new Class[]{java.io.Serializable.class};
 name = "MockName";
 spiedInstance = mock(Object.class);
 defaultAnswer = new AnswerStub();
 }

 @Test
 public void testSerializable() {
 mockSettings = mockSettings.serializable();
 assertThat(mockSettings.isSerializable()).isTrue();
 }

 @Test(expected = AssertionError.class)
 public void testSerializable_withoutInterface() {
 mockSettings = mockSettings.serializable();
 mockSettings.extraInterfaces(String.class);
 }

 @Test
 public void testExtraInterfaces() {
 mockSettings = mockSettings.extraInterfaces(extraInterfaces);
 assertThat(mockSettings.getExtraInterfaces()).isEqualTo(extraInterfaces);
 }

 @Test(expected = AssertionError.class)
 public void testExtraInterfaces_nullParameter() {
 mockSettings = mockSettings.extraInterfaces(null);
 }

 @Test(expected = AssertionError.class)
 public void testExtraInterfaces_nonInterfaceParameter() {
 mockSettings = mockSettings.extraInterfaces(String.class);
 }

 @Test
 public void testGetMockName() {
 mockSettings.initiateMockName(String.class);
 mockName = mockSettings.getMockName();
 verify(mockName).name(name);
 verify(mockName).classToMock(String.class);
 }

 @Test
 public void testGetSpiedInstance() {
 mockSettings.spiedInstance(spiedInstance);
 assertThat(mockSettings.getSpiedInstance()).isEqualTo(spiedInstance);
 }

 @Test
 public void testName() {
 mockSettings.name(name);
 assertThat(mockSettings.getName()).isEqualTo(name);
 }

 @Test
 public void testDefaultAnswer() {
 mockSettings.defaultAnswer(defaultAnswer);
 mockSettings.initiateMockName(String.class);
 Answer answer = mockSettings.getDefaultAnswer();
 verify(defaultAnswer).answersCalls(mockSettings);
 verify(answer).answersCalls(spiedInstance);
 }

 @Test
 public void testIsSerializable() {
 mockSettings = mockSettings.serializable();
 assertThat(mockSettings.isSerializable()).isTrue();
 }
}