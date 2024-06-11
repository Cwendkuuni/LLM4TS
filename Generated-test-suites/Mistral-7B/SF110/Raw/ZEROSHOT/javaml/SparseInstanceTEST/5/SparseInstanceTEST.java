package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SparseInstanceTest {

 private SparseInstance createInstance(int noAttributes, double[] datavector, double defaultValue, Object classValue) {
 SparseInstance instance = new SparseInstance(noAttributes, defaultValue, classValue);
 if (datavector != null) {
 instance.initiate(datavector);
 }
 return instance;
 }

 @Test
 public void testEmptyConstructor() {
 SparseInstance instance = new SparseInstance();
 assertEquals(-1, instance.noAttributes());
 assertFalse(instance.containsKey(0));
 }

 @Test
 public void testConstructorWithNoAttributes() {
 SparseInstance instance = new SparseInstance(0);
 assertEquals(0, instance.noAttributes());
 assertFalse(instance.containsKey(0));
 }

 @Test
 public void testConstructorWithNoAttributesAndDefaultValue() {
 SparseInstance instance = new SparseInstance(0, 0.0);
 assertEquals(0, instance.noAttributes());
 assertFalse(instance.containsKey(0));
 }

 @Test
 public void testConstructorWithNoAttributesAndClassValue() {
 SparseInstance instance = new SparseInstance(0, "StringClassValue");
 assertEquals(0, instance.noAttributes());
 assertFalse(instance.containsKey(0));
 assertEquals("StringClassValue", instance.getClassValue());
 }

 @Test
 public void testConstructorWithNoAttributesAndDefaultValueAndClassValue() {
 SparseInstance instance = new SparseInstance(0, 0.0, "StringClassValue");
 assertEquals(0, instance.noAttributes());
 assertFalse(instance.containsKey(0));
 assertEquals("StringClassValue", instance.getClassValue());
 }

 @Test
 public void testConstructorWithDatavector() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector);
 assertEquals(3, instance.noAttributes());
 assertTrue(instance.containsKey(0));
 assertEquals(1.0, instance.get(0), 0.001);
 assertTrue(instance.containsKey(1));
 assertEquals(2.0, instance.get(1), 0.001);
 assertTrue(instance.containsKey(2));
 assertEquals(3.0, instance.get(2), 0.001);
 assertFalse(instance.containsKey(3));
 }

 @Test
 public void testSetNoAttributes() {
 SparseInstance instance = new SparseInstance();
 instance.setNoAttributes(5);
 assertEquals(5, instance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNoAttributesNegativeValue() {
 SparseInstance instance = new SparseInstance();
 instance.setNoAttributes(-1);
 }

 @Test
 public void testValue() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector);
 assertEquals(1.0, instance.value(0), 0.001);
 assertEquals(2.0, instance.value(1), 0.001);
 assertEquals(3.0, instance.value(2), 0.001);
 assertEquals(0.0, instance.value(3), 0.001);
 }

 @Test
 public void testClear() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector);
 instance.clear();
 assertEquals(0, instance.noAttributes());
 assertFalse(instance.containsKey(0));
 }

 @Test
 public void testContainsKey() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector);
 assertTrue(instance.containsKey(0));
 assertTrue(instance.containsKey(1));
 assertTrue(instance.containsKey(2));
 assertFalse(instance.containsKey(3));
 }

 @Test
 public void testContainsValue() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector);
 assertTrue(instance.containsValue(1.0));
 assertTrue(instance.containsValue(2.0));
 assertTrue(instance.containsValue(3.0));
 assertFalse(instance.containsValue(4.0));
 }

 @Test
 public void testEntrySet() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector);
 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 assertEquals(3, entrySet.size());
 List<Map.Entry<Integer, Double>> entryList = new ArrayList<>(entrySet);
 assertEquals(1.0, entryList.get(0).getValue(), 0.001);
 assertEquals(0, entryList.get(0).getKey().intValue());
 assertEquals(2.0, entryList.get(1).getValue(), 0.001);
 assertEquals(1, entryList.get(1).getKey().intValue());
 assertEquals(3.0, entryList.get(2).getValue(), 0.001);
 assertEquals(2, entryList.get(2).getKey().intValue());
 }

 @Test
 public void testGet() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector);
 assertEquals(1.0, instance.get(0), 0.001);
 assertEquals(2.0, instance.get(1), 0.001);
 assertEquals(3.0, instance.get(2), 0.001);
 assertEquals(0.0, instance.get(3), 0.001);
 }

 @Test
 public void testIsEmpty() {
 SparseInstance instance = new SparseInstance();
 assertTrue(instance.isEmpty());
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 instance.initiate(datavector);
 assertFalse(instance.isEmpty());
 }

 @Test
 public void testKeySet() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector);
 Set<Integer> keySet = instance.keySet();
 assertEquals(3, keySet.size());
 List<Integer> keyList = new ArrayList<>(keySet);
 assertEquals(0, keyList.get(0).intValue());
 assertEquals(1, keyList.get(1).intValue());
 assertEquals(2, keyList.get(2).intValue());
 }

 @Test
 public void testPut() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance();
 instance.put(Integer.valueOf(0), Double.valueOf(4.0));
 instance.put(Integer.valueOf(1), Double.valueOf(5.0));
 instance.put(Integer.valueOf(2), Double.valueOf(6.0));
 assertEquals(3, instance.noAttributes());
 assertTrue(instance.containsKey(0));
 assertEquals(4.0, instance.get(0), 0.001);
 assertTrue(instance.containsKey(1));
 assertEquals(5.0, instance.get(1), 0.001);
 assertTrue(instance.containsKey(2));
 assertEquals(6.0, instance.get(2), 0.001);
 assertFalse(instance.containsKey(3));
 }

 @Test
 public void testPutAll() {
 double[] datavector1 = new double[]{1.0, 2.0, 3.0};
 double[] datavector2 = new double[]{4.0, 5.0, 6.0};
 SparseInstance instance = new SparseInstance();
 Map<Integer, Double> map = new HashMap<>();
 map.put(Integer.valueOf(0), Double.valueOf(4.0));
 map.put(Integer.valueOf(1), Double.valueOf(5.0));
 map.put(Integer.valueOf(2), Double.valueOf(6.0));
 instance.putAll(map);
 assertEquals(3, instance.noAttributes());
 assertTrue(instance.containsKey(0));
 assertEquals(4.0, instance.get(0), 0.001);
 assertTrue(instance.containsKey(1));
 assertEquals(5.0, instance.get(1), 0.001);
 assertTrue(instance.containsKey(2));
 assertEquals(6.0, instance.get(2), 0.001);
 assertFalse(instance.containsKey(3));
 }

 @Test
 public void testRemove() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector);
 double removedValue = instance.remove(0);
 assertEquals(1.0, removedValue, 0.001);
 assertEquals(2, instance.noAttributes());
 assertFalse(instance.containsKey(0));
 }

 @Test
 public void testSize() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector);
 assertEquals(3, instance.noAttributes());
 }

 @Test
 public void testValues() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector);
 List<Double> values = new ArrayList<>(instance.values());
 assertEquals(3, values.size());
 assertEquals(1.0, values.get(0), 0.001);
 assertEquals(2.0, values.get(1), 0.001);
 assertEquals(3.0, values.get(2), 0.001);
 }

 @Test
 public void testNoAttributes() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector);
 assertEquals(3, instance.noAttributes());
 instance.removeAttribute(0);
 assertEquals(2, instance.noAttributes());
 instance.removeAttribute(1);
 assertEquals(1, instance.noAttributes());
 instance.removeAttribute(2);
 assertEquals(0, instance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0};
 SparseInstance instance = new SparseInstance(datavector);
 instance.removeAttribute(1);
 assertEquals(3, instance.noAttributes());
 instance.removeAttribute(2);
 assertEquals(2, instance.noAttributes());
 instance.removeAttribute(0);
 assertEquals(1, instance.noAttributes());
 }

 @Test
 public void testToString() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector);
 assertEquals("{" + instance.data.toString() + ";" + instance.classValue + "}", instance.toString());
 }

 @Test
 public void testHashCode() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance1 = new SparseInstance(datavector);
 double[] datavector2 = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance2 = new SparseInstance(datavector2);
 SparseInstance instance3 = new SparseInstance();
 assertEquals(instance1.hashCode(), instance1.hashCode());
 assertEquals(instance1.hashCode(), instance2.hashCode());
 assertNotEquals(instance1.hashCode(), instance3.hashCode());
 }

 @Test
 public void testEquals() {
 double[] datavector1 = new double[]{1.0, 2.0, 3.0};
 double[] datavector2 = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance1 = new SparseInstance(datavector1);
 SparseInstance instance2 = new SparseInstance(datavector2);
 SparseInstance instance3 = new SparseInstance();
 SparseInstance instance4 = new SparseInstance(datavector1);
 assertTrue(instance1.equals(instance1));
 assertTrue(instance1.equals(instance4));
 assertFalse(instance1.equals(instance2));
 assertFalse(instance1.equals(instance3));
 }

 @Test
 public void testCopy() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector);
 SparseInstance copiedInstance = instance.copy();
 assertEquals(instance.noAttributes(), copiedInstance.noAttributes());
 assertEquals(instance.data.size(), copiedInstance.data.size());
 for (Map.Entry<Integer, Double> entry : instance.entrySet()) {
 Double value = copiedInstance.get(entry.getKey());
 assertEquals(entry.getValue(), value, 0.001);
 }
 }

 @Test
 public void testRemoveAttributes() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);
 Set<Integer> indices = new HashSet<>(Arrays.asList(1, 2, 3));
 instance.removeAttributes(indices);
 assertEquals(2, instance.noAttributes());
 assertFalse(instance.containsKey(0));
 assertFalse(instance.containsKey(1));
 assertFalse(instance.containsKey(2));
 }

}