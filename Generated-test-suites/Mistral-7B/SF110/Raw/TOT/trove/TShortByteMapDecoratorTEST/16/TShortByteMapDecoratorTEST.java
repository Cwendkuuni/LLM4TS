import gnu.trove.map.TShortByteMap;
import gnu.trove.map.TShortByteMapImpl;
import gnu.trove.iterator.TShortByteIterator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TShortByteMapImpl();
 decorator = new TShortByteMapDecorator(_map);
 }

 @Test
 public void testEmptyMap() {
 assertEquals(0, decorator.size());
 assertNull(decorator.get(Short.MIN_VALUE));
 assertFalse(decorator.containsKey(Short.MIN_VALUE));
 }

 @Test
 public void testPut() {
 decorator.put((short) 1, (byte) 'A');
 decorator.put((short) 2, (byte) 'B');
 decorator.put((short) 3, (byte) null);

 assertEquals(3, decorator.size());
 assertSame((byte)'A', decorator.get((short) 1));
 assertSame((byte)'B', decorator.get((short) 2));
 assertNull(decorator.get((short) 3));
 assertFalse(decorator.containsValue((byte) 0));

 assertEquals((Short) 1, decorator.keySet().iterator().next());
 assertEquals((Short) 2, decorator.keySet().iterator().next());
 assertEquals((Short) 3, decorator.keySet().iterator().next());
 }

 @Test
 public void testPutNullKey() {
 try {
 decorator.put(null, (byte) 'C');
 fail();
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testPutNullValue() {
 decorator.put((short) 1, null);
 assertEquals(1, decorator.size());
 assertNull(decorator.get((short) 1));
 }

 @Test
 public void testClear() {
 decorator.put((short) 1, (byte) 'A');
 decorator.put((short) 2, (byte) 'B');
 decorator.clear();
 assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 decorator.put((short) 1, (byte) 'A');
 decorator.put((short) 2, (byte) 'B');
 decorator.remove((short) 1);
 assertEquals(1, decorator.size());
 assertNull(decorator.get((short) 1));
 }

 @Test
 public void testRemoveNullKey() {
 decorator.put((short) 1, (byte) 'A');
 try {
 decorator.remove(null);
 fail();
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testRemoveNonExistentKey() {
 decorator.remove((short) Short.MAX_VALUE);
 assertEquals(0, decorator.size());
 }

 @Test
 public void testEntrySet() {
 decorator.put((short) 1, (byte) 'A');
 decorator.put((short) 2, (byte) 'B');

 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
 assertEquals(2, entrySet.size());

 Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();
 assertEquals((Short) 1, iterator.next().getKey());
 assertEquals((Byte) 'A', iterator.next().getValue());
 }

 @Test
 public void testContainsValue() {
 decorator.put((short) 1, (byte) 'A');
 decorator.put((short) 2, (byte) 'B');
 decorator.put((short) 3, (byte) 'B');

 assertTrue(decorator.containsValue((byte) 'B'));
 assertFalse(decorator.containsValue((byte) 'C'));
 }

 @Test
 public void testContainsKey() {
 decorator.put((short) 1, (byte) 'A');
 decorator.put((short) 2, (byte) 'B');
 decorator.put((short) 3, (byte) 'B');

 assertTrue(decorator.containsKey((short) 1));
 assertTrue(decorator.containsKey((short) 2));
 assertTrue(decorator.containsKey((short) 3));
 }

 @Test
 public void testSize() {
 decorator.put((short) 1, (byte) 'A');
 decorator.put((short) 2, (byte) 'B');
 decorator.put((short) 3, (byte) 'B');
 decorator.put((short) 4, (byte) 'C');

 assertEquals(4, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 assertEquals(true, decorator.isEmpty());
 decorator.put((short) 1, (byte) 'A');
 assertEquals(false, decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap map = new TShortByteMapImpl();
 map.put((short) 1, (byte) 'A');
 map.put((short) 2, (byte) 'B');
 decorator.putAll(map);

 assertEquals(2, decorator.size());
 assertEquals((Short) 1, decorator.keySet().iterator().next());
 assertEquals((Short) 2, decorator.keySet().iterator().next());
 }

 @Test
 public void testExternalizable() {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
 decorator.writeExternal(objStream);
 byte[] bytes = byteStream.toByteArray();
 ObjectInputStream objInput = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TShortByteMapDecorator decorator2 = (TShortByteMapDecorator) objInput.readObject();

 assertEquals(decorator.size(), decorator2.size());
 for (Entry<Short, Byte> entry : decorator.entrySet()) {
 assertEquals(entry.getKey(), decorator2.get(entry.getKey()));
 assertEquals(entry.getValue(), decorator2.get(entry.getKey()));
 }
 }
}