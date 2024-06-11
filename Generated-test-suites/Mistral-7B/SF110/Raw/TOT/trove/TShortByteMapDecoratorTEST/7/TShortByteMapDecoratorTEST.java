import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.TShortByteMap.MapEntry;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Before
 public void setup() {
 _map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(_map);
 }

 @Test
 public void testEmptyMap() {
 assertEquals(0, decorator.size());
 assertNull(decorator.get((Short)null));
 assertNull(decorator.get(Short.MIN_VALUE));
 assertNull(decorator.get(Short.MAX_VALUE));
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutGet() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);

 assertEquals(2, decorator.size());
 assertNull(decorator.get((Short)null));
 assertEquals((byte)1, decorator.get((short)1));
 assertEquals((byte)2, decorator.get((short)2));
 }

 @Test
 public void testGetNullKey() {
 assertNull(decorator.get(null));
 }

 @Test(expected = ClassCastException.class)
 public void testGetInvalidKeyType() {
 decorator.put((short)1, (byte)1);
 assertNull(decorator.get("invalid key"));
 }

 @Test
 public void testClear() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 decorator.clear();

 assertEquals(0, decorator.size());
 assertNull(decorator.get((Short)null));
 assertNull(decorator.get((short)1));
 assertNull(decorator.get((short)2));
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemove() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 decorator.remove((Short)1);

 assertEquals(1, decorator.size());
 assertNull(decorator.get((Short)null));
 assertNull(decorator.get((short)1));
 assertEquals((byte)2, decorator.get((short)2));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
 decorator.remove(null);
 }

 @Test(expected = ClassCastException.class)
 public void testRemoveInvalidKeyType() {
 decorator.put((short)1, (byte)1);
 decorator.remove("invalid key");
 }

 @Test
 public void testEntrySet() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);

 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
 assertEquals(2, entrySet.size());

 Map.Entry<Short, Byte> entry = entrySet.iterator().next();
 assertEquals((short)1, entry.getKey());
 assertEquals((byte)1, entry.getValue());
 }

 @Test
 public void testContainsValue() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);

 assertTrue(decorator.containsValue((byte)1));
 assertTrue(decorator.containsValue((byte)2));
 assertFalse(decorator.containsValue((byte)3));
 }

 @Test
 public void testContainsKey() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);

 assertTrue(decorator.containsKey((Short)1));
 assertTrue(decorator.containsKey((Short)2));
 assertFalse(decorator.containsKey((Short)3));
 }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 decorator.writeExternal(oos);
 ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 TShortByteMapDecorator deserializedDecorator = (TShortByteMapDecorator)ois.readObject();
 assertEquals(decorator.size(), deserializedDecorator.size());
 }
}