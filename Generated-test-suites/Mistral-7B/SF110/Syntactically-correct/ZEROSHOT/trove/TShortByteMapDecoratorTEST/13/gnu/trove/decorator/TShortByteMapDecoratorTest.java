import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.io.*;

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
 assertTrue(decorator.isEmpty());
 assertEquals(0, decorator.size());
 assertNull(decorator.get(Short.MIN_VALUE));
 }

 @Test
 public void testPutGet() {
 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);
 decorator.put(key, value);
 assertEquals(1, decorator.size());
 Byte getValue = decorator.get(key);
 assertNotNull(getValue);
 assertEquals(value, getValue);
 }

 @Test
 public void testPutNullKey() {
 try {
 decorator.put(null, Byte.valueOf((byte)1));
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testPutNullValue() {
 Short key = Short.valueOf((short)1);
 decorator.put(key, null);
 assertEquals(1, decorator.size());
 Byte getValue = decorator.get(key);
 assertNull(getValue);
 }

 @Test
 public void testClear() {
 decorator.clear();
 assertTrue(decorator.isEmpty());
 assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);
 decorator.put(key, value);
 assertEquals(1, decorator.size());
 Byte getValue = decorator.get(key);
 assertNotNull(getValue);
 decorator.remove(key);
 assertEquals(0, decorator.size());
 assertNull(decorator.get(key));
 }

 @Test
 public void testRemoveNullKey() {
 Short key = Short.valueOf((short)1);
 decorator.put(key, Byte.valueOf((byte)1));
 decorator.clear();
 try {
 decorator.remove(null);
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
 assertEquals(0, entrySet.size());
 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);
 decorator.put(key, value);
 Entry<Short, Byte> entry = entrySet.iterator().next();
 assertEquals(key, entry.getKey());
 assertEquals(value, entry.getValue());
 }

 @Test
 public void testContainsValue() {
 Byte value = Byte.valueOf((byte)1);
 assertTrue(decorator.containsValue(value));
 }

 @Test
 public void testContainsKey() {
 Short key = Short.valueOf((short)1);
 assertTrue(decorator.containsKey(key));
 }

 @Test
 public void testSize() {
 assertEquals(0, decorator.size());
 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);
 decorator.put(key, value);
 assertEquals(1, decorator.size());
 }

 @Test
 public void testPutAll() {
 TShortByteMap map = new TShortByteMap();
 map.put(Short.valueOf((short)1), Byte.valueOf((byte)1));
 map.put(Short.valueOf((short)2), Byte.valueOf((byte)2));
 decorator.putAll(map);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ObjectOutput objOut = new ObjectOutputStream(out);
 decorator.writeExternal(objOut);
 ObjectInput objIn = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
 TShortByteMapDecorator dec = (TShortByteMapDecorator)objIn.readObject();
 assertNotNull(dec);
 assertEquals(decorator.size(), dec.size());
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
 TShortByteMapDecorator dec = (TShortByteMapDecorator)decorator.clone();
 assertEquals(decorator.size(), dec.size());
 }
}