package net.sf.javaml.core;

import java.util.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class SparseInstanceTest {

 private SparseInstance sparseInstance;

 @Test
 public void testSparseInstanceEmptyConstruction() {
 sparseInstance = new SparseInstance();
 assertEquals(-1, sparseInstance.noAttributes());
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithNoAttributesAndDefaultValueConstruction() {
 sparseInstance = new SparseInstance(0);
 assertEquals(0, sparseInstance.noAttributes());
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithNoAttributesAndDefaultValueAndClassValueConstruction() {
 sparseInstance = new SparseInstance(0, "ClassValue");
 assertEquals("ClassValue", sparseInstance.classValue());
 assertEquals(0, sparseInstance.noAttributes());
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithNoAttributesAndDefaultValueAndClassValueAndValuesConstruction() {
 double[] datavector = new double[5];
 sparseInstance = new SparseInstance(datavector, 0.0, "ClassValue");
 assertEquals("ClassValue", sparseInstance.classValue());
 assertEquals(5, sparseInstance.noAttributes());
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithValuesConstruction() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
 sparseInstance = new SparseInstance(datavector);
 assertEquals(5, sparseInstance.noAttributes());
 assertFalse(sparseInstance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithValuesAndDefaultValueConstruction() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
 sparseInstance = new SparseInstance(datavector, 0.0);
 assertEquals(5, sparseInstance.noAttributes());
 assertFalse(sparseInstance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithValuesAndDefaultValueAndClassValueConstruction() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
 sparseInstance = new SparseInstance(datavector, 0.0, "ClassValue");
 assertEquals("ClassValue", sparseInstance.classValue());
 assertEquals(5, sparseInstance.noAttributes());
 assertFalse(sparseInstance.isEmpty());
 }

 @Test
 public void testValue() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 assertEquals(1.0, sparseInstance.value(0), 0.001);
 assertEquals(2.0, sparseInstance.value(1), 0.001);
 assertEquals(3.0, sparseInstance.value(2), 0.001);
 assertEquals(4.0, sparseInstance.value(3), 0.001);
 assertEquals(5.0, sparseInstance.value(4), 0.001);
 }

 @Test
 public void testClear() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 sparseInstance.clear();
 assertEquals(0, sparseInstance.noAttributes());
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testContainsKey() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 assertTrue(sparseInstance.containsKey(0));
 assertTrue(sparseInstance.containsKey(1));
 assertTrue(sparseInstance.containsKey(2));
 assertTrue(sparseInstance.containsKey(3));
 assertTrue(sparseInstance.containsKey(4));
 }

 @Test
 public void testContainsValue() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 for (int i = 0; i < 5; i++) {
 assertTrue(sparseInstance.containsValue(Double.valueOf(i + 1.0)));
 }
 }

 @Test
 public void testEntrySet() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 Set<Map.Entry<Integer, Double>> entrySet = sparseInstance.entrySet();
 assertEquals(5, entrySet.size());
 }

 @Test
 public void testGet() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 assertEquals(1.0, sparseInstance.get(0).doubleValue(), 0.001);
 assertEquals(2.0, sparseInstance.get(1).doubleValue(), 0.001);
 assertEquals(3.0, sparseInstance.get(2).doubleValue(), 0.001);
 assertEquals(4.0, sparseInstance.get(3).doubleValue(), 0.001);
 assertEquals(5.0, sparseInstance.get(4).doubleValue(), 0.001);
 }

 @Test
 public void testIsEmpty() {
 sparseInstance = new SparseInstance();
 assertTrue(sparseInstance.isEmpty());
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 assertFalse(sparseInstance.isEmpty());
 sparseInstance.clear();
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 Set<Integer> keySet = sparseInstance.keySet();
 assertEquals(5, keySet.size());
 }

 @Test
 public void testPut() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(1.5));
 assertEquals(1, sparseInstance.noAttributes());
 assertEquals(1.5, sparseInstance.value(0), 0.001);
 }

 @Test
 public void testPutAll() {
 Map<Integer, Double> map = new HashMap<Integer, Double>();
 map.put(Integer.valueOf(1), Double.valueOf(1.5));
 map.put(Integer.valueOf(2), Double.valueOf(2.5));
 map.put(Integer.valueOf(3), Double.valueOf(3.5));
 sparseInstance = new SparseInstance();
 sparseInstance.putAll(map);
 assertEquals(3, sparseInstance.noAttributes());
 assertEquals(1.5, sparseInstance.value(0), 0.001);
 assertEquals(2.5, sparseInstance.value(1), 0.001);
 assertEquals(3.5, sparseInstance.value(2), 0.001);
 }

 @Test
 public void testRemove() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 Double removedValue = sparseInstance.remove(Integer.valueOf(1));
 assertEquals(1.0, removedValue.doubleValue(), 0.001);
 assertEquals(4, sparseInstance.noAttributes());
 }

 @Test
 public void testSize() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 assertEquals(5, sparseInstance.size());
 }

 @Test
 public void testValues() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 Collection<Double> values = sparseInstance.values();
 assertEquals(5, values.size());
 }

 @Test
 public void testNoAttributes() {
 sparseInstance = new SparseInstance();
 assertEquals(-1, sparseInstance.noAttributes());
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 assertEquals(5, sparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 sparseInstance.removeAttribute(1);
 assertEquals(4, sparseInstance.noAttributes());
 }

 @Test
 public void testToString() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 assertEquals("{" + "[1.0, 2.0, 3.0, 4.0, 5.0]", sparseInstance.toString());
 }

 @Test
 public void testHashCode() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 SparseInstance sparseInstance2 = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 SparseInstance sparseInstance3 = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 6.0});
 assertEquals(sparseInstance.hashCode(), sparseInstance2.hashCode());
 assertNotSame(sparseInstance.hashCode(), sparseInstance3.hashCode());
 }

 @Test
 public void testEquals() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 SparseInstance sparseInstance2 = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 SparseInstance sparseInstance3 = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 6.0});
 assertTrue(sparseInstance.equals(sparseInstance2));
 assertFalse(sparseInstance.equals(sparseInstance3));
 }

 @Test
 public void testCopy() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 SparseInstance copy = sparseInstance.copy();
 assertEquals(sparseInstance.noAttributes(), copy.noAttributes());
 assertEquals(sparseInstance.value(0), copy.value(0), 0.001);
 assertEquals(sparseInstance.value(1), copy.value(1), 0.001);
 assertEquals(sparseInstance.value(2), copy.value(2), 0.001);
 assertEquals(sparseInstance.value(3), copy.value(3), 0.001);
 assertEquals(sparseInstance.value(4), copy.value(4), 0.001);
 }

 @Test
 public void testRemoveAttributes() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 Set<Integer> indices = new HashSet<Integer>();
 indices.add(1);
 indices.add(3);
 sparseInstance.removeAttributes(indices);
 assertEquals(3, sparseInstance.noAttributes());
 }
}