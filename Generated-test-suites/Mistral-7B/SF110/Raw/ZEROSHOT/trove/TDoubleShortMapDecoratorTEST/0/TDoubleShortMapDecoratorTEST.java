import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.view.TDoubleMapView;
import gnu.trove.map.view.TSortedMapView;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap map;
 private TDoubleShortMapDecorator decorator;

 @Test
 public void testConstructor() {
 decorator = new TDoubleShortMapDecorator();
 Assert.assertNotNull(decorator);
 Assert.assertNull(decorator.getMap());
 Assert.assertEquals(0, decorator.size());
 }

 @Test
 public void testConstructorWithMap() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 Assert.assertNotNull(decorator);
 Assert.assertSame(map, decorator.getMap());
 Assert.assertEquals(0, decorator.size());
 }

 @Test
 public void testPut() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);

 TDoubleMapView<Double, Short> view = decorator.asMap();
 TSortedMapView<Double, Short> sortedView = decorator.asSortedMap();

 Assert.assertEquals(3, decorator.size());
 Assert.assertEquals((short)1, decorator.get(1.0));
 Assert.assertEquals((short)2, decorator.get(2.0));
 Assert.assertEquals((short)3, decorator.get(3.0));

 Assert.assertEquals(1.0, view.firstKey(), 0.001);
 Assert.assertEquals(1.0, sortedView.firstKey(), 0.001);
 Assert.assertEquals(3.0, view.lastKey(), 0.001);
 Assert.assertEquals(3.0, sortedView.lastKey(), 0.001);

 Assert.assertNull(decorator.get(5.0));
 Assert.assertNull(view.get(5.0));
 Assert.assertNull(sortedView.get(5.0));

 decorator.clear();
 Assert.assertEquals(0, decorator.size());
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(null, (short)1);
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, null);
 }

 @Test
 public void testGet() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);

 Assert.assertEquals((short)1, decorator.get(1.0));
 Assert.assertEquals((short)2, decorator.get(2.0));
 Assert.assertEquals((short)3, decorator.get(3.0));
 Assert.assertNull(decorator.get(5.0));
 }

 @Test
 public void testClear() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);
 decorator.clear();

 Assert.assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);
 decorator.remove(2.0);

 Assert.assertEquals(2, decorator.size());
 Assert.assertEquals((short)1, decorator.get(1.0));
 Assert.assertEquals((short)3, decorator.get(3.0));
 Assert.assertNull(decorator.get(2.0));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
 decorator = new TDoubleShortMapDecorator(map);
 decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);

 Assert.assertEquals(3, decorator.size());

 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 Iterator<Entry<Double, Short>> iterator = entrySet.iterator();

 Assert.assertTrue(iterator.hasNext());
 Entry<Double, Short> entry = iterator.next();
 Assert.assertEquals(1.0, entry.getKey(), 0.001);
 Assert.assertEquals((short)1, entry.getValue());

 Assert.assertTrue(iterator.hasNext());
 entry = iterator.next();
 Assert.assertEquals(2.0, entry.getKey(), 0.001);
 Assert.assertEquals((short)2, entry.getValue());

 Assert.assertTrue(iterator.hasNext());
 entry = iterator.next();
 Assert.assertEquals(3.0, entry.getKey(), 0.001);
 Assert.assertEquals((short)3, entry.getValue());

 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testContainsValue() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);

 Assert.assertTrue(decorator.containsValue((short)1));
 Assert.assertTrue(decorator.containsValue((short)2));
 Assert.assertTrue(decorator.containsValue((short)3));
 Assert.assertFalse(decorator.containsValue((short)4));
 }

 @Test
 public void testContainsKey() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);

 Assert.assertTrue(decorator.containsKey(1.0));
 Assert.assertTrue(decorator.containsKey(2.0));
 Assert.assertTrue(decorator.containsKey(3.0));
 Assert.assertFalse(decorator.containsKey(5.0));
 }

 @Test
 public void testSize() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);

 Assert.assertEquals(3, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);

 Assert.assertTrue(decorator.isEmpty());
 decorator.put(1.0, (short)1);
 Assert.assertFalse(decorator.isEmpty());
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

 Assert.assertEquals(3, decorator.size());
 Assert.assertEquals((short)1, decorator.get(1.0));
 Assert.assertEquals((short)2, decorator.get(2.0));
 Assert.assertEquals((short)3, decorator.get(3.0));
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ObjectOutput output = new ObjectOutputStream(outputStream);
 decorator.writeExternal(output);
 output.close();

 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 ObjectInput input = new ObjectInputStream(inputStream);
 TDoubleShortMapDecorator dec = (TDoubleShortMapDecorator)input.readObject();
 input.close();

 Assert.assertEquals(3, dec.size());
 Assert.assertEquals((short)1, dec.get(1.0));
 Assert.assertEquals((short)2, dec.get(2.0));
 Assert.assertEquals((short)3, dec.get(3.0));
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);

 TDoubleShortMapDecorator clone = (TDoubleShortMapDecorator)decorator.clone();

 Assert.assertEquals(3, clone.size());
 Assert.assertEquals((short)1, clone.get(1.0));
 Assert.assertEquals((short)2, clone.get(2.0));
 Assert.assertEquals((short)3, clone.get(3.0));
 }
}