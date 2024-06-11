import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(_map);
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

 assertEquals(2, decorator.size());
 assertNull(decorator.get(null));
 assertEquals((short)1, decorator.get(new Double(1.0)));
 assertEquals((short)2, decorator.get(new Double(2.0)));

 decorator.put(new Double(1.0), (short)3);
 assertEquals((short)3, decorator.get(new Double(1.0)));
 }

 @Test
 public void testGetNullKey() {
 assertNull(decorator.get(null));
 }

 @Test(expected = NullPointerException.class)
 public void testGetNullValue() {
 decorator.put(new Double(1.0), (short)1);
 decorator.get(new Double(1.0), null);
 }

 @Test(expected = ClassCastException.class)
 public void testGetWrongTypeValue() {
 decorator.put(new Double(1.0), (String)1);
 decorator.get(new Double(1.0));
 }

 @Test
 public void testClear() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.clear();

 assertTrue(decorator.isEmpty());
 assertEquals(0, decorator.size());
 assertNull(decorator.get(new Double(1.0)));
 assertNull(decorator.get(new Double(2.0)));
 }

 @Test
 public void testRemove() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);

 assertEquals(2, decorator.size());
 assertNull(decorator.remove(null));
 assertEquals((short)1, decorator.remove(new Double(1.0)));
 assertEquals((short)2, decorator.get(new Double(2.0)));

 assertEquals(1, decorator.size());
 assertNull(decorator.remove(new Double(1.0)));
 assertEquals((short)2, decorator.get(new Double(2.0)));
 }

 @Test
 public void testEntrySet() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);

 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 assertEquals(2, entrySet.size());

 Entry<Double, Short> entry = entrySet.iterator().next();
 assertTrue(entry.equals(new Entry<Double, Short>() {
 @Override
 public Double getKey() {
 return new Double(1.0);
 }

 @Override
 public Short getValue() {
 return (short)1;
 }
 }));

 entry = entrySet.iterator().next();
 assertTrue(entry.equals(new Entry<Double, Short>() {
 @Override
 public Double getKey() {
 return new Double(2.0);
 }

 @Override
 public Short getValue() {
 return (short)2;
 }
 }));
 }

 @Test
 public void testContainsValue() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);

 assertTrue(decorator.containsValue((short)1));
 assertTrue(decorator.containsValue((short)2));
 assertFalse(decorator.containsValue(new Short((short)3)));
 }

 @Test
 public void testContainsKey() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);

 assertTrue(decorator.containsKey(new Double(1.0)));
 assertTrue(decorator.containsKey(new Double(2.0)));
 assertFalse(decorator.containsKey(new Double(3.0)));
 }

 @Test
 public void testSize() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(new Double(3.0), (short)3);

 assertEquals(3, decorator.size());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap anotherMap = new TDoubleShortMap();
 anotherMap.put(new Double(1.0), (short)1);
 anotherMap.put(new Double(2.0), (short)2);
 anotherMap.put(new Double(3.0), (short)3);

 decorator.putAll(anotherMap);

 assertEquals(3, decorator.size());
 assertNull(decorator.get(null));
 assertEquals((short)1, decorator.get(new Double(1.0)));
 assertEquals((short)2, decorator.get(new Double(2.0)));
 assertEquals((short)3, decorator.get(new Double(3.0)));
 }
}