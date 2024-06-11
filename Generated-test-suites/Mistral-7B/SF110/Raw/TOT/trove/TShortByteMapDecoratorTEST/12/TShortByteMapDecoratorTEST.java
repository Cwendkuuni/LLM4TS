import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TShortByteMap;
import gnu.trove.map.TShortByteMapImpl;
import gnu.trove.iterator.TShortByteIterator;
import java.util.Arrays;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TShortByteMapImpl();
 decorator = new TShortByteMapDecorator(_map);
 }

 @Test
 public void testEmptyMap() {
 assertEquals(0, decorator.size());
 assertNull(decorator.get((Short)null));
 assertNull(decorator.get((Short)0));
 assertFalse(decorator.containsKey((Short)0));
 }

 @Test
 public void testPutGet() {
 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);
 decorator.put(key, value);
 assertEquals(1, decorator.size());
 assertEquals(value, decorator.get(key));
 }

 @Test
 public void testPutNullKey() {
 assertNull(decorator.put((Short)null, (Byte)null));
 assertEquals(0, decorator.size());
 }

 @Test
 public void testPutNullValue() {
 Byte value = null;
 Short key = Short.valueOf((short)1);
 decorator.put(key, value);
 assertEquals(1, decorator.size());
 assertNull(decorator.get(key));
 }

 @Test
 public void testPutSameKey() {
 Short key = Short.valueOf((short)1);
 Byte value1 = Byte.valueOf((byte)1);
 decorator.put(key, value1);
 Byte value2 = Byte.valueOf((byte)2);
 decorator.put(key, value2);
 assertEquals(1, decorator.size());
 assertEquals(value2, decorator.get(key));
 }

 @Test
 public void testClear() {
 decorator.clear();
 assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);
 decorator.put(key, value);
 assertEquals(1, decorator.size());
 assertEquals(value, decorator.remove(key));
 assertEquals(0, decorator.size());
 assertNull(decorator.get(key));
 }

 @Test
 public void testRemoveNullKey() {
 assertNull(decorator.remove((Short)null));
 assertEquals(0, decorator.size());
 }

 @Test
 public void testEntrySet() {
 Short key1 = Short.valueOf((short)1);
 Byte value1 = Byte.valueOf((byte)1);
 Short key2 = Short.valueOf((short)2);
 Byte value2 = Byte.valueOf((byte)2);
 decorator.put(key1, value1);
 decorator.put(key2, value2);

 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
 Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();
 Entry<Short, Byte> entry = iterator.next();
 assertEquals(key1, entry.getKey());
 assertEquals(value1, entry.getValue());

 iterator.next();
 assertEquals(key2, iterator.next().getKey());
 assertEquals(value2, iterator.next().getValue());
 }

 @Test
 public void testContainsValue() {
 Byte value = Byte.valueOf((byte)1);
 decorator.put((Short)1, value);
 decorator.put((Short)2, (Byte)null);
 assertTrue(decorator.containsValue(value));
 assertFalse(decorator.containsValue(Byte.valueOf((byte)2)));
 }

 @Test
 public void testContainsKey() {
 Short key = Short.valueOf((short)1);
 Byte value = Byte.valueOf((byte)1);
 decorator.put(key, value);
 assertTrue(decorator.containsKey(key));
 assertFalse(decorator.containsKey((Short)2));
 }

 @Test
 public void testSize() {
 Short key1 = Short.valueOf((short)1);
 Byte value1 = Byte.valueOf((byte)1);
 Short key2 = Short.valueOf((short)2);
 Byte value2 = Byte.valueOf((byte)2);
 decorator.put(key1, value1);
 decorator.put(key2, value2);
 assertEquals(2, decorator.size());
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
 public void testExternalizable() {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 out.close();
 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInput in = new ObjectInputStream(bais);
 TShortByteMapDecorator decoratedMap = (TShortByteMapDecorator)in.readObject();
 in.close();
 assertEquals(decorator.size(), decoratedMap.size());
 for (TShortByteIterator it = decorator.iterator(); it.hasNext(); ) {
 Entry<Short, Byte> entry = it.next();
 assertEquals(entry.getKey(), decoratedMap.get(entry.getKey()));
 assertEquals(entry.getValue(), decoratedMap.get(entry.getKey()));
 }
 }
}