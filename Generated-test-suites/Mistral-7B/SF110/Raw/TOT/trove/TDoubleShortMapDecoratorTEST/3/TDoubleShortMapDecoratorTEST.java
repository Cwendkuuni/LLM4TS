import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStarStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Collectors;

@RunWith(org.junit.runner.JUnit4.class)
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
 assertEquals(Collections.emptyMap(), decorator);
 assertTrue(decorator.isEmpty());
 assertFalse(decorator.containsKey(null));
 assertFalse(decorator.containsKey(Double.NaN));
 assertFalse(decorator.containsKey(1.0));
 assertFalse(decorator.containsValue((short) 1));
 }

 @Test
 public void testPut() {
 decorator.put(1.0, (short) 1);
 assertTrue(decorator.containsKey(1.0));
 assertEquals((short) 1, decorator.get(1.0));

 decorator.put(1.0, (short) 2);
 assertEquals((short) 2, decorator.get(1.0));
 assertNull(decorator.get(null));
 assertNull(decorator.get(Double.NaN));
 }

 @Test
 public void testClear() {
 decorator.put(1.0, (short) 1);
 decorator.clear();
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemove() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 assertEquals((short) 1, decorator.remove(1.0));
 assertFalse(decorator.containsKey(1.0));
 assertEquals((short) 2, decorator.get(2.0));
 }

 @Test
 public void testEntrySet() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 Set<Map.Entry<Double, Short>> entrySet = decorator.entrySet();
 assertEquals(2, entrySet.size());
 Iterator<Map.Entry<Double, Short>> iterator = entrySet.iterator();
 Map.Entry<Double, Short> firstEntry = iterator.next();
 assertEquals(1.0, firstEntry.getKey());
 assertEquals((short) 1, firstEntry.getValue());
 Map.Entry<Double, Short> secondEntry = iterator.next();
 assertEquals(2.0, secondEntry.getKey());
 assertEquals((short) 2, secondEntry.getValue());
 }

 @Test
 public void testContainsValue() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 assertTrue(decorator.containsValue((short) 1));
 assertTrue(decorator.containsValue((short) 2));
 }

 @Test
 public void testContainsKey() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 assertTrue(decorator.containsKey(1.0));
 assertTrue(decorator.containsKey(2.0));
 assertFalse(decorator.containsKey(null));
 assertFalse(decorator.containsKey(Double.NaN));
 }

 @Test
 public void testSize() {
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
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ByteArrayInputStreamp byteArrayInputStream = new ByteArrayInputStreamp(byteArrayOutputStream.toByteArray());
 decorator.writeExternal(byteArrayOutput);
 decorator = new TDoubleShortMapDecorator();
 decorator.readExternal(byteArrayInputStream);
 assertEquals(_map, decorator.getMap());
 }

 @Test
 public void testCloneable() {
 TDoubleShortMapDecorator clone = decorator.clone();
 assertEquals(_map, clone.getMap());
 }

 @Test
 public void testNoEntryKeyAndValue() {
 double noEntryKey = Double.MIN_VALUE;
 short noEntryValue = Short.MIN_VALUE;
 TDoubleShortMapDecorator noEntryDecorator = new TDoubleShortMapDecorator();
 assertEquals(noEntryKey, noEntryDecorator.getNoEntryKey());
 assertEquals(noEntryValue, noEntryDecorator.getNoEntryValue());
 }

 @Test
 public void testWrapKeyAndUnwrapKey() {
 assertEquals(1.0, decorator.unwrapKey(1.0));
 assertEquals(1.0, decorator.wrapKey(1.0));
 }

 @Test
 public void testWrapValueAndUnwrapValue() {
 assertEquals((short) 1, decorator.unwrapValue((short) 1));
 assertEquals((short) 1, decorator.wrapValue((short) 1));
 }
}