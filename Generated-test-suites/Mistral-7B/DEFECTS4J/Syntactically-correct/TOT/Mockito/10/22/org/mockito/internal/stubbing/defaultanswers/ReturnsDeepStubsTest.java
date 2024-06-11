package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object deepStubMock;

 @Before
 public void setUp() {
 returnsDeepStubs = new ReturnsDeepStubs();
 deepStubMock = Mockito.mock(Map.class, Mockito.withSettings().defaultAnswer(returnsDeepStubs));
 }

 @Test
 public void testDeepStubForSimpleType() {
 Map<Integer, String> deepStubMap = (Map) deepStubMock.entrySet().iterator().next().getValue();
 assertEquals("deep stub for simple type", deepStubMap.get(1));
 }

 @Test
 public void testDeepStubForNestedType1() {
 Map<String, Map<Integer, Number>> deepStubMap = (Map) deepStubMock.entrySet().iterator().next().getValue();
 Map<Integer, Number> nestedMap = deepStubMap.values().iterator().next();
 Number number = nestedMap.iterator().next();
 assertEquals("deep stub for nested type 1", number.doubleValue(), 1.5);
 }

 @Test
 public void testDeepStubForNestedType2() {
 Map<String, Map<Integer, Number>> deepStubMap = (Map) deepStubMock.entrySet().iterator().next().getValue();
 Map<Integer, Number> nestedMap = deepStubMap.values().iterator().next();
 Number number = nestedMap.iterator().next();
 number.setDouble(2.5);
 Map<String, Map<Integer, Number>> deepStubMap2 = (Map) deepStubMock.entrySet().iterator().next().getValue();
 Map<Integer, Number> nestedMap2 = deepStubMap2.values().iterator().next();
 Number number2 = nestedMap2.iterator().next();
 assertEquals("deep stub for nested type 2", number2.doubleValue(), 2.5);
 }

 @Test(expected = IllegalStateException.class)
 public void testDeepStubForUnmockableType() {
 IOResource deepStubIoResource = (IOResource) deepStubMock; // IOException is not mockable
 }

 @Test
 public void testDeepStubForAnonymousClass() {
 Map<String, Map<Integer, Number>> deepStubMap = (Map) deepStubMock.entrySet().iterator().next().getValue();
 deepStubMap.put("test", new Object() {
 @Override
 public int hashCode() {
 return 1;
 }
 }.getClass(), Mockito.mock(Number.class, Mockito.withSettings().defaultAnswer(returnsDeepStubs)));
 Map<String, Map<Integer, Number>> deepStubMap2 = (Map) deepStubMock.entrySet().iterator().next().getValue();
 Number number = deepStubMap2.get("test");
 assertEquals("deep stub for anonymous class", number.doubleValue(), 1.5);
 }

 @Test
 public void testDeepStubForLambdaExpression() {
 Map<String, Map<Integer, Number>> deepStubMap = (Map) deepStubMock.entrySet().iterator().next().getValue();
 deepStubMap.put("test", (Map<Integer, Number> map) -> Mockito.mock(Number.class, Mockito.withSettings().defaultAnswer(returnsDeepStubs)));
 Map<String, Map<Integer, Number>> deepStubMap2 = (Map) deepStubMock.entrySet().iterator().next().getValue();
 Number number = deepStubMap2.get("test");
 assertEquals("deep stub for lambda expression", number.doubleValue(), 1.5);
 }
}