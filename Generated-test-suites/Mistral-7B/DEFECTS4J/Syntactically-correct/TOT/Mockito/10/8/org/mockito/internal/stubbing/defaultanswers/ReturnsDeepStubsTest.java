package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 @Mock
 private GenericsNest<Number> mockGenericsNest;
 @Mock
 private GenericsNest<String> mockStringGenericsNest;
 @InjectMocks
 private ReturnsDeepStubs returnsDeepStubs;

 @Before
 public void setup() {
 Mockito.initMocks(this);
 }

 @Test
 public void shouldReturnDeepStubWhenValidGenericType() throws Exception {
 // Given
 GenericsNest<Number> genericNest = Mockito.spy(new GenericsNest<Number>() {});
 Mockito.doReturn(genericNest).when(mockGenericsNest).entrySet().iterator().next();
 Mockito.doReturn(mockGenericsNest).when(mockGenericsNest).entrySet().iterator();
 Number number = Mockito.spy(1);
 Mockito.doReturn(number).when(genericNest).getValue().iterator().next();

 // When
 Object deepStub = returnsDeepStubs.answer(new InvocationOnMock(this, 0, "getNestedValue", new Object[0]) {
 @Override
 public Object[] getArguments() {
 return new Object[]{mockGenericsNest};
 }
 });

 // Then
 assertEquals(Number.class, deepStub.getClass());
 verify(mockGenericsNest).getValue().iterator().next();
 }

 @Test
 public void shouldReturnDefaultAnswerWhenNonMockableType() throws Exception {
 // Given
 Object nonMockableObject = new Object() {};

 // When
 Object deepStub = returnsDeepStubs.answer(new InvocationOnMock(this, 0, "getNonMockableValue", new Object[0]) {
 @Override
 public Object[] getArguments() {
 return new Object[]{nonMockableObject};
 }
 });

 // Then
 assertEquals(Mockito.RETURNS_DEEP_STUBS.delegate().returnValueFor(nonMockableObject.getClass()), deepStub);
 }

 @Test
 public void shouldReturnSameDeepStubForSameInvocation() throws Exception {
 // Given
 GenericsNest<Number> genericNest = Mockito.spy(new GenericsNest<Number>() {});
 Mockito.doReturn(genericNest).when(mockGenericsNest).entrySet().iterator().next();
 Number number = Mockito.spy(1);
 Mockito.doReturn(number).when(genericNest).getValue().iterator().next();

 // When
 Object deepStub1 = returnsDeepStubs.answer(new InvocationOnMock(this, 0, "getNestedValue", new Object[0]) {
 @Override
 public Object[] getArguments() {
 return new Object[]{mockGenericsNest};
 }
 });
 Object deepStub2 = returnsDeepStubs.answer(new InvocationOnMock(this, 0, "getNestedValue", new Object[0]) {
 @Override
 public Object[] getArguments() {
 return new Object[]{mockGenericsNest};
 }
 });

 // Then
 assertEquals(deepStub1, deepStub2);
 }

 @Test
 public void shouldReturnNewDeepStubForDifferentInvocations() throws Exception {
 // Given
 GenericsNest<Number> genericNest1 = Mockito.spy(new GenericsNest<Number>() {});
 Mockito.doReturn(genericNest1).when(mockGenericsNest).entrySet().iterator().next();
 Number number1 = Mockito.spy(1);
 Mockito.doReturn(number1).when(genericNest1).getValue().iterator().next();

 GenericsNest<String> genericNest2 = Mockito.spy(new GenericsNest<String>() {});
 Mockito.doReturn(genericNest2).when(mockStringGenericsNest).entrySet().iterator().next();
 String string = Mockito.spy("test");
 Mockito.doReturn(string).when(genericNest2).getValue().iterator().next();

 // When
 Object deepStub1 = returnsDeepStubs.answer(new InvocationOnMock(this, 0, "getNestedValue", new Object[0]) {
 @Override
 public Object[] getArguments() {
 return new Object[]{mockGenericsNest1};
 }
 });
 Object deepStub2 = returnsDeepStubs.answer(new InvocationOnMock(this, 0, "getNestedStringValue", new Object[0]) {
 @Override
 public Object[] getArguments() {
 return new Object[]{mockStringGenericsNest2};
 }
 });

 // Then
 assertNotSame(deepStub1, deepStub2);
 }

 @Test(expected = RuntimeException.class)
 public void shouldThrowExceptionWhenInvocationDoesNotMatchAnyStub() throws Exception {
 // Given
 GenericsNest<Number> genericNest = Mockito.spy(new GenericsNest<Number>() {});
 Mockito.doReturn(genericNest).when(mockGenericsNest).entrySet().iterator().next();
 Number number = Mockito.spy(1);
 Mockito.doReturn(number).when(genericNest).getValue().iterator().next();

 // When, Then
 returnsDeepStubs.answer(new InvocationOnMock(this, 0, "getDifferentValue", new Object[0]) {
 @Override
 public Object[] getArguments() {
 return new Object[]{mockGenericsNest};
 }
 });
 }
}