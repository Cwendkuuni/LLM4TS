package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class DenseInstanceTest {

 @Test
 public void testDenseInstanceConstruction() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(3, instance.noAttributes());
 assertArrayEquals(new double[]{1.0, 2.0, 3.0}, instance.attributes, 0.001);
 }

 @Test
 public void testDenseInstanceWithClassValue() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue");
 assertEquals("ClassValue", instance.classValue());
 }

 @Test
 public void testDenseInstanceWithEmptyArray() {
 DenseInstance instance = new DenseInstance(new double[]{});
 assertEquals(0, instance.noAttributes());
 assertArrayEquals(new double[0], instance.attributes, 0.001);
 }

 @Test
 public void testDenseInstanceWithNegativeSize() {
 try {
 new DenseInstance(-1);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testValueMethod() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(1.0, instance.value(0), 0.001);
 assertEquals(2.0, instance.value(1), 0.001);
 assertEquals(3.0, instance.value(2), 0.001);
 }

 @Test
 public void testClearMethod() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 instance.clear();
 assertEquals(0, instance.noAttributes());
 assertArrayEquals(new double[0], instance.attributes, 0.001);
 }

 @Test
 public void testContainsKeyMethod() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 assertTrue(instance.containsKey(0));
 assertTrue(instance.containsKey(1));
 assertTrue(instance.containsKey(2));
 assertFalse(instance.containsKey(3));
 }

 @Test
 public void testContainsValueMethod() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 1.0, 2.0, 2.0, 3.0});
 assertTrue(instance.containsValue(1.0));
 assertTrue(instance.containsValue(2.0));
 assertFalse(instance.containsValue(4.0));
 }

 @Test
 public void testEntrySetMethod() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
 assertEquals(3, entries.size());
 for (Map.Entry<Integer, Double> entry : entries) {
 assertEquals(entry.getKey(), entry.getValue().intValue());
 }
 }

 @Test
 public void testGetMethod() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 assertEquals(1.0, instance.get(0), 0.001);
 assertEquals(2.0, instance.get(1), 0.001);
 assertEquals(3.0, instance.get(2), 0.001);
 assertNull(instance.get(3));
 }

 @Test
 public void testIsEmptyMethod() {
 DenseInstance instance = new DenseInstance();
 assertTrue(instance.isEmpty());
 }

 @Test
 public void testKeySetMethod() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 SortedSet<Integer> keys = instance.keySet();
 assertEquals(3, keys.size());
 for (Integer key : keys) {
 assertEquals(key, instance.value(key.intValue()));
 }
 }

 @Test
 public void testPutMethod() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 instance.put(0, 4.0);
 assertEquals(4.0, instance.value(0), 0.001);
 }

 @Test
 public void testPutAllMethod() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 DenseInstance other = new DenseInstance(new double[]{4.0, 5.0, 6.0});
 instance.putAll(other.entrySet());
 assertEquals(6, instance.noAttributes());
 assertArrayEquals(new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0}, instance.attributes, 0.001);
 }

 @Test
 public void testRemoveAttributeMethod() {
 DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 instance.removeAttribute(1);
 assertEquals(2, instance.noAttributes());
 assertArrayEquals(new double[]{1.0, 3.0}, instance.attributes, 0.001);
 }

 @Test
 public void testHashCodeAndEqualsMethods() {
 DenseInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 DenseInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
 DenseInstance instance3 = new DenseInstance(new double[]{2.0, 2.0, 3.0});
 assertEquals(instance1.hashCode(), instance1.hashCode());
 assertEquals(instance1.hashCode(), instance2.hashCode());
 assertFalse(instance1.equals(instance3));
 assertTrue(instance1.equals(instance1));
 assertTrue(instance1.equals(instance2));
 }
}