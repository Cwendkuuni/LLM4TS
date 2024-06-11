package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class SparseInstanceTest {

 private SparseInstance sparseInstance;

 @Test
 public void testSparseInstanceWithDefaultConstructor() {
 sparseInstance = new SparseInstance();
 assertEquals(-1, sparseInstance.noAttributes());
 }

 @Test
 public void testSparseInstanceWithNoAttributes() {
 sparseInstance = new SparseInstance(5);
 assertEquals(5, sparseInstance.noAttributes());
 }

 @Test
 public void testSparseInstanceWithDefaultValue() {
 sparseInstance = new SparseInstance(5, 1.0);
 assertEquals(5, sparseInstance.noAttributes());
 assertEquals(1.0, sparseInstance.getDefaultValue(), 0.001);
 }

 @Test
 public void testSparseInstanceWithClassValue() {
 sparseInstance = new SparseInstance(5, "TestClass");
 assertEquals("TestClass", sparseInstance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithDatavector() {
 double[] datavector = {1.2, 2.3, 3.4, 0.0, 5.6};
 sparseInstance = new SparseInstance(datavector);
 assertEquals(5, sparseInstance.noAttributes());
 assertEquals(1.2, sparseInstance.get(0), 0.001);
 assertEquals(2.3, sparseInstance.get(1), 0.001);
 assertEquals(3.4, sparseInstance.get(2), 0.001);
 assertEquals(0.0, sparseInstance.get(4), 0.001);
 assertEquals(5.6, sparseInstance.get(5), 0.001);
 }

 @Test
 public void testValue() {
 sparseInstance = new SparseInstance(5, 1.0);
 assertEquals(1.0, sparseInstance.value(0), 0.001);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueWithInvalidIndex() {
 sparseInstance = new SparseInstance(5, 1.0);
 sparseInstance.value(6);
 }

 @Test
 public void testClear() {
 sparseInstance = new SparseInstance(5, 1.0);
 sparseInstance.put(1, 2.0);
 sparseInstance.put(3, 3.0);
 sparseInstance.clear();
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testContainsKey() {
 sparseInstance = new SparseInstance(5, 1.0);
 sparseInstance.put(1, 2.0);
 assertTrue(sparseInstance.containsKey(1));
 }

 @Test
 public void testContainsValue() {
 sparseInstance = new SparseInstance(5, 1.0);
 sparseInstance.put(1, 2.0);
 assertTrue(sparseInstance.containsValue(2.0));
 }

 @Test
 public void testEntrySet() {
 sparseInstance = new SparseInstance(5, 1.0);
 sparseInstance.put(1, 2.0);
 sparseInstance.put(3, 3.0);
 Set<Map.Entry<Integer, Double>> entrySet = sparseInstance.entrySet();
 assertEquals(2, entrySet.size());
 }

 @Test
 public void testGet() {
 sparseInstance = new SparseInstance(5, 1.0);
 sparseInstance.put(1, 2.0);
 assertEquals(2.0, sparseInstance.get(1), 0.001);
 assertEquals(1.0, sparseInstance.get(4), 0.001);
 }

 @Test
 public void testIsEmpty() {
 sparseInstance = new SparseInstance(5, 1.0);
 assertTrue(sparseInstance.isEmpty());
 sparseInstance.put(1, 2.0);
 assertFalse(sparseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 sparseInstance = new SparseInstance(5, 1.0);
 sparseInstance.put(1, 2.0);
 sparseInstance.put(3, 3.0);
 Set<Integer> keySet = sparseInstance.keySet();
 assertEquals(2, keySet.size());
 }

 @Test
 public void testPut() {
 sparseInstance = new SparseInstance(5, 1.0);
 assertEquals(1.0, sparseInstance.get(0), 0.001);
 sparseInstance.put(1, 2.0);
 assertEquals(2.0, sparseInstance.get(1), 0.001);
 }

 @Test
 public void testPutAll() {
 Map<Integer, Double> map = new HashMap<>();
 map.put(1, 2.0);
 map.put(3, 3.0);
 sparseInstance = new SparseInstance(5, 1.0);
 sparseInstance.putAll(map);
 assertEquals(2, sparseInstance.noAttributes());
 assertEquals(2.0, sparseInstance.get(1), 0.001);
 assertEquals(3.0, sparseInstance.get(3), 0.001);
 }

 @Test
 public void testRemove() {
 sparseInstance = new SparseInstance(5, 1.0);
 sparseInstance.put(1, 2.0);
 sparseInstance.put(3, 3.0);
 sparseInstance.remove(1);
 assertEquals(1, sparseInstance.noAttributes());
 assertEquals(3.0, sparseInstance.get(0), 0.001);
 }

 @Test
 public void testSize() {
 sparseInstance = new SparseInstance(5, 1.0);
 sparseInstance.put(1, 2.0);
 sparseInstance.put(3, 3.0);
 assertEquals(2, sparseInstance.noAttributes());
 }

 @Test
 public void testValues() {
 sparseInstance = new SparseInstance(5, 1.0);
 sparseInstance.put(1, 2.0);
 sparseInstance.put(3, 3.0);
 Collection<Double> values = sparseInstance.values();
 assertEquals(2, values.size());
 }

 @Test
 public void testNoAttributes() {
 sparseInstance = new SparseInstance(5, 1.0);
 assertEquals(-1, sparseInstance.noAttributes());
 sparseInstance.put(1, 2.0);
 assertEquals(1, sparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 sparseInstance = new SparseInstance(5, 1.0);
 sparseInstance.put(1, 2.0);
 sparseInstance.put(3, 3.0);
 sparseInstance.removeAttribute(1);
 assertEquals(1, sparseInstance.noAttributes());
 assertEquals(3.0, sparseInstance.get(0), 0.001);
 }

 @Test
 public void testToString() {
 sparseInstance = new SparseInstance(5, 1.0);
 sparseInstance.put(1, 2.0);
 sparseInstance.put(3, 3.0);
 assertEquals("{" + sparseInstance.data.toString() + ";TestClass}", sparseInstance.toString());
 }

 @Test
 public void testHashCode() {
 sparseInstance = new SparseInstance(5, 1.0);
 sparseInstance.put(1, 2.0);
 sparseInstance.put(3, 3.0);
 int hashCode1 = sparseInstance.hashCode();
 SparseInstance sparseInstanceCopy = new SparseInstance();
 (sparseInstanceCopy.data = new HashMap<Integer, Double>()).putAll(sparseInstance.data);
 sparseInstanceCopy.defaultValue = sparseInstance.defaultValue;
 sparseInstanceCopy.noAttributes = sparseInstance.noAttributes;
 sparseInstanceCopy.setClassValue(sparseInstance.getClassValue());
 int hashCode2 = sparseInstanceCopy.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testEquals() {
 sparseInstance = new SparseInstance(5, 1.0);
 sparseInstance.put(1, 2.0);
 sparseInstance.put(3, 3.0);
 SparseInstance sparseInstanceCopy = new SparseInstance();
 (sparseInstanceCopy.data = new HashMap<Integer, Double>()).putAll(sparseInstance.data);
 sparseInstanceCopy.defaultValue = sparseInstance.defaultValue;
 sparseInstanceCopy.noAttributes = sparseInstance.noAttributes;
 sparseInstanceCopy.setClassValue(sparseInstance.getClassValue());
 assertTrue(sparseInstance.equals(sparseInstanceCopy));
 }

 @Test
 public void testCopy() {
 sparseInstance = new SparseInstance(5, 1.0);
 sparseInstance.put(1, 2.0);
 sparseInstance.put(3, 3.0);
 SparseInstance copiedInstance = sparseInstance.copy();
 assertEquals(sparseInstance, copiedInstance);
 }

 @Test
 public void testRemoveAttributes() {
 sparseInstance = new SparseInstance(5, 1.0);
 sparseInstance.put(1, 2.0);
 sparseInstance.put(3, 3.0);
 Set<Integer> indices = new HashSet<>();
 indices.add(1);
 indices.add(3);
 sparseInstance.removeAttributes(indices);
 assertEquals(2, sparseInstance.noAttributes());
 assertEquals(1.0, sparseInstance.get(0), 0.001);
 }
}