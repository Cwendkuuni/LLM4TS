package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

 private interface TestService {
 String methodWithNullReturn(String arg);
 }

 @Test
 public void testMethodWithNullReturn() {
 TestService testService = mock(TestService.class, new ReturnsSmartNulls());

 String result = testService.methodWithNullReturn(null);
 Assertions.assertEquals("SmartNull returned by unstubbed methodWithNullReturn(null) method on mock", result);
 }

 @Test
 public void testMethodWithNonNullReturn() {
 TestService testService = mock(TestService.class, new ReturnsSmartNulls());

 String result = testService.methodWithNullReturn("test");
 Assertions.assertEquals("test", result);
 }

 @Test
 public void testMethodWithNonMockableReturnType() {
 TestService testService = mock(TestService.class, new ReturnsSmartNulls());

 int result = testService.methodWithIntReturn();
 Assertions.assertNull(result);
 }

 @Test
 public void testMethodWithToStringMethod() {
 TestService testService = mock(TestService.class, new ReturnsSmartNulls());

 String result = testService.methodWithToStringMethod();
 Assertions.assertEquals("SmartNull returned by unstubbed methodWithToStringMethod() method on mock", result);
 }

 @Test
 public void testMethodWithArbitraryArguments() {
 TestService testService = mock(TestService.class, new ReturnsSmartNulls());

 Object[] args = new Object[]{1, 2, "three"};
 String result = testService.methodWithVarArgs(args);
 Assertions.assertEquals("SmartNull returned by unstubbed methodWithVarArgs(Object[]) method on mock", result);
 }

 @Test
 public void testMethodWithException() {
 TestService testService = mock(TestService.class, new ReturnsSmartNulls());

 try {
 testService.methodWithException();
 } catch (RuntimeException e) {
 String result = e.getMessage();
 Assertions.assertEquals("SmartNull returned by unstubbed methodWithException() method on mock", result);
 }
 }

 @Test
 public void testMethodWithStaticMethod() {
 TestService testService = mock(TestService.class, new ReturnsSmartNulls());

 String result = testService.methodWithStaticMethod();
 Assertions.assertEquals("SmartNull returned by unstubbed methodWithStaticMethod() method on mock", result);
 }

 @Test
 public void testMethodWithFinalClassReturnType() {
 TestService testService = mock(TestService.class, new ReturnsSmartNulls());

 Class<String> result = testService.methodWithFinalClassReturnType();
 Assertions.assertNull(result);
 }
}