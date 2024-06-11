package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class SparseInstanceTest {

 private SparseInstance sparseInstance;

 @Test
 public void testSparseInstanceEmptyConstruction() {
 sparseInstance = new SparseInstance();
 assertEquals(0, sparseInstance.noAttributes());
 assertNull(sparseInstance.getClassValue());
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithDefaultValueConstruction() {
 sparseInstance = new SparseInstance(3);
 assertEquals(3, sparseInstance.noAttributes());
 assertNull(sparseInstance.getClassValue());
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithClassValueConstruction() {
 sparseInstance = new SparseInstance(3, "ClassValue");
 assertEquals(3, sparseInstance.noAttributes());
 assertEquals("ClassValue", sparseInstance.getClassValue().toString());
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithDefaultValueAndDataConstruction() {
 sparseInstance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.5);
 assertEquals(3, sparseInstance.noAttributes());
 assertNull(sparseInstance.getClassValue());
 assertFalse(sparseInstance.isEmpty());
 assertEquals(1.0, sparseInstance.value(0), 0.001);
 assertEquals(2.0, sparseInstance.value(1), 0.001);
 assertEquals(3.0, sparseInstance.value(2), 0.001);
 }

 @Test
 public void testSparseInstanceWithClassValueAndDataConstruction() {
 sparseInstance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.5, "ClassValue");
 assertEquals(3, sparseInstance.noAttributes());
 assertEquals("ClassValue", sparseInstance.getClassValue().toString());
 assertFalse(sparseInstance.isEmpty());
 assertEquals(1.0, sparseInstance.value(0), 0.001);
 assertEquals(2.0, sparseInstance.value(1), 0.001);
 assertEquals(3.0, sparseInstance.value(2), 0.001);
 }

 @Test
 public void testSetNoAttributes() {
 sparseInstance = new SparseInstance(3);
 sparseInstance.setNoAttributes(4);
 assertEquals(4, sparseInstance.noAttributes());
 }

 @Test
 public void testValue() {
 sparseInstance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.5);
 assertEquals(1.0, sparseInstance.value(0), 0.001);
 assertEquals(2.0, sparseInstance.value(1), 0.001);
 assertEquals(3.0, sparseInstance.value(2), 0.001);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueNegativeIndex() {
 sparseInstance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.5);
 sparseInstance.value(-1);
 }

 @Test
 public void testClear() {
 sparseInstance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.5);
 sparseInstance.clear();
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testContainsKey() {
 sparseInstance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.5);
 assertTrue(sparseInstance.containsKey(0));
 assertTrue(sparseInstance.containsKey(1));
 assertTrue(sparseInstance.containsKey(2));
 assertFalse(sparseInstance.containsKey(-1));
 }

 @Test
 public void testContainsValue() {
 sparseInstance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.5);
 assertTrue(sparseInstance.containsValue(1.0));
 assertTrue(sparseInstance.containsValue(2.0));
 assertTrue(sparseInstance.containsValue(3.0));
 assertFalse(sparseInstance.containsValue(4.0));
 }

 @Test
 public void testEntrySet() {
 sparseInstance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.5);
 Set<Map.Entry<Integer, Double>> entrySet = sparseInstance.entrySet();
 assertEquals(3, entrySet.size());
 }

 @Test
 public void testGet() {
 sparseInstance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.5);
 assertEquals(1.0, sparseInstance.get(0).doubleValue(), 0.001);
 assertEquals(2.0, sparseInstance.get(1).doubleValue(), 0.001);
 assertEquals(3.0, sparseInstance.get(2).doubleValue(), 0.001);
 assertEquals(0.5, sparseInstance.get(-1).doubleValue(), 0.001);
 }

 @Test
 public void testIsEmpty() {
 sparseInstance = new SparseInstance();
 assertTrue(sparseInstance.isEmpty());
 sparseInstance.put(0, 1.0);
 assertFalse(sparseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 sparseInstance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.5);
 Set<Integer> keySet = sparseInstance.keySet();
 assertEquals(3, keySet.size());
 }

 @Test
 public void testPut() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 assertEquals(1, sparseInstance.noAttributes());
 assertEquals(1.0, sparseInstance.value(0), 0.001);
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullKey() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(null, 1.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutNegativeKey() {
 sparseInstance = new SparseInstance();
 sparseInstance.put(-1, 1.0);
 }

 @Test
 public void testPutAll() {
 SparseInstance sparseInstance2 = new SparseInstance(new double[] { 4.0, 5.0, 6.0 }, 0.5);
 sparseInstance.putAll(sparseInstance2.entrySet());
 assertEquals(6, sparseInstance.noAttributes());
 }

 @Test
 public void testRemove() {
 sparseInstance = new SparseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 }, 0.5);
 sparseInstance.remove(1);
 assertEquals(3, sparseInstance.noAttributes());
 assertEquals(1.0, sparseInstance.value(0), 0.001);
 assertEquals(3.0, sparseInstance.value(2), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveNegativeIndex() {
 sparseInstance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.5);
 sparseInstance.remove(-1);
 }

 @Test
 public void testSize() {
 sparseInstance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.5);
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test(expected = DeprecatedWarning.class)
 public void testSizeDeprecated() {
 sparseInstance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.5);
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testValues() {
 sparseInstance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.5);
 List<Double> values = new ArrayList<Double>(sparseInstance.values());
 assertEquals(3, values.size());
 assertEquals(1.0, values.get(0), 0.001);
 assertEquals(2.0, values.get(1), 0.001);
 assertEquals(3.0, values.get(2), 0.001);
 }

 @Test
 public void testNoAttributes() {
 sparseInstance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.5);
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 sparseInstance = new SparseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 }, 0.5);
 sparseInstance.removeAttribute(1);
 assertEquals(3, sparseInstance.noAttributes());
 assertEquals(1.0, sparseInstance.value(0), 0.001);
 assertEquals(3.0, sparseInstance.value(2), 0.001);
 }

 @Test(expected = NoSuchElementException.class)
 public void testRemoveAttributeOutOfBounds() {
 sparseInstance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.5);
 sparseInstance.removeAttribute(3);
 }

 @Test
 public void testToString() {
 sparseInstance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.5);
 assertEquals("{" + "1.0=1.0;" + "2.0=2.0;" + "3.0=3.0;" + ";ClassValue}", sparseInstance.toString());
 }

 @Test
 public void testHashCode() {
 SparseInstance sparseInstance3 = new SparseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.5);
 SparseInstance sparseInstance4 = new SparseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.5);
 SparseInstance sparseInstance5 = new SparseInstance(new double[] { 1.1, 2.1, 3.1 }, 0.5);
 assertEquals(hashCode(), sparseInstance3.hashCode());
 assertNotEquals(hashCode(), sparseInstance4.hashCode());
 assertNotEquals(hashCode(), sparseInstance5.hashCode());
 }

 @Test
 public void testEquals() {
 SparseInstance sparseInstance3 = new SparseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.5);
 SparseInstance sparseInstance4 = new SparseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.5);
 SparseInstance sparseInstance5 = new SparseInstance(new double[] { 1.1, 2.1, 3.1 }, 0.5);
 assertTrue(sparseInstance3.equals(sparseInstance3));
 assertTrue(sparseInstance3.equals(sparseInstance4));
 assertFalse(sparseInstance3.equals(sparseInstance5));
 }

 @Test
 public void testCopy() {
 SparseInstance sparseInstance3 = new SparseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.5);
 SparseInstance copiedInstance = sparseInstance3.copy();
 assertEquals(sparseInstance3.noAttributes(), copiedInstance.noAttributes());
 assertEquals(sparseInstance3.getClassValue(), copiedInstance.getClassValue());
 assertEquals(sparseInstance3.hashCode(), copiedInstance.hashCode());
 for (int i = 0; i < sparseInstance3.noAttributes(); i++) {
 assertEquals(sparseInstance3.value(i), copiedInstance.value(i), 0.001);
 }
 }

 @Test
 public void testRemoveAttributes() {
 SparseInstance sparseInstance3 = new SparseInstance(new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 }, 0.5);
 Set<Integer> indices = new HashSet<Integer>() {{ add(1); add(3); add(5); }};
 sparseInstance3.removeAttributes(indices);
 assertEquals(2, sparseInstance3.noAttributes());
 assertEquals(1.0, sparseInstance3.value(0), 0.001);
 assertEquals(4.0, sparseInstance3.value(1), 0.001);
 }
}