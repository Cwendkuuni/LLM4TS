import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.view.TDoubleMapView;

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
 assertTrue(decorator.isEmpty());
 assertEquals(0, decorator.size());
 assertNull(decorator.get(null));
 assertNull(decorator.get(new Double(0.0)));
 }

 @Test
 public void testPut() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(new Double(3.0), null);

 assertEquals(3, decorator.size());
 assertEquals((short)1, decorator.get(new Double(1.0)));
 assertEquals((short)2, decorator.get(new Double(2.0)));
 assertNull(decorator.get(new Double(3.0)));

 TDoubleMapView view = _map.view();
 TDoubleShortIterator it = decorator.entrySet().iterator();

 double key;
 Short value;
 while (it.hasNext()) {
 key = it.nextKey();
 value = it.nextValue();
 assertEquals(key, view.get(key));
 }
 }

 @Test(expected = NullPointerException.class)
 public void testNullKey() {
 decorator.put(null, (short)1);
 }

 @Test(expected = NullPointerException.class)
 public void testNullValue() {
 decorator.put(new Double(1.0), null);
 }

 @Test(expected = ClassCastException.class)
 public void testNonDoubleKey() {
 decorator.put("test", (short)1);
 }

 @Test(expected = ClassCastException.class)
 public void testNonShortValue() {
 decorator.put(new Double(1.0), new Integer(1));
 }

 @Test
 public void testClear() {
 decorator.clear();
 assertTrue(decorator.isEmpty());
 assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);

 assertEquals(2, decorator.size());
 decorator.remove(new Double(1.0));
 assertEquals(1, decorator.size());
 assertNull(decorator.get(new Double(1.0)));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
 decorator.remove(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveNonExistentKey() {
 decorator.remove(new Double(3.0));
 }

 @Test
 public void testExternalizable() {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 out.close();

 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInput in = new ObjectInputStream(bais);
 TDoubleShortMapDecorator dec = (TDoubleShortMapDecorator)in.readObject();
 in.close();

 assertEquals(decorator.size(), dec.size());
 for (Object key : decorator.keySet()) {
 assertEquals(decorator.get(key), dec.get(key));
 }
 }
}