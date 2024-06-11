import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.TShortByteMapImpl;
import java.util.Arrays;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Test
 public void testConstructor() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     Assert.assertNotNull(decorator);
 }

 @Test
 public void testGetMap() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     Assert.assertNotNull(decorator.getMap());
 }

 @Test
 public void testPut() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     final Byte value = Byte.valueOf((byte) 1);
     decorator.put((short) 1, value);
     final Byte result = decorator.get((short) 1);
     Assert.assertEquals(value, result);
 }

 @Test
 public void testPutNullKey() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     final Byte value = Byte.valueOf((byte) 1);
     final Short key = Short.valueOf((short) 0);
     decorator.put(key, value);
     Assert.assertNull(decorator.get(null));
 }

 @Test
 public void testPutNullValue() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     final Short key = Short.valueOf((short) 1);
     decorator.put(key, null);
     final Byte result = decorator.get(key);
     Assert.assertNull(result);
 }

 @Test
 public void testGetNullKey() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     Assert.assertNull(decorator.get(null));
 }

 @Test
 public void testClear() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     decorator.put((short) 1, Byte.valueOf((byte) 1));
     decorator.clear();
     final Byte result = decorator.get((short) 1);
     Assert.assertNull(result);
 }

 @Test
 public void testRemove() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     decorator.put((short) 1, Byte.valueOf((byte) 1));
     decorator.remove((short) 1);
     final Byte result = decorator.get((short) 1);
     Assert.assertNull(result);
 }

 @Test
 public void testRemoveNullKey() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     final Byte value = Byte.valueOf((byte) 1);
     decorator.put((short) 1, value);
     final Byte result = decorator.remove(null);
     Assert.assertNull(result);
 }

 @Test
 public void testEntrySet() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     decorator.put((short) 1, Byte.valueOf((byte) 1));
     decorator.put((short) 2, Byte.valueOf((byte) 2));
     final Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
     final Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();
     Assert.assertTrue(iterator.hasNext());
     final Entry<Short, Byte> entry1 = iterator.next();
     Assert.assertEquals((short) 1, entry1.getKey());
     Assert.assertEquals(Byte.valueOf((byte) 1), entry1.getValue());
     Assert.assertTrue(iterator.hasNext());
     final Entry<Short, Byte> entry2 = iterator.next();
     Assert.assertEquals((short) 2, entry2.getKey());
     Assert.assertEquals(Byte.valueOf((byte) 2), entry2.getValue());
     Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testContainsValue() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     decorator.put((short) 1, Byte.valueOf((byte) 1));
     decorator.put((short) 2, Byte.valueOf((byte) 2));
     Assert.assertTrue(decorator.containsValue(Byte.valueOf((byte) 1)));
     Assert.assertTrue(decorator.containsValue(Byte.valueOf((byte) 2)));
     Assert.assertFalse(decorator.containsValue(Byte.valueOf((byte) 3)));
 }

 @Test
 public void testContainsKey() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     decorator.put((short) 1, Byte.valueOf((byte) 1));
     decorator.put((short) 2, Byte.valueOf((byte) 2));
     Assert.assertTrue(decorator.containsKey((short) 1));
     Assert.assertTrue(decorator.containsKey((short) 2));
     Assert.assertFalse(decorator.containsKey((short) 3));
 }

 @Test
 public void testSize() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     decorator.put((short) 1, Byte.valueOf((byte) 1));
     decorator.put((short) 2, Byte.valueOf((byte) 2));
     Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     Assert.assertTrue(decorator.isEmpty());
     decorator.put((short) 1, Byte.valueOf((byte) 1));
     Assert.assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     final TShortByteMap map = new TShortByteMapImpl();
     map.put(Short.valueOf((short) 1), Byte.valueOf((byte) 1));
     map.put(Short.valueOf((short) 2), Byte.valueOf((byte) 2));
     decorator.putAll(map);
     final Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
     final Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();
     Assert.assertTrue(iterator.hasNext());
     final Entry<Short, Byte> entry1 = iterator.next();
     Assert.assertEquals(Short.valueOf((short) 1), entry1.getKey());
     Assert.assertEquals(Byte.valueOf((byte) 1), entry1.getValue());
     Assert.assertTrue(iterator.hasNext());
     final Entry<Short, Byte> entry2 = iterator.next();
     Assert.assertEquals(Short.valueOf((short) 2), entry2.getKey());
     Assert.assertEquals(Byte.valueOf((byte) 2), entry2.getValue());
     Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     decorator.put((short) 1, Byte.valueOf((byte) 1));
     decorator.put((short) 2, Byte.valueOf((byte) 2));
     final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
     final ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
     objStream.writeObject(decorator);
     objStream.close();
     final ObjectInputStream objInputStream = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
     final TShortByteMapDecorator deserialized = (TShortByteMapDecorator) objInputStream.readObject();
     objInputStream.close();
     final Set<Entry<Short, Byte>> entrySet = deserialized.entrySet();
     final Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();
     Assert.assertTrue(iterator.hasNext());
     final Entry<Short, Byte> entry1 = iterator.next();
     Assert.assertEquals(Short.valueOf((short) 1), entry1.getKey());
     Assert.assertEquals(Byte.valueOf((byte) 1), entry1.getValue());
     Assert.assertTrue(iterator.hasNext());
     final Entry<Short, Byte> entry2 = iterator.next();
     Assert.assertEquals(Short.valueOf((short) 2), entry2.getKey());
     Assert.assertEquals(Byte.valueOf((byte) 2), entry2.getValue());
     Assert.assertFalse(iterator.hasNext());
 }
}