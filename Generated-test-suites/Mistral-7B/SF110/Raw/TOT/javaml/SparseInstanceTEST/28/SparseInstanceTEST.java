package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;

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
 public void testSparseInstanceNoArgs() {
 SparseInstance instance = new SparseInstance();
 assertEquals(0, instance.noAttributes());
 assertNull(instance.getClassValue());
 assertNull(instance.defaultValue);
 }

 // Test constructor with single argument
 @Test
 public void testSparseInstanceSingleArg() {
 int noAttributes = 5;
 SparseInstance instance = new SparseInstance(noAttributes);
 assertEquals(noAttributes, instance.noAttributes());
 assertNull(instance.getClassValue());
 assertNull(instance.defaultValue);
 }

 // Test constructor with three arguments
 @Test
 public void testSparseInstanceThreeArgs() {
 int noAttributes = 5;
 double defaultValue = 1.0;
 Object classValue = new Object();
 SparseInstance instance = new SparseInstance(noAttributes, defaultValue, classValue);
 assertEquals(noAttributes, instance.noAttributes());
 assertEquals(classValue, instance.getClassValue());
 assertEquals(defaultValue, instance.defaultValue);
 }

 // Test constructor with double array
 @Test
 public void testSparseInstanceDoubleArray() {
 int noAttributes = 5;
 double[] datavector = new double[noAttributes];
 double defaultValue = 1.0;
 SparseInstance instance = new SparseInstance(datavector, defaultValue, null);
 assertEquals(noAttributes, instance.noAttributes());
 assertEquals(defaultValue, instance.defaultValue);
 }

 // Test setNoAttributes method
 @Test
 public void testSetNoAttributes() {
 int noAttributes = 5;
 SparseInstance instance = new SparseInstance();
 instance.setNoAttributes(noAttributes);
 assertEquals(noAttributes, instance.noAttributes());
 }

 // Test get method
 @Test
 public void testGet() {
 int noAttributes = 5;
 double[] datavector = new double[noAttributes];
 double defaultValue = 1.0;
 SparseInstance instance = new SparseInstance(datavector, defaultValue, null);
 instance.initiate(datavector);
 for (int i = 0; i < noAttributes; ++i) {
 if (datavector[i] != defaultValue) {
 assertEquals(datavector[i], instance.get(Integer.valueOf(i)));
 }
 }
 assertEquals(defaultValue, instance.get(Integer.valueOf(noAttributes)));
 }

 // Test containsKey method
 @Test
 public void testContainsKey() {
 int noAttributes = 5;
 double[] datavector = new double[noAttributes];
 double defaultValue = 1.0;
 SparseInstance instance = new SparseInstance(datavector, defaultValue, null);
 instance.initiate(datavector);
 for (int i = 0; i < noAttributes; ++i) {
 if (datavector[i] != defaultValue) {
 assertTrue(instance.containsKey(Integer.valueOf(i)));
 }
 }
 assertFalse(instance.containsKey(Integer.valueOf(-1)));
 }

 // Test isEmpty method
 @Test
 public void testIsEmpty() {
 SparseInstance instance = new SparseInstance();
 assertTrue(instance.isEmpty());
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertFalse(instance.isEmpty());
 }

 // Test keySet method
 @Test
 public void testKeySet() {
 int noAttributes = 5;
 double[] datavector = new double[noAttributes];
 double defaultValue = 1.0;
 SparseInstance instance = new SparseInstance(datavector, defaultValue, null);
 instance.initiate(datavector);
 Set<Map.Entry<Integer, Double>> expected = instance.entrySet();
 TreeSet<Integer> keys = new TreeSet<Integer>();
 keys.addAll(instance.keySet());
 assertEquals(expected, keys);
 }

 // Test put method
 @Test
 public void testPut() {
 int noAttributes = 5;
 double defaultValue = 1.0;
 SparseInstance instance = new SparseInstance(-1, defaultValue, null);
 instance.put(Integer.valueOf(0), Double.valueOf(2.0));
 assertEquals(1, instance.noAttributes());
 assertEquals(2.0, instance.get(Integer.valueOf(0)));
 }

 // Test putAll method
 @Test
 public void testPutAll() {
 int noAttributes = 5;
 double defaultValue = 1.0;
 double[] datavector = new double[noAttributes];
 Map<Integer, Double> map = new HashMap<Integer, Double>();
 for (int i = 0; i < noAttributes; ++i) {
 datavector[i] = i + 1.0;
 map.put(Integer.valueOf(i), Double.valueOf(i + 1.0));
 }
 SparseInstance instance = new SparseInstance(-1, defaultValue, null);
 instance.putAll(map);
 assertEquals(noAttributes, instance.noAttributes());
 for (int i = 0; i < noAttributes; ++i) {
 assertEquals(i + 1.0, instance.get(Integer.valueOf(i)));
 }
 }

 // Test remove method
 @Test
 public void testRemove() {
 int noAttributes = 5;
 double defaultValue = 1.0;
 double[] datavector = new double[noAttributes];
 SparseInstance instance = new SparseInstance(datavector, defaultValue, null);
 instance.initiate(datavector);
 double removedValue = instance.get(Integer.valueOf(3));
 instance.remove(Integer.valueOf(3));
 assertEquals(noAttributes - 1, instance.noAttributes());
 assertNotSame(removedValue, instance.get(Integer.valueOf(3)));
 }

 // Test size method
 @Test
 public void testSize() {
 int noAttributes = 5;
 double defaultValue = 1.0;
 double[] datavector = new double[noAttributes];
 SparseInstance instance = new SparseInstance(datavector, defaultValue, null);
 instance.initiate(datavector);
 assertEquals(noAttributes, instance.size());
 instance.remove(Integer.valueOf(0));
 assertEquals(noAttributes - 1, instance.size());
 }

 // Test values method
 @Test
 public void testValues() {
 int noAttributes = 5;
 double defaultValue = 1.0;
 double[] datavector = new double[noAttributes];
 SparseInstance instance = new SparseInstance(datavector, defaultValue, null);
 instance.initiate(datavector);
 List<Double> expected = Arrays.asList(datavector);
 List<Double> values = new ArrayList<Double>(instance.values());
 assertEquals(expected, values);
 }

 // Test noAttributes method
 @Test
 public void testNoAttributes() {
 int noAttributes = 5;
 SparseInstance instance = new SparseInstance();
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(1, instance.noAttributes());
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 assertEquals(2, instance.noAttributes());
 instance.removeAttribute(0);
 assertEquals(1, instance.noAttributes());
 instance.clear();
 assertEquals(0, instance.noAttributes());
 }

 // Test removeAttribute method
 @Test
 public void testRemoveAttribute() {
 int noAttributes = 5;
 double[] datavector = new double[noAttributes];
 double defaultValue = 1.0;
 SparseInstance instance = new SparseInstance(datavector, defaultValue, null);
 instance.initiate(datavector);
 instance.removeAttribute(0);
 assertEquals(noAttributes - 1, instance.noAttributes());
 instance.removeAttribute(noAttributes - 1);
 assertEquals(noAttributes - 2, instance.noAttributes());
 }

 // Test toString method
 @Test
 public void testToString() {
 int noAttributes = 5;
 double[] datavector = new double[noAttributes];
 double defaultValue = 1.0;
 SparseInstance instance = new SparseInstance(datavector, defaultValue, null);
 instance.initiate(datavector);
 String expected = "{" + Arrays.toString(datavector) + ";Object}";
 String toString = instance.toString();
 assertEquals(expected, toString);
 }

 // Test hashCode method
 @Test
 public void testHashCode() {
 int noAttributes = 5;
 double[] datavector = new double[noAttributes];
 double defaultValue = 1.0;
 SparseInstance instance1 = new SparseInstance(datavector, defaultValue, null);
 instance1.initiate(datavector);
 SparseInstance instance2 = new SparseInstance(datavector, defaultValue, null);
 instance2.initiate(datavector);
 assertEquals(instance1.hashCode(), instance2.hashCode());
 }

 // Test equals method
 @Test
 public void testEquals() {
 int noAttributes = 5;
 double[] datavector = new double[noAttributes];
 double defaultValue = 1.0;
 SparseInstance instance1 = new SparseInstance(datavector, defaultValue, null);
 instance1.initiate(datavector);
 SparseInstance instance2 = new SparseInstance(datavector, defaultValue, null);
 instance2.initiate(datavector);
 assertTrue(instance1.equals(instance2));
 }

 // Test copy method
 @Test
 public void testCopy() {
 int noAttributes = 5;
 double[] datavector = new double[noAttributes];
 double defaultValue = 1.0;
 SparseInstance instance = new SparseInstance(datavector, defaultValue, null);
 instance.initiate(datavector);
 SparseInstance copy = instance.copy();
 assertEquals(instance, copy);
 }

 // Test removeAttributes method
 @Test
 public void testRemoveAttributes() {
 int noAttributes = 5;
 double[] datavector = new double[noAttributes];
 double defaultValue = 1.0;
 SparseInstance instance = new SparseInstance(datavector, defaultValue, null);
 Set<Integer> indices = new TreeSet<Integer>(Arrays.asList(2, 3, 4));
 instance.removeAttributes(indices);
 assertEquals(noAttributes - indices.size(), instance.noAttributes());
 }
}