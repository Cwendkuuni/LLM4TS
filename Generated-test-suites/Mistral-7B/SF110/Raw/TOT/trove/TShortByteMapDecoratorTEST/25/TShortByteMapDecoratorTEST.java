import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(_map);
 }

 @Test
 public void testEmptyMap() {
 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey((Short)null));
 assertFalse(decorator.containsKey((short)0));
 assertFalse(decorator.containsValue((byte)0));
 }

 @Test
 public void testPut() {
 Short key1 = (short)1;
 Byte value1 = new Byte((byte)1);
 decorator.put(key1, value1);
 assertEquals(1, decorator.size());
 assertTrue(decorator.containsKey(key1));
 assertEquals(value1, decorator.get(key1));

 Short key2 = (short)2;
 Byte value2 = new Byte((byte)2);
 decorator.put(key2, value2);
 assertEquals(2, decorator.size());
 assertFalse(decorator.containsKey((Short)null));
 assertTrue(decorator.containsKey(key1));
 assertTrue(decorator.containsKey(key2));
 assertFalse(decorator.containsValue((byte)1));
 assertTrue(decorator.containsValue(value1));
 assertTrue(decorator.containsValue(value2));
 }

 @Test
 public void testClear() {
 decorator.clear();
 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey((Short)null));
 assertFalse(decorator.containsKey((short)0));
 assertFalse(decorator.containsValue((byte)0));
 }

 @Test
 public void testGetNullKey() {
 assertNull(decorator.get((Object)null));
 }

 @Test
 public void testGetWrongKeyType() {
 assertNull(decorator.get(1L));
 }

 @Test(expected = ClassCastException.class)
 public void testGetWrongValueType() {
 decorator.put((short)1, new Integer(1));
 decorator.get((short)1);
 }

 @Test
 public void testRemove() {
 decorator.put(key1, value1);
 decorator.put(key2, value2);
 assertEquals(2, decorator.size());
 assertTrue(decorator.containsKey(key1));
 assertTrue(decorator.containsKey(key2));
 Byte removedValue = decorator.remove(key1);
 assertEquals(1, decorator.size());
 assertNull(decorator.get(key1));
 assertEquals(value1, removedValue);
 }

 @Test
 public void testRemoveNullKey() {
 assertNull(decorator.remove((Object)null));
 }

 @Test
 public void testRemoveWrongKeyType() {
 assertNull(decorator.remove(1L));
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
 assertEquals(0, entrySet.size());
 decorator.put(key1, value1);
 decorator.put(key2, value2);
 assertEquals(2, entrySet.size());
 }

 @Test
 public void testContainsValue() {
 decorator.put(key1, value1);
 decorator.put(key2, value2);
 assertTrue(decorator.containsValue(value1));
 assertTrue(decorator.containsValue(value2));
 }

 @Test
 public void testPutAll() {
 TShortByteMap map = new TShortByteMap();
 map.put((short)1, new Byte((byte)1));
 map.put((short)2, new Byte((byte)2));
 decorator.putAll(map.entrySet());
 assertEquals(2, decorator.size());
 assertTrue(decorator.containsKey((short)1));
 assertTrue(decorator.containsKey((short)2));
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(byteStream);
 decorator.writeExternal(out);
 out.close();
 ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
 ObjectInputStream in = new ObjectInputStream(byteInput);
 TShortByteMapDecorator decorator2 = (TShortByteMapDecorator)in.readObject();
 assertEquals(decorator.size(), decorator2.size());
 for (Entry<Short, Byte> entry : decorator.entrySet()) {
 assertEquals(entry.getKey(), decorator2.get(entry.getKey()));
 assertEquals(entry.getValue(), decorator2.get(entry.getKey()));
 }
 in.close();
 }
}