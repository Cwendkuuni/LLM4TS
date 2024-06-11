import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setup() {
 _map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(_map);
 }

 @Test
 public void testPutNullKey() {
 assertNull(decorator.put(null, (short) 0));
 assertEquals(0, decorator.size());
 }

 @Test
 public void testPutNullValue() {
 decorator.put(1.0, (Short) null);
 assertEquals(1, decorator.size());
 assertNull(decorator.get(1.0));
 }

 @Test
 public void testPutValidKeyValue() {
 decorator.put(1.0, (short) 1);
 assertEquals(1, decorator.size());
 assertEquals((short) 1, decorator.get(1.0));
 }

 @Test
 public void testPutDuplicateKey() {
 decorator.put(1.0, (short) 1);
 decorator.put(1.0, (short) 2);
 assertEquals(1, decorator.size());
 assertEquals((short) 2, decorator.get(1.0));
 }

 @Test
 public void testGetNullKey() {
 assertNull(decorator.get(null));
 }

 @Test
 public void testGetValidKey() {
 decorator.put(1.0, (short) 1);
 assertSame(1.0, decorator.get(1.0).getKey());
 assertEquals((short) 1, decorator.get(1.0).getValue());
 }

 @Test
 public void testClear() {
 decorator.put(1.0, (short) 1);
 decorator.clear();
 assertEquals(0, decorator.size());
 }

 @Test
 public void testRemoveValidKey() {
 decorator.put(1.0, (short) 1);
 assertTrue(decorator.remove(1.0) != null);
 assertEquals(0, decorator.size());
 }

 @Test
 public void testRemoveNullKey() {
 assertNull(decorator.remove(null));
 }

 @Test
 public void testRemoveNonExistentKey() {
 assertNull(decorator.remove(2.0));
 assertEquals(0, decorator.size());
 }

 @Test
 public void testEntrySet() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);

 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 Iterator<Entry<Double, Short>> iterator = entrySet.iterator();

 Entry<Double, Short> entry = iterator.next();
 assertEquals(1.0, entry.getKey());
 assertEquals((short) 1, entry.getValue());

 iterator.hasNext();
 entry = iterator.next();
 assertEquals(2.0, entry.getKey());
 assertEquals((short) 2, entry.getValue());

 assertFalse(iterator.hasNext());
 }

 @Test
 public void testContainsValue() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);

 assertTrue(decorator.containsValue((short) 1));
 assertTrue(decorator.containsValue((short) 2));
 }

 @Test
 public void testContainsKey() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);

 assertTrue(decorator.containsKey(1.0));
 assertTrue(decorator.containsKey(2.0));
 }

 @Test
 public void testSize() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);

 assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 assertEquals(true, decorator.isEmpty());
 decorator.put(1.0, (short) 1);
 assertEquals(false, decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap map = new TDoubleShortMap();
 map.put(1.0, (short) 1);
 map.put(2.0, (short) 2);

 decorator.putAll(map);
 assertEquals(2, decorator.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddEntrySet() {
 decorator.entrySet().add(new Entry<Double, Short>(1.0, (short) 1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveEntrySet() {
 decorator.entrySet().remove(new Entry<Double, Short>(1.0, (short) 1));
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 decorator.writeExternal(oos);
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 TDoubleShortMapDecorator readDecorator = new TDoubleShortMapDecorator();
 readDecorator.readExternal(in);
 assertEquals(decorator.size(), readDecorator.size());
 }
}