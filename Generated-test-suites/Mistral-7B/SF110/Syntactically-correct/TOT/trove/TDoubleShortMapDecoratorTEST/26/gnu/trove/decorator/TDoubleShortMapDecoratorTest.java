import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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

 @Test
 public void testEmptyMap() {
 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey(null));
 assertFalse(decorator.containsKey(new Double(1.0)));
 assertFalse(decorator.containsValue((short)1));
 assertNull(decorator.get(null));
 assertNull(decorator.get(new Double(1.0)));
 }

 @Test
 public void testPut() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 assertEquals(2, decorator.size());
 assertEquals((short)1, decorator.get(new Double(1.0)));
 assertEquals((short)2, decorator.get(new Double(2.0)));
 assertNull(decorator.get(null));
 assertNull(decorator.get(new Double(3.0)));
 }

 @Test
 public void testPutNullKey() {
 decorator.put(null, (short)1);
 assertEquals(0, decorator.size());
 assertNull(decorator.get(null));
 }

 @Test
 public void testPutNullValue() {
 decorator.put(new Double(1.0), null);
 assertEquals(0, decorator.size());
 assertNull(decorator.get(new Double(1.0)));
 }

 @Test
 public void testClear() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.clear();
 assertEquals(0, decorator.size());
 assertNull(decorator.get(new Double(1.0)));
 assertNull(decorator.get(new Double(2.0)));
 }

 @Test
 public void testRemove() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.remove(new Double(1.0));
 assertEquals(1, decorator.size());
 assertNull(decorator.get(new Double(1.0)));
 assertEquals((short)2, decorator.get(new Double(2.0)));
 }

 @Test
 public void testRemoveNullKey() {
 decorator.put(new Double(1.0), (short)1);
 decorator.clear();
 assertNull(decorator.get(new Double(1.0)));
 assertFalse(decorator.containsKey(new Double(1.0)));
 }

 @Test
 public void testRemoveNonExistentKey() {
 decorator.clear();
 assertNull(decorator.get(new Double(1.0)));
 assertFalse(decorator.containsKey(new Double(1.0)));
 assertFalse(decorator.remove(new Double(1.0)) instanceof Short);
 }

 @Test
 public void testEntrySet() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 TDoubleShortMapDecorator.Entry entry = (TDoubleShortMapDecorator.Entry)decorator.entrySet().iterator().next();
 assertEquals(new Double(1.0), entry.getKey());
 assertEquals((short)1, entry.getValue());
 }

 @Test
 public void testContainsValue() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)1);
 assertTrue(decorator.containsValue((short)1));
 }

 @Test
 public void testContainsKey() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)1);
 assertTrue(decorator.containsKey(new Double(1.0)));
 assertTrue(decorator.containsKey(new Double(2.0)));
 }

 @Test
 public void testSize() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)1);
 decorator.put(new Double(3.0), (short)1);
 assertEquals(3, decorator.size());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap map = new TDoubleShortMap();
 map.put(new Double(1.0), (short)1);
 map.put(new Double(2.0), (short)2);
 decorator.putAll(map);
 assertEquals(2, decorator.size());
 assertEquals((short)1, decorator.get(new Double(1.0)));
 assertEquals((short)2, decorator.get(new Double(2.0)));
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(byteStream);
 decorator.writeExternal(out);
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
 TDoubleShortMapDecorator decoratorCopy = (TDoubleShortMapDecorator)in.readObject();
 assertEquals(decorator.size(), decoratorCopy.size());
 for (TDoubleShortMapDecorator.Entry entry : decorator.entrySet()) {
 assertEquals(entry.getKey(), decoratorCopy.get(entry.getKey()));
 assertEquals(entry.getValue(), decoratorCopy.get(entry.getKey()));
 }
 }
}