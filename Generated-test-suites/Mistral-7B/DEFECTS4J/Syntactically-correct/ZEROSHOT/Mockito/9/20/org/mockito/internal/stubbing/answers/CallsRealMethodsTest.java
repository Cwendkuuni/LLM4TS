package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answer;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer2;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CallsRealMethodsTest {

 private interface TestInterface {
 void methodToBeTested();
 }

 @Test
 public void testCallsRealMethod() {
 TestInterface testInterface = mock(TestInterface.class, new CallsRealMethods());

 TestClass testClass = new TestClass();
 testInterface = Mockito.spy(testInterface);

 testClass.setDependency(testInterface);
 testClass.methodThatUsesDependency();

 // Verify that methodToBeTested() was called on the spy
 verify(testInterface).methodToBeTested();
 }

 static class TestClass {
 private TestInterface dependency;

 public void methodThatUsesDependency() {
 dependency.methodToBeTested();
 }

 public void setDependency(TestInterface dependency) {
 this.dependency = dependency;
 }
 }

 @Test
 public void testCallsRealMethodTwice() {
 TestInterface testInterface = mock(TestInterface.class, new CallsRealMethods());

 TestClass testClass = new TestClass();
 testClass.setDependency(testInterface);

 testClass.methodThatUsesDependency();
 testClass.methodThatUsesDependency();

 // Verify that methodToBeTested() was called twice on the spy
 verify(testInterface, Mockito.times(2)).methodToBeTested();
 }

 @Test
 public void testCallsRealMethodWithArguments() {
 TestInterface testInterface = mock(TestInterface.class, new CallsRealMethods());

 TestClass testClass = new TestClass();
 testClass.setDependency(testInterface);

 testClass.methodThatUsesDependencyWithArguments("Argument1", "Argument2");

 // Verify that methodToBeTested() was called with the provided arguments
 verify(testInterface).methodToBeTested(Mockito.eq("Argument1"), Mockito.eq("Argument2"));
 }

 @Test
 public void testCallsRealMethodWithTwoArguments() {
 TestInterface testInterface = mock(TestInterface.class, new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 Object arg1 = invocation.getArgumentAt(0, Object.class);
 Object arg2 = invocation.getArgumentAt(1, Object.class);
 return new Object[] { arg1, arg2 };
 }
 });

 TestClass testClass = new TestClass();
 testClass.setDependency(testInterface);

 testClass.methodThatUsesDependencyWithTwoArguments(new Object(), new Object());

 // Verify that methodToBeTested() was called with the provided arguments
 TestInterface spy = Mockito.spy(testInterface);
 verify(spy).methodToBeTested(Mockito.eq(new Object()), Mockito.eq(new Object()));
 }

 @Test
 public void testCallsRealMethodWithAnswer() {
 TestInterface testInterface = mock(TestInterface.class, new CallsRealMethods());

 TestClass testClass = new TestClass();
 testClass.setDependency(testInterface);

 Object result = testClass.methodThatUsesDependencyAndReturnsSomething();

 // Verify that methodToBeTested() was called and returns the expected result
 TestInterface spy = Mockito.spy(testInterface);
 verify(spy).methodToBeTested().equals(result);
 }

 @Test
 public void testCallsRealMethodWithAnswer2() {
 TestInterface testInterface = mock(TestInterface.class, new Answer2<Object, Object>() {
 @Override
 public Object answer(InvocationOnMock invocation, Object argument) throws Throwable {
 Object arg1 = invocation.getArgumentAt(0, Object.class);
 Object arg2 = argument;
 return new Object[] { arg1, arg2 };
 }
 });

 TestClass testClass = new TestClass();
 testClass.setDependency(testInterface);

 Object result = testClass.methodThatUsesDependencyAndReturnsSomethingWithArgument(new Object());

 // Verify that methodToBeTested() was called with the provided argument and returns the expected result
 TestInterface spy = Mockito.spy(testInterface);
 verify(spy).methodToBeTested((Object)Mockito.any(), Mockito.eq(result));
 }
}