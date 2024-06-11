import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.Answers;
import org.mockito.stubbing.Stub;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 @Mock private Object mockObject1;
 @Mock private Object mockObject2;
 @Mock private Object mockObject3;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 }

 @Test
 public void testMockCreation() {
 Object mockedObject = mockitoCore.mock(Object.class, new MockSettingsImpl());
 verify(mockObject1).equals(mockedObject);
 }

 @Test
 public void testStubbing() {
 Object mockedObject = mockitoCore.mock(Object.class, new MockSettingsImpl());
 Stub<Object> stub = mockitoCore.stub(mockedObject);
 stub.returns(new Object());
 Object returnedValue = mockitoCore.verify(mockedObject).someMethod();
 verify(mockedObject).someMethod();
 verify(mockedObject).someMethod().equals(returnedValue);
 }

 @Test
 public void testDeprecatedStubbing() {
 Object mockedObject = mockitoCore.mock(Object.class, new MockSettingsImpl());
 DeprecatedOngoingStubbing<Object> stub = mockitoCore.stub(mockedObject);
 stub.thenReturn(new Object());
 Object returnedValue = mockitoCore.verify(mockedObject).someMethod();
 verify(mockedObject).someMethod();
 verify(mockedObject).someMethod().equals(returnedValue);
 }

 @Test
 public void testOngoingStubbing() {
 Object mockedObject = mockitoCore.mock(Object.class, new MockSettingsImpl());
 OngoingStubbing<Object> stub = mockitoCore.when(mockedObject).someMethod();
 stub.thenReturn(new Object());
 Object returnedValue = mockitoCore.verify(mockedObject).someMethod();
 verify(mockedObject).someMethod();
 verify(mockedObject).someMethod().equals(returnedValue);
 }

 @Test
 public void testVerification() {
 Object mockedObject = mockitoCore.mock(Object.class, new MockSettingsImpl());
 verify(mockedObject).someMethod();
 }

 @Test(expected = AssertionError.class)
 public void testVerificationWithNullMock() {
 mockitoCore.verify(null, Mockito.any(VerificationMode.class));
 }

 @Test(expected = AssertionError.class)
 public void testVerificationWithNotAMock() {
 Object notAMock = new Object();
 mockitoCore.verify(notAMock, Mockito.any(VerificationMode.class));
 }

 @Test
 public void testReset() {
 Object mockedObject = mockitoCore.mock(Object.class, new MockSettingsImpl());
 Object resetMockedObject = mockitoCore.verify(mockedObject).someMethod();
 mockitoCore.reset(mockedObject, resetMockedObject);
 verify(mockedObject, never()).someMethod();
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object[] mocks = new Object[]{mockObject1, mockObject2, mockObject3};
 mockitoCore.verifyNoMoreInteractions(mocks);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsWithNullMocks() {
 mockitoCore.verifyNoMoreInteractions((Object[]) null);
 }

 @Test
 public void testVerifyNoMoreInteractionsWithEmptyMocks() {
 mockitoCore.verifyNoMoreInteractions();
 }

 @Test
 public void testVerifyNoMoreInteractionsWithNotAMock() {
 Object notAMock = new Object();
 mockitoCore.verifyNoMoreInteractions(notAMock);
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 Object[] mocks = new Object[]{mockObject1, mockObject2, mockObject3};
 InOrder inOrder = inOrder(mocks);
 when(mockObject1.someMethod()).thenReturn("MockObject1");
 when(mockObject2.someMethod()).thenReturn("MockObject2");
 Object[] invocations = new Object[]{mockObject1, mockObject2};
 inOrder.verify(mockObject1).someMethod();
 inOrder.verify(mockObject2).someMethod();
 inOrder.verifyNoMoreInteractions();
 verifyNoMoreInteractions(mocks);
 }

 @Test
 public void testInOrder() {
 Object[] mocks = new Object[]{mockObject1, mockObject2, mockObject3};
 InOrder inOrder = mockitoCore.inOrder(mocks);
 when(mockObject1.someMethod()).thenReturn("MockObject1");
 when(mockObject2.someMethod()).thenReturn("MockObject2");
 inOrder.verify(mockObject1).someMethod();
 inOrder.verify(mockObject2).someMethod();
 }

 @Test
 public void testDoAnswer() {
 Object mockedObject = mockitoCore.mock(Object.class, new MockSettingsImpl());
 Answer<Object> answer = Answers.returnValues(new Object[]{new Object(), new Object()});
 Stubber stubber = mockitoCore.doAnswer(answer);
 stubber.doAnswer(Answers.callRealMethod());
 Object returnedValue1 = mockitoCore.verify(mockedObject).someMethod();
 Object returnedValue2 = mockitoCore.verify(mockedObject).someMethod();
 verify(mockedObject).someMethod().equals(returnedValue1);
 verify(mockedObject).someMethod().equals(returnedValue2);
 }

 @Test
 public void testStubVoid() {
 Object mockedObject = mockitoCore.mock(Object.class, new MockSettingsImpl());
 Stub<Void> stub = mockitoCore.stubVoid(mockedObject);
 stub.thenAnswer(Answers.always(null));
 mockitoCore.verify(mockedObject).someVoidMethod();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 Object mockedObject = mockitoCore.mock(Object.class, new MockSettingsImpl());
 OngoingStubbing<Object> stub = mockitoCore.when(mockedObject).someMethod();
 stub.thenReturn(new Object());
 Object returnedValue = mockitoCore.verify(mockedObject).someMethod();
 Invocation lastInvocation = mockitoCore.getLastInvocation();
 verify(mockedObject).someMethod().equals(returnedValue);
 verify(lastInvocation.getTarget()).equals(mockedObject);
 }
}