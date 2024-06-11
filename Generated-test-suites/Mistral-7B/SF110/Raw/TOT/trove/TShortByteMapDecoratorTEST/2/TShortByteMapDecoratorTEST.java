import org.junit.Assert.*;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.view.TShortByteMapView;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Test
 public void testConstructorWithEmptyMap() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     assertEquals(0, decorator.size());
     assertFalse(decorator.isEmpty());
     assertNull(decorator.get(Short.MIN_VALUE));
     assertNull(decorator.get(Short.MAX_VALUE));
 }

 @Test
 public void testConstructorWithPopulatedMap() {
     _map = new TShortByteMap();
     _map.put((short)1, (byte)1);
     _map.put((short)2, (byte)2);
     decorator = new TShortByteMapDecorator(_map);
     assertEquals(2, decorator.size());
     assertFalse(decorator.isEmpty());
     assertEquals((byte)1, decorator.get((short)1));
     assertEquals((byte)2, decorator.get((short)2));
     assertNull(decorator.get(Short.MIN_VALUE));
     assertNull(decorator.get(Short.MAX_VALUE));
 }

 @Test
 public void testPut() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     assertEquals(0, decorator.size());
     assertFalse(decorator.isEmpty());
     assertNull(decorator.get(Short.MIN_VALUE));
     assertNull(decorator.get(Short.MAX_VALUE));

     Short key = Short.valueOf((short)1);
     Byte value = Byte.valueOf((byte)1);
     Byte putValue = decorator.put(key, value);
     assertEquals(1, decorator.size());
     assertFalse(decorator.isEmpty());
     assertEquals(value, decorator.get(key));
     assertEquals(null, putValue);
 }

 @Test
 public void testPutWithNullKey() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     assertEquals(0, decorator.size());
     assertFalse(decorator.isEmpty());
     assertNull(decorator.get(Short.MIN_VALUE));
     assertNull(decorator.get(Short.MAX_VALUE));

     Byte value = Byte.valueOf((byte)1);
     Byte putValue = decorator.put((short)0, value);
     assertEquals(1, decorator.size());
     assertFalse(decorator.isEmpty());
     assertEquals(value, decorator.get((short)0));
     assertEquals(null, putValue);
 }

 @Test
 public void testPutWithNullValue() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     assertEquals(0, decorator.size());
     assertFalse(decorator.isEmpty());
     assertNull(decorator.get(Short.MIN_VALUE));
     assertNull(decorator.get(Short.MAX_VALUE));

     Short key = Short.valueOf((short)1);
     Byte putValue = decorator.put(key, null);
     assertEquals(1, decorator.size());
     assertFalse(decorator.isEmpty());
     assertNull(decorator.get(key));
     assertNull(putValue);
 }

 @Test
 public void testGet() {
     _map = new TShortByteMap();
     _map.put((short)1, (byte)1);
     decorator = new TShortByteMapDecorator(_map);
     assertEquals(1, decorator.size());
     assertFalse(decorator.isEmpty());
     assertEquals((byte)1, decorator.get((short)1));
     assertNull(decorator.get(Short.MIN_VALUE));
     assertNull(decorator.get(Short.MAX_VALUE));
 }

 @Test
 public void testGetWithNullKey() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     assertEquals(0, decorator.size());
     assertFalse(decorator.isEmpty());
     assertNull(decorator.get(Short.MIN_VALUE));
     assertNull(decorator.get(Short.MAX_VALUE));

     Byte value = Byte.valueOf((byte)1);
     decorator.put((short)0, value);
     assertNull(decorator.get(null));
     assertEquals(value, decorator.get((short)0));
 }

 @Test
 public void testClear() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     assertEquals(0, decorator.size());
     assertFalse(decorator.isEmpty());
     assertNull(decorator.get(Short.MIN_VALUE));
     assertNull(decorator.get(Short.MAX_VALUE));

     Short key = Short.valueOf((short)1);
     Byte value = Byte.valueOf((byte)1);
     Byte putValue = decorator.put(key, value);
     assertEquals(1, decorator.size());
     assertFalse(decorator.isEmpty());
     assertEquals(value, decorator.get(key));
     assertEquals(null, putValue);

     decorator.clear();
     assertEquals(0, decorator.size());
     assertTrue(decorator.isEmpty());
     assertNull(decorator.get(Short.MIN_VALUE));
     assertNull(decorator.get(Short.MAX_VALUE));
 }

 @Test
 public void testRemove() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     assertEquals(0, decorator.size());
     assertFalse(decorator.isEmpty());
     assertNull(decorator.get(Short.MIN_VALUE));
     assertNull(decorator.get(Short.MAX_VALUE));

     Short key = Short.valueOf((short)1);
     Byte value = Byte.valueOf((byte)1);
     Byte putValue = decorator.put(key, value);
     assertEquals(1, decorator.size());
     assertFalse(decorator.isEmpty());
     assertEquals(value, decorator.get(key));
     assertEquals(null, putValue);

     Byte removedValue = decorator.remove(key);
     assertEquals(0, decorator.size());
     assertTrue(decorator.isEmpty());
     assertNull(decorator.get(Short.MIN_VALUE));
     assertNull(decorator.get(Short.MAX_VALUE));
     assertEquals(value, removedValue);
 }

 @Test
 public void testRemoveWithNullKey() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     assertEquals(0, decorator.size());
     assertFalse(decorator.isEmpty());
     assertNull(decorator.get(Short.MIN_VALUE));
     assertNull(decorator.get(Short.MAX_VALUE));

     Short key = Short.valueOf((short)1);
     Byte value = Byte.valueOf((byte)1);
     Byte putValue = decorator.put(key, value);
     assertEquals(1, decorator.size());
     assertFalse(decorator.isEmpty());
     assertEquals(value, decorator.get(key));
     assertEquals(null, putValue);

     assertNull(decorator.remove(null));
     assertEquals(1, decorator.size());
     assertFalse(decorator.isEmpty());
     assertEquals(value, decorator.get(key));
 }

 @Test
 public void testEntrySet() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     assertEquals(0, decorator.size());
     assertFalse(decorator.isEmpty());
     assertFalse(decorator.containsValue((byte)1));
     assertFalse(decorator.containsKey((short)1));

     Short key = Short.valueOf((short)1);
     Byte value = Byte.valueOf((byte)1);
     decorator.put(key, value);
     TShortByteMapView view = decorator.entrySet().iterator().next().getValue();
     assertEquals(value, view.get());
     assertEquals(key, view.getKey());
 }

 @Test
 public void testEntrySetWithNullKey() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     assertEquals(0, decorator.size());
     assertFalse(decorator.isEmpty());
     assertFalse(decorator.containsValue((byte)1));
     assertFalse(decorator.containsKey((short)1));

     Byte value = Byte.valueOf((byte)1);
     decorator.put((short)0, value);
     decorator.remove((short)0);
     TShortByteMapView view = decorator.entrySet().iterator().next().getValue();
     assertNull(view);
 }

 @Test
 public void testContainsValue() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     assertEquals(0, decorator.size());
     assertFalse(decorator.isEmpty());
     assertFalse(decorator.containsValue((byte)1));

     Short key = Short.valueOf((short)1);
     Byte value = Byte.valueOf((byte)1);
     decorator.put(key, value);
     assertTrue(decorator.containsValue(value));
 }

 @Test
 public void testContainsKey() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     assertEquals(0, decorator.size());
     assertFalse(decorator.isEmpty());
     assertFalse(decorator.containsKey((short)1));

     Short key = Short.valueOf((short)1);
     Byte value = Byte.valueOf((byte)1);
     decorator.put(key, value);
     assertTrue(decorator.containsKey(key));
 }

 @Test
 public void testSize() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     assertEquals(0, decorator.size());
     assertFalse(decorator.isEmpty());

     Short key = Short.valueOf((short)1);
     Byte value = Byte.valueOf((byte)1);
     decorator.put(key, value);
     assertEquals(1, decorator.size());
 }

 @Test
 public void testIsEmpty() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     assertTrue(decorator.isEmpty());

     Short key = Short.valueOf((short)1);
     Byte value = Byte.valueOf((byte)1);
     decorator.put(key, value);
     assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     assertEquals(0, decorator.size());
     assertFalse(decorator.isEmpty());

     TShortByteMap source = new TShortByteMap();
     source.put((short)1, (byte)1);
     source.put((short)2, (byte)2);

     decorator.putAll(source);
     assertEquals(2, decorator.size());
     assertFalse(decorator.isEmpty());
     assertEquals((byte)1, decorator.get((short)1));
     assertEquals((byte)2, decorator.get((short)2));
 }

 @Test
 public void testWriteExternal() throws IOException, ClassNotFoundException {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     assertEquals(0, decorator.size());
     assertFalse(decorator.isEmpty());

     Short key = Short.valueOf((short)1);
     Byte value = Byte.valueOf((byte)1);
     decorator.put(key, value);
     assertEquals(1, decorator.size());
     assertFalse(decorator.isEmpty());

     ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
     ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
     decorator.writeExternal(objStream);
     objStream.close();
     byte[] data = byteStream.toByteArray();
     ByteArrayInputStream byteInput = new ByteArrayInputStream(data);
     ObjectInputStream objInput = new ObjectInputStream(byteInput);
     TShortByteMapDecorator newDecorator = (TShortByteMapDecorator)objInput.readObject();
     objInput.close();

     assertEquals(1, newDecorator.size());
     assertFalse(newDecorator.isEmpty());
     assertEquals(value, newDecorator.get(key));
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
     ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
     ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
     objStream.writeByte(0);
     objStream.writeObject(new TShortByteMap());
     objStream.close();
     byte[] data = byteStream.toByteArray();
     ByteArrayInputStream byteInput = new ByteArrayInputStream(data);
     ObjectInputStream objInput = new ObjectInputStream(byteInput);
     TShortByteMapDecorator decorator = (TShortByteMapDecorator)objInput.readObject();
     objInput.close();

     assertEquals(0, decorator.size());
     assertFalse(decorator.isEmpty());
     assertNull(decorator.get(Short.MIN_VALUE));
     assertNull(decorator.get(Short.MAX_VALUE));
 }
}