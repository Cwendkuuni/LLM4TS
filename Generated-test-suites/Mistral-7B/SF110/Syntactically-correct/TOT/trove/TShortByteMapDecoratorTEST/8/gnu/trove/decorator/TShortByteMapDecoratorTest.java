package gnu.trove.decorator.test;

import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import static org.junit.Assert.*;

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
 assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPut() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 assertEquals(2, decorator.size());
 assertEquals((Byte)Byte.valueOf("1"), decorator.get((Short)Short.valueOf("1")));
 assertEquals((Byte)Byte.valueOf("2"), decorator.get((Short)Short.valueOf("2")));
 assertNull(decorator.get((Short)null));
 }

 @Test
 public void testPutNullKey() {
 assertNull(decorator.put((Short)null, (Byte)null));
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 decorator.put((Short)1, null);
 }

 @Test
 public void testClear() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 decorator.clear();
 assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 assertEquals(2, decorator.size());
 decorator.remove((Short)Short.valueOf("1"));
 assertEquals(1, decorator.size());
 assertNull(decorator.get((Short)Short.valueOf("1")));
 }

 @Test
 public void testRemoveNullKey() {
 assertNull(decorator.remove((Short)null));
 }

 @Test
 public void testEntrySet() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 Set<Entry<Short, Byte>> set = decorator.entrySet();
 assertEquals(2, set.size());
 Iterator<Entry<Short, Byte>> it = set.iterator();
 Entry<Short, Byte> entry = it.next();
 assertSame(decorator.get((Short)Short.valueOf("1")), entry.getValue());
 it.hasNext();
 entry = it.next();
 assertSame(decorator.get((Short)Short.valueOf("2")), entry.getValue());
 }

 @Test
 public void testContainsValue() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 assertTrue(decorator.containsValue((byte)1));
 assertTrue(decorator.containsValue((byte)2));
 }

 @Test
 public void testContainsKey() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 assertTrue(decorator.containsKey((Short)Short.valueOf("1")));
 assertTrue(decorator.containsKey((Short)Short.valueOf("2")));
 assertFalse(decorator.containsKey((Short)Short.valueOf("3")));
 }

 @Test
 public void testExternalization() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 decorator.writeExternal(oos);
 ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 TShortByteMapDecorator dec = (TShortByteMapDecorator)ois.readObject();
 assertEquals(decorator.size(), dec.size());
 for (Entry<Short, Byte> entry : decorator.entrySet()) {
 assertEquals(entry.getKey(), dec.get(entry.getKey()));
 assertEquals(entry.getValue(), dec.get(entry.getKey()));
 }
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 TShortByteMapDecorator orig = new TShortByteMapDecorator(_map);orig.put((short)1, (byte)1);
 orig.put((short)2, (byte)2);TShortByteMapDecorator copy = (TShortByteMapDecorator) orig.clone();assertEquals(orig.size(), copy.size());for (Entry<Short, Byte> entry : orig.entrySet()) {assertEquals(entry.getKey(), copy.get(entry.getKey()));assertEquals(entry.getValue(), copy.get(entry.getKey()));}
 }
}