package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class SparseInstanceTest {

 private SparseInstance sparseInstance;
 private SparseInstance sparseInstanceWithDefaultValue;
 private SparseInstance sparseInstanceWithClassValue;
 private SparseInstance sparseInstanceWithDataVector;
 private SparseInstance sparseInstanceWithEmptyData;

 @Test
 public void testSparseInstanceWithDefaultConstructor() {
 sparseInstance = new SparseInstance();
 assertEquals(-1, sparseInstance.noAttributes());
 }

 @Test
 public void testSparseInstanceWithNoAttributesConstructor() {
 sparseInstance = new SparseInstance(0);
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testSparseInstanceWithNoAttributesAndDefaultValueConstructor() {
 sparseInstance = new SparseInstance(0, 0.0);
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testSparseInstanceWithNoAttributesAndClassValueConstructor() {
 sparseInstance = new SparseInstance(0, "ClassValue");
 assertEquals("ClassValue", sparseInstance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithNoAttributesAndDefaultValueAndClassValueConstructor() {
 sparseInstance = new SparseInstance(0, 0.0, "ClassValue");
 assertEquals("ClassValue", sparseInstance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithDataVectorConstructor() {
 double[] datavector = {0.1, 0.2, 0.3};
 sparseInstanceWithDataVector = new SparseInstance(datavector);
 assertEquals(3, sparseInstanceWithDataVector.noAttributes());
 }

 @Test
 public void testSparseInstanceWithDataVectorAndDefaultValueConstructor() {
 double[] datavector = {0.1, 0.2, 0.3};
 sparseInstanceWithDefaultValue = new SparseInstance(datavector, 0.0);
 assertEquals(3, sparseInstanceWithDefaultValue.noAttributes());
 }

 @Test
 public void testSparseInstanceWithDataVectorAndClassValueConstructor() {
 double[] datavector = {0.1, 0.2, 0.3};
 sparseInstanceWithClassValue = new SparseInstance(datavector, "ClassValue");
 assertEquals("ClassValue", sparseInstanceWithClassValue.getClassValue());
 }

 @Test
 public void testSparseInstanceWithDataVectorAndDefaultValueAndClassValueConstructor() {
 double[] datavector = {0.1, 0.2, 0.3};
 sparseInstanceWithDefaultValue = new SparseInstance(datavector, 0.0, "ClassValue");
 assertEquals("ClassValue", sparseInstanceWithDefaultValue.getClassValue());
 }

 @Test
 public void testSparseInstanceWithEmptyDataConstructor() {
 sparseInstanceWithEmptyData = new SparseInstance();
 assertEquals(0, sparseInstanceWithEmptyData.noAttributes());
 }

 @Test
 public void testSetNoAttributes() {
 sparseInstance = new SparseInstance(0);
 sparseInstance.setNoAttributes(5);
 assertEquals(5, sparseInstance.noAttributes());
 }

 @Test
 public void testValue() {
 sparseInstanceWithDataVector = new SparseInstance(new double[]{0.1, 0.2, 0.3});
 assertEquals(0.1, sparseInstanceWithDataVector.value(0), 0.001);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueOutOfBoundsException() {
 sparseInstanceWithDataVector = new SparseInstance(new double[]{0.1, 0.2, 0.3});
 sparseInstanceWithDataVector.value(3);
 }

 @Test
 public void testClear() {
 sparseInstanceWithDataVector = new SparseInstance(new double[]{0.1, 0.2, 0.3});
 sparseInstanceWithDataVector.clear();
 assertEquals(0, sparseInstanceWithDataVector.noAttributes());
 }

 @Test
 public void testContainsKey() {
 sparseInstanceWithDataVector = new SparseInstance(new double[]{0.1, 0.2, 0.3});
 assertTrue(sparseInstanceWithDataVector.containsKey(Integer.valueOf(0)));
 }

 @Test
 public void testContainsValue() {
 sparseInstanceWithDataVector = new SparseInstance(new double[]{0.1, 0.2, 0.3});
 assertTrue(sparseInstanceWithDataVector.containsValue(0.1));
 }

 @Test
 public void testEntrySet() {
 sparseInstanceWithDataVector = new SparseInstance(new double[]{0.1, 0.2, 0.3});
 Set<Map.Entry<Integer, Double>> entrySet = sparseInstanceWithDataVector.entrySet();
 assertEquals(3, entrySet.size());
 }

 @Test
 public void testGet() {
 sparseInstanceWithDataVector = new SparseInstance(new double[]{0.1, 0.2, 0.3});
 assertEquals(0.1, sparseInstanceWithDataVector.get(Integer.valueOf(0)), 0.001);
 }

 @Test
 public void testGetDefaultValue() {
 sparseInstanceWithDefaultValue = new SparseInstance(new double[]{0.1, 0.2, 0.3}, 0.5);
 assertEquals(0.5, sparseInstanceWithDefaultValue.get(Integer.valueOf(3)), 0.001);
 }

 @Test
 public void testIsEmpty() {
 sparseInstanceWithEmptyData = new SparseInstance();
 assertTrue(sparseInstanceWithEmptyData.isEmpty());
 }

 @Test
 public void testKeySet() {
 sparseInstanceWithDataVector = new SparseInstance(new double[]{0.1, 0.2, 0.3});
 Set<Integer> keySet = sparseInstanceWithDataVector.keySet();
 assertEquals(3, keySet.size());
 }

 @Test
 public void testPut() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(0.1));
 assertEquals(1, sparseInstance.noAttributes());
 assertEquals(0.1, sparseInstance.get(Integer.valueOf(0)), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutNullKey() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(null, Double.valueOf(0.1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutNullValue() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), null);
 }

 @Test
 public void testPutAll() {
 Map<Integer, Double> map = new HashMap<>();
 map.put(Integer.valueOf(0), Double.valueOf(0.1));
 map.put(Integer.valueOf(1), Double.valueOf(0.2));
 sparseInstance = new SparseInstance();
 sparseInstance.putAll(map);
 assertEquals(2, sparseInstance.noAttributes());
 assertEquals(0.1, sparseInstance.get(Integer.valueOf(0)), 0.001);
 assertEquals(0.2, sparseInstance.get(Integer.valueOf(1)), 0.001);
 }

 @Test
 public void testRemove() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(0.1));
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(0.2));
 sparseInstance.remove(Integer.valueOf(0));
 assertEquals(1, sparseInstance.noAttributes());
 assertNull(sparseInstance.get(Integer.valueOf(0)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveNullKey() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(0.1));
 sparseInstance.remove(null);
 }

 @Test
 public void testSize() {
 sparseInstanceWithDataVector = new SparseInstance(new double[]{0.1, 0.2, 0.3});
 assertEquals(3, sparseInstanceWithDataVector.size());
 }

 @Test(expected = DeprecatedWarning.class)
 public void testSizeDeprecated() {
 sparseInstanceWithDataVector = new SparseInstance(new double[]{0.1, 0.2, 0.3});
 assertEquals(3, sparseInstanceWithDataVector.size());
 }

 @Test
 public void testValues() {
 sparseInstanceWithDataVector = new SparseInstance(new double[]{0.1, 0.2, 0.3});
 Collection<Double> values = sparseInstanceWithDataVector.values();
 assertEquals(3, values.size());
 }

 @Test
 public void testNoAttributes() {
 sparseInstanceWithDataVector = new SparseInstance(new double[]{0.1, 0.2, 0.3});
 assertEquals(3, sparseInstanceWithDataVector.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 sparseInstanceWithDataVector = new SparseInstance(new double[]{0.1, 0.2, 0.3});
 sparseInstanceWithDataVector.removeAttribute(0);
 assertEquals(2, sparseInstanceWithDataVector.noAttributes());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeOutOfBoundsException() {
 sparseInstanceWithDataVector = new SparseInstance(new double[]{0.1, 0.2, 0.3});
 sparseInstanceWithDataVector.removeAttribute(3);
 }

 @Test
 public void testToString() {
 sparseInstanceWithDataVector = new SparseInstance(new double[]{0.1, 0.2, 0.3});
 assertEquals("{0.1=0.1, 1.0=0.2, 2.0=0.3;ClassValue}", sparseInstanceWithDataVector.toString());
 }

 @Test
 public void testHashCode() {
 sparseInstanceWithDataVector = new SparseInstance(new double[]{0.1, 0.2, 0.3});
 SparseInstance other = new SparseInstance(new double[]{0.1, 0.2, 0.3});
 assertEquals(sparseInstanceWithDataVector.hashCode(), other.hashCode());
 }

 @Test
 public void testEquals() {
 sparseInstanceWithDataVector = new SparseInstance(new double[]{0.1, 0.2, 0.3});
 SparseInstance other = new SparseInstance(new double[]{0.1, 0.2, 0.3});
 assertTrue(sparseInstanceWithDataVector.equals(other));
 }

 @Test
 public void testEqualsFalse() {
 sparseInstanceWithDataVector = new SparseInstance(new double[]{0.1, 0.2, 0.3});
 SparseInstance other = new SparseInstance(new double[]{0.1, 0.2, 0.4});
 assertFalse(sparseInstanceWithDataVector.equals(other));
 }

 @Test
 public void testCopy() {
 sparseInstanceWithDataVector = new SparseInstance(new double[]{0.1, 0.2, 0.3});
 SparseInstance copy = sparseInstanceWithDataVector.copy();
 assertEquals(sparseInstanceWithDataVector, copy);
 }

 @Test
 public void testRemoveAttributes() {
 sparseInstanceWithDataVector = new SparseInstance(new double[]{0.1, 0.2, 0.3});
 Set<Integer> indices = new HashSet<>();
 indices.add(0);
 indices.add(2);
 sparseInstanceWithDataVector.removeAttributes(indices);
 assertEquals(1, sparseInstanceWithDataVector.noAttributes());
 }
}