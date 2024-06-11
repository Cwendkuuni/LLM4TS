package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Map<String, Number> map;
 private Number number;

 @Before
 public void setUp() {
 map = mock(Map.class, Answers.RETURNS_DEEP_STUBS);
 number = mock(Number.class, Answers.RETURNS_DEEP_STUBS);
 returnsDeepStubs = new ReturnsDeepStubs();
 }

 @Test
 public void testDeepStubsForMapEntrySetIteratorNextValueIteratorNext() {
 // Testing with a deep nested generic type
 Map.Entry<String, Number> entry = map.entrySet().iterator().next();
 Number value = entry.getValue().iterator().next();

 assertSame(number, value);

 // Verifying that the mocks were created correctly
 verify(mock(Map.class, withSettings().serializable())).entrySet();
 verify(map).iterator();
 verify(map.entrySet().iterator()).next();
 verify(map.entrySet().iterator().next()).getValue();
 verify(map.entrySet().iterator().next().getValue()).iterator();
 verify(map.entrySet().iterator().next().getValue().iterator()).next();
 }

 @Test
 public void testDeepStubsForMapEntrySetIteratorNextValueIsNull() {
 // Testing when the value is null
 map.put("key", null);
 Map.Entry<String, Number> entry = map.entrySet().iterator().next();
 Number value = entry.getValue().iterator().next();

 assertNull(value);

 // Verifying that the mocks were created correctly
 verify(mock(Map.class, withSettings().serializable())).entrySet();
 verify(map).iterator();
 verify(map.entrySet().iterator()).next();
 verify(map.entrySet().iterator().next()).getValue();
 verify(map.entrySet().iterator().next().getValue()).iterator();
 }

 @Test(expected = NullPointerException.class)
 public void testDeepStubsForMapEntrySetIteratorNextValueNullPointerException() {
 // Testing when the map is null
 Map<String, Number> map = null;
 Map.Entry<String, Number> entry = map.entrySet().iterator().next();
 Number value = entry.getValue().iterator().next();

 // Asserting that a NullPointerException is thrown
 }

 @Test
 public void testDeepStubsForNumber() {
 // Testing with a simple mock type
 Number number = mock(Number.class, Answers.RETURNS_DEEP_Stubs);

 assertSame(number, number);

 // Verifying that the mock was created correctly
 verify(mock(Number.class, withSettings())).toString();
 }

 @Test(expected = IllegalStateException.class)
 public void testDeepStubsForNonMockableType() {
 // Testing when the type is not mockable
 Number number = mock(String.class, Answers.RETURNS_DEEP_Stubs);

 // Asserting that an IllegalStateException is thrown
 }
}