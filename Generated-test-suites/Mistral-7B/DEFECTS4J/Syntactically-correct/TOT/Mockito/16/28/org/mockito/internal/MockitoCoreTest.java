import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoCore;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class MockitoCoreTest {

 @Mock
 private MockingProgress mockingProgress;

 @Before
 public void setupMockingProgress() {
 Mockito.initMocks(this);
 }

 @Test
 public void testMockCreation() {
 Class<String> stringClass = String.class;
 String mock = MockitoCore.mock(stringClass, new MockSettingsImpl(), false);
 verify(mockingProgress).validateState();
 verify(mockingProgress).resetOngoingStubbing();
 verify(mockUtil, Mockito.atLeastOnce()).createMock(stringClass, (MockSettingsImpl) mockSettings);
 }

 @Test
 public void testStubbing() {
 Object mock = mock(Object.class);
 MockitoCore.stub(mock).returns("result");
 verify(mockingProgress).stubbingStarted();
 ArgumentCaptor<Answer<String>> answerCaptor = ArgumentCaptor.forClass(Answer.class);
 verify(stubber).doAnswer(answerCaptor.capture());
 assertEquals("result", answerCaptor.getValue().answer(ArgumentMatchers.any()));
 }

 @Test
 public void testVerify() {
 Object mock = mock(Object.class);
 verify(mock).methodCall();
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNull() {
 MockitoCore.verify(null, Mockito.any());
 }

 @Test(expected = IllegalStateException.class)
 public void testVerifyNotAMock() {
 Object notAMock = new Object();
 MockitoCore.verify(notAMock, Mockito.any());
 }

 @Test
 public void testReset() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 MockitoCore.reset(mock1, mock2);
 verify(mockUtil, Mockito.times(2)).resetMock(Mockito.eq(mock1));
 verify(mockUtil, Mockito.times(2)).resetMock(Mockito.eq(mock2));
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object mock = mock(Object.class);
 verifyNoMoreInteractions(mock);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsNull() {
 MockitoCore.verifyNoMoreInteractions(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNoMoreInteractionsEmptyArray() {
 MockitoCore.verifyNoMoreInteractions();
 }

 @Test
 public void testVerifyNoMoreInteractionsMultipleMocks() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 MockitoCore.verifyNoMoreInteractions(mock1, mock2);
 }

 @Test
 public void testInOrder() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 InOrder inOrder = MockitoCore.inOrder(mock1, mock2);
 inOrder.verify(mock1).methodCall1();
 inOrder.verify(mock1).methodCall2();
 inOrder.verify(mock2).methodCall1();
 inOrder.verify(mock2).methodCall2();
 }

 @Test
 public void testAnswer() {
 Object mock = mock(Object.class);
 Answer<Integer> answer = invocation -> 42;
 MockitoCore.doAnswer(answer).when(mock).methodCall();
 assertEquals(42, MockitoCore.getLastInvocation().getArgument(0));
 }

 @Test
 public void testStubVoid() {
 Object mock = mock(Object.class);
 MockitoCore.stubVoid(mock).methodCall();
 verify(mockingProgress).stubbingStarted();
 verify(mockUtil.getMockHandler(mock)).voidMethodStubbable(mock).methodCall();
 }

 @Test
 public void testValidateMockitoUsage() {
 MockitoCore.validateMockitoUsage();
 }

 @Test(expected = IllegalStateException.class)
 public void testVerifyInteractionsAfterReset() {
 Object mock = mock(Object.class);
 MockitoCore.reset(mock);
 MockitoCore.verify(mock).methodCall();
 }

 @Test
 public void testVerifyInteractionsAfterStubbingWithAnswer() {
 Object mock = mock(Object.class);
 Answer<Integer> answer = invocation -> 42;
 MockitoCore.stub(mock).methodCall().thenAnswer(answer);
 MockitoCore.verify(mock).methodCall();
 assertEquals(42, answer.getNumberOfInvocations());
 }

 @Test(expected = IllegalStateException.class)
 public void testVerifyInteractionsAfterStubbingWithCallRealMethod() {
 Object mock = mock(Object.class);
 MockitoCore.stub(mock).methodCall().callRealMethod();
 MockitoCore.verify(mock).methodCall();
 }

 @Test(expected = IllegalStateException.class)
 public void testVerifyInteractionsAfterStubbingWithStub() {
 Object mock = mock(Object.class);
 Object stub = mock(Object.class);
 MockitoCore.stub(mock).methodCall().thenStub(stub);
 MockitoCore.verify(mock).methodCall();
 }

 @Test(expected = IllegalStateException.class)
 public void testVerifyInteractionsAfterStubbingWithAnswerAndStub() {
 Object mock = mock(Object.class);
 Object stub = mock(Object.class);
 Answer<Integer> answer = invocation -> 42;
 MockitoCore.stub(mock).methodCall().thenAnswer(answer).thenStub(stub);
 MockitoCore.verify(mock).methodCall();
 }
}