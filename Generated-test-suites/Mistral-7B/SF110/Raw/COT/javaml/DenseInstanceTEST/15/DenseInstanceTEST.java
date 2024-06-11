package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import net.sf.javaml.core.DenseInstance;

public class DenseInstanceTest {

 private DenseInstance createInstance(double[] attributes, Object classValue) {
 return new DenseInstance(attributes, classValue);
 }

 @Test
 public void testConstructorWithArray() {
 DenseInstance instance = createInstance(new double[] {1.0, 2.0, 3.0}, null);
 assertEquals(3, instance.noAttributes());
 assertArrayEquals(new double[] {1.0, 2.0, 3.0}, instance.attributes, 0.0);
 }

 @Test
 public void testConstructorWithArrayAndClassValue() {
 DenseInstance instance = createInstance(new double[] {1.0, 2.0, 3.0}, "ClassValue");
 assertEquals("ClassValue", instance.classValue());
 }

 @Test
 public void testConstructorWithSize() {
 DenseInstance instance = new DenseInstance(5);
 assertEquals(5, instance.noAttributes());
 assertArrayEquals(new double[5], instance.attributes, 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNegativeSize() {
 new DenseInstance(-5);
 }

 @Test
 public void testValueMethod() {
 DenseInstance instance = createInstance(new double[] {1.0, 2.0, 3.0}, null);
 assertEquals(1.0, instance.value(0), 0.0);
 assertEquals(2.0, instance.value(1), 0.0);
 assertEquals(3.0, instance.value(2), 0.0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueMethodWithInvalidIndex() {
 DenseInstance instance = createInstance(new double[] {1.0, 2.0, 3.0}, null);
 instance.value(-1);
 }

 @Test
 public void testClearMethod() {
 DenseInstance instance = createInstance(new double[] {1.0, 2.0, 3.0}, null);
 instance.clear();
 assertEquals(0, instance.noAttributes());
 assertEmpty(instance.attributes);
 }

 @Test
 public void testContainsKeyMethodWithIntegerKey() {
 DenseInstance instance = createInstance(new double[] {1.0, 2.0, 3.0}, null);
 assertTrue(instance.containsKey(0));
 assertTrue(instance.containsKey(1));
 assertTrue(instance.containsKey(2));
 }

 @Test
 public void testContainsKeyMethodWithNonIntegerKey() {
 DenseInstance instance = createInstance(new double[] {1.0, 2.0, 3.0}, null);
 assertFalse(instance.containsKey("StringKey"));
 }

 @Test
 public void testContainsValueMethodWithDoubleValue() {
 DenseInstance instance = createInstance(new double[] {1.0, 2.0, 3.0}, null);
 assertTrue(instance.containsValue(1.0));
 assertTrue(instance.containsValue(2.0));
 assertTrue(instance.containsValue(3.0));
 }

 @Test
 public void testContainsValueMethodWithNonSupportedValueType() {
 DenseInstance instance = createInstance(new double[] {1.0, 2.0, 3.0}, null);
 assertFalse(instance.containsValue('A'));
 }

 @Test
 public void testEntrySetMethod() {
 DenseInstance instance = createInstance(new double[] {1.0, 2.0, 3.0}, null);
 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 assertEquals(3, entrySet.size());
 }

 @Test
 public void testGetMethodWithValidKey() {
 DenseInstance instance = createInstance(new double[] {1.0, 2.0, 3.0}, null);
 assertEquals(1.0, instance.get(0), 0.0);
 assertEquals(2.0, instance.get(1), 0.0);
 assertEquals(3.0, instance.get(2), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMethodWithInvalidKey() {
 DenseInstance instance = createInstance(new double[] {1.0, 2.0, 3.0}, null);
 instance.get(-1);
 }

 @Test
 public void testIsEmptyMethod() {
 DenseInstance instance = createInstance(new double[] {1.0, 2.0, 3.0}, null);
 assertFalse(instance.isEmpty());
 }

 @Test
 public void testKeySetMethod() {
 DenseInstance instance = createInstance(new double[] {1.0, 2.0, 3.0}, null);
 SortedSet<Integer> keySet = instance.keySet();
 assertEquals(3, keySet.size());
 }

 @Test
 public void testPutMethod() {
 DenseInstance instance = createInstance(new double[] {1.0, 2.0, 3.0}, null);
 instance.put(0, 4.0);
 assertEquals(4.0, instance.value(0), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutMethodWithInvalidKey() {
 DenseInstance instance = createInstance(new double[] {1.0, 2.0, 3.0}, null);
 instance.put(-1, 4.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutMethodWithInvalidValueType() {
 DenseInstance instance = createInstance(new double[] {1.0, 2.0, 3.0}, null);
 instance.put(0, 'A');
 }

 @Test
 public void testPutAllMethod() {
 DenseInstance instance1 = createInstance(new double[] {1.0, 2.0, 3.0}, null);
 DenseInstance instance2 = createInstance(new double[] {4.0, 5.0, 6.0}, null);
 Set<Map.Entry<Integer, Double>> entrySet = instance2.entrySet();
 instance1.putAll(entrySet);
 assertEquals(6, instance1.noAttributes());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveMethod() {
 DenseInstance instance = createInstance(new double[] {1.0, 2.0, 3.0}, null);
 instance.remove(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAttributeMethod() {
 DenseInstance instance = createInstance(new double[] {1.0, 2.0, 3.0}, null);
 instance.removeAttribute(0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeMethodWithInvalidIndex() {
 DenseInstance instance = createInstance(new double[] {1.0, 2.0, 3.0}, null);
 instance.removeAttribute(-1);
 }

}