import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<DummyClass> mockHandler;
 private DummyClass dummyClass;
 private DummyClass spyDummyClass;

 @Before
 public void setUp() {
 mockHandler = new MockHandler<>();
 dummyClass = mock(DummyClass.class);
 spyDummyClass = mock(DummyClass.class, Mockito.CALLS_REAL_METHODS);
 }

 @Test
 public void testHandleVoidInvocationDuringStubbing() {
 // Given
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(mockHandler.voidMethodStubbable(dummyClass).voidMethod()).thenReturn(new Answer<Void>() {
 @Override
 public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
 // Stubbing void method
 System.out.println("Void method stubbed.");
 return null;
 }
 });

 // When
 mockHandler.handle(invocation);

 // Then
 // Verify that the void method was stubbed
 verify(dummyClass).voidMethod();
 }

 @Test
 public void testHandleVerification() {
 // Given
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(dummyClass.method1()).thenReturn("Return value from method1.");

 // When
 mockHandler.handle(invocation);

 // Then
 // Verify that the method1 was called and returned the expected value
 verify(dummyClass).method1();
 verify(dummyClass).method1().thenReturn("Return value from method1.");
 }

 @Test(expected = AssertionError.class)
 public void testHandleVerificationOnSpy() {
 // Given
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(spyDummyClass.method1()).thenReturn("Return value from method1.");

 // When
 mockHandler.handle(invocation);

 // Then
 // Verify that the method1 was called and returned the expected value
 verify(spyDummyClass).method1();

 // But when we try to verify the stubbing on a spy, an AssertionError should be thrown
 verify(spyDummyClass).method1().thenReturn("Return value from method1.");
 }

 @Test
 public void testHandleInvocationAfterStubbing() {
 // Given
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(dummyClass.method1()).thenReturn("Return value from method1.");

 // When
 String result = (String) mockHandler.handle(invocation);

 // Then
 // Verify that the method1 was called and returned the expected value
 verify(dummyClass).method1();

 // Check that the result is the expected value
 assertEquals("Return value from method1.", result);
 }

 @Test
 public void testSetAnswersForStubbing() {
 // Given
 List<Answer> answers = new ArrayList<>();
 answers.add(answers -> answers.add("Answer 1"));
 answers.add(answers -> answers.add("Answer 2"));

 mockHandler.setAnswersForStubbing(answers);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

 // When
 mockHandler.handle(invocation);

 // Then
 // Verify that the answers were used in the correct order
 verify(dummyClass).method1();
 List<String> capturedAnswers = invocation.getArguments()[0];
 assertEquals(2, capturedAnswers.size());
 assertEquals("Answer 1", capturedAnswers.get(0));
 assertEquals("Answer 2", capturedAnswers.get(1));
 }
}

class DummyClass {
 public void voidMethod() {}
 public String method1() { return "Return value from method1."; }
}