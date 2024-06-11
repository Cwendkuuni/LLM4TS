package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class SparseInstanceTest {

 private SparseInstance sparseInstance;

 @Test
 public void testSparseInstanceWithNoArguments() {
 sparseInstance = new SparseInstance();
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testSparseInstanceWithOneArgument() {
 sparseInstance = new SparseInstance(1);
 assertEquals(1, sparseInstance.noAttributes());
 }

 @Test
 public void testSparseInstanceWithTwoArguments() {
 sparseInstance = new SparseInstance(2, 0.0);
 assertEquals(2, sparseInstance.noAttributes());
 }

 @Test
 public void testSparseInstanceWithThreeArguments() {
 sparseInstance = new SparseInstance(3, "classValue");
 assertEquals("classValue", sparseInstance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithDoubleArray() {
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector);
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testSetNoAttributes() {
 sparseInstance = new SparseInstance(5);
 sparseInstance.setNoAttributes(3);
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNoAttributesWithNegativeNumber() {
 sparseInstance = new SparseInstance(5);
 sparseInstance.setNoAttributes(-3);
 }

 @Test
 public void testDefaultValue() {
 sparseInstance = new SparseInstance(3, 1.5);
 assertEquals(1.5, sparseInstance.getDefaultValue(), 0.001);
 }

 @Test
 public void testEmptyInstance() {
 sparseInstance = new SparseInstance();
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testContainsKey() {
 sparseInstance = new SparseInstance(3, 1.0, 1);
 assertTrue(sparseInstance.containsKey(1));
 }

 @Test
 public void testContainsValue() {
 sparseInstance = new SparseInstance(3, 1.0, 1);
 assertTrue(sparseInstance.containsValue(1.0));
 }

 @Test
 public void testEntrySet() {
 sparseInstance = new SparseInstance(3, 1.0, 1);
 Set<Map.Entry<Integer, Double>> entrySet = sparseInstance.entrySet();
 assertFalse(entrySet.isEmpty());
 }

 @Test
 public void testGet() {
 sparseInstance = new SparseInstance(3, 1.0, 1);
 assertEquals(1.0, sparseInstance.get(1), 0.001);
 }

 @Test
 public void testGetWithInvalidKey() {
 sparseInstance = new SparseInstance(3, 1.0, 1);
 assertEquals(sparseInstance.getDefaultValue(), sparseInstance.get(2), 0.001);
 }

 @Test
 public void testIsEmpty() {
 sparseInstance = new SparseInstance();
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 sparseInstance = new SparseInstance(3, 1.0, 1);
 Set<Integer> keySet = sparseInstance.keySet();
 assertFalse(keySet.isEmpty());
 }

 @Test
 public void testPut() {
 sparseInstance = new SparseInstance(3, 1.0, 1);
 sparseInstance.put(2, 2.0);
 assertEquals(2.0, sparseInstance.get(2), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithInvalidKey() {
 sparseInstance = new SparseInstance(3, 1.0, 1);
 sparseInstance.put(-1, 2.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithDuplicateKey() {
 sparseInstance = new SparseInstance(3, 1.0, 1);
 sparseInstance.put(1, 2.0);
 sparseInstance.put(1, 3.0);
 }

 @Test
 public void testPutAll() {
 Map<Integer, Double> hashMap = new HashMap<>();
 hashMap.put(1, 1.0);
 hashMap.put(2, 2.0);
 sparseInstance = new SparseInstance();
 sparseInstance.putAll(hashMap);
 assertEquals(2.0, sparseInstance.get(2), 0.001);
 }

 @Test(expected = NullPointerException.class)
 public void testPutAllWithNullMap() {
 sparseInstance = new SparseInstance();
 sparseInstance.putAll(null);
 }

 @Test
 public void testRemove() {
 sparseInstance = new SparseInstance(3, 1.0, 1);
 sparseInstance.put(2, 2.0);
 sparseInstance.remove(2);
 assertFalse(sparseInstance.containsKey(2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveWithInvalidKey() {
 sparseInstance = new SparseInstance(3, 1.0, 1);
 sparseInstance.remove(-1);
 }

 @Test
 public void testSize() {
 sparseInstance = new SparseInstance(3, 1.0, 1);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(2, 2.0);
 assertEquals(2, sparseInstance.noAttributes());
 }

 @Test
 public void testValues() {
 sparseInstance = new SparseInstance(3, 1.0, 1);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(2, 2.0);
 List<Double> values = new ArrayList<>(sparseInstance.values());
 assertEquals(2, values.size());
 }

 @Test
 public void testNoAttributes() {
 sparseInstance = new SparseInstance(3, 1.0, 1);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(2, 2.0);
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 sparseInstance = new SparseInstance(4, 1.0, 1);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(2, 2.0);
 sparseInstance.put(3, 3.0);
 sparseInstance.removeAttribute(2);
 assertEquals(2, sparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttributeWithInvalidIndex() {
 sparseInstance = new SparseInstance(4, 1.0, 1);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(2, 2.0);
 sparseInstance.put(3, 3.0);
 sparseInstance.removeAttribute(-1);
 }

 @Test
 public void testToString() {
 sparseInstance = new SparseInstance(3, 1.0, 1);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(2, 2.0);
 String toString = sparseInstance.toString();
 assertTrue(toString.contains("[1.0, 2.0]"));
 }

}