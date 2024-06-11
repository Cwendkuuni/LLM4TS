package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.mock.MockitoMockitoMockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubsAnswer;
 private Object mockA;
 private Object mockB;
 private Object mockC;
 private Object mockD;

 @Before
 public void setUp() {
 returnsDeepStubsAnswer = new ReturnsDeepStubs();
 mockA = mock(Object.class, Answers.RETURNS_DEEP_STUBS);
 mockB = mock(Object.class, Answers.RETURNS_DEEP_STUBS);
 mockC = mock(Map.class, Answers.RETURNS_DEEP_STUBS);
 mockD = mock(Number.class, Answers.RETURNS_DEEP_STUBS);
 }

 @Test
 public void testReturnDeepStub_SameInvocation() {
 // given
 List<Object> mockList = Arrays.asList(mockA, mockB);
 when(mockC.entrySet()).thenReturn((Map.Entry<Object, Object>) new Map.Entry<Object, Object>() {
 @Override
 public Object getKey() {
 return mockList.get(0);
 }

 @Override
 public Object getValue() {
 return mockList.get(1);
 }
 });
 Object value = mockC.entrySet().iterator().next().getValue().iterator().next();

 // when
 Object deepStubA = value;
 Object deepStubB = value;

 // then
 assertEquals(mockB, deepStubB);
 assertEquals(mockA, deepStubA);
 }

 @Test(expected = IllegalStateException.class)
 public void testReturnDeepStub_InvalidClass() {
 // given
 when(mockA.toString()).thenReturn("Mock A");
 when(mockB.toString()).thenReturn("Mock B");

 // when, then
 verify(mockA).toString();
 verify(mockB).toString();

 // when (this should fail)
 Object invalidStub = returnsDeepStubsAnswer.answer(new InvocationOnMock(mockA, new MethodTestMethod()));
 }

 @Test
 public void testReturnDeepStub_ValidClass_NewMock() {
 // given
 when(mockA.toString()).thenReturn("Mock A");
 when(mockB.toString()).thenReturn("Mock B");
 when(mockC.toString()).thenReturn("Mock C");
 when(mockD.toString()).thenReturn("Mock D");

 // when
 Object newMockA = ReturnsDeepStubs.getMock(new InvocationOnMock(mockA, new MethodTestMethod()));
 Object newMockB = ReturnsDeepStubs.getMock(new InvocationOnMock(mockB, new MethodTestMethod()));
 Object newMockC = ReturnsDeepStubs.getMock(new InvocationOnMock(mockC, new MethodTestMethod()));
 Object newMockD = ReturnsDeepStubs.getMock(new InvocationOnMock(mockD, new MethodTestMethod()));

 // then
 assertEquals(mockA, newMockA);
 assertEquals(mockB, newMockB);
 assertEquals(mockC, newMockC);
 assertEquals(mockD, newMockD);
 }

 @Test
 public void testReturnDeepStub_ValidClass_ExistingMock() {
 // given
 when(mockA.toString()).thenReturn("Mock A");
 when(mockB.toString()).thenReturn("Mock B");
 when(mockC.toString()).thenReturn("Mock C");
 when(mockD.toString()).thenReturn("Mock D");

 InvocationContainerImpl container = new InvocationContainerImpl();
 container.addAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return mockC;
 }
 }, false);

 // when
 Object existingMockC = ReturnsDeepStubs.getMock(new InvocationOnMock(mockC, new MethodTestMethod(), container));

 // then
 assertEquals(mockC, existingMockC);
 }

 @Test
 public void testReturnDeepStub_NestedGenericTypes() {
 // given
 when(mockA.toString()).thenReturn("Mock A");
 when(mockB.toString()).thenReturn("Mock B");
 when(mockC.toString()).thenReturn("Mock C");
 when(mockD.toString()).thenReturn("Mock D");

 // when
 Map<Object, Map<Object, List<Number>>> complexMap = mock(Map.class, Answers.RETURNS_DEEP_STUBS);
 Map.Entry<Object, Map<Object, List<Number>>> entry = complexMap.entrySet().iterator().next();
 Object value = entry.getValue().iterator().next().iterator().next();

 // then
 assertEquals(mockD, value);
 }

 @Test
 public void testReturnDeepStub_EdgeCase_NestedGenericTypes() {
 // given
 when(mockA.toString()).thenReturn("Mock A");
 when(mockB.toString()).thenReturn("Mock B");
 when(mockC.toString()).thenReturn("Mock C");
 when(mockD.toString()).thenReturn("Mock D");

 Map<Object, Map<Object, List<Number>>> complexMap = mock(Map.class, Answers.RETURNS_DEEP_STUBS);
 Map.Entry<Object, Map<Object, List<Number>>> entry = complexMap.entrySet().iterator().next();
 Map<Object, List<Number>> nestedMap = entry.getValue();
 List<Number> nestedList = nestedMap.iterator().next();
 Number nestedNumber = nestedList.iterator().next();

 // when
 Object deepStubA = ReturnsDeepStubs.getMock(new InvocationOnMock(mockA, new MethodTestMethod()));
 Object deepStubB = ReturnsDeepStubs.getMock(new InvocationOnMock(mockB, new MethodTestMethod()));
 Object deepStubC = ReturnsDeepStubs.getMock(new InvocationOnMock(mockC, new MethodTestMethod()));
 Object deepStubD = ReturnsDeepStubs.getMock(new InvocationOnMock(mockD, new MethodTestMethod()));
 Object deepStubComplexMap = ReturnsDeepStubs.getMock(new InvocationOnMock(complexMap, new MethodTestMethod()));
 Object deepStubEntry = ReturnsDeepStubs.getMock(new InvocationOnMock(entry, new MethodTestMethod(), container));
 Object deepStubNestedMap = ReturnsDeepStubs.getMock(new InvocationOnMock(nestedMap, new MethodTestMethod()));
 Object deepStubNestedList = ReturnsDeepStubs.getMock(new InvocationOnMock(nestedList, new MethodTestMethod()));
 Object deepStubNestedNumber = ReturnsDeepStubs.getMock(new InvocationOnMock(nestedNumber, new MethodTestMethod()));

 // then
 assertEquals(mockA, deepStubA);
 assertEquals(mockB, deepStubB);
 assertEquals(mockC, deepStubC);
 assertEquals(mockD, deepStubD);
 assertEquals(complexMap, deepStubComplexMap);
 assertEquals(entry, deepStubEntry);
 assertEquals(nestedMap, deepStubNestedMap);
 assertEquals(nestedList, deepStubNestedList);
 assertEquals(nestedNumber, deepStubNestedNumber);
 }

}

class MethodTestMethod {
}