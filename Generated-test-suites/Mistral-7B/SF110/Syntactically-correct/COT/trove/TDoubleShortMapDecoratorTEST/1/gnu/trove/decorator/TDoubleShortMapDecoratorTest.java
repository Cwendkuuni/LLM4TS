import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.views.TDoubleMap;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setup() {
 _map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(_map);
 }

 @Test
 public void testConstructor() {
 // Test default constructor
 decorator = new TDoubleShortMapDecorator();
Assert.assertNotNull(decorator.getMap());
 }

 @Test
 public void testConstructorWithMap() {
 // Test constructor with an existing map
 _map.put(1.0, (short)1);
 _map.put(2.0, (short)2);
 decorator = new TDoubleShortMapDecorator(_map);
 TDoubleMap view = decorator.entrySet();
 assertEquals(2, view.size());
 TDoubleShortIterator it = _map.iterator();
 assertEquals(it.next().getKey(), decorator.get(1.0));
 assertEquals(it.next().getValue(), decorator.get(2.0));
 }

 @Test
 public void testGetMap() {
 decorator.getMap(); // no need to assert here
 }

 @Test
 public void testPut() {
 // Basic functionality test
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 TDoubleMap view = decorator.entrySet();
 assertEquals(2, view.size());

 // Test with null key
 assertNull(decorator.put(null, (short)1));

 // Test with null value
 decorator.put(1.0, null);
 assertNull(decorator.get(1.0));
 }

 @Test
 public void testGet() {
 // Basic functionality test
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 assertEquals((short)1, decorator.get(1.0));
 assertEquals((short)2, decorator.get(2.0));
 assertNull(decorator.get(3.0));

 // Test with null key
 assertNull(decorator.get(null));
 }

 @Test
 public void testClear() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.clear();
 TDoubleMap view = decorator.entrySet();
 assertFalse(view.isEmpty());
 }

 @Test
 public void testRemove() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.remove(1.0);
 TDoubleMap view = decorator.entrySet();
 assertEquals(1, view.size());
 assertSame(1.0, decorator.get(2.0).getKey());
 }

 @Test
 public void testRemoveWithNullKey() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 assertNull(decorator.remove(null));
 }

 @Test
 public void testEntrySet() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 TDoubleMap view = decorator.entrySet();
 assertEquals(2, view.size());
 }

 @Test
 public void testContainsValue() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 assertTrue(decorator.containsValue((short)1));
 assertTrue(decorator.containsValue((short)2));
 assertFalse(decorator.containsValue(Byte.MIN_VALUE));
 }

 @Test
 public void testContainsKey() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 assertTrue(decorator.containsKey(1.0));
 assertTrue(decorator.containsKey(2.0));
 assertFalse(decorator.containsKey(3.0));
 }

 @Test
 public void testSize() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap anotherMap = new TDoubleShortMap();
 anotherMap.put(1.0, (short)1);
 anotherMap.put(2.0, (short)2);
 decorator.putAll(anotherMap);
 TDoubleMap view = decorator.entrySet();
 assertEquals(2, view.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddEntrySet() {
 decorator.entrySet().add(new Entry<>(1.0, (short)1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveEntrySet() {
 Entry<Double, Short> entry = new Entry<>(1.0, (short)1);
 decorator.entrySet().remove(entry);
 }

 @Test
 public void testContainsKeyWithNull() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 assertTrue(decorator.containsKey(1.0));
 assertFalse(decorator.containsKey(null));
 }

 @Test
 public void testGetWithNull() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 assertNull(decorator.get(null));
 }

 @Test
 public void testPutWithNullKey() {
 // Test with null key
 assertNull(decorator.put(null, (short)1));
 }

 @Test
 public void testPutWithNullValue() {
 // Test with null value
 decorator.put(1.0, (short)1);
 assertNull(decorator.put(1.0, null));
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 TDoubleShortMapDecorator decorator2 = new TDoubleShortMapDecorator();
 decorator2.readExternal(in);
 assertEquals(decorator.getMap().size(), decorator2.getMap().size());
 }

 @Test
 public void testWriteExternal() throws IOException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 TDoubleShortMapDecorator decorator2 = new TDoubleShortMapDecorator();
 decorator2.readExternal(in);
 assertEquals(decorator.getMap().toString(), decorator2.getMap().toString());
 }
}