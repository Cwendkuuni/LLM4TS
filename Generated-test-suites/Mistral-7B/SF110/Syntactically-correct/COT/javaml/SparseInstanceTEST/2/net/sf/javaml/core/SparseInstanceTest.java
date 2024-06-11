package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;

public class SparseInstanceTest {

 private static final double DEFAULT_VALUE = 0.0;

 @Test
 public void testSparseInstanceWithNoArguments() {
 SparseInstance sparseInstance = new SparseInstance();
 assertEquals(DefaultInstance.class, sparseInstance.getClassValue());
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithOneArgument() {
 int noAttributes = 5;
 SparseInstance sparseInstance = new SparseInstance(noAttributes);
 assertEquals(Integer.class, sparseInstance.getClassValue().getClass());
 assertEquals(noAttributes, sparseInstance.noAttributes());
 }

 @Test
 public void testSparseInstanceWithDefaultValueAndOneArgument() {
 int noAttributes = 5;
 SparseInstance sparseInstance = new SparseInstance(noAttributes, DEFAULT_VALUE);
 assertEquals(Integer.class, sparseInstance.getClassValue().getClass());
 assertEquals(noAttributes, sparseInstance.noAttributes());
 }

 @Test
 public void testSparseInstanceWithClassValue() {
 int noAttributes = 5;
 Object classValue = new Object();
 SparseInstance sparseInstance = new SparseInstance(noAttributes, classValue);
 assertEquals(classValue, sparseInstance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithDefaultValueAndClassValue() {
 int noAttributes = 5;
 Object classValue = new Object();
 SparseInstance sparseInstance = new SparseInstance(noAttributes, DEFAULT_VALUE, classValue);
 assertEquals(classValue, sparseInstance.getClassValue());
 assertEquals(noAttributes, sparseInstance.noAttributes());
 }

 @Test
 public void testSparseInstanceWithDataVector() {
 double[] datavector = {1.1, 2.2, 3.3, 4.4, 5.5};
 SparseInstance sparseInstance = new SparseInstance(datavector);
 assertEquals(Integer.class, sparseInstance.getClassValue().getClass());
 assertEquals(datavector.length, sparseInstance.noAttributes());
 for (int i = 0; i < datavector.length; i++) {
 assertEquals(datavector[i], sparseInstance.get(i));
 }
 }

 @Test
 public void testSparseInstanceWithDataVectorAndDefaultValue() {
 double[] datavector = {1.1, 2.2, 3.3, 4.4, 5.5};
 SparseInstance sparseInstance = new SparseInstance(datavector, DEFAULT_VALUE);
 assertEquals(Integer.class, sparseInstance.getClassValue().getClass());
 assertEquals(datavector.length, sparseInstance.noAttributes());
 for (int i = 0; i < datavector.length; i++) {
 if (datavector[i] != DEFAULT_VALUE) {
 assertEquals(datavector[i], sparseInstance.get(i));
 } else {
 assertEquals(DEFAULT_VALUE, sparseInstance.get(i));
 }
 }
 }

 @Test
 public void testSparseInstanceWithDataVectorAndClassValue() {
 double[] datavector = {1.1, 2.2, 3.3, 4.4, 5.5};
 Object classValue = new Object();
 SparseInstance sparseInstance = new SparseInstance(datavector, classValue);
 assertEquals(classValue, sparseInstance.getClassValue());
 assertEquals(datavector.length, sparseInstance.noAttributes());
 for (int i = 0; i < datavector.length; i++) {
 assertEquals(datavector[i], sparseInstance.get(i));
 }
 }

 @Test
 public void testSparseInstanceWithDataVectorAndDefaultValueAndClassValue() {
 double[] datavector = {1.1, 2.2, 3.3, 4.4, 5.5};
 Object classValue = new Object();
 SparseInstance sparseInstance = new SparseInstance(datavector, DEFAULT_VALUE, classValue);
 assertEquals(classValue, sparseInstance.getClassValue());
 assertEquals(datavector.length, sparseInstance.noAttributes());
 for (int i = 0; i < datavector.length; i++) {
 if (datavector[i] != DEFAULT_VALUE) {
 assertEquals(datavector[i], sparseInstance.get(i));
 } else {
 assertEquals(DEFAULT_VALUE, sparseInstance.get(i));
 }
 }
 }

 @Test
 public void testSetNoAttributes() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.setNoAttributes(5);
 assertEquals(5, sparseInstance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNoAttributesNegativeNumber() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.setNoAttributes(-5);
 }

 @Test
 public void testClear() {
 SparseInstance sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(2, 2.0);
 sparseInstance.clear();
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testContainsKey() {
 SparseInstance sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 assertTrue(sparseInstance.containsKey(1));
 }

 @Test
 public void testContainsValue() {
 SparseInstance sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 assertTrue(sparseInstance.containsValue(1.0));
 }

 @Test
 public void testEntrySet() {
 SparseInstance sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(2, 2.0);
 Set<Map.Entry<Integer, Double>> entries = sparseInstance.entrySet();
 assertEquals(2, entries.size());
 }

 @Test
 public void testGet() {
 SparseInstance sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 assertEquals(1.0, sparseInstance.get(1));
 }

 @Test
 public void testIsEmpty() {
 SparseInstance sparseInstance = new SparseInstance();
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 SparseInstance sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(2, 2.0);
 Set<Integer> keys = sparseInstance.keySet();
 assertEquals(2, keys.size());
 }

 @Test
 public void testPut() {
 SparseInstance sparseInstance = new SparseInstance(5);
 Double oldValue = sparseInstance.put(1, 1.0);
 assertEquals(null, oldValue);
 assertEquals(1.0, sparseInstance.get(1));
 }

 @Test
 public void testPutAll() {
 SparseInstance sparseInstance = new SparseInstance(5);
 Map<Integer, Double> map = new HashMap<>();
 map.put(1, 1.0);
 map.put(2, 2.0);
 sparseInstance.putAll(map);
 assertEquals(2, sparseInstance.noAttributes());
 for (Map.Entry<Integer, Double> entry : map.entrySet()) {
 assertEquals(entry.getValue(), sparseInstance.get(entry.getKey()));
 }
 }

 @Test
 public void testRemove() {
 SparseInstance sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(2, 2.0);
 Double oldValue = sparseInstance.remove(1);
 assertEquals(1.0, oldValue);
 assertFalse(sparseInstance.containsKey(1));
 }

 @Test
 public void testSize() {
 SparseInstance sparseInstance = new SparseInstance();
 assertEquals(0, sparseInstance.size());
 sparseInstance.put(1, 1.0);
 assertEquals(1, sparseInstance.size());
 }

 @Test
 public void testValues() {
 SparseInstance sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(2, 2.0);
 Collection<Double> values = sparseInstance.values();
 assertEquals(2, values.size());
 }

 @Test
 public void testNoAttributes() {
 SparseInstance sparseInstance = new SparseInstance(5);
 assertEquals(5, sparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 SparseInstance sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(2, 2.0);
 sparseInstance.removeAttribute(1);
 assertEquals(4, sparseInstance.noAttributes());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeNegativeNumber() {
 SparseInstance sparseInstance = new SparseInstance(5);
 sparseInstance.removeAttribute(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeOutOfBounds() {
 SparseInstance sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(2, 2.0);
 sparseInstance.removeAttribute(6);
 }

 @Test
 public void testToString() {
 SparseInstance sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(2, 2.0);
 String toString = sparseInstance.toString();
 assertTrue(toString.contains("{"));
 assertTrue(toString.contains("}"));
 }

 @Test
 public void testHashCode() {
 SparseInstance sparseInstance1 = new SparseInstance(5);
 SparseInstance sparseInstance2 = new SparseInstance(5);
 sparseInstance1.put(1, 1.0);
 sparseInstance1.put(2, 2.0);
 sparseInstance2.put(1, 1.0);
 sparseInstance2.put(2, 2.0);
 sparseInstance2.put(3, 3.0);
 assertNotEquals(sparseInstance1.hashCode(), sparseInstance2.hashCode());
 }

 @Test
 public void testEquals() {
 SparseInstance sparseInstance1 = new SparseInstance(5);
 SparseInstance sparseInstance2 = new SparseInstance(5);
 SparseInstance sparseInstance3 = new SparseInstance(5);
 sparseInstance1.put(1, 1.0);
 sparseInstance1.put(2, 2.0);
 sparseInstance2.put(1, 1.0);
 sparseInstance2.put(2, 2.0);
 sparseInstance3.put(1, 1.0);
 sparseInstance3.put(3, 3.0);
 assertTrue(sparseInstance1.equals(sparseInstance1));
 assertTrue(sparseInstance1.equals(sparseInstance2));
 assertFalse(sparseInstance1.equals(sparseInstance3));
 }

 @Test
 public void testCopy() {
 SparseInstance sparseInstance1 = new SparseInstance(5);
 SparseInstance sparseInstance2 = new SparseInstance();
 sparseInstance1.put(1, 1.0);
 sparseInstance1.put(2, 2.0);
 SparseInstance copy = sparseInstance1.copy();
 assertEquals(sparseInstance1, copy);
 }

 @Test
 public void testRemoveAttributes() {
 SparseInstance sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(2, 2.0);
 sparseInstance.put(3, 3.0);
 Set<Integer> indices = new HashSet<>();
 indices.add(2);
 indices.add(1);
 sparseInstance.removeAttributes(indices);
 assertEquals(2, sparseInstance.noAttributes());
 assertEquals(1.0, sparseInstance.get(0));
 assertEquals(3.0, sparseInstance.get(1));
 }
}