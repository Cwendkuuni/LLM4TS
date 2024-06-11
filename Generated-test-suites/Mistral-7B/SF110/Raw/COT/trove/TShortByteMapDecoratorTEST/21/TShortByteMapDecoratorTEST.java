package test;

import static org.junit.Assert.*;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;

import org.junit.Test;

public class TShortByteMapDecoratorTest {

 private TShortByteMap map;
 private TShortByteMapDecorator decorator;

 @Test
 public void testConstructor() {
     decorator = new TShortByteMapDecorator();
     assertNotNull(decorator);
     map = decorator.getMap();
     assertNotNull(map);
     assertTrue(map instanceof TShortByteMap);
     assertTrue(decorator.isEmpty());
 }

 @Test
 public void testConstructorWithMap() {
     map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(map);
     assertNotNull(decorator);
     assertEquals(map, decorator.getMap());
 }

 @Test
 public void testPut() {
     decorator = new TShortByteMapDecorator();
     assertNull(decorator.put((short)1, (byte)1));
     assertEquals(1, decorator.size());
     assertNull(decorator.get((short)1));
     decorator.put((short)1, (byte)2);
     assertNull(decorator.put((short)1, (byte)1)); // should override
     assertEquals(1, decorator.size());
     assertEquals((byte)2, decorator.get((short)1));
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
     decorator = new TShortByteMapDecorator();
     decorator.put(null, (byte)1);
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
     decorator = new TShortByteMapDecorator();
     decorator.put((short)1, null);
 }

 @Test
 public void testGet() {
     decorator = new TShortByteMapDecorator();
     decorator.put((short)1, (byte)1);
     assertEquals((byte)1, decorator.get((short)1));
     decorator.put((short)1, (byte)2);
     assertEquals((byte)2, decorator.get((short)1));
     assertNull(decorator.get((short)0));
 }

 @Test(expected = NullPointerException.class)
 public void testGetWithNullKey() {
     decorator = new TShortByteMapDecorator();
     decorator.put((short)1, (byte)1);
     assertNull(decorator.get(null));
 }

 @Test(expected = NoSuchElementException.class)
 public void testGetWithNonExistentKey() {
     decorator = new TShortByteMapDecorator();
     assertNull(decorator.get((short)0));
 }

 @Test
 public void testClear() {
     decorator = new TShortByteMapDecorator();
     decorator.put((short)1, (byte)1);
     decorator.clear();
     assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemove() {
     decorator = new TShortByteMapDecorator();
     decorator.put((short)1, (byte)1);
     assertEquals((byte)1, decorator.remove((short)1));
     assertNull(decorator.get((short)1));
     assertFalse(decorator.containsKey((short)1));
     assertEquals(0, decorator.size());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
     decorator = new TShortByteMapDecorator();
     decorator.put((short)1, (byte)1);
     assertNull(decorator.remove(null));
 }

 @Test(expected = NoSuchElementException.class)
 public void testRemoveWithNonExistentKey() {
     decorator = new TShortByteMapDecorator();
     assertNull(decorator.remove((short)0));
 }

 @Test
 public void testEntrySet() {
     decorator = new TShortByteMapDecorator();
     decorator.put((short)1, (byte)1);
     decorator.put((short)2, (byte)2);
     Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
     assertEquals(2, entrySet.size());
     assertTrue(entrySet.contains(new Entry<Short, Byte>((short)1, (byte)1)));
     assertTrue(entrySet.contains(new Entry<Short, Byte>((short)2, (byte)2)));
     Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();
     assertEquals((short)1, iterator.next().getKey());
     assertEquals((short)2, iterator.next().getKey());
 }

 @Test
 public void testContainsValue() {
     decorator = new TShortByteMapDecorator();
     decorator.put((short)1, (byte)1);
     decorator.put((short)2, (byte)1);
     assertTrue(decorator.containsValue((byte)1));
     assertFalse(decorator.containsValue((byte)0));
 }

 @Test
 public void testContainsKey() {
     decorator = new TShortByteMapDecorator();
     decorator.put((short)1, (byte)1);
     decorator.put((short)2, (byte)1);
     assertTrue(decorator.containsKey((short)1));
     assertTrue(decorator.containsKey((short)2));
     assertFalse(decorator.containsKey((short)0));
 }

 @Test
 public void testSize() {
     decorator = new TShortByteMapDecorator();
     decorator.put((short)1, (byte)1);
     decorator.put((short)2, (byte)1);
     assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
     decorator = new TShortByteMapDecorator();
     assertTrue(decorator.isEmpty());
     decorator.put((short)1, (byte)1);
     assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
     TShortByteMap map1 = new TShortByteMap();
     TShortByteMapDecorator decorator1 = new TShortByteMapDecorator(map1);
     TShortByteMap map2 = new TShortByteMap();
     map2.put((short)1, (byte)1);
     map2.put((short)2, (byte)2);
     decorator1.putAll(map2);
     assertEquals(2, decorator1.size());
     assertTrue(decorator1.containsKey((short)1));
     assertTrue(decorator1.containsKey((short)2));
     assertEquals((byte)1, decorator1.get((short)1));
     assertEquals((byte)2, decorator1.get((short)2));
 }

 @Test
 public void testWrapKey() {
     decorator = new TShortByteMapDecorator();
     assertEquals((short)1, decorator.wrapKey((short)1));
 }

 @Test
 public void testUnwrapKey() {
     decorator = new TShortByteMapDecorator();
     assertEquals((short)1, decorator.unwrapKey((short)1));
 }

 @Test
 public void testWrapValue() {
     decorator = new TShortByteMapDecorator();
     assertEquals((byte)1, decorator.wrapValue((byte)1));
 }

 @Test
 public void testUnwrapValue() {
     decorator = new TShortByteMapDecorator();
     assertEquals((byte)1, decorator.unwrapValue((byte)1));
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
     decorator = new TShortByteMapDecorator();
     decorator.put((short)1, (byte)1);
     ByteArrayOutputStream baos = new ByteArrayOutputStream();
     ObjectOutput out = new java.io.ObjectOutputStream(baos);
     decorator.writeExternal(out);
     ObjectInput in = new java.io.ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
     TShortByteMapDecorator decorator2 = (TShortByteMapDecorator)in.readObject();
     assertEquals(1, decorator2.size());
     assertEquals((byte)1, decorator2.get((short)1));
 }

}