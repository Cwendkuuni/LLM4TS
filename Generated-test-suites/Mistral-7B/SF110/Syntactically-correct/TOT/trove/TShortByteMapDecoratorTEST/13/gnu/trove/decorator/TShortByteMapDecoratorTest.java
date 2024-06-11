import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.view.TShortByteMapView;
import gnu.trove.map.hash.TShortByteHashMap;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TShortByteHashMap();
 decorator = new TShortByteMapDecorator(_map);
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
 decorator.put((short) 3, (byte) null);
 decorator.put((short) null, (byte) 0);
 assertFalse(decorator.isEmpty());
 assertEquals(4, decorator.size());
 assertEquals((byte) 1, decorator.get((short) 1));
 assertEquals((byte) 2, decorator.get((short) 2));
 assertNull(decorator.get((short) 3));
 assertNull(decorator.get((short) null));
 TShortByteMapView view = decorator.entrySet().iterator().next().getValue();
 assertEquals(Byte.valueOf((byte) 1), view.getKey());
 assertEquals(Byte.valueOf((byte) 1), decorator.put(Short.parseShort("1"), Byte.valueOf((byte) 2)));
 }

 @Test
 public void testClear() {
 decorator.clear();
 assertTrue(decorator.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testNullKeyPut() {
 decorator.put(null, (byte) 1);
 }

 @Test(expected = NullPointerException.class)
 public void testNullValuePut() {
 decorator.put((short) 1, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeKeyPut() {
 decorator.put((short) Short.MIN_VALUE - 1, (byte) 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateKeyPut() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 1, (byte) 2);
 }

 @Test
 public void testRemove() {
 decorator.put((short) 1, (byte) 1);
 decorator.remove((short) 1);
 assertNull(decorator.get((short) 1));
 }

 @Test(expected = NullPointerException.class)
 public void testNullKeyRemove() {
 decorator.remove(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeKeyRemove() {
 decorator.remove((short) Short.MIN_VALUE - 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNonExistentKeyRemove() {
 decorator.remove((short) 100);
 }

 @Test
 public void testContainsValue() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 1);
 assertTrue(decorator.containsValue(Byte.valueOf((byte) 1)));
 }

 @Test
 public void testContainsKey() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);
 assertTrue(decorator.containsKey((short) 1));
 assertFalse(decorator.containsKey((short) 3));
 }

 @Test
 public void testSize() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);
 decorator.put((short) 3, (byte) null);
 decorator.put((short) null, (byte) 0);
 assertEquals(4, decorator.size());
 }

 @Test
 public void testPutAll() {
 TShortByteMap map = new TShortByteHashMap();
 map.put((short) 1, (byte) 1);
 map.put((short) 2, (byte) 2);
 decorator.putAll(map);
 assertEquals(2, decorator.size());
 assertEquals((byte) 1, decorator.get((short) 1));
 assertEquals((byte) 2, decorator.get((short) 2));
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 TShortByteMapDecorator decorator2 = new TShortByteMapDecorator();
 decorator2.readExternal(in);
 assertEquals(decorator.size(), decorator2.size());
 }
}