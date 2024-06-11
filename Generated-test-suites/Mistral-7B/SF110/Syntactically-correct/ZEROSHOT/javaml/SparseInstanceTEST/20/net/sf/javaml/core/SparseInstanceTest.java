package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class SparseInstanceTest {

 private SparseInstance sparseInstance;
 private SparseInstance sparseInstanceWithDefaultValue;
 private SparseInstance sparseInstanceWithClassValue;
 private SparseInstance sparseInstanceWithDatavector;

 @Test
 public void testSparseInstanceWithDefaultConstructor() {
 sparseInstance = new SparseInstance();
 assertEquals(-1, sparseInstance.noAttributes());
 assertNull(sparseInstance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithNoAttributesConstructor() {
 sparseInstance = new SparseInstance(0);
 assertEquals(0, sparseInstance.noAttributes());
 assertNull(sparseInstance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithNoAttributesAndDefaultValueConstructor() {
 sparseInstance = new SparseInstance(0, 0.0);
 assertEquals(0, sparseInstance.noAttributes());
 assertNull(sparseInstance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithNoAttributesAndClassValueConstructor() {
 sparseInstanceWithClassValue = new SparseInstance(0, "TestClass");
 assertEquals(0, sparseInstanceWithClassValue.noAttributes());
 assertEquals("TestClass", sparseInstanceWithClassValue.getClassValue());
 }

 @Test
 public void testSparseInstanceWithNoAttributesAndDefaultValueAndClassValueConstructor() {
 sparseInstanceWithDefaultValue = new SparseInstance(0, 0.0, "TestClass");
 assertEquals(0, sparseInstanceWithDefaultValue.noAttributes());
 assertEquals("TestClass", sparseInstanceWithDefaultValue.getClassValue());
 }

 @Test
 public void testSparseInstanceWithDatavectorConstructor() {
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstanceWithDatavector = new SparseInstance(datavector);
 assertEquals(3, sparseInstanceWithDatavector.noAttributes());
 assertArrayEquals(datavector, toArray(sparseInstanceWithDatavector.toArray(new Double[0])));
 }

 @Test
 public void testSparseInstanceWithDatavectorAndDefaultValueConstructor() {
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstanceWithDatavector = new SparseInstance(datavector, 0.0);
 assertEquals(3, sparseInstanceWithDatavector.noAttributes());
 assertArrayEquals(datavector, toArray(sparseInstanceWithDatavector.toArray(new Double[0])));
 }

 @Test
 public void testSparseInstanceWithDatavectorAndClassValueConstructor() {
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstanceWithClassValue = new SparseInstance(datavector, "TestClass");
 assertEquals(3, sparseInstanceWithClassValue.noAttributes());
 assertArrayEquals(datavector, toArray(sparseInstanceWithClassValue.toArray(new Double[0])));
 assertEquals("TestClass", sparseInstanceWithClassValue.getClassValue());
 }

 @Test
 public void testSparseInstanceWithDatavectorAndDefaultValueAndClassValueConstructor() {
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstanceWithDefaultValue = new SparseInstance(datavector, 0.0, "TestClass");
 assertEquals(3, sparseInstanceWithDefaultValue.noAttributes());
 assertArrayEquals(datavector, toArray(sparseInstanceWithDefaultValue.toArray(new Double[0])));
 assertEquals("TestClass", sparseInstanceWithDefaultValue.getClassValue());
 }

 @Test
 public void testSetNoAttributes() {
 sparseInstance = new SparseInstance();
 sparseInstance.setNoAttributes(5);
 assertEquals(5, sparseInstance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNoAttributesNegativeValue() {
 sparseInstance = new SparseInstance();
 sparseInstance.setNoAttributes(-5);
 }

 @Test
 public void testValue() {
 sparseInstanceWithDatavector = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(1.0, sparseInstanceWithDatavector.value(0), 0.001);
 }

 @Test
 public void testClear() {
 sparseInstanceWithDatavector = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 sparseInstanceWithDatavector.clear();
 assertEquals(0, sparseInstanceWithDatavector.noAttributes());
 assertFalse(sparseInstanceWithDatavector.containsKey(0));
 assertFalse(sparseInstanceWithDatavector.containsKey(1));
 assertFalse(sparseInstanceWithDatavector.containsKey(2));
 }

 @Test
 public void testContainsKey() {
 sparseInstanceWithDatavector = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 assertTrue(sparseInstanceWithDatavector.containsKey(0));
 assertTrue(sparseInstanceWithDatavector.containsKey(1));
 assertTrue(sparseInstanceWithDatavector.containsKey(2));
 }

 @Test
 public void testContainsValue() {
 sparseInstanceWithDatavector = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 assertTrue(sparseInstanceWithDatavector.containsValue(1.0));
 assertTrue(sparseInstanceWithDatavector.containsValue(2.0));
 assertTrue(sparseInstanceWithDatavector.containsValue(3.0));
 }

 @Test
 public void testEntrySet() {
 sparseInstanceWithDatavector = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 Set<Map.Entry<Integer, Double>> entrySet = sparseInstanceWithDatavector.entrySet();
 assertEquals(3, entrySet.size());
 }

 @Test
 public void testGet() {
 sparseInstanceWithDatavector = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(1.0, sparseInstanceWithDatavector.get(0), 0.001);
 assertEquals(2.0, sparseInstanceWithDatavector.get(1), 0.001);
 assertEquals(3.0, sparseInstanceWithDatavector.get(2), 0.001);
 assertEquals(0.0, sparseInstanceWithDatavector.get(-1), 0.001);
 }

 @Test
 public void testIsEmpty() {
 sparseInstanceWithDatavector = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 assertFalse(sparseInstanceWithDatavector.isEmpty());
 sparseInstanceWithDatavector.clear();
 assertTrue(sparseInstanceWithDatavector.isEmpty());
 }

 @Test
 public void testKeySet() {
 sparseInstanceWithDatavector = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 Set<Integer> keySet = sparseInstanceWithDatavector.keySet();
 assertEquals(3, keySet.size());
 }

 @Test
 public void testPut() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(1, sparseInstance.noAttributes());
 assertEquals(1.0, sparseInstance.get(0), 0.001);
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullKey() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(null, Double.valueOf(1.0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutNegativeKey() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(-1), Double.valueOf(1.0));
 }

 @Test
 public void testPutAll() {
 Map<Integer, Double> map = new HashMap<>();
 map.put(Integer.valueOf(0), Double.valueOf(1.0));
 map.put(Integer.valueOf(1), Double.valueOf(2.0));
 map.put(Integer.valueOf(2), Double.valueOf(3.0));
 sparseInstance = new SparseInstance();
 sparseInstance.putAll(map);
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test(expected = NullPointerException.class)
 public void testPutAllNullMap() {
 sparseInstance = new SparseInstance();
 sparseInstance.putAll(null);
 }

 @Test
 public void testRemove() {
 sparseInstanceWithDatavector = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 sparseInstanceWithDatavector.put(Integer.valueOf(1), Double.valueOf(4.0));
 assertEquals(4, sparseInstanceWithDatavector.noAttributes());
 Double value = sparseInstanceWithDatavector.remove(Integer.valueOf(1));
 assertEquals(3, sparseInstanceWithDatavector.noAttributes());
 assertEquals(4.0, value.doubleValue(), 0.001);
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
 sparseInstanceWithDatavector = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 sparseInstanceWithDatavector.remove(null);
 }

 @Test
 public void testSize() {
 sparseInstanceWithDatavector = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 sparseInstanceWithDatavector.put(Integer.valueOf(4), Double.valueOf(4.0));
 assertEquals(4, sparseInstanceWithDatavector.noAttributes());
 }

 @Test
 @Deprecated
 public void testSizeDeprecated() {
 sparseInstanceWithDatavector = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 sparseInstanceWithDatavector.put(Integer.valueOf(4), Double.valueOf(4.0));
 assertEquals(4, sparseInstanceWithDatavector.size());
 }

 @Test
 public void testValues() {
 sparseInstanceWithDatavector = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 List<Double> values = new ArrayList<>(sparseInstanceWithDatavector.values());
 assertEquals(3, values.size());
 }

 @Test
 public void testNoAttributes() {
 sparseInstanceWithDatavector = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(3, sparseInstanceWithDatavector.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 sparseInstanceWithDatavector = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 sparseInstanceWithDatavector.removeAttribute(0);
 assertEquals(2, sparseInstanceWithDatavector.noAttributes());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeNegativeIndex() {
 sparseInstanceWithDatavector = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 sparseInstanceWithDatavector.removeAttribute(-1);
 }

 @Test
 public void testRemoveAttributes() {
 sparseInstanceWithDatavector = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 Set<Integer> indices = new HashSet<>();
 indices.add(1);
 indices.add(3);
 sparseInstanceWithDatavector.removeAttributes(indices);
 assertEquals(3, sparseInstanceWithDatavector.noAttributes());
 }

 @Test
 public void testToString() {
 sparseInstanceWithDatavector = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals("{1.0=1.0, 2.0=2.0, 3.0=3.0}", sparseInstanceWithDatavector.toString());
 }

 @Test
 public void testHashCode() {
 sparseInstanceWithDatavector = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 SparseInstance sparseInstanceWithSameData = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(sparseInstanceWithDatavector.hashCode(), sparseInstanceWithSameData.hashCode());
 }

 @Test
 public void testEquals() {
 sparseInstanceWithDatavector = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 SparseInstance sparseInstanceWithSameData = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 assertTrue(sparseInstanceWithDatavector.equals(sparseInstanceWithSameData));
 }

 @Test
 public void testNotEquals() {
 sparseInstanceWithDatavector = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 SparseInstance sparseInstanceWithDifferentData = new SparseInstance(new double[] {1.0, 2.0, 4.0});
 assertFalse(sparseInstanceWithDatavector.equals(sparseInstanceWithDifferentData));
 }

 @Test
 public void testCopy() {
 sparseInstanceWithDatavector = new SparseInstance(new double[] {1.0, 2.0, 3.0});
 SparseInstance copiedInstance = sparseInstanceWithDatavector.copy();
 assertEquals(sparseInstanceWithDatavector, copiedInstance);
 }

 private Double[] toArray(Double[] array) {
 if (array == null) {
 return new Double[0];
 }
 return array;
 }
}