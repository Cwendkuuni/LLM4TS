package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

public class SparseInstanceTest {

 private static final double DEFAULT_VALUE = 0.0;

 @Test
 public void testConstructorWithNoArguments() {
 SparseInstance sparseInstance = new SparseInstance();
 assertEquals(-1, sparseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithOneArgument() {
 SparseInstance sparseInstance = new SparseInstance(1);
 assertEquals(1, sparseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithTwoArguments() {
 SparseInstance sparseInstance = new SparseInstance(1, DEFAULT_VALUE);
 assertEquals(1, sparseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithThreeArguments() {
 SparseInstance sparseInstance = new SparseInstance(1, DEFAULT_VALUE, "ClassValue");
 assertEquals(1, sparseInstance.noAttributes());
 assertEquals("ClassValue", sparseInstance.getClassValue());
 }

 @Test
 public void testConstructorWithArray() {
 double[] datavector = {1.0, 2.0, 3.0};
 SparseInstance sparseInstance = new SparseInstance(datavector);
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithArrayAndDefaultValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 SparseInstance sparseInstance = new SparseInstance(datavector, DEFAULT_VALUE);
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testConstructorWithArrayAndClassValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 SparseInstance sparseInstance = new SparseInstance(datavector, "ClassValue");
 assertEquals(3, sparseInstance.noAttributes());
 assertEquals("ClassValue", sparseInstance.getClassValue());
 }

 @Test
 public void testConstructorWithArrayDefaultValueAndClassValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 SparseInstance sparseInstance = new SparseInstance(datavector, DEFAULT_VALUE, "ClassValue");
 assertEquals(3, sparseInstance.noAttributes());
 assertEquals("ClassValue", sparseInstance.getClassValue());
 }

 @Test
 public void testSetNoAttributes() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.setNoAttributes(3);
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNoAttributesNegativeValue() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.setNoAttributes(-1);
 }

 @Test
 public void testGetValue() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 assertEquals(1.0, sparseInstance.getValue(0), 0.0);
 }

 @Test
 public void testGetValueDefaultValue() {
 SparseInstance sparseInstance = new SparseInstance();
 assertEquals(DEFAULT_VALUE, sparseInstance.getValue(0), 0.0);
 }

 @Test
 public void testClear() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 sparseInstance.clear();
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testContainsKeyNull() {
 SparseInstance sparseInstance = new SparseInstance();
 assertFalse(sparseInstance.containsKey(null));
 }

 @Test
 public void testContainsKey() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 assertTrue(sparseInstance.containsKey(0));
 }

 @Test
 public void testContainsValueNull() {
 SparseInstance sparseInstance = new SparseInstance();
 assertFalse(sparseInstance.containsValue(null));
 }

 @Test
 public void testContainsValue() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 assertTrue(sparseInstance.containsValue(1.0));
 }

 @Test
 public void testEntrySet() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 Set<Map.Entry<Integer, Double>> entries = sparseInstance.entrySet();
 assertEquals(1, entries.size());
 }

