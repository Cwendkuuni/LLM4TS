package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class SparseInstanceTest {

 private SparseInstance sparseInstance;

 @Test
 public void testEmptyConstructor() {
 sparseInstance = new SparseInstance();
 assertEquals(0, sparseInstance.noAttributes());
 assertNull(sparseInstance.getClassValue());
 }

 @Test
 public void testConstructorWithNoAttributes() {
 sparseInstance = new SparseInstance(0);
 assertEquals(0, sparseInstance.noAttributes());
 assertNull(sparseInstance.getClassValue());
 }

 @Test
 public void testConstructorWithNoAttributesAndDefaultValue() {
 sparseInstance = new SparseInstance(0, 0.0);
 assertEquals(0, sparseInstance.noAttributes());
 assertNull(sparseInstance.getClassValue());
 }

 @Test
 public void testConstructorWithNoAttributesAndClassValue() {
 sparseInstance = new SparseInstance(0, "ClassValue");
 assertEquals(0, sparseInstance.noAttributes());
 assertEquals("ClassValue", sparseInstance.getClassValue());
 }

 @Test
 public void testConstructorWithNoAttributesDefaultValueAndClassValue() {
 sparseInstance = new SparseInstance(0, 0.0, "ClassValue");
 assertEquals(0, sparseInstance.noAttributes());
 assertNull(sparseInstance.getClassValue());
 }

 @Test
 public void testConstructorWithDatavector() {
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector);
 assertEquals(3, sparseInstance.noAttributes());
 assertNull(sparseInstance.getClassValue());
 assertEquals(1.0, sparseInstance.get(0), 0.0);
 assertEquals(2.0, sparseInstance.get(1), 0.0);
 assertEquals(3.0, sparseInstance.get(2), 0.0);
 }

 @Test
 public void testConstructorWithDatavectorDefaultValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector, 0.0);
 assertEquals(3, sparseInstance.noAttributes());
 assertNull(sparseInstance.getClassValue());
 assertEquals(0.0, sparseInstance.get(3), 0.0);
 assertEquals(1.0, sparseInstance.get(0), 0.0);
 assertEquals(2.0, sparseInstance.get(1), 0.0);
 assertEquals(3.0, sparseInstance.get(2), 0.0);
 }

 @Test
 public void testConstructorWithDatavectorClassValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector, "ClassValue");
 assertEquals(3, sparseInstance.noAttributes());
 assertEquals("ClassValue", sparseInstance.getClassValue());
 assertEquals(1.0, sparseInstance.get(0), 0.0);
 assertEquals(2.0, sparseInstance.get(1), 0.0);
 assertEquals(3.0, sparseInstance.get(2), 0.0);
 }

 @Test
 public void testConstructorWithDatavectorDefaultValueAndClassValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector, 0.0, "ClassValue");
 assertEquals(3, sparseInstance.noAttributes());
 assertEquals("ClassValue", sparseInstance.getClassValue());
 assertEquals(0.0, sparseInstance.get(3), 0.0);
 assertEquals(1.0, sparseInstance.get(0), 0.0);
 assertEquals(2.0, sparseInstance.get(1), 0.0);
 assertEquals(3.0, sparseInstance.get(2), 0.0);
 }

 @Test
 public void testSetNoAttributes() {
 sparseInstance = new SparseInstance(0);
 sparseInstance.setNoAttributes(5);
 assertEquals(5, sparseInstance.noAttributes());
 }

 @Test
 public void testValue() {
 sparseInstance = new SparseInstance(3, 1.0, 1.0, 2.0);
 assertEquals(1.0, sparseInstance.value(0), 0.0);
 assertEquals(1.0, sparseInstance.value(1), 0.0);
 assertEquals(2.0, sparseInstance.value(2), 0.0);
 }

 @Test
 public void testClear() {
 sparseInstance = new SparseInstance(3, 1.0, 1.0, 2.0);
 sparseInstance.clear();
 assertEquals(0, sparseInstance.noAttributes());
 assertNull(sparseInstance.getClassValue());
 }

 @Test
 public void testContainsKey() {
 sparseInstance = new SparseInstance(3, 1.0, 1.0, 2.0);
 assertTrue(sparseInstance.containsKey(0));
 assertTrue(sparseInstance.containsKey(1));
 assertTrue(sparseInstance.containsKey(2));
 assertFalse(sparseInstance.containsKey(3));
 }

 @Test
 public void testContainsValue() {
 sparseInstance = new SparseInstance(3, 1.0, 1.0, 2.0);
 assertTrue(sparseInstance.containsValue(1.0));
 assertFalse(sparseInstance.containsValue(2.1));
 }

 @Test
 public void testEntrySet() {
 sparseInstance = new SparseInstance(3, 1.0, 1.0, 2.0);
 Set<Map.Entry<Integer, Double>> entries = sparseInstance.entrySet();
 assertEquals(3, entries.size());
 }

 @Test
 public void testGet() {
 sparseInstance = new SparseInstance(3, 1.0, 1.0, 2.0);
 assertEquals(1.0, sparseInstance.get(0), 0.0);
 assertEquals(1.0, sparseInstance.get(1), 0.0);
 assertEquals(2.0, sparseInstance.get(2), 0.0);
 assertEquals(0.0, sparseInstance.get(3), 0.0);
 }

 @Test
 public void testIsEmpty() {
 sparseInstance = new SparseInstance(3, 1.0, 1.0, 2.0);
 assertFalse(sparseInstance.isEmpty());
 sparseInstance.clear();
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 sparseInstance = new SparseInstance(3, 1.0, 1.0, 2.0);
 Set<Integer> keys = sparseInstance.keySet();
 assertEquals(3, keys.size());
 }

 @Test
 public void testPut() {
 sparseInstance = new SparseInstance(3, 1.0, 1.0, 2.0);
 sparseInstance.put(3, 2.0);
 assertEquals(4, sparseInstance.noAttributes());
 assertEquals(2.0, sparseInstance.get(3), 0.0);
 }

 @Test
 public void testPutAll() {
 Map<Integer, Double> map = new HashMap<>();
 map.put(0, 1.0);
 map.put(1, 2.0);
 sparseInstance = new SparseInstance(3, 1.0, 1.0, 2.0);
 sparseInstance.putAll(map);
 assertEquals(4, sparseInstance.noAttributes());
 assertEquals(1.0, sparseInstance.get(0), 0.0);
 assertEquals(2.0, sparseInstance.get(1), 0.0);
 assertEquals(1.0, sparseInstance.get(2), 0.0);
 assertEquals(2.0, sparseInstance.get(3), 0.0);
 }

 @Test
 public void testRemove() {
 sparseInstance = new SparseInstance(3, 1.0, 1.0, 2.0);
 sparseInstance.put(3, 2.0);
 Double removed = sparseInstance.remove(3);
 assertEquals(2.0, removed.doubleValue());
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testSize() {
 sparseInstance = new SparseInstance(3, 1.0, 1.0, 2.0);
 sparseInstance.put(3, 2.0);
 assertEquals(4, sparseInstance.noAttributes());
 }

 @Test
 public void testValues() {
 sparseInstance = new SparseInstance(3, 1.0, 1.0, 2.0);
 Collection<Double> values = sparseInstance.values();
 assertEquals(3, values.size());
 }

 @Test
 public void testNoAttributes() {
 sparseInstance = new SparseInstance(3, 1.0, 1.0, 2.0);
 assertEquals(3, sparseInstance.noAttributes());
 sparseInstance.removeAttribute(0);
 assertEquals(2, sparseInstance.noAttributes());
 }

 @Test
 public void testToString() {
 sparseInstance = new SparseInstance(3, 1.0, 1.0, 2.0);
 assertEquals("{1.0=1.0, 2.0=2.0, 3.0=2.0;ClassValue}", sparseInstance.toString());
 }

 @Test
 public void testHashCode() {
 sparseInstance = new SparseInstance(3, 1.0, 1.0, 2.0);
 SparseInstance sparseInstance2 = new SparseInstance(3, 1.0, 1.0, 2.0);
 SparseInstance sparseInstance3 = new SparseInstance(3, 1.1, 1.0, 2.0);
 assertEquals(sparseInstance.hashCode(), sparseInstance2.hashCode());
 assertNotEquals(sparseInstance.hashCode(), sparseInstance3.hashCode());
 }

 @Test
 public void testEquals() {
 sparseInstance = new SparseInstance(3, 1.0, 1.0, 2.0);
 SparseInstance sparseInstance2 = new SparseInstance(3, 1.0, 1.0, 2.0);
 SparseInstance sparseInstance3 = new SparseInstance(3, 1.1, 1.0, 2.0);
 assertTrue(sparseInstance.equals(sparseInstance2));
 assertFalse(sparseInstance.equals(sparseInstance3));
 }

 @Test
 public void testCopy() {
 sparseInstance = new SparseInstance(3, 1.0, 1.0, 2.0);
 SparseInstance copied = sparseInstance.copy();
 assertEquals(sparseInstance, copied);
 }

 @Test
 public void testRemoveAttributes() {
 sparseInstance = new SparseInstance(3, 1.0, 1.0, 2.0);
 Set<Integer> indices = new HashSet<>();
 indices.add(0);
 indices.add(2);
 sparseInstance.removeAttributes(indices);
 assertEquals(1, sparseInstance.noAttributes());
 }
}