package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class DenseInstanceTest {

 private DenseInstance createDenseInstance(double[] attributes, Object classValue) {
 return new DenseInstance(attributes, classValue);
 }

 @Test
 public void testConstructorWithDoubleArray() {
 DenseInstance denseInstance = createDenseInstance(new double[]{}, null);
 assertEquals(0, denseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithDoubleArrayAndClassValue() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue");
 assertEquals(3, denseInstance.noAttributes());
 assertEquals("ClassValue", denseInstance.classValue());
 }

 @Test
 public void testValue() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue");
 assertEquals(1.0, denseInstance.value(0), 0.0);
 assertEquals(2.0, denseInstance.value(1), 0.0);
 assertEquals(3.0, denseInstance.value(2), 0.0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueWithInvalidIndex() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue");
 denseInstance.value(3);
 }

 @Test
 public void testClear() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue");
 denseInstance.clear();
 assertEquals(0, denseInstance.noAttributes());
 }

 @Test
 public void testContainsKey() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue");
 assertTrue(denseInstance.containsKey(0));
 assertTrue(denseInstance.containsKey(1));
 assertTrue(denseInstance.containsKey(2));
 assertFalse(denseInstance.containsKey(3));
 }

 @Test
 public void testContainsValue() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue");
 assertTrue(denseInstance.containsValue(1.0));
 assertTrue(denseInstance.containsValue(2.0));
 assertTrue(denseInstance.containsValue(3.0));
 assertFalse(denseInstance.containsValue(4.0));
 }

 @Test
 public void testEntrySet() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue");
 Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
 assertEquals(3, entrySet.size());
 }

 @Test
 public void testGet() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue");
 assertEquals(1.0, denseInstance.get(0), 0.0);
 assertEquals(2.0, denseInstance.get(1), 0.0);
 assertEquals(3.0, denseInstance.get(2), 0.0);
 }

 @Test(expected = ClassCastException.class)
 public void testGetWithInvalidKeyType() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue");
 denseInstance.get("InvalidKey");
 }

 @Test
 public void testIsEmpty() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue");
 assertFalse(denseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue");
 SortedSet<Integer> keySet = denseInstance.keySet();
 assertEquals(3, keySet.size());
 }

 @Test
 public void testPut() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue");
 denseInstance.put(0, 5.0);
 assertEquals(5.0, denseInstance.value(0), 0.0);
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullAttributeArray() {
 DenseInstance denseInstance = new DenseInstance();
 denseInstance.put(0, 5.0);
 }

 @Test
 public void testPutAll() {
 DenseInstance denseInstance1 = createDenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue1");
 DenseInstance denseInstance2 = createDenseInstance(new double[]{4.0, 5.0, 6.0}, "ClassValue2");
 denseInstance1.putAll(denseInstance2.entrySet());
 assertEquals(6, denseInstance1.noAttributes());
 assertEquals("ClassValue2", denseInstance1.classValue());
 }

 @Test
 public void testRemove() {
 throw new UnsupportedOperationException("Cannot unset values from a dense instance.");
 }

 @Test
 public void testSize() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue");
 assertEquals(3, denseInstance.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSizeDeprecated() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue");
 denseInstance.size();
 }

 @Test
 public void testValues() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue");
 List<Double> values = new ArrayList<>(denseInstance.values());
 assertEquals(3, values.size());
 }

 @Test
 public void testCopy() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue");
 DenseInstance copiedInstance = denseInstance.copy();
 assertEquals(denseInstance, copiedInstance);
 }

 @Test
 public void testRemoveAttribute() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0, 4.0}, "ClassValue");
 denseInstance.removeAttribute(2);
 assertEquals(3, denseInstance.noAttributes());
 }

 @Test
 public void testHashCode() {
 DenseInstance denseInstance1 = createDenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue1");
 DenseInstance denseInstance2 = createDenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue2");
 assertEquals(denseInstance1.hashCode(), denseInstance1.hashCode());
 assertNotEquals(denseInstance1.hashCode(), denseInstance2.hashCode());
 }

 @Test
 public void testEquals() {
 DenseInstance denseInstance1 = createDenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue1");
 DenseInstance denseInstance2 = createDenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue2");
 DenseInstance denseInstance3 = createDenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue1");
 DenseInstance denseInstance4 = new DenseInstance();
 assertFalse(denseInstance1.equals(null));
 assertFalse(denseInstance1.equals(denseInstance2));
 assertTrue(denseInstance1.equals(denseInstance3));
 assertFalse(denseInstance1.equals(denseInstance4));
 }
}