 @Test
 public void testGet() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 assertEquals(1.0, sparseInstance.get(0), 0.0);
 }

 @Test
 public void testGetDefaultValue() {
 SparseInstance sparseInstance = new SparseInstance();
 assertEquals(DEFAULT_VALUE, sparseInstance.get(0), 0.0);
 }

 @Test
 public void testIsEmpty() {
 SparseInstance sparseInstance = new SparseInstance();
 assertTrue(sparseInstance.isEmpty());
 }

 @Test
 public void testIsEmptyAfterPut() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 assertFalse(sparseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 Set<Integer> keys = sparseInstance.keySet();
 assertEquals(1, keys.size());
 }

 @Test
 public void testPut() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 assertEquals(1.0, sparseInstance.get(0), 0.0);
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullKey() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(null, 1.0);
 }

 @Test
 public void testPutNullValue() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(0, null);
 assertEquals(DEFAULT_VALUE, sparseInstance.get(0), 0.0);
 }

 @Test
 public void testPutSameKeyTwice() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 sparseInstance.put(0, 2.0);
 assertEquals(2.0, sparseInstance.get(0), 0.0);
 }

 @Test
 public void testPutAll() {
 SparseInstance sparseInstance = new SparseInstance();
 Map<Integer, Double> map = new HashMap<>();
 map.put(0, 1.0);
 map.put(1, 2.0);
 sparseInstance.putAll(map);
 assertEquals(2, sparseInstance.noAttributes());
 }

 @Test(expected = NullPointerException.class)
 public void testPutAllNullMap() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.putAll(null);
 }

 @Test
 public void testRemove() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 sparseInstance.put(1, 2.0);
 sparseInstance.remove(0);
 assertEquals(1, sparseInstance.noAttributes());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.remove(null);
 }

 @Test
 public void testSize() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 sparseInstance.put(1, 2.0);
 assertEquals(2, sparseInstance.noAttributes());
 }

 @Test(expected = DeprecatedWarning.class)
 public void testSizeDeprecated() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 sparseInstance.put(1, 2.0);
 assertEquals(2, sparseInstance.size());
 }

 @Test
 public void testValues() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 sparseInstance.put(1, 2.0);
 Collection<Double> values = sparseInstance.values();
 assertEquals(2, values.size());
 }

 @Test
 public void testNoAttributes() {
 SparseInstance sparseInstance = new SparseInstance();
 assertEquals(-1, sparseInstance.noAttributes());
 }

 @Test
 public void testNoAttributesAfterInitiate() {
 SparseInstance sparseInstance = new SparseInstance();
 double[] datavector = {1.0, 2.0, 3.0};
 sparseInstance.initiate(datavector);
 assertEquals(3, sparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 sparseInstance.put(1, 2.0);
 sparseInstance.removeAttribute(0);
 assertEquals(1, sparseInstance.noAttributes());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeNegativeIndex() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 sparseInstance.removeAttribute(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeIndexOutOfBounds() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 sparseInstance.put(1, 2.0);
 sparseInstance.removeAttribute(2);
 }

 @Test
 public void testToString() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 sparseInstance.put(1, 2.0);
 assertEquals("{" + sparseInstance.data.toString() + ";" + sparseInstance.getClassValue() + "}", sparseInstance.toString());
 }

 @Test
 public void testHashCode() {
 SparseInstance sparseInstance1 = new SparseInstance();
 SparseInstance sparseInstance2 = new SparseInstance();
 sparseInstance1.put(0, 1.0);
 sparseInstance2.put(0, 1.0);
 assertNotEquals(sparseInstance1.hashCode(), sparseInstance2.hashCode());
 }

 @Test
 public void testEquals() {
 SparseInstance sparseInstance1 = new SparseInstance();
 SparseInstance sparseInstance2 = new SparseInstance();
 sparseInstance1.put(0, 1.0);
 sparseInstance2.put(0, 1.0);
 assertTrue(sparseInstance1.equals(sparseInstance2));
 }

 @Test
 public void testEqualsDifferentClass() {
 SparseInstance sparseInstance = new SparseInstance();
 Object otherInstance = new Object();
 assertFalse(sparseInstance.equals(otherInstance));
 }

 @Test
 public void testEqualsDifferentData() {
 SparseInstance sparseInstance1 = new SparseInstance();
 SparseInstance sparseInstance2 = new SparseInstance();
 sparseInstance1.put(0, 1.0);
 sparseInstance2.put(1, 1.0);
 assertFalse(sparseInstance1.equals(sparseInstance2));
 }

 @Test
 public void testEqualsSameDataDifferentDefaultValue() {
 SparseInstance sparseInstance1 = new SparseInstance();
 SparseInstance sparseInstance2 = new SparseInstance();
 sparseInstance1.put(0, 1.0);
 sparseInstance2.put(0, 2.0);
 assertFalse(sparseInstance1.equals(sparseInstance2));
 }

 @Test
 public void testEqualsSameDataSameDefaultValueDifferentClassValue() {
 SparseInstance sparseInstance1 = new SparseInstance();
 SparseInstance sparseInstance2 = new SparseInstance();
 sparseInstance1.put(0, 1.0);
 sparseInstance2.put(0, 1.0);
 sparseInstance1.setClassValue("ClassValue1");
 sparseInstance2.setClassValue("ClassValue2");
 assertFalse(sparseInstance1.equals(sparseInstance2));
 }

 @Test
 public void testCopy() {
 SparseInstance original = new SparseInstance();
 original.put(0, 1.0);
 original.put(1, 2.0);
 SparseInstance copy = original.copy();
 assertEquals(original.noAttributes(), copy.noAttributes());
 assertEquals(original.data, copy.data);
 assertEquals(original.defaultValue, copy.defaultValue);
 assertEquals(original.classValue, copy.classValue);
 }

 @Test
 public void testRemoveAttributes() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 sparseInstance.put(1, 2.0);
 Set<Integer> indices = new HashSet<>();
 indices.add(0);
 indices.add(1);
 sparseInstance.removeAttributes(indices);
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveAttributesNullIndices() {
 SparseInstance sparseInstance = new SparseInstance();
 sparseInstance.put(0, 1.0);
 sparseInstance.put(1, 2.0);
 sparseInstance.removeAttributes(null);
 }
}