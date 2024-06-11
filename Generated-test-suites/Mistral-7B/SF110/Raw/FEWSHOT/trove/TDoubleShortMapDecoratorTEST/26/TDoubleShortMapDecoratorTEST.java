package org.apache.commons.cli; // Change the package name if necessary

import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {

 TDoubleShortMap map;
 TDoubleShortMapDecorator decorator;

 @Test
 public void testEmptyMap() {
 decorator = new TDoubleShortMapDecorator();
 assertSame(decorator, decorator.getMap());
 assertTrue(decorator.isEmpty());
 assertFalse(decorator.containsKey(Double.valueOf(1.0)));
 assertFalse(decorator.containsKey(Double.valueOf(2.0)));
 assertFalse(decorator.containsValue((short) 1));
 assertFalse(decorator.containsValue((short) 2));
 assertNull(decorator.get(null));
 assertNull(decorator.get(Double.valueOf(1.0)));
 assertNull(decorator.get(Double.valueOf(2.0)));
 assertNull(decorator.remove(null));
 assertNull(decorator.remove(Double.valueOf(1.0)));
 assertNull(decorator.remove(Double.valueOf(2.0)));
 assertNull(decorator.put(Double.valueOf(1.0), (short) 1));
 assertNull(decorator.put(Double.valueOf(2.0), (short) 2));
 assertSame(decorator, decorator.clone());
 }

 @Test
 public void testPutGetAndRemove() {
 map = TDoubleShortMaps.create();
 map.put(1.0, (short) 1);
 map.put(2.0, (short) 2);
 decorator = new TDoubleShortMapDecorator(map);

 assertEquals(map, decorator.getMap());
 assertFalse(decorator.isEmpty());
 assertTrue(decorator.containsKey(Double.valueOf(1.0)));
 assertTrue(decorator.containsKey(Double.valueOf(2.0)));
 assertFalse(decorator.containsValue((short) 3));
 assertFalse(decorator.containsValue((short) 4));
 assertEquals((short) 1, decorator.get(Double.valueOf(1.0)));
 assertEquals((short) 2, decorator.get(Double.valueOf(2.0)));
 assertNull(decorator.get(Double.valueOf(3.0)));
 assertEquals((short) 1, decorator.remove(Double.valueOf(1.0)));
 assertEquals((short) 2, decorator.remove(Double.valueOf(2.0)));
 assertNull(decorator.remove(Double.valueOf(1.0)));
 assertNull(decorator.remove(Double.valueOf(2.0)));
 assertNull(decorator.put(Double.valueOf(1.0), (short) 1));
 assertNull(decorator.put(Double.valueOf(2.0), (short) 2));

 assertTrue(Arrays.equals(map.toArray(), decorator.entrySet().toArray()));
 }

 @Test
 public void testEntrySet() {
 map = TDoubleShortMaps.create();
 map.put(1.0, (short) 1);
 map.put(2.0, (short) 2);
 decorator = new TDoubleShortMapDecorator(map);

 Iterator<Entry<Double, Short>> iterator = decorator.entrySet().iterator();
 Entry<Double, Short> entry = iterator.next();
 assertEquals(1.0, entry.getKey().doubleValue(), 0.001);
 assertEquals((short) 1, entry.getValue().shortValue());
 assertTrue(iterator.hasNext());
 entry = iterator.next();
 assertEquals(2.0, entry.getKey().doubleValue(), 0.001);
 assertEquals((short) 2, entry.getValue().shortValue());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testContainsValue() {
 map = TDoubleShortMaps.create();
 map.put(1.0, (short) 1);
 map.put(2.0, (short) 2);
 decorator = new TDoubleShortMapDecorator(map);

 assertTrue(decorator.containsValue((short) 1));
 assertTrue(decorator.containsValue((short) 2));
 assertFalse(decorator.containsValue((short) 3));
 }

 @Test
 public void testContainsKey() {
 map = TDoubleShortMaps.create();
 map.put(1.0, (short) 1);
 map.put(2.0, (short) 2);
 decorator = new TDoubleShortMapDecorator(map);

 assertTrue(decorator.containsKey(Double.valueOf(1.0)));
 assertTrue(decorator.containsKey(Double.valueOf(2.0)));
 assertFalse(decorator.containsKey(Double.valueOf(3.0)));
 }

 @Test
 public void testSize() {
 map = TDoubleShortMaps.create();
 map.put(1.0, (short) 1);
 map.put(2.0, (short) 2);
 decorator = new TDoubleShortMapDecorator(map);

 assertEquals(2, decorator.size());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap map1 = TDoubleShortMaps.create();
 map1.put(1.0, (short) 1);
 map1.put(2.0, (short) 2);

 TDoubleShortMap map2 = TDoubleShortMaps.create();
 map2.put(3.0, (short) 3);
 map2.put(4.0, (short) 4);

 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
 decorator.putAll(map1);
 decorator.putAll(map2);

 assertEquals(map1.size() + map2.size(), decorator.size());
 assertTrue(Arrays.equals(map1.toArrayConcat(map2), decorator.entrySet().toArray()));
 }

 @Test
 public void testClone() {
 map = TDoubleShortMaps.create();
 map.put(1.0, (short) 1);
 map.put(2.0, (short) 2);
 decorator = new TDoubleShortMapDecorator(map);

 TDoubleShortMapDecorator clone = (TDoubleShortMapDecorator)decorator.clone();
 assertEquals(map, clone.getMap());
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 map = TDoubleShortMaps.create();
 map.put(1.0, (short) 1);
 map.put(2.0, (short) 2);
 decorator = new TDoubleShortMapDecorator(map);
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 TDoubleShortMapDecorator clone2 = (TDoubleShortMapDecorator)in.readObject();
 assertTrue(Arrays.equals(map.toArrayConcat(map), clone2.entrySet().toArray()));
 }
}