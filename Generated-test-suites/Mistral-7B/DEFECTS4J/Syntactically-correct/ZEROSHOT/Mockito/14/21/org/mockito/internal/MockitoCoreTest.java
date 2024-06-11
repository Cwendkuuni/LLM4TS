import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoCore;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 @Mock private Object mockObject;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 mockitoCore = new MockitoCore();
 }

 @Test
 public void testMockCreation() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 verify(mockitoCore).mockingStarted(mock, Object.class, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testMockCreationWithNull() {
 mockitoCore.mock(null, new MockSettingsImpl());
 }

 @Test
 public void testStubbing() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 OngoingStubbing stubbing = mockitoCore.stub();
 when(mock.method()).thenReturn("result");
 verify(stubbing).thenReturn("result");
 verify(mock).method().equals("result");
 }

 @Test
 public void testDeprecatedOngoingStubbing() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 DeprecatedOngoingStubbing<Object> stubbing = mockitoCore.stub(mock);
 when(mock.method()).thenReturn("result");
 verify(stubbing).thenReturn("result");
 verify(mock).method().equals("result");
 }

 @Test
 public void testOngoingStubbing() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 OngoingStubbing<Object> stubbing = mockitoCore.when(mock);
 when(mock.method()).thenReturn("result");
 verify(stubbing).thenReturn("result");
 verify(mock).method().equals("result");
 }

 @Test
 public void testVerification() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 verify(mock).method();
 }

 @Test(expected = NotAMockException.class)
 public void testVerificationWithNull() {
 mockitoCore.verify(null, Mockito.any());
 }

 @Test
 public void testVerificationMode() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 verify(mock).method().times(1);
 }

 @Test
 public void testVerificationModeTimes() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 verify(mock).method().times(2);
 }

 @Test
 public void testVerificationModeNever() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 verify(mock).method().never();
 }

 @Test
 public void testVerificationModeAtLeastOnce() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 verify(mock).method().atLeastOnce();
 }

 @Test
 public void testVerificationModeAtLeastOnceWithTimes() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 verify(mock).method().atLeast(2, ArgumentMatchers.anyInt());
 }

 @Test
 public void testVerificationModeAtLeastOnceWithArguments() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 verify(mock).method(ArgumentMatchers.eq("expectedArgument")).atLeastOnce();
 }

 @Test
 public void testVerificationOrder() {
 Object mock1 = mockitoCore.mock(Object1.class, new MockSettingsImpl());
 Object mock2 = mockitoCore.mock(Object2.class, new MockSettingsImpl());
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 when(mock1.method1()).thenReturn("result1");
 when(mock2.method2()).thenReturn("result2");
 inOrder.verify(mock1).method1();
 inOrder.verify(mock2).method2();
 }

 @Test
 public void testVerificationOrderWithMultipleInvocations() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 when(mock.method()).thenReturn("result1").thenReturn("result2");
 InOrder inOrder = mockitoCore.inOrder(mock);
 inOrder.verify(mock).method().twice();
 }

 @Test
 public void testVerificationNoMoreInteractions() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 verifyNoMoreInteractions(mock);
 }

 @Test(expected = NotAMockException.class)
 public void testVerificationNoMoreInteractionsWithNull() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test
 public void testVerificationNoMoreInteractionsWithList() {
 Object mock1 = mockitoCore.mock(Object1.class, new MockSettingsImpl());
 Object mock2 = mockitoCore.mock(Object2.class, new MockSettingsImpl());
 List<Object> mocks = Arrays.asList(mock1, mock2);
 mockitoCore.verifyNoMoreInteractions(mocks);
 }

 @Test
 public void testVerificationNoMoreInteractionsInOrder() {
 Object mock1 = mockitoCore.mock(Object1.class, new MockSettingsImpl());
 Object mock2 = mockitoCore.mock(Object2.class, new MockSettingsImpl());
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 when(mock1.method1()).thenReturn("result1");
 when(mock2.method2()).thenReturn("result2");
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
 }

 @Test
 public void testReset() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 mockitoCore.reset(mock);
 verifyZeroInteractions(mockitoCore.mock(Object.class, new MockSettingsImpl()));
 }

 @Test
 public void testAnswer() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 Answer<String> answer = ArgumentCaptor.forClass(String.class).capture(ArgumentMatchers.argument(0));
 when(mock.method()).thenAnswer(answer);
 verify(mock).method();
 String result = answer.getValue();
 assertEquals("expectedResult", result);
 }

 @Test
 public void testVoidMethodStubbable() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 VoidMethodStubbable<Object> stubbing = mockitoCore.stubVoid(mock);
 stubbing.thenCallRealMethod();
 stubbing.stub();
 verify(mock).method();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 when(mock.method()).thenReturn("result");
 Invocation lastInvocation = mockitoCore.getLastInvocation();
 assertEquals("result", lastInvocation.getArgument(0));
 }
}