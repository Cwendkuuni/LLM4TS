package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;

import org.junit.Test;

public class SparseInstanceTest {

 private static final double DELTA = 0.001;

 @Test
 public void testConstructorWithNoArguments() {
 SparseInstance instance = new SparseInstance();
 assertEquals(0, instance.noAttributes());
 assertNull(instance.getClassValue());
 }

 @Test
 public void testConstructorWithOneArgument() {
 SparseInstance instance = new SparseInstance(1);
 assertEquals(1, instance.noAttributes());
 assertNull(instance.getClassValue());
 }

 @Test
 public void testConstructorWithTwoArguments() {
 SparseInstance instance = new SparseInstance(1, 0.0);
 assertEquals(1, instance.noAttributes());
 assertNull(instance.getClassValue());
 }

 @Test
 public void testConstructorWithThreeArguments() {
 SparseInstance instance = new SparseInstance(1, 0.0, "TestClass");
 assertEquals(1, instance.noAttributes());
 assertEquals("TestClass", instance.getClassValue());
 }

 @Test
 public void testConstructorWithDoubleArray() {
 double[] datavector = {1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector);
 assertEquals(3, instance.noAttributes());
 assertArrayEquals(datavector, instance.getAttributesAsArray());
 assertNull(instance.getClassValue());
 }

