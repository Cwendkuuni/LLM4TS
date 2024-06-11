package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.*;

import net.sf.javaml.core.*;

import org.junit.Test;

public class DenseInstanceTest {

 private DenseInstance createInstance(double[] attributes, Object classValue) {
 return new DenseInstance(attributes, classValue);
 }

 @Test
 public void testConstructorWithDoubleArray() {
 DenseInstance denseInstance = createInstance(new double[] { 1.0, 2.0, 3.0 }, null);
 assertEquals(3, denseInstance.noAttributes());
 assertEquals(1.0, denseInstance.value(0), 0.0);
 assertEquals(2.0, denseInstance.value(1), 0.0);
 assertEquals(3.0, denseInstance.value(2), 0.0);
 }

 @Test
 public void testConstructorWithDoubleArrayAndClassValue() {
 DenseInstance denseInstance = createInstance(new double[] { 1.0, 2.0, 3.0 }, "ClassValue");
 assertEquals("ClassValue", denseInstance.classValue());
 }

 @Test
 public void testConstructorWithSize() {
 DenseInstance denseInstance = createInstance(new double[5], null);
 assertEquals(5, denseInstance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValueWithInvalidIndex() {
 DenseInstance denseInstance = createInstance(new double[] { 1.0, 2.0, 3.0 }, null);
 denseInstance.value(-1);
 }

 @Test
 public void testClear() {
 DenseInstance denseInstance = createInstance(new double[] { 1.0, 2.0, 3.0 }, null);
 denseInstance.clear();
 assertEquals(0, denseInstance.noAttributes());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testContainsKeyWithInvalidType() {
 DenseInstance denseInstance = createInstance(new double[] { 1.0, 2.0, 3.0 }, null);
 denseInstance.containsKey('a');
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testContainsValueWithInvalidIndex() {
 DenseInstance denseInstance = createInstance(new double[] { 1.0, 2.0, 3.0 }, null);
 denseInstance.containsValue(4.0);
 }

 @Test
 public void testEntrySet() {
 DenseInstance denseInstance = createInstance(new double[] { 1.0, 2.0, 3.0 }, null);
 Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
 assertEquals(3, entrySet.size());
 }

 @Test
 public void testGet() {
 DenseInstance denseInstance = createInstance(new double[] { 1.0, 2.0, 3.0 }, null);
 assertEquals(1.0, denseInstance.get(0), 0.0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetWithInvalidIndex() {
 DenseInstance denseInstance = createInstance(new double[] { 1.0, 2.0, 3.0 }, null);
 denseInstance.get(3);
 }

 @Test
 public void testIsEmpty() {
 DenseInstance denseInstance = createInstance(new double[] { 1.0, 2.0, 3.0 }, null);
 assertFalse(denseInstance.isEmpty());
 }

 @Test
 public void testKeySet() {
 DenseInstance denseInstance = createInstance(new double[] { 1.0, 2.0, 3.0 }, null);
 SortedSet<Integer> keySet = denseInstance.keySet();
 assertEquals(3, keySet.size());
 }

 @Test
 public void testPut() {
 DenseInstance denseInstance = createInstance(new double[] { 1.0, 2.0, 3.0 }, null);
 denseInstance.put(1, 4.0);
 assertEquals(4.0, denseInstance.value(1), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithInvalidKeyType() {
 DenseInstance denseInstance = createInstance(new double[] { 1.0, 2.0, 3.0 }, null);
 denseInstance.put('a', 4.0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testPutWithInvalidIndex() {
 DenseInstance denseInstance = createInstance(new double[] { 1.0, 2.0, 3.0 }, null);
 denseInstance.put(4, 4.0);
 }

 @Test
 public void testPutAll() {
 DenseInstance denseInstance = createInstance(new double[] { 1.0, 2.0, 3.0 }, null);
 Map<Integer, Double> map = new HashMap<Integer, Double>();
 map.put(0, 4.0);
 map.put(1, 5.0);
 denseInstance.putAll(map);
 assertEquals(5.0, denseInstance.value(1), 0.0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 DenseInstance denseInstance = createInstance(new double[] { 1.0, 2.0, 3.0 }, null);
 denseInstance.remove(1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSize() {
 DenseInstance denseInstance = createInstance(new double[] { 1.0, 2.0, 3.0 }, null);
 assertEquals(3, denseInstance.size());
 }

 @Test
 public void testValues() {
 DenseInstance denseInstance = createInstance(new double[] { 1.0, 2.0, 3.0 }, null);
 Collection<Double> values = denseInstance.values();
 assertEquals(3, values.size());
 }

 @Test
 public void testNoAttributes() {
 DenseInstance denseInstance = createInstance(new double[] { 1.0, 2.0, 3.0 }, null);
 assertEquals(3, denseInstance.noAttributes());
 }

 @Test
 public void testToString() {
 DenseInstance denseInstance = createInstance(new double[] { 1.0, 2.0, 3.0 }, null);
 assertEquals("{1.0, 2.0, 3.0;null}", denseInstance.toString());
 }

 @Test
 public void testRemoveAttribute() {
 DenseInstance denseInstance = createInstance(new double[] { 1.0, 2.0, 3.0, 4.0 }, null);
 denseInstance.removeAttribute(2);
 assertEquals(3, denseInstance.noAttributes());
 }
}