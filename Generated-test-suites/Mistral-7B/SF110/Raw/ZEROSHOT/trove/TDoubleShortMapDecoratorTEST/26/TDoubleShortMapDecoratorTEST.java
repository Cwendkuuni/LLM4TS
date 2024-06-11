import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.view.AbstractMapEntry;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setUp() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
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
 decorator.put(new Double(2.0), (short)2);
 decorator.put(null, (short)3);
 decorator.put(new Double(1.0), (short)4);

 assertEquals(3, decorator.size());
 assertTrue(decorator.containsKey(new Double(1.0)));
 assertTrue(decorator.containsKey(new Double(2.0)));
 assertTrue(decorator.containsKey(null));
 assertFalse(decorator.containsKey(new Double(3.0)));

 assertEquals((short)1, decorator.get(new Double(1.0)));
 assertEquals((short)2, decorator.get(new Double(2.0)));
 assertEquals((short)3, decorator.get(null));
 assertEquals(null, decorator.get(new Double(3.0)));

 assertEquals((short)4, decorator.put(new Double(1.0), (short)4));
 assertEquals((short)4, decorator.get(new Double(1.0)));
 assertEquals(null, decorator.put(new Double(3.0), (short)3));
 assertFalse(decorator.containsKey(new Double(2.0)));
 }

 @Test
 public void testClear() {
 decorator.clear();
 assertEquals(0, decorator.size());
 assertFalse(decorator.isEmpty());
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 Iterator<Entry<Double, Short>> it = entrySet.iterator();

 assertEquals(3, entrySet.size());
 assertFalse(entrySet.isEmpty());

 Entry<Double, Short> entry = it.next();
 assertEquals(new Double(1.0), entry.getKey());
 assertEquals((short)4, entry.getValue());

 entry = it.next();
 assertEquals(new Double(2.0), entry.getKey());
 assertEquals((short)2, entry.getValue());

 entry = it.next();
 assertNull(entry.getKey());
 assertEquals((short)3, entry.getValue());

 assertFalse(it.hasNext());
 }

 @Test
 public void testContainsValue() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(null, (short)3);
 decorator.put(new Double(1.0), (short)4);

 assertTrue(decorator.containsValue((short)1));
 assertTrue(decorator.containsValue((short)2));
 assertTrue(decorator.containsValue((short)3));
 assertTrue(decorator.containsValue((short)4));

 assertFalse(decorator.containsValue((short)5));
 }

 @Test
 public void testContainsKey() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(null, (short)3);
 decorator.put(new Double(1.0), (short)4);

 assertTrue(decorator.containsKey(new Double(1.0)));
 assertTrue(decorator.containsKey(new Double(2.0)));
 assertFalse(decorator.containsKey(new Double(3.0)));
 assertTrue(decorator.containsKey(null));
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 TDoubleShortMapDecorator decoratedMap = (TDoubleShortMapDecorator)in.readObject();

 assertEquals(decorator.size(), decoratedMap.size());
 for (TDoubleShortIterator it = decorator.entrySet().iterator(); it.hasNext(); ) {
 Entry<Double, Short> entry = it.next();
 assertEquals(entry.getKey(), decoratedMap.get(entry.getKey()));
 assertEquals(entry.getValue(), decoratedMap.get(entry.getKey()));
 }
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
 TDoubleShortMapDecorator clone = (TDoubleShortMapDecorator)decorator.clone();
 assertEquals(decorator.size(), clone.size());
 for (TDoubleShortIterator it = decorator.entrySet().iterator(); it.hasNext(); ) {
 Entry<Double, Short> entry = it.next();
 assertEquals(entry.getKey(), clone.get(entry.getKey()));
 assertEquals(entry.getValue(), clone.get(entry.getKey()));
 }
 }
}