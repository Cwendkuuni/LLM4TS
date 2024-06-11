import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.view.TShortByteMapView;
import gnu.trove.map.view.TShortByteMapView.Entry;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Test
 public void testConstructor() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     Assert.assertEquals(_map, decorator.getMap());
 }

 @Test
 public void testPut() {
     _map.put((short)1, (byte)1);
     decorator = new TShortByteMapDecorator(_map);
     Assert.assertEquals((byte)1, decorator.put((short)1, (byte)2));
     Assert.assertEquals((byte)1, decorator.get((short)1));
 }

 @Test
 public void testPutNullKey() {
     decorator = new TShortByteMapDecorator();
     Assert.assertNull(decorator.put((Short)null, (Byte)1));
 }

 @Test
 public void testPutNullValue() {
     _map.put((short)1, (byte)1);
     decorator = new TShortByteMapDecorator(_map);
     Assert.assertEquals((byte)1, decorator.put((short)1, (Short)null));
     Assert.assertNull(decorator.get((short)1));
 }

 @Test
 public void testGetNullKey() {
     decorator = new TShortByteMapDecorator();
     Assert.assertNull(decorator.get((Short)null));
 }

 @Test
 public void testGet() {
     _map.put((short)1, (byte)1);
     decorator = new TShortByteMapDecorator(_map);
     Assert.assertEquals((byte)1, decorator.get((short)1));
 }

 @Test
 public void testClear() {
     _map.put((short)1, (byte)1);
     _map.put((short)2, (byte)2);
     decorator = new TShortByteMapDecorator(_map);
     decorator.clear();
     Assert.assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
     _map.put((short)1, (byte)1);
     _map.put((short)2, (byte)2);
     decorator = new TShortByteMapDecorator(_map);
     Assert.assertEquals((byte)1, decorator.remove((short)1));
     Assert.assertEquals((byte)2, decorator.get((short)2));
 }

 @Test
 public void testRemoveNullKey() {
     _map.put((short)1, (byte)1);
     decorator = new TShortByteMapDecorator(_map);
     Assert.assertNull(decorator.remove((Short)null));
 }

 @Test
 public void testSize() {
     _map.put((short)1, (byte)1);
     _map.put((short)2, (byte)2);
     decorator = new TShortByteMapDecorator(_map);
     Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
     decorator = new TShortByteMapDecorator();
     Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
     TShortByteMap map = new TShortByteMap();
     map.put((short)1, (byte)1);
     map.put((short)2, (byte)2);
     decorator = new TShortByteMapDecorator();
     decorator.putAll(map);
     TShortByteMapView view = decorator.entrySet().iterator().next().getValue();
     Assert.assertEquals((byte)1, view.getValue());
     Assert.assertEquals((short)1, view.getKey());
 }

 @Test
 public void testContainsValue() {
     _map.put((short)1, (byte)1);
     decorator = new TShortByteMapDecorator(_map);
     Assert.assertTrue(decorator.containsValue((byte)1));
 }

 @Test
 public void testContainsKey() {
     _map.put((short)1, (byte)1);
     decorator = new TShortByteMapDecorator(_map);
     Assert.assertTrue(decorator.containsKey((short)1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddEntrySet() {
     decorator = new TShortByteMapDecorator();
     decorator.entrySet().add(new Entry((short)1, (byte)1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveEntrySet() {
     _map.put((short)1, (byte)1);
     decorator = new TShortByteMapDecorator(_map);
     decorator.entrySet().remove(new Entry((short)1, (byte)1));
 }

 @Test
 public void testEntrySetIterator() {
     _map.put((short)1, (byte)1);
     _map.put((short)2, (byte)2);
     decorator = new TShortByteMapDecorator(_map);
     TShortByteIterator entryIterator = decorator.entrySet().iterator();
     Assert.assertEquals((short)1, entryIterator.nextKey());
     Assert.assertEquals((byte)1, entryIterator.nextValue());
     Assert.assertEquals((short)2, entryIterator.nextKey());
     Assert.assertEquals((byte)2, entryIterator.nextValue());
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
     _map.put((short)1, (byte)1);
     decorator = new TShortByteMapDecorator(_map);
     ByteArrayOutputStream baos = new ByteArrayOutputStream();
     ObjectOutput out = new ObjectOutputStream(baos);
     decorator.writeExternal(out);
     ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
     TShortByteMapDecorator deserializedDecorator = new TShortByteMapDecorator();
     deserializedDecorator.readExternal(in);
     Assert.assertEquals(_map, deserializedDecorator.getMap());
     Assert.assertEquals((byte)1, deserializedDecorator.get((short)1));
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
     _map.put((short)1, (byte)1);
     decorator = new TShortByteMapDecorator(_map);
     TShortByteMapDecorator clonedDecorator = (TShortByteMapDecorator)decorator.clone();
     Assert.assertEquals(_map, clonedDecorator.getMap());
     Assert.assertEquals((byte)1, clonedDecorator.get((short)1));
 }
}