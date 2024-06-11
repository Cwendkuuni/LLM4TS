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
 public void testConstructorWithOneArgument() {
 sparseInstance = new SparseInstance(1);
 assertEquals(1, sparseInstance.noAttributes());
 assertNull(sparseInstance.getClassValue());
 }

 @Test
 public void testConstructorWithTwoArguments() {
 sparseInstance = new SparseInstance(2, 0.0);
 assertEquals(2, sparseInstance.noAttributes());
 assertNull(sparseInstance.getClassValue());
 }

 @Test
 public void testConstructorWithThreeArguments() {
 sparseInstance = new SparseInstance(2, 0.0, "TestClass");
 assertEquals(2, sparseInstance.noAttributes());
 assertEquals("TestClass", sparseInstance.getClassValue());
 }

 @Test
 public void testConstructorWithArrayOfDoubles() {
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector);
 assertEquals(3, sparseInstance.noAttributes());
 assertArrayEquals(datavector, sparseInstance.toArray(), 0.0);
 }

 @Test
 public void testConstructorWithArrayOfDoublesAndDefaultValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector, 0.5);
 assertEquals(3, sparseInstance.noAttributes());
 assertArrayEquals(datavector, sparseInstance.toArray(), 0.5);
 }

 @Test
 public void testConstructorWithArrayOfDoublesAndClassValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector, 0.5, "TestClass");
 assertEquals(3, sparseInstance.noAttributes());
 assertEquals("TestClass", sparseInstance.getClassValue());
 assertArrayEquals(datavector, sparseInstance.toArray(), 0.5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNoAttributesWithNegativeValue() {
 sparseInstance = new SparseInstance();
 sparseInstance.setNoAttributes(-1);
 }

 @Test
 public void testSetNoAttributes() {
 sparseInstance = new SparseInstance();
 sparseInstance.setNoAttributes(3);
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testInitiateWithEmptyArray() {
 double[] datavector = {};
 sparseInstance = new SparseInstance(datavector);
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testInitiateWithNonEmptyArray() {
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector);
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testValue() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 assertEquals(1.0, sparseInstance.value(0), 0.0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueWithInvalidIndex() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 assertEquals(1.0, sparseInstance.value(0), 0.0);
 sparseInstance.value(-1);
 }

 @Test
 public void testClear() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 sparseInstance.put(1, 2.0);
 sparseInstance.clear();
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testContainsKey() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 assertTrue(sparseInstance.containsKey(0));
 }

 @Test
 public void testContainsValue() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 assertTrue(sparseInstance.containsValue(1.0));
 }

 @Test
 public void testEntrySet() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 sparseInstance.put(1, 2.0);
 Set<Map.Entry<Integer, Double>> entries = sparseInstance.entrySet();
 assertEquals(2, entries.size());
 }

 @Test
 public void testGet() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 assertEquals(1.0, sparseInstance.get(0), 0.0);
 }

 @Test
 public void testGetWithInvalidKey() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 assertEquals(0.0, sparseInstance.get(1), 0.0);
 }

 @Test
 public void testIsEmpty() {
 sparseInstance = new SparseInstance();
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testIsEmptyAfterPuttingValue() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 assertFalse(sparseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 sparseInstance.put(1, 2.0);
 Set<Integer> keys = sparseInstance.keySet();
 assertEquals(2, keys.size());
 }

 @Test
 public void testPut() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 assertEquals(1.0, sparseInstance.get(0), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithInvalidKey() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 try {
 sparseInstance.put(Double.NaN, Double.NaN);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithNullKey() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 try {
 sparseInstance.put(null, Double.NaN);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithNullValue() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 try {
 sparseInstance.put(0, null);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testPutAll() {
 Map<Integer, Double> map = new HashMap<>();
 map.put(0, 1.0);
 map.put(1, 2.0);
 sparseInstance = new SparseInstance();
 sparseInstance.putAll(map);
 assertEquals(2, sparseInstance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutAllWithNullMap() {
 Map<Integer, Double> map = null;
 sparseInstance = new SparseInstance();
 try {
 sparseInstance.putAll(map);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testRemove() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 sparseInstance.put(1, 2.0);
 sparseInstance.remove(1);
 assertEquals(1, sparseInstance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveWithInvalidKey() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 try {
 sparseInstance.remove(Double.NaN);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveWithInvalidIndex() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 try {
 sparseInstance.remove(-1);
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testSize() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 assertEquals(1, sparseInstance.noAttributes());
 }

 @Test
 public void testValues() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 sparseInstance.put(1, 2.0);
 Collection<Double> values = sparseInstance.values();
 assertEquals(2, values.size());
 }

 @Test
 public void testNoAttributes() {
 sparseInstance = new SparseInstance();
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testNoAttributesAfterPuttingValue() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 assertEquals(1, sparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 sparseInstance.put(1, 2.0);
 sparseInstance.removeAttribute(1);
 assertEquals(1, sparseInstance.noAttributes());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeWithInvalidIndex() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 sparseInstance.put(1, 2.0);
 try {
 sparseInstance.removeAttribute(-1);
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testToString() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 sparseInstance.put(1, 2.0);
 assertEquals("{" + sparseInstance.data.toString() + ";" + sparseInstance.getClassValue() + "}", sparseInstance.toString());
 }

 @Test
 public void testHashCode() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 sparseInstance.put(1, 2.0);
 SparseInstance other = new SparseInstance();
 other.put(0, 1.0);
 other.put(1, 2.0);
 assertEquals(sparseInstance.hashCode(), other.hashCode());
 }

 @Test
 public void testEquals() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 sparseInstance.put(1, 2.0);
 SparseInstance other = new SparseInstance();
 other.put(0, 1.0);
 other.put(1, 2.0);
 assertTrue(sparseInstance.equals(other));
 }

 @Test
 public void testCopy() {
 SparseInstance original = new SparseInstance();
 original.put(0, 1.0);
 original.put(1, 2.0);
 SparseInstance copy = original.copy();
 assertEquals(original.noAttributes(), copy.noAttributes());
 assertArrayEquals(original.toArray(), copy.toArray(), 0.0);
 }

 @Test
 public void testRemoveAttributes() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 sparseInstance.put(1, 2.0);
 sparseInstance.put(2, 3.0);
 Set<Integer> indices = new HashSet<>();
 indices.add(1);
 indices.add(2);
 sparseInstance.removeAttributes(indices);
 assertEquals(1, sparseInstance.noAttributes());
 }
}