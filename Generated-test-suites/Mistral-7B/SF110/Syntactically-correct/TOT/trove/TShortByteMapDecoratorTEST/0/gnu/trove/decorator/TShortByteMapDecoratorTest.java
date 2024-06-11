import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Before
 public void setup() {
 _map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(_map);
 }

 @Test
 public void testNullKeyPut() {
 byte result = decorator.put((Short) null, (Byte) null);
 assertNull(result);
 assertEquals(0, decorator.size());
 }

 @Test
 public void testNullKeyGet() {
 assertNull(decorator.get((Object) null));
 }

 @Test
 public void testNullValuePut() {
 byte key = 1;
 byte result = decorator.put(key, (Byte) null);
 assertNull(result);
 assertEquals(0, decorator.size());
 }

 @Test
 public void testNullValueGet() {
 byte key = 1;
 assertNull(decorator.get(key));
 }

 @Test
 public void testPutAndGet() {
 byte key = 1;
 byte value = 10;
 byte result = decorator.put(key, value);
 assertNull(result);
 byte retrievedValue = decorator.get(key);
 assertEquals(value, retrievedValue);
 }

 @Test
 public void testPutAndGetDifferentKeys() {
 byte key1 = 1;
 byte key2 = 2;
 byte value1 = 10;
 byte value2 = 20;
 byte result1 = decorator.put(key1, value1);
 assertNull(result1);
 byte result2 = decorator.put(key2, value2);
 assertNull(result2);
 byte retrievedValue1 = decorator.get(key1);
 assertEquals(value1, retrievedValue1);
 byte retrievedValue2 = decorator.get(key2);
 assertEquals(value2, retrievedValue2);
 }

 @Test
 public void testClear() {
 byte key = 1;
 byte value = 10;
 decorator.put(key, value);
 decorator.clear();
 assertEquals(0, decorator.size());
 assertNull(decorator.get(key));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 byte key = 1;
 decorator.remove(key);
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
 assertEquals(0, entrySet.size());
 byte key = 1;
 byte value = 10;
 Byte entryValue = decorator.put(key, value);
 Entry<Short, Byte> entry = entrySet.iterator().next();
 assertEquals(key, entry.getKey());
 assertEquals(value, entry.getValue());
 }

 @Test
 public void testContainsValue() {
 byte value = 10;
 assertTrue(decorator.containsValue(value));
 decorator.put((Short) null, value);
 assertFalse(decorator.containsValue(value));
 }

 @Test
 public void testContainsKey() {
 byte key = 1;
 assertTrue(decorator.containsKey(key));
 decorator.remove(key);
 assertFalse(decorator.containsKey(key));
 }

 @Test
 public void testSize() {
 assertEquals(0, decorator.size());
 byte key = 1;
 byte value = 10;
 decorator.put(key, value);
 assertEquals(1, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 assertTrue(decorator.isEmpty());
 byte key = 1;
 byte value = 10;
 decorator.put(key, value);
 assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap anotherMap = new TShortByteMap();
 anotherMap.put(1, (byte) 1);
 anotherMap.put(2, (byte) 2);
 decorator.putAll(anotherMap);
 assertEquals(2, decorator.size());
 byte retrievedValue1 = decorator.get((Short) 1);
 assertEquals(Byte.valueOf((byte) 1), retrievedValue1);
 byte retrievedValue2 = decorator.get((Short) 2);
 assertEquals(Byte.valueOf((byte) 2), retrievedValue2);
 }

 @Test
 public void testExternalizable() {
 try {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(byteStream);
 decorator.writeExternal(out);
 byte[] bytes = byteStream.toByteArray();
 ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);
 ObjectInput in = new ObjectInputStream(byteIn);
 TShortByteMapDecorator newDecorator = (TShortByteMapDecorator) in.readObject();
 assertEquals(decorator.size(), newDecorator.size());
 for (Entry<Short, Byte> e : decorator.entrySet()) {
 assertEquals(e.getKey(), newDecorator.get(e.getKey()));
 assertEquals(e.getValue(), newDecorator.get(e.getKey()));
 }
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException while testing externalizable");
 } catch (ClassNotFoundException e) {
 e.printStackTrace();
 fail("ClassNotFoundException while testing externalizable");
 }
}

 @Test
 public void testCloneable() {
 try {
 TShortByteMapDecorator clonedDecorator = (TShortByteMapDecorator) decorator.clone();
 assertEquals(decorator.size(), clonedDecorator.size());
 for (Entry<Short, Byte> e : decorator.entrySet()) {
 assertEquals(e.getKey(), clonedDecorator.get(e.getKey()));
 assertEquals(e.getValue(), clonedDecorator.get(e.getKey()));
 }
 } catch (CloneNotSupportedException e) {
 e.printStackTrace();
 fail("CloneNotSupportedException while testing cloneable");
 }
}
}