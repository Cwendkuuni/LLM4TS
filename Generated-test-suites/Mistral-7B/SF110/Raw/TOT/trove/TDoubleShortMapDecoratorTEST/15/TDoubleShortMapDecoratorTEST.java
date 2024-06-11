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
 assertEquals(0, decorator.size());
 assertNull(decorator.get(null));
 assertNull(decorator.get(new Double(0.0)));
 }

 @Test
 public void testPutNullKey() {
 assertNull(decorator.put(null, (short) 0));
 assertNull(decorator.get(null));
 }

 @Test
 public void testPutNullValue() {
 decorator.put(new Double(1.0), (short) 0);
 assertNull(decorator.get(new Double(1.0)));
 }

 @Test
 public void testPut() {
 decorator.put(new Double(1.0), (short) 1);
 assertEquals(1, decorator.size());
 assertEquals((short) 1, decorator.get(new Double(1.0)));
 }

 @Test
 public void testPutSameKey() {
 Short oldValue = decorator.put(new Double(1.0), (short) 2);
 assertEquals((short) 2, decorator.get(new Double(1.0)));
 assertNull(oldValue);
 }

 @Test
 public void testGetNonexistentKey() {
 assertNull(decorator.get(new Double(2.0)));
 }

 @Test
 public void testClear() {
 decorator.clear();
 assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 decorator.put(new Double(1.0), (short) 1);
 assertEquals(1, decorator.size());
 Short removedValue = decorator.remove(new Double(1.0));
 assertNotNull(removedValue);
 assertEquals((short) 1, removedValue);
 assertEquals(0, decorator.size());
 }

 @Test
 public void testEntrySet() {
 TDoubleMapView<Double, Short> view = decorator.entrySet().iterator();
 assertEquals(0, view.size());
 decorator.put(new Double(1.0), (short) 1);
 decorator.put(new Double(2.0), (short) 2);
 view = decorator.entrySet().iterator();
 assertEquals(2, view.size());
 }

 @Test
 public void testContainsValue() {
 decorator.put(new Double(1.0), (short) 1);
 assertTrue(decorator.containsValue((short) 1));
 }

 @Test
 public void testContainsKey() {
 decorator.put(new Double(1.0), (short) 1);
 assertTrue(decorator.containsKey(new Double(1.0)));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddEntryToSet() {
 decorator.entrySet().add(new Entry<Double, Short>(new Double(1.0), (short) 1));
 }

 @Test
 public void testRemoveEntryFromSet() {
 TDoubleMapView<Double, Short> view = decorator.entrySet().iterator();
 assertEquals(1, view.size());
 decorator.remove(new Double(1.0));
 view = decorator.entrySet().iterator();
 assertEquals(0, view.size());
 }

 @Test
 public void testExternalization() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 TDoubleShortMapDecorator decoratorFromStream = (TDoubleShortMapDecorator) in.readObject();
 assertEquals(decorator.size(), decoratorFromStream.size());
 }
}