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
 public void testEmptyMap() {
 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey((Short)null));
 assertFalse(decorator.containsKey((Short)1));
 assertFalse(decorator.containsValue((Byte)0));
 }

 @Test
 public void testPut() {
 Short key1 = (Short)1;
 Byte value1 = (Byte)1;
 Byte value2 = (Byte)2;
 decorator.put(key1, value1);
 decorator.put(key1, value2);
 assertEquals(1, decorator.size());
 assertSame(value2, decorator.get(key1));
 assertNull(decorator.get((Short)2));
 }

 @Test
 public void testGetNullKey() {
 assertNull(decorator.get((Short)null));
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullKey() {
 decorator.put(null, (Byte)1);
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 decorator.put((Short)1, null);
 }

 @Test
 public void testClear() {
 decorator.put((Short)1, (Byte)1);
 decorator.put((Short)2, (Byte)2);
 decorator.clear();
 assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 Short key = (Short)1;
 decorator.put(key, (Byte)1);
 Byte removedValue = decorator.remove(key);
 assertEquals(1, decorator.size());
 assertSame(removedValue, decorator.get(key));
 assertNull(decorator.get(key));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
 decorator.remove((Short)null);
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
 assertEquals(0, entrySet.size());
 decorator.put((Short)1, (Byte)1);
 decorator.put((Short)2, (Byte)2);
 entrySet = decorator.entrySet();
 Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();
 assertSame((Entry<Short, Byte>)iterator.next(), new AbstractMap.SimpleEntry<Short, Byte>((Short)1, (Byte)1));
 }

 @Test
 public void testContainsValue() {
 decorator.put((Short)1, (Byte)1);
 assertTrue(decorator.containsValue((Byte)1));
 }

 @Test
 public void testContainsKey() {
 decorator.put((Short)1, (Byte)1);
 assertTrue(decorator.containsKey((Short)1));
 }

 @Test
 public void testSize() {
 decorator.put((Short)1, (Byte)1);
 decorator.put((Short)2, (Byte)2);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 decorator.put((Short)1, (Byte)1);
 assertFalse(decorator.isEmpty());
 decorator.clear();
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap anotherMap = new TShortByteMap();
 anotherMap.put((Short)1, (Byte)1);
 anotherMap.put((Short)2, (Byte)2);
 decorator.putAll(anotherMap);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 out.close();
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 TShortByteMapDecorator decoratorFromStream = (TShortByteMapDecorator)in.readObject();
 in.close();
 assertEquals(decorator, decoratorFromStream);
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
 TShortByteMapDecorator cloned = (TShortByteMapDecorator)decorator.clone();
 assertEquals(decorator, cloned);
 }
}