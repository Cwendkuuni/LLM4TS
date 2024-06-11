import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import java.util.Arrays;
import java.util.NoSuchElementException;

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
 assertTrue(decorator.isEmpty());
 assertNull(decorator.get(null));
 assertNull(decorator.get(new Double(0.0)));
 }

 @Test
 public void testPut() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(new Double(1.0), (short)3); // overlapping key

 assertEquals(2, decorator.size());
 assertEquals((Short)1, decorator.get(new Double(1.0)));
 assertEquals((Short)2, decorator.get(new Double(2.0)));
 assertEquals((Short)3, decorator.get(new Double(1.0)));

 assertNull(decorator.get(new Double(3.0)));
 }

 @Test
 public void testRemove() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(new Double(1.0), (short)3); // overlapping key
 decorator.remove(new Double(1.0));

 assertEquals(1, decorator.size());
 assertNull(decorator.get(new Double(1.0)));
 assertEquals((Short)2, decorator.get(new Double(2.0)));
 }

 @Test(expected = NullPointerException.class)
 public void testNullKey() {
 decorator.put(null, (short)1);
 }

 @Test(expected = NullPointerException.class)
 public void testNullValue() {
 decorator.put(new Double(1.0), null);
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Double, Short>> entrySet = decorator.entrySet();

 assertEquals(2, entrySet.size());

 Entry<Double, Short> entry1 = entrySet.iterator().next();
 assertEquals(new Double(1.0), entry1.getKey());
 assertEquals((Short)1, entry1.getValue());

 Entry<Double, Short> entry2 = entrySet.iterator().next();
 assertEquals(new Double(2.0), entry2.getKey());
 assertEquals((Short)2, entry2.getValue());
 }

 @Test
 public void testContainsKey() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(new Double(1.0), (short)3); // overlapping key

 assertTrue(decorator.containsKey(new Double(1.0)));
 assertTrue(decorator.containsKey(new Double(2.0)));
 assertFalse(decorator.containsKey(new Double(3.0)));
 }

 @Test
 public void testContainsValue() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(new Double(1.0), (short)3); // overlapping key

 assertTrue(decorator.containsValue((short)1));
 assertTrue(decorator.containsValue((short)2));
 assertFalse(decorator.containsValue((short)4));
 }

 @Test
 public void testClear() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.clear();

 assertEquals(0, decorator.size());
 assertNull(decorator.get(new Double(1.0)));
 assertNull(decorator.get(new Double(2.0)));
 }

 @Test(expected = NoSuchElementException.class)
 public void testGetNullKey() {
 decorator.get(null);
 }

 @Test(expected = NoSuchElementException.class)
 public void testRemoveNullKey() {
 decorator.remove(null);
 }

 @Test
 public void testExternalizable() {
 TDoubleShortMapDecorator decorator1 = new TDoubleShortMapDecorator(_map);
 decorator1.put(new Double(1.0), (short)1);
 decorator1.put(new Double(2.0), (short)2);

 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(byteStream);
 decorator1.writeExternal(out);
 out.close();

 ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
 ObjectInput in = new ObjectInputStream(byteInput);
 TDoubleShortMapDecorator decorator2 = (TDoubleShortMapDecorator)in.readObject();
 in.close();

 assertEquals(decorator1, decorator2);
 }
}