package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private GenericsNest<Integer> mockGenericsNest;
 private GenericsNest<Number> deepStub;

 @Before
 public void setUp() {
 mockitoCoreSetup();
 returnsDeepStubs = new ReturnsDeepStubs();
 mockGenericsNest = mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);
 }

 @Test
 public void shouldReturnDeepStubWhenInvocationMatches() throws Exception {
 Number number = mockGenericsNest.entrySet().iterator().next().getValue().iterator().next();
 deepStub = (GenericsNest<Number>) returnsDeepStubs.answer(Mockito.mock(InvocationOnMock.class).withArguments(mockGenericsNest, ArgumentMatchers.eq(number)));

 verify(mockGenericsNest).entrySet();
 verify(mockGenericsNest).iterator();
 verify(mockGenericsNest.iterator()).next();
 verify(mockGenericsNest.iterator()).next();

 assertThat(deepStub).isNotSameAs(mockGenericsNest);
 }

 @Test
 public void shouldReturnDeepStubWithNestedGenerics() throws Exception {
 GenericsNest<Map<Integer, Set<Number>>> mockNestedGenericsNest = mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);
 Map<Integer, Set<Number>> nestedMap = mockNestedGenericsNest.entrySet().iterator().next().getValue();
 Set<Number> nestedSet = nestedMap.iterator().next().iterator().next();
 Number nestedNumber = nestedSet.iterator().next();
 deepStub = (GenericsNest<Number>) returnsDeepStubs.answer(Mockito.mock(InvocationOnMock.class).withArguments(mockNestedGenericsNest, ArgumentMatchers.eq(nestedMap), ArgumentMatchers.eq(nestedSet), ArgumentMatchers.eq(nestedNumber)));

 verify(mockNestedGenericsNest).entrySet();
 verify(mockNestedGenericsNest).iterator();
 verify(nestedMap).iterator();
 verify(nestedMap).iterator().next();
 verify(nestedMap.iterator()).next();
 verify(nestedSet).iterator();
 verify(nestedSet).iterator().next();

 assertThat(deepStub).isNotSameAs(mockNestedGenericsNest);
 }

 @Test(expected = IllegalStateException.class)
 public void shouldThrowExceptionWhenTypeIsNotMockable() throws Exception {
 Object nonMockableType = new Object() {};
 deepStub = (GenericsNest<?>) returnsDeepStubs.answer(Mockito.mock(InvocationOnMock.class).withArguments(mockGenericsNest, ArgumentMatchers.eq(nonMockableType)));
 }

 private void mockitoCoreSetup() {
 MockitoCore mockitoCore = MockitoCore.getInstance();
 mockitoCore.registerDefaultAnswer("ReturnsDeepStubs", new ReturnsDeepStubs());
 }
}