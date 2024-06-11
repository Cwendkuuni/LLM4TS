import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.view.TShortByteMapView;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(_map);
 }

 @Test
 public void testConstructor() {
 assertNotNull(decorator);
 assertEquals("Map not initialized", _map.size(), decorator.size());
 }

 @Test
 public void testConstructorWithMap() {
 TShortByteMap map = new TShortByteMap();
 map.put((short) 1, (byte) 1);
 map.put((short) 2, (byte) 2);
 TShortByteMapDecorator decoratorWithMap = new TShortByteMapDecorator(map);

 assertEquals("Size not correct", map.size(), decoratorWithMap.size());

 assertEquals("Map not equal", map.get((short) 1), decoratorWithMap.get((short) 1));
 assertEquals("Map not equal", map.get((short) 2), decoratorWithMap.get((short) 2));
 }

 @Test
 public void testPut() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 assertEquals("Size not correct", 2, decorator.size());

 assertEquals("Value not set correctly", (byte) 1, decorator.get((short) 1));
 assertEquals("Value not set correctly", (byte) 2, decorator.get((short) 2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithNullKey() {
 decorator.put(null, (byte) 1);
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
 decorator.put((short) 1, null);
 fail("Should have thrown a NullPointerException");
 }

 @Test
 public void testGet() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 assertEquals("Value not returned correctly", (byte) 1, decorator.get((short) 1));
 assertEquals("Value not returned correctly", (byte) 2, decorator.get((short) 2));

 assertEquals("Value not returned correctly for null key", null, decorator.get(null));
 }

 @Test
 public void testGetWithUnwrappedKey() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 assertEquals("Value not returned correctly", (byte) 1, decorator.get((short) 1));
 assertEquals("Value not returned correctly", (byte) 2, decorator.get((short) 2));

 Short key = Short.valueOf((short) 1);
 assertEquals("Value not returned correctly", (byte) 1, decorator.get(key));
 }

 @Test
 public void testClear() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);
 decorator.clear();

 assertEquals("Size not zero after clear", 0, decorator.size());
 }

 @Test
 public void testRemove() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 assertEquals("Size not correct before remove", 2, decorator.size());

 decorator.remove((short) 1);
 assertEquals("Size not correct after remove", 1, decorator.size());

 assertNull("Wrong value removed", decorator.get((short) 1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveWithNullKey() {
 decorator.remove(null);
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test
 public void testEntrySet() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();

 assertEquals("Size not correct", 2, entrySet.size());

 assertEquals("Entry not correct", new Entry<Short, Byte>() {
 @Override public Short getKey() { return Short.valueOf((short) 1); }
 @Override public Byte getValue() { return Byte.valueOf((byte) 1); }
 }, entrySet.iterator().next());

 assertEquals("Entry not correct", new Entry<Short, Byte>() {
 @Override public Short getKey() { return Short.valueOf((short) 2); }
 @Override public Byte getValue() { return Byte.valueOf((byte) 2); }
 }, entrySet.iterator().next());

 assertFalse("Iterator not exhausted", entrySet.iterator().hasNext());
 }

 @Test
 public void testContainsValue() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 assertTrue("Wrong value not contained", decorator.containsValue((byte) 1));
 assertTrue("Wrong value not contained", decorator.containsValue((byte) 2));

 assertFalse("Wrong value contained", decorator.containsValue((byte) 3));
 }

 @Test
 public void testContainsKey() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 assertTrue("Wrong key not contained", decorator.containsKey((short) 1));
 assertTrue("Wrong key not contained", decorator.containsKey((short) 2));

 assertFalse("Wrong key contained", decorator.containsKey((short) 3));

 Short key = Short.valueOf((short) 1);
 assertTrue("Wrong key not contained", decorator.containsKey(key));
 }

 @Test
 public void testSize() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);
 decorator.put((short) 3, (byte) 3);

 assertEquals("Size not correct", 3, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 decorator.clear();

 assertTrue("Map is not empty", decorator.isEmpty());

 decorator.put((short) 1, (byte) 1);
 assertFalse("Map is empty", decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap map = new TShortByteMap();
 map.put((short) 4, (byte) 4);
 map.put((short) 5, (byte) 5);

 decorator.putAll(map);

 assertEquals("Size not correct", 5, decorator.size());

 assertEquals("Value not set correctly", (byte) 4, decorator.get((short) 4));
 assertEquals("Value not set correctly", (byte) 5, decorator.get((short) 5));
 }

 @Test
 public void testWrapKey() {
 assertEquals("Wrong key returned", (short) 1, decorator.wrapKey((short) 1));
 }

 @Test
 public void testUnwrapKey() {
 assertEquals("Wrong key returned", (short) 1, decorator.unwrapKey((Short) Short.valueOf((short) 1)));
 }

 @Test
 public void testWrapValue() {
 assertEquals("Wrong value returned", (byte) 1, decorator.wrapValue((byte) 1));
 }

 @Test
 public void testUnwrapValue() {
 assertEquals("Wrong value returned", (byte) 1, decorator.unwrapValue(Byte.valueOf((byte) 1)));
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 DataOutputStream dataStream = new DataOutputStream(byteStream);
 decorator.writeExternal(dataStream);
 dataStream.flush();

 byte[] bytes = byteStream.toByteArray();
 ByteArrayInputStream byteStreamInput = new ByteArrayInputStream(bytes);
 DataInputStream dataStreamInput = new DataInputStream(byteStreamInput);

 TShortByteMapDecorator readDecorator = new TShortByteMapDecorator();
 readDecorator.readExternal(dataStreamInput);

 assertEquals("Decorators not equal", decorator, readDecorator);
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
 TShortByteMapDecorator cloned = (TShortByteMapDecorator) decorator.clone();

 assertEquals("Decorators not equal", decorator, cloned);
 }

 @After
 public void tearDown() {
 _map = null;
 decorator = null;
 }
}