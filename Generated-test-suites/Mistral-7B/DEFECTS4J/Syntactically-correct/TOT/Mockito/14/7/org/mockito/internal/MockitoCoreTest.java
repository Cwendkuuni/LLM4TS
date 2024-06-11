import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.verification.api.InOrder;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractionsInOrder;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.internal.verification.api.StubbingVerifications.verifyZeroStubbing;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 @Mock
 private Object mockObject1;
 @Mock
 private Object mockObject2;
 @Mock
 private Object mockObject3;
 @Mock
 private Object mockObject4;
 @Mock
 private MockingProgress mockingProgress;

 private InOrder inOrder;

 @Before
 public void setUp() {
 mockitoCore = new MockitoCore();
 inOrder = mock(InOrder.class);
 }

 @Test
 public void testMockCreation() {
 Object mock = mockitoCore.mock(Object.class, null);
 MockHandlerInterface handler = Mockito.mockHandler(mock);
 verify(handler).handleMethodCalls(ArgumentMatchers.isInstance(mock), ArgumentMatchers.any());
 }

 @Test
 public void testStubbing() {
 Object mock = mockitoCore.mock(Object.class, null);
 ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptors.capture(Integer.class);
 when(mock).thenReturn(42);
 verify(mock).someMethod(integerArgumentCaptor.capture());
 int capturedArgument = integerArgumentCaptor.getValue();
 verify(mock).someMethod(capturedArgument);
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyWithNullMock() {
 mockitoCore.verify(null, Mockito.eq(1));
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyWithNonMock() {
 Object nonMock = new Object();
 mockitoCore.verify(nonMock, Mockito.eq(1));
 }

 @Test
 public void testVerifyModeFailFast() {
 Object mock = mockitoCore.mock(Object.class, null);
 when(mock.someMethod()).thenReturn(1);
 mockitoCore.verify(mock, Mockito.atLeastOnce()).someMethod();
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyNoMoreInteractionsWithNullMock() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyNoMoreInteractionsWithNonMock() {
 Object nonMock = new Object();
 mockitoCore.verifyNoMoreInteractions(nonMock);
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object mock = mockitoCore.mock(Object.class, null);
 when(mock.someMethod()).thenReturn(1);
 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 Object mock1 = mockitoCore.mock(Object.class, null);
 Object mock2 = mockitoCore.mock(Object.class, null);
 when(mock1.someMethod()).thenReturn(1);
 when(mock2.someMethod()).thenReturn(2);
 inOrder.verify(mock1, mock2).someMethod();
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
 }

 @Test
 public void testVerifyZeroInteractions() {
 Object mock = mockitoCore.mock(Object.class, null);
 verifyZeroInteractions(mock);
 }

 @Test
 public void testVerifyZeroStubbing() {
 Object mock = mockitoCore.mock(Object.class, null);
 mockitoCore.stub(mock).someMethod();
 verifyZeroStubbing(mockitoCore.mockingProgress);
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyStubbingAfterReset() {
 Object mock = mockitoCore.mock(Object.class, null);
 mockitoCore.reset(mock);
 mockitoCore.when(mock.someMethod()).thenReturn(1);
 mockitoCore.verify(mock).someMethod();
 }

 @Test
 public void testReset() {
 Object mock = mockitoCore.mock(Object.class, null);
 mockitoCore.reset(mock);
 verifyZeroInteractions(mock);
 }

 @Test
 public void testAnswer() {
 Object mock = mockitoCore.mock(Object.class, null);
 Answer<Integer> answer = arg -> arg.intValue() * 2;
 doAnswer(answer).when(mock).someMethod(ArgumentMatchers.anyInt());
 verify(mock).someMethod(1);
 verify(mock).someMethod(2);
 int result1 = mockitoCore.getLastInvocation().getArgument(0).intValue();
 int result2 = mockitoCore.getLastInvocation().getArgument(0).intValue();
 assertEquals(2, result1);
 assertEquals(2, result2);
 }

 @Test
 public void testVoidMethodStubbing() {
 Object mock = mockitoCore.mock(Object.class, null);
 mockitoCore.stubVoid(mock).someVoidMethod();
 mock.someVoidMethod();
 }

 @Test
 public void testValidaMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }
}