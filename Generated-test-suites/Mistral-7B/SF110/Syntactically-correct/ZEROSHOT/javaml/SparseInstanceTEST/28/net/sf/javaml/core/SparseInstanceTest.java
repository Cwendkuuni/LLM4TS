package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class SparseInstanceTest {

 private static final double DELTA = 0.001;

 @Test
 public void testEmptyConstructor() {
 SparseInstance instance = new SparseInstance();
 assertEquals(-1, instance.noAttributes);
 assertNull(instance.getClassValue());
 assertFalse(instance.isEmpty());
 assertNotNull(instance.data);
 assertEmpty(instance.keySet());
 }

 @Test
 public void testConstructorWithNoAttributes() {
 SparseInstance instance = new SparseInstance(0);
 assertEquals(0, instance.noAttributes);
 assertNull(instance.getClassValue());
 assertFalse(instance.isEmpty());
 assertNotNull(instance.data);
 assertEmpty(instance.keySet());
 }

 @Test
 public void testConstructorWithNoAttributesAndDefaultValue() {
 SparseInstance instance = new SparseInstance(0, 0.0);
 assertEquals(0, instance.noAttributes);
 assertNull(instance.getClassValue());
 assertFalse(instance.isEmpty());
 assertNotNull(instance.data);
 assertEmpty(instance.keySet());
 }

 @Test
 public void testConstructorWithNoAttributesAndClassValue() {
 SparseInstance instance = new SparseInstance(0, "ClassValue");
 assertEquals(0, instance.noAttributes);
 assertEquals("ClassValue", instance.getClassValue());
 assertFalse(instance.isEmpty());
 assertNotNull(instance.data);
 assertEmpty(instance.keySet());
 }

 @Test
 public void testConstructorWithNoAttributesAndDefaultValueAndClassValue() {
 SparseInstance instance = new SparseInstance(0, 0.0, "ClassValue");
 assertEquals(0, instance.noAttributes);
 assertEquals("ClassValue", instance.getClassValue());
 assertFalse(instance.isEmpty());
 assertNotNull(instance.data);
 assertEmpty(instance.keySet());
 }

 @Test
 public void testConstructorWithDatavector() {
 double[] datavector = new double[] { 1.0, 2.0, 3.0 };
 SparseInstance instance = new SparseInstance(datavector);
 assertEquals(3, instance.noAttributes());
 assertEquals(1.0, instance.value(0), DELTA);
 assertEquals(2.0, instance.value(1), DELTA);
 assertEquals(3.0, instance.value(2), DELTA);
 assertNull(instance.getClassValue());
 assertFalse(instance.isEmpty());
 assertNotNull(instance.data);
 assertFalse(instance.containsKey(4));
 }

 @Test
 public void testConstructorWithDatavectorAndDefaultValue() {
 double[] datavector = new double[] { 1.0, 2.0, 3.0 };
 SparseInstance instance = new SparseInstance(datavector, 0.0);
 assertEquals(3, instance.noAttributes());
 assertEquals(1.0, instance.value(0), DELTA);
 assertEquals(2.0, instance.value(1), DELTA);
 assertEquals(3.0, instance.value(2), DELTA);
 assertNull(instance.getClassValue());
 assertFalse(instance.isEmpty());
 assertNotNull(instance.data);
 assertFalse(instance.containsKey(4));
 }

 @Test
 public void testConstructorWithDatavectorAndClassValue() {
 double[] datavector = new double[] { 1.0, 2.0, 3.0 };
 SparseInstance instance = new SparseInstance(datavector, "ClassValue");
 assertEquals(3, instance.noAttributes());
 assertEquals(1.0, instance.value(0), DELTA);
 assertEquals(2.0, instance.value(1), DELTA);
 assertEquals(3.0, instance.value(2), DELTA);
 assertEquals("ClassValue", instance.getClassValue());
 assertFalse(instance.isEmpty());
 assertNotNull(instance.data);
 assertFalse(instance.containsKey(4));
 }

 @Test
 public void testConstructorWithDatavectorAndDefaultValueAndClassValue() {
 double[] datavector = new double[] { 1.0, 2.0, 3.0 };
 SparseInstance instance = new SparseInstance(datavector, 0.0, "ClassValue");
 assertEquals(3, instance.noAttributes());
 assertEquals(1.0, instance.value(0), DELTA);
 assertEquals(2.0, instance.value(1), DELTA);
 assertEquals(3.0, instance.value(2), DELTA);
 assertEquals("ClassValue", instance.getClassValue());
 assertFalse(instance.isEmpty());
 assertNotNull(instance.data);
 assertFalse(instance.containsKey(4));
 }

 @Test
 public void testSetNoAttributes() {
 SparseInstance instance = new SparseInstance();
 instance.setNoAttributes(5);
 assertEquals(5, instance.noAttributes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNoAttributesNegativeValue() {
 SparseInstance instance = new SparseInstance();
 instance.setNoAttributes(-1);
 }

 @Test
 public void testDefaultValue() {
 SparseInstance instance = new SparseInstance(5, 0.5);
 assertEquals(5, instance.noAttributes());
 assertEquals(0.5, instance.defaultValue, DELTA);
 }

 @Test
 public void testIsEmpty() {
 SparseInstance instance = new SparseInstance();
 assertTrue(instance.isEmpty());

 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertFalse(instance.isEmpty());
 }

 @Test
 public void testContainsKey() {
 SparseInstance instance = new SparseInstance(5);
 assertFalse(instance.containsKey(3));
 instance.put(Integer.valueOf(3), Double.valueOf(2.0));
 assertTrue(instance.containsKey(3));
 }

 @Test
 public void testContainsValue() {
 SparseInstance instance = new SparseInstance(5);
 assertFalse(instance.containsValue(2.0));
 instance.put(Integer.valueOf(0), Double.valueOf(2.0));
 assertTrue(instance.containsValue(2.0));
 }

 @Test
 public void testEntrySet() {
 SparseInstance instance = new SparseInstance(5);
 assertEquals(0, instance.data.size());
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 assertEquals(2, entrySet.size());
 }

 @Test
 public void testGet() {
 SparseInstance instance = new SparseInstance(5);
 assertEquals(0.0, instance.get(0), DELTA);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(1.0, instance.get(0), DELTA);
 }

 @Test
 public void testGetWithInvalidKey() {
 SparseInstance instance = new SparseInstance(5);
 assertEquals(0.0, instance.get(0), DELTA);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(1.0, instance.get(0), DELTA);
 assertEquals(0.0, instance.get(6), DELTA);
 }

 @Test
 public void testClear() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.clear();
 assertEquals(0, instance.data.size());
 }

 @Test
 public void testKeySet() {
 SparseInstance instance = new SparseInstance();
 Set<Integer> keySet = instance.keySet();
 assertTrue(keySet.isEmpty());
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 Set<Integer> keySet2 = instance.keySet();
 assertEquals(2, keySet2.size());
 }

 @Test
 public void testPut() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(1.0, instance.value(0), DELTA);
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(null, Double.valueOf(1.0));
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(Integer.valueOf(0), null);
 }

 @Test
 public void testPutAll() {
 SparseInstance instance = new SparseInstance(5);
 SparseInstance instance2 = new SparseInstance(5);
 instance2.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance2.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.putAll(instance2.data);
 assertEquals(2, instance.data.size());
 assertEquals(1.0, instance.value(0), DELTA);
 assertEquals(2.0, instance.value(1), DELTA);
 }

 @Test(expected = NullPointerException.class)
 public void testPutAllWithNullMap() {
 SparseInstance instance = new SparseInstance(5);
 instance.putAll(null);
 }

 @Test
 public void testRemove() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.remove(Integer.valueOf(0));
 assertEquals(1, instance.data.size());
 assertEquals(2.0, instance.value(0), DELTA);
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.remove(null);
 }

 @Test(expected = NoSuchElementException.class)
 public void testRemoveWithMissingKey() {
 SparseInstance instance = new SparseInstance(5);
 assertEquals(0, instance.data.size());
 }

 @Test
 public void testSize() {
 SparseInstance instance = new SparseInstance(5);
 assertEquals(0, instance.size());
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(1, instance.size());
 }

 @Test(expected = DeprecatedWarning.class)
 public void testSizeDeprecatedWarning() {
 SparseInstance instance = new SparseInstance(5);
 assertEquals(0, instance.size());
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(1, instance.size());
 }

 @Test
 public void testValues() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 Collection<Double> values = instance.values();
 assertEquals(2, values.size());
 assertEquals(1.0, values.iterator().next(), DELTA);
 assertEquals(2.0, values.iterator().next(), DELTA);
 }

 @Test
 public void testNoAttributes() {
 SparseInstance instance = new SparseInstance();
 assertEquals(-1, instance.noAttributes());
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(1, instance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.removeAttribute(1);
 assertEquals(1, instance.data.size());
 assertEquals(1.0, instance.value(0), DELTA);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeWithMissingIndex() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.removeAttribute(2);
 }

 @Test
 public void testToString() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 String toString = instance.toString();
 assertTrue(toString.contains("{"));
 assertTrue(toString.contains(";"));
 assertTrue(toString.contains("ClassValue"));
 }

 @Test
 public void testHashCode() {
 SparseInstance instance1 = new SparseInstance(5);
 instance1.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance1.put(Integer.valueOf(1), Double.valueOf(2.0));
 SparseInstance instance2 = new SparseInstance(5);
 instance2.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance2.put(Integer.valueOf(1), Double.valueOf(2.0));
 int hashCode = instance1.hashCode();
 int hashCode2 = instance2.hashCode();
 assertEquals(hashCode, hashCode2);
 }

 @Test
 public void testEquals() {
 SparseInstance instance1 = new SparseInstance(5);
 instance1.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance1.put(Integer.valueOf(1), Double.valueOf(2.0));
 SparseInstance instance2 = new SparseInstance(5);
 instance2.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance2.put(Integer.valueOf(1), Double.valueOf(2.0));
 assertTrue(instance1.equals(instance2));
 }

 @Test
 public void testEqualsFalse() {
 SparseInstance instance1 = new SparseInstance(5);
 instance1.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance1.put(Integer.valueOf(1), Double.valueOf(2.0));
 SparseInstance instance2 = new SparseInstance(5);
 instance2.put(Integer.valueOf(0), Double.valueOf(1.1));
 instance2.put(Integer.valueOf(1), Double.valueOf(2.0));
 assertFalse(instance1.equals(instance2));
 }

 @Test
 public void testCopy() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 SparseInstance copy = instance.copy();
 assertEquals(instance.noAttributes(), copy.noAttributes());
 assertEquals(instance.data.size(), copy.data.size());
 assertEquals(instance.value(0), copy.value(0), DELTA);
 assertEquals(instance.value(1), copy.value(1), DELTA);
 }

 @Test
 public void testRemoveAttributes() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.put(Integer.valueOf(2), Double.valueOf(3.0));
 Set<Integer> indices = new HashSet<Integer>();
 indices.add(1);
 indices.add(2);
 instance.removeAttributes(indices);
 assertEquals(1, instance.data.size());
 assertEquals(1.0, instance.value(0), DELTA);
 }

 @Test
 public void testRemoveAttributesEmptySet() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 Set<Integer> indices = new HashSet<Integer>();
 instance.removeAttributes(indices);
 assertEquals(2, instance.data.size());
 }
}