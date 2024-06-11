import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.view.TShortByteMapView;

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
 decorator.clear();
 }

 @Test
 public void testEmptyMap() {
 assertTrue(decorator.isEmpty());
 assertEquals(0, decorator.size());
 }

 @Test
 public void testPut() {
 decorator.put((short) 1, (byte) 1);
 assertEquals((byte) 1, decorator.get((short) 1));
 assertTrue(decorator.containsKey((short) 1));

 decorator.put((short) 2, (byte) 2);
 assertEquals((byte) 2, decorator.get((short) 2));
 assertTrue(decorator.containsKey((short) 2));

 decorator.put((short) 1, (byte) 2);
 assertEquals((byte) 2, decorator.get((short) 1));
 }

 @Test
 public void testPutNullKey() {
 assertNull(decorator.put((Short) null, (Byte) null));
 assertFalse(decorator.containsKey(null));
 }

 @Test
 public void testPutNullValue() {
 decorator.put((short) 1, (Byte) null);
 assertEquals(null, decorator.get((short) 1));
 }

 @Test
 public void testGetNullKey() {
 assertNull(decorator.get(null));
 }

 @Test
 public void testClear() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);
 decorator.clear();
 assertTrue(decorator.isEmpty());
 assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);
 decorator.remove((short) 1);
 assertFalse(decorator.containsKey((short) 1));
 assertEquals((byte) 2, decorator.get((short) 2));
 }

 @Test
 public void testRemoveNullKey() {
 decorator.put((short) 1, (byte) 1);
 assertFalse(decorator.remove(null) == null);
 assertFalse(decorator.containsKey(null));
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 byte[] bytes = baos.toByteArray();
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TShortByteMapDecorator decorator2 = (TShortByteMapDecorator)in.readObject();
 assertEquals(decorator.size(), decorator2.size());
 for (TShortByteIterator it = decorator.iterator(); it.hasNext(); ) {
 Entry<Short, Byte> entry = it.next();
 assertEquals(entry.getKey(), decorator2.get(entry.getKey()));
 assertEquals(entry.getValue(), decorator2.get(entry.getKey()));
 }
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
 TShortByteMapDecorator original = new TShortByteMapDecorator(_map);
 TShortByteMapDecorator cloned = (TShortByteMapDecorator)original.clone();
 assertEquals(original.size(), cloned.size());
 for (TShortByteIterator it = original.iterator(); it.hasNext(); ) {
 Entry<Short, Byte> entry = it.next();
 assertEquals(entry.getKey(), cloned.get(entry.getKey()));
 assertEquals(entry.getValue(), cloned.get(entry.getKey()));
 }
 }

 @Test
 public void testEntrySet() {
 TShortByteMapView view = _map.view();
 assertEquals(decorator.size(), view.size());
 for (Entry<Short, Byte> entry : decorator.entrySet()) {
 assertTrue(view.containsKey(entry.getKey()));
 assertEquals(entry.getValue(), view.get(entry.getKey()));
 }
 }
}