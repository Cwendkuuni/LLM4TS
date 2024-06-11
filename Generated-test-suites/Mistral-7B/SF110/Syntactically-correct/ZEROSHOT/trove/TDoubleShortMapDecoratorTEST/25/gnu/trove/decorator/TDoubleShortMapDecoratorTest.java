import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.view.TDoubleShortMapView;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator _decorator;

 @Before
 public void setUp() {
 _map = new TDoubleShortMap();
 _decorator = new TDoubleShortMapDecorator(_map);
 }

 @After
 public void tearDown() {
 _map.clear();
 _decorator = null;
 }

 @Test
 public void testConstructor() {
 // Test default constructor
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
 assertNull(decorator.getMap());
 // Test constructor with existing TDoubleShortMap
 TDoubleShortMap map = new TDoubleShortMap();
 TDoubleShortMapDecorator decoratorWithMap = new TDoubleShortMapDecorator(map);
 assertSame(map, decoratorWithMap.getMap());
 }

 @Test
 public void testPut() {
 // Test null key and null value
 assertNull(_decorator.put(null, null));
 // Test null key and non-null value
 assertNull(_decorator.put(null, (short)1));
 // Test non-null key and null value
 assertNull(_decorator.put((double)1.0, null));
 // Test valid put
 _decorator.put((double)1.0, (short)1);
 assertEquals(1, _map.size());
 assertEquals((short)1, _decorator.get((double)1.0));
 // Test put with existing key
 _decorator.put((double)1.0, (short)2);
 assertEquals((short)2, _decorator.get((double)1.0));
 }

 @Test
 public void testGet() {
 // Test get for non-existing key
 assertNull(_decorator.get(null));
 assertNull(_decorator.get((double)1.5));
 // Test get for existing key
 _decorator.put((double)1.0, (short)1);
 assertEquals((short)1, _decorator.get((double)1.0));
 }

 @Test
 public void testClear() {
 // Test clear
 _decorator.put((double)1.0, (short)1);
 _decorator.clear();
 assertEquals(0, _map.size());
 }

 @Test
 public void testRemove() {
 // Test remove for non-existing key
 assertNull(_decorator.remove(null));
 // Test remove for existing key
 _decorator.put((double)1.0, (short)1);
 assertEquals((short)1, _decorator.get((double)1.0));
 _decorator.remove((double)1.0);
 assertNull(_decorator.get((double)1.0));
 }

 @Test
 public void testEntrySet() {
 // Test size
 _decorator.put((double)1.0, (short)1);
 _decorator.put((double)2.0, (short)2);
 assertEquals(2, _decorator.size());
 // Test isEmpty
 assertTrue(_decorator.isEmpty());
 _decorator.put((double)1.0, (short)1);
 assertFalse(_decorator.isEmpty());
 // Test contains
 TDoubleShortMapView view = _decorator.entrySet().iterator().next().getValue();
 assertTrue(_decorator.containsValue(view));
 // Test containsKey
 assertFalse(_decorator.containsKey((Object)null));
 assertTrue(_decorator.containsKey((double)1.0));
 // Test iteration
 int i = 0;
 for (Entry<Double, Short> entry : _decorator.entrySet()) {
 assertEquals(entry.getKey(), (double)i++, 0.001);
 assertEquals(entry.getValue(), (short)i);
 }
 }

 @Test
 public void testContainsValue() {
 // Test empty map
 assertFalse(_decorator.containsValue(null));
 // Test non-empty map with null value
 _decorator.put((double)1.0, null);
 assertFalse(_decorator.containsValue(null));
 // Test non-empty map with valid value
 _decorator.put((double)1.0, (short)1);
 assertFalse(_decorator.containsValue((short)2));
 // Test non-empty map with same value
 _decorator.put((double)1.0, (short)1);
 _decorator.put((double)2.0, (short)1);
 assertTrue(_decorator.containsValue((short)1));
 }

 @Test
 public void testContainsKey() {
 // Test empty map
 assertFalse(_decorator.containsKey(null));
 // Test non-empty map with non-existing key
 assertFalse(_decorator.containsKey((Object)new Object()));
 // Test non-empty map with existing key
 _decorator.put((double)1.0, (short)1);
 assertTrue(_decorator.containsKey((double)1.0));
 }

 @Test
 public void testSize() {
 // Test empty map
 assertEquals(0, _decorator.size());
 // Test non-empty map
 _decorator.put((double)1.0, (short)1);
 _decorator.put((double)2.0, (short)2);
 assertEquals(2, _decorator.size());
 }

 @Test
 public void testIsEmpty() {
 // Test empty map
 assertTrue(_decorator.isEmpty());
 // Test non-empty map
 _decorator.put((double)1.0, (short)1);
 assertFalse(_decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 // Test empty decorator
 TDoubleShortMap mapToAdd = new TDoubleShortMap();
 mapToAdd.put((double)1.0, (short)1);
 mapToAdd.put((double)2.0, (short)2);
 _decorator.putAll(mapToAdd);
 assertEquals(2, _decorator.size());
 // Test non-empty decorator
 _decorator.put((double)3.0, (short)3);
 mapToAdd.clear();
 mapToAdd.put((double)4.0, (short)4);
 _decorator.putAll(mapToAdd);
 assertEquals(4, _decorator.size());
 }
}