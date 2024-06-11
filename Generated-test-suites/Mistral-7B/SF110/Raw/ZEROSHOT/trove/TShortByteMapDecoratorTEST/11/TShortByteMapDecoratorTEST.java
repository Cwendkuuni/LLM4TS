import static org.junit.Assert.*;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import org.junit.Test;

public class TShortByteMapDecoratorTest {

 private TShortByteMap map;
 private TShortByteMapDecorator decorator;

 @Test
 public void testEmptyConstructor() {
 decorator = new TShortByteMapDecorator();
 assertEquals(0, decorator.size());
 assertFalse(decorator.isEmpty());
 }

 @Test
 public void testConstructorWithMap() {
 map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(map);
 assertEquals(0, decorator.size());
 assertFalse(decorator.isEmpty());
 }

 @Test
 public void testGetMap() {
 map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(map);
 assertEquals(map, decorator.getMap());
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullKey() {
 decorator = new TShortByteMapDecorator();
 decorator.put((Short) null, (Byte) 1);
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 decorator = new TShortByteMapDecorator();
 decorator.put((Short) 1, (Byte) null);
 }

 @Test
 public void testPut() {
 decorator = new TShortByteMapDecorator();
 decorator.put((Short) 1, (Byte) 1);
 assertEquals(1, decorator.size());
 assertFalse(decorator.isEmpty());
 assertEquals((Byte) 1, decorator.get((Short) 1));
 }

 @Test
 public void testGetNullKey() {
 decorator = new TShortByteMapDecorator();
 decorator.put((Short) 1, (Byte) 1);
 assertEquals(null, decorator.get(null));
 }

 @Test
 public void testGetUnknownKey() {
 decorator = new TShortByteMapDecorator();
 decorator.put((Short) 1, (Byte) 1);
 assertEquals(null, decorator.get((Short) 2));
 }

 @Test
 public void testClear() {
 decorator = new TShortByteMapDecorator();
 decorator.put((Short) 1, (Byte) 1);
 decorator.clear();
 assertEquals(0, decorator.size());
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemoveNullKey() {
 decorator = new TShortByteMapDecorator();
 decorator.put((Short) 1, (Byte) 1);
 assertEquals((Byte) 1, decorator.remove(null));
 assertEquals(0, decorator.size());
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemoveUnknownKey() {
 decorator = new TShortByteMapDecorator();
 decorator.put((Short) 1, (Byte) 1);
 assertEquals(null, decorator.remove((Short) 2));
 assertEquals(1, decorator.size());
 assertFalse(decorator.isEmpty());
 }

 @Test
 public void testRemoveKnownKey() {
 decorator = new TShortByteMapDecorator();
 decorator.put((Short) 1, (Byte) 1);
 assertEquals((Byte) 1, decorator.remove((Short) 1));
 assertEquals(0, decorator.size());
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testEntrySetSize() {
 decorator = new TShortByteMapDecorator();
 decorator.put((Short) 1, (Byte) 1);
 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
 assertEquals(1, entrySet.size());
 }

 @Test
 public void testContainsValue() {
 decorator = new TShortByteMapDecorator();
 decorator.put((Short) 1, (Byte) 1);
 assertTrue(decorator.containsValue((Byte) 1));
 }

 @Test
 public void testContainsKey() {
 decorator = new TShortByteMapDecorator();decorator.put((Short) 1, (Byte) 1);
 assertTrue(decorator.containsKey((Short) 1));
 }

 @Test
 public void testSize() {
 decorator = new TShortByteMapDecorator();
 decorator.put((Short) 1, (Byte) 1);
 assertEquals(1, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 decorator = new TShortByteMapDecorator();
 assertEquals(true, decorator.isEmpty());
 decorator.put((Short) 1, (Byte) 1);
 assertEquals(false, decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 map = new TShortByteMap();
 map.put((Short) 1, (Byte) 1);
 map.put((Short) 2, (Byte) 2);
 decorator = new TShortByteMapDecorator();
 decorator.putAll(map.entrySet());
 assertEquals(map.size(), decorator.size());
 }

 @Test
 public void testWrapKey() {
 decorator = new TShortByteMapDecorator();
 short key = 1;
 assertEquals(key, decorator.wrapKey(key));
 }

 @Test
 public void testUnwrapKeyNull() {
 decorator = new TShortByteMapDecorator();
 assertNull(decorator.unwrapKey(null));
 }

 @Test
 public void testUnwrapKeyShort() {
 decorator = new TShortByteMapDecorator();
 short key = 1;
 assertEquals(key, decorator.unwrapKey(key));
 }

 @Test
 public void testWrapValue() {
 decorator = new TShortByteMapDecorator();
 byte value = 1;
 assertEquals(value, decorator.wrapValue(value));
 }

 @Test
 public void testUnwrapValueNull() {
 decorator = new TShortByteMapDecorator();
 assertNull(decorator.unwrapValue(null));
 }

 @Test
 public void testUnwrapValueByte() {
 decorator = new TShortByteMapDecorator();
 byte value = 1;
 assertEquals(value, decorator.unwrapValue(value));
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator = new TShortByteMapDecorator(map);
 decorator.writeExternal(out);
 byte[] bytes = baos.toByteArray();
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TShortByteMapDecorator decorator2 = (TShortByteMapDecorator) in.readObject();
 assertEquals(map.size(), decorator2.size());
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
 decorator = new TShortByteMapDecorator(map);
 TShortByteMapDecorator clone = (TShortByteMapDecorator) decorator.clone();
 assertEquals(map.size(), clone.size());
 }
}