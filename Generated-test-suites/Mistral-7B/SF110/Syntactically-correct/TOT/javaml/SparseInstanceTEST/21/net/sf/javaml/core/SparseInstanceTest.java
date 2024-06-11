package net.sf.javaml.core;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SparseInstanceTest {

 private SparseInstance sampleSparseInstance;
 private SparseInstance emptySparseInstance;
 private SparseInstance nullClassValueInstance;
 private SparseInstance nullKeyInstance;
 private SparseInstance nullValueInstance;

 @Before
 public void setUp() {
 sampleSparseInstance = new SparseInstance(new int[]{1, 2, 3}, 0.1);
 emptySparseInstance = new SparseInstance();
 nullClassValueInstance = new SparseInstance(null);
 nullKeyInstance = new SparseInstance(new int[]{1}, null);
 nullValueInstance = new SparseInstance(new int[]{1, 2, 3}, null);
 }

 @Test
 public void testConstructor() {
 // Test empty constructor
 assertEquals(0, emptySparseInstance.noAttributes());
 assertNull(emptySparseInstance.getClassValue());
 // Test constructor with noAttributes and defaultValue
 assertEquals(0, sampleSparseInstance.noAttributes());
 assertEquals(0.1, sampleSparseInstance.defaultValue, 0.01);
 assertSame(Object.class, sampleSparseInstance.getClassValue());
 // Test constructor with noAttributes, defaultValue, and classValue
 assertEquals(3, sampleSparseInstance.noAttributes());
 assertEquals(0.1, sampleSparseInstance.defaultValue, 0.01);
 assertSame(Object.class, sampleSparseInstance.getClassValue());
 }

 @Test
 public void testInitiate() {
 // Test initiate with double[] datavector and defaultValue
 sampleSparseInstance.initiate(new double[]{1.1, 2.2, 3.3});
 assertEquals(3, sampleSparseInstance.noAttributes());
 assertEquals(1.1, sampleSparseInstance.get(1), 0.01);
 assertEquals(2.2, sampleSparseInstance.get(2), 0.01);
 assertEquals(3.3, sampleSparseInstance.get(3), 0.01);
 assertEquals(0.1, sampleSparseInstance.get(4), 0.01);
 // Test initiate with double[] datavector and null
 sampleSparseInstance.initiate(new double[]{1.1, 2.2, 3.3, null});
 assertEquals(3, sampleSparseInstance.noAttributes());
 assertEquals(1.1, sampleSparseInstance.get(1), 0.01);
 assertEquals(2.2, sampleSparseInstance.get(2), 0.01);
 assertEquals(3.3, sampleSparseInstance.get(3), 0.01);
 assertNull(sampleSparseInstance.get(4));
 }

 @Test
 public void testValue() {
 // Test value with valid index
 assertEquals(1.1, sampleSparseInstance.value(1), 0.01);
 // Test value with invalid index
 assertEquals(0.1, sampleSparseInstance.value(4), 0.01);
 }

 @Test
 public void testClear() {
 // Test clear
 sampleSparseInstance.clear();
 assertEquals(0, sampleSparseInstance.noAttributes());
 assertNull(sampleSparseInstance.getClassValue());
 }

 @Test
 public void testContainsKey() {
 // Test containsKey with valid key
 assertTrue(sampleSparseInstance.containsKey(1));
 // Test containsKey with invalid key
 assertFalse(sampleSparseInstance.containsKey(4));
 }

 @Test
 public void testContainsValue() {
 // Test containsValue with valid value
 assertTrue(sampleSparseInstance.containsValue(1.1));
 // Test containsValue with invalid value
 assertFalse(sampleSparseInstance.containsValue(4.0));
 }

 @Test
 public void testEntrySet() {
 // Test entrySet
 Map.Entry<Integer, Double> entry = sampleSparseInstance.entrySet().iterator().next();
 assertEquals(1, entry.getKey().intValue());
 assertEquals(1.1, entry.getValue(), 0.01);
 }

 @Test
 public void testGet() {
 // Test get with valid key
 assertEquals(1.1, sampleSparseInstance.get(1), 0.01);
 // Test get with invalid key
 assertEquals(0.1, sampleSparseInstance.get(4), 0.01);
 }

 @Test
 public void testIsEmpty() {
 // Test isEmpty with empty instance
 assertTrue(emptySparseInstance.isEmpty());
 // Test isEmpty with non-empty instance
 assertFalse(sampleSparseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 // Test keySet
 List<Integer> keys = new ArrayList<>(sampleSparseInstance.keySet());
 assertEquals(3, keys.size());
 assertTrue(keys.contains(1));
 assertTrue(keys.contains(2));
 assertTrue(keys.contains(3));
 }

 @Test
 public void testPut() {
 // Test put with valid key and value
 sampleSparseInstance.put(2, 4.0);
 assertEquals(4.0, sampleSparseInstance.get(2), 0.01);
 // Test put with existing key and same value
 sampleSparseInstance.put(1, 1.1);
 assertEquals(1.1, sampleSparseInstance.get(1), 0.01);
 // Test put with existing key and different value
 sampleSparseInstance.put(1, 2.2);
 assertEquals(2.2, sampleSparseInstance.get(1), 0.01);
 }

 @Test
 public void testPutAll() {
 // Test putAll with empty map
 Map<Integer, Double> map = new HashMap<>();
 sampleSparseInstance.putAll(map);
 assertEquals(3, sampleSparseInstance.noAttributes());
 // Test putAll with non-empty map
 Map<Integer, Double> map2 = new HashMap<>();
 map2.put(1, 1.1);
 map2.put(2, 2.2);
 sampleSparseInstance.putAll(map2);
 assertEquals(5, sampleSparseInstance.noAttributes());
 assertEquals(1.1, sampleSparseInstance.get(1), 0.01);
 assertEquals(2.2, sampleSparseInstance.get(2), 0.01);
 }

 @Test
 public void testRemove() {
 // Test remove with valid key
 sampleSparseInstance.remove(1);
 assertFalse(sampleSparseInstance.containsKey(1));
 // Test remove with invalid key
 sampleSparseInstance.remove(4);
 assertFalse(sampleSparseInstance.containsKey(4));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveAttributeWithNullKey() {
 nullKeyInstance.removeAttribute(null);
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveAttributeWithNullValue() {
 nullValueInstance.removeAttribute(Collections.emptyList());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveAttributeWithNegativeIndex() {
 sampleSparseInstance.removeAttribute(-1);
 }

 @Test
 public void testNoAttributes() {
 // Test noAttributes with empty instance
 assertEquals(0, emptySparseInstance.noAttributes());
 // Test noAttributes with instance with data
 assertEquals(3, sampleSparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 // Test removeAttribute with valid index
 sampleSparseInstance.removeAttribute(1);
 assertFalse(sampleSparseInstance.containsKey(1));
 // Test removeAttribute with invalid index
 sampleSparseInstance.removeAttribute(4);
 assertFalse(sampleSparseInstance.containsKey(4));
 }

 @Test
 public void testToString() {
 assertEquals("{1.1=1.1, 2.2=2.2, 3.3=3.3;class java.lang.Object}", sampleSparseInstance.toString());
 }

 @Test
 public void testHashCode() {
 // Test hashcode with equal instances
 SparseInstance instance1 = new SparseInstance(new int[]{1, 2, 3}, 0.1);
 SparseInstance instance2 = new SparseInstance(new int[]{1, 2, 3}, 0.1);
 assertEquals(instance1.hashCode(), instance2.hashCode());
 // Test hashcode with different instances
 SparseInstance instance3 = new SparseInstance(new int[]{1, 2, 4}, 0.1);
 assertNotEquals(instance1.hashCode(), instance3.hashCode());
 }

 @Test
 public void testEquals() {
 // Test equals with null object
 assertFalse(sampleSparseInstance.equals(null));
 // Test equals with different class
 assertFalse(sampleSparseInstance.equals(new Object()));
 // Test equals with same instance
 assertTrue(sampleSparseInstance.equals(sampleSparseInstance));
 // Test equals with equal instance
 SparseInstance instance1 = new SparseInstance(new int[]{1, 2, 3}, 0.1);
 assertTrue(sampleSparseInstance.equals(instance1));
 // Test equals with different instance
 SparseInstance instance2 = new SparseInstance(new int[]{1, 2, 4}, 0.1);
 assertFalse(sampleSparseInstance.equals(instance2));
 }

 @Test
 public void testCopy() {
 SparseInstance copiedInstance = sampleSparseInstance.copy();
 assertEquals(sampleSparseInstance.noAttributes(), copiedInstance.noAttributes());
 assertEquals(sampleSparseInstance.defaultValue, copiedInstance.defaultValue, 0.01);
 assertEquals(sampleSparseInstance.getClassValue(), copiedInstance.getClassValue());
 // Test copy with modified original instance
 sampleSparseInstance.put(1, 3.0);
 assertNotEquals(sampleSparseInstance.get(1), copiedInstance.get(1));
 }

 @Test
 public void testRemoveAttributes() {
 // Test removeAttributes with empty set
 sampleSparseInstance.removeAttributes(Collections.emptySet());
 assertEquals(3, sampleSparseInstance.noAttributes());
 // Test removeAttributes with non-empty set
 sampleSparseInstance.removeAttributes(Arrays.asList(1, 2));
 assertEquals(1, sampleSparseInstance.noAttributes());
 // Test removeAttributes with invalid indices
 sampleSparseInstance.removeAttributes(Arrays.asList(-1, 2));
 assertEquals(1, sampleSparseInstance.noAttributes());
 }
}