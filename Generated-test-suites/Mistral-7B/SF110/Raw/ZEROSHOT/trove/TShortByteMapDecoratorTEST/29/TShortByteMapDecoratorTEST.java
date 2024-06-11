import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TShortByteMap;
import gnu.trove.map.TShortByteMapImpl;
import gnu.trove.iterator.TShortByteIterator;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

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
 _map.clear();
 decorator = null;
 }

 @Test
 public void testEmptyMap() {
 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey((short) 0));
 assertNull(decorator.get((short) 0));
 }

 @Test
 public void testPut() {
 decorator.put((short) 1, (byte) 1);
 assertEquals(1, decorator.size());
 assertTrue(decorator.containsKey((short) 1));
 assertEquals((byte) 1, decorator.get((short) 1));

 decorator.put((short) 2, (byte) 2);
 assertEquals(2, decorator.size());
 assertTrue(decorator.containsKey((short) 2));
 assertEquals((byte) 2, decorator.get((short) 2));

 decorator.put((short) 1, (byte) 2);
 assertEquals(2, decorator.size());
 assertFalse(decorator.containsKey((short) 1));
 assertEquals((byte) 2, decorator.get((short) 1));
 }

 @Test
 public void testGetNullKey() {
 assertNull(decorator.get((Object) null));
 }

 @Test(expected = ClassCastException.class)
 public void testGetInvalidKeyType() {
 decorator.put((short) 1, (byte) 1);
 decorator.get("invalidKeyType");
 }

 @Test
 public void testClear() {
 decorator.put((short) 1, (byte) 1);
 decorator.clear();
 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey((short) 1));
 assertNull(decorator.get((short) 1));
 }

 @Test
 public void testRemove() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);
 assertEquals(2, decorator.size());
 assertTrue(decorator.containsKey((short) 1));
 assertTrue(decorator.containsKey((short) 2));

 decorator.remove((Object) null);
 assertEquals(2, decorator.size());
 assertFalse(decorator.containsKey((short) 1));
 assertFalse(decorator.containsKey((short) 2));

 decorator.remove((short) 1);
 assertEquals(1, decorator.size());
 assertFalse(decorator.containsKey((short) 1));
 assertTrue(decorator.containsKey((short) 2));

 decorator.remove((short) 2);
 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey((short) 1));
 assertFalse(decorator.containsKey((short) 2));
 }

 @Test
 public void testEntrySet() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
 int size = entrySet.size();

 assertTrue(entrySet.contains(new Entry<Short, Byte>() {
 @Override
 public Short getKey() {
 return (short) 1;
 }

 @Override
 public Byte getValue() {
 return (byte) 1;
 }

 @Override
 public boolean equals(Object o) {
 if (o instanceof Map.Entry) {
 Map.Entry<Short, Byte> entry = (Map.Entry<Short, Byte>) o;
 return entry.getKey().equals(getKey()) && entry.getValue().equals(getValue());
 }
 return false;
 }

 @Override
 public int hashCode() {
 return getKey().hashCode() + getValue().hashCode();
 }
 }));

 assertTrue(entrySet.contains(new Entry<Short, Byte>() {
 @Override
 public Short getKey() {
 return (short) 2;
 }

 @Override
 public Byte getValue() {
 return (byte) 2;
 }

 @Override
 public boolean equals(Object o) {
 if (o instanceof Map.Entry) {
 Map.Entry<Short, Byte> entry = (Map.Entry<Short, Byte>) o;
 return entry.getKey().equals(getKey()) && entry.getValue().equals(getValue());
 }
 return false;
 }

 @Override
 public int hashCode() {
 return getKey().hashCode() + getValue().hashCode();
 }
 }));

 assertFalse(entrySet.contains(new Entry<Short, Byte>() {
 @Override
 public Short getKey() {
 return (short) 3;
 }

 @Override
 public Byte getValue() {
 return (byte) 3;
 }

 @Override
 public boolean equals(Object o) {
 if (o instanceof Map.Entry) {
 Map.Entry<Short, Byte> entry = (Map.Entry<Short, Byte>) o;
 return entry.getKey().equals(getKey()) && entry.getValue().equals(getValue());
 }
 return false;
 }

 @Override
 public int hashCode() {
 return getKey().hashCode() + getValue().hashCode();
 }
 }));

 for (Entry<Short, Byte> entry : entrySet) {
 assertEquals(entry.getKey(), decorator.get(entry.getKey()));
 assertEquals(entry.getValue(), decorator.get(entry.getKey()));
 }

 entrySet.clear();
 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey((short) 1));
 assertFalse(decorator.containsKey((short) 2));
 }

 @Test
 public void testContainsValue() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 assertTrue(decorator.containsValue((byte) 1));
 assertTrue(decorator.containsValue((byte) 2));

 assertFalse(decorator.containsValue((byte) 3));
 }

 @Test
 public void testContainsKey() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 assertTrue(decorator.containsKey((short) 1));
 assertTrue(decorator.containsKey((short) 2));

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
 assertTrue(decorator.isEmpty());
 decorator.put((short) 1, (byte) 1);
 assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap map = new TShortByteMapImpl();
 map.put((short) 3, (byte) 3);
 map.put((short) 4, (byte) 4);
 decorator.putAll(map);
 assertEquals(2, decorator.size());
 assertTrue(decorator.containsKey((short) 3));
 assertTrue(decorator.containsKey((short) 4));
 }

 @Test
 public void testExternalization() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ObjectOutputStream objOut = new ObjectOutputStream(out);
 decorator.writeExternal(objOut);
 objOut.close();

 ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
 ObjectInputStream objIn = new ObjectInputStream(in);
 TShortByteMapDecorator tempDecorator = (TShortByteMapDecorator) objIn.readObject();
 objIn.close();

 assertEquals(decorator.size(), tempDecorator.size());
 for (Entry<Short, Byte> entry : decorator.entrySet()) {
 assertEquals(entry.getKey(), tempDecorator.get(entry.getKey()));
 assertEquals(entry.getValue(), tempDecorator.get(entry.getKey()));
 }
 }
}