package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class SparseInstanceTest {

 private SparseInstance createInstance(int noAttributes, Double... data) {
  return new SparseInstance(noAttributes, data != null ? data : new Double[0]);
 }

 @Test
 public void testConstructorWithNoAttributes() {
  SparseInstance instance = new SparseInstance();
  assertEquals(0, instance.noAttributes());
 }

 @Test
 public void testConstructorWithNoAttributesAndData() {
  SparseInstance instance = new SparseInstance(10, new Double[]{1.0, 2.0, 3.0, 4.0, 5.0});
  assertEquals(5, instance.noAttributes());
  assertEquals(1.0, instance.get(0), 0.001);
  assertEquals(2.0, instance.get(1), 0.001);
  assertEquals(3.0, instance.get(2), 0.001);
  assertEquals(4.0, instance.get(3), 0.001);
  assertEquals(5.0, instance.get(4), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNegativeNoAttributes() {
  new SparseInstance(-1);
 }

 @Test
 public void testSetNoAttributes() {
  SparseInstance instance = new SparseInstance();
  instance.setNoAttributes(5);
  assertEquals(5, instance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNoAttributesWithNegativeValue() {
  SparseInstance instance = new SparseInstance();
  instance.setNoAttributes(-5);
 }

 @Test
 public void testGetValue() {
  SparseInstance instance = new SparseInstance(new Double[]{1.0, 2.0, 3.0, 4.0, 5.0});
  assertEquals(1.0, instance.value(0), 0.001);
  assertEquals(2.0, instance.value(1), 0.001);
  assertEquals(3.0, instance.value(2), 0.001);
  assertEquals(4.0, instance.value(3), 0.001);
  assertEquals(5.0, instance.value(4), 0.001);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueWithNegativeIndex() {
  SparseInstance instance = new SparseInstance(new Double[]{1.0, 2.0, 3.0, 4.0, 5.0});
  instance.get(-1);
 }

 @Test
 public void testGetValueWithDefaultValue() {
  SparseInstance instance = new SparseInstance();
  assertEquals(0.0, instance.value(0), 0.001);
  assertEquals(0.0, instance.value(1), 0.001);
  assertEquals(0.0, instance.value(2), 0.001);
  // ...
  assertEquals(0.0, instance.value(100), 0.001);
 }

 @Test
 public void testClear() {
  SparseInstance instance = new SparseInstance(new Double[]{1.0, 2.0, 3.0, 4.0, 5.0});
  instance.clear();
  assertTrue(instance.isEmpty());
 }

 @Test
 public void testContainsKey() {
  SparseInstance instance = new SparseInstance(new Double[]{1.0, 2.0, 3.0, 4.0, 5.0});
  assertTrue(instance.containsKey(0));
  assertTrue(instance.containsKey(1));
  assertTrue(instance.containsKey(2));
  // ...
  assertTrue(instance.containsKey(4));
  assertFalse(instance.containsKey(5));
 }

 @Test
 public void testContainsValue() {
  SparseInstance instance = new SparseInstance(new Double[]{1.0, 2.0, 3.0, 4.0, 5.0});
  assertTrue(instance.containsValue(1.0));
  assertTrue(instance.containsValue(2.0));
  assertTrue(instance.containsValue(3.0));
  assertTrue(instance.containsValue(4.0));
  assertTrue(instance.containsValue(5.0));
 }

 @Test
 public void testEntrySet() {
  SparseInstance instance = new SparseInstance(new Double[]{1.0, 2.0, 3.0, 4.0, 5.0});
  Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
  assertEquals(5, entries.size());
  // ...
  Map.Entry<Integer, Double> entry = entries.iterator().next();
  assertEquals(0, entry.getKey().intValue());
  assertEquals(1.0, entry.getValue().doubleValue(), 0.001);
 }

 @Test(expected = NullPointerException.class)
 public void testGetWithNullKey() {
  SparseInstance instance = new SparseInstance();
  instance.get(null);
 }

 @Test
 public void testPut() {
  SparseInstance instance = new SparseInstance();
  instance.put(Integer.valueOf(0), Double.valueOf(1.0));
  assertEquals(1.0, instance.get(0), 0.001);
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
  SparseInstance instance = new SparseInstance();
  instance.put(null, Double.valueOf(1.0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithDuplicateKey() {
  SparseInstance instance = new SparseInstance();
  instance.put(Integer.valueOf(0), Double.valueOf(1.0));
  instance.put(Integer.valueOf(0), Double.valueOf(2.0));
 }

 @Test
 public void testPutAll() {
  SparseInstance instance = new SparseInstance();
  HashMap<Integer, Double> map = new HashMap<Integer, Double>();
  map.put(Integer.valueOf(0), Double.valueOf(1.0));
  map.put(Integer.valueOf(1), Double.valueOf(2.0));
  map.put(Integer.valueOf(2), Double.valueOf(3.0));
  instance.putAll(map);
  assertEquals(3, instance.noAttributes());
  assertEquals(1.0, instance.get(0), 0.001);
  assertEquals(2.0, instance.get(1), 0.001);
  assertEquals(3.0, instance.get(2), 0.001);
 }

 @Test(expected = NullPointerException.class)
 public void testPutAllWithNullMap() {
  SparseInstance instance = new SparseInstance();
  instance.putAll(null);
 }

 @Test
 public void testRemove() {
  SparseInstance instance = new SparseInstance(new Double[]{1.0, 2.0, 3.0, 4.0, 5.0});
  Double removedValue = instance.remove(0);
  assertEquals(1.0, removedValue.doubleValue(), 0.001);
  assertEquals(4, instance.noAttributes());
  assertFalse(instance.containsKey(0));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
  SparseInstance instance = new SparseInstance(new Double[]{1.0, 2.0, 3.0, 4.0, 5.0});
  instance.remove(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveWithNonExistentKey() {
  SparseInstance instance = new SparseInstance(new Double[]{1.0, 2.0, 3.0, 4.0, 5.0});
  instance.remove(10);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitiateWithNullDatavector() {
  new SparseInstance(null);
 }

 @Test
 public void testInitiate() {
  Double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
  SparseInstance instance = new SparseInstance(datavector);
  assertEquals(5, instance.noAttributes());
  assertEquals(1.0, instance.get(0), 0.001);
  assertEquals(2.0, instance.get(1), 0.001);
  assertEquals(3.0, instance.get(2), 0.001);
  assertEquals(4.0, instance.get(3), 0.001);
  assertEquals(5.0, instance.get(4), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitiateWithDefaultValueAndNullDatavector() {
  new SparseInstance(0.0, null);
 }

 @Test
 public void testInitiateWithDefaultValue() {
  Double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
  SparseInstance instance = new SparseInstance(datavector, 0.1);
  assertEquals(5, instance.noAttributes());
  assertEquals(0.1, instance.get(0), 0.001);
  assertEquals(0.1, instance.get(1), 0.001);
  assertEquals(0.1, instance.get(2), 0.001);
  assertEquals(0.1, instance.get(3), 0.001);
  assertEquals(0.1, instance.get(4), 0.001);
 }

 @Test
 public void testToString() {
  Double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
  String expected = "{1.0=1.0, 2.0=2.0, 3.0=3.0, 4.0=4.0, 5.0=5.0,}";
  SparseInstance instance = new SparseInstance(datavector);
  assertEquals(expected, instance.toString());
 }

 @Test
 public void testHashCode() {
  SparseInstance instance1 = new SparseInstance(new Double[]{1.0, 2.0, 3.0, 4.0, 5.0});
  SparseInstance instance2 = new SparseInstance(new Double[]{1.0, 2.0, 3.0, 4.0, 5.0});
  SparseInstance instance3 = new SparseInstance(new Double[]{1.1, 2.0, 3.0, 4.0, 5.0});
  assertEquals(hashCode(instance1), hashCode(instance2));
  assertNotEquals(hashCode(instance1), hashCode(instance3));
 }

 @Test
 public void testEquals() {
  SparseInstance instance1 = new SparseInstance(new Double[]{1.0, 2.0, 3.0, 4.0, 5.0});
  SparseInstance instance2 = new SparseInstance(new Double[]{1.0, 2.0, 3.0, 4.0, 5.0});
  SparseInstance instance3 = new SparseInstance(new Double[]{1.1, 2.0, 3.0, 4.0, 5.0});
  SparseInstance instance4 = instance1.copy();
  assertTrue(instance1.equals(instance2));
  assertTrue(instance1.equals(instance4));
  assertFalse(instance1.equals(instance3));
 }

 @Test
 public void testCopy() {
  Double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
  SparseInstance instance = new SparseInstance(datavector);
  SparseInstance copy = instance.copy();
  assertEquals(instance.noAttributes(), copy.noAttributes());
  assertEquals(instance.getClassValue(), copy.getClassValue());
  for (int i = 0; i < instance.noAttributes(); ++i) {
   assertEquals(instance.get(i), copy.get(i), 0.001);
  }
 }

 @Test
 public void testRemoveAttributesWithEmptyInstance() {
  SparseInstance instance = new SparseInstance();
  Set<Integer> indices = new HashSet<Integer>(Arrays.asList(0, 1, 2, 3, 4));
  instance.removeAttributes(indices);
  assertTrue(instance.isEmpty());
 }

 @Test
 public void testRemoveAttributes() {
  Double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
  SparseInstance instance = new SparseInstance(datavector);
  Set<Integer> indices = new HashSet<Integer>(Arrays.asList(1, 3));
  instance.removeAttributes(indices);
  assertEquals(3, instance.noAttributes());
  assertFalse(instance.containsKey(1));
  assertFalse(instance.containsKey(3));
  assertEquals(1.0, instance.get(0), 0.001);
  assertEquals(4.0, instance.get(2), 0.001);
  assertEquals(5.0, instance.get(4), 0.001);
 }

 @Test
 public void testRemoveAttributesWithOverlappingIndices() {
  Double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
  SparseInstance instance = new SparseInstance(datavector);
  Set<Integer> indices1 = new HashSet<Integer>(Arrays.asList(1, 3));
  Set<Integer> indices2 = new HashSet<Integer>(Arrays.asList(2, 3));
  instance.removeAttributes(indices1);
  instance.removeAttributes(indices2);
  assertEquals(3, instance.noAttributes());
  assertFalse(instance.containsKey(1));
  assertFalse(instance.containsKey(2));
  assertFalse(instance.containsKey(3));
  assertEquals(1.0, instance.get(0), 0.001);
  assertEquals(4.0, instance.get(4), 0.001);
 }

 private int hashCode(SparseInstance instance) {
  return instance.hashCode();
 }

}