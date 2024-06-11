import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.view.TShortByteMapView;
import gnu.trove.map.view.TShortByteMapView.Entry;
import gnu.trove.decorator.TShortByteMapDecorator;

public class TShortByteMapDecoratorTest {

 private TShortByteMap map;
 private TShortByteMapDecorator decorator;

 @Test
 public void testEmptyMap() {
 decorator = new TShortByteMapDecorator();
 Assert.assertEquals(0, decorator.size());
 }

 @Test
 public void testPutGet() {
 map = TShortByteMap.emptyMap();
 decorator = new TShortByteMapDecorator(map);
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 Byte value = decorator.get((short) 1);
 Assert.assertEquals((byte) 1, value);
 value = decorator.get((short) 2);
 Assert.assertEquals((byte) 2, value);
 }

 @Test
 public void testPutNullKey() {
 map = TShortByteMap.emptyMap();
 decorator = new TShortByteMapDecorator(map);
 decorator.put((short) 1, (byte) 1);

 Assert.assertNull(decorator.get((Short) null));
 }

 @Test
 public void testPutNullValue() {
 map = TShortByteMap.emptyMap();
 decorator = new TShortByteMapDecorator(map);
 decorator.put((short) 1, (byte) 1);

 decorator.put((short) 1, null);
 Assert.assertNull(decorator.get((short) 1));
 }

 @Test
 public void testClear() {
 map = TShortByteMap.emptyMap();
 decorator = new TShortByteMapDecorator(map);
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);
 decorator.clear();

 Assert.assertEquals(0, decorator.size());
 Assert.assertNull(decorator.get((short) 1));
 Assert.assertNull(decorator.get((short) 2));
 }

 @Test
 public void testRemove() {
 map = TShortByteMap.emptyMap();
 decorator = new TShortByteMapDecorator(map);
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 Byte value = decorator.remove((short) 1);
 Assert.assertEquals((byte) 1, value);
 Assert.assertEquals(1, decorator.size());
 value = decorator.remove((short) 2);
 Assert.assertNull(value);
 Assert.assertEquals(0, decorator.size());
 }

 @Test
 public void testRemoveNullKey() {
 map = TShortByteMap.emptyMap();
 decorator = new TShortByteMapDecorator(map);
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 Assert.assertNull(decorator.remove((Short) null));
 }

 @Test
 public void testIterator() {
 map = TShortByteMap.emptyMap();
 decorator = new TShortByteMapDecorator(map);
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 TShortByteIterator iterator = decorator.entrySet().iterator();
 Entry<Short, Byte> entry = iterator.next();
 Assert.assertEquals((short) 1, entry.getKey().shortValue());
 Assert.assertEquals((byte) 1, entry.getValue().byteValue());

 entry = iterator.next();
 Assert.assertEquals((short) 2, entry.getKey().shortValue());
 Assert.assertEquals((byte) 2, entry.getValue().byteValue());

 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testEntrySet() {
 map = TShortByteMap.emptyMap();
 decorator = new TShortByteMapDecorator(map);
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 TShortByteMapView view = decorator.entrySet().asMap();
 Assert.assertEquals(2, view.size());
 Assert.assertEquals((byte) 1, view.get((short) 1).getValue().byteValue());
 Assert.assertEquals((byte) 2, view.get((short) 2).getValue().byteValue());
 }

 @Test
 public void testContainsValue() {
 map = TShortByteMap.emptyMap();
 decorator = new TShortByteMapDecorator(map);
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 1);
 decorator.put((short) 3, (byte) 2);

 Assert.assertTrue(decorator.containsValue((byte) 1));
 Assert.assertFalse(decorator.containsValue((byte) 3));
 }

 @Test
 public void testContainsKey() {
 map = TShortByteMap.emptyMap();
 decorator = new TShortByteMapDecorator(map);
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 Assert.assertTrue(decorator.containsKey((short) 1));
 Assert.assertTrue(decorator.containsKey((short) 2));
 Assert.assertFalse(decorator.containsKey((short) 3));
 }

 @Test
 public void testSize() {
 map = TShortByteMap.emptyMap();
 decorator = new TShortByteMapDecorator(map);
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);
 decorator.put((short) 3, (byte) 3);

 Assert.assertEquals(3, decorator.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddEntry() {
 map = TShortByteMap.emptyMap();
 decorator = new TShortByteMapDecorator(map);
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 decorator.entrySet().add(new Entry<Short, Byte>(null, null));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveEntry() {
 map = TShortByteMap.emptyMap();
 decorator = new TShortByteMapDecorator(map);
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 decorator.entrySet().remove(new Entry<Short, Byte>(null, null));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClearEntrySet() {
 map = TShortByteMap.emptyMap();
 decorator = new TShortByteMapDecorator(map);
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 decorator.entrySet().clear();
 }

 @Test
 public void testExternalizable() {
 map = TShortByteMap.emptyMap();
 decorator = new TShortByteMapDecorator(map);
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
 decorator.writeExternal(objStream);
 objStream.close();

 byte[] bytes = byteStream.toByteArray();
 ByteArrayInputStream byteStream2 = new ByteArrayInputStream(bytes);
 ObjectInputStream objStream2 = new ObjectInputStream(byteStream2);
 TShortByteMapDecorator decorator2 = (TShortByteMapDecorator)objStream2.readObject();
 objStream2.close();

 Assert.assertEquals(decorator.size(), decorator2.size());
 Assert.assertEquals(decorator.get((short) 1), decorator2.get((short) 1));
 Assert.assertEquals(decorator.get((short) 2), decorator2.get((short) 2));
 }
}