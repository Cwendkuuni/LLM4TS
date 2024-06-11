import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.view.TShortByteMapView;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Test
 public void testEmptyMap() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     Assert.assertEquals(0, decorator.size());
     Assert.assertNull(decorator.get(Short.MIN_VALUE));
     Assert.assertFalse(decorator.containsKey(Short.MIN_VALUE));
 }

 @Test
 public void testPut() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     decorator.put((short)1, (byte)1);
     Assert.assertEquals(1, decorator.size());
     Assert.assertEquals((byte)1, decorator.get((short)1));
     Assert.assertEquals((short)1, decorator.keySet().iterator().next());
 }

 @Test
 public void testPutNullKey() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     Assert.assertNull(decorator.put((Short)null, (byte)1));
     Assert.assertEquals(0, decorator.size());
 }

 @Test
 public void testPutNullValue() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     decorator.put((short)1, (Byte)null);
     Assert.assertEquals(1, decorator.size());
     Assert.assertNull(decorator.get((short)1));
 }

 @Test
 public void testPutSameKey() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     decorator.put((short)1, (byte)1);
     decorator.put((short)1, (byte)2);
     Assert.assertEquals(1, decorator.size());
     Assert.assertEquals((byte)2, decorator.get((short)1));
 }

 @Test
 public void testGetNotFound() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     Assert.assertNull(decorator.get((short)-1));
 }

 @Test
 public void testClear() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     decorator.put((short)1, (byte)1);
     decorator.clear();
     Assert.assertEquals(0, decorator.size());
     Assert.assertNull(decorator.get((short)1));
 }

 @Test
 public void testRemove() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     decorator.put((short)1, (byte)1);
     decorator.put((short)2, (byte)2);
     decorator.remove((short)1);
     Assert.assertEquals(1, decorator.size());
     Assert.assertEquals((byte)2, decorator.get((short)2));
 }

 @Test
 public void testRemoveNotFound() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     decorator.remove((short)1);
     Assert.assertEquals(0, decorator.size());
     Assert.assertNull(decorator.get((short)1));
 }

 @Test
 public void testEntrySet() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     decorator.put((short)1, (byte)1);
     decorator.put((short)2, (byte)2);
     TShortByteMapView view = decorator.entrySet().iterator();
     Assert.assertEquals(1, view.size());
     Assert.assertEquals(Short.valueOf((short)1), view.next().getKey());
     Assert.assertEquals(Byte.valueOf((byte)1), view.next().getValue());
 }

 @Test
 public void testContainsValue() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     decorator.put((short)1, (byte)1);
     decorator.put((short)2, (byte)2);
     Assert.assertTrue(decorator.containsValue(Byte.valueOf((byte)1)));
     Assert.assertTrue(decorator.containsValue(Byte.valueOf((byte)2)));
     Assert.assertFalse(decorator.containsValue(Byte.valueOf((byte)3)));
 }

 @Test
 public void testContainsKey() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     decorator.put((short)1, (byte)1);
     decorator.put((short)2, (byte)2);
     Assert.assertTrue(decorator.containsKey(Short.valueOf((short)1)));
     Assert.assertTrue(decorator.containsKey(Short.valueOf((short)2)));
     Assert.assertFalse(decorator.containsKey(Short.valueOf((short)0)));
 }

 @Test
 public void testSize() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     decorator.put((short)1, (byte)1);
     decorator.put((short)2, (byte)2);
     Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     Assert.assertTrue(decorator.isEmpty());
     decorator.put((short)1, (byte)1);
     Assert.assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
     TShortByteMap map = new TShortByteMap();
     map.put((short)1, (byte)1);
     map.put((short)2, (byte)2);
     decorator = new TShortByteMapDecorator();
     decorator.putAll(map);
     Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testExternalizable() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     ByteArrayOutputStream baos = new ByteArrayOutputStream();
     ObjectOutput out = new ObjectOutputStream(baos);
     decorator.writeExternal(out);
     ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
     TShortByteMapDecorator dec = (TShortByteMapDecorator)in.readObject();
     Assert.assertEquals(decorator.size(), dec.size());
 }

 @Test
 public void testCloneable() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     TShortByteMapDecorator cloned = (TShortByteMapDecorator)decorator.clone();
     Assert.assertEquals(decorator.size(), cloned.size());
     Assert.assertNotSame(decorator, cloned);
 }
}