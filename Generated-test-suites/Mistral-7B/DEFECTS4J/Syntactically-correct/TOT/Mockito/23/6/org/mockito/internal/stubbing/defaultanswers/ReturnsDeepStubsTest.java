package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object deepStubMock;

 @Before
 public void setup() {
 returnsDeepStubs = new ReturnsDeepStubs();
 deepStubMock = mock(Object.class, new ReturnsDeepStubs());
 }

 @Test
 public void testAnswerWithDeepStubMock() {
 Object answer = returnsDeepStubs.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == Object.class));
 assertEquals(deepStubMock, answer);
 }

 @Test
 public void testAnswerWithNonMockableType() {
 Class<String> stringClass = String.class;
 Object answer = returnsDeepStubs.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == stringClass));
 assertEquals(Mockito.mock(String.class), answer);
 }

 @Test
 public void testAnswerWithMatchedInvocation() throws Throwable {
 InsideClass insideClassMock = mock(InsideClass.class);
 Mockito.when(insideClassMock.getNestedMapValue())
 .thenAnswer(new Answer<Number>() {
 @Override
 public Number answer(InvocationOnMock invocation) throws Throwable {
 Number number = Mockito.mock(Number.class);
 return number;
 }
 });

 Object deepStub = returnsDeepStubs.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == Number.class
 && invocation.getTarget() instanceof InsideClass));
 assertEquals(deepStub, Mockito.argThat(invocation -> invocation.getTarget() == insideClassMock));
 }

 @Test
 public void testAnswerWithNonMatchedInvocation() throws Throwable {
 Object otherMock = mock(OtherClass.class);
 InvocationOnMock nonMatchedInvocation = Mockito.mock(InvocationOnMock.class);
 nonMatchedInvocation.getMethod().setReturnValue(otherMock);

 Object answer = returnsDeepStubs.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == Object.class
 && invocation.getTarget() != otherMock));
 assertEquals(Mockito.mock(Object.class), answer);
 }

 @Test(expected = Throwable.class)
 public void testAnswerWithNullInvocation() throws Throwable {
 Object answer = returnsDeepStubs.answer(null);
 }

 class InsideClass {
 Map<Integer, Set<Number>> map;

 Number getNestedMapValue() {
 return map.entrySet().iterator().next().getValue().iterator().next();
 }
 }

 class OtherClass {
 }
}