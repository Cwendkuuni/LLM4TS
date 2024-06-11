package com.example.junit4test;

import static org.junit.Assert.*;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {

 @Test
 public void testConstructorNoArgs() {
 // ... test constructor with no arguments
 }

 @Test
 public void testConstructorWithMap() {
 // ... test constructor with TDoubleShortMap argument
 }

 @Test
 public void testPut() {
 // ... test put method
 }

 @Test
 public void testGet() {
 // ... test get method
 }

 @Test
 public void testClear() {
 // ... test clear method
 }

 @Test
 public void testEntrySet() {
 TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
 map.put(1.0, (short) 1);
 map.put(2.0, (short) 2);
 map.put(3.0, (short) 3);

 Set<Entry<Double, Short>> entrySet = map.entrySet();
 assertEquals(3, entrySet.size());
 // test for each entry in the set
 for (Entry<Double, Short> entry : entrySet) {
 Double key = entry.getKey();
 Short value = entry.getValue();
 assertEquals(key, map.get(key));
 assertEquals(value, map.get(key));
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddEntrySet() {
 TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
 map.put(1.0, (short) 1);
 Set<Entry<Double, Short>> entrySet = map.entrySet();
 entrySet.add(new Entry<Double, Short>(1.0, (short) 2));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveEntrySet() {
 TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
 map.put(1.0, (short) 1);
 Set<Entry<Double, Short>> entrySet = map.entrySet();
 entrySet.remove(new Entry<Double, Short>(1.0, (short) 1));
 }

 @Test
 public void testContainsValue() {
 TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
 map.put(1.0, (short) 1);
 map.put(2.0, (short) 2);
 map.put(3.0, (short) 3);

 assertTrue(map.containsValue((short) 1));
 assertTrue(map.containsValue((short) 2));
 assertTrue(map.containsValue((short) 3));
 }

 @Test
 public void testContainsKey() {
 TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
 map.put(1.0, (short) 1);
 map.put(2.0, (short) 2);
 map.put(3.0, (short) 3);

 assertTrue(map.containsKey(1.0));
 assertTrue(map.containsKey(2.0));
 assertTrue(map.containsKey(3.0));
 }

 @Test
 public void testSize() {
 TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
 map.put(1.0, (short) 1);
 map.put(2.0, (short) 2);
 map.put(3.0, (short) 3);

 assertEquals(3, map.size());
 }

 @Test
 public void testIsEmpty() {
 TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();

 assertTrue(map.isEmpty());

 map.put(1.0, (short) 1);
 map.clear();

 assertTrue(map.isEmpty());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMapDecorator map1 = new TDoubleShortMapDecorator();
 map1.put(1.0, (short) 1);
 TDoubleShortMapDecorator map2 = new TDoubleShortMapDecorator();
 map2.put(2.0, (short) 2);
 map2.put(3.0, (short) 3);

 map1.putAll(map2);
 assertEquals(3, map1.size());
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
 oos.writeObject(map);
 oos.writeByte(0);
 oos.close();
 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInput in = new ObjectInputStream(bais);
 TDoubleShortMapDecorator mapFromStream = (TDoubleShortMapDecorator) in.readObject();
 in.close();

 assertEquals(map, mapFromStream);
 }

 @Test
 public void testWriteExternal() throws IOException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
 map.put(1.0, (short) 1);
 map.put(2.0, (short) 2);
 map.put(3.0, (short) 3);
 oos.writeByte(0);
 oos.writeObject(map);
 oos.close();
 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInput in = new ObjectInputStream(bais);
 TDoubleShortMapDecorator mapFromStream = (TDoubleShortMapDecorator) in.readObject();
 in.close();

 assertEquals(map, mapFromStream);
 }

 @Test
 public void testNullKey() {
 TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
 try {
 map.put(null, (short) 1);
 fail("Should throw NullPointerException");
 } catch (NullPointerException e) {
 // Expected result
 }
 }

 @Test
 public void testNullValue() {
 TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
 Double key = 1.0;
 try {
 map.put(key, null);
 fail("Should throw NullPointerException");
 } catch (NullPointerException e) {
 // Expected result
 }
 }

 @Test
 public void testPutWithKeyAlreadyExists() {
 TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
 Double key = 1.0;
 Short oldValue = (Short)1;
 map.put(key, oldValue);
 Short newValue = (Short)2;
 Short retrievedValue = map.put(key, newValue);
 assertEquals(newValue, retrievedValue);
 assertEquals(oldValue, map.get(key));
 }

 @Test
 public void testGetWithKeyDoesNotExist() {
 TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
 assertNull(map.get(null));
 assertNull(map.get(new Double(1.1)));
 }

 @Test
 public void testRemoveWithKeyDoesNotExist() {
 TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
 assertNull(map.remove(null));
 assertNull(map.remove(new Double(1.1)));
 }

 @Test
 public void testReadExternalNullObject() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 oos.writeObject(null);
 oos.writeByte(0);
 oos.close();
 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInput in = new ObjectInputStream(bais);
 try {
 TDoubleShortMapDecorator mapFromStream = (TDoubleShortMapDecorator) in.readObject();
 fail("Should throw ClassNotFoundException");
 } catch (ClassNotFoundException e) {
 // Expected result
 } finally {
 in.close();
 }
 }

 @Test
 public void testWriteExternalNullObject() throws IOException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 TDoubleShortMapDecorator map = null;
 try {
 oos.writeByte(0);
 oos.writeObject(map);
 fail("Should throw NullPointerException");
 } catch (NullPointerException e) {
 // Expected result
 } finally {
 oos.close();
 }
 }
}