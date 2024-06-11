import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.view.TDoubleMapView;
import gnu.trove.map.view.TShortMapView;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(_map);
 }

 @Test
 public void testPutNullKey() {
 assertNull(decorator.put(null, (short) 1));
 assertEquals(0, decorator.size());
 }

 @Test
 public void testPutNullValue() {
 decorator.put(1.0, (Short) null);
 assertEquals(1, decorator.size());
 assertNull(decorator.get(1.0));
 }

 @Test
 public void testPutSameKey() {
 decorator.put(1.0, (short) 1);
 decorator.put(1.0, (short) 2);
 assertEquals(1, decorator.size());
 assertEquals((short) 2, decorator.get(1.0));
 }

 @Test
 public void testClear() {
 decorator.put(1.0, (short) 1);
 decorator.clear();
 assertEquals(0, decorator.size());
 }

 @Test
 public void testRemoveKey() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 assertFalse(decorator.remove(null));
 assertFalse(decorator.remove(2.0));
 assertTrue(decorator.remove(1.0));
 assertEquals(1, decorator.size());
 }

 @Test
 public void testEntrySet() {
 TDoubleMapView<Double, Short> mapView = decorator.entrySet().asMap();
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 Iterator<Entry<Double, Short>> it = mapView.iterator();
 Entry<Double, Short> entry = it.next();
 assertEquals(1.0, entry.getKey());
 assertEquals((short) 1, entry.getValue());
 }

 @Test
 public void testContainsValue() {
 decorator.put(1.0, (short) 1);
 assertTrue(decorator.containsValue((short) 1));
 }

 @Test
 public void testContainsKey() {
 decorator.put(1.0, (short) 1);
 assertTrue(decorator.containsKey(1.0));
 }

 @Test
 public void testSize() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 assertTrue(decorator.isEmpty());
 decorator.put(1.0, (short) 1);
 assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap anotherMap = new TDoubleShortMap();
 anotherMap.put(1.0, (short) 1);
 anotherMap.put(2.0, (short) 2);
 decorator.putAll(anotherMap);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator) in.readObject();
 TDoubleShortMap deserializedMap = deserializedDecorator.getMap();
 assertEquals(2, deserializedMap.size());
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
 TDoubleShortMapDecorator clonedDecorator = (TDoubleShortMapDecorator) decorator.clone();
 assertEquals(decorator.size(), clonedDecorator.size());
 }
}