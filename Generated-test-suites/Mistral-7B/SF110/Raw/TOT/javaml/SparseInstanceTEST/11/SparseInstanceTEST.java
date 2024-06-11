package net.sf.javaml.core;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SparseInstanceTest {

 private SparseInstance sparseInstance;

 @Before
 public void setUp() {
 sparseInstance = new SparseInstance(2);
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
 }

 @Test
 public void testEmptyInstance() {
 SparseInstance emptyInstance = new SparseInstance();
 assertTrue(emptyInstance.isEmpty());
 assertEquals(0, emptyInstance.noAttributes());
 }

 @Test
 public void testSetNoAttributes() {
 SparseInstance instance = new SparseInstance();
 instance.setNoAttributes(5);
 assertEquals(5, instance.noAttributes());
 }

 @Test
 public void testDefaultValueInstance() {
 SparseInstance instance = new SparseInstance(1, 1.0);
 assertEquals(1.0, instance.value(0), 0.001);
 assertEquals(1.0, instance.defaultValue);
 }

 @Test
 public void testGetValue() {
 assertEquals(1.0, sparseInstance.value(0), 0.001);
 assertEquals(2.0, sparseInstance.value(1), 0.001);
 }

 @Test
 public void testClear() {
 SparseInstance instance = new SparseInstance(2);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.clear();
 assertTrue(instance.isEmpty());
 assertEquals(0, instance.noAttributes());
 }

 @Test
 public void testContainsKey() {
 assertTrue(sparseInstance.containsKey(Integer.valueOf(0)));
 assertTrue(sparseInstance.containsKey(Integer.valueOf(1)));
 assertFalse(sparseInstance.containsKey(Integer.valueOf(2)));
 }

 @Test
 public void testContainsValue() {
 assertTrue(sparseInstance.containsValue(Double.valueOf(1.0)));
 assertTrue(sparseInstance.containsValue(Double.valueOf(2.0)));
 assertFalse(sparseInstance.containsValue(Double.valueOf(3.0)));
 }

 @Test
 public void testGet() {
 assertEquals(1.0, sparseInstance.get(Integer.valueOf(0)), 0.001);
 assertEquals(2.0, sparseInstance.get(Integer.valueOf(1)), 0.001);
 assertEquals(0.0, sparseInstance.get(Integer.valueOf(2)), 0.001);
 }

 @Test
 public void testIsEmpty() {
 SparseInstance emptyInstance = new SparseInstance();
 assertTrue(emptyInstance.isEmpty());
 SparseInstance notEmptyInstance = new SparseInstance(2);
 notEmptyInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertFalse(notEmptyInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 Set<Integer> keys = sparseInstance.keySet();
 assertEquals(2, keys.size());
 assertTrue(keys.contains(Integer.valueOf(0)));
 assertTrue(keys.contains(Integer.valueOf(1)));
 }

 @Test
 public void testPut() {
 SparseInstance instance = new SparseInstance(2);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.put(Integer.valueOf(1), Double.valueOf(3.0));
 assertEquals(3.0, instance.get(Integer.valueOf(1)), 0.001);
 }

 @Test
 public void testPutAll() {
 Map<Integer, Double> map = new HashMap<>();
 map.put(Integer.valueOf(0), Double.valueOf(1.0));
 map.put(Integer.valueOf(1), Double.valueOf(2.0));
 SparseInstance instance = new SparseInstance(2);
 instance.putAll(map);
 assertEquals(1.0, instance.get(Integer.valueOf(0)), 0.001);
 assertEquals(2.0, instance.get(Integer.valueOf(1)), 0.001);
 }

 @Test
 public void testRemove() {
 SparseInstance instance = new SparseInstance(3);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.put(Integer.valueOf(2), Double.valueOf(3.0));
 assertEquals(3.0, instance.get(Integer.valueOf(2)), 0.001);
 instance.remove(Integer.valueOf(2));
 assertFalse(instance.containsKey(Integer.valueOf(2)));
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullKey() {
 SparseInstance instance = new SparseInstance(2);
 try {
 instance.put(null, Double.valueOf(1.0));
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 SparseInstance instance = new SparseInstance(2);
 try {
 instance.put(Integer.valueOf(0), null);
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveInvalidIndex() {
 SparseInstance instance = new SparseInstance(2);
 instance.remove(Integer.valueOf(3));
 }

 @Test
 public void testNoAttributes() {
 assertEquals(2, sparseInstance.noAttributes());
 SparseInstance emptyInstance = new SparseInstance();
 assertEquals(0, emptyInstance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 SparseInstance instance = new SparseInstance(3);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.put(Integer.valueOf(2), Double.valueOf(3.0));
 instance.removeAttribute(1);
 assertEquals(2, instance.noAttributes());
 assertEquals(1.0, instance.get(Integer.valueOf(0)), 0.001);
 assertFalse(instance.containsKey(Integer.valueOf(1)));
 }

 @Test
 public void testCopy() {
 SparseInstance original = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0));
 SparseInstance copy = original.copy();
 assertEquals(original.noAttributes(), copy.noAttributes());
 for (int i = 0; i < original.noAttributes(); ++i) {
 assertEquals(original.get(Integer.valueOf(i)), copy.get(Integer.valueOf(i)), 0.001);
 }
 }

 @Test
 public void testRemoveAttributes() {
 SparseInstance instance = new SparseInstance(5);
 Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(1, 2, 3));
 instance.removeAttributes(indicesToRemove);
 assertEquals(2, instance.noAttributes());
 assertFalse(instance.containsKey(Integer.valueOf(1)));
 assertFalse(instance.containsKey(Integer.valueOf(2)));
 assertFalse(instance.containsKey(Integer.valueOf(3)));
 }

 @Test
 public void testToString() {
 assertEquals("{1.0=1.0, 2.0=2.0}", sparseInstance.toString());
 }
}