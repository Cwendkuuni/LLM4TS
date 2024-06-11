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
 public void setUp() {
 _map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(_map);
 }

 @Test
 public void testEmptyMap() {
 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey((Short)null));
 assertFalse(decorator.containsKey((Short)1));
 assertFalse(decorator.containsValue((Byte)0));
 }

 @Test
 public void testPut() {
 decorator.put((Short)1, (Byte)1);
 decorator.put((Short)2, (Byte)2);
 decorator.put((Short)1, (Byte)3);

 assertEquals(2, decorator.size());
 assertTrue(decorator.containsKey((Short)1));
 assertTrue(decorator.containsKey((Short)2));
 assertFalse(decorator.containsKey((Short)3));
 assertFalse(decorator.containsValue((Byte)1));
 assertTrue(decorator.containsValue((Byte)3));

 Byte value = decorator.get((Short)1);
 assertEquals((Byte)3, value);
 }

 @Test(expected = NullPointerException.class)
 public void testNullKey() {
 decorator.put(null, (Byte)1);
 }

 @Test(expected = NullPointerException.class)
 public void testNullValue() {
 decorator.put((Short)1, null);
 }

 @Test
 public void testClear() {
 decorator.clear();
 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey((Short)1));
 }

 @Test
 public void testEntrySet() {
 Set<Map.Entry<Short, Byte>> entries = decorator.entrySet();
 TShortByteIterator iterator = _map.iterator();
 int i = 0;
 while (iterator.hasNext()) {
 Map.Entry<Short, Byte> entry = iterator.next();
 assertTrue(entries.contains(entry));
 i++;
 }
 assertEquals(decorator.size(), i);
 }

 @Test
 public void testRemove() {
 decorator.put((Short)1, (Byte)1);
 decorator.put((Short)2, (Byte)2);
 decorator.put((Short)1, (Byte)3);

 Byte removedValue = decorator.remove((Short)1);
 assertEquals((Byte)3, removedValue);
 assertEquals(2, decorator.size());
 assertFalse(decorator.containsKey((Short)1));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
 decorator.remove(null);
 }

 @Test
 public void testContainsValue() {
 decorator.put((Short)1, (Byte)1);
 decorator.put((Short)1, (Byte)3);
 assertFalse(decorator.containsValue((Byte)1));
 assertTrue(decorator.containsValue((Byte)3));
 }

 @Test
 public void testContainsKey() {
 decorator.put((Short)1, (Byte)1);
 decorator.put((Short)2, (Byte)2);
 decorator.put((Short)1, (Byte)3);

 assertTrue(decorator.containsKey((Short)1));
 assertTrue(decorator.containsKey((Short)2));
 assertFalse(decorator.containsKey((Short)3));
 }

 @Test
 public void testPutAll() {
 TShortByteMap map = new TShortByteMap();
 map.put((Short)1, (Byte)1);
 map.put((Short)2, (Byte)2);
 decorator.putAll(map.entrySet());

 assertEquals(2, decorator.size());
 assertTrue(decorator.containsKey((Short)1));
 assertTrue(decorator.containsKey((Short)2));
 assertFalse(decorator.containsKey((Short)3));
 }

 @Test
 public void testExternalizable() {
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
 try {
 decorator.writeExternal(new ObjectOutputStream(out));
 out.reset();
 decorator = new TShortByteMapDecorator();
 decorator.readExternal(new ObjectInputStream(new ByteArrayInputStream(out.toByteArray())));
 } catch (Exception e) {
 e.printStackTrace();
 fail("Write/Read failed: " + e.getMessage());
 }
 assertEquals(0, decorator.size());
 }
}