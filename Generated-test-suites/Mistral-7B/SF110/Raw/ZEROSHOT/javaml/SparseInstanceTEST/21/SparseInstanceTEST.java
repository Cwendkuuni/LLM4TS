package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class SparseInstanceTest {

 private SparseInstance sparseInstance;

 @Test
 public void testDefaultConstructor() {
 sparseInstance = new SparseInstance();
 assertEquals(-1, sparseInstance.noAttributes());
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
 }

 @Test
 public void testConstructorWithDatavectorDefaultValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector, 0.0);
 assertEquals(3, sparseInstance.noAttributes());
 assertNull(sparseInstance.getClassValue());
 }

 @Test
 public void testConstructorWithDatavectorAndClassValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector, "ClassValue");
 assertEquals(3, sparseInstance.noAttributes());
 assertEquals("ClassValue", sparseInstance.getClassValue());
 }

 @Test
 public void testConstructorWithDatavectorDefaultValueAndClassValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector, 0.0, "ClassValue");
 assertEquals(3, sparseInstance.noAttributes());
 assertNull(sparseInstance.getClassValue());
 }

 @Test
 public void testValue() {
 sparseInstance = new SparseInstance(3, 1.0, "ClassValue");
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));
 assertEquals(2.0, sparseInstance.value(0), 0.001);
 assertEquals(1.0, sparseInstance.value(2), 0.001);
 }

 @Test
 public void testClear() {
 sparseInstance = new SparseInstance(3, 1.0, "ClassValue");
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));
 sparseInstance.clear();
 assertEquals(0, sparseInstance.noAttributes());
 assertNull(sparseInstance.getClassValue());
 }

 @Test
 public void testContainsKey() {
 sparseInstance = new SparseInstance(3, 1.0, "ClassValue");
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));
 assertTrue(sparseInstance.containsKey(Integer.valueOf(0)));
 assertFalse(sparseInstance.containsKey(Integer.valueOf(4)));
 }

 @Test
 public void testContainsValue() {
 sparseInstance = new SparseInstance(3, 1.0, "ClassValue");
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));
 assertTrue(sparseInstance.containsValue(Double.valueOf(2.0)));
 assertFalse(sparseInstance.containsValue(Double.valueOf(4.0)));
 }

 @Test
 public void testEntrySet() {
 sparseInstance = new SparseInstance(3, 1.0, "ClassValue");
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));
 Set<Map.Entry<Integer, Double>> entrySet = sparseInstance.entrySet();
 assertEquals(2, entrySet.size());
 }

 @Test
 public void testGet() {
 sparseInstance = new SparseInstance(3, 1.0, "ClassValue");
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));
 assertEquals(2.0, sparseInstance.get(Integer.valueOf(0)).doubleValue(), 0.001);
 assertEquals(3.0, sparseInstance.get(Integer.valueOf(2)).doubleValue(), 0.001);
 assertEquals(1.0, sparseInstance.get(Integer.valueOf(1)).doubleValue(), 0.001);
 }

 @Test
 public void testIsEmpty() {
 sparseInstance = new SparseInstance();
 assertTrue(sparseInstance.isEmpty());
 sparseInstance = new SparseInstance(3, 1.0, "ClassValue");
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));
 assertFalse(sparseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 sparseInstance = new SparseInstance(3, 1.0, "ClassValue");
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));
 TreeSet<Integer> keySet = sparseInstance.keySet();
 assertEquals(2, keySet.size());
 }

 @Test
 public void testPut() {
 sparseInstance = new SparseInstance();
 assertEquals(0, sparseInstance.noAttributes());
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 assertEquals(1, sparseInstance.noAttributes());
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(3.0));
 assertEquals(2, sparseInstance.noAttributes());
 }

 @Test
 public void testPutAll() {
 Map<Integer, Double> map = new HashMap<>();
 map.put(Integer.valueOf(0), Double.valueOf(2.0));
 map.put(Integer.valueOf(1), Double.valueOf(3.0));
 map.put(Integer.valueOf(2), Double.valueOf(4.0));
 sparseInstance = new SparseInstance();
 sparseInstance.putAll(map);
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testRemove() {
 sparseInstance = new SparseInstance(3, 1.0, "ClassValue");
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));
 assertEquals(2.0, sparseInstance.get(Integer.valueOf(0)).doubleValue(), 0.001);
 assertEquals(3.0, sparseInstance.get(Integer.valueOf(2)).doubleValue(), 0.001);
 sparseInstance.remove(Integer.valueOf(0));
 assertEquals(1.0, sparseInstance.get(Integer.valueOf(0)).doubleValue(), 0.001);
 assertEquals(3.0, sparseInstance.get(Integer.valueOf(2)).doubleValue(), 0.001);
 assertEquals(2, sparseInstance.noAttributes());
 }

 @Test
 public void testNoAttributes() {
 sparseInstance = new SparseInstance(3, 1.0, "ClassValue");
 assertEquals(3, sparseInstance.noAttributes());
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));
 assertEquals(2, sparseInstance.noAttributes());
 sparseInstance.clear();
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 sparseInstance = new SparseInstance(3, 1.0, "ClassValue");
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));
 assertEquals(2, sparseInstance.noAttributes());
 sparseInstance.removeAttribute(0);
 assertEquals(1, sparseInstance.noAttributes());
 sparseInstance.removeAttribute(1);
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testToString() {
 sparseInstance = new SparseInstance(3, 1.0, "ClassValue");
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));
 assertEquals("{2.0=0, 1.0=2.0, 3.0=2, ClassValue=}", sparseInstance.toString());
 }

 @Test
 public void testHashCode() {
 sparseInstance = new SparseInstance(3, 1.0, "ClassValue");
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));
 HashMap<SparseInstance, Integer> map = new HashMap<>();
 map.put(sparseInstance, 1);
 SparseInstance instance = new SparseInstance(sparseInstance);
 assertEquals(map.get(sparseInstance), map.get(instance));
 }

 @Test
 public void testEquals() {
 sparseInstance = new SparseInstance(3, 1.0, "ClassValue");
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));
 SparseInstance instance = new SparseInstance(sparseInstance);
 assertTrue(sparseInstance.equals(instance));
 instance.put(Integer.valueOf(1), Double.valueOf(4.0));
 assertFalse(sparseInstance.equals(instance));
 }

 @Test
 public void testCopy() {
 sparseInstance = new SparseInstance(3, 1.0, "ClassValue");
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));
 SparseInstance instance = sparseInstance.copy();
 assertEquals(sparseInstance, instance);
 }

 @Test
 public void testRemoveAttributes() {
 sparseInstance = new SparseInstance(3, 1.0, "ClassValue");
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));
 Set<Integer> indices = new HashSet<Integer>();
 indices.add(0);
 indices.add(2);
 sparseInstance.removeAttributes(indices);
 assertEquals(1, sparseInstance.noAttributes());
 }
}