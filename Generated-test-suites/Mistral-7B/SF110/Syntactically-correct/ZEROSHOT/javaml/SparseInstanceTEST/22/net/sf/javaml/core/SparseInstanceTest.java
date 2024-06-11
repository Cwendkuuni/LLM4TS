package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class SparseInstanceTest {

 private SparseInstance sparseInstance;

 @Before
 public void setUp() {
 sparseInstance = new SparseInstance(new int[] { 1, 2, 3, 4 }, 0.0, "TestClass");
 }

 @Test
 public void testSparseInstanceWithDefaultConstructor() {
 SparseInstance instance = new SparseInstance();
 assertEquals(-1, instance.noAttributes());
 assertNull(instance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithNoAttributesConstructor() {
 assertEquals(0, sparseInstance.noAttributes());
 assertNotNull(sparseInstance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithNoAttributesAndDefaultValueConstructor() {
 SparseInstance instance = new SparseInstance(5);
 assertEquals(5, instance.noAttributes());
 assertNull(instance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithNoAttributesAndClassValueConstructor() {
 SparseInstance instance = new SparseInstance(5, "TestClass");
 assertEquals(5, instance.noAttributes());
 assertNotNull(instance.getClassValue());
 assertEqual("TestClass", instance.getClassValue().toString());
 }

 @Test
 public void testSparseInstanceWithNoAttributesAndDefaultValueAndClassValueConstructor() {
 SparseInstance instance = new SparseInstance(5, 0.0, "TestClass");
 assertEquals(5, instance.noAttributes());
 assertNull(instance.getClassValue());
 assertEqual("TestClass", instance.getClassValue().toString());
 }

 @Test
 public void testSparseInstanceWithDoubleArrayConstructor() {
 assertEquals(4, sparseInstance.noAttributes());
 assertNotNull(sparseInstance.getClassValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSparseInstanceWithNullDoubleArrayConstructor() {
 new SparseInstance(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSparseInstanceWithEmptyDoubleArrayConstructor() {
 new SparseInstance(new double[0]);
 }

 @Test
 public void testSparseInstanceWithDoubleArrayAndDefaultValueConstructor() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 }, 0.0);
 assertEquals(4, instance.noAttributes());
 assertNull(instance.getClassValue());
 }

 @Test
 public void testSparseInstanceWithDoubleArrayAndClassValueConstructor() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 }, "TestClass");
 assertEquals(4, instance.noAttributes());
 assertNotNull(instance.getClassValue());
 assertEqual("TestClass", instance.getClassValue().toString());
 }

 @Test
 public void testSparseInstanceWithDoubleArrayAndDefaultValueAndClassValueConstructor() {
 SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0, 4.0 }, 0.0, "TestClass");
 assertEquals(4, instance.noAttributes());
 assertNull(instance.getClassValue());
 assertEqual("TestClass", instance.getClassValue().toString());
 }

 @Test
 public void testValueMethod() {
 assertEquals(1.0, sparseInstance.value(0), 0.0);
 assertEquals(2.0, sparseInstance.value(1), 0.0);
 assertEquals(3.0, sparseInstance.value(2), 0.0);
 assertEquals(4.0, sparseInstance.value(3), 0.0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueMethodWithInvalidIndex() {
 sparseInstance.value(-1);
 }

 @Test
 public void testClearMethod() {
 sparseInstance.clear();
 assertEquals(0, sparseInstance.noAttributes());
 }

 @Test
 public void testContainsKeyMethod() {
 assertTrue(sparseInstance.containsKey(0));
 assertTrue(sparseInstance.containsKey(1));
 assertTrue(sparseInstance.containsKey(2));
 assertTrue(sparseInstance.containsKey(3));
 assertFalse(sparseInstance.containsKey(4));
 }

 @Test
 public void testContainsValueMethod() {
 assertTrue(sparseInstance.containsValue(1.0));
 assertTrue(sparseInstance.containsValue(2.0));
 assertTrue(sparseInstance.containsValue(3.0));
 assertTrue(sparseInstance.containsValue(4.0));
 }

 @Test
 public void testEntrySetMethod() {
 Set<Map.Entry<Integer, Double>> entrySet = sparseInstance.entrySet();
 assertEquals(4, entrySet.size());
 }

 @Test
 public void testGetMethod() {
 assertEquals(1.0, sparseInstance.get(0), 0.0);
 assertEquals(2.0, sparseInstance.get(1), 0.0);
 assertEquals(3.0, sparseInstance.get(2), 0.0);
 assertEquals(4.0, sparseInstance.get(3), 0.0);
 assertEquals(0.0, sparseInstance.get(-1), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMethodWithInvalidKey() {
 sparseInstance.get(4);
 }

 @Test
 public void testIsEmptyMethod() {
 assertFalse(sparseInstance.isEmpty());
 }

 @Test
 public void testKeySetMethod() {
 Set<Integer> keySet = sparseInstance.keySet();
 assertEquals(4, keySet.size());
 }

 @Test
 public void testPutMethod() {
 SparseInstance instance = new SparseInstance();
 instance.put(Integer.valueOf(5), Double.valueOf(5.0));
 assertEquals(1, instance.noAttributes());
 assertEquals(5.0, instance.get(5), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutMethodWithInvalidKey() {
 sparseInstance.put(Integer.valueOf(-1), Double.valueOf(5.0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutMethodWithNullKey() {
 sparseInstance.put(null, Double.valueOf(5.0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutMethodWithNullValue() {
 sparseInstance.put(Integer.valueOf(1), null);
 }

 @Test
 public void testPutAllMethod() {
 SparseInstance instance = new SparseInstance();
 Map<Integer, Double> map = new HashMap<>();
 map.put(Integer.valueOf(5), Double.valueOf(5.0));
 instance.putAll(map);
 assertEquals(1, instance.noAttributes());
 assertEquals(5.0, instance.get(5), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutAllMethodWithNullMap() {
 sparseInstance.putAll(null);
 }

 @Test
 public void testRemoveMethod() {
 SparseInstance instance = new SparseInstance();
 instance.put(Integer.valueOf(5), Double.valueOf(5.0));
 instance.remove(Integer.valueOf(5));
 assertEquals(0, instance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveMethodWithInvalidKey() {
 sparseInstance.remove(Integer.valueOf(-1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveMethodWithNullKey() {
 sparseInstance.remove(null);
 }

 @Test
 public void testSizeMethod() {
 assertEquals(4, sparseInstance.size());
 }

 @Test
 public void testValuesMethod() {
 Collection<Double> values = sparseInstance.values();
 assertEquals(4, values.size());
 }

 @Test
 public void testNoAttributesMethod() {
 assertEquals(4, sparseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttributeMethod() {
 SparseInstance instance = new SparseInstance();
 instance.put(Integer.valueOf(5), Double.valueOf(5.0));
 instance.removeAttribute(5);
 assertEquals(0, instance.noAttributes());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeMethodWithInvalidIndex() {
 sparseInstance.removeAttribute(-1);
 }

 @Test
 public void testToStringMethod() {
 assertEquals("{" + sparseInstance.data.toString() + ";" + sparseInstance.getClassValue() + "}", sparseInstance.toString());
 }

 @Test
 public void testHashCodeMethod() {
 SparseInstance instance1 = new SparseInstance();
 instance1.put(Integer.valueOf(5), Double.valueOf(5.0));
 SparseInstance instance2 = new SparseInstance();
 instance2.put(Integer.valueOf(5), Double.valueOf(5.0));
 assertEquals(instance1.hashCode(), instance2.hashCode());
 }

 @Test
 public void testEqualsMethodWithSameInstance() {
 assertTrue(sparseInstance.equals(sparseInstance));
 }

 @Test
 public void testEqualsMethodWithNullObject() {
 assertFalse(sparseInstance.equals(null));
 }

 @Test
 public void testEqualsMethodWithDifferentClass() {
 assertFalse(sparseInstance.equals(new String("Test")));
 }

 @Test
 public void testEqualsMethodWithDifferentData() {
 SparseInstance instance1 = new SparseInstance();
 instance1.put(Integer.valueOf(5), Double.valueOf(5.0));
 SparseInstance instance2 = new SparseInstance();
 assertFalse(instance1.equals(instance2));
 }

 @Test
 public void testEqualsMethodWithDifferentDefaultValue() {
 SparseInstance instance1 = new SparseInstance(new int[] { 1, 2, 3, 4 }, 0.0, "TestClass");
 SparseInstance instance2 = new SparseInstance(new int[] { 1, 2, 3, 4 }, 1.0, "TestClass");
 assertFalse(instance1.equals(instance2));
 }

 @Test
 public void testEqualsMethodWithSameDataAndClassValueButDifferentInstance() {
 SparseInstance instance1 = new SparseInstance();
 instance1.put(Integer.valueOf(5), Double.valueOf(5.0));
 SparseInstance instance2 = new SparseInstance();
 instance2.put(Integer.valueOf(5), Double.valueOf(5.0));
 assertFalse(instance1.equals(instance2));
 }

 @Test
 public void testCopyMethod() {
 SparseInstance instance1 = new SparseInstance();
 instance1.put(Integer.valueOf(5), Double.valueOf(5.0));
 SparseInstance instance2 = instance1.copy();
 assertEquals(1, instance1.noAttributes());
 assertEquals(1, instance2.noAttributes());
 assertEquals(5.0, instance1.get(5), 0.0);
 assertEquals(5.0, instance2.get(5), 0.0);
 }

 @Test
 public void testRemoveAttributesMethod() {
 Set<Integer> indices = new HashSet<>();
 indices.add(1);
 indices.add(3);
 sparseInstance.removeAttributes(indices);
 assertEquals(2, sparseInstance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveAttributesMethodWithNullSet() {
 sparseInstance.removeAttributes(null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributesMethodWithInvalidIndex() {
 sparseInstance.removeAttribute(-1);
 }
}