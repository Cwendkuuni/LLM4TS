import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TShortByteMap;
import gnu.trove.map.TShortByteMapImpl;
import gnu.trove.iterator.TShortByteIterator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;

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
 assertTrue(decorator.isEmpty());
 assertNull(decorator.get(Short.MIN_VALUE));
 assertNull(decorator.get(Short.MAX_VALUE));
 }

 @Test
 public void testPut() {
 final byte value1 = 1;
 decorator.put((short)1, value1);
 assertEquals(1, decorator.size());
 assertSame(value1, decorator.get((short)1));

 final byte value2 = 2;
 decorator.put((short)2, value2);
 assertEquals(2, decorator.size());
 assertSame(value1, decorator.get((short)1));
 assertSame(value2, decorator.get((short)2));
 }

 @Test
 public void testClear() {
 decorator.clear();
 assertEquals(0, decorator.size());
 assertNull(decorator.get(Short.MIN_VALUE));
 assertNull(decorator.get(Short.MAX_VALUE));
 }

 @Test
 public void testPutAll() {
 final TShortByteMap mapToAdd = new TShortByteMapImpl();
 mapToAdd.put((short)3, (byte)3);
 mapToAdd.put((short)4, (byte)4);
 decorator.putAll(mapToAdd);

 assertEquals(4, decorator.size());
 assertSame((byte)3, decorator.get((short)3));
 assertSame((byte)4, decorator.get((short)4));
 }

 @Test
 public void testContainsKey() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);

 assertTrue(decorator.containsKey((short)1));
 assertTrue(decorator.containsKey((short)2));
 assertFalse(decorator.containsKey((short)3));
 }

 @Test
 public void testContainsValue() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);

 assertTrue(decorator.containsValue((byte)1));
 assertTrue(decorator.containsValue((byte)2));
 assertFalse(decorator.containsValue((byte)3));
 }

 @Test
 public void testEntrySet() {
 final Set<Entry<Short, Byte>> entrySet = decorator.entrySet();

 assertEquals(decorator.size(), entrySet.size());

 final Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();
 iterator.next(); // skip entry for key = NO_ENTRY_KEY

 Entry<Short, Byte> entry = iterator.next();
 assertEquals((short)1, entry.getKey());
 assertEquals((byte)1, entry.getValue());

 entry = iterator.next();
 assertEquals((short)2, entry.getKey());
 assertEquals((byte)2, entry.getValue());
 }

 @Test
 public void testRemove() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);

 assertEquals(2, decorator.size());

 decorator.remove((short)1);
 assertEquals(1, decorator.size());
 assertNull(decorator.get((short)1));
 }

 @Test
 public void testExternalizable() {
 final byte testValue = 0x05;
 decorator.put((short)5, testValue);

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ObjectOutput output = new ObjectOutputStream(outputStream);
 decorator.writeExternal(output);
 output.close();

 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 ObjectInput input = new ObjectInputStream(inputStream);
 TShortByteMapDecorator deserializedDecorator = (TShortByteMapDecorator)input.readObject();
 input.close();

 assertEquals(testValue, deserializedDecorator.get((short)5));
 }
}