package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.stubbing.answer.Answer;
import org.mockito.internal.stubbing.answer.ReturnsDeepStubs;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mock;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 returnsDeepStubs = new ReturnsDeepStubs();
 mock = Mockito.mock(ArrayList.class, Answers.RETURNS_DEEP_STUBS);
 }

 @Test
 public void testDeepStubForList() {
 List list = (List) mock;
 list.add(1);
 list.add(2);

 Object deepStub = returnsDeepStubs.answer(Mockito.mock(InvocationOnMock.class).withMethodCalls(
         Mockito.methodCall(list, "add", 3),
         Mockito.methodCall(list, "size")));

 verify(deepStub).size(); // Verifies the deep stub answer
 }

 @Test(expected = Throwable.class)
 public void testDeepStubForNonMockableType() {
 // This type is not mockable by default in Mockito
 Object nonMockableType = Mockito.mock(Integer.TYPE);

 ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
 Object deepStub = deepStubs.answer(Mockito.mock(InvocationOnMock.class).withMethodCalls(
         Mockito.methodCall(nonMockableType, "intValue")));

 // Throws an exception as this type is not mockable
 deepStub.intValue();
 }

 @Test
 public void testDeepStubForNestedGeneric() {
 interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
 }

 GenericsNest<Number> mockGenericsNest = Mockito.mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);
 Number number = mockGenericsNest.entrySet().iterator().next().getValue().iterator().next();

 Object deepStub = returnsDeepStubs.answer(Mockito.mock(InvocationOnMock.class).withMethodCalls(
         Mockito.methodCall(mockGenericsNest, "entrySet"),
         Mockito.methodCall(mockGenericsNest.entrySet(), "iterator"),
         Mockito.methodCall(mockGenericsNest.entrySet().iterator(), "next"),
         Mockito.methodCall(mockGenericsNest.entrySet().iterator().next(), "getValue"),
         Mockito.methodCall(mockGenericsNest.entrySet().iterator().next().getValue(), "iterator"),
         Mockito.methodCall(mockGenericsNest.entrySet().iterator().next().getValue().iterator(), "next")));

 verify(deepStub).getClass(); // Verifies the deep stub answer, which is the correct type
 }
}