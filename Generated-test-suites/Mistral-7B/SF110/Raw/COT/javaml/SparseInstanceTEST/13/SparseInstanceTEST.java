package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;

public class SparseInstanceTest {

 private SparseInstance createInstance(int noAttributes) {
 return new SparseInstance(noAttributes);
 }

 private SparseInstance createInstance(int noAttributes, double defaultValue) {
 return new SparseInstance(noAttributes, defaultValue);
 }

 private SparseInstance createInstance(int noAttributes, double[] datavector) {
 return new SparseInstance(datavector);
 }

 private void testSizeAndNoAttributes(SparseInstance instance) {
 assertEquals("Size and noAttributes should be equal", instance.size(), instance.noAttributes());
 }

 @Test
 public void testDefaultConstructor() {
 SparseInstance instance = new SparseInstance();
 assertEquals("Default constructor should initialize noAttributes with -1", -1, instance.noAttributes());
 }

 @Test
 public void testConstructorWithNoAttributes() {
 SparseInstance instance = new SparseInstance(0);
 assertEquals("Constructor with noAttributes should initialize noAttributes to 0", 0, instance.noAttributes());
 }

 @Test
 public void testConstructorWithNoAttributesAndDefaultValue() {
 SparseInstance instance = new SparseInstance(0, 0.0);
 assertEquals("Constructor with noAttributes and defaultValue should initialize noAttributes to 0", 0, instance.noAttributes());
 }

 @Test
 public void testConstructorWithNoAttributesAndClassValue() {
 SparseInstance instance = new SparseInstance(0, "ClassValue");
 assertEquals("Constructor with noAttributes and classValue should set classValue correctly", "ClassValue", instance.getClassValue());
 }

 @Test
 public void testConstructorWithNoAttributesDefaultValueAndClassValue() {
 SparseInstance instance = new SparseInstance(0, 0.0, "ClassValue");
 assertEquals("Constructor with noAttributes, defaultValue and classValue should initialize noAttributes to 0 and set classValue correctly", 0, instance.noAttributes());
 assertEquals("Constructor with noAttributes, defaultValue and classValue should set classValue correctly", "ClassValue", instance.getClassValue());
 }

 @Test
 public void testConstructorWithDatavector() {
 int noAttributes = 5;
 double[] datavector = new double[noAttributes];
 SparseInstance instance = new SparseInstance(datavector);
 assertEquals("Constructor with datavector should initialize noAttributes correctly", noAttributes, instance.noAttributes());
 }

 @Test
 public void testConstructorWithDatavectorDefaultValue() {
 int noAttributes = 5;
 double defaultValue = 0.0;
 double[] datavector = new double[noAttributes];
 SparseInstance instance = new SparseInstance(datavector, defaultValue);
 assertEquals("Constructor with datavector and defaultValue should initialize noAttributes correctly", noAttributes, instance.noAttributes());
 }

 @Test
 public void testConstructorWithDatavectorAndClassValue() {
 int noAttributes = 5;
 double[] datavector = new double[noAttributes];
 String classValue = "ClassValue";
 SparseInstance instance = new SparseInstance(datavector, classValue);
 assertEquals("Constructor with datavector and classValue should set classValue correctly", classValue, instance.getClassValue());
 }

 @Test
 public void testConstructorWithDatavectorDefaultValueAndClassValue() {
 int noAttributes = 5;
 double defaultValue = 0.0;
 double[] datavector = new double[noAttributes];
 String classValue = "ClassValue";
 SparseInstance instance = new SparseInstance(datavector, defaultValue, classValue);
 assertEquals("Constructor with datavector, defaultValue and classValue should initialize noAttributes, set defaultValue and classValue correctly", noAttributes, instance.noAttributes());
 assertEquals("Constructor with datavector, defaultValue and classValue should set classValue correctly", classValue, instance.getClassValue());
 assertEquals("Constructor with datavector, defaultValue and classValue should set defaultValue correctly", defaultValue, instance.getDefaultValue());
 }

 @Test
 public void testSetNoAttributes() {
 SparseInstance instance = new SparseInstance();
 instance.setNoAttributes(10);
 assertEquals("SetNoAttributes method should set noAttributes correctly", 10, instance.noAttributes());
 }

 @Test
 public void testGet() {
 SparseInstance instance = createInstance(3, new double[] { 1.0, 2.0, 3.0 });
 assertEquals("Get method should return the correct value for existing key", 1.0, instance.get(0));
 assertEquals("Get method should return the defaultValue for non-existing key", 0.0, instance.get(4));
 }

 @Test
 public void testPut() {
 SparseInstance instance = createInstance(3);
 Double oldValue = 0.0;
 Double newValue = 2.0;
 instance.put(Integer.valueOf(2), newValue);
 assertEquals("Put method should set the new value for the given key", newValue, instance.get(2));
 assertEquals("Put method should not change the value for another key", oldValue, instance.get(1));
 }

