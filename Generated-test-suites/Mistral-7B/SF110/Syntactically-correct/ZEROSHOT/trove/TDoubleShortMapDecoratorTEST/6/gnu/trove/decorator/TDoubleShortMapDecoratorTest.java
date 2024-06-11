import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.view.TDoubleMap;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setUp() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 }

 @Test
 public void testEmptyMap() {
 assertTrue(decorator.isEmpty());
 assertEquals(0, decorator.size());
 assertNull(decorator.get(null));
 assertNull(decorator.get(0.0));
 }

 @Test
 public void testPut() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);

 assertEquals(3, decorator.size());
 assertNotNull(decorator.get(1.0));
 assertEquals((short)1, decorator.get(1.0).shortValue());
 assertNotNull(decorator.get(2.0));
 assertEquals((short)2, decorator.get(2.0).shortValue());
 assertNotNull(decorator.get(3.0));
 assertEquals((short)3, decorator.get(3.0).shortValue());
 assertNull(decorator.get(0.0));

 TDoubleMap view = decorator.entrySet().view();
 TDoubleShortIterator it = view.iterator();
 it.next(); // skip no entry key
 assertEquals(1.0, it.key(), 0.001);
 assertEquals(1, it.value());
 it.next(); // skip 2.0
 assertEquals(2.0, it.key(), 0.001);
 assertEquals(2, it.value());
 it.next(); // skip 3.0
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
 decorator.clear();
 assertTrue(decorator.isEmpty());
 assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);

 assertEquals(3, decorator.size());

 decorator.remove(1.0);
 assertEquals(2, decorator.size());
 assertNull(decorator.get(1.0));
 assertNotNull(decorator.get(2.0));
 assertNotNull(decorator.get(3.0));

 decorator.remove(3.0);
 assertEquals(1, decorator.size());
 assertNull(decorator.get(1.0));
 assertNull(decorator.get(2.0));
 assertNull(decorator.get(3.0));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
 decorator.remove(null);
 }

 @Test
 public void testExternalization() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 decorator.writeExternal(oos);
 byte[] bytes = baos.toByteArray();
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TDoubleShortMapDecorator decoratorFromSerial = (TDoubleShortMapDecorator)in.readObject();
 assertEquals(decorator, decoratorFromSerial);
 }
}