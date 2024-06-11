package tests;

import static org.junit.Assert.*;

import gnu.trove.decorator.TShortByteMapDecorator;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class TShortByteMapDecoratorTest {

 private TShortByteMap map;
 private TShortByteMapDecorator decorator;

 @Before
 public void setUp() {
 map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(map);
 }

 @Test
 public void testConstructor() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator();
 assertNotNull(decorator);
 }

 @Test
 public void testConstructorWithMap() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
 assertEquals(map, decorator.getMap());
 }

 @Test
 public void testGetMap() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
 assertSame(map, decorator.getMap());
 }

 @Test
 public void testPut() {
 final byte value = 1;
 final Short key = 1;
 final Byte result = decorator.put(key, value);
 assertEquals(value, result);
 assertTrue(decorator.containsKey(key));
 assertEquals(value, decorator.get(key));
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
 decorator.put(null, (Byte)1);
 fail("NullPointerException expected");
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
 decorator.put((Short)1, null);
 fail("NullPointerException expected");
 }

 @Test
 public void testGetWithNullKey() {
 assertNull(decorator.get(null));
 }

 @Test
 public void testGetWithNonExistentKey() {
 assertNull(decorator.get((Short)10));
 }

 @Test
 public void testClear() {
 assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 final byte value = 1;
 final Short key = 1;
 decorator.put(key, value);
 final Byte result = decorator.remove(key);
 assertEquals(value, result);
 assertFalse(decorator.containsKey(key));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
 decorator.remove(null);
 fail("NullPointerException expected");
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
 assertFalse(entrySet.isEmpty());
 }

 @Test
 public void testContainsValue() {
 final byte value = 1;
 decorator.put((Short)1, value);
 assertTrue(decorator.containsValue(value));
 }

 @Test
 public void testContainsKey() {
 final byte value = 1;
 decorator.put((Short)1, value);
 assertTrue(decorator.containsKey((Short)1));
 }

 @Test
 public void testSize() {
 assertEquals(0, decorator.size());
 decorator.put((Short)1, (Byte)1);
 assertEquals(1, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 assertTrue(decorator.isEmpty());
 decorator.put((Short)1, (Byte)1);
 assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap map2 = new TShortByteMap();
 map2.put((short)1, (byte)2);
 map2.put((short)2, (byte)3);
decorator.putAll(map2);
 assertEquals(2, decorator.size());
assertTrue(decorator.containsKey((Short)1));
assertTrue(decorator.containsKey((Short)2));
assertEquals((Byte)1, decorator.get((Short)1));
assertEquals((Byte)3, decorator.get((Short)2));
 }

 @Test
 public void testWrapKey() {
 Short key = decorator.wrapKey((short)1);
 assertEquals((short)1, key.shortValue());
 }

 @Test
 public void testUnwrapKey() {
 Object key = decorator.unwrapKey((Short)1);
 assertEquals((short)1, key);
 }

 @Test
 public void testWrapValue() {
 Byte value = decorator.wrapValue((byte)1);
 assertEquals((byte)1, value.byteValue());
 }

 @Test
 public void testUnwrapValue() {
 Object value = decorator.unwrapValue((Byte)1);
 assertEquals((byte)1, value);
 }

 @Test
 public void testSerializable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectOutputStream out = new ObjectOutputStream(baos);
 ObjectInputStream in = new ObjectInputStream(bais);
 decorator.writeExternal(out);
 TShortByteMapDecorator decorator2 = (TShortByteMapDecorator)in.readObject();
 assertEquals(map.size(), decorator2.size());
 for (TShortByteIterator it = map.iterator(); it.hasNext();) {
 Entry<Short, Byte> entry = (Entry<Short, Byte>)map.nextEntry();
 assertEquals(entry.getKey(), decorator2.get(decorator2.unwrapKey(entry.getKey())));
 assertEquals(entry.getValue(), decorator2.get(decorator2.unwrapKey(entry.getKey())));
 }
 }
}