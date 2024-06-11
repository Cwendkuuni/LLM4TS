import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.view.TShortByteMapView;

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
 assertNull(decorator.getMap());
 }

 @Test
 public void testConstructorWithMap() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
 assertEquals(map, decorator.getMap());
 }

 @Test
 public void testPut() {
 byte value = 0;
 Short key = 0;
 assertEquals(null, decorator.get(key));
 decorator.put(key, value);
 assertEquals(value, decorator.get(key));
 Short key1 = 1;
 assertEquals(null, decorator.get(key1));
 decorator.put(key1, value);
 assertEquals(value, decorator.get(key1));
 Short key2 = 2;
 decorator.put(key2, (byte)1);
 assertEquals((byte)1, decorator.get(key2));
 }

 @Test
 public void testGet() {
 byte value = 0;
 Short key = 0;
 assertEquals(null, decorator.get(key));
 decorator.put(key, value);
 assertEquals(value, decorator.get(key));
 Short key1 = 1;
 assertEquals(null, decorator.get(key1));
 decorator.put(key1, value);
 assertEquals(value, decorator.get(key1));
 Short key2 = 2;
 decorator.put(key2, (byte)1);
 assertEquals((byte)1, decorator.get(key2));
 }

 @Test
 public void testClear() {
 decorator.clear();
 assertFalse(decorator.isEmpty());
 decorator.clear();
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemove() {
 Short key = 0;
 byte value = 0;
 decorator.put(key, value);
 byte removedValue = decorator.remove(key);
 assertEquals(value, removedValue);
 assertNull(decorator.get(key));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveWithNullKey() {
 decorator.remove(null);
 }

 @Test
 public void testContainsValue() {
 byte value = 0;
 decorator.put((short)0, value);
 assertTrue(decorator.containsValue(value));
 decorator.put((short)1, value);
 assertTrue(decorator.containsValue(value));
 decorator.put((short)2, (byte)1);
 assertFalse(decorator.containsValue(value));
 }

 @Test
 public void testContainsKey() {
 Short key = 0;
 assertFalse(decorator.containsKey(key));
 decorator.put(key, (byte)0);
 assertTrue(decorator.containsKey(key));
 }

 @Test
 public void testSize() {
 assertEquals(0, decorator.size());
 decorator.put((short)0, (byte)0);
 assertEquals(1, decorator.size());
 decorator.put((short)1, (byte)1);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 assertTrue(decorator.isEmpty());
 decorator.put((short)0, (byte)0);
 assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap map1 = new TShortByteMap();
 map1.put((short)0, (byte)0);
 map1.put((short)1, (byte)1);
 TShortByteMap map2 = new TShortByteMap();
 map2.put((short)2, (byte)2);
 map2.put((short)3, (byte)3);
 TShortByteMapDecorator decorator1 = new TShortByteMapDecorator(map1);
 TShortByteMapDecorator decorator2 = new TShortByteMapDecorator();
 decorator2.putAll(decorator1);
 assertEquals(map1.size(), decorator2.size());
 for (TShortByteIterator it = decorator1.entrySet().iterator(); it.hasNext();) {
 Entry<Short,Byte> entry = it.next();
 assertEquals(entry.getKey(), decorator2.get(entry.getKey()));
 assertEquals(entry.getValue(), decorator2.get(entry.getKey()));
 }
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Short,Byte>> entrySet = decorator.entrySet();
 assertEquals(0, entrySet.size());
 decorator.put((short)0, (byte)0);
 Entry<Short,Byte> entry = decorator.entrySet().iterator().next();
 assertEquals((short)0, entry.getKey());
 assertEquals((byte)0, entry.getValue());
 }

 @Test
 public void testContains() {
 Short key = 0;
 assertTrue(decorator.containsKey(key));
 Short key1 = 1;
 decorator.put(key1, (byte)1);
 assertTrue(decorator.containsKey(key1));
 assertFalse(decorator.containsKey(key));
 }

 @After
 public void tearDown() {
 decorator = null;
 map = null;
 }

}