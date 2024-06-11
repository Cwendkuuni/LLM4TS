package org.mockito.internal.stubbing.answers.test;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class CallsRealMethodsTest {

 private CallsRealMethods callsRealMethods;
 private Object realObject;

 @Before
 public void beforeEach() {
 callsRealMethods = new CallsRealMethods();
 realObject = mock(Object.class);
 }

 @Test
 public void testCallRealMethodWhenMethodIsNotStubbed() {
 // given
 when(realObject.method1()).thenAnswer(callsRealMethods);

 // when
 Object result = realObject.method1();

 // then
 verify(realObject).method1(); // real method was called
 assertEquals(result, result); // the result is the one returned by the real method
 }

 @Test
 public void testCallRealMethodReturnsNullWhenInvocationThrowsANullPointerException() {
 // given
 when(realObject.method1()).thenAnswer(callsRealMethods);
 doThrow(NullPointerException.class).when(realObject).method2();

 // when
 Object result = realObject.method1();

 // then
 verify(realObject).method1(); // real method was called
 assertNull(result); // null pointer exception was thrown and CallsRealMethods didn't catch it
 }

 @Test
 public void testCallRealMethodWhenMethodThrowsAnException() {
 // given
 when(realObject.method1()).thenAnswer(callsRealMethods);
 doThrow(RuntimeException.class).when(realObject).method2();

 // when
 try {
 realObject.method1();
 } catch (Exception e) {
 // then
 verify(realObject).method1(); // real method was called
 throw e;
 }

 // then
 verify(realObject).method2(); // the exception was thrown by the real method
 }

 @Test
 public void testCallRealMethodWhenMethodIsAnInterfaceMethod() {
 // given
 Object mockInterface = mock(Object.class);
 CallsRealMethods callsRealMethods = new CallsRealMethods();
 Interface interfaceInstance = (Interface) mockInterface;
 when(mockInterface.method()).thenAnswer(callsRealMethods);
 Interface expected = new Implementation();

 // when
 Object result = interfaceInstance.method();

 // then
 assertEquals(expected, result); // the result is the one returned by the real method
 verify(interfaceInstance).method(); // real method was called
 }

 @Test
 public void testCallRealMethodWhenMethodIsAnAbstractClassMethod() {
 // given
 Object mockAbstractClass = mock(AbstractClass.class);
 CallsRealMethods callsRealMethods = new CallsRealMethods();
 AbstractClass abstractClassInstance = (AbstractClass) mockAbstractClass;
 when(mockAbstractClass.method()).thenAnswer(callsRealMethods);
 AbstractClass.Expected expected = new AbstractClass.Implementation();

 // when
 Object result = abstractClassInstance.method();

 // then
 assertEquals(expected, result); // the result is the one returned by the real method
 verify(abstractClassInstance).method(); // real method was called
 }

 @Test
 public void testCallRealMethodWhenMethodIsPrivate() {
 // given
 Object mockPrivateClass = mock(PrivateClass.class);
 CallsRealMethods callsRealMethods = new CallsRealMethods();
 PrivateClass privateClassInstance = (PrivateClass) mockPrivateClass;
 ArgumentCaptor<PrivateMethodArgument> argumentCaptor = ArgumentCaptor.forClass(PrivateMethodArgument.class);

 // when
 privateClassInstance.publicMethod(argumentCaptor.capture());

 // then
 invokePrivate(privateClassInstance, "privateMethod", argumentCaptor.getValue());
 verify(privateClassInstance).privateMethod(argumentCaptor.getValue()); // real method was called
 }

 @Test
 public void testCallRealMethodWhenMethodIsProtected() {
 // given
 Object mockProtectedClass = mock(ProtectedClass.class);
 CallsRealMethods callsRealMethods = new CallsRealMethods();
 ProtectedClass protectedClassInstance = (ProtectedClass) mockProtectedClass;
 ArgumentCaptor<ProtectedMethodArgument> argumentCaptor = ArgumentCaptor.forClass(ProtectedMethodArgument.class);

 // when
 protectedClassInstance.publicMethod(argumentCaptor.capture());

 // then
 invokeProtected(protectedClassInstance, "protectedMethod", argumentCaptor.getValue());
 verify(protectedClassInstance).protectedMethod(argumentCaptor.getValue()); // real method was called
 }

 @Test
 public void testCallRealMethodWhenMethodIsStatic() {
 // given
 Object mockStaticClass = mock(StaticClass.class);
 CallsRealMethods callsRealMethods = new CallsRealMethods();

 // when
 StaticClass.result = callsRealMethods.answer(Mockito.mock(InvocationOnMock.class));
 StaticClass.result = StaticClass.method();

 // then
 assertEquals(StaticClass.result, StaticClass.method()); // the result is the one returned by the real method
 verifyZeroInteractions(StaticClass.class); // no stubbed methods were called
 }

 // helper methods
 private void invokePrivate(Object object, String methodName, Object argument) throws Exception {
 Class<?> clazz = object.getClass();
 Method method = clazz.getDeclaredMethod(methodName, argument.getClass());
 method.setAccessible(true);
 method.invoke(object, argument);
 }

 private void invokeProtected(Object object, String methodName, Object argument) throws Exception {
 Class<?> superclass = object.getClass().getSuperclass();
 Method method = superclass.getDeclaredMethod(methodName, argument.getClass());
 method.setAccessible(true);
 method.invoke(object, argument);
 }
}