package net.sf.javaml.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

public class SparseInstanceTest {

 private SparseInstance sparseInstance;
 private SparseInstance sparseInstanceWithDefaultValue;
 private SparseInstance sparseInstanceWithClassValue;
 private SparseInstance sparseInstanceWithDatavector;

 @Test
 public void testConstructors() {
 // Test default constructor
 sparseInstance = new SparseInstance();
 Assert.assertEquals(0, sparseInstance.noAttributes());
 // Test constructor with number of attributes
 sparseInstance = new SparseInstance(5);
 Assert.assertEquals(5, sparseInstance.noAttributes());
 // Test constructor with default value
 sparseInstanceWithDefaultValue = new SparseInstance(5, 0.5);
 Assert.assertEquals(5, sparseInstanceWithDefaultValue.noAttributes());
 Assert.assertEquals(0.5, sparseInstanceWithDefaultValue.defaultValue, 0.001);
 // Test constructor with class value
 sparseInstanceWithClassValue = new SparseInstance(5, "ClassValue");
 Assert.assertEquals(5, sparseInstanceWithClassValue.noAttributes());
 Assert.assertEquals("ClassValue", sparseInstanceWithClassValue.getClassValue());
 }

 @Test
 public void testConstructorsWithDatavector() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
 // Test constructor with datavector and default value
 sparseInstanceWithDatavector = new SparseInstance(datavector, 0.5);
 Assert.assertEquals(5, sparseInstanceWithDatavector.noAttributes());
 for (int i = 0; i < datavector.length; i++) {
 if (datavector[i] != 0.5) {
 Assert.assertEquals(datavector[i], sparseInstanceWithDatavector.get(i), 0.001);
 }
 }
 // Test constructor with datavector and class value
 sparseInstanceWithDatavector = new SparseInstance(datavector, "ClassValue");
 Assert.assertEquals(5, sparseInstanceWithDatavector.noAttributes());
 Assert.assertEquals("ClassValue", sparseInstanceWithDatavector.getClassValue());
 for (int i = 0; i < datavector.length; i++) {
 if (datavector[i] != 0.5) {
 Assert.assertEquals(datavector[i], sparseInstanceWithDatavector.get(i), 0.001);
 }
 }
 }

 @Test
 public void testMethods() {
 // Test getter and setter for noAttributes
 sparseInstance.setNoAttributes(7);
 Assert.assertEquals(7, sparseInstance.noAttributes());
 // Test clear method
 sparseInstance.clear();
 Assert.assertEquals(0, sparseInstance.noAttributes());
 // Test containsKey method
 sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
 Assert.assertTrue(sparseInstance.containsKey(Integer.valueOf(0)));
 Assert.assertFalse(sparseInstance.containsKey(Integer.valueOf(1)));
 // Test containsValue method
 Assert.assertTrue(sparseInstance.containsValue(Double.valueOf(1.0)));
 Assert.assertFalse(sparseInstance.containsValue(Double.valueOf(2.0)));
 // Test entrySet method
 Set<Map.Entry<Integer, Double>> entrySet = sparseInstance.entrySet();
 Assert.assertEquals(1, entrySet.size());
 // Test get method
 Assert.assertEquals(1.0, sparseInstance.get(Integer.valueOf(0)), 0.001);
 Assert.assertEquals(0.5, sparseInstance.get(Integer.valueOf(1)), 0.001);
 // Test isEmpty method
 sparseInstance.clear();
 Assert.assertTrue(sparseInstance.isEmpty());
 // Test keySet method
 Set<Integer> keySet = sparseInstance.keySet();
 Assert.assertEquals(0, keySet.size());
 // Test put method
 sparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
 Assert.assertEquals(2.0, sparseInstance.get(Integer.valueOf(1)), 0.001);
 Assert.assertEquals(1, sparseInstance.noAttributes());
 // Test putAll method
 HashMap<Integer, Double> map = new HashMap<Integer, Double>();
 map.put(Integer.valueOf(2), Double.valueOf(3.0));
 map.put(Integer.valueOf(3), Double.valueOf(4.0));
 sparseInstance.putAll(map);
 Assert.assertEquals(3, sparseInstance.noAttributes());
 Assert.assertEquals(3.0, sparseInstance.get(Integer.valueOf(2)), 0.001);
 Assert.assertEquals(4.0, sparseInstance.get(Integer.valueOf(3)), 0.001);
 // Test remove method
 sparseInstance.remove(Integer.valueOf(2));
 Assert.assertEquals(2, sparseInstance.noAttributes());
 // Test size method (deprecated)
 Assert.assertEquals(3, sparseInstance.size());
 // Test values method
 List<Double> values = new ArrayList<Double>(sparseInstance.values());
 Assert.assertEquals(2, values.size());
 Assert.assertEquals(4.0, values.get(0), 0.001);
 Assert.assertEquals(1.0, values.get(1), 0.001);
 }

 @Test
 public void testMethodsWithDefaultValue() {
 // Test value method
 sparseInstanceWithDefaultValue.put(Integer.valueOf(0), Double.valueOf(1.0));
 Assert.assertEquals(1.0, sparseInstanceWithDefaultValue.value(0), 0.001);
 Assert.assertEquals(0.5, sparseInstanceWithDefaultValue.value(1), 0.001);
 // Test removeAttribute method
 sparseInstanceWithDefaultValue.removeAttribute(0);
 Assert.assertEquals(4, sparseInstanceWithDefaultValue.noAttributes());
 // Test toString method
 Assert.assertEquals("{};ClassValue", sparseInstanceWithDefaultValue.toString());
 }

 @Test
 public void testMethodsWithClassValue() {
 // Test removeAttributes method
 List<Integer> indices = new ArrayList<Integer>();
 indices.add(1);
 indices.add(3);
 sparseInstanceWithClassValue.removeAttributes(new TreeSet<Integer>(indices));
 Assert.assertEquals(3, sparseInstanceWithClassValue.noAttributes());
 // Test toString method
 Assert.assertEquals("{1.0=1.0, 2.0=?, 4.0=?};ClassValue", sparseInstanceWithClassValue.toString());
 }

}