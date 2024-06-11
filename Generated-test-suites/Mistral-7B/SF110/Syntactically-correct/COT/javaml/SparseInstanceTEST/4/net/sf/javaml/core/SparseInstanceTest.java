package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.junit.Test;

public class SparseInstanceTest {

 private static final double DEFAULT_VALUE = 0.0;

 @Test
 public void testConstructorWithNoArguments() {
 SparseInstance instance = new SparseInstance();
 assertEquals(-1, instance.noAttributes());
 }

 @Test
 public void testConstructorWithNoAttributes() {
 SparseInstance instance = new SparseInstance(0);
 assertEquals(0, instance.noAttributes());
 }

 @Test
 public void testConstructorWithNoAttributesAndDefaultValue() {
 SparseInstance instance = new SparseInstance(0, DEFAULT_VALUE);
 assertEquals(0, instance.noAttributes());
 }

 @Test
 public void testConstructorWithNoAttributesAndClassValue() {
 SparseInstance instance = new SparseInstance(0, "ClassValue");
 assertEquals("ClassValue", instance.classValue());
 }

 @Test
 public void testConstructorWithNoAttributesDefaultValueAndClassValue() {
 SparseInstance instance = new SparseInstance(0, DEFAULT_VALUE, "ClassValue");
 assertEquals("ClassValue", instance.classValue());
 }

 @Test
 public void testConstructorWithDatavector() {
 double[] datavector = {1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector);
 assertEquals(3, instance.noAttributes());
 }

 @Test
 public void testConstructorWithDatavectorDefaultValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector, DEFAULT_VALUE);
 assertEquals(3, instance.noAttributes());
 }

 @Test
 public void testConstructorWithDatavectorAndClassValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector, "ClassValue");
 assertEquals("ClassValue", instance.classValue());
 }

 @Test
 public void testConstructorWithDatavectorDefaultValueAndClassValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector, DEFAULT_VALUE, "ClassValue");
 assertEquals("ClassValue", instance.classValue());
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
 public void testGetValue() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(1.0, instance.value(0), 0.001);
 assertEquals(3.0, instance.value(2), 0.001);
 }

 @Test
 public void testClear() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 instance.clear();
 assertEquals(0, instance.noAttributes());
 }

 @Test
 public void testContainsKeyTrue() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertTrue(instance.containsKey(Integer.valueOf(0)));
 }

 @Test
 public void testContainsKeyFalse() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertFalse(instance.containsKey(Integer.valueOf(4)));
 }

 @Test
 public void testEntrySet() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 Map<Integer, Double> expectedMap = new HashMap<>();
 expectedMap.put(0, 1.0);
 expectedMap.put(1, 2.0);
 expectedMap.put(2, 3.0);
 TreeSet<Map.Entry<Integer, Double>> entrySet = new TreeSet<>(instance.entrySet());
 assertEquals(expectedMap, entrySet);
 }

 @Test
 public void testGetDefaultValue() {
 SparseInstance instance = new SparseInstance();
 assertEquals(instance.getDefaultValue(), DEFAULT_VALUE, 0.001);
 }

 @Test
 public void testGetWithEmptyInstance() {
 SparseInstance instance = new SparseInstance();
 assertEquals(DEFAULT_VALUE, instance.get(0), 0.001);
 }

 @Test
 public void testGetWithKeyInInstance() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(1.0, instance.get(0), 0.001);
 }

 @Test
 public void testIsEmptyTrue() {
 SparseInstance instance = new SparseInstance();
 assertTrue(instance.isEmpty());
 }

 @Test
 public void testIsEmptyFalse() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertFalse(instance.isEmpty());
 }

 @Test
 public void testKeySet() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 TreeSet<Integer> expectedSet = new TreeSet<>();
 expectedSet.add(0);
 expectedSet.add(1);
 expectedSet.add(2);
 TreeSet<Integer> keySet = new TreeSet<>(instance.keySet());
 assertEquals(expectedSet, keySet);
 }

 @Test
 public void testPut() {
 SparseInstance instance = new SparseInstance();
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 assertEquals(2, instance.noAttributes());
 }

 @Test
 public void testPutAll() {
 SparseInstance instance = new SparseInstance();
 Map<Integer, Double> map = new HashMap<>();
 map.put(Integer.valueOf(0), Double.valueOf(1.0));
 map.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.putAll(map);
 assertEquals(2, instance.noAttributes());
 }

 @Test
 public void testRemove() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 instance.remove(Integer.valueOf(1));
 assertEquals(2, instance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveKeyNotFound() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 instance.remove(Integer.valueOf(4));
 }

 @Test
 public void testSize() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(3, instance.size());
 }

 @Test
 public void testValues() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 List<Double> expectedList = new ArrayList<>();
 expectedList.add(1.0);
 expectedList.add(2.0);
 expectedList.add(3.0);
 List<Double> values = new ArrayList<>(instance.values());
 assertEquals(expectedList, values);
 }

 @Test
 public void testNoAttributes() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(3, instance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 instance.removeAttribute(1);
 assertEquals(2, instance.noAttributes());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeIndexOutOfBounds() {
 SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
 instance.removeAttribute(-1);
 }
}