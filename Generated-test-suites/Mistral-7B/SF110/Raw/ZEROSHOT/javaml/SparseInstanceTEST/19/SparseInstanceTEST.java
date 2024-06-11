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

 private static final double DEFAULT_VALUE = 0.0;
 private static final Object CLASS_VALUE = "ClassValue";

 @Test
 public void testConstructorWithNoArguments() {
 SparseInstance sparseInstance = new SparseInstance();
 assertEquals(-1, sparseInstance.noAttributes());
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testConstructorWithNoAttributesAndDefaultValue() {
 SparseInstance sparseInstance = new SparseInstance(0);
 assertEquals(0, sparseInstance.noAttributes());
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testConstructorWithNoAttributesAndDefaultValueAndClassValue() {
 SparseInstance sparseInstance = new SparseInstance(0, CLASS_VALUE);
 assertEquals(0, sparseInstance.noAttributes());
 assertEquals(CLASS_VALUE, sparseInstance.getClassValue());
 }

 @Test
 public void testConstructorWithNoAttributesAndDefaultValueAndClassValueAndDataVector() {
 double[] datavector = new double[0];
 SparseInstance sparseInstance = new SparseInstance(datavector);
 assertEquals(0, sparseInstance.noAttributes());
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testConstructorWithNoAttributesAndDefaultValueAndClassValueAndDataVectorAndPutData() {
 double[] datavector = new double[1];
 datavector[0] = 1.0;
 SparseInstance sparseInstance = new SparseInstance(datavector);
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
 assertEquals(1, sparseInstance.noAttributes());
 assertFalse(sparseInstance.isEmpty());
 assertEquals(2.0, sparseInstance.get(0), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNegativeNumberOfAttributes() {
 new SparseInstance(-1);
 }

 @Test
 public void testSetNoAttributes() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.setNoAttributes(5);
 assertEquals(5, sparseInstance.noAttributes());
 }

 @Test
 public void testClear() {
 SparseInstance sparseInstance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 sparseInstance.clear();
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testContainsKey() {
 SparseInstance sparseInstance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 assertTrue(sparseInstance.containsKey(Integer.valueOf(1)));
 assertFalse(sparseInstance.containsKey(Integer.valueOf(4)));
 }

 @Test
 public void testContainsValue() {
 SparseInstance sparseInstance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 assertTrue(sparseInstance.containsValue(Double.valueOf(2.0)));
 assertFalse(sparseInstance.containsValue(Double.valueOf(4.0)));
 }

 @Test
 public void testEntrySet() {
 SparseInstance sparseInstance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 Set<Map.Entry<Integer, Double>> entrySet = sparseInstance.entrySet();
 assertEquals(3, entrySet.size());
 }

 @Test
 public void testGet() {
 SparseInstance sparseInstance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 assertEquals(1.0, sparseInstance.get(Integer.valueOf(0)), 0.001);
 assertEquals(DEFAULT_VALUE, sparseInstance.get(Integer.valueOf(1)));
 }

 @Test
 public void testIsEmpty() {
 SparseInstance sparseInstance = new SparseInstance();
 assertTrue(sparseInstance.isEmpty());
 SparseInstance sparseInstanceWithData = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 assertFalse(sparseInstanceWithData.isEmpty());
 }

 @Test
 public void testKeySet() {
 SparseInstance sparseInstance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 Set<Integer> keySet = sparseInstance.keySet();
 assertEquals(3, keySet.size());
 }

 @Test
 public void testPut() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(1, sparseInstance.noAttributes());
 assertFalse(sparseInstance.isEmpty());
 assertEquals(1.0, sparseInstance.get(Integer.valueOf(0)), 0.001);
 }

 @Test
 public void testPutAll() {
 SparseInstance sparseInstance = new SparseInstance();
 Map<Integer, Double> map = new HashMap<>();
 map.put(Integer.valueOf(0), Double.valueOf(1.0));
 map.put(Integer.valueOf(1), Double.valueOf(2.0));
 sparseInstance.putAll(map);
 assertEquals(2, sparseInstance.noAttributes());
 assertFalse(sparseInstance.isEmpty());
 assertEquals(1.0, sparseInstance.get(Integer.valueOf(0)), 0.001);
 assertEquals(2.0, sparseInstance.get(Integer.valueOf(1)), 0.001);
 }

 @Test
 public void testRemove() {
 SparseInstance sparseInstance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 sparseInstance.remove(Integer.valueOf(1));
 assertEquals(2, sparseInstance.noAttributes());
 assertFalse(sparseInstance.isEmpty());
 assertFalse(sparseInstance.containsKey(Integer.valueOf(1)));
 }

 @Test
 public void testSize() {
 SparseInstance sparseInstance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 assertEquals(3, sparseInstance.size());
 SparseInstance sparseInstanceWithEmptyData = new SparseInstance();
 assertEquals(0, sparseInstanceWithEmptyData.size());
 }

 @Test
 public void testValues() {
 SparseInstance sparseInstance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 List<Double> values = new ArrayList<>(sparseInstance.values());
 assertEquals(3, values.size());
 assertEquals(1.0, values.get(0), 0.001);
 assertEquals(DEFAULT_VALUE, values.get(1), 0.001);
 assertEquals(3.0, values.get(2), 0.001);
 }

 @Test
 public void testNoAttributes() {
 SparseInstance sparseInstance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 assertEquals(3, sparseInstance.noAttributes());
 SparseInstance sparseInstanceWithEmptyData = new SparseInstance();
 assertEquals(0, sparseInstanceWithEmptyData.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 SparseInstance sparseInstance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 sparseInstance.removeAttribute(1);
 assertEquals(2, sparseInstance.noAttributes());
 assertFalse(sparseInstance.containsKey(Integer.valueOf(1)));
 }

 @Test
 public void testToString() {
 SparseInstance sparseInstance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 assertEquals("{" + sparseInstance.data.toString() + ";" + CLASS_VALUE + "}", sparseInstance.toString());
 }

 @Test
 public void testHashCode() {
 SparseInstance sparseInstance1 = new SparseInstance(Arrays.asList(1.0, 2.0).toArray());
 SparseInstance sparseInstance2 = new SparseInstance(Arrays.asList(1.0, 2.0).toArray());
 SparseInstance sparseInstance3 = new SparseInstance(Arrays.asList(1.0, 3.0).toArray());
 assertEquals(sparseInstance1.hashCode(), sparseInstance1.hashCode());
 assertEquals(sparseInstance1.hashCode(), sparseInstance2.hashCode());
 assertNotEquals(sparseInstance1.hashCode(), sparseInstance3.hashCode());
 }

 @Test
 public void testEquals() {
 SparseInstance sparseInstance1 = new SparseInstance(Arrays.asList(1.0, 2.0).toArray());
 SparseInstance sparseInstance2 = new SparseInstance(Arrays.asList(1.0, 2.0).toArray());
 SparseInstance sparseInstance3 = new SparseInstance(Arrays.asList(1.0, 3.0).toArray());
 SparseInstance sparseInstance4 = new SparseInstance(Arrays.asList(1.0, 2.0).toArray());
 SparseInstance sparseInstance5 = new SparseInstance(Arrays.asList(1.0, 2.0).toArray());
 SparseInstance sparseInstance6 = new SparseInstance();
 assertTrue(sparseInstance1.equals(sparseInstance1));
 assertTrue(sparseInstance1.equals(sparseInstance2));
 assertFalse(sparseInstance1.equals(sparseInstance3));
 assertFalse(sparseInstance1.equals(sparseInstance4));
 assertFalse(sparseInstance1.equals(sparseInstance5));
 assertFalse(sparseInstance1.equals(sparseInstance6));
 }

 @Test
 public void testCopy() {
 SparseInstance sparseInstance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 Instance copy = sparseInstance.copy();
 assertEquals(sparseInstance.noAttributes(), copy.noAttributes());
 assertEquals(sparseInstance.size(), copy.size());
 for (Map.Entry<Integer, Double> entry : sparseInstance.entrySet()) {
 assertEquals(entry.getKey(), copy.get(entry.getKey()));
 assertEquals(entry.getValue(), copy.get(entry.getKey()), 0.001);
 }
 }

 @Test
 public void testRemoveAttributes() {
 SparseInstance sparseInstance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0).toArray());
 Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(2, 4));
 sparseInstance.removeAttributes(indicesToRemove);
 assertEquals(3, sparseInstance.noAttributes());
 assertFalse(sparseInstance.containsKey(Integer.valueOf(2)));
 assertFalse(sparseInstance.containsKey(Integer.valueOf(4)));
 }
}