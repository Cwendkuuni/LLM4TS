package com.example.mockito.test; // replace with your test package name

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.stubbing.answers.CallsRealMethods;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CallsRealMethodsTest {

 private Object realObject;

 @Before
 public void setup() {
 realObject = new Object() {
 // define some methods here that CallsRealMethods class might call
 };
 }

 @Test
 public void testCallRealMethod() {
 CallsRealMethods callRealMethods = Mockito.mock(CallsRealMethods.class);
 Object target = mock(SomeClass.class); // replace SomeClass with the actual class that uses CallsRealMethods

 // set up the mocks for the methods that CallsRealMethods might call
 Mockito.doReturn("mocked return value").when(target).methodName(); // replace methodName with the actual method name

 Mockito.when(callRealMethods.answer(Mockito.eq(new InvocationOnMock(target, "methodToTest", new Object[0])))).then(callRealMethods);

 // test the method that uses CallsRealMethods
 Object result = target.methodToTest(); // replace methodToTest with the actual method name

 // verify that CallsRealMethods.answer method was called with the correct arguments
 verify(callRealMethods).answer(Mockito.eq(new InvocationOnMock(target, "methodToTest", new Object[0])));

 // verify that the method that should be called in real object was called
 verify(target).methodName(); // replace methodName with the actual method name

 // verify that the return value from CallsRealMethods.answer method was returned
 assertEquals("mocked return value", result);
 }

 @Test
 public void testCallAnotherMethodOnRealObject() {
 CallsRealMethods callRealMethods = Mockito.mock(CallsRealMethods.class);
 Object target = mock(SomeClass.class); // replace SomeClass with the actual class that uses CallsRealMethods

 Mockito.when(callRealMethods.answer(Mockito.eq(new InvocationOnMock(target, "methodToTest1", new Object[0])))).then(callRealMethods);
 Mockito.when(callRealMethods.answer(Mockito.eq(new InvocationOnMock(target, "methodToTest2", new Object[0])))).thenReturn("mocked return value"); // replace methodToTest2 with the actual method name that returns a value

 // test the method that uses CallsRealMethods
 Object result = target.methodToTest(); // replace methodToTest with the actual method name

 // verify that CallsRealMethods.answer methods were called with the correct arguments
 verify(callRealMethods, Mockito.times(2)).answer(Mockito.any(InvocationOnMock.class));

 // verify that the methods that should be called in real object were called
 verify(target).methodToTest1(); // replace methodToTest1 with the actual method name
 verify(target).methodToTest2(); // replace methodToTest2 with the actual method name

 // verify that the return values from CallsRealMethods.answer methods were returned
 if (result instanceof String) {
 assertEquals("mocked return value", result);
 } else {
 // add verification for the return type of methodToTest()
 }
 }
}