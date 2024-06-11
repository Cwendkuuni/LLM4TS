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
 public void testEmptyMap() {
 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey(null));
 assertFalse(decorator.containsKey(1.0));
 assertFalse(decorator.containsValue((short)1));
 }

 @Test
 public void testPut() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 assertEquals(2, decorator.size());
 assertFalse(decorator.containsKey(null));
 assertFalse(decorator.containsKey(1.5));
 assertTrue(decorator.containsKey(1.0));
 assertTrue(decorator.containsKey(2.0));
 assertEquals((short)1, decorator.get(1.0));
 assertEquals((short)2, decorator.get(2.0));
 assertNull(decorator.get(null));
 assertNull(decorator.get(1.5));
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullKey() {
 decorator.put(null, (short)1);
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 decorator.put(1.0, null);
 }

 @Test
 public void testClear() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.clear();
 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey(1.0));
 assertFalse(decorator.containsKey(2.0));
 }

 @Test
 public void testIterator() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 TDoubleShortIterator it = decorator.entrySet().iterator();
 it.advance(); // skip no entry
 Map.Entry<Double, Short> entry = it.next();
 assertEquals(1.0, entry.getKey());
 assertEquals((short)1, entry.getValue());
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator)in.readObject();
 assertEquals(decorator, deserializedDecorator);
 }

 @Test(expected = ClassCastException.class)
 public void testWriteExternalInvalidClass() throws IOException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 out.writeObject(new Object());
 }
}