 @Test
 public void testConstructorWithDoubleArrayAndDefaultValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector, 0.5);
 assertEquals(3, instance.noAttributes());
 assertArrayEquals(datavector, instance.getAttributesAsArray());
 assertEquals(0.5, instance.getDefaultValue(), DELTA);
 assertNull(instance.getClassValue());
 }

 @Test
 public void testConstructorWithDoubleArrayAndClassValue() {
 double[] datavector = {1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector, "TestClass");
 assertEquals(3, instance.noAttributes());
 assertArrayEquals(datavector, instance.getAttributesAsArray());
 assertEquals("TestClass", instance.getClassValue());
 }

 @Test
 public void testSetNoAttributes() {
 SparseInstance instance = new SparseInstance();
 instance.setNoAttributes(3);
 assertEquals(3, instance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNoAttributesNegativeValue() {
 SparseInstance instance = new SparseInstance();
 instance.setNoAttributes(-1);
 }

 @Test
 public void testGet() {
 SparseInstance instance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 assertEquals(1.0, instance.get(0), DELTA);
 assertEquals(2.0, instance.get(1), DELTA);
 assertEquals(3.0, instance.get(2), DELTA);
 assertEquals(instance.getDefaultValue(), instance.get(3), DELTA);
 }

 @Test
 public void testGetWithDefaultValue() {
 SparseInstance instance = new SparseInstance();
 assertEquals(instance.getDefaultValue(), instance.get(0), DELTA);
 }

 @Test
 public void testClear() {
 SparseInstance instance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 instance.clear();
 assertEquals(0, instance.noAttributes());
 }

 @Test
 public void testContainsKey() {
 SparseInstance instance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 assertTrue(instance.containsKey(0));
 assertTrue(instance.containsKey(1));
 assertTrue(instance.containsKey(2));
 assertFalse(instance.containsKey(3));
 }

 @Test
 public void testContainsValue() {
 SparseInstance instance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 assertTrue(instance.containsValue(1.0));
 assertTrue(instance.containsValue(2.0));
 assertTrue(instance.containsValue(3.0));
 assertFalse(instance.containsValue(4.0));
 }

 @Test
 public void testEntrySet() {
 SparseInstance instance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 Map.Entry<Integer, Double> entry = instance.entrySet().iterator().next();
 assertEquals(0, entry.getKey().intValue());
 assertEquals(1.0, entry.getValue().doubleValue(), DELTA);
 }

 @Test
 public void testIsEmpty() {
 SparseInstance instance = new SparseInstance();
 assertTrue(instance.isEmpty());
 SparseInstance instance2 = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 assertFalse(instance2.isEmpty());
 }

 @Test
 public void testKeySet() {
 SparseInstance instance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 assertEquals(3, instance.keySet().size());
 TreeSet<Integer> treeSet = new TreeSet<Integer>(instance.keySet());
 assertEquals(Arrays.asList(0, 1, 2), treeSet.stream().map(i -> i.intValue()).collect(Collectors.toList()));
 }

 @Test
 public void testPut() {
 SparseInstance instance = new SparseInstance();
 instance.put(0, Double.valueOf(2.0));
 assertEquals(1, instance.noAttributes());
 assertEquals(2.0, instance.get(0), DELTA);
 }

 @Test
 public void testPutWithExistingKey() {
 SparseInstance instance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 instance.put(1, Double.valueOf(4.0));
 assertEquals(3, instance.noAttributes());
 assertEquals(4.0, instance.get(1), DELTA);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutNullKey() {
 SparseInstance instance = new SparseInstance();
 instance.put(null, Double.valueOf(4.0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutNullValue() {
 SparseInstance instance = new SparseInstance();
 instance.put(0, null);
 }

 @Test
 public void testPutAll() {
 SparseInstance instance = new SparseInstance();
 Map<Integer, Double> map = new HashMap<Integer, Double>();
 map.put(0, Double.valueOf(1.0));
 map.put(1, Double.valueOf(2.0));
 map.put(2, Double.valueOf(3.0));
 instance.putAll(map);
 assertEquals(3, instance.noAttributes());
 assertEquals(1.0, instance.get(0), DELTA);
 assertEquals(2.0, instance.get(1), DELTA);
 assertEquals(3.0, instance.get(2), DELTA);
 }

 @Test
 public void testRemove() {
 SparseInstance instance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 instance.remove(1);
 assertEquals(2, instance.noAttributes());
 assertEquals(1.0, instance.get(0), DELTA);
 assertEquals(3.0, instance.get(1), DELTA);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveNullKey() {
 SparseInstance instance = new SparseInstance();
 instance.remove(null);
 }

 @Test
 public void testSize() {
 SparseInstance instance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 assertEquals(3, instance.size());
 instance.clear();
 assertEquals(0, instance.size());
 }

 @Test
 public void testValues() {
 SparseInstance instance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 List<Double> values = new ArrayList<Double>(instance.values());
 assertEquals(3, values.size());
 assertEquals(Arrays.asList(1.0, 2.0, 3.0), values);
 }

 @Test
 public void testNoAttributes() {
 SparseInstance instance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 assertEquals(3, instance.noAttributes());
 instance.remove(0);
 assertEquals(2, instance.noAttributes());
 instance.remove(1);
 assertEquals(1, instance.noAttributes());
 instance.clear();
 assertEquals(0, instance.noAttributes());
 }

 @Test
 public void testRemoveAttribute() {
 SparseInstance instance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 instance.removeAttribute(1);
 assertEquals(2, instance.noAttributes());
 assertEquals(1.0, instance.get(0), DELTA);
 assertEquals(3.0, instance.get(1), DELTA);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeOutOfBounds() {
 SparseInstance instance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 instance.removeAttribute(4);
 }

 @Test
 public void testToString() {
 SparseInstance instance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 assertEquals("{" + instance.data.toString() + ";" + instance.getClassValue() + "}", instance.toString());
 }

 @Test
 public void testHashCode() {
 SparseInstance instance1 = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 SparseInstance instance2 = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 SparseInstance instance3 = new SparseInstance(Arrays.asList(1.1, 2.1, 3.1).toArray());
 assertEquals(instance1.hashCode(), instance1.hashCode());
 assertEquals(instance1.hashCode(), instance2.hashCode());
 assertNotEquals(instance1.hashCode(), instance3.hashCode());
 }

 @Test
 public void testEquals() {
 SparseInstance instance1 = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 SparseInstance instance2 = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 SparseInstance instance3 = new SparseInstance(Arrays.asList(1.1, 2.1, 3.1).toArray());
 SparseInstance instance4 = instance1.copy();
 assertTrue(instance1.equals(instance1));
 assertTrue(instance1.equals(instance2));
 assertFalse(instance1.equals(instance3));
 assertFalse(instance1.equals(instance4));
 }

 @Test
 public void testCopy() {
 SparseInstance instance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 SparseInstance copy = instance.copy();
 assertEquals(instance.noAttributes(), copy.noAttributes());
 assertEquals(instance.size(), copy.size());
 assertArrayEquals(instance.getAttributesAsArray(), copy.getAttributesAsArray());
 assertEquals(instance.getClassValue(), copy.getClassValue());
 }

 @Test
 public void testRemoveAttributes() {
 SparseInstance instance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0, 4.0).toArray());
 Set<Integer> indices = new HashSet<Integer>(Arrays.asList(1, 2));
 instance.removeAttributes(indices);
 assertEquals(2, instance.noAttributes());
 assertEquals(3.0, instance.get(0), DELTA);
 assertEquals(instance.getDefaultValue(), instance.get(1), DELTA);
 assertEquals(instance.getDefaultValue(), instance.get(2), DELTA);
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveAttributesNullIndices() {
 SparseInstance instance = new SparseInstance(Arrays.asList(1.0, 2.0, 3.0).toArray());
 instance.removeAttributes(null);
 }
}