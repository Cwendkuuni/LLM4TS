import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<DummyClass> mockHandler;
 private DummyClass dummyClass;

 @Before
 public void setup() {
 dummyClass = Mockito.mock(DummyClass.class);
 mockHandler = new MockHandler<>(new MockSettingsImpl());
 }

 @Test
 public void testHandleVoidMethodInvocation() {
 // Given
 when(dummyClass.voidMethod()).thenCallRealMethod();

 // When
 mockHandler.handle(new InvocationOnMock(dummyClass).withMethodCall("voidMethod"));

 // Then
 verifyNoMoreInteractions(dummyClass);
 }

 @Test
 public void testHandleMethodInvocationWithAnswer() {
 // Given
 final int expectedResult = 42;
 when(dummyClass.methodWithAnswer(ArgumentMatchers.anyInt()))
 .thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return expectedResult;
 }
 });

 // When
 int result = (int) mockHandler.handle(new InvocationOnMock(dummyClass).withMethodCall("methodWithAnswer", 1));

 // Then
 verify(dummyClass).methodWithAnswer(ArgumentMatchers.eq(1));
 assertEquals(expectedResult, result);
 }

 @Test
 public void testHandleMethodInvocationWithVerification() {
 // Given
 final int expectedInvocationCount = 3;
 when(dummyClass.methodWithVerification()).thenCallRealMethod();

 // When
 for (int i = 0; i < expectedInvocationCount; i++) {
 mockHandler.handle(new InvocationOnMock(dummyClass).withMethodCall("methodWithVerification"));
 }

 // Then
 verify(dummyClass, Mockito.times(expectedInvocationCount)).methodWithVerification();
 }

 @Test(expected = RuntimeException.class)
 public void testHandleMethodInvocationWithError() {
 // Given
 when(dummyClass.methodWithError()).thenThrow(new RuntimeException("Expected error"));

 // When
 // Then an exception should be thrown
 mockHandler.handle(new InvocationOnMock(dummyClass).withMethodCall("methodWithError"));
 }

 @Test
 public void testHandleMethodInvocationWithMultipleAnswers() {
 // Given
 final List<Answer<Integer>> answers = new ArrayList<>();
 answers.add(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 1;
 }
 });
 answers.add(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 2;
 }
 });
 when(dummyClass.methodWithMultipleAnswers(ArgumentMatchers.anyInt()))
 .thenAnswer(AnswerUtils.answer(answers));

 // When
 List<Integer> results = new ArrayList<>();
 for (int i = 0; i < 2; i++) {
 results.add((int) mockHandler.handle(new InvocationOnMock(dummyClass).withMethodCall("methodWithMultipleAnswers", 1)));
 }

 // Then
 verify(dummyClass, Mockito.times(2)).methodWithMultipleAnswers(ArgumentMatchers.eq(1));
 assertEquals(1, results.get(0));
 assertEquals(2, results.get(1));
 }
}

class AnswerUtils {
 static <T> Answer<T> answer(List<Answer<T>> answers) {
 return invocation -> {
 for (Answer<T> answer : answers) {
 try {
 return answer.answer(invocation);
 } catch (Throwable cause) {
 throw new RuntimeException("Answer execution failed", cause);
 }
 }
 throw new IllegalStateException("Answers should provide an answer");
 };
 }
}