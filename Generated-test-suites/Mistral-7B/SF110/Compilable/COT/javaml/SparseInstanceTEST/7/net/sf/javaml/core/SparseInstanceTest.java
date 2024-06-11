package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class SparseInstanceTest {

 private SparseInstance sparseInstance;

 @Test
 public void testSparseInstanceWithNoArguments() {
 sparseInstance = new SparseInstance();
 assertNotNull(sparseInstance);
 }

 @Test
 public void testSparseInstanceWithOneArgument() {
 sparseInstance = new SparseInstance(1);
 assertNotNull(sparseInstance);
 }

 @Test
 public void testSparseInstanceWithTwoArguments() {
 sparseInstance = new SparseInstance(2, 0.0);
 assertNotNull(sparseInstance);
 }

 @Test
 public void testSparseInstanceWithThreeArguments() {
 sparseInstance = new SparseInstance(3, 0.0, "SomeClass");
 assertNotNull(sparseInstance);
 }

 @Test
 public void testSparseInstanceWithDoubleArray() {
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector);
 assertNotNull(sparseInstance);
 }

 @Test
 public void testSparseInstanceWithDoubleArrayAndDefaultValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector, 0.0);
 assertNotNull(sparseInstance);
 }

 @Test
 public void testSparseInstanceWithDoubleArrayAndClassValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector, 0.0, "SomeClass");
 assertNotNull(sparseInstance);
 }

 @Test
 public void testSetNoAttributes() {
 sparseInstance = new SparseInstance(3, 0.0, "SomeClass");
 sparseInstance.setNoAttributes(5);
 assertEquals(5, sparseInstance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNoAttributesWithNegativeValue() {
 sparseInstance = new SparseInstance(3, 0.0, "SomeClass");
 sparseInstance.setNoAttributes(-1);
 }

 @Test
 public void testValue() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(1.0, sparseInstance.value(0), 0.001);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueWithOutOfBoundsIndex() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.value(3);
 }

 @Test
 public void testClear() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.clear();
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testContainsKey() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertTrue(sparseInstance.containsKey(Integer.valueOf(0)));
 assertFalse(sparseInstance.containsKey(Integer.valueOf(4)));
 }

 @Test
 public void testContainsValue() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertTrue(sparseInstance.containsValue(Double.valueOf(1.0)));
 assertFalse(sparseInstance.containsValue(Double.valueOf(4.0)));
 }

 @Test
 public void testEntrySet() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 Set<Map.Entry<Integer, Double>> entries = sparseInstance.entrySet();
 assertFalse(entries.isEmpty());
 }

 @Test
 public void testGet() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(1.0, sparseInstance.get(Integer.valueOf(0)), 0.001);
 assertEquals(0.0, sparseInstance.get(Integer.valueOf(2)), 0.001);
 }

 @Test(expected = NoSuchElementException.class)
 public void testGetWithNonExistentKey() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.get(Integer.valueOf(3));
 }

 @Test
 public void testIsEmpty() {
 sparseInstance = new SparseInstance();
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 Set<Integer> keys = sparseInstance.keySet();
 assertFalse(keys.isEmpty());
 }

 @Test
 public void testPut() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.put(Integer.valueOf(3), Double.valueOf(4.0));
 assertEquals(4.0, sparseInstance.get(Integer.valueOf(3)), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithDuplicateKey() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 }

 @Test
 public void testPutAll() {
 Map<Integer, Double> map = new HashMap<>();
 map.put(Integer.valueOf(0), Double.valueOf(1.0));
 map.put(Integer.valueOf(1), Double.valueOf(2.0));
 map.put(Integer.valueOf(2), Double.valueOf(3.0));
 sparseInstance.putAll(map);
 assertEquals(3.0, sparseInstance.get(Integer.valueOf(2)), 0.001);
 }

 @Test(expected = NullPointerException.class)
 public void testPutAllWithNullMap() {
 sparseInstance.putAll(null);
 }

 @Test
 public void testRemove() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(4.0));
 Double removedValue = sparseInstance.remove(Integer.valueOf(1));
 assertEquals(4.0, removedValue, 0.001);
 assertFalse(sparseInstance.containsKey(Integer.valueOf(1)));
 }

 @Test(expected = NoSuchElementException.class)
 public void testRemoveWithNonExistentKey() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.remove(Integer.valueOf(3));
 }

 @Test
 public void testSize() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(3, sparseInstance.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSizeWithModification() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.put(Integer.valueOf(3), Double.valueOf(4.0));
 sparseInstance.size();
 sparseInstance.put(Integer.valueOf(4), Double.valueOf(5.0));
 }

 @Test
 public void testValues() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 Collection<Double> values = sparseInstance.values();
 assertFalse(values.isEmpty());
 }

 @Test
 public void testNoAttributes() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0});
 sparseInstance.removeAttribute(1);
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeWithOutOfBoundsIndex() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0});
 sparseInstance.removeAttribute(4);
 }
}