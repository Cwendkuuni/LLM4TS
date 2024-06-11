import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.view.AbstractMapEntry;

public class TShortByteMapDecoratorTest {

 private TShortByteMap map;
 private TShortByteMapDecorator decorator;

 @Before
 public void setUp() {
 map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(map);
 }

 @Test
 public void testEmptyMap() {
 assertTrue(decorator.isEmpty());
 assertEquals(0, decorator.size());
 assertNull(decorator.get(Short.MIN_VALUE));
 assertNull(decorator.get(Short.MAX_VALUE));
 }

 @Test
 public void testPut() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 assertEquals(2, decorator.size());
 assertNotNull(decorator.get((short) 1));
 assertNotNull(decorator.get((short) 2));
 assertEquals((byte) 1, decorator.get((short) 1));
 assertEquals((byte) 2, decorator.get((short) 2));

 assertNull(decorator.get(Short.MIN_VALUE));
 assertNull(decorator.get(Short.MAX_VALUE));
 }

 @Test
 public void testPutNullKey() {
 assertNull(decorator.put(null, (byte) 1));
 }

 @Test
 public void testPutNullValue() {
 decorator.put((short) 1, null);
 assertNull(decorator.get((short) 1));
 }

 @Test
 public void testPutExistingKey() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 1, (byte) 2);

 assertEquals(1, decorator.size());
 assertEquals((byte) 2, decorator.get((short) 1));
 }

 @Test
 public void testClear() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);
 decorator.clear();

 assertTrue(decorator.isEmpty());
 assertEquals(0, decorator.size());
 assertNull(decorator.get((short) 1));
 assertNull(decorator.get((short) 2));
 }

 @Test
 public void testRemove() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 assertEquals(2, decorator.size());
 assertNotNull(decorator.get((short) 1));
 assertNotNull(decorator.get((short) 2));

 decorator.remove((short) 1);
 assertEquals(1, decorator.size());
 assertNull(decorator.get((short) 1));
 assertNotNull(decorator.get((short) 2));
 }

 @Test
 public void testRemoveNullKey() {
 assertNull(decorator.remove(null));
 }

 @Test
 public void testRemoveNonExistingKey() {
 assertNull(decorator.remove((short) 3));
 }

 @Test
 public void testEntrySet() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();

 assertEquals(2, entrySet.size());

 Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();
 Entry<Short, Byte> entry = iterator.next();
 assertEquals((short) 1, entry.getKey());
 assertEquals((byte) 1, entry.getValue());

 entry = iterator.next();
 assertEquals((short) 2, entry.getKey());
 assertEquals((byte) 2, entry.getValue());
 }

 @Test
 public void testEntrySetAdd() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();

 Entry<Short, Byte> entry = new AbstractMapEntry<Short, Byte>() {
 @Override public Short getKey() { return (short) 3; }
 @Override public Byte getValue() { return (byte) 3; }
 };

 entrySet.add(entry);

 assertEquals(3, decorator.size());
 assertEquals((byte) 3, decorator.get((short) 3));
 }

 @Test
 public void testEntrySetRemove() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();

 Entry<Short, Byte> entry = new AbstractMapEntry<Short, Byte>() {
 @Override public Short getKey() { return (short) 1; }
 @Override public Byte getValue() { return (byte) 1; }
 };

 entrySet.remove(entry);

 assertEquals(1, decorator.size());
 assertNull(decorator.get((short) 1));
 }

 @Test
 public void testContainsKey() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 assertTrue(decorator.containsKey((short) 1));
 assertTrue(decorator.containsKey((short) 2));
 assertFalse(decorator.containsKey((short) 3));
 }

 @Test
 public void testContainsValue() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 assertTrue(decorator.containsValue((byte) 1));
 assertTrue(decorator.containsValue((byte) 2));
 assertFalse(decorator.containsValue((byte) 3));
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 DataOutputStream dataStream = new DataOutputStream(byteStream);
 decorator.writeExternal(dataStream);
 dataStream.close();
 byte[] bytes = byteStream.toByteArray();

 ByteArrayInputStream byteInput = new ByteArrayInputStream(bytes);
 DataInputStream dataInput = new DataInputStream(byteInput);
 TShortByteMapDecorator decoratorCopy = new TShortByteMapDecorator();
 decoratorCopy.readExternal(dataInput);
 dataInput.close();

 assertEquals(decorator.size(), decoratorCopy.size());
 for (TShortByteIterator iterator = decorator.iterator(); iterator.hasNext(); ) {
 Entry<Short, Byte> entry = iterator.next();
 assertEquals(entry.getKey(), decoratorCopy.get(entry.getKey()));
 assertEquals(entry.getValue(), decoratorCopy.get(entry.getKey()));
 }
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
 TShortByteMapDecorator decoratorClone = (TShortByteMapDecorator) decorator.clone();
 assertEquals(decorator.size(), decoratorClone.size());
 for (TShortByteIterator iterator = decorator.iterator(); iterator.hasNext(); ) {
 Entry<Short, Byte> entry = iterator.next();
 assertEquals(entry.getKey(), decoratorClone.get(entry.getKey()));
 assertEquals(entry.getValue(), decoratorClone.get(entry.getKey()));
 }
 }
}