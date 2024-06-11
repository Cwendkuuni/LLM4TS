package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;

public class SparseInstanceTest {

 private SparseInstance sparseInstance;

 @Test
 public void testSparseInstanceConstructorWithNoArguments() {
 sparseInstance = new SparseInstance();
 assertEquals(0, sparseInstance.noAttributes());
 assertNull(sparseInstance.defaultValue);
 assertEmpty(sparseInstance.data);
 }

 @Test
 public void testSparseInstanceConstructorWithOneArgument() {
 sparseInstance = new SparseInstance(1);
 assertEquals(1, sparseInstance.noAttributes());
 assertNull(sparseInstance.defaultValue);
 assertEmpty(sparseInstance.data);
 }

 @Test
 public void testSparseInstanceConstructorWithTwoArguments() {
 sparseInstance = new SparseInstance(1, 0.0);
 assertEquals(1, sparseInstance.noAttributes());
 assertEquals(0.0, sparseInstance.defaultValue);
 assertEmpty(sparseInstance.data);
 }

 @Test
 public void testSparseInstanceConstructorWithThreeArguments() {
 sparseInstance = new SparseInstance(1, 0.0, "ClassValue");
 assertEquals(1, sparseInstance.noAttributes());
 assertEquals(0.0, sparseInstance.defaultValue);
 assertEquals("ClassValue", sparseInstance.getClassValue());
 assertEmpty(sparseInstance.data);
 }

