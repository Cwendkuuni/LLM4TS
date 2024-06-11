package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class DenseInstanceTest {

 // Test constructor with valid input
 @Test
 public void testDenseInstanceValidConstructor() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(3, instance.noAttributes());
 assertArrayEquals(new double[]{1.0, 2.0, 3.0}, instance.attributes, 0.001);
 }

 // Test value method with valid index
 @Test
 public void testValueValidIndex() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(1.0, instance.value(0), 0.001);
 }

 // Test value method with out of bounds index
 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testValueOutOfBoundsIndex() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 instance.value(3);
 }

 // Test clear method
 @Test
 public void testClear() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 instance.clear();
 assertEquals(0, instance.noAttributes());
 }

 // Test containsKey method with valid index
 @Test
 public void testContainsKeyValidIndex() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 assertTrue(instance.containsKey(0));
 assertTrue(instance.containsKey(1));
 assertTrue(instance.containsKey(2));
 }

 // Test containsKey method with out of bounds index
 @Test
 public void testContainsKeyOutOfBoundsIndex() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 assertFalse(instance.containsKey(3));
 }

 // Test containsValue method with valid value
 @Test
 public void testContainsValueValidValue() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 assertTrue(instance.containsValue(1.0));
 }

 // Test containsValue method with out of bounds value
 @Test
 public void testContainsValueOutOfBoundsValue() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 assertFalse(instance.containsValue(4.0));
 }

 // Test entrySet method
 @Test
 public void testEntrySet() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 assertEquals(3, entrySet.size());
 }

 // Test get method with valid index
 @Test
 public void testGetValidIndex() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(1.0, instance.get(0), 0.001);
 }

 // Test get method with out of bounds index
 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetOutOfBoundsIndex() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 instance.get(3);
 }

 // Test isEmpty method
 @Test
 public void testIsEmpty() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 assertFalse(instance.isEmpty());
 DenseInstance emptyInstance = new DenseInstance();
 assertTrue(emptyInstance.isEmpty());
 }

 // Test keySet method
 @Test
 public void testKeySet() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 SortedSet<Integer> keySet = instance.keySet();
 assertEquals(3, keySet.size());
 }

 // Test put method with valid input
 @Test
 public void testPutValidInput() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 instance.put(0, 10.0);
 assertEquals(10.0, instance.attributes[0], 0.001);
 }

 // Test put method with same key
 @Test(expected = UnsupportedOperationException.class)
 public void testPutSameKey() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 instance.put(0, 10.0);
 instance.put(0, 20.0);
 }

 // Test putAll method with valid input
 @Test
 public void testPutAllValidInput() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 Map<Integer, Double> inputMap = new HashMap<>();
 inputMap.put(0, 10.0);
 inputMap.put(1, 20.0);
 instance.putAll(inputMap);
 assertArrayEquals(new double[]{1.0, 20.0, 3.0}, instance.attributes, 0.001);
 }

 // Test remove method with valid index
 @Test
 public void testRemoveValidIndex() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 double removedValue = instance.removeAttribute(1);
 assertEquals(2.0, removedValue, 0.001);
 assertEquals(2, instance.noAttributes());
 }

 // Test remove method with out of bounds index
 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testRemoveOutOfBoundsIndex() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 instance.removeAttribute(3);
 }
}