package org.apache.commons.cli; //Assuming you want to use JUnit 4 for testing, please make sure you have the JUnit 4 library in your classpath.

import static org.junit.Assert.*;
import gnu.trove.map.TShortByteMap;
import gnu.trove.map.TShortByteMapImpl;
import gnu.trove.iterator.TShortByteIterator;
import java.util.Arrays;
import org.junit.Test;

public class TShortByteMapDecoratorTest {

 private TShortByteMap getTestMap() {
 TShortByteMap map = new TShortByteMapImpl();
 map.put((short)1, (byte)1);
 map.put((short)2, (byte)2);
 map.put((short)3, (byte)3);
 return map;
 }

 @Test
 public void testTShortByteMapDecorator() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(this.getTestMap());

 assertEquals(this.getTestMap().size(), decorator.size());
 assertTrue(decorator.isEmpty() == this.getTestMap().isEmpty());

 Short key1 = Short.valueOf((short)1);
 Byte value1 = Byte.valueOf((byte)1);
 assertEquals(value1, decorator.get(key1));
 assertEquals(key1, decorator.get(value1));

 assertNull(decorator.get(null));
 assertNull(decorator.get(Short.valueOf((short)4)));

 decorator.put(Short.valueOf((short)4), Byte.valueOf((byte)4));
 assertEquals(decorator.size(), this.getTestMap().size() + 1);
 assertEquals(Byte.valueOf((byte)4), decorator.get(Short.valueOf((short)4)));
 assertEquals(Short.valueOf((short)4), decorator.get(Byte.valueOf((byte)4)));

 decorator.clear();
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutNullKeyOrValue() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator();

 assertNull(decorator.put(null, null));
 assertNull(decorator.put(Short.valueOf((short)1), null));
 assertNull(decorator.put(null, Byte.valueOf((byte)1)));
 }

 @Test
 public void testPutEntryWithWrappedKeyAndValue() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator();

 assertNull(decorator.put(Short.valueOf((short)1), Byte.valueOf((byte)1)));

 Short key1 = Short.valueOf((short)1);
 Byte value1 = Byte.valueOf((byte)1);
 decorator.put(key1, value1);
 assertEquals(key1, decorator.get(value1));
 assertEquals(value1, decorator.get(key1));
 }

 @Test
 public void testPutEmptyMap() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(new TShortByteMapImpl());
 assertEquals(decorator.size(), 0);
 }

 @Test
 public void testPutAll() {
 TShortByteMap map = new TShortByteMapImpl();
 map.put((short)1, (byte)1);
 map.put((short)2, (byte)2);
 map.put((short)3, (byte)3);

 TShortByteMapDecorator decorator = new TShortByteMapDecorator();
 decorator.putAll(map);

 assertEquals(decorator.size(), map.size());

 Short key1 = Short.valueOf((short)1);
 Byte value1 = Byte.valueOf((byte)1);
 assertEquals(value1, decorator.get(key1));
 assertEquals(key1, decorator.get(value1));

 assertNull(decorator.get(null));
 assertNull(decorator.get(Short.valueOf((short)4)));
 }

 @Test
 public void testRemove() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(this.getTestMap());

 assertEquals(this.getTestMap().size(), decorator.size());

 Short key1 = Short.valueOf((short)1);
 Byte value1 = Byte.valueOf((byte)1);
 Byte removedValue = decorator.remove(key1);

 assertEquals(this.getTestMap().size() - 1, decorator.size());
 assertNull(decorator.get(key1));
 assertEquals(value1, removedValue);

 Short key2 = Short.valueOf((short)2);
 Byte value2 = Byte.valueOf((byte)2);
 removedValue = decorator.remove(key2);

 assertEquals(this.getTestMap().size() - 1, decorator.size());
 assertNull(decorator.get(key2));
 assertEquals(value2, removedValue);
 }

 @Test
 public void testRemoveNullKey() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(this.getTestMap());

 assertNull(decorator.remove(null));
 }

 @Test
 public void testRemoveNonExistingKey() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(this.getTestMap());

 assertNull(decorator.remove(Short.valueOf((short)4)));
 }

 @Test
 public void testEntrySet() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(this.getTestMap());

 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();

 assertEquals(this.getTestMap().size(), entrySet.size());

 Byte[] values = new Byte[3];
 Arrays.setAll(values, i -> Byte.valueOf((byte)i));
 Short[] keys = new Short[3];
 Arrays.setAll(keys, i -> Short.valueOf((short)i));

 Iterator<Entry<Short, Byte>> entryIterator = entrySet.iterator();
 for (int i = 0; i < 3; i++) {
 Entry<Short, Byte> entry = entryIterator.next();
 assertEquals(keys[i], entry.getKey());
 assertEquals(values[i], entry.getValue());
 }
 }

 @Test
 public void testContainsValue() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(this.getTestMap());

 for (Byte value : Arrays.asList(Byte.valueOf((byte)1), Byte.valueOf((byte)2), Byte.valueOf((byte)3))) {
 assertTrue(decorator.containsValue(value));
 }

 assertFalse(decorator.containsValue(Byte.valueOf((byte)4)));
 }

 @Test
 public void testContainsKey() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(this.getTestMap());

 for (Short key : Arrays.asList(Short.valueOf((short)1), Short.valueOf((short)2), Short.valueOf((short)3))) {
 assertTrue(decorator.containsKey(key));
 }

 assertFalse(decorator.containsKey(Short.valueOf((short)4)));
 }
}