package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class DenseInstanceTest {

 private DenseInstance createDenseInstance(double[] attributes, Object classValue) {
 return new DenseInstance(attributes, classValue);
 }

 @Test
 public void testDenseInstanceWithEmptyArray() {
 DenseInstance denseInstance = new DenseInstance();
 assertEquals(0, denseInstance.noAttributes());
 assertNull(denseInstance.getClassValue());
 assertArrayEquals(new double[0], denseInstance.attributes);
 }

 @Test
 public void testDenseInstanceWithAttributes() {
 double[] attributes = new double[] {1.0, 2.0, 3.0};
 DenseInstance denseInstance = createDenseInstance(attributes, null);
 assertEquals(3, denseInstance.noAttributes());
 assertNull(denseInstance.getClassValue());
 assertArrayEquals(attributes, denseInstance.attributes);
 }

 @Test
 public void testDenseInstanceWithClassValue() {
 double[] attributes = new double[] {1.0, 2.0, 3.0};
 DenseInstance denseInstance = createDenseInstance(attributes, "classValue");
 assertEquals(3, denseInstance.noAttributes());
 assertEquals("classValue", denseInstance.getClassValue().toString());
 assertArrayEquals(attributes, denseInstance.attributes);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAttribute() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 denseInstance.removeAttribute(1);
 }

 @Test
 public void testValue() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 assertEquals(1.0, denseInstance.value(0), 0.0);
 assertEquals(2.0, denseInstance.value(1), 0.0);
 assertEquals(3.0, denseInstance.value(2), 0.0);
 }

 @Test
 public void testClear() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 denseInstance.clear();
 assertEquals(0, denseInstance.noAttributes());
 assertNull(denseInstance.getClassValue());
 assertArrayEquals(new double[0], denseInstance.attributes);
 }

 @Test
 public void testContainsKey() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 assertTrue(denseInstance.containsKey(0));
 assertTrue(denseInstance.containsKey(1));
 assertTrue(denseInstance.containsKey(2));
 assertFalse(denseInstance.containsKey(3));
 }

 @Test
 public void testContainsValue() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 1.5, 2.0, 3.0}, null);
 assertTrue(denseInstance.containsValue(1.0));
 assertTrue(denseInstance.containsValue(1.5));
 assertTrue(denseInstance.containsValue(2.0));
 assertFalse(denseInstance.containsValue(4.0));
 }

 @Test
 public void testEntrySet() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 Set<Map.Entry<Integer, Double>> entries = denseInstance.entrySet();
 assertEquals(3, entries.size());
 for (Map.Entry<Integer, Double> entry : entries) {
 assertEquals(entry.getKey(), entry.getValue().intValue());
 }
 }

 @Test
 public void testGet() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 assertEquals(1.0, denseInstance.get(0), 0.0);
 assertEquals(2.0, denseInstance.get(1), 0.0);
 assertEquals(3.0, denseInstance.get(2), 0.0);
 assertNull(denseInstance.get(3));
 }

 @Test
 public void testIsEmpty() {
 DenseInstance denseInstance = new DenseInstance();
 assertTrue(denseInstance.isEmpty());
 denseInstance.put(0, 1.0);
 assertFalse(denseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 SortedSet<Integer> keys = denseInstance.keySet();
 assertEquals(3, keys.size());
 for (Integer key : keys) {
 assertEquals(key, denseInstance.value(key).intValue());
 }
 }

 @Test
 public void testPut() {
 DenseInstance denseInstance = new DenseInstance(3);
 double value = 1.0;
 denseInstance.put(0, value);
 assertEquals(value, denseInstance.value(0), 0.0);
 denseInstance.put(1, 2.0);
 assertEquals(2.0, denseInstance.value(1), 0.0);
 denseInstance.put(2, value);
 assertEquals(value, denseInstance.value(2), 0.0);
 }

 @Test
 public void testPutAll() {
 DenseInstance denseInstance = new DenseInstance(5);
 double[] attributes = new double[] {1.0, 2.0, 3.0, 4.0, 5.0};
 Map<Integer, Double> map = new HashMap<>();
 for (int i = 0; i < attributes.length; i++) {
 map.put(i, (double) i);
 }
 denseInstance.putAll(map);
 for (int i = 0; i < attributes.length; i++) {
 assertEquals(attributes[i], denseInstance.value(i), 0.0);
 }
 }

 @Test
 public void testRemoveAttributes() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0, 4.0, 5.0}, null);
 Set<Integer> indicesToRemove = new HashSet<Integer>() {{
 add(2);
 add(4);
 }};
 denseInstance.removeAttributes(indicesToRemove);
 assertEquals(3, denseInstance.noAttributes());
 assertArrayEquals(new double[] {1.0, 3.0}, denseInstance.attributes);
 }

 @Test
 public void testSize() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 assertEquals(3, denseInstance.noAttributes());
 }

 @Test
 public void testValues() {
 DenseInstance denseInstance = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 Collection<Double> values = denseInstance.values();
 assertEquals(3, values.size());
 for (Double value : values) {
 assertEquals(value, denseInstance.value(values.indexOf(value)), 0.0);
 }
 }

 @Test
 public void testHashCode() {
 DenseInstance denseInstance1 = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance2 = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance3 = createDenseInstance(new double[] {1.0, 2.0, 4.0}, null);
 assertEquals(denseInstance1.hashCode(), denseInstance1.hashCode());
 assertEquals(denseInstance1.hashCode(), denseInstance2.hashCode());
 assertNotEquals(denseInstance1.hashCode(), denseInstance3.hashCode());
 }

 @Test
 public void testEquals() {
 DenseInstance denseInstance1 = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance2 = createDenseInstance(new double[] {1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance3 = createDenseInstance(new double[] {1.0, 2.0, 4.0}, null);
 DenseInstance denseInstance4 = denseInstance1.copy();
 assertTrue(denseInstance1.equals(denseInstance1));
 assertTrue(denseInstance1.equals(denseInstance4));
 assertFalse(denseInstance1.equals(denseInstance2));
 assertFalse(denseInstance1.equals(denseInstance3));
 }
}