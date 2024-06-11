import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.map.view.TMapView;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;

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
 assertFalse(decorator.containsKey(null));
 assertFalse(decorator.containsKey(new Double(0.0)));
 assertNull(decorator.get(null));
 assertNull(decorator.get(new Double(0.0)));
 }

 @Test
 public void testPut() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(new Double(-1.0), (short)-1);

 assertEquals(3, decorator.size());
 assertTrue(decorator.containsKey(new Double(1.0)));
 assertTrue(decorator.containsKey(new Double(2.0)));
 assertTrue(decorator.containsKey(new Double(-1.0)));
 assertEquals((short)1, decorator.get(new Double(1.0)));
 assertEquals((short)2, decorator.get(new Double(2.0)));
 assertEquals((short)-1, decorator.get(new Double(-1.0)));

 assertNull(decorator.get(null));
 assertNull(decorator.get(new Double(3.0)));
 }

 @Test
 public void testPutNullKey() {
 decorator.put(null, (short)0);
 assertEquals(1, decorator.size());
 assertTrue(decorator.containsKey(null));
 assertNull(decorator.get(null));
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 decorator.put(new Double(3.0), null);
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
 decorator.put(new Double(-1.0), (short)-1);

 assertEquals(3, decorator.size());
 decorator.remove(new Double(-1.0));
 assertEquals(2, decorator.size());
 assertFalse(decorator.containsKey(new Double(-1.0)));
 assertEquals((short)1, decorator.get(new Double(1.0)));
 assertEquals((short)2, decorator.get(new Double(2.0)));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
 decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
 TObjectIntMap<Double> expected = new TObjectIntHashMap<>();
 expected.put(new Double(1.0), 1);
 expected.put(new Double(2.0), 2);
 expected.put(new Double(-1.0), -1);

 TMapView<Double, Short> view = decorator.entrySet().view();
 assertEquals(expected.size(), view.size());

 int i = 0;
 for (Object entry : view) {
 Object key = ((Map.Entry<Double, Short>)entry).getKey();
 Short value = ((Map.Entry<Double, Short>)entry).getValue();

 assertEquals(key, ((Double)view.get(key)));
 assertEquals(value, decorator.get(key));

 i++;
 }
 }

 @Test
 public void testContainsValue() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(new Double(-1.0), (short)-1);

 assertTrue(decorator.containsValue((short)1));
 assertTrue(decorator.containsValue((short)2));
 assertFalse(decorator.containsValue((short)3));
 }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
 ObjectOutput os = new ObjectOutputStream(out);
 ObjectInput is = new ObjectInputStream(in);

 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(new Double(-1.0), (short)-1);

 os.writeObject(decorator);
 in.readObject(); // consume the initial marker byte

 TDoubleShortMapDecorator deserialized = (TDoubleShortMapDecorator)os.readObject();
 assertEquals(decorator.size(), deserialized.size());
 assertEquals(decorator, deserialized);
 }
}