package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answer;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

 @Mock
 private MyClass myClass;

 @Test
 public void testSmartNullForStringMethod() {
     when(myClass.toString()).thenAnswer(returnsSmartNulls);
     String smartNull = myClass.toString();
     verify(myClass).toString();
     assertNull(smartNull);
     // Stack trace will point to the line where toString was called
     // and will show "SmartNull returned by unstubbed toString method on mock"
 }

 @Test
 public void testNullReturnType() {
     when(myClass.someMethod(ArgumentMatchers.anyInt())).thenAnswer(returnsSmartNulls);
     assertNull(myClass.someMethod(1));
 }

 @Test
 public void testDelegateAnswer() {
     when(myClass.someMethod(ArgumentMatchers.anyInt())).thenAnswer(new Answer<Integer>() {
         @Override
         public Integer answer(InvocationOnMock invocation) throws Throwable {
             return 42;
         }
     });
     assertEquals(42, myClass.someMethod(1));
 }

 @Test(expected = NullPointerException.class)
 public void testUnstubbedMethodCall() {
     when(myClass.someNullMethod()).thenAnswer(returnsSmartNulls);
     myClass.someNullMethod();
 }

}

 class MyClass {
 public String toString() {
 return null;
 }

 public void someNullMethod() {
 // do nothing
 }

 public int someMethod(int i) {
 return 42;
 }
 }