import org.junit.Assert;
import org.junit.Test;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.map.TObjectDoubleMap;
import gnu.trove.map.TObjectShortMap;
import gnu.trove.map.view.TMapView;
import gnu.trove.structure.TDouble;
import gnu.trove.structure.TShort;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Test
 public void testConstructor() {
     decorator = new TDoubleShortMapDecorator();
     Assert.assertNotNull(decorator);
     Assert.assertNull(decorator.getMap());
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

     Double key = new Double(1.0);
     Short value = new Short((short)1);

     Assert.assertEquals(null, decorator.get(key));
     Assert.assertEquals(null, decorator.put(key, value));
     Assert.assertEquals(value, decorator.get(key));
     Assert.assertEquals(1, decorator.size());
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     Assert.assertEquals(null, decorator.get(null));
     Assert.assertNull(decorator.put(null, (Short)1));
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     Double key = new Double(1.0);
     Assert.assertEquals(null, decorator.get(key));
     Assert.assertNull(decorator.put(key, null));
 }

 @Test
 public void testGet() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     Double key = new Double(1.0);
     Short value = new Short((short)1);
     decorator.put(key, value);

     Assert.assertEquals(null, decorator.get(null));
     Assert.assertEquals(value, decorator.get(key));
 }

 @Test(expected = NullPointerException.class)
 public void testGetWithNullKey() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     Assert.assertEquals(null, decorator.get(null));
 }

 @Test
 public void testClear() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     Double key1 = new Double(1.0);
     Double key2 = new Double(2.0);
     Short value1 = new Short((short)1);
     Short value2 = new Short((short)2);

     decorator.put(key1, value1);
     decorator.put(key2, value2);

     Assert.assertEquals(2, decorator.size());
     Assert.assertEquals(value1, decorator.get(key1));
     Assert.assertEquals(value2, decorator.get(key2));

     decorator.clear();

     Assert.assertEquals(0, decorator.size());
     Assert.assertEquals(null, decorator.get(key1));
     Assert.assertEquals(null, decorator.get(key2));
 }

 @Test
 public void testRemove() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     Double key1 = new Double(1.0);
     Double key2 = new Double(2.0);
     Short value1 = new Short((short)1);
     Short value2 = new Short((short)2);

     decorator.put(key1, value1);
     decorator.put(key2, value2);

     Assert.assertEquals(2, decorator.size());
     Assert.assertEquals(value1, decorator.get(key1));
     Assert.assertEquals(value2, decorator.get(key2));

     Assert.assertEquals(value1, decorator.remove(key1));
     Assert.assertEquals(value2, decorator.get(key2));
     Assert.assertEquals(1, decorator.size());

     Assert.assertEquals(null, decorator.remove(key1));
     Assert.assertEquals(value2, decorator.get(key2));
     Assert.assertEquals(1, decorator.size());

     Assert.assertEquals(null, decorator.remove(key2));
     Assert.assertEquals(null, decorator.get(key2));
     Assert.assertEquals(0, decorator.size());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     Assert.assertEquals(0, decorator.size());
     Assert.assertEquals(null, decorator.get(null));
     Assert.assertNull(decorator.remove(null));
 }

 @Test
 public void testEntrySet() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     Double key1 = new Double(1.0);
     Short value1 = new Short((short)1);
     Double key2 = new Double(2.0);
     Short value2 = new Short((short)2);

     decorator.put(key1, value1);
     decorator.put(key2, value2);

     Set<Entry<Double, Short>> entrySet = decorator.entrySet();

     Assert.assertEquals(2, entrySet.size());

     Iterator<Entry<Double, Short>> iterator = entrySet.iterator();
     Entry<Double, Short> entry1 = iterator.next();
     Entry<Double, Short> entry2 = iterator.next();

     Assert.assertEquals(key1, entry1.getKey());
     Assert.assertEquals(value1, entry1.getValue());

     Assert.assertEquals(key2, entry2.getKey());
     Assert.assertEquals(value2, entry2.getValue());

     iterator.remove();

     Assert.assertEquals(1, entrySet.size());
     Assert.assertEquals(key2, entrySet.iterator().next().getKey());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddEntrySet() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     Set<Entry<Double, Short>> entrySet = decorator.entrySet();

     Entry<Double, Short> entry = new AbstractMap.SimpleEntry<>(new Double(1.0), new Short((short)1));

     entrySet.add(entry);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveEntrySet() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     Set<Entry<Double, Short>> entrySet = decorator.entrySet();

     Entry<Double, Short> entry = new AbstractMap.SimpleEntry<>(new Double(1.0), new Short((short)1));

     entrySet.remove(entry);
 }

 @Test
 public void testContainsValue() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     Double key1 = new Double(1.0);
     Short value1 = new Short((short)1);
     Double key2 = new Double(2.0);
     Short value2 = new Short((short)2);

     decorator.put(key1, value1);
     decorator.put(key2, value2);

     Assert.assertTrue(decorator.containsValue(value1));
     Assert.assertTrue(decorator.containsValue(value2));
 }

 @Test
 public void testContainsKey() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     Double key1 = new Double(1.0);
     Double key2 = new Double(2.0);
     Short value1 = new Short((short)1);
     Short value2 = new Short((short)2);

     decorator.put(key1, value1);
     decorator.put(key2, value2);

     Assert.assertTrue(decorator.containsKey(key1));
     Assert.assertTrue(decorator.containsKey(key2));
 }

 @Test
 public void testSize() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     Assert.assertEquals(0, decorator.size());

     Double key1 = new Double(1.0);
     Short value1 = new Short((short)1);
     Double key2 = new Double(2.0);
     Short value2 = new Short((short)2);

     decorator.put(key1, value1);
     decorator.put(key2, value2);

     Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     Assert.assertTrue(decorator.isEmpty());

     Double key1 = new Double(1.0);
     Short value1 = new Short((short)1);
     Double key2 = new Double(2.0);
     Short value2 = new Short((short)2);

     decorator.put(key1, value1);
     decorator.put(key2, value2);

     Assert.assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     TObjectDoubleMap<Double, Short> map = new TObjectShortMap();
     map.put(new Double(1.0), new Short((short)1));
     map.put(new Double(2.0), new Short((short)2));

     decorator.putAll(map);

     Assert.assertEquals(2, decorator.size());
     Assert.assertEquals(new Double(1.0), decorator.keySet().iterator().next());
     Assert.assertEquals(new Double(2.0), decorator.keySet().iterator().next());
     Assert.assertEquals(new Short((short)1), decorator.get(new Double(1.0)));
     Assert.assertEquals(new Short((short)2), decorator.get(new Double(2.0)));
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     ByteArrayOutputStream bytes = new ByteArrayOutputStream();
     ObjectOutput out = new ObjectOutputStream(bytes);

     out.writeObject(decorator);
     out.close();

     byte[] data = bytes.toByteArray();

     ByteArrayInputStream input = new ByteArrayInputStream(data);
     ObjectInput in = new ObjectInputStream(input);

     TDoubleShortMapDecorator dec = (TDoubleShortMapDecorator)in.readObject();
     in.close();

     Assert.assertNotNull(dec);
     Assert.assertNotNull(dec.getMap());
     Assert.assertEquals(_map.size(), dec.size());
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     TDoubleShortMapDecorator cloned = (TDoubleShortMapDecorator)decorator.clone();

     Assert.assertNotNull(cloned);
     Assert.assertNotNull(cloned.getMap());
     Assert.assertEquals(_map.size(), cloned.size());
 }
}