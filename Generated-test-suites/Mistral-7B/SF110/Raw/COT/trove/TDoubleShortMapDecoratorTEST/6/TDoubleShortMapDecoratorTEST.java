import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setUp() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 }

 @Test
 public void testConstructor() {
 TDoubleShortMapDecorator test = new TDoubleShortMapDecorator();
 assertNotNull(test);
 }

 @Test
 public void testConstructorWithMap() {
 TDoubleShortMapDecorator test = new TDoubleShortMapDecorator(map);
 assertEquals(map, test.getMap());
 }

 @Test
 public void testGetMap() {
 TDoubleShortMapDecorator test = new TDoubleShortMapDecorator();
 assertNull(test.getMap());

 TDoubleShortMapDecorator test2 = new TDoubleShortMapDecorator(map);
 assertEquals(map, test2.getMap());
 }

 @Test
 public void testPutNullKey() {
 assertNull(decorator.put(null, (short) 0));
 assertEquals(0, decorator.size());
 }

 @Test
 public void testPut() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);

 assertEquals(2, decorator.size());
 Short value1 = decorator.get(1.0);
 assertEquals((short) 1, value1);
 Short value2 = decorator.get(2.0);
 assertEquals((short) 2, value2);
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 decorator.put(1.0, null);
 }

 @Test
 public void testGetNullKey() {
 assertNull(decorator.get(null));
 }

 @Test
 public void testClear() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 decorator.clear();

 assertEquals(0, decorator.size());
 }

 @Test
 public void testRemoveNullKey() {
 assertNull(decorator.remove(null));
 }

 @Test
 public void testRemove() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 decorator.remove(1.0);

 assertEquals(1, decorator.size());
 assertNull(decorator.get(1.0));
 Short value2 = decorator.get(2.0);
 assertEquals((short) 2, value2);
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Double, Short>> set = decorator.entrySet();
 assertTrue(set instanceof AbstractSet);
 }

 @Test
 public void testContainsValue() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 assertTrue(decorator.containsValue((short) 1));
 assertFalse(decorator.containsValue((short) 3));
 }

 @Test
 public void testContainsKey() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 assertTrue(decorator.containsKey(1.0));
 assertTrue(decorator.containsKey(2.0));
 assertFalse(decorator.containsKey(3.0));
 }

 @Test
 public void testSize() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 decorator.clear();
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap map2 = new TDoubleShortMap();
 map2.put(1.5, (short) 3);
 map2.put(2.5, (short) 4);

 decorator.putAll(map2);
 assertEquals(2, decorator.size());
 Short value1 = decorator.get(1.0);
 assertNull(value1);
 Short value2 = decorator.get(1.5);
 assertEquals((short) 3, value2);
 Short value3 = decorator.get(2.0);
 assertNull(value3);
 Short value4 = decorator.get(2.5);
 assertEquals((short) 4, value4);
 }

 @Test
 public void testWrapKey() {
 double key = 1.0;
 assertEquals(key, decorator.wrapKey(key));
 }

 @Test
 public void testUnwrapKey() {
 Double key = 1.0;
 assertEquals(key.doubleValue(), decorator.unwrapKey(key));
 }

 @Test
 public void testWrapValue() {
 short value = (short) 1;
 assertEquals(value, decorator.wrapValue(value));
 }

 @Test
 public void testUnlockValue() {
 Short value = (Short) 1;
 assertEquals(value.shortValue(), decorator.unwrapValue(value));
 }

 @Test
 public void testIteration() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 decorator.put(3.0, (short) 3);

 Set<Entry<Double, Short>> set = decorator.entrySet();
 Iterator<Entry<Double, Short>> iterator = set.iterator();

 assertEquals(3, iterator.size());
 Entry<Double, Short> entry1 = iterator.next();
 assertEquals(1.0, entry1.getKey());
 assertEquals((short) 1, entry1.getValue());

 Entry<Double, Short> entry2 = iterator.next();
 assertEquals(2.0, entry2.getKey());
 assertEquals((short) 2, entry2.getValue());

 Entry<Double, Short> entry3 = iterator.next();
 assertEquals(3.0, entry3.getKey());
 assertEquals((short) 3, entry3.getValue());

 assertFalse(iterator.hasNext());
 }

 @Test(expected = NoSuchElementException.class)
 public void testIterationEmptyMap() {
 Set<Entry<Double, Short>> set = decorator.entrySet();
 Iterator<Entry<Double, Short>> iterator = set.iterator();
 iterator.next();
 iterator.next();
 iterator.next();
 iterator.next();
 }
}