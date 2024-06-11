import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 @Mock
 private Object mockObject1;
 @Mock
 private Object mockObject2;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 mockitoCore = new MockitoCore();
 }

 @Test
 public void testMockCreation() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 Mockito.verify(mockitoCore).mockingStarted(mock, Object.class, new MockSettingsImpl());
 }

 @Test
 public void testStubbing() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 OngoingStubbing<Object> ongoingStubbing = mockitoCore.stub(mock);
 ongoingStubbing.thenReturn("test");
 String result = (String) mock.someMethod();
 Mockito.verify(mockitoCore).stubbingStarted();
 Mockito.verify(mock).someMethod();
 Mockito.verify(mock).someMethod(); // returns "test"
 }

 @Test
 public void testVerification() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 when(mock.someMethod()).thenReturn("test");
 verify(mock).someMethod();
 Mockito.verifyNoMoreInteractions(mock);
 }

 @Test(expected = AssertionError.class)
 public void testVerificationNullMock() {
 verify(null, VerificationMode.times(1));
 }

 @Test(expected = AssertionError.class)
 public void testVerificationNonMock() {
 Object nonMock = new Object();
 verify(nonMock, VerificationMode.times(1));
 }

 @Test
 public void testReset() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 when(mock.someMethod()).thenReturn("test");
 mockitoCore.reset(mock);
 verify(mockitoCore).reset();
 verifyNoMoreInteractions(mock);
 }

 @Test
 public void testVerificationNoMoreInteractions() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 verifyNoMoreInteractions(mock);
 }

 @Test(expected = AssertionError.class)
 public void testVerificationNoMoreInteractionsNullMock() {
 verifyNoMoreInteractions(null);
 }

 @Test(expected = AssertionError.class)
 public void testVerificationNoMoreInteractionsNonMock() {
 Object nonMock = new Object();
 verifyNoMoreInteractions(nonMock);
 }

 @Test
 public void testVerificationOrder() {
 Object mockObject1 = mockitoCore.mock(Object.class, new MockSettingsImpl());
 Object mockObject2 = mockitoCore.mock(Object.class, new MockSettingsImpl());
 InOrder inOrder = inOrder(mockObject1, mockObject2);
 when(mockObject1.someMethod()).thenReturn(1);
 when(mockObject2.someMethod()).thenReturn(2);

 Object result = null;
 try {
 result = ((Object) mockObject1).invokeMethod("someOtherMethod", mockObject2);
 } catch (Exception e) {
 // expected
 }

 inOrder.verify(mockObject1).someMethod();
 inOrder.verify(mockObject2).someMethod();
 inOrder.verify(mockObject1).someOtherMethod(mockObject2);
 }

 @Test
 public void testAnswer() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 when(mock.someMethod()).thenAnswer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "test";
 }
 });
 String result = (String) mock.someMethod();
 Mockito.verify(mockitoCore).stubbingStarted();
 Mockito.verify(mock).someMethod();
 Mockito.verify(mock).someMethod(); // returns "test"
 }

 @Test
 public void testStubbingVoidMethod() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 Object stub = mockitoCore.stubVoid(mock);
 stub.andReturn("test");
 mock.someVoidMethod();
 Mockito.verify(mockitoCore).stubbingStarted();
 Mockito.verify(mock).someVoidMethod();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }
}