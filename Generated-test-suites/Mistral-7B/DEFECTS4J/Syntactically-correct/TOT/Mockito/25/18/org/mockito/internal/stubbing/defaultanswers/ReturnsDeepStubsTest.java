package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.mockito.internal.stubbing.answers.ReturnsDeepStubsAnswer.returnsDeepStubs;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Map<String, Set<Number>> map;
 private Set<Number> set;
 private Number number;

 @Before
 public void setup() {
 mockStatic(Mockito.class);
 when(Mockito.mock(Map.class)).then(returnsDeepStubs(new MyMap()));
 when(Mockito.mock(Set.class)).then(returnsDeepStubs(new MySet()));
 when(Mockito.mock(Number.class)).then(returnsDeepStubs(new MyNumber()));
 returnsDeepStubs = new ReturnsDeepStubs();
 }

 @Test
 public void testDeepStubbingMapEntrySet() {
 map = mock(Map.class);

 // typical use case
 List<Map.Entry<String, Set<Number>>> entryList = new ArrayList<>();
 Map.Entry<String, Set<Number>> entry = new AbstractMap.SimpleEntry<>("key", set);
 entryList.add(entry);
 when(map.entrySet()).thenReturn(entryList);
 String key = "key";
 Set<Number> numbers = new ArrayList<>();
 Number number = 1;
 numbers.add(number);
 when(set.iterator()).thenReturn(numbers.iterator());

 // deep stubbing the nested Set and Number
 when(numbers.iterator().next()).thenReturn(number);

 assertSame(map, when(map).thenCallRealMethod().thenReturn(map).thenReturn(map).thenCallRealMethod());

 verify(map).entrySet();
 verify(map.entrySet()).iterator().next().getValue().iterator().next();
 verify(set).iterator();
 verify(numbers).iterator();
 verify(numbers.iterator().next()).equals(number);
 }

 @Test(expected = RuntimeException.class)
 public void testDeepStubbingMapEntrySetThrowsExceptionWhenReturnTypeIsNotMockable() {
 map = mock(Map.class);
 when(map.entrySet()).thenReturn(new ArrayList<>());
 assertThrows(RuntimeException.class, () -> when(map).thenCallRealMethod());
 }

 @Test
 public void testDeepStubbingMapEntrySetReturnsDifferentInstances() {
 map = mock(Map.class);

 // typical use case
 List<Map.Entry<String, Set<Number>>> entryList = new ArrayList<>();
 Map.Entry<String, Set<Number>> entry1 = new AbstractMap.SimpleEntry<>("key1", set);
 Map.Entry<String, Set<Number>> entry2 = new AbstractMap.SimpleEntry<>("key2", set);
 entryList.add(entry1);
 entryList.add(entry2);
 when(map.entrySet()).thenReturn(entryList);
 String key1 = "key1";
 String key2 = "key2";
 Set<Number> numbers = new ArrayList<>();
 Number number = 1;
 numbers.add(number);
 when(set.iterator()).thenReturn(numbers.iterator());
 when(numbers.iterator().next()).thenReturn(number);

 // deep stubbing the nested Set and Number
 when(numbers.iterator().next()).thenReturn(number);

 assertNotSame(map, when(map).thenCallRealMethod().thenReturn(map).thenReturn(map).thenCallRealMethod());
 }

 @Test
 public void testDeepStubbingSetIterator() {
 set = mock(Set.class);

 // typical use case
 List<Number> numberList = new ArrayList<>();
 number = 1;
 numberList.add(number);
 when(set.iterator()).thenReturn(numberList.iterator());

 // deep stubbing the nested Number
 when(numberList.get(0)).thenReturn(number);

 assertSame(set, when(set).thenCallRealMethod().thenReturn(set).thenReturn(set).thenCallRealMethod());
 }

 @Test(expected = RuntimeException.class)
 public void testDeepStubbingSetIteratorThrowsExceptionWhenReturnTypeIsNotMockable() {
 set = mock(Set.class);
 when(set.iterator()).thenReturn(Collections.emptyList().iterator());
 assertThrows(RuntimeException.class, () -> when(set).thenCallRealMethod());
 }

 @Test
 public void testDeepStubbingSetIteratorReturnsDifferentInstances() {
 set = mock(Set.class);
 List<Number> numberList1 = new ArrayList<>();
 List<Number> numberList2 = new ArrayList<>();
 Number number = 1;
 numberList1.add(number);
 numberList2.add(number);
 when(set.iterator()).thenReturn(numberList1.iterator());
 when(numberList1.get(0)).thenReturn(number);
 when(set.iterator()).thenReturn(numberList2.iterator());
 when(numberList2.get(0)).thenReturn(number);

 assertNotSame(set, when(set).thenCallRealMethod().thenReturn(set).thenReturn(set).thenCallRealMethod());
 }

 @Test
 public void testDeepStubbingNumber() {
 number = mock(Number.class);

 // deep stubbing the Number
 when(number.intValue()).thenReturn(1);

 assertSame(number, when(number).thenCallRealMethod().thenReturn(number).thenReturn(number).thenCallRealMethod());
 }

 @Test(expected = RuntimeException.class)
 public void testDeepStubbingNumberThrowsExceptionWhenReturnTypeIsNotMockable() {
 number = mock(Number.class);
 assertThrows(RuntimeException.class, () -> when(number).thenCallRealMethod());
 }

 @Test
 public void testDeepStubbingNumberReturnsDifferentInstances() {
 number = mock(Number.class);
 Number number2 = mock(Number.class);
 when(number.intValue()).thenReturn(1);
 when(number2.intValue()).thenReturn(2);

 assertNotSame(number, when(number).thenCallRealMethod().thenReturn(number).thenReturn(number).thenCallRealMethod());
 }
}

