import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.view.TShortByteMapView;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Test
 public void testConstructor() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     Assert.assertNotNull(decorator);
 }

 @Test
 public void testGetMap() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     TShortByteMap actual = decorator.getMap();
     Assert.assertSame(_map, actual);
 }

 @Test
 public void testPut() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     Short key = Short.valueOf((short)1);
     Byte value = Byte.valueOf((byte)1);
     Byte putValue = decorator.put(key, value);
     Assert.assertEquals(value, putValue);
     Byte existingValue = Byte.valueOf((byte)2);
     decorator.put(key, existingValue);
     Byte newPutValue = decorator.put(key, value);
     Assert.assertNull(newPutValue);
 }

 @Test
 public void testGet() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     Short key = Short.valueOf((short)1);
     Byte value = Byte.valueOf((byte)1);
     decorator.put(key, value);
     Byte actual = decorator.get(key);
     Assert.assertEquals(value, actual);
 }

 @Test
 public void testClear() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     decorator.put(Short.valueOf((short)1), Byte.valueOf((byte)1));
     decorator.clear();
     Assert.assertFalse(decorator.isEmpty());
     decorator.clear();
     Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemove() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     Short key = Short.valueOf((short)1);
     Byte value = Byte.valueOf((byte)1);
     decorator.put(key, value);
     Byte removedValue = decorator.remove(key);
     Assert.assertEquals(value, removedValue);
     Assert.assertFalse(decorator.containsKey(key));
 }

 @Test
 public void testEntrySet() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     Iterator<Entry<Short, Byte>> iterator = decorator.entrySet().iterator();
     Assert.assertFalse(iterator.hasNext());
     decorator.put(Short.valueOf((short)1), Byte.valueOf((byte)1));
     decorator.put(Short.valueOf((short)2), Byte.valueOf((byte)2));
     int size = decorator.size();
     while (iterator.hasNext()) {
         Entry<Short, Byte> entry = iterator.next();
         Assert.assertTrue(decorator.containsKey(entry.getKey()));
         Assert.assertEquals(entry.getValue(), decorator.get(entry.getKey()));
         size--;
     }
     Assert.assertEquals(0, size);
 }

 @Test
 public void testContainsValue() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     Byte value = Byte.valueOf((byte)1);
     decorator.put(Short.valueOf((short)1), value);
     decorator.put(Short.valueOf((short)2), Byte.valueOf((byte)2));
     Assert.assertTrue(decorator.containsValue(value));
 }

 @Test
 public void testContainsKey() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     Short key = Short.valueOf((short)1);
     decorator.put(key, Byte.valueOf((byte)1));
     Assert.assertTrue(decorator.containsKey(key));
     Short anotherKey = Short.valueOf((short)2);
     Assert.assertFalse(decorator.containsKey(anotherKey));
 }

 @Test
 public void testSize() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     decorator.put(Short.valueOf((short)1), Byte.valueOf((byte)1));
     decorator.put(Short.valueOf((short)2), Byte.valueOf((byte)2));
     Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     Assert.assertTrue(decorator.isEmpty());
     decorator.put(Short.valueOf((short)1), Byte.valueOf((byte)1));
     Assert.assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     TShortByteMap mapToAdd = new TShortByteMap();
     mapToAdd.put(Short.valueOf((short)1), Byte.valueOf((byte)1));
     mapToAdd.put(Short.valueOf((short)2), Byte.valueOf((byte)2));
     decorator.putAll(mapToAdd);
     Iterator<Short> mapIterator = mapToAdd.keySet().iterator();
     while (mapIterator.hasNext()) {
         Short key = mapIterator.next();
         Byte value = decorator.get(key);
         Assert.assertEquals(value, mapToAdd.get(key));
     }
 }

 @Test
 public void testWrapKey() {
     TShortByteMapDecorator decorator = new TShortByteMapDecorator();
     short key = 1;
     Short wrappedKey = decorator.wrapKey(key);
     Assert.assertEquals(key, wrappedKey.shortValue());
 }

 @Test
 public void testUnwrapKey() {
     TShortByteMapDecorator decorator = new TShortByteMapDecorator();
     Short key = Short.valueOf((short)1);
     short unwrappedKey = decorator.unwrapKey(key);
     Assert.assertEquals(key.shortValue(), unwrappedKey);
 }

 @Test
 public void testWrapValue() {
     TShortByteMapDecorator decorator = new TShortByteMapDecorator();
     byte value = 1;
     Byte wrappedValue = decorator.wrapValue(value);
     Assert.assertEquals(value, wrappedValue.byteValue());
 }

 @Test
 public void testUnwrapValue() {
     TShortByteMapDecorator decorator = new TShortByteMapDecorator();
     Byte value = Byte.valueOf((byte)1);
     byte unwrappedValue = decorator.unwrapValue(value);
     Assert.assertEquals(value.byteValue(), unwrappedValue);
 }

 @Test
 public void testExternalizable() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
     ObjectOutput out = new ObjectOutputStream(byteStream);
     decorator.writeExternal(out);
     byte[] bytes = byteStream.toByteArray();
     ByteArrayInputStream byteInput = new ByteArrayInputStream(bytes);
     ObjectInput in = new ObjectInputStream(byteInput);
     TShortByteMapDecorator deserializedDecorator = new TShortByteMapDecorator();
     deserializedDecorator.readExternal(in);
     Assert.assertNotNull(deserializedDecorator.getMap());
     Assert.assertSame(_map, deserializedDecorator.getMap());
 }

 @Test
 public void testCloneable() {
     _map = new TShortByteMap();
     decorator = new TShortByteMapDecorator(_map);
     TShortByteMapDecorator clonedDecorator = (TShortByteMapDecorator)decorator.clone();
     Assert.assertNotNull(clonedDecorator.getMap());
     Assert.assertNotSame(_map, clonedDecorator.getMap());
 }
}