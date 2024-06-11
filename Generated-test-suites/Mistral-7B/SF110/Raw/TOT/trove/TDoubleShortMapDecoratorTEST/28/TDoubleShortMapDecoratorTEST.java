import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import java.util.NoSuchElementException;

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
 assertEquals(0, decorator.size());
 assertNull(decorator.get(null));
 assertNull(decorator.get(new Double(0.0)));
 }

 @Test
 public void testPutGet() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 assertEquals(2, decorator.size());
 assertEquals((short)1, decorator.get(new Double(1.0)));
 assertEquals((short)2, decorator.get(new Double(2.0)));
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullKey() {
 decorator.put(null, (short)1);
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 decorator.put(new Double(1.0), null);
 }

 @Test
 public void testClear() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.clear();
 assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 assertEquals(2, decorator.size());
 decorator.remove(new Double(1.0));
 assertEquals(1, decorator.size());
 assertNull(decorator.get(new Double(1.0)));
 }

 @Test(expected = NoSuchElementException.class)
 public void testGetNullKey() {
 decorator.get(null);
 }

 @Test(expected = NoSuchElementException.class)
 public void testGetNonExistentKey() {
 decorator.put(new Double(1.0), (short)1);
 decorator.get(new Double(2.0));
 }

 @Test
 public void testEntrySet() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 assertEquals(2, entrySet.size());
 assertTrue(entrySet.contains(new Entry<Double, Short>() {
 @Override
 public Double getKey() {
 return new Double(1.0);
 }

 @Override
 public Short getValue() {
 return (short)1;
 }
 }));
 assertTrue(entrySet.contains(new Entry<Double, Short>() {
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

 @Test(expected = UnsupportedOperationException.class)
 public void testAddInEntrySet() {
 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 assertFalse(entrySet.add(new Entry<Double, Short>() {
 @Override
 public Double getKey() {
 return new Double(3.0);
 }

 @Override
 public Short getValue() {
 return (short)3;
 }
 }));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveInEntrySet() {
 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 assertTrue(entrySet.remove(new Entry<Double, Short>() {
 @Override
 public Double getKey() {
 return new Double(1.0);
 }

 @Override
 public Short getValue() {
 return (short)1;
 }
 }));
 assertFalse(entrySet.remove(new Entry<Double, Short>() {
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
 assertFalse(decorator.containsValue((short)3));
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
 assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 assertEquals(true, decorator.isEmpty());
 decorator.put(new Double(1.0), (short)1);
 assertEquals(false, decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap otherMap = new TDoubleShortMap();
 otherMap.put(new Double(1.0), (short)1);
 otherMap.put(new Double(2.0), (short)2);
 decorator.putAll(otherMap);
 assertEquals(2, decorator.size());
 }
}