package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class SparseInstanceTest {

 private SparseInstance instance;

 @Before
 public void setUp() {
 double[] datavector = {1.0, 2.0, 3.0, 4.0};
 instance = new SparseInstance(datavector, 0.0, "ClassValue");
 }

 @Test
 public void testDefaultConstructor() {
 SparseInstance s = new SparseInstance();
 assertEquals(0, s.noAttributes());
 assertNull(s.getClassValue());
 }

 @Test
 public void testConstructorWithNoAttributes() {
 assertEquals(-1, instance.noAttributes());
 }

 @Test
 public void testConstructorWithNoAttributesAndDefaultValue() {
 SparseInstance s = new SparseInstance(5);
 assertEquals(5, s.noAttributes());
 }

 @Test
 public void testConstructorWithNoAttributesAndClassValue() {
 SparseInstance s = new SparseInstance(5, "ClassValue");
 assertEquals("ClassValue", s.getClassValue());
 }

 @Test
 public void testConstructorWithNoAttributesDefaultValueAndClassValue() {
 SparseInstance s = new SparseInstance(5, 0.0, "ClassValue");
 assertEquals(5, s.noAttributes());
 assertEquals(0.0, s.get(0), 0.0);
 assertEquals("ClassValue", s.getClassValue());
 }

 @Test
 public void testConstructorWithDatavector() {
 assertEquals(datavector.length, instance.noAttributes());
 for (int i = 0; i < datavector.length; i++) {
 assertEquals(datavector[i], instance.get(i), 0.0);
 }
 }

 @Test
 public void testConstructorWithDatavectorDefaultValueAndClassValue() {
 double[] datavector = {1.1, 2.1, 3.1, 4.1};
 SparseInstance s = new SparseInstance(datavector, 0.0, "ClassValue");
 assertEquals(datavector.length, s.noAttributes());
 for (int i = 0; i < datavector.length; i++) {
 if (datavector[i] != 0.0) {
 assertEquals(datavector[i], s.get(i), 0.0);
 } else {
 assertEquals(0.0, s.get(i), 0.0);
 }
 }
 assertEquals("ClassValue", s.getClassValue());
 }

 @Test
 public void testValue() {
 assertEquals(instance.value(0), instance.get(0), 0.0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testValueWithInvalidIndex() {
 instance.value(-1);
 }

 @Test
 public void testClear() {
 instance.clear();
 assertEquals(0, instance.noAttributes());
 }

 @Test
 public void testIsEmpty() {
 assertTrue(instance.isEmpty());
 instance.put(0, 1.0);
 assertFalse(instance.isEmpty());
 }

 @Test
 public void testContainsKey() {
 for (int i = 0; i < instance.noAttributes(); i++) {
 assertTrue(instance.containsKey(Integer.valueOf(i)));
 }
 assertTrue(instance.containsKey(Integer.valueOf(-1)));
 }

 @Test
 public void testContainsValue() {
 for (int i = 0; i < instance.noAttributes(); i++) {
 assertTrue(instance.containsValue(Double.valueOf(instance.get(i))));
 }
 }

 @Test
 public void testEntrySet() {
 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 int count = 0;
 for (Map.Entry<Integer, Double> entry : entrySet) {
 count++;
 assertEquals(instance.get(entry.getKey()), entry.getValue());
 }
 assertEquals(instance.noAttributes(), count);
 }

 @Test
 public void testGet() {
 for (int i = 0; i < instance.noAttributes(); i++) {
 assertEquals(instance.get(Integer.valueOf(i)), Double.valueOf(instance.get(i)), 0.0);
 }
 assertEquals(instance.defaultValue, instance.get(Integer.valueOf(-1)), 0.0);
 }

 @Test(expected = NullPointerException.class)
 public void testGetWithNullKey() {
 instance.get(null);
 }

 @Test
 public void testSize() {
 assertEquals(instance.noAttributes(), instance.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 @Deprecated
 public void testSizeDeprecated() {
 instance.size();
 }

 @Test
 public void testKeySet() {
 Set<Integer> keySet = instance.keySet();
 int count = 0;
 for (Integer key : keySet) {
 count++;
 assertEquals(instance.get(key), instance.get(Integer.valueOf(key)), 0.0);
 }
 assertEquals(instance.noAttributes(), count);
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
 instance.put(null, Double.valueOf(1.0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithInvalidKey() {
 instance.put(-1, Double.valueOf(1.0));
 }

 @Test
 public void testPut() {
 Map<Integer, Double> map = new LinkedHashMap<Integer, Double>();
 map.put(0, Double.valueOf(2.0));
 instance.putAll(map);
 for (int i = 0; i < instance.noAttributes(); i++) {
 if (i != 0) {
 assertEquals(instance.get(Integer.valueOf(i)), instance.get(Integer.valueOf(i - 1)), 0.0);
 }
 assertEquals(instance.get(Integer.valueOf(i)), Double.valueOf(i < 1 ? instance.defaultValue : 2.0), 0.0);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testPutAllWithNullMap() {
 instance.putAll(null);
 }

 @Test
 public void testRemove() {
 instance.removeAttribute(0);
 assertEquals(instance.noAttributes() - 1, instance.noAttributes());
 for (int i = 0; i < instance.noAttributes(); i++) {
 if (i != 0) {
 assertEquals(instance.get(Integer.valueOf(i)), instance.get(Integer.valueOf(i - 1)), 0.0);
 }
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveWithInvalidIndex() {
 instance.removeAttribute(-1);
 }

 @Test
 public void testToString() {
 String expected = "{" + Arrays.toString(instance.getValuesArray()) + ";ClassValue}";
 assertEquals(expected, instance.toString());
 }

 @Test
 public void testHashCode() {
 SparseInstance s1 = new SparseInstance(instance.getValuesArray(), instance.defaultValue, instance.getClassValue());
 SparseInstance s2 = new SparseInstance(instance.getValuesArray(), instance.defaultValue, instance.getClassValue());
 assertEquals(s1.hashCode(), s2.hashCode());
 }

 @Test
 public void testEquals() {
 SparseInstance s1 = new SparseInstance(instance.getValuesArray(), instance.defaultValue, instance.getClassValue());
 SparseInstance s2 = new SparseInstance(instance.getValuesArray(), instance.defaultValue, instance.getClassValue());
 assertTrue(s1.equals(s2));
 }

 @Test
 public void testEqualsFalse() {
 SparseInstance s1 = new SparseInstance(instance.getValuesArray(), instance.defaultValue, instance.getClassValue());
 SparseInstance s2 = new SparseInstance(instance.getValuesArray().clone(), instance.defaultValue, instance.getClassValue());
 assertFalse(s1.equals(s2));
 }

 @Test
 public void testCopy() {
 SparseInstance copiedInstance = instance.copy();
 assertEquals(instance, copiedInstance);
 }

 @Test
 public void testRemoveAttributes() {
 Set<Integer> indices = new HashSet<Integer>(Arrays.asList(1, 2));
 instance.removeAttributes(indices);
 assertEquals(instance.noAttributes() - indices.size(), instance.noAttributes());
 }
}