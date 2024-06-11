package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.stubbing.answer.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.mock.MockSettings;
import org.mockito.mock.MockitoCore;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

 private MockitoCore mockitoCore;
 private ReturnsDeepStubs returnsDeepStubs;

 @Before
 public void setup() {
 mockitoCore = mock(MockitoCore.class);
 returnsDeepStubs = new ReturnsDeepStubs();
 }

 @Test
 public void shouldReturnDeepStubForSupportedTypes() {
 // given
 InterfaceWithNestedGenerics<Integer, Map<String, Set<String>>> interfaceWithNestedGenerics = mock(
 InterfaceWithNestedGenerics.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));

 // when
 Object deepStub = interfaceWithNestedGenerics.getMap().entrySet().iterator().next().getValue().iterator().next();

 // then
 assertThat(deepStub, isA(String.class));
 verify(interfaceWithNestedGenerics, times(1)).getMap();
 }

 @Test
 public void shouldReturnDefaultAnswerForUnsupportedTypes() {
 // given
 InterfaceWithoutNestedGenerics<String> interfaceWithoutNestedGenerics = mock(InterfaceWithoutNestedGenerics.class, withSettings().defaultAnswer(returnsDeepStubs));

 // when
 Object result = interfaceWithoutNestedGenerics.foo();

 // then
 assertThat(result, is(nullValue()));
 }

 @Test
 public void shouldCreateNewMocksForDeepStubbing() {
 // given
 InterfaceWithNestedGenerics<Integer, Map<String, Set<String>>> interfaceWithNestedGenerics = mock(
 InterfaceWithNestedGenerics.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));

 // when
 Object deepStub1 = interfaceWithNestedGenerics.getMap().entrySet().iterator().next().getValue().iterator().next();
 Object deepStub2 = interfaceWithNestedGenerics.getMap().entrySet().iterator().next().getValue().iterator().next();

 // then
 assertThat(deepStub1, isA(DeepStubMock.class));
 assertThat(deepStub2, isA(DeepStubMock.class));
 assertThat(deepStub1, is(not(sameInstance(deepStub2))));
 }

 @Test
 public void shouldRecordDeepStubAnswer() {
 // given
 InterfaceWithNestedGenerics<Integer, Map<String, Set<String>>> interfaceWithNestedGenerics = mock(
 InterfaceWithNestedGenerics.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));

 // when
 Object deepStub = interfaceWithNestedGenerics.getMap().entrySet().iterator().next().getValue().iterator().next();

 // then
 verify(interfaceWithNestedGenerics, times(1)).getMap();
 verify(deepStub).foo();
 }

 @Test
 public void shouldReturnDeepStubForNestedGenerics() {
 // given
 InterfaceWithNestedGenerics<Integer, Map<String, Set<String>>> interfaceWithNestedGenerics = mock(
 InterfaceWithNestedGenerics.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));

 // when
 Object deepStubMap = interfaceWithNestedGenerics.getMap();
 Object deepStubSet = ((Map<String, Set<String>>) deepStubMap).entrySet().iterator().next().getValue();
 Object deepStubNumber = ((Set<String>) deepStubSet).iterator().next();

 // then
 assertThat(deepStubNumber, isA(Number.class));
 }

 @Test
 public void shouldReturnDeepStubsForNestedInterfaces() {
 // given
 InterfaceWithNestedInterfaces<String, Integer, Map<String, Set<String>>> interfaceWithNestedInterfaces = mock(
 InterfaceWithNestedInterfaces.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));

 // when
 Object deepStubMap = interfaceWithNestedInterfaces.getMap();
 Object deepStubSet = ((Map<String, Set<String>>) deepStubMap).entrySet().iterator().next().getValue();
 Object deepStubNumber = ((Set<String>) deepStubSet).iterator().next();
 Object deepStubInteger = interfaceWithNestedInterfaces.getInteger();

 // then
 assertThat(deepStubInteger, isA(Integer.class));
 }

 private static class DeepStubMock implements Serializable {
 }

 private static class InterfaceWithNestedGenerics<T, M extends Map<String, S>, S extends Set<N>, N> {
 M getMap() throws IOException;
 N foo() throws IOException;
 }

 private static class InterfaceWithNestedInterfaces<S, T extends Number, M extends Map<String, S>> extends InterfaceWithNestedGenerics<S, M, Set<T>, T> {
 }
}