package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.InternalMockHandler;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.util.serialization.SerializableUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private InternalMockHandler<Object> handler;
 private InvocationContainerImpl container;
 private Object deepStubMock;

 @Before
 public void setUp() {
 handler = new MockUtil().getMockHandler(mock(Object.class));
 container = (InvocationContainerImpl) handler.getInvocationContainer();
 returnsDeepStubs = new ReturnsDeepStubs();
 deepStubMock = newDeepStubWithReturnType(Mockito.class);
 }

 @Test
 public void shouldReturnDeepStubIfInvocationMatches() {
 // Given
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 ArgumentCaptor<InvocationOnMock> invocationCaptor = ArgumentCaptor.forClass(InvocationOnMock.class);
 // When
 Object returnValue = handler.handle(new DeeplyStubbedAnswer(deepStubMock), invocationCaptor.capture(), false);
 // Then
 verify(deepStubMock).answer(invocationCaptor.getValue());
 assertEquals(deepStubMock, returnValue);
 }

 @Test
 public void shouldNotReturnDeepStubIfInvocationDoesNotMatch() {
 // Given
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 // When
 Object returnValue = handler.handle(new DeeplyStubbedAnswer(mock), new InvocationOnMock(Mockito.class, "test"), false);
 // Then
 assertNotNull(returnValue);
 assertNotSame(mock, returnValue);
 }

 @Test
 public void shouldRecordDeepStubAnswer() throws Throwable {
 // Given
 Object returnValue = deepStubMock.methodCall();
 // When
 container.getInvocationForStubbing().answer(new DeeplyStubbedAnswer(deepStubMock));
 // Then
 verify(deepStubMock).answer(any(InvocationOnMock.class));
 assertEquals(returnValue, deepStubMock.answer(new InvocationOnMock(Mockito.class, "test")));
 }

 @Test
 public void shouldReturnDeepStubForNestedGenerics() {
 // Given
 interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
 }
 GenericsNest<Number> mock = mock(GenericsNest.class, new ReturnsDeepStubs());
 Number number = mock.entrySet().iterator().next().getValue().iterator().next();
 // When
 Object deepStub = mock.methodCall();
 // Then
 assertEquals(deepStub, number);
 }

 @Test
 public void shouldSerializeAndDeserializeDeepStub() throws Exception {
 // Given
 Object deepStub = mock(Object.class, new ReturnsDeepStubs());
 Object serialized = SerializableUtil.serialize(deepStub);
 Object deserialized = (Object) SerializableUtil.deserialize(serialized);
 // When
 // Then
 assertEquals(deepStub, deserialized);
 }

 private Object newDeepStubWithReturnType(Class<?> returnType) {
 return mockitoCore().mock(returnType, new ReturnsDeepStubs());
 }

 private MockitoCore mockitoCore() {
 return MockitoCore.getInstance();
 }
}