class MyMap implements Map<String, Set<Number>> {
 @Override public int size() { return 0; }
 @Override public boolean isEmpty() { return true; }
 @Override public boolean containsKey(Object key) { return false; }
 @Override public boolean containsValue(Object value) { return false; }
 @Override public Set<Map.Entry<String, Set<Number>>> entrySet() { return Collections.emptySet(); }
 @Override public Set<String> keySet() { return Collections.emptySet(); }
 @Override public Collection<Set<Number>> values() { return Collections.emptyList(); }
 @Override public Set<Map.Entry<String, Set<Number>> > entrySet() { return Collections.emptySet(); }
 @Override public Map.Entry<String, Set<Number>> put(String key, Set<Number> value) { return null; }
 @Override public void putAll(Map<? extends String, ? extends Set<Number>> m) { }
 @Override public Set<Number> get(Object key) { return null; }
 @Override public Set<Number> remove(Object key) { return null; }
 @Override public void clear() { }
 @Override public boolean equals(Object obj) { return false; }
 @Override public int hashCode() { return 0; }
}

class MySet extends AbstractSet<Number> {
 @Override public int size() { return 0; }
 @Override public boolean isEmpty() { return true; }
 @Override public boolean contains(Object o) { return false; }
 @Override public Iterator<Number> iterator() { return Collections.emptyIterator(); }
 @Override public Object[] toArray() { return new Object[0]; }
 @Override public <T> T[] toArray(T[] a) { return null; }
 @Override public boolean add(Number e) { return false; }
 @Override public boolean remove(Object o) { return false; }
 @Override public boolean containsAll(Collection<?> c) { return false; }
 @Override public boolean addAll(Collection<? extends Number> c) { return false; }
 @Override public void clear() { }
 @Override public boolean equals(Object o) { return false; }
 @Override public int hashCode() { return 0; }
}

class MyNumber implements Number {
 @Override public int intValue() { return 0; }
 @Override public long longValue() { return 0; }
 @Override public float floatValue() { return 0f; }
 @Override public double doubleValue() { return 0d; }
 @Override public boolean equals(Object obj) { return false; }
 @Override public int hashCode() { return 0; }
}