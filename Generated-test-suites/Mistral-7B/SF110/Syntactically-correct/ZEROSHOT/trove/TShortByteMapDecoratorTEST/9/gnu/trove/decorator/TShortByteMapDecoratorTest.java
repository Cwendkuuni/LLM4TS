import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.view.TShortByteMapView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ByteArrayInputStream;
import java.util.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class TShortByteMapDecoratorTest {

 private TShortByteMapDecorator testMap;
 private TShortByteMap underMap;

 @Test
 public void testEmptyMap() {
 testMap = new TShortByteMapDecorator();
 Assert.assertEquals(0, testMap.size());
 Assert.assertTrue(testMap.isEmpty());
 }

 @Test
 public void testMapConstructor() {
 underMap = new TShortByteOpenHashMap(10);
 testMap = new TShortByteMapDecorator(underMap);
 Assert.assertEquals(underMap, testMap.getMap());
 }

 @Test
 public void testPut() {
 underMap = new TShortByteOpenHashMap(10);
 testMap = new TShortByteMapDecorator(underMap);

 Short key = Short.valueOf((short) 1);
 Byte value = Byte.valueOf((byte) 1);

 Byte putValue = testMap.put(key, value);
 Assert.assertNull(putValue);
 Assert.assertEquals(value, testMap.get(key));
 }

 @Test
 public void testPutWithNullKey() {
 testMap = new TShortByteMapDecorator();
 Assert.assertNull(testMap.put((Short) null, (Byte) null));
 Assert.assertNull(testMap.get((Short) null));
 }

 @Test
 public void testPutWithNullValue() {
 testMap = new TShortByteMapDecorator();
 Short key = Short.valueOf((short) 1);
 Assert.assertNull(testMap.put(key, null));
 Assert.assertNull(testMap.get(key));
 }

 @Test
 public void testGetNullKey() {
 testMap = new TShortByteMapDecorator();
 Assert.assertNull(testMap.get(null));
 }

 @Test
 public void testGetWrongKeyType() {
 testMap = new TShortByteMapDecorator();
 Integer key = Integer.valueOf(1);
 Assert.assertNull(testMap.get(key));
 }

 @Test
 public void testClear() {
 testMap = new TShortByteMapDecorator();
 testMap.put((Short) null, (Byte) null);
 testMap.put((Short) 1, (Byte) 1);
 testMap.clear();
 Assert.assertEquals(0, testMap.size());
 Assert.assertTrue(testMap.isEmpty());
 }

 @Test
 public void testRemove() {
 testMap = new TShortByteMapDecorator();
 testMap.put((Short) null, (Byte) null);
 testMap.put((Short) 1, (Byte) 1);

 Byte removedValue = testMap.remove((Short) 1);
 Assert.assertEquals(Byte.valueOf((byte) 1), removedValue);
 Assert.assertFalse(testMap.containsKey((Short) 1));
 }

 @Test
 public void testRemoveNullKey() {
 testMap = new TShortByteMapDecorator();
 testMap.put((Short) null, (Byte) null);
 testMap.put((Short) 1, (Byte) 1);

 Assert.assertNull(testMap.remove(null));
 Assert.assertEquals((Short) 1, testMap.keySet().iterator().next());
 }

 @Test
 public void testRemoveWrongKeyType() {
 testMap = new TShortByteMapDecorator();
 testMap.put((Short) null, (Byte) null);
 testMap.put((Short) 1, (Byte) 1);

 Integer key = Integer.valueOf(1);
 Assert.assertNull(testMap.remove(key));
 Assert.assertEquals((Short) 1, testMap.keySet().iterator().next());
 }

 @Test
 public void testEntrySet() {
 testMap = new TShortByteMapDecorator();
 testMap.put((Short) null, (Byte) null);
 testMap.put((Short) 1, (Byte) 1);
 testMap.put((Short) 2, (Byte) 2);

 Set<Entry<Short, Byte>> entrySet = testMap.entrySet();
 Assert.assertEquals(3, entrySet.size());

 Entry<Short, Byte> entry = entrySet.iterator().next();
 Assert.assertEquals((Short) 2, entry.getKey());
 Assert.assertEquals((Byte) 2, entry.getValue());
 }

 @Test
 public void testContainsValue() {
 testMap = new TShortByteMapDecorator();testMap.put((Short) 1, (Byte) 1);
 Assert.assertTrue(testMap.containsValue(Byte.valueOf((byte) 1)));
 Assert.assertFalse(testMap.containsValue(Byte.valueOf((byte) 2)));
 }

 @Test
 public void testContainsKey() {
 testMap = new TShortByteMapDecorator();
 testMap.put((Short) null, (Byte) null);
 testMap.put((Short) 1, (Byte) 1);
 testMap.put((Short) 2, (Byte) 2);

 Assert.assertTrue(testMap.containsKey((Short) 1));
 Assert.assertTrue(testMap.containsKey((Short) 2));
 Assert.assertFalse(testMap.containsKey((Short) 3));
 }

 @Test
 public void testSize() {
 testMap = new TShortByteMapDecorator();
 testMap.put((Short) null, (Byte) null);
 testMap.put((Short) 1, (Byte) 1);
 testMap.put((Short) 2, (Byte) 2);

 Assert.assertEquals(3, testMap.size());
 }

 @Test
 public void testIsEmpty() {
 testMap = new TShortByteMapDecorator();
 testMap.put((Short) null, (Byte) null);
 testMap.put((Short) 1, (Byte) 1);
 testMap.put((Short) 2, (Byte) 2);

 Assert.assertFalse(testMap.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap mapToAdd = new TShortByteOpenHashMap(10);
 mapToAdd.put((Short) 3, Byte.valueOf((byte) 3));
 mapToAdd.put((Short) 4, Byte.valueOf((byte) 4));

 testMap = new TShortByteMapDecorator();
 testMap.putAll(mapToAdd);

 Assert.assertEquals(4, testMap.size());
 Assert.assertTrue(testMap.containsKey((Short) 3));
 Assert.assertTrue(testMap.containsKey((Short) 4));
 }

 @Test
 public void testSerializedMap() throws Exception {
 // Serialize map and write to byte array
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
 objStream.writeObject(testMap);
 objStream.close();

 // Read object from byte array
 ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteStream.toByteArray());
 ObjectInputStream objInputStream = new ObjectInputStream(byteInputStream);
 TShortByteMapDecorator deserializedMap = (TShortByteMapDecorator) objInputStream.readObject();
 objInputStream.close();

 // Check deserialized map
 Assert.assertEquals(testMap.size(), deserializedMap.size());
 for (Map.Entry<Short, Byte> entry : testMap.entrySet()) {
 Entry<Short, Byte> deserializedEntry = deserializedMap.getEntry(entry.getKey());
 Assert.assertEquals(entry.getValue(), deserializedEntry.getValue());
 }
 }
}