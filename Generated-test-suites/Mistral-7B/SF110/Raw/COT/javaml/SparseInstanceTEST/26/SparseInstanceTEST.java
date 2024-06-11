package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class SparseInstanceTest {

 private SparseInstance sparseInstance;

 @Test
 public void testConstructorWithNoArguments() {
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
 assertEquals("ClassValue", sparseInstance.getClassValue().toString());
 }

 @Test
 public void testConstructorWithNoAttributesDefaultValueAndClassValue() {
 sparseInstance = new SparseInstance(0, 0.0, "ClassValue");
 assertEquals(0, sparseInstance.noAttributes());
 assertNull(sparseInstance.getClassValue());
 }

 @Test
 public void testConstructorWithDatavectorNoAttributesDefaultValue() {
 double[] datavector = new double[5];
 sparseInstance = new SparseInstance(datavector);
 assertEquals(5, sparseInstance.noAttributes());
 assertNull(sparseInstance.getClassValue());
 }

 @Test
 public void testConstructorWithDatavectorNoAttributesDefaultValueAndClassValue() {
 double[] datavector = new double[5];
 sparseInstance = new SparseInstance(datavector, "ClassValue");
 assertEquals(5, sparseInstance.noAttributes());
 assertEquals("ClassValue", sparseInstance.getClassValue().toString());
 }

 @Test
 public void testConstructorWithDatavectorNoAttributesDefaultValueAndDefaultValue() {
 double[] datavector = new double[5];
 sparseInstance = new SparseInstance(datavector, 0.0);
 assertEquals(5, sparseInstance.noAttributes());
 assertNull(sparseInstance.getClassValue());
 }

 @Test
 public void testConstructorWithDatavectorNoAttributesDefaultValueAndClassValueAndDefaultValue() {
 double[] datavector = new double[5];
 sparseInstance = new SparseInstance(datavector, 0.0, "ClassValue");
 assertEquals(5, sparseInstance.noAttributes());
 assertEquals("ClassValue", sparseInstance.getClassValue().toString());
 }

 @Test
 public void testSetNoAttributes() {
 sparseInstance = new SparseInstance();
 sparseInstance.setNoAttributes(5);
 assertEquals(5, sparseInstance.noAttributes());
 }

 @Test
 public void testGetValue() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(1.0, sparseInstance.value(0), 0.001);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueOutOfBounds() {
 sparseInstance = new SparseInstance();
 assertEquals(this.defaultValue, sparseInstance.value(-1), 0.001);
 }

 @Test
 public void testClear() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 sparseInstance.clear();
 assertEquals(0, sparseInstance.noAttributes());
 assertNull(sparseInstance.getClassValue());
 }

 @Test
 public void testContainsKey() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertTrue(sparseInstance.containsKey(Integer.valueOf(0)));
 }

 @Test
 public void testContainsValue() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertTrue(sparseInstance.containsValue(Double.valueOf(1.0)));
 }

 @Test
 public void testEntrySet() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 Set<Map.Entry<Integer, Double>> entrySet = sparseInstance.entrySet();
 assertEquals(1, entrySet.size());
 }

 @Test
 public void testGet() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(1.0, sparseInstance.get(Integer.valueOf(0)), 0.001);
 }

 @Test(expected = NullPointerException.class)
 public void testGetNullKey() {
 sparseInstance = new SparseInstance();
 assertNull(sparseInstance.get(null));
 }

 @Test
 public void testIsEmpty() {
 sparseInstance = new SparseInstance();
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 Set<Integer> keySet = sparseInstance.keySet();
 assertEquals(1, keySet.size());
 }

 @Test
 public void testPut() {
 sparseInstance = new SparseInstance();
 assertEquals(Double.valueOf(0.0), sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0)));
 }

 @Test
 public void testPutAll() {
 Map<Integer, Double> map = new HashMap<>();
 map.put(Integer.valueOf(0), Double.valueOf(1.0));
 map.put(Integer.valueOf(1), Double.valueOf(2.0));
 sparseInstance = new SparseInstance();
 sparseInstance.putAll(map);
 assertEquals(2, sparseInstance.noAttributes());
 }

 @Test
 public void testRemove() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(Double.valueOf(1.0), sparseInstance.remove(Integer.valueOf(0)));
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
 sparseInstance = new SparseInstance();
 assertNull(sparseInstance.remove(null));
 }

 @Test
 public void testSize() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(1, sparseInstance.size());
 }

 @Test
 public void testValues() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 Collection<Double> values = sparseInstance.values();
 assertEquals(1, values.size());
 }

 @Test
 public void testNoAttributes() {
 sparseInstance = new SparseInstance();
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 sparseInstance.removeAttribute(0);
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeOutOfBounds() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 sparseInstance.removeAttribute(-1);
 }

 @Test
 public void testToString() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals("{" + sparseInstance.data.toString() + ";" + sparseInstance.classValue + "}", sparseInstance.toString());
 }

 @Test
 public void testHashCode() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 SparseInstance other = new SparseInstance();
 other.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(sparseInstance.hashCode(), other.hashCode());
 }

 @Test
 public void testEquals() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 SparseInstance other = new SparseInstance();
 other.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertTrue(sparseInstance.equals(other));
 }

 @Test
 public void testEqualsDifferentClass() {
 Object other = new Object();
 assertFalse(sparseInstance.equals(other));
 }

 @Test
 public void testEqualsWithNull() {
 assertFalse(sparseInstance.equals(null));
 }

 @Test
 public void testCopy() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 SparseInstance copiedInstance = sparseInstance.copy();
 assertEquals(sparseInstance.noAttributes(), copiedInstance.noAttributes());
 }

 @Test
 public void testRemoveAttributesNullIndices() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 Set<Integer> indices = new HashSet<>();
 assertEquals(0, sparseInstance.removeAttributes(indices).size());
 assertEquals(1, sparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttributesEmptyIndices() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 Set<Integer> indices = new HashSet<>();
 assertEquals(0, sparseInstance.removeAttributes(indices).size());
 assertEquals(1, sparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttributesValidIndices() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
 Set<Integer> indices = new HashSet<>();
 indices.add(0);
 indices.add(1);
 assertEquals(2, sparseInstance.noAttributes());
 assertEquals(1, sparseInstance.removeAttributes(indices).size());
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttributesValidIndicesOutOfOrder() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
 Set<Integer> indices = new HashSet<>();
 indices.add(1);
 indices.add(0);
 assertEquals(2, sparseInstance.noAttributes());
 assertEquals(1, sparseInstance.removeAttributes(indices).size());
 assertEquals(0, sparseInstance.noAttributes());
 }
}