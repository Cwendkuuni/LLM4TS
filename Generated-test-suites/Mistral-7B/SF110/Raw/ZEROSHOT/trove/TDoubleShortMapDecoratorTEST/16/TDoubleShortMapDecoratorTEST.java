import org.junit.After;
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

 @After
 public void tearDown() {
 _map.clear();
 decorator = null;
 }

 @Test
 public void testEmptyMap() {
 assertEquals(0, decorator.size());
 assertFalse(decorator.isEmpty());
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPut() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 assertEquals(2, decorator.size());
 assertFalse(decorator.isEmpty());

 Double key = 1.0;
 Short value = (Short) decorator.get(key);
 assertEquals(1, value.shortValue());
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullKey() {
 decorator.put(null, (short) 1);
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 decorator.put(1.0, null);
 }

 @Test
 public void testGet() {
 assertNull(decorator.get(null));
 assertNull(decorator.get(2.0));
 assertSame(decorator.get(1.0), (Short) 1);
 }

 @Test(expected = NullPointerException.class)
 public void testGetNullKey() {
 decorator.get(null);
 }

 @Test
 public void testClear() {
 decorator.clear();
 assertEquals(0, decorator.size());
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemove() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 decorator.remove(1.0);
 assertEquals(1, decorator.size());
 assertFalse(decorator.containsKey(1.0));
 assertSame(decorator.get(2.0), (Short) 2);
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
 decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 assertEquals(0, entrySet.size());
 assertFalse(entrySet.isEmpty());
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 entrySet = decorator.entrySet();
 Iterator<Entry<Double, Short>> iterator = entrySet.iterator();
 assertEquals(2, entrySet.size());
 assertFalse(entrySet.isEmpty());
 Entry<Double, Short> entry = iterator.next();
 assertEquals(1.0, entry.getKey().doubleValue());
 assertEquals(1, entry.getValue().shortValue());
 }

 @Test
 public void testContainsValue() {
 assertFalse(decorator.containsValue((short) 3));
 decorator.put(1.0, (short) 1);
 assertTrue(decorator.containsValue((short) 1));
 }

 @Test
 public void testContainsKey() {
 assertFalse(decorator.containsKey(1.5));
 decorator.put(1.0, (short) 1);
 assertTrue(decorator.containsKey(1.0));
 }

 @Test
 public void testSize() {
 assertEquals(0, decorator.size());
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap map = new TDoubleShortMap();
 map.put(1.0, (short) 1);
 map.put(2.0, (short) 2);
 decorator.putAll(map);
 assertEquals(2, decorator.size());
 assertFalse(decorator.isEmpty());
 }

 @Test
 public void testExternalizable() {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(byteStream);
 decorator.writeExternal(out);
 byte[] bytes = byteStream.toByteArray();
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TDoubleShortMapDecorator dec = (TDoubleShortMapDecorator)in.readObject();
 assertNotNull(dec);
 }
}