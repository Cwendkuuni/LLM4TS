package gnu.trove.decorator.test;

import gnu.trove.map.TShortByteMap;
import gnu.trove.map.TShortByteMapImpl;
import gnu.trove.iterator.TShortByteIterator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator _decorator;

 @Before
 public void setUp() {
 _map = new TShortByteMapImpl();
 _decorator = new TShortByteMapDecorator(_map);
 }

 @After
 public void tearDown() {
 _map.clear();
 _decorator = null;
 }

 @Test
 public void testEmptyMap() {
 assertEquals(0, _decorator.size());
 assertNull(_decorator.get(Short.MIN_VALUE));
 assertFalse(_decorator.containsKey(Short.MIN_VALUE));
 }

 @Test
 public void testPut() {
 _decorator.put((short)1, (byte)'A');
 _decorator.put((short)2, (byte)'B');
 assertEquals(2, _decorator.size());
 assertEquals((byte)'A', _decorator.get((short)1));
 assertEquals((byte)'B', _decorator.get((short)2));
 assertNull(_decorator.get(Short.MIN_VALUE));
 assertTrue(_decorator.containsKey((short)1));
 assertTrue(_decorator.containsKey((short)2));
 assertFalse(_decorator.containsKey(Short.MIN_VALUE));
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullKey() {
 _decorator.put(null, (byte)'C');
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 _decorator.put((short)1, null);
 }

 @Test
 public void testClear() {
 _decorator.put((short)1, (byte)'A');
 _decorator.put((short)2, (byte)'B');
 _decorator.clear();
 assertEquals(0, _decorator.size());
 assertNull(_decorator.get(Short.MIN_VALUE));
 }

 @Test
 public void testRemove() {
 _decorator.put((short)1, (byte)'A');
 _decorator.put((short)2, (byte)'B');
 _decorator.put((short)3, (byte)'C');
 _decorator.remove((short)1);
 assertEquals(2, _decorator.size());
 assertNull(_decorator.get((short)1));
 assertTrue(_decorator.containsKey((short)2));
 assertTrue(_decorator.containsKey((short)3));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
 _decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Short, Byte>> entrySet = _decorator.entrySet();
 assertEquals(0, entrySet.size());
 _decorator.put((short)1, (byte)'A');
 _decorator.put((short)2, (byte)'B');
 assertEquals(2, entrySet.size());
 Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();
 assertTrue(iterator.hasNext());
 Entry<Short, Byte> entry = iterator.next();
 assertEquals((short)1, entry.getKey());
 assertEquals((byte)'A', entry.getValue());
 assertTrue(iterator.hasNext());
 entry = iterator.next();
 assertEquals((short)2, entry.getKey());
 assertEquals((byte)'B', entry.getValue());
 }

 @Test
 public void testContainsValue() {
 _decorator.put((short)1, (byte)'A');
 _decorator.put((short)1, (byte)'B');
 assertTrue(_decorator.containsValue((byte)'A'));
 assertTrue(_decorator.containsValue((byte)'B'));
 assertFalse(_decorator.containsValue(Byte.MIN_VALUE));
 }

 @Test
 public void testContainsKey() {
 _decorator.put((short)1, (byte)'A');
 _decorator.put((short)2, (byte)'B');
 assertTrue(_decorator.containsKey((short)1));
 assertTrue(_decorator.containsKey((short)2));
 assertFalse(_decorator.containsKey(Short.MIN_VALUE));
 }

 @Test
 public void testSize() {
 _decorator.put((short)1, (byte)'A');
 _decorator.put((short)2, (byte)'B');
 _decorator.put((short)3, (byte)'C');
 assertEquals(3, _decorator.size());
 }

 @Test
 public void testIsEmpty() {
 assertEquals(true, _decorator.isEmpty());
 _decorator.put((short)1, (byte)'A');
 assertEquals(false, _decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap anotherMap = new TShortByteMapImpl();
 anotherMap.put((short)1, (byte)'A');
 anotherMap.put((short)2, (byte)'B');
 _decorator.putAll(anotherMap);
 assertEquals(2, _decorator.size());
 assertEquals((byte)'A', _decorator.get((short)1));
 assertEquals((byte)'B', _decorator.get((short)2));
 }
}