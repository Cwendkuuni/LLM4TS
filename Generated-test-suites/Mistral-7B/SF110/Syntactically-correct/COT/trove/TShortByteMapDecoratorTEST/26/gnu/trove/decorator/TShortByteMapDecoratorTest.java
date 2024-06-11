import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.TShortByteMapImpl;
import gnu.trove.decorator.TShortByteMapDecorator;
import java.util.Arrays;
import java.util.Collections;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Test
 public void testConstructor() {
     decorator = new TShortByteMapDecorator();
     Assert.assertNotNull(decorator);
     Assert.assertNull(decorator.getMap());
 }

 @Test
 public void testConstructorWithMap() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     Assert.assertNotNull(decorator);
     Assert.assertSame(_map, decorator.getMap());
 }

 @Test
 public void testPut() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     final Short key = Short.valueOf((short)1);
     final Byte value = Byte.valueOf((byte)10);
     final Byte putValue = decorator.put(key, value);
     Assert.assertSame(value, putValue);
     Assert.assertEquals(value, decorator.get(key));
     Assert.assertEquals(Collections.singletonMap(key, value), decorator);
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     final Byte value = Byte.valueOf((byte)10);
     decorator.put(null, value);
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     final Short key = Short.valueOf((short)1);
     decorator.put(key, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithInvalidKey() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     decorator.put((short)Short.MAX_VALUE, (byte)1);
 }

 @Test
 public void testGet() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     final Short key = Short.valueOf((short)1);
     final Byte value = Byte.valueOf((byte)10);
     decorator.put(key, value);
     final Byte getValue = decorator.get(key);
     Assert.assertSame(value, getValue);
     Assert.assertNull(decorator.get(Short.valueOf((short)2)));
 }

 @Test(expected = NullPointerException.class)
 public void testGetWithNullKey() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     decorator.get(null);
 }

 @Test
 public void testClear() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     final Short key1 = Short.valueOf((short)1);
     final Byte value1 = Byte.valueOf((byte)10);
     final Short key2 = Short.valueOf((short)2);
     final Byte value2 = Byte.valueOf((byte)20);
     decorator.put(key1, value1);
     decorator.put(key2, value2);
     decorator.clear();
     Assert.assertEquals(Collections.emptyMap(), decorator);
 }

 @Test
 public void testRemove() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     final Short key = Short.valueOf((short)1);
     final Byte value = Byte.valueOf((byte)10);
     decorator.put(key, value);
     final Byte removedValue = decorator.remove(key);
     Assert.assertSame(value, removedValue);
     Assert.assertNull(decorator.get(key));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     final Short key1 = Short.valueOf((short)1);
     final Byte value1 = Byte.valueOf((byte)10);
     final Short key2 = Short.valueOf((short)2);
     final Byte value2 = Byte.valueOf((byte)20);
     decorator.put(key1, value1);
     decorator.put(key2, value2);
     final Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
     final Entry<Short, Byte> entry1 = entrySet.iterator().next();
     final Entry<Short, Byte> entry2 = Collections.singletonMap(key2, value2).entrySet().iterator().next();
     Assert.assertEquals(entry2, entry1);
 }

 @Test
 public void testContainsValue() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     final Short key = Short.valueOf((short)1);
     final Byte value = Byte.valueOf((byte)10);
     decorator.put(key, value);
     Assert.assertTrue(decorator.containsValue(value));
     Assert.assertFalse(decorator.containsValue(Byte.valueOf((byte)20)));
 }

 @Test
 public void testContainsKey() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     final Short key1 = Short.valueOf((short)1);
     final Byte value1 = Byte.valueOf((byte)10);
     decorator.put(key1, value1);
     Assert.assertTrue(decorator.containsKey(key1));
     Assert.assertFalse(decorator.containsKey(Short.valueOf((short)2)));
 }

 @Test
 public void testSize() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     final Short key1 = Short.valueOf((short)1);
     final Byte value1 = Byte.valueOf((byte)10);
     final Short key2 = Short.valueOf((short)2);
     final Byte value2 = Byte.valueOf((byte)20);
     decorator.put(key1, value1);
     decorator.put(key2, value2);
     Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     Assert.assertTrue(decorator.isEmpty());
     final Short key = Short.valueOf((short)1);
     final Byte value = Byte.valueOf((byte)10);
     decorator.put(key, value);
     Assert.assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     final Map<Short, Byte> map = new TShortByteMapImpl();
     map.put(Short.valueOf((short)1), Byte.valueOf((byte)10));
     map.put(Short.valueOf((short)2), Byte.valueOf((byte)20));
     decorator.putAll(map);
     Assert.assertEquals(map, decorator);
 }

 @Test
 public void testWrapKey() {
     decorator = new TShortByteMapDecorator();
     Assert.assertEquals((short)0, decorator.wrapKey(0));
 }

 @Test
 public void testUnwrapKey() {
     decorator = new TShortByteMapDecorator();
     Assert.assertEquals(Short.valueOf((short)0), decorator.unwrapKey(Short.valueOf((short)0)));
 }

 @Test
 public void testWrapValue() {
     decorator = new TShortByteMapDecorator();
     Assert.assertEquals((byte)0, decorator.wrapValue(0));
 }

 @Test
 public void testUnwrapValue() {
     decorator = new TShortByteMapDecorator();
     Assert.assertEquals(Byte.valueOf((byte)0), decorator.unwrapValue(Byte.valueOf((byte)0)));
 }

 @Test
 public void testSerializable() throws IOException, ClassNotFoundException {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
     final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
     objectOutputStream.writeObject(decorator);
     final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
     final ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
     final TShortByteMapDecorator deserializedDecorator = (TShortByteMapDecorator)objectInputStream.readObject();
     Assert.assertNotNull(deserializedDecorator);
     Assert.assertSame(_map, deserializedDecorator.getMap());
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     final TShortByteMapDecorator clone = (TShortByteMapDecorator)decorator.clone();
     Assert.assertNotNull(clone);
     Assert.assertSame(_map, clone.getMap());
 }
}