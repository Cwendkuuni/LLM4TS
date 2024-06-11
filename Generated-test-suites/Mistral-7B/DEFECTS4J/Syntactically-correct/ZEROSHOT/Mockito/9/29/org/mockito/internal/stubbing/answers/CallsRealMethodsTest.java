package org.mockito.internal.stubbing.answers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CallsRealMethodsTest {

 private CallsRealMethods callsRealMethods;
 private Object target;

 @Before
 public void setUp() {
 callsRealMethods = new CallsRealMethods();
 target = mock(TestClass.class);
 }

 @Test
 public void testCallRealMethod() {
     ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
     callsRealMethods.answer(new InvocationOnMock(target, "methodWithStringParameter", new Object[]{"test"}, new Class[]{String.class}));

     String result = (String) callsRealMethods.answer(new InvocationOnMock(target, "methodWithStringParameter", new Object[]{"test"}, new Class[]{String.class}));

     verify(target).methodWithStringParameter("test");
     assertNull(result); // since methodWithStringParameter is a void method in TestClass
 }

 @Test
 public void testCallRealMethodWithReturnValue() {
     callsRealMethods = new CallsRealMethods() {
         @Override
         public Object answer(InvocationOnMock invocation) throws Throwable {
             return "test";
         }
     };
     ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

     String result = (String) callsRealMethods.answer(new InvocationOnMock(target, "methodWithStringReturnValue", new Object[0], new Class[0]));

     verify(target).methodWithStringReturnValue();
     assertEquals("test", result);
 }

 @Test(expected = Throwable.class)
 public void testCallRealMethodWithException() {
     callsRealMethods = new CallsRealMethods() {
         @Override
         public Object answer(InvocationOnMock invocation) throws Throwable {
             throw new RuntimeException("Test exception");
         }
     };

     ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
     callsRealMethods.answer(new InvocationOnMock(target, "methodWithException", new Object[]{"test"}, new Class[]{String.class}));

     try {
         callsRealMethods.answer(new InvocationOnMock(target, "methodWithException", new Object[]{"test"}, new Class[]{String.class}));
     } catch (RuntimeException e) {
         assertEquals("Test exception", e.getMessage());
         throw e;
     }

     verify(target).methodWithException("test");
 }
}

class TestClass {
 void methodWithStringParameter(String parameter) {}
 void methodWithStringReturnValue() { return "test"; }
 void methodWithException() throws Exception { throw new Exception(); }
}