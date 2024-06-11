import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.TShortByteMapImpl;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutputStream;
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

 @After
 public void tearDown() {
 _map.clear();
 decorator = null;
 }

 @Test
 public void testEmptyMap() {
 assertEquals(0, decorator.size());
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutGet() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);

 assertEquals((byte)1, decorator.get((short)1));
 assertEquals((byte)2, decorator.get((short)2));

 assertNull(decorator.get(Short.valueOf((short)-1)));
 }

 @Test
 public void testClear() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 decorator.clear();

 assertEquals(0, decorator.size());
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemove() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 decorator.remove((short)1);

 assertEquals((byte)2, decorator.get((short)2));
 assertNull(decorator.get((short)1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetValue() {
 final Short key = Short.valueOf((short)1);
 final Byte value = Byte.valueOf((byte)1);
 decorator.put(key, value);
 final Byte newValue = Byte.valueOf((byte)2);
 decorator.setValueForKey(newValue, key);
 }

 @Test
 public void testEntrySet() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);

 final Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
 assertEquals(2, entrySet.size());

 final Iterator<Entry<Short, Byte>> entryIterator = entrySet.iterator();
 Entry<Short, Byte> entry = entryIterator.next();
 assertEquals((short)1, entry.getKey().shortValue());
 assertEquals((byte)1, entry.getValue().byteValue());

 entry = entryIterator.next();
 assertEquals((short)2, entry.getKey().shortValue());
 assertEquals((byte)2, entry.getValue().byteValue());
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
 public void testContainsKey() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);

 assertTrue(decorator.containsKey(Short.valueOf((short)1)));
 assertTrue(decorator.containsKey(Short.valueOf((short)2)));

 assertFalse(decorator.containsKey(Short.valueOf((short)-1)));
 }

 @Test
 public void testSerializable() throws IOException, ClassNotFoundException {
 final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 final ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
 decorator.writeExternal(objStream);
 objStream.close();

 final ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
 final ObjectInput objInput = new ObjectInput(byteInput);
 TShortByteMapDecorator deserializedDecorator = new TShortByteMapDecorator();
 deserializedDecorator.readExternal(objInput);
 objInput.close();

 assertEquals(2, deserializedDecorator.size());
 assertTrue(deserializedDecorator.containsKey(Short.valueOf((short)1)));
 assertTrue(deserializedDecorator.containsKey(Short.valueOf((short)2)));

 for (TShortByteIterator iterator = deserializedDecorator.entrySet().iterator(); iterator.hasNext();) {
 Entry<Short, Byte> entry = iterator.next();
 assertEquals(entry.getKey().shortValue(), iterator.key().shortValue());
 assertEquals(entry.getValue().byteValue(), iterator.value().byteValue());
 }
 }
}