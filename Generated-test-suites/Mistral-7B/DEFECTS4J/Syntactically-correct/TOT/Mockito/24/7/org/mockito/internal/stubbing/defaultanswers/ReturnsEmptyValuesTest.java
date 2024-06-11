package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues answer;
 private Object mock;

 @Before
 public void setUp() {
 answer = new ReturnsEmptyValues();
 mock = mock(Object.class);
 }

 @Test
 public void testToString() {
 when(mock.toString()).thenAnswer(answer);
 assertEquals("Mock for Object, hashCode: " + mock.hashCode(), mock.toString());
 }

 @Test
 public void testIsDefaultMockName() {
 when(answer.answer(Mockito.any())).thenReturn("Mock for Object, hashCode: 123456");
 MockName name = Mockito.mock(MockName.class);
 Mockito.when(name.isDefault()).thenReturn(true);
 assertEquals("Mock for Object, hashCode: 123456", name.toString());
 }

 @Test
 public void testIsCompareToMethod() {
 when(answer.answer(Mockito.argThat(ArgumentMatchers.isInstanceOf(Comparable.class)))).thenReturn(1);
 Integer first = new Integer(42);
 Integer second = new Integer(42);
 assertEquals(0, first.compareTo(second));
 }

 @Test
 public void testReturnPrimitives() {
 when(answer.answer(Mockito.anyInt())).thenReturn(1);
 assertEquals(1, answer.answer(Mockito.anyInt()));

 when(answer.answer(Mockito.anyByte())).thenReturn((byte) 1);
 assertEquals((byte) 1, answer.answer(Mockito.anyByte()));

 when(answer.answer(Mockito.anyShort())).thenReturn((short) 1);
 assertEquals((short) 1, answer.answer(Mockito.anyShort()));

 when(answer.answer(Mockito.anyChar())).thenReturn((char) 1);
 assertEquals((char) 1, answer.answer(Mockito.anyChar()));

 when(answer.answer(Mockito.anyBoolean())).thenReturn(true);
 assertEquals(true, answer.answer(Mockito.anyBoolean()));

 when(answer.answer(Mockito.anyLong())).thenReturn(1L);
 assertEquals(1L, answer.answer(Mockito.anyLong()));

 when(answer.answer(Mockito.anyFloat())).thenReturn((float) 1.0);
 assertEquals((float) 1.0, answer.answer(Mockito.anyFloat()));

 when(answer.answer(Mockito.anyDouble())).thenReturn(1.0);
 assertEquals(1.0, answer.answer(Mockito.anyDouble()));
 }

 @Test
 public void testReturnCollections() {
 when(answer.answer(Mockito.any(Collection.class))).thenReturn(Collections.emptyList());
 assertEquals(Collections.emptyList(), answer.answer(Mockito.any(Collection.class)));

 when(answer.answer(Mockito.any(Set.class))).thenReturn(Collections.emptySet());
 assertEquals(Collections.emptySet(), answer.answer(Mockito.any(Set.class)));

 when(answer.answer(Mockito.any(HashSet.class))).thenReturn(Collections.emptySet());
 assertEquals(Collections.emptySet(), answer.answer(Mockito.any(HashSet.class)));

 when(answer.answer(Mockito.any(List.class))).thenReturn(new ArrayList<>());
 assertEquals(new ArrayList<>(), answer.answer(Mockito.any(List.class)));

 when(answer.answer(Mockito.any(LinkedList.class))).thenReturn(new LinkedList<>());
 assertEquals(new LinkedList<>(), answer.answer(Mockito.any(LinkedList.class)));

 when(answer.answer(Mockito.any(ArrayList.class))).thenReturn(new ArrayList<>());
 assertEquals(new ArrayList<>(), answer.answer(Mockito.any(ArrayList.class)));
 }

 @Test
 public void testReturnMaps() {
 when(answer.answer(Mockito.any(Map.class))).thenReturn(new HashMap<>());
 assertEquals(new HashMap<>(), answer.answer(Mockito.any(Map.class)));

 when(answer.answer(Mockito.any(HashMap.class))).thenReturn(new HashMap<>());
 assertEquals(new HashMap<>(), answer.answer(Mockito.any(HashMap.class)));

 when(answer.answer(Mockito.any(SortedMap.class))).thenReturn(new TreeMap<>());
 assertEquals(new TreeMap<>(), answer.answer(Mockito.any(SortedMap.class)));

 when(answer.answer(Mockito.any(TreeMap.class))).thenReturn(new TreeMap<>());
 assertEquals(new TreeMap<>(), answer.answer(Mockito.any(TreeMap.class)));

 when(answer.answer(Mockito.any(LinkedHashMap.class))).thenReturn(new LinkedHashMap<>());
 assertEquals(new LinkedHashMap<>(), answer.answer(Mockito.any(LinkedHashMap.class)));
 }
}