package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.junit.Test;

public class SparseInstanceTest {

 private static final double DEFAULT_VALUE = 0.0;
 private static final int NUM_ATTRIBUTES = 5;

 @Test
 public void testEmptyConstructor() {
 SparseInstance instance = new SparseInstance();
 assertEquals(Instance.class, instance.getClass());
 assertEquals(-1, instance.noAttributes());
 assertTrue(instance.isEmpty());
 }

 @Test
 public void testConstructorWithNoAttributes() {
 SparseInstance instance = new SparseInstance(NUM_ATTRIBUTES);
 assertEquals(Instance.class, instance.getClass());
 assertEquals(NUM_ATTRIBUTES, instance.noAttributes());
 assertTrue(instance.isEmpty());
 }

 @Test
 public void testConstructorWithDefaultValue() {
 SparseInstance instance = new SparseInstance(NUM_ATTRIBUTES, DEFAULT_VALUE);
 assertEquals(Instance.class, instance.getClass());
 assertEquals(NUM_ATTRIBUTES, instance.noAttributes());
 assertTrue(instance.isEmpty());
 }

 @Test
 public void testConstructorWithClassValue() {
 SparseInstance instance = new SparseInstance(NUM_ATTRIBUTES, "classValue");
 assertEquals(Instance.class, instance.getClass());
 assertEquals("classValue", instance.classValue());
 assertEquals(NUM_ATTRIBUTES, instance.noAttributes());
 assertTrue(instance.isEmpty());
 }

 @Test
 public void testConstructorWithDataVector() {
 double[] datavector = new double[NUM_ATTRIBUTES];
 SparseInstance instance = new SparseInstance(datavector);
 assertEquals(Instance.class, instance.getClass());
 assertEquals(NUM_ATTRIBUTES, instance.noAttributes());
 assertFalse(instance.isEmpty());
 for (int i = 0; i < datavector.length; i++) {
 assertEquals(datavector[i], instance.value(i), 0.0);
 }
 }

 @Test
 public void testConstructorWithDefaultValueAndDataVector() {
 double[] datavector = new double[NUM_ATTRIBUTES];
 SparseInstance instance = new SparseInstance(datavector, DEFAULT_VALUE);
 assertEquals(Instance.class, instance.getClass());
 assertEquals(NUM_ATTRIBUTES, instance.noAttributes());
 assertFalse(instance.isEmpty());
 for (int i = 0; i < datavector.length; i++) {
 if (datavector[i] != DEFAULT_VALUE) {
 assertEquals(datavector[i], instance.value(i), 0.0);
 } else {
 assertEquals(DEFAULT_VALUE, instance.value(i), 0.0);
 }
 }
 }

 @Test
 public void testConstructorWithClassValueAndDataVector() {
 double[] datavector = new double[NUM_ATTRIBUTES];
 SparseInstance instance = new SparseInstance(datavector, "classValue");
 assertEquals(Instance.class, instance.getClass());
 assertEquals("classValue", instance.classValue());
 assertEquals(NUM_ATTRIBUTES, instance.noAttributes());
 assertFalse(instance.isEmpty());
 for (int i = 0; i < datavector.length; i++) {
 if (datavector[i] != DEFAULT_VALUE) {
 assertEquals(datavector[i], instance.value(i), 0.0);
 } else {
 assertEquals(DEFAULT_VALUE, instance.value(i), 0.0);
 }
 }
 }

