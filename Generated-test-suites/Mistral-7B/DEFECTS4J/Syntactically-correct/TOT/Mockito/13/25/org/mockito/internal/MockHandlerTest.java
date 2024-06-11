import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<Foo> mockHandler;
 private Foo mock;

 @Before
 public void setUp() {
 mock = mock(Foo.class);
 mockHandler = new MockHandler<>(mock.getClass());
 }

 @Test
 public void testHandleVoidMethodCall() {
 // Given
 when(mock.voidMethod()).thenCallRealMethod();
 // When
 mockHandler.handle(new InvocationOnMock(mock, "voidMethod", new Object[0]));
 // Then
 // no verification or stubbing should be performed since voidMethod is not stubbed
 // and no verification mode was pulled
 // Verify that the real method was called
 verify(mock).voidMethod();
 }

 @Test
 public void testHandleMethodCallWithAnswer() {
 // Given
 when(mock.methodWithReturnType(int.class)).thenReturn(42);
 // When
 int result = (int) mockHandler.handle(new InvocationOnMock(mock, "methodWithReturnType", new Object[0]));
 // Then
 // verify that the answer was returned
 verify(mock).methodWithReturnType(ArgumentMatchers.eq(ArgumentMatchers.isNull()));
 assertEquals(42, result);
 }

 @Test
 public void testHandleMethodCallWithAnswerThatThrowsException() {
 // Given
 when(mock.methodThatThrowsException()).thenThrow(new RuntimeException("Exception message"));
 // When
 try {
 mockHandler.handle(new InvocationOnMock(mock, "methodThatThrowsException", new Object[0]));
 } catch (Throwable e) {
 // Then
 // verify that the exception was thrown
 verify(mock).methodThatThrowsException(ArgumentMatchers.eq(ArgumentMatchers.isNull()));
 throw e;
 }
 }

 @Test
 public void testHandleMethodCallWithStub() {
 // Given
 when(mock.methodWithParameter(int.class)).thenAnswer(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return 42;
 }
 });
 // When
 int result = (int) mockHandler.handle(new InvocationOnMock(mock, "methodWithParameter", new Object[]{3}));
 // Then
 // verify that the invocation was stubbed
 verify(mock).methodWithParameter(ArgumentMatchers.eq(3));
 assertEquals(42, result);
 }

 @Test
 public void testHandleMethodCallWithVerification() {
 // Given
 when(mock.methodWithParameter(int.class)).thenReturn(42);
 // When
 mockHandler.handle(new InvocationOnMock(mock, "methodWithParameter", new Object[]{3}));
 // Then
 // verify that the invocation was verified
 verify(mock).methodWithParameter(ArgumentMatchers.eq(3));
 }

 @Test
 public void testHandleMethodCallWithMultipleVerifications() {
 // Given
 when(mock.methodWithParameter(int.class)).thenReturn(42);
 // When
 mockHandler.handle(new InvocationOnMock(mock, "methodWithParameter", new Object[]{3}));
 mockHandler.handle(new InvocationOnMock(mock, "methodWithParameter", new Object[]{5}));
 // Then
 // verify that both invocations were verified
 verify(mock).methodWithParameter(ArgumentMatchers.eq(3));
 verify(mock).methodWithParameter(ArgumentMatchers.eq(5));
 }

 @Test
 public void testHandleMethodCallWithMultipleStubs() {
 // Given
 when(mock.methodWithParameter(int.class)).thenAnswer(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 if (invocation.getArguments()[0] instanceof Integer && ((Integer) invocation.getArguments()[0]).intValue() == 3) {
 return "Three";
 } else if (invocation.getArguments()[0] instanceof Integer && ((Integer) invocation.getArguments()[0]).intValue() == 5) {
 return "Five";
 }
 return "Default";
 }
 });
 // When
 String result1 = (String) mockHandler.handle(new InvocationOnMock(mock, "methodWithParameter", new Object[]{3}));
 String result2 = (String) mockHandler.handle(new InvocationOnMock(mock, "methodWithParameter", new Object[]{5}));
 // Then
 // verify that both invocations were verified
 verify(mock).methodWithParameter(ArgumentMatchers.eq(3));
 verify(mock).methodWithParameter(ArgumentMatchers.eq(5));
 // Verify that the correct answers were returned
 assertEquals("Three", result1);
 assertEquals("Five", result2);
 }
}

class Foo {
 void voidMethod() {}
 int methodWithReturnType(int i) { return i; }
 int methodWithParameter(int i) { return i; }
 int methodThatThrowsException() throws Exception { throw new Exception(); }
}