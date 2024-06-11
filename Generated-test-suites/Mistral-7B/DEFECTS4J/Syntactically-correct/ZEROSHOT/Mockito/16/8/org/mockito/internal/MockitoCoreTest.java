package org.mockito.internal. tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.AnswerStub;
import org.mockito.stubbing.Stubber;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.inOrder;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private MockSettingsImpl mockSettings;
 private MockitoAnnotations mockitoAnnotations;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 mockSettings = new MockSettingsImpl();
 mockitoAnnotations = new MockitoAnnotations();
 }

 @Test
 public void testMockCreation() {
 Class<String> classToMock = String.class;
 String mock = mockitoCore.mock(classToMock, mockSettings, false);
 assert mock != null;
 }

 @Test
 public void testMockCreationWithSettings() {
 Class<String> classToMock = String.class;
 String mock = mockitoCore.mock(classToMock, mockSettings);
 assert mock != null;
 }

 @Test
 public void testStubbing() {
 String mock = mockitoCore.mock(String.class, mockSettings);
 Stubber stubber = mockitoCore.stub(mock);
 stubber.thenReturn("Hello, World!");

 String result = (String) mockitoCore.verify(mock).toString();
 assert result.equals("Hello, World!");
 }

 @Test
 public void testDeprecatedStubbing() {
 String mock = mockitoCore.mock(String.class, mockSettings);
 Stubber stubber = mockitoCore.stub(mock);
 stubber.thenReturn("Hello, World!");

 String result = (String) mockitoCore.stub(mock).thenReturn("Hello, World!");
 assert result.equals("Hello, World!");
 }

 @Test
 public void testOngoingStubbing() {
 String mock = mockitoCore.mock(String.class, mockSettings);
 Stubber stubber = mockitoCore.stub(mock);
 stubber.thenReturn("Hello, World!");
 stubber.thenReturn("Goodbye, World!");

 String firstResult = (String) mockitoCore.verify(mock).toString();
 assert firstResult.equals("Hello, World!");

 String secondResult = (String) mockitoCore.verify(mock).toString();
 assert secondResult.equals("Goodbye, World!");
 }

 @Test
 public void testVerify() {
 String mock = mockitoCore.mock(String.class, mockSettings);

 verify(mock).toString();
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNullMock() {
 mockitoCore.verify(null, VerificationMode.VERIFY_NO_MORE_INTERACTIONS);
 }

 @Test(expected = AssertionError.class)
 public void testNotAMockPassedToVerify() {
 Object notAMock = new Object();
 mockitoCore.verify(notAMock, VerificationMode.VERIFY_NO_MORE_INTERACTIONS);
 }

 @Test
 public void testReset() {
 String mock = mockitoCore.mock(String.class, mockSettings);
 Stubber stubber = mockitoCore.stub(mock);
 stubber.thenReturn("Hello, World!");

 mockitoCore.reset(mock);

 String result = (String) mockitoCore.verify(mock).toString();
 assert result == null;
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 String mock = mockitoCore.mock(String.class, mockSettings);
 Stubber stubber = mockitoCore.stub(mock);
 stubber.thenReturn("Hello, World!");

 mockitoCore.verifyNoMoreInteractions(mock);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsNullMock() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsNotAMock() {
 Object notAMock = new Object();
 mockitoCore.verifyNoMoreInteractions(notAMock);
 }

 @Test
 public void testInOrder() {
 String mock1 = mockitoCore.mock(String.class, mockSettings);
 String mock2 = mockitoCore.mock(String.class, mockSettings);

 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);

 given(mock1.toString()).willReturn("Mock 1");
 given(mock2.toString()).willReturn("Mock 2");

 inOrder.verify(mock1).toString();
 inOrder.verify(mock2).toString();
 }

 @Test
 public void testAnswer() {
 String mock = mockitoCore.mock(String.class, mockSettings);
 Answer answer = new AnswerStub() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return invocation.getArgumentAt(0, String.class);
 }
 };
 Stubber stubber = mockitoCore.doAnswer(answer);
 stubber.stub(mock).thenCallRealMethod();

 String result = (String) mockitoCore.verify(mock).toString("Test");
 assert result.equals("Test");
 }

 @Test
 public void testStubVoid() {
 String mock = mockitoCore.mock(String.class, mockSettings);
 mockitoCore.stubVoid(mock).andThrow(new RuntimeException("Test Exception"));

 try {
 mockitoCore.verify(mock).toString();
 } catch (RuntimeException e) {
 assert e.getMessage().equals("Test Exception");
 }
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 String mock = mockitoCore.mock(String.class, mockSettings);
 Stubber stubber = mockitoCore.stub(mock);
 stubber.thenReturn("Hello, World!");
 stubber.thenReturn("Goodbye, World!");

 Invocation invocation = mockitoCore.getLastInvocation();
 assert invocation != null;
 assert invocation.getMock() == mock;
 assert invocation.getMethod().getName().equals("toString");
 }
}