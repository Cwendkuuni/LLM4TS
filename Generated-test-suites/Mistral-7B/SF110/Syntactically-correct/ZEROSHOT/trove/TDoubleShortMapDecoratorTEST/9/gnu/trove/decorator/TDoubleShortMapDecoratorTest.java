import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.view.TDoubleMap;
import gnu.trove.map.view.TShortMap;

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
 decorator = null;
 _map = null;
 }

 @Test
 public void testEmptyMap() {
 assertEquals(0, decorator.size());
 assertFalse(decorator.containsKey(null));
 assertFalse(decorator.containsKey(new Double(0.0)));
 assertFalse(decorator.containsValue((short)0));
 }

 @Test
 public void testPutGetAndRemove() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(new Double(1.0), (short)3);

 assertEquals(2, decorator.size());
 assertEquals((short)1, decorator.get(new Double(1.0)));
 assertEquals((short)2, decorator.get(new Double(2.0)));
 assertNull(decorator.get(new Double(3.0)));

 decorator.remove(new Double(1.0));
 assertEquals(1, decorator.size());
 assertNull(decorator.get(new Double(1.0)));
 }

 @Test
 public void testEntrySet() {
 TDoubleMap<Double,Short> view = decorator.entrySet().asMap();
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(new Double(3.0), (short)3);

 assertEquals(3, decorator.size());
 assertEquals(view.get(new Double(1.0)), decorator.entrySet().iterator().next().getValue());

 view.put(new Double(4.0), (short)4);
 assertEquals(3, decorator.size());
 assertFalse(decorator.entrySet().contains(new Entry<Double, Short>() {
 @Override public Double getKey() { return new Double(4.0); }
 @Override public Short getValue() { return (short)4; }
 }));
 }

 @Test
 public void testContainsKeyAndValue() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);

 assertTrue(decorator.containsKey(new Double(1.0)));
 assertTrue(decorator.containsKey(new Double(2.0)));
 assertTrue(decorator.containsValue((short)1));
 assertTrue(decorator.containsValue((short)2));

 assertFalse(decorator.containsKey(null));
 assertFalse(decorator.containsValue((short)3));
 }

 @Test
 public void testClear() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.clear();

 assertEquals(0, decorator.size());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap anotherMap = new TDoubleShortMap();
 anotherMap.put(new Double(1.0), (short)1);
 anotherMap.put(new Double(2.0), (short)2);
 anotherMap.put(new Double(3.0), (short)3);

 decorator.putAll(anotherMap);

 assertEquals(3, decorator.size());
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 TDoubleShortMapDecorator decoratorFromStream = (TDoubleShortMapDecorator) in.readObject();
 assertEquals(decorator.size(), decoratorFromStream.size());
 }
}