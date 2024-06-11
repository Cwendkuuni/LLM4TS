package gnu.trove.decorator.test;

import static org.junit.Assert.*;

import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setUp() {
     _map = new TDoubleShortMap();
     decorator = new TDoubleShortMapDecorator(_map);
 }

 // Test for constructor with empty map
 @Test
 public void testTDoubleShortMapDecoratorEmptyConstructor() {
     assertNotNull(decorator);
 }

 // Test for constructor with TDoubleShortMap
 @Test
 public void testTDoubleShortMapDecoratorWithMapConstructor() {
     assertNotNull(decorator);
     assertEquals(_map, decorator.getMap());
 }

 // Test for put method
 @Test
 public void testPut() {
     decorator.put(1.0, (short)1);
     assertEquals(1.0, decorator.keySet().toArray()[0]);
     assertEquals((short)1, decorator.get(1.0));
     assertEquals(1, decorator.size());

     decorator.put(2.0, (short)2);
     assertEquals(2.0, decorator.keySet().toArray()[1]);
     assertEquals((short)2, decorator.get(2.0));
 }

 // Test for get method with key in map
 @Test
 public void testGetWithKeyInMap() {
     decorator.put(1.0, (short)1);
     decorator.put(2.0, (short)2);
     assertEquals((short)1, decorator.get(1.0));
 }

 // Test for get method with key not in map
 @Test
 public void testGetWithKeyNotInMap() {
     decorator.put(1.0, (short)1);
     decorator.put(2.0, (short)2);
     assertEquals(null, decorator.get(3.0));
 }

 // Test for clear method
 @Test
 public void testClear() {
     decorator.put(1.0, (short)1);
     decorator.put(2.0, (short)2);
     assertEquals(2, decorator.size());
     decorator.clear();
     assertEquals(0, decorator.size());
 }

 // Test for remove method with key in map
 @Test
 public void testRemoveWithKeyInMap() {
     decorator.put(1.0, (short)1);
     decorator.put(2.0, (short)2);
     assertEquals(2, decorator.size());
     assertEquals((short)1, decorator.remove(1.0));
     assertEquals(1, decorator.size());
 }

 // Test for remove method with key not in map
 @Test
 public void testRemoveWithKeyNotInMap() {
     decorator.put(1.0, (short)1);
     decorator.put(2.0, (short)2);
     assertEquals(2, decorator.size());
     assertEquals(null, decorator.remove(3.0));
     assertEquals(2, decorator.size());
 }

 // Test for entrySet method
 @Test
 public void testEntrySet() {
     decorator.put(1.0, (short)1);
     decorator.put(2.0, (short)2);
     Set<Entry<Double, Short>> entries = decorator.entrySet();
     assertEquals(2, entries.size());
     assertTrue(entries.contains(new Entry<Double, Short>(1.0, (short)1)));
     assertTrue(entries.contains(new Entry<Double, Short>(2.0, (short)2)));
 }

 // Test for containsValue method
 @Test
 public void testContainsValue() {
     decorator.put(1.0, (short)1);
     decorator.put(1.0, (short)2);
     assertTrue(decorator.containsValue((short)1));
     assertFalse(decorator.containsValue((short)3));
 }

 // Test for containsKey method with key in map
 @Test
 public void testContainsKeyWithKeyInMap() {
     decorator.put(1.0, (short)1);
     decorator.put(2.0, (short)2);
     assertTrue(decorator.containsKey(1.0));
     assertTrue(decorator.containsKey(2.0));
 }

 // Test for containsKey method with key not in map
 @Test
 public void testContainsKeyWithKeyNotInMap() {
     decorator.put(1.0, (short)1);
     decorator.put(2.0, (short)2);
     assertFalse(decorator.containsKey(3.0));
 }

 // Test for size method
 @Test
 public void testSize() {
     assertEquals(0, decorator.size());
     decorator.put(1.0, (short)1);
     decorator.put(2.0, (short)2);
     assertEquals(2, decorator.size());
 }

 // Test for isEmpty method
 @Test
 public void testIsEmpty() {
     assertEquals(true, decorator.isEmpty());
     decorator.put(1.0, (short)1);
     assertEquals(false, decorator.isEmpty());
 }

 // Test for putAll method
 @Test
 public void testPutAll() {
     TDoubleShortMap map = new TDoubleShortMap();
     map.put(1.0, (short)1);
     map.put(2.0, (short)2);
     decorator.putAll(map);
     assertEquals(2, decorator.size());
     assertTrue(decorator.containsKey(1.0));
     assertTrue(decorator.containsKey(2.0));
 }

 // Test for serialization
 @Test
 public void testSerializable() throws IOException, ClassNotFoundException {
     decorator.put(1.0, (short)1);
     decorator.put(2.0, (short)2);
     ByteArrayOutputStream out = new ByteArrayOutputStream();
     ObjectOutput outStream = new ObjectOutputStream(out);
     decorator.writeExternal(outStream);
     ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
     TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator)in.readObject();
     assertEquals(2, deserializedDecorator.size());
     assertTrue(deserializedDecorator.containsKey(1.0));
     assertTrue(deserializedDecorator.containsKey(2.0));
 }
}