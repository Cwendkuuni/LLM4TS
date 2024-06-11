import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.stubbing.Answer;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class MockitoCoreTest {

 @Mock
 private Object mockObject;
 @Mock
 private Object anotherMockObject;

 @Before
 public void setUp() {
 MockitoCore mockitoCore = new MockitoCore();
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testMockCreation() {
 Object mockedObject = mockitoCore.mock(String.class, new MockSettingsImpl(), false);
 assertNotNull(mockedObject);
 }

 @Test
 public void testStubbing() {
 Object mockedObject = mockitoCore.mock(String.class, new MockSettingsImpl(), false);
 Mockito.stub(mockedObject.toString()).returnValue("Mockito");
 String result = (String) mockedObject.toString();
 assertNotNull(result);
 assertNotNull(result.getClass());
 assertEquals("Mockito", result);
 }

 @Test
 public void testVerifyNoInteractions() {
 mockitoCore.verify(mockObject, Mockito.never());
 }

 @Test
 public void testVerification() {
 mockitoCore.verify(mockObject).toString();
 }

 @Test
 public void testVerifyOrder() {
 Object mockedObject1 = mockitoCore.mock(String.class, new MockSettingsImpl(), false);
 Object mockedObject2 = mockitoCore.mock(String.class, new MockSettingsImpl(), false);
 inOrder(mockedObject1, mockedObject2).verify(mockedObject1).toString();
 inOrder(mockedObject1, mockedObject2).verify(mockedObject2).toString();
 }

 @Test(expected = RuntimeException.class)
 public void testMockingProgressException() {
 mockitoCore.mock(String.class, new MockSettingsImpl(), false);
 mockitoCore.mock(String.class, new MockSettingsImpl(), false);
 mockitoCore.mock(String.class, new MockSettingsImpl(), false);
 mockitoCore.verify(mockObject); // should throw an exception
 }

 @Test
 public void testAnswer() {
 Object mockedObject = mockitoCore.mock(String.class, new MockSettingsImpl(), false);
 Answer<String> answer = new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "Answer";
 }
 };
 mockitoCore.doAnswer(answer).when(mockedObject).toString();
 String result = (String) mockedObject.toString();
 assertNotNull(result);
 assertEquals("Answer", result);
 }
}