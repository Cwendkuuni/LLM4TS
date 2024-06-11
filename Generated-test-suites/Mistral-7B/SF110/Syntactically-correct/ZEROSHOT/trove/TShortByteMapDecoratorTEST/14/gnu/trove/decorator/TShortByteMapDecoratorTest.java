import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.TShortByteMapImpl;
import gnu.trove.map.view.TShortByteMapBackingMap;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TShortByteMapImpl();
 decorator = new TShortByteMapDecorator(_map);
 }

 @After
 public void tearDown() {
 decorator = null;
 _map = null;
 }

 @Test
 public void testConstructor() {
 assertNotNull(decorator);
 assertEquals(decorator.size(), _map.size());
 }

 @Test
 public void testGetMap() {
 TShortByteMap actualMap = decorator.getMap();
 assertEquals(actualMap, _map);
 }

 @Test
 public void testPut() {
 Short key = Short.valueOf((short) 1);
 Byte value = Byte.valueOf((byte) 1);
 Byte existingValue = Byte.valueOf((byte) 2);

 decorator.put(key, value);
 assertEquals(decorator.get(key), value);

 decorator.put(key, existingValue);
 assertEquals(decorator.get(key), existingValue);

 Short noEntryKey = _map.getNoEntryKey();
 Byte noEntryValue = _map.getNoEntryValue();

 decorator.put(noEntryKey, value);
 assertNull(decorator.get(noEntryKey));
 assertEquals(decorator.size(), _map.size());
 }

 @Test
 public void testGet() {
 Short key = Short.valueOf((short) 1);
 Byte value = Byte.valueOf((byte) 1);
 _map.put((short) 1, value);

 assertEquals(decorator.get(key), value);

 Short noKey = Short.valueOf((short) 2);
 assertNull(decorator.get(noKey));
 }

 @Test
 public void testClear() {
 decorator.clear();
 assertEquals(decorator.size(), 0);
 }

 @Test
 public void testRemove() {
 Short key = Short.valueOf((short) 1);
 Byte value = Byte.valueOf((byte) 1);
 _map.put((short) 1, value);

 Byte removedValue = decorator.remove(key);
 assertEquals(removedValue, value);
 assertNull(decorator.get(key));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveWithNullKey() {
 decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
 assertFalse(entrySet.isEmpty());

 Short key = Short.valueOf((short) 1);
 Byte value = Byte.valueOf((byte) 1);
 Byte existingValue = Byte.valueOf((byte) 2);

 decorator.put(key, value);
 Entry<Short, Byte> entry = decorator.entrySet().iterator().next();
 assertEquals(entry.getKey(), key);
 assertEquals(entry.getValue(), value);

 decorator.put(key, existingValue);
 entry = decorator.entrySet().iterator().next();
 assertEquals(entry.getKey(), key);
 assertEquals(entry.getValue(), existingValue);
 }

 @Test
 public void testContainsValue() {
 Byte value = Byte.valueOf((byte) 1);
 assertTrue(decorator.containsValue(value));
 }

 @Test
 public void testContainsKey() {
 Short key = Short.valueOf((short) 1);
 assertTrue(decorator.containsKey(key));
 }

 @Test
 public void testSize() {
 int size = decorator.size();
 assertEquals(size, _map.size());
 }

 @Test
 public void testIsEmpty() {
 assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap map = new TShortByteMapImpl();
 map.put((short) 1, Byte.valueOf((byte) 1));
 map.put((short) 2, Byte.valueOf((byte) 2));
 decorator.putAll(map);

 assertEquals(decorator.size(), map.size());
 assertEquals(decorator.get((short) 1), Byte.valueOf((byte) 1));
 assertEquals(decorator.get((short) 2), Byte.valueOf((byte) 2));
 }

 @Test
 public void testExternalizable() {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 byte[] bytes = baos.toByteArray();
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TShortByteMapDecorator deserializedDecorator = (TShortByteMapDecorator)in.readObject();
 assertEquals(deserializedDecorator.size(), decorator.size());
 }

 @Test
 public void testCloneable() {
 try {
 TShortByteMapDecorator clonedDecorator = (TShortByteMapDecorator)decorator.clone();
 assertEquals(clonedDecorator.size(), decorator.size());
 } catch (CloneNotSupportedException e) {
 fail("Cloneable interface should not throw CloneNotSupportedException");
 }
}
}