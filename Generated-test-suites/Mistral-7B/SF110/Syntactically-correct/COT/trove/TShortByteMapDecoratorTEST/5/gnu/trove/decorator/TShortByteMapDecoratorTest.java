import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.view.TShortByteMapView;
import gnu.trove.map.TShortByteMapImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
     Short key = Short.valueOf((short)1);
     Byte value = Byte.valueOf((byte)1);
     Byte putValue = decorator.put(key, value);
     Assert.assertNull(putValue);
     Assert.assertEquals(value, decorator.get(key));
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     Byte putValue = decorator.put((Short)null, (Byte)1);
     Assert.assertNull(putValue);
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     Short key = Short.valueOf((short)1);
     Byte putValue = decorator.put(key, (Byte)null);
     Assert.assertNull(putValue);
 }

 @Test
 public void testGet() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     Short key = Short.valueOf((short)1);
     Byte value = Byte.valueOf((byte)1);
     decorator.put(key, value);
     Assert.assertEquals(value, decorator.get(key));
 }

 @Test(expected = NullPointerException.class)
 public void testGetWithNullKey() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     Assert.assertNull(decorator.get((Short)null));
 }

 @Test
 public void testClear() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     Short key1 = Short.valueOf((short)1);
     Byte value1 = Byte.valueOf((byte)1);
     decorator.put(key1, value1);
     decorator.clear();
     Assert.assertFalse(decorator.containsKey(key1));
 }

 @Test
 public void testRemove() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     Short key1 = Short.valueOf((short)1);
     Byte value1 = Byte.valueOf((byte)1);
     decorator.put(key1, value1);
     Byte removedValue = decorator.remove(key1);
     Assert.assertNotNull(removedValue);
     Assert.assertFalse(decorator.containsKey(key1));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     Assert.assertNull(decorator.remove((Short)null));
 }

 @Test
 public void testEntrySet() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     Short key1 = Short.valueOf((short)1);
     Byte value1 = Byte.valueOf((byte)1);
     decorator.put(key1, value1);
     TShortByteMapView view = decorator.entrySet();
     List<Entry<Short, Byte>> entries = new ArrayList<>(view);
     Assert.assertEquals(1, entries.size());
     Assert.assertEquals(key1, entries.get(0).getKey());
     Assert.assertEquals(value1, entries.get(0).getValue());
 }

 @Test
 public void testContainsValue() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     Short key1 = Short.valueOf((short)1);
     Byte value1 = Byte.valueOf((byte)1);
     decorator.put(key1, value1);
     Assert.assertTrue(decorator.containsValue(value1));
 }

 @Test
 public void testContainsKey() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     Short key1 = Short.valueOf((short)1);
     Byte value1 = Byte.valueOf((byte)1);
     decorator.put(key1, value1);
     Assert.assertTrue(decorator.containsKey(key1));
 }

 @Test
 public void testSize() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     Short key1 = Short.valueOf((short)1);
     Byte value1 = Byte.valueOf((byte)1);
     decorator.put(key1, value1);
     Assert.assertEquals(1, decorator.size());
 }

 @Test
 public void testIsEmpty() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     Short key1 = Short.valueOf((short)1);
     Byte value1 = Byte.valueOf((byte)1);
     Short key2 = Short.valueOf((short)2);
     Byte value2 = Byte.valueOf((byte)2);
     Map<Short, Byte> map = new TShortByteMapImpl();
     map.put(key1, value1);
     map.put(key2, value2);
     decorator.putAll(map);
     Assert.assertEquals(2, decorator.size());
     Assert.assertEquals(value1, decorator.get(key1));
     Assert.assertEquals(value2, decorator.get(key2));
 }

 @Test
 public void testExternalizable() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     ByteArrayOutputStream output = new ByteArrayOutputStream();
     ObjectOutputStream out = new ObjectOutputStream(output);
     decorator.writeExternal(out);
     out.close();
     byte[] bytes = output.toByteArray();
     ByteArrayInputStream input = new ByteArrayInputStream(bytes);
     ObjectInputStream in = new ObjectInputStream(input);
     TShortByteMapDecorator decorator2 = (TShortByteMapDecorator)in.readObject();
     in.close();
     Assert.assertNotNull(decorator2);
     Assert.assertNotNull(decorator2.getMap());
     Assert.assertEquals(decorator.getMap(), decorator2.getMap());
 }

 @Test
 public void testCloneable() {
     _map = new TShortByteMapImpl();
     decorator = new TShortByteMapDecorator(_map);
     TShortByteMapDecorator cloned = (TShortByteMapDecorator)decorator.clone();
     Assert.assertNotNull(cloned);
     Assert.assertNotNull(cloned.getMap());
     Assert.assertEquals(decorator.getMap(), cloned.getMap());
 }

}