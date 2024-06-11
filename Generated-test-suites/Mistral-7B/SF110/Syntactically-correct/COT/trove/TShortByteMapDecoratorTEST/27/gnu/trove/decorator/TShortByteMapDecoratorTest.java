import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.TShortByteMapImpl;
import gnu.trove.decorator.TShortByteMapDecorator;
import java.util.Arrays;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

public class TShortByteMapDecoratorTest {

 private TShortByteMap map;
 private TShortByteMapDecorator decorator;

 @Before
 public void setUp() {
 map = new TShortByteMapImpl();
 decorator = new TShortByteMapDecorator(map);
 }

 @Test
 public void testConstructor() {
 // Test default constructor
 TShortByteMapDecorator decoratedMap = new TShortByteMapDecorator();
 Assert.assertNotNull(decoratedMap);
 }

 @Test
 public void testConstructorWithMap() {
 // Test constructor with map
 TShortByteMap map = new TShortByteMapImpl();
 map.put((short) 1, (byte) 1);
 map.put((short) 2, (byte) 2);
 TShortByteMapDecorator decoratedMap = new TShortByteMapDecorator(map);
 // Test getMap method
 TShortByteMap actualMap = decoratedMap.getMap();
 Assert.assertSame(map, actualMap);
 }

 @Test
 public void testPut() {
 // Test putting valid key-value pairs
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);
 // Test putting null key
 Assert.assertNull(decorator.put(null, (byte) 1));
 // Test putting null value
 decorator.put((short) 1, null);
 Assert.assertNull(decorator.get((short) 1));
 // Test putting the same key-value pair multiple times
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 1, (byte) 1);
 // Test putting a key that already exists
 Byte existingValue = decorator.get((short) 1);
 decorator.put((short) 1, (byte) 3);
 Assert.assertNotSame(existingValue, decorator.get((short) 1));
 }

 @Test
 public void testGet() {
 // Test getting valid key
 Byte value = decorator.put((short) 1, (byte) 1);
 Assert.assertEquals(value, decorator.get((short) 1));
 // Test getting null key
 Assert.assertNull(decorator.get((short) 0));
 // Test getting the same key multiple times
 Byte value1 = decorator.get((short) 1);
 Byte value2 = decorator.get((short) 1);
 Assert.assertSame(value1, value2);
 }

 @Test
 public void testClear() {
 // Test clearing the map
 decorator.clear();
 // Test checking if map is empty
 assertTrue(decorator.isEmpty());
 // Test getting size after clearing
 assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 // Test removing valid key
 Byte value = decorator.put((short) 1, (byte) 1);
 boolean removed = decorator.remove((short) 1);
 Assert.assertTrue(removed);
 Assert.assertNull(decorator.get((short) 1));
 // Test removing null key
 removed = decorator.remove(null);
 Assert.assertFalse(removed);
 // Test removing a key that does not exist
 removed = decorator.remove((short) 2);
 Assert.assertFalse(removed);
 }

 @Test
 public void testEntrySet() {
 // Test getting entry set
 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
 // Test checking if entry set is empty
 assertTrue(entrySet.isEmpty());
 // Test adding an entry to the map
 decorator.put((short) 1, (byte) 1);
 Entry<Short, Byte> entry = decorator.entrySet().iterator().next();
 // Test removing an entry from the map
 decorator.remove((short) 1);
 // Test checking if entry set is empty again
 assertTrue(decorator.entrySet().isEmpty());
 }

 @Test
 public void testContainsValue() {
 // Test containing a value
 decorator.put((short) 1, (byte) 1);
 assertTrue(decorator.containsValue((byte) 1));
 // Test not containing a value
 assertTrue(decorator.containsValue((byte) 0));
 }

 @Test
 public void testContainsKey() {
 // Test containing a key
 decorator.put((short) 1, (byte) 1);
 assertTrue(decorator.containsKey((short) 1));
 // Test not containing a key
 assertFalse(decorator.containsKey((short) 2));
 }

 @Test
 public void testSize() {
 // Test size when map is empty
 assertEquals(0, decorator.size());
 // Test size when map has elements
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 // Test isEmpty when map is empty
 assertTrue(decorator.isEmpty());
 // Test isEmpty when map has elements
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);
 assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap anotherMap = new TShortByteMapImpl();
 anotherMap.put((short) 2, (byte) 2);
 anotherMap.put((short) 3, (byte) 3);
 decorator.putAll(anotherMap);
 // Test checking if all entries from anotherMap have been added
 for (TShortByteIterator iterator = anotherMap.iterator(); iterator.hasNext(); ) {
 Entry<Short, Byte> entry = iterator.next();
 assertEquals(entry.getKey(), decorator.get(entry.getKey()));
 assertEquals(entry.getValue(), decorator.get(entry.getKey()));
 }
 // Test checking if original map still has the same entries
 for (TShortByteIterator iterator = map.iterator(); iterator.hasNext(); ) {
 Entry<Short, Byte> entry = iterator.next();
 assertEquals(entry.getKey(), decorator.get(entry.getKey()));
 assertEquals(entry.getValue(), decorator.get(entry.getKey()));
 }
 }

 @Test
 public void testWrapKey() {
 // Test wrapping a short key
 assertEquals(1, decorator.wrapKey((short) 1));
 }

 @Test
 public void testUnwrapKey() {
 // Test unwrapping a short key
 assertEquals((short) 1, decorator.unwrapKey((short) 1));
 }

 @Test
 public void testWrapValue() {
 // Test wrapping a byte value
 assertEquals((byte) 1, decorator.wrapValue((byte) 1));
 }

 @Test
 public void testUnwrapValue() {
 // Test unwrapping a byte value
 assertEquals((byte) 1, decorator.unwrapValue((byte) 1));
 }

 @Test(expected = IOException.class)
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 'T', (byte) 'S', (byte) ' ', (byte) 'S', (byte) 'h', (byte) 'o', (byte) 'r', (byte) 't', (byte) 'B', (byte) 'y', (byte) 't', (byte) 'e', (byte) 'M', (byte) 'a', (byte) 'p', (byte) '<', (byte) 'S', (byte) 'h', (byte) 'o', (byte) 'r', (byte) 't', (byte) ' ', (byte) 'B', (byte) 'y', (byte) 't', (byte) 'e', (byte) ' ', (byte) 'm', (byte) 'a', (byte) 'p', (byte) '>', (byte) 0});
 ObjectInput in = new ObjectInputStream(input);
 TShortByteMapDecorator decoratedMap = new TShortByteMapDecorator();
 decoratedMap.readExternal(in);
 }

 @Test(expected = ClassNotFoundException.class)
 public void testWriteExternal() throws IOException {
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(output);
 decorator.writeExternal(out);
 }
}