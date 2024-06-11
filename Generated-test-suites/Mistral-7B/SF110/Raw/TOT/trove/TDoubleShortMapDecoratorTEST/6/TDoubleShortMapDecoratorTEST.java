import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import java.util.NoSuchElementException;

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
 assertFalse(decorator.containsValue((short) 1));
 }

 @Test
 public void testPut() {
 decorator.put(1.0, (short) 1);
 assertEquals(1, decorator.size());
 assertTrue(decorator.containsKey(1.0));
 assertEquals((short) 1, decorator.get(1.0));
 }

 @Test
 public void testPutNullKey() {
 decorator.put(null, (short) 1);
 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey(null));
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 decorator.put(1.0, null);
 }

 @Test
 public void testGet() {
 decorator.put(1.0, (short) 1);
 assertEquals((short) 1, decorator.get(1.0));
 }

 @Test
 public void testGetNullKey() {
 decorator.put(1.0, (short) 1);
 assertNull(decorator.get(null));
 }

 @Test
 public void testClear() {
 decorator.put(1.0, (short) 1);
 decorator.clear();
 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey(1.0));
 }

 @Test
 public void testRemove() {
 decorator.put(1.0, (short) 1);
 assertEquals(1, decorator.size());
 decorator.remove(1.0);
 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey(1.0));
 }

 @Test
 public void testRemoveNullKey() {
 decorator.put(1.0, (short) 1);
 assertEquals(1, decorator.size());
 decorator.remove(null);
 assertEquals(1, decorator.size());
 assertFalse(decorator.containsKey(null));
 }

 @Test
 public void testExternalizable() {
 decorator.put(1.0, (short) 1);
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(byteStream);
 decorator.writeExternal(out);
 out.close();
 ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
 ObjectInput in = new ObjectInputStream(byteInput);
 TDoubleShortMapDecorator decorator2 = new TDoubleShortMapDecorator();
 decorator2.readExternal(in);
 assertEquals(1, decorator2.size());
 assertTrue(decorator2.containsKey(1.0));
 assertEquals((short) 1, decorator2.get(1.0));
 }

 @Test
 public void testCloneable() {
 decorator.put(1.0, (short) 1);
 TDoubleShortMapDecorator cloned = (TDoubleShortMapDecorator) decorator.clone();
 assertEquals(1, cloned.size());
 assertTrue(cloned.containsKey(1.0));
 assertEquals((short) 1, cloned.get(1.0));
 }

 @Test(expected = NoSuchElementException.class)
 public void testIterator() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 TDoubleShortIterator it = decorator.entrySet().iterator();
 it.next();
 it.next();
 it.next(); // Throws NoSuchElementException
 }
}