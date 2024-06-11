import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.view.TDoubleMap;
import gnu.trove.map.view.TShortMap;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setup() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 }

 @Test
 public void testEmptyDecorator() {
 assertEquals(0, decorator.size());
 assertFalse(decorator.isEmpty());
 assertTrue(decorator.isEmpty());
 assertFalse(decorator.containsKey(null));
 assertFalse(decorator.containsKey(Double.NaN));
 assertFalse(decorator.containsValue((short)1));
 assertFalse(decorator.containsValue((short)2));
 assertNull(decorator.get(null));
 assertNull(decorator.get(Double.NaN));
 assertNull(decorator.remove(null));
 assertNull(decorator.remove(Double.NaN));
 }

 @Test
 public void testPutGet() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);

 assertEquals(3, decorator.size());
 assertFalse(decorator.isEmpty());

 assertEquals((short)1, decorator.get(1.0));
 assertEquals((short)2, decorator.get(2.0));
 assertEquals((short)3, decorator.get(3.0));
 assertNull(decorator.get(null));
 assertNull(decorator.get(Double.NaN));

 assertEquals(1.0, decorator.keyAt(0));
 assertEquals(2.0, decorator.keyAt(1));
 assertEquals(3.0, decorator.keyAt(2));

 assertEquals(1, decorator.valueAt(0));
 assertEquals(2, decorator.valueAt(1));
 assertEquals(3, decorator.valueAt(2));
 }

 @Test
 public void testRemove() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);

 assertEquals(3, decorator.size());
 assertFalse(decorator.isEmpty());

 decorator.remove(2.0);
 assertEquals(2, decorator.size());
 assertFalse(decorator.isEmpty());
 assertNull(decorator.get(2.0));

 decorator.remove(1.0);
 assertEquals(1, decorator.size());
 assertFalse(decorator.isEmpty());
 assertNull(decorator.get(1.0));

 decorator.remove(3.0);
 assertEquals(0, decorator.size());
 assertTrue(decorator.isEmpty());
 assertNull(decorator.get(3.0));
 }

 @Test
 public void testContainsKey() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);

 assertTrue(decorator.containsKey(1.0));
 assertTrue(decorator.containsKey(2.0));
 assertTrue(decorator.containsKey(3.0));
 assertFalse(decorator.containsKey(null));
 assertFalse(decorator.containsKey(Double.NaN));
 }

 @Test
 public void testContainsValue() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);

 assertTrue(decorator.containsValue((short)1));
 assertTrue(decorator.containsValue((short)2));
 assertTrue(decorator.containsValue((short)3));
 assertFalse(decorator.containsValue((short)4));
 }

 @Test
 public void testEntrySet() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);

 List<Map.Entry<Double, Short>> entries = new ArrayList<>(decorator.entrySet());
 assertEquals(3, entries.size());

 entries.forEach(entry -> {
 double key = entry.getKey();
 short value = entry.getValue();
 assertEquals(key, decorator.get(key));
 assertEquals(value, decorator.valueAt(decorator.indexOfKey(key)));
 });
 }

 @Test
 public void testClear() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);

 decorator.clear();
 assertEquals(0, decorator.size());
 assertTrue(decorator.isEmpty());
 assertNull(decorator.get(null));
 assertNull(decorator.get(Double.NaN));
 }

 @Test
 public void testWrapUnwrapKey() {
 assertEquals(1.0, decorator.wrapKey(1.0));
 assertEquals(1.0, decorator.unwrapKey(1.0));
 assertEquals(1.0, decorator.unwrapKey(decorator.wrapKey(1.0)));
 }

 @Test
 public void testWrapUnwrapValue() {
 assertEquals((short)1, decorator.wrapValue((short)1));
 assertEquals((short)1, decorator.unwrapValue((short)1));
 assertEquals((short)1, decorator.unwrapValue(decorator.wrapValue((short)1)));
 }

 @Test
 public void testSerialization() throws Exception {
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
 ObjectOutput out = new ObjectOutputStream(output);
 ObjectInput in = new ObjectInputStream(input);

 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.put(3.0, (short)3);
 out.writeObject(decorator);
 out.close();
 input.reset();

 TDoubleShortMapDecorator deserialized = (TDoubleShortMapDecorator)in.readObject();
 in.close();

 assertEquals(3, deserialized.size());
 assertFalse(deserialized.isEmpty());

 assertEquals((short)1, deserialized.get(1.0));
 assertEquals((short)2, deserialized.get(2.0));
 assertEquals((short)3, deserialized.get(3.0));
 }

 @After
 public void teardown() {
 map = null;
 decorator = null;
 }
}