import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;

public class TShortByteMapDecoratorTest {

 private TShortByteMap map;
 private TShortByteMapDecorator decorator;

 @Before
 public void setUp() {
 map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(map);
 }

 @After
 public void tearDown() {
 decorator = null;
 map = null;
 }

 @Test
 public void testEmptyMap() {
 assertEquals(0, decorator.size());
 assertTrue(decorator.isEmpty());
 assertFalse(decorator.containsKey((Short)null));
 assertFalse(decorator.containsKey((Short)1));
 assertFalse(decorator.containsValue((Byte)null));
 assertFalse(decorator.containsValue((Byte)1));
 }

 @Test
 public void testPut() {
 decorator.put((Short)1, (Byte)1);
 decorator.put((Short)2, (Byte)2);
 assertEquals(2, decorator.size());
 assertTrue(decorator.containsKey((Short)1));
 assertTrue(decorator.containsKey((Short)2));
 assertFalse(decorator.containsKey((Short)null));
 assertTrue(decorator.containsValue((Byte)1));
 assertTrue(decorator.containsValue((Byte)2));
 assertEquals((Byte)1, decorator.get((Short)1));
 assertEquals((Byte)2, decorator.get((Short)2));
 assertNull(decorator.get((Short)null));
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullKey() {
 decorator.put(null, (Byte)1);
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 decorator.put((Short)1, null);
 }

 @Test
 public void testRemove() {
 decorator.put((Short)1, (Byte)1);
 decorator.put((Short)2, (Byte)2);
 decorator.remove((Short)1);
 assertEquals(1, decorator.size());
 assertFalse(decorator.containsKey((Short)1));
 assertTrue(decorator.containsKey((Short)2));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
 decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Short, Byte>> set = decorator.entrySet();
 assertEquals(0, set.size());
 decorator.put((Short)1, (Byte)1);
 decorator.put((Short)2, (Byte)2);
 set = decorator.entrySet();
 Entry<Short, Byte> entry = set.iterator().next();
 assertEquals((Short)1, entry.getKey());
 assertEquals((Byte)1, entry.getValue());
 }

 @Test
 public void testContainsValue() {
 decorator.put((Short)1, (Byte)1);
 decorator.put((Short)2, (Byte)2);
 assertTrue(decorator.containsValue((Byte)1));
 assertTrue(decorator.containsValue((Byte)2));
 assertFalse(decorator.containsValue((Byte)3));
 }

 @Test
 public void testClear() {
 decorator.put((Short)1, (Byte)1);
 decorator.put((Short)2, (Byte)2);
 decorator.clear();
 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey((Short)1));
 assertFalse(decorator.containsKey((Short)2));
 }

 @Test
 public void testPutAll() {
 TShortByteMap map2 = new TShortByteMap();
 map2.put((Short)3, (Byte)3);
 map2.put((Short)4, (Byte)4);
 decorator.putAll(map2);
 assertEquals(2, decorator.size());
 assertTrue(decorator.containsKey((Short)3));
 assertTrue(decorator.containsKey((Short)4));
 assertFalse(decorator.containsKey((Short)1));
 assertFalse(decorator.containsKey((Short)2));
 }

 @Test
 public void testExternalizable() {
 try {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 out.close();
 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInput in = new ObjectInputStream(bais);
 TShortByteMapDecorator decorator2 = (TShortByteMapDecorator)in.readObject();
 in.close();
 assertEquals(decorator.size(), decorator2.size());
 } catch (IOException | ClassNotFoundException e) {
 e.printStackTrace();
 fail();
 }
}

}