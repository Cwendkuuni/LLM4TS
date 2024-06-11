package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class SparseInstanceTest {

 @Test
 public void testEmptySparseInstance() {
 SparseInstance emptySparseInstance = new SparseInstance();
 assertTrue(emptySparseInstance.isEmpty());
 assertEquals(0, emptySparseInstance.noAttributes());
 }

 @Test
 public void testSettingNoAttributes() {
 SparseInstance testInstance = new SparseInstance(5);
 assertEquals(5, testInstance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeNoAttributes() {
 SparseInstance testInstance = new SparseInstance(-5);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testSettingDefaultValue() {
 SparseInstance testInstance = new SparseInstance(5, 3.0);
 assertEquals(3.0, testInstance.defaultValue, 0.001);
 }

 @Test
 public void testSettingClassValue() {
 SparseInstance testInstance = new SparseInstance(5, 3.0, "ClassValue");
 assertEquals("ClassValue", testInstance.getClassValue());
 }

 @Test
 public void testInitializingWithEmptyDoubleArray() {
 SparseInstance testInstance = new SparseInstance(new double[0]);
 assertTrue(testInstance.isEmpty());
 assertEquals(0, testInstance.noAttributes());
 }

 @Test
 public void testInitializingWithNonEmptyDoubleArray() {
 double[] dataVector = {1.5, 2.0, 3.0};
 SparseInstance testInstance = new SparseInstance(dataVector);
 assertEquals(3, testInstance.noAttributes());
 assertEquals(1.5, testInstance.value(0), 0.001);
 assertEquals(2.0, testInstance.value(1), 0.001);
 assertEquals(3.0, testInstance.value(2), 0.001);
 }

 @Test
 public void testSettingValue() {
 SparseInstance testInstance = new SparseInstance(5);
 testInstance.put(Integer.valueOf(2), Double.valueOf(4.5));
 assertEquals(4.5, testInstance.value(2), 0.001);
 }

 @Test
 public void testPuttingMultipleValues() {
 SparseInstance testInstance = new SparseInstance(5);
 testInstance.put(Integer.valueOf(0), Double.valueOf(1.5));
 testInstance.put(Integer.valueOf(2), Double.valueOf(4.5));
 testInstance.put(Integer.valueOf(4), Double.valueOf(6.5));
 assertEquals(1.5, testInstance.value(0), 0.001);
 assertEquals(4.5, testInstance.value(2), 0.001);
 assertEquals(6.5, testInstance.value(4), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPuttingNullKey() {
 SparseInstance testInstance = new SparseInstance(5);
 try {
 testInstance.put(null, Double.valueOf(4.5));
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPuttingNullValue() {
 SparseInstance testInstance = new SparseInstance(5);
 try {
 testInstance.put(Integer.valueOf(2), null);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testContainsKey() {
 SparseInstance testInstance = new SparseInstance(5);
 testInstance.put(Integer.valueOf(2), Double.valueOf(4.5));
 assertTrue(testInstance.containsKey(Integer.valueOf(2)));
 }

 @Test
 public void testContainsValue() {
 SparseInstance testInstance = new SparseInstance(5);
 testInstance.put(Integer.valueOf(2), Double.valueOf(4.5));
 assertTrue(testInstance.containsValue(Double.valueOf(4.5)));
 }

 @Test
 public void testGet() {
 SparseInstance testInstance = new SparseInstance(5);
 testInstance.put(Integer.valueOf(2), Double.valueOf(4.5));
 assertEquals(4.5, testInstance.get(Integer.valueOf(2)), 0.001);
 }

 @Test
 public void testGetDefaultValue() {
 SparseInstance testInstance = new SparseInstance(5);
 assertEquals(0.0, testInstance.get(Integer.valueOf(2)), 0.001);
 }

 @Test
 public void testClear() {
 SparseInstance testInstance = new SparseInstance(5);
 testInstance.put(Integer.valueOf(2), Double.valueOf(4.5));
 testInstance.clear();
 assertTrue(testInstance.isEmpty());
 assertEquals(0, testInstance.noAttributes());
 }

 @Test
 public void testEntrySet() {
 SparseInstance testInstance = new SparseInstance(5);
 testInstance.put(Integer.valueOf(2), Double.valueOf(4.5));
 Set<Map.Entry<Integer, Double>> entrySet = testInstance.entrySet();
 assertEquals(1, entrySet.size());
 Map.Entry<Integer, Double> entry = entrySet.iterator().next();
 assertEquals(Integer.valueOf(2), entry.getKey());
 assertEquals(Double.valueOf(4.5), entry.getValue());
 }

 @Test
 public void testKeySet() {
 SparseInstance testInstance = new SparseInstance(5);
 testInstance.put(Integer.valueOf(2), Double.valueOf(4.5));
 Set<Integer> keySet = testInstance.keySet();
 assertEquals(1, keySet.size());
 assertTrue(keySet.contains(Integer.valueOf(2)));
 }

 @Test
 public void testValues() {
 SparseInstance testInstance = new SparseInstance(5);
 testInstance.put(Integer.valueOf(2), Double.valueOf(4.5));
 Collection<Double> values = testInstance.values();
 assertEquals(1, values.size());
 assertEquals(Double.valueOf(4.5), values.iterator().next());
 }

 @Test
 public void testRemoveAttribute() {
 SparseInstance testInstance = new SparseInstance(5);
 testInstance.put(Integer.valueOf(2), Double.valueOf(4.5));
 testInstance.removeAttribute(2);
 assertFalse(testInstance.containsKey(Integer.valueOf(2)));
 assertEquals(4, testInstance.noAttributes());
 }

 @Test
 public void testRemoveAttributeWithIndexOutOfBoundsException() {
 SparseInstance testInstance = new SparseInstance(5);
 testInstance.put(Integer.valueOf(2), Double.valueOf(4.5));
 try {
 testInstance.removeAttribute(-1);
 fail("Should throw IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }
}