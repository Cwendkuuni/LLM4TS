package tests;

import gnu.trove.decorator.TShortByteMapDecorator;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(_map);
 }

 @After
 public void tearDown() {
 _map.clear();
 decorator = null;
 }

 @Test
 public void testConstructor() {
 TShortByteMapDecorator emptyDecorator = new TShortByteMapDecorator();
 assertNotNull(emptyDecorator);
 assertTrue(emptyDecorator.isEmpty());
 }

 @Test
 public void testConstructorWithMap() {
 TShortByteMap map = new TShortByteMap();
 map.put((short) 1, (byte) 1);
 TShortByteMapDecorator decoratorWithMap = new TShortByteMapDecorator(map);
 assertEquals(1, decoratorWithMap.size());
 assertSame((Short)Integer.valueOf((short) 1), decoratorWithMap.keySet().iterator().next());
 assertEquals((Byte)Byte.valueOf((byte) 1), decoratorWithMap.get((Short)Integer.valueOf((short) 1)));
 }

 @Test
 public void testGetMap() {
 TShortByteMap map = decorator.getMap();
 assertEquals(decorator, new TShortByteMapDecorator(map));
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullKey() {
 decorator.put((Short) null, (Byte) Byte.valueOf((byte) 1));
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 decorator.put((Short) Integer.valueOf((short) 1), (Byte) null);
 }

 @Test
 public void testPut() {
 decorator.put((Short) Integer.valueOf((short) 1), (Byte) Byte.valueOf((byte) 1));
 assertEquals(1, decorator.size());
 assertEquals((Byte)Byte.valueOf((byte) 1), decorator.get((Short)Integer.valueOf((short) 1)));
 }

 @Test
 public void testGetNullKey() {
 assertNull(decorator.get(null));
 }

 @Test
 public void testGetWrongKeyType() {
 assertNull(decorator.get(Integer.valueOf(1)));
 }

 @Test
 public void testGetCorrectKey() {
 decorator.put((Short) Integer.valueOf((short) 1), (Byte) Byte.valueOf((byte) 1));
 assertEquals((Byte)Byte.valueOf((byte) 1), decorator.get((Short)Integer.valueOf((short) 1)));
 }

 @Test
 public void testClear() {
 decorator.put((Short) Integer.valueOf((short) 1), (Byte) Byte.valueOf((byte) 1));
 decorator.clear();
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemoveNullKey() {
 assertNull(decorator.remove(null));
 }

 @Test(expected = ClassCastException.class)
 public void testRemoveWrongKeyType() {
 decorator.put((Short) Integer.valueOf((short) 1), (Byte) Byte.valueOf((byte) 1));
 assertNull(decorator.remove(Integer.valueOf(1)));
 }

 @Test
 public void testRemoveCorrectKey() {
 decorator.put((Short) Integer.valueOf((short) 1), (Byte) Byte.valueOf((byte) 1));
 assertEquals((Byte)Byte.valueOf((byte) 1), decorator.remove((Short)Integer.valueOf((short) 1)));
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
 assertEquals(0, entrySet.size());
 }

 @Test
 public void testContainsValue() {
 decorator.put((Short) Integer.valueOf((short) 1), (Byte) Byte.valueOf((byte) 1));
 assertTrue(decorator.containsValue((Byte)Byte.valueOf((byte) 1)));
 }

 @Test
 public void testContainsKey() {
 decorator.put((Short) Integer.valueOf((short) 1), (Byte) Byte.valueOf((byte) 1));
 assertTrue(decorator.containsKey((Short)Integer.valueOf((short) 1)));
 }

 @Test
 public void testSize() {
 decorator.put((Short) Integer.valueOf((short) 1), (Byte) Byte.valueOf((byte) 1));
 assertEquals(1, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 decorator.put((Short) Integer.valueOf((short) 1), (Byte) Byte.valueOf((byte) 1));
 assertTrue(decorator.isEmpty() == false);
 decorator.clear();
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap map1 = new TShortByteMap();
 map1.put((short) 1, (byte) 1);
 map1.put((short) 2, (byte) 2);
 TShortByteMap map2 = new TShortByteMap();
 map2.put((short) 3, (byte) 3);
 TShortByteMapDecorator decorator1 = new TShortByteMapDecorator(map1);
 TShortByteMapDecorator decorator2 = new TShortByteMapDecorator();
 decorator2.putAll(decorator1);
 assertEquals(2, decorator2.size());
 assertEquals((Byte)Byte.valueOf((byte) 1), decorator2.get((Short)Integer.valueOf((short) 1)));
 assertEquals((Byte)Byte.valueOf((byte) 2), decorator2.get((Short)Integer.valueOf((short) 2)));
 }

 @Test
 public void testIterator() {
 decorator.put((Short) Integer.valueOf((short) 1), (Byte) Byte.valueOf((byte) 1));
 decorator.put((Short) Integer.valueOf((short) 2), (Byte) Byte.valueOf((byte) 2));
 TShortByteIterator it = decorator.entrySet().iterator();
 assertEquals((Short)Integer.valueOf((short) 1), it.next().getKey());
 assertEquals((Short)Integer.valueOf((short) 2), it.next().getKey());
 }

 @Test(expected = NoSuchElementException.class)
 public void testIteratorNoNextElement() {
 decorator.put((Short) Integer.valueOf((short) 1), (Byte) Byte.valueOf((byte) 1));
 decorator.put((Short) Integer.valueOf((short) 2), (Byte) Byte.valueOf((byte) 2));
 TShortByteIterator it = decorator.entrySet().iterator();
 it.next();
 it.next();
 it.next();
 }

 @Test
 public void testRemoveIteratorElement() {
 decorator.put((Short) Integer.valueOf((short) 1), (Byte) Byte.valueOf((byte) 1));
 decorator.put((Short) Integer.valueOf((short) 2), (Byte) Byte.valueOf((byte) 2));
 TShortByteIterator it = decorator.entrySet().iterator();
 it.next();
 it.remove();
 assertEquals(1, decorator.size());
 }

 @Test(expected = ConcurrentModificationException.class)
 public void testRemoveIteratorElementConcurrently() {
 decorator.put((Short) Integer.valueOf((short) 1), (Byte) Byte.valueOf((byte) 1));
 decorator.put((Short) Integer.valueOf((short) 2), (Byte) Byte.valueOf((byte) 2));
 TShortByteIterator it = decorator.entrySet().iterator();
 it.next();
 decorator.put((Short) Integer.valueOf((short) 3), (Byte) Byte.valueOf((byte) 3));
 it.remove();
 }

 @Test
 public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
 decorator.writeExternal(new ObjectOutput() {
 @Override
 public void writeByte(int b) throws IOException {
 out.write((byte) b);
 }

 @Override
 public void writeObject(Object obj) throws IOException {
 out.writeObject(obj);
 }

 @Override
 public void writeBytes(byte[] b) throws IOException {
 out.write(b);
 }

 @Override
 public void writeInt(int i) throws IOException {
 out.writeInt(i);
 }

 @Override
 public void writeLong(long l) throws IOException {
 out.writeLong(l);
 }

 @Override
 public void writeFloat(float f) throws IOException {
 out.writeFloat(f);
 }

 @Override
 public void writeDouble(double d) throws IOException {
 out.writeDouble(d);
 }

 @Override
 public void writeBoolean(boolean b) throws IOException {
 out.writeBoolean(b);
 }

 @Override
 public void writeChars(String s) throws IOException {
 out.writeBytes(s.getBytes());
 }

 @Override
 public void writeUTF(String string) throws IOException {
 out.writeUTF(string);
 }
 });
 TShortByteMapDecorator readDecorator = new TShortByteMapDecorator();
 readDecorator.readExternal(new ObjectInput() {
 @Override
 public void readByte() throws IOException, ClassNotFoundException {
 }

 @Override
 public void readObject(Class<?> c) throws IOException, ClassNotFoundException {
 readDecorator._map = (TShortByteMap)readObject();
 }

 @Override
 public void readBytes(byte[] b) throws IOException {
 }

 @Override
 public int readInt() throws IOException, ClassNotFoundException {
 return 0;
 }

 @Override
 public long readLong() throws IOException, ClassNotFoundException {
 return 0;
 }

 @Override
 public float readFloat() throws IOException, ClassNotFoundException {
 return 0;
 }

 @Override
 public double readDouble() throws IOException, ClassNotFoundException {
 return 0;
 }

 @Override
 public boolean readBoolean() throws IOException, ClassNotFoundException {
 return false;
 }

 @Override
 public String readUTF() throws IOException, ClassNotFoundException {
 return "";
 }
 });
 assertEquals(decorator, readDecorator);
 }
}