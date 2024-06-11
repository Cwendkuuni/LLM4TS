import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TShortByteMap;
import gnu.trove.map.TShortByteMapImpl;
import gnu.trove.iterator.TShortByteIterator;

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
 // Test default constructor
 TShortByteMapDecorator emptyDecorator = new TShortByteMapDecorator();
 assertSame(emptyDecorator.getMap(), null);

 // Test constructor with TShortByteMap
 TShortByteMap map = new TShortByteMapImpl();
 TShortByteMapDecorator decoratorWithMap = new TShortByteMapDecorator(map);
 assertSame(decoratorWithMap.getMap(), map);
 }

 @Test
 public void testPut() {
 short key = 1;
 byte value = 1;

 // Test put null key
 assertNull(decorator.put((Short)null, value));
 // Test put key already exists
 decorator.put(key, (Byte)1);
 assertSame(decorator.get(key), (Byte)1);
 // Test put new key-value pair
 decorator.put((Short)1, value);
 assertSame(decorator.get(key), value);
 }

 @Test
 public void testGet() {
 short key = 1;
 byte value = 1;

 // Test get null key
 assertNull(decorator.get(null));
 // Test get key not exists
 assertNull(decorator.get((Short)2));
 // Test get key exists
 decorator.put(key, value);
 assertSame(decorator.get(key), value);
 }

 @Test
 public void testClear() {
 // Test clear
 decorator.clear();
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemove() {
 short key = 1;
 byte value = 1;
 decorator.put(key, value);

 // Test remove null key
 assertNull(decorator.remove(null));
 // Test remove key not exists
 assertNull(decorator.remove((Short)2));
 // Test remove key exists
 assertSame(decorator.remove(key), value);
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();

 // Test size
 assertEquals(entrySet.size(), decorator.size());
 // Test contains
 assertTrue(entrySet.contains(new Entry<Short, Byte>(new Short((short)1), new Byte((byte)1))));
 // Test iterator
 TShortByteIterator it = _map.iterator();
 Entry<Short, Byte> entry;
 while (it.hasNext()) {
 entry = new Entry<Short, Byte>() {
 @Override
 public boolean equals(Object o) {
 return o instanceof Entry && equals((Entry<Short, Byte>)o);
 }

 @Override
 public Short getKey() {
 return it.key();
 }

 @Override
 public Byte getValue() {
 return it.value();
 }

 @Override
 public int hashCode() {
 return getKey().hashCode() + getValue().hashCode();
 }

 @Override
 public Byte setValue(Byte value) {
 _map.put(it.key(), value);
 return value;
 }

 @Override
 public boolean equals(Entry<Short, Byte> entry) {
 return getKey().equals(entry.getKey()) && getValue().equals(entry.getValue());
 }
 } = new Entry<Short, Byte>() {
 @Override
 public boolean equals(Object o) {
 return o instanceof Entry && equals((Entry<Short, Byte>)o);
 }

 @Override
 public Short getKey() {
 return it.key();
 }

 @Override
 public Byte getValue() {
 return it.value();
 }

 @Override
 public int hashCode() {
 return getKey().hashCode() + getValue().hashCode();
 }

 @Override
 public Byte setValue(Byte value) {
 _map.put(it.key(), value);
 return value;
 }
 };
 assertSame(entry, entrySet.iterator().next());
 it.advance();
 }
 }

 @Test
 public void testContainsValue() {
 byte value = 1;
 // Test containsValue null value
 assertFalse(decorator.containsValue(null));
 // Test containsValue value exists
 decorator.put((Short)1, value);
 assertTrue(decorator.containsValue(value));
 // Test containsValue value not exists
 assertFalse(decorator.containsValue((Byte)2));
 }

 @Test
 public void testContainsKey() {
 short key = 1;
 // Test containsKey null key
 assertFalse(decorator.containsKey(null));
 // Test containsKey key exists
 decorator.put(key, (Byte)1);
 assertTrue(decorator.containsKey(key));
 // Test containsKey key not exists
 assertFalse(decorator.containsKey((Short)2));
 }

 @Test
 public void testSize() {
 assertEq(0, decorator.size());
 // Test size after put
 decorator.put((Short)1, (Byte)1);
 assertEquals(1, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 assertTrue(decorator.isEmpty());
 // Test isEmpty after put
 decorator.put((Short)1, (Byte)1);
 assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap mapToPut = new TShortByteMapImpl();
 mapToPut.put((Short)1, (Byte)1);
 mapToPut.put((Short)2, (Byte)2);
 decorator.putAll(mapToPut);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testWrapKey() {
 assertEquals((Short)1, decorator.wrapKey((short)1));
 }

 @Test
 public void testUnwrapKey() {
 assertEquals((short)1, decorator.unwrapKey((Short)1));
 }

 @Test
 public void testWrapValue() {
 assertEquals((Byte)1, decorator.wrapValue((byte)1));
 }

 @Test
 public void testUnwrapValue() {
 assertEquals((byte)1, decorator.unwrapValue((Byte)1));
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 DataOutputStream dos = new DataOutputStream(baos);
 decorator.writeExternal(dos);
 TShortByteMapDecorator deserializedDecorator = new TShortByteMapDecorator();
 deserializedDecorator.readExternal(new DataInputStream(new ByteArrayInputStream(baos.toByteArray())));
 assertSame(decorator.getMap(), deserializedDecorator.getMap());
 }

 @Test
 public void testWriteExternal() throws IOException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 DataOutputStream dos = new DataOutputStream(baos);
 decorator.writeExternal(dos);
 TShortByteMapDecorator deserializedDecorator = new TShortByteMapDecorator();
 deserializedDecorator.readExternal(new DataInputStream(new ByteArrayInputStream(baos.toByteArray())));
 assertSame(decorator.getMap(), deserializedDecorator.getMap());
 }
}