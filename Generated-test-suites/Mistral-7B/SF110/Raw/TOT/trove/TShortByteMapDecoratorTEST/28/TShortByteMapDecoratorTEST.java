package gnu.trove.decorator.test;

import static org.junit.Assert.*;

import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;

public class TShortByteMapDecoratorTest {

 private TShortByteMap map;
 private TShortByteMapDecorator decorator;

 @Test
 public void testEmptyDecorator() {
 decorator = new TShortByteMapDecorator();
 assertTrue(decorator.isEmpty());
 assertNull(decorator.getMap());
 }

 @Test
 public void testDecoratorWithEmptyMap() {
 map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(map);
 assertEquals(map, decorator.getMap());
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPut() {
 map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(map);

 short key = 1;
 byte value = 2;
 decorator.put(key, value);

 assertEquals(1, decorator.size());
 assertTrue(decorator.containsKey(key));
 assertEquals(value, decorator.get(key));

 assertEquals(map.size(), 1);
 assertTrue(map.containsKey(key.shortValue()));
 assertEquals(value, map.get(key.shortValue()));
 }

 @Test
 public void testGetNullKey() {
 map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(map);

 assertNull(decorator.get(null));
 assertNull(map.get(Short.MIN_VALUE));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutNullValue() {
 map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(map);

 short key = 1;
 decorator.put(key, null);
 }

 @Test
 public void testPutSameKeyDifferentValue() {
 map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(map);

 short key = 1;
 byte value1 = 2;
 byte value2 = 3;
 decorator.put(key, value1);
 decorator.put(key, value2);

 assertEquals(1, decorator.size());
 assertEquals(value2, decorator.get(key));

 assertEquals(1, map.size());
 assertEquals(value2, map.get(key.shortValue()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWrongTypeKey() {
 map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(map);

 Object wrongKey = new Object();
 byte value = 2;
 decorator.put(wrongKey, value);
 }

 @Test
 public void testClear() {
 map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(map);

 short key1 = 1;
 byte value1 = 2;
 decorator.put(key1, value1);
 short key2 = 2;
 byte value2 = 3;
 decorator.put(key2, value2);

 assertEquals(2, decorator.size());
 decorator.clear();

 assertTrue(decorator.isEmpty());
 assertNull(decorator.getMap());
 assertEquals(0, map.size());
 }

 @Test
 public void testRemove() {
 map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(map);

 short key = 1;
 byte value = 2;
 decorator.put(key, value);

 assertEquals(1, decorator.size());
 assertTrue(decorator.containsKey(key));
 assertEquals(value, decorator.get(key));

 assertTrue(map.containsKey(key.shortValue()));
 assertEquals(value, map.get(key.shortValue()));

 decorator.remove(key);

 assertEquals(0, decorator.size());
 assertNull(decorator.getMap());
 assertFalse(decorator.containsKey(key));
 assertFalse(map.containsKey(key.shortValue()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveWrongTypeKey() {
 map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(map);

 Object wrongKey = new Object();decorator.remove(wrongKey);
 }

 @Test
 public void testEntrySet() {
 map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(map);

 short key1 = 1;
 byte value1 = 2;
 short key2 = 2;
 byte value2 = 3;
 decorator.put(key1, value1);
 decorator.put(key2, value2);

 Collection<Entry<Short, Byte>> entrySet = decorator.entrySet();

 assertEquals(2, entrySet.size());

 for (Entry<Short, Byte> entry : entrySet) {
 assertEquals(entry.getKey(), entry.getValue() ? entry.getValue().shortValue() : null);
 }

 for (Entry<Short, Byte> entry : map.entrySet()) {
 assertEquals(entry.getKey(), entry.getValue());
 }
 }

 @Test
 public void testContainsValue() {
 map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(map);

 byte value = 2;
 decorator.put((short)1, value);
 decorator.put((short)2, (byte)3);

 assertTrue(decorator.containsValue(value));
 assertFalse(decorator.containsValue(Byte.MIN_VALUE));
 assertFalse(decorator.containsValue(new Byte((byte)127)));

 assertTrue(map.containsValue(value));
 assertFalse(map.containsValue(Byte.MIN_VALUE));
 assertFalse(map.containsValue(new Byte((byte)127)));
 }

 @Test
 public void testContainsKey() {
 map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(map);

 short key = 1;
 decorator.put(key, (byte)1);

 assertTrue(decorator.containsKey(key));
 assertFalse(decorator.containsKey(Short.MIN_VALUE));

 assertTrue(map.containsKey(key.shortValue()));
 assertFalse(map.containsKey(Short.MIN_VALUE));
 }

 @Test
 public void testSize() {
 map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(map);

 assertEquals(0, decorator.size());
 assertEquals(0, map.size());

 short key = 1;
 byte value = 2;
 decorator.put(key, value);
 decorator.put((short)-1, (byte)3);

 assertEquals(2, decorator.size());
 assertEquals(2, map.size());
 }

 @Test
 public void testPutAll() {
 map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(map);

 TShortByteMap anotherMap = new TShortByteMap();
 anotherMap.put((short)1, (byte)1);
 anotherMap.put((short)2, (byte)2);
 anotherMap.put((short)3, (byte)3);

 decorator.putAll(anotherMap);

 assertEquals(3, decorator.size());
 assertTrue(decorator.containsAll(anotherMap));
 assertEquals(3, map.size());
 assertTrue(map.containsAll(anotherMap));
 }

 @Test
 public void testWriteExternal() throws IOException, ClassNotFoundException {
 map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(map);

 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);

 ByteArrayOutputStream output = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(output);
 decorator.writeExternal(out);

 TShortByteMapDecorator newDecorator = new TShortByteMapDecorator();
 newDecorator.readExternal(new ObjectInputStream(new ByteArrayInputStream(output.toByteArray())));

 assertEquals(decorator.size(), newDecorator.size());
 }
}