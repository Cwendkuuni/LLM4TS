import org.junit.Test;
import org.junit.Assert.*;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.TShortByteMap.Entry;
import gnu.trove.decorator.TShortByteMapDecorator;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Before
 public void setup() {
 _map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(_map);
 }

 @Test
 public void testConstructor() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator();
 Assert.assertNotNull(decorator);
 }

 @Test
 public void testConstructorWithMap() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(new TShortByteMap());
 Assert.assertNotNull(decorator);
 }

 @Test
 public void testGetMap() {
 TShortByteMapMap = decorator.getMap();
 Assert.assertSame(map, decorator._map);
 }

 @Test
 public void testPut() {
 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);
 Byte putValue = decorator.put(key, value);
 assertEquals(value, putValue);
 assertEquals(1, decorator.size());
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
 decorator.put(null, Byte.valueOf((byte)1));
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
 decorator.put(Short.valueOf((short)1), null);
 }

 @Test(expected = ClassCastException.class)
 public void testPutWithNonShortKey() {
 decorator.put(Long.valueOf((long)1), Byte.valueOf((byte)1));
 }

 @Test(expected = ClassCastException.class)
 public void testPutWithNonByteValue() {
 decorator.put(Short.valueOf((short)1), Integer.valueOf(1));
 }

 @Test
 public void testGet() {
 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);
 decorator.put(key, value);
 Byte getValue = decorator.get(key);
 assertEquals(value, getValue);
 }

 @Test(expected = NullPointerException.class)
 public void testGetWithNullKey() {
 decorator.get(null);
 }

 @Test(expected = ClassCastException.class)
 public void testGetWithNonShortKey() {
 decorator.get(Long.valueOf((long)1));
 }

 @Test
 public void testClear() {
 Short key1 = Short.valueOf((short)1);
 Byte value1 = Byte.valueOf((byte)1);
 decorator.put(key1, value1);
 decorator.clear();
 assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);
 decorator.put(key, value);
 Byte removedValue = decorator.remove(key);
 assertEquals(value, removedValue);
 assertEquals(0, decorator.size());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
 decorator.remove(null);
 }

 @Test(expected = ClassCastException.class)
 public void testRemoveWithNonShortKey() {
 decorator.remove(Long.valueOf((long)1));
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
 assertTrue(entrySet instanceof AbstractSet);
 }

 @Test
 public void testContainsValue() {
 Byte value = Byte.valueOf((byte)1);
 decorator.put(Short.valueOf((short)1), value);
 assertTrue(decorator.containsValue(value));
 }

 @Test
 public void testContainsKey() {
 Short key = Short.valueOf((short)1);
 decorator.put(key, Byte.valueOf((byte)1));
 assertTrue(decorator.containsKey(key));
 }

 @Test
 public void testSize() {
 assertEquals(0, decorator.size());
 decorator.put(Short.valueOf((short)1), Byte.valueOf((byte)1));
 assertEquals(1, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 assertTrue(decorator.isEmpty());
 decorator.put(Short.valueOf((short)1), Byte.valueOf((byte)1));
 assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap map = new TShortByteMap();
 map.put(Short.valueOf((short)1), Byte.valueOf((byte)1));
 map.put(Short.valueOf((short)2), Byte.valueOf((byte)2));
 decorator.putAll(map);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(byteStream);
 decorator.writeExternal(out);
 ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
 TShortByteMapDecorator decorator = (TShortByteMapDecorator)input.readObject();
 assertEquals(decorator._map, this._map);
 }

 @Test
 public void testWriteExternal() throws IOException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(byteStream);
 decorator.writeExternal(out);
 ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
 TShortByteMapDecorator deserializedDecorator = (TShortByteMapDecorator)input.readObject();
 assertEquals(deserializedDecorator._map, this._map);
 }
}