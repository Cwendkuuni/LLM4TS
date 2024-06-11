import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.view.Testers;
import gnu.trove.map.view.MapView;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Test
 public void testEmptyMap() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);
     Assert.assertEquals(0, decorator.size());
     Assert.assertTrue(decorator.isEmpty());
     Assert.assertNull(decorator.get(null));
     Assert.assertNull(decorator.get(new Double(0.0)));
 }

 @Test
 public void testPut() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     decorator.put(null, (short) 0);
     Assert.assertEquals(1, decorator.size());
     Assert.assertFalse(decorator.isEmpty());
     Assert.assertEquals((short) 0, decorator.get(null));

     decorator.put(new Double(1.0), (short) 1);
     Assert.assertEquals(2, decorator.size());
     Assert.assertEquals((short) 1, decorator.get(new Double(1.0)));
     Assert.assertEquals((short) 0, decorator.get(null));
 }

 @Test
 public void testPutNullKey() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     decorator.put(null, (short) 0);
     decorator.put(new Double(1.0), (short) 1);

     try {
         decorator.put(null, (short) 2);
         Assert.fail("Expected IllegalArgumentException");
     } catch (IllegalArgumentException e) {
         // Expected
     }
 }

 @Test
 public void testGetNullKey() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     decorator.put(new Double(1.0), (short) 1);

     Assert.assertNull(decorator.get(null));
     Assert.assertEquals((short) 1, decorator.get(new Double(1.0)));
 }

 @Test
 public void testClear() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     decorator.put(new Double(1.0), (short) 1);
     decorator.put(new Double(2.0), (short) 2);

     decorator.clear();

     Assert.assertEquals(0, decorator.size());
     Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemove() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     decorator.put(new Double(1.0), (short) 1);
     decorator.put(new Double(2.0), (short) 2);

     decorator.remove(new Double(1.0));

     Assert.assertEquals(1, decorator.size());
     Assert.assertFalse(decorator.isEmpty());
     Assert.assertEquals((short) 2, decorator.get(new Double(2.0)));
     Assert.assertNull(decorator.get(new Double(1.0)));
 }

 @Test
 public void testRemoveNullKey() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     decorator.put(new Double(1.0), (short) 1);
     decorator.put(new Double(2.0), (short) 2);

     decorator.remove(null);

     Assert.assertEquals(2, decorator.size());
     Assert.assertFalse(decorator.isEmpty());
     Assert.assertEquals((short) 1, decorator.get(new Double(1.0)));
     Assert.assertEquals((short) 2, decorator.get(new Double(2.0)));
 }

 @Test
 public void testEntrySet() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     decorator.put(new Double(1.0), (short) 1);
     decorator.put(new Double(2.0), (short) 2);

     MapView<Double, Short> view = decorator.entrySet().asMap();

     Assert.assertEquals(2, view.size());
     Assert.assertEquals(1.0, view.keySet().iterator().next(), 0.0);
     Assert.assertEquals(1, view.get(1.0));
     Assert.assertEquals(2.0, view.keySet().iterator().next(), 0.0);
     Assert.assertEquals(2, view.get(2.0));
 }

 @Test
 public void testContainsValue() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     decorator.put(new Double(1.0), (short) 1);
     decorator.put(new Double(2.0), (short) 2);

     Assert.assertTrue(decorator.containsValue((short) 1));
     Assert.assertTrue(decorator.containsValue((short) 2));
     Assert.assertFalse(decorator.containsValue((short) 3));
 }

 @Test
 public void testContainsKey() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     decorator.put(new Double(1.0), (short) 1);
     decorator.put(new Double(2.0), (short) 2);

     Assert.assertTrue(decorator.containsKey(new Double(1.0)));
     Assert.assertTrue(decorator.containsKey(new Double(2.0)));
     Assert.assertFalse(decorator.containsKey(new Double(3.0)));
 }

 @Test
 public void testSize() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     decorator.put(new Double(1.0), (short) 1);
     decorator.put(new Double(2.0), (short) 2);

     Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     Assert.assertTrue(decorator.isEmpty());
     decorator.put(new Double(1.0), (short) 1);
     Assert.assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
     TDoubleShortMap map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(map);

     Map<Double, Short> anotherMap = new TDoubleShortMap();
     anotherMap.put(new Double(1.0), (short) 1);
     anotherMap.put(new Double(2.0), (short) 2);

     decorator.putAll(anotherMap);

     Assert.assertEquals(2, decorator.size());
     Assert.assertEquals((short) 1, decorator.get(new Double(1.0)));
     Assert.assertEquals((short) 2, decorator.get(new Double(2.0)));
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);

     ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
     ObjectOutput out = new ObjectOutputStream(byteStream);
     out.writeObject(decorator);
     out.close();

     ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
     ObjectInput in = new ObjectInputStream(byteInput);
     TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator)in.readObject();
     in.close();

     Assert.assertEquals(0, deserializedDecorator.size());
     Assert.assertTrue(deserializedDecorator.isEmpty());

     _map.put(new Double(1.0), (short) 1);
     decorator = new TDoubleShortMapDecorator(_map);

     byteStream = new ByteArrayOutputStream();
     out = new ObjectOutputStream(byteStream);
     out.writeObject(decorator);
     out.close();

     byteInput = new ByteArrayInputStream(byteStream.toByteArray());
     in = new ObjectInputStream(byteInput);
     TDoubleShortMapDecorator deserializedDecorator2 = (TDoubleShortMapDecorator)in.readObject();
     in.close();

     Assert.assertEquals(1, deserializedDecorator2.size());
     Assert.assertEquals((short) 1, deserializedDecorator2.get(new Double(1.0)));
 }

 @Test
 public void testWriteExternal() throws IOException {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);
     decorator.put(new Double(1.0), (short) 1);
     decorator.put(new Double(2.0), (short) 2);

     ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
     ObjectOutput out = new ObjectOutputStream(byteStream);
     decorator.writeExternal(out);
     out.close();

     byte[] data = byteStream.toByteArray();

     ByteArrayInputStream byteInput = new ByteArrayInputStream(data);
     ObjectInput in = new ObjectInputStream(byteInput);
     TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator)in.readObject();
     in.close();

     Assert.assertEquals(2, deserializedDecorator.size());
     Assert.assertEquals((short) 1, deserializedDecorator.get(new Double(1.0)));
     Assert.assertEquals((short) 2, deserializedDecorator.get(new Double(2.0)));
 }
}