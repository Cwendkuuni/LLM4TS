import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(_map);
 }

 @After
 public void tearDown() {
 decorator = null;
 _map = null;
 }

 @Test
 public void testEmptyMap() {
 assertTrue(decorator.isEmpty());
 assertEquals(0, decorator.size());
 assertNull(decorator.get(null));
 assertNull(decorator.get(new Double(0.0)));
 }

 @Test
 public void testPutGetClear() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(new Double(3.0), (short)3);

 assertEquals(3, decorator.size());
 assertNotNull(decorator.get(new Double(1.0)));
 assertEquals((short)1, decorator.get(new Double(1.0)).shortValue());
 assertNotNull(decorator.get(new Double(2.0)));
 assertEquals((short)2, decorator.get(new Double(2.0)).shortValue());
 assertNotNull(decorator.get(new Double(3.0)));
 assertEquals((short)3, decorator.get(new Double(3.0)).shortValue());

 decorator.clear();
 assertTrue(decorator.isEmpty());
 assertEquals(0, decorator.size());
 }

 @Test
 public void testPutNullKey() {
 decorator.put(null, (short)1);
 assertEquals(0, decorator.size());
 assertNull(decorator.get(null));
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 decorator.put(new Double(1.0), null);
 }

 @Test
 public void testRemove() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(new Double(3.0), (short)3);

 assertEquals(3, decorator.size());
 decorator.remove(new Double(2.0));
 assertEquals(2, decorator.size());
 assertNull(decorator.get(new Double(2.0)));
 assertNotNull(decorator.get(new Double(1.0)));
 assertNotNull(decorator.get(new Double(3.0)));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
 decorator.remove(null);
 }

 @Test
 public void testContainsKey() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(new Double(3.0), (short)3);

 assertTrue(decorator.containsKey(new Double(1.0)));
 assertTrue(decorator.containsKey(new Double(2.0)));
 assertTrue(decorator.containsKey(new Double(3.0)));
 assertFalse(decorator.containsKey(new Double(4.0)));
 }

 @Test
 public void testContainsValue() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)1);
 decorator.put(new Double(3.0), (short)2);

 assertTrue(decorator.containsValue((short)1));
 assertTrue(decorator.containsValue((short)2));
 assertFalse(decorator.containsValue((short)3));
 }

 @Test
 public void testEntrySet() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(new Double(3.0), (short)3);

 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 assertEquals(3, entrySet.size());

 Entry<Double, Short> entry = entrySet.iterator().next();
 assertEquals(new Double(1.0), entry.getKey());
 assertEquals((short)1, entry.getValue().shortValue());
 }

 @Test
 public void testSerializable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 out.close();
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 TDoubleShortMapDecorator deserializeDecorator = (TDoubleShortMapDecorator)in.readObject();
 in.close();
 assertNotNull(deserializeDecorator);
 assertEquals(decorator.size(), deserializeDecorator.size());
 }

}