package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.junit.Test;

public class SparseInstanceTest {

 private static final double DEFAULT_VALUE = 0.0;

 @Test
 public void testEmptySparseInstance() {
 SparseInstance instance = new SparseInstance();
 assertEquals(-1, instance.noAttributes());
 assertFalse(instance.containsKey(0));
 assertNull(instance.get(0));
 assertFalse(instance.containsValue(0.0));
 assertFalse(instance.isEmpty());
 assertFalse(instance.isInstanceOf(String.class));
 assertEquals(0, instance.hashCode());
 }

 @Test
 public void testSparseInstanceWithDefaultValue() {
 SparseInstance instance = new SparseInstance(3);
 assertEquals(3, instance.noAttributes());
 assertFalse(instance.containsKey(0));
 assertNull(instance.get(0));
 assertFalse(instance.containsValue(0.0));
 assertTrue(instance.isEmpty());
 assertFalse(instance.isInstanceOf(String.class));
 assertEquals(0, instance.hashCode());
 }

 @Test
 public void testSparseInstanceWithData() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(3, instance.noAttributes());
 assertTrue(instance.containsKey(0));
 assertEquals(1.0, instance.get(0), 0.0);
 assertTrue(instance.containsKey(1));
 assertEquals(2.0, instance.get(1), 0.0);
 assertTrue(instance.containsKey(2));
 assertEquals(3.0, instance.get(2), 0.0);
 assertFalse(instance.containsValue(4.0));
 assertFalse(instance.isEmpty());
 assertFalse(instance.isInstanceOf(String.class));
 }

 @Test
 public void testSetNoAttributes() {
 SparseInstance instance = new SparseInstance();
 instance.setNoAttributes(5);
 assertEquals(5, instance.noAttributes());
 }

 @Test
 public void testSetDefaultValue() {
 SparseInstance instance = new SparseInstance();
 instance.setDefaultValue(4.1);
 assertEquals(instance.getClass(), SparseInstance.class);
 assertEquals(4.1, instance.defaultValue, 0.0);
 }

 @Test
 public void testSetClassValue() {
 SparseInstance instance = new SparseInstance();
 instance.setClassValue("classValue");
 assertEquals("classValue", instance.classValue());
 }

 @Test
 public void testCopy() {
 SparseInstance original = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 SparseInstance copy = original.copy();
 assertTrue(original != copy);
 assertFalse(original.equals(copy));
 assertEquals(original.noAttributes(), copy.noAttributes());
 assertEquals(original.defaultValue, copy.defaultValue);
 for (Map.Entry<Integer, Double> entry : original.entrySet()) {
 assertEquals(entry.getKey(), copy.get(entry.getKey()));
 assertEquals(entry.getValue(), copy.get(entry.getKey()), 0.0);
 }
 }

 @Test
 public void testGetValue() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(1.0, instance.value(0), 0.0);
 assertEquals(2.0, instance.value(1), 0.0);
 assertEquals(3.0, instance.value(2), 0.0);
 assertEquals(DEFAULT_VALUE, instance.value(-1), 0.0);
 }

 @Test
 public void testClear() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 instance.clear();
 assertEquals(-1, instance.noAttributes());
 assertFalse(instance.containsKey(0));
 assertNull(instance.get(0));
 assertFalse(instance.containsValue(1.0));
 assertFalse(instance.containsValue(2.0));
 assertFalse(instance.containsValue(3.0));
 assertFalse(instance.isEmpty());
 }

 @Test
 public void testContainsKey() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertTrue(instance.containsKey(0));
 assertTrue(instance.containsKey(1));
 assertTrue(instance.containsKey(2));
 assertFalse(instance.containsKey(3));
 }

 @Test
 public void testContainsValue() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertTrue(instance.containsValue(1.0));
 assertTrue(instance.containsValue(2.0));
 assertTrue(instance.containsValue(3.0));
 assertFalse(instance.containsValue(4.0));
 }

 @Test
 public void testEntrySet() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 Map<Integer, Double> expectedMap = new HashMap<Integer, Double>();
 expectedMap.put(0, 1.0);
 expectedMap.put(1, 2.0);
 expectedMap.put(2, 3.0);
 List<Map.Entry<Integer, Double>> entries = new ArrayList<Map.Entry<Integer, Double>>(instance.entrySet());
 for (Map.Entry<Integer, Double> entry : expectedMap.entrySet()) {
 assertEquals(entry.getKey(), entries.get(0).getKey());
 assertEquals(entry.getValue(), entries.get(0).getValue(), 0.0);
 // remove the first entry from the list to continue the test
 entries.remove(0);
 }
 assertTrue(entries.isEmpty());
 }

 @Test
 public void testGet() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(1.0, instance.get(0), 0.0);
 assertEquals(2.0, instance.get(1), 0.0);
 assertEquals(3.0, instance.get(2), 0.0);
 assertEquals(DEFAULT_VALUE, instance.get(3), 0.0);
 }

 @Test
 public void testIsEmpty() {
 SparseInstance instance = new SparseInstance();
 assertTrue(instance.isEmpty());
 SparseInstance instanceWithData = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertFalse(instanceWithData.isEmpty());
 }

 @Test
 public void testKeySet() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 TreeSet<Integer> expectedSet = new TreeSet<Integer>(Arrays.asList(0, 1, 2));
 TreeSet<Integer> set = new TreeSet<Integer>(instance.keySet());
 assertEquals(expectedSet, set);
 }

 @Test
 public void testPut() {
 SparseInstance instance = new SparseInstance();
 instance.put(0, Double.valueOf(1.0));
 assertEquals(1.0, instance.get(0), 0.0);
 instance.put(1, Double.valueOf(2.0));
 assertEquals(2.0, instance.get(1), 0.0);
 instance.put(2, Double.valueOf(3.0));
 assertEquals(3.0, instance.get(2), 0.0);
 instance.put(3, Double.valueOf(4.0));
 assertEquals(4.0, instance.get(3), 0.0);
 assertEquals(4, instance.noAttributes());
 }

 @Test
 public void testPutAll() {
 SparseInstance instance = new SparseInstance();
 Map<Integer, Double> map = new HashMap<Integer, Double>();
 map.put(0, Double.valueOf(1.0));
 map.put(1, Double.valueOf(2.0));
 map.put(2, Double.valueOf(3.0));
 instance.putAll(map);
 assertEquals(3, instance.noAttributes());
 assertEquals(1.0, instance.get(0), 0.0);
 assertEquals(2.0, instance.get(1), 0.0);
 assertEquals(3.0, instance.get(2), 0.0);
 }

 @Test
 public void testRemove() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(3.0, instance.get(2), 0.0);
 instance.remove(2);
 assertEquals(DEFAULT_VALUE, instance.get(2), 0.0);
 assertEquals(2, instance.noAttributes());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAttributeWithEmptyInstance() {
 SparseInstance instance = new SparseInstance();
 instance.removeAttribute(0);
 }

 @Test
 public void testRemoveAttribute() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 instance.removeAttribute(1);
 assertEquals(2.0, instance.get(0), 0.0);
 assertEquals(2, instance.noAttributes());
 }

 @Test
 public void testRemoveAttributes() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
 Set<Integer> indices = new TreeSet<Integer>(Arrays.asList(2, 4));
 instance.removeAttributes(indices);
 assertEquals(3.0, instance.get(0), 0.0);
 assertEquals(1.0, instance.get(1), 0.0);
 assertEquals(5.0, instance.get(2), 0.0);
 assertEquals(3, instance.noAttributes());
 }

 @Test
 public void testSize() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(3, instance.size());
 }

 @Test(expected = Deprecated.class)
 public void testSizeDeprecated() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 int size = instance.size();
 }

 @Test
 public void testValues() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 List<Double> values = new ArrayList<Double>(instance.values());
 assertEquals(Arrays.asList(1.0, 2.0, 3.0), values);
 }

 @Test
 public void testNoAttributes() {
 SparseInstance instance = new SparseInstance();
 assertEquals(-1, instance.noAttributes());
 SparseInstance instanceWithData = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(3, instanceWithData.noAttributes());
 }

 @Test
 public void testNoAttributesWithEmptyInstance() {
 SparseInstance instance = new SparseInstance();
 assertEquals(0, instance.noAttributes());
 }

 @Test
 public void testRemoveAttributeWithIndexOutOfBounds() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 try {
 instance.removeAttribute(-1);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }
}