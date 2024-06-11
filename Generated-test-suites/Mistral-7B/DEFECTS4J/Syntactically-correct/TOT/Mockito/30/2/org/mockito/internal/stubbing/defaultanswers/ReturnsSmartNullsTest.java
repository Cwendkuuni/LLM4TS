import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;
 private MyMock mock;

 @Before
 public void setUp() {
 returnsSmartNulls = new ReturnsSmartNulls();
 mock = mock(MyMock.class, returnsSmartNulls);
 }

 @Test
 public void testStringMethodReturnsSmartNull() {
 String result = returnsSmartNulls.answer(mock, new MethodCall("toString"));
 assertEquals("SmartNull returned by unstubbed toString method on mock", result);
 }

 @Test
 public void testIntMethodReturnsNull() {
 int result = (int) returnsSmartNulls.answer(mock, new MethodCall("getIntMethod"));
 assertEquals(0, result);
 }

 @Test
 public void testMethodWithMultipleArgumentsReturnsNull() {
 int result = (int) returnsSmartNulls.answer(mock, new MethodCall("multipleArgumentsMethod", 1, "test"));
 assertEquals(0, result);
 }

 @Test
 public void testMethodWithFinalClassReturnsNull() {
 String result = (String) returnsSmartNulls.answer(mock, new MethodCall("finalClassMethod"));
 assertNull(result);
 }

 @Test(expected = NullPointerException.class)
 public void testMethodCallThrowsNullPointerException() {
 Object result = returnsSmartNulls.answer(mock, new MethodCall("throwNullPointerExceptionMethod"));
 }

 private class MethodCall {
 private Method method;

 MethodCall(String methodName) {
 try {
 method = MyMock.class.getMethod(methodName);
 } catch (NoSuchMethodException e) {
 throw new RuntimeException(e);
 }
 }

 MethodCall(String methodName, Object... arguments) {
 try {
 method = MyMock.class.getMethod(methodName, Arrays.asList(arguments).toArray(new Class[0]));
 } catch (NoSuchMethodException e) {
 throw new RuntimeException(e);
 }
 }

 public Method getMethod() {
 return method;
 }
 }
}