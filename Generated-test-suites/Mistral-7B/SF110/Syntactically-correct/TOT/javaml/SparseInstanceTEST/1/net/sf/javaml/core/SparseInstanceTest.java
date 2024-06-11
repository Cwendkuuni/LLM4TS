package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

public class SparseInstanceTest {

 // Test constructor with no arguments
 @Test
 public void testSparseInstanceEmpty() {
 SparseInstance instance = new SparseInstance();
 assertEquals(0, instance.noAttributes());
 assertNull(instance.getClassValue());
 assertNotNull(instance.data);
 assertTrue(instance.data.isEmpty());
 }

 // Test constructor with noAttributes
 @Test
 public void testSparseInstanceWithNoAttributes() {
 SparseInstance instance = new SparseInstance(10);
 assertEquals(10, instance.noAttributes());
 assertNull(instance.getClassValue());
 assertNotNull(instance.data);
 assertTrue(instance.data.isEmpty());
 }

 // Test constructor with defaultValue
 @Test
 public void testSparseInstanceWithDefaultValue() {
 SparseInstance instance = new SparseInstance(10, 5.0);
 assertEquals(10, instance.noAttributes());
 assertNull(instance.getClassValue());
 assertNotNull(instance.data);
 assertTrue(instance.data.isEmpty());
 assertEquals(5.0, instance.defaultValue, 0.001);
 }

 // Test constructor with classValue
 @Test
 public void testSparseInstanceWithClassValue() {
 SparseInstance instance = new SparseInstance(10, "ClassValue");
 assertEquals(10, instance.noAttributes());
 assertEquals("ClassValue", instance.getClassValue());
 assertNotNull(instance.data);
 assertTrue(instance.data.isEmpty());
 }

