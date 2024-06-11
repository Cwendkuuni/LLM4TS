import org.junit.Test;
import org.junit.Assert.*;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.iterator.TShortByteMapIterator;
import java.util.*;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(_map);
 }

 // Test constructor
 @Test
 public void testTShortByteMapDecorator_Constructor() {
 decorator = new TShortByteMapDecorator();
 assertEquals("Size should be zero.", 0, decorator.size());
 }

 // Test constructor with map as argument
 @Test
 public void testTShortByteMapDecorator_ConstructorWithMap() {
 decorator = new TShortByteMapDecorator(_map);
 assertEquals("Size should be the same as the map.", _map.size(), decorator.size());
 }

 // Test getMap method
 @Test
 public void testGetMap() {
 TShortByteMap actual = decorator.getMap();
 assertEquals("The map should be the same instance.", _map, actual);
 }

 // Test put method
 @Test
 public void testPut() {
 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);

 Byte expected = decorator.put(key, value);
 Byte actual = decorator.get(key);

 assertEquals("The value should be the same as the one passed.", value, actual);
 assertEquals("The original key should not have been changed.", key, key);
 assertEquals("The original value should not have been changed.", value, decorator.get(key));
 assertEquals("The put method should return the old value if the key was already present.", (Byte)Byte.valueOf((byte)0), expected);
 }

 // Test put with null key
 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
 decorator.put(null, (Byte)Byte.valueOf((byte)1));
 }

 // Test put with null value
 @Test
 public void testPutWithNullValue() {
 decorator.put((Short)Short.valueOf((short)1), null);
 assertEquals("The size should be unchanged.", 0, decorator.size());
 }

 // Test get method
 @Test
 public void testGet() {
 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);
 decorator.put(key, value);

 Byte actual = decorator.get(key);
 assertEquals("The value should be the same as the one stored.", value, actual);
 }

 // Test get with null key
 @Test
 public void testGetWithNullKey() {
 Byte actual = decorator.get(null);
 assertNull("The method should return null with a null key.");
 }

 // Test get with non-existing key
 @Test
 public void testGetWithNonExistingKey() {
 Byte actual = decorator.get(Short.valueOf((short)1));
 assertNull("The method should return null with a non-existing key.");
 }

 // Test clear method
 @Test
 public void testClear() {
 Byte value = Byte.valueOf((byte)1);
 decorator.put((Short)Short.valueOf((short)1), value);
 decorator.clear();
 assertEquals("The size should be zero after clear.", 0, decorator.size());
 }

 // Test remove method
 @Test
 public void testRemove() {
 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);
 decorator.put(key, value);

 Byte removed = decorator.remove(key);
 Byte actual = decorator.get(key);

 assertEquals("The removed value should be the same as the one stored.", value, removed);
 assertNull("The value should be removed.", actual);
 }

 // Test remove with null key
 @Test
 public void testRemoveWithNullKey() {
 Byte removed = decorator.remove(null);
 assertNull("The method should return null with a null key.");
 }

 // Test remove with non-existing key
 @Test
 public void testRemoveWithNonExistingKey() {
 Byte removed = decorator.remove(Short.valueOf((short)1));
 assertNull("The method should return null with a non-existing key.");
 }

 // Test entrySet method
 @Test
 public void testEntrySet() {
 Set<Entry<Short, Byte>> set = decorator.entrySet();
 Iterator<Entry<Short, Byte>> it = set.iterator();
 assertTrue("The iterator should not be empty.", it.hasNext());
 }

 // Test containsValue method
 @Test
 public void testContainsValue() {
 Byte value = Byte.valueOf((byte)1);
 decorator.put((Short)Short.valueOf((short)1), value);
 assertTrue("The decorator should contain the value.", decorator.containsValue(value));
 }

 // Test containsKey method
 @Test
 public void testContainsKey() {
 Short key = Short.valueOf((short)1);
 decorator.put(key, (Byte)Byte.valueOf((byte)1));
 assertTrue("The decorator should contain the key.", decorator.containsKey(key));
 }

 // Test containsKey with null key
 @Test
 public void testContainsKeyWithNullKey() {
 TShortByteMapDecorator decoratorWithNullKey = new TShortByteMapDecorator();
 assertFalse("The decorator should not contain a null key.", decoratorWithNullKey.containsKey(null));
 }

 // Test size method
 @Test
 public void testSize() {
 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);
 decorator.put(key, value);
 assertEquals("The size should be one.", 1, decorator.size());
 }

 // Test isEmpty method
 @Test
 public void testIsEmpty() {
 assertTrue("An empty decorator should be empty.", decorator.isEmpty());
 }

 // Test putAll method
 @Test
 public void testPutAll() {
 TShortByteMap map = new TShortByteMap();
 map.put((Short)Short.valueOf((short)1), (Byte)Byte.valueOf((byte)1));
 map.put((Short)Short.valueOf((short)2), (Byte)Byte.valueOf((byte)2));
 decorator.putAll(map);

 Byte value1 = Byte.valueOf((byte)1);
 Byte value2 = Byte.valueOf((byte)2);

 Byte actual1 = decorator.get(Short.valueOf((short)1));
 Byte actual2 = decorator.get(Short.valueOf((short)2));

 assertEquals("The decorator should contain the entries from the map.", value1, actual1);
 assertEquals("The decorator should contain the entries from the map.", value2, actual2);
 }

 // Test putAll with null map
 @Test(expected = NullPointerException.class)
 public void testPutAllWithNullMap() {
 decorator.putAll(null);
 }

 // Test externalization
 @Test
 public void testExternalization() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 TShortByteMapDecorator deserialized = (TShortByteMapDecorator)in.readObject();

 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);
 deserialized.put(key, value);
 Byte expected = value;
 Byte actual = deserialized.get(key);

 assertEquals("The deserialized decorator should contain the same entry as the original one.", expected, actual);
 }

 // Test cloning
 @Test
 public void testCloning() throws CloneNotSupportedException {
 TShortByteMapDecorator original = new TShortByteMapDecorator(_map);
 TShortByteMapDecorator clone = (TShortByteMapDecorator)original.clone();

 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);
 original.put(key, value);

 Byte expected = value;
 Byte actual = clone.get(key);

 assertEquals("The clone should contain the same entry as the original one.", expected, actual);
 }
}