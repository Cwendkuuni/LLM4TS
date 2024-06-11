package org.mockito.internal.test.mockhandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doCallRealMethod;

public class MockHandlerTest {

 private MockHandler<MockitoTestSubject> mockHandler;
 private MockedMockitoTestSubject mockedMock;
 private RealMockitoTestSubject realMock;

 @Before
 public void setup() {
 mockedMock = mock(MockitoTestSubject.class);
 realMock = new RealMockitoTestSubject();
 mockHandler = new MockHandler<>(new MockSettingsImpl());
 }

 @Test
 public void testHandle_whenStubbingVoidMethod_thenReturnNull() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

 doAnswer(invocation1 -> {
 // stubbing void methods with stubVoid() or doAnswer() style
 }).when(mockedMock).doSomething();

 mockHandler.handle(invocation);

 // assert that handle() method returns null
 assertNull(mockHandler.handle(invocation));
 }

 @Test
 public void testHandle_whenVerifyingInvocations_thenVerifySuccess() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(mockedMock.doSomethingElse()).thenCallRealMethod();

 // verify() call with matchers
 verify(mockedMock).doSomethingElse();

 // verify() call with no matchers
 verify(mockedMock).doSomethingElse();
 }

 @Test
 public void testHandle_whenVerifyingMultipleInvocationsWithSameMatcher_thenVerifySuccess() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
 when(mockedMock.doSomethingElse(ArgumentMatchers.eq("test"))).thenCallRealMethod();

 // verify() call with matchers
 verify(mockedMock).doSomethingElse(ArgumentMatchers.eq("test")).times(2);

 // verify() call with no matchers
 verify(mockedMock).doSomethingElse();
 }

 @Test
 public void testHandle_whenVerifyingMultipleInvocationsWithDifferentMatchers_thenVerifySuccess() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 ArgumentCaptor<String> stringArgumentCaptor1 = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
 when(mockedMock.doSomethingElse(ArgumentMatchers.eq("test"))).thenCallRealMethod();
 when(mockedMock.doSomethingElse(ArgumentMatchers.eq("test2"), ArgumentMatchers.eq(1))).thenCallRealMethod();

 // verify() calls with matchers
 verify(mockedMock).doSomethingElse(ArgumentMatchers.eq("test"));
 verify(mockedMock).doSomethingElse(ArgumentMatchers.eq("test2"), ArgumentMatchers.eq(1));

 // verify() calls with no matchers
 verify(mockedMock).doSomethingElse();
 }

 @Test
 public void testHandle_whenVerifyingNoInteractions_thenVerifyNoInteractions() {
 ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
 when(mockedMock.doSomethingElse(stringArgumentCaptor.capture())).thenCallRealMethod();

 // verify() call with no interactions
 verifyZeroInteractions(mockedMock);

 // assert that argument captor contains no arguments
 assertEmpty(stringArgumentCaptor.getAllValues());
 }

 @Test
 public void testHandle_whenVerifyingInteractionsInOrder_thenVerifyInteractionsInOrder() {
 InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
 InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
 ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
 when(mockedMock.doSomethingElse(stringArgumentCaptor.capture())).thenCallRealMethod();

 // verify() calls in order
 verify(mockedMock).doSomethingElse(ArgumentMatchers.eq("test1"));
 verify(mockedMock).doSomethingElse(ArgumentMatchers.eq("test2"));

 // assert that argument captor contains the expected arguments
 List<String> strings = new ArrayList<>(stringArgumentCaptor.getAllValues());
 assertEquals("test1", strings.get(0));
 assertEquals("test2", strings.get(1));
 }

 @Test
 public void testHandle_whenVerifyingInteractionsOutOfOrder_thenVerifyInteractionsOutOfOrder() {
 InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
 InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
 ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
 when(mockedMock.doSomethingElse(stringArgumentCaptor.capture())).thenCallRealMethod();

 // verify() calls out of order
 verify(mockedMock).doSomethingElse(ArgumentMatchers.eq("test2"));
 verify(mockedMock).doSomethingElse(ArgumentMatchers.eq("test1"));

 // assert that argument captor contains the expected arguments
 List<String> strings = new ArrayList<>(stringArgumentCaptor.getAllValues());
 assertEquals("test1", strings.get(0));
 assertEquals("test2", strings.get(1));
 }

 @Test
 public void testHandle_whenVerifyingNoMoreInteractions_thenVerifyNoMoreInteractions() {
 InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
 InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
 ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
 when(mockedMock.doSomethingElse(stringArgumentCaptor.capture())).thenCallRealMethod();

 // verify() calls
 verify(mockedMock).doSomethingElse(ArgumentMatchers.eq("test1"));
 verify(mockedMock).doSomethingElse(ArgumentMatchers.eq("test2"));

 // verifyNoMoreInteractions() call
 verifyNoMoreInteractions(mockedMock);

 // assert that argument captor contains the expected arguments
 List<String> strings = new ArrayList<>(stringArgumentCaptor.getAllValues());
 assertEquals("test1", strings.get(0));
 assertEquals("test2", strings.get(1));
 }

 @Test
 public void testHandle_whenThrowingAnException_thenThrowTheSameException() throws Exception {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(mockedMock.doSomethingElse()).thenThrow(new RuntimeException("Test Exception"));

 try {
 mockHandler.handle(invocation);
 fail("Exception expected");
 } catch (RuntimeException e) {
 assertEquals("Test Exception", e.getMessage());
 }
 }

 @Test
 public void testHandle_whenReturningANull_thenReturnANull() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(mockedMock.doSomethingElse()).thenReturn(null);

 assertNull(mockHandler.handle(invocation));
 }

 @Test
 public void testHandle_whenReturningAValue_thenReturnTheSameValue() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(mockedMock.doSomethingElse()).thenReturn("Test Value");

 assertEquals("Test Value", mockHandler.handle(invocation));
 }

 @Test
 public void testHandle_whenUsingOngoingStubbing_thenReturnTheAnswer() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(mockedMock.doSomethingElse()).thenAnswer(i -> "Test Value");

 assertEquals("Test Value", mockHandler.handle(invocation));
 }

 @Test
 public void testHandle_whenUsingDifferentMockHandlerInstances_thenEachInstanceHandlesInvocationsIndependently() {
 MockHandler<MockitoTestSubject> mockHandler1 = new MockHandler<>(new MockSettingsImpl());
 MockHandler<MockitoTestSubject> mockHandler2 = new MockHandler<>(new MockSettingsImpl());
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

 mockHandler1.handle(invocation);
 mockHandler2.handle(invocation);

 // verify that both instances handle the same invocation independently
 verify(mockedMock).doSomethingElse();
 }
}

class RealMockitoTestSubject {
 // real method implementation
 public String doSomethingElse(String argument) {
 return argument.toUpperCase();
 }
}

class MockitoTestSubject {
 // interface or abstract class implementation
 public String doSomethingElse(String argument);
}