 @Test
 public void testSetNoAttributes() {
 SparseInstance instance = new SparseInstance(NUM_ATTRIBUTES);
 instance.setNoAttributes(NUM_ATTRIBUTES * 2);
 assertEquals(NUM_ATTRIBUTES * 2, instance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNoAttributesNegative() {
 SparseInstance instance = new SparseInstance(NUM_ATTRIBUTES);
 instance.setNoAttributes(-1);
 }

 @Test
 public void testValue() {
 SparseInstance instance = new SparseInstance(NUM_ATTRIBUTES);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 assertEquals(1.0, instance.value(0), 0.0);
 assertEquals(2.0, instance.value(1), 0.0);
 assertEquals(DEFAULT_VALUE, instance.value(-1), 0.0);
 }

 @Test
 public void testClear() {
 SparseInstance instance = new SparseInstance(NUM_Attributes);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.clear();
 assertTrue(instance.isEmpty());
 }

 @Test
 public void testContainsKey() {
 SparseInstance instance = new SparseInstance(NUM_ATTRIBUTES);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 assertTrue(instance.containsKey(Integer.valueOf(0)));
 assertTrue(instance.containsKey(Integer.valueOf(1)));
 assertFalse(instance.containsKey(Integer.valueOf(-1)));
 }

 @Test
 public void testContainsValue() {
 SparseInstance instance = new SparseInstance(NUM_ATTRIBUTES);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(0), Double.valueOf(2.0));
 assertTrue(instance.containsValue(Double.valueOf(1.0)));
 assertTrue(instance.containsValue(Double.valueOf(2.0)));
 }

 @Test
 public void testEntrySet() {
 SparseInstance instance = new SparseInstance(NUM_ATTRIBUTES);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 List<Map.Entry<Integer, Double>> entries = new ArrayList<>(instance.entrySet());
 assertEquals(2, entries.size());
 assertEquals(Integer.valueOf(0), entries.get(0).getKey());
 assertEquals(Double.valueOf(1.0), entries.get(0).getValue());
 assertEquals(Integer.valueOf(1), entries.get(1).getKey());
 assertEquals(Double.valueOf(2.0), entries.get(1).getValue());
 }

 @Test
 public void testGet() {
 SparseInstance instance = new SparseInstance(NUM_ATTRIBUTES);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 assertEquals(1.0, instance.get(Integer.valueOf(0)), 0.0);
 assertEquals(2.0, instance.get(Integer.valueOf(1)), 0.0);
 assertEquals(DEFAULT_VALUE, instance.get(Integer.valueOf(-1)), 0.0);
 }

 @Test
 public void testIsEmpty() {
 SparseInstance instance = new SparseInstance();
 assertTrue(instance.isEmpty());
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertFalse(instance.isEmpty());
 }

 @Test
 public void testKeySet() {
 SparseInstance instance = new SparseInstance(NUM_ATTRIBUTES);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 TreeSet<Integer> keys = new TreeSet<Integer>(instance.keySet());
 assertEquals(2, keys.size());
 assertEquals(Integer.valueOf(0), keys.first());
 assertEquals(Integer.valueOf(1), keys.last());
 }

 @Test
 public void testPut() {
 SparseInstance instance = new SparseInstance();
 assertEquals(DEFAULT_VALUE, instance.put(Integer.valueOf(0), Double.valueOf(1.0)), DEFAULT_VALUE);
 assertEquals(1.0, instance.value(0), 0.0);
 }

 @Test
 public void testPutAll() {
 SparseInstance instance = new SparseInstance();
 Map<Integer, Double> map = new HashMap<Integer, Double>();
 map.put(Integer.valueOf(0), Double.valueOf(1.0));
 map.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.putAll(map);
 assertEquals(2, instance.noAttributes());
 assertEquals(1.0, instance.value(0), 0.0);
 assertEquals(2.0, instance.value(1), 0.0);
 }

 @Test
 public void testRemove() {
 SparseInstance instance = new SparseInstance(NUM_ATTRIBUTES);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 assertEquals(Double.valueOf(1.0), instance.remove(Integer.valueOf(0)), Double.valueOf(1.0));
 assertEquals(1.0, instance.noAttributes());
 assertFalse(instance.containsKey(Integer.valueOf(0)));
 }

 @Test
 public void testSize() {
 SparseInstance instance = new SparseInstance(NUM_ATTRIBUTES);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 assertEquals(2, instance.noAttributes());
 }

 @Test
 public void testValues() {
 SparseInstance instance = new SparseInstance(NUM_ATTRIBUTES);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 List<Double> values = new ArrayList<Double>(instance.values());
 assertEquals(2, values.size());
 assertEquals(1.0, values.get(0), 0.0);
 assertEquals(2.0, values.get(1), 0.0);
 }

 @Test
 public void testNoAttributes() {
 SparseInstance instance = new SparseInstance();
 assertEquals(-1, instance.noAttributes());
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(1, instance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 SparseInstance instance = new SparseInstance(NUM_ATTRIBUTES);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.removeAttribute(0);
 assertEquals(1, instance.noAttributes());
 assertFalse(instance.containsKey(Integer.valueOf(0)));
 }

 @Test
 public void testToString() {
 SparseInstance instance = new SparseInstance(NUM_ATTRIBUTES);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 String expected = "{1.0=1.0, 2.0=2.0;" + instance.classValue() + "}";
 assertEquals(expected, instance.toString());
 }

 @Test
 public void testHashCode() {
 SparseInstance instance1 = new SparseInstance(NUM_ATTRIBUTES);
 instance1.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance1.put(Integer.valueOf(1), Double.valueOf(2.0));
 SparseInstance instance2 = new SparseInstance(NUM_ATTRIBUTES);
 instance2.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance2.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance2.setClassValue(instance1.classValue());
 assertEquals(instance1.hashCode(), instance2.hashCode());
 }

 @Test
 public void testEquals() {
 SparseInstance instance1 = new SparseInstance(NUM_ATTRIBUTES);
 instance1.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance1.put(Integer.valueOf(1), Double.valueOf(2.0));
 SparseInstance instance2 = new SparseInstance(NUM_ATTRIBUTES);
 instance2.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance2.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance2.setClassValue(instance1.classValue());
 assertTrue(instance1.equals(instance2));
 SparseInstance instance3 = new SparseInstance(NUM_ATTRIBUTES);
 instance3.put(Integer.valueOf(0), Double.valueOf(1.1));
 instance3.put(Integer.valueOf(1), Double.valueOf(2.1));
 assertFalse(instance1.equals(instance3));
 }

 @Test
 public void testCopy() {
 SparseInstance instance = new SparseInstance(NUM_ATTRIBUTES);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 SparseInstance copy = instance.copy();
 assertEquals(instance.noAttributes(), copy.noAttributes());
 assertEquals(instance.getClassValue(), copy.getClassValue());
 for (Map.Entry<Integer, Double> entry : instance.entrySet()) {
 assertEquals(entry.getKey(), copy.get(entry.getKey()));
 assertEquals(entry.getValue(), copy.get(entry.getKey()), 0.0);
 }
 }

 @Test
 public void testRemoveAttributes() {
 SparseInstance instance = new SparseInstance(NUM_ATTRIBUTES);
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 Set<Integer> indices = new TreeSet<Integer>(Arrays.asList(0, 1));
 instance.removeAttributes(indices);
 assertEquals(0, instance.noAttributes());
 }
}