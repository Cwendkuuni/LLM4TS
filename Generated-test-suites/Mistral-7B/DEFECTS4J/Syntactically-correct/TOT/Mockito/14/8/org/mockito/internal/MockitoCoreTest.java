import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.verification.VerificationMode;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock1;
 private Object mock2;
 private Object mock3;
 private InOrder inOrder;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 mockitoCore = new MockitoCore();
 mock1 = mock(Object.class);
 mock2 = mock(Object.class);
 mock3 = mock(Object.class);
 inOrder = inOrder(mock1, mock2, mock3);
 }

 @Test
 public void testMockCreation() {
 T mock = mockitoCore.<T> mock(Object.class, null);
 assertNotNull(mock);
 }

 @Test
 public void testStubbing() {
 Object result;
 when(mock1.someMethod(ArgumentMatchers.anyInt())).thenReturn(42);
 result = mock1.someMethod(1);
 assertEquals(42, result);
 }

 @Test
 public void testDeprecatedStubbing() {
 mockitoCore.<DeprecatedOngoingStubbing> stub(mock1).thenReturn(42);
 Object result = mock1.someMethod();
 assertEquals(42, result);
 }

 @Test
 public void testOngoingStubbing() {
 mockitoCore.<OngoingStubbing> when(mock1.someMethod()).thenReturn(42);
 Object result = mock1.someMethod();
 assertEquals(42, result);
 }

 @Test
 public void testVerify() {
 verify(mock1).someMethod();
 }

 @Test
 public void testVerifyWithMode() {
 verify(mock1).someMethod().times(2);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyWithException() {
 verify(mock1).someMethod();
 verify(mock2).someMethod();
 Mockito.verifyNoMoreInteractions(mock1, mock2);
 }

 @Test
 public void testVerifyNoInteractions() {
 verifyNoMoreInteractions(mock1);
 }

 @Test
 public void testVerifyInteractionsInOrder() {
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
 }

 @Test
 public void testAnswer() {
 Object result = mockitoCore.doAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return invocation.getArgument(0);
 }
 }).<Object> stub(mock1).someMethod(ArgumentMatchers.anyInt());
 assertEquals(1, result);
 }

 @Test
 public void testVoidMethodStubbing() {
 mockitoCore.<VoidMethodStubbable> stubVoid(mock1).someVoidMethod();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 Invocation lastInvocation = mockitoCore.getLastInvocation();
 assertNotNull(lastInvocation);
 }
}