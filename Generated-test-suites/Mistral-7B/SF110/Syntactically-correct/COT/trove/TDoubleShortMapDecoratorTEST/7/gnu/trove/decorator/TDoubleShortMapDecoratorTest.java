package com.example.junit.test; // replace with your package name if necessary

import static org.junit.Assert.*;

import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import org.junit.*;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setUp() {
     _map = new TDoubleShortHashMap();
     decorator = new TDoubleShortMapDecorator(_map);
 }

 @Test
 public void testConstructor() {
     assertNotNull(decorator);
 }

 @Test
 public void testGetMap() {
     assertSame(decorator._map, decorator.getMap());
 }

 @Test
 public void testPutNullKey() {
     assertNull(decorator.put(null, (short) 0));
 }

 @Test
 public void testPutNullValue() {
     decorator.put((double) 1.0, (Double) 2.0); // test put with Double key and Object value
     assertNull(decorator.put((double) 1.0, null));
 }

 @Test
 public void testPutExistingKey() {
     decorator.put((double) 1.0, (short) 1);
     assertSame((short) 2, decorator.put((double) 1.0, (short) 2));
 }

 @Test
 public void testGetNullKey() {
     assertNull(decorator.get(null));
 }

 @Test
 public void testGetObjectKey() {
     decorator.put((double) 1.0, (short) 1);
     assertEquals((short) 1, decorator.get((Double) 1.0));
 }

 @Test(expected = ClassCastException.class)
 public void testGetClassCastException() {
     decorator.put((double) 1.0, (short) 1);
     decorator.get("String");
 }

 @Test
 public void testClear() {
     decorator.put((double) 1.0, (short) 1);
     decorator.clear();
     assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemoveNullKey() {
     decorator.put((double) 1.0, (short) 1);
     assertNull(decorator.remove(null));
 }

 @Test
 public void testRemoveExistingKey() {
     decorator.put((double) 1.0, (short) 1);
     assertEquals((short) 1, decorator.remove((Double) 1.0));
     assertNull(decorator.remove((Double) 1.0));
 }

 @Test
 public void testRemoveNonExistingKey() {
     assertNull(decorator.remove((Double) 2.0));
 }

 @Test
 public void testSize() {
     assertEquals(0, decorator.size());
     decorator.put((double) 1.0, (short) 1);
     assertEquals(1, decorator.size());
 }

 @Test
 public void testIsEmpty() {
     assertTrue(decorator.isEmpty());
     decorator.put((double) 1.0, (short) 1);
     assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
     TDoubleShortMap map = new TDoubleShortHashMap();
     map.put((double) 1.0, (short) 1);
     map.put((double) 2.0, (short) 2);

     decorator.putAll(map);
     assertEquals(2, decorator.size());
     assertTrue(decorator.containsKey((Double) 1.0));
     assertTrue(decorator.containsKey((Double) 2.0));
 }

 @Test
 public void testContainsValue() {
     decorator.put((double) 1.0, (short) 1);
     assertTrue(decorator.containsValue((short) 1));
 }

 @Test
 public void testContainsKey() {
     decorator.put((double) 1.0, (short) 1);
     assertTrue(decorator.containsKey((Double) 1.0));
 }

 @Test
 public void testEntrySet() {
     decorator.put((double) 1.0, (short) 1);
     decorator.put((double) 2.0, (short) 2);

     Set<Entry<Double, Short>> entrySet = decorator.entrySet();
     assertEquals(2, entrySet.size());
     assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(1.0d, (short) 1)));
     assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(2.0d, (short) 2)));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddEntrySet() {
     decorator.put((double) 1.0, (short) 1);
     Set<Entry<Double, Short>> entrySet = decorator.entrySet();
     entrySet.add(new AbstractMap.SimpleEntry<>(1.0d, (short) 2));
 }

 @Test
 public void testRemoveEntrySet() {
     decorator.put((double) 1.0, (short) 1);
     decorator.put((double) 2.0, (short) 2);

     Set<Entry<Double, Short>> entrySet = decorator.entrySet();
     assertTrue(entrySet.remove(new AbstractMap.SimpleEntry<>(1.0d, (short) 1)));
     assertEquals(1, decorator.size());
 }

 @Test
 public void testContainsValueNull() {
     decorator.put((double) 1.0, (short) 1);
     assertFalse(decorator.containsValue(null));
 }

 @Test
 public void testContainsKeyNull() {
     decorator.put((double) 1.0, (short) 1);
     assertFalse(decorator.containsKey(null));
 }

 // Add more test cases to cover edge cases and exception handling scenarios

 @After
 public void tearDown() {
     decorator = null;
 }
}