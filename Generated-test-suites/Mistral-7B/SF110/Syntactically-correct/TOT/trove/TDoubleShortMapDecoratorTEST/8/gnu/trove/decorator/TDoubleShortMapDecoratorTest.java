import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.view.TDoubleMap;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(_map);
 }

 @Test
 public void testPut() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);
 TDoubleMap<Double, Short> map = decorator.entrySet().iterator().next().getValue();
 TDoubleShortIterator it = _map.iterator();
 assertEquals(1, map.size());
 assertEquals((short)1, decorator.get(1.0));
 assertEquals((short)1, map.get(1.0));
 it.advance();
 assertEquals(2, map.size());
 assertEquals((short)2, decorator.get(2.0));
 assertEquals((short)2, map.get(2.0));
 it.advance();
 assertEquals(3, map.size());
 assertEquals((short)3, decorator.get(3.0));
 assertEquals((short)3, map.get(3.0));
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullKey() {
 decorator.put(null, (short)1);
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 decorator.put(1.0, null);
 }

 @Test
 public void testGet() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);
 assertEquals((short)1, decorator.get(1.0));
 assertEquals((short)1, decorator.get(1.0));
 assertEquals((short)2, decorator.get(2.0));
 assertEquals((short)2, decorator.get(2.0));
 assertEquals((short)3, decorator.get(3.0));
 assertEquals((short)3, decorator.get(3.0));
 assertEquals(null, decorator.get(null));
 }

 @Test
 public void testClear() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);
 decorator.clear();
 assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);
 assertEquals((short)1, decorator.remove(1.0));
 assertEquals((short)1, decorator.get(1.0));
 assertEquals((short)2, decorator.remove(2.0));
 assertEquals(null, decorator.get(2.0));
 assertEquals((short)3, decorator.get(3.0));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
 decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);
 assertEquals(3, decorator.entrySet().size());
 }

 @Test
 public void testContainsValue() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);
 assertTrue(decorator.containsValue((short)1));
 assertTrue(decorator.containsValue((short)2));
 assertTrue(decorator.containsValue((short)3));
 }

 @Test
 public void testContainsKey() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);
 assertTrue(decorator.containsKey(1.0));
 assertTrue(decorator.containsKey(2.0));
 assertTrue(decorator.containsKey(3.0));
 assertTrue(decorator.containsKey(null));
 }

 @Test
 public void testSize() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);
 assertEquals(3, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 decorator.clear();
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap map1 = new TDoubleShortMap();
 map1.put(1.0, (short)1);
 map1.put(2.0, (short)2);
 TDoubleShortMap map2 = new TDoubleShortMap();
 map2.put(3.0, (short)3);
 map2.put(4.0, (short)4);
 decorator.putAll(map1);
 assertEquals(2, decorator.size());
 assertEquals((short)1, decorator.get(1.0));
 assertEquals((short)1, decorator.get(1.0));
 assertEquals((short)2, decorator.get(2.0));
 assertEquals((short)2, decorator.get(2.0));
 decorator.putAll(map2);
 assertEquals(4, decorator.size());
 assertEquals((short)3, decorator.get(3.0));
 assertEquals((short)3, decorator.get(3.0));
 assertEquals((short)4, decorator.get(4.0));
 assertEquals((short)4, decorator.get(4.0));
 }
}