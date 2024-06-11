import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;
 private Object unstubbedMock;

 @Before
 public void setup() {
 unstubbedMock = mock(UnstubbedMock.class);
 returnsSmartNulls = new ReturnsSmartNulls();
 }

 @Test
 public void testReturnsSmartNullForNullReturnFromDelegate() {
 // Given
 Mockito.when(unstubbedMock.unstubbedMethod()).thenAnswer(new ReturnsMoreEmptyValues());
 // When
 Object result = returnsSmartNulls.answer(Mockito.argThat(i -> i.getMethod().getName().equals("unstubbedMethod")));
 // Then
 verify(unstubbedMock).unstubbedMethod(); // Unstubbed method was called
 assertNull(result);
 // And verify that smart null was returned instead of null
 verify(unstubbedMock).toString();
 }

 @Test
 public void testReturnsSmartNullForNonMockableType() {
 // Given
 Object nonMockableType = new Object() {};
 // When
 returnsSmartNulls.answer(Mockito.argThat(i -> i.getMethod().getReturnType().equals(nonMockableType.getClass())));
 // Then
 assertNull(returnsSmartNulls.answer(Mockito.argThat(i -> i.getMethod().getReturnType().equals(nonMockableType.getClass()))));
 }

 @Test
 public void testToStringMethodIsCalledWhenSmartNullIsReturned() {
 // Given
 String expectedToString = "SmartNull returned by unstubbed method on mock";
 // When
 Object result = returnsSmartNulls.answer(Mockito.argThat(i -> i.getMethod().getName().equals("toString")));
 // Then
 assertEquals(expectedToString, result.toString());
 }

 @Test(expected = org.mockito.exceptions.SmartNullPointerException.class)
 public void testSmartNullPointerExceptionIsThrownWhenInvokingToStringMethodOnUnstubbedMethod() {
 // Given
 String unstubbedMethodName = "unstubbedMethod";
 // When
 returnsSmartNulls.answer(Mockito.argThat(i -> i.getMethod().getName().equals(unstubbedMethodName)));
 // Then
 String unstubbedMethodCall = unstubbedMethodName + "()";
 throwsException(SmartNullPointerException.class, () -> returnsSmartNulls.answer(Mockito.argThat(i -> i.getMethod().getName().equals(unstubbedMethodName))));
 // Verify that smart null pointer exception was thrown for the correct method call
 verify(System.err).println("java.lang.reflect.InvocationTargetException: SmartNullPointerException: SmartNull returned by unstubbed " + unstubbedMethodCall + " method on mock");
 }

 private void throwsException(Class<? extends Throwable> expectedException, Runnable action) {
 try {
 action.run();
 } catch (Throwable e) {
 if (expectedException.isInstance(e)) {
 throw e;
 }
 }
 throw new AssertionError("Expected exception of type " + expectedException.getName() + " but got " + e.getClass().getName());
 }
}

class UnstubbedMock {
 void toString() {}
 void unstubbedMethod() {}
}