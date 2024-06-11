package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class DenseInstanceTest {

 private DenseInstance createDenseInstance(double[] attributes, Object classValue) {
 return new DenseInstance(attributes, classValue);
 }

 @Test
 public void testConstructorWithDoubleArray() {
 DenseInstance denseInstance = createDenseInstance(new double[]{}, null);
 assertFalse(denseInstance.isEmpty());
 assertNotNull(denseInstance.getClassValue());
 }

 @Test
 public void testConstructorWithDoubleArrayAndClassValue() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, "ClassValue");
 assertFalse(denseInstance.isEmpty());
 assertNotNull(denseInstance.getClassValue());
 assertEquals("ClassValue", denseInstance.getClassValue().toString());
 }

 @Test
 public void testConstructorWithInt() {
 DenseInstance denseInstance = createDenseInstance(new double[5], null);
 assertFalse(denseInstance.isEmpty());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testClearMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 denseInstance.clear();
 }

 @Test
 public void testContainsKeyMethodWithInt() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 assertTrue(denseInstance.containsKey(0));
 assertTrue(denseInstance.containsKey(1));
 assertTrue(denseInstance.containsKey(2));
 }

 @Test
 public void testContainsKeyMethodWithNonIntObject() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 assertFalse(denseInstance.containsKey(new Integer(3)));
 }

 @Test
 public void testContainsValueMethodWithDouble() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 assertFalse(denseInstance.containsValue(1.1));
 assertTrue(denseInstance.containsValue(1.0));
 assertTrue(denseInstance.containsValue(2.0));
 assertTrue(denseInstance.containsValue(3.0));
 }

 @Test
 public void testContainsValueMethodWithNaN() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 assertFalse(denseInstance.containsValue(Double.NaN));
 }

 @Test
 public void testEntrySetMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
 assertEquals(3, entrySet.size());
 }

 @Test
 public void testGetMethodWithInt() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 assertEquals(1.0, denseInstance.get(0), 0.0);
 assertEquals(2.0, denseInstance.get(1), 0.0);
 assertEquals(3.0, denseInstance.get(2), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMethodWithOutOfBoundInt() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 denseInstance.get(-1);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetMethodWithNegativeIntIndex() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 denseInstance.get(-2);
 }

 @Test
 public void testIsEmptyMethod() {
 DenseInstance denseInstance = new DenseInstance();
 assertTrue(denseInstance.isEmpty());
 }

 @Test
 public void testKeySetMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 SortedSet<Integer> keySet = denseInstance.keySet();
 assertEquals(3, keySet.size());
 }

 @Test
 public void testPutMethodWithIntAndDouble() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 double oldValue = denseInstance.put(0, 4.0);
 assertEquals(1.0, oldValue, 0.0);
 assertEquals(4.0, denseInstance.get(0), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutMethodWithNegativeIntKey() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 denseInstance.put(-1, 4.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutMethodWithNullKey() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 denseInstance.put(null, 4.0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testPutAllMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Map<Integer, Double> map = new HashMap<>();
 map.put(0, 4.0);
 denseInstance.putAll(map);
 }

 @Test
 public void testRemoveAttributeMethodWithInt() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0, 4.0}, null);
 denseInstance.removeAttribute(2);
 assertEquals(3, denseInstance.noAttributes());
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testRemoveAttributeMethodWithOutOfBoundInt() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 denseInstance.removeAttribute(3);
 }

 @Test
 public void testSizeMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 assertEquals(3, denseInstance.noAttributes());
 }

 @Test
 public void testToStringMethod() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 String toString = denseInstance.toString();
 assertTrue(toString.contains("{"));
 assertTrue(toString.contains(";ClassValue}"));
 }

 @Test
 public void testHashCodeMethod() {
 DenseInstance denseInstance1 = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance2 = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 assertEquals(denseInstance1.hashCode(), denseInstance2.hashCode());
 }

 @Test
 public void testEqualsMethodWithSameObject() {
 DenseInstance denseInstance1 = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance2 = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 assertTrue(denseInstance1.equals(denseInstance2));
 }

 @Test
 public void testEqualsMethodWithDifferentObjects() {
 DenseInstance denseInstance1 = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance2 = new DenseInstance();
 assertFalse(denseInstance1.equals(denseInstance2));
 }

 @Test
 public void testEqualsMethodWithDifferentValues() {
 DenseInstance denseInstance1 = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance2 = createDenseInstance(new double[]{1.0, 2.1, 3.0}, null);
 assertFalse(denseInstance1.equals(denseInstance2));
 }

 @Test
 public void testEqualsMethodWithDifferentClasses() {
 DenseInstance denseInstance1 = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Object other = new Object();
 assertFalse(denseInstance1.equals(other));
 }

 @Test
 public void testCopyMethod() {
 DenseInstance denseInstance1 = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 DenseInstance denseInstance2 = denseInstance1.copy();
 assertEquals(denseInstance1.hashCode(), denseInstance2.hashCode());
 }

 @Test
 public void testRemoveAttributesMethodWithNullSet() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Set<Integer> indices = null;
 denseInstance.removeAttributes(indices);
 }

 @Test
 public void testRemoveAttributesMethodWithEmptySet() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0}, null);
 Set<Integer> indices = new HashSet<>();
 denseInstance.removeAttributes(indices);
 }

 @Test
 public void testRemoveAttributesMethodWithValidSet() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, null);
 Set<Integer> indices = new HashSet<>();
 indices.add(2);
 denseInstance.removeAttributes(indices);
 assertEquals(3, denseInstance.noAttributes());
 }

 @Test
 public void testRemoveAttributesMethodWithValidSetAndDifferentOrder() {
 DenseInstance denseInstance = createDenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, null);
 Set<Integer> indices = new HashSet<>();
 indices.add(4);
 indices.add(0);
 denseInstance.removeAttributes(indices);
 assertEquals(3, denseInstance.noAttributes());
 }
}