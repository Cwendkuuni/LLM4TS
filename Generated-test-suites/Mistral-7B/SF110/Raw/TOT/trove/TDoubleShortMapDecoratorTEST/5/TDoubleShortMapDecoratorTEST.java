import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.view.TDoubleMapView;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator _decorator;

 @Test
 public void testEmptyConstructor() {
     _decorator = new TDoubleShortMapDecorator();
     Assert.assertEquals(0, _decorator.size());
     Assert.assertTrue(_decorator.isEmpty());
 }

 @Test
 public void testMapConstructor() {
     _map = new TDoubleShortMap();
     _decorator = new TDoubleShortMapDecorator(_map);
     Assert.assertEquals(_map, _decorator.getMap());
 }

 @Test
 public void testPut() {
     _map = new TDoubleShortMap();
     _decorator = new TDoubleShortMapDecorator(_map);
     _decorator.put(1.0, (short)1);
     _decorator.put(2.0, (short)2);
     Assert.assertEquals(2, _decorator.size());
     Assert.assertEquals((short)1, _decorator.get(1.0));
     Assert.assertEquals((short)2, _decorator.get(2.0));
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullKey() {
     _map = new TDoubleShortMap();
     _decorator = new TDoubleShortMapDecorator(_map);
     _decorator.put(null, (short)1);
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
     _map = new TDoubleShortMap();
     _decorator = new TDoubleShortMapDecorator(_map);
     _decorator.put(1.0, null);
 }

 @Test
 public void testGet() {
     _map = new TDoubleShortMap();
     _decorator = new TDoubleShortMapDecorator(_map);
     _decorator.put(1.0, (short)1);
     _decorator.put(2.0, (short)2);
     Assert.assertEquals((short)1, _decorator.get(1.0));
     Assert.assertEquals(null, _decorator.get(3.0));
 }

 @Test
 public void testGetNullKey() {
     _map = new TDoubleShortMap();
     _decorator = new TDoubleShortMapDecorator(_map);
     Assert.assertEquals(null, _decorator.get(null));
 }

 @Test
 public void testClear() {
     _map = new TDoubleShortMap();
     _decorator = new TDoubleShortMapDecorator(_map);
     _decorator.put(1.0, (short)1);
     _decorator.put(2.0, (short)2);
     _decorator.clear();
     Assert.assertEquals(0, _decorator.size());
 }

 @Test
 public void testRemove() {
     _map = new TDoubleShortMap();
     _decorator = new TDoubleShortMapDecorator(_map);
     _decorator.put(1.0, (short)1);
     _decorator.put(2.0, (short)2);
     _decorator.remove(1.0);
     Assert.assertEquals(1, _decorator.size());
     Assert.assertEquals((short)2, _decorator.get(2.0));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
     _map = new TDoubleShortMap();
     _decorator = new TDoubleShortMapDecorator(_map);
     _decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
     _map = new TDoubleShortMap();
     _decorator = new TDoubleShortMapDecorator(_map);
     _decorator.put(1.0, (short)1);
     _decorator.put(2.0, (short)2);
     TDoubleMapView<Double, Short> view = _decorator.entrySet().view();
     int i = 0;
     for (TDoubleShortMap.Entry<Double, Short> entry : view) {
         Assert.assertEquals(i++, entry.getKey().doubleValue());
         Assert.assertEquals(i++, entry.getValue());
     }
 }

 @Test
 public void testContainsValue() {
     _map = new TDoubleShortMap();
     _decorator = new TDoubleShortMapDecorator(_map);
     _decorator.put(1.0, (short)1);
     _decorator.put(2.0, (short)2);
     Assert.assertTrue(_decorator.containsValue((short)1));
     Assert.assertFalse(_decorator.containsValue((short)3));
 }

 @Test
 public void testContainsKey() {
     _map = new TDoubleShortMap();
     _decorator = new TDoubleShortMapDecorator(_map);
     _decorator.put(1.0, (short)1);
     _decorator.put(2.0, (short)2);
     Assert.assertTrue(_decorator.containsKey(1.0));
     Assert.assertFalse(_decorator.containsKey(3.0));
 }

 @Test
 public void testSize() {
     _map = new TDoubleShortMap();
     _decorator = new TDoubleShortMapDecorator(_map);
     _decorator.put(1.0, (short)1);
     _decorator.put(2.0, (short)2);
     Assert.assertEquals(2, _decorator.size());
 }

 @Test
 public void testIsEmpty() {
     _map = new TDoubleShortMap();
     _decorator = new TDoubleShortMapDecorator(_map);
     Assert.assertTrue(_decorator.isEmpty());
     _decorator.put(1.0, (short)1);
     Assert.assertFalse(_decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
     TDoubleShortMap map1 = new TDoubleShortMap();
     TDoubleShortMapDecorator decorator1 = new TDoubleShortMapDecorator(map1);
     decorator1.put(1.0, (short)1);
     decorator1.put(2.0, (short)2);

     TDoubleShortMap map2 = new TDoubleShortMap();
     TDoubleShortMapDecorator decorator2 = new TDoubleShortMapDecorator(map2);
     decorator2.put(3.0, (short)3);
     decorator2.put(4.0, (short)4);

     TDoubleShortMapDecorator decorator3 = new TDoubleShortMapDecorator();
     decorator3.putAll(decorator1);
     decorator3.putAll(decorator2);

     Assert.assertEquals(4, decorator3.size());
     Assert.assertEquals((short)1, decorator3.get(1.0));
     Assert.assertEquals((short)2, decorator3.get(2.0));
     Assert.assertEquals((short)3, decorator3.get(3.0));
     Assert.assertEquals((short)4, decorator3.get(4.0));
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
     _map = new TDoubleShortMap();
     _decorator = new TDoubleShortMapDecorator(_map);
     _decorator.put(1.0, (short)1);
     _decorator.put(2.0, (short)2);

     ByteArrayOutputStream baos = new ByteArrayOutputStream();
     ObjectOutput out = new ObjectOutputStream(baos);
     _decorator.writeExternal(out);
     out.close();

     ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
     ObjectInput in = new ObjectInputStream(bais);
     TDoubleShortMapDecorator decorator4 = (TDoubleShortMapDecorator)in.readObject();
     in.close();

     Assert.assertEquals(2, decorator4.size());
     Assert.assertEquals((short)1, decorator4.get(1.0));
     Assert.assertEquals((short)2, decorator4.get(2.0));
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
     _map = new TDoubleShortMap();
     _decorator = new TDoubleShortMapDecorator(_map);
     _decorator.put(1.0, (short)1);
     _decorator.put(2.0, (short)2);

     TDoubleShortMapDecorator clone = (TDoubleShortMapDecorator)_decorator.clone();

     Assert.assertEquals(2, clone.size());
     Assert.assertEquals((short)1, clone.get(1.0));
     Assert.assertEquals((short)2, clone.get(2.0));
 }
}