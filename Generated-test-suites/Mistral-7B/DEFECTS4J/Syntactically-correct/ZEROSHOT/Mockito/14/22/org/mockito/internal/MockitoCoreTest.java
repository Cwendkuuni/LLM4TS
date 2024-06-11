import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractionsInOrder;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 @Mock private Object mock1;
 @Mock private Object mock2;
 @Mock private Object mock3;

 @Before
 public void setUp() {
 mockitoCore = new MockitoCore();
 }

 @Test
 public void testMockCreation() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 assertNotNull(mock);
 }

 @Test(expected = NotAMockException.class)
 public void testMockCreationWithNullClass() {
 mockitoCore.mock(null, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testMockCreationWithNullMockSettings() {
 mockitoCore.mock(Object.class, null);
 }

 @Test
 public void testStubbing() {
 OngoingStubbing ongoingStubbing = mockitoCore.stub();
 assertNotNull(ongoingStubbing);
 }

 @Test(expected = NotAMockException.class)
 public void testStubbingWithNullObject() {
 mockitoCore.stub(null);
 }

 @Test
 public void testStubbingWithMethodCall() {
 mockitoCore.stub(ArgumentMatchers.anyInt());
 }

 @Test(expected = NotAMockException.class)
 public void testStubbingWithNullMethodCall() {
 mockitoCore.stub(null);
 }

 @Test
 public void testWhenMethod() {
 OngoingStubbing ongoingStubbing = mockitoCore.when(mock1);
 assertNotNull(ongoingStubbing);
 }

 @Test(expected = NotAMockException.class)
 public void testWhenMethodWithNullObject() {
 mockitoCore.when(null);
 }

 @Test(expected = NotAMockException.class)
 public void testWhenMethodWithNullMethodCall() {
 mockitoCore.when(mock1, null);
 }

 @Test
 public void testVerify() {
 mockitoCore.verify(mock1, VerificationMode.verifyNoMoreInteractions());
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyWithNullObject() {
 mockitoCore.verify(null, VerificationMode.verifyNoMoreInteractions());
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyWithNullVerificationMode() {
 mockitoCore.verify(mock1, null);
 }

 @Test
 public void testReset() {
 mockitoCore.reset(mock1, mock2, mock3);
 }

 @Test(expected = NotAMockException.class)
 public void testResetWithNullObject() {
 mockitoCore.reset(null);
 }

 @Test(expected = NotAMockException.class)
 public void testResetWithEmptyArray() {
 mockitoCore.reset();
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 mockitoCore.verifyNoMoreInteractions(mock1, mock2, mock3);
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyNoMoreInteractionsWithNullObject() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyNoMoreInteractionsWithEmptyArray() {
 mockitoCore.verifyNoMoreInteractions();
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2, mock3);
 verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2, mock3), inOrder);
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyNoMoreInteractionsInOrderWithNullObjects() {
 mockitoCore.verifyNoMoreInteractionsInOrder(null, inOrder);
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrderWithEmptyList() {
 verifyNoMoreInteractionsInOrder(Arrays.asList(), inOrder);
 }

 @Test
 public void testGetLastInvocation() {
 Invocation lastInvocation = mockitoCore.getLastInvocation();
 assertNotNull(lastInvocation);
 }

 @Test(expected = NotAMockException.class)
 public void testGetLastInvocationWithNullOngoingStubbing() {
 mockitoCore.getLastInvocation();
 }

 @Test
 public void testDoAnswer() {
 Answer answer = Mockito.answer(1);
 Stubber stubber = mockitoCore.doAnswer(answer);
 assertEquals(1, stubber.callRealMethod());
 }

 @Test
 public void testStubVoid() {
 Object mock = mockitoCore.mock(Object.class);
 VoidMethodStubbable voidMethodStubbable = mockitoCore.stubVoid(mock);
 voidMethodStubbable.answer(1);
 }

 @Test(expected = NotAMockException.class)
 public void testStubVoidWithNullObject() {
 mockitoCore.stubVoid(null);
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

}