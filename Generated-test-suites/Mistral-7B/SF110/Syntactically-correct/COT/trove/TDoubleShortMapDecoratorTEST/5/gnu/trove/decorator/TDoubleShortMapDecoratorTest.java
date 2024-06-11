package test;

import gnu.trove.decorator.TDoubleShortMapDecorator;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputoutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(_map);
 }

 @Test
 public void testConstructor() {
 assertNull(decorator.getMap());
 }

 @Test
 public void testConstructorWithMap() {
 TDoubleShortMap map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 assertEquals(map, decorator.getMap());
 }

 @Test
 public void testPut() {
 double key = 1.0;
 short value = 1;
 short originalValue = decorator.put(key, value);
 assertNull(originalValue);
 assertEquals(value, decorator.get(key));
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
 decorator.put(null, (short) 1);
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
 decorator.put(1.0, null);
 }

 @Test
 public void testGet() {
 double key = 1.0;
 short value = 1;
 decorator.put(key, value);
 assertEquals(value, decorator.get(key));
 }

 @Test(expected = NullPointerException.class)
 public void testGetWithNullKey() {
 decorator.get(null);
 }

 @Test(expected = ClassCastException.class)
 public void testGetWithNonDoubleKey() {
 decorator.put("key", (short) 1);
 decorator.get("key");
 }

 @Test
 public void testClear() {
 decorator.put(1.0, 1);
 decorator.clear();
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemove() {
 decorator.put(1.0, 1);
 short originalValue = decorator.remove(1.0);
 assertEquals(1, (int) originalValue);
 assertFalse(decorator.containsKey(1.0));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
 decorator.remove(null);
 }

 @Test(expected = ClassCastException.class)
 public void testRemoveWithNonDoubleKey() {
 decorator.put("key", 1);
 decorator.remove("key");
 }

 @Test
 public void testEntrySet() {
 Map<Double, Short> entrySet = decorator.entrySet();
 Assert.assertEquals(Collections.emptyMap(), entrySet);
 }

 @Test
 public void testContainsValue() {
 decorator.put(1.0, 1);
 assertTrue(decorator.containsValue((short) 1));
 }

 @Test
 public void testContainsKey() {
 decorator.put(1.0, 1);
 assertTrue(decorator.containsKey(1.0));
 }

 @Test
 public void testSize() {
 decorator.put(1.0, 1);
 assertEquals(1, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 decorator.put(1.0, 1);
 assertFalse(decorator.isEmpty());
 decorator.clear();
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 Map<Double, Short> map = new HashMap<>();
 map.put(1.0, 1);
 map.put(2.0, 2);
 decorator.putAll(map);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testWrapKey() {
 assertEquals(1.0, decorator.wrapKey(1.0));
 }

 @Test
 public void testUnwrapKey() {
 assertEquals(1.0, decorator.unwrapKey(1.0), 0.0);
 }

 @Test
 public void testWrapValue() {
 assertEquals((short) 1, decorator.wrapValue((short) 1));
 }

 @Test
 public void testUnwrapValue() {
 assertEquals((short) 1, decorator.unwrapValue((short) 1));
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 ByteArrayInputoutputStream input = new ByteArrayInputoutputStream(output.toByteArray());
 decorator.writeExternal(output);
 TDoubleShortMapDecorator readDecorator = new TDoubleShortMapDecorator();
 readDecorator.readExternal(input);
 Assert.assertEquals(decorator.size(), readDecorator.size());
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
 TDoubleShortMapDecorator clone = (TDoubleShortMapDecorator) decorator.clone();
 assertEquals(decorator.size(), clone.size());
 }
}