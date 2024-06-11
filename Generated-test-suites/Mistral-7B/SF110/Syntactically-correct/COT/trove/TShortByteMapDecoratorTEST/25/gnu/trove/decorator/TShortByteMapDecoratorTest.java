import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.decorator.TShortByteMapDecorator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Before
 public void setup() {
 _map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(_map);
 }

 @Test
 public void testConstructor() {
 assertNull(decorator.getMap());
 }

 @Test
 public void testConstructorWithMap() {
 TShortByteMap map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(map);
 assertEquals(map, decorator.getMap());
 }

 @Test
 public void testPut() {
 decorator.put((short) 1, (byte) 1);
 assertEquals((byte) 1, decorator.get((short) 1));
 decorator.put((short) 2, (byte) 2);
 assertEquals((byte) 2, decorator.get((short) 2));
 assertEquals(2, decorator.size());
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullKey() {
 decorator.put(null, (byte) 1);
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 decorator.put((short) 1, null);
 }

 @Test
 public void testGet() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);
 assertEquals((byte) 1, decorator.get((short) 1));
 assertEquals((byte) 2, decorator.get((short) 2));
 assertEquals(null, decorator.get(null));
 }

 @Test(expected = NullPointerException.class)
 public void testGetNullKey() {
 decorator.get(null);
 }

 @Test
 public void testClear() {
 decorator.put((short) 1, (byte) 1);
 assertEquals(1, decorator.size());
 decorator.clear();
 assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 decorator.put((short) 1, (byte) 1);
 assertEquals(1, decorator.size());
 decorator.remove((short) 1);
 assertEquals(0, decorator.size());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
 decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
 assertEquals(0, entrySet.size());
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);
 assertEquals(2, entrySet.size());
 }

 @Test
 public void testContainsValue() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);
 assertTrue(decorator.containsValue((byte) 1));
 assertTrue(decorator.containsValue((byte) 2));
 }

 @Test
 public void testContainsKey() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);
 assertTrue(decorator.containsKey((short) 1));
 assertTrue(decorator.containsKey((short) 2));
 assertTrue(decorator.containsKey((short) 0));
 assertFalse(decorator.containsKey((short) 3));
 }

 @Test
 public void testSize() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 decorator.put((short) 1, (byte) 1);
 assertFalse(decorator.isEmpty());
 decorator.clear();
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap map = new TShortByteMap();
 map.put((short) 1, (byte) 1);
 map.put((short) 2, (byte) 2);
 decorator.putAll(map);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testWrapKey() {
 assertEquals((short) 1, decorator.wrapKey((short) 1));
 }

 @Test
 public void testUnwrapKey() {
 assertEquals((short) 1, decorator.unwrapKey((short) 1));
 }

 @Test
 public void testWrapValue() {
 assertEquals((byte) 1, decorator.wrapValue((byte) 1));
 }

 @Test
 public void testUnwrapValue() {
 assertEquals((byte) 1, decorator.unwrapValue((byte) 1));
 }

 @Test
 public void testExternalization() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
 ObjectOutputStream objectOutput = new ObjectOutputStream(out);
 ObjectInputStream objectInput = new ObjectInputStream(in);
 decorator.writeExternal(objectOutput);
 TShortByteMapDecorator deserialized = (TShortByteMapDecorator)objectInput.readObject();
 assertEquals(decorator.getMap(), deserialized.getMap());
 }
}