package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class DenseInstanceTest {

 @Test
 public void testConstructorWithDoubleArray() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(3, denseInstance.noAttributes());
 assertArrayEquals(new double[] {1.0, 2.0, 3.0}, denseInstance.attributes, 0.001);
 }

 @Test
 public void testConstructorWithDoubleArrayAndClassValue() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0}, "ClassValue");
 assertEquals("ClassValue", denseInstance.classValue());
 assertEquals(3, denseInstance.noAttributes());
 assertArrayEquals(new double[] {1.0, 2.0, 3.0}, denseInstance.attributes, 0.001);
 }

 @Test
 public void testConstructorWithSize() {
 DenseInstance denseInstance = new DenseInstance(5);
 assertEquals(5, denseInstance.noAttributes());
 assertArrayEquals(new double[5], denseInstance.attributes);
 }

 @Test
 public void testValue() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(1.0, denseInstance.value(0), 0.001);
 assertEquals(2.0, denseInstance.value(1), 0.001);
 assertEquals(3.0, denseInstance.value(2), 0.001);
 }

 @Test
 public void testClear() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 denseInstance.clear();
 assertEquals(0, denseInstance.noAttributes());
 assertArrayEquals(new double[0], denseInstance.attributes);
 }

 @Test
 public void testContainsKey() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertTrue(denseInstance.containsKey(0));
 assertTrue(denseInstance.containsKey(1));
 assertTrue(denseInstance.containsKey(2));
 assertFalse(denseInstance.containsKey(3));
 }

 @Test
 public void testContainsValue() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 1.5, 2.0, 2.5, 3.0});
 assertTrue(denseInstance.containsValue(1.0));
 assertTrue(denseInstance.containsValue(1.5));
 assertTrue(denseInstance.containsValue(2.0));
 assertTrue(denseInstance.containsValue(2.5));
 assertFalse(denseInstance.containsValue(3.1));
 }

 @Test
 public void testEntrySet() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 Set<Map.Entry<Integer, Double>> entries = denseInstance.entrySet();
 assertEquals(3, entries.size());
 for (Map.Entry<Integer, Double> entry : entries) {
 Integer key = entry.getKey();
 Double value = entry.getValue();
 assertEquals(key, entry.getKey());
 assertEquals(value, entry.getValue());
 }
 }

 @Test
 public void testGet() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertEquals(1.0, denseInstance.get(0), 0.001);
 assertEquals(2.0, denseInstance.get(1), 0.001);
 assertEquals(3.0, denseInstance.get(2), 0.001);
 }

 @Test
 public void testIsEmpty() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 assertFalse(denseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 SortedSet<Integer> keys = denseInstance.keySet();
 assertEquals(3, keys.size());
 for (Integer key : keys) {
 assertEquals(key, denseInstance.value(key));
 }
 }

 @Test
 public void testPut() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 denseInstance.put(0, 4.0);
 assertEquals(4.0, denseInstance.value(0), 0.001);
 }

 @Test
 public void testPutAll() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance denseInstanceToAdd = new DenseInstance(new double[] {4.0, 5.0, 6.0});
 denseInstance.putAll(denseInstanceToAdd.entrySet());
 assertEquals(6, denseInstance.noAttributes());
 for (int i = 0; i < 6; i++) {
 assertEquals(i < 3 ? denseInstance.attributes[i] : denseInstanceToAdd.attributes[i - 3], denseInstance.attributes[i], 0.001);
 }
 }

 @Test
 public void testRemoveAttribute() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0});
 denseInstance.removeAttribute(2);
 assertEquals(3, denseInstance.noAttributes());
 assertArrayEquals(new double[] {1.0, 4.0}, denseInstance.attributes, 0.001);
 }

 @Test
 public void testHashCode() {
 DenseInstance denseInstance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance denseInstance2 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance denseInstance3 = new DenseInstance(new double[] {1.1, 2.1, 3.1});
 assertEquals(denseInstance1.hashCode(), denseInstance1.hashCode());
 assertEquals(denseInstance1.hashCode(), denseInstance2.hashCode());
 assertNotEquals(denseInstance1.hashCode(), denseInstance3.hashCode());
 }

 @Test
 public void testEquals() {
 DenseInstance denseInstance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance denseInstance2 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance denseInstance3 = new DenseInstance(new double[] {1.1, 2.1, 3.1});
 assertTrue(denseInstance1.equals(denseInstance1));
 assertTrue(denseInstance1.equals(denseInstance2));
 assertFalse(denseInstance1.equals(denseInstance3));
 }

 @Test
 public void testCopy() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
 DenseInstance copy = denseInstance.copy();
 assertEquals(denseInstance, copy);
 assertArrayEquals(denseInstance.attributes, copy.attributes);
 }

 @Test
 public void testRemoveAttributes() {
 DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0});
 Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(2, 4));
 denseInstance.removeAttributes(indicesToRemove);
 assertEquals(3, denseInstance.noAttributes());
 assertArrayEquals(new double[] {1.0, 3.0}, denseInstance.attributes, 0.001);
 }
}