package test;

import static org.junit.Assert.*;

import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.view.TShortByteMapView;
import org.junit.Before;
import org.junit.Test;

public class TShortByteMapDecoratorTest {

 private TShortByteMap baseMap;
 private TShortByteMapDecorator decorator;

 @Before
 public void setUp() {
 baseMap = new TShortByteMap();
 decorator = new TShortByteMapDecorator(baseMap);
 }

 @Test
 public void testConstructor() {
 assertNotNull(decorator);
 assertEquals(decorator.getClass(), TShortByteMapDecorator.class);
 assertSame(decorator.getMap(), baseMap);
 }

 @Test
 public void testPut() {
 short key = 1;
 byte value = 1;

 // Null key
 assertNull(decorator.put((Short)null, value));
 assertEquals(0, decorator.size());
 // Wrapped key
 Byte wrappedKey = 1;
 Byte wrappedValue = decorator.put(key, value);
 assertEquals(Byte.class, wrappedValue.getClass());
 assertEquals(value, decorator.get(wrappedKey));
 // Existing key
 key = 2;
 value = 2;
 Byte existingValue = decorator.put(key, value);
 assertNotNull(existingValue);
 assertEquals(value, decorator.get(key));
 // Same key
 assertEquals(existingValue, decorator.put(key, value));
 // No entry key
 key = Short.MIN_VALUE;
 assertNull(decorator.put(key, value));
 assertEquals(existingValue, decorator.get(key));
 }

 @Test
 public void testGet() {
 short key = 1;
 byte value = 1;
 decorator.put(key, value);
 Byte retrievedValue = decorator.get(key);
 assertEquals(value, retrievedValue);
 // Null key
 assertNull(decorator.get(null));
 // Non-existing key
 assertNull(decorator.get((Short)Short.MIN_VALUE));
 }

 @Test
 public void testClear() {
 decorator.put(Short.MIN_VALUE, (byte)0);
 decorator.put((Short)1, (byte)1);
 decorator.clear();
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemove() {
 short key = 1;
 byte value = 1;
 decorator.put(key, value);
 Byte removedValue = decorator.remove(key);
 assertEquals(value, removedValue);
 assertNull(decorator.get(key));
 // Non-existing key
 removedValue = decorator.remove((Short)2);
 assertNull(removedValue);
 }

 @Test
 public void testEntrySet() {
 Iterator<Entry<Short, Byte>> iterator = decorator.entrySet().iterator();
 assertEquals(0, iterator.nextCount());
 short key = 1;
 byte value = 1;
 decorator.put(key, value);
 iterator = decorator.entrySet().iterator();
 Entry<Short, Byte> entry = iterator.next();
 assertEquals(key, entry.getKey());
 assertEquals(value, entry.getValue());
 // Test remove
 decorator.remove(key);
 iterator = decorator.entrySet().iterator();
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testContainsValue() {
 byte value = 1;
 assertTrue(decorator.containsValue(value));
 decorator.put((Short)1, value);
 assertFalse(decorator.containsValue((byte)2));
 }

 @Test
 public void testContainsKey() {
 short key = 1;
 assertTrue(decorator.containsKey(key));
 assertFalse(decorator.containsKey((Short)Short.MIN_VALUE));
 decorator.put(key, (byte)1);
 assertFalse(decorator.containsKey((Short)2));
 }

 @Test
 public void testSize() {
 assertEquals(0, decorator.size());
 decorator.put((Short)1, (byte)1);
 assertEquals(1, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 assertTrue(decorator.isEmpty());
 decorator.put((Short)1, (byte)1);
 assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap newMap = new TShortByteMap();
 newMap.put((short)1, (byte)1);
 newMap.put((short)2, (byte)2);
 decorator.putAll(newMap);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testWrapKey() {
 assertEquals(Short.MIN_VALUE, decorator.wrapKey((short)Short.MIN_VALUE));
 }

 @Test
 public void testUnwrapKey() {
 assertEquals((short)Short.MIN_VALUE, decorator.unwrapKey(Short.MIN_VALUE));
 }

 @Test
 public void testWrapValue() {
 assertEquals((byte)0, decorator.wrapValue((byte)0));
 }

 @Test
 public void testUnwrapValue() {
 assertEquals((byte)0, decorator.unwrapValue((byte)0));
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 TShortByteMapDecorator deserializedDecorator = (TShortByteMapDecorator)in.readObject();
 assertEquals(decorator.getMap(), deserializedDecorator.getMap());
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
 TShortByteMapDecorator clonedDecorator = (TShortByteMapDecorator)decorator.clone();
 assertEquals(decorator.getMap(), clonedDecorator.getMap());
 }
}