 // Test constructor with datavector
 @Test
 public void testSparseInstanceWithDatavector() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);
 assertEquals(datavector.length, instance.noAttributes());
 assertNull(instance.getClassValue());
 assertNotNull(instance.data);
 assertFalse(instance.data.isEmpty());
 for (int i = 0; i < datavector.length; i++) {
 assertEquals(datavector[i], instance.get(Integer.valueOf(i)), 0.001);
 }
 }

 // Test constructor with datavector, defaultValue, and classValue
 @Test
 public void testSparseInstanceWithDatavectorDefaultValueAndClassValue() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector, 5.0, "ClassValue");
 assertEquals(datavector.length, instance.noAttributes());
 assertEquals("ClassValue", instance.getClassValue());
 assertNotNull(instance.data);
 assertFalse(instance.data.isEmpty());
 for (int i = 0; i < datavector.length; i++) {
 assertEquals(datavector[i], instance.get(Integer.valueOf(i)), 0.001);
 }
 }

 // Test setNoAttributes
 @Test
 public void testSetNoAttributes() {
 SparseInstance instance = new SparseInstance();
 instance.setNoAttributes(5);
 assertEquals(5, instance.noAttributes());
 }

 // Test setNoAttributes with negative value
 @Test(expected = IllegalArgumentException.class)
 public void testSetNoAttributesNegativeValue() {
 SparseInstance instance = new SparseInstance();
 instance.setNoAttributes(-5);
 }

 // Test value
 @Test
 public void testValue() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);
 assertEquals(datavector[0], instance.value(0), 0.001);
 }

 // Test value with out of bounds index
 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueOutOfBoundsIndex() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector);
 instance.value(3);
 }

 // Test clear
 @Test
 public void testClear() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);
 instance.clear();
 assertEquals(0, instance.noAttributes());
 assertNotNull(instance.data);
 assertTrue(instance.data.isEmpty());
 }

 // Test containsKey
 @Test
 public void testContainsKey() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);
 assertTrue(instance.containsKey(Integer.valueOf(0)));
 }

 // Test containsKey with out of bounds index
 @Test(expected = IndexOutOfBoundsException.class)
 public void testContainsKeyOutOfBoundsIndex() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector);
 instance.containsKey(Integer.valueOf(3));
 }

 // Test containsValue
 @Test
 public void testContainsValue() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);
 assertTrue(instance.containsValue(Double.valueOf(1.0)));
 }

 // Test containsValue with non-matching value
 @Test
 public void testContainsValueNonMatchingValue() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);
 assertFalse(instance.containsValue(Double.valueOf(6.0)));
 }

 // Test entrySet
 @Test
 public void testEntrySet() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);
 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 assertEquals(datavector.length, entrySet.size());
 }

 // Test get
 @Test
 public void testGet() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);
 assertEquals(datavector[0], instance.get(Integer.valueOf(0)), 0.001);
 assertEquals(instance.defaultValue, instance.get(Integer.valueOf(4)));
 }

 // Test get with non-existent key
 @Test
 public void testGetNonExistentKey() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);
 assertEquals(instance.defaultValue, instance.get(Integer.valueOf(6)));
 }

 // Test isEmpty
 @Test
 public void testIsEmpty() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);
 assertFalse(instance.isEmpty());
 }

 // Test keySet
 @Test
 public void testKeySet() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);
 Set<Integer> keySet = instance.keySet();
 assertEquals(datavector.length, keySet.size());
 }

 // Test put
 @Test
 public void testPut() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector);
 Double value = Double.valueOf(6.0);
 instance.put(Integer.valueOf(3), value);
 assertEquals(value, instance.get(Integer.valueOf(3)));
 }

 // Test put with existent key
 @Test(expected = IllegalArgumentException.class)
 public void testPutExistentKey() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector);
 Double value = Double.valueOf(6.0);
 instance.put(Integer.valueOf(0), value);
 }

 // Test putAll
 @Test
 public void testPutAll() {
 double[] datavector1 = new double[]{1.0, 2.0, 3.0};
 double[] datavector2 = new double[]{4.0, 5.0, 6.0};
 SparseInstance instance1 = new SparseInstance(datavector1);
 SparseInstance instance2 = new SparseInstance(datavector2);
 Map<Integer, Double> map = new HashMap<>();
 map.putAll(instance2.entrySet());
 instance1.putAll(map);
 assertEquals(datavector1.length + datavector2.length, instance1.noAttributes());
 }

 // Test remove
 @Test
 public void testRemove() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);
 instance.remove(Integer.valueOf(1));
 assertEquals(datavector.length - 1, instance.noAttributes());
 assertEquals(instance.defaultValue, instance.get(Integer.valueOf(1)));
 }

 // Test remove with non-existent key
 @Test
 public void testRemoveNonExistentKey() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);
 instance.remove(Integer.valueOf(6));
 assertEquals(datavector.length, instance.noAttributes());
 assertEquals(instance.defaultValue, instance.get(Integer.valueOf(6)));
 }

 // Test size
 @Deprecated
 @Test
 public void testSize() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);
 assertEquals(datavector.length, instance.size());
 }

 // Test values
 @Test
 public void testValues() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);
 List<Double> values = new ArrayList<>(instance.values());
 assertEquals(datavector.length, values.size());
 for (int i = 0; i < datavector.length; i++) {
 assertEquals(datavector[i], values.get(i), 0.001);
 }
 }

 // Test noAttributes
 @Test
 public void testNoAttributes() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);
 assertEquals(datavector.length, instance.noAttributes());
 }

 // Test noAttributes with empty data
 @Test
 public void testNoAttributesEmptyData() {
 SparseInstance instance = new SparseInstance();
 assertEquals(0, instance.noAttributes());
 }

 // Test removeAttribute
 @Test
 public void testRemoveAttribute() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);
 instance.removeAttribute(2);
 assertEquals(datavector.length - 1, instance.noAttributes());
 assertEquals(instance.defaultValue, instance.get(Integer.valueOf(2)));
 }

 // Test removeAttribute with out of bounds index
 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeOutOfBoundsIndex() {
 double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
 SparseInstance instance = new SparseInstance(datavector);
 instance.removeAttribute(5);
 }
}