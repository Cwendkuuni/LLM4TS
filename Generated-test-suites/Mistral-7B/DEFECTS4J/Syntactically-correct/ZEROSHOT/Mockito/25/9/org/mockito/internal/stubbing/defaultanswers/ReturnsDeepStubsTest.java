package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.internal.mock.MockComponent;
import org.mockito.internal.mock.MockHandler;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.AnswerWith;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

 private MockComponent mockComponent;
 private MockHandler mockHandler;
 private InvocationContainer invocationContainer;
 private ReturnsDeepStubs deepStubsAnswer;

 @Rule
 public MockitoJUnitRunner mockitoJUnitRunner = new MockitoJUnitRunner();

 @Before
 public void setUp() {
 mockComponent = mock(MockComponent.class);
 mockHandler = mock(MockHandler.class);
 invocationContainer = mock(InvocationContainer.class);
 deepStubsAnswer = new ReturnsDeepStubs();
 doReturn(deepStubsAnswer).when(mockHandler).getMockAnswer();
 doReturn(mockHandler).when(mockComponent).createMockHandler(any());
 }

 @Test
 public void testReturnsSameDeepStubForSameInvocation() {
 // given
 InterfaceOne interfaceOneMock = mock(InterfaceOne.class, deepStubsAnswer);
 Object stubOne = interfaceOneMock.methodOne();

 // when
 Object stubTwo = interfaceOneMock.methodOne();

 // then
 assertEquals(stubOne, stubTwo);
 }

 @Test
 public void testReturnsDeepStubForDifferentInvocations() {
 // given
 InterfaceOne interfaceOneMock = mock(InterfaceOne.class, deepStubsAnswer);
 Object stubOne = interfaceOneMock.methodOne();
 Object stubTwo = interfaceOneMock.methodTwo();

 // then
 assertEquals(stubOne, stubOne);
 assertEquals(stubTwo, stubTwo);
 assertEquals(stubOne, stubTwo); // this should return the stubOne, not stubTwo
 }

 @Test
 public void testDoesNotReturnStubForNonMockableTypes() {
 // given
 final String nonMockableType = "nonMockableType";
 doReturn(deepStubsAnswer).when(mockHandler).getMockAnswer();
 doReturn(mockHandler).when(mockComponent).createMockHandler(any());
 doReturn(new Object()).when(deepStubsAnswer).delegateReturnValueFor(ArgumentMatchers.eq(String.class));

 // when
 Object nonMockableObject = mock(String.class).answer(argThat(isInstanceOf(String.class))).getMock();

 // then
 assertNotNull(nonMockableObject);
 assertEquals(nonMockableType, nonMockableObject);
 }

 @Test
 public void testCreatesNewDeepStubForDifferentTypes() {
 // given
 InterfaceOne interfaceOneMock = mock(InterfaceOne.class, deepStubsAnswer);
 Object stubOne = interfaceOneMock.methodOne();
 Object stubTwo = interfaceOneMock.methodTwo();

 // when
 InterfaceTwo interfaceTwoMock = mock(InterfaceTwo.class, deepStubsAnswer);
 Object stubThree = interfaceTwoMock.methodOne();

 // then
 assertEquals(stubOne, stubOne);
 assertEquals(stubTwo, stubTwo);
 assertEquals(stubOne, stubTwo); // this should return the stubOne, not stubTwo
 assertEquals(stubThree, stubThree);
 }

 @Test
 public void testDeepStubsInNestedGenericTypes() {
 // given
 final Map<String, Set<Number>> mapMock = mock(Map.class, deepStubsAnswer);
 final Set<Number> setMock = mock(Set.class, deepStubsAnswer);
 final Number numberMock = mock(Number.class, deepStubsAnswer);

 doReturn(mapMock).when(mapMock).entrySet();
 doReturn(setMock).when(mapMock).entrySet().iterator().next().getValue().iterator().next();
 doReturn(numberMock).when(setMock).iterator().next();

 // when
 Number number = mapMock.entrySet().iterator().next().getValue().iterator().next().getValue().iterator().next();

 // then
 assertEquals(numberMock, number);
 }

 @Test
 public void testDeepStubsInNestedGenericTypesWithComplexType() {
 // given
 final Map<String, Map<Integer, Set<Number>>> complexMapMock = mock(Map.class, deepStubsAnswer);
 final Map<Integer, Set<Number>> innerMapMock = mock(Map.class, deepStubsAnswer);
 final Set<Number> innerSetMock = mock(Set.class, deepStubsAnswer);
 final Number numberMock = mock(Number.class, deepStubsAnswer);

 doReturn(complexMapMock).when(complexMapMock).get(any());
 doReturn(innerMapMock).when(complexMapMock).get(any()).values();
 doReturn(innerSetMock).when(innerMapMock).values().iterator().next();
 doReturn(numberMock).when(innerSetMock).iterator().next();

 // when
 Number number = complexMapMock.get(anyString()).values().iterator().next().iterator().next().iterator().next();

 // then
 assertEquals(numberMock, number);
 }

 @Test
 public void testDeepStubsInNestedGenericTypesWithWildcard() {
 // given
 final Map<? extends Map<? extends Integer, ? extends Set<? extends Number>>> wildcardMapMock = mock(Map.class, deepStubsAnswer);
 final Map<Integer, Set<Number>> innerMapMock = mock(Map.class, deepStubsAnswer);
 final Set<Number> innerSetMock = mock(Set.class, deepStubsAnswer);
 final Number numberMock = mock(Number.class, deepStubsAnswer);

 doReturn(wildcardMapMock).when(wildcardMapMock).get(any());
 doReturn(innerMapMock).when(wildcardMapMock).get(any()).values();
 doReturn(innerSetMock).when(innerMapMock).values().iterator().next();
 doReturn(numberMock).when(innerSetMock).iterator().next();

 // when
 Number number = wildcardMapMock.get(anyMap()).values().iterator().next().iterator().next().iterator().next();

 // then
 assertEquals(numberMock, number);
 }

 @Test
 public void testRecordDeepStubMockAddsAnswerToInvocationContainer() {
 // given
 final Class<?> clz = Mockito.class;
 final InvocationOnMock invocation = mock(InvocationOnMock.class);
 final InvocationContainer invocationContainer = mock(InvocationContainer.class);
 doReturn(invocationContainer).when(mockHandler).getInvocationContainer();

 final ReturnsDeepStubs deepStubsAnswer = new ReturnsDeepStubs();
 when(mockHandler.getMockAnswer()).thenReturn(deepStubsAnswer);

 when(deepStubsAnswer.recordDeepStubMock(ArgumentMatchers.eq(invocation), ArgumentMatchers.eq(invocationContainer))).thenReturn(mock(AnswerWith.class));

 // when
 Object mock = mock(clz, deepStubsAnswer);

 // then
 verify(invocationContainer).addAnswer(ArgumentMatchers.isA(Answer.class), ArgumentMatchers.eq(true));
 }

 @Test
 public void testRecordDeepStubMockReturnsDeepStubForSameInvocation() {
 // given
 final Class<?> clz = Mockito.class;
 final InvocationOnMock invocation = mock(InvocationOnMock.class);
 final InvocationContainer invocationContainer = mock(InvocationContainer.class);
 doReturn(invocationContainer).when(mockHandler).getInvocationContainer();

 final Object stub = mock(clz, deepStubsAnswer);
 when(mockHandler.getMockAnswer()).thenReturn(deepStubsAnswer);

 final Object deepStub = deepStubsAnswer.recordDeepStubMock(invocation, invocationContainer);

 // when
 Object stub2 = deepStubsAnswer.recordDeepStubMock(invocation, invocationContainer);

 // then
 assertEquals(deepStub, stub2);
 }
}

class InterfaceOne {
 InterfaceTwo methodTwo();
 Number methodOne();
}

interface InterfaceTwo {
 Number methodOne();
}