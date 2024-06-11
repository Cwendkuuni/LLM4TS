package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object deepMock;

 @Before
 public void setup() {
 returnsDeepStubs = new ReturnsDeepStubs();
 deepMock = mock(Object.class, with(ReturnsDeepStubs.RETURNS_DEEP_STUBS));
 }

 @Test
 public void testDeepStubForInterface() throws Exception {
 // given
 InterfaceMock interfaceMock = mock(InterfaceMock.class, with(ReturnsDeepStubs.RETURNS_DEEP_STUBS));
 InterfaceInner inner = new InterfaceInner();
 // when
 InterfaceMock deepInterface = returnsDeepStubs.getMock(new InvocationOnMock(interfaceMock, ArgumentMatchers.any()),
 GenericMetadataSupport.parse(InterfaceMock.class, InterfaceInner.class));
 InterfaceInner deepInner = (InterfaceInner) deepInterface.getInner();
 deepInner.setNumber(2);
 // then
 assertEquals(2, deepInner.getNumber());
 // when calling the same method on the deep mock
 InterfaceInner shallowInner = interfaceMock.getInner();
 shallowInner.setNumber(3);
 // then
 assertEquals(2, deepInner.getNumber());
 assertEquals(3, shallowInner.getNumber());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeepStubForNonMockableType() throws Exception {
 // given
 Object nonMockableType = new Object();
 // when
 try {
 returnsDeepStubs.getMock(new InvocationOnMock(nonMockableType, ArgumentMatchers.any()),
 GenericMetadataSupport.parse(Object.class, Object.class));
 } catch (IllegalArgumentException e) {
 // then
 throw e;
 }
 }

 @Test
 public void testDeepStubForNestedGenericTypes() throws Exception {
 // given
 InterfaceNested interfaceNested = mock(InterfaceNested.class, with(ReturnsDeepStubs.RETURNS_DEEP_STUBS));
 Map<String, Map<String, Number>> map = map(keySet(ArgumentMatchers.anyString()),
 valueSet(map(ArgumentMatchers.anyString(), ArgumentMatchers.any(Number.class))));
 Map<String, Map<String, Number>> deepMap = returnsDeepStubs.getMock(new InvocationOnMock(interfaceNested, ArgumentMatchers.any()),
 GenericMetadataSupport.parse(InterfaceNested.class, Map.class.getClass(), String.class, Map.class.getClass(), String.class, Number.class));
 Map<String, Map<String, Number>> shallowMap = interfaceNested.getMap();
 Map<String, Map<String, Number>> deepMapEntrySet = deepMap.entrySet();
 Map<String, Map<String, Number>> deepMapEntrySetIterator = deepMapEntrySet.iterator();
 Map<String, Number> deepMapEntryValue = deepMapEntrySetIterator.next().getValue();
 Map<String, Number> deepMapEntryValueIterator = deepMapEntryValue.iterator();
 Number deepMapEntryValueNumber = deepMapEntryValueIterator.next();
 // when
 shallowMap.put("key", map(ArgumentMatchers.anyString(), ArgumentMatchers.any(Number.class)));
 // then
 assertEquals(1, deepMapEntrySet.size());
 assertEquals(1, deepMapEntryValue.size());
 assertEquals(1, deepMapEntryValueNumber.intValue());
 }
}

interface InterfaceMock {
 interface InterfaceInner {
 int getNumber();
 void setNumber(int number);
 InterfaceInner getInner();
 }
}

interface InterfaceNested {
 Map<String, Map<String, Number>> getMap();
}