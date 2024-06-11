package org.mockito.internal.stubbing.answers.test;

 import org.junit.Test;
 import org.mockito.ArgumentMatchers;
 import org.mockito.Mockito;
 import org.mockito.invocation.InvocationOnMock;

 import static org.mockito.Mockito.mock;

 public class CallsRealMethodsTest {

 private CallsRealMethods callRealMethods = new CallsRealMethods();

 @Test
 public void testCallRealMethodWhenMethodExists() {
 Callable callable = mock(Callable.class);
 Object result = callable.call();

 Mockito.doAnswer(callRealMethods).when(callable).method1();
 Mockito.doAnswer(callRealMethods).when(callable).method2();

 callable.method1();
 callable.method2();

 Mockito.verify(callable, Mockito.times(2)).method1();
 Mockito.verify(callable, Mockito.times(2)).method2();
 Mockito.verify(callable, Mockito.never()).call(); // should not be called since it is not stubbed

 }

 @Test
 public void testCallRealMethodWhenMethodDoesNotExist() {
 Callable callable = mock(Callable.class);

 Mockito.doAnswer(callRealMethods).when(callable).method3(); // method3 does not exist in Callable interface

 callable.method1();
 callable.method2();

 // no verification since method3 does not exist
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCallRealMethodWhenMethodThrowsException() {
 Callable callable = mock(Callable.class);

 Mockito.doAnswer(callRealMethods).when(callable).method4(); // method4 throws UnsupportedOperationException

 callable.method4();
 }

 class Callable implements Runnable {
 public void method1() {
 // implementation
 }

 public void method2() {
 // implementation
 }

 public void method3() {
 // does not exist
 }

 public void method4() throws UnsupportedOperationException {
 throw new UnsupportedOperationException();
 }

 public Object call() {
 // implementation
 }
 }

 }