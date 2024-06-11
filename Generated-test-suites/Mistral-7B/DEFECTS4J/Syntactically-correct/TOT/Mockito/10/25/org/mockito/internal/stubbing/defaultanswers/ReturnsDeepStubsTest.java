package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object deepMock;

 @Before
 public void setUp() {
 deepMock = mock(Object.class, new ReturnsDeepStubs());
 returnsDeepStubs = Mockito.spy(new ReturnsDeepStubs());
 }

 @Test
 public void shouldReturnDeepStubWhenMatchingInvocation() throws Exception {
 Object deepStub = deepMock;
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
 Mockito.when(returnTypeGenericMetadata.rawType()).thenReturn(Integer.class);
 Mockito.when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(false);
 Mockito.when(returnTypeGenericMetadata.resolveGenericReturnType(ArgumentMatchers.any(Method.class))).thenReturn(returnTypeGenericMetadata);
 Mockito.when(deepMock.deepStub(ArgumentMatchers.eq(invocation), ArgumentMatchers.eq(returnTypeGenericMetadata))).thenReturn(new Integer(42));
 Mockito.when(returnsDeepStubs.recordDeepStubAnswer(ArgumentMatchers.eq(deepStub), ArgumentMatchers.eq(Mockito.any(InvocationContainerImpl.class)))).thenReturn(new Integer(43));

 Object result = returnsDeepStubs.answer(invocation);

 assertSame(new Integer(43), result);
 verify(deepMock).deepStub(invocation, returnTypeGenericMetadata);
 }

 @Test
 public void shouldReturnDelegateAnswerWhenTypeIsNotMockable() throws Exception {
 Object delegateAnswer = mock(Answer.class);
 returnsDeepStubs.delegate().returnValueFor(String.class);
 Mockito.when(mockitoCore().isTypeMockable(ArgumentMatchers.eq(String.class))).thenReturn(false);

 Object result = returnsDeepStubs.answer(Mockito.mock(InvocationOnMock.class));

 assertSame(delegateAnswer, result);
 }

 @Test
 public void shouldReturnDeepStubWhenInvocationDoesNotMatchAnyStubbedInvocation() throws Exception {
 Object deepStub = deepMock;
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
 Mockito.when(returnTypeGenericMetadata.rawType()).thenReturn(Integer.class);
 Mockito.when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(false);
 Mockito.when(returnTypeGenericMetadata.resolveGenericReturnType(ArgumentMatchers.any(Method.class))).thenReturn(returnTypeGenericMetadata);

 Object result = returnsDeepStubs.answer(invocation);

 assertSame(deepStub, result);
 }

 @Test
 public void shouldRecordDeepStubAnswer() throws Exception {
 Object deepStub = deepMock;
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
 Mockito.when(returnTypeGenericMetadata.rawType()).thenReturn(Integer.class);
 Mockito.when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(false);
 Mockito.when(returnTypeGenericMetadata.resolveGenericReturnType(ArgumentMatchers.any(Method.class))).thenReturn(returnTypeGenericMetadata);

 Object deepStubMock = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);
 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
 Mockito.when(container.getInvocationForStubbing()).thenReturn(invocation);

 returnsDeepStubs.recordDeepStubAnswer(deepStubMock, container);

 verify(container).addAnswer(ArgumentMatchers.isInstanceOf(DeeplyStubbedAnswer.class), ArgumentMatchers.eq(false));
 }

 @Test
 public void shouldPassThroughGenericMetadataToNewDeepStubMock() throws Exception {
 Object deepStub = deepMock;
 GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
 Mockito.when(returnTypeGenericMetadata.rawType()).thenReturn(Map.class);
 Mockito.when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(false);
 Mockito.when(returnTypeGenericMetadata.resolveGenericReturnType(ArgumentMatchers.any(Method.class))).thenReturn(returnTypeGenericMetadata);
 Mockito.when(mockitoCore().mock(Map.class, withSettings().serializable().defaultAnswer(ArgumentMatchers.eq(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata)))))
         .thenReturn(new HashMap<>());
 Object deepStubMock = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);

 assertSame(returnTypeGenericMetadata, deepStubMock.getClass().getAnnotation(GenericMetadataSupport.class).value());
 }

 private Answer returnsDeepStubsAnswerUsing(final GenericMetadataSupport returnTypeGenericMetadata) {
 return new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);
 }
}