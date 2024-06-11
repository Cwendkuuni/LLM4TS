package test;

import static org.junit.Assert.*;

import gnu.trove.map.TDoubleShortMap;
import gnu.trove.map.view.TDoubleShortMapView;
import gnu.trove.iterator.TDoubleShortIterator;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap map;
 private TDoubleShortMapDecorator decorator;

 @Test
 public void testEmptyConstructor() {
 decorator = new TDoubleShortMapDecorator();
 assertNull(decorator.getMap());
 assertEquals(0, decorator.size());
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testConstructorWithMap() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 assertSame(map, decorator.getMap());
 assertEquals(0, decorator.size());
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPut() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);

 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);

 TDoubleShortMapView view = map.view();
 TDoubleShortIterator it = view.iterator();

 it.next(); // skip NoEntryKey
 assertEquals(1.0, it.key(), 0.001);
 assertEquals(1, it.value());

 it.next();
 assertEquals(2.0, it.key(), 0.001);
 assertEquals(2, it.value());

 it.next();
 assertEquals(3.0, it.key(), 0.001);
 assertEquals(3, it.value());

 assertEquals(3, decorator.size());
 assertFalse(decorator.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testNullKey() {
 decorator.put(null, (short)0);
 }

 @Test(expected = NullPointerException.class)
 public void testNullValue() {
 decorator.put(1.0, null);
 }

 @Test
 public void testGet() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);

 assertEquals((short)1, decorator.get(1.0));
 assertEquals((short)2, decorator.get(2.0));
 assertEquals(null, decorator.get(3.0));
 }

 @Test(expected = NullPointerException.class)
 public void testNullEntry() {
 decorator.get(null);
 }

 @Test
 public void testClear() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.clear();

 assertEquals(0, decorator.size());
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemove() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);

 assertEquals((short)1, decorator.remove(1.0));
 assertEquals((short)2, decorator.get(2.0));
 assertEquals(1, decorator.size());

 assertEquals(null, decorator.remove(1.0));
 assertEquals(1, decorator.size());

 assertEquals((short)2, decorator.remove(2.0));
 assertEquals(0, decorator.size());
 assertTrue(decorator.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testNullKeyRemoval() {
 decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);

 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 assertEquals(2, entrySet.size());

 Iterator<Entry<Double, Short>> iterator = entrySet.iterator();
 Entry<Double, Short> entry = iterator.next();
 assertEquals(1.0, entry.getKey(), 0.001);
 assertEquals(1, entry.getValue());

 entry = iterator.next();
 assertEquals(2.0, entry.getKey(), 0.001);
 assertEquals(2, entry.getValue());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddEntry() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);

 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 Entry<Double, Short> entry = new Entry<Double, Short>() {
 @Override public Double getKey() {
 return 3.0;
 }

 @Override public Short getValue() {
 return (short)3;
 }
 };

 entrySet.add(entry);
 }

 @Test
 public void testContainsValue() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);

 assertTrue(decorator.containsValue((short)1));
 assertTrue(decorator.containsValue((short)2));
 assertFalse(decorator.containsValue((short)3));
 }

 @Test
 public void testContainsKey() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);

 assertTrue(decorator.containsKey(1.0));
 assertTrue(decorator.containsKey(2.0));
 assertFalse(decorator.containsKey(3.0));
 }

 @Test
 public void testSize() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);

 assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);

 assertFalse(decorator.isEmpty());

 decorator.clear();
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);

 TDoubleShortMap anotherMap = new TDoubleShortMap();
 anotherMap.put(1.0, (short)1);
 anotherMap.put(2.0, (short)2);
 anotherMap.put(3.0, (short)3);

 decorator.putAll(anotherMap);

 assertEquals(3, decorator.size());
 assertFalse(decorator.isEmpty());
 }

 @Test
 public void testExternalizable() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);

 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ObjectOutput objOut = new ObjectOutputStream(out);
 decorator.writeExternal(objOut);
 ObjectInput objIn = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));

 TDoubleShortMapDecorator loadedDecorator = new TDoubleShortMapDecorator();
 loadedDecorator.readExternal(objIn);

 assertEquals(2, loadedDecorator.size());
 assertFalse(loadedDecorator.isEmpty());
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);

 TDoubleShortMapDecorator clonedDecorator = (TDoubleShortMapDecorator)decorator.clone();

 assertEquals(2, clonedDecorator.size());
 assertFalse(clonedDecorator.isEmpty());
 }
}