import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.view.TDoubleMapView;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setUp() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 }

 @Test
 public void testEmptyMap() {
 assertTrue(decorator.isEmpty());
 assertEquals(0, decorator.size());
 assertNull(decorator.get(null));
 assertNull(decorator.get(new Double(0.0)));
 }

 @Test
 public void testPut() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(new Double(3.0), (short)3);

 assertEquals(3, decorator.size());

 assertNotNull(decorator.get(new Double(1.0)));
 assertNotNull(decorator.get(new Double(2.0)));
 assertNotNull(decorator.get(new Double(3.0)));

 assertEquals((short)1, decorator.get(new Double(1.0)));
 assertEquals((short)2, decorator.get(new Double(2.0)));
 assertEquals((short)3, decorator.get(new Double(3.0)));

 assertNull(decorator.get(null));
 assertNull(decorator.get(new Double(0.0)));
 }

 @Test
 public void testGetNullKey() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(new Double(3.0), (short)3);

 assertEquals(3, decorator.size());

 assertEquals((short)1, decorator.get(new Double(1.0)));
 assertEquals((short)2, decorator.get(new Double(2.0)));
 assertEquals((short)3, decorator.get(new Double(3.0)));

 assertNull(decorator.get(null));
 }

 @Test
 public void testClear() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(new Double(3.0), (short)3);

 assertEquals(3, decorator.size());

 decorator.clear();

 assertTrue(decorator.isEmpty());
 assertEquals(0, decorator.size());
 assertNull(decorator.get(new Double(1.0)));
 assertNull(decorator.get(new Double(2.0)));
 assertNull(decorator.get(new Double(3.0)));
 }

 @Test
 public void testRemove() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(new Double(3.0), (short)3);

 assertEquals(3, decorator.size());

 assertNotNull(decorator.remove(new Double(2.0)));
 assertEquals(2, decorator.size());

 assertNotNull(decorator.get(new Double(1.0)));
 assertNotNull(decorator.get(new Double(3.0)));

 assertNull(decorator.get(new Double(2.0)));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveNullKey() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(new Double(3.0), (short)3);

 decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(new Double(3.0), (short)3);

 TDoubleMapView<Double, Short> view = decorator.entrySet().view();
 TDoubleShortIterator it = view.iterator();

 assertEquals(3, view.size());

 it.advance();
 assertEquals(new Double(1.0), it.key());
 assertEquals((short)1, it.value());

 it.advance();
 assertEquals(new Double(2.0), it.key());
 assertEquals((short)2, it.value());

 it.advance();
 assertEquals(new Double(3.0), it.key());
 assertEquals((short)3, it.value());
 }

 @Test
 public void testContainsKey() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(new Double(3.0), (short)3);

 assertTrue(decorator.containsKey(new Double(1.0)));
 assertTrue(decorator.containsKey(new Double(2.0)));
 assertTrue(decorator.containsKey(new Double(3.0)));

 assertFalse(decorator.containsKey(null));
 }

 @Test
 public void testContainsValue() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(new Double(3.0), (short)3);

 assertTrue(decorator.containsValue((short)1));
 assertTrue(decorator.containsValue((short)2));
 assertTrue(decorator.containsValue((short)3));

 assertFalse(decorator.containsValue((short)4));
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap otherMap = new TDoubleShortMap();
 otherMap.put(new Double(1.0), (short)1);
 otherMap.put(new Double(2.0), (short)2);
 otherMap.put(new Double(3.0), (short)3);

 decorator.putAll(otherMap);

 assertEquals(3, decorator.size());

 assertNotNull(decorator.get(new Double(1.0)));
 assertNotNull(decorator.get(new Double(2.0)));
 assertNotNull(decorator.get(new Double(3.0)));
 }
}