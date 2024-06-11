import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.view.TDoubleShortMapView;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Test
 public void testConstructor() {
     decorator = new TDoubleShortMapDecorator();
     Assert.assertNotNull(decorator);
 }

 @Test
 public void testConstructorWithMap() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);
     Assert.assertNotNull(decorator);
     Assert.assertSame(_map, decorator.getMap());
 }

 @Test
 public void testPut() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);
     Short value = (Short)1;

     Double key1 = 1.0;
     Double key2 = 2.0;
     Double key3 = null;

     Short placedValue1 = decorator.put(key1, value);
     assertTrue(decorator.containsKey(key1));
     assertEquals(value, decorator.get(key1));
     assertEquals(value, placedValue1);

     Short placedValue2 = decorator.put(key2, value);
     assertTrue(decorator.containsKey(key2));
     assertEquals(value, decorator.get(key2));
     assertSame(placedValue1, placedValue2);

     assertNull(decorator.put(key1, (Short)2));
     assertFalse(decorator.containsValue((Short)2));
     assertFalse(decorator.containsKey(key1));
     assertEquals(value, decorator.get(key2));

     assertNull(decorator.put(key3, (Short)3));
     assertFalse(decorator.containsKey(key3));
 }

 @Test
 public void testGet() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     Double key = 1.0;
     Short value = (Short)1;
     decorator.put(key, value);

     Short retrievedValue = decorator.get(key);
     assertEquals(value, retrievedValue);
 }

 @Test(expected = NullPointerException.class)
 public void testGetWithNullKey() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);
     decorator.get(null);
 }

 @Test
 public void testClear() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);
     decorator.put(1.0, (Short)1);
     decorator.clear();
     assertFalse(decorator.containsKey(1.0));
 }

 @Test
 public void testRemove() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);
     decorator.put(1.0, (Short)1);

     Short removedValue = decorator.remove(1.0);
     assertTrue(removedValue != null);
     assertFalse(decorator.containsKey(1.0));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);
     decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     Set<Entry<Double, Short>> entrySet = decorator.entrySet();
     assertFalse(entrySet.isEmpty());

     decorator.clear();
     assertTrue(entrySet.isEmpty());
 }

 @Test
 public void testContainsValue() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     decorator.put(1.0, (Short)1);
     decorator.put(2.0, (Short)2);

     assertTrue(decorator.containsValue((Short)1));
     assertTrue(decorator.containsValue((Short)2));
     assertFalse(decorator.containsValue(null));
     assertFalse(decorator.containsValue((Short)3));
 }

 @Test
 public void testContainsKey() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     decorator.put(1.0, (Short)1);
     decorator.put(2.0, (Short)2);

     assertTrue(decorator.containsKey(1.0));
     assertTrue(decorator.containsKey(2.0));
     assertFalse(decorator.containsKey(3.0));
     assertFalse(decorator.containsKey(null));
 }

 @Test
 public void testSize() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     decorator.put(1.0, (Short)1);
     decorator.put(2.0, (Short)2);
     decorator.put(3.0, (Short)3);

     assertEquals(3, decorator.size());
 }

 @Test
 public void testIsEmpty() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     assertFalse(decorator.isEmpty());

     decorator.clear();
     assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     Map<? extends Double, ? extends Short> mapToPut = new TDoubleShortMap();
     mapToPut.put(1.0, (Short)1);
     mapToPut.put(2.0, (Short)2);

     decorator.putAll(mapToPut);

     assertEquals(2, decorator.size());
     assertTrue(decorator.containsKey(1.0));
     assertTrue(decorator.containsKey(2.0));
 }

 @Test
 public void testWrapKey() {
     decorator = new TDoubleShortMapDecorator();
     assertEquals(0.0, decorator.wrapKey(0.0));
 }

 @Test
 public void testUnwrapKey() {
     decorator = new TDoubleShortMapDecorator();
     Double key = decorator.unwrapKey(0.0);
     assertEquals(0.0, key.doubleValue());
 }

 @Test
 public void testWrapValue() {
     decorator = new TDoubleShortMapDecorator();
     assertEquals((short)0, decorator.wrapValue((short)0));
 }

 @Test
 public void testUnwrapValue() {
     decorator = new TDoubleShortMapDecorator();
     Short value = decorator.unwrapValue((short)0);
     assertEquals((short)0, value);
 }

 @Test
 public void testSerializable() throws IOException, ClassNotFoundException {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     ByteArrayOutputStream baos = new ByteArrayOutputStream();
     ObjectOutput out = new ObjectOutputStream(baos);
     decorator.writeExternal(out);

     ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
     TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator) in.readObject();

     assertEquals(decorator.size(), deserializedDecorator.size());
     assertSame(decorator.getMap(), deserializedDecorator.getMap());
 }

 @Test
 public void testCloneable() throws CloneNotSupportException {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     TDoubleShortMapDecorator clonedDecorator = (TDoubleShortMapDecorator) decorator.clone();
     assertNotNull(clonedDecorator);
     assertEquals(decorator.size(), clonedDecorator.size());
     assertSame(decorator.getMap(), clonedDecorator.getMap());
 }

 @Test
 public void testIterator() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     decorator.put(1.0, (Short)1);
     decorator.put(2.0, (Short)2);

     TDoubleShortMapView view = decorator.entrySet().iterator();
     TDoubleShortIterator it = view.iterator();

     Entry<Double, Short> entry;

     entry = it.next();
     assertEquals(1.0, entry.getKey().doubleValue());
     assertEquals(Short.valueOf((short)1), entry.getValue());

     entry = it.next();
     assertEquals(2.0, entry.getKey().doubleValue());
     assertEquals(Short.valueOf((short)2), entry.getValue());

     assertFalse(it.hasNext());
 }
}