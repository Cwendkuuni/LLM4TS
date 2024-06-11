package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SparseInstanceTest {

 @Test
 public void testEmptySparseInstance() {
 SparseInstance instance = new SparseInstance();
 assertEquals(0, instance.noAttributes());
 assertTrue(instance.isEmpty());
 }

 @Test
 public void testSparseInstanceWithDefaultValue() {
 SparseInstance instance = new SparseInstance(5);
 assertEquals(5, instance.noAttributes());
 assertFalse(instance.isEmpty());
 for (int i = 0; i < 5; i++) {
 assertEquals(0.0, instance.value(i), 0.001);
 }
 }

 @Test
 public void testSparseInstanceWithCustomValue() {
 SparseInstance instance = new SparseInstance(5, 1.0);
 SparseInstance expected = new SparseInstance();
 expected.put(0, 1.0);
 assertEquals(5, instance.noAttributes());
 assertFalse(instance.isEmpty());
 assertEquals(expected, instance);
 }

 @Test
 public void testSparseInstanceWithClassValue() {
 SparseInstance instance = new SparseInstance(5, "StringClass");
 assertEquals("StringClass", instance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithCustomData() {
 double[] data = {1.1, 2.2, 3.3, 4.4, 5.5};
 SparseInstance instance = new SparseInstance(data, 0.0);
 assertEquals(5, instance.noAttributes());
 assertFalse(instance.isEmpty());
 for (int i = 0; i < 5; i++) {
 assertEquals(data[i], instance.value(i), 0.001);
 }
 }

 @Test
 public void testSparseInstanceWithCustomDataAndDefaultValue() {
 double[] data = {1.1, 2.2, 3.3, 4.4, 5.5};
 SparseInstance instance = new SparseInstance(data, 0.5);
 assertEquals(5, instance.noAttributes());
 assertFalse(instance.isEmpty());
 for (int i = 0; i < 5; i++) {
 if (data[i] != 0.5) {
 assertEquals(data[i], instance.value(i), 0.001);
 } else {
 assertEquals(0.5, instance.value(i), 0.001);
 }
 }
 }

 @Test
 public void testSparseInstanceWithCustomDataAndClassValue() {
 double[] data = {1.1, 2.2, 3.3, 4.4, 5.5};
 SparseInstance instance = new SparseInstance(data, 0.0, "StringClass");
 assertEquals("StringClass", instance.getClassValue());
 for (int i = 0; i < 5; i++) {
 if (data[i] != 0.0) {
 assertEquals(data[i], instance.value(i), 0.001);
 } else {
 assertEquals(0.0, instance.value(i), 0.001);
 }
 }
 }

 @Test
 public void testSetNoAttributes() {
 SparseInstance instance = new SparseInstance(5);
 instance.setNoAttributes(3);
 assertEquals(3, instance.noAttributes());
 }

 @Test
 public void testSetNoAttributesNegativeValue() {
 SparseInstance instance = new SparseInstance(5);
 try {
 instance.setNoAttributes(-1);
 fail();
 } catch (Exception e) {
 // Expected
 }
 }

 @Test
 public void testClear() {
 SparseInstance instance = new SparseInstance(5);
 instance.clear();
 assertTrue(instance.isEmpty());
 }

 @Test
 public void testContainsKey() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(0, 1.0);
 assertTrue(instance.containsKey(0));
 }

 @Test
 public void testContainsKeyNonExistentKey() {
 SparseInstance instance = new SparseInstance(5);
 assertFalse(instance.containsKey(6));
 }

 @Test
 public void testContainsValue() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(0, 1.0);
 assertTrue(instance.containsValue(1.0));
 }

 @Test
 public void testContainsValueNonExistentValue() {
 SparseInstance instance = new SparseInstance(5);
 assertFalse(instance.containsValue(2.0));
 }

 @Test
 public void testEntrySet() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(0, 1.0);
 instance.put(1, 2.0);
 Set<Map.Entry<Integer, Double>> set = instance.entrySet();
 assertEquals(2, set.size());
 }

 @Test
 public void testGet() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(0, 1.0);
 assertEquals(1.0, instance.get(0), 0.001);
 }

 @Test
 public void testGetNonExistentKey() {
 SparseInstance instance = new SparseInstance(5);
 assertEquals(0.0, instance.get(6), 0.001);
 }

 @Test
 public void testIsEmpty() {
 SparseInstance instance = new SparseInstance();
 assertTrue(instance.isEmpty());
 }

 @Test
 public void testIsEmptyAfterAddingData() {
 SparseInstance instance = new SparseInstance();
 instance.put(0, 1.0);
 assertFalse(instance.isEmpty());
 }

 @Test
 public void testKeySet() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(0, 1.0);
 Set<Integer> set = instance.keySet();
 assertEquals(1, set.size());
 }

 @Test
 public void testPut() {
 SparseInstance instance = new SparseInstance();
 instance.put(0, 1.0);
 assertEquals(1.0, instance.value(0), 0.001);
 }

 @Test
 public void testPutNullKey() {
 SparseInstance instance = new SparseInstance();
 try {
 instance.put(null, 1.0);
 fail();
 } catch (Exception e) {
 // Expected
 }
 }

 @Test
 public void testPutNullValue() {
 SparseInstance instance = new SparseInstance();
 instance.put(0, null);
 assertNull(instance.value(0));
 }

 @Test
 public void testPutDuplicateKey() {
 SparseInstance instance = new SparseInstance();
 instance.put(0, 1.0);
 instance.put(0, 2.0);
 assertEquals(1.0, instance.value(0), 0.001);
 }

 @Test
 public void testPutAll() {
 SparseInstance instance = new SparseInstance();
 Map<Integer, Double> map = new HashMap<>();
 map.put(0, 1.0);
 map.put(1, 2.0);
 instance.putAll(map);
 assertEquals(2, instance.noAttributes());
 }

 @Test
 public void testRemove() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(0, 1.0);
 instance.put(1, 2.0);
 assertEquals(2, instance.noAttributes());
 instance.remove(0);
 assertEquals(1, instance.noAttributes());
 }

 @Test
 public void testRemoveNonExistentKey() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(0, 1.0);
 assertFalse(instance.remove(6));
 }

 @Test
 public void testSize() { // Deprecated
 SparseInstance instance = new SparseInstance();
 Map<Integer, Double> map = new HashMap<>();
 map.put(0, 1.0);
 map.put(1, 2.0);
 instance.putAll(map);
 assertEquals(2, instance.size()); // Deprecated
 }

 @Test
 public void testValues() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(0, 1.0);
 instance.put(1, 2.0);
 List<Double> list = new ArrayList<>(instance.values());
 assertEquals(2, list.size());
 }

 @Test
 public void testNoAttributes() {
 SparseInstance instance = new SparseInstance();
 instance.put(0, 1.0);
 assertEquals(1, instance.noAttributes());
 }

 @Test
 public void testNoAttributesNegativeValue() {
 SparseInstance instance = new SparseInstance();
 instance.put(0, 1.0);
 try {
 instance.setNoAttributes(-1);
 fail();
 } catch (Exception e) {
 // Expected
 }
 }

 @Test
 public void testNoAttributesAfterAddingData() {
 SparseInstance instance = new SparseInstance();
 instance.put(0, 1.0);
 assertEquals(1, instance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(0, 1.0);
 instance.put(1, 2.0);
 instance.removeAttribute(1);
 assertEquals(1, instance.noAttributes());
 }

 @Test
 public void testRemoveAttributeNonExistentIndex() {
 SparseInstance instance = new SparseInstance(5);
 instance.put(0, 1.0);
 assertFalse(instance.removeAttribute(6));
 }

 @Test
 public void testToString() {
 SparseInstance instance = new SparseInstance();
 instance.put(0, 1.0);
 instance.put(1, 2.0);
 assertEquals("{" + instance.data.toString() + ";" + instance.getClassValue() + "}", instance.toString());
 }
}