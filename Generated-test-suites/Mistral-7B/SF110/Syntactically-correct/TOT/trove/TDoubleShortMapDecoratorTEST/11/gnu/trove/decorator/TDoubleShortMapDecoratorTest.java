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

 @Test
 public void testEmptyMap() {
 assertEquals(0, decorator.size());
 assertNull(decorator.get(null));
 assertNull(decorator.get(new Double(0.0)));
 }

 @Test
 public void testPutGet() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);

 assertEquals(2, decorator.size());
 assertEquals((short)1, decorator.get(new Double(1.0)));
 assertEquals((short)2, decorator.get(new Double(2.0)));
 assertEquals(null, decorator.get(null));
 }

 @Test
 public void testPutNullKey() {
 decorator.put(null, (short)1);
 assertEquals(1, decorator.size());
 assertNull(decorator.get(null));
 }

 @Test
 public void testPutNullValue() {
 decorator.put(new Double(1.0), null);
 assertEquals(1, decorator.size());
 assertEquals((short)0, decorator.get(new Double(1.0)));
 }

 @Test
 public void testClear() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.clear();
 assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.remove(new Double(1.0));
 assertEquals(1, decorator.size());
 assertNull(decorator.get(new Double(1.0)));
 }

 @Test
 public void testRemoveNullKey() {
 decorator.put(new Double(1.0), (short)1);
 decorator.remove(null);
 assertEquals(1, decorator.size());
 }

 @Test
 public void testEntrySet() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);

 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 assertEquals(2, entrySet.size());

 Entry<Double, Short> entry = entrySet.iterator().next();
 assertEquals(new Double(1.0), entry.getKey());
 assertEquals((short)1, entry.getValue());
 }

 @Test
 public void testContainsValue() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);

 assertTrue(decorator.containsValue((short)1));
 assertTrue(decorator.containsValue((short)2));
 }

 @Test
 public void testContainsKey() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);

 assertTrue(decorator.containsKey(new Double(1.0)));
 assertTrue(decorator.containsKey(new Double(2.0)));
 assertFalse(decorator.containsKey(null));
 }

 @Test
 public void testIterator() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(new Double(3.0), (short)3);

 TDoubleShortIterator iterator = decorator.entrySet().iterator();
 assertEquals(new Double(1.0), iterator.key());
 assertEquals((short)1, iterator.value());
 iterator.advance();
 assertEquals(new Double(2.0), iterator.key());
 assertEquals((short)2, iterator.value());
 iterator.advance();
 assertEquals(new Double(3.0), iterator.key());
 assertEquals((short)3, iterator.value());
 }

 @Test
 public void testExternalizable() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ObjectOutputStream os = new ObjectOutputStream(out);
 decorator.writeExternal(os);
 byte[] bytes = out.toByteArray();

 TDoubleShortMapDecorator deserializedDecorator = new TDoubleShortMapDecorator();
 deserializedDecorator.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));

 assertEquals(2, deserializedDecorator.size());
 assertEquals((short)1, deserializedDecorator.get(new Double(1.0)));
 assertEquals((short)2, deserializedDecorator.get(new Double(2.0)));
 }
}