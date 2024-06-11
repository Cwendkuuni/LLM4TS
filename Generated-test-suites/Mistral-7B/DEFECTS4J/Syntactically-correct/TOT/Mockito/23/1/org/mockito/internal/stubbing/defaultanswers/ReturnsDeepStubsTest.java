package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAssert.assertSame;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object deepMock;

 @Before
 public void setup() {
 returnsDeepStubs = new ReturnsDeepStubs();
 deepMock = mock(Object.class, with(new ReturnsDeepStubs()));
 }

 @Test
 public void testDeepStubbingWithGenericType() {
 Object deepMockWithGenericType = mock(MyGenericClass.class, with(new ReturnsDeepStubs()));
 MyGenericClass myGenericClass = (MyGenericClass) deepMockWithGenericType;

 assertSame(deepMockWithGenericType, myGenericClass.getNestedObject1().getNestedObject2().getNestedObject3());
 }

 @Test
 public void testDeepStubbingWithComplexTypeHierarchy() {
 Object deepMockWithComplexTypeHierarchy = mock(MyComplexClass.class, with(new ReturnsDeepStubs()));
 MyComplexClass myComplexClass = (MyComplexClass) deepMockWithComplexTypeHierarchy;

 assertSame(deepMockWithComplexTypeHierarchy, myComplexClass.getChildClass1().getChildClass2().getChildClass3().getChildClass4().getChildClass5());
 }

 @Test(expected = IllegalStateException.class)
 public void testDeepStubbingWithNonMockableType() {
 Object nonMockableType = mock(NonMockableClass.class, with(new ReturnsDeepStubs()));

 try {
 assertSame(nonMockableType, nonMockableType.someNonStaticMethod());
 } catch (Throwable e) {
 throw new IllegalStateException("Expected mock to be instantiated, but was : " + nonMockableType.getClass().getName(), e);
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testDeepStubbingWithMismatchedTypes() {
 Object deepMockWithMismatchedTypes = mock(MyMismatchedTypesClass.class, with(new ReturnsDeepStubs()));
 MyMismatchedTypesClass myMismatchedTypesClass = (MyMismatchedTypesClass) deepMockWithMismatchedTypes;

 try {
 assertSame(deepMockWithMismatchedTypes, myMismatchedTypesClass.getNestedObject1());
 assertSame(1, myMismatchedTypesClass.getNestedObject2());
 } catch (Throwable e) {
 throw new IllegalStateException("Expected mock to be instantiated, but was : " + deepMockWithMismatchedTypes.getClass().getName(), e);
 }
 }

 @Test
 public void testAnswerDoesNotExecutedWhenInvocationIsStubbed() throws Throwable {
 Object answerMock = mock(Answer.class, with(new ReturnsDeepStubs()));
 InvocationOnMock invocationOnMock = mock(InvocationOnMock.class);

 doAnswer(answerMock).when(deepMock).answerMethod();
 deepMock.answerMethod();
 verify(answerMock).answer(ArgumentMatchers.any(InvocationOnMock.class));
 }
}