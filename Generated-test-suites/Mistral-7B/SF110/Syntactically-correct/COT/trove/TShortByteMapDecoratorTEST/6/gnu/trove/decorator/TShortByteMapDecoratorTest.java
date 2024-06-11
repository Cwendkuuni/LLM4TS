import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.view.TShortByteMapView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
 assertNull(decorator.getMap());
 }

 @Test
 public void testConstructorWithMap() {
 TShortByteMap map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(map);
 assertEquals(map, decorator.getMap());
 }

 @Test
 public void testPut() {
 Short key = Short.valueOf("1");
 Byte value = Byte.valueOf((byte)1);
 Byte returnedValue = decorator.put(key, value);
 assertNotNull(returnedValue);
 assertEquals(value, returnedValue);
 assertTrue(decorator.containsKey(key));
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
 decorator.put(null, (Byte)1);
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
 decorator.put((Short)1, null);
 }

 @Test
 public void testGet() {
 Short key = Short.valueOf("1");
 Byte value = Byte.valueOf((byte)1);
 decorator.put(key, value);
 Byte retrievedValue = decorator.get(key);
 assertNotNull(retrievedValue);
 assertEquals(value, retrievedValue);
 }

 @Test(expected = NullPointerException.class)
 public void testGetWithNullKey() {
 decorator.get(null);
 }

 @Test(expected = NullPointerException.class)
 public void testGetWithNonShortKey() {
 decorator.get("nonShortKey");
 }

 @Test
 public void testClear() {
 Byte value = Byte.valueOf((byte)1);
 Short key1 = Short.valueOf("1");
 decorator.put(key1, value);
 Short key2 = Short.valueOf("2");
 decorator.put(key2, value);
 decorator.clear();
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemove() {
 Byte value = Byte.valueOf((byte)1);
 Short key1 = Short.valueOf("1");
 decorator.put(key1, value);
 Byte removedValue = decorator.remove(key1);
 assertNotNull(removedValue);
 assertEquals(value, removedValue);
 assertFalse(decorator.containsKey(key1));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
 decorator.remove(null);
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNonShortKey() {
 decorator.remove("nonShortKey");
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
 assertFalse(entrySet.isEmpty());
 }

 @Test
 public void testContainsValue() {
 Byte value = Byte.valueOf((byte)1);
 decorator.put((Short)1, value);
 assertTrue(decorator.containsValue(value));
 }

 @Test
 public void testContainsKey() {
 Short key = Short.valueOf("1");
 decorator.put(key, (Byte)1);
 assertTrue(decorator.containsKey(key));
 }

 @Test
 public void testSize() {
 Short key1 = Short.valueOf("1");
 decorator.put(key1, (Byte)1);
 Short key2 = Short.valueOf("2");
 decorator.put(key2, (Byte)1);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap map = new TShortByteMap();
 map.put((short)1, (byte)1);
 map.put((short)2, (byte)2);
 decorator.putAll(map);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
 decorator.writeExternal(byteInput);
 TShortByteMapDecorator deserializedDecorator = new TShortByteMapDecorator();
 deserializedDecorator.readExternal(byteInput);
 TShortByteMap deserializedMap = deserializedDecorator.getMap();
 assertEquals(decorator.getMap(), deserializedMap);
 }

 @Test(expected = ClassCastException.class)
 public void testGetWithInvalidValueType() {
 decorator.put((Short)1, new String("test"));
 Byte retrievedValue = decorator.get((Short)1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithInvalidKeyType() {
 decorator.put(new Object(){}, (Byte)1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllWithNullCollection() {
 decorator.addAll(null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllWithInvalidCollectionType() {
 TShortByteMapView view = _map.view();
 decorator.addAll(view.entrySet());
 }
}