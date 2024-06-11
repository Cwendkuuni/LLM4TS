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
 sparseInstance = new SparseInstance(1, 0.0);
 assertEquals(1, sparseInstance.noAttributes());
 }

 @Test
 public void testSparseInstanceWithThreeArguments() {
 sparseInstance = new SparseInstance(1, 0.0, "ClassValue");
 assertEquals("ClassValue", sparseInstance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithDoubleArray() {
 double[] datavector = new double[3];
 sparseInstance = new SparseInstance(datavector);
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testSetNoAttributes() {
 sparseInstance = new SparseInstance(3);
 sparseInstance.setNoAttributes(5);
 assertEquals(5, sparseInstance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNoAttributesWithNegativeValue() {
 sparseInstance = new SparseInstance(3);
 sparseInstance.setNoAttributes(-5);
 }

 @Test
 public void testSparseInstanceWithDefaultValue() {
 sparseInstance = new SparseInstance(3, 1.0);
 assertEquals(1.0, sparseInstance.getDefaultValue(), 0.001);
 }

 @Test
 public void testSparseInstanceWithClassValue() {
 sparseInstance = new SparseInstance(3, 1.0, "ClassValue");
 assertEquals("ClassValue", sparseInstance.getClassValue());
 }

 @Test
 public void testInitiate() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0};
 sparseInstance = new SparseInstance(datavector, 0.0);
 assertEquals(4, sparseInstance.noAttributes());
 for (int i = 0; i < datavector.length; ++i) {
 if (datavector[i] != 0.0) {
 assertEquals(datavector[i], sparseInstance.get(i), 0.001);
 }
 }
 }

 @Test
 public void testGet() {
 sparseInstance = new SparseInstance(3, 1.0);
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 assertEquals(2.0, sparseInstance.get(0), 0.001);
 }

 @Test
 public void testGetWithDefaultValue() {
 sparseInstance = new SparseInstance(3, 1.0);
 assertEquals(1.0, sparseInstance.get(3), 0.001);
 }

 @Test
 public void testClear() {
 sparseInstance = new SparseInstance(3, 1.0);
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 sparseInstance.clear();
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testContainsKey() {
 sparseInstance = new SparseInstance(3, 1.0);
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 assertTrue(sparseInstance.containsKey(Integer.valueOf(0)));
 }

 @Test
 public void testContainsKeyWithDefaultValue() {
 sparseInstance = new SparseInstance(3, 1.0);
 assertFalse(sparseInstance.containsKey(Integer.valueOf(3)));
 }

 @Test
 public void testContainsValue() {
 sparseInstance = new SparseInstance(3, 1.0);
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 assertTrue(sparseInstance.containsValue(Double.valueOf(2.0)));
 }

 @Test
 public void testContainsValueWithDefaultValue() {
 sparseInstance = new SparseInstance(3, 1.0);
 assertTrue(sparseInstance.containsValue(Double.valueOf(1.0)));
 }

 @Test
 public void testEntrySet() {
 sparseInstance = new SparseInstance(3, 1.0);
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 Set<Map.Entry<Integer, Double>> entrySet = sparseInstance.entrySet();
 assertTrue(entrySet.size() == 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEntrySetThrowsException() {
 sparseInstance = new SparseInstance(3, 1.0);
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 Set<Map.Entry<Integer, Double>> entrySet = sparseInstance.entrySet();
 for (Map.Entry<Integer, Double> entry : entrySet) {
 entry.setValue(Double.valueOf(3.0));
 }
 }

 @Test
 public void testPut() {
 sparseInstance = new SparseInstance(3, 1.0);
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 assertEquals(2.0, sparseInstance.get(0), 0.001);
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
 sparseInstance = new SparseInstance(3, 1.0);
 try {
 sparseInstance.put(null, Double.valueOf(2.0));
 } catch (NullPointerException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithDuplicateKey() {
 sparseInstance = new SparseInstance(3, 1.0);
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 try {
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(3.0));
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testPutAll() {
 Map<Integer, Double> map = new HashMap<>();
 map.put(Integer.valueOf(0), Double.valueOf(2.0));
 map.put(Integer.valueOf(1), Double.valueOf(3.0));
 sparseInstance = new SparseInstance(3, 1.0);
 sparseInstance.putAll(map);
 assertEquals(2, sparseInstance.noAttributes());
 for (int i = 0; i < 2; ++i) {
 assertEquals(i == 0 ? 2.0 : 3.0, sparseInstance.get(i), 0.001);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testPutAllWithNullMap() {
 sparseInstance = new SparseInstance(3, 1.0);
 try {
 sparseInstance.putAll(null);
 } catch (NullPointerException e) {
 // Expected exception
 }
 }

 @Test
 public void testRemove() {
 sparseInstance = new SparseInstance(3, 1.0);
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(3.0));
 Double removedValue = sparseInstance.remove(Integer.valueOf(0));
 assertEquals(2.0, removedValue, 0.001);
 }

 @Test
 public void testRemoveWithDefaultValue() {
 sparseInstance = new SparseInstance(3, 1.0);
 assertEquals(1.0, sparseInstance.remove(Integer.valueOf(3)), 0.001);
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
 sparseInstance = new SparseInstance(3, 1.0);
 try {
 sparseInstance.remove(null);
 } catch (NullPointerException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveWithNonExistentKey() {
 sparseInstance = new SparseInstance(3, 1.0);
 try {
 sparseInstance.remove(Integer.valueOf(4));
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testSize() {
 sparseInstance = new SparseInstance(3, 1.0);
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(3.0));
 assertEquals(2, sparseInstance.size());
 }

 @Test(expected = DeprecatedException.class)
 public void testSizeDeprecated() {
 sparseInstance = new SparseInstance(3, 1.0);
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(3.0));
 assertEquals(2, sparseInstance.size());
 }

 @Test
 public void testValues() {
 sparseInstance = new SparseInstance(3, 1.0);
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(3.0));
 Collection<Double> values = sparseInstance.values();
 assertEquals(2, values.size());
 }

 @Test
 public void testNoAttributes() {
 sparseInstance = new SparseInstance(3, 1.0);
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testNoAttributesWithEmptyInstance() {
 sparseInstance = new SparseInstance();
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testNoAttributesWithDataMapSizeZero() {
 sparseInstance = new SparseInstance(3, 1.0);
 assertEquals(3, sparseInstance.noAttributes());
 sparseInstance.data.clear();
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 sparseInstance = new SparseInstance(3, 1.0);
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(3.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(4.0));
 sparseInstance.removeAttribute(1);
 assertEquals(2, sparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttributeWithNonExistentIndex() {
 sparseInstance = new SparseInstance(3, 1.0);
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(3.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(4.0));
 sparseInstance.removeAttribute(4);
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testToString() {
 sparseInstance = new SparseInstance(3, 1.0);
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(3.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(4.0));
 String result = sparseInstance.toString();
 String expected = "{" + "{0=2.0, 1=3.0, 2=4.0};" + sparseInstance.getClassValue() + "}";
 assertEquals(expected, result);
 }

 @Test
 public void testHashCode() {
 sparseInstance = new SparseInstance(3, 1.0);
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(3.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(4.0));
 int hashCode = sparseInstance.hashCode();
 int expectedHashCode = 1 * 31 + (sparseInstance.data.hashCode() ^ ((long) sparseInstance.defaultValue >>> 32));
 assertEquals(expectedHashCode, hashCode);
 }

 @Test
 public void testEquals() {
 SparseInstance sparseInstance1 = new SparseInstance(3, 1.0);
 sparseInstance1.put(Integer.valueOf(0), Double.valueOf(2.0));
 sparseInstance1.put(Integer.valueOf(1), Double.valueOf(3.0));
 sparseInstance1.put(Integer.valueOf(2), Double.valueOf(4.0));
 SparseInstance sparseInstance2 = new SparseInstance(3, 1.0);
 SparseInstance sparseInstance3 = new SparseInstance(3, 1.0);
 assertTrue(sparseInstance1.equals(sparseInstance1));
 assertFalse(sparseInstance1.equals(sparseInstance2));
 assertTrue(sparseInstance1.equals(sparseInstance3));
 }

 @Test
 public void testCopy() {
 SparseInstance originalSparseInstance = new SparseInstance(3, 1.0);
 originalSparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 originalSparseInstance.put(Integer.valueOf(1), Double.valueOf(3.0));
 originalSparseInstance.put(Integer.valueOf(2), Double.valueOf(4.0));
 SparseInstance copiedSparseInstance = originalSparseInstance.copy();
 assertTrue(originalSparseInstance.equals(copiedSparseInstance));
 }

 @Test
 public void testRemoveAttributes() {
 SparseInstance sparseInstance = new SparseInstance(3, 1.0);
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(3.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(4.0));
 Set<Integer> indices = new TreeSet<>();
 indices.add(1);
 indices.add(2);
 sparseInstance.removeAttributes(indices);
 assertEquals(1, sparseInstance.noAttributes());
 }

}