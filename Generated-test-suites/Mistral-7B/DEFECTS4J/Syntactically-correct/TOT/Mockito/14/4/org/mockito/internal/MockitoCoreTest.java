import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.mockito.internal.MockitoCore;
import org.mockito.internal.MockitoCore.InvocationInfo;
import org.mockito.internal.MockitoCore.MockHandler;
import org.mockito.internal.MockitoCore.ThreadSafeMockingProgress;
import org.mockito.stubbing.AnswerWithMock;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyOrder;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private ThreadSafeMockingProgress mockingProgress;
 private MockHandler<Object> mockHandler;
 private Object mock;

 @Before
 public void setUp() {
 mockitoCore = new MockitoCore();
 mockingProgress = mockitoCore.mockingProgress;
 mockHandler = mockitoCore.mockUtil.getMockHandler(mock);
 mock = mock(Object.class);
 }

 @Test
 public void testMockCreation() {
 Object mockedObject = mockitoCore.mock(Object.class, new MockSettingsImpl());
 verify(mockitoCore).mockingStarted(mockedObject, Object.class, new MockSettingsImpl());
 }

 @Test
 public void testStubbing() {
 OngoingStubbing<Object> stubbing = mockitoCore.stub();
 ArgumentCaptor<Integer> integerCaptor = ArgumentCaptor.forClass(Integer.class);
 when(mockitoCore.mock(Integer.class)).thenReturn(123);
 when(mockitoCore.when(mockitoCore.mock(Object.class))).thenReturn(mock);
 mockitoCore.stubbingStarted();
 assertEquals(123, mockitoCore.verify(mockitoCore.mock(Integer.class), VerificationMode.RETURN));
 assertEquals(mock, mockitoCore.verify(mockitoCore.when(mockitoCore.mock(Object.class))));

 verify(stubbing).thenReturn(456);
 verify(stubbing).thenThrow(new RuntimeException("Test Exception"));
 verify(stubbing).thenAnswer(new AnswerWithMock<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return invocation.getArgument(0) * 2;
 }
 });
 }

 @Test
 public void testVerification() {
 mockitoCore.verify(mockitoCore.mock(Object.class), VerificationMode.TIMES(1));
 }

 @Test
 public void testVerificationWithCustomVerifier() {
 ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
 when(mockitoCore.mock(String.class)).thenReturn("Test String");
 mockitoCore.verify(mockitoCore.mock(Object.class), new VerificationMode() {
 @Override
 public void verify(InvocationInfo invocation) {
 assertEquals("Test String", invocation.getTarget());
 assertEquals("methodCall", invocation.getMethod());
 assertEquals("String", invocation.getTargetClass());
 }
 });
 }

 @Test
 public void testVerificationOrder() {
 Object mock1 = mockitoCore.mock(Object.class);
 Object mock2 = mockitoCore.mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 when(mock1.doSomething()).thenReturn(1);
 when(mock2.doSomething()).thenReturn(2);
 verifyOrder(inOrder);
 }

 @Test(expected = RuntimeException.class)
 public void testVerificationError() {
 mockitoCore.verify(mockitoCore.mock(Object.class), VerificationMode.TIMES(2));
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object mockedObject = mockitoCore.mock(Object.class);
 mockitoCore.mockingProgress.mockingStarted();
 mockitoCore.verifyNoMoreInteractions(mockedObject);
 }

 @Test
 public void testVerifyNoMoreInteractionsWithInOrder() {
 Object mock1 = mockitoCore.mock(Object.class);
 Object mock2 = mockitoCore.mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 when(mock1.doSomething()).thenReturn(1);
 when(mock2.doSomething()).thenReturn(2);
 mockitoCore.verifyNoMoreInteractions(Arrays.asList(mock1, mock2), inOrder);
 }

 @Test
 public void testVerifyNoMoreInteractionsWithCustomVerifier() {
 Object mock = mockitoCore.mock(Object.class);
 InvocationOnMock mockInvocation = mockitoCore.mock(InvocationOnMock.class);
 when(mock.doSomething()).thenReturn(42);
 when(mock.doSomethingElse()).thenReturn(7);
 mockitoCore.verify(mockitoCore.mock(Object.class), new VerificationMode() {
 @Override
 public void verify(InvocationInfo invocation) {
 if (invocation.getTarget() == mock) {
 if (invocation.getMethod().getName().equals("doSomething")) {
 assertEquals(42, invocation.getArguments()[0]);
 } else if (invocation.getMethod().getName().equals("doSomethingElse")) {
 assertEquals(7, invocation.getArguments()[0]);
 }
 }
 }
 });
 mockitoCore.verifyNoMoreInteractions(mock);
 verify(mockInvocation).verifyNoMoreInteractions();
 }

 @Test
 public void testVerifyNoMoreInteractionsWithCustomVerifierAndInOrder() {
 Object mock1 = mockitoCore.mock(Object.class);
 Object mock2 = mockitoCore.mock(Object.class);
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 when(mock1.doSomething()).thenReturn(1);
 when(mock2.doSomething()).thenReturn(2);
 mockitoCore.verify(mockitoCore.mock(Object.class), new VerificationMode() {
 @Override
 public void verify(InvocationInfo invocation) {
 if (invocation.getTarget() == mock1) {
 if (invocation.getMethod().getName().equals("doSomething")) {
 assertEquals(1, invocation.getArguments()[0]);
 }
 } else if (invocation.getTarget() == mock2) {
 if (invocation.getMethod().getName().equals("doSomething")) {
 assertEquals(2, invocation.getArguments()[0]);
 }
 }
 }
 }, inOrder);
 }

 @Test
 public void testAnswer() {
 Object mock = mockitoCore.mock(Object.class);
 Answer<String> answer = Mockito.answer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "Answer: " + invocation.getArgument(0);
 }
 });
 when(mockitoCore.doAnswer(answer)).thenReturn("Test Answer");
 assertTrue(mockitoCore.verify(mockitoCore.mock(Object.class), VerificationMode.RETURN).equals("Test Answer"));
 }

 @Test
 public void testAnswerWithStubbing() {
 Object mock = mockitoCore.mock(Object.class);
 Answer<String> answer = Mockito.answer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "Answer: " + invocation.getArgument(0).toString();
 }
 });
 mockitoCore.doAnswer(answer).when(mockitoCore.mock(Object.class)).doSomething();
 assertEquals("Answer: Object", mockitoCore.verify(mockitoCore.mock(Object.class)).doSomething());
 }

 @Test
 public void testAnswerWithArgumentCaptor() {
 Object mock = mockitoCore.mock(Object.class);
 ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
 Answer<String> answer = Mockito.answer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 String argument = stringArgumentCaptor.getValue();
 return "Answer: " + argument;
 }
 });
 mockitoCore.doAnswer(answer).when(mockitoCore.mock(Object.class)).doSomething(Mockito.anyString());
 assertEquals("Answer: Test", mockitoCore.verify(mockitoCore.mock(Object.class)).doSomething("Test"));
 }

 @Test
 public void testAnswerWithArgumentMatcher() {
 Object mock = mockitoCore.mock(Object.class);
 ArgumentMatcher<String> stringArgumentMatcher = Mockito.argThat(new ArgumentMatcher<String>() {
 @Override
 public boolean matches(Object argument) {
 return argument.equals("Test");
 }
 });
 Answer<String> answer = Mockito.answer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 String argument = (String) invocation.getArguments()[0];
 return "Answer: " + argument;
 }
 });
 mockitoCore.doAnswer(answer).when(mockitoCore.mock(Object.class)).doSomething(stringArgumentMatcher.matches(Mockito.anyString()));
 assertEquals("Answer: Test", mockitoCore.verify(mockitoCore.mock(Object.class)).doSomething("Test"));
 }

 @Test
 public void testAnswerWithArgumentMatcherAndCaptor() {
 Object mock = mockitoCore.mock(Object.class);
 ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentMatcher<String> stringArgumentMatcher = Mockito.argThat(new ArgumentMatcher<String>() {
 @Override
 public boolean matches(Object argument) {
 return argument.equals("Test");
 }
 });
 Answer<String> answer = Mockito.answer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 String argument = stringArgumentCaptor.getValue();
 return "Answer: " + argument;
 }
 });
 mockitoCore.doAnswer(answer).when(mockitoCore.mock(Object.class)).doSomething(stringArgumentMatcher.matches(Mockito.anyString()), Mockito.capture(stringArgumentCaptor));
 assertEquals("Answer: Test", mockitoCore.verify(mockitoCore.mock(Object.class)).doSomething("Test"));
 assertEquals("Test", mockitoCore.verify(mockitoCore.mock(Object.class)).doSomething("Test"));
 }
}