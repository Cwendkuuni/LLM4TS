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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mock1;
 private Object mock2;
 private Object mock3;
 private Object mock4;

 @Before
 public void setup() {
 returnsDeepStubs = new ReturnsDeepStubs();
 mock1 = mock(Map.class, returnsDeepStubs);
 mock2 = mock(Number.class);
 mock3 = mock(Number.class);
 mock4 = mock(Map.Entry.class, ArgumentMatchers.any(), mock2);
 }

 @Test
 public void testAnswerWithSameInstance() {
 Map<Object, Object> map = (Map<Object, Object>) returnsDeepStubs.answer(Mockito.argThat(invocation -> {
 return invocation.getMethod().getReturnType() == Map.class;
 }));

 verify(mock1).entrySet();
 verify(mock1).entrySet().iterator();
 verify(mock1.entrySet().iterator()).next();
 verify(mock1.entrySet().iterator().next()).getValue();
 verify(mock1.entrySet().iterator().next()).getValue().iterator();
 verify(mock1.entrySet().iterator().next().getValue().iterator()).next();

 Map<Object, Object> map2 = (Map<Object, Object>) returnsDeepStubs.answer(Mockito.argThat(invocation -> {
 return invocation.getMethod().getReturnType() == Map.class;
 }));

 assertEquals(map, map2);
 }

 @Test
 public void testAnswerWithNewInstance() {
 Map<Object, Object> map1 = (Map<Object, Object>) returnsDeepStubs.answer(Mockito.argThat(invocation -> {
 return invocation.getMethod().getReturnType() == Map.class;
 }));

 Map<Object, Object> map2 = (Map<Object, Object>) returnsDeepStubs.answer(Mockito.argThat(invocation -> {
 return invocation.getMethod().getReturnType() == Map.class;
 }));

 assertNotSame(map1, map2);
 }

 @Test
 public void testRecordDeepStubMock() throws Throwable {
 Map<Object, Object> map = (Map<Object, Object>) returnsDeepStubs.answer(Mockito.argThat(invocation -> {
 return invocation.getMethod().getReturnType() == Map.class;
 }));

 verifyZeroInteractions(map);

 Map<Object, Object> map2 = (Map<Object, Object>) returnsDeepStubs.answer(Mockito.argThat(invocation -> {
 return invocation.getMethod().getReturnType() == Map.class;
 }));

 assertEquals(map, map2);

 verify(mock1).entrySet();
 verify(mock1.entrySet().iterator()).next();
 verify(mock1.entrySet().iterator().next()).getValue();
 verify(mock1.entrySet().iterator().next().getValue()).iterator();
 verify(mock1.entrySet().iterator().next().getValue().iterator()).next();
 verify(mock2).equals(mock3);
 }

 @Test(expected = IllegalStateException.class)
 public void testCannotMockRawType() {
 Object rawTypeMock = mock(Object.class, returnsDeepStubs);
 }
}