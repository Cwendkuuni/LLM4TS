package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

public class SparseInstanceTest {

 private SparseInstance instance;

 @Before
 public void setUp() {
 // Test constructor with default values
 instance = new SparseInstance();
 // Test constructor with specified number of attributes
 instance = new SparseInstance(5);
 // Test constructor with specified number of attributes and default value
 instance = new SparseInstance(5, 0.5);
 // Test constructor with specified number of attributes, default value, and class value
 instance = new SparseInstance(5, 0.5, "TestClass");
 // Test constructor with double array
 double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
 instance = new SparseInstance(datavector);
 // Test constructor with double array, default value, and class value
 instance = new SparseInstance(datavector, 0.5, "TestClass");
 }

 @Test
 public void testSetNoAttributes() {
 // Test setting the number of attributes
 instance.setNoAttributes(10);
 assertEquals(10, instance.noAttributes());
 }

 @Test
 public void testEmptyInstance() {
 // Test an empty instance
 assertTrue(instance.isEmpty());
 assertEquals(0, instance.noAttributes());
 assertEquals(instance.defaultValue, instance.value(0));
 }

 @Test
 public void testSingleAttributeInstance() {
 // Test setting a single attribute
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 assertEquals(1, instance.noAttributes());
 assertEquals(1.0, instance.value(0), 0.0);
 }

 @Test
 public void testMultipleAttributesInstance() {
 // Test setting multiple attributes
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.put(Integer.valueOf(2), Double.valueOf(3.0));
 assertEquals(3, instance.noAttributes());
 assertEquals(1.0, instance.value(0), 0.0);
 assertEquals(2.0, instance.value(1), 0.0);
 assertEquals(3.0, instance.value(2), 0.0);
 }

 @Test
 public void testContainsKey() {
 // Test checking if an instance contains a key
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 assertTrue(instance.containsKey(Integer.valueOf(0)));
 assertTrue(instance.containsKey(Integer.valueOf(1)));
 assertFalse(instance.containsKey(Integer.valueOf(2)));
 }

 @Test
 public void testContainsValue() {
 // Test checking if an instance contains a value
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 assertTrue(instance.containsValue(Double.valueOf(1.0)));
 assertFalse(instance.containsValue(Double.valueOf(3.0)));
 }

 @Test
 public void testEntrySet() {
 // Test getting the entry set of an instance
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 List<Map.Entry<Integer, Double>> entries = new ArrayList<>(instance.entrySet());
 assertEquals(2, entries.size());
 }

 @Test
 public void testGet() {
 // Test getting a value from an instance
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 assertEquals(1.0, instance.get(Integer.valueOf(0)), 0.0);
 assertEquals(2.0, instance.get(Integer.valueOf(1)), 0.0);
 assertEquals(instance.defaultValue, instance.get(Integer.valueOf(2)));
 }

 @Test
 public void testClear() {
 // Test clearing an instance
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.clear();
 assertEquals(0, instance.noAttributes());
 assertFalse(instance.isEmpty());
 }

 @Test
 public void testPutAll() {
 // Test adding multiple attributes at once
 Map<Integer, Double> map = new HashMap<>();
 map.put(Integer.valueOf(3), Double.valueOf(4.0));
 map.put(Integer.valueOf(4), Double.valueOf(5.0));
 instance.putAll(map);
 assertEquals(5, instance.noAttributes());
 assertEquals(4.0, instance.value(3), 0.0);
 assertEquals(5.0, instance.value(4), 0.0);
 }

 @Test
 public void testRemove() {
 // Test removing an attribute
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.put(Integer.valueOf(2), Double.valueOf(3.0));
 instance.remove(Integer.valueOf(1));
 assertEquals(3, instance.noAttributes());
 assertFalse(instance.containsKey(Integer.valueOf(1)));
 }

 @Test(expected =

NullPointerException.class)
 public void testNullKey() {
 // Test trying to add a null key
 instance.put(null, Double.valueOf(1.0));
 }

 @Test(expected =

IllegalArgumentException.class)
 public void testNegativeKey() {
 // Test trying to add a negative key
 instance.put(Integer.valueOf(-1), Double.valueOf(1.0));
 }

 @Test
 public void testSize() {
 // Test getting the size of an instance
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.put(Integer.valueOf(2), Double.valueOf(3.0));
 assertEquals(3, instance.size());
 }

 @Test
 public void testNoAttributes() {
 // Test getting the number of attributes of an instance
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.put(Integer.valueOf(2), Double.valueOf(3.0));
 assertEquals(3, instance.noAttributes());
 instance.clear();
 assertEquals(0, instance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 // Test removing an attribute by index
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.put(Integer.valueOf(2), Double.valueOf(3.0));
 instance.removeAttribute(1);
 assertEquals(2, instance.noAttributes());
 assertFalse(instance.containsKey(Integer.valueOf(1)));
 }

 @Test
 public void testToString() {
 // Test the toString() method
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.put(Integer.valueOf(2), Double.valueOf(3.0));
 assertEquals("{1.0=1.0, 2.0=2.0, TestClass=TestClass}", instance.toString());
 }

 @Test
 public void testHashCode() {
 // Test the hashCode() method
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.put(Integer.valueOf(2), Double.valueOf(3.0));
 SparseInstance other = new SparseInstance();
 other.put(Integer.valueOf(0), Double.valueOf(1.0));
 other.put(Integer.valueOf(1), Double.valueOf(2.0));
 other.put(Integer.valueOf(2), Double.valueOf(3.0));
 assertEquals(instance.hashCode(), other.hashCode());
 }

 @Test
 public void testEquals() {
 // Test the equals() method
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.put(Integer.valueOf(2), Double.valueOf(3.0));
 SparseInstance other = new SparseInstance();
 other.put(Integer.valueOf(0), Double.valueOf(1.0));
 other.put(Integer.valueOf(1), Double.valueOf(2.0));
 other.put(Integer.valueOf(2), Double.valueOf(3.0));
 assertTrue(instance.equals(other));
 other.put(Integer.valueOf(3), Double.valueOf(4.0));
 assertFalse(instance.equals(other));
 }

 @Test
 public void testCopy() {
 // Test the copy() method
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.put(Integer.valueOf(2), Double.valueOf(3.0));
 SparseInstance copiedInstance = instance.copy();
 assertEquals(instance.noAttributes(), copiedInstance.noAttributes());
 assertEquals(instance.defaultValue, copiedInstance.defaultValue);
 for (Map.Entry<Integer, Double> entry : instance.entrySet()) {
 assertEquals(entry.getKey(), copiedInstance.get(entry.getKey()));
 assertEquals(entry.getValue(), copiedInstance.get(entry.getKey()), 0.0);
 }
 }

 @Test
 public void testRemoveAttributes() {
 // Test removing multiple attributes at once
 instance.put(Integer.valueOf(0), Double.valueOf(1.0));
 instance.put(Integer.valueOf(1), Double.valueOf(2.0));
 instance.put(Integer.valueOf(2), Double.valueOf(3.0));
 TreeSet<Integer> indices = new TreeSet<Integer>(Arrays.asList(0, 1));
 instance.removeAttributes(indices);
 assertEquals(1, instance.noAttributes());
 assertFalse(instance.containsKey(Integer.valueOf(0)));
 assertFalse(instance.containsKey(Integer.valueOf(1)));
 }
}