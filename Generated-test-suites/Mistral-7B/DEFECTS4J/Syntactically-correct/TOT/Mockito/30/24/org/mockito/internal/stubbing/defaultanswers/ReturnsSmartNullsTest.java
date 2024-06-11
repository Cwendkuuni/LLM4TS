import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class ReturnsSmartNullsTest {

 private Object mock;

 @Test
 public void testReturnsSmartNullForUnstubbedMethod() {
 mock = mock(Object.class, new ReturnsSmartNulls());
 mock.toString(); // should return "SmartNull returned by unstubbed toString() method on mock"
 }

 @Test
 public void testReturnsDefaultValueForStubbedMethod() {
 Answer<String> answer = Mockito.mock(Answer.class);
 mock = mock(Object.class, answer);
 Mockito.doReturn("hello").when(mock).toString();
 assertEquals("hello", mock.toString());
 }

 @Test
 public void testReturnsNullForFinalClass() {
 mock = mock(String.class, new ReturnsSmartNulls());
 verify(mock).toString(); // should return null
 }

 @Test(expected = NullPointerException.class)
 public void testNullPointerExceptionThrownForUnstubbedMethodWithNullArgument() {
 mock = mock(Object.class, new ReturnsSmartNulls());
 mock.toString(null); // should throw NPE with smart null message
 }

 @Test
 public void testToStringMethodInterceptorIsCalledForUnstubbedMethod() throws Exception {
 mock = mock(Object.class, new ReturnsSmartNulls());
 Method method = mock.getClass().getDeclaredMethod("toString");
 method.setAccessible(true);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMethod(method);
 invocation.setArguments(new Object[]{});
 Object result = mock.answer(invocation);
 assertEquals("SmartNull returned by unstubbed toString() method on mock", result.toString());
 }

 @Test
 public void testReturnsSmartNullForUnstubbedMethodWithCustomMessage() {
 mock = mock(Object.class, new ReturnsSmartNulls());
 mock.toString = (Answer<String>) Mockito.mock(Answer.class);
 Mockito.doThrow(new RuntimeException("custom message")).when(mock.toString).thenAnswer((Answer<String>) mock.toString);
 assertEquals("SmartNull returned by unstubbed toString() method on mock with custom message: custom message", mock.toString());
 }
}