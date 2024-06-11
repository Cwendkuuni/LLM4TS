package net.sf.javaml.core;

import java.util.*;

import static org.junit.Assert.*;

import org.junit.Test;

public class SparseInstanceTest {

 private SparseInstance sparseInstance;

 @Test
 public void testSparseInstanceWithDefaultConstructor() {
 sparseInstance = new SparseInstance();
 assertEquals(0, sparseInstance.noAttributes());
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithNoAttributes() {
 sparseInstance = new SparseInstance(0);
 assertEquals(0, sparseInstance.noAttributes());
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithNoAttributesAndDefaultValue() {
 sparseInstance = new SparseInstance(0, 0.0);
 assertEquals(0, sparseInstance.noAttributes());
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithNoAttributesAndClassValue() {
 sparseInstance = new SparseInstance(0, "ClassValue");
 assertEquals("ClassValue", sparseInstance.classValue());
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithNoAttributesAndDefaultValueAndClassValue() {
 sparseInstance = new SparseInstance(0, 0.0, "ClassValue");
 assertEquals("ClassValue", sparseInstance.classValue());
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithDatavector() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector);
 assertEquals(3, sparseInstance.noAttributes());
 assertFalse(sparseInstance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithDatavectorAndDefaultValue() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector, 0.0);
 assertEquals(3, sparseInstance.noAttributes());
 assertFalse(sparseInstance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithDatavectorAndClassValue() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector, "ClassValue");
 assertEquals("ClassValue", sparseInstance.classValue());
 assertEquals(3, sparseInstance.noAttributes());
 assertFalse(sparseInstance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithDatavectorAndDefaultValueAndClassValue() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector, 0.0, "ClassValue");
 assertEquals("ClassValue", sparseInstance.classValue());
 assertEquals(3, sparseInstance.noAttributes());
 assertFalse(sparseInstance.isEmpty());
 }

 @Test
 public void testValue() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(1.0, sparseInstance.value(0), 0.001);
 assertEquals(2.0, sparseInstance.value(1), 0.001);
 assertEquals(3.0, sparseInstance.value(2), 0.001);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueWithInvalidIndex() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.value(-1);
 }

 @Test
 public void testClear() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.clear();
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testContainsKey() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertTrue(sparseInstance.containsKey(Integer.valueOf(0)));
 assertTrue(sparseInstance.containsKey(Integer.valueOf(1)));
 assertTrue(sparseInstance.containsKey(Integer.valueOf(2)));
 }

 @Test
 public void testContainsValue() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertTrue(sparseInstance.containsValue(Double.valueOf(1.0)));
 assertTrue(sparseInstance.containsValue(Double.valueOf(2.0)));
 assertTrue(sparseInstance.containsValue(Double.valueOf(3.0)));
 }

 @Test
 public void testEntrySet() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 Set<Map.Entry<Integer, Double>> entrySet = sparseInstance.entrySet();
 assertEquals(3, entrySet.size());
 }

 @Test
 public void testGet() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(1.0, sparseInstance.get(Integer.valueOf(0)), 0.001);
 assertEquals(2.0, sparseInstance.get(Integer.valueOf(1)), 0.001);
 assertEquals(3.0, sparseInstance.get(Integer.valueOf(2)), 0.001);
 assertEquals(0.0, sparseInstance.get(Integer.valueOf(-1)), 0.001);
 }

 @Test(expected = NullPointerException.class)
 public void testGetWithNullKey() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.get(null);
 }

 @Test
 public void testIsEmpty() {
 sparseInstance = new SparseInstance();
 assertTrue(sparseInstance.isEmpty());
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertFalse(sparseInstance.isEmpty());
 sparseInstance.clear();
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 Set<Integer> keySet = sparseInstance.keySet();
 assertEquals(3, keySet.size());
 }

 @Test
 public void testPut() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
 sparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));
 assertEquals(3, sparseInstance.noAttributes());
 assertFalse(sparseInstance.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(null, Double.valueOf(1.0));
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), null);
 }

 @Test
 public void testPutAll() {
 Map<Integer, Double> map = new HashMap<>();
 map.put(Integer.valueOf(0), Double.valueOf(1.0));
 map.put(Integer.valueOf(1), Double.valueOf(2.0));
 map.put(Integer.valueOf(2), Double.valueOf(3.0));
 sparseInstance.putAll(map);
 assertEquals(3, sparseInstance.noAttributes());
 assertFalse(sparseInstance.isEmpty());
 }

 @Test
 public void testRemove() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.removeAttribute(0);
 assertEquals(2, sparseInstance.noAttributes());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveWithInvalidIndex() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.removeAttribute(-1);
 }

 @Test
 public void testSize() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test(expected = UnsupportedOperationException.class)
 @Deprecated
 public void testSizeDeprecated() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 sparseInstance.size();
 }

 @Test
 public void testValues() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 Collection<Double> values = sparseInstance.values();
 assertEquals(3, values.size());
 }

 @Test
 public void testNoAttributes() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0});
 sparseInstance.removeAttribute(1);
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeWithInvalidIndex() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0});
 sparseInstance.removeAttribute(-1);
 }

 @Test
 public void testToString() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 String toString = sparseInstance.toString();
 assertTrue(toString.contains("{"));
 assertTrue(toString.contains("ClassValue"));
 }

 @Test
 public void testHashCode() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 int hashCode = sparseInstance.hashCode();
 }

 @Test
 public void testEquals() {
 SparseInstance sparseInstance1 = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 SparseInstance sparseInstance2 = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertTrue(sparseInstance1.equals(sparseInstance2));
 }

 @Test
 public void testCopy() {
 SparseInstance sparseInstance1 = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 SparseInstance sparseInstance2 = sparseInstance1.copy();
 assertEquals(sparseInstance1, sparseInstance2);
 }

 @Test
 public void testRemoveAttributes() {
 sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 Set<Integer> indices = new HashSet<>();
 indices.add(1);
 indices.add(3);
 sparseInstance.removeAttributes(indices);
 assertEquals(3, sparseInstance.noAttributes());
 }
}