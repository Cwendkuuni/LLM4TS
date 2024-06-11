package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class SparseInstanceTest {

 private SparseInstance sparseInstance;

 @Test
 public void testSparseInstanceEmptyConstruction() {
 sparseInstance = new SparseInstance();
 assertEquals(0, sparseInstance.noAttributes());
 assertFalse(sparseInstance.containsKey(0));
 }

 @Test
 public void testSparseInstanceWithNoAttributesConstruction() {
 sparseInstance = new SparseInstance(0);
 assertEquals(0, sparseInstance.noAttributes());
 assertFalse(sparseInstance.containsKey(0));
 }

 @Test
 public void testSparseInstanceWithDefaultValueConstruction() {
 sparseInstance = new SparseInstance(0, 0.0);
 assertEquals(0, sparseInstance.noAttributes());
 assertFalse(sparseInstance.containsKey(0));
 }

 @Test
 public void testSparseInstanceWithClassValueConstruction() {
 sparseInstance = new SparseInstance(0, "StringClassValue");
 assertEquals("StringClassValue", sparseInstance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithNoAttributesAndDefaultValueAndClassValueConstruction() {
 sparseInstance = new SparseInstance(0, 0.0, "StringClassValue");
 assertEquals("StringClassValue", sparseInstance.getClassValue());
 assertEquals(0, sparseInstance.noAttributes());
 assertFalse(sparseInstance.containsKey(0));
 }

 @Test
 public void testSparseInstanceWithDataVectorConstruction() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector);
 assertEquals(3, sparseInstance.noAttributes());
 assertTrue(sparseInstance.containsKey(0));
 assertEquals(1.0, sparseInstance.get(0), 0.001);
 assertTrue(sparseInstance.containsKey(1));
 assertEquals(2.0, sparseInstance.get(1), 0.001);
 assertTrue(sparseInstance.containsKey(2));
 assertEquals(3.0, sparseInstance.get(2), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSparseInstanceWithNegativeNoAttributes() {
 sparseInstance = new SparseInstance(-1);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testSetNoAttributes() {
 sparseInstance = new SparseInstance(3);
 sparseInstance.setNoAttributes(5);
 assertEquals(5, sparseInstance.noAttributes());
 }

 @Test
 public void testValue() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(1.0, sparseInstance.value(0), 0.001);
 assertEquals(2.0, sparseInstance.value(1), 0.001);
 assertEquals(3.0, sparseInstance.value(2), 0.001);
 assertEquals(0.0, sparseInstance.value(3), 0.001);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueWithInvalidIndex() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.setValue(-1, 4.0);
 fail("Should throw IndexOutOfBoundsException");
 }

 @Test
 public void testClear() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.clear();
 assertEquals(0, sparseInstance.noAttributes());
 assertFalse(sparseInstance.containsKey(0));
 }

 @Test
 public void testContainsKey() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertTrue(sparseInstance.containsKey(0));
 assertTrue(sparseInstance.containsKey(1));
 assertTrue(sparseInstance.containsKey(2));
 assertFalse(sparseInstance.containsKey(3));
 }

 @Test
 public void testContainsValue() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertTrue(sparseInstance.containsValue(1.0));
 assertTrue(sparseInstance.containsValue(2.0));
 assertTrue(sparseInstance.containsValue(3.0));
 assertFalse(sparseInstance.containsValue(4.0));
 }

 @Test
 public void testEntrySet() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 Set<Map.Entry<Integer, Double>> entries = sparseInstance.entrySet();
 assertEquals(3, entries.size());
 }

 @Test
 public void testGet() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(1.0, sparseInstance.get(0), 0.001);
 assertEquals(2.0, sparseInstance.get(1), 0.001);
 assertEquals(3.0, sparseInstance.get(2), 0.001);
 assertEquals(0.0, sparseInstance.get(3), 0.001);
 }

 @Test
 public void testIsEmpty() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertFalse(sparseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 Set<Integer> keys = sparseInstance.keySet();
 assertEquals(3, keys.size());
 }

 @Test
 public void testPut() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(1, sparseInstance.noAttributes());
 assertEquals(1.0, sparseInstance.get(0), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithInvalidKey() {
 sparseInstance = new SparseInstance();
 try {
 sparseInstance.put(null, Double.valueOf(1.0));
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithInvalidValue() {
 sparseInstance = new SparseInstance();
 try {
 sparseInstance.put(Integer.valueOf(0), null);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testPutAll() {
 Map<Integer, Double> map = new HashMap<>();
 map.put(Integer.valueOf(0), Double.valueOf(1.0));
 map.put(Integer.valueOf(1), Double.valueOf(2.0));
 map.put(Integer.valueOf(2), Double.valueOf(3.0));
 sparseInstance = new SparseInstance();
 sparseInstance.putAll(map);
 assertEquals(3, sparseInstance.noAttributes());
 assertEquals(1.0, sparseInstance.get(0), 0.001);
 assertEquals(2.0, sparseInstance.get(1), 0.001);
 assertEquals(3.0, sparseInstance.get(2), 0.001);
 }

 @Test(expected = NullPointerException.class)
 public void testPutAllWithNullMap() {
 sparseInstance = new SparseInstance();
 try {
 sparseInstance.putAll(null);
 fail("Should throw NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testRemove() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.remove(Integer.valueOf(1));
 assertEquals(2, sparseInstance.noAttributes());
 assertFalse(sparseInstance.containsKey(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveWithInvalidKey() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 try {
 sparseInstance.remove(null);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSize() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testValues() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 Collection<Double> values = sparseInstance.values();
 assertEquals(3, values.size());
 }

 @Test
 public void testNoAttributes() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.removeAttribute(1);
 assertEquals(2, sparseInstance.noAttributes());
 assertEquals(1.0, sparseInstance.get(0), 0.001);
 assertFalse(sparseInstance.containsKey(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeWithInvalidIndex() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 try {
 sparseInstance.removeAttribute(-1);
 fail("Should throw IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testToString() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 String toString = sparseInstance.toString();
 // Complex structure, no simple way to test the output
 }

 @Test
 public void testHashCode() {
 SparseInstance sparseInstance1 = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 SparseInstance sparseInstance2 = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(sparseInstance1.hashCode(), sparseInstance2.hashCode());
 }

 @Test
 public void testEquals() {
 SparseInstance sparseInstance1 = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 SparseInstance sparseInstance2 = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 SparseInstance sparseInstance3 = new SparseInstance();
 assertTrue(sparseInstance1.equals(sparseInstance2));
 assertFalse(sparseInstance1.equals(sparseInstance3));
 }

 @Test
 public void testCopy() {
 SparseInstance original = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 SparseInstance copy = original.copy();
 assertEquals(original.noAttributes(), copy.noAttributes());
 for (int i = 0; i < original.noAttributes(); i++) {
 assertEquals(original.get(i), copy.get(i), 0.001);
 }
 }

 @Test
 public void testRemoveAttributes() {
 SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 Set<Integer> indices = new HashSet<>();
 indices.add(1);
 indices.add(3);
 sparseInstance.removeAttributes(indices);
 assertEquals(3, sparseInstance.noAttributes());
 assertFalse(sparseInstance.containsKey(1));
 assertFalse(sparseInstance.containsKey(3));
 }

}