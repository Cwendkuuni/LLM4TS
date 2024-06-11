package com.example.junit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.map.TShortByteMapImpl;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.view.TShortByteMapView;
import gnu.trove.map.view.TShortByteMapViewAdapter;
import gnu.trove.decorator.TShortByteMapDecorator;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TShortByteMapImpl();
 decorator = new TShortByteMapDecorator(_map);
 }

 @Test
 public void testConstructor() {
 assertNull(decorator.getMap());
 }

 @Test
 public void testConstructorWithMap() {
 TShortByteMap map = new TShortByteMapImpl();
 map.put((short)1, (byte)1);
 decorator = new TShortByteMapDecorator(map);
 assertSame(map, decorator.getMap());
 }

 @Test
 public void testPutNullKey() {
 short key = Short.MIN_VALUE;
 decorator.put(key, (byte)1);
 Assert.assertEquals(decorator.get(key), (byte)1);
 }

 @Test
 public void testPutNullValue() {
 decorator.put((short)1, (Byte)null);
 Assert.assertEquals(decorator.get((short)1), null);
 }

 @Test
 public void testPutExistingKey() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)1, (byte)2);
 Assert.assertEquals(decorator.get((short)1), (byte)2);
 }

 @Test
 public void testPutNewKey() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 Assert.assertEquals(decorator.get((short)1), (byte)1);
 Assert.assertEquals(decorator.get((short)2), (byte)2);
 }

 @Test
 public void testGetNullKey() {
 Assert.assertNull(decorator.get(Short.MIN_VALUE));
 }

 @Test
 public void testGetExistingKey() {
 decorator.put((short)1, (byte)1);
 Assert.assertEquals(decorator.get((short)1), (byte)1);
 }

 @Test
 public void testGetNonExistingKey() {
 Assert.assertNull(decorator.get(Short.MAX_VALUE));
 }

 @Test
 public void testClear() {
 decorator.put((short)1, (byte)1);
 decorator.clear();
 Assert.assertEquals(decorator.size(), 0);
 }

 @Test
 public void testRemoveNullKey() {
 decorator.put((short)1, (byte)1);
 Assert.assertNull(decorator.remove(Short.MIN_VALUE));
 }

 @Test
 public void testRemoveExistingKey() {
 decorator.put((short)1, (byte)1);
 Assert.assertEquals(decorator.remove((short)1), (byte)1);
 Assert.assertEquals(decorator.size(), 0);
 }

 @Test
 public void testRemoveNonExistingKey() {
 Assert.assertNull(decorator.remove(Short.MAX_VALUE));
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Short, Byte>> entries = decorator.entrySet();
 Assert.assertEquals(entries.size(), decorator.size());
 }

 @Test
 public void testContainsValue() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)1);
 Assert.assertTrue(decorator.containsValue((byte)1));
 }

 @Test
 public void testContainsKey() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)1);
 Assert.assertTrue(decorator.containsKey((short)1));
 }

 @Test
 public void testSize() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)1);
 Assert.assertEquals(decorator.size(), 2);
 }

 @Test
 public void testIsEmpty() {
 Assert.assertTrue(decorator.isEmpty());
 decorator.put((short)1, (byte)1);
 Assert.assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap map = new TShortByteMapImpl();
 map.put((short)1, (byte)1);
 map.put((short)2, (byte)2);
 decorator.putAll(map);
 Assert.assertEquals(decorator.size(), 2);
 }

 @Test
 public void testSerializability() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 oos.writeObject(decorator);
 byte[] bytes = baos.toByteArray();
 ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
 ObjectInputStream ois = new ObjectInputStream(bais);
 TShortByteMapDecorator deserialized = (TShortByteMapDecorator) ois.readObject();
 Assert.assertEquals(deserialized.get((short)1), (byte)1);
 }

 @Test
 public void testCloneability() {
 TShortByteMapDecorator original = new TShortByteMapDecorator(_map);
 TShortByteMapDecorator cloned = (TShortByteMapDecorator) original.clone();
 original.put((short)1, (byte)1);
 Assert.assertNotSame(original, cloned);
 Assert.assertTrue(original.containsKey((short)1));
 Assert.assertFalse(cloned.containsKey((short)1));
 }

 @Test
 public void testExternalizability() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 byte[] bytes = baos.toByteArray();
 ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
 ObjectInput in = new ObjectInputStream(bais);
 TShortByteMapDecorator deserialized = new TShortByteMapDecorator();
 deserialized.readExternal(in);
 Assert.assertEquals(deserialized.get((short)1), (byte)1);
 }
}