import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock;
 private Object stub;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 mock = mock(Object.class);
 stub = mockitoCore.mock(Object.class, new MockSettingsImpl());
 }

 @Test
 public void testMockCreation() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 assert mock != null;
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMockCreationWithNullClass() {
 mockitoCore.mock(null, new MockSettingsImpl());
 }

 @Test
 public void testStubbing() {
 Stubber stubber = mockitoCore.stub();
 stubber.thenReturn("Hello");
 String result = (String) mockitoCore.when(mock).methodCall();
 assert result.equals("Hello");
 }

 @Test
 public void testStubbingWithArgument() {
 Stubber stubber = mockitoCore.stub();
 stubber.thenReturn("Hello").given(mock).methodCall(ArgumentMatchers.anyString());
 String result = (String) mockitoCore.when(mock).methodCall("test");
 assert result.equals("Hello");
 }

 @Test
 public void testStubbingWithAnswer() {
 Stubber stubber = mockitoCore.doAnswer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "Answer";
 }
 }).when(mock).methodCall();
 String result = (String) mockitoCore.when(mock).methodCall();
 assert result.equals("Answer");
 }

 @Test
 public void testVerification() {
 mockitoCore.verify(mock, Mockito.eq(1));
 }

 @Test(expected = AssertionError.class)
 public void testVerificationWithNullMock() {
 mockitoCore.verify(null, Mockito.eq(1));
 }

 @Test(expected = AssertionError.class)
 public void testVerificationWithNotAMock() {
 Object notAMock = new Object();
 mockitoCore.verify(notAMock, Mockito.eq(1));
 }

 @Test
 public void testVerificationNoInteractions() {
 Object mock = mock(Object.class);
 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test(expected = AssertionError.class)
 public void testVerificationNoInteractionsWithNullMock() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test
 public void testVerificationInOrder() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 mockitoCore.when(mock1).methodCall().thenReturn(1);
 mockitoCore.when(mock2).methodCall().thenReturn(2);
 mockitoCore.verify(mock1).methodCall();
 mockitoCore.verify(mock2).methodCall();
 inOrder.verify(mock1).methodCall();
 inOrder.verify(mock2).methodCall();
 }

 @Test(expected = AssertionError.class)
 public void testVerificationInOrderWithNullMock() {
 Object mock1 = mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(null);
 mockitoCore.when(mock1).methodCall().thenReturn(1);
 mockitoCore.verify(mock1).methodCall();
 inOrder.verify(mock1).methodCall();
 }

 @Test
 public void testStubbingMultipleReturns() {
 Stubber stubber = mockitoCore.stub();
 stubber.thenReturn("Hello").thenReturn("Goodbye");
 String result1 = (String) mockitoCore.when(mock).methodCall();
 assert result1.equals("Hello");
 String result2 = (String) mockitoCore.when(mock).methodCall();
 assert result2.equals("Goodbye");
 }

 @Test
 public void testVerificationWithMockSettings() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl().name("Test Mock"));
 mockitoCore.verify(mock, Mockito.eq(1)).methodCall();
 }

 @Test
 public void testVerificationWithCustomReporter() {
 Reporter customReporter = new CustomReporter();
 MockitoCore mockitoCoreWithReporter = new MockitoCore() {
 @Override
 public Reporter getReporter() {
 return customReporter;
 }
 };
 Object mock = mockitoCoreWithReporter.mock(Object.class);
 mockitoCoreWithReporter.verify(mock, Mockito.eq(1));
 }

 @Test
 public void testGetLastInvocation() {
 Stubber stubber = mockitoCore.stub();
 stubber.thenReturn("Hello").given(mockitoCore.mock(Object.class)).methodCall();
 Invocation lastInvocation = mockitoCore.getLastInvocation();
 assert lastInvocation.getMock() == mockitoCore.mock(Object.class);
 assert lastInvocation.getMethod() != null;
 assert lastInvocation.getArguments()[0] instanceof Object;
 }
}

class CustomReporter extends Reporter {
 @Override
 public void missingMethodInvocation() {
 // Do nothing
 }
}