 @Test
 public void testSparseInstanceConstructorWithDoubleArray() {
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector);
 assertEquals(3, sparseInstance.noAttributes());
 assertNull(sparseInstance.defaultValue);
 assertEquals(datavector, sparseInstance.toArray());
 assertFalse(sparseInstance.isEmpty());
 }

 @Test
 public void testSparseInstanceConstructorWithDoubleArrayAndDefaultValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector, 0.5);
 assertEquals(3, sparseInstance.noAttributes());
 assertEquals(0.5, sparseInstance.defaultValue);
 assertEquals(datavector, sparseInstance.toArray());
 assertFalse(sparseInstance.isEmpty());
 }

 @Test
 public void testSparseInstanceConstructorWithDoubleArrayAndClassValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector, "ClassValue");
 assertEquals(3, sparseInstance.noAttributes());
 assertEquals(datavector, sparseInstance.toArray());
 assertFalse(sparseInstance.isEmpty());
 assertEquals("ClassValue", sparseInstance.getClassValue());
 }

 @Test
 public void testSparseInstanceConstructorWithDoubleArrayAndDefaultValueAndClassValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstance = new SparseInstance(datavector, 0.5, "ClassValue");
 assertEquals(3, sparseInstance.noAttributes());
 assertEquals(0.5, sparseInstance.defaultValue);
 assertEquals(datavector, sparseInstance.toArray());
 assertFalse(sparseInstance.isEmpty());
 assertEquals("ClassValue", sparseInstance.getClassValue());
 }

 @Test
 public void testSetNoAttributes() {
 sparseInstance = new SparseInstance(1, 0.0, "ClassValue");
 sparseInstance.setNoAttributes(3);
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testValue() {
 sparseInstance = new SparseInstance(1, 1.0, "ClassValue");
 assertEquals(1.0, sparseInstance.value(0), 0.0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueWithInvalidIndex() {
 sparseInstance = new SparseInstance(1, 1.0, "ClassValue");
 sparseInstance.value(2);
 }

 @Test
 public void testClear() {
 sparseInstance = new SparseInstance(1, 1.0, "ClassValue");
 sparseInstance.put(0, 2.0);
 sparseInstance.clear();
 assertEmpty(sparseInstance.data);
 }

 @Test
 public void testContainsKey() {
 sparseInstance = new SparseInstance(1, 1.0, "ClassValue");
 sparseInstance.put(0, 2.0);
 assertTrue(sparseInstance.containsKey(0));
 }

 @Test
 public void testContainsValue() {
 sparseInstance = new SparseInstance(1, 1.0, "ClassValue");
 sparseInstance.put(0, 2.0);
 assertTrue(sparseInstance.containsValue(2.0));
 }

 @Test
 public void testEntrySet() {
 sparseInstance = new SparseInstance(1, 1.0, "ClassValue");
 sparseInstance.put(0, 2.0);
 Set<Map.Entry<Integer, Double>> entrySet = sparseInstance.entrySet();
 assertTrue(entrySet.size() > 0);
 }

 @Test
 public void testGet() {
 sparseInstance = new SparseInstance(1, 1.0, "ClassValue");
 sparseInstance.put(0, 2.0);
 assertEquals(2.0, sparseInstance.get(0), 0.0);
 }

 @Test
 public void testGetWithInvalidKey() {
 sparseInstance = new SparseInstance(1, 1.0, "ClassValue");
 sparseInstance.put(0, 2.0);
 assertEquals(sparseInstance.defaultValue, sparseInstance.get(1), 0.0);
 }

 @Test
 public void testIsEmpty() {
 sparseInstance = new SparseInstance(1, 1.0, "ClassValue");
 assertTrue(sparseInstance.isEmpty());
 sparseInstance.put(0, 2.0);
 assertFalse(sparseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 sparseInstance = new SparseInstance(1, 1.0, "ClassValue");
 sparseInstance.put(0, 2.0);
 Set<Integer> keySet = sparseInstance.keySet();
 assertTrue(keySet.size() > 0);
 }

 @Test
 public void testPut() {
 sparseInstance = new SparseInstance(1, 1.0, "ClassValue");
 assertEquals(sparseInstance.defaultValue, sparseInstance.put(0, 2.0), 0.0);
 }

 @Test
 public void testPutAll() {
 Map<Integer, Double> map = new HashMap<>();
 map.put(0, 2.0);
 map.put(1, 3.0);
 sparseInstance = new SparseInstance(1, 1.0, "ClassValue");
 sparseInstance.putAll(map);
 assertEquals(map, sparseInstance.entrySet());
 }

 @Test
 public void testRemove() {
 sparseInstance = new SparseInstance(1, 1.0, "ClassValue");
 sparseInstance.put(0, 2.0);
 Double removedValue = sparseInstance.remove(0);
 assertEquals(2.0, removedValue, 0.0);
 }

 @Test
 public void testSize() {
 sparseInstance = new SparseInstance(1, 1.0, "ClassValue");
 sparseInstance.put(0, 2.0);
 sparseInstance.put(1, 3.0);
 assertEquals(2, sparseInstance.noAttributes());
 }

 @Test
 public void testValues() {
 sparseInstance = new SparseInstance(1, 1.0, "ClassValue");
 sparseInstance.put(0, 2.0);
 sparseInstance.put(1, 3.0);
 List<Double> values = new ArrayList<>(sparseInstance.values());
 assertEquals(2, values.size());
 }

 @Test
 public void testNoAttributes() {
 sparseInstance = new SparseInstance(1, 1.0, "ClassValue");
 sparseInstance.put(0, 2.0);
 sparseInstance.put(1, 3.0);
 assertEquals(2, sparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttributeWithValidIndex() {
 sparseInstance = new SparseInstance(1, 1.0, "ClassValue");
 sparseInstance.put(0, 2.0);
 sparseInstance.put(1, 3.0);
 sparseInstance.removeAttribute(0);
 assertEquals(1, sparseInstance.noAttributes());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeWithInvalidIndex() {
 sparseInstance = new SparseInstance(1, 1.0, "ClassValue");
 sparseInstance.put(0, 2.0);
 sparseInstance.put(1, 3.0);
 sparseInstance.removeAttribute(2);
 }

 @Test
 public void testToString() {
 sparseInstance = new SparseInstance(1, 1.0, "ClassValue");
 sparseInstance.put(0, 2.0);
 sparseInstance.put(1, 3.0);
 assertEquals("{" + sparseInstance.data.toString() + ";" + sparseInstance.getClassValue() + "}", sparseInstance.toString());
 }

 @Test
 public void testHashCode() {
 sparseInstance = new SparseInstance(1, 1.0, "ClassValue");
 sparseInstance.put(0, 2.0);
 sparseInstance.put(1, 3.0);
 SparseInstance other = new SparseInstance(sparseInstance);
 assertEquals(sparseInstance.hashCode(), other.hashCode());
 }

 @Test
 public void testEquals() {
 sparseInstance = new SparseInstance(1, 1.0, "ClassValue");
 sparseInstance.put(0, 2.0);
 sparseInstance.put(1, 3.0);
 SparseInstance other = new SparseInstance();
 assertFalse(sparseInstance.equals(other));
 other.putAll(sparseInstance.entrySet());
 assertTrue(sparseInstance.equals(other));
 }

 @Test
 public void testCopy() {
 sparseInstance = new SparseInstance(1, 1.0, "ClassValue");
 sparseInstance.put(0, 2.0);
 sparseInstance.put(1, 3.0);
 SparseInstance copy = (SparseInstance) sparseInstance.copy();
 assertEquals(sparseInstance, copy);
 }

 @Test
 public void testRemoveAttributesWithEmptySet() {
 sparseInstance = new SparseInstance(1, 1.0, "ClassValue");
 sparseInstance.put(0, 2.0);
 sparseInstance.put(1, 3.0);
 Set<Integer> emptySet = new TreeSet<>();
 sparseInstance.removeAttributes(emptySet);
 assertFalse(sparseInstance.isEmpty());
 }

 @Test
 public void testRemoveAttributesWithValidIndices() {
 sparseInstance = new SparseInstance(1, 1.0, "ClassValue");
 sparseInstance.put(0, 2.0);
 sparseInstance.put(1, 3.0);
 Set<Integer> validIndices = new TreeSet<>();
 validIndices.add(0);
 validIndices.add(1);
 sparseInstance.removeAttributes(validIndices);
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAttributesWithInvalidIndices() {
 sparseInstance = new SparseInstance(1, 1.0, "ClassValue");
 sparseInstance.put(0, 2.0);
 sparseInstance.put(1, 3.0);
 Set<Integer> invalidIndices = new TreeSet<>();
 invalidIndices.add(2);
 sparseInstance.removeAttributes(invalidIndices);
 }
}