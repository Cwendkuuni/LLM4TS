import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import java.util.Arrays;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setUp() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 }

 @After
 public void tearDown() {
 map.clear();
 decorator.clear();
 }

 @Test
 public void testEmptyMap() {
 assertFalse(decorator.isEmpty());
 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey(null));
 assertFalse(decorator.containsKey(Double.valueOf(0.0)));
 assertNull(decorator.get(null));
 assertNull(decorator.get(Double.valueOf(0.0)));
 }

 @Test
 public void testPut() {
 decorator.put(Double.valueOf(1.0), (short)1);
 decorator.put(Double.valueOf(2.0), (short)2);
 decorator.put(Double.valueOf(3.0), null);
 decorator.put(null, (short)3);

 assertEquals(4, decorator.size());
 assertTrue(decorator.containsKey(Double.valueOf(1.0)));
 assertTrue(decorator.containsKey(Double.valueOf(2.0)));
 assertFalse(decorator.containsKey(Double.valueOf(3.0))); // null key is considered equal to no entry key
 assertTrue(decorator.containsKey(null));

 assertEquals((short)1, decorator.get(Double.valueOf(1.0)));
 assertEquals((short)2, decorator.get(Double.valueOf(2.0)));
 assertNull(decorator.get(Double.valueOf(3.0)));
 assertNull(decorator.get(null));
 }

 @Test
 public void testPutNullValue() {
 decorator.put(Double.valueOf(1.0), (short)1);
 decorator.put(Double.valueOf(2.0), null);
 decorator.put(Double.valueOf(3.0), (short)3);

 assertEquals(3, decorator.size());
 assertTrue(decorator.containsKey(Double.valueOf(1.0)));
 assertTrue(decorator.containsKey(Double.valueOf(2.0)));
 assertFalse(decorator.containsKey(Double.valueOf(3.0)));

 assertEquals((short)1, decorator.get(Double.valueOf(1.0)));
 assertNull(decorator.get(Double.valueOf(2.0)));
 assertEquals((short)3, decorator.get(Double.valueOf(3.0)));
 }

 @Test
 public void testGetNullKey() {
 decorator.put(Double.valueOf(1.0), (short)1);
 decorator.put(Double.valueOf(2.0), (short)2);
 decorator.put(null, (short)3);

 assertEquals(3, decorator.size());
 assertNull(decorator.get(null));
 assertEquals((short)1, decorator.get(Double.valueOf(1.0)));
 assertEquals((short)2, decorator.get(Double.valueOf(2.0)));
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullKeyAndValue() {
 decorator.put(null, null);
 }

 @Test
 public void testContainsValue() {
 decorator.put(Double.valueOf(1.0), (short)1);
 decorator.put(Double.valueOf(2.0), (short)2);
 decorator.put(Double.valueOf(3.0), (short)3);

 assertTrue(decorator.containsValue((short)1));
 assertTrue(decorator.containsValue((short)2));
 assertFalse(decorator.containsValue((short)4));
 }

 @Test
 public void testRemove() {
 decorator.put(Double.valueOf(1.0), (short)1);
 decorator.put(Double.valueOf(2.0), (short)2);
 decorator.put(Double.valueOf(3.0), (short)3);
 decorator.put(null, (short)4);

 assertEquals(4, decorator.size());
 decorator.remove(Double.valueOf(1.0));
 decorator.remove(Double.valueOf(2.0));
 decorator.remove(Double.valueOf(3.0));
 decorator.remove(null);

 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey(Double.valueOf(1.0)));
 assertFalse(decorator.containsKey(Double.valueOf(2.0)));
 assertFalse(decorator.containsKey(Double.valueOf(3.0)));
 assertFalse(decorator.containsKey(null));
 }

 @Test
 public void testClear() {
 decorator.put(Double.valueOf(1.0), (short)1);
 decorator.put(Double.valueOf(2.0), (short)2);
 decorator.put(Double.valueOf(3.0), (short)3);
 decorator.put(null, (short)4);

 assertEquals(4, decorator.size());
 decorator.clear();

 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey(Double.valueOf(1.0)));
 assertFalse(decorator.containsKey(Double.valueOf(2.0)));
 assertFalse(decorator.containsKey(Double.valueOf(3.0)));
 assertFalse(decorator.containsKey(null));
 }

 @Test
 public void testEntrySet() {
 decorator.put(Double.valueOf(1.0), (short)1);
 decorator.put(Double.valueOf(2.0), (short)2);
 decorator.put(Double.valueOf(3.0), (short)3);
 decorator.put(null, (short)4);

 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 assertEquals(4, entrySet.size());

 for (Entry<Double, Short> entry : entrySet) {
 System.out.println("Entry: " + entry.getKey() + " = " + entry.getValue());
 }
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 out.close();

 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInput in = new ObjectInputStream(bais);
 TDoubleShortMapDecorator newDecorator = new TDoubleShortMapDecorator();
 newDecorator.readExternal(in);
 in.close();

 assertEquals(decorator.size(), newDecorator.size());
 for (Entry<Double, Short> entry : decorator.entrySet()) {
 assertEquals(entry.getKey(), newDecorator.get(entry.getKey()));
 assertEquals(entry.getValue(), newDecorator.get(entry.getKey()));
 }
 }
}