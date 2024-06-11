package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockitoMock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues;
 private MockitoMock mock;

 @Before
 public void setup() {
 returnsEmptyValues = new ReturnsEmptyValues();
 mock = new MockitoMock(MockitoMock.Mode.STRICT);
 }

 @Test
 public void testToStringAnswer() {
 when(mock.toString()).thenAnswer(returnsEmptyValues);

 String expected = "Mock for " + MockitoMock.class.getSimpleName() + ", hashCode: " + mock.hashCode();
 String actual = mock.toString();

 verify(mock).toString();
 assert(expected.equals(actual));
 }

 @Test
 public void testCompareToAnswer() {
 when(mock.toString()).thenAnswer(returnsEmptyValues);
 when(mock.compareTo(ArgumentMatchers.any())).thenAnswer(returnsEmptyValues);

 int result = mock.compareTo(mock);

 verify(mock).compareTo(mock);
 assert(1 == result);
 }

 @Test
 public void testReturnPrimitiveOrWrapperType() {
 when(mock.hashCode()).thenAnswer(returnsEmptyValues);

 int intResult = mock.hashCode();
 long longResult = mock.hashCode();
 float floatResult = mock.hashCode();
 double doubleResult = mock.hashCode();
 boolean booleanResult = mock.hashCode();

 verify(mock).hashCode();

 assert(0 == intResult);
 assert(0L == longResult);
 assert(0.0f == floatResult);
 assert(0.0d == doubleResult);
 assert(false == booleanResult);
 }

 @Test
 public void testCollectionReturnType() {
 when(mock.getClass()).thenReturn(ArrayList.class);
 when(mock.newInstance()).thenReturn(new ArrayList<>());
 when(mock.iterator()).thenReturn(mock.newInstance().iterator());
 when(mock.isEmpty()).thenAnswer(returnsEmptyValues);

 List<String> list = (List<String>) mock.newInstance();

 assert(list.isEmpty());
 }

 @Test
 public void testSetReturnType() {
 when(mock.getClass()).thenReturn(HashSet.class);
 when(mock.newInstance()).thenReturn(new HashSet<>());
 when(mock.add(ArgumentMatchers.any())).thenAnswer(invocation -> {
 boolean oldValue = invocation.getMock().contains(invocation.getArguments()[0]);
 invocation.getMock().add(invocation.getArguments()[0]);
 return !oldValue;
 });
 when(mock.isEmpty()).thenAnswer(returnsEmptyValues);

 Set<String> set = (Set<String>) mock.newInstance();

 assert(set.isEmpty());
 }

 @Test
 public void testSortedSetReturnType() {
 when(mock.getClass()).thenReturn(TreeSet.class);
 when(mock.newInstance()).thenReturn(new TreeSet<>());
 when(mock.add(ArgumentMatchers.any())).thenAnswer(invocation -> {
 boolean oldValue = invocation.getMock().contains(invocation.getArguments()[0]);
 invocation.getMock().add(invocation.getArguments()[0]);
 return !oldValue;
 });
 when(mock.isEmpty()).thenAnswer(returnsEmptyValues);

 SortedSet<String> sortedSet = (SortedSet<String>) mock.newInstance();

 assert(sortedSet.isEmpty());
 }

 @Test
 public void testMapReturnType() {
 when(mock.getClass()).thenReturn(HashMap.class);
 when(mock.newInstance()).thenReturn(new HashMap<>());
 when(mock.isEmpty()).thenAnswer(returnsEmptyValues);

 Map<String, String> map = (Map<String, String>) mock.newInstance();

 assert(map.isEmpty());
 }

 @Test
 public void testSortedMapReturnType() {
 when(mock.getClass()).thenReturn(TreeMap.class);
 when(mock.newInstance()).thenReturn(new TreeMap<>());
 when(mock.isEmpty()).thenAnswer(returnsEmptyValues);

 SortedMap<String, String> sortedMap = (SortedMap<String, String>) mock.newInstance();

 assert(sortedMap.isEmpty());
 }

 @Test
 public void testLinkedHashMapReturnType() {
 when(mock.getClass()).thenReturn(LinkedHashMap.class);when(mock.newInstance()).thenReturn(new LinkedHashMap<>());
 when(mock.isEmpty()).thenAnswer(returnsEmptyValues);

 Map<String, String> map = (Map<String, String>) mock.newInstance();

 assert(map.isEmpty());
 }

 @Test
 public void testLinkedHashSetReturnType() {
 when(mock.getClass()).thenReturn(LinkedHashSet.class);
 when(mock.newInstance()).thenReturn(new LinkedHashSet<>());
 when(mock.add(ArgumentMatchers.any())).thenAnswer(invocation -> {
 boolean oldValue = invocation.getMock().contains(invocation.getArguments()[0]);
 invocation.getMock().add(invocation.getArguments()[0]);
 return !oldValue;
 });
 when(mock.isEmpty()).thenAnswer(returnsEmptyValues);

 Set<String> set = (Set<String>) mock.newInstance();

 assert(set.isEmpty());
 }

 @Test
 public void testLinkedListReturnType() {
 when(mock.getClass()).thenReturn(LinkedList.class);
 when(mock.newInstance()).thenReturn(new LinkedList<>());
 when(mock.isEmpty()).thenAnswer(returnsEmptyValues);

 List<String> list = (List<String>) mock.newInstance();

 assert(list.isEmpty());
 }
}