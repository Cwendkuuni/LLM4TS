package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class DenseInstanceTest {

 private double[] attributeValues = new double[]{1.0, 2.0, 3.0, 4.0};
 private DenseInstance denseInstance;

 @Test
 public void testDenseInstanceConstructionWithAttributes() {
 denseInstance = new DenseInstance(attributeValues);
 assertArrayEquals(attributeValues, denseInstance.attributes, "Attributes should be copied correctly");
 }

 @Test
 public void testDenseInstanceConstructionWithClassValue() {
 denseInstance = new DenseInstance(attributeValues, "ClassValue");
 assertArrayEquals(attributeValues, denseInstance.attributes, "Attributes should be copied correctly");
 assertSame("ClassValue", denseInstance.getClassValue(), "Class value should be set correctly");
 }

 @Test
 public void testDenseInstanceEmptyConstruction() {
 denseInstance = new DenseInstance();
 assertEquals(0, denseInstance.noAttributes(), "Number of attributes should be zero for empty instance");
 }

 @Test
 public void testDenseInstanceSingleAttributeConstruction() {
 denseInstance = new DenseInstance(1);
 assertEquals(1, denseInstance.noAttributes(), "Number of attributes should be one for instance with single attribute");
 }

 @Test
 public void testDenseInstanceGetValue() {
 denseInstance = new DenseInstance(attributeValues);
 for (int i = 0; i < attributeValues.length; i++) {
 assertEquals(attributeValues[i], denseInstance.value(i), "Value should be retrieved correctly");
 }
 }

 @Test
 public void testDenseInstanceClear() {
 denseInstance = new DenseInstance(attributeValues);
 denseInstance.clear();
 assertEquals(0, denseInstance.noAttributes(), "Number of attributes should be zero after clearing");
 }

 @Test
 public void testDenseInstanceContainsKey() {
 denseInstance = new DenseInstance(attributeValues);
 for (int i = 0; i < attributeValues.length; i++) {
 assertTrue(denseInstance.containsKey(i), "Instance should contain keys from 0 to " + attributeValues.length);
 }
 }

 @Test
 public void testDenseInstanceContainsValue() {
 denseInstance = new DenseInstance(attributeValues);
 for (double value : attributeValues) {
 assertTrue(denseInstance.containsValue(value), "Instance should contain values from the attribute array");
 }
 }

 @Test
 public void testDenseInstanceEntrySet() {
 denseInstance = new DenseInstance(attributeValues);
 Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
 assertEquals(attributeValues.length, entrySet.size(), "Size of entry set should match number of attributes");
 }

 @Test
 public void testDenseInstanceGet() {
 denseInstance = new DenseInstance(attributeValues);
 for (int i = 0; i < attributeValues.length; i++) {
 assertEquals(attributeValues[i], denseInstance.get(i), "Value should be retrieved correctly using index as key");
 }
 }

 @Test
 public void testDenseInstanceIsEmpty() {
 denseInstance = new DenseInstance();
 assertFalse(denseInstance.isEmpty(), "Instance should not be empty");
 }

 @Test
 public void testDenseInstanceKeySet() {
 denseInstance = new DenseInstance(attributeValues);
 Set<Integer> keySet = denseInstance.keySet();
 assertEquals(attributeValues.length, keySet.size(), "Size of key set should match number of attributes");
 }

 @Test
 public void testDenseInstancePut() {
 denseInstance = new DenseInstance(new double[]{1.0, 2.0});
 double newValue = 3.0;
 denseInstance.put(1, newValue);
 assertEquals(newValue, denseInstance.value(1), "New value should be set correctly");
 }

 @Test
 public void testDenseInstancePutAll() {
 denseInstance = new DenseInstance(new double[]{1.0, 2.0});
 DenseInstance otherInstance = new DenseInstance(new double[]{3.0, 4.0});
 denseInstance.putAll(otherInstance.entrySet());
 assertEquals(4, denseInstance.noAttributes(), "Number of attributes should be four after putAll");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testDenseInstanceRemove() {
 denseInstance = new DenseInstance(new double[]{1.0, 2.0});
 denseInstance.remove(0);
 fail("remove method should throw UnsupportedOperationException");
 }

 @Test
 public void testDenseInstanceSize() {
 denseInstance = new DenseInstance(new double[]{1.0, 2.0});
 assertEquals(2, denseInstance.noAttributes(), "Number of attributes should be two");
 }

 @Test
 public void testDenseInstanceValues() {
 denseInstance = new DenseInstance(attributeValues);
 Set<Double> valuesSet = new HashSet<>(Arrays.asList(attributeValues));
 assertTrue(valuesSet.containsAll(denseInstance.values()), "Values should match attribute array");
 }

 @Test
 public void testDenseInstanceHashCode() {
 denseInstance = new DenseInstance(attributeValues);
 DenseInstance otherInstance = new DenseInstance(attributeValues.clone());
 assertEquals(denseInstance.hashCode(), otherInstance.hashCode(), "Hashcodes should be equal");
 }

 @Test
 public void testDenseInstanceEquals() {
 denseInstance = new DenseInstance(attributeValues);
 DenseInstance otherInstance = new DenseInstance(attributeValues.clone());
 assertTrue(denseInstance.equals(otherInstance), "Instances with same attributes should be equal");
 }

 @Test
 public void testDenseInstanceCopy() {
 denseInstance = new DenseInstance(attributeValues);
 DenseInstance copiedInstance = denseInstance.copy();
 assertArrayEquals(attributeValues, copiedInstance.attributes, "Attributes should be copied correctly when using copy method");
 }

 @Test
 public void testDenseInstanceRemoveAttributes() {
 denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0});
 Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(1, 3));
 denseInstance.removeAttributes(indicesToRemove);
 assertEquals(2, denseInstance.noAttributes(), "Number of attributes should be two after removing some");
 }
}