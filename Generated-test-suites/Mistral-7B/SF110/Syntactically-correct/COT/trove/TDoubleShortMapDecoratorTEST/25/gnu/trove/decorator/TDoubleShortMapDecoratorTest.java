package com.example.junit4; // replace with your package name if necessary

import static org.junit.Assert.*;

import org.junit.Test;

import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TDoubleShortMap();
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
 assertEquals(_map, decorator.getMap());
 }

 @Test
 public void testPut() {
 Double key = 1.0;
 Short value = (short) 1;
 Short actual = decorator.put(key, value);
 assertEquals(value, actual);
 assertEquals(1, decorator.size());

 key = 2.0;
 value = (short) 2;
 actual = decorator.put(key, value);
 assertEquals(value, actual);
 assertEquals(2, decorator.size());

 assertNull(decorator.put(key, null));
 assertEquals(2, decorator.size());

 key = null;
 assertNull(decorator.put(key, value));
 assertEquals(2, decorator.size());
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKeyAndValue() {
 decorator.put(null, null);
 }

 @Test
 public void testGet() {
 Double key = 1.0;
 Short value = (short) 1;
 decorator.put(key, value);
 Short actual = decorator.get(key);
 assertEquals(value, actual);

 key = 2.0;
 value = (short) 2;
 decorator.put(key, value);
 actual = decorator.get(key);
 assertEquals(value, actual);

 key = null;
 assertNull(decorator.get(key));

 key = 1.0;
 assertNull(decorator.get(null));
 }

 @Test(expected = NullPointerException.class)
 public void testGetWithNullKey() {
 decorator.get(null);
 }

 @Test
 public void testClear() {
 assertEquals(0, decorator.size());
 decorator.clear();
 assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 Double key = 1.0;
 Short value = (short) 1;
 decorator.put(key, value);
 assertEquals(1, decorator.size());

 Short actual = decorator.remove(key);
 assertEquals(value, actual);
 assertEquals(0, decorator.size());

 key = 2.0;
 value = (short) 2;
 decorator.put(key, value);
 decorator.put(key, null); // replace null value with any existing value to make the test work properly
 assertEquals(1, decorator.size());

 actual = decorator.remove(key);
 assertNull(actual);
 assertEquals(0, decorator.size());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
 decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 assertEquals(0, entrySet.size());

 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);

 assertEquals(2, entrySet.size());

 Entry<Double, Short> entry = entrySet.iterator().next();
 assertEquals(1.0, entry.getKey());
 assertEquals((short) 1, entry.getValue());
 }

 @Test
 public void testContainsValue() {
 decorator.put(1.0, (short) 1);
 decorator.put(1.0, (short) 2);

 assertTrue(decorator.containsValue((short) 1));
 assertTrue(decorator.containsValue((short) 2));
 }

 @Test
 public void testContainsKey() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);

 assertTrue(decorator.containsKey(1.0));
 assertTrue(decorator.containsKey(2.0));
 assertFalse(decorator.containsKey(3.0));
 }

 @Test
 public void testSize() {
 assertEquals(0, decorator.size());
 decorator.put(1.0, (short) 1);
 assertEquals(1, decorator.size());
 decorator.put(2.0, (short) 2);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 assertTrue(decorator.isEmpty());
 decorator.put(1.0, (short) 1);
 assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap otherMap = new TDoubleShortMap();
 otherMap.put(1.0, (short) 1);
 otherMap.put(2.0, (short) 2);

 decorator.putAll(otherMap);
 assertEquals(2, decorator.size());

 assertEquals(1.0, decorator.keySet().toArray()[0]);
 assertEquals(1.0, decorator.keySet().toArray()[1]);
 }

 @Test
 public void testWrapKey() {
 assertEquals(1.0, decorator.wrapKey(1.0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWrapKeyWithNonDouble() {
 decorator.wrapKey("non-double");
 }

 @Test
 public void testUnwrapKey() {
 Double key = 1.0;
 assertEquals(key, decorator.unwrapKey(decorator.wrapKey(key)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnwrapKeyWithNull() {
 decorator.unwrapKey(null);
 }

 @Test
 public void testWrapValue() {
 assertEquals((short) 1, decorator.wrapValue((short) 1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWrapValueWithNonShort() {
 decorator.wrapValue(1);
 }

 @Test
 public void testUnwrapValue() {
 Short value = (short) 1;
 assertEquals(value, decorator.unwrapValue(decorator.wrapValue(value)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnwrapValueWithNull() {
 decorator.unwrapValue(null);
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 TDoubleShortMapDecorator deserializedDecorator = new TDoubleShortMapDecorator();
 deserializedDecorator.readExternal(in);
 assertSame(decorator, deserializedDecorator);
 }

 @Test
 public void testWriteExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 TDoubleShortMapDecorator deserializedDecorator = new TDoubleShortMapDecorator();
 deserializedDecorator.readExternal(in);
 assertEquals(decorator, deserializedDecorator);
 }

}