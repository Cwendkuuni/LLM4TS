import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.map.TShortByteMapImpl;
import gnu.trove.iterator.TShortByteIterator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class TShortByteMapDecoratorTest {

 @Test
 public void testConstructor() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator();
 Assert.assertNotNull(decorator);
 }

 @Test
 public void testConstructorWithMap() {
 TShortByteMap map = new TShortByteMapImpl();
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
 Assert.assertNotNull(decorator);
 Assert.assertEquals(map, decorator.getMap());
 }

 @Test
 public void testPut() {
 TShortByteMap map = new TShortByteMapImpl();
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

 Short key1 = Short.valueOf((short) 1);
 Byte value1 = Byte.valueOf((byte) 1);
 decorator.put(key1, value1);

 Short key2 = Short.valueOf((short) 2);
 Byte value2 = Byte.valueOf((byte) 2);
 decorator.put(key2, value2);

 Byte existingValue = decorator.get(key1);
 Assert.assertEquals(value1, existingValue);

 Byte nonExistingValue = decorator.get(Short.valueOf((short) 3));
 Assert.assertNull(nonExistingValue);

 TShortByteMap mapCopy = decorator.getMap();
 TShortByteIterator mapIterator = mapCopy.iterator();
 Map.Entry<Short, Byte> entry = mapIterator.next();
 Assert.assertEquals(key1, entry.getKey());
 Assert.assertEquals(value1, entry.getValue());

 mapIterator = mapCopy.iterator();
 entry = mapIterator.next();
 Assert.assertEquals(key2, entry.getKey());
 Assert.assertEquals(value2, entry.getValue());
 }

 @Test
 public void testClear() {
 TShortByteMap map = new TShortByteMapImpl();
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

 Short key1 = Short.valueOf((short) 1);
 Byte value1 = Byte.valueOf((byte) 1);
 decorator.put(key1, value1);

 decorator.clear();

 Byte nonExistingValue = decorator.get(key1);
 Assert.assertNull(nonExistingValue);

 TShortByteMap mapCopy = decorator.getMap();
 TShortByteIterator mapIterator = mapCopy.iterator();
 Assert.assertFalse(mapIterator.hasNext());
 }

 @Test
 public void testRemove() {
 TShortByteMap map = new TShortByteMapImpl();
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

 Short key1 = Short.valueOf((short) 1);
 Byte value1 = Byte.valueOf((byte) 1);
 decorator.put(key1, value1);

 Short key2 = Short.valueOf((short) 2);
 Byte value2 = Byte.valueOf((byte) 2);
 decorator.put(key2, value2);

 Byte removedValue = decorator.remove(key1);
 Assert.assertEquals(value1, removedValue);

 Byte nonExistingValue = decorator.get(Short.valueOf((short) 1));
 Assert.assertNull(nonExistingValue);

 TShortByteMap mapCopy = decorator.getMap();
 TShortByteIterator mapIterator = mapCopy.iterator();
 Map.Entry<Short, Byte> entry = mapIterator.next();
 Assert.assertEquals(key2, entry.getKey());
 Assert.assertEquals(value2, entry.getValue());
 }

 @Test
 public void testEntrySet() {
 TShortByteMap map = new TShortByteMapImpl();
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

 Short key1 = Short.valueOf((short) 1);
 Byte value1 = Byte.valueOf((byte) 1);
 Short key2 = Short.valueOf((short) 2);
 Byte value2 = Byte.valueOf((byte) 2);

 decorator.put(key1, value1);
 decorator.put(key2, value2);

 Set<Map.Entry<Short, Byte>> entrySet = decorator.entrySet();

 int size = entrySet.size();
 Assert.assertEquals(2, size);

 boolean containsEntry1 = false;
 for (Map.Entry<Short, Byte> entry : entrySet) {
 if (entry.getKey().equals(key1) && entry.getValue().equals(value1)) {
 containsEntry1 = true;
 break;
 }
 }
 Assert.assertTrue(containsEntry1);

 boolean containsEntry2 = false;
 for (Map.Entry<Short, Byte> entry : entrySet) {
 if (entry.getKey().equals(key2) && entry.getValue().equals(value2)) {
 containsEntry2 = true;
 break;
 }
 }
 Assert.assertTrue(containsEntry2);
 }

 @Test
 public void testContainsValue() {
 TShortByteMap map = new TShortByteMapImpl();
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

 Byte value1 = Byte.valueOf((byte) 1);
 Byte value2 = Byte.valueOf((byte) 2);
 decorator.put(Short.valueOf((short) 1), value1);
 decorator.put(Short.valueOf((short) 2), value2);

 boolean containsValue1 = decorator.containsValue(value1);
 Assert.assertTrue(containsValue1);

 boolean containsValue3 = decorator.containsValue(Byte.valueOf((byte) 3));
 Assert.assertFalse(containsValue3);
 }

 @Test
 public void testContainsKey() {
 TShortByteMap map = new TShortByteMapImpl();
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

 Byte value1 = Byte.valueOf((byte) 1);
 Byte value2 = Byte.valueOf((byte) 2);
 decorator.put(Short.valueOf((short) 1), value1);
 decorator.put(Short.valueOf((short) 2), value2);

 boolean containsKey1 = decorator.containsKey(Short.valueOf((short) 1));
 Assert.assertTrue(containsKey1);

 boolean containsKey3 = decorator.containsKey(Short.valueOf((short) 3));
 Assert.assertFalse(containsKey3);
 }

 @Test
 public void testSize() {
 TShortByteMap map = new TShortByteMapImpl();
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

 Byte value1 = Byte.valueOf((byte) 1);
 Byte value2 = Byte.valueOf((byte) 2);
 decorator.put(Short.valueOf((short) 1), value1);
 decorator.put(Short.valueOf((short) 2), value2);

 int size = decorator.size();
 Assert.assertEquals(2, size);
 }

 @Test
 public void testIsEmpty() {
 TShortByteMap map = new TShortByteMapImpl();
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

 int size = decorator.size();
 Assert.assertEquals(0, size);

 boolean isEmpty = decorator.isEmpty();
 Assert.assertTrue(isEmpty);
 }

 @Test
 public void testPutAll() {
 TShortByteMap map = new TShortByteMapImpl();
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

 Short key1 = Short.valueOf((short) 1);
 Byte value1 = Byte.valueOf((byte) 1);
 Short key2 = Short.valueOf((short) 2);
 Byte value2 = Byte.valueOf((byte) 2);

 Map<Short, Byte> anotherMap = new TShortByteMapImpl();
 anotherMap.put(key1, value1);
 anotherMap.put(key2, value2);

 decorator.putAll(anotherMap);

 Byte existingValue1 = decorator.get(key1);
 Assert.assertEquals(value1, existingValue1);

 Byte existingValue2 = decorator.get(key2);
 Assert.assertEquals(value2, existingValue2);

 Byte nonExistingValue = decorator.get(Short.valueOf((short) 3));
 Assert.assertNull(nonExistingValue);
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 TShortByteMap map = new TShortByteMapImpl();
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
 decorator.writeExternal(objectStream);
 objectStream.close();

 byte[] bytes = byteStream.toByteArray();
 ByteArrayInputStream byteInput = new ByteArrayInputStream(bytes);
 ObjectInputStream objectInput = new ObjectInputStream(byteInput);
 TShortByteMapDecorator deserializedDecorator = (TShortByteMapDecorator)objectInput.readObject();
 objectInput.close();

 Assert.assertNotNull(deserializedDecorator);
 TShortByteMap deserializedMap = deserializedDecorator.getMap();
 Assert.assertNotNull(deserializedMap);
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
 TShortByteMap map = new TShortByteMapImpl();
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

 TShortByteMapDecorator clonedDecorator = (TShortByteMapDecorator)decorator.clone();

 Assert.assertNotNull(clonedDecorator);
 Assert.assertNotSame(decorator, clonedDecorator);
 TShortByteMap clonedMap = clonedDecorator.getMap();
 Assert.assertNotSame(map, clonedMap);
 }
}