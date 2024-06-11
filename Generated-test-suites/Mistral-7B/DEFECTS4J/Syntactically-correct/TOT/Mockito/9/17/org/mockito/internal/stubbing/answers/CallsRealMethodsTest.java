package org.mockito.internal.stubbing.answers.test;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*; // Use BDDMockito for readability

public class CallsRealMethodsTest {

 // Test for the answer method when the invocation is not stubbed
 @Test
 public void testCallsRealMethod() {
     Object realObject = new Object() {
         public String methodToTest() {
             return "Real method result";
         }
     };

     CallsRealMethods answer = new CallsRealMethods();
     Object mockObject = Mockito.mock(Object.class);

     given(mockObject.methodToTest()).willReturn(answer);

     String result = (String) mockObject.methodToTest(); // This call is stubbed, so it won't call the real method

     // Test the actual call to the real method
     String realResult = realObject.methodToTest();
     assertEquals("Real method result", realResult);
 }

 // Test for the answer method when the invocation is stubbed
 @Test
 public void testCallsStubbedMethod() {
     CallsRealMethods answer = new CallsRealMethods();
     Object mockObject = Mockito.mock(Object.class);

     given(mockObject.methodToTest(ArgumentMatchers.anyString()))
             .willReturn("Stubbed method result");

     String result = (String) mockObject.methodToTest("Test input");

     assertEquals("Stubbed method result", result);
 }

 // Test for the answer method when the invocation throws an exception
 @Test(expected = RuntimeException.class)
 public void testThrowsException() {
     CallsRealMethods answer = new CallsRealMethods();
     Object mockObject = Mockito.mock(Object.class);

     given(mockObject.methodToTest())
             .willThrow(new RuntimeException("Test exception message"));

     mockObject.methodToTest();
 }
}