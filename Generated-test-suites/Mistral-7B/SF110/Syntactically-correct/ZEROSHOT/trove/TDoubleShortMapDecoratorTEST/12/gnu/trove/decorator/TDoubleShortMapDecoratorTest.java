import org.junit.After;
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
 assertFalse(decorator.isEmpty());
 assertFalse(decorator.containsKey(null));
 assertFalse(decorator.containsKey(new Double(0.0)));
 assertFalse(decorator.containsValue((short)0));
 }

 @Test
 public void testPut() {
 decorator.put(new Double(1.0), (short)1);
assertEquals(1, decorator.size());
 assertFalse(decorator.isEmpty());
 assertEquals(new Double(1.0), decorator.keySet().iterator().next());
 assertEquals((short)1, decorator.get(new Double(1.0)));

 decorator.put(new Double(2.0), (short)2);
 assertEquals(2, decorator.size());
 assertEquals(new Double(2.0), decorator.keySet().iterator().next());
 assertEquals((short)2, decorator.get(new Double(2.0)));

 decorator.put(new Double(1.0), (short)3);
 assertEquals(2, decorator.size());
 assertFalse(decorator.containsValue((short)1));
 assertEquals((short)3, decorator.get(new Double(1.0)));
 }

 @Test
 public void testGet() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(new Double(1.0), (short)3);

 assertEquals((short)3, decorator.get(new Double(1.0)));
 assertEquals((short)2, decorator.get(new Double(2.0)));
 assertNull(decorator.get(null));
 assertNull(decorator.get(new Double(3.0)));
 }

 @Test
 public void testClear() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.clear();

 assertEquals(0, decorator.size());
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testEntrySet() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);

 TDoubleMapView<Double, Short> view = decorator.entrySet().iterator().next().getValue();
 Short value = view.iterator().next();

 assertEquals(new Double(1.0), decorator.keySet().iterator().next());
 assertEquals(new Double(2.0), decorator.keySet().iterator().next());
 assertEquals((short)1, decorator.get(new Double(1.0)));
 assertEquals((short)2, decorator.get(new Double(2.0)));
 assertEquals((short)1, value);
 }

 @Test
 public void testContainsValue() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);

 assertTrue(decorator.containsValue((short)1));
 assertTrue(decorator.containsValue((short)2));
 assertFalse(decorator.containsValue((short)3));
 }

 @Test
 public void testContainsKey() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);

 assertFalse(decorator.containsKey(null));
 assertTrue(decorator.containsKey(new Double(1.0)));
 assertTrue(decorator.containsKey(new Double(2.0)));
 assertFalse(decorator.containsKey(new Double(3.0)));
 }

 @Test
 public void testSize() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(new Double(3.0), (short)3);

 assertEquals(3, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 assertEquals(false, decorator.isEmpty());
 decorator.clear();
 assertEquals(true, decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap map = new TDoubleShortMap();
 map.put(new Double(1.0), (short)1);
 map.put(new Double(2.0), (short)2);
 map.put(new Double(3.0), (short)3);

 decorator.putAll(map);

 assertEquals(3, decorator.size());
 assertFalse(decorator.isEmpty());
 }

 @Test
 public void testWrapKey() {
 assertEquals(1.0, decorator.wrapKey(1.0));
 }

 @Test
 public void testUnwrapKey() {
 double key = decorator.unwrapKey(new Double(1.0));
 assertEquals(1.0, key);
 }

 @Test
 public void testWrapValue() {
 assertEquals((short)1, decorator.wrapValue((short)1));
 }

 @Test
 public void testUnwrapValue() {
 short value = decorator.unwrapValue((short)1);
 assertEquals((short)1, value);
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 TDoubleShortMapDecorator newDecorator = new TDoubleShortMapDecorator();
 newDecorator.readExternal(in);

 assertEquals(decorator.size(), newDecorator.size());
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
 TDoubleShortMapDecorator original = new TDoubleShortMapDecorator(_map);
 TDoubleShortMapDecorator clone = (TDoubleShortMapDecorator)original.clone();

 assertEquals(original.size(), clone.size());
 }

 @After
 public void tearDown() {
 _map = null;
 decorator = null;
 }
}