package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class SparseInstanceTest {

 private SparseInstance sparseInstance;

 @Test
 public void testSparseInstanceWithDefaultConstructor() {
 sparseInstance = new SparseInstance();
 assertEquals(-1, sparseInstance.noAttributes());
 assertNull(sparseInstance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithIntConstructor() {
 sparseInstance = new SparseInstance(5);
 assertEquals(5, sparseInstance.noAttributes());
 assertNull(sparseInstance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithIntAndDefaultValueConstructor() {
 sparseInstance = new SparseInstance(5, 0.0);
 assertEquals(5, sparseInstance.noAttributes());
 assertNull(sparseInstance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithIntAndClassValueConstructor() {
 sparseInstance = new SparseInstance(5, "ClassValue");
 assertEquals(5, sparseInstance.noAttributes());
 assertEquals("ClassValue", sparseInstance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithIntArrayConstructor() {
 double[] datavector = new double[5];
 sparseInstance = new SparseInstance(datavector);
 assertEquals(5, sparseInstance.noAttributes());
 assertNull(sparseInstance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithIntArrayAndDefaultValueConstructor() {
 double[] datavector = new double[5];
 sparseInstance = new SparseInstance(datavector, 0.0);
 assertEquals(5, sparseInstance.noAttributes());
 assertNull(sparseInstance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithIntArrayAndClassValueConstructor() {
 double[] datavector = new double[5];
 sparseInstance = new SparseInstance(datavector, "ClassValue");
 assertEquals(5, sparseInstance.noAttributes());
 assertEquals("ClassValue", sparseInstance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithIntArrayAndDefaultValueAndClassValueConstructor() {
 double[] datavector = new double[5];
 sparseInstance = new SparseInstance(datavector, 0.0, "ClassValue");
 assertEquals(5, sparseInstance.noAttributes());
 assertEquals("ClassValue", sparseInstance.getClassValue());
 }

 @Test
 public void testSetNoAttributes() {
 sparseInstance = new SparseInstance(5);
 sparseInstance.setNoAttributes(10);
 assertEquals(10, sparseInstance.noAttributes());
 }

 @Test
 public void testValue() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 assertEquals(1.0, sparseInstance.value(0), 0.001);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueWithInvalidIndex() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 sparseInstance.value(-1);
 }

 @Test
 public void testClear() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 sparseInstance.clear();
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testContainsKey() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 assertTrue(sparseInstance.containsKey(Integer.valueOf(0)));
 assertFalse(sparseInstance.containsKey(Integer.valueOf(6)));
 }

 @Test
 public void testContainsValue() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 assertTrue(sparseInstance.containsValue(Double.valueOf(1.0)));
 assertFalse(sparseInstance.containsValue(Double.valueOf(6.0)));
 }

 @Test
 public void testEntrySet() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 Set<Map.Entry<Integer, Double>> entries = sparseInstance.entrySet();
 assertEquals(5, entries.size());
 }

 @Test
 public void testGet() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 assertEquals(1.0, sparseInstance.get(Integer.valueOf(0)), 0.001);
 assertEquals(0.0, sparseInstance.get(Integer.valueOf(5)), 0.001);
 }

 @Test
 public void testIsEmpty() {
 sparseInstance = new SparseInstance();
 assertTrue(sparseInstance.isEmpty());
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 assertFalse(sparseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 Set<Integer> keys = sparseInstance.keySet();
 assertEquals(5, keys.size());
 }

 @Test
 public void testPut() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(1, sparseInstance.noAttributes());
 assertEquals(1.0, sparseInstance.get(Integer.valueOf(0)), 0.001);
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
 sparseInstance = new SparseInstance();
 sparseInstance.putAll(map);
 assertEquals(2, sparseInstance.noAttributes());
 assertEquals(1.0, sparseInstance.get(Integer.valueOf(0)), 0.001);
 assertEquals(2.0, sparseInstance.get(Integer.valueOf(1)), 0.001);
 }

 @Test(expected = NullPointerException.class)
 public void testPutAllWithNullMap() {
 sparseInstance = new SparseInstance();
 sparseInstance.putAll(null);
 }

 @Test
 public void testRemove() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 sparseInstance.remove(Integer.valueOf(1));
 assertEquals(4, sparseInstance.noAttributes());
 assertNull(sparseInstance.get(Integer.valueOf(1)));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 sparseInstance.remove(null);
 }

 @Test
 public void testSize() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 assertEquals(5, sparseInstance.noAttributes());
 }

 @Test(expected = DeprecatedWarning.class)
 public void testSizeDeprecated() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 assertEquals(5, sparseInstance.size());
 }

 @Test
 public void testValues() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 Collection<Double> values = sparseInstance.values();
 assertEquals(5, values.size());
 }

 @Test
 public void testNoAttributes() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 assertEquals(5, sparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 sparseInstance.removeAttribute(1);
 assertEquals(4, sparseInstance.noAttributes());
 assertNull(sparseInstance.get(Integer.valueOf(1)));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeWithInvalidIndex() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 sparseInstance.removeAttribute(-1);
 }

 @Test
 public void testToString() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 assertEquals("{" + data.toString() + ";ClassValue}", sparseInstance.toString());
 }

 @Test
 public void testHashCode() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 int hashcode = sparseInstance.hashCode();
 SparseInstance instanceToCompare = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 assertEquals(hashcode, instanceToCompare.hashCode());
 }

 @Test
 public void testEquals() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 SparseInstance instanceToCompare = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 assertTrue(sparseInstance.equals(instanceToCompare));
 }

 @Test
 public void testCopy() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 SparseInstance copiedInstance = sparseInstance.copy();
 assertEquals(sparseInstance.noAttributes(), copiedInstance.noAttributes());
 for (Map.Entry<Integer, Double> entry : sparseInstance.entrySet()) {
 assertEquals(entry.getKey(), copiedInstance.get(entry.getKey()));
 assertEquals(entry.getValue(), copiedInstance.get(entry.getKey()), 0.001);
 }
 }

 @Test
 public void testRemoveAttributes() {
 sparseInstance = new SparseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 Set<Integer> indicesToRemove = new HashSet<>();
 indicesToRemove.add(2);
 indicesToRemove.add(4);
 sparseInstance.removeAttributes(indicesToRemove);
 assertEquals(3, sparseInstance.noAttributes());
 }
}