package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

public class SparseInstanceTest {

 private SparseInstance instance;

 @Before
 public void setup() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0};
 instance = new SparseInstance(datavector, 0.0, "ClassValue");
 }

 @Test
 public void testConstructorWithNoArguments() {
 SparseInstance emptyInstance = new SparseInstance();
 assertEquals(0, emptyInstance.noAttributes());
 assertNull(emptyInstance.getClassValue());
 }

 @Test
 public void testConstructorWithNoAttributes() {
 assertEquals(0, instance.noAttributes());
 }

 @Test
 public void testConstructorWithNoAttributesAndDefaultValue() {
 SparseInstance instanceWithDefaultValue = new SparseInstance(0);
 assertEquals(0, instanceWithDefaultValue.noAttributes());
 }

 @Test
 public void testConstructorWithNoAttributesAndClassValue() {
 SparseInstance instanceWithClassValue = new SparseInstance(0, "ClassValue");
 assertEquals(0, instanceWithClassValue.noAttributes());
 assertEquals("ClassValue", instanceWithClassValue.getClassValue());
 }

 @Test
 public void testConstructorWithNoAttributesAndDefaultValueAndClassValue() {
 SparseInstance instanceWithDefaultValueAndClassValue = new SparseInstance(0, 0.0, "ClassValue");
 assertEquals(0, instanceWithDefaultValueAndClassValue.noAttributes());
 assertEquals("ClassValue", instanceWithDefaultValueAndClassValue.getClassValue());
 }

 @Test
 public void testConstructorWithDatavector() {
 assertEquals(datavector.length, instance.noAttributes());
 }

 @Test
 public void testConstructorWithDatavectorAndDefaultValue() {
 double[] datavectorWithDefaultValue = {1.0, 2.0, 3.0, 4.0, 0.0};
 SparseInstance instanceWithDatavectorAndDefaultValue = new SparseInstance(datavectorWithDefaultValue, 0.0, "ClassValue");
 assertEquals(datavectorWithDefaultValue.length, instanceWithDatavectorAndDefaultValue.noAttributes());
 }

 @Test
 public void testConstructorWithDatavectorAndClassValue() {
 SparseInstance instanceWithDatavectorAndClassValue = new SparseInstance(datavector, "ClassValue");
 assertEquals(datavector.length, instanceWithDatavectorAndClassValue.noAttributes());
 assertEquals("ClassValue", instanceWithDatavectorAndClassValue.getClassValue());
 }

 @Test
 public void testConstructorWithDatavectorAndDefaultValueAndClassValue() {
 SparseInstance instanceWithDatavectorAndDefaultValueAndClassValue = new SparseInstance(datavector, 0.0, "ClassValue");
 assertEquals(datavector.length, instanceWithDatavectorAndDefaultValueAndClassValue.noAttributes());
 assertEquals("ClassValue", instanceWithDatavectorAndDefaultValueAndClassValue.getClassValue());
 }

 @Test
 public void testGetValue() {
 for (int i = 0; i < instance.noAttributes(); i++) {
 assertEquals(instance.value(i), instance.get(i), 0.001);
 }
 }

 @Test
 public void testClear() {
 instance.clear();
 assertEquals(0, instance.noAttributes());
 }

 @Test
 public void testContainsKey() {
 for (int i = 0; i < instance.noAttributes(); i++) {
 assertTrue(instance.containsKey(Integer.valueOf(i)));
 }
 }

 @Test
 public void testContainsValue() {
 for (int i = 0; i < instance.noAttributes(); i++) {
 assertTrue(instance.containsValue(Double.valueOf(instance.get(i))));
 }
 }

 @Test
 public void testEntrySet() {
 Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
 assertEquals(instance.noAttributes(), entries.size());
 }

 @Test
 public void testGet() {
 for (int i = 0; i < instance.noAttributes(); i++) {
 assertEquals(instance.get(Integer.valueOf(i)), instance.get(i), 0.001);
 }
 }

 @Test
 public void testIsEmpty() {
 instance.clear();
 assertTrue(instance.isEmpty());
 }

 @Test
 public void testKeySet() {
 Set<Integer> keys = instance.keySet();
 assertEquals(instance.noAttributes(), keys.size());
 }

 @Test
 public void testPut() {
 Map<Integer, Double> newData = new HashMap<>();
 newData.put(1, 5.0);
 instance.putAll(newData);
 assertEquals(instance.noAttributes() + 1, instance.noAttributes());
 assertEquals(5.0, instance.get(1), 0.001);
 }

 @Test
 public void testPutAll() {
 Map<Integer, Double> newData = new HashMap<>();
 newData.put(1, 5.0);
 newData.put(2, 6.0);
 instance.putAll(newData);
 assertEquals(instance.noAttributes() + 2, instance.noAttributes());
 assertEquals(5.0, instance.get(1), 0.001);
 assertEquals(6.0, instance.get(2), 0.001);
 }

 @Test
 public void testRemove() {
 instance.removeAttribute(0);
 assertEquals(instance.noAttributes() - 1, instance.noAttributes());
 assertFalse(instance.containsKey(Integer.valueOf(0)));
 }

 @Test
 public void testSize() {
 assertEquals(instance.noAttributes(), instance.size());
 }

 @Test
 public void testValues() {
 Collection<Double> values = instance.values();
 List<Double> valuesList = new ArrayList<>(values);
 assertEquals(instance.noAttributes(), valuesList.size());
 }

 @Test
 public void testNoAttributes() {
 assertEquals(instance.noAttributes(), instance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 instance.removeAttribute(0);
 assertEquals(instance.noAttributes() - 1, instance.noAttributes());
 assertFalse(instance.containsKey(Integer.valueOf(0)));
 }

 @Test
 public void testToString() {
 String expectedToString = "{1.0=[1.0], 2.0=[2.0], 3.0=[3.0], 4.0=[4.0];ClassValue}";
 assertEquals(expectedToString, instance.toString());
 }

 @Test
 public void testHashCode() {
 SparseInstance instance1 = new SparseInstance(datavector, 0.0, "ClassValue");
 SparseInstance instance2 = new SparseInstance(datavector, 0.0, "ClassValue");
 assertEquals(instance.hashCode(), instance1.hashCode());
 }

 @Test
 public void testEquals() {
 SparseInstance instance1 = new SparseInstance(datavector, 0.0, "ClassValue");
 SparseInstance instance2 = new SparseInstance(datavector, 0.0, "ClassValue");
 assertTrue(instance.equals(instance1));
 }

 @Test
 public void testCopy() {
 SparseInstance copiedInstance = instance.copy();
 assertEquals(instance.noAttributes(), copiedInstance.noAttributes());
 }

 @Test
 public void testRemoveAttributes() {
 Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(0, 1));
 instance.removeAttributes(indicesToRemove);
 assertEquals(instance.noAttributes() - 2, instance.noAttributes());
 }
}