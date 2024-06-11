import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setup() {
 _map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(_map);
 }

 @Test
 public void testEmptyMap() {
 assertFalse(decorator.isEmpty());
 assertEquals(0, decorator.size());
 assertNull(decorator.get(null));
 assertNull(decorator.get(1.0));
 assertFalse(decorator.containsKey(1.0));
 assertFalse(decorator.containsValue((short)1));
 }

 @Test
 public void testPut() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(1.0, (short)3);

 assertEquals(2, decorator.size());
 assertEquals((short)3, decorator.get(1.0));
 assertEquals((short)1, decorator.get(2.0));
 assertTrue(decorator.containsKey(1.0));
 assertTrue(decorator.containsKey(2.0));
 assertTrue(decorator.containsValue((short)1));
 assertTrue(decorator.containsValue((short)2));

 assertEquals(null, decorator.put(1.0, (short)4));
 assertEquals((short)3, decorator.get(1.0));
 assertEquals(1, decorator.size());
 }

 @Test
 public void testClear() {
 decorator.clear();
 assertFalse(decorator.isEmpty());
 assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);

 assertEquals(3, decorator.size());
 assertTrue(decorator.containsKey(1.0));
 assertTrue(decorator.containsKey(2.0));
 assertTrue(decorator.containsKey(3.0));

 assertEquals(null, decorator.remove(null));
 assertEquals(null, decorator.remove(1.1));
 assertEquals((short)2, decorator.remove(2.0));
 assertEquals((short)3, decorator.remove(3.0));

 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey(1.0));
 assertFalse(decorator.containsKey(2.0));
 assertFalse(decorator.containsKey(3.0));
 }

 @Test
 public void testEntrySet() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);

 Set<Entry<Double, Short>> set = decorator.entrySet();
 assertEquals(3, set.size());

 Entry<Double, Short> entry = set.iterator().next();
 assertEquals(1.0, entry.getKey());
 assertEquals((short)1, entry.getValue());

 set.remove(entry);
 assertEquals(2, decorator.size());
 assertFalse(set.contains(entry));
 }

 @Test
 public void testContainsValue() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)1);
 decorator.put(3.0, (short)2);

 assertTrue(decorator.containsValue((short)1));
 assertFalse(decorator.containsValue((short)3));
 }

 @Test
 public void testContainsKey() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)1);
 decorator.put(3.0, (short)2);

 assertTrue(decorator.containsKey(1.0));
 assertTrue(decorator.containsKey(2.0));
 assertFalse(decorator.containsKey(3.1));
 }

 @Test
 public void testSize() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)1);
 decorator.put(3.0, (short)2);

 assertEquals(3, decorator.size());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap map = new TDoubleShortMap();
 map.put(1.0, (short)1);
 map.put(2.0, (short)2);
 map.put(3.0, (short)3);

 decorator.putAll(map);

 assertEquals(3, decorator.size());
 assertEquals((short)1, decorator.get(1.0));
 assertEquals((short)2, decorator.get(2.0));
 assertEquals((short)3, decorator.get(3.0));
 }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 TDoubleShortMapDecorator deserializederator = (TDoubleShortMapDecorator)in.readObject();

 assertEquals(3, deserializederator.size());
 assertEquals((short)1, deserializederator.get(1.0));
 assertEquals((short)2, deserializederator.get(2.0));
 assertEquals((short)3, deserializederator.get(3.0));
 }
}