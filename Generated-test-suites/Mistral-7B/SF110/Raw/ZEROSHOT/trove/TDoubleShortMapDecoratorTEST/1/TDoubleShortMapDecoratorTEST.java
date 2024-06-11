import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.io.*;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TDoubleShortHashMap();
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
 assertTrue(decorator.isEmpty());
 assertNull(decorator.get(null));
 assertNull(decorator.get(new Double(0.0)));
 }

 @Test
 public void testPut() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(null, (short)3);
 decorator.put(new Double(1.0), (short)4);

 assertEquals(3, decorator.size());
 assertFalse(decorator.isEmpty());

 assertEquals((short)1, decorator.get(new Double(1.0)));
 assertEquals((short)2, decorator.get(new Double(2.0)));
 assertEquals((short)3, decorator.get(null));
 assertEquals((short)4, decorator.get(new Double(1.0)));

 assertNull(decorator.get(new Double(3.0)));

 assertEquals((short)3, decorator.remove(new Double(1.0)));
 assertEquals((short)2, decorator.remove(new Double(2.0)));
 assertEquals(null, decorator.remove(null));
 assertEquals(1, decorator.size());
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Double, Short>> entrySet = decorator.entrySet();

 assertEquals(0, entrySet.size());
 assertFalse(entrySet.isEmpty());

 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(null, (short)3);

 assertEquals(3, entrySet.size());
 assertFalse(entrySet.isEmpty());

 Entry<Double, Short> entry = entrySet.iterator().next();
 assertEquals(new Double(1.0), entry.getKey());
 assertEquals((short)1, entry.getValue());
 }

 @Test
 public void testContainsKey() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(null, (short)3);

 assertTrue(decorator.containsKey(new Double(1.0)));
 assertTrue(decorator.containsKey(new Double(2.0)));
 assertTrue(decorator.containsKey(null));

 assertFalse(decorator.containsKey(new Double(3.0)));
 }

 @Test
 public void testContainsValue() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)1);
 decorator.put(new Double(3.0), (short)2);

 assertTrue(decorator.containsValue((short)1));
 assertFalse(decorator.containsValue((short)3));
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap anotherMap = new TDoubleShortHashMap();
 anotherMap.put(new Double(1.0), (short)1);
 anotherMap.put(new Double(2.0), (short)2);
 anotherMap.put(new Double(3.0), (short)3);

 decorator.putAll(anotherMap);

 assertEquals(3, decorator.size());
 assertFalse(decorator.isEmpty());

 assertEquals((short)1, decorator.get(new Double(1.0)));
 assertEquals((short)2, decorator.get(new Double(2.0)));
 assertEquals((short)3, decorator.get(new Double(3.0)));
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 TDoubleShortMapDecorator dec = (TDoubleShortMapDecorator)in.readObject();

 assertEquals(decorator.size(), dec.size());
 for (Entry<Double, Short> entry : decorator.entrySet()) {
 assertEquals(entry.getKey(), dec.get(entry.getKey()));
 assertEquals(entry.getValue(), dec.get(entry.getKey()));
 }
 }
}