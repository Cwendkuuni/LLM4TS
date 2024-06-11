import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(_map);
 }

 @Test
 public void testEmptyDecorator() {
 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey(null));
 assertFalse(decorator.containsKey(1.0));
 assertNull(decorator.get(null));
 assertNull(decorator.get(1.0));
 }

 @Test
 public void testPutGetDecorator() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(1.0, (short)3); // should overwrite the previous value

 assertEquals(2, decorator.size());
 assertEquals(1.0, decorator.get(1.0).doubleValue(), 0.01); // get value using double key
 assertEquals(1.0, decorator.get(1.0).getKey(), 0.01); // get key using double key
 assertEquals((short)3, decorator.get(1.0).getValue()); // get value using Short wrapper key
 assertEquals((short)1, decorator.get((Double)1.0).getValue()); // get value using Double wrapper key
 }

 @Test
 public void testClearDecorator() {
 decorator.clear();
 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey(1.0));
 assertFalse(decorator.containsKey(2.0));
 }

 @Test
 public void testRemoveDecorator() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.remove(1.0);

 assertEquals(1, decorator.size());
 assertFalse(decorator.containsKey(1.0));
 assertFalse(decorator.containsKey(2.0));
 assertNull(decorator.get(1.0));
 assertEquals((short)2, decorator.get(2.0).getValue());
 }

 @Test
 public void testEntrySetDecorator() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);

 Set<Entry<Double, Short>> entrySet = decorator.entrySet();

 assertEquals(2, entrySet.size());
 assertFalse(entrySet.isEmpty());

 for (Entry<Double, Short> entry : entrySet) {
 if (entry.getKey().equals(1.0)) {
 assertEquals(1.0, entry.getKey(), 0.01);
 assertEquals((short)1, entry.getValue());
 } else if (entry.getKey().equals(2.0)) {
 assertEquals(2.0, entry.getKey(), 0.01);
 assertEquals((short)2, entry.getValue());
 }
 }
 }

 @Test
 public void testContainsValueDecorator() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);

 assertTrue(decorator.containsValue((short)1));
 assertTrue(decorator.containsValue((short)2));
 }

 @Test
 public void testContainsKeyDecorator() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);

 assertTrue(decorator.containsKey(1.0));
 assertTrue(decorator.containsKey(2.0));
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullKeyDecorator() {
 decorator.put(null, (short)1);
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValueDecorator() {
 decorator.put(1.0, null);
 }

 @Test
 public void testExternalizableDecorator() throws IOException, ClassNotFoundException {
 // Test serialization and deserialization
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(byteStream);
 out.writeObject(decorator);
 out.close();

 ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteStream.toByteArray());
 ObjectInputStream in = new ObjectInputStream(byteInputStream);
 TDoubleShortMapDecorator desorator = (TDoubleShortMapDecorator)in.readObject();
 in.close();

 assertEquals(decorator.size(), desorator.size());
 for (Entry<Double, Short> entry : desorator.entrySet()) {
 assertEquals(decorator.get(entry.getKey()), entry.getValue());
 }
 }
}