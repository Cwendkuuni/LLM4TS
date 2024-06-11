package org.mockito.internal.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentsMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private Reporter reporter;
 private MockUtil mockUtil;
 private MockingProgress mockingProgress;
 private Stubber stubber;
 private IOngoingStubbing ongoingStubbing;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 reporter = MockitoCore.newInstance().reporter;
 mockUtil = MockitoCore.newInstance().mockUtil;
 mockingProgress = MockitoCore.newInstance().mockingProgress;
 }

 @Test
 public void testMockCreation() {
 Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
 MockSettings mockSettings = new MockSettingsImpl();
 MockitoCore mockitoCore = MockitoCore.newInstance();

 T mockedMock = mockitoCore.mock(mockitoCoreClass, mockSettings);
 verify(mockitoCore).mock(eq(mockitoCoreClass), eq(mockSettings), isA(MockSettingsImpl.class));
 verifyZeroInteractions(mockedMock);
 }

 @Test
 public void testMockingProgress() {
 IOngoingStubbing stubbing = mockingProgress.pullOngoingStubbing();
 stubbing = mockitoCore.stub();
 stubbing = mockitoCore.when(any());
 verify(mockingProgress).validateState();
 verify(mockingProgress).resetOngoingStubbing();
 verify(mockingProgress).stubbingStarted();
 }

 @Test
 public void testStubbing() {
 Object mock = mock(Object.class);
 Stubber stubber = mockitoCore.stub();

 when(mock.toString()).thenReturn("MockitoCoreTest");

 String result = stubber.thenReturn("Test result");
 String expectedResult = "Test result";

 verify(mock).toString();
 verify(mock).toString(); // should not call the stubbed method again

 String actualResult = (String) result.getValue();
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = NotAMockException.class)
 public void testVerificationWithNull() {
 mockitoCore.verify(null, eq(VerificationMode.VERIFY_NO_MORE_INTERACTIONS));
 }

 @Test(expected = NotAMockException.class)
 public void testVerificationWithNonMock() {
 Object nonMock = new Object();
 mockitoCore.verify(nonMock, eq(VerificationMode.VERIFY_NO_MORE_INTERACTIONS));
 }

 @Test
 public void testVerification() {
 Object mock = mock(Object.class);
 when(mock.toString()).thenReturn("MockitoCoreTest");

 String result = (String) mockitoCore.verify(mock, eq(VerificationMode.RETURN_VALUE)).toString();
 assertEquals("MockitoCoreTest", result);
 }

 @Test
 public void testVerificationMode() {
 Object mock = mock(Object.class);
 when(mock.toString()).thenReturn("MockitoCoreTest");

 String result = (String) mockitoCore.verify(mock, eq(VerificationMode.THROW_EXCEPTION)).toString();
 assertFalse("Mock should not throw exception", result instanceof Throwable);
 }

 @Test
 public void testReset() {
 Object mock = mock(Object.class);
 when(mock.toString()).thenReturn("MockitoCoreTest");

 mockitoCore.reset(mock);
 verifyZeroInteractions(mock);
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object mock = mock(Object.class);
 when(mock.toString()).thenReturn("MockitoCoreTest");

 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test
 public void testVerifyNoMoreInteractionsWithArguments() {
 Object mock = mock(Object.class);
 when(mock.toString()).thenReturn("MockitoCoreTest");
 when(mock.method()).thenReturn(1);

 mockitoCore.verifyNoMoreInteractions(mock, argThat(isA(String.class)));
 }

 @Test
 public void testVerifyNoMoreInteractionsWithArgumentMatcher() {
 Object mock = mock(Object.class);
 when(mock.toString()).thenReturn("MockitoCoreTest");
 when(mock.method()).thenReturn(1);

 ArgumentMatcher<String> stringMatcher = argThat(isA(String.class));
 mockitoCore.verifyNoMoreInteractions(mock, stringMatcher);
 }

 @Test
 public void testVerifyNoMoreInteractionsWithMultipleMocks() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 when(mock1.toString()).thenReturn("Mock1");
 when(mock2.toString()).thenReturn("Mock2");

 mockitoCore.verifyNoMoreInteractions(mock1, mock2);
 }

 @Test
 public void testInOrder() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 when(mock1.toString()).thenReturn("Mock1");
 when(mock2.toString()).thenReturn("Mock2");

 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 verify(mock1).method1();
 verify(mock2).method2();
 inOrder.verify(mock1).method1();
 inOrder.verify(mock2).method2();
 }

 @Test
 public void testAnswer() {
 Object mock = mock(Object.class);
 Answer<String> answer = invocation -> "Answer: " + invocation.getMethodName();
 Stubber stubber = mockitoCore.doAnswer(answer);

 String result = stubber.thenAnswer(answer).method().toString();
 assertEquals("Answer: method", result);
 }

 @Test
 public void testVoidMethodStubbable() {
 Object mock = mock(Object.class);
 VoidMethodStubbable<Object> voidMethodStubbable = mockitoCore.stubVoid(mock);

 voidMethodStubbable.doNothing();
 verify(mock).doNothing();
 }

 @Test
 public void testGetLastInvocation() {
 Object mock = mock(Object.class);
 when(mock.toString()).thenReturn("MockitoCoreTest");

 IOngoingStubbing stubbing = mockitoCore.stub();
 Invocation lastInvocation = mockitoCore.getLastInvocation();

 verify(mock).toString();
 assertEquals("toString", lastInvocation.getMethod().getName());
 }
}