 @Test
 public void testPutAll() {
 SparseInstance instance = createInstance(5);
 Map<Integer, Double> map = new HashMap<>();
 map.put(Integer.valueOf(0), Double.valueOf(0.1));
 map.put(Integer.valueOf(3), Double.valueOf(3.1));
 instance.putAll(map);
 assertEquals("PutAll method should add all entries from the given map to the instance", 2, instance.size());
 assertEquals("PutAll method should set the value for the key 0", 0.1, instance.get(0));
 assertEquals("PutAll method should set the value for the key 3", 3.1, instance.get(3));
 }

 @Test
 public void testClear() {
 SparseInstance instance = createInstance(3, new double[] { 1.0, 2.0, 3.0 });
 instance.clear();
 assertEquals("Clear method should remove all entries from the instance", 0, instance.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAttributeWhenEmpty() {
 SparseInstance instance = new SparseInstance();
 instance.removeAttribute(0);
 }

 @Test
 public void testRemoveAttribute() {
 SparseInstance instance = createInstance(5, new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 });
 instance.removeAttribute(2);
 assertEquals("RemoveAttribute method should remove the entry for the given index", 3, instance.size());
 assertEquals("RemoveAttribute method should set the new index for the remaining entries", 2, instance.noAttributes());
 }

 @Test
 public void testRemoveAttributes() {
 SparseInstance instance = createInstance(5, new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 });
 Set<Integer> indices = new HashSet<>();
 indices.add(2);
 indices.add(4);
 instance.removeAttributes(indices);
 assertEquals("RemoveAttributes method should remove the entries for the given indices", 2, instance.size());
 assertEquals("RemoveAttributes method should set the new noAttributes for the remaining entries", 3, instance.noAttributes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithNullKey() {
 SparseInstance instance = createInstance(3);
 instance.put(null, Double.valueOf(2.0));
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
 SparseInstance instance = createInstance(3);
 instance.put(Integer.valueOf(2), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithDuplicateKey() {
 SparseInstance instance = createInstance(3);
 instance.put(Integer.valueOf(2), Double.valueOf(2.0));
 instance.put(Integer.valueOf(2), Double.valueOf(3.0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeWithInvalidIndex() {
 SparseInstance instance = createInstance(3, new double[] { 1.0, 2.0, 3.0 });
 instance.removeAttribute(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributeWithLastIndex() {
 SparseInstance instance = createInstance(3, new double[] { 1.0, 2.0, 3.0 });
 instance.removeAttribute(2);
 instance.removeAttribute(2);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributesWithInvalidIndex() {
 SparseInstance instance = createInstance(3, new double[] { 1.0, 2.0, 3.0 });
 Set<Integer> indices = new HashSet<>();
 indices.add(-1);
 instance.removeAttributes(indices);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAttributesWithLastIndex() {
 SparseInstance instance = createInstance(3, new double[] { 1.0, 2.0, 3.0 });
 Set<Integer> indices = new HashSet<>();
 indices.add(2);
 instance.removeAttributes(indices);
 instance.removeAttributes(indices);
 }

 @Test
 public void testToString() {
 SparseInstance instance = createInstance(3, new double[] { 1.0, 2.0, 3.0 });
 assertEquals("ToString method should return the correct string representation of the instance", "[1.0=1.0, 2.0=2.0, 3.0=3.0]", instance.toString());
 }

 @Test
 public void testHashCode() {
 SparseInstance instance1 = createInstance(3, new double[] { 1.0, 2.0, 3.0 });
 SparseInstance instance2 = createInstance(3, new double[] { 1.0, 2.0, 3.0 });
 SparseInstance instance3 = createInstance(3, new double[] { 1.1, 2.1, 3.1 });
 assertEquals("Hashcode method should be equal for equal instances", instance1.hashCode(), instance2.hashCode());
 assertNotEquals("Hashcode method should be different for different instances", instance1.hashCode(), instance3.hashCode());
 }

 @Test
 public void testEquals() {
 SparseInstance instance1 = createInstance(3, new double[] { 1.0, 2.0, 3.0 });
 SparseInstance instance2 = createInstance(3, new double[] { 1.0, 2.0, 3.0 });
 SparseInstance instance3 = createInstance(3, new double[] { 1.1, 2.1, 3.1 });
 assertTrue("Equals method should return true for equal instances", instance1.equals(instance2));
 assertFalse("Equals method should return false for different instances", instance1.equals(instance3));
 }

 @Test
 public void testCopy() {
 SparseInstance instance = createInstance(3, new double[] { 1.0, 2.0, 3.0 });
 SparseInstance copy = instance.copy();
 assertEquals("Copy method should create a deep copy of the instance", instance, copy);
 assertNotEquals("Copy method should create a different instance", instance, copy);
 testSizeAndNoAttributes(copy);
 }

}