import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;

public class TShortByteMapDecoratorTest {

 private TShortByteMap map;
 private TShortByteMapDecorator decorator;

 @Before
 public void setUp() {
 map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(map);
 }

 @After
 public void tearDown() {
 map.clear();
 decorator = null;
 }

 @Test
 public void testConstructor() {
 // Test default constructor
 decorator = new TShortByteMapDecorator();
 assertNull(decorator.getMap());

 // Test constructor with TShortByteMap
 decorator = new TShortByteMapDecorator(map);
 assertEquals(map, decorator.getMap());
 }

 @Test
 public void testPut() {
 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);

 // Test null key
 assertNull(decorator.put((Short)null, value));
 assertFalse(decorator.containsKey(null));

 // Test null value
 assertNull(decorator.put(key, null));
 assertEquals(0, decorator.size());

 // Test put key-value pair
 Byte oldValue = decorator.put(key, value);
 assertEquals(value, decorator.get(key));
 assertNull(oldValue);
 assertEquals(1, decorator.size());
 }

 @Test
 public void testGet() {
 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);
 decorator.put(key, value);

 Byte getValue = decorator.get(key);
 assertEquals(value, getValue);

 Byte getNullValue = decorator.get(Short.valueOf((short)-1));
 assertNull(getNullValue);
 }

 @Test
 public void testClear() {
 decorator.put(Short.valueOf((short)1), Byte.valueOf((byte)1));
 decorator.clear();
 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey(Short.valueOf((short)1)));
 }

 @Test
 public void testRemove() {
 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);
 decorator.put(key, value);

 Byte removedValue = decorator.remove(key);
 assertEquals(value, removedValue);
 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey(key));
 }

 @Test
 public void testEntrySet() {
 // Test size
 assertEquals(decorator.size(), decorator.entrySet().size());

 // Test isEmpty
 assertTrue(decorator.isEmpty() == decorator.entrySet().isEmpty());

 // Test contains
 Byte value = Byte.valueOf((byte)1);
 decorator.put(Short.valueOf((short)1), value);
 assertTrue(decorator.containsValue(value) == decorator.entrySet().contains(new Entry<Short, Byte>(Short.valueOf((short)1), value)));

 // Test iterator
 TShortByteIterator mapIterator = map.iterator();
 Iterator<Entry<Short, Byte>> decoratorIterator = decorator.entrySet().iterator();

 assertEquals(mapIterator.next().getKey(), decoratorIterator.next().getKey());
 assertEquals(mapIterator.next().getValue(), decoratorIterator.next().getValue());
 }

 @Test
 public void testContainsValue() {
 Byte value = Byte.valueOf((byte)1);
 decorator.put(Short.valueOf((short)1), value);
 assertTrue(decorator.containsValue(value) == map.containsValue(value));

 Byte otherValue = Byte.valueOf((byte)2);
 assertFalse(decorator.containsValue(otherValue) == map.containsValue(otherValue));
 }

 @Test
 public void testContainsKey() {
 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);
 decorator.put(key, value);
 assertTrue(decorator.containsKey(key) == map.containsKey(key.shortValue()));

 Short otherKey = Short.valueOf((short)-1);
 assertFalse(decorator.containsKey(otherKey) == map.containsKey(otherKey.shortValue()));
 }

 @Test
 public void testSize() {
 assertEquals(decorator.size(), map.size());
 }

 @Test
 public void testIsEmpty() {
 assertTrue(decorator.isEmpty() == map.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap anotherMap = new TShortByteMap();
 anotherMap.put(Short.valueOf((short)2), Byte.valueOf((byte)2));
 anotherMap.put(Short.valueOf((short)3), Byte.valueOf((byte)3));
 decorator.putAll(anotherMap);
 assertEquals(anotherMap.size(), decorator.size());

 Byte value = Byte.valueOf((byte)1);
 decorator.put(Short.valueOf((short)1), value);
 assertEquals(anotherMap.size() + 1, decorator.size());
 }

 @Test
 public void testSerializatio() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 out.close();

 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInput in = new ObjectInputStream(bais);
 TShortByteMapDecorator deserializedDecorator = (TShortByteMapDecorator)in.readObject();
 in.close();

 assertEquals(decorator.size(), deserializedDecorator.size());
 }
}