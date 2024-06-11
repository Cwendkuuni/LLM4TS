import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setup() {
 _map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(_map);
 }

 @Test
 public void testConstructor() {
 assertNotNull(decorator);
 assertEquals(0, decorator.size());
 }

 @Test
 public void testConstructorWithMap() {
 _map.put(1.0, (short) 1);
 _map.put(2.0, (short) 2);
 decorator = new TDoubleShortMapDecorator(_map);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testGetMap() {
 TDoubleShortMap map = decorator.getMap();
 assertEquals(_map, map);
 }

 @Test
 public void testPut() {
 Double key = 3.0;
 Short value = (short) 3;
 Short result = decorator.put(key, value);
 assertEquals(null, result);
 assertEquals(1, decorator.size());
 assertEquals(value, decorator.get(key));

 key = 1.0;
 value = (short) 1;
 result = decorator.put(key, value);
 assertEquals(value, result);
 assertEquals(2, decorator.size());
 assertEquals(value, decorator.get(key));
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
 decorator.put(null, (short) 0);
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
 decorator.put(1.0, null);
 }

 @Test
 public void testGet() {
 Double key = 1.0;
 Short value = (short) 1;
 decorator.put(key, value);
 assertEquals(value, decorator.get(key));
 }

 @Test
 public void testGetWithNullKey() {
 assertEquals(null, decorator.get(null));
 }

 @Test
 public void testGetWithNonExistentKey() {
 assertEquals(null, decorator.get(1.5));
 }

 @Test
 public void testClear() {
 decorator.put(1.0, (short) 1);
 decorator.clear();
 assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 decorator.put(1.0, (short) 1);
 Short result = decorator.remove(1.0);
 assertEquals((short) 1, result);
 assertEquals(0, decorator.size());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
 decorator.remove(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveWithNonExistentKey() {
 decorator.remove(1.5);
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 assertFalse(entrySet.isEmpty());
 }

 @Test
 public void testContainsValue() {
 decorator.put(1.0, (short) 1);
 assertTrue(decorator.containsValue((short) 1));
 }

 @Test
 public void testContainsKey() {
 decorator.put(1.0, (short) 1);
 assertTrue(decorator.containsKey(1.0));
 }

 @Test
 public void testSize() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 assertEquals(true, decorator.isEmpty());
 decorator.put(1.0, (short) 1);
 assertEquals(false, decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap anotherMap = new TDoubleShortMap();
 anotherMap.put(1.0, (short) 1);
 anotherMap.put(2.0, (short) 2);
 decorator.putAll(anotherMap);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testExternalizable() {
 // Externalizability testing is complex and beyond the scope of this example.
 }

 @Test
 public void testCloneable() {
 try {
 TDoubleShortMapDecorator clonedDecorator = (TDoubleShortMapDecorator) decorator.clone();
 assertEquals(decorator, clonedDecorator);
 } catch (CloneNotSupportedException e) {
 fail("Clone not supported, but it should be");
 }
}

 @Test
 public void testEdgeCases() {
 // Edge cases testing is important. Here are some potential edge cases:

 // Testing with empty map
 TDoubleShortMap emptyMap = new TDoubleShortMap();
 TDoubleShortMapDecorator emptyDecorator = new TDoubleShortMapDecorator(emptyMap);
 assertEquals(0, emptyDecorator.size());

 // Testing with null key and value
 decorator.put(null, null);
 assertEquals(0, decorator.size());

 // Testing with negative key
 decorator.put(-1.0, (short) -1);
 assertEquals(1, decorator.size());
 assertEquals((short) -1, decorator.get(-1.0));

 // Testing with large key
 decorator.put(Double.MAX_VALUE, (short) Double.MAX_VALUE);
 assertEquals(1, decorator.size());
 assertEquals((short) Double.MAX_VALUE, decorator.get(Double.MAX_VALUE));

 // Testing with large value
 decorator.put(1.0, (short) Short.MAX_VALUE);
 assertEquals(1, decorator.size());
 assertEquals((short) Short.MAX_VALUE, decorator.get(1.0));

 // Testing with keys equal to no entry key
 decorator.put(this._map.getNoEntryKey(), (short) 1);
 decorator.put(this._map.getNoEntryKey(), (short) 2);
 assertEquals(1, decorator.size());
 assertEquals((short) 2, decorator.get(this._map.getNoEntryKey()));

 // Testing with keys equal
 decorator.put(1.0, (short) 1);
 decorator.put(1.0, (short) 2);
 assertEquals(1, decorator.size());
 assertEquals((short) 2, decorator.get(1.0));

 // Testing with keys close to each other
 decorator.put(0.1, (short) 1);
 decorator.put(0.2, (short) 2);
 assertEquals(1, decorator.size());
 assertEquals((short) 2, decorator.get(0.2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithDuplicateKeys() {
 decorator.put(1.0, (short) 1);
 decorator.put(1.0, (short) 2);
 }

 @Test(expected = ClassCastException.class)
 public void testGetWithNonShortValue() {
 decorator.put(1.0, new Integer(1));
 assertEquals(null, decorator.get(1.0));
 }

 @Test(expected = ClassCastException.class)
 public void testRemoveWithNonShortValue() {
 decorator.put(1.0, new Integer(1));
 decorator.remove(1.0);
 assertEquals(0, decorator.size());
 }

 @Test(expected = ClassCastException.class)
 public void testPutAllWithNonShortValues() {
 TDoubleShortMap anotherMap = new TDoubleShortMap();
 anotherMap.put(1.0, new Integer(1));
 decorator.putAll(anotherMap);
 assertEquals(0, decorator.size());
 }

 @Test(expected = ClassCastException.class)
 public void testIteratorWithNonShortValues() {
 for (Entry<Double, Short> entry : decorator.entrySet()) {
 decorator.put(entry.getKey(), new Integer(1));
 }
 TDoubleShortIterator it = decorator.iterator();
 assertFalse(it.hasNext());
 }
}