import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.decorator.TShortByteMapDecorator;

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
 public void testEmptyMap() {
 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey((Short)null));
 assertFalse(decorator.containsKey((short)0));
 assertFalse(decorator.containsValue((byte)0));
 }

 @Test
 public void testPutGetRemove() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);

 assertEquals(2, decorator.size());
 assertTrue(decorator.containsKey((Short) Short.valueOf((short)1)));
 assertTrue(decorator.containsKey((short)1));
 assertTrue(decorator.containsValue((byte)1));

 Byte value = decorator.get((Short) Short.valueOf((short)1));
 assertEquals((byte)1, value);

 decorator.remove((Short) Short.valueOf((short)1));
 assertEquals(1, decorator.size());
 assertFalse(decorator.containsKey((Short) Short.valueOf((short)1)));
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullKey() {
 decorator.put(null, (byte)1);
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 decorator.put((short)1, null);
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
 assertEquals(0, entrySet.size());

 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);

 Entry<Short, Byte> entry = entrySet.iterator().next();
 assertEquals((Short)Short.valueOf((short)1), entry.getKey());
 assertEquals((byte)1, entry.getValue());
 }

 @Test
 public void testContainsValue() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);

 assertTrue(decorator.containsValue((byte)1));
 assertFalse(decorator.containsValue((byte)3));
 }

 @Test
 public void testClear() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);

 assertEquals(2, decorator.size());
 decorator.clear();
 assertEquals(0, decorator.size());
 }

 @Test
 public void testPutAll() {
 TShortByteMap map = new TShortByteMap();
 map.put((short)1, (byte)1);
 map.put((short)2, (byte)2);

 decorator.putAll(map);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 decorator.writeExternal(oos);
 ObjectInput ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 TShortByteMapDecorator decoratorFromStream = (TShortByteMapDecorator)ois.readObject();
 assertEquals(decorator.size(), decoratorFromStream.size());
 }

}