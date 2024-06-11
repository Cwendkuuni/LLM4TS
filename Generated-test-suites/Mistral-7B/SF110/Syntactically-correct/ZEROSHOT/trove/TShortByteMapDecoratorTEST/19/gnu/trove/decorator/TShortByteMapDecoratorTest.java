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
 public void testEmptyMap() {
 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey((Short)null));
 assertFalse(decorator.containsKey((Short)1));
 assertFalse(decorator.containsValue((Byte)null));
 assertFalse(decorator.containsValue((Byte)1));
 assertNull(decorator.get((Short)null));
 assertNull(decorator.get((Short)1));
 }

 @Test
 public void testPut() {
 decorator.put((Short)1, (Byte)1);
 decorator.put((Short)2, (Byte)2);
 assertEquals(2, decorator.size());
 assertTrue(decorator.containsKey((Short)1));
 assertTrue(decorator.containsKey((Short)2));
 assertFalse(decorator.containsKey((Short)0));
 assertFalse(decorator.containsKey((Short)3));
 assertNull(decorator.get((Short)0));
 assertNull(decorator.get((Short)3));
 assertEquals((Byte)1, decorator.get((Short)1));
 assertEquals((Byte)2, decorator.get((Short)2));
 }

 @Test
 public void testPutNullKey() {
 decorator.put((Short)null, (Byte)1);
 assertEquals(1, decorator.size());
 assertNull(decorator.get((Short)null));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testPutNullValue() {
 decorator.put((Short)1, null);
 }

 @Test
 public void testGetNullKey() {
 assertNull(decorator.get((Short)null));
 }

 @Test
 public void testClear() {
 decorator.clear();
 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey((Short)1));
 assertFalse(decorator.containsKey((Short)2));
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

 @Test
 public void testRemoveNullKey() {
 decorator.put((Short)1, (Byte)1);
 decorator.remove((Short)null);
 assertEquals(1, decorator.size());
 assertTrue(decorator.containsKey((Short)1));
 }

 @Test
 public void testRemoveValue() {
 decorator.put((Short)1, (Byte)1);
 decorator.put((Short)1, (Byte)2);
 decorator.remove((Byte)1);
 assertEquals(1, decorator.size());
 assertFalse(decorator.containsValue((Byte)1));
 assertTrue(decorator.containsValue((Byte)2));
 }

 @Test
 public void testContainsValue() {
 decorator.put((Short)1, (Byte)1);
 decorator.put((Short)2, (Byte)2);
 assertTrue(decorator.containsValue((Byte)1));
 assertTrue(decorator.containsValue((Byte)2));
 assertFalse(decorator.containsValue((Byte)0));
 }

 @Test
 public void testContainsKey() {
 decorator.put((Short)1, (Byte)1);
 decorator.put((Short)2, (Byte)2);
 assertTrue(decorator.containsKey((Short)1));
 assertTrue(decorator.containsKey((Short)2));
 assertFalse(decorator.containsKey((Short)0));
 }

 @Test
 public void testSize() {
 decorator.put((Short)1, (Byte)1);
 decorator.put((Short)2, (Byte)2);
 decorator.put((Short)3, (Byte)3);
 assertEquals(3, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 decorator.clear();
 assertTrue(decorator.isEmpty());
 decorator.put((Short)1, (Byte)1);
 assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap map = new TShortByteMap();
 map.put((Short)1, (Byte)1);
 map.put((Short)2, (Byte)2);
 decorator.putAll(map);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
 assertEquals(decorator.size(), entrySet.size());
 TShortByteMapView view = _map.view();
 for (Entry<Short, Byte> entry : entrySet) {
 Short key = entry.getKey();
 Byte value = entry.getValue();
 assertEquals(key, view.get(key));
 assertEquals(value, view.get(key));
 }
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
 decorator.writeExternal(in);
 TShortByteMapDecorator dec = new TShortByteMapDecorator();
 dec.readExternal(in);
 assertEquals(decorator.size(), dec.size());
 for (Short key : decorator.keySet()) {
 assertEquals(decorator.get(key), dec.get(key));
 }
 }
}