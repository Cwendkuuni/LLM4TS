import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.TShortByteMapImpl;
import gnu.trove.map.view.TShortByteMapView;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Test
 public void testEmptyMap() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);

     Assert.assertEquals(0, decorator.size());
     Assert.assertNull(decorator.get(Short.MIN_VALUE));
     Assert.assertFalse(decorator.containsKey(Short.MIN_VALUE));
     Assert.assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutGetAndRemove() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);

     decorator.put((short) 1, (byte) 1);
     decorator.put((short) 2, (byte) 2);

     Assert.assertEquals((byte) 1, decorator.get((short) 1));
     Assert.assertEquals((byte) 2, decorator.get((short) 2));
     Assert.assertNull(decorator.get((short) 0));

     decorator.remove((short) 1);
     Assert.assertEquals((byte) 2, decorator.get((short) 2));
     Assert.assertNull(decorator.get((short) 1));
     Assert.assertEquals(1, decorator.size());
 }

 @Test
 public void testPutNullKey() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);

     Assert.assertNull(decorator.put((short) null, (byte) 1));
     Assert.assertEquals(0, decorator.size());
 }

 @Test
 public void testPutNullValue() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);

     Assert.assertEquals((byte) 0, decorator.put((short) 1, null));
     Assert.assertEquals(1, decorator.size());
     Assert.assertNull(decorator.get((short) 1));
 }

 @Test
 public void testEntrySet() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);

     decorator.put((short) 1, (byte) 1);
     decorator.put((short) 2, (byte) 2);

     TShortByteMapView view = decorator.entrySet().iterator().next().getValue();
     Assert.assertEquals(view.getClass(), TShortByteMapDecorator.class);
     Assert.assertEquals(view.size(), 2);
 }

 @Test
 public void testContainsValue() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);

     decorator.put((short) 1, (byte) 1);
     decorator.put((short) 2, (byte) 1);

     Assert.assertTrue(decorator.containsValue((byte) 1));
     Assert.assertFalse(decorator.containsValue((byte) 0));
 }

 @Test
 public void testClear() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);

     decorator.put((short) 1, (byte) 1);
     decorator.put((short) 2, (byte) 2);
     decorator.clear();

     Assert.assertEquals(0, decorator.size());
     Assert.assertNull(decorator.get((short) 1));
     Assert.assertNull(decorator.get((short) 2));
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     ByteArrayOutputStream baos = new ByteArrayOutputStream();
     ObjectOutput out = new ObjectOutputStream(baos);

     decorator.writeExternal(out);
     byte[] bytes = baos.toByteArray();
     ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes));
     TShortByteMapDecorator deserialized = (TShortByteMapDecorator)in.readObject();
     Assert.assertEquals(decorator.size(), deserialized.size());
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     TShortByteMapDecorator cloned = (TShortByteMapDecorator)decorator.clone();

     decorator.put((short) 1, (byte) 1);
     cloned.put((short) 2, (byte) 2);

     Assert.assertNotSame(decorator, cloned);
     Assert.assertEquals(decorator.size(), cloned.size());
 }
}