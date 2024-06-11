package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class DenseInstanceTest {

 // Test constructor with double array
 @Test
 public void testDenseInstanceWithDoubleArray() {
     double[] attributes = new double[] { 1.0, 2.0, 3.0 };
     DenseInstance denseInstance = new DenseInstance(attributes);
     assertEquals(3, denseInstance.noAttributes());
     assertArrayEquals(attributes, denseInstance.attributes, 0.0);
 }

 // Test constructor with double array and class value
 @Test
 public void testDenseInstanceWithDoubleArrayAndClassValue() {
     double[] attributes = new double[] { 1.0, 2.0, 3.0 };
     Object classValue = "label1";
     DenseInstance denseInstance = new DenseInstance(attributes, classValue);
     assertEquals(classValue, denseInstance.classValue());
     assertEquals(3, denseInstance.noAttributes());
     assertArrayEquals(attributes, denseInstance.attributes, 0.0);
 }

 // Test constructor with no arguments
 @Test(expected = IllegalArgumentException.class)
 public void testDenseInstanceWithNoArguments() {
     new DenseInstance();
 }

 // Test constructor with int argument
 @Test
 public void testDenseInstanceWithIntArgument() {
     DenseInstance denseInstance = new DenseInstance(5);
     assertEquals(5, denseInstance.noAttributes());
     assertNotNull(denseInstance.attributes);
     assertTrue(denseInstance.attributes.length == 5);
 }

 // Test getValue method
 @Test
 public void testGetValue() {
     double[] attributes = new double[] { 1.0, 2.0, 3.0 };
     DenseInstance denseInstance = new DenseInstance(attributes);
     for (int i = 0; i < attributes.length; i++) {
         assertEquals(attributes[i], denseInstance.value(i), 0.0);
     }
 }

 // Test clear method
 @Test
 public void testClear() {
     double[] attributes = new double[] { 1.0, 2.0, 3.0 };
     DenseInstance denseInstance = new DenseInstance(attributes);
     denseInstance.clear();
     assertArrayEquals(new double[0], denseInstance.attributes, 0.0);
 }

 // Test containsKey method
 @Test
 public void testContainsKey() {
     double[] attributes = new double[] { 1.0, 2.0, 3.0 };
     DenseInstance denseInstance = new DenseInstance(attributes);
     for (int i = 0; i < attributes.length; i++) {
         assertTrue(denseInstance.containsKey(i));
         assertFalse(denseInstance.containsKey("non_integer_key"));
     }
 }

 // Test containsValue method
 @Test
 public void testContainsValue() {
     double[] attributes = new double[] { 1.0, 2.0, 3.0 };
     DenseInstance denseInstance = new DenseInstance(attributes);
     double value = 2.0;
     for (int i = 0; i < attributes.length; i++) {
         if (Math.abs(attributes[i] - value) < 1.0E-8) {
             assertTrue(denseInstance.containsValue(value));
             break;
         }
     }
     assertFalse(denseInstance.containsValue(4.0));
 }

 // Test entrySet method
 @Test
 public void testEntrySet() {
     double[] attributes = new double[] { 1.0, 2.0, 3.0 };
     DenseInstance denseInstance = new DenseInstance(attributes);
     Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
     assertEquals(attributes.length, entrySet.size());
     for (Map.Entry<Integer, Double> entry : entrySet) {
         assertEquals(entry.getKey(), entry.getValue().intValue());
     }
 }

 // Test get method
 @Test
 public void testGet() {
     double[] attributes = new double[] { 1.0, 2.0, 3.0 };
     DenseInstance denseInstance = new DenseInstance(attributes);
     for (int i = 0; i < attributes.length; i++) {
         assertEquals(attributes[i], denseInstance.get(i), 0.0);
     }
 }

 // Test isEmpty method
 @Test
 public void testIsEmpty() {
     double[] attributes = new double[0];
     DenseInstance denseInstance = new DenseInstance(attributes);
     assertTrue(denseInstance.isEmpty());

     attributes = new double[] { 1.0 };
     denseInstance = new DenseInstance(attributes);
     assertFalse(denseInstance.isEmpty());
 }

 // Test keySet method
 @Test
 public void testKeySet() {
     double[] attributes = new double[] { 1.0, 2.0, 3.0 };
     DenseInstance denseInstance = new DenseInstance(attributes);
     Set<Integer> keySet = denseInstance.keySet();
     assertEquals(attributes.length, keySet.size());
     for (Integer key : keySet) {
         assertTrue(denseInstance.containsKey(key));
     }
 }

 // Test put method
 @Test
 public void testPut() {
     double[] attributes = new double[] { 1.0, 2.0 };
     DenseInstance denseInstance = new DenseInstance(attributes);
     double newValue = 3.0;
     int index = 2;
     denseInstance.put(index, newValue);
     assertEquals(newValue, denseInstance.attributes[index], 0.0);
 }

 // Test putAll method
 @Test
 public void testPutAll() {
     double[] attributes1 = new double[] { 1.0, 2.0 };
     double[] attributes2 = new double[] { 3.0, 4.0 };
     DenseInstance denseInstance = new DenseInstance(attributes1);
     DenseInstance denseInstance2 = new DenseInstance(attributes2);
     denseInstance.putAll(denseInstance2.entrySet());
     assertEquals(attributes1.length + attributes2.length, denseInstance.noAttributes());
     for (int i = 0; i < attributes1.length; i++) {
         assertEquals(attributes1[i], denseInstance.attributes[i], 0.0);
     }
     for (int i = 0; i < attributes2.length; i++) {
         assertEquals(attributes2[i], denseInstance.attributes[attributes1.length + i], 0.0);
     }
 }

 // Test remove method (Deprecated)
 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
     double[] attributes = new double[] { 1.0, 2.0, 3.0 };
     DenseInstance denseInstance = new DenseInstance(attributes);
     denseInstance.remove(1);
 }

 // Test size method (Deprecated)
 @Test
 public void testSize() {
     double[] attributes = new double[] { 1.0, 2.0, 3.0 };
     DenseInstance denseInstance = new DenseInstance(attributes);
     assertEquals(attributes.length, denseInstance.size());
 }

 // Test values method
 @Test
 public void testValues() {
     double[] attributes = new double[] { 1.0, 2.0, 3.0 };
     DenseInstance denseInstance = new DenseInstance(attributes);
     List<Double> values = new ArrayList<>(denseInstance.values());
     assertEquals(attributes.length, values.size());
     for (int i = 0; i < attributes.length; i++) {
         assertEquals(attributes[i], values.get(i), 0.0);
     }
 }

 // Test noAttributes method
 @Test
 public void testNoAttributes() {
     double[] attributes = new double[] { 1.0, 2.0, 3.0 };
     DenseInstance denseInstance = new DenseInstance(attributes);
     assertEquals(attributes.length, denseInstance.noAttributes());
 }

 // Test toString method
 @Test
 public void testToString() {
     double[] attributes = new double[] { 1.0, 2.0, 3.0 };
     DenseInstance denseInstance = new DenseInstance(attributes);
     String expected = "{" + Arrays.toString(attributes) + ";" + denseInstance.classValue() + "}";
     assertEquals(expected, denseInstance.toString());
 }

 // Test removeAttribute method
 @Test
 public void testRemoveAttribute() {
     double[] attributes = new double[] { 1.0, 2.0, 3.0 };
     DenseInstance denseInstance = new DenseInstance(attributes);
     denseInstance.removeAttribute(1);
     assertEquals(attributes.length - 1, denseInstance.noAttributes());
     assertNotSame(attributes, denseInstance.attributes);
     for (int i = 0; i < denseInstance.noAttributes(); i++) {
         assertEquals(attributes[i], denseInstance.attributes[i], 0.0);
     }
 }

 // Test hashCode method
 @Test
 public void testHashCode() {
     double[] attributes1 = new double[] { 1.0, 2.0, 3.0 };
     double[] attributes2 = new double[] { 4.0, 5.0, 6.0 };
     DenseInstance denseInstance1 = new DenseInstance(attributes1);
     DenseInstance denseInstance2 = new DenseInstance(attributes2);
     int hashCode1 = denseInstance1.hashCode();
     int hashCode2 = denseInstance2.hashCode();
     assertNotSame(hashCode1, hashCode2);
 }

 // Test equals method
 @Test
 public void testEquals() {
     double[] attributes1 = new double[] { 1.0, 2.0, 3.0 };
     double[] attributes2 = new double[] { 1.0, 2.0, 3.0 };
     DenseInstance denseInstance1 = new DenseInstance(attributes1);
     DenseInstance denseInstance2 = new DenseInstance(attributes2);
     DenseInstance denseInstance3 = new DenseInstance(attributes1);
     DenseInstance denseInstance4 = new DenseInstance(attributes2);
     DenseInstance denseInstance5 = new DenseInstance(new double[] { 1.0, 2.1, 3.0 });
     assertTrue(denseInstance1.equals(denseInstance1));
     assertTrue(denseInstance1.equals(denseInstance3));
     assertTrue(denseInstance1.equals(denseInstance5.copy()));
     assertFalse(denseInstance1.equals(denseInstance2));
     assertFalse(denseInstance1.equals(new Object()));
     assertFalse(denseInstance1.equals(null));
 }

 // Test copy method
 @Test
 public void testCopy() {
     double[] attributes = new double[] { 1.0, 2.0, 3.0 };
     DenseInstance denseInstance = new DenseInstance(attributes);
     DenseInstance copiedInstance = denseInstance.copy();
     assertEquals(attributes, denseInstance.attributes);
     assertNotSame(attributes, copiedInstance.attributes);
     assertArrayEquals(denseInstance.attributes, copiedInstance.attributes);
 }

 // Test removeAttributes method
 @Test
 public void testRemoveAttributes() {
     double[] attributes = new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 };
     DenseInstance denseInstance = new DenseInstance(attributes);
     Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(1, 3));
     denseInstance.removeAttributes(indicesToRemove);
     assertEquals(3, denseInstance.noAttributes());
     for (int i = 0; i < denseInstance.noAttributes(); i++) {
         assertEquals(attributes[i], denseInstance.attributes[i], 0.0);
     }
 }
}