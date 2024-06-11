import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.MockitoCore.MockingProgress;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractionsInOrder;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private MockingProgress mockingProgress;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 mockingProgress = mockitoCore.mockingProgress;
 }

 @Test
 public void testMockCreation() {
 // Test creating a mock with default settings
 Object mock = mockitoCore.mock(String.class, MockSettingsImpl.defaultSettings());
 // Test creating a mock with custom settings
 Object mockWithCustomSettings = mockitoCore.mock(String.class, new MockSettingsImpl().mockBehavior(MockSettings.DefaultBehavior.LENIENT));

 // Verify that mocks have been created
 verify(mockitoCore).mock(String.class, any(MockSettings.class));
 verify(mockitoCore).mock(String.class, ArgumentMatchers.eq(new MockSettingsImpl().mockBehavior(MockSettings.DefaultBehavior.LENIENT)));
 }

 @Test
 public void testStubbing() {
 // Test stubbing a method call
 Object mock = mockitoCore.mock(String.class, MockSettingsImpl.defaultSettings());
 mockitoCore.stub(mock).myMethod();
 mockitoCore.when(mock).myMethod().thenReturn("Hello");

 // Test verifying the stubbed method call
 verify(mock).myMethod();
 verify(mock).myMethod().thenReturn("Hello");

 // Test resetting stubbing
 reset(mock);
 verify(mock).myMethod();
 verify(mock).myMethod().thenReturn(null);
 }

 @Test
 public void testVerification() {
 // Test verifying a mock with no interactions
 Object mock = mockitoCore.mock(String.class, MockSettingsImpl.defaultSettings());
 verify(mock).myMethod();

 // Test verifying a mock with interactions
 Object mockWithInteractions = mockitoCore.mock(String.class, MockSettingsImpl.defaultSettings());
 mockitoCore.stub(mockWithInteractions).myMethod().thenReturn("Hello");
 verify(mockWithInteractions).myMethod();

 // Test verifying no more interactions
 Object mockWithNoMoreInteractions = mockitoCore.mock(String.class, MockSettingsImpl.defaultSettings());
 mockitoCore.stub(mockWithNoMoreInteractions).myMethod().thenReturn("Hello");
 verifyNoMoreInteractions(mockWithNoMoreInteractions);
 }

 @Test
 public void testVerificationInOrder() {
 // Test verifying interactions in order
 Object mockWithInteractions = mockitoCore.mock(String.class, MockSettingsImpl.defaultSettings());
 mockitoCore.stub(mockWithInteractions).myMethod1().thenReturn("Hello1");
 mockitoCore.stub(mockWithInteractions).myMethod2().thenReturn("Hello2");
 List<Object> mocks = Arrays.asList(mockWithInteractions);
 InOrder inOrder = inOrder(mocks);
 mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrder);
 }

 @Test
 public void testAnswer() {
 // Test using an Answer
 Object mock = mockitoCore.mock(String.class, MockSettingsImpl.defaultSettings());
 Answer<String> answer = invocation -> "Hello" + invocation.getArgumentAt(0, String.class);
 mockitoCore.doAnswer(answer).when(mock).myMethod(anyString());

 // Test verifying the call
 verify(mock).myMethod("Test");
 }

 @Test(expected = AssertionError.class)
 public void testExceptionWhenNullPassedToVerify() {
 // Test exception when null passed to verify
 mockitoCore.verify(null, Mockito.eq(1));
 }

 @Test(expected = AssertionError.class)
 public void testExceptionWhenNotAMockPassedToVerify() {
 // Test exception when not a mock passed to verify
 Object notAMock = new Object();
 mockitoCore.verify(notAMock, Mockito.eq(1));
 }

 @Test(expected = AssertionError.class)
 public void testExceptionWhenNullPassedToVerifyNoMoreInteractions() {
 // Test exception when null passed to verifyNoMoreInteractions
 mockitoCore.verifyNoMoreInteractions((Object) null);
 }

 @Test(expected = AssertionError.class)
 public void testExceptionWhenNotAMockPassedToVerifyNoMoreInteractions() {
 // Test exception when not a mock passed to verifyNoMoreInteractions
 Object notAMock = new Object();
 mockitoCore.verifyNoMoreInteractionsInOrder((List) null, inOrder(new Object[]{}));
 }

 @Test
 public void testGetLastInvocation() {
 // Test getting the last invocation
 Object mock = mockitoCore.mock(String.class, MockSettingsImpl.defaultSettings());
 mockitoCore.stub(mock).myMethod().then(new Answer<Void>() {
 @Override
 public Void answer(InvocationOnMock invocation) throws Throwable {
 mockitoCore.getLastInvocation();
 return null;
 }
 });
 verify(mock).myMethod();
 Invocation lastInvocation = mockitoCore.getLastInvocation();
 // Test verifying the last invocation
 verify(mock).myMethod();
 verify(mock).myMethod().thenReturn("Hello");
 verify(lastInvocation).getArgumentAt(0, String.class).equals("Test");
 }
}