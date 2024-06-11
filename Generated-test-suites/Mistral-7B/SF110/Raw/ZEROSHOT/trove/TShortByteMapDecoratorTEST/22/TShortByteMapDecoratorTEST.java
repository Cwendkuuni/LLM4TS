import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import gnu.trove.map.TShortByteMap;
import gnu.trove.map.TShortByteMapImpl;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.iterator.TShortByteMapIterator;
import gnu.trove.map.view.TShortByteMapView;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TShortByteMapImpl();
 decorator = new TShortByteMapDecorator(_map);
 }

 @Test
 public void testConstructor() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator();
 Assert.assertNotNull(decorator);
 Assert.assertNull(decorator.getMap());
 }

 @Test
 public void testConstructorWithMap() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(_map);
 Assert.assertNotNull(decorator);
 Assert.assertEquals(_map, decorator.getMap());
 }

 @Test
 public void testPut() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 TShortByteMap map = decorator.getMap();
 TShortByteMapView view = map.view();
 TShortByteIterator iterator = view.iterator();

 iterator.advance();
 assertEquals(1, iterator.key());
 assertEquals(Byte.valueOf((byte)1), iterator.value());

 iterator.advance();
 assertEquals(2, iterator.key());
 assertEquals(Byte.valueOf((byte)2), iterator.value());
 }

 @Test(expected = NullPointerException.class)
 public void testNullKeyPut() {
 decorator.put(null, (byte)1);
 }

 @Test(expected = NullPointerException.class)
 public void testNullValuePut() {
 decorator.put((short)1, null);
 }

 @Test
 public void testGet() {
 decorator.put((short)1, (byte)1);
 assertEquals(Byte.valueOf((byte)1), decorator.get((short)1));
 assertEquals(null, decorator.get(null));
 }

 @Test
 public void testClear() {
 decorator.put((short)1, (byte)1);
 decorator.clear();
 assertFalse(decorator.containsKey((short)1));
 }

 @Test
 public void testRemove() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 assertEquals(Byte.valueOf((byte)1), decorator.remove((short)1));
 assertTrue(decorator.containsKey((short)2));
 }

 @Test(expected = NullPointerException.class)
 public void testNullKeyRemove() {
 decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
 assertEquals(0, entrySet.size());
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 assertEquals(2, entrySet.size());
 }

 @Test
 public void testContainsValue() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)1);
 assertTrue(decorator.containsValue(Byte.valueOf((byte)1)));
 assertFalse(decorator.containsValue(Byte.valueOf((byte)2)));
 }

 @Test
 public void testContainsKey() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)1);
 assertTrue(decorator.containsKey((short)1));
 assertFalse(decorator.containsKey((short)3));
 }

 @Test
 public void testSize() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 decorator.clear();
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap map = new TShortByteMapImpl();
 map.put((short)1, (byte)1);
 map.put((short)2, (byte)2);
 decorator.putAll(map);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testWrapKey() {
 assertEquals((short)1, decorator.wrapKey((short)1));
 }

 @Test
 public void testUnwrapKey() {
 assertEquals((short)1, decorator.unwrapKey(decorator.wrapKey((short)1)));
 }

 @Test
 public void testWrapValue() {
 assertEquals((byte)1, decorator.wrapValue((byte)1));
 }

 @Test
 public void testUnwrapValue() {
 assertEquals((byte)1, decorator.unwrapValue(decorator.wrapValue((byte)1)));
 }

 @Test
 public void testExternalizable() {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 byte[] bytes = baos.toByteArray();
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TShortByteMapDecorator decorator2 = new TShortByteMapDecorator();
 decorator2.readExternal(in);
 TShortByteMap map2 = decorator2.getMap();
 assertEquals(_map, map2);
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
 TShortByteMapDecorator decoratorCloned = (TShortByteMapDecorator)decorator.clone();
 assertEquals(_map, decoratorCloned.getMap());
 }
}