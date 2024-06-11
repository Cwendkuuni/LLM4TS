import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.map.view.TMapView;
import gnu.trove.map.view.TObjectObjectMapView;
import gnu.trove.map.view.TObjectShortMapView;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Test
 public void testEmptyMap() {
     _map = new TObjectShortHashMap<Object, Short>();
     decorator = new TDoubleShortMapDecorator(_map);
     Assert.assertEquals(0, decorator.size());
     Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutGetKeyNull() {
     _map = new TObjectShortHashMap<Object, Short>();
     decorator = new TDoubleShortMapDecorator(_map);
     decorator.put(null, null);
     Assert.assertEquals(0, decorator.size());
     Assert.assertNull(decorator.get(null));
 }

 @Test
 public void testPutGetKeyValid() {
     _map = new TObjectShortHashMap<Object, Short>();
     decorator = new TDoubleShortMapDecorator(_map);
     decorator.put(1.0, (short)1);
     decorator.put(2.0, (short)2);
     Assert.assertEquals(2, decorator.size());
     Assert.assertEquals((short)1, decorator.get(1.0));
     Assert.assertEquals((short)2, decorator.get(2.0));
     Assert.assertNull(decorator.get(3.0));
 }

 @Test
 public void testPutOverwriteValue() {
     _map = new TObjectShortHashMap<Object, Short>();
     decorator = new TDoubleShortMapDecorator(_map);
     decorator.put(1.0, (short)1);
     decorator.put(1.0, (short)2);
     Assert.assertEquals(1, decorator.size());
     Assert.assertEquals((short)2, decorator.get(1.0));
 }

 @Test
 public void testPutNullValue() {
     _map = new TObjectShortHashMap<Object, Short>();
     decorator = new TDoubleShortMapDecorator(_map);
     decorator.put(1.0, null);
     Assert.assertEquals(1, decorator.size());
     Assert.assertNull(decorator.get(1.0));
 }

 @Test
 public void testClear() {
     _map = new TObjectShortHashMap<Object, Short>();
     decorator = new TDoubleShortMapDecorator(_map);
     decorator.put(1.0, (short)1);
     decorator.put(2.0, (short)2);
     decorator.clear();
     Assert.assertEquals(0, decorator.size());
     Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemove() {
     _map = new TObjectShortHashMap<Object, Short>();
     decorator = new TDoubleShortMapDecorator(_map);
     decorator.put(1.0, (short)1);
     decorator.put(2.0, (short)2);
     decorator.remove(1.0);
     Assert.assertEquals(1, decorator.size());
     Assert.assertEquals((short)2, decorator.get(2.0));
     Assert.assertNull(decorator.get(1.0));
 }

 @Test
 public void testRemoveNonExistentKey() {
     _map = new TObjectShortHashMap<Object, Short>();
     decorator = new TDoubleShortMapDecorator(_map);
     Assert.assertNull(decorator.remove(1.0));
 }

 @Test
 public void testEntrySet() {
     _map = new TObjectShortHashMap<Object, Short>();
     decorator = new TDoubleShortMapDecorator(_map);
     decorator.put(1.0, (short)1);
     decorator.put(2.0, (short)2);
     TObjectObjectMapView<Double, Short> view = decorator.entrySet().view();
     Assert.assertEquals(2, view.size());
     for (TMap.Entry<Double, Short> entry : view) {
         Assert.assertEquals(entry.getKey(), entry.getValue().getKey());
         Assert.assertEquals(entry.getValue().getValue(), entry.getValue());
     }
 }

 @Test
 public void testContainsValue() {
     _map = new TObjectShortHashMap<Object, Short>();
     decorator = new TDoubleShortMapDecorator(_map);
     decorator.put(1.0, (short)1);
     decorator.put(2.0, (short)2);
     Assert.assertTrue(decorator.containsValue((short)1));
     Assert.assertTrue(decorator.containsValue((short)2));
     Assert.assertFalse(decorator.containsValue((short)3));
 }

 @Test
 public void testContainsKey() {
     _map = new TObjectShortHashMap<Object, Short>();
     decorator = new TDoubleShortMapDecorator(_map);
     decorator.put(1.0, (short)1);
     decorator.put(2.0, (short)2);
     Assert.assertTrue(decorator.containsKey(1.0));
     Assert.assertTrue(decorator.containsKey(2.0));
     Assert.assertFalse(decorator.containsKey(3.0));
 }

 @Test
 public void testSerializable() throws IOException, ClassNotFoundException {
     _map = new TDoubleShortHashMap();
     decorator = new TDoubleShortMapDecorator(_map);
     ByteArrayOutputStream baos = new ByteArrayOutputStream();
     ObjectOutput out = new ObjectOutputStream(baos);
     decorator.writeExternal(out);
     ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
     TDoubleShortMapDecorator decoratedMap = (TDoubleShortMapDecorator) in.readObject();
     Assert.assertEquals(decorator, decoratedMap);
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
     _map = new TDoubleShortHashMap();
     decorator = new TDoubleShortMapDecorator(_map);
     TDoubleShortMapDecorator cloned = (TDoubleShortMapDecorator) decorator.clone();
     Assert.assertNotSame(decorator, cloned);
     Assert.assertEquals(decorator, cloned);
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
     _map = new TDoubleShortHashMap();
     decorator = new TDoubleShortMapDecorator(_map);
     ByteArrayOutputStream baos = new ByteArrayOutputStream();
     ObjectOutput out = new ObjectOutputStream(baos);
     decorator.writeExternal(out);
     ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
     TDoubleShortMapDecorator cloned = (TDoubleShortMapDecorator) in.readObject();
     Assert.assertNotSame(decorator, cloned);
     Assert.assertEquals(decorator, cloned);
 }

 @Test
 public void testIterators() {
     _map = new TObjectShortHashMap<Object, Short>();
     decorator = new TDoubleShortMapDecorator(_map);
     decorator.put(1.0, (short)1);
     decorator.put(2.0, (short)2);
     TDoubleShortIterator it = decorator.iterator();
     Assert.assertTrue(it.hasNext());
     Assert.assertEquals(1.0, it.key(), 0.001);
     Assert.assertEquals((short)1, it.value());
     it.advance();
     Assert.assertTrue(it.hasNext());
     Assert.assertEquals(2.0, it.key(), 0.001);
     Assert.assertEquals((short)2, it.value());
     it.remove();
     Assert.assertFalse(it.hasNext());
 }

 @Test
 public void testKeySet() {
     _map = new TObjectShortHashMap<Object, Short>();
     decorator = new TDoubleShortMapDecorator(_map);
     decorator.put(1.0, (short)1);
     decorator.put(2.0, (short)2);
     TObjectShortMapView<Double> keySetView = decorator.keySet().view();
     Assert.assertEquals(2, keySetView.size());
     Assert.assertEquals(1.0, keySetView.get(0), 0.001);
     Assert.assertEquals(2.0, keySetView.get(1), 0.001);
 }

 @Test
 public void testValues() {
     _map = new TObjectShortHashMap<Object, Short>();
     decorator = new TDoubleShortMapDecorator(_map);
     decorator.put(1.0, (short)1);
     decorator.put(2.0, (short)2);
     TObjectIntMap<Short> valuesView = decorator.values().view();
     Assert.assertEquals(2, valuesView.size());
     Assert.assertEquals((short)1, valuesView.get(0));
     Assert.assertEquals((short)2, valuesView.get(1));
 }
}