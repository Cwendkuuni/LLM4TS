package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class SparseInstanceTest {

 private SparseInstance sparseInstance;

 @Test
 public void testConstructorWithNoArguments() {
 sparseInstance = new SparseInstance();
 assertEquals(-1, sparseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithNumberOfAttributes() {
 sparseInstance = new SparseInstance(5);
 assertEquals(5, sparseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithNumberOfAttributesAndDefaultValue() {
 sparseInstance = new SparseInstance(5, 0.0);
 assertEquals(5, sparseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithNumberOfAttributesAndClassValue() {
 sparseInstance = new SparseInstance(5, "StringClassValue");
 assertEquals("StringClassValue", sparseInstance.getClassValue());
 }

 @Test
 public void testConstructorWithDoubleArray() {
 double[] datavector = new double[3];
 datavector[0] = 1.0;
 datavector[1] = 2.0;
 datavector[2] = 3.0;
 sparseInstance = new SparseInstance(datavector);
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithDoubleArrayAndDefaultValue() {
 double[] datavector = new double[3];
 datavector[0] = 1.0;
 datavector[1] = 2.0;
 datavector[2] = 3.0;
 sparseInstance = new SparseInstance(datavector, 0.0);
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithDoubleArrayAndClassValue() {
 double[] datavector = new double[3];
 datavector[0] = 1.0;
 datavector[1] = 2.0;
 datavector[2] = 3.0;
 sparseInstance = new SparseInstance(datavector, "StringClassValue");
 assertEquals("StringClassValue", sparseInstance.getClassValue());
 }

 @Test
 public void testConstructorWithDoubleArrayAndDefaultValueAndClassValue() {
 double[] datavector = new double[3];
 datavector[0] = 1.0;
 datavector[1] = 2.0;
 datavector[2] = 3.0;
 sparseInstance = new SparseInstance(datavector, 0.0, "StringClassValue");
 assertEquals("StringClassValue", sparseInstance.getClassValue());
 }

 @Test
 public void testSetNoAttributes() {
 sparseInstance = new SparseInstance(5);
 sparseInstance.setNoAttributes(10);
 assertEquals(10, sparseInstance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNoAttributesWithNegativeNumber() {
 sparseInstance = new SparseInstance(5);
 sparseInstance.setNoAttributes(-1);
 }

 @Test
 public void testGetValue() {
 sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 assertEquals(1.0, sparseInstance.getValue(1), 0.001);
 }

 @Test
 public void testGetValueWithInvalidIndex() {
 sparseInstance = new SparseInstance(5);
 assertEquals(0.0, sparseInstance.getValue(-1), 0.001);
 }

 @Test
 public void testClear() {
 sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 sparseInstance.clear();
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testContainsKey() {
 sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 assertTrue(sparseInstance.containsKey(1));
 }

 @Test
 public void testContainsKeyWithInvalidIndex() {
 sparseInstance = new SparseInstance(5);
 assertFalse(sparseInstance.containsKey(-1));
 }

 @Test
 public void testContainsValue() {
 sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 assertTrue(sparseInstance.containsValue(1.0));
 }

 @Test
 public void testContainsValueWithInvalidValue() {
 sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 assertFalse(sparseInstance.containsValue(2.0));
 }

 @Test
 public void testEntrySet() {
 sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 Set<Map.Entry<Integer, Double>> entrySet = sparseInstance.entrySet();
 assertTrue(entrySet.isEmpty());
 }

 @Test
 public void testGet() {
 sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 assertEquals(1.0, sparseInstance.get(1), 0.001);
 }

 @Test
 public void testGetWithInvalidIndex() {
 sparseInstance = new SparseInstance(5);
 assertEquals(0.0, sparseInstance.get(-1), 0.001);
 }

 @Test
 public void testIsEmpty() {
 sparseInstance = new SparseInstance(5);
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testIsEmptyAfterPuttingValue() {
 sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 assertFalse(sparseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 sparseInstance = new SparseInstance(5);
 TreeSet<Integer> keySet = sparseInstance.keySet();
 assertTrue(keySet.isEmpty());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testPutWithDuplicateKey() {
 sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(1, 2.0);
 }

 @Test
 public void testPut() {
 sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 assertEquals(1.0, sparseInstance.getValue(1), 0.001);
 }

 @Test
 public void testPutWithNullKey() {
 sparseInstance = new SparseInstance(5);
 try {
 sparseInstance.put(null, 1.0);
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testPutAll() {
 Map<Integer, Double> map = new HashMap<>();
 map.put(1, 1.0);
 map.put(2, 2.0);
 sparseInstance = new SparseInstance(5);
 sparseInstance.putAll(map);
 assertEquals(2, sparseInstance.noAttributes());
 assertEquals(1.0, sparseInstance.getValue(1), 0.001);
 assertEquals(2.0, sparseInstance.getValue(2), 0.001);
 }

 @Test
 public void testRemove() {
 sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(2, 2.0);
 Double removedValue = sparseInstance.remove(1);
 assertEquals(1.0, removedValue, 0.001);
 assertEquals(1, sparseInstance.noAttributes());
 }

 @Test
 public void testRemoveWithInvalidIndex() {
 sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 assertNull(sparseInstance.remove(-1));
 }

 @Test
 public void testSize() {
 sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(2, 2.0);
 assertEquals(2, sparseInstance.noAttributes());
 }

 @Test(expected = DeprecatedWarning.class)
 public void testSizeDeprecated() {
 sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(2, 2.0);
 assertEquals(2, sparseInstance.size());
 }

 @Test
 public void testValues() {
 sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(2, 2.0);
 Collection<Double> values = sparseInstance.values();
 assertEquals(2, values.size());
 assertTrue(values.contains(1.0));
 assertTrue(values.contains(2.0));
 }

 @Test
 public void testNoAttributes() {
 sparseInstance = new SparseInstance(5);
 assertEquals(-1, sparseInstance.noAttributes());
 }

 @Test
 public void testNoAttributesAfterInitializationWithDataVector() {
 sparseInstance = new SparseInstance(new double[5]);
 assertEquals(5, sparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(2, 2.0);
 sparseInstance.removeAttribute(1);
 assertEquals(1, sparseInstance.noAttributes());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeWithInvalidIndex() {
 sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(2, 2.0);
 sparseInstance.removeAttribute(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveAttributeWithInvalidIndexWithData() {
 sparseInstance = new SparseInstance(new double[5]);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(2, 2.0);
 sparseInstance.removeAttribute(-1);
 }

 @Test
 public void testToString() {
 sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(2, 2.0);
 String toString = sparseInstance.toString();
 assertTrue(toString.contains("{"));
 assertTrue(toString.contains("}"));
 }

 @Test
 public void testHashCode() {
 sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(2, 2.0);
 int hashCode = sparseInstance.hashCode();
 }

 @Test
 public void testEquals() {
 sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(2, 2.0);
 SparseInstance anotherSparseInstance = new SparseInstance(5);
 anotherSparseInstance.put(1, 1.0);
 anotherSparseInstance.put(2, 2.0);
 assertTrue(sparseInstance.equals(anotherSparseInstance));
 }

 @Test
 public void testEqualsWithDifferentClass() {
 SparseInstance sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(2, 2.0);
 String anotherSparseInstance = new String("TestString");
 assertFalse(sparseInstance.equals(anotherSparseInstance));
 }

 @Test
 public void testEqualsWithDifferentData() {
 SparseInstance sparseInstance = new SparseInstance(5);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(2, 2.0);
 SparseInstance anotherSparseInstance = new SparseInstance(5);
 anotherSparseInstance.put(1, 2.0);
 anotherSparseInstance.put(2, 3.0);
 assertFalse(sparseInstance.equals(anotherSparseInstance));
 }

 @Test
 public void testCopy() {
 SparseInstance originalSparseInstance = new SparseInstance(new double[5]);
 originalSparseInstance.put(1, 1.0);
 originalSparseInstance.put(2, 2.0);
 SparseInstance copySparseInstance = originalSparseInstance.copy();
 assertEquals(originalSparseInstance.toString(), copySparseInstance.toString());
 }

 @Test
 public void testRemoveAttributes() {
 SparseInstance sparseInstance = new SparseInstance(new double[5]);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(2, 2.0);
 Set<Integer> indices = new HashSet<>();
 indices.add(1);
 indices.add(2);
 sparseInstance.removeAttributes(indices);
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveAttributesWithNullIndices() {
 SparseInstance sparseInstance = new SparseInstance(new double[5]);
 sparseInstance.put(1, 1.0);
 sparseInstance.put(2, 2.0);
 Set<Integer> indices = null;
 sparseInstance.removeAttributes(indices);
 }
}