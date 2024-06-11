package com.example.junit.test; // replace with your test package if needed

import static org.junit.Assert.*;

import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TDoubleShortOpenHashMap();
 decorator = new TDoubleShortMapDecorator(_map);
 }

 @Test
 public void testConstructor() {
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
 assertNotNull(decorator);
 }

 @Test
 public void testConstructorWithMap() {
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(_map);
 assertEquals(decorator._map, _map);
 }

 @Test
 public void testGetMap() {
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(_map);
 TDoubleShortMap map = decorator.getMap();
 assertEquals(decorator._map, map);
 }

 @Test
 public void testPut() {
 decorator.put(1.0d, (short)1);
 assertEquals(1, decorator.size());
 Short value = decorator.get(1.0d);
 assertEquals((short)1, value);
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
 decorator.put(null, (short)1);
 fail("NullPointerException expected but not thrown");
 }

 @Test
 public void testPutWithNullValue() {
 decorator.put(1.0d, null);
 assertEquals(1, decorator.size());
 assertNull(decorator.get(1.0d));
 }

 @Test
 public void testGetWithNullKey() {
 assertNull(decorator.get(null));
 }

 @Test(expected = ClassCastException.class)
 public void testGetWithNonDoubleKey() {
 decorator.put(1, (short)1);
 assertNull(decorator.get("1"));
 }

 @Test
 public void testClear() {
 decorator.put(1.0d, (short)1);
 decorator.clear();
 assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 decorator.put(1.0d, (short)1);
 assertEquals(1, decorator.size());
 decorator.remove(1.0d);
 assertEquals(0, decorator.size());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
 decorator.remove(null);
 fail("NullPointerException expected but not thrown");
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 assertEquals(0, entrySet.size());
 }

 @Test
 public void testContainsValue() {
 decorator.put(1.0d, (short)1);
 assertTrue(decorator.containsValue((short)1));
 }

 @Test
 public void testContainsKey() {
 decorator.put(1.0d, (short)1);
 assertTrue(decorator.containsKey(1.0d));
 }

 @Test
 public void testSize() {
 decorator.put(1.0d, (short)1);
 assertEquals(1, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 assertTrue(decorator.isEmpty());
 decorator.put(1.0d, (short)1);
 assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap map = new TDoubleShortOpenHashMap();
 map.put(1.0d, (short)1);
 map.put(2.0d, (short)2);
 decorator.putAll(map);
 assertEquals(2, decorator.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddEntrySet() {
 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 entrySet.add(null);
 fail("UnsupportedOperationException expected but not thrown");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveEntrySet() {
 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 entrySet.remove(null);
 fail("UnsupportedOperationException expected but not thrown");
 }

 @Test
 public void testContainsKeySet() {
 TDoubleShortMap map = new TDoubleShortOpenHashMap();
 map.put(1.0d, (short)1);
 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 assertFalse(entrySet.contains(null));
 assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(1.0d, (short)1)));
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(_map);
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(output);
 decorator.writeExternal(out);
 byte[] data = output.toByteArray();
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(data));
 TDoubleShortMapDecorator newDecorator = (TDoubleShortMapDecorator)in.readObject();
 TDoubleShortMap newMap = newDecorator.getMap();
 assertEquals(_map, newMap);
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(_map);
 TDoubleShortMapDecorator cloned = (TDoubleShortMapDecorator)decorator.clone();
 TDoubleShortMap clonedMap = cloned.getMap();
 assertEquals(_map, clonedMap);
 }
}