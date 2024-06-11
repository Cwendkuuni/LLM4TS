package org.apache.commons.cli;

import gnu.trove.map.TShortByteMap;
import gnu.trove.map.TShortByteMapImpl;
import gnu.trove.iterator.TShortByteIterator;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Assert;
import org.junit.Test;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Test
 public void testTShortByteMapDecoratorEmpty() {
 decorator = new TShortByteMapDecorator();
 Assert.assertEquals(Collections.emptyMap(), decorator);
 }

 @Test
 public void testTShortByteMapDecoratorInitWithMap() {
 _map = new TShortByteMapImpl();
 decorator = new TShortByteMapDecorator(_map);
 Assert.assertEquals(_map, decorator.getMap());
 }

 @Test
 public void testPut() {
 _map = new TShortByteMapImpl();
 decorator = new TShortByteMapDecorator(_map);

 short key = 1;
 byte value = 1;
 decorator.put(key, value);
 _map.put(key, value);

 Assert.assertEquals(Collections.singletonMap(key, value), decorator);
 }

 @Test
 public void testPutSameKeyDifferentValue() {
 _map = new TShortByteMapImpl();
 decorator = new TShortByteMapDecorator(_map);

 short key = 1;
 byte value1 = 1;
 byte value2 = 2;
 decorator.put(key, value1);
 decorator.put(key, value2);

 Assert.assertEquals(Collections.singletonMap(key, value2), decorator);
 }

 @Test
 public void testGet() {
 _map = new TShortByteMapImpl();
 decorator = new TShortByteMapDecorator(_map);

 short key = 1;
 byte value = 1;
 decorator.put(key, value);

 Assert.assertEquals(value, decorator.get(key));
 }

 @Test
 public void testGetNullKey() {
 decorator = new TShortByteMapDecorator();
 Assert.assertNull(decorator.get(null));
 }

 @Test
 public void testGetWrongKeyType() {
 decorator = new TShortByteMapDecorator();
 Assert.assertNull(decorator.get("foo"));
 }

 @Test
 public void testClear() {
 _map = new TShortByteMapImpl();
 decorator = new TShortByteMapDecorator(_map);

 short key1 = 1;
 byte value1 = 1;
 short key2 = 2;
 byte value2 = 2;
 decorator.put(key1, value1);
 decorator.put(key2, value2);

 decorator.clear();
 _map.clear();

 Assert.assertEquals(Collections.emptyMap(), decorator);
 }

 @Test
 public void testRemove() {
 _map = new TShortByteMapImpl();
 decorator = new TShortByteMapDecorator(_map);

 short key = 1;
 byte value = 1;
 decorator.put(key, value);

 Assert.assertEquals(value, decorator.remove(key));
 _map.remove(key);

 Assert.assertEquals(Collections.emptyMap(), decorator);
 }

 @Test
 public void testRemoveNullKey() {
 decorator = new TShortByteMapDecorator();
 Assert.assertNull(decorator.remove(null));
 }

 @Test
 public void testRemoveWrongKeyType() {
 decorator = new TShortByteMapDecorator();
 Assert.assertNull(decorator.remove("foo"));
 }

 @Test
 public void testEntrySet() {
 _map = new TShortByteMapImpl();
 decorator = new TShortByteMapDecorator(_map);

 short key1 = 1;
 byte value1 = 1;
 short key2 = 2;
 byte value2 = 2;
 decorator.put(key1, value1);
 decorator.put(key2, value2);

 Set<Entry<Short, Byte>> entries = decorator.entrySet();

 int size = entries.size();
 Assert.assertEquals(size, 2);

 Entry<Short, Byte> entry1 = entries.iterator().next();
 Assert.assertEquals(key1, entry1.getKey());
 Assert.assertEquals(value1, entry1.getValue());

 Entry<Short, Byte> entry2 = entries.iterator().next();
 Assert.assertEquals(key2, entry2.getKey());
 Assert.assertEquals(value2, entry2.getValue());
 }

 @Test
 public void testContainsValue() {
 _map = new TShortByteMapImpl();
 decorator = new TShortByteMapDecorator(_map);

 short key = 1;
 byte value = 1;
 decorator.put(key, value);

 Assert.assertTrue(decorator.containsValue(value));
 }

 @Test
 public void testContainsKey() {
 _map = new TShortByteMapImpl();
 decorator = new TShortByteMapDecorator(_map);

 short key = 1;
 byte value = 1;
 decorator.put(key, value);

 Assert.assertTrue(decorator.containsKey(key));
 }

 @Test
 public void testSize() {
 _map = new TShortByteMapImpl();
 decorator = new TShortByteMapDecorator(_map);

 short key1 = 1;
 byte value1 = 1;
 short key2 = 2;
 byte value2 = 2;
 decorator.put(key1, value1);
 decorator.put(key2, value2);

 Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 _map = new TShortByteMapImpl();
 decorator = new TShortByteMapDecorator(_map);

 Assert.assertTrue(decorator.isEmpty());

 short key = 1;
 byte value = 1;
 decorator.put(key, value);

 Assert.assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap map = new TShortByteMapImpl();
 map.put(1, (byte) 1);
 map.put(2, (byte) 2);

 TShortByteMapDecorator decorator = new TShortByteMapDecorator();
 decorator.putAll(map);

 Assert.assertEquals(map, decorator.getMap());
 }

 @Test
 public void testSerializeDeserialize() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator = new TShortByteMapDecorator();
 decorator.writeExternal(out);
 out.close();

 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInput in = new ObjectInputStream(bais);
 TShortByteMapDecorator deserializedDecorator = (TShortByteMapDecorator)in.readObject();
 in.close();

 Assert.assertEquals(decorator, deserializedDecorator);
 }
}