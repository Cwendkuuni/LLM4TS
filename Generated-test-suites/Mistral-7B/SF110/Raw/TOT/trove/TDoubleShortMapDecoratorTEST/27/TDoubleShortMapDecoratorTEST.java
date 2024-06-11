import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TDoubleDoubleHashMap();
 decorator = new TDoubleShortMapDecorator(_map);
 }

 @Test
 public void testEmptyDecorator() {
 assertTrue(decorator.isEmpty());
 assertEquals(0, decorator.size());
 assertNull(decorator.get(null));
 assertNull(decorator.get(new Double(1.0)));
 }

 @Test
 public void testPut() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);
 decorator.put(null, (short)3);

 assertEquals(3, decorator.size());
 assertEquals((short)1, decorator.get(new Double(1.0)));
 assertEquals((short)2, decorator.get(new Double(2.0)));
 assertNull(decorator.get(new Double(3.0)));
 assertNull(decorator.get(null));

 decorator.put(new Double(1.0), (short)4);
 assertEquals((short)4, decorator.get(new Double(1.0)));
 }

 @Test(expected = NullPointerException.class)
 public void testNullKeyPut() {
 decorator.put(null, (short)1);
 }

 @Test(expected = NullPointerException.class)
 public void testNullValuePut() {
 decorator.put(new Double(1.0), null);
 }

 @Test
 public void testClear() {
 decorator.clear();
 assertTrue(decorator.isEmpty());
 assertEquals(0, decorator.size());
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 Iterator<Entry<Double, Short>> iterator = entrySet.iterator();

 assertEquals(0, entrySet.size());
 assertFalse(iterator.hasNext());

 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);

 assertEquals(2, entrySet.size());

 Entry<Double, Short> entry1 = iterator.next();
 assertEquals(new Double(1.0), entry1.getKey());
 assertEquals((short)1, entry1.getValue());

 Entry<Double, Short> entry2 = iterator.next();
 assertEquals(new Double(2.0), entry2.getKey());
 assertEquals((short)2, entry2.getValue());

 assertFalse(iterator.hasNext());
 }

 @Test
 public void testContainsValue() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);

 assertTrue(decorator.containsValue((short)1));
 assertTrue(decorator.containsValue((short)2));

 assertFalse(decorator.containsValue((short)0));
 }

 @Test
 public void testContainsKey() {
 decorator.put(new Double(1.0), (short)1);
 decorator.put(new Double(2.0), (short)2);

 assertTrue(decorator.containsKey(new Double(1.0)));
 assertTrue(decorator.containsKey(new Double(2.0)));

 assertFalse(decorator.containsKey(null));
 assertFalse(decorator.containsKey(new Double(3.0)));
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 TDoubleShortMapDecorator testDecorator = (TDoubleShortMapDecorator)in.readObject();

 assertEquals(decorator.size(), testDecorator.size());
 for (Entry<Double, Short> entry : decorator.entrySet()) {
 assertEquals(entry.getKey(), testDecorator.get(entry.getKey()));
 assertEquals(entry.getValue(), testDecorator.get(entry.getKey()));
 }
 }
}