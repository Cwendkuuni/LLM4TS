import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.TObjectShortMap;
import gnu.trove.map.hash.TObjectShortHashMap;
import gnu.trove.map.view.TIdentityMap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TObjectShortHashMap<>();
 decorator = new TDoubleShortMapDecorator(_map);
 }

 @Test
 public void testEmptyMap() {
 assertEquals(0, decorator.size());
 assertNull(decorator.get(null));
 assertNull(decorator.get(1.0));
 assertFalse(decorator.containsKey(null));
 assertFalse(decorator.containsKey(1.0));
 }

 @Test
 public void testPut() {
 decorator.put(1.0, (short)1);
 assertEquals(1, decorator.size());
 assertEquals((short)1, decorator.get(1.0));
 assertTrue(decorator.containsKey(1.0));

 decorator.put(2.0, (short)2);
 assertEquals(2, decorator.size());
 assertEquals((short)1, decorator.get(1.0));
 assertEquals((short)2, decorator.get(2.0));
 assertTrue(decorator.containsKey(1.0));
 assertTrue(decorator.containsKey(2.0));
 }

 @Test
 public void testPutNullKey() {
 decorator.put(null, (short)1);
 assertEquals(1, decorator.size());
 assertNull(decorator.get(null));
 assertNull(decorator.get(1.0));
 assertTrue(decorator.containsKey(null));
 assertTrue(decorator.containsKey(1.0));
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 decorator.put(1.0, null);
 }

 @Test
 public void testClear() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 decorator.clear();
 assertEquals(0, decorator.size());
 assertNull(decorator.get(1.0));
 assertNull(decorator.get(2.0));
 assertFalse(decorator.containsKey(1.0));
 assertFalse(decorator.containsKey(2.0));
 }

 @Test
 public void testRemove() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 assertEquals(2, decorator.size());
 assertTrue(decorator.containsKey(1.0));
 assertTrue(decorator.containsKey(2.0));

 decorator.remove(1.0);
 assertEquals(1, decorator.size());
 assertNull(decorator.get(1.0));
 assertTrue(decorator.containsKey(2.0));

 decorator.remove(2.0);
 assertEquals(0, decorator.size());
 assertNull(decorator.get(1.0));
 assertNull(decorator.get(2.0));
 assertFalse(decorator.containsKey(1.0));
 assertFalse(decorator.containsKey(2.0));
 }

 @Test
 public void testRemoveNullKey() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 assertEquals(2, decorator.size());
 assertFalse(decorator.containsKey(null));

 decorator.remove(null);
 assertEquals(2, decorator.size());
 assertNull(decorator.get(null));
 assertNull(decorator.get(1.0));
 assertNull(decorator.get(2.0));
 assertFalse(decorator.containsKey(null));
 assertTrue(decorator.containsKey(1.0));
 assertTrue(decorator.containsKey(2.0));
 }

 @Test
 public void testEntrySet() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);

 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 assertEquals(2, entrySet.size());

 for (Entry<Double, Short> entry : entrySet) {
 if (entry.getKey().equals(1.0)) {
 assertEquals(1, entry.getValue().shortValue());
 } else if (entry.getKey().equals(2.0)) {
 assertEquals(2, entry.getValue().shortValue());
 }
 }
 }

 @Test
 public void testContainsValue() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 assertTrue(decorator.containsValue((short)1));
 assertTrue(decorator.containsValue((short)2));
 }

 @Test
 public void testClone() {
 TDoubleShortMapDecorator clone = decorator.clone();
 assertEquals(decorator.size(), clone.size());

 for (Entry<Double, Short> entry : decorator.entrySet()) {
 Short value = clone.get(entry.getKey());
 assertEquals(entry.getValue(), value);
 }
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream os = new ByteArrayOutputStream();
 ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
 ObjectOutput out = is;
 ObjectInput in = os;

 decorator.writeExternal(out);
 TDoubleShortMapDecorator clone2 = new TDoubleShortMapDecorator();
 clone2.readExternal(in);

 assertEquals(decorator.size(), clone2.size());

 for (Entry<Double, Short> entry : decorator.entrySet()) {
 Short value = clone2.get(entry.getKey());
 assertEquals(entry.getValue(), value);
